package com.blackstrawai.common;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class ChartOfAccountsThread extends Thread{
	
	private BaseDao baseDao;
	private int organizationId;
	private String userId;
	private String name;
	private String displayName;
	
	public ChartOfAccountsThread(BaseDao baseDao,int organizationId, String userId,String name,String displayName){
		this.baseDao=baseDao;
		this.organizationId=organizationId;
		this.userId=userId;
		this.name=name;
		this.displayName=displayName;
	}
	
	public void run(){
		try {
			
			baseDao.insertIntoChartOfAccountsLevel6(organizationId, name, userId, true,displayName);	
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}
	}
	
	
}
