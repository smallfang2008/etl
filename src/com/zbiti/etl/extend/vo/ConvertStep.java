package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

/**
 * 转换步骤
 * @author yhp
 *
 */
public class ConvertStep extends Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8792562702325514051L;

	String superClass;
	String charset;
	public String getSuperClass() {
		return superClass;
	}
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
