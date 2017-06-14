package com.zbiti.etl.extend.vo;

import java.io.Serializable;
import java.util.Date;

import com.zbiti.etl.core.vo.Step;

/**
 * 转换记录
 * @author yhp
 *
 */
public class ConvertRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4693986816584744626L;
	String recordId;
	Step Step;
	String filePath;
	long convertBytes;
	Date insertTime;
	Date modifyTime;
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public Step getStep() {
		return Step;
	}
	public void setStep(Step step) {
		Step = step;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getConvertBytes() {
		return convertBytes;
	}
	public void setConvertBytes(long convertBytes) {
		this.convertBytes = convertBytes;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
