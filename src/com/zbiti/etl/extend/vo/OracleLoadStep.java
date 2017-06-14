package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

public class OracleLoadStep extends Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5819480142358488929L;
	String serverName;//对应资源名称
	String prepareSql;
	String postSql;
	String tableName;
	String columns;
	String dataSplit;
	String encoding;//入库编码
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getPrepareSql() {
		return prepareSql;
	}
	public void setPrepareSql(String prepareSql) {
		this.prepareSql = prepareSql;
	}
	public String getPostSql() {
		return postSql;
	}
	public void setPostSql(String postSql) {
		this.postSql = postSql;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getDataSplit() {
		return dataSplit;
	}
	public void setDataSplit(String dataSplit) {
		this.dataSplit = dataSplit;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
