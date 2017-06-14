package com.zbiti.etl.extend.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.EtlUser;

public interface EtlUserDao extends SqlMapper{
	public List<Map<String, Object>> qryEtlUser(Map map);

	public List<EtlUser> selectEtlUserListPage(Map<String, Object> map);

	public void delete(@Param("username")String username);

	public void save(EtlUser user);

	public void update(EtlUser user);
	public EtlUser getByUsername(@Param("username")String username);

}
