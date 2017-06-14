package com.zbiti.etl.runtime;

import java.util.List;
import java.util.Map;

import sun.security.util.PropertyExpander.ExpandException;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.CommandShow;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;

public interface ICommandService {

	/**
	 * 获取运行历史指令
	 * @return
	 */
	List<Command> listHisCommands() throws Exception;
	/**
	 * 正在运行异常的到异常节点
	 */
	void runningExcp2Error() throws Exception;
	/**
	 * 获取正在运行的定时指令
	 * @return
	 */
	List<Command> listRunningTimer() throws Exception;
	/**
	 * 获取等待的定时指令
	 * @return
	 */
	List<Command> listWaitTimer() throws Exception;
//	Command createCmdByStep(String taskSeries,Step step,Map<String,Object> param);
	/**
	 * 获取指令ID
	 */
	String getCommandId();
	/**
	 * 创建等待节点
	 * @param taskSeries
	 * @param step
	 * @param param
	 * @throws Exception
	 */
	void createNodeForWait(String taskSeries,String preCmdId,Step step,Map<String,Object> param) throws Exception;
	/**
	 * 判断节点是否存在
	 * @param path
	 * @return
	 */
	boolean isNodeExists(String path) throws Exception;
	/**
	 * 获取子节点
	 * @param path
	 * @return
	 */
	List<String> getChildren(String path) throws Exception;
	/**
	 * 移除正在运行的定时
	 * @param cmd
	 */
	void removeRunningTimer(Command cmd) throws Exception;
	/**
	 * 获取等待的指令
	 * @param nodeCode
	 * @param step
	 * @return
	 * @throws Exception
	 */
	Command getWaitedCommand(String nodeCode,Step step) throws Exception;
	/**
	 * 根据指令路径获取指令
	 * @param path
	 * @return
	 */
	Command getCommandByPath(String path) throws Exception;
	/**
	 * 运行任务前创建正在执行指令
	 * @param cmd
	 * @throws ExpandException
	 */
	void createNodeForStart(Command cmd) throws Exception;
	/**
	 * 任务结束后删除临时指令
	 * @param cmd
	 * @throws Exception
	 */
	void deleteNodeForEnd(Command cmd) throws Exception;
	
	boolean isTaskInsOver(Step step,Command cmd);
	void createWaitTimer(Scene scene);
	/**
	 * 停止定时
	 * @param scene
	 */
	void stopTimer(String sceneId);
	
	String getMasterNode();
	
	public PageQueryResult<CommandShow> selectTaskDoingListPage(List<Step> stepList,Map<String, String> param);
	public PageQueryResult<CommandShow> selectTaskWaitListPage(List<Step> stepList,Map<String, String> param);
	public PageQueryResult<CommandShow> selectTaskErrorListPage(List<Step> stepList,Map<String, String> param);
	
	public void stopStepTask(String path);
	
	public void deleteNode(String path);
	
	public void restartErrorCommand(String command) throws Exception;
}
