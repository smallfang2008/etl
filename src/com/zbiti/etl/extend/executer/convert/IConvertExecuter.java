package com.zbiti.etl.extend.executer.convert;

public interface IConvertExecuter {
	//需要转换的数据，需要转换的文件路径+文件名
	/**
	 * 转换对应的分隔符请根据 ConvertUtil.SPIT_SIGN，来确定
	 */
	public String  doConvert(String data,String filePathName) throws Exception; 
}
