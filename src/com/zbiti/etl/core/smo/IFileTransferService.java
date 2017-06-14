package com.zbiti.etl.core.smo;

import com.zbiti.etl.core.vo.Resource;

/**
 * 
 * @author 严海平
 *
 */
public interface IFileTransferService {
	public IFileTransferClient getClient(String serverName);
	public IFileTransferClient getClient(Resource resource);
}
