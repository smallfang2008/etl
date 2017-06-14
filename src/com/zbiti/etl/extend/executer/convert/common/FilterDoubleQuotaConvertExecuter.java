package com.zbiti.etl.extend.executer.convert.common;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("FilterDoubleQuotaConvertExecuter")
public class FilterDoubleQuotaConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		String[] dataArray=data.split(",");
		StringBuffer sb=new StringBuffer();
		for(String dataColumn:dataArray){
			sb.append(filterQuota(dataColumn)+ ConvertUtil.SPIT_SIGN );
		}
		return sb.toString()+"\n";
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
}
