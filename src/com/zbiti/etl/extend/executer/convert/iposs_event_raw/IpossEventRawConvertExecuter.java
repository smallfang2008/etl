package com.zbiti.etl.extend.executer.convert.iposs_event_raw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.TimeUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.common.stream.BufferedRandomAccessFile;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.ConvertRecord;
import com.zbiti.etl.extend.vo.ConvertStep;

public class IpossEventRawConvertExecuter implements ICommandExecuter<Boolean>{
	private static final Log logger = LogFactory.getLog(IpossEventRawConvertExecuter.class);
	IStepService stepService;
	
	@Override
	public Boolean execute(ApplicationContext ctx, Node node, Step step,
			Command command, IFileDescQueue fileDescQueue) throws Exception {
		logger.info("转换步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		logger.info("获取队列");
		@SuppressWarnings("unchecked")
		List<MorphDynaBean> fileQueue=(List<MorphDynaBean>) command.getParam().get("FILE_QUEUE");
		if(fileQueue==null||fileQueue.isEmpty()){
			command.setExecInfo("队列为空");
			logger.info("队列为空，退出");
			return false;
		}
		ConvertStep convertStep=stepService.getConvertStepCache(step.getStepId());
		if(convertStep==null){
			logger.info("找不到转换配置，退出");
			command.setExecInfo("找不到转换配置");
			return false;
		}
//		IConvertExecuter convertExecuter = (IConvertExecuter) ctx.getBean(convertStep.getSuperClass());
		for(MorphDynaBean bean:fileQueue){
			FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
			// 根目录/场景编码/convert/文件名
			String toPathFileName = this.getToPathFileName(node, step, fileDesc);
			logger.info("定义目标文件名："+toPathFileName);
			if(toPathFileName==null)
				continue;

			String charset = convertStep.getCharset() != null&& !"".equals(convertStep.getCharset()) ? convertStep.getCharset() : "gbk";
			logger.info("转换编码："+charset);
			
			logger.info("开始转换["+fileDesc.getFileName()+"]");
			convert(fileDesc.getFileName(), toPathFileName, charset);
			logger.info("结束转换，目标文件为"+toPathFileName);
			
			logger.info("将处理完成的文件放入队列");
			fileDesc.setFileName(toPathFileName);
			fileDesc.setServerName(node.getServerName());
			fileDescQueue.push(fileDesc);
		}
		return true;
	}
	
	private void convert(String fromPath,String toPath,String charset) throws Exception{
		BufferedReader fileRead=null;
		PrintWriter pw = null;
		try{
			File fromFile=new File(fromPath);
			if(!fromFile.exists()){
				return ;
			}
			pw=new PrintWriter(new File(toPath));
			fileRead=new BufferedReader(new InputStreamReader(new FileInputStream(fromFile),charset));
			char[] buffer=new char[1024];
			
			int count=0;
			StringBuilder sbLast=new StringBuilder();
            while(-1!=(count=fileRead.read(buffer))){
            	sbLast.append(new String(buffer,0,count));
            	String[] tmps=sbLast.toString().split("@\\|#\\\\#",-1);
                if(tmps!=null&&tmps.length>0){
                	for(int i=0;i<tmps.length-1;i++){
                		pw.write(tmps[i].replaceAll("\\$\\|\\|#", "#_#")+"\n");
                	}
                	sbLast=new StringBuilder(tmps[tmps.length-1]);
                }
            }
			
            pw.write(sbLast.toString().replaceAll("\\$\\|\\|#", "#_#"));
			pw.flush();
		}catch (Exception e) {
			throw e;
		}finally{
			try{
				if(fileRead!=null)
					fileRead.close();
				if(pw!=null)
					pw.close();
			}catch (Exception e) {
				logger.error("关闭资源出错", e);
			}
		}
	}

	/**
	 * //转换后目录规范 //根目录/场景编码/convert/文件名
	 * 
	 * @param fromPathFileName
	 * @param scene
	 * @return
	 * @throws Exception
	 */
	private String getToPathFileName(Node node,Step step,FileDesc fileDesc) {
		if(fileDesc.getFileName()==null || "".equals(fileDesc.getFileName())){
			return null;
		}
		int last = fileDesc.getFileName().lastIndexOf("/");
		if (last > 0) {
			String fileName = fileDesc.getFileName().substring(last + 1,
					fileDesc.getFileName().length());
			String rootPath = node.getServerCluster().getRootPath();
			String toPath = rootPath + "/" + step.getScene().getName() + "/convert";
			File tpath=new File(toPath);
			if(!tpath.exists()){
				tpath.mkdirs();
			}
			// 转换之后生成新的文件名，每次处理都生成新的文件名
			String toPathFileName = toPath + "/" + fileName + "_" + TimeUtil.getRandom(5)+ ".csv";
		   
			return toPathFileName;
		}
		return null;
	}

	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}
}
