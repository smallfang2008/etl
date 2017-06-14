package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

public class ShellStep extends Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8485336424365661105L;
	String resourceName;//对应资源名称
	String shellCommands;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getShellCommands() {
		return shellCommands;
	}
	public void setShellCommands(String shellCommands) {
		this.shellCommands = shellCommands;
	}
	
}
