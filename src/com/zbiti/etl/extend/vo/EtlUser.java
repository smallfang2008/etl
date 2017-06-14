package com.zbiti.etl.extend.vo;

import java.io.Serializable;

public class EtlUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String username;
	private String password;
}
