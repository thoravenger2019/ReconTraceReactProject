package com.admin.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.admin.model.User;

public interface Trace_Service {

	public List<User> getData(String useername, String password);

	public String getMenuData(String username, String roleID, String clientId);

	public String getSubMenuData(String username, String roleID, String clientId);

	public String getRoleDetails(String clientID);

	public String getRoleMaster(String roleName, String homePage, String mode, String roleID, String clientID);

	public String getRoleAccessRights(String roleID, String clientID);

	public String AssignRoleAccessRights(String roleID, String clientID, String menuString, String username);

	public String getBranchDetails(String clientID, String branchID);

	public String getUserDetails(String username, String clientID, String branchID, String roleID);

	public String resetPassword();

	public List<JSONObject> getUploadFiletype();

	public List<JSONObject> getChannelID();

	public String addUser(String userid, String password, String firstname, String lastname, String roleid,
			String clientid, String branchid, String emailid, String contactno, String securityq, String securitya,
			String createdby, String salt);

	public String deleteUser(String userid, String roleid, String branchid, String createdby);

	public String addUserRole(String userid, String roleid, String clientid, String createdby);

	public String addUserUpdate(String userid, String roleid, String branchid, String emailid, String createdby,
			String channel);

	public List<JSONObject> getDomainTypeList();

	public List<JSONObject> getModuleTypeList();

	public List<JSONObject> getClientNameList();

	public List<JSONObject> getVendorTypeList();

	public List<JSONObject> getVendorDetails();

	public String vendorMasterModes(String mode, String vendorid, String vendorname, String vendortypeid,
			String createdby, String vendor);

	public List<JSONObject> getContryTypeList();

	public List<JSONObject> getCurrencyDetails();

	public String currencyMasterModes(String mode, String currencyid, String currencycode, String currencydescription,
			String countryid, String countryname, String numericcode, String createdby, String country, String scale);

	public String clientConfigMaster(String clientid, String mode, String domainid, String moduleid, String clientcode,
			String clientname, String address, String contactno, String emailid, String concernperson, String cpemailid,
			String cpcontactno, String isbank, String isactive, String countryid, String currencyid, String ftp_ip,
			String ftpusername, String ftppassword, String ftpport, MultipartFile logofile, String userlimit,
			String terminalcount, String reporttime, String username, String colorcode, String logoName);

	public List<JSONObject> getclientcurrency(String countryid);

	public List<JSONObject> getClientCode(String clientcode);

	public List<JSONObject> getclientmastermodeget(String clientid, String mode, String domainid, String moduleid);

	public byte[] getBranchFile(String clientid);

	public byte[] getTerminalFile(String clientid);

	public List mapBranchMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user, String clientid);

	public List mapTerminalMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user, String clientid);

	public List<JSONObject> getClientCode();

	public String clientchannelmodeinsert(String channelid, String modeid, String clientcode, String user);

	public List<JSONObject> getChannelData(String clientid);

	public List<JSONObject> updateChangePwd(String userid, String clientcode, String oldpassword, String newpassword,
			String confirmpassword, String createdby);

	public List<JSONObject> getLogList(String clientid);

	public List<JSONObject> getModeType(String clientid, String channelid);

	public List<JSONObject> getinsertfileformat(String p_CLIENTID, String p_VENDORID, String p_FILEEXT,
			String p_FILEXML, String p_CUTOFFTIME, String p_USER, String p_FILEPREFIX, String p_VENDORTYPE,
			String p_CHANNELID, String p_MODEID, String p_SEPARATORTYPE);

	public List<JSONObject> getchanneltype(String clientid, String userid);

	public List<JSONObject> getVendorDetailsByType(String vendortype);

	public List<JSONObject> getfileformatclient(String clientid);

	// public List<JSONObject> importFile(MultipartFile file, String clientid,
	// String createdby);
	public int[] importFileNpciATMFiles(MultipartFile file, String clientid, String createdby)
			throws IOException, EncryptedDocumentException, InvalidFormatException, Exception;

	public List<JSONObject> getFileFormatHistory(String p_VendorType, String p_ClientID, String p_ChannelID,
			String p_ModeID, String p_VendorID);

	public List<JSONObject> getfileformat(String p_VENDORID, String p_CLIENTID, String p_FILEPREFIX, String p_FILEEXT,
			String p_SEPARATORTYPE, String p_MODEID, String p_CHANNELID);

	public List<JSONObject> importFileIMPSFiles(MultipartFile imps, String clientid, String createdby);

	public List<JSONObject> importPosSettlementSummaryReportFiles(MultipartFile pos, String clientid, String createdby);

	public List<JSONObject> importEJFileData(MultipartFile ej, String clientid, String createdby, String fileTypeName)
			throws ParseException;

	public List<JSONObject> importGlcbsFileData(MultipartFile glCbs, String clientid, String createdby,
			String fileTypeName);

	public List<JSONObject> importSwitchFile(MultipartFile sw, String clientid, String createdby, String fileTypeName)
			throws IOException, SQLException, ParserConfigurationException, SAXException;

//	public List<JSONObject> getchannelmodeinfo(String clientid,);

	public List<JSONObject> getfieldidentification(String clientid, String vendorid, String channelid, String modeid,
			String formatid);

	public List<JSONObject> getfilevendordetails(String clientid);

	public String addfieldconfig(String p_CLIENTID, String p_VENDORID, String p_FORMATID, String p_TERMINALCODE,
			String p_BINNO, String p_ACQUIRERID, String p_REVCODE1, String p_REVCODE2, String p_REVTYPE,
			String p_REVENTRY, String p_TXNDATETIME, String p_TXNVALUEDATETIME, String p_TXNPOSTDATETIME,
			String p_ATMTYPE, String p_POSTYPE, String p_ECOMTYPE, String p_IMPSTYPE, String p_UPITYPE,
			String p_MICROATMTYPE, String p_MOBILERECHARGETYPE, String p_DEPOSIT, String p_BALENQ,
			String p_MINISTATEMENT, String p_PINCHANGE, String p_CHEQUEBOOKREQ, String p_RESPCODE1, String p_RESPCODE2,
			String p_RESPTPE, String p_EODCODE, String p_OFFLINECODE, String p_DEBITCODE, String p_CREDITCODE,
			String createdby, String p_CHANNELID);

	public List<JSONObject> getformatid(String clientid, String vendorid);

	public List<JSONObject> getchanneldetails(String clientid);

	public List<JSONObject> getchannelmodeinfo(String clientid, String channelid);

	public List<JSONObject> getmatchingmodeinfo(String clientid, String channelid);

	public List<JSONObject> getMatchingRuleSetForClient(String clientid, String channelid, String ruleid);

	public List<JSONObject> getaddmatchingruleconfig(String clientid, String columnname, String channelid,
			String modeid, String ruletype, String createdby);

	public List<JSONObject> getStatusMaster();

	public List<JSONObject> getclientreportdetails(String clientid);

	public List<JSONObject> forcesettlementtxns(String clientid, String channelid, String modeid, String glstatus,
			String ejstatus, String nwstatus, String swstatus, String fromdatetxn, String todatetxn, String recontype,
			String settlementtype, String user, String tdrc, String branchid);

	public List<JSONObject> getchanneltypeall(String clientid, String userid);

	public List<JSONObject> getFileType(String clientid);

	public List<JSONObject> getchanneltyperun(String clientid);

	public List<JSONObject> getModeTypeRun(String clientid, String channelid);

	public String changeUndefindToNull() throws InterruptedException;

	public List<JSONObject> getFileFormatDefualt(String p_FileExt, String p_SeparatorType, String p_ChannelID,
			String p_ModeID, String p_VendorID);

	public List<JSONObject> getbranchname(String clientid);

	public List<JSONObject> getterminaldetailschannelwise(String clientid, String channelid, String userid);

	public List<JSONObject> getchannelmodedetailsremodify(String clientid);

	public List<JSONObject> getdispensesummaryreport(String clientID, String channelID, String modeID,
			String terminalID, String fromDateTxns, String toDateTxns, String txnType) throws ParseException;

	public List<JSONObject> getnetworktype(String clientid);

	public List<JSONObject> runreconall(String clientid, String fromdate, String todate, String channelid, String user,
			String modeid, String terminalid);

	public List<JSONObject> getunmatchedtxnreport(String clientid, String channelid, String modeid, String terminalid,
			String fromdatetxns, String todatetxns, String txntype);

	public List<JSONObject> getsuccessfultxnreport(String clientid, String channelid, String modeid, String terminalid,
			String fromdatetxns, String todatetxns, String txntype);

	public List<JSONObject> getreversaltxnreport(String clientid, String channelid, String modeid, String terminalid,
			String fromdatetxns, String todatetxns, String txntype);

	public List<JSONObject> getforcesettlementtxns(String clientid, String channelid, String modeid, String glstatus,
			String ejstatus, String nwstatus, String swstatus, String fromdatetxns, String todatetxns, String recontype,
			String settlementtype, String userid);

}
