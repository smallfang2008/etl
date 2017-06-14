package com.zbiti.common.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 * @author shaojing
 *
 */
public class StreamGobbler extends Thread {
	public  final Log logger = LogFactory.getLog(StreamGobbler.class);
	InputStream is;
	String type;
	OutputStream os;
	    
	public StreamGobbler(InputStream is, String type) {
		this(is, type, null);
	}

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }
    
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            if (os != null)
                pw = new PrintWriter(os);
                
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                if("ERROR".equals(type)){
                	logger.info("do cmd is " + type + " > " + line);    
                }
            } 
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        } finally{
        	try{
        		pw.close();
        	}catch(Exception ex){       		
        	}
        	try{
        		br.close();
        	}catch(Exception ex){       		
        	}
        	try{
        		isr.close();
        	}catch(Exception ex){       		
        	} 
        }
    }
} 
