package com.zbiti.common.modelframe;

import java.io.IOException;
import java.util.Properties;

import com.zbiti.common.jedis.JedisConf;

public class ModelframeConf {

	private String ndc1JdbcDriver;
	private String ndc1JdbcUrl;
	private String ndc1JdbcUsername;
	private String ndc1JdbcPassword;
	private String osswykJdbcDriver;
	private String osswykJdbcUrl;
	private String osswykJdbcUsername;
	private String osswykJdbcPassword;
	private String iqJdbcDriver;
	private String iqJdbcUrl;
	private String iqJdbcUsername;
	private String iqJdbcPassword;
	public String getNdc1JdbcDriver() {
		return ndc1JdbcDriver;
	}
	public void setNdc1JdbcDriver(String ndc1JdbcDriver) {
		this.ndc1JdbcDriver = ndc1JdbcDriver;
	}
	public String getNdc1JdbcUrl() {
		return ndc1JdbcUrl;
	}
	public void setNdc1JdbcUrl(String ndc1JdbcUrl) {
		this.ndc1JdbcUrl = ndc1JdbcUrl;
	}
	public String getNdc1JdbcUsername() {
		return ndc1JdbcUsername;
	}
	public void setNdc1JdbcUsername(String ndc1JdbcUsername) {
		this.ndc1JdbcUsername = ndc1JdbcUsername;
	}
	public String getNdc1JdbcPassword() {
		return ndc1JdbcPassword;
	}
	public void setNdc1JdbcPassword(String ndc1JdbcPassword) {
		this.ndc1JdbcPassword = ndc1JdbcPassword;
	}
	public String getOsswykJdbcDriver() {
		return osswykJdbcDriver;
	}
	public void setOsswykJdbcDriver(String osswykJdbcDriver) {
		this.osswykJdbcDriver = osswykJdbcDriver;
	}
	public String getOsswykJdbcUrl() {
		return osswykJdbcUrl;
	}
	public void setOsswykJdbcUrl(String osswykJdbcUrl) {
		this.osswykJdbcUrl = osswykJdbcUrl;
	}
	public String getOsswykJdbcUsername() {
		return osswykJdbcUsername;
	}
	public void setOsswykJdbcUsername(String osswykJdbcUsername) {
		this.osswykJdbcUsername = osswykJdbcUsername;
	}
	public String getOsswykJdbcPassword() {
		return osswykJdbcPassword;
	}
	public void setOsswykJdbcPassword(String osswykJdbcPassword) {
		this.osswykJdbcPassword = osswykJdbcPassword;
	}
	public String getIqJdbcDriver() {
		return iqJdbcDriver;
	}
	public void setIqJdbcDriver(String iqJdbcDriver) {
		this.iqJdbcDriver = iqJdbcDriver;
	}
	public String getIqJdbcUrl() {
		return iqJdbcUrl;
	}
	public void setIqJdbcUrl(String iqJdbcUrl) {
		this.iqJdbcUrl = iqJdbcUrl;
	}
	public String getIqJdbcUsername() {
		return iqJdbcUsername;
	}
	public void setIqJdbcUsername(String iqJdbcUsername) {
		this.iqJdbcUsername = iqJdbcUsername;
	}
	public String getIqJdbcPassword() {
		return iqJdbcPassword;
	}
	public void setIqJdbcPassword(String iqJdbcPassword) {
		this.iqJdbcPassword = iqJdbcPassword;
	}
	
	public ModelframeConf(){
		readConf();
	}
	
	void readConf(){
		Properties prop = new Properties();
		try {
			prop.load(JedisConf.class.getResourceAsStream("/modelframe.properties"));
			ndc1JdbcDriver=prop.get("ndc1.jdbc.driverClassName").toString();
			ndc1JdbcUrl=prop.get("ndc1.jdbc.url").toString();
			ndc1JdbcUsername=prop.get("ndc1.jdbc.username").toString();
			ndc1JdbcPassword=prop.get("ndc1.jdbc.password").toString();
			
			osswykJdbcDriver=prop.get("osswyk.jdbc.driverClassName").toString();
			osswykJdbcUrl=prop.get("osswyk.jdbc.url").toString();
			osswykJdbcUsername=prop.get("osswyk.jdbc.username").toString();
			osswykJdbcPassword=prop.get("osswyk.jdbc.password").toString();
			
			iqJdbcDriver=prop.get("iq.jdbc.driverClassName").toString();
			iqJdbcUrl=prop.get("iq.jdbc.url").toString();
			iqJdbcUsername=prop.get("iq.jdbc.username").toString();
			iqJdbcPassword=prop.get("iq.jdbc.password").toString();
		} catch (IOException e) {
			System.out.println("/threadpool.properties load failure!");
			e.printStackTrace();
		}
	}
}
