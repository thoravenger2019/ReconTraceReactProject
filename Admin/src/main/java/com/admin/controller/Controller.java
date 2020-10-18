package com.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.json.XML;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.admin.model.FileUploadModel;
import com.admin.model.User;
import com.admin.model.XmlClass;
import com.admin.service.Trace_Service;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api/")
public class Controller {

	@Autowired
	private Trace_Service traceService;

	@Autowired
	HttpSession session;

	@RequestMapping("/")

	public String home() {
		return "This is what i was looking for";
	}

	User username = null;

	@GetMapping("login1/{username}/{password}")
	public Map<String, String> userLogin(@PathVariable("username") String user_name,
			@PathVariable("password") String password) {
		List<User> lst = traceService.getData(user_name, password);
		username = lst.get(0);
		session.setAttribute("username", username.getUsername());
		session.setAttribute("roleID", username.getRoleID());
		session.setAttribute("clientID", username.getClientID());
		session.setAttribute("branchID", username.getBranchID());
		String user = username.getUsername();
		String roleID = String.valueOf(username.getRoleID());
		String clientID = String.valueOf(username.getClientID());

		String menu = traceService.getMenuData(user, roleID, clientID);

		String subMenu = traceService.getSubMenuData(user, roleID, clientID);

		Map<String, String> hm = new HashMap<String, String>();
		hm.put("menu", menu);
		hm.put("subMenu", subMenu);
		hm.put("username", user);

		return hm;

	}

	@GetMapping("GetRoleDetails/{clientID}")
	public Map<String, String> getRoleDtails(@PathVariable("clientID") String clientID) {

		Map<String, String> hm = new HashMap<String, String>();

		String roles = traceService.getRoleDetails(clientID);

		hm.put("roleNames", roles);

		return hm;

	}

	@GetMapping("GetRoleMaster/{roleName}/{homePage}/{mode}/{roleID}/{clientID}")
	public Map<String, String> getRoleDtails(@PathVariable("roleName") String roleName,
			@PathVariable("homePage") String homePage, @PathVariable("mode") String mode,
			@PathVariable("roleID") String roleID, @PathVariable("clientID") String clientID) {

		Map<String, String> hm = new HashMap<String, String>();
		String rowsInserted = traceService.getRoleMaster(roleName, homePage, mode, roleID, clientID);
		hm.put("roleNames", rowsInserted);
		return hm;
	}

	@GetMapping("GetRoleAccessRights/{roleID}/{clientID}")
	public Map<String, String> getRoleAccessRights(@PathVariable("roleID") String roleID,
			@PathVariable("clientID") String clientID) {
		Map<String, String> hm = new HashMap<String, String>();
		String menuList = traceService.getRoleAccessRights(roleID, clientID);
		hm.put("roleNames", menuList);

		return hm;

	}

	@GetMapping("AssignRoleAccessRights/{roleID}/{clientID}/{menuString}")
	public Map<String, String> assignRoleAccessRights(@PathVariable("roleID") String roleID,
			@PathVariable("clientID") String clientID, @PathVariable("menuString") String menuString) {
		String username = String.valueOf(session.getAttribute("username"));

		System.out.println();

		Map<String, String> hm = new HashMap<String, String>();

		String branchList = traceService.AssignRoleAccessRights(roleID, clientID, menuString, username);

		hm.put("roleNames", branchList);

		return hm;

	}

	@GetMapping("GetBranchDetails/{clientID}/{branchID}")
	public Map<String, String> getBranchDetails(@PathVariable("clientID") String clientID,
			@PathVariable("branchID") String branchID) {
		Map<String, String> hm = new HashMap<String, String>();
		String branchList = traceService.getBranchDetails(clientID, branchID);

		hm.put("branchName", branchList);

		return hm;

	}

	@GetMapping("GetUserDetails/{clientID}/{branchName}/{roleType}")
	public Map<String, String> getUserDetails(@PathVariable("clientID") String clientID,
			@PathVariable("branchName") String branchID, @PathVariable("roleType") String roleID) {
		String username1 = username.getUsername();
		Map<String, String> hm = new HashMap<String, String>();
		String userList = traceService.getUserDetails(username1, clientID, branchID, roleID);
		hm.put("getUser", userList);

		return hm;
	}

	@GetMapping("/{usAddUserername}/{clientID}/{branchID}/{roleID}")
	public Map<String, String> addUser(@PathVariable("username") String username,
			@PathVariable("clientID") String clientID, @PathVariable("branchID") String branchID,
			@PathVariable("roleID") String roleID) {
		Map<String, String> hm = new HashMap<String, String>();
		String userList = traceService.getUserDetails(username, clientID, branchID, roleID);
		hm.put("roleNames", userList);

		return hm;

	}

	@GetMapping("getUploadFiletype")
	public List<JSONObject> getUploadFiletype() {
		List<JSONObject> getUploadFiletype = traceService.getUploadFiletype();
		return getUploadFiletype;
	}

	@GetMapping("getChannelID")
	public List<JSONObject> getChannelID() {
		List<JSONObject> getChannelID = traceService.getChannelID();
		return getChannelID;
	}

	@GetMapping("uploadLogFiles")
	public String fileUpload(@RequestParam("fileType") String fileType, MultipartFile file) throws IOException {
		List<FileUploadModel> al = new ArrayList<FileUploadModel>();

		byte[] bytes = file.getBytes();
		String completeData = new String(bytes);
		String[] rows = completeData.split("\\r?\\n");
		String sLine;
		for (int i = 0; i < rows.length; i++) {
			sLine = rows[i];
			String participant_ID = sLine.substring(0, 3).trim();
			String transaction_Type = sLine.substring(3, 5).trim();
			String from_Account_Type = sLine.substring(5, 7).trim();
			String to_Account_Type = sLine.substring(7, 9).trim();
			String transaction_Serial_Number = sLine.substring(9, 12).trim();
			String response_Code = sLine.substring(21, 23).trim();
			String pan_Number = sLine.substring(23, 42).trim();
			String member_Number = sLine.substring(42, 43).trim();
			String approval_Number = sLine.substring(43, 49).trim();
			String system_Trace_Audit_Number = sLine.substring(49, 61).trim();
			String transaction_Date = sLine.substring(61, 67).trim();
			String transaction_Time = sLine.substring(67, 73).trim();

			String merchant_Category_Code = sLine.substring(73, 77).trim();

			String card_Acceptor_Settlement_Date = sLine.substring(77, 83).trim();
			String card_Acceptor_ID = sLine.substring(83, 98).trim();
			String card_Acceptor_Terminal_ID = sLine.substring(98, 106).trim();
			String card_Acceptor_Terminal_Location = sLine.substring(106, 146).trim();
			String acquirer_ID = sLine.substring(146, 157).trim();
			String acquirer_Settlement_Date = sLine.substring(157, 163).trim();
			String transaction_Currency_code = sLine.substring(163, 166).trim();
			String transaction_Amount = sLine.substring(166, 181).trim();
			String actual_Transaction_Amount = sLine.substring(181, 196).trim();
			String transaction_Acitivity_fee = sLine.substring(196, 211).trim();
			String acquirer_settlement_Currency_Code = sLine.substring(211, 214).trim();
			String acquirer_settlement_Amount = sLine.substring(214, 229).trim();
			String acquirer_Settlement_Fee = sLine.substring(229, 244).trim();
			String acquirer_settlement_processing_fee = sLine.substring(244, 259).trim();
			String transaction_Acquirer_Conversion_Rate = sLine.substring(259, 274).trim();

			al.add(new FileUploadModel(participant_ID, transaction_Type, from_Account_Type, to_Account_Type,
					transaction_Serial_Number, response_Code, pan_Number, member_Number, approval_Number,
					system_Trace_Audit_Number, transaction_Date, transaction_Time, merchant_Category_Code,
					card_Acceptor_Settlement_Date, card_Acceptor_ID, card_Acceptor_Terminal_ID,
					card_Acceptor_Terminal_Location, acquirer_ID, acquirer_Settlement_Date, transaction_Currency_code,
					transaction_Amount, actual_Transaction_Amount, transaction_Acitivity_fee,
					acquirer_settlement_Currency_Code, acquirer_settlement_Amount, acquirer_Settlement_Fee,
					acquirer_settlement_processing_fee, transaction_Acquirer_Conversion_Rate));

		}
		return "success";
	}

	@GetMapping(value = "AddUser/{clientId}/{FirstName}/{LastName}/{UserId}/{EmailID}/{Channel}/{RoleType}/{Password}/{ConfirmPassword}/{BranchName}/{ContactNo}")
	public Map<String, String> addUser(@PathVariable("clientId") String clientId,
			@PathVariable("FirstName") String FirstName, @PathVariable("LastName") String LastName,
			@PathVariable("UserId") String UserId, @PathVariable("EmailID") String EmailId,
			@PathVariable("Channel") String Channel, @PathVariable("RoleType") String RoleType,
			@PathVariable("Password") String password, @PathVariable("ConfirmPassword") String ConfirmPassword,
			@PathVariable("BranchName") String BranchName, @PathVariable("ContactNo") String ContactNo) {
		Map<String, String> hm = new HashMap<String, String>();

		String userid = UserId;

		String password1 = password;

		String firstname = FirstName;

		String lastname = LastName;

		String emailid = EmailId;

		String contactno = ContactNo;

		String securityq = "";
		String securitya = "";
		String createdby = "sam";
		String salt = "525252";
		String channel = Channel;

		String addUserList = traceService.addUser(userid, password1, firstname, lastname, RoleType, clientId,
				BranchName, emailid, contactno, securityq, securitya, createdby, salt, channel);

		hm.put("addedUser", addUserList);

		return hm;
	}

	@GetMapping("DeleteUser/{userid}/{roleid}/{branchid}/{createdby}")
	public Map<String, String> deleteUser(@PathVariable("userid") String userid, @PathVariable("roleid") String roleid,
			@PathVariable("branchid") String branchid, @PathVariable("createdby") String createdby) {
		Map<String, String> hm = new HashMap<String, String>();
		String deleteUser = traceService.deleteUser(userid, roleid, branchid, createdby);
		hm.put("deletedUser", deleteUser);
		return hm;

	}

	@GetMapping("AddUserRole/{userid}/{roleid}/{clientid}/{createdby}")
	public Map<String, String> addUserRole(@PathVariable("userid") String userid, @PathVariable("roleid") String roleid,
			@PathVariable("clientid") String clientid, @PathVariable("createdby") String createdby) {
		Map<String, String> hm = new HashMap<String, String>();
		String addUserRole = traceService.addUserRole(userid, roleid, clientid, createdby);
		hm.put("addUserRole", addUserRole);
		return hm;

	}

	@GetMapping("addUserUpdate/{userid}/{roleid}/{branchid}/{emailid}/{createdby}/{channel}")
	public Map<String, String> addUserUpdate(@PathVariable("userid") String userid,
			@PathVariable("roleid") String roleid, @PathVariable("branchid") String branchid,
			@PathVariable("emailid") String emailid, @PathVariable("createdby") String createdby,
			@PathVariable("channel") String channel) {
		Map<String, String> hm = new HashMap<String, String>();
		String addUserUpdate = traceService.addUserUpdate(userid, roleid, branchid, emailid, createdby, channel);
		hm.put("addUserUpdate", addUserUpdate);
		return hm;
	}

	@GetMapping("/domainTypeList")
	public List<JSONObject> getDomainTypeList() {
		List<JSONObject> domainTypeList = traceService.getDomainTypeList();
		return domainTypeList;
	}

	@GetMapping("/moduleTypeList")
	public List<JSONObject> getModuleTypeList() {
		List<JSONObject> moduleTypeList = traceService.getModuleTypeList();
		return moduleTypeList;
	}

	@GetMapping("/clientName")
	public List<JSONObject> getClientName() {
		List<JSONObject> clientNameList = traceService.getClientNameList();

		return clientNameList;
	}

	@GetMapping("/vendorTypeList")
	public List<JSONObject> getVendorTypeList() {
		List<JSONObject> vendorTypeList = traceService.getVendorTypeList();

		return vendorTypeList;
	}

	@GetMapping("/vendorDetails")
	public List<JSONObject> getVendorDetails() {
		List<JSONObject> vendorDetails = traceService.getVendorDetails();

		return vendorDetails;
	}

	@GetMapping("vendorMasterModes/{mode}/{vendorid}/{VendorTypeName}/{vendortypeid}")
	public Map<String, String> vendorMasterModes(@PathVariable("mode") String mode,
			@PathVariable("vendorid") String vendorid, @PathVariable("VendorTypeName") String vendorname,
			@PathVariable("vendortypeid") String vendortypeid) {
		String createdby = username.getUsername();
		String vendor = vendorname;
		Map<String, String> hm = new HashMap<String, String>();
		String venderMasterRes = traceService.vendorMasterModes(mode, vendorid, vendorname, vendortypeid, createdby,
				vendor);
		hm.put("vendorMasterModes", venderMasterRes);

		return hm;
	}

	@GetMapping("/countryTypeList")
	public List<JSONObject> getContryTypeList() {
		List<JSONObject> countryTypeList = traceService.getContryTypeList();

		return countryTypeList;
	}

	@GetMapping("/currencyDetails")
	public List<JSONObject> getCurrencyDetails() {
		List<JSONObject> currencyDetails = traceService.getCurrencyDetails();

		return currencyDetails;
	}

	@GetMapping("currencyMasterModes/{mode}/{currencyid}/{currencycode}/{currencydescription}/{countryid}/{countryname}/{numericcode}/{country}/{scale}")
	public Map<String, String> currencyMasterModes(@PathVariable("mode") String mode,
			@PathVariable("currencyid") String currencyid, @PathVariable("currencycode") String currencycode,
			@PathVariable("currencydescription") String currencydescription,
			@PathVariable("countryid") String countryid, @PathVariable("countryname") String countryname,
			@PathVariable("numericcode") String numericcode, @PathVariable("country") String country,
			@PathVariable("scale") String scale) {
		String createdby = username.getUsername();
		Map<String, String> hm = new HashMap<String, String>();
		String currencyMasterRes = traceService.currencyMasterModes(mode, currencyid, currencycode, currencydescription,
				countryid, countryname, numericcode, createdby, country, scale);
		hm.put("currencyMasterRes", currencyMasterRes);

		return hm;
	}

	@PostMapping("clientConfigMaster/{clientid}/{mode}/{domainid}/{moduleid}/{clientcode}/{clientname}/{address}/{contactno}/{emailid}/{concernperson}/{cpemailid}/{cpcontactno}/{isbank}/{isactive}/{countryid}/{currencyid}/{ftp_ip}/{ftpusername}/{ftppassword}/{ftpport}/{userlimit}/{terminalcount}/{reporttime}/{colorcode}")
	public String clientConfigMaster(@PathVariable("clientid") String clientid, @PathVariable("mode") String mode,
			@PathVariable("domainid") String domainid, @PathVariable("moduleid") String moduleid,
			@PathVariable("clientcode") String clientcode, @PathVariable("clientname") String clientname,
			@PathVariable("address") String address, @PathVariable("contactno") String contactno,
			@PathVariable("emailid") String emailid, @PathVariable("concernperson") String concernperson,
			@PathVariable("cpemailid") String cpemailid, @PathVariable("cpcontactno") String cpcontactno,
			@PathVariable("isbank") String isbank, @PathVariable("isactive") String isactive,
			@PathVariable("countryid") String countryid, @PathVariable("currencyid") String currencyid,
			@PathVariable("ftp_ip") String ftp_ip, @PathVariable("ftpusername") String ftpusername,
			@PathVariable("ftppassword") String ftppassword, @PathVariable("ftpport") String ftpport,
			@PathVariable("userlimit") String userlimit, @PathVariable("terminalcount") String terminalcount,
			@PathVariable("reporttime") String reporttime, @PathVariable("colorcode") String colorcode,
			@RequestParam("file") MultipartFile logofile) {
		String logoName = logofile.getOriginalFilename();
		String username1 = username.getUsername();
		if (mode.equalsIgnoreCase("ADD") || mode.equalsIgnoreCase("UPDATE")) {
			String clientConfigMaster = traceService.clientConfigMaster(clientid, mode, domainid, moduleid, clientcode,
					clientname, address, contactno, emailid, concernperson, cpemailid, cpcontactno, isbank, isactive,
					countryid, currencyid, ftp_ip, ftpusername, ftppassword, ftpport, logofile, userlimit,
					terminalcount, reporttime, username1, colorcode, logoName);
			return clientConfigMaster;
		} else {
			return null;
		}
	}

	@PostMapping("clientchannelmodeinsert/{channelid}/{modeid}/{clientcode}")
	public String clientchannelmodeinsert(@PathVariable("channelid") String channelid,
			@PathVariable("modeid") String modeid, @PathVariable("clientcode") String clientcode) {
		String user = username.getUsername();
		String clientchannelmodeinsert = traceService.clientchannelmodeinsert(channelid, modeid, clientcode, user);
		return clientchannelmodeinsert;
	}

	@GetMapping("getClientCode")
	public List<JSONObject> getClientCode() {
		List<JSONObject> clientCode = traceService.getClientCode();
		return clientCode;

	}

	@GetMapping("getclientmastermodeget/{clientid}/{mode}/{domainid}/{moduleid}")
	public List<JSONObject> getclientmastermodeget(@PathVariable("clientid") String clientid,
			@PathVariable("mode") String mode, @PathVariable("domainid") String domainid,
			@PathVariable("moduleid") String moduleid) {
		List<JSONObject> getclientmastermodeget = traceService.getclientmastermodeget(clientid, mode, domainid,
				moduleid);
		return getclientmastermodeget;
	}

	@GetMapping("/getclientcurrency/{countryid}")
	public List<JSONObject> getclientcurrency(@PathVariable("countryid") String countryid) {
		List<JSONObject> getclientcurrency = traceService.getclientcurrency(countryid);
		return getclientcurrency;
	}

	@GetMapping("/getClientCode/{clientcode}")
	public List<JSONObject> getClientCode(@PathVariable("clientcode") String clientcode) {
		List<JSONObject> getClientCode = traceService.getClientCode(clientcode);
		return getClientCode;
	}

	@GetMapping("/getLogList/{clientid}")
	public List<JSONObject> getLogList(@PathVariable("clientid") String clientid) {
		List<JSONObject> getLogList = traceService.getLogList(clientid);
		return getLogList;
	}

	@GetMapping("/getchanneltyperun/{clientid}")
	public List<JSONObject> getchanneltyperun(@PathVariable("clientid") String clientid) {
		List<JSONObject> getchanneltyperun = traceService.getchanneltyperun(clientid);
		return getchanneltyperun;
	}

	@GetMapping("/getModeType/{clientid}/{channelid}")
	public List<JSONObject> getModeType(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid) {
		List<JSONObject> getModeType = traceService.getModeType(clientid, channelid);
		return getModeType;
	}

	@GetMapping("/getModeTypeRun/{clientid}/{channelid}")
	public List<JSONObject> getModeTypeRun(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid) {
		List<JSONObject> getModeTypeRun = traceService.getModeTypeRun(clientid, channelid);
		return getModeTypeRun;
	}

	@PostMapping("getxmlfileformat")
	public List<JSONObject> getSearchUserProfiles(@RequestBody XmlClass xmlcls) {
		String userid = username.getUsername();
		String P_FILEXML = xmlcls.getMyXmlData();
		String P_CLIENTID = xmlcls.getClientID();
		String P_VENDORTYPE = xmlcls.getVendorType();
		String P_CHANNELID = xmlcls.getChannelID();
		String P_VENDORID = xmlcls.getVendorID();
		String P_FILEPREFIX = xmlcls.getFilePre();
		String P_FILEEXT = xmlcls.getFileExt();
		String P_MODEID = xmlcls.getModeID();
		String P_SEPARATORTYPE = "";
		String P_CUTOFFTIME = xmlcls.getCutOffTime();
		System.out.println("P_CUTOFFTIME" + P_CUTOFFTIME);
		List<JSONObject> getinsertfileformat = traceService.getinsertfileformat(P_CLIENTID, P_VENDORID, P_FILEEXT,
				P_FILEXML, P_CUTOFFTIME, userid, P_FILEPREFIX, P_VENDORTYPE, P_CHANNELID, P_MODEID, P_SEPARATORTYPE);
		return getinsertfileformat;
	}

	@PostMapping("/uploadBranchMasterFile")
	public Map<String, String> mapBranchMasterReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) {
		Map<String, String> hm = new HashMap<String, String>();
		String extFile = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());

		String user = username.getUsername();

		List result = traceService.mapBranchMasterReapExcelDatatoDB(reapExcelDataFile, user);
		hm.put("mapBranchMasterReapExcelDatatoDB", result.toString());
		return hm;
	}

	@PostMapping("/uploadTerminalMasterFile")
	public Map<String, String> mapTerminaMasterReapExcelDatatoDB(
			@RequestParam("file") MultipartFile reapExcelDataFile) {
		String user = username.getUsername();
		String extFile = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());

		Map<String, String> hm = new HashMap<String, String>();
		List result = traceService.mapTerminalMasterReapExcelDatatoDB(reapExcelDataFile, user);
		hm.put("mapTerminaMasterReapExcelDatatoDB", result.toString());
		return hm;
	}

	@GetMapping("branchTemplate/{clientid}")
	public byte[] getBranchTemplate(@PathVariable("clientid") String clientid) {
		byte[] byteBranch = traceService.getBranchFile(clientid);

		return byteBranch;
	}

	@GetMapping("terminalTemplate/{clientid}")
	public byte[] getTerminalTemplate(@PathVariable("clientid") String clientid) {
		byte[] byteTerminal = traceService.getTerminalFile(clientid);

		return byteTerminal;
	}

	@PostMapping("getChannelData/{clientid}")
	public List<JSONObject> getChannelData(@PathVariable("clientid") String clientid) {
		List<JSONObject> getChannelData = traceService.getChannelData(clientid);
		return getChannelData;
	}

	@PostMapping("updateChangePwdUserName")
	public Map<String, String> updateChangePwdUserName() {
		Map<String, String> hm = new HashMap<String, String>();
		String updateChangePwdUserName = username.getUsername();
		hm.put("updateChangePwdUserName", updateChangePwdUserName);
		return hm;
	}

	@PostMapping("updateChangePwd")
	public List<JSONObject> updateChangePwd(@RequestParam("userid") String userid,
			@RequestParam("clientcode") String clientcode, @RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword") String newpassword, @RequestParam("confirmpassword") String confirmpassword) {

		String createdby = username.getUsername();
		List<JSONObject> updateChangePwd = traceService.updateChangePwd(userid, clientcode, oldpassword, newpassword,
				confirmpassword, createdby);
		return updateChangePwd;
	}

	@GetMapping("getchanneltype/{clientid}")
	public List<JSONObject> getchanneltype(@PathVariable("clientid") String clientid) {
		String userid = username.getUsername();
		List<JSONObject> getchanneltype = traceService.getchanneltype(clientid, userid);
		return getchanneltype;
	}

	@GetMapping("getVendorDetailsByType/{vendortype}")
	public List<JSONObject> getVendorDetailsByType(@PathVariable("vendortype") String vendortype) {
		List<JSONObject> getVendorDetailsByType = traceService.getVendorDetailsByType(vendortype);
		return getVendorDetailsByType;
	}

	@GetMapping("getfileformatclient/{clientid}")
	public List<JSONObject> getfileformatclient(@PathVariable("clientid") String clientid) {
		List<JSONObject> getfileformatclient = traceService.getfileformatclient(clientid);
		return getfileformatclient;
	}

	@GetMapping("getFileFormatHistory/{p_VendorType}/{p_ClientID}/{p_ChannelID}/{p_ModeID}/{p_VendorID}")
	public List<JSONObject> getFileFormatHistory(@PathVariable("p_VendorType") String p_VendorType,
			@PathVariable("p_ClientID") String p_ClientID, @PathVariable("p_ChannelID") String p_ChannelID,
			@PathVariable("p_ModeID") String p_ModeID, @PathVariable("p_VendorID") String p_VendorID) throws Exception {
		List<JSONObject> getFileFormatHistory = traceService.getFileFormatHistory(p_VendorType, p_ClientID, p_ChannelID,
				p_ModeID, p_VendorID);

		System.out.println("getFileFormatHistory:  " + getFileFormatHistory);
		String statusInstr = "";
		JSONObject status = getFileFormatHistory.get(0);
		try {
			statusInstr = status.get("Status").toString();
		} catch (Exception e) {

		}
		System.out.println("statusInstr:  " + statusInstr);
		if (statusInstr.equals("[not exist]") && p_VendorType.equalsIgnoreCase("NETWORK")
				&& p_ModeID.equalsIgnoreCase("2")) {
			String line = "", str = "";
			StringBuffer result = new StringBuffer();
			String link = "C:\\Users\\suyog.mate.MAXIMUS\\git\\SpringReactProject\\Admin\\src\\main\\xmlFiles\\acq_ATM_NPCI.xml";
			BufferedReader br = new BufferedReader(new FileReader(link));
			while ((line = br.readLine()) != null) {
				result.append(line.trim());
			}
//	        Map<String, List<JSONObject>> hm=new HashMap<>();
			str = result.toString();
			System.out.println("str:  " + str);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(str)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			List<JSONObject> JSONObjects = new ArrayList<JSONObject>(nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				JSONObject obj = new JSONObject();
				String nodeName = nodeList.item(i).getNodeName();
				Node childNode = nodeList.item(i);
				NodeList childNodeList = childNode.getChildNodes();

				String startPos = childNodeList.item(0).getNodeName();
				Node startPosNode = childNodeList.item(0);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String startPosNodeValueNode = startPosNodeValue.item(0).getNodeValue();

				String length = childNodeList.item(1).getNodeName();
				Node lengthNode = childNodeList.item(1);
				NodeList lengthNodeValue = lengthNode.getChildNodes();
				String lengthNodeValueNode = lengthNodeValue.item(0).getNodeValue();

				obj.put("NodeName", nodeName);
				obj.put("startPosNodeValueNode", startPosNodeValueNode);
				obj.put("LengthNodeValueNode", lengthNodeValueNode);

				System.out.println("NodeName:  " + nodeName);
				System.out.println("startPosNodeValueNode:  " + startPosNodeValueNode);
				System.out.println("LengthNodeValueNode:  " + lengthNodeValueNode);
				JSONObjects.add(obj);
			}
//			String concatVendorTypeMode=p_VendorType+p_ModeID;
//			hm.put(concatVendorTypeMode, JSONObjects);
			return JSONObjects;
		} else if (statusInstr.equals("[not exist]") && p_VendorType.equalsIgnoreCase("NETWORK")
				&& p_ModeID.equalsIgnoreCase("3")) {
			String line = "", str = "";
			StringBuffer result = new StringBuffer();
			String link = "C:\\Users\\suyog.mate.MAXIMUS\\git\\SpringReactProject\\Admin\\src\\main\\xmlFiles\\iss_atm_npci.xml";
			BufferedReader br = new BufferedReader(new FileReader(link));
			while ((line = br.readLine()) != null) {
				result.append(line.trim());
			}
			str = result.toString();
//	        Map<String, List<JSONObject>> hm=new HashMap<>();
			System.out.println("str:  " + str);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(str)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			List<JSONObject> JSONObjects = new ArrayList<JSONObject>(nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				JSONObject obj = new JSONObject();
				String nodeName = nodeList.item(i).getNodeName();
				Node childNode = nodeList.item(i);
				NodeList childNodeList = childNode.getChildNodes();

				String startPos = childNodeList.item(0).getNodeName();
				Node startPosNode = childNodeList.item(0);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String startPosNodeValueNode = startPosNodeValue.item(0).getNodeValue();

				String length = childNodeList.item(1).getNodeName();
				Node lengthNode = childNodeList.item(1);
				NodeList lengthNodeValue = lengthNode.getChildNodes();
				String lengthNodeValueNode = lengthNodeValue.item(0).getNodeValue();

				obj.put("NodeName", nodeName);
				obj.put("startPosNodeValueNode", startPosNodeValueNode);
				obj.put("LengthNodeValueNode", lengthNodeValueNode);

				System.out.println("NodeName:  " + nodeName);
				System.out.println("startPosNodeValueNode:  " + startPosNodeValueNode);
				System.out.println("LengthNodeValueNode:  " + lengthNodeValueNode);
				JSONObjects.add(obj);
			}
//			String concatVendorTypeMode=p_VendorType+p_ModeID;
//			hm.put(concatVendorTypeMode, JSONObjects);
			return JSONObjects;
		} else if (statusInstr.equals("[not exist]") && p_VendorType.equalsIgnoreCase("CBS")
				&& p_ModeID.equalsIgnoreCase("0")) {
			String line = "", str = "";
			StringBuffer result = new StringBuffer();
			String link = "C:\\Users\\suyog.mate.MAXIMUS\\git\\SpringReactProject\\Admin\\src\\main\\xmlFiles\\cbs_modeAll.xml";
			BufferedReader br = new BufferedReader(new FileReader(link));
			while ((line = br.readLine()) != null) {
				result.append(line.trim());
			}
			str = result.toString();
//	        Map<String, List<JSONObject>> hm=new HashMap<>();
			System.out.println("str:  " + str);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(str)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			List<JSONObject> JSONObjects = new ArrayList<JSONObject>(nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				JSONObject obj = new JSONObject();
				String nodeName = nodeList.item(i).getNodeName();
				Node childNode = nodeList.item(i);
				NodeList childNodeList = childNode.getChildNodes();

				String startPos = childNodeList.item(0).getNodeName();
				Node startPosNode = childNodeList.item(0);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String IndexPosition = startPosNodeValue.item(0).getNodeValue();

//				String length = childNodeList.item(1).getNodeName();
//				Node lengthNode = childNodeList.item(1);
//				NodeList lengthNodeValue = lengthNode.getChildNodes();
//				String lengthNodeValueNode = lengthNodeValue.item(0).getNodeValue();

				obj.put("NodeName", nodeName);
				obj.put("IndexPosition", IndexPosition);
//				obj.put("LengthNodeValueNode", lengthNodeValueNode);

				System.out.println("NodeName:  " + nodeName);
				System.out.println("IndexPosition:  " + IndexPosition);
//				System.out.println("LengthNodeValueNode:  " + lengthNodeValueNode);
				JSONObjects.add(obj);
			}
//			String concatVendorTypeMode=p_VendorType+p_ModeID;
//			hm.put(concatVendorTypeMode, JSONObjects);
			return JSONObjects;
		} 
		else {
			JSONObject xmlFormatDescription = getFileFormatHistory.get(0);
//			Map<String, List<JSONObject>> hm=new HashMap<>();
			List<JSONObject> xmlTojsonList = xmlTojson(xmlFormatDescription.get("FormatDescriptionXml"));

//			List<JSONObject> joinedJsonList = Stream.concat(getFileFormatHistory.stream(), xmlTojsonList.stream())
//					.collect(Collectors.toList());
//			String concatVendorTypeMode=p_VendorType+p_ModeID;
//			hm.put(concatVendorTypeMode, xmlTojsonList);
			return xmlTojsonList;
		}
	}

	@GetMapping("getfileformat/{P_VENDORID}/{P_CLIENTID}/{P_FILEPREFIX}/{P_FILEEXT}/{P_SEPARATORTYPE}/{P_MODEID}/{P_CHANNELID}")
	public List<JSONObject> getfileformat(@PathVariable("P_VENDORID") String P_VENDORID,
			@PathVariable("P_CLIENTID") String P_CLIENTID, @PathVariable("P_FILEPREFIX") String P_FILEPREFIX,
			@PathVariable("P_FILEEXT") String P_FILEEXT, @PathVariable("P_SEPARATORTYPE") String P_SEPARATORTYPE,
			@PathVariable("P_MODEID") String P_MODEID, @PathVariable("P_CHANNELID") String P_CHANNELID)
			throws Exception {
		List<JSONObject> getfileformat = traceService.getfileformat(P_VENDORID, P_CLIENTID, P_FILEPREFIX, P_FILEEXT,
				P_SEPARATORTYPE, P_MODEID, P_CHANNELID);
		JSONObject xmlFormatDescription = getfileformat.get(0);
		List<JSONObject> xmlTojsonList = xmlTojson(xmlFormatDescription.get("FormatDescriptionXml").toString());
		List<JSONObject> joinedJsonList = Stream.concat(getfileformat.stream(), xmlTojsonList.stream())
				.collect(Collectors.toList());
		return joinedJsonList;
	}

	@PostMapping("importFileNpciATMFiles/{clientid}")
	public List<JSONObject> importFileNpciATMFiles(@PathVariable("clientid") String clientid,
			@RequestParam("npci") MultipartFile npci) throws Exception {
		String createdby = "suyog";
//		String clientid="1";
		List<JSONObject> importFileNpciATMFiles = traceService.importFileNpciATMFiles(npci, clientid, createdby);
		return importFileNpciATMFiles;
	}

	@PostMapping("importFileIMPSFiles")
	public List<JSONObject> importFileIMPSFiles(@RequestParam("imps") MultipartFile imps) {
		String createdby = "suyog";
		String clientid = "1";
		List<JSONObject> importFileIMPSFiles = traceService.importFileIMPSFiles(imps, clientid, createdby);
		return importFileIMPSFiles;
	}

	@PostMapping("importPosSettlementSummaryReportFiles/{clientid}")
	public List<JSONObject> importPosSettlementSummaryReportFiles(@PathVariable("clientid") String clientid,
			@RequestParam("pos") MultipartFile pos) {
		String createdby = username.getUsername();
		List<JSONObject> importPosSettlementSummaryReportFiles = traceService.importPosSettlementSummaryReportFiles(pos,
				clientid, createdby);
		return importPosSettlementSummaryReportFiles;
	}

	@PostMapping("importEJFiledata")
	public List<JSONObject> importEJFileData(@RequestParam("ej") MultipartFile ej) {
		String createdby = "suyog";
		String clientid = "1";
		List<JSONObject> importEJFileData = traceService.importEJFileData(ej, clientid, createdby);
		return importEJFileData;

	}

	@PostMapping("importGlcbsFileData")
	public List<JSONObject> importGlcbsFileData(/*@PathVariable("clientid")String clientid,@PathVariable("fileTypeName")String fileTypeName,*/@RequestParam("glCbs") MultipartFile glCbs) {
		String createdby ="suyog";
		String clientid = "1";
		String fileTypeName="ATM_ISSUER_NPCI";
		List<JSONObject> importGlcbsFileData = traceService.importGlcbsFileData(glCbs, clientid, createdby,fileTypeName);
		return importGlcbsFileData;
	}

	@PostMapping("importSwitchFile")
	public List<JSONObject> importSwitchFile(@RequestParam("sw") MultipartFile sw) {

		String createdby = "suyog";
		String clientid = "1";
		List<JSONObject> importSwitchFile = traceService.importSwitchFile(sw, clientid, createdby);
		return importSwitchFile;
	}

	@GetMapping("getfieldidentification/{clientid}/{vendorid}/{channelid}/{modeid}/{formatid}")
	public List<JSONObject> getfieldidentification(@PathVariable("clientid") String clientid,
			@PathVariable("vendorid") String vendorid, @PathVariable("channelid") String channelid,
			@PathVariable("modeid") String modeid, @PathVariable("formatid") String formatid) {
		List<JSONObject> getfieldidentification = traceService.getfieldidentification(clientid, vendorid, channelid,
				modeid, formatid);
		return getfieldidentification;
	}

	@GetMapping("getFileType/{clientid}")
	public List<JSONObject> getFileType(@PathVariable("clientid") String clientid) {
		List<JSONObject> getFileType = traceService.getFileType(clientid);
		return getFileType;
	}

	@GetMapping("getfilevendordetails/{clientid}")
	public List<JSONObject> getfilevendordetails(@PathVariable("clientid") String clientid) {
		List<JSONObject> getfilevendordetails = traceService.getfilevendordetails(clientid);
		return getfilevendordetails;
	}

	@PostMapping("addfieldconfig/{P_CLIENTID}/{P_VENDORID}/{P_FORMATID}")
	public Map<String, String> addfieldconfig(@PathVariable("P_CLIENTID") String P_CLIENTID,
			@PathVariable("P_VENDORID") String P_VENDORID, @PathVariable("P_FORMATID") String P_FORMATID,
			@RequestParam(value = "P_TERMINALCODE", defaultValue = "0") String P_TERMINALCODE,
			@RequestParam(value = "P_BINNO", defaultValue = "0") String P_BINNO,
			@RequestParam(value = "P_ACQUIRERID", defaultValue = "0") String P_ACQUIRERID,
			@RequestParam(value = "P_REVCODE1", defaultValue = "0") String P_REVCODE1,
			@RequestParam(value = "P_REVCODE2", defaultValue = "0") String P_REVCODE2,
			@RequestParam(value = "P_REVTYPE", defaultValue = "0") String P_REVTYPE,
			@RequestParam(value = "P_REVENTRY", defaultValue = "0") String P_REVENTRY,
			@RequestParam(value = "P_TXNDATETIME", defaultValue = "0") String P_TXNDATETIME,
			@RequestParam(value = "P_TXNVALUEDATETIME", defaultValue = "0") String P_TXNVALUEDATETIME,
			@RequestParam(value = "P_TXNPOSTDATETIME", defaultValue = "0") String P_TXNPOSTDATETIME,
			@RequestParam(value = "P_ATMTYPE", defaultValue = "0") String P_ATMTYPE,
			@RequestParam(value = "P_POSTYPE", defaultValue = "0") String P_POSTYPE,
			@RequestParam(value = "P_ECOMTYPE", defaultValue = "0") String P_ECOMTYPE,
			@RequestParam(value = "P_IMPSTYPE", defaultValue = "0") String P_IMPSTYPE,
			@RequestParam(value = "P_UPITYPE", defaultValue = "0") String P_UPITYPE,
			@RequestParam(value = "P_MICROATMTYPE", defaultValue = "0") String P_MICROATMTYPE,
			@RequestParam(value = "P_MOBILERECHARGETYPE", defaultValue = "0") String P_MOBILERECHARGETYPE,
			@RequestParam(value = "P_DEPOSIT", defaultValue = "0") String P_DEPOSIT,
			@RequestParam(value = "P_BALENQ", defaultValue = "0") String P_BALENQ,
			@RequestParam(value = "P_MINISTATEMENT", defaultValue = "0") String P_MINISTATEMENT,
			@RequestParam(value = "P_PINCHANGE", defaultValue = "0") String P_PINCHANGE,
			@RequestParam(value = "P_CHEQUEBOOKREQ", defaultValue = "0") String P_CHEQUEBOOKREQ,
			@RequestParam(value = "P_RESPCODE1", defaultValue = "0") String P_RESPCODE1,
			@RequestParam(value = "P_RESPCODE2", defaultValue = "0") String P_RESPCODE2,
			@RequestParam(value = "P_RESPTPE", defaultValue = "0") String P_RESPTPE,
			@RequestParam(value = "P_EODCODE", defaultValue = "0") String P_EODCODE,
			@RequestParam(value = "P_OFFLINECODE", defaultValue = "0") String P_OFFLINECODE,
			@RequestParam(value = "P_DEBITCODE", defaultValue = "0") String P_DEBITCODE,
			@RequestParam(value = "P_CREDITCODE", defaultValue = "0") String P_CREDITCODE) throws InterruptedException {

		Map<String, String> hm = new HashMap<>();
		String createdby = username.getUsername();
		String addfieldconfig = traceService.addfieldconfig(P_CLIENTID, P_VENDORID, P_FORMATID, P_TERMINALCODE, P_BINNO,
				P_ACQUIRERID, P_REVCODE1, P_REVCODE2, P_REVTYPE, P_REVENTRY, P_TXNDATETIME, P_TXNVALUEDATETIME,
				P_TXNPOSTDATETIME, P_ATMTYPE, P_POSTYPE, P_ECOMTYPE, P_IMPSTYPE, P_UPITYPE, P_MICROATMTYPE,
				P_MOBILERECHARGETYPE, P_DEPOSIT, P_BALENQ, P_MINISTATEMENT, P_PINCHANGE, P_CHEQUEBOOKREQ, P_RESPCODE1,
				P_RESPCODE2, P_RESPTPE, P_EODCODE, P_OFFLINECODE, P_DEBITCODE, P_CREDITCODE, createdby);
		hm.put("status:", addfieldconfig);
		return hm;
	}

	@GetMapping("getformatid/{clientid}/{vendorid}")
	public List<JSONObject> getformatid(@PathVariable("clientid") String clientid,
			@PathVariable("vendorid") String vendorid) {
		List<JSONObject> getformatid = traceService.getformatid(clientid, vendorid);
		return getformatid;

	}

	@GetMapping("getchanneldetails/{clientid}")
	public List<JSONObject> getchanneldetails(@PathVariable("clientid") String clientid) {
		List<JSONObject> getchanneldetails = traceService.getchanneldetails(clientid);
		return getchanneldetails;
	}

	@GetMapping("getchannelmodeinfo/{clientid}/{channelid}")
	public List<JSONObject> getchannelmodeinfo(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid) {
		List<JSONObject> getchannelmodeinfo = traceService.getchannelmodeinfo(clientid, channelid);
		return getchannelmodeinfo;
	}

	@GetMapping("getmatchingmodeinfo/{clientid}/{channelid}")
	public List<JSONObject> getmatchingmodeinfo(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid) {
		List<JSONObject> getmatchingmodeinfo = traceService.getmatchingmodeinfo(clientid, channelid);
		return getmatchingmodeinfo;
	}

	@GetMapping("getMatchingRuleSetForClient/{clientid}/{channelid}/{ruleid}")
	public List<JSONObject> getMatchingRuleSetForClient(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid, @PathVariable("ruleid") String ruleid) {
		List<JSONObject> getMatchingRuleSetForClient = traceService.getMatchingRuleSetForClient(clientid, channelid,
				ruleid);
		return getMatchingRuleSetForClient;
	}

	@GetMapping("getaddmatchingruleconfig/{clientid}/{columnname}/{channelid}/{modeid}/{ruletype}")
	public List<JSONObject> getaddmatchingruleconfig(@PathVariable("clientid") String clientid,
			@PathVariable("columnname") String columnname, @PathVariable("channelid") String channelid,
			@PathVariable("modeid") String modeid, @PathVariable("ruletype") String ruletype) {
		String createdby = username.getUsername();
		List<JSONObject> getaddmatchingruleconfig = traceService.getaddmatchingruleconfig(clientid, columnname,
				channelid, modeid, ruletype, createdby);
		return getaddmatchingruleconfig;
	}

	@GetMapping("getStatusMaster")
	public List<JSONObject> getStatusMaster() {
		List<JSONObject> getStatusMaster = traceService.getStatusMaster();
		return getStatusMaster;
	}

	@GetMapping("getclientreportdetails/{clientid}")
	public List<JSONObject> getclientreportdetails(@PathVariable("clientid") String clientid) {
		List<JSONObject> getclientreportdetails = traceService.getclientreportdetails(clientid);
		return getclientreportdetails;
	}

	@GetMapping("forcesettlementtxns/{clientid}/{channelid}/{modeid}/{glstatus}/{ejstatus}/{nwstatus}/{swstatus}/{fromdatetxn}/{todatetxn}/{recontype}/{settlementtype}")
	public List<JSONObject> forcesettlementtxns(@PathVariable("clientid") String clientid,
			@PathVariable("channelid") String channelid, @PathVariable("modeid") String modeid,
			@PathVariable("glstatus") String glstatus, @PathVariable("ejstatus") String ejstatus,
			@PathVariable("nwstatus") String nwstatus, @PathVariable("swstatus") String swstatus,
			@PathVariable("fromdatetxn") String fromdatetxn, @PathVariable("todatetxn") String todatetxn,
			@PathVariable("recontype") String recontype, @PathVariable("settlementtype") String settlementtype) {
		String user = username.getUsername();
		String tdrc = "0";
		String branchid = "0";
		List<JSONObject> forcesettlementtxns = traceService.forcesettlementtxns(clientid, channelid, modeid, glstatus,
				ejstatus, nwstatus, swstatus, fromdatetxn, todatetxn, recontype, settlementtype, user, tdrc, branchid);
		return forcesettlementtxns;
	}

	@GetMapping("/getchanneltypeall/{clientid}")
	public List<JSONObject> getchanneltypell(@PathVariable("clientid") String clientid) {
		String userid = username.getUsername();
		List<JSONObject> getchanneltypeall = traceService.getchanneltypeall(clientid, userid);
		return getchanneltypeall;
	}

	@GetMapping("/getFileFormatDefualt/{p_FileExt}/{ p_SeparatorType}/{p_ChannelID}/{p_ModeID}/{p_VendorID}")
	public List<JSONObject> getFileFormatDefualt(@PathVariable("p_FileExt") String p_FileExt,
			@PathVariable("p_SeparatorType") String p_SeparatorType, @PathVariable("p_ChannelID") String p_ChannelID,
			@PathVariable("p_ModeID") String p_ModeID, @PathVariable("p_VendorID") String p_VendorID) throws Exception {
		List<JSONObject> getFileFormatDefualt = traceService.getFileFormatDefualt(p_FileExt, p_SeparatorType,
				p_ChannelID, p_ModeID, p_VendorID);
		JSONObject xmlFormatDescription = getFileFormatDefualt.get(0);

		List<JSONObject> xmlTojsonList = xmlTojson(xmlFormatDescription.get("FormatDescriptionXml"));

		List<JSONObject> joinedJsonList = Stream.concat(getFileFormatDefualt.stream(), xmlTojsonList.stream())
				.collect(Collectors.toList());
		return joinedJsonList;
	}

	public List<JSONObject> xmlTojson(Object xmlstrfromdb) throws Exception {
		String xmlStr = xmlstrfromdb.toString();
		System.out.println("xmlStr:  " + xmlStr);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(xmlStr)));
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(nodeList.getLength());

		for (int i = 0; i < nodeList.getLength(); i++) {
			JSONObject obj = new JSONObject();
			String nodeName = nodeList.item(i).getNodeName();
			Node childNode = nodeList.item(i);
			NodeList childNodeList = childNode.getChildNodes();

			String startPos = childNodeList.item(0).getNodeName();
			Node startPosNode = childNodeList.item(0);

			NodeList startPosNodeValue = startPosNode.getChildNodes();
			String startPosNodeValueNode = startPosNodeValue.item(0).getNodeValue();

			String length = childNodeList.item(1).getNodeName();
			Node lengthNode = childNodeList.item(1);
			NodeList lengthNodeValue = lengthNode.getChildNodes();
			String lengthNodeValueNode = lengthNodeValue.item(0).getNodeValue();

			obj.put("NodeName", nodeName);
			obj.put("startPosNodeValueNode", startPosNodeValueNode);
			obj.put("LengthNodeValueNode", lengthNodeValueNode);

			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

}