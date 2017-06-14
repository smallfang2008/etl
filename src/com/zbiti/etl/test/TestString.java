package com.zbiti.etl.test;

public class TestString {

	public static void main(String[] args) {
		String temp="1~_~2~_~2~_~2~_~2~_~2~_~2~_~2";
		System.out.println(temp.replace("~_~", "\"|\""));
	}
}
