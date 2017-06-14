package com.zbiti.etl.extend.executer.convert.lte4g;
 
import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.zbiti.common.TimeUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.ConvertRecord;
import com.zbiti.etl.extend.vo.ConvertStep;

@Service("LteConvertExecuter")
public class LteConvertExecuter implements ICommandExecuter<Boolean> {

	/**
	 * 转换的抽象父类，实现转换的公共的基本的功能，包括取消息，参数获取，发布消息等。 lte 4G话单的转换实现类，基于文件的转换，非行转换
	 * 不实现具体的转换
	 * 
	 * @param args
	 */
	private static final Log logger = LogFactory.getLog(LteConvertExecuter.class);
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
			long convertBytes=convert(fileDesc.getFileName(), toPathFileName,startBytes);
			logger.info("结束转换，目标文件为"+toPathFileName+"，结束字节为："+convertBytes);
			logger.info("变更文件名为.finish");
			new File(toPathFileName).renameTo(new File(toPathFileName+".finish"));
			if(startBytes>=convertBytes)
				continue;
			convertRecord.setModifyTime(new Date());
			convertRecord.setConvertBytes(convertBytes);
			logger.info("保存转换记录");
			stepService.saveConvertRecord(convertRecord);
			logger.info("将处理完成的文件放入队列");
			fileDesc.setFileName(toPathFileName+".finish");
			fileDesc.setServerName(node.getServerName());
			fileDescQueue.push(fileDesc);
		}
		return true;
	}
	
	private long convert(String fromPath,String toPath,long convertBytes) throws Exception{
		
		try{
			File fromFile=new File(fromPath);
			if(!fromFile.exists()||convertBytes>=fromFile.length()){
				return convertBytes;
			}
			ConvertForASN d = new ConvertForASN();
			if(fromPath.contains("JSNJCG01")){
				d.setSourceIp("132.228.151.6");
			}else if(fromPath.contains("JSNJCG02")){
				d.setSourceIp("132.228.242.6");
			}
			d.setFtpType(getFtpType(fromPath));
			d.convert(fromPath, toPath, convertBytes);
			return fromFile.length();
		}catch (Exception e) {
			throw e;
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
	
	/**
	 * 获取ftp类型
	 * --132.228.151.6                        
	/jiangsu/YFCG/AP/first/pre_except_pgwcdr/ TYPE =1
	gzAP11080005458844_JSNJCG01_-_00433014.20151027_-_2359+0800.ctc.dat.gz 
	/jiangsu/YFCG/AP/first/pre_normal_pgwcdr/ TYPE =2
	gzAP11080005458846_JSNJCG01_-_01163691.20151027_-_2359+0800.ctc.dat.gz
	/jiangsu/YFCG/AP/first/pos_normal_pgwcdr/ TYPE =3
	gzAP11080005458849_JSNJCG01_-_01434257.20151027_-_2359+0800.ctc.dat.gz

	--132.228.242.6
	/jiangsu/GLCG/AP/first/pre_except_pgwcdr/ TYPE =1
	gzAP11080005455798_JSNJCG02_-_00438470.20151027_-_2359+0800.ctc.dat.gz
	/jiangsu/GLCG/AP/first/pre_normal_pgwcdr/ TYPE =2
	gzAP11080005455797_JSNJCG02_-_01209143.20151027_-_2359+0800.ctc.dat.gz
	/jiangsu/GLCG/AP/first/pos_normal_pgwcdr/ TYPE =3
	gzAP11080005445743_JSNJCG02_-_01470899.20151027_-_1717+0800.ctc.dat.gz
	yhp 修改根据文件夹名字区分
	 * @param fromPathFileName
	 * @return
	 */
	private String getFtpType(String fromPathFileName) {
//		if(fromPathFileName.contains("JSNJCG01_-_004") ||  fromPathFileName.contains("JSNJCG02_-_004") ){
//			return "1" ;
//		}
//		if(fromPathFileName.contains("JSNJCG01_-_011") ||  fromPathFileName.contains("JSNJCG02_-_012") ){
//			return "2" ;
//		}
//		if(fromPathFileName.contains("JSNJCG01_-_014") ||  fromPathFileName.contains("JSNJCG02_-_014") ){
//			return "3" ;
//		}
		if(fromPathFileName.contains("pre_except_pgwcdr")){
			return "1" ;
		}
		if(fromPathFileName.contains("pre_normal_pgwcdr")){
			return "2" ;
		}
		if(fromPathFileName.contains("pos_normal_pgwcdr")){
			return "3" ;
		}
		return "";
	}

	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}

}
