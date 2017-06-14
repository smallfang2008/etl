package com.zbiti.etl.extend.smo.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbiti.common.dao.DaoUtil;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.extend.dao.EtlUserDao;
import com.zbiti.etl.extend.smo.IEtlUserService;
import com.zbiti.etl.extend.vo.EtlUser;

/**
 * @author sudh
 * 
 * 2014-5-20
 */
@Service("EtlUserServiceImpl")
public class EtlUserServiceImpl implements IEtlUserService {
	@Resource
	private EtlUserDao userDao;
	
	@Override
	public void delete(String username) {
		userDao.delete(username);
	}

	@Override
	public EtlUser getByUsername(String username) {
		return userDao.getByUsername(username);
	}

	@Override
	public PageQueryResult<EtlUser> queryPage(Map<String, String> map, Page page) {
		List<EtlUser> userList = userDao.selectEtlUserListPage(DaoUtil.toMap(map, page));
		return new PageQueryResult<EtlUser>(userList, page.getTotalResult());
	}

	@Override
	public void save(EtlUser user) {
		userDao.save(user);
	}

	@Override
	public void update(EtlUser user) {
		userDao.update(user);
	}
	

}
