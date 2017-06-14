package com.zbiti.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.ROWID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBCommon {
	private static final Log log = LogFactory.getLog(DBCommon.class);
	private int maxConnTimes;
	private long waitTime;
	public DBCommon(int maxConnTimes, long waitTime) {
		this.maxConnTimes = maxConnTimes;
		this.waitTime = waitTime * 1000;
	}

	public Connection getConnection(String driver, String url, String username,
			String password) throws Exception {
		if (log.isDebugEnabled())
			log.debug("数据库连接信息---Driver: " + driver + "---Url: " + url
					+ "---Username: " + username + "---Password: " + password);
		Class.forName(driver).newInstance();
		Connection conn = null;
		int connTimes = 1;
		while (true) {
			try {
				conn = DriverManager.getConnection(url, username, password);// 连接用户名和密码
				break;
			} catch (Exception e) {
				if (log.isWarnEnabled())
					log.warn("第 " + connTimes + "次获取数据库连接失败！" + e.getMessage());
				if (connTimes >= maxConnTimes) {
					throw new Exception("尝试" + connTimes + "次，获取数据库连接失败！"
							+ e.getMessage());
				}
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e1) {
					if (log.isErrorEnabled())
						log.error("线程休眠失败！");
				}
				connTimes++;
				continue;
			}
		}
		return conn;
	}

	public void execSql(String sql, String driver, Connection conn)
			throws Exception {
		PreparedStatement ps = null;
		try {
			if (driver.lastIndexOf("mysql") > 0)
				conn.createStatement().execute("set sql_mode=''");
			if (driver.lastIndexOf("SybDriver") > 0) {
				conn.createStatement().execute(
						"set temporary option escape_character='On'");
				conn.createStatement().execute(
						"set temporary option SORT_PINNABLE_CACHE_PERCENT = 1");
			}
			conn.commit();
			ps = conn.prepareStatement(sql);
			ps.execute();
			conn.commit();
		} catch (Exception e) {
			throw new Exception("执行加载语句出错！" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				if (log.isErrorEnabled())
					log.error("关闭连接失败！" + e.getMessage());
			}
		}
	}
	
	public void execSqls(String sqls, String driver, Connection conn)
			throws Exception {
		Statement ps = null;
		try {
			if (driver.lastIndexOf("mysql") > 0)
				conn.createStatement().execute("set sql_mode=''");
			if (driver.lastIndexOf("SybDriver") > 0) {
				conn.createStatement().execute(
						"set temporary option escape_character='On'");
				conn.createStatement().execute(
						"set temporary option SORT_PINNABLE_CACHE_PERCENT = 1");
			}
			conn.commit();
			
			ps=conn.createStatement();
			for(String sql:sqls.split(";")){
				ps.addBatch(sql);
			}
			ps.executeBatch();
			conn.commit();
		} catch (Exception e) {
			throw new Exception("执行SQL出错！" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				if (log.isErrorEnabled())
					log.error("关闭连接失败！" + e.getMessage());
			}
		}
	}
	
	
	public static Map<String,String> getCon(String sql, String driver, Connection conn) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(sql==null||sql.trim().equals(""))
			return map;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String keyStr = rs.getString(1);
				String valueStr = rs.getString(2);
				if(keyStr == null){
					continue;
				}
				String key = new String(keyStr.getBytes("GBK"));
				String value = (valueStr == null?"":new String(valueStr.getBytes("GBK")));
				map.put(key, value);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw e;
		} finally{
			if(rs != null) {
				rs.close();
			}
			if (stmt != null){
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return map;
	}
	
	public static List<Map<String, Object>> getListData(String sql, String driver, Connection conn) throws Exception {
		
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int colCount = rsmd.getColumnCount();
			List<String> listColNames = new ArrayList<String>();
			for (int i = 1; i <= colCount; ++i) {
				listColNames.add(rsmd.getColumnName(i));
			}
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; ++i) {
					String colName = listColNames.get(i - 1);
					Object value = rs.getObject(i);
					if(value instanceof ROWID)
						map.put(colName, rs.getString(i));
					else
						map.put(colName, value);
				}
				result.add(map);
			}
			return result;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw e;
		} finally{
			if(rs != null) {
				rs.close();
			}
			if (stmt != null){
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}
