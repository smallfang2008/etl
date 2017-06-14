package com.zbiti.etl.core.smo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbiti.common.memory.DataToMemory;
import com.zbiti.etl.core.dao.NodeDao;
import com.zbiti.etl.core.dao.SceneDao;
import com.zbiti.etl.core.dao.ServerClusterDao;
import com.zbiti.etl.core.dao.StepDao;
import com.zbiti.etl.core.dao.StepTypeDao;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.ServerCluster;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepType;
import com.zbiti.etl.extend.dao.ConvertRecordDao;
import com.zbiti.etl.extend.dao.ConvertStepDao;
import com.zbiti.etl.extend.dao.DownloadLogDao;
import com.zbiti.etl.extend.dao.FileUpStepDao;
import com.zbiti.etl.extend.dao.FinderStepDao;
import com.zbiti.etl.extend.dao.IQLoadStepDao;
import com.zbiti.etl.extend.dao.KettleStepDao;
import com.zbiti.etl.extend.dao.OracleExpStepDao;
import com.zbiti.etl.extend.dao.OracleLoadStepDao;
import com.zbiti.etl.extend.dao.ShellStepDao;
import com.zbiti.etl.extend.dao.SourceFileDao;
import com.zbiti.etl.extend.dao.SourceFileDirDao;
import com.zbiti.etl.extend.dao.SqlStepDao;
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

@Service
public class StepServiceImpl implements IStepService{

	@Resource
	StepDao stepDao;
	
	//j
	@Resource
	private StepTypeDao stepTypeDao;
	
	@Resource
	private NodeDao nodeDao;
	
	@Resource
	private ServerClusterDao clusterDao;
	
	@Resource
	private FileUpStepDao fileUpStepDao; 
	
	@Resource
	private ConvertStepDao convertStepDao;
	
	@Resource
	private OracleLoadStepDao oracleLoadStepDao;
	
	@Resource
	private IQLoadStepDao iqLoadStepDao;
	
	@Resource
	private SceneDao sceneDao;
	
	@Resource
	private FinderStepDao finderStepDao;
	
	@Resource
	private SourceFileDirDao sourceFileDirDao;
	
	@Resource
	private SourceFileDao sourceFileDao;
	
	@Resource
	private SqlStepDao sqlStepDao;
	
	@Resource
	private ConvertRecordDao convertRecordDao;
	
	@Resource
	private DownloadLogDao downloadLogDao;
	@Resource
	private KettleStepDao kettleStepDao;
	@Resource
	private ShellStepDao shellStepDao;
	@Resource
	private OracleExpStepDao oracleExpStepDao;
	
	
	@Override
	public List<Step> listStepByNode(String nodeCode) {
		return stepDao.listStepByNode(nodeCode);
	}
	@Override
	public List<Step> listStepByNodeCache(String nodeCode) {
		Object o = DataToMemory.readData("Step.ListByNode", nodeCode);
		if(o==null){
			List<Step> listByNode=stepDao.listStepByNode(nodeCode);
			DataToMemory.write("Step.ListByNode", nodeCode, listByNode);
			return listByNode;
		}
		return (List<Step>)o;
	}
	@Override
	public List<Step> listStepByCluster(String cluster) {
		Object o = DataToMemory.readData("Step.ListByCluster", cluster);
		if(o==null){
			List<Step> listByCluster=stepDao.listStepByCluster(cluster);
			DataToMemory.write("Step.ListByCluster", cluster, listByCluster);
			return listByCluster;
		}
		return (List<Step>)o;
	}
	@Override
	public List<Step> listPreStep(Step step) {
		Object o = DataToMemory.readData("Step.ListPreStep", step.getStepId());
		if(o==null){
			List<Step> listByCluster=stepDao.listPreStep(step);
			DataToMemory.write("Step.ListPreStep", step.getStepId(), listByCluster);
			return listByCluster;
		}
		return (List<Step>)o;
	}

	@Override
	public List<Step> listFirstStepBySceneId(String sceneId) {
		Object o = DataToMemory.readData("Step.FirstSteps", sceneId);
		if(o==null){
			List<Step> firstSteps=stepDao.listFirstStepBySceneId(sceneId);;
			DataToMemory.write("Step.FirstSteps", sceneId, firstSteps);
			return firstSteps;
		}
		return (List<Step>)o;
	}
	@Override
	public List<Step> listStepBySceneId(String sceneId) {
		return stepDao.listStepBySceneId(sceneId);
	}

	@Override
	public List<Step> listStepBySceneIdCache(String sceneId) {
		Object o = DataToMemory.readData("Step.ListByScene", sceneId);
		if(o==null){
			List<Step> listByScene=stepDao.listStepBySceneId(sceneId);
			DataToMemory.write("Step.ListByScene", sceneId, listByScene);
			return listByScene;
		}
		return (List<Step>)o;
	}

	@Override
	public Step getStepContainsNextById(String stepId) {
		Object o = DataToMemory.readData("Step", stepId);
		Step step=null;
		if (o == null) {
			step=stepDao.getStep(stepId);
			// 数据缓存
			DataToMemory.write("Step", stepId, step); 
		} else {
			step=(Step)o;
		}
		Object o1=DataToMemory.readData("Step.ListNextStep", stepId);
		System.out.println(o1);
		List<Step> nextStep=null;
		if(o1==null){
			nextStep=stepDao.listNextSteps(step);
			DataToMemory.write("Step.ListNextStep", stepId, nextStep); 
		}else{
			nextStep=(List<Step>)o1;
		}
		step.setNextStep(nextStep);
		return step;
	}
	
	@Override
	public void saveStep(Step step) {
		stepDao.saveStep(step);
//		DataToMemory.write("Step", step.getStepId(), step); 
	}

	@Override
	public void updateStep(Step step) {
		stepDao.updateStep(step);
//		DataToMemory.write("Step", step.getStepId(), step); 
	}

	@Override
	public void deleteStep(String stepCode) {
		stepDao.deleteStep(stepCode);
//		DataToMemory.delete("Step", stepCode);
	}

	@Override
	public List<StepType> findstepType() {
		return stepTypeDao.findstepType();
	}

	@Override
	public List<Map<String,Object>> listStepNameBySceneId(String sceneId) {
		return stepDao.listStepNameBySceneId(sceneId);
	}

	@Override
	public List<Map<String, Object>> listnodeall() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Node> listnode = nodeDao.selectNodeList(null);
		for (Node node : listnode) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("text", node.getNodeCode());
			m.put("value", node.getNodeCode());
			list.add(m);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> listserverall() {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<ServerCluster> listserver = clusterDao.queryServerClusterList(null);
		for (ServerCluster sc : listserver) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("text", sc.getServerClusterName());
			m.put("value", sc.getServerClusterId());
			list.add(m);
		}
		return list;
	}

	@Override
	public String getStepSeq() {
		String seq = "";
		return stepDao.getStepSeq(seq);
	}

	@Override
	public Step getStep(String stepId) {
//		Object o = DataToMemory.readData("Step", stepId);
//		if (o == null) {
			Step step=stepDao.getStep(stepId);
			// 数据缓存
//			DataToMemory.write("Step", stepId, step); 
			return step;
//		} else {
//			return (Step) o;
//		}
	}

	@Override
	public List<Map<String, Object>> getResurceList(String typeId) {
		return stepDao.getResurceList(typeId);
	}

	@Override
	public void saveFileUpStep(FileUpStep fileUpStep,Step step) {
		stepDao.saveStep(step);
		fileUpStepDao.save(fileUpStep);
	}

	@Override
	public void updateFileUpStep(FileUpStep fileUpStep,Step step) {
		stepDao.updateStep(step);
		fileUpStepDao.update(fileUpStep);
	}

	@Override
	public FileUpStep getFileUpStep(String stepId) {
//		Object o = DataToMemory.readData("FileUpStep", stepId);
//		if (o == null) {

			FileUpStep fileUpStep=fileUpStepDao.getById(stepId);
			// 数据缓存
//			DataToMemory.write("FileUpStep", stepId, fileUpStep); 
			return fileUpStep;
//		} else {
//			return (FileUpStep) o;
//		}
	}

	@Override
	public void updateConvertStep(ConvertStep convertStep,Step step) {
		stepDao.updateStep(step);
		convertStepDao.update(convertStep);
	}

	@Override
	public void saveConvertStep(ConvertStep convertStep,Step step) {
		stepDao.saveStep(step);
		convertStepDao.save(convertStep);
	}

	@Override
	public ConvertStep getConvertStep(String stepId) {
		return convertStepDao.getById(stepId);
	}
	@Override
	public ConvertStep getConvertStepCache(String stepId) {
		Object o = DataToMemory.readData("ConvertStep", stepId);
		if (o == null) {

			ConvertStep convertStep=convertStepDao.getById(stepId);
			// 数据缓存
			DataToMemory.write("ConvertStep", stepId, convertStep); 
			return convertStep;
		} else {
			return (ConvertStep) o;
		}
	}

	@Override
	public OracleLoadStep getOracleLoadStep(String stepId) {
		return oracleLoadStepDao.getById(stepId);
	}
	@Override
	public OracleLoadStep getOracleLoadStepCache(String stepId) {
		Object o = DataToMemory.readData("OracleLoadStep", stepId);
		if (o == null) {

			OracleLoadStep OracleLoadStep=oracleLoadStepDao.getById(stepId);
			// 数据缓存
			DataToMemory.write("IQLoadStep", stepId, OracleLoadStep); 
			return OracleLoadStep;
		} else {
			return (OracleLoadStep) o;
		}
		
	}

	@Override
	public void updateOracleLoadStep(OracleLoadStep oracleLoadStep,Step step) {
		stepDao.updateStep(step);
		oracleLoadStepDao.update(oracleLoadStep);
	}

	@Override
	public void saveOracleLoadStep(OracleLoadStep oracleLoadStep,Step step) {
		stepDao.saveStep(step);
		oracleLoadStepDao.save(oracleLoadStep);
	}

	@Override
	public IQLoadStep getIQLoadStep(String stepId) {
		return iqLoadStepDao.getById(stepId);
	}
	@Override
	public IQLoadStep getIQLoadStepCache(String stepId) {
		
		Object o = DataToMemory.readData("IQLoadStep", stepId);
		if (o == null) {

			IQLoadStep IQLoadStep=iqLoadStepDao.getById(stepId);
			// 数据缓存
			DataToMemory.write("IQLoadStep", stepId, IQLoadStep); 
			return IQLoadStep;
		} else {
			return (IQLoadStep) o;
		}
	}

	@Override
	public void updateIQLoadStep(IQLoadStep iqLoadStep,Step step) {
		stepDao.updateStep(step);
		iqLoadStepDao.update(iqLoadStep);
	}

	@Override
	public void saveIQLoadStep(IQLoadStep iqLoadStep,Step step) {
		stepDao.saveStep(step);
		iqLoadStepDao.save(iqLoadStep);
	}

	@Override
	public Scene getByIdScene(String sceneId) {
		return sceneDao.getSceneById(sceneId);
	}
	
	@Override
	public void delFileUpStep(String stepId) {
		fileUpStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	@Override
	public void delIQLoadStep(String stepId) {
		iqLoadStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	@Override
	public void delOracleLoadStep(String stepId) {
		oracleLoadStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	@Override
	public void delConvertStep(String stepId) {
		convertStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	
	@Override
	public SqlStep getSqlStepById(String stepId) {
		Object o = DataToMemory.readData("SqlStep", stepId);
		if (o == null) {

			SqlStep sqlStep=sqlStepDao.getSqlStepByStepId(stepId);
			// 数据缓存
			DataToMemory.write("SqlStep", stepId, sqlStep); 
			return sqlStep;
		} else {
			return (SqlStep) o;
		}
	}
	@Override
	public FinderStep getFinderStepByStepId(String stepId) {
		Object o = DataToMemory.readData("FinderStep", stepId);
		if (o == null) {

			FinderStep finderStep=finderStepDao.getFinderStepByStepId(stepId);
			// 数据缓存
			DataToMemory.write("FinderStep", stepId, finderStep); 
			return finderStep;
		} else {
			return (FinderStep) o;
		}
	}
	@Override
	public List<SourceFileDir> listSourceFileDirByStepId(String stepId) {
		Object o = DataToMemory.readData("SourceFileDir.ListByStep", stepId);
		if(o==null){
			List<SourceFileDir> sourceFileDirList=sourceFileDirDao.listSourceFileDirByStepId(stepId);
			DataToMemory.write("SourceFileDir.ListByStep", stepId, sourceFileDirList); 
			return sourceFileDirList;
		}
		return (List<SourceFileDir>)o;
	}
	@Override
	public void editAllStep(List<Step> list) {
		for (Step step : list) {
			stepDao.updateStep(step);
		}
	}
	@Override
	public SourceFile getSourceFileByDirIdAndDir(String sourceFileDirId,
			String sourceFileDir) {
		return sourceFileDao.getSourceFileByDirIdAndDir(sourceFileDirId, sourceFileDir);
	}
	@Override
	public void saveSourceFile(SourceFile sourceFile) {
		if(sourceFile==null)
			return;
		if(sourceFile.getSourceFileId()==null||"".equals(sourceFile.getSourceFileId())){
			sourceFileDao.saveSourceFile(sourceFile);
		}else{
			sourceFileDao.updateSourceFile(sourceFile);
		}
	}
	@Override
	public List<SourceFileDir> findSourceFileDir(String stepId) {
		return sourceFileDirDao.listSourceFileDirByStepId(stepId);
	}
	@Override
	public FinderStep getFinderStep(String stepId) {
//		Object o = DataToMemory.readData("FinderStep", stepId);
//		if (o == null) {

			FinderStep finderStep=finderStepDao.getFinderStepByStepId(stepId);
			// 数据缓存
//			DataToMemory.write("FinderStep", stepId, finderStep); 
			return finderStep;
//		} else {
//			return (FinderStep) o;
//		}
	}
	@Override
	public void savefx(List<SourceFileDir> list, FinderStep finderStep, Step step) {
		stepDao.saveStep(step);
		finderStepDao.save(finderStep);
		for (SourceFileDir sourceFileDir : list) {
			sourceFileDir.setStep(step);
			sourceFileDirDao.save(sourceFileDir);
		}
//		DataToMemory.write("SqlStep", step.getStepId(), step); 
//		DataToMemory.write("FinderStep", step.getStepId(), finderStep); 
//		DataToMemory.write("SourceFileDirList", step.getStepId(), list); 
	}
	@Override
	public void updatefx(List<SourceFileDir> list, FinderStep finderStep, Step step, String ids) {
		stepDao.updateStep(step);
		finderStepDao.update(finderStep);
		if (ids != null && !"".equals(ids)) {
			sourceFileDao.deleteGetDir(ids);
			sourceFileDirDao.deleteGetDir(ids);
		}
		for (SourceFileDir sourceFileDir : list) {
			if (sourceFileDir.getSourceFileDirId() == null) {
				sourceFileDir.setStep(step);
				sourceFileDirDao.save(sourceFileDir);
			} else {
				sourceFileDirDao.update(sourceFileDir);
			}
		}
//		DataToMemory.write("SqlStep", step.getStepId(), step); 
//		DataToMemory.write("FinderStep", step.getStepId(), finderStep); 
//		DataToMemory.write("SourceFileDirList", step.getStepId(), list); 
	}
	public FileUpStep getFileUpStepByStepId(String stepId){
		Object o = DataToMemory.readData("FileUpStep", stepId);
		if (o == null) {

			FileUpStep fileUpStep=fileUpStepDao.getById(stepId);
			// 数据缓存
			DataToMemory.write("FileUpStep", stepId, fileUpStep); 
			return fileUpStep;
		} else {
			return (FileUpStep) o;
		}
	}
	@Override
	public void delfx(String stepId) {
		sourceFileDao.deleteGetStep(stepId);
		sourceFileDirDao.delete(stepId);
		finderStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
		DataToMemory.delete("SqlStep", stepId);
		DataToMemory.delete("FinderStep", stepId);
//		DataToMemory.delete("SourceFileDirList", stepId);
	}
	
	void getConvertStepId(List<Step> steps,List<String> convertStepIds){
		for(Step step:steps){
			if("3".equals(step.getStepType().getStepTypeId())){//如果是清洗步骤
				convertStepIds.add(step.getStepId());
			}else{
				getConvertStepId(stepDao.listNextSteps(step),convertStepIds);
			}
		}
	}
	
	@Override
	public void clearConvertRecord(String sourceFileDirId) {
//		List<SourceFile> sfList=sourceFileDao.listSourceFileByDirId(sourceFileDirId);
//		if(sfList==null||sfList.size()==0)
//			return;
//		Step finderStep=stepDao.getStep(stepId);
		//获取下一步
//		List<Step> steps=stepDao.listNextSteps(finderStep);
//		if(steps==null||steps.size()==0)
//			return;
//		List<String> convertStepIds=new ArrayList<String>();
		//从下一步开始递归到转换步骤
//		getConvertStepId(steps, convertStepIds);
		
//		for(SourceFile sf:sfList){
//			for(String convertStepId : convertStepIds)
//				convertRecordDao.deleteConvertRecord(convertStepId, sf.getDirectory());
//		}
		
		convertRecordDao.deleteConvertRecord(sourceFileDirId);
		
	}
	@Override
	public void clearScanRecord(String sourceFileDirId) {
		sourceFileDao.deleteGetDir(sourceFileDirId);
	}
	@Override
	public ConvertRecord getConvertRecord(String stepId, String fileName) {
		return convertRecordDao.getConvertRecord(stepId, fileName);
	}
	@Override
	public void saveConvertRecord(ConvertRecord convertRecord) {
		if(convertRecord==null)
			return;
		if(convertRecord.getRecordId()==null||"".equals(convertRecord.getRecordId()))
			convertRecordDao.saveConvertRecord(convertRecord);
		else
			convertRecordDao.updateConvertRecord(convertRecord);
			
	}
	@Override
	public void saveDownloadLog(DownloadLog downloadLog) {
		downloadLogDao.saveDownloadLog(downloadLog);
	}
	
	
	@Override
	public void updateKettleStep(Step step, KettleStep kettleStep) {
		stepDao.updateStep(step);
		kettleStepDao.update(kettleStep);
	}
	@Override
	public KettleStep getKettleStep(String stepId) {
		return kettleStepDao.getKettleById(stepId);
	}
	@Override
	public void saveKettleStep(KettleStep kettleStep,Step step) {
		stepDao.saveStep(step);
		kettleStepDao.save(kettleStep);
	}
	@Override
	public void delKettleStep(String stepId) {
		kettleStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	
	@Override
	public void saveSqlStep(SqlStep sqlStep, Step step) {
		stepDao.saveStep(step);
		sqlStepDao.save(sqlStep);
	}
	
	@Override
	public SqlStep getSqlStep(String stepId) {
		return sqlStepDao.getSqlStepByStepId(stepId);
	}
	
	@Override
	public void updateSqlStep(SqlStep sqlStep, Step step) {
		stepDao.updateStep(step);
		sqlStepDao.update(sqlStep);
	}
	
	@Override
	public void delSqlStep(String stepId) {
		sqlStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	
	
	@Override
	public void delShellStep(String stepId) {
		shellStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	@Override
	public ShellStep getShellStep(String stepId) {
		return shellStepDao.getShellStepByStepId(stepId);
	}
	@Override
	public ShellStep getShellStepCache(String stepId) {
		Object o = DataToMemory.readData("ShellStep", stepId);
		if (o == null) {

			ShellStep shellStep=shellStepDao.getShellStepByStepId(stepId);
			// 数据缓存
			DataToMemory.write("ShellStep", stepId, shellStep); 
			return shellStep;
		} else {
			return (ShellStep) o;
		}
	}
	@Override
	public void saveShellStep(ShellStep shellStep, Step step) {
		stepDao.saveStep(step);
		shellStepDao.save(shellStep);
	}
	@Override
	public void updateShellStep(ShellStep shellStep, Step step) {
		shellStepDao.update(shellStep);
		stepDao.updateStep(step);
	}
	@Override
	public void delOracleExpStep(String stepId) {
		oracleExpStepDao.delete(stepId);
		stepDao.deleteStep(stepId);
	}
	@Override
	public OracleExpStep getOracleExpStep(String stepId) {
		return oracleExpStepDao.getOracleExpStepByStepId(stepId);
	}
	@Override
	public OracleExpStep getOracleExpStepCache(String stepId) {
		Object o = DataToMemory.readData("OracleExpStep", stepId);
		if (o == null) {

			OracleExpStep oracleExpStep=oracleExpStepDao.getOracleExpStepByStepId(stepId);
			// 数据缓存
			DataToMemory.write("OracleExpStep", stepId, oracleExpStep); 
			return oracleExpStep;
		} else {
			return (OracleExpStep) o;
		}
	}
	@Override
	public void saveOracleExpStep(OracleExpStep oracleExpStep, Step step) {
		stepDao.saveStep(step);
		oracleExpStepDao.save(oracleExpStep);
	}
	@Override
	public void updateOracleExpStep(OracleExpStep oracleExpStep, Step step) {
		oracleExpStepDao.update(oracleExpStep);
		stepDao.updateStep(step);
	}
	
	
}
