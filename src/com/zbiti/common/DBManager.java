package com.zbiti.common;
import java.sql.Connection;
import java.sql.DriverManager;
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

import com.zbiti.common.modelframe.ModelframeConf;

public class DBManager {
	private static final Log log = LogFactory.getLog(DBManager.class);
	public static final int ORACLE = 1;
	public static final int ORACLE144 = 2;
	public static final int SYBASE = 3;
	private Connection con;
	private String userName;
	private String password;
	private ResultSet rs;
	private Statement s;
	private int databaseType;
	private ModelframeConf mfc;

	public DBManager(int databaseType) {
		this.databaseType = databaseType;
		mfc=new ModelframeConf();
		try {
			if (databaseType == SYBASE) {
				this.userName = mfc.getIqJdbcUsername();
				this.password = mfc.getIqJdbcPassword();
				Class.forName(mfc.getIqJdbcDriver());//有可能更换
			} else if (databaseType == ORACLE) {
				this.userName = mfc.getNdc1JdbcUsername();
				this.password = mfc.getNdc1JdbcPassword();
				Class.forName(mfc.getNdc1JdbcDriver());
			} else if (databaseType == ORACLE144) {
				this.userName = mfc.getOsswykJdbcUsername();
				this.password = mfc.getOsswykJdbcPassword();
				Class.forName(mfc.getOsswykJdbcDriver());
			}

		} catch (Exception e) {
			log.error("Database constructor error!");
			log.error(e);
		}
	}

	private void checkConnection() throws SQLException {
		boolean isClosed = true;
		try {
			isClosed = con.isClosed();
		} catch (Exception e) {
		}
		if (isClosed) {
			if (databaseType == SYBASE) {
				con = DriverManager.getConnection(
						// "jdbc:sybase:Tds:132.228.165.136:2638/ossne_B",
						mfc.getIqJdbcUrl(), userName,
						password);
			} else if (databaseType == ORACLE) {
				con = DriverManager.getConnection(
						// "jdbc:oracle:thin:@132.228.165.144:1521:osswyk",
						mfc.getNdc1JdbcUrl(), userName,
						password);
			} else if (databaseType == ORACLE144) {
				con = DriverManager.getConnection(
						// "jdbc:oracle:thin:@132.228.165.144:1521:osswyk",
						mfc.getOsswykJdbcUrl(), userName,
						password);
			}
		}
	}

	public int execNonQuery(String sql) {
		try {
			checkConnection();
			s = con.createStatement();
			return s.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("Database execNonQuery error!");
			log.error(e);
		}finally{
			try {
				s.close();
			} catch (Exception e) {

			}
		}
		return -1;
	}
	
	public void execNonQuery(String[] sqls) {
		
		try {
			checkConnection();
			s = con.createStatement();
			for(String sql:sqls){
				s.executeUpdate(sql);
			}
		} catch (SQLException e) {
			log.error("Database execNonQuery error!");
			log.error(e);
		}finally{
			try {
				s.close();
			} catch (Exception e) {

			}
		}
	}
	
	public int execNonQueryTransaction(String[] sqls) {
		int flag=0;
		try {
			checkConnection();
			con.setAutoCommit(false);
			s = con.createStatement();
			for(String sql:sqls){
				if(sql!=null&&!"".equals(sql))
					s.executeUpdate(sql);
			}
			con.commit();
			flag=1;
		} catch (SQLException e) {
			flag=-1;
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("Database execNonQuery error!");
			log.error(e);
		}finally{
			try {
				s.close();
			} catch (Exception e) {

			}
		}
		return flag;
	}

	public List<Map<String, Object>> execQuery(String sql) {
		 
		try {
			checkConnection();
			s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = s.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> listColNames = new ArrayList<String>();
			for (int i = 1; i <= colCount; ++i) {
				listColNames.add(rsmd.getColumnName(i));
			}
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//			int lastRowCountHundredThousand = -1;
//			int rowCount = 0;
			while (rs.next()) {
//				if (++rowCount / 100000 != lastRowCountHundredThousand) {
//					lastRowCountHundredThousand = rowCount / 100000;
//					log.info("Downloading data, downloaded rows "
//							+ lastRowCountHundredThousand + "w.");
//				}
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
			log.error("Database execQuery error!");
			log.error(e);
		}finally{
			try {
				rs.close();
				s.close();
			} catch (Exception e) {

			}
		}
		return new ArrayList<Map<String, Object>>();
	}
	/**
	 * 返回一个List<Map>类型
	 * @param sql
	 * @return
	 */
	public List<Map> execQueryForString(String sql) {
		 
		try {
			checkConnection();
			s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = s.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> listColNames = new ArrayList<String>();
			for (int i = 1; i <= colCount; ++i) {
				listColNames.add(rsmd.getColumnName(i));
			}
			List<Map> result = new ArrayList<Map>();
			int lastRowCountHundredThousand = -1;
			int rowCount = 0;
			while (rs.next()) {
				if (++rowCount / 100000 != lastRowCountHundredThousand) {
					lastRowCountHundredThousand = rowCount / 100000;
					log.info("Downloading data, downloaded rows "
							+ lastRowCountHundredThousand + "w.");
				}
				Map map = new HashMap();
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
			log.error("Database execQuery error!");
			log.error(e);
		}finally{
			try {
				rs.close();
				s.close();
			} catch (Exception e) {

			}
		}
		return new ArrayList<Map>();
	}


	public void close() {
		try {
			rs.close();
		} catch (Exception e) {

		}
		try {
			s.close();
		} catch (Exception e) {

		}
		try {
			con.close();
		} catch (Exception e) {

		}
	}

	/**
	 * 判断表名是否存在
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public boolean isTableExist(String tableName) {
		String mysql = (databaseType != SYBASE ? "select count(1) as NUM from user_tables where table_name='"
				: "select count(1) as NUM from sysobjects where type='U' and name='")
				+ (databaseType != SYBASE ? tableName.toUpperCase() : tableName)
				+ "'";
		List<Map<String, Object>> mapList = execQuery(mysql);
		if (mapList != null && !mapList.isEmpty()) {
			Map<String, Object> map = mapList.get(0);
			if (map != null && !map.isEmpty()) {
				String numStr = String.valueOf(map.get("NUM"));
				int num = 0;
				try {
					num = Integer.parseInt(numStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (num > 0) {
					return true;
				}
			}
		}
		return false;
	}

}
