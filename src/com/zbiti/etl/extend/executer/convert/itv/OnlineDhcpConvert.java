package com.zbiti.etl.extend.executer.convert.itv;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
@Service("OnlineDhcpConvert")
public class OnlineDhcpConvert  implements IConvertExecuter{
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(data==null||"".equals(data))
			return "";
		String[] datas=data.split(",");
		if(datas.length<4)
			return "";
		if(!"IPTV".equals(datas[2]))
			return "";
		return datas[0] + ConvertUtil.SPIT_SIGN
				+ datas[3]+ ConvertUtil.SPIT_SIGN
				+"009"+ConvertUtil.SPIT_SIGN
				+ ConvertUtil.cityMap.get(datas[1])+"\n";
	}
}
