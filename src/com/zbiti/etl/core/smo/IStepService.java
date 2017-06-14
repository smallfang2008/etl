package com.zbiti.etl.core.smo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepType;
import com.zbiti.etl.extend.vo.ConvertRecord;
import com.zbiti.etl.extend.vo.ConvertStep;
import com.zbiti.etl.extend.vo.DownloadLog;
import com.zbiti.etl.extend.vo.FileUpStep;
import com.zbiti.etl.extend.vo.FinderStep;
import com.zbiti.etl.extend.vo.IQLoadStep;
import com.zbiti.etl.extend.vo.KettleStep;
import com.zbiti.etl.extend.vo.OracleExpStep;
import com.zbiti.etl.extend.vo.OracleLoadStep;
import com.zbiti.etl.extend.vo.ShellStep;
import com.zbiti.etl.extend.vo.SourceFile;
import com.zbiti.etl.extend.vo.SourceFileDir;
import com.zbiti.etl.extend.vo.SqlStep;


public interface IStepService {

	/**
	 * 获取节点下的步骤
	 * @param nodeCode
	 * @return
	 */
	List<Step> listStepByNode(String nodeCode);
	List<Step> listStepByNodeCache(String nodeCode);
	/**
	 * 获取集群下的步骤
	 * @param cluster
	 * @return
	 */
	List<Step> listStepByCluster(String cluster);
	/**
	 * 获取步骤的前置步骤
	 * @param step
	 * @return
	 */
	List<Step> listPreStep(Step step);
	/**
	 * 根据场景Id获取第一步
	 * @param sceneId
	 * @return
	 */
	List<Step> listFirstStepBySceneId(String sceneId);
	/**
	 * 获取步骤（包含后续步骤）
	 * @param stepId
	 * @return
	 */
	Step getStepContainsNextById(String stepId);
	/**
	 * 获取场景下的所有步骤
	 * @param sceneId
	 * @return
	 */
	public List<Step> listStepBySceneId(String sceneId);
	public List<Step> listStepBySceneIdCache(String sceneId);
	
	public Scene getByIdScene(String sceneId);
	
	public String getStepSeq();
	
	public Step getStep(String stepId);
	
	public List<StepType> findstepType();
	
	public void saveStep(Step step);
	
	public void updateStep(Step step);
	
	public void deleteStep(String stepCode);
	
	public void editAllStep(List<Step> list);
	
	
	public List<Map<String,Object>> listStepNameBySceneId(String sceneId);
	public List<Map<String,Object>> listnodeall();
	public List<Map<String,Object>> listserverall();

	public List<Map<String, Object>> getResurceList(String typeId);

	/*
	 * 文件上传
	 */
	public FileUpStep getFileUpStep(String stepId);
	
	public void saveFileUpStep(FileUpStep fileUpStep,Step step);

	public void updateFileUpStep(FileUpStep fileUpStep,Step step);
	
	public void delFileUpStep(String stepId);

	
	/*
	 * 装换
	 */
	public void updateConvertStep(ConvertStep convertStep,Step step);

	public void saveConvertStep(ConvertStep convertStep,Step step);

	public ConvertStep getConvertStep(String stepId);
	public ConvertStep getConvertStepCache(String stepId);
	
	public void delConvertStep(String stepId);
	

	/*
	 * Oracle加载
	 */
	public OracleLoadStep getOracleLoadStep(String stepId);
	public OracleLoadStep getOracleLoadStepCache(String stepId);

	public void updateOracleLoadStep(OracleLoadStep oracleLoadStep,Step step);

	public void saveOracleLoadStep(OracleLoadStep oracleLoadStep,Step step);
	
	public void delOracleLoadStep(String stepId);
	
	
	
	/*
	 * sybase加载
	 */
	public IQLoadStep getIQLoadStep(String stepId);
	public IQLoadStep getIQLoadStepCache(String stepId);

	public void updateIQLoadStep(IQLoadStep iqLoadStep,Step step);

	public void saveIQLoadStep(IQLoadStep iqLoadStep,Step step);
	
	public void delIQLoadStep(String stepId);
	
	
	/*
	 * 发现
	 */
	public List<SourceFileDir> findSourceFileDir(String stepId);
	
	public FinderStep getFinderStep(String stepId);
	
	public void savefx(List<SourceFileDir> list,FinderStep finderStep,Step step);
	
	public void updatefx(List<SourceFileDir> list,FinderStep finderStep,Step step,String ids);
	
	public void delfx(String stepId);
	public void clearScanRecord(String sourceFileDirId);
	public void clearConvertRecord(String sourceFileDirId);
	
	/*
	 * kettle步骤
	 */

	public KettleStep getKettleStep(String stepId);
	public void saveKettleStep(KettleStep kettleStep,Step step);
	public void delKettleStep(String stepId);
	public void updateKettleStep(Step step, KettleStep kettleStep);
	
	SqlStep getSqlStepById(String stepId);

	/**
	 * 根据步骤id获取发现步骤
	 * @param stepId
	 * @return
	 */
	public FinderStep getFinderStepByStepId(String stepId);
	/**
	 * 根据步骤id获取源文件路径
	 * @param stepId
	 * @return
	 */
	public List<SourceFileDir> listSourceFileDirByStepId(String stepId);
	/**
	 * 根据源文件路径id和源文件目录获取
	 * @param sourceFileDirId
	 * @param sourceFileDir
	 * @return
	 */
	public SourceFile getSourceFileByDirIdAndDir(String sourceFileDirId,String sourceFileDir);
	public void saveSourceFile(SourceFile sourceFile);
	/**
	 * 根据步骤id获取文件上传步骤
	 * @param stepId
	 * @return
	 */
	public FileUpStep getFileUpStepByStepId(String stepId);
	
	/**
	 * 获取转换记录。fileName包含文件路径
	 */
	ConvertRecord getConvertRecord(@Param("stepId") String stepId,@Param("fileName") String fileName);
	void  saveConvertRecord(ConvertRecord convertRecord);
	
	void saveDownloadLog(DownloadLog downloadLog);
	
	/**
	 * sql
	 */
	public void saveSqlStep(SqlStep sqlStep,Step step);
	public SqlStep getSqlStep(String stepId);
	public void updateSqlStep(SqlStep sqlStep,Step step);
	public void delSqlStep(String stepId);
	/**
	 * shell step
	 */
	public void saveShellStep(ShellStep shellStep,Step step);
	public ShellStep getShellStep(String stepId);
	public ShellStep getShellStepCache(String stepId);//后端调度用
	public void updateShellStep(ShellStep shellStep,Step step);
	public void delShellStep(String stepId);

	/**
	 * oracle exp step
	 */
	public void saveOracleExpStep(OracleExpStep oracleExpStep,Step step);
	public OracleExpStep getOracleExpStep(String stepId);
	public OracleExpStep getOracleExpStepCache(String stepId);//后端调度用
	public void updateOracleExpStep(OracleExpStep oracleExpStep,Step step);
	public void delOracleExpStep(String stepId);
}
