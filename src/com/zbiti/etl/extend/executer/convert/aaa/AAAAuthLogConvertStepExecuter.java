package com.zbiti.etl.extend.executer.convert.aaa;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
@Service("AAAAuthLogConvertStepExecuter")
public class AAAAuthLogConvertStepExecuter extends AbstractAAA implements IConvertExecuter {

	private String temp = null;
	private StringBuffer result = new StringBuffer();
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(data==null||"".equals(data.trim())){
			return "";
		}
		result.delete(0, result.length());
		// 截取时间

		// //截取时间
		// if(line.indexOf("user") > -1){
		// temp = line.substring(0, line.indexOf("user")).trim();
		// } else {
		// throw new Exception("该行不存在user,截取发生错误" + line);
		// }
		// //将时间转换成对应ID
		// result.append(timeToId(temp,"AAA"));
		// result.append(ConvertUtil.SPIT_SIGN);
		// //将日期写入stringbuffer中
		// result.append(getFormatDate(temp));
		// result.append(ConvertUtil.SPIT_SIGN);
		// //截取用戶名
		// if(line.indexOf("from") > -1){
		// temp = line.substring(line.indexOf("user")+4,
		// line.indexOf("from")).trim();
		// } else {
		// throw new Exception("该行不存在from,截取发生错误" + line);
		// }
		//
		//
		// if(temp.indexOf(":") > -1){
		// result.append(temp.substring(0,temp.indexOf(":")).trim());
		// } else {
		// throw new Exception("该行不存在:,截取发生错误" + line);
		// }
		// result.append(ConvertUtil.SPIT_SIGN);
		// //将对应的IMSI号码匹配出地域ID(REGION_ID)
		// if(temp.indexOf(":") > -1){
		// temp = temp.substring(temp.indexOf(":")+ 1).trim();
		// } else {
		// throw new Exception("该行不存在:,截取发生错误" + line);
		// }
		//
		//
		// result.append(imsiToregion(temp));
		// result.append(ConvertUtil.SPIT_SIGN);
		// //将IMSI号码写入stringbuffer中
		// result.append(temp);
		// result.append(ConvertUtil.SPIT_SIGN);
		// //接入地
		// if(line.indexOf("authenticate") > -1){
		// temp =
		// line.substring(line.indexOf("from")+4,line.indexOf("authenticate")).trim();
		// } else {
		// throw new Exception("该行不存在authenticate,截取发生错误" + line);
		// }
		//
		//
		// result.append(temp);
		// result.append(ConvertUtil.SPIT_SIGN);
		// //错误原因
		// if(line.indexOf("authenticate") > -1){
		// temp = line.substring(line.indexOf("authenticate") + 12).trim();
		// } else {
		// throw new Exception("该行不存在authenticate,截取发生错误" + line);
		// }
		// if(temp.indexOf("error") > -1){
		// temp = temp.substring(temp.indexOf("error") + 6).trim();
		// } else {
		// throw new Exception("该行不存在error,截取发生错误" + line);
		// }
		// result.append(errorToId(temp, "AAA"));
		// result.append(ConvertUtil.SPIT_SIGN);
		// result.append(temp);
		// result.append(ConvertUtil.SPIT_SIGN).append(this.getMachineName()).append(ConvertUtil.SPIT_SIGN);
		// result.append("\n");
		// return result.toString();
		String tmpArr[] = data.split(",", -2);
		SimpleDateFormat timeStampType = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateId = new SimpleDateFormat("yyyyMMddHH");
		if (tmpArr.length != 9) {
			return "";
		}
		for (int i = 0; i < tmpArr.length; i++) {
			result.append(tmpArr[i]).append(ConvertUtil.SPIT_SIGN);
		}
		long dateTime =0l;
		try{
			dateTime = Long.parseLong(tmpArr[0]);
		}catch (Exception e) {
			return "";
		}
		String timeStampNew = timeStampType.format(dateTime * 1000);
		result.append(timeStampNew).append(ConvertUtil.SPIT_SIGN);
		String dateIdNew = dateId.format(dateTime * 1000);
		result.append(dateIdNew).append(ConvertUtil.SPIT_SIGN);
		if (tmpArr[5].equals("") || tmpArr[5] == null) {
//			String ismi = tmpArr[2].substring(0, 11);
			temp = imsiToregion(tmpArr[2]);
			result.append(temp).append(ConvertUtil.SPIT_SIGN);
		}else{
			result.append("").append(ConvertUtil.SPIT_SIGN);
		}
		result.append("\n");
		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
