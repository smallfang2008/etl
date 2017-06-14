package com.zbiti.etl.extend.executer.convert.provinceRing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.springframework.stereotype.Service;

import com.zbiti.common.StringUtil;
import com.zbiti.common.stream.BufferedRandomAccessFile;
import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("ProvinceRingSysConvert")
public class ProvinceRingSysConvert  implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(StringUtil.objectToStr(data).equals(""))
			return "";
		if(data.startsWith("\"告警级别\""))
			return "";
		String[] dataArray=data.split("\",\"");
		if(dataArray==null||dataArray.length!=15)
			return "";
		StringBuffer sb=new StringBuffer();
		int i=0;
		
		for(String dataColumn:dataArray){
			if(i!=0){
				sb.append(ConvertUtil.SPIT_SIGN);
			}
			sb.append(filterQuota(dataColumn));
			i++;
		}
		return sb.toString()+"\n";
	}
	
	private String filterQuota(String data){
		if(data.startsWith("\"")){
			data=data.substring(1);
		}
		if(data.endsWith("\"")){
			data=data.substring(0,data.length()-1);
		}
		return data;
	}

	public static void main(String[] args) {
		
//		String str="Device,Task,Measuring Object,Measuring Object ID,Collection Time ,Polling Period ,SEIZ,TEBID,TRNBID,CNT,ANS,TEANS,TRNANS,INTCGCL,CGT,CBSY,ARGABN,LTNANS,INSCIR,AVACIR,BLKCIR,INSBICIR,AVABICIR,BLKBICIR,BISERL,SERL,CERL,RERL,AST,IADDT,EMPTY,INCOM,ITKAVAR,CNTR,ANSR,BLKR,BLKBIR,PCCWCAT,PCCWAT,TIPEFCCG,HCIRCUITN,INTKLICENSE";
//		String[] strArray=str.split(",");
////		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<strArray.length;i++){
//			System.out.println(strArray[i]+"("+i+")");
//		}
		
		BufferedRandomAccessFile fileRead =null;
		PrintWriter pw=null;
		try{
			pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/result.dat",true),"utf-8")),true);//auto flush,维护时请不要在下面在写一条
//			File f=new File("D:/pcrf_24hours_log.txt"); 
			File f=new File("D:/alarm-2015042912.csv");
			if(f.exists()){
				fileRead=new BufferedRandomAccessFile(f, "r");//read模式
			}
			String temp=new String(); 
			StringBuffer sb=new StringBuffer();
			while((temp=fileRead.readLine())!=null){
				temp=StringUtil.ChangeCode(temp, "iso8859-1", "utf-8");
				if(temp.equals(""))
					continue;
				System.out.print(new ProvinceRingSysConvert().doConvert(temp, ""));
//				System.out.println(temp);
//				System.out.println(temp.split(",").length);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			pw.close();
		}
	}
}
