package com.zbiti.etl.extend.executer.convert.lte4g;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zbiti.common.FileUtil;
import com.zbiti.common.StringUtil;
 

/**
 * Created by IntelliJ IDEA.
 * User: wuhf
 * Date: 13-11-13
 * Time: 下午2:10a
 * To change this template use File | Settings | File Templates.
 */
public class ConvertForASN extends AbstractBinaryForASN {

    //type: 0-INTEGER  1-TBCD-STRING  2-OCTET STRING 3-CHOICE IP 4-SEQUENCE OC  5-IA5String   6-Boolean  7-Time 8-SEQUENCE OF ChangeOfServiceConditions(34,12)UNIVERSAL  9-userLocationInformation 10-HexToDec
    //数组说明：字段名,TAG，类型（type）,值


    private String ftpType = "";
    private String sourceIp = "";


	private String[][] getAS34() {
        return new String[][]{{"RatingGroup", "1", "0", ""}, {"chargingRuleBaseName", "2", "5", ""}, {" localSequenceNumber", "4", "0", ""}, {"timeOfFirstUsage", "5", "7", ""}
                , {"timeOfLastUsage", "6", "7", ""}, {"timeUsage", "7", "0", ""}, {"serviceConditionChange", "8", "0", ""}, {"qoSInformationNeg", "9", "2", ""}
                , {"sgsn-Address", "10", "3", ""}, {"sGSNPLMNIdentifier", "11", "1", ""}, {"datavolumeFBCUplink", "12", "0", ""}, {"datavolumeFBCDownlink", "13", "0", ""}
                , {"timeOfReport", "14", "7", ""}, {"rATType", "15", "6", ""}, {"userLocationInformation", "20", "2", ",,,,"}
        };
    }

    private String[][] getAS12() {
        return new String[][]{{"qosNegotiated", "2", "2", ""}, {"dataVolumeGPRSUplink", "3", "0", ""}, {" dataVolumeGPRSDownlink", "4", "0", ""}, {"changeCondition", "5", "0", ""}
                , {"changeTime", "6", "7", ""}
        };
    }

    private String[][] getAS79() {
        return new String[][]{{"recordType", "0", "0", ""}, {"servedIMSI", "3", "1", ""}, {"iMSIunauthenticatedFlag", "42", "", ""}, {"servedIMEISV", "29", "1", ""}
                , {"served3gpp2MEID", "40", "2", ""}, {"pGWAddress", "4", "3", ""}, {"chargingID", "5", "0", ""}, {"pDNConnectionChargingID", "41", "0", ""}
                , {"servingNodeAddress", "6", "3", ""}, {"servingNodeType", "35", "4", ""}, {"pGWPLMNIdentifier", "37", "1", ""}, {"accessPointNameNI", "7", "5", ""}
                , {"pdpPDNType", "8", "2", ""}, {"servedPDPPDNAddress", "9", "3", ""}, {"servedPDPPDNAddressExt", "45", "3", ""}, {"dynamicAddressFlag", "11", "6", ""}
                , {"dynamicAddressFlagExt", "47", "6", ""}, {"listOfServiceData", "34", "8", ",,,,,,,,,,,,,,,,,,,"}, {"recordOpeningTime", "13", "7", ""}, {"mSTimeZone", "31", "2", ""}
                , {"duration", "14", "0", ""}, {"causeForRecClosing", "15", "0", ""}, {"diagnostics", "16", "4", ""}, {"recordSequenceNumber", "17", "0", ""}
                , {"nodeID", "18", "5", ""}, {"recordExtensions", "19", "", ""}, {"localSequenceNumber", "20", "0", ""}, {"apnSelectionMode", "21", "0", ""}
                , {"servedMSISDN", "22", "1", ""}, {"userLocationInformation", "32", "9", ",,,,,,,"}, {"userCSGInformation", "43", "4", ""}, {"threeGPP2UserLocationInformation", "44", "2", ""}
                , {"chargingCharacteristics", "23", "2", ""}, {"chChSelectionMode", "24", "2", ""}, {"iMSsignalingContext", "25", "2", ""}, {"servingNodePLMNIdentifier", "27", "1", ""}
                , {"rATType", "30", "0", ""}, {"pSFurnishChargingInformation", "28", "4", ""}, {"startTime", "38", "7", ""}, {"stopTime", "39", "7", ""}
                , {"listOfTrafficVolumes", "12", "8", ",,,,,"}, {"cAMELChargingInformation", "33", "2", ""}, {"externalChargingID", "26", "2", ""}, {"servedMNNAI", "36", "6", ""}
        };
    }

    /**
     * 传入最上层ASN.1对象，会根据传入解码类型组来将本次需要解开对象进行解析，并形成值，所有关联以TAG进行,本方法主要针对传入ASN.1对象的其下层对象列表进行
     *
     * @param asn 待解码最上层ASN.1对象
     * @param ias 解码类型组 数组说明：字段名,TAG，类型（type）,值
     * @return
     * @throws Exception
     */  
    public String[][] getASNStringListByASNOBJ(ASN asn, String[][] ias) throws Exception {
        Map<String, ASN> tagASN = new HashMap<String, ASN>();
        List<ASN> asnlist = asn.getAsnList();
        for (int i = 0; i < asnlist.size(); i++) {
            tagASN.put(asnlist.get(i).getTag() + "", asnlist.get(i));
        }
        for (int i = 0; i < ias.length; i++) {
            ASN asnObj = tagASN.get(ias[i][1]);
 
            if (!ias[i][2].equals("") && asnObj != null) {
                try {
                	ias[i][3] = this.getConvertResultValue(Integer.parseInt(ias[i][2]), asnObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ias;
    }

    /**
     * 根据传入的解码类型将对象解开，所有支持解码类型详见本类最上面type说明 ,本方法主要针对单个对象解码
     *
     * @param type   解码类型
     * @param asnObj asn.1对象
     * @return
     * @throws Exception
     */
    public String getConvertResultValue(int type, ASN asnObj) throws Exception {
        switch (type) {
            case 0:  //Integer
                return "" + StringUtil.hexStringToDec(StringUtil.binaryToHexString(asnObj.getContent()).replaceAll(" ", ""));
            case 1: //TBCD-STRING
                return "" + StringUtil.binaryToTBCD(asnObj.getContent()).replaceAll("F", "");
            case 2: //OCTET STRING
                return StringUtil.binaryToHexString(asnObj.getContent());
            case 3: //CHOICE ip
                return getIP(asnObj);
            case 4: //CHOICE OC
                return getOC(asnObj);
            case 5: //IA5String
                return new String(asnObj.getContent(), "GBK");
            case 6: //Boolean 按规范中描述，只要出现些字段则付值TRUE
                return "1";
            case 7: //OC -TIME
                return getTime(StringUtil.binaryToHexString(asnObj.getContent()));
            case 8: //解压下层明细-针对34,12
                return getSequenceList(asnObj);
            case 9:
                return getTAIAndECGI(asnObj);
        }
        return "";
    }

    public String getTAIAndECGI(ASN asn) {
        if ((asn.getContent()[0] & 0xFF) != 0x18)
            return ",,,,,,,";
        //return ""; //ludl edit for bug
        byte[] tai = new byte[2];
        byte[] ecgi = new byte[4];
        
        System.arraycopy(asn.getContent(), 4, tai, 0, 2);
        System.arraycopy(asn.getContent(), 9, ecgi, 0, 4);
        String stai = StringUtil.binaryToHexString(tai).replaceAll(" ", "");
        String secgi = StringUtil.binaryToHexString(ecgi).replaceAll(" ", "");
        String asnContent = StringUtil.binaryToHexString(asn.getContent()).replaceAll(" ", "");
       
        //byte[] temp=new byte[10];
        //解析userLocationInformation时同时截取赋值7个字段
        String tempStr="";
        tempStr=","+asnContent.substring(2, 8)+",";
        
        tempStr=tempStr+asnContent.substring(8, 12)+",";
        tempStr=tempStr+Integer.parseInt(asnContent.substring(8, 12),16)+",";
        
        tempStr=tempStr+asnContent.substring(8, 10)+",";
        
        tempStr=tempStr+asnContent.substring(12, 18)+",";
        
        
        tempStr=tempStr+Integer.parseInt(asnContent.substring(19, 24),16)+",";
        
        tempStr=tempStr+Integer.parseInt(asnContent.substring(24, 26),16);
        return stai + "_" + secgi + tempStr;
    }

    /**
     * @param asn
     * @return
     * @throws Exception
     */
    public String getSequenceList(ASN asn) throws Exception {
        String[][] sequenceCoditions = null;
        if (asn.getTag() == 34) {
        	//listOfServiceData中ratingGroup中的每个字段都需要入库，对每个ratingGroup做拆分，
        	//例如： ratingGroup:3532702900中   timeOfFirstUsage、timeOfLastUsage、  userLocationInformation值不一样所以就是两条记录
            sequenceCoditions = this.getAS34();
            String return34=null;
            for(int i=0;i<asn.getAsnList().size();i++){
            	String[][] result34 = this.getASNStringListByASNOBJ(asn.getAsnList().get(i), sequenceCoditions);
            	if(result34[14][3] != null && !"".equals(result34[14][3]) ){
            		//System.out.println("AS34:userLocationInformation值为："+result34[14][3]);
//            		result34[14][3]:18 64 F0 11 69 2F 64 F0 11 06 99 6A 33
            		String[] areaInfos = result34[14][3].split(" ");
            		if(areaInfos.length > 1){
                		String city_id = areaInfos[4];
                		String eNodeBID = (areaInfos[9]+areaInfos[10]+areaInfos[11]).substring(1,6);
                		String cellId = areaInfos[12];
                		String eNodeIdTen = String.valueOf(StringUtil.hexStringToDec(eNodeBID));
                		String eci = eNodeBID+cellId;
                		//把地市、基站、小区 三条信息拼接起来
                		String areaInfo = city_id+","+eNodeBID+","+eNodeIdTen+","+cellId+","+eci;
//                		System.out.println(areaInfo);
                		result34[14][3] = areaInfo;
//                		System.out.println("result34[14][3]:"+result34[14][3]);
            		}
            	}
            	
            	//结果数据与前面的结果，进行合并处理，多条以 ; 进 行分割 	
            	String temp=convertChildList2String(result34);
            	
            	if(return34==null){
            		return34=temp;
            	}else{
            		return34=return34+";"+temp;
            	}
            }
//            System.out.println(return34);
            return return34;
            
        }else if (asn.getTag() == 12) {
        	// 1.listOfTrafficVolumes中对所有的dataVolumeGPRSUplink、dataVolumeGPRSDownlink做累加，
        	//changeTime、userLocationInformation中保留最新的时间和地点信息。
            sequenceCoditions = this.getAS12();
            String[][] result12=null;
            String newTime = "";//最新时间 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for(int i=0;i<asn.getAsnList().size();i++){
            	String[][] temp   = this.getASNStringListByASNOBJ(asn.getAsnList().get(i), sequenceCoditions);
            	//结果数据与前面的结果，进行累加处理
            	if(result12==null){
            		result12=temp;
            		newTime = temp[4][3];
            	}else{
            		/* {"qosNegotiated", "2", "2", ""}, {"dataVolumeGPRSUplink", "3", "0", ""}, {" dataVolumeGPRSDownlink", "4", "0", ""}, {"changeCondition", "5", "0", ""}
                    , {"changeTime", "6", "7", ""}*/
            		//dataVolumeGPRSUplink 上联累加
            		if(temp[1][3]!=null&&!"".equalsIgnoreCase(temp[1][3])){
            			result12[1][3]=String.valueOf(Long.valueOf(result12[1][3])+Long.valueOf(temp[1][3]));
            		}
            		//dataVolumeGPRSDownlink 下联累加
            		if(temp[2][3]!=null&&!"".equalsIgnoreCase(temp[2][3])){
            			result12[2][3]=String.valueOf(Long.valueOf(result12[2][3])+Long.valueOf(temp[2][3]));
            		}
            		//获取最新时间
            		if(sdf.parse(temp[4][3]).getTime() - sdf.parse(newTime).getTime() > 0){
            			newTime = temp[4][3];
                		result12[4][3] = newTime;
                		result12[0][3]= temp[0][3];//最新的地址
            		}
            	}
            }
            //返回累加后的值
            if(result12!=null){
            	return convertChildList2String(result12);
            }else{
            	return "";
            }
        }else{
        	String[][] result = this.getASNStringListByASNOBJ(asn.getAsnList().get(0), sequenceCoditions);
        	return convertChildList2String(result);
        }        
    }

    /**
     * @param str 13 10 29 14 19 26 2B 08 00
     * @return 2013-10-29 14:19:26
     */
    public String getTime(String str) {
        String[] strs = str.split(" ");
        if (strs.length < 5) {
            return "";
        }
        return "20" + strs[0] + "-" + strs[1] + "-" + strs[2] + " " + strs[3] + ":" + strs[4] + ":" + strs[5];
    }

    /**
     * 根据传入asn数据获取下层asn.1对象转为IP值输出，目前对TAG79有效
     * @param asn
     * @return
     * @throws Exception
     */
    public String getIP(ASN asn) throws Exception {
        String result = "";

        if (asn.getAsnList().get(0).isStructured()) { //如果其子还为结构体则需要再次调用此方法
            for (int i = 0; i < asn.getAsnList().size(); i++) {
                result += getIP(asn.getAsnList().get(i)) + " ";
            }
            return result.trim();
        }
        for (int i = 0; i < asn.getAsnList().size(); i++) {
            if (asn.getAsnList().get(i).getTag() < 2) {
            	/*IPv4v6:
            	  * 36.14.0.232.241.1.37.94.0.0.0.0.0.0.0.0
            		servedPDPPDNAddress = 01A1:20B3:0:0:0:0:0:0
            		A9 14 A0 12 81 10 
            		01 A1 20 B3 00 00 00 00 00 00 00 00 00 00 00 00
            	    servedPDPPDNAddressExt = 10.80.0.3:
            		BF 2D 08 A0 06 80 04 0A 50 00 03
            		*/
//            	System.out.println("asn.getLength():"+asn.getLength());
            	if(asn.getLength()==18){
            		//长度是18说明是IPV6,则进行转换
            		result += getIpv6(asn.getAsnList().get(i).getContent());
            	}else{
            		result += StringUtil.binaryToDecString(asn.getAsnList().get(i).getContent()).replaceAll(" ", ".");
            	}
                
            } else {           
            	result += new String(asn.getAsnList().get(i).getContent(), "GBK");
            }
        }
//        System.out.println("ip result:"+result);
        return result;
    }
    /**
     * 获取ipv6地址
     * //24 0E 00 E8 F1 01 4A A7 00 00 00 00 00 00 00 00
     * @return
     */
    private String getIpv6(byte[] ipv6byte){
    	String tempv6=StringUtil.binaryToHexString(ipv6byte);
    	int index=tempv6.indexOf(" ");
    	int tag=1;
    	while(index>0){
    		if(tag%2==1){
    			tempv6=tempv6.replaceFirst(" ", "");
    		}else{
    			tempv6=tempv6.replaceFirst(" ", ":");
    		}
    		index=tempv6.indexOf(" ");
    		tag=tag+1;
    	}
    	tempv6=tempv6.replace("0000", "0");
    	return tempv6;
    }
    
    /**
     * 将asn的下层asn.1对象直接十六进制输出，不做改任何转义
     *
     * @param asn
     * @return
     */
    public String getOC(ASN asn) {
        String result = "";
        for (int i = 0; i < asn.getAsnList().size(); i++) {
            result += StringUtil.binaryToDecString(asn.getAsnList().get(i).getContent());
        }
        return result;
    }

    /**
     * 将传入的str数组转为要写入文件的string,不要修改！！！
     * ludl 注释  最新方法看上面的
     * @param str
     * @return
     * @throws Exception
     */
    private String convertChildList2String(String[][] str) throws Exception {
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {       	
            if (str[i][2].equals("8")) {
                sb.append(str[i][3]);
            } else {
                sb.append(str[i][3] + ",");
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
    	String src = "D:\\lte\\gzAP11080000926476_JSNJCG02_-_00066409.20150226_-_1740+0800.ctc.txt";
		String dest = "D:\\lte\\result.txt";
        ConvertForASN d = new ConvertForASN(); 
       // d.convertDirectory(src, dest); 
        d.convert(src, dest); 
    }

    /**
     * @param directory 文件目录
     * @throws Exception 
     */
    public void convertDirectory(String directory, String destFile) throws Exception {
        File file = new File(directory);
        System.out.println("directory:"+directory);
        System.out.println("destFile:"+destFile);
        File[] files = file.listFiles();
        System.out.println("files:"+files);
        for (File cFile : files) { 
//            if (cFile.getAbsoluteFile().toString().endsWith(".txt")) {
                this.convert(cFile.getAbsoluteFile().toString(), destFile);
                System.out.println(cFile.getAbsoluteFile());
//            }

        }
    }
    @Override
    public String convertASN2String(ASN asn) {
        //异常数据处理
    	String result="";
        if (asn == null || asn.getTag()!=79) {
            return "";
        }
        List<ASN> asnlist = asn.getAsnList();
        if (asnlist == null || asnlist.size() == 0 || asn.getTag() != 79) {
            return "";
        }
        try {
            String[][] str = this.getASNStringListByASNOBJ(asn, this.getAS79());
            //ludl add 
            if(str.length==44){
            	String templistOfServiceData=str[17][3];
            	String[] list=templistOfServiceData.split(";");
            	for(int i=0;i<list.length;i++){
            		str[17][3]=list[i];
            		//String tempString =this.convertChildList2String(str);
            		/*int length=tempString.length()-tempString.replace(",", "").length();
                    if(length!=73){
                    	this.writeErrorLine(fileName,"error-line-data:"+result);
                    }else{
                    	result=result+ tempString + ftpType + "," + sourceIp + "\n";
                    }*/
            		result=result+ this.convertChildList2String(str) + ftpType + "," + sourceIp + "\n";
            	}
            }else{
            	//String tempString =this.convertChildList2String(str);
        		//int length=tempString.length()-tempString.replace(",", "").length();
        		 /*if(length!=73){
                 	System.out.println(result);
                 }else{
                	 result=tempString + ftpType + "," + sourceIp + "\n";
                 } */
        		 result=this.convertChildList2String(str) + ftpType + "," + sourceIp + "\n";
            }
            
            
            return result;
        } catch (Exception e) {
            return "";
        }

    }
   /**
    * 将解析错误的行写入的错误文件中
    * @param fileName
    * @param string
    */
  private void writeErrorLine(String fileName, String result) {
	  System.out.println("error-line-data:"+result);
	  try{
		  FileUtil.appendDataToFile(fileName+".error", result);
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	}

	/*  @Override
    public String convertASN2String(ASN asn) {
        //异常数据处理
        if (asn == null || asn.getTag()!=79) {
            return "";
        }
        List<ASN> asnlist = asn.getAsnList();
        if (asnlist == null || asnlist.size() == 0 || asn.getTag() != 79) {
            return "";
        }
        try {
            String[][] str = this.getASNStringListByASNOBJ(asn, this.getAS79());
            
            return this.convertChildList2String(str) + ftpType + "," + sourceIp + "\n";
        } catch (Exception e) {
            return "";
        }

    }
*/
    @Override
    public void init() {

    }


    public String getFtpType() {
        return ftpType;
    }

    public void setFtpType(String ftpType) {
        this.ftpType = ftpType;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
