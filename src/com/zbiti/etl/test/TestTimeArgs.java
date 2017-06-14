package com.zbiti.etl.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zbiti.common.StringUtil;

public class TestTimeArgs {

	public static void main(String[] args) {
		//文件名
//		String filename="xx${yyyyMMdd}.txt";
//		Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
//        Matcher matcher = pattern.matcher(filename);
//        int timeoffset=-1;//时间偏移量
//        while(matcher.find()){
//        	filename=filename.replace("${"+matcher.group()+"}", new SimpleDateFormat(matcher.group()).format(new Date(System.currentTimeMillis()+timeoffset*24*60*60*1000)));
//        }
//        System.out.println(filename);
		
		System.out.println(StringUtil.dealFilePath("/dev/${yyyyMMdd }/", -1));
	}
}
