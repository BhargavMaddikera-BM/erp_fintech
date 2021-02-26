package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blackstrawai.request.vendorsettings.PredefinedSettingsRequest;
import com.blackstrawai.request.vendorsettings.SettingsModuleRequest;
import com.blackstrawai.request.vendorsettings.SettingsTemplateRequest;
import com.blackstrawai.request.vendorsettings.SettingsValidationRequest;
import com.blackstrawai.request.vendorsettings.VamVendorGroupRequest;
import com.blackstrawai.request.vendorsettings.VamVendorGroupSettingsRequest;
import com.blackstrawai.request.vendorsettings.VamVendorRequest;
import com.blackstrawai.request.vendorsettings.VamVendorSettingsRequest;
import com.blackstrawai.vendorsettings.PredefinedSettingsVo;
import com.blackstrawai.vendorsettings.SettingsModuleVo;
import com.blackstrawai.vendorsettings.SettingsTemplateVo;
import com.blackstrawai.vendorsettings.SettingsValidationVo;
import com.blackstrawai.vendorsettings.VamVendorGroupSettingsVo;
import com.blackstrawai.vendorsettings.VamVendorGroupVo;
import com.blackstrawai.vendorsettings.VamVendorSettingsVo;
import com.blackstrawai.vendorsettings.VamVendorVo;

public class VendorSettingsConvertToVoHelper {

	private static VendorSettingsConvertToVoHelper vendorSettingsConvertToVoHelper;

	public static VendorSettingsConvertToVoHelper getInstance() {
		if (vendorSettingsConvertToVoHelper == null) {
			vendorSettingsConvertToVoHelper = new VendorSettingsConvertToVoHelper();
		}
		return vendorSettingsConvertToVoHelper;
	}

	public VamVendorVo convertVamVendorVoFromVamVendorRequest(VamVendorRequest vamVendorRequest) {
		VamVendorVo vamVendorVo = new VamVendorVo();
		vamVendorVo.setSettingId(vamVendorRequest.getSettingsId());
		vamVendorVo.setStatus(vamVendorRequest.getStatus());
		vamVendorVo.setOrgId(vamVendorRequest.getOrgId());
		vamVendorVo.setSuperAdmin(vamVendorRequest.isSuperAdmin());
		vamVendorVo.setUserId(vamVendorRequest.getUserId());
		vamVendorVo.setVendorId(vamVendorRequest.getVendorId());
		if (vamVendorRequest.getContactsId() != null && vamVendorRequest.getContactsId().size() > 0) {
			vamVendorVo.setContactsId(vamVendorRequest.getContactsId());
		}
		return vamVendorVo;
	}

	public VamVendorGroupVo convertVamVendorGroupVoFromVamVendorGroupRequest(
			VamVendorGroupRequest vamVendorGroupRequest) {
		VamVendorGroupVo vamVendorGroupVo = new VamVendorGroupVo();
		vamVendorGroupVo.setOrgId(vamVendorGroupRequest.getOrgId());
		vamVendorGroupVo.setSeetingsId(vamVendorGroupRequest.getSettingsId());
		vamVendorGroupVo.setStatus(vamVendorGroupRequest.getStatus());
		vamVendorGroupVo.setSuperAdmin(vamVendorGroupRequest.isSuperAdmin());
		vamVendorGroupVo.setVendorGroupId(vamVendorGroupRequest.getVendorGroupId());
		return vamVendorGroupVo;
	}

	public VamVendorSettingsVo convertVamVendorSettingsVoFromVamVendorSettingsRequest(
			VamVendorSettingsRequest vamVendorSettingsRequest) {
		VamVendorSettingsVo vamVendorSettingsVo = new VamVendorSettingsVo();
		vamVendorSettingsVo.setVendorId(vamVendorSettingsRequest.getVendorId());
		vamVendorSettingsVo.setVendorName(vamVendorSettingsRequest.getVendorName());
		vamVendorSettingsVo.setStatus(vamVendorSettingsRequest.getStatus());
		vamVendorSettingsVo.setSettingsId(vamVendorSettingsRequest.getSettingsId());
		vamVendorSettingsVo.setSettingsName(vamVendorSettingsRequest.getSettingsName());
		vamVendorSettingsVo.setOrganizationId(vamVendorSettingsRequest.getOrganizationId());
		PredefinedSettingsVo predefinedSettingsVo = convertPredefinedSettingsVoFromRequest(
				vamVendorSettingsRequest.getSettingsData());
		predefinedSettingsVo.setId(vamVendorSettingsRequest.getSettingsId());
		predefinedSettingsVo.setName(vamVendorSettingsRequest.getSettingsName());
		vamVendorSettingsVo.setSettingsData(predefinedSettingsVo);
		if (vamVendorSettingsRequest.getContactsId() != null && vamVendorSettingsRequest.getContactsId().size() > 0) {
			vamVendorSettingsVo.setContactsId(vamVendorSettingsRequest.getContactsId());
		}
		return vamVendorSettingsVo;
	}

	public VamVendorGroupSettingsVo convertVamVendorGroupSettingsVoFromVamVendorGroupSettingsRequest(
			VamVendorGroupSettingsRequest vamVendorGroupSettingsRequest) {
		VamVendorGroupSettingsVo vamVendorGroupSettingsVo = new VamVendorGroupSettingsVo();
		vamVendorGroupSettingsVo.setVendorGroupId(vamVendorGroupSettingsRequest.getVendorGroupId());
		vamVendorGroupSettingsVo.setStatus(vamVendorGroupSettingsRequest.getStatus());
		vamVendorGroupSettingsVo.setSettingsId(vamVendorGroupSettingsRequest.getSettingsId());
		vamVendorGroupSettingsVo.setSettingsName(vamVendorGroupSettingsRequest.getSettingsName());
		vamVendorGroupSettingsVo.setVendorGroupName(vamVendorGroupSettingsRequest.getVendorGroupName());
		vamVendorGroupSettingsVo.setOrganizationId(vamVendorGroupSettingsRequest.getOrganizationId());
		PredefinedSettingsVo predefinedSettingsVo = convertPredefinedSettingsVoFromRequest(
				vamVendorGroupSettingsRequest.getSettingsData());
		predefinedSettingsVo.setId(vamVendorGroupSettingsRequest.getSettingsId());
		predefinedSettingsVo.setName(vamVendorGroupSettingsRequest.getSettingsName());
		vamVendorGroupSettingsVo.setSettingsData(predefinedSettingsVo);
		return vamVendorGroupSettingsVo;
	}

	public PredefinedSettingsVo convertPredefinedSettingsVoFromRequest(PredefinedSettingsRequest request) {
		PredefinedSettingsVo settingsVo = new PredefinedSettingsVo();
		settingsVo.setId(request.getId());
		settingsVo.setIsDefault(request.getIsDefault());
		settingsVo.setIsSuperAdmin(request.getIsSuperAdmin());
		settingsVo.setName(request.getName());
		settingsVo.setOrganizationId(request.getOrganizationId());
		settingsVo.setStatus(request.getStatus());
		settingsVo.setUserId(request.getUserId());
		List<SettingsModuleVo> modules = new ArrayList<SettingsModuleVo>();
		request.getModules().forEach(module -> {
			modules.add(convertSettingsModuleVoFromRequest(module));
		});
		settingsVo.setModules(modules);
		Map<String, List<SettingsTemplateVo>> templateMap = new HashMap<String, List<SettingsTemplateVo>>();
		request.getTemplate().forEach((templateType, templateList) -> {
			List<SettingsTemplateVo> list = new ArrayList<SettingsTemplateVo>();
			templateList.forEach(template -> list.add(convertSettingsTemplateVoFromRequest(template)));
			templateMap.put(templateType, list);
		});
		settingsVo.setTemplate(templateMap);
		List<SettingsValidationVo> validations = new ArrayList<SettingsValidationVo>();
		request.getValidation().forEach(validation -> {
			validations.add(convertSettingsValidationVoFromRequest(validation));
		});
		settingsVo.setValidation(validations);
		return settingsVo;

	}

	public SettingsModuleVo convertSettingsModuleVoFromRequest(SettingsModuleRequest moduleRequest) {
		SettingsModuleVo moduleVo = new SettingsModuleVo();
		moduleVo.setBaseId(moduleRequest.getBaseId());
		moduleVo.setId(moduleRequest.getId());
		moduleVo.setIsActive(moduleRequest.getIsActive());
		moduleVo.setModule(moduleRequest.getModule());
		return moduleVo;
	}


	public SettingsTemplateVo convertSettingsTemplateVoFromRequest(SettingsTemplateRequest templateRequest) {
		SettingsTemplateVo templateVo = new SettingsTemplateVo();
		templateVo.setBaseId(templateRequest.getBaseId());
		templateVo.setId(templateRequest.getId());
		templateVo.setIsActive(templateRequest.getIsActive());
		templateVo.setTemplateName(templateRequest.getTemplateName());
		templateVo.setTemplateType(templateRequest.getTemplateType());
		return templateVo;
	}

	public SettingsValidationVo convertSettingsValidationVoFromRequest(SettingsValidationRequest validationRequest) {
		SettingsValidationVo validationVo = new SettingsValidationVo();
		validationVo.setActivity(validationRequest.getActivity());
		validationVo.setBaseId(validationRequest.getBaseId());
		validationVo.setId(validationRequest.getId());
		validationVo.setIsActive(validationRequest.getIsActive());
		validationVo.setValidationRule(validationRequest.getValidationRule());
		return validationVo;
	}



}
