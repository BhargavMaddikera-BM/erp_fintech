package com.blackstrawai.ar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ar.dropdowns.BasicLutVo;
import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.OrganizationDao;

@Repository
public class LetterOfUndertakingDao extends BaseDao {

	private Logger logger = Logger.getLogger(LetterOfUndertakingDao.class);

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private AttachmentsDao attachmentsDao;


	public LetterOfUndertakingVo createLetterOfUndertaking(LetterOfUndertakingVo letterOfUndertakingVo) throws ApplicationException {
		logger.info("Entry into method: createLetterOfUndertaking");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {	
			con = getAccountsReceivableConnection();
			boolean isRefundExist=checkLetterOfUndertakingExist(letterOfUndertakingVo, con);
			if(isRefundExist){
				throw new Exception("LetterOfUndertaking Exist for the Organization");
			}
			String sql = LUTConstants.INSERT_INTO_LUT;
			preparedStatement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			//id,ack_no,date,expiry_date,STATUS,create_ts,update_ts,isSuperAdmin,user_id,organization_id
			preparedStatement.setString(1, letterOfUndertakingVo.getAckNo());
			String date=letterOfUndertakingVo.getDateOfCreation();
			preparedStatement.setString(2, date!=null&& date.length()>0?DateConverter.getInstance().correctDatePickerDateToString(date):null);

			Connection con1=getUserMgmConnection();
			String dateFormat=organizationDao.getDefaultDateForOrganization(letterOfUndertakingVo.getOrganizationId(), con1);
			closeResources(null, null, con1);
			preparedStatement.setDate(3, (dateFormat!=null &&letterOfUndertakingVo.getExpiryDate()!=null)? DateConverter.getInstance().convertStringToDate(letterOfUndertakingVo.getExpiryDate(), dateFormat):null);

			preparedStatement.setInt(4, letterOfUndertakingVo.getLocationId());
			preparedStatement.setString(5, letterOfUndertakingVo.getGstNumber());
			preparedStatement.setString(6, letterOfUndertakingVo.getFinYear());
			preparedStatement.setString(7, letterOfUndertakingVo.getStatus());
			preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setBoolean(10, letterOfUndertakingVo.getIsSuperAdmin());
			preparedStatement.setString(11, letterOfUndertakingVo.getUserId());
			preparedStatement.setInt(12, letterOfUndertakingVo.getOrganizationId());
			preparedStatement.setString(13, letterOfUndertakingVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					letterOfUndertakingVo.setId(rs.getInt(1));
				}
			}
			if (letterOfUndertakingVo.getId() >0 && letterOfUndertakingVo.getAttachments()!=null && letterOfUndertakingVo.getAttachments().size()>0) {
				attachmentsDao.createAttachments(letterOfUndertakingVo.getOrganizationId(),letterOfUndertakingVo.getUserId(),letterOfUndertakingVo.getAttachments(),AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT, letterOfUndertakingVo.getId(),letterOfUndertakingVo.getRoleName());

			}	
		} catch (Exception e) {
			logger.error("Error during createLetterOfUndertaking ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return letterOfUndertakingVo;
	}

	public LetterOfUndertakingVo updateLetterOfUndertaking(LetterOfUndertakingVo letterOfUndertakingVo) throws ApplicationException {
		logger.info("Entry into method: updateLetterOfUndertaking");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsReceivableConnection();
			boolean isRefundExist=checkLetterOfUndertakingExist(letterOfUndertakingVo, con);
			if(isRefundExist){
				throw new Exception("LetterOfUndertaking Exist for the Organization");
			}		
			String sql = LUTConstants.UPDATE_LUT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			//id,ack_no,date,expiry_date,STATUS,create_ts,update_ts,isSuperAdmin,user_id,organization_id
			preparedStatement.setString(1, letterOfUndertakingVo.getAckNo());
			String date=letterOfUndertakingVo.getDateOfCreation();
			DateConverter dc=DateConverter.getInstance();
			Connection con1=getUserMgmConnection();
			String dateFormat=organizationDao.getDefaultDateForOrganization(letterOfUndertakingVo.getOrganizationId(), con1);
			closeResources(null, null, con1);
			if(date!=null && date.length()>0 && date.contains("T")&& date.contains("Z") ) {
				date=dc.correctDatePickerDateToString(date);
			}else if(date!=null&& date.length()>0 ) {
				date=dc.convertDateToGivenFormat(dc.convertStringToDate(date, dateFormat),"yyyy-MM-dd");;
			}
			preparedStatement.setString(2, date);
			System.out.println("updation:"+letterOfUndertakingVo.getExpiryDate()+""+dateFormat);
			Date expiryDate=(dateFormat!=null &&letterOfUndertakingVo.getExpiryDate()!=null)? DateConverter.getInstance().convertStringToDate(letterOfUndertakingVo.getExpiryDate(), dateFormat):null;
			preparedStatement.setDate(3, expiryDate);
			preparedStatement.setInt(4, letterOfUndertakingVo.getLocationId());			
			preparedStatement.setString(5, letterOfUndertakingVo.getGstNumber());
			preparedStatement.setString(6, letterOfUndertakingVo.getFinYear());
			String status=letterOfUndertakingVo.getStatus();
			//If user selects existing expired record and changed to future date then that record should be active
			if(expiryDate!=null && status!=null&& status.equalsIgnoreCase(CommonConstants.STATUS_AS_EXPIRED)) {
				long diff = expiryDate.getTime() - new Date(Calendar.getInstance().getTimeInMillis()).getTime();
				long daysDiff=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				logger.info(daysDiff+"-"+expiryDate);
				if(daysDiff>0) {
					status=CommonConstants.STATUS_AS_ACTIVE;
				}

			}


			preparedStatement.setString(7, status);
			preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setBoolean(9, letterOfUndertakingVo.getIsSuperAdmin());
			preparedStatement.setString(10, letterOfUndertakingVo.getUserId());
			preparedStatement.setInt(11, letterOfUndertakingVo.getOrganizationId());
			preparedStatement.setString(12, letterOfUndertakingVo.getRoleName());
			preparedStatement.setInt(13, letterOfUndertakingVo.getId());

			preparedStatement.executeUpdate();

			if(letterOfUndertakingVo.getAttachments()!=null && letterOfUndertakingVo.getAttachments().size()>0) {
				attachmentsDao.createAttachments(letterOfUndertakingVo.getOrganizationId(),letterOfUndertakingVo.getUserId(),letterOfUndertakingVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT, letterOfUndertakingVo.getId(),letterOfUndertakingVo.getRoleName());
			}

			if(letterOfUndertakingVo.getAttachmentsToRemove()!=null && letterOfUndertakingVo.getAttachmentsToRemove().size()>0) {
				for(Integer attachmentId : letterOfUndertakingVo.getAttachmentsToRemove()) {
					attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,letterOfUndertakingVo.getUserId(),letterOfUndertakingVo.getRoleName());  
				}
			}
		} catch (Exception e) {
			logger.error("Error during updateLetterOfUndertaking ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return letterOfUndertakingVo;
	}

	public LetterOfUndertakingVo deleteLetterOfUndertaking(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteLetterOfUndertaking");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		LetterOfUndertakingVo letterOfUndertakingVo = new LetterOfUndertakingVo();
		try {
			con = getAccountsReceivableConnection();
			String sql = LUTConstants.DELETE_LUT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			letterOfUndertakingVo.setId(id);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error during deleteLetterOfUndertaking ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return letterOfUndertakingVo;
	}

	public List<LetterOfUndertakingVo> getAllLetterOfUndertakingsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllLetterOfUndertakingsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<LetterOfUndertakingVo> listLetterOfUndertakings = new ArrayList<LetterOfUndertakingVo>();
		try {

			con = getAccountsReceivableConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query=LUTConstants.GET_LUT_BY_ORGANIZATION;
			}else{
				query = LUTConstants.GET_LUT_BY_ORGANIZATION_USER_ROLE;
			}

			preparedStatement = con.prepareStatement(query);
			//id,ack_no,date,expiry_date,STATUS,create_ts,update_ts,isSuperAdmin,user_id,organization_id
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}			
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				LetterOfUndertakingVo letterOfUndertakingVo = new LetterOfUndertakingVo();
				letterOfUndertakingVo.setId(rs.getInt(1));
				letterOfUndertakingVo.setAckNo(rs.getString(2));	
				Connection con1=getUserMgmConnection();
				String dateFormat=organizationDao.getDefaultDateForOrganization(organizationId, con1);
				closeResources(null, null, con1);
				letterOfUndertakingVo.setDateOfCreation(rs.getDate(3)!=null?DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), dateFormat):null);
				String status=rs.getString(8);
				if(rs.getDate(4)!=null) {
					Date expiryDate=rs.getDate(4);
					long diff = expiryDate.getTime() - new Date(Calendar.getInstance().getTimeInMillis()).getTime();
					long daysDiff=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					//Change status to expired if it is active and less than today's date
					if(daysDiff<0 && status!=null && status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
						status=CommonConstants.STATUS_AS_EXPIRED;
					}
					letterOfUndertakingVo.setExpiryDate(DateConverter.getInstance().convertDateToGivenFormat(expiryDate, dateFormat));
				}

				letterOfUndertakingVo.setLocationId(rs.getInt(5));
				letterOfUndertakingVo.setGstNumber(rs.getString(6));
				letterOfUndertakingVo.setFinYear(rs.getString(7));
				letterOfUndertakingVo.setStatus(status);
				letterOfUndertakingVo.setCreateTs(rs.getTimestamp(9));
				letterOfUndertakingVo.setUpdateTs(rs.getTimestamp(10));
				letterOfUndertakingVo.setIsSuperAdmin(rs.getBoolean(11));
				letterOfUndertakingVo.setUserId(rs.getString(12));
				letterOfUndertakingVo.setOrganizationId(rs.getInt(13));
				letterOfUndertakingVo.setRoleName(rs.getString(14));
				listLetterOfUndertakings.add(letterOfUndertakingVo);
			}
		} catch (Exception e) {
			logger.error("Error during getAllLetterOfUndertakingsOfAnOrganization ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listLetterOfUndertakings;
	}


	public LetterOfUndertakingVo getLetterOfUndertakingById(int id) throws ApplicationException {
		logger.info("Entry into method: getLetterOfUndertakingMappingById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		LetterOfUndertakingVo letterOfUndertakingVo = new LetterOfUndertakingVo();
		try {
			con = getAccountsReceivableConnection();
			String query = LUTConstants.GET_LUT_BY_ID;
			preparedStatement = con.prepareStatement(query);
			//id,ack_no,date,expiry_date,STATUS,create_ts,update_ts,isSuperAdmin,user_id,organization_id
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				letterOfUndertakingVo.setId(rs.getInt(1));
				letterOfUndertakingVo.setAckNo(rs.getString(2));
				Connection con1=getUserMgmConnection();
				String dateFormat=organizationDao.getDefaultDateForOrganization(rs.getInt(13), con1);
				closeResources(null, null, con1);
				letterOfUndertakingVo.setDateOfCreation(rs.getDate(3)!=null?DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), "yyyy-MM-dd"):null);
				String status=rs.getString(8);
				if(rs.getDate(4)!=null) {
					Date expiryDate=rs.getDate(4);
					long diff = expiryDate.getTime() - new Date(Calendar.getInstance().getTimeInMillis()).getTime();
					long daysDiff=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					System.out.println(expiryDate+",DIff:"+daysDiff+status);
					if(daysDiff<0 && status!=null && status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
						status=CommonConstants.STATUS_AS_EXPIRED;
					}
					letterOfUndertakingVo.setExpiryDate(DateConverter.getInstance().convertDateToGivenFormat(expiryDate, dateFormat));
				}
				letterOfUndertakingVo.setLocationId(rs.getInt(5));
				letterOfUndertakingVo.setGstNumber(rs.getString(6));
				letterOfUndertakingVo.setFinYear(rs.getString(7));
				letterOfUndertakingVo.setStatus(status);
				letterOfUndertakingVo.setCreateTs(rs.getTimestamp(9));
				letterOfUndertakingVo.setUpdateTs(rs.getTimestamp(10));
				letterOfUndertakingVo.setIsSuperAdmin(rs.getBoolean(11));
				letterOfUndertakingVo.setUserId(rs.getString(12));
				letterOfUndertakingVo.setOrganizationId(rs.getInt(13));
				letterOfUndertakingVo.setRoleName(rs.getString(14));

			}

			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
			for(AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(letterOfUndertakingVo.getId(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT))
			{
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVos.add(uploadFileVo);
			}
			letterOfUndertakingVo.setAttachments(uploadFileVos);

		} catch (Exception e) {
			logger.error("Error during getLetterOfUndertakingById ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return letterOfUndertakingVo;
	}

	private boolean checkLetterOfUndertakingExist(LetterOfUndertakingVo letterOfUndertakingVo,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkLetterOfUndertakingExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if(letterOfUndertakingVo!=null&&letterOfUndertakingVo.getGstNumber()!=null && letterOfUndertakingVo.getGstNumber().length()>0 && letterOfUndertakingVo.getFinYear()!=null ) {
				String query = LUTConstants.CHECK_LUT_ORGANIZATION;
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setInt(1, letterOfUndertakingVo.getOrganizationId());
				preparedStatement.setString(2, letterOfUndertakingVo.getGstNumber());
				preparedStatement.setString(3, letterOfUndertakingVo.getFinYear());
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					if(letterOfUndertakingVo.getId()==0) {//create scenario
						return true;	
					}else {
						if(letterOfUndertakingVo.getId()!=rs.getInt(1)) {//If it is not same record
							return true;
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error in checkLetterOfUndertakingExist",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}


	public List<BasicLutVo> getLUTDetailsForCurrentFinYear(Integer orgId) throws ApplicationException{
		logger.info("To getLUTDetailsForCurrentFinYear");
		List<BasicLutVo> lutVos = new ArrayList<BasicLutVo>();
		Integer currentYear = Year.now().getValue();
		Integer nextYear = Year.now().getValue() % 100 +1;
		String financialYear = String.valueOf(currentYear)+"-"+String.valueOf(nextYear);
		logger.info("Financial year :: "+financialYear);
		Connection con = null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try{
			con = getAccountsReceivableConnection() ;
			preparedStatement= con.prepareStatement(LUTConstants.GET_BASIC_LUT);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, financialYear);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				BasicLutVo vo = new BasicLutVo();
				vo.setId(rs.getInt(1));
				vo.setLutNo(rs.getString(2));
				vo.setLutDate(rs.getDate(3));
				lutVos.add(vo);
			}
		}catch (Exception e) {
			logger.info("Error in getLUTDetailsForCurrentFinYear",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return lutVos;

	}
}
