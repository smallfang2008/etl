package com.zbiti.etl.extend.executer.convert.dhcp;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("DhcpLogConvertExecuter")
public class DhcpLogConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if (null == data || "".equals(data.trim())){
			return "";
		}
		//时间处理结果|错误码|请求包类型|业务类型编号|ipoe方式编号|接口编号|来源IP|giaddr|mac|用户名|域名|线路信息|请求的地址|分配的地址|租约时长|响应包类型|NAK返回错误代码
		StringBuilder returnLine = new StringBuilder();
		String[] info = data.split("\\|",-1);
		if(info==null||info.length<17)
			return "";
		//info[0]:2015/04/27 14:00:00 failed
		String[] head = info[0].split(" ");		
		returnLine.append(head[0]+" "+head[1]+ConvertUtil.SPIT_SIGN+head[2]);
		String resTypeName=DhcpDic.dhcpLogPackageType.get(info[2])==null?"":DhcpDic.dhcpLogPackageType.get(info[2]);
		String errorCodeName=DhcpDic.dhcpLogErrorCode.get(info[1])==null?"":DhcpDic.dhcpLogErrorCode.get(info[1]);
		String qosTypeName=DhcpDic.dhcpLogPackageType.get(info[15])==null?"":DhcpDic.dhcpLogPackageType.get(info[15]);
		String nakErrorCodeName=DhcpDic.dhcpLogErrorCode.get(info[16])==null?"":DhcpDic.dhcpLogErrorCode.get(info[16]);
		String city=DhcpDic.dhcpLogIpCity.get(info[6])==null?"":DhcpDic.dhcpLogIpCity.get(info[6]);
		for(int i=1;i<info.length;i++){
			returnLine.append(ConvertUtil.SPIT_SIGN+info[i]);
			if(i==1){
				returnLine.append(ConvertUtil.SPIT_SIGN+errorCodeName);
			}
			if(i==2){
				returnLine.append(ConvertUtil.SPIT_SIGN+resTypeName);
			}
			if(i==6){
				returnLine.append(ConvertUtil.SPIT_SIGN+city);
			}
			if(i==15){
				returnLine.append(ConvertUtil.SPIT_SIGN+qosTypeName);
			}
			if(i==16){
				returnLine.append(ConvertUtil.SPIT_SIGN+nakErrorCodeName);
			}
		}
	    return returnLine + "\n";
	}
	
	public static void main(String[] args) {
		String data="2015/03/04 05:00:00 ok|0|102|4|4|350|10.192.1.187|10.192.1.187|00:25:5e:85:a4:e2|0511200551271|vod|0 0/0/0:0.0 ZJ-SM-OLT-1.MAN.MA5680T/0/0/11/0/5/0000000040AA98B02C430CBD GP|10.193.16.232|10.193.16.232|1200|5|0";
		try {
			System.out.println(new DhcpLogConvertExecuter().doConvert(data, ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
