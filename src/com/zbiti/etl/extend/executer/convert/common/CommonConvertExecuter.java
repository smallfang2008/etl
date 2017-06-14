package com.zbiti.etl.extend.executer.convert.common;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("CommonConvertExecuter")
public class CommonConvertExecuter implements IConvertExecuter{

	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		return data+"\n";
	}

}
