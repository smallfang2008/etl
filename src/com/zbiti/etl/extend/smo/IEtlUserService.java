package com.zbiti.etl.extend.smo;

import java.util.Map;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.extend.vo.EtlUser;

/**
 * @author yhp
 * 
 * 2014-5-20
 */
public interface IEtlUserService {
	/**
	 * 根据username找用户
	 * @param username
	 * @return
	 */
	public EtlUser getByUsername (String username);
	
	/**
	 * 保存
	 * @param user
	 */
	public void save(EtlUser user);
	
	/**
	 * 更新
	 * @param user
	 */
	public void update(EtlUser user);
	
	/**
	 * 删除
	 * @param username
	 */
	public void delete(String username);
	
	/**
	 * 分页查询
	 * @param user
	 * @param page
	 * @return
	 */
	public PageQueryResult<EtlUser> queryPage(Map<String, String> map,Page page);

}
