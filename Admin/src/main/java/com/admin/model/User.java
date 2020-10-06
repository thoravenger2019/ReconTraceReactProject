package com.admin.model;




public class User {


    private String username;

    private String password;
    
    private String roleID;
    
    private String clientID;
    
    private String branchID;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	
	

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", roleID=" + roleID + ", clientID=" + clientID
				+ ", branchID=" + branchID + "]";
	}
    
   

	


 
    
    
}

   