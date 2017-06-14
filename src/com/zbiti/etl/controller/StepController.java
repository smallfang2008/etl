package com.zbiti.etl.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zbiti.common.vo.DataVO;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepType;
import com.zbiti.etl.extend.vo.ConvertStep;
import com.zbiti.etl.extend.vo.FileUpStep;
import com.zbiti.etl.extend.vo.FinderStep;
import com.zbiti.etl.extend.vo.IQLoadStep;
import com.zbiti.etl.extend.vo.KettleStep;
import com.zbiti.etl.extend.vo.OracleExpStep;
import com.zbiti.etl.extend.vo.OracleLoadStep;
import com.zbiti.etl.extend.vo.ShellStep;
import com.zbiti.etl.extend.vo.SourceFileDir;
import com.zbiti.etl.extend.vo.SqlStep;

@Controller
@RequestMapping("/etl/procedure")
public class StepController {
	private Log log = LogFactory.getLog(StepController.class);
	@Autowired(required = true)
	private IStepService procedureService;

	@RequestMapping("/toEditProcedure")
	public ModelAndView toEditNode(@RequestParam(value = "sceneId", required = false) String sceneId) {
		Scene scene = procedureService.getByIdScene(sceneId);
		List<Step> queryResult = procedureService.listStepBySceneId(sceneId);
		JSONObject json = new JSONObject();
		json.put("-1", "无");
		for (Step step : queryResult) {
			json.put(step.getStep(), step.getStepName());
		}
		ModelAndView mav = new ModelAndView("/etl/task_manager/ProcedureList");
		mav.addObject("scene", scene);
		mav.addObject("syb", json.toString());
		return mav;
	}

	@RequestMapping("/queryProcedure")
	@ResponseBody
	public List<Step> queryProcedure(DataVO<String, String> vo, @RequestParam(value = "sceneId", required = false) String sceneId) throws IOException {
		List<Step> queryResult = procedureService.listStepBySceneId(sceneId);
		return queryResult;
	}

	@RequestMapping("/queryStepTypeList")
	@ResponseBody
	public List<Map<String, Object>> queryStepTypeList(HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<StepType> steptypelist = procedureService.findstepType();
		List<Map<String, Object>> reslutList = new ArrayList<Map<String, Object>>();
		Map<String, String> steptypemap = new HashMap<String, String>();
		Map<String, Object> map = null;
		if (null != steptypelist && !steptypelist.isEmpty()) {
			for (StepType st : steptypelist) {
				map = new HashMap<String, Object>();
				map.put("text", st.getStepTypeName());
				map.put("value", st.getStepTypeId() + "j-" + st.getControllerMapping());
				steptypemap.put(st.getStepTypeId(), st.getStepTypeName());
				reslutList.add(map);
			}
		}
			session.setAttribute("steptypemap", steptypemap);
		
		return reslutList;
	}

	@RequestMapping("/queryStepNameList")
	@ResponseBody
	public List<Map<String, Object>> queryStepNameList(@RequestParam(value = "sceneId", required = false) String sceneId) {
		List<Map<String, Object>> reslutList = procedureService.listStepNameBySceneId(sceneId);
		return reslutList;
	}

	@RequestMapping("/queryyxlxList")
	@ResponseBody
	public Map<String, Object> queryyxlxList(@RequestParam(value = "sceneId", required = false) String sceneId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> jqlist = procedureService.listnodeall();
		List<Map<String, Object>> zdfulist = procedureService.listserverall();
		map.put("2", jqlist);
		map.put("1", zdfulist);
		return map;
	}

	@RequestMapping("/toEditStep")
	public ModelAndView toEditStep(@RequestParam(value = "stepId", required = false) String stepId) {
		Step step = procedureService.getStep(stepId);
		ModelAndView mav = new ModelAndView("/etl/task_manager/edit" + step.getStepType().getControllerMapping());
		mav.addObject("step", step);
		return mav;
	}

	// 发现、转换、oracle加载、sybase加载
	/*
	 * 下载
	 */
	@RequestMapping("/editStepxz")
	@ResponseBody
	public String editNodexz(Step step) {
		try {
			procedureService.updateStep(step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepxz")
	@ResponseBody
	public String addStepxz(Step step) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			procedureService.saveStep(step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("delProcedure-xz")
	@ResponseBody
	public String delStepxz(String stepId) {
		try {
			procedureService.deleteStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	/*
	 * 文件上传
	 */
	@RequestMapping("/getFtpList")
	@ResponseBody
	public List<Map<String, Object>> getFtpList() {
		List<Map<String, Object>> reslutList = procedureService.getResurceList("1");
		return reslutList;
	}

	@RequestMapping("/getFtp")
	@ResponseBody
	public FileUpStep getFtp(String stepId) {
		FileUpStep fileUpStep = procedureService.getFileUpStep(stepId);
		return fileUpStep;
	}

	@RequestMapping("/editStepwjsc")
	@ResponseBody
	public String editStepwjsc(Step step, FileUpStep fileUpStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateFileUpStep(fileUpStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/delProcedure-wjsc")
	@ResponseBody
	public String delStepwjsc(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delFileUpStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepwjsc")
	@ResponseBody
	public String addStepwjsc(Step step, FileUpStep fileUpStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			fileUpStep.setStepId(stepId);
			procedureService.saveFileUpStep(fileUpStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	/*
	 * 装换
	 */
	@RequestMapping("/getConvertStep")
	@ResponseBody
	public ConvertStep getConvertStep(String stepId) {
		ConvertStep convertStep = procedureService.getConvertStep(stepId);
		return convertStep;
	}

	@RequestMapping("/editStepzh")
	@ResponseBody
	public String editStepzh(Step step, ConvertStep convertStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateConvertStep(convertStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepzh")
	@ResponseBody
	public String addStepzh(Step step, ConvertStep convertStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			convertStep.setStepId(stepId);
			procedureService.saveConvertStep(convertStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/delProcedure-zh")
	@ResponseBody
	public String delStepzh(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delConvertStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	/*
	 * Oracle加载
	 */
	@RequestMapping("/getOracleList")
	@ResponseBody
	public List<Map<String, Object>> getOracleList() {
		List<Map<String, Object>> reslutList = procedureService.getResurceList("2");
		return reslutList;
	}

	@RequestMapping("/getOracleLoadStep")
	@ResponseBody
	public OracleLoadStep getOracleLoadStep(String stepId) {
		OracleLoadStep oracleLoadStep = procedureService.getOracleLoadStep(stepId);
		return oracleLoadStep;
	}

	@RequestMapping("/editStepOracle")
	@ResponseBody
	public String editStepOracle(Step step, OracleLoadStep oracleLoadStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateOracleLoadStep(oracleLoadStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepOracle")
	@ResponseBody
	public String addStepOracle(Step step, OracleLoadStep oracleLoadStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			oracleLoadStep.setStepId(stepId);
			procedureService.saveOracleLoadStep(oracleLoadStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/delProcedure-oracle")
	@ResponseBody
	public String delStepOracle(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delOracleLoadStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	/*
	 * sybase加载
	 */
	@RequestMapping("/getSybaseList")
	@ResponseBody
	public List<Map<String, Object>> getSybaseList() {
		List<Map<String, Object>> reslutList = procedureService.getResurceList("2");
		return reslutList;
	}

	@RequestMapping("/getSybaseStep")
	@ResponseBody
	public IQLoadStep getIQLoadStep(String stepId) {
		IQLoadStep iqLoadStep = procedureService.getIQLoadStep(stepId);
		return iqLoadStep;
	}

	@RequestMapping("/editStepSybase")
	@ResponseBody
	public String editStepSybase(Step step, IQLoadStep iqLoadStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateIQLoadStep(iqLoadStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepSybase")
	@ResponseBody
	public String addStepSybase(Step step, IQLoadStep iqLoadStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			iqLoadStep.setStepId(stepId);
			procedureService.saveIQLoadStep(iqLoadStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/delProcedure-sybase")
	@ResponseBody
	public String delStepSybase(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delIQLoadStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/getKettleStep")
	@ResponseBody
	public KettleStep getKettleStep(String stepId) {
		KettleStep kettleStep = procedureService.getKettleStep(stepId);
		return kettleStep;
	}
	
	@RequestMapping("addStepKettle")
	@ResponseBody
	public ModelAndView addStepKettle(Step step, KettleStep kettleStep) {

		ModelAndView mav = toEditNode(step.getScene().getSceneId());
		try {
			if(kettleStep.getKettleType()==null)
				return mav;
			kettleStep.setKettleFileName(kettleStep.getKettleFile().getOriginalFilename());
			kettleStep.setKettleFileStream(FileCopyUtils.copyToByteArray(kettleStep.getKettleFile().getInputStream()));
			
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			kettleStep.setStepId(stepId);
			procedureService.saveKettleStep(kettleStep, step);
			return mav;
		} catch (Exception e) {
			log.error(e);
		}
		return mav;
	}
	@RequestMapping("/delProcedure-kettle")
	@ResponseBody
	public String delStepKettle(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delKettleStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	@RequestMapping(method = RequestMethod.POST, value = "/editStepKettle", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public ModelAndView editStepKettle(Step step, KettleStep kettleStep) {
		ModelAndView mav = toEditNode(step.getScene().getSceneId());
		try {
			// procedureService.updateStep(step);
			if(kettleStep.getKettleFile()!=null){
				kettleStep.setKettleFileName(kettleStep.getKettleFile().getOriginalFilename());
				kettleStep.setKettleFileStream(FileCopyUtils.copyToByteArray(kettleStep.getKettleFile().getInputStream()));
			}
			procedureService.updateKettleStep( step,kettleStep);
			return mav;
		} catch (Exception e) {
			log.error(e,e);
		}
		return mav;
	}
	@RequestMapping("/downloadKettleFile")
	public void downloadKettleFile(String stepId,HttpServletRequest request, HttpServletResponse response) {
		KettleStep kettleStep = procedureService.getKettleStep(stepId);
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition","attachment;fileName=" + kettleStep.getKettleFileName());// 设置文件名
		try {
			OutputStream os = response.getOutputStream();
			os.write(kettleStep.getKettleFileStream());
		} catch (IOException e) {
			log.error("下载文件出错",e);
		}
	}
	
	
	/*
	 * 发现
	 */
	@RequestMapping("/getfxList")
	@ResponseBody
	public List<Map<String, Object>> getfxList() {
		List<Map<String, Object>> reslutList = procedureService.getResurceList("1,4");
		return reslutList;
	}

	@RequestMapping("/querySource")
	@ResponseBody
	public List<SourceFileDir> querySource(DataVO<String, String> vo, @RequestParam(value = "stepId", required = false) String stepId) throws IOException {
		List<SourceFileDir> queryResult = procedureService.findSourceFileDir(stepId);
		return queryResult;
	}
	
	@RequestMapping("/queryFinder")
	@ResponseBody
	public FinderStep queryFinder(DataVO<String, String> vo, @RequestParam(value = "stepId", required = false) String stepId) throws IOException {
		FinderStep queryResult = procedureService.getFinderStep(stepId);
		return queryResult;
	}

	@RequestMapping("/editStepfx")
	@ResponseBody
	public String editStepfx(Step step, FinderStep finderStep, String listSourceFileDir, String ids) {
		try {
			ids = ids.equals("")?"":ids.substring(1);
			List<SourceFileDir> sourceFileDirList = new ArrayList<SourceFileDir>();
			JSONArray stepArray = JSONArray.fromObject(listSourceFileDir);
			for (Object o : stepArray) {
				JSONObject s = (JSONObject) o;
				SourceFileDir sfd = new SourceFileDir();
				sfd.setSourceFileDirId(s.get("sourceFileDirId") == null ? null : s.getString("sourceFileDirId"));
				sfd.setFilePath(s.get("filePath") == null ? "" : s.getString("filePath").equals("null")? "" : s.getString("filePath"));
				sfd.setFilePathPattern(s.get("filePathPattern") == null ? "" : s.getString("filePathPattern").equals("null")? "" : s.getString("filePathPattern"));
				sfd.setFilePattern(s.get("filePattern") == null ? "" : s.getString("filePattern").equals("null")? "" : s.getString("filePattern"));
				sfd.setServerName(s.get("serverName") == null ? "" : s.getString("serverName").equals("null")? "" : s.getString("serverName"));
				sfd.setStartDate(new Date(s.getLong("startDate")));
				sourceFileDirList.add(sfd);
			}
			procedureService.updatefx(sourceFileDirList, finderStep, step, ids);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("addStepfx")
	@ResponseBody
	public String addStepfx(Step step, FinderStep finderStep, String listSourceFileDir) {
		try {
//			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			List<SourceFileDir> sourceFileDirList = new ArrayList<SourceFileDir>();
			JSONArray stepArray = JSONArray.fromObject(listSourceFileDir);
			for (Object o : stepArray) {
				JSONObject s = (JSONObject) o;
				SourceFileDir sfd = new SourceFileDir();
				sfd.setFilePath(s.get("filePath") == null ? "" : s.getString("filePath"));
				sfd.setFilePathPattern(s.get("filePathPattern") == null ? "" : s.getString("filePathPattern"));
				sfd.setFilePattern(s.get("filePattern") == null ? "" : s.getString("filePattern"));
				sfd.setServerName(s.get("serverName") == null ? "" : s.getString("serverName"));
				sfd.setStartDate(new Date(s.getLong("startDate")));
				sourceFileDirList.add(sfd);
			}
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
//			List<SourceFileDir> sourceFileDirList = JSONArray.toList(stepArray, SourceFileDir.class);
			finderStep.setStepId(stepId);
			procedureService.savefx(sourceFileDirList, finderStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/delProcedure-fx")
	@ResponseBody
	public String delStepfx(String stepId) {
		try {
			procedureService.delfx(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/clearScanRecord")
	@ResponseBody
	public String clearScanRecord(String sourceFileDirId) {
		try {
			procedureService.clearScanRecord(sourceFileDirId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	@RequestMapping("/clearConvertRecord")
	@ResponseBody
	public String clearConvertRecord(String sourceFileDirId,String stepId) {
		try {
			procedureService.clearConvertRecord(sourceFileDirId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/editAllStep")
	@ResponseBody
	public String editAllStep(String step) {
		try {
			JSONArray stepArray = JSONArray.fromObject(step);
			List<Step> stepList = JSONArray.toList(stepArray, Step.class);
			for (Step ss : stepList) {
				if ("1".equals(ss.getRunPositionType())) {
					// 服务器集群的方式
					ss.getServerCluster().setServerClusterId(ss.getRunPosition());
					ss.getNode().setNodeCode(null);
				} else if ("2".endsWith(ss.getRunPositionType())) {
					// 指定服务器方式
					ss.getNode().setNodeCode(ss.getRunPosition());
					ss.getServerCluster().setServerClusterId(null);
				}
			}
			procedureService.editAllStep(stepList);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("addStepSql")
	@ResponseBody
	public String addStepSql(Step step, SqlStep sqlStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			sqlStep.setStepId(stepId);
			procedureService.saveSqlStep(sqlStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/getSqlStep")
	@ResponseBody
	public SqlStep getSqlStep(String stepId) {
		SqlStep sqlStep = procedureService.getSqlStep(stepId);
		return sqlStep;
	}
	
	@RequestMapping("/editStepSql")
	@ResponseBody
	public String editStepSql(Step step, SqlStep sqlStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateSqlStep(sqlStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/delProcedure-sql")
	@ResponseBody
	public String delStepSql(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delSqlStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	
	/**
	 * 
	 * @param step
	 * @param shellStep
	 * @return
	 */
	@RequestMapping("addStepShell")
	@ResponseBody
	public String addStepShell(Step step, ShellStep shellStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			shellStep.setStepId(stepId);
			procedureService.saveShellStep(shellStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/getShellStep")
	@ResponseBody
	public ShellStep getShellStep(String stepId) {
		ShellStep shellStep = procedureService.getShellStep(stepId);
		return shellStep;
	}
	
	@RequestMapping("/editStepShell")
	@ResponseBody
	public String editStepShell(Step step, ShellStep shellStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateShellStep(shellStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/delProcedure-shell")
	@ResponseBody
	public String delStepShell(String stepId) {
		try {
			// procedureService.deleteStep(stepId);
			procedureService.delShellStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	
	/**
	 * 
	 * @param step
	 * @param oracleExpStep
	 * @return
	 */
	@RequestMapping("addStepOracleExp")
	@ResponseBody
	public String addStepOracleExp(Step step, OracleExpStep oracleExpStep) {
		try {
			String stepId = procedureService.getStepSeq();
			step.setStepId(stepId);
			// procedureService.saveStep(step);
			oracleExpStep.setStepId(stepId);
			procedureService.saveOracleExpStep(oracleExpStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/getOracleExpStep")
	@ResponseBody
	public OracleExpStep getOracleExpStep(String stepId) {
		OracleExpStep oracleExpStep = procedureService.getOracleExpStep(stepId);
		return oracleExpStep;
	}
	
	@RequestMapping("/editStepOracleExp")
	@ResponseBody
	public String editStepOracleExp(Step step, OracleExpStep oracleExpStep) {
		try {
			// procedureService.updateStep(step);
			procedureService.updateOracleExpStep(oracleExpStep, step);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	@RequestMapping("/delProcedure-oracleExp")
	@ResponseBody
	public String delStepOracleExp(String stepId) {
		try {
			procedureService.delOracleExpStep(stepId);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
}
