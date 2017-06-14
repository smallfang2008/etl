package com.zbiti.common.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;



import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings("all")
public class FreeMarkerUtil {
	
	protected static final Log logger = LogFactory.getLog(FreeMarkerUtil.class);
	/**
	 * 通过Freemarker编译文本
	 * @param template
	 * @param param
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static String parseString(String template,Map param) throws Exception{
		try{
		Configuration cfg = new Configuration();
		StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();
		cfg.setTemplateLoader(sqlTemplateLoader);
		sqlTemplateLoader.putTemplate("template", template);
		Map rootMap =new HashMap();
		rootMap.put("p", param);
		Iterator iter= param.keySet().iterator();
		while(iter.hasNext()){
			String key= (String)iter.next();
			rootMap.put(key, param.get(key));
		}
		StringWriter writer = new  StringWriter();
		cfg.getTemplate("template").process(rootMap, writer);
		return writer.toString();
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
			throw ex;
		}
	}
	
	
	
}
