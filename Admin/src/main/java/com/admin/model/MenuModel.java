package com.admin.model;




public class MenuModel {
	
	private String menuName;
	private String menuID;
	private String menuLevel;
	private String FilePath;
	private String ParentMenuID;
	private String sequenceNo;
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuID() {
		return menuID;
	}
	public void setMenuID(String menuID) {
		this.menuID = menuID;
	}
	public String getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getParentMenuID() {
		return ParentMenuID;
	}
	public void setParentMenuID(String parentMenuID) {
		ParentMenuID = parentMenuID;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	
	@Override
	public String toString() {
		return "MenuModel [menuName=" + menuName + ", menuID=" + menuID + ", menuLevel=" + menuLevel + ", FilePath="
				+ FilePath + ", ParentMenuID=" + ParentMenuID + ", sequenceNo=" + sequenceNo + "]";
	}
	
	
	
	

}
