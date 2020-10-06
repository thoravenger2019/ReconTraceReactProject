package com.admin.model;

public class BranchEntry {

	private String BranchCode;

	private String BranchName;

	private String Address;

	private String ContactNo;

	private String EmailID;

	private String ConcernPerson;

//	public BranchEntry(String branchCode, String branchName, String address, String contactNo, String emailID,
//			String concernPerson) {
//		// super();
//		this.BranchCode = branchCode;
//		this.BranchName = branchName;
//		this.Address = address;
//		this.ContactNo = contactNo;
//		this.EmailID = emailID;
//		this.ConcernPerson = concernPerson;
//	}

	public String getBranchCode() {
		return BranchCode;
	}

	public void setBranchCode(String branchCode) {
		this.BranchCode = branchCode;
	}

	public String getBranchName() {
		return BranchName;
	}

	public void setBranchName(String branchName) {
		this.BranchName = branchName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		this.Address = address;
	}

	public String getContactNo() {
		return ContactNo;
	}

	public void setContactNo(String contactNo) {
		this.ContactNo = contactNo;
	}

	public String getEmailID() {
		return EmailID;
	}

	public void setEmailID(String emailID) {
		this.EmailID = emailID;
	}

	public String getConcernPerson() {
		return ConcernPerson;
	}

	public void setConcernPerson(String concernPerson) {
		this.ConcernPerson = concernPerson;
	}

}
