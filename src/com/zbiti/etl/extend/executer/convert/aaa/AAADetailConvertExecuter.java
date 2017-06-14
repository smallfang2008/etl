package com.zbiti.etl.extend.executer.convert.aaa;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.zbiti.etl.extend.executer.convert.IConvertExecuter;
import com.zbiti.etl.extend.executer.convert.nel.SqlMap;

@Service("AAADetailConvertExecuter")
public class AAADetailConvertExecuter extends AbstractAAA implements IConvertExecuter {

	// private String temp = null;
	
	// 用于存储解析后的结果
	private StringBuffer result = new StringBuffer();
	// 定义分隔符(注意分隔符不能为'|'，因为原数据中含有'|')

	// 标识两个特殊PDSN的IP段
	private String pdsnPrefix42 = "115.168.42";
	private String pdsnPrefix74 = "115.168.74";

	// 错误原因查询sql语句
	private String errorSql = "select SRC_ERROR_ID,ERROR_ID from demo.C_DIM_ERROR where ERROR_TYPE_ID = '14'";

	// 预付费和后付费话单标识，默认为后付费HFF
	private String payType = "HFF";

//	private String imsiFlag = ""; // 判断是否有匹配的记录
	private String newFileName = "";
	
	//修改分隔符 by zd at 20130601
	final static String SPIT_SIGN = "	";

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	private String newFileTempPath = "/ossne_data/detail_tranfer_realtime_data/";// 将符合条件的记录写到newFileTempPath路径中
	private int flushCount = 0; // 定义多少次提交
//	private int tempCount = 0;
//	private StringBuffer sb = new StringBuffer(); // 临时存储记录
//	private RandomAccessFile accessFile = null;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public AAADetailConvertExecuter() {
	}

	// 测试用
	public static void main(String[] args) {
//		AAADetailConvertExecuter a = new AAADetailConvertExecuter();
		//a.setDbUrl(dbUrl)
//		a.convert("d:\\8613310128877.txt", "d:\\8613310128877.txt.result");
	}
	
	public String doConvert(String line, String filePathName) throws Exception {
		// 需要在传入的参数末尾加上一个字符，否则会出现解析错误
		line = line + ",";
		result.delete(0, result.length());
//		System.out.println("aa=" + line);
		
		// 按TAB键分隔传入的参数
		String[] columns = line.split("	");
		if (columns.length != 67 && columns.length != 62 && columns.length != 60) {
//			throw new Exception("columns.length error");
			return "";
		}
		// 定义需要进行转换的字段
		String msid = "";
		String naiId = "";
		String pdsnId = "";
		String pcfId = "";
		String pcfRegion = "";
		String baseId = "";
		String serviceOption = "";
		String eventTimestamp = "";
		String errorId = "";
		String dateId = "";
		String subnet = "";
		String bsId = "";

		// 首先录入记录ID，为空
		result.append(SPIT_SIGN);
		if (flushCount == 0) {
			flushCount = 10;
		}

		for (int i = 0; i < columns.length; i++) {
			// System.out.println(i + "-->" + columns[i]);
			String column = columns[i];
			switch (i) {
				case 5:
					// 取MSID_REGIONID
					// System.out.println("MSID===>" + column);
					msid = imsiToregion(columns[i]);
					break;
				case 9:
					// 取NAI_ID
					// System.out.println("NAI===>" + column);
					/**********2012-6-27 huyulei modify start***********/
					// 先匹配vpdn
					if(columns[i].indexOf("vpdn")>=0 || columns[i].indexOf("VPDN")>=0) {
						naiId = "vpdn";
						// 匹配烧号的IPHONE终端
					} else if (columns[i].indexOf("vzw3g.com")>=0) {
						naiId = "iphone";
					} else{
						naiId = this.getValueByKey(SqlMap.SQL_AAA_naiMap, columns[i]);
					}
					// 如果都匹配不到就作为其他处理
					if (naiId == null || naiId.equals("")) {
						naiId = "other";
					}
					/**********2012-6-27 huyulei modify end***********/
					break;
				case 19:
					// 取PDSN_ID
					// System.out.println("PDSNADDRESS===>" + column);
					if (column != null
							&& column.length() >= 10
							&& (column.substring(0, 10).equals(pdsnPrefix42) || column
									.substring(0, 10).equals(pdsnPrefix74))) {
						// 首先取特殊的IP段--115.168.42和115.168.74
						pdsnId = this.getValueByKey(SqlMap.SQL_AAA_pdsnAddressMap,
								column);
						if (pdsnId == null || pdsnId.equals("")) {
							// 如果没有取到则匹配该IP段前缀
							pdsnId = this.getValueByKey(
									SqlMap.SQL_AAA_pdsnPrefixMap, column.substring(
											0, 10));
						}
					} else if (column != null && column.length() >= 10) {
						// 然后进行前缀匹配
						if (pdsnId == null || pdsnId.equals("")) {
							pdsnId = this.getValueByKey(
									SqlMap.SQL_AAA_pdsnPrefixMap, column.substring(
											0, 10));
						}
					}
					// 如果以上匹配完成后依然为空，则赋予其他未关联的ID
					if (pdsnId == null || pdsnId.equals("")) {
						pdsnId = "115168999999";
					}
					break;
				case 20:
					// 取PCF_REGIONID 和 PCF_ID
					// System.out.println("PCF===>" + column);
					if (column.length() >= 9) {
						// 首先匹配PCF对应的地域ID
						pcfRegion = this.getValueByKey(SqlMap.SQL_AAA_pcfRegionMap,
								column.substring(0, 9));
						// 再匹配PCF对应的PCF ID
						pcfId = this.getValueByKey(SqlMap.SQL_AAA_pcfMap, column
								.substring(0, 9));
					}
					// 如果PCF对应的地域ID匹配为空，则计入外省ID
					if (pcfRegion == null || pcfRegion.equals("")) {
						pcfRegion = "86999000";
					}
					// 如果PCFid为空，则计入未关联ID
					if (pcfId == null || pcfId.equals("")) {
						pcfId = "999999999";
					}
					break;
				case 21:
					// 取BASE_ID
					baseId = column;
					// System.out.println("baseId ===> " + baseId);
					break;
				case 23:
					// 取subnet
					subnet = column;
					// if (subnet.length() >= 36) {
					// System.out.println("subnet ===> " +
					// subnet.substring(36).toUpperCase());
					// }
					break;
				case 27:
					// 取service option 33--cdma 59--evdo
					serviceOption = column;
					break;
				case 37:
					// 取EVENT_TIMESTAMP
					try {
	
						eventTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date(Long.parseLong(column) * 1000));
					} catch (Exception e) {
						eventTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date());
					}
					dateId = timeToId(eventTimestamp, "");
					break;
				case 56:
					// 取ERROR_ID-- 会话结束原因ID
					// System.out.println("ERROR===>" + column);
					errorId = errorToId(column, "AAA");
					break;
				case 59:
					// 后付费中Session_id设置为空
					if (this.payType.equals("HFF")) {
						column = "";
					}
					// 预付费中session_id设置为最后一个值
					if (this.payType.equals("YFF")) {
						if(columns.length == 60){
							column = "".equals(column)?"":column.substring(0, column.length() - 1);
						}
					}
					break;
				case 61:
					//ludl edit 2014-12-17 for 0或空置为0表示非ccg用户 ,其他的置为1 表示是ccg用户
					//System.out.println("befor ="+column);
					//column = "".equals(column)?"":column.substring(0, column.length() - 1);
					if("".equals(column)||"0".equals(column)){
						column="0";
					}else{
						column="1";
					}			
					//System.out.println("after ="+column);
					break;
			}
			if(i==59&&columns.length == 60){
				result.append("");
				result.append(SPIT_SIGN);
				result.append(column);
				result.append(SPIT_SIGN);
				result.append("");
				result.append(SPIT_SIGN);
			}else if(i<=61){
				result.append(column);
				result.append(SPIT_SIGN);
			}
		}

		// 写入FLAG字段（预付费话单0和后付费话单1）
		if (this.payType.equals("HFF")) {
			result.append("1");
			result.append(SPIT_SIGN);
		} else {
			result.append("0");
			result.append(SPIT_SIGN);
		}

		// 添加事件时间
		result.append(eventTimestamp == null ? "" : eventTimestamp);
		result.append(SPIT_SIGN);

		// 添加会话结束原因ID
		result.append(errorId == null ? "" : errorId);
		result.append(SPIT_SIGN);

		// 添加PCF对应的接入地ID
		result.append(pcfRegion);
		result.append(SPIT_SIGN);

		// 添加用户IMSI对应归属地ID
		result.append(msid);
		result.append(SPIT_SIGN);

		// 添加时间ID
		result.append(dateId == null ? "" : dateId);
		result.append(SPIT_SIGN);

		// 添加网络接入标识ID
		result.append(naiId == null ? "" : naiId);
		result.append(SPIT_SIGN);

		// System.out.println("serviceOption ===>" + serviceOption);
		// 添加BASE_ID
		if (serviceOption.equals("33")) {
			// 如果是CDMA，则根据sid_ci_hex去匹配对应的BASE_ID
			if (!baseId.equals("") && baseId.length() >= 12) {
				bsId = this.getValueByKey(SqlMap.SQL_AAA_baseMap, baseId
						.substring(0, 4)
						+ baseId.substring(8, 12));
			}
		} else if (serviceOption.equals("59")) {
			// 如果是EVDO，则根据sector_id去匹配对应的BASE_ID
			if (subnet.length() >= 36) {
				bsId = this.getValueByKey(SqlMap.SQL_AAA_subnetMap, subnet
						.substring(35));
			}
			// 如果获取失败，再次根据CDMA的方式获取一次
			if ((bsId == null || bsId.equals(""))
					&& (!baseId.equals("") && baseId.length() >= 12)) {
				bsId = this.getValueByKey(SqlMap.SQL_AAA_baseMap, baseId
						.substring(0, 4)
						+ baseId.substring(8, 12));
			}
		} else if (serviceOption.equals("32876")) {
			// 如果是WLAN，则根据sid_ci_hex去匹配对应的BASE_ID
			if (!baseId.equals("") && baseId.length() >= 12) {
				bsId = this.getValueByKey(SqlMap.SQL_AAA_baseMap, baseId.substring(0, 4) + baseId.substring(8, 12));
			}
		}
		result.append(bsId == null ? "" : bsId);
		result.append(SPIT_SIGN);

		// 添加PCF维度ID
		result.append(pcfId);
		result.append(SPIT_SIGN);

		// 添加PDSN维度ID
		result.append(pdsnId);
		result.append(SPIT_SIGN).append(this.getMachineName())
				.append(SPIT_SIGN);
		result.append("\n");
//		boolean imsiFlag = this.ifMatch(SqlMap.SQL_AAA_newFileImsiMap, columns[5]);
//		boolean mdnFlag = false;
//		if(!imsiFlag){
//			mdnFlag = this.ifMatch(SqlMap.SQL_AAA_newFileMdnMap, columns[4]);
//		}
//		if (imsiFlag || mdnFlag) {
//			tempCount++;
//			sb.append(result.toString());
//			if(tempCount == flushCount){
//				writeString2File(
//						sb.toString(),
//						newFileTempPath + newFileName + "_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()),accessFile);
//				tempCount = 0;
//				sb.delete(0, sb.length());
//			}
//		}
//		System.out.println("bb=" +  result.toString());
		return result.toString();
	}
	public void afterRun(){
//		if(sb.length()>0){
//			try {
//				writeString2File(sb.toString(),newFileTempPath + newFileName + "_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()),accessFile);
//				sb.delete(0, sb.length());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			IOClosable(accessFile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	public void NewFileOutPut() {

	}

	public String getErrorSql() {
		return errorSql;
	}

	public void setErrorSql(String errorSql) {
		this.errorSql = errorSql;
	}

	public void initExt() {

	}

	public String getNewFileTempPath() {
		return newFileTempPath;
	}

	public void setNewFileTempPath(String newFileTempPath) {
		this.newFileTempPath = newFileTempPath;
	}

	public int getFlushCount() {
		return flushCount;
	}

	public void setFlushCount(int flushCount) {
		this.flushCount = flushCount;
	}
}