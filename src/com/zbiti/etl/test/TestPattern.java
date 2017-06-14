package com.zbiti.etl.test;

public class TestPattern {

	public static void main(String[] args) {

//		String filePattern="*.gz";
//
//		filePattern = filePattern.replaceAll("\\.", "[.]");
//		filePattern = filePattern.replaceAll("\\*", ".*");
//		String filename="xx.gz.tmp";
//		System.out.println(filename.matches(filePattern));
		String filePattern="192.168.[2|3].(16|17|18)";

		filePattern = filePattern.replaceAll("\\.", "[.]");
		filePattern = filePattern.replaceAll("\\*", ".*");
		String filename="192.168.2.18";
		System.out.println(filename.matches(filePattern));
	}
}
