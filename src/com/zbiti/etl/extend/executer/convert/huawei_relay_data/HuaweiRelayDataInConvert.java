package com.zbiti.etl.extend.executer.convert.huawei_relay_data;

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
/**
 * 入华为中继数据
 * @author admin
 *
 */
@Service("HuaweiRelayDataInConvert")
public class HuaweiRelayDataInConvert implements IConvertExecuter{

	/*
	 * Device,Task,Measuring Object,Measuring Object ID,Collection Time ,Polling Period ,SEIZ,TEBID,TRNBID,CNT,ANS,TEANS,TRNANS,INTCGCL,CGT,CBSY,ARGABN,LTNANS,INSCIR,AVACIR,BLKCIR,INSBICIR,AVABICIR,BLKBICIR,BISERL,SERL,CERL,RERL,AST,IADDT,EMPTY,INCOM,ITKAVAR,CNTR,ANSR,BLKR,BLKBIR,PCCWCAT,PCCWAT,TIPEFCCG,HCIRCUITN,INTKLICENSE
入方向入库字段
序号	属性	描述
1	Device		设备ip
2	Task	任务
3	Measuring Object	中继群名称
4	Measuring ObjectID	中继群号
5	CollectionTime	采集时间
6	PollingPeriod	采集周期
7	SEIZ	占用次数
8	CNT	接通次数 
9	ANS	应答次数
10	SEIZR	占用率   不取了
11	CNTR	接通率
12	ANSR	应答率
13	SERL	占用话务量
14	CERL	接通话务量
15	RERL	应答话务量
16	AST	平均占用时常
	 */
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(StringUtil.objectToStr(data).equals(""))
			return "";
		if(data.startsWith("Device"))
			return "";
		String[] dataArray=data.split(",");
		if(dataArray.length<42)
			return "";

		return dataArray[0] + ConvertUtil.SPIT_SIGN 
				+filterQuota(dataArray[1])+ ConvertUtil.SPIT_SIGN 
				+ filterQuota(dataArray[2]) + ConvertUtil.SPIT_SIGN
				+ filterQuota(dataArray[3]) + ConvertUtil.SPIT_SIGN 
				+ dataArray[4]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[5] + ConvertUtil.SPIT_SIGN
				+ dataArray[6] + ConvertUtil.SPIT_SIGN 
//				+ dataArray[6] + ConvertUtil.SPIT_SIGN //占用率   占用率不要了。
				+ dataArray[9]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[10] + ConvertUtil.SPIT_SIGN
				+ dataArray[33] + ConvertUtil.SPIT_SIGN 
				+ dataArray[34]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[25] + ConvertUtil.SPIT_SIGN
				+ dataArray[26] + ConvertUtil.SPIT_SIGN
				+ dataArray[27] + ConvertUtil.SPIT_SIGN 
				+ dataArray[28]+ ConvertUtil.SPIT_SIGN+"IN"+ ConvertUtil.SPIT_SIGN+ "\n";
	}

	
	/*
Device(0)
Task(1)
Measuring Object(2)
Measuring Object ID(3)
Collection Time (4)
Polling Period (5)
SEIZ(6)
TEBID(7)
TRNBID(8)
CNT(9)
ANS(10)
TEANS(11)
TRNANS(12)
INTCGCL(13)
CGT(14)
CBSY(15)
ARGABN(16)
LTNANS(17)
INSCIR(18)
AVACIR(19)
BLKCIR(20)
INSBICIR(21)
AVABICIR(22)
BLKBICIR(23)
BISERL(24)
SERL(25)
CERL(26)
RERL(27)
AST(28)
IADDT(29)
EMPTY(30)
INCOM(31)
ITKAVAR(32)
CNTR(33)
ANSR(34)
BLKR(35)
BLKBIR(36)
PCCWCAT(37)
PCCWAT(38)
TIPEFCCG(39)
HCIRCUITN(40)
INTKLICENSE(41)
	 */
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
			File f=new File("D:/huawei_delay_data/in.txt");
			if(f.exists()){
				fileRead=new BufferedRandomAccessFile(f, "r");//read模式
			}
			String temp=new String(); 
			StringBuffer sb=new StringBuffer();
			while((temp=fileRead.readLine())!=null){
				temp=StringUtil.ChangeCode(temp, "iso8859-1", "utf-8");
				if(temp.equals(""))
					continue;
				System.out.print(new HuaweiRelayDataInConvert().doConvert(temp, ""));
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
