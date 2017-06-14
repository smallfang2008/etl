package com.zbiti.etl.extend.executer;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.StringUtil;
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
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
import com.zbiti.etl.extend.vo.ConvertRecord;
import com.zbiti.etl.extend.vo.ConvertStep;

public class ConvertStepExecuter implements ICommandExecuter<Boolean>{
	private static final Log logger = LogFactory.getLog(ConvertStepExecuter.class);
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
		IConvertExecuter convertExecuter = (IConvertExecuter) ctx.getBean(convertStep.getSuperClass());
		for(MorphDynaBean bean:fileQueue){
			FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
			// 根目录/场景编码/convert/文件名
			String toPathFileName = this.getToPathFileName(node, step, fileDesc);
			logger.info("定义目标文件名："+toPathFileName);
			if(toPathFileName==null)
				continue;
			logger.info("获取文件转换记录");
			ConvertRecord convertRecord=stepService.getConvertRecord(step.getStepId(), fileDesc.getFileName());
			if(convertRecord==null){
				convertRecord=new ConvertRecord();
				convertRecord.setFilePath(fileDesc.getFileName());
				convertRecord.setStep(step);
				convertRecord.setConvertBytes(0);
				convertRecord.setInsertTime(new Date());
			}
			
			String charset = convertStep.getCharset() != null&& !"".equals(convertStep.getCharset()) ? convertStep.getCharset() : "gbk";
			logger.info("转换编码："+charset);
			long startBytes=convertRecord.getConvertBytes();
			//如果不追加，则当新文件处理
			if ("1".equals(fileDesc.getSourceType())) {
				logger.info("文件不追加，设定初始大小为0");
				startBytes=0;
			}
			logger.info("开始转换["+fileDesc.getFileName()+"]，初始字节为："+startBytes);
			long convertBytes=convert(convertExecuter, fileDesc.getFileName(), toPathFileName,startBytes, charset);
			logger.info("结束转换，目标文件为"+toPathFileName+"，结束字节为："+convertBytes);
			if(startBytes>=convertBytes)
				continue;
			convertRecord.setModifyTime(new Date());
			convertRecord.setConvertBytes(convertBytes);
			logger.info("保存转换记录");
			stepService.saveConvertRecord(convertRecord);
			logger.info("将处理完成的文件放入队列");
			fileDesc.setFileName(toPathFileName);
			fileDesc.setServerName(node.getServerName());
			fileDescQueue.push(fileDesc);
		}
		return true;
	}
	
	private long convert(IConvertExecuter convertExecuter,String fromPath,String toPath,long convertBytes,String charset) throws Exception{
		BufferedRandomAccessFile fileRead = null;
		PrintWriter pw = null;
		try{
			File fromFile=new File(fromPath);
			if(!fromFile.exists()||convertBytes>=fromFile.length()){
				return convertBytes;
			}
			File toPathFile=new File(toPath);
			if(toPathFile.exists())
				toPathFile.delete();
			pw=new PrintWriter(toPathFile);
			fileRead=new BufferedRandomAccessFile(fromFile, "r");
			fileRead.seek(convertBytes);//因为要标记转换位置，故此没有预先设置内容读取编码
			String temp;
			while ((temp = fileRead.readLine()) != null) {
				temp=StringUtil.ChangeCode(temp, "iso-8859-1",charset);
				String result=convertExecuter.doConvert(temp, fromPath);
				if (result != null && !"".equals(result)) {
					pw.write(result);
				}
			}
			pw.flush();
			return fileRead.getFilePointer();
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
			if(fileName.endsWith(".csv")){
				fileName=fileName.substring(0,fileName.lastIndexOf(".csv"));
			}
			// 转换之后生成新的文件名，每次处理都生成新的文件名
			String toPathFileName = toPath + "/" + fileName + ("1".equals(fileDesc.getSourceType())?"":"_" + TimeUtil.getRandom(5))+ ".csv";
		   
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
