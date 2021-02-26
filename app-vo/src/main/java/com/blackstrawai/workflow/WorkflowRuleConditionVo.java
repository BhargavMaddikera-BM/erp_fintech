package com.blackstrawai.workflow;

import java.util.List;

public class WorkflowRuleConditionVo {
	private int id;
	private String name;
	private List<WorkflowRuleChoiceVo> choice;
	
	public List<WorkflowRuleChoiceVo> getChoice() {
		return choice;
	}
	public void setChoice(List<WorkflowRuleChoiceVo> choice) {
		this.choice = choice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
