package com.zbiti.etl.extend.executer.convert.provinceRing;

import org.springframework.stereotype.Service;

import com.zbiti.common.StringUtil;
import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.executer.convert.IConvertExecuter;

@Service("ProvinceRingSysHisConvert")
public class ProvinceRingSysHisConvert   implements IConvertExecuter{
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if(StringUtil.objectToStr(data).equals("")||data.startsWith("\"局站名称\""))
			return "";
		String[] dataArray=data.split("\",\"");
		StringBuffer sb=new StringBuffer();
		int i=0;
		for(String dataColumn:dataArray){
			if(i!=0){
				sb.append(ConvertUtil.SPIT_SIGN);
			}
			sb.append(filterQuota(dataColumn));
			i++;
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

	public static void main(String[] args) {
		try {
			System.out.println(new ProvinceRingSysHisConvert().doConvert("\"宁_L_浦口区信息工程大学文园13栋\",\"蓄电池组(1#云泰300)\",\"总电压\",\"53.7\",\"V\",\"2015-05-18 08:31:34\"", ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
