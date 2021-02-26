package com.blackstrawai.ap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.PurchaseOrderDropDownVo;
import com.blackstrawai.ap.purchaseorder.PoFilterVo;
import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.ap.purchaseorder.PoProductVo;
import com.blackstrawai.ap.purchaseorder.PoTaxComputationVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDetailsVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDistributionVo;
import com.blackstrawai.ap.purchaseorder.PurchaseOrderVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.DeclinePoVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoFilterVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoListVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.settings.TaxGroupDao;
import com.blackstrawai.settings.TaxGroupVo;

@Service
public class PurchaseOrderService extends BaseService{

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired 
	private TaxGroupDao taxGroupDao;
	
	@Autowired
	private DropDownDao dropDownDao;
	
	
	private Logger logger = Logger.getLogger(PurchaseOrderService.class);

	
	public void createPurchaseOrder(PurchaseOrderVo purchaseOrderVo) throws ApplicationException{	
		logger.info("Entry into createPurchaseOrder");
		try {
			boolean isTxnSuccess = purchaseOrderDao.createPurchaseOrder(purchaseOrderVo);
			if(isTxnSuccess && !purchaseOrderVo.getAttachments().isEmpty() && purchaseOrderVo.getId()!=null) {
				logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_PO, purchaseOrderVo.getId(), purchaseOrderVo.getOrganizationId(), purchaseOrderVo.getAttachments());
					logger.info("Upload Successfull");
			}
			logger.info("PO created Successfully in service layer ");
		} catch (Exception e) {
			logger.info("Error in PO create in service layer ");
			purchaseOrderDao.deletePurchaseOrderEntries(purchaseOrderVo.getId(),purchaseOrderVo.getUserId(),purchaseOrderVo.getRoleName());
			throw new ApplicationException(e);
		}
	}


	public PoTaxComputationVo computeTaxCalculation(List<PoProductVo> productVoList, Integer organizationId ,Boolean isExclusive) throws ApplicationException {
		logger.info("Entry into method: computeTaxCalculation");
		PoTaxComputationVo computationVo = new PoTaxComputationVo();
		List<PoProductVo> prodList = computeTaxForEachProduct(productVoList,organizationId);
		computationVo.setGroupedTax(calculateTotalTax(productVoList));
		prodList = calculteIncluseExclusiveTotalAmount(productVoList,organizationId, isExclusive);
		computationVo.setProducts(prodList);
		return computationVo;
	}

	
	public PurchaseOrderDropDownVo getPurchaseOrderDropDownData(int organizationId)throws ApplicationException {	
		logger.info("Entry into method:getPurchaseOrderDropDownData");
		return dropDownDao.getPurchaseOrderDropDownData(organizationId);
	}

	
	private List<PoTaxDistributionVo> calculateTotalTax(List<PoProductVo> productList){
		logger.info("To method calculateTotalTax::"+productList.size());
		Map<String,Double> taxDetailsMap = new HashMap<String, Double>();
		if(productList.size()>0){
			productList.forEach(product ->{
				logger.info("To method product" + product.getTaxDetails());
				if(product.getTaxDetails()!=null && !(CommonConstants.STATUS_AS_DELETE.equals(product.getStatus()))) {
				product.getTaxDetails().getTaxDistribution().forEach(taxDistribution -> {
					logger.info("To method product" + product.getTaxDetails().getTaxDistribution().size());
					String taxName = taxDistribution.getTaxName() +"~"+ taxDistribution.getTaxRate();
					if(taxName!=null && taxDetailsMap.containsKey(taxName)) {
						Double amount = taxDetailsMap.get(taxName);
						amount = amount +  taxDistribution.getTaxAmount();
						logger.info("amount"+amount +"for key "+taxName);
						taxDetailsMap.put(taxName , Math.round(amount * 100.0) / 100.0 );
					}else {
						logger.info("Adding new entry in map for key"+taxName);
						taxDetailsMap.put(taxName ,Math.round(taxDistribution.getTaxAmount() * 100.0) / 100.0  );
					}
				});
				}
			});
		}
		List<PoTaxDistributionVo> taxDistributionList = new ArrayList<PoTaxDistributionVo>();
		if(taxDetailsMap.size()>0) {
		taxDetailsMap.forEach((k,v)->{
			PoTaxDistributionVo distribution = new PoTaxDistributionVo();
			distribution.setTaxName(k.split("~")[0]);
			distribution.setTaxRate(k.split("~")[1]);
			distribution.setTaxAmount(v);
			taxDistributionList.add(distribution);
		});
		}
		logger.info("Completed method calculateTotalTax");
		return taxDistributionList;
	}
	

	private List<PoProductVo> computeTaxForEachProduct(List<PoProductVo> productList, Integer organizationId) throws ApplicationException {
		logger.info("To calculate computeTaxForEachProduct");
		if(productList.size()>0) {
		List<TaxGroupVo> taxGroupList = taxGroupDao.getTaxGroupForOrganization(organizationId);
		Map<String , String> taxRateMap = taxGroupDao.getTaxRatesMappingForOrganization(organizationId);
		logger.info("TaxGroupListSize"+taxGroupList.size());
		Map<Integer , TaxGroupVo> taxGroupMap = new HashMap<Integer, TaxGroupVo>();
		if(taxGroupList!=null && taxGroupList.size()>0) {
		 taxGroupMap = taxGroupList.stream().collect(Collectors.toMap(TaxGroupVo::getId, vo -> vo));
		}
		logger.info("taxGroupMapSize"+taxGroupMap);
		if(taxGroupMap.size()>0) {
			for(PoProductVo product : productList) {
				if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
				TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
				logger.info("TaxGroupVo"+taxDetails);
				PoTaxDetailsVo taxDetailVo = new PoTaxDetailsVo();
				if(taxDetails!=null) {
					String[] taxIncuded = taxDetails.getTaxesIncluded().split(",");
					taxDetailVo.setGroupName(taxDetails.getName());
					List<PoTaxDistributionVo> distributionVos = new ArrayList<PoTaxDistributionVo>();
					for(int i=0; i<taxIncuded.length;i++) {
						logger.info("taxIncuded"+taxIncuded[i]);						
						String taxName = taxIncuded[i].trim();
						logger.info("taxName is ::"+taxName);
						//String name = taxName.split(" ")[0];
						//logger.info("taxName is ::"+name);
						String taxRate =  taxRateMap.containsKey(taxName) ?  taxRateMap.get(taxName) : null;
						logger.info("taxRate is ::"+taxRate);
						Double taxAmount =taxRate !=null ? (product.getAmount()*(Double.valueOf(taxRate)))/100 : 0;
						logger.info("taxAmount is ::"+taxAmount);
						PoTaxDistributionVo distributionVo = new PoTaxDistributionVo();
						distributionVo.setTaxName(taxName);
						distributionVo.setTaxRate(taxRate);
						distributionVo.setTaxAmount(Math.round(taxAmount * 100.0) / 100.0 );
						distributionVos.add(distributionVo);
						taxDetailVo.setTaxDistribution(distributionVos);
					}
					logger.info("taxDetailVo"+taxDetailVo);
				}
				product.setTaxDetails(taxDetailVo);
			}
			}
		}
		}
		logger.info("Completed calculate computeTaxForEachProduct");
		return productList;
	}
	
	
	private List<PoProductVo> calculteIncluseExclusiveTotalAmount(List<PoProductVo> productVos , Integer orgId, Boolean isExclusive) throws ApplicationException{
		logger.info("To calculteIncluseExclusiveTotalAmount ");
			if(isExclusive) {
				productVos.forEach(product -> {
					if(product.getQuantity()!=null ) {
					Double totalAmount = calculteExclusiveAmount(product.getUnitPrice() , Double.valueOf(product.getQuantity()));
					product.setAmount(totalAmount);
					}
					});
			}else {
				List<TaxGroupVo> taxGroupList = taxGroupDao.getTaxGroupForOrganization(orgId);
				logger.info("TaxGroupListSize"+taxGroupList.size());
				Map<Integer , TaxGroupVo> taxGroupMap = new HashMap<Integer, TaxGroupVo>();
				if(taxGroupList!=null && taxGroupList.size()>0) {
				 taxGroupMap = taxGroupList.stream().collect(Collectors.toMap(TaxGroupVo::getId, vo -> vo));
				}
				logger.info("taxGroupMapSize"+taxGroupMap);
				if(taxGroupMap.size()>0) {
				for(PoProductVo product : productVos) {
					if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
					TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
					logger.info("TaxGroupVo"+taxDetails);
						if(taxDetails.getCombinedRate()!=null && product.getQuantity()!=null ) {
							Double totalAmount = calculteInclusiveAmount(Double.valueOf(taxDetails.getCombinedRate()), product.getUnitPrice(), Integer.valueOf(product.getQuantity()));
							product.setAmount(totalAmount);
						}
					}
					}			
			}
			}
	
		return productVos;
		
	}
	
	private Double calculteInclusiveAmount(Double taxrate , Double unitPrice , Integer quantity) {
		Double inclusiveTotAmnt = ((quantity * unitPrice )/(100+taxrate))*100;
		inclusiveTotAmnt = Math.round(inclusiveTotAmnt * 100.0) / 100.0  ;
		logger.info("Inclusive Amount is :: "+ inclusiveTotAmnt);
		return inclusiveTotAmnt;
	}
	
	private Double calculteExclusiveAmount( Double unitPrice , Double quantity) {
		Double exclusiveTotAmnt = (quantity * unitPrice );
		exclusiveTotAmnt = Math.round(exclusiveTotAmnt * 100.0) / 100.0  ;
		logger.info("Exclusive Amount is :: "+ exclusiveTotAmnt);
		return exclusiveTotAmnt;
	}
	
	public List<PoListVo> getAllPurchaseOrderForOrg(PoFilterVo filterVo) throws ApplicationException{
		logger.info("Entry into getAllPurchaseOrderForOrg");
			return purchaseOrderDao.getPoFilteredList(filterVo);
	}
	
	
	public Map<String, Object> getFilterDropdownValues(Integer orgId) throws ApplicationException {
		logger.info("Entry into getMaxAmountForOrg");
		return purchaseOrderDao.getFilterDropdownValues(orgId);
	}
	
	public PurchaseOrderVo getPurchaseOrder(Integer poId) throws ApplicationException {
		logger.info("Entry into getPurchaseOrder");
		
		
		 PurchaseOrderVo poVo;
		try {
			poVo = purchaseOrderDao.getPurchaseOrderById(poId);
			
			if(poVo.getItems()!=null && poVo.getItems().getProducts()!=null){
				poVo.getItems().setGroupedTax(calculateTotalTax(poVo.getItems().getProducts()));
			}
			
			if(poVo!=null && poVo.getAttachments().size()>0 && poVo.getId()!=null) {
				attachmentService.encodeAllFiles(poVo.getOrganizationId(), poVo.getId(),AttachmentsConstants.MODULE_TYPE_PO, poVo.getAttachments());
			}
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		logger.info("getPurchaseOrder executed Successfully in service Layer ");
		return poVo;
	}
	
	
	public void activateOrDeactivatePurchaseOrder(int purchaseOrderId , String status, String userId, String roleName) throws ApplicationException {
		logger.info("Entry into deletePurchaseOrder");
			try {
				purchaseOrderDao.activateOrDeactivatePo(purchaseOrderId, status,userId,roleName);;
				logger.info("deletePurchaseOrder executed Successfully in service Layer ");
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
		
		
	}
	
	public void updatePurchaseOrder(PurchaseOrderVo purchaseOrderVo) throws ApplicationException {
		logger.info("Entry into updatePurchaseOrder");
		if(purchaseOrderVo.getId() != null) {
			 try {
				 boolean isTxnSuccess = purchaseOrderDao.updatePo(purchaseOrderVo);
				 if(isTxnSuccess && !purchaseOrderVo.getAttachments().isEmpty() && purchaseOrderVo.getId()!=null) {
						logger.info("Entry into upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_PO, purchaseOrderVo.getId(), purchaseOrderVo.getOrganizationId(), purchaseOrderVo.getAttachments());
						logger.info("Upload Successfull");
				 }
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
	}
	}	
	
	
	//Vendor Portal Accept PO 
	public void acceptPo(Integer poId, String userId, String roleName) throws ApplicationException {
		 purchaseOrderDao.acceptPurchaseOrder(poId,userId,roleName);
	}
	
	//Vendor Portal Decline PO 
	public void declinePo(DeclinePoVo declinePoVo) throws ApplicationException {
		purchaseOrderDao.declinePurchaseOrder(declinePoVo);
	}
	
	//Vendor Portal PO list also has the filter implementation
	public List<VendorPortalPoListVo> getPoListForvendorPortal(VendorPortalPoFilterVo filterVo) throws ApplicationException {
		return purchaseOrderDao.getPurchaseOrderFilteredList(filterVo);
	}
	
	//Vendor Portal drop downs for reasons
		public Map<String, Object> getDropDowns() throws ApplicationException {
			return purchaseOrderDao.getVpReasonDropdownValues();
		}
		
	//Vendor portal filter dropdown 
	public Map<String,Object> getVpPoFilterDropDowns(Integer voId) throws ApplicationException {
		logger.info("To get details for vendor Id ::" +voId);
		return purchaseOrderDao.getVpFilterDropdownValues(voId);
	}
	
}
