package com.zbiti.etl.extend.executer.convert.hss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.json.JSONUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.ConvertStep;
/**
 * 此转换步骤会将接受到的上一步传输过来的队列解析结果全部追加到相同的文件中
 * @author yhp
 *
 */
public class HssUserinfoExportConvertExecuter implements ICommandExecuter<Boolean>{
	private static final Log logger = LogFactory.getLog(HssUserinfoExportConvertExecuter.class);
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
		Set<String> pushFileSet=new HashSet<String>();
		FileDesc fileDesc=null;
		for(MorphDynaBean bean:fileQueue){
			fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
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
			pushFileSet.add(toPathFileName);
		}
		for(String toPath:pushFileSet){
			logger.info("将处理完成的文件放入队列");
			fileDesc.setFileSize(new File(toPath).length());
			fileDesc.setModifyDate(new File(toPath).lastModified());
			fileDesc.setFileName(toPath);
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
			pw=new PrintWriter(new FileOutputStream(toPath,true));//append=true  追加
			fileRead=new BufferedReader(new InputStreamReader(new FileInputStream(fromFile),charset));
			String temp=new String(); 
			StringBuilder sb=new StringBuilder();
            while((temp=fileRead.readLine())!=null){
            	temp = temp.trim();
				if(temp.equals("<SUBBEGIN"))
					sb=new StringBuilder();
				if(temp.startsWith("IMSI")){
					sb.append(temp.substring(temp.indexOf("=")+1,temp.indexOf(";"))+" ");
				}
				if(temp.startsWith("MSISDN")){
					sb.append(temp.substring(temp.indexOf("=")+1,temp.indexOf(";"))+" ");
				}
				if(temp.startsWith("LOCK")){
					if(temp.substring(temp.indexOf("=")+1,temp.indexOf(";")).equals("EPSLOCK&NON3GPPLOCK"))
						sb.append("TRUE");
					else
						sb.append("FALSE");
				}
				if(temp.equals("<SUBEND")){
					if(!sb.toString().endsWith("TRUE")&&!sb.toString().endsWith("FALSE")){
						sb.append("FALSE");
					}
					pw.write(sb.toString()+"\n");
				}
            }
			
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
	 * 2559_OK_MemExpFile_HSS9860_2_27_20170318000001.tar.gz.datDir-->2017031800.txt
	 * @param fromPathFileName
	 * @param scene
	 * @return
	 * @throws Exception
	 */
	private String getToPathFileName(Node node,Step step,FileDesc fileDesc) {
		if(fileDesc.getFileName()==null || "".equals(fileDesc.getFileName())){
			return null;
		}

		String fileName=fileDesc.getFileName().substring(0,fileDesc.getFileName().lastIndexOf("/"));
		fileName=fileName.substring(fileName.lastIndexOf("/")+1);
		fileName=fileName.substring(fileName.lastIndexOf(".tar.gz")-14,fileName.lastIndexOf(".tar.gz")-4);
		String rootPath = node.getServerCluster().getRootPath();
		String toPath = rootPath + "/" + step.getScene().getName() + "/convert";
		File tpath=new File(toPath);
		if(!tpath.exists()){
			tpath.mkdirs();
		}
		// 转换之后生成新的文件名，每次处理都生成新的文件名
		String toPathFileName = toPath + "/" + fileName+".txt";
	   
		return toPathFileName;
	}

	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}
}
