package com.zbiti.etl.extend.executer.convert.nel;

import java.util.HashMap;
import java.util.Map;

public class SqlMap {
	private static Map<String,String> sqlMap = new HashMap<String,String>();
	public final static String SQL_GW_DIM_NUM_COMPANY="1";//短信网关
	public final static String SQL_GW_C_DIM_NET="2";//短信网关流向
	public final static String SQL_REGION_ID="4";//短信流向
	public final static String SQL_NET_MESSAGE_ID="5";
	public final static String SQL_ERROR_CODE_HWMO="6";//短信错误代码hwmo
	public final static String SQL_ERROR_CODE_HWMT="7";//短信错误代码hwmt
	public final static String SQL_ERROR_CODE_ZTE="8";//短信错误代码zte
	public final static String SQL_IvpnActiveDetailMap="9";
	public final static String SQL_IvpnActiveDetailCityMap="10";
	public final static String SQL_CITY_FORMAT="11";//城市转换代码
	public final static String SQL_DATE_FORMAT="12";//时间格式转换代码
	public final static String SQL_SMS_ZTE_XW_ERRORCODE="13";//欣网中兴短信错误码转换
	public final static String SQL_SMS_ZTE_XW_CITY="14";//欣网中兴短信地市转换
	public final static String SQL_CITY_AREA_NAME_TO_CODE="15";//地市区县名称转编码
	public final static String SQL_CITY_AREA_NAME_TO_UPCODE="16";//地市区县名称转父级编码
	public final static String SQL_DEVICE_ID_TO_CITY_ID="17";//设备资源表设备ID查询属地CODE
	public final static String SQL_AREA_CODE_TO_AREA_NAME="18";//属地CODE查询属地名称
	public final static String SQL_DEVICE_ID_TO_LOOPBACKIP="19";//设备资源表设备ID查询LOOPBACK_IP
	public final static String SQL_ZD_CITY_NAMR_TO_CODE="20";//综调本地网名称转换
	public final static String SQL_DPI_MSID="21";//IMSI对应归属地ID匹配
	public final static String SQL_DPI_IMSI_CITY="22";//REDIS_DPI的根据IMSI查用户数地信息
	public final static String SQL_DPI_MDN_CITY="23";//REDIS_DPI的根据MDN查用户数地信息

	public final static String SQL_AAA_pcfMap="A1";//PCF匹配map
	public final static String SQL_AAA_pcfRegionMap="A2";//PCF对应的地市匹配map
	public final static String SQL_AAA_pdsnPrefixMap="A3";//PDSN前缀匹配map
	public final static String SQL_AAA_pdsnAddressMap="A4";//PDSN地址匹配map
	public final static String SQL_AAA_naiMap="A5";//网络接入标识匹配map
	public final static String SQL_AAA_baseMap="A6";//基站(BS字段)匹配map
	public final static String SQL_AAA_subnetMap="A7";//基站(subnet字段)匹配map
	public final static String SQL_AAA_yfflogMap="A8";//AAA预付费日志匹配map
	public final static String SQL_AAA_newFileImsiMap="A9";
	public final static String SQL_AAA_newFileMdnMap="A10";
	public final static String SQL_DPI_PCF="A11";//PCF对应的接入地ID匹配
	
	public final static String SQL_DPI_CODE="DPI";
	//Rating-Group	业务类型	业务类型标识  20150508 ludianlong add
	public final static String C_DIM_LTE_RATINGGROUP="C_DIM_LTE_RATINGGROUP";
	static{
		sqlMap.put(SQL_GW_DIM_NUM_COMPANY, "select add_num, company from demo.dim_num_company");
		sqlMap.put(SQL_GW_C_DIM_NET, "select net_name, net_id from demo.c_dim_net");
		sqlMap.put(SQL_REGION_ID, "select h_id,city_id from demo.dim_qh");
		sqlMap.put(SQL_NET_MESSAGE_ID, "select account,net_id||'_'||message_flag new_net_message from DEMO.DIM_NET");
		sqlMap.put(SQL_ERROR_CODE_HWMO, "select ERROR_CODE,ERROR_CODE_ID from demo.dim_get_errorcode where device_com='1' and sm_type='HMO'");
		sqlMap.put(SQL_ERROR_CODE_HWMT, "select ERROR_CODE,ERROR_CODE_ID from demo.dim_get_errorcode where device_com='1' and sm_type='HMT'");
		sqlMap.put(SQL_ERROR_CODE_ZTE, "select ERROR_CODE,ERROR_CODE_ID from demo.dim_get_errorcode where device_com='2' ");
		sqlMap.put(SQL_IvpnActiveDetailMap,"select h_id,city_id from DEMO.DIM_QH");
		sqlMap.put(SQL_IvpnActiveDetailCityMap,"select distinct qh_id,city_id from DEMO.DIM_QH");
		sqlMap.put(SQL_CITY_FORMAT,"select data_code,data_name from dw.COM_DICTIONARY_DATA where DIC_CODE='city_format' and state='1'");
		sqlMap.put(SQL_DATE_FORMAT,"select data_code,data_name from dw.COM_DICTIONARY_DATA where DIC_CODE='date_format' and state='1'");
		sqlMap.put(SQL_SMS_ZTE_XW_ERRORCODE,"select data_code,data_name from dw.COM_DICTIONARY_DATA where DIC_CODE='sms_zte_xw_errorcode' and state='1'");
		sqlMap.put(SQL_SMS_ZTE_XW_CITY,"select data_code,data_name from dw.COM_DICTIONARY_DATA where DIC_CODE='sms_zte_xw_city' and state='1'");
		sqlMap.put(SQL_CITY_AREA_NAME_TO_CODE,"select area_name,area_code from dw.area");
		sqlMap.put(SQL_CITY_AREA_NAME_TO_UPCODE,"select area_name,up_area_code from dw.area");
		sqlMap.put(SQL_DEVICE_ID_TO_CITY_ID,"select device_id,city_id from dw.iposs_deviceresource");
		sqlMap.put(SQL_AREA_CODE_TO_AREA_NAME,"select area_code,area_name from dw.area");
		sqlMap.put(SQL_DEVICE_ID_TO_LOOPBACKIP,"select device_id,loopback_ip from dw.iposs_deviceresource");
		sqlMap.put(SQL_ZD_CITY_NAMR_TO_CODE,"select data_name,data_code from dw.COM_DICTIONARY_DATA where dic_code='zd_city'");
		sqlMap.put(SQL_DPI_MSID,"select IMSI , REGION_ID from DEMO.C_PSD_D_IMSI where province ='江苏省'");
		sqlMap.put(SQL_DPI_IMSI_CITY,"select IMSI,NUM from demo.C_PSD_D_IMSI,dw.BSS_CITY where city=city_name");
		sqlMap.put(SQL_DPI_MDN_CITY,"select h_id,city  from demo.DIM_QH ");
		
		
		
		
		sqlMap.put(SQL_AAA_pcfMap, "select PCF_IP,PCF_ID from demo.C_PSD_D_PCF");
		sqlMap.put(SQL_AAA_pcfRegionMap, "select PCF_IP,PCF_REGIONID from demo.C_PSD_D_PCF");
		sqlMap.put(SQL_AAA_pdsnPrefixMap, "select PDSN_PREFIX,PDSN_ID from demo.C_PSD_D_PDSN where PDSN_ID < 11551680000");
		sqlMap.put(SQL_AAA_pdsnAddressMap, "select PDSN_ADDRESS,PDSN_ID from demo.C_PSD_D_PDSN WHERE PDSN_ID BETWEEN 11551680000 AND 115168999999");
		/**********2012-6-27 huyulei modify start***********/
		sqlMap.put(SQL_AAA_naiMap, "select NAI_CODE, NAI_DESC from demo.C_PSD_D_NAI");
		/**********2012-6-27 huyulei modify end***********/
		sqlMap.put(SQL_AAA_baseMap, "select a.sid_ci_hex, b.base_id from demo.C_PSD_MID_BS a,demo.C_PSD_D_BSINFO b where a.bs_name = b.bs_name and b.bs_flag=1");
		/**********2012-6-27 huyulei modify start***********/
		sqlMap.put(SQL_AAA_subnetMap, "select a.sector_id, b.base_id from demo.C_PSD_MID_BS a,demo.C_PSD_D_BSINFO b where a.bs_name = b.bs_name AND a.sector_id <> '' and b.bs_flag=1");
		/**********2012-6-27 huyulei modify end***********/
		sqlMap.put(SQL_AAA_yfflogMap, "select data_code,data_name from dw.COM_DICTIONARY_DATA where DIC_CODE='aaa_yfflog_error' and state='1'");
		sqlMap.put(SQL_AAA_newFileImsiMap, "select field2,field1 from dw.detail_controldata");
		sqlMap.put(SQL_AAA_newFileMdnMap, "select field1,field2 from dw.detail_controldata");
		sqlMap.put(SQL_DPI_PCF, "select PCF_IP,PCF_REGIONID from demo.C_PSD_D_PCF");
		/**********2013-12-13 yujl modify end***********/
		sqlMap.put(SQL_DPI_CODE, "select distinct code_num,code_info from DW.C_DIM_DPI_CODE");	
		//××××××××××××××× 2015-05-08 ludianlong add for Rating-Group	业务类型	业务类型标识 数据字段查询//////
		sqlMap.put(C_DIM_LTE_RATINGGROUP, "select ratinggroup,ratinggroup_name from dw.C_DIM_LTE_RATINGGROUP");
		
	}
	
	public static Map<String,String> getSqlMap(){
		return sqlMap;
	}
}
