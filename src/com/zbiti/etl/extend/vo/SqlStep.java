package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

public class SqlStep extends Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6384401117925037675L;
	String dbName;//对应资源名称
	String sqls;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getSqls() {
		return sqls;
	}
	public void setSqls(String sqls) {
		this.sqls = sqls;
	}
	
}
