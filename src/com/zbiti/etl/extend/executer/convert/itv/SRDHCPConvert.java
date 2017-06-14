package com.zbiti.etl.extend.executer.convert.itv;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
@Service("SRDHCPConvert")
public class SRDHCPConvert implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(data==null||"".equals(data))
			return "";
		String[] datas=data.split(",");
		if(datas.length<5)
			return "";
		if(!"IPTV".equals(datas[3]))
			return "";
		return datas[0] + ConvertUtil.SPIT_SIGN
				+ datas[2]+ ConvertUtil.SPIT_SIGN
				+ datas[4]+ ConvertUtil.SPIT_SIGN
				+"050"+ConvertUtil.SPIT_SIGN
				+ ConvertUtil.cityMap.get(datas[1])+"\n";
	}

}
