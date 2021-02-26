package com.blackstrawai.accessandroles;

import java.util.List;
import java.util.Map;

public class Level2AccessVo {

	private Integer id;
	private String name;
	private String key;
	private Boolean hasAccess;
	private Boolean hasChildren;
	private Map<String, Boolean> actions;
	private List<Level3AccessVo> submodules;
	private Boolean isDetailed;
	
	public Boolean getIsDetailed() {
		return isDetailed;
	}

	public void setIsDetailed(Boolean isDetailed) {
		this.isDetailed = isDetailed;
	}

	public Map<String, Boolean> getActions() {
		return actions;
	}

	public void setActions(Map<String, Boolean> actions) {
		this.actions = actions;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Boolean getHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(Boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<Level3AccessVo> getSubmodules() {
		return submodules;
	}

	public void setSubmodules(List<Level3AccessVo> submodules) {
		this.submodules = submodules;
	}

}
