package com.zbiti.etl.extend.executer.convert.dhcp;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("DhcpAuthLogConvertExecuter")
public class DhcpAuthLogConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if (null == data || "".equals(data.trim())){
			return "";
		}

		//时间 处理结果|错误码|ipoe方式编号|SR地址|终端标识|option60类型|option82|认证错误原因|
		StringBuilder returnLine = new StringBuilder();
		String[] info = data.split("\\|");
		if(info==null||info.length<8)
			return "";
		//info[0]:2015/04/27 14:00:00 failed
		String[] head = info[0].split(" ");		
		returnLine.append(head[0]+" "+head[1]+ConvertUtil.SPIT_SIGN+head[2]);
		String option60TypeName=DhcpDic.dhcpAuthLogOption60Type.get(info[5])==null?"":DhcpDic.dhcpAuthLogOption60Type.get(info[5]);
		String errorCodeName=DhcpDic.dhcpAuthLogErrorCode.get(info[1])==null?"":DhcpDic.dhcpAuthLogErrorCode.get(info[1]);
		String city=DhcpDic.dhcpLogIpCity.get(info[3])==null?"":DhcpDic.dhcpLogIpCity.get(info[3]);
		for(int i=1;i<info.length;i++){
			returnLine.append(ConvertUtil.SPIT_SIGN+info[i]);
			if(i==1){
				returnLine.append(ConvertUtil.SPIT_SIGN+errorCodeName);
			}
			if(i==3){
				returnLine.append(ConvertUtil.SPIT_SIGN+city);
			}
			if(i==5){
				returnLine.append(ConvertUtil.SPIT_SIGN+option60TypeName);
			}
		}
	    return returnLine + "\n";
	}

	public static void main(String[] args) {
		String data="2016/08/26 14:00:00 failed|101|4|10.206.56.1|38:fa:ca:d4:8f:66|2|0 0/0/0:0.0 ZXAN/0/0/2/0/7/000000001625B6ACA72CC1BC GP|Status_Err|";
		try {
			System.out.println(new DhcpAuthLogConvertExecuter().doConvert(data, ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
