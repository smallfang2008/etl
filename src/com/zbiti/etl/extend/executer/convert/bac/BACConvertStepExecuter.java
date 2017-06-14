package com.zbiti.etl.extend.executer.convert.bac;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("BACConvertStepExecuter")
public class BACConvertStepExecuter implements IConvertExecuter {

	/**
	 * command: disp sbc reginfo
	 * usercount,Device_name:JH-IMS-ZJBAC1_ZHIHUILOU,date
	 * :2015-07-15-10,UserType:sip,Registered:43784 cdn data conver
	 */
	@Override
	public String doConvert(String line, String filePathName) {

		if (line == null || line.trim().equals("")) {
			return "";
		}
		String[] subLine = line.split(",");
		if (subLine.length < 4) {
			return "";
		}
		String returnLine = "";

		String[] commandArray = subLine[0].split(":");
		if (commandArray.length < 2) {
			return "";
		}
		String command = commandArray[1];// 命令

		String[] deviceArray = subLine[1].split(":");
		if (deviceArray.length < 2) {
			return "";
		}
		String device = deviceArray[1];// 设备名称

		String[] dateeArray = subLine[2].split(":");
		if (dateeArray.length < 2) {
			return "";
		}
		String date = dateeArray[1];// 日期

		int first = line.indexOf(",date:");
		if (line.length() < first + 20) {
			return "";
		}
		// 命令返回字符串
		String commandResult = line.substring(first + 20, line.length());
		//System.out.println(line);
		//System.out.println(commandResult);
		returnLine = command + ConvertUtil.SPIT_SIGN + device
				+ ConvertUtil.SPIT_SIGN + date + ConvertUtil.SPIT_SIGN
				+ commandResult + "\n";
		//System.out.println(returnLine);
		return returnLine;
	}
	public static void main(String[] args) {
		BACConvertStepExecuter a= new BACConvertStepExecuter();
		a.doConvert("command: show sip statistic,Device_name:JH-SS-YZBAC2_2,date:2015-07-15-10,Online Call Numbers      :  10,Online Call Numbers      :  0","");		
	}

}