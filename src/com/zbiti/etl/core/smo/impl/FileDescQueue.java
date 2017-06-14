package com.zbiti.etl.core.smo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;

import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;


/**
 * 
 * 文件描述推送队列
 * @author 吴昌政
 *
 */
public class FileDescQueue implements IFileDescQueue{
	
	protected static final Log logger = LogFactory.getLog(FileDescQueue.class);
	
	
	final int SIZE_EVERY_COMMAND =20;//每指令文件数，默认文件数量上50
	
	Step step;//本步骤
	
	List<Queue<FileDesc>> queues=new ArrayList<Queue<FileDesc>>();//每个下一步骤对应一个队列,使用Hash算法分区
	
	List<Step> nextSteps;//下一步骤列表
	
	int nextStepNum;//下一步骤的数量
	
	ICommandService commandService;//指令管理服务
	
	Command cmd;
	
	public FileDescQueue(){}
	/**
	 * 文件描述队列构造器
	 * 根据步骤的下一步骤构造
	 * @param step
	 */
	public FileDescQueue(Step step,ICommandService commandService,Command cmd){
		this.step =step;
		this.nextSteps = step.getNextStep();
		this.nextStepNum = nextSteps.size();
		this.commandService = commandService;
		this.cmd=cmd;
		for (int i=0;i<nextStepNum;i++){
			Queue<FileDesc> queue = new LinkedBlockingQueue<FileDesc>();
			queues.add(queue);
		}
	}
	/**
	 * 推送文件描述,根据hashcode决定将文件放到哪一个队列中
	 */
	@Override
	public void push(FileDesc fileDesc) {
		int hashCode=fileDesc.getFileName().hashCode();
		if(nextStepNum==0)return;
		int mod =Math.abs(hashCode)%nextStepNum;
		Queue<FileDesc> queue = queues.get(mod);
		queue.add(fileDesc);
	}
	
	/**
	 * 判断队列是否为空,所有队列都为空,才认为队列为空
	 * 
	 */
	private boolean isEmpty(){
		try{
			if(queues==null || queues.size()==0){
				return true;
			}
			for (Queue<FileDesc> queue:queues){
				if(queue==null){
					continue; 
				}
				if (queue.size()>0){
					return false;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return true;
		}
		return true;
	}
	
	void scheduleCommands(Step nextStep,Queue<FileDesc> queue) {
		int size = this.getFileSize(queue.size());
		logger.info("队列大小:"+queue.size()+",每条指令将最多包含"+size);
		List<FileDesc> fileDescs = new ArrayList<FileDesc>();
		for (int i=0;i<size;i++){
			FileDesc fileDesc = queue.poll();
			if (fileDesc==null) break;
			fileDescs.add(fileDesc);
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("FILE_QUEUE", fileDescs);
		//System.out.println("begin create command");
		
		try {
			commandService.createNodeForWait(cmd.getTaskSeries(), cmd.getCmdId(), nextStep, params);
		} catch (KeeperException e) {
			logger.error(e.getMessage(),e);
			queue.addAll(fileDescs);//如果指令调度失败,还将文件名放回队列
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
			queue.addAll(fileDescs);//如果指令调度失败,还将文件名放回队列
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			queue.addAll(fileDescs);//如果指令调度失败,还将文件名放回队列
		}
	}
	/**
	 * 根据一定的规则决定每次包含的文件的数量
	 * @param size
	 * @return
	 */
	private int getFileSize(int size) {
		return SIZE_EVERY_COMMAND;
	}
	public static void main(String[] args) {
		List<FileDesc> fileDescs=new ArrayList<FileDesc>();
		FileDesc fd=new FileDesc();
		fileDescs.add(fd);
		System.out.println(JSONArray.fromObject(fileDescs).toString());
	}
	
	@Override
	public boolean clear()  {
		boolean isNext=false;
		while (!isEmpty()){
			isNext=true;
			for (int i=0;i<nextStepNum;i++){
				Step nextStep = nextSteps.get(i);
//				System.out.println("nextStep-code-is:"+nextStep.getStepCode());
				Queue<FileDesc> queue= queues.get(i);
				scheduleCommands(nextStep,queue);
			}			
		}
		return isNext;
	}
	
	
	
}
