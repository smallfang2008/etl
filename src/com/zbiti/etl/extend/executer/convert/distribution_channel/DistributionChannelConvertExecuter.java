package com.zbiti.etl.extend.executer.convert.distribution_channel;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
@Service("DistributionChannelConvertExecuter")
public class DistributionChannelConvertExecuter implements IConvertExecuter {
	
	@Override
	public String doConvert(String data, String filePathName) throws Exception {
		if (null == data || "".equals(data))
			return null;
		//销售渠道数据文件字段信息
		if (data.startsWith("SN|CHANNEL_ID|") || data.startsWith("SN|AGENT_ID|")
				|| data.startsWith("SN|STAFF_ID|") || data.startsWith("SN|STORE_ID|")) 
			return null;
		return data.replace("null", "") + "\n";
	}
}
