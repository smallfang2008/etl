package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

public class FinderStep extends Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6185683783353463254L;
	String sourceType;//是否追加
	String compressPattern;//压缩格式 0:无压缩;1gz 2tar.gz 3zip 4jar 5....
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getCompressPattern() {
		return compressPattern;
	}
	public void setCompressPattern(String compressPattern) {
		this.compressPattern = compressPattern;
	}
}
