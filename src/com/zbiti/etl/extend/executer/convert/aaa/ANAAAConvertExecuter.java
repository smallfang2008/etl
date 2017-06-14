package com.zbiti.etl.extend.executer.convert.aaa;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
@Service("ANAAAConvertExecuter")
public class ANAAAConvertExecuter extends AbstractAAA implements IConvertExecuter{

	public static void main(String[] args) {
		String MSID="460030367473395";
		System.out.println(MSID.length());
		System.out.println(MSID.substring(0, 15));
	}
	
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if("".equals(data.trim())){
			return "";
		}
		StringBuffer result = new StringBuffer();
		result.delete(0, result.length());
		String[] columns = data.split(", ");
		if(columns.length<8)
			return "";
		String DATE_ID = columns[0];
		String AUTH_DATE = columns[1];
		String MSID = columns[3];
		String MSID_REGIONID="";
		if(MSID.length()>=15)
			MSID_REGIONID = imsiToregion(MSID.substring(0, 15));
		else
			MSID_REGIONID=imsiToregion(MSID);
		String STATE = "2";
		String TERMINATE_IP  = columns[5];
		String INVALID_ID = "";
		String RESULT  = columns[7];
		String UNIQUE_ID_IN_SRC_SYS = "";
		String MSID_ID =  columns[2].trim();
		String DOMAIN = "";
		if(MSID_ID.indexOf("@")> -1){
			DOMAIN=MSID_ID.substring(columns[2].indexOf("@")+1).trim();
		}
		//AUTH_DATE格式调整，由yyyyMMddHHmmss改为yyyy-MM-dd HH:mm:ss
		if("".equals(AUTH_DATE)){
			AUTH_DATE = "";
		}else{
			AUTH_DATE = this.getFormatDate(this.aaaNewTimeId,this.aaaNew,AUTH_DATE);
		}
	//	String RESULT_CODE = columns[6];
		result.append(DATE_ID);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(AUTH_DATE);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(MSID_REGIONID);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(MSID);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(STATE);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(TERMINATE_IP);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(INVALID_ID);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(RESULT);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(UNIQUE_ID_IN_SRC_SYS);
		result.append(ConvertUtil.SPIT_SIGN);
		result.append(DOMAIN);
		result.append("\n");
		
		return result.toString();
	}

	
}
