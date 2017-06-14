package com.zbiti.etl.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zbiti.common.FileUtil;
import com.zbiti.common.GZip;
import com.zbiti.etl.extend.vo.FileUpStep;

public class TestFile {

	public static void main(String[] args) {

//		try {
//			File file=new File(new File(".").getCanonicalPath()+"/kettleConvert/xx.ktr");
//			if(!file.getParentFile().exists()){
//				file.getParentFile().mkdirs();
//			}
//			FileOutputStream fos= new FileOutputStream(file);
//			fos.write(new byte[]{1});
//			fos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			File file=new File(new File(".").getCanonicalPath()+"/kettleJob/com.zip");
//			//执行路径-目录名称截取掉kettle文件名后缀
//			String execPath=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("."));
//			//如果执行过，先删除上一次的目录
//			FileUtil.deleteDir(execPath);
//			//解压文件到执行目录
//			FileUtil.unzipFile(file, execPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		for(String filename:new File(".").list()){
//			System.out.println(filename);
//		}
		
//		try {
//
//			new File("D://test_oracle_data_exp/oracleExp/test_20170312.txt.sh").delete();
//			FileUtil.writeToFileV2("D://test_oracle_data_exp/oracleExp/test_20170312.txt.sh", "1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String fileName="xx.cs";
//		if(fileName.endsWith(".csv")){
//			fileName=fileName.substring(0,fileName.lastIndexOf(".csv"));
//		}
//		System.out.println(fileName);
//		String fromPath="D:/temp/建外关联表最新.txt";
//		FileUpStep fileUpStep=new FileUpStep();
//		fileUpStep.setIsAppendTime(3);
//		fileUpStep.setAppendTimePattern("yyyyMMdd");
//		fileUpStep.setAppendTimeOffset(0);
//		fileUpStep.setIsGz(1);
//		String toFileName=getToPathFile(fileUpStep, fromPath);
//		System.out.println(toFileName);
//		try {
////			GZip.compressFile(filename, filename+".gz");
//			if(fileUpStep.getIsGz()==1){//压缩
//				toFileName+=".gz";
//				GZip.compressFile(fromPath, fromPath.substring(0,fromPath.lastIndexOf("/")+1)+toFileName);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(filename.substring(filename.lastIndexOf("/")+1));
//		System.out.println(filename.substring(0,filename.lastIndexOf("/")+1));
		
		
		
		System.out.println(new File("D:/xxxxx").length());
	}
	
	private static String getToPathFile(FileUpStep fileUpStep,String fromPath){
		String resultFilePath=fromPath.substring(fromPath.lastIndexOf("/")+1);
		if(fileUpStep.getIsAppendTime()!=1){//1表示不追加
			SimpleDateFormat sdf=new SimpleDateFormat(fileUpStep.getAppendTimePattern().trim());
			String timeid=sdf.format(new Date(System.currentTimeMillis()+fileUpStep.getAppendTimeOffset()*24*60*60*1000));
			String fromPathFile=fromPath.substring(fromPath.lastIndexOf("/")+1);
			if(fileUpStep.getIsAppendTime()==2){//文件前
				resultFilePath=timeid+"_"+fromPathFile;
			}else if (fileUpStep.getIsAppendTime()==3){//后缀前
				if(fromPathFile.indexOf(".")!=-1){
					resultFilePath=fromPathFile.substring(0,fromPathFile.indexOf("."))+"_"+timeid+fromPathFile.substring(fromPathFile.indexOf("."));
				}else{
					resultFilePath=fromPathFile+"_"+timeid;
				}
			}else if (fileUpStep.getIsAppendTime()==4){//文件名后
				resultFilePath=fromPathFile+"_"+timeid;
			}
		}
		return resultFilePath;
	}
}
