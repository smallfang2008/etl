package com.zbiti.etl.extend.executer.convert.c_psd_wlan_detail;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("CPsdWlanDetailConvertExecuter")
public class CPsdWlanDetailConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		return data.replaceAll("~_~", "\"|\"")+"\n";
	}

}
