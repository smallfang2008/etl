package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.util.List;

/**	
 * 步骤
 * @author yhp
 *
 */
public class Step implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1981481215120760839L;
	String stepId;
//	String stepTypeId;
	StepType stepType;
	String runPositionType;//1表示集群 2表示节点
//	String serverClusterId;
	ServerCluster serverCluster;
//	String nodeCode;
	Node node;
//	String sceneId;
	Scene scene;
	String step;//步骤序号
	String previousStep;//前置步骤序号，可多个，逗号隔开
	String stepName;
	String createOp;
	String modifyOp;
	String createDate;
	String modifyDate;
	int threadNum;
	int memMin;
	int memMax;
	int runType;//0进程 1线程 先不支持线程，看效率后续拓展
	int isWaitPre;//是否等待前置任务执行完成 1是 0否
	List<Step> nextStep;
	
	String runPosition;
	
	public String getRunPosition() {
		return runPosition;
	}
	public void setRunPosition(String runPosition) {
		this.runPosition = runPosition;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public StepType getStepType() {
		return stepType;
	}
	public void setStepType(StepType stepType) {
		this.stepType = stepType;
	}
	public String getRunPositionType() {
		return runPositionType;
	}
	public void setRunPositionType(String runPositionType) {
		this.runPositionType = runPositionType;
	}
	public ServerCluster getServerCluster() {
		return serverCluster;
	}
	public void setServerCluster(ServerCluster serverCluster) {
		this.serverCluster = serverCluster;
	}
	
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getPreviousStep() {
		return previousStep;
	}
	public void setPreviousStep(String previousStep) {
		this.previousStep = previousStep;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getCreateOp() {
		return createOp;
	}
	public void setCreateOp(String createOp) {
		this.createOp = createOp;
	}
	public String getModifyOp() {
		return modifyOp;
	}
	public void setModifyOp(String modifyOp) {
		this.modifyOp = modifyOp;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public int getMemMin() {
		return memMin;
	}
	public void setMemMin(int memMin) {
		this.memMin = memMin;
	}
	public int getMemMax() {
		return memMax;
	}
	public void setMemMax(int memMax) {
		this.memMax = memMax;
	}
	public int getRunType() {
		return runType;
	}
	public void setRunType(int runType) {
		this.runType = runType;
	}
	public int getIsWaitPre() {
		return isWaitPre;
	}
	public void setIsWaitPre(int isWaitPre) {
		this.isWaitPre = isWaitPre;
	}
	public List<Step> getNextStep() {
		return nextStep;
	}
	public void setNextStep(List<Step> nextStep) {
		this.nextStep = nextStep;
	}
	
}
