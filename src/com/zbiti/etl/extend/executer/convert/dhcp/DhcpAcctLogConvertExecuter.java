package com.zbiti.etl.extend.executer.convert.dhcp;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("DhcpAcctLogConvertExecuter")
public class DhcpAcctLogConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if (null == data || "".equals(data.trim())){
			return "";
		}

		//时间 处理结果|错误码|请求类型|SR地址|业务类型|ipoe方式编号|用户名|域名|mac|用户IP |计费域名|过滤前缀|用户上线时间|用户下线时间|
		StringBuilder returnLine = new StringBuilder();
		String[] info = data.split("\\|");
		if(info==null||info.length<14)
			return "";
		//info[0]:2015/04/27 14:00:00 failed
		String[] head = info[0].split(" ");		
		returnLine.append(head[0]+" "+head[1]+ConvertUtil.SPIT_SIGN+head[2]);
		String resTypeName=DhcpDic.dhcpAcctLogRequestType.get(info[2])==null?"":DhcpDic.dhcpAcctLogRequestType.get(info[2]);
		String errorCodeName=DhcpDic.dhcpAcctLogErrorCode.get(info[1])==null?"":DhcpDic.dhcpAcctLogErrorCode.get(info[1]);
		String city=DhcpDic.dhcpLogIpCity.get(info[3])==null?"":DhcpDic.dhcpLogIpCity.get(info[3]);
		for(int i=1;i<info.length;i++){
			returnLine.append(ConvertUtil.SPIT_SIGN+info[i]);
			if(i==1){
				returnLine.append(ConvertUtil.SPIT_SIGN+errorCodeName);
			}
			if(i==2){
				returnLine.append(ConvertUtil.SPIT_SIGN+resTypeName);
			}
			if(i==3){
				returnLine.append(ConvertUtil.SPIT_SIGN+city);
			}
		}
	    return returnLine + "\n";
	}

	public static void main(String[] args) {
		String data="2016/08/24 15:00:00 failed|303|2|10.144.40.31|4|4|0512201895048|vod|20:8b:37:23:37:4c|10.152.198.168|vod|ad|2016/08/24 11:00:00|2016/08/24 15:40:00|";
		try {
			System.out.println(new DhcpAcctLogConvertExecuter().doConvert(data, ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

}
