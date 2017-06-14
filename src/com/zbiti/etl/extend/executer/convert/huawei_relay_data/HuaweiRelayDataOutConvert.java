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
 * 出华为中继数据
 * @author admin
 *
 */
@Service("HuaweiRelayDataOutConvert")
public class HuaweiRelayDataOutConvert implements IConvertExecuter{

	/*
	 * Device,Task,Measuring Object,Measuring Object ID,Collection Time ,Polling Period ,BID,SEIZ,TRNBID,TRNSEIZ,CNT,ANS,OFL,DNMA,CNMA,DSEIZ,RETRY,POFCCG,CGT,NETADM,DRRUCL,CIRORI,CTSCL,ARF,RUJMP,TMPAMB,CBSY,CTB,CLB,ARGABN,LTNANS,INSCIR,AVACIR,BLKCIR,INSBICIR,AVABICIR,BLKBICIR,OTKAVAR,SEIZR,CNTR,ANSR,BLKR,BLKBIR,BISERL,SERL,CERL,RERL,AST,EMPTY,INCOM,TOPEFCCG,HCIRCUITN,SCRDT,SCRT,LOSTCALL,INTERNALRELEASE,IFT
	 * 出方向入库字段：
		序号	属性	描述
		1	Device	设备ip
		2	Task	任务
		3	MeasuringObject	中继群名称
		4	Measuring ObjectID	中继群号
		5	CollectionTime	采集时间
		6	PollingPeriod	采集周期
		7	SEIZ	占用次数
		8	CNT	接通次数
		9	ANS	应答次数
		10	SERL	占用话务量
		11	CERL	接通话务量
		12	RERL	应答话务量
		13	AST	平均占用时常
		14	CNTR	接通率
		15	ANSR	应答率
		
	 */
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(StringUtil.objectToStr(data).equals(""))
			return "";
		if(data.startsWith("Device"))
			return "";
		String[] dataArray=data.split(",");
		if(dataArray.length<57)
			return "";

		return dataArray[0] + ConvertUtil.SPIT_SIGN 
				+filterQuota(dataArray[1])+ ConvertUtil.SPIT_SIGN 
				+ filterQuota(dataArray[2]) + ConvertUtil.SPIT_SIGN
				+ filterQuota(dataArray[3]) + ConvertUtil.SPIT_SIGN 
				+ dataArray[4]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[5] + ConvertUtil.SPIT_SIGN
				+ dataArray[7] + ConvertUtil.SPIT_SIGN 
				+ dataArray[10]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[11] + ConvertUtil.SPIT_SIGN
				+ dataArray[44] + ConvertUtil.SPIT_SIGN 
				+ dataArray[45]+ ConvertUtil.SPIT_SIGN 
				+ dataArray[46] + ConvertUtil.SPIT_SIGN
				+ dataArray[47] + ConvertUtil.SPIT_SIGN
				+ dataArray[39] + ConvertUtil.SPIT_SIGN 
				+ dataArray[40]+ ConvertUtil.SPIT_SIGN+"OUT"+ ConvertUtil.SPIT_SIGN+ dataArray[38]+ ConvertUtil.SPIT_SIGN+ "\n";
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
	
	/*
Device(0)
Task(1)
Measuring Object(2)
Measuring Object ID(3)
Collection Time (4)
Polling Period (5)
BID(6)
SEIZ(7)
TRNBID(8)
TRNSEIZ(9)
CNT(10)
ANS(11)
OFL(12)
DNMA(13)
CNMA(14)
DSEIZ(15)
RETRY(16)
POFCCG(17)
CGT(18)
NETADM(19)
DRRUCL(20)
CIRORI(21)
CTSCL(22)
ARF(23)
RUJMP(24)
TMPAMB(25)
CBSY(26)
CTB(27)
CLB(28)
ARGABN(29)
LTNANS(30)
INSCIR(31)
AVACIR(32)
BLKCIR(33)
INSBICIR(34)
AVABICIR(35)
BLKBICIR(36)
OTKAVAR(37)
SEIZR(38)
CNTR(39)
ANSR(40)
BLKR(41)
BLKBIR(42)
BISERL(43)
SERL(44)
CERL(45)
RERL(46)
AST(47)
EMPTY(48)
INCOM(49)
TOPEFCCG(50)
HCIRCUITN(51)
SCRDT(52)
SCRT(53)
LOSTCALL(54)
INTERNALRELEASE(55)
IFT(56)
	 */
	
	public static void main(String[] args) {
		
//		String str="Device,Task,Measuring Object,Measuring Object ID,Collection Time ,Polling Period ,BID,SEIZ,TRNBID,TRNSEIZ,CNT,ANS,OFL,DNMA,CNMA,DSEIZ,RETRY,POFCCG,CGT,NETADM,DRRUCL,CIRORI,CTSCL,ARF,RUJMP,TMPAMB,CBSY,CTB,CLB,ARGABN,LTNANS,INSCIR,AVACIR,BLKCIR,INSBICIR,AVABICIR,BLKBICIR,OTKAVAR,SEIZR,CNTR,ANSR,BLKR,BLKBIR,BISERL,SERL,CERL,RERL,AST,EMPTY,INCOM,TOPEFCCG,HCIRCUITN,SCRDT,SCRT,LOSTCALL,INTERNALRELEASE,IFT";
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
			File f=new File("D:/huawei_delay_data/out.txt");
			if(f.exists()){
				fileRead=new BufferedRandomAccessFile(f, "r");//read模式
			}
			String temp=new String(); 
			StringBuffer sb=new StringBuffer();
			while((temp=fileRead.readLine())!=null){
				temp=StringUtil.ChangeCode(temp, "iso8859-1", "utf-8");
				if(temp.equals(""))
					continue;
				System.out.print(new HuaweiRelayDataOutConvert().doConvert(temp, ""));
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
