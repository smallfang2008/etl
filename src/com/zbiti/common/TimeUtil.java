package com.zbiti.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TimeUtil {
	
	static Map<String,SimpleDateFormat> formatMap=new HashMap<String, SimpleDateFormat>();

	public static String getFomartSysDate(String format) {
		if (format == null || "".equals(format)) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static String getFomartSysDate(String format,Date date) {
		if (format == null || "".equals(format)) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date getFomartSysDate(String format,String date) {
		if (format == null || "".equals(format)) {
			return new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date returnDate = null;
		try {
			returnDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
	}
	
	public static String parseNumberToString(String t) {
		String tempendtime = "";
		if(t.length() == 8){
			tempendtime = t.substring(0,4) + "-" + t.substring(4,6) + "-" + t.substring(6); 
		} else if(t.length() == 10)  {
			tempendtime = t.substring(0,4) + "-" + t.substring(4,6) + "-" + t.substring(6,8) + " " + t.substring(8);
		}else if(t.length() == 14)  {
			tempendtime = t.substring(0,4) + "-" + t.substring(4,6) + "-" + t.substring(6,8) + " " + t.substring(8,10)+":"
							+t.substring(10,12) + ":"+t.substring(12,14);
		}
		return tempendtime;
	}
	
	/**
	 * 转换long to date time yyyyMMddHHmmss 24 hours
	 * @param stringtime
	 * @return
	 */
	public static String getNowDateTime() {
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String yeardatatime = formatter.format(calendar.getTime());
		return yeardatatime;
	}
	
	/**
	 *	
	 * @return
	 */
	public static String getNowDateTime(String format) {
		SimpleDateFormat formatter = formatMap.get(format);
		if(formatter==null){
			formatter=new SimpleDateFormat(format);
			formatMap.put(format, formatter);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String yeardatatime = formatter.format(calendar.getTime());
		return yeardatatime;
	}
	
	/**
	 * 日期格式为string类型
	 * @param formate
	 * @param beforDays
	 * @return
	 */
	public static String dateFormat(Date tempDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(tempDate);
	}
	/**
	 * 转换long to date time yyyyMMddHHmmssSSS 24 hours
	 * 时分秒毫秒
	 * @param stringtime
	 * @return
	 */
	public static String getNowDateTimeMillss() {
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String yeardatatime = formatter.format(calendar.getTime());
		return yeardatatime;
	}	
	
	  //根据指定长度生成字母和数字的随机数
    //0~9的ASCII为48~57
    //A~Z的ASCII为65~90
    //a~z的ASCII为97~122
    public static String  getRandom(int length)
    {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
            case 0:
                 data=randdata.nextInt(10);//仅仅会生成0~9
                 sb.append(data);
                break;
            case 1:
                data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                sb.append((char)data);
                break;
            case 2:
                data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                sb.append((char)data);
                break;
            }
        } 
        return sb.toString();
    }
    
    /**
     * 将格式为yyyy-MM-dd HH:mm:ss的日期格式化成5分钟颗粒的yyyyMMddHHmm  
     * @param dateStr
     * @return
     */
    public static String getTime5MByDateStr(String dateStr,String fromFormat,String toFormat){
    	Calendar c=Calendar.getInstance();
    	try {
			c.setTime(new SimpleDateFormat(fromFormat).parse(dateStr));
		} catch (ParseException e) {
			System.out.println("日期格式化失败，需是yyyy-MM-dd HH:mm:ss格式");
		}
		if(c.get(Calendar.SECOND)>0)
			c.add(Calendar.MINUTE, 1);
		int minite=c.get(Calendar.MINUTE);
		if(minite==0){
			return new SimpleDateFormat(toFormat).format(c.getTime());
		}
		minite=get5MByMinite(minite);
		if(minite%5!=0){
			System.out.println("获取5分钟失败");
		}
		if(minite==0){
			c.add(Calendar.HOUR_OF_DAY, 1);
		}
		c.set(Calendar.MINUTE, minite);
    	return new SimpleDateFormat(toFormat).format(c.getTime());
    }
    
    public static int get5MByMinite(int minite){
    	if(minite<=5&&minite>0)
    		return 5;
    	else if(minite<=10&&minite>5)
    		return 10;
    	else if(minite<=15&&minite>10)
    		return 15;
    	else if(minite<=20&&minite>15)
    		return 20;
    	else if(minite<=25&&minite>20)
    		return 25;
    	else if(minite<=30&&minite>25)
    		return 30;
    	else if(minite<=35&&minite>30)
    		return 35;
    	else if(minite<=40&&minite>35)
    		return 40;
    	else if(minite<=45&&minite>40)
    		return 45;
    	else if(minite<=50&&minite>45)
    		return 50;
    	else if(minite<=55&&minite>50)
    		return 55;
    	else if(minite<=59&&minite>55)
    		return 0;
    	return minite;
    }
}