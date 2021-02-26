package com.blackstrawai.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.blackstrawai.ApplicationException;

public abstract class BaseDao {	
	
	public void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) throws ApplicationException {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}
	public void insertIntoChartOfAccountsLevel6(int organizationId,String name,String userId,boolean isSuperAdmin,String displayName)throws ApplicationException{
	}
	
	
	public  Connection getUserMgmConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getUserMgmtUrl(),
					ConnectionHelper.getInstance().getUserMgmtUserName(),
					ConnectionHelper.getInstance().getUserMgmtPassword());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	//con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/usermgmt", "root", "root");
	//con = DriverManager.getConnection("jdbc:mysql://16.0.2.70:3306/usermgmt", "decifer", "uDvQpFJTCSjdMd3bxnZg");
	
	public  Connection getBankingConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getBankingUrl(),
					
					ConnectionHelper.getInstance().getBankingUserName(),
					ConnectionHelper.getInstance().getBankingUserPassword());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}

	
	
	public  Connection getFinanceCommon() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getFinanceCommonUrl(),
					ConnectionHelper.getInstance().getFinanceCommonUserName(),
					ConnectionHelper.getInstance().getFinanceCommonPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}

	public  Connection getAccountingConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getAccountingUrl(),
					ConnectionHelper.getInstance().getAccountingUserName(),
					ConnectionHelper.getInstance().getAccountingUserPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	public  Connection getAccountsPayable() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getAPUrl(),
					ConnectionHelper.getInstance().getAPUserName(),
					ConnectionHelper.getInstance().getAPUserPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	public  Connection getAccountsReceivableConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getARUrl(),
					ConnectionHelper.getInstance().getARUserName(),
					ConnectionHelper.getInstance().getARUserPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	public  Connection getJournalTransactionConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getJournalTransactionUrl(),
					ConnectionHelper.getInstance().getJournalTransactionUserName(),
					ConnectionHelper.getInstance().getJournalTransactionUserPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	public  Connection getPayrollConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getPayrollUrl(),
					ConnectionHelper.getInstance().getPayrollUserName(),
					ConnectionHelper.getInstance().getPayrollUserPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	
	public  Connection getInventoryMgmtConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getInventoryMgmtUrl(),
					ConnectionHelper.getInstance().getInventoryMgmtName(),
					ConnectionHelper.getInstance().getInventoryMgmtPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
	
	public Connection getEmployeeProvidentFundConnection() throws ApplicationException {
		Connection con = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					ConnectionHelper.getInstance().getEpfUrl(),
					ConnectionHelper.getInstance().getEpfUserName(),
					ConnectionHelper.getInstance().getEpfPassword());

		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return con;
	}
}
