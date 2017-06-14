package com.zbiti.etl.test;

import com.zbiti.etl.extend.vo.SourceFile;

public class TestObject {

	public static void main(String[] args) {
		SourceFile sourceFile=new SourceFile();
		sourceFile.setMaxFile("1");
		String maxFile=sourceFile.getMaxFile();
		sourceFile.setMaxFile("2");
		System.out.println(maxFile);
	}
}
