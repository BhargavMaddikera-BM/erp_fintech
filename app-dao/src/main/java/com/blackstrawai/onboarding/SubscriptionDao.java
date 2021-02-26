package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.loginandregistration.SubscriptionVo;

@Repository
public class SubscriptionDao extends BaseDao{
	
	private  Logger logger = Logger.getLogger(SubscriptionDao.class);

	public List<SubscriptionVo> getAllSubscriptions()throws ApplicationException {	
		logger.info("Entry into method:getAllSubscriptions");		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<SubscriptionVo>listAllSubscriptions=new ArrayList<SubscriptionVo>();
		try{
			con = getUserMgmConnection();
			String query=CommonConstants.GET_ALL_SUBSCRIPTIONS;
			preparedStatement=con.prepareStatement(query);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				SubscriptionVo subscriptionVo=new SubscriptionVo();
				subscriptionVo.setId(rs.getInt(1));
				subscriptionVo.setType(rs.getString(2));
				subscriptionVo.setStartPeriod(rs.getDate(3));
				subscriptionVo.setEndPeriod(rs.getDate(4));
				subscriptionVo.setPreDiscountAmount(rs.getDouble(5));
				subscriptionVo.setDiscountAmount(rs.getDouble(6));
				subscriptionVo.setFinalAmount(rs.getDouble(7));
				subscriptionVo.setModules(rs.getString(8));
				listAllSubscriptions.add(subscriptionVo);				
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return listAllSubscriptions;
	}
}
