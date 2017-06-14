package com.zbiti.etl.extend.vo;

import org.springframework.web.multipart.MultipartFile;

import com.zbiti.etl.core.vo.Step;

public class KettleStep extends Step{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1425168820237657282L;

	String kettleFileName;
	String kettleType;
	byte[] kettleFileStream;
	MultipartFile kettleFile;
	public String getKettleType() {
		return kettleType;
	}
	public void setKettleType(String kettleType) {
		this.kettleType = kettleType;
	}
	public String getKettleFileName() {
		return kettleFileName;
	}
	public void setKettleFileName(String kettleFileName) {
		this.kettleFileName = kettleFileName;
	}
	public byte[] getKettleFileStream() {
		return kettleFileStream;
	}
	public void setKettleFileStream(byte[] kettleFileStream) {
		this.kettleFileStream = kettleFileStream;
	}
	public MultipartFile getKettleFile() {
		return kettleFile;
	}
	public void setKettleFile(MultipartFile kettleFile) {
		this.kettleFile = kettleFile;
	}
	
}
