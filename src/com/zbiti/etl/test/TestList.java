package com.zbiti.etl.test;

import java.util.HashSet;
import java.util.Set;

public class TestList {

	public static void main(String[] args) {
		Set<String> fileList=new HashSet<String>();
		fileList.add("file1");
		fileList.add("file1");
		fileList.add("file2");
		fileList.add("file1");
		System.out.println(fileList);
	}
}
