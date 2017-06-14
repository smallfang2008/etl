package com.zbiti.common;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	 private static String hexStr =  "0123456789ABCDEF";
	    private static String[] binaryArray =
	        {"0000","0001","0010","0011",
	        "0100","0101","0110","0111",
	        "1000","1001","1010","1011",
	        "1100","1101","1110","1111"};
	/**
	 * 将空对象转成空字符串
	 * @param o
	 * @return
	 */
	public static String objectToStr(Object o){
		return o==null?"":o.toString();
	}
	
	public static String objectToStr2(Object o){
		return o==null||o.toString().equals("null")?"":o.toString();
	}
	
	/**
	 * 处理文件路劲
	 * 如果没有以/结束,则添加/
	 * @param o
	 * @return
	 */
	public static String dealFileDirectory(Object o){
		if(o!=null){
			if(!o.toString().endsWith("/")){
				return o.toString()+"/";
			}else{
				return o.toString();
			}
		}
		return "";
	}
	
	/**
	 * 获取文件名(传入文件路劲)
	 * @param o
	 * @return
	 */
	public static String getFileNameByDirectory(Object o){
		if(o!=null){
			return o.toString().substring(o.toString().lastIndexOf("/")+1);
		}
		return "";
	}
	/**
	 * 字符串编码转换
	 * @param o
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String ChangeCode(String preStr,String fromCode ,String toCode) throws UnsupportedEncodingException{
		if(fromCode==null ||"".equals(fromCode)|| toCode==null ||"".equals(toCode) ||  preStr==null ||"".equals(preStr)){
			return preStr;
		}else{
			return new String(preStr.getBytes(fromCode), toCode);
		}		
	}
	public static String getFilePath(String filename) {
		return System.getProperty("user.dir")+"/"+filename;
	}
	   /**
    *
    * @param bytes
    * @return 将二进制转换为十六进制字符输出,且以两位十六进制先后翻转
    */
   public static String binaryToHexStringTrans(byte[] bytes){
       String result = "";
       String hex = "";
       for(int i=bytes.length-1;i>-1;i--){
           //字节高4位
           hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
           //字节低4位
           hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
           result +=hex+" ";
       }
       String aaa = result.trim().replaceAll(" ","");
       return result.trim();
   }
   /**
   *
   * @param bytes
   * @return 将二进制转换为十六进制字符输出
   */
  public static String binaryToHexString(byte[] bytes){
      String result = "";
      String hex = "";
      for(int i=0;i<bytes.length;i++){
          //字节高4位
          hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
          //字节低4位
          hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
          result +=hex+" ";
      }
      return result.trim();
  }
  

  /**
   *
   * @param bytes
   * @return 将二进制转换为十进制字符输出
   */
  public static String binaryToDecString(byte[] bytes){

      String result = "";
      for(int i=0;i<bytes.length;i++){
          result +=(bytes[i]&0xFF)+" ";
      }
      return result.trim();
  }
  /**
   *
   * @param bytes
   * @return 将二进制转换为十六进制字符输出,且高低位颠倒
   */
  public static String binaryToTBCD(byte[] bytes){
      String result = "";
      String hex = "";
      for(int i=0;i<bytes.length;i++){
          //字节低4位
          hex = String.valueOf(hexStr.charAt(bytes[i]&0x0F));
          //字节高4位
          hex += String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
          result +=hex;
      }
      return result;
  }
   /**
   *
   * @param hexString
   * @return 将十六进制转换为十进制输出
   */
  public static long hexStringToDec(String hexString){

      long result = 0;
      try{
          result = Long.parseLong(hexString,16);
      }catch (Exception e){
      }
       return result;
  }

  /**
   * 将文件路径中的动态格式参数替换成日期
   * 如/dev/${yyyyMMdd}/偏移-1即当前日期-1，当前日期是20170314，偏移之后为/dev/20170313/
   * @param filename
   * @param timeoffset
   * @return
   */
  public static String dealFilePath(String filepath,int timeoffset){
		Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
        Matcher matcher = pattern.matcher(filepath);
        while(matcher.find()){
        	filepath=filepath.replace("${"+matcher.group()+"}", new SimpleDateFormat(matcher.group().trim()).format(new Date(System.currentTimeMillis()+timeoffset*24*60*60*1000)));
        }
        return filepath;
	}
}
