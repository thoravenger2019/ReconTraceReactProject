package com.admin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.admin.dao.Trace_DAO;
import com.admin.model.User;

@Service
@Transactional
public class Trace_Service_Imp implements Trace_Service {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Trace_DAO traceDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<User> getData(String username, String password) {

		return traceDao.getData(username, password);
	}

	@Override
	public String getMenuData(String username, String roleID, String clientID) {

		return traceDao.getMenuData(username, roleID, clientID);
	}

	@Override
	public String getSubMenuData(String username, String roleID, String clientID) {

		return traceDao.getSubMenuData(username, roleID, clientID);
	}

	@Override
	public String resetPassword() {

		return traceDao.resetPassword();
	}

	@Override
	public String getRoleDetails(String clientID) {

		return traceDao.getRoleDetails(clientID);
	}

	@Override
	public String getRoleMaster(String roleName, String homePage, String mode, String roleID, String clientID) {

		return traceDao.getRoleMaster(roleName, homePage, mode, roleID, clientID);
	}

	@Override
	public String getRoleAccessRights(String roleID, String clientID) {

		return traceDao.getRoleAccessRights(roleID, clientID);
	}

	@Override
	public String getBranchDetails(String clientID, String branchID) {

		return traceDao.getBranchDetails(clientID, branchID);
	}

	@Override
	public String AssignRoleAccessRights(String roleID, String clientID, String menuString, String username) {

		return traceDao.AssignRoleAccessRights(roleID, clientID, menuString, username);
	}

	@Override
	public String getUserDetails(String username, String clientID, String branchID, String roleID) {

		return traceDao.getUserDetails(username, clientID, branchID, roleID);
	}

	@Override
	public String AddUser(String userName, String password, String firstName, String lastName, String roleID,
			String clientID, String branchID, String emailID, String contactNo, String securityq, String securitya,
			String createBy, String p_salt, String channel) {

		return traceDao.AddUser(userName, password, firstName, lastName, roleID, clientID, branchID, emailID, contactNo,
				securityq, securitya, createBy, p_salt, channel);
	}

	@Override
	public List<JSONObject> getUploadFiletype() {

		return traceDao.getUploadFiletype();
	}

	@Override
	public List<JSONObject> getChannelID() {

		return traceDao.getChannelID();
	}

	public String addUser(String userid, String password, String firstname, String lastname, String roleid,
			String clientid, String branchid, String emailid, String contactno, String securityq, String securitya,
			String createdby, String salt, String channel) {

		return traceDao.addUser(userid, password, firstname, lastname, roleid, clientid, branchid, emailid, contactno,
				securityq, securitya, createdby, salt, channel);
	}

	@Override
	public String deleteUser(String userid, String roleid, String branchid, String createdby) {

		return traceDao.deleteUser(userid, roleid, branchid, createdby);
	}

	@Override
	public String addUserRole(String userid, String roleid, String clientid, String createdby) {

		return traceDao.addUserRole(userid, roleid, clientid, createdby);
	}

	@Override
	public String addUserUpdate(String userid, String roleid, String branchid, String emailid, String createdby,
			String channel) {

		return traceDao.addUserUpdate(userid, roleid, branchid, emailid, createdby, channel);
	}

	@Override
	public List<JSONObject> getDomainTypeList() {

		return traceDao.getDomainTypeList();
	}

	@Override
	public List<JSONObject> getModuleTypeList() {

		return traceDao.getModuleTypeList();
	}

	@Override
	public List<JSONObject> getClientNameList() {

		return traceDao.getClientNameList();
	}

	@Override
	public List<JSONObject> getVendorTypeList() {

		return traceDao.getVendorTypeList();
	}

	@Override
	public List<JSONObject> getVendorDetails() {

		return traceDao.getVendorDetails();
	}

	@Override
	public String vendorMasterModes(String mode, String vendorid, String vendorname, String vendortypeid,
			String createdby, String vendor) {

		return traceDao.vendorMasterModes(mode, vendorid, vendorname, vendortypeid, createdby, vendor);
	}

	@Override
	public List<JSONObject> getContryTypeList() {

		return traceDao.getContryTypeList();
	}

	@Override
	public List<JSONObject> getCurrencyDetails() {

		return traceDao.getCurrencyDetails();
	}

	@Override
	public String currencyMasterModes(String mode, String currencyid, String currencycode, String currencydescription,
			String countryid, String countryname, String numericcode, String createdby, String country, String scale) {

		return traceDao.currencyMasterModes(mode, currencyid, currencycode, currencydescription, countryid, countryname,
				numericcode, createdby, country, scale);
	}

	@Override
	public String clientConfigMaster(String clientid, String mode, String domainid, String moduleid, String clientcode,
			String clientname, String address, String contactno, String emailid, String concernperson, String cpemailid,
			String cpcontactno, String isbank, String isactive, String countryid, String currencyid, String ftp_ip,
			String ftpusername, String ftppassword, String ftpport, /* byte[] logoArray */MultipartFile logofile,
			String userlimit, String terminalcount, String reporttime, String username, String colorcode,
			String logoName) {

		return traceDao.clientConfigMaster(clientid, mode, domainid, moduleid, clientcode, clientname, address,
				contactno, emailid, concernperson, cpemailid, cpcontactno, isbank, isactive, countryid, currencyid,
				ftp_ip, ftpusername, ftppassword, ftpport, logofile, userlimit, terminalcount, reporttime, username,
				colorcode, logoName);
	}

	@Override
	public List<JSONObject> getclientcurrency(String countryid) {

		return traceDao.getclientcurrency(countryid);
	}

	@Override
	public List<JSONObject> getClientCode(String clientcode) {

		return traceDao.getClientCode(clientcode);
	}

	@Override
	public List<JSONObject> getclientmastermodeget(String clientid, String mode, String domainid, String moduleid) {

		return traceDao.getclientmastermodeget(clientid, mode, domainid, moduleid);
	}

	@Override
	public byte[] getBranchFile(String clientid) {

		return traceDao.getBranchFile(clientid);
	}

	@Override
	public byte[] getTerminalFile(String clientid) {

		return traceDao.getTerminalFile(clientid);
	}

	@Override
	public List mapBranchMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user) {

		return traceDao.mapBranchMasterReapExcelDatatoDB(reapExcelDataFile, user);
	}

	@Override
	public List mapTerminalMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user) {

		return traceDao.mapTerminalMasterReapExcelDatatoDB(reapExcelDataFile, user);
	}

	@Override
	public List<JSONObject> getClientCode() {

		return traceDao.getClientCode();
	}

	@Override
	public String clientchannelmodeinsert(String channelid, String modeid, String clientcode, String user) {

		return traceDao.clientchannelmodeinsert(channelid, modeid, clientcode, user);
	}

	@Override
	public List<JSONObject> getChannelData(String clientid) {

		return traceDao.getChannelData(clientid);
	}

	@Override
	public List<JSONObject> updateChangePwd(String userid, String clientcode, String oldpassword, String newpassword,
			String confirmpassword, String createdby) {

		byte[] salt = getSalt();
		String securePassword = get_SHA_512_SecurePassword(newpassword, salt);
		return traceDao.updateChangePwdwithsalt(userid, clientcode, oldpassword, securePassword, confirmpassword,
				createdby);
	}

	private String get_SHA_512_SecurePassword(String clientid, byte[] salt) {

		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(clientid.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	private byte[] getSalt() {

		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	@Override
	public List<JSONObject> getLogList(String clientid) {

		return traceDao.getLogList(clientid);
	}

	@Override
	public List<JSONObject> getModeType(String clientid, String channelid) {

		return traceDao.getModeType(clientid, channelid);
	}

	@Override
	public List<JSONObject> getinsertfileformat(String p_CLIENTID, String p_VENDORID, String p_FILEEXT,
			String p_FILEXML, String p_CUTOFFTIME, String p_USER, String p_FILEPREFIX, String p_VENDORTYPE,
			String p_CHANNELID, String p_MODEID, String p_SEPARATORTYPE) {

		return traceDao.getinsertfileformat(p_CLIENTID, p_VENDORID, p_FILEEXT, p_FILEXML, p_CUTOFFTIME, p_USER,
				p_FILEPREFIX, p_VENDORTYPE, p_CHANNELID, p_MODEID, p_SEPARATORTYPE);
	}

	@Override
	public List<JSONObject> getchanneltype(String clientid, String userid) {

		return traceDao.getchanneltype(clientid, userid);
	}

	@Override
	public List<JSONObject> getVendorDetailsByType(String vendortype) {

		return traceDao.getVendorDetailsByType(vendortype);
	}

	@Override
	public List<JSONObject> getfileformatclient(String clientid) {

		return traceDao.getfileformatclient(clientid);
	}

	public List<String> getXmlFields(NodeList nodeList, String xmlString, int j) {
		List<String> returnNodeList = null;

		String nodeName = nodeList.item(j).getNodeName();
		if (xmlString.equalsIgnoreCase(nodeName)) {
			Node childNode = nodeList.item(j);
			NodeList childNodeList = childNode.getChildNodes();

			String startPos = childNodeList.item(0).getNodeName();
			Node startPosNode = childNodeList.item(0);

			NodeList startPosNodeValue = startPosNode.getChildNodes();
			String startPosNodeValueNode = startPosNodeValue.item(0).getNodeValue();

			String length = childNodeList.item(1).getNodeName();
			Node lengthNode = childNodeList.item(1);
			NodeList lengthNodeValue = lengthNode.getChildNodes();
			String lengthNodeValueNode = lengthNodeValue.item(0).getNodeValue();

			returnNodeList = new ArrayList<String>(3);

			returnNodeList.add(nodeName);
			returnNodeList.add(startPosNodeValueNode);
			returnNodeList.add(lengthNodeValueNode);
			return returnNodeList;
		}

		return null;
	}

	@Override
	public List<JSONObject> importFileNpciATMFiles(MultipartFile file, String clientid, String createdby)
			throws Exception {
		List<String> content = null;
		String fileName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());

		if (fileName.contains("ACQ")) {
			try {
				String participant_ID = null, transaction_Type = null, from_Account_Type = null, to_Account_Type = null,
						RRN = null, response_Code = null, card_number = null, member_Number = null,
						approval_Number = null, system_Trace_Audit_Number = null, transaction_Date = null,
						transaction_Time = null, merchant_Category_Code = null, card_Acceptor_Settlement_Date = null,
						card_Acceptor_ID = null, card_Acceptor_Terminal_ID = null,
						card_Acceptor_Terminal_Location = null, acquirer_ID = null, acquirer_Settlement_Date = null,
						transaction_Currency_code = null, transaction_Amount = null, actual_Transaction_Amount = null,
						transaction_Acitivity_fee = null, acquirer_settlement_Currency_Code = null,
						acquirer_settlement_Amount = null, acquirer_Settlement_Fee = null,
						acquirer_settlement_processing_fee = null, transaction_Acquirer_Conversion_Rate = null;
				Connection con = dataSource.getConnection();
				System.out.println("Con  " + con);
				CallableStatement stmt = con.prepareCall(
						"{call SPIMPORTNPCIACQUIEREFILE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				List<JSONObject> npcifileformatxml = traceDao.getformatfileinxml(clientid, 2);
				JSONObject xmlFormatDescription = npcifileformatxml.get(0);
				String tempStr = xmlFormatDescription.get("FormatDescriptionXml").toString();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(new StringReader(tempStr)));
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getDocumentElement().getChildNodes();
				
				
				File convFile = new File(file.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
				content = Files.readAllLines(convFile.toPath());
				String contentData = "";
				String ForceMatch = file.getOriginalFilename();
				String cycle = ForceMatch.substring(15, 17);
				String fileDate = ForceMatch.substring(8, 14).trim();
				List<JSONObject> importFileNpciATMFilesACQ = new ArrayList<JSONObject>();
				JSONObject obj = new JSONObject();
				JSONObject obj1 = new JSONObject();
				int count = 0, batchSize = 30000;
				long start = System.currentTimeMillis();
				for (int i = 0; i < content.size(); i++) {
					contentData = content.get(i);
					for (int j = 0; j < nodeList.getLength(); j++) {
						List<String> nodeData = getXmlFields(nodeList, nodeList.item(j).getNodeName(), j);
						String nodeName = nodeList.item(j).getNodeName();
						int startPos = Integer.parseInt(nodeData.get(1));
						int length = Integer.parseInt(nodeData.get(2));
						String contentFieldData = contentData.substring(startPos - 1, (startPos - 1) + length).trim();
						obj.put(nodeName, contentFieldData);
					}
					String nodeName = null;

					stmt.setString(1, clientid);
					nodeName = nodeList.item(0).getNodeName();
					if (obj.containsKey(nodeName)) {
						participant_ID = obj.get(nodeName).toString();
						stmt.setString(2, participant_ID);
					}

					nodeName = nodeList.item(1).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Type = obj.get(nodeName).toString();

						stmt.setString(3, transaction_Type);
					}

					nodeName = nodeList.item(2).getNodeName();
					if (obj.containsKey(nodeName)) {
						from_Account_Type = obj.get(nodeName).toString();
						stmt.setString(4, from_Account_Type);
					}

					nodeName = nodeList.item(3).getNodeName();
					if (obj.containsKey(nodeName)) {
						to_Account_Type = obj.get(nodeName).toString();

						stmt.setString(5, to_Account_Type);
					}

					nodeName = nodeList.item(4).getNodeName();
					if (obj.containsKey(nodeName)) {
						RRN = obj.get(nodeName).toString();
//						obj1.put("RRN", RRN);
						stmt.setString(6, RRN);
					}
					nodeName = nodeList.item(5).getNodeName();
					if (obj.containsKey(nodeName)) {
						response_Code = obj.get(nodeName).toString();
//						obj1.put("response_Code", response_Code);
						stmt.setString(7, response_Code);
					}
					nodeName = nodeList.item(6).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_number = obj.get(nodeName).toString();
//						obj1.put("card_number", card_number);
						stmt.setString(8, card_number);
					}
					nodeName = nodeList.item(7).getNodeName();
					if (obj.containsKey(nodeName)) {
						member_Number = obj.get(nodeName).toString();
//						obj1.put("member_Number", member_Number);
						stmt.setString(9, member_Number);
					}
					nodeName = nodeList.item(8).getNodeName();
					if (obj.containsKey(nodeName)) {
						approval_Number = obj.get(nodeName).toString();
//						obj1.put("approval_Number", approval_Number);
						stmt.setString(10, approval_Number);
					}
					nodeName = nodeList.item(9).getNodeName();
					if (obj.containsKey(nodeName)) {
						system_Trace_Audit_Number = obj.get(nodeName).toString();
//						obj1.put("system_Trace_Audit_Number", system_Trace_Audit_Number);
						stmt.setString(11, system_Trace_Audit_Number);
					}
					nodeName = nodeList.item(10).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Date = obj.get(nodeName).toString();
//						obj1.put("transaction_Date", transaction_Date);
						stmt.setString(12, transaction_Date);
					}
					nodeName = nodeList.item(11).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Time = obj.get(nodeName).toString();
//						obj1.put("transaction_Time", transaction_Time);
						stmt.setString(13, transaction_Time);
					}
					nodeName = nodeList.item(12).getNodeName();
					if (obj.containsKey(nodeName)) {
						merchant_Category_Code = obj.get(nodeName).toString();
//						obj1.put("merchant_Category_Code", merchant_Category_Code);
						stmt.setString(14, merchant_Category_Code);
					}
					nodeName = nodeList.item(13).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Settlement_Date = obj.get(nodeName).toString();
//						obj1.put("card_Acceptor_Settlement_Date", card_Acceptor_Settlement_Date);
						stmt.setString(15, card_Acceptor_Settlement_Date);
					}
					nodeName = nodeList.item(14).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_ID = obj.get(nodeName).toString();
//						obj1.put("card_Acceptor_ID", card_Acceptor_ID);
						stmt.setString(16, card_Acceptor_ID);
					}
					nodeName = nodeList.item(15).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Terminal_ID = obj.get(nodeName).toString();
						stmt.setString(17, card_Acceptor_Terminal_ID);
					}
					nodeName = nodeList.item(16).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Terminal_Location = obj.get(nodeName).toString();
						stmt.setString(18, card_Acceptor_Terminal_Location);
					}
					nodeName = nodeList.item(17).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_ID = obj.get(nodeName).toString();

						stmt.setString(19, acquirer_ID);
					}
					nodeName = nodeList.item(18).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_Settlement_Date = obj.get(nodeName).toString();
						stmt.setString(20, acquirer_Settlement_Date);
					}
					nodeName = nodeList.item(19).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Currency_code = obj.get(nodeName).toString();
						stmt.setString(21, transaction_Currency_code);
					}
					nodeName = nodeList.item(20).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Amount = obj.get(nodeName).toString();
						stmt.setString(22, transaction_Amount);
					}
					nodeName = nodeList.item(21).getNodeName();
					if (obj.containsKey(nodeName)) {
						actual_Transaction_Amount = obj.get(nodeName).toString();
						stmt.setString(23, actual_Transaction_Amount);
					}
					nodeName = nodeList.item(22).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Acitivity_fee = obj.get(nodeName).toString();
						stmt.setString(24, transaction_Acitivity_fee);
					}
					nodeName = nodeList.item(23).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_settlement_Currency_Code = obj.get(nodeName).toString();
						stmt.setString(25, acquirer_settlement_Currency_Code);
					}
					nodeName = nodeList.item(24).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_settlement_Amount = obj.get(nodeName).toString();
						stmt.setString(26, acquirer_settlement_Amount);
					}
					nodeName = nodeList.item(25).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_Settlement_Fee = obj.get(nodeName).toString();
						stmt.setString(27, acquirer_Settlement_Fee);
					}
					nodeName = nodeList.item(26).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_settlement_processing_fee = obj.get(nodeName).toString();
						stmt.setString(28, acquirer_settlement_processing_fee);
					}
					nodeName = nodeList.item(27).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Acquirer_Conversion_Rate = obj.get(nodeName).toString();
						stmt.setString(29, transaction_Acquirer_Conversion_Rate);
					}
					stmt.setString(30, ForceMatch);
					stmt.setString(31, cycle);
					stmt.setString(32, fileDate);

					stmt.setString(33, createdby);
					stmt.addBatch();
					count++;
					System.out.println("count: " + count);
					if (count % batchSize == 0 || count == content.size()) {
						stmt.executeBatch();
						long end = System.currentTimeMillis();
						System.out.println("TIME:  " + (end - start));
					}
				}
				stmt.close();
				con.close();
				return importFileNpciATMFilesACQ;
			} catch (Exception e) {
				return null;
			}
		} else if (fileName.contains("ISS")) {
			Connection con = dataSource.getConnection();
			CallableStatement stmt = con.prepareCall(
					"{call SPIMPORTNPCIISSUERFILE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			String participant_ID = null;
			String transaction_Type = null;
			String from_Account_Type = null;
			String to_Account_Type = null;
			String RRN = null;
			String response_Code = null;
			String card_number = null;
			String member_Number = null;
			String approval_Number = null;
			String system_Trace_Audit_Number = null;
			String transaction_Date = null;
			String transaction_Time = null;
			String merchant_Category_Code = null;
			String card_Acceptor_Settlement_Date = null;
			String card_Acceptor_ID = null;
			String card_Acceptor_Terminal_ID = null;
			String card_Acceptor_Terminal_Location = null;
			String acquirer_ID = null;
			String networkID = null;
			String accountNo1 = null;
			String account1BranchId = null;
			String accountNo2 = null;
			String account2BranchId = null;
			String transCurrencyCode = null;
			String txnAmount = null;
			String actualTransAmount = null;
			String tranActivityFee = null;
			String issuerSetCurrencyCode = null;
			String issuerSetAmount = null;
			String issuerSetFee = null;
			String issuerSetProcFee = null;
			String cardHolderBillcurnCode = null;
			String cardHolderBillAmount = null;
			String cardHolderBillActFee = null;
			String cardHolderBillProcFee = null;
			String cardHolderBillServiceFee = null;
			String tRAN_ISSUERCONVERSRATE = null;
			String tRANS_CARDHOLDERCONVERRATE = null;
			
			try {
				List<JSONObject> npcifileformatxml = traceDao.getformatfileinxml(clientid, 3);
				JSONObject xmlFormatDescription = npcifileformatxml.get(0);
				String tempStr = xmlFormatDescription.get("FormatDescriptionXml").toString();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(new StringReader(tempStr)));
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getDocumentElement().getChildNodes();
				File convFile = new File(file.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
				content = Files.readAllLines(convFile.toPath());
				List<JSONObject> importFileNpciATMFilesISS = null;
				JSONObject obj = new JSONObject();
				String contentData = "";
				int count = 0, batchSize = 30000;
				String ForceMatch = file.getOriginalFilename();
				String cycle = ForceMatch.substring(15, 17);
				String fileDate = ForceMatch.substring(8, 14).trim();
				long start = System.currentTimeMillis();
				for (int i = 0; i < content.size(); i++) {
					contentData = content.get(i);
					for (int j = 0; j < nodeList.getLength(); j++) {
						List<String> nodeData = getXmlFields(nodeList, nodeList.item(j).getNodeName(), j);
						String nodeName = nodeList.item(j).getNodeName();
						int startPos = Integer.parseInt(nodeData.get(1));
						int length = Integer.parseInt(nodeData.get(2));
						String contentFieldData = contentData.substring(startPos - 1, (startPos - 1) + length).trim();
						obj.put(nodeName, contentFieldData);
						System.out.println("nodeName:  "+nodeName+"   contentFieldData  "+contentFieldData);
					}
//					importFileNpciATMFilesISS = traceDao.importFileNpciATMFilesISS(obj, clientid, fileDate, cycle,
//							ForceMatch, createdby, nodeList);
					String nodeName = null;
					nodeName = nodeList.item(0).getNodeName();
					if (obj.containsKey(nodeName)) {
						participant_ID = obj.get(nodeName).toString();
//						System.out.println("participant_ID: "+participant_ID);
					}

					nodeName = nodeList.item(1).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Type = obj.get(nodeName).toString();
//						System.out.println("transaction_Type: "+transaction_Type);
					}

					nodeName = nodeList.item(2).getNodeName();
					if (obj.containsKey(nodeName)) {
						from_Account_Type = obj.get(nodeName).toString();
					}

					nodeName = nodeList.item(3).getNodeName();
					if (obj.containsKey(nodeName)) {
						to_Account_Type = obj.get(nodeName).toString();
					}

					nodeName = nodeList.item(4).getNodeName();
					if (obj.containsKey(nodeName)) {
						RRN = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(5).getNodeName();
					if (obj.containsKey(nodeName)) {
						response_Code = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(6).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_number = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(7).getNodeName();
					if (obj.containsKey(nodeName)) {
						member_Number = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(8).getNodeName();
					if (obj.containsKey(nodeName)) {
						approval_Number = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(9).getNodeName();
					if (obj.containsKey(nodeName)) {
						system_Trace_Audit_Number = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(10).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Date = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(11).getNodeName();
					if (obj.containsKey(nodeName)) {
						transaction_Time = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(12).getNodeName();
					if (obj.containsKey(nodeName)) {
						merchant_Category_Code = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(13).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Settlement_Date = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(14).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_ID = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(15).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Terminal_ID = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(16).getNodeName();
					if (obj.containsKey(nodeName)) {
						card_Acceptor_Terminal_Location = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(17).getNodeName();
					if (obj.containsKey(nodeName)) {
						acquirer_ID = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(18).getNodeName();
					if (obj.containsKey(nodeName)) {
						networkID = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(19).getNodeName();
					if (obj.containsKey(nodeName)) {
						accountNo1 = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(20).getNodeName();
					if (obj.containsKey(nodeName)) {
						account1BranchId = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(21).getNodeName();
					if (obj.containsKey(nodeName)) {
						accountNo2 = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(22).getNodeName();
					if (obj.containsKey(nodeName)) {
						account2BranchId = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(23).getNodeName();
					if (obj.containsKey(nodeName)) {
						transCurrencyCode = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(24).getNodeName();
					if (obj.containsKey(nodeName)) {
						txnAmount = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(25).getNodeName();
					if (obj.containsKey(nodeName)) {
						actualTransAmount = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(26).getNodeName();
					if (obj.containsKey(nodeName)) {
						tranActivityFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(27).getNodeName();
					if (obj.containsKey(nodeName)) {
						issuerSetCurrencyCode = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(28).getNodeName();
					if (obj.containsKey(nodeName)) {
						issuerSetAmount = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(29).getNodeName();
					if (obj.containsKey(nodeName)) {
						issuerSetFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(30).getNodeName();
					if (obj.containsKey(nodeName)) {
						issuerSetProcFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(31).getNodeName();
					if (obj.containsKey(nodeName)) {
						cardHolderBillcurnCode = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(32).getNodeName();
					if (obj.containsKey(nodeName)) {
						cardHolderBillAmount = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(33).getNodeName();
					if (obj.containsKey(nodeName)) {
						cardHolderBillActFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(34).getNodeName();
					if (obj.containsKey(nodeName)) {
						cardHolderBillProcFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(35).getNodeName();
					if (obj.containsKey(nodeName)) {
						cardHolderBillServiceFee = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(36).getNodeName();
					if (obj.containsKey(nodeName)) {
						tRAN_ISSUERCONVERSRATE = obj.get(nodeName).toString();
					}
					nodeName = nodeList.item(37).getNodeName();
					if (obj.containsKey(nodeName)) {
						tRANS_CARDHOLDERCONVERRATE = obj.get(nodeName).toString();
					}	
					stmt.setString(1, clientid);
					stmt.setString(2, participant_ID);
					stmt.setString(3, transaction_Type);
					stmt.setString(4, from_Account_Type);
					stmt.setString(5, to_Account_Type);
					stmt.setString(6, RRN);
					stmt.setString(7, response_Code);
					stmt.setString(8, card_number);
					stmt.setString(9, member_Number);
					stmt.setString(10, approval_Number);
					stmt.setString(11, system_Trace_Audit_Number);
					stmt.setString(12, transaction_Date);
					stmt.setString(13, transaction_Time);
					stmt.setString(14, merchant_Category_Code);
					stmt.setString(15, card_Acceptor_Settlement_Date);
					stmt.setString(16, card_Acceptor_ID);
					stmt.setString(17, card_Acceptor_Terminal_ID);
					stmt.setString(18, card_Acceptor_Terminal_Location);
					stmt.setString(19, acquirer_ID);
					stmt.setString(20, networkID);
					stmt.setString(21, accountNo1);
					stmt.setString(22, account1BranchId);
					stmt.setString(23, accountNo2);
					stmt.setString(24, account2BranchId);
					stmt.setString(25, transCurrencyCode);
					stmt.setString(26, txnAmount);
					stmt.setString(27, actualTransAmount);
					stmt.setString(28, tranActivityFee);
					stmt.setString(29, issuerSetCurrencyCode);
					stmt.setString(30, issuerSetAmount);
					stmt.setString(31, issuerSetFee);
					stmt.setString(32, issuerSetProcFee);
					stmt.setString(33, cardHolderBillcurnCode);
					stmt.setString(34, cardHolderBillAmount);
					stmt.setString(35, cardHolderBillActFee);
					stmt.setString(36, cardHolderBillProcFee);
					stmt.setString(37, cardHolderBillServiceFee);
					stmt.setString(38, tRAN_ISSUERCONVERSRATE);
					stmt.setString(39, tRANS_CARDHOLDERCONVERRATE);
					stmt.setString(40, cycle);
					stmt.setString(41, fileDate);
					stmt.setString(42, ForceMatch);
					stmt.setString(43, createdby);
					stmt.addBatch();
					count++;
					System.out.println("count: " + count);
					if (count % batchSize == 0 || count == content.size()) {
						stmt.executeBatch();
						long end = System.currentTimeMillis();
						System.out.println("TIME:  " + (end - start));
					}
				}
				return importFileNpciATMFilesISS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (ext.equalsIgnoreCase("xls")) {
			Workbook tempWorkBook = null;
			try {
				tempWorkBook = new HSSFWorkbook(file.getInputStream());
			} catch (IOException e) {

				e.printStackTrace();
			}
			Sheet sheet = tempWorkBook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int dec = 0, setAmt = 0;
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				int tempDec = 0;
				int tempSetamt = 0;
				Row row = sheet.getRow(i);
				for (Cell cell : row) {
					CellReference cellRef1 = new CellReference(row.getRowNum(), cell.getColumnIndex());
					String text = formatter.formatCellValue(cell);
					if ("Description".equals(text)) {
						tempDec = cellRef1.getRow();
					}
				}
				for (Cell cell : row) {
					CellReference cellRef1 = new CellReference(row.getRowNum(), cell.getColumnIndex());
					String text = formatter.formatCellValue(cell);

					if ("Final Settlement Amount".equals(text)) {
						tempSetamt = cellRef1.getRow();
					}
				}
				if (tempDec == 0) {
				} else {
					dec = tempDec;
				}
				if (tempSetamt == 0) {
				} else {
					setAmt = tempSetamt;
				}
				if (dec != 0 && setAmt != 0) {
					ntslAtmFile(sheet, dec, setAmt, file, clientid, createdby);
					dec = 0;
					setAmt = 0;
				}

			}

		}

		return null;
	}
	private void ntslAtmFile(Sheet sheet, int dec2, int setAmt, MultipartFile file, String clientid, String createdby) {
		HSSFRow tempRow = null, forTitle = null;
		String description = null;
		double noOftxn = 0, debit = 0, credit = 0;
		forTitle = (HSSFRow) sheet.getRow(dec2 - 2);
		String titleString = forTitle.getCell(0).getStringCellValue();
		String date = titleString.substring(titleString.length() - 10);
		for (int i = dec2 + 1; i < setAmt + 1; i++) {
			tempRow = (HSSFRow) sheet.getRow(i);
			if (tempRow.getCell(0) == null) {
			} else {
				description = tempRow.getCell(0).getStringCellValue();
			}
			if (tempRow.getCell(1) == null) {
				noOftxn = 0;
			} else {
				noOftxn = tempRow.getCell(1).getNumericCellValue();
			}
			if (tempRow.getCell(2) == null) {
				debit = 0;
			} else {
				debit = tempRow.getCell(2).getNumericCellValue();
			}
			if (tempRow.getCell(3) == null) {
				credit = 0;
			} else {
				credit = tempRow.getCell(3).getNumericCellValue();
			}
			traceDao.ntsAtmFile(description, noOftxn, credit, debit, file, date, clientid, createdby);
		}
	}

	@Override
	public List<JSONObject> getFileFormatHistory(String p_VendorType, String p_ClientID, String p_ChannelID,
			String p_ModeID, String p_VendorID) {

		return traceDao.getFileFormatHistory(p_VendorType, p_ClientID, p_ChannelID, p_ModeID, p_VendorID);
	}

	@Override
	public List<JSONObject> getfileformat(String p_VENDORID, String p_CLIENTID, String p_FILEPREFIX, String p_FILEEXT,
			String p_SEPARATORTYPE, String p_MODEID, String p_CHANNELID) {

		return traceDao.getfileformat(p_VENDORID, p_CLIENTID, p_FILEPREFIX, p_FILEEXT, p_SEPARATORTYPE, p_MODEID,
				p_CHANNELID);
	}

	@Override
	public List<JSONObject> importFileIMPSFiles(MultipartFile imps, String clientid, String createdby) {

		List<String> content = null;
		String fileName = imps.getOriginalFilename();
		if (fileName.contains("ACQ")) {
			try {

				File convFile = new File(imps.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(imps.getBytes());
				fos.close();
				content = Files.readAllLines(convFile.toPath());
				String participant_ID = "";
				String transaction_Type = "";
				String from_Account_Type = "";
				String to_Account_Type = "";
				String transaction_Serial_Number = "";
				String response_Code = "";
				String pan_Number = "";
				String member_Number = "";
				String approval_Number = "";
				String system_Trace_Audit_Number = "";
				String transaction_Date = "";
				String transaction_Time = "";
				String merchant_Category_Code = "";
				String card_Acceptor_Settlement_Date = "";
				String card_Acceptor_ID = "";
				String card_Acceptor_Terminal_ID = "";
				String card_Acceptor_Terminal_Location = "";
				String acquirer_ID = "";
				String acquirer_Settlement_Date = "";
				String transaction_Currency_code = "";
				String transaction_Amount = "";
				String actual_Transaction_Amount = "";
				String transaction_Acitivity_fee = "";
				String acquirer_settlement_Currency_Code = "";
				String acquirer_settlement_Amount = "";
				String acquirer_Settlement_Fee = "";
				String acquirer_settlement_processing_fee = "";
				String transaction_Acquirer_Conversion_Rate = "";
				String cycle = "";
				String fileDate = "";
				String contentData = "";
				String ForceMatch = "";
				System.out.println("before loop");
				List<JSONObject> importFileNpciATMFilesACQ = null;
				// List<NpciAcqModel> npci=new ArrayList<>();
				for (int i = 0; i < content.size(); i++) {
					contentData = content.get(i);
					participant_ID = contentData.substring(0, 3).trim();
					transaction_Type = contentData.substring(3, 5).trim();
					from_Account_Type = contentData.substring(5, 7).trim();
					to_Account_Type = contentData.substring(7, 9).trim();
					transaction_Serial_Number = contentData.substring(9, 12).trim();
					response_Code = contentData.substring(21, 23).trim();
					pan_Number = contentData.substring(23, 42).trim();
					member_Number = contentData.substring(42, 43).trim();
					approval_Number = contentData.substring(43, 49).trim();
					system_Trace_Audit_Number = contentData.substring(49, 61).trim();
					transaction_Date = contentData.substring(61, 67).trim();
					transaction_Time = contentData.substring(67, 73).trim();

					merchant_Category_Code = contentData.substring(73, 77).trim();

					card_Acceptor_Settlement_Date = contentData.substring(77, 83).trim();
					card_Acceptor_ID = contentData.substring(83, 98).trim();
					card_Acceptor_Terminal_ID = contentData.substring(98, 106).trim();
					card_Acceptor_Terminal_Location = contentData.substring(106, 146).trim();
					acquirer_ID = contentData.substring(146, 157).trim();
					acquirer_Settlement_Date = contentData.substring(157, 163).trim();
					transaction_Currency_code = contentData.substring(163, 166).trim();
					transaction_Amount = contentData.substring(166, 181).trim();
					actual_Transaction_Amount = contentData.substring(181, 196).trim();
					transaction_Acitivity_fee = contentData.substring(196, 211).trim();
					acquirer_settlement_Currency_Code = contentData.substring(211, 214).trim();
					acquirer_settlement_Amount = contentData.substring(214, 229).trim();
					acquirer_Settlement_Fee = contentData.substring(229, 244).trim();
					acquirer_settlement_processing_fee = contentData.substring(244, 259).trim();
					transaction_Acquirer_Conversion_Rate = contentData.substring(259, 274).trim();

					ForceMatch = imps.getOriginalFilename();
					cycle = ForceMatch.substring(15, 17).trim();
					fileDate = ForceMatch.substring(8, 14).trim();

					System.out.println("started");

					System.out.println("1participant_ID" + participant_ID);
					System.out.println("2transaction_Type" + transaction_Type);
					System.out.println("3from_Account_Type" + from_Account_Type);
					System.out.println("4to_Account_Type" + to_Account_Type);
					System.out.println("5transaction_Serial_Number" + transaction_Serial_Number);
					System.out.println("6response_Code" + response_Code);
					System.out.println("7pan_Number" + pan_Number);
					System.out.println("8member_Number" + member_Number);
					System.out.println("9approval_Number" + approval_Number);
					System.out.println("10system_Trace_Audit_Number" + system_Trace_Audit_Number);
					System.out.println("11transaction_Date" + transaction_Date);
					System.out.println("12transaction_Time" + transaction_Time);
					System.out.println("13merchant_Category_Code" + merchant_Category_Code);
					System.out.println("14card_Acceptor_Settlement_Date" + card_Acceptor_Settlement_Date);
					System.out.println("15card_Acceptor_ID" + card_Acceptor_ID);
					System.out.println("16card_Acceptor_Terminal_ID" + card_Acceptor_Terminal_ID);
					System.out.println("17card_Acceptor_Terminal_Location" + card_Acceptor_Terminal_Location);
					System.out.println("18acquirer_ID" + acquirer_ID);
					System.out.println("19acquirer_Settlement_Date" + acquirer_Settlement_Date);
					System.out.println("20transaction_Currency_code" + transaction_Currency_code);
					System.out.println("21transaction_Amount" + transaction_Amount);
					System.out.println("22actual_Transaction_Amount" + actual_Transaction_Amount);
					System.out.println("23transaction_Acitivity_fee" + transaction_Acitivity_fee);
					System.out.println("24acquirer_settlement_Currency_Code" + acquirer_settlement_Currency_Code);
					System.out.println("25acquirer_settlement_Amount" + acquirer_settlement_Amount);
					System.out.println("26acquirer_Settlement_Fee" + acquirer_Settlement_Fee);
					System.out.println("27acquirer_settlement_processing_fee" + acquirer_settlement_processing_fee);
					System.out.println("28transaction_Acquirer_Conversion_Rate" + transaction_Acquirer_Conversion_Rate);
					System.out.println("filedate" + fileDate);
					System.out.println("end");
//					importFileNpciATMFilesACQ = traceDao.importFileNpciATMFilesACQ(participant_ID, transaction_Type,
//							from_Account_Type, to_Account_Type, transaction_Serial_Number, response_Code, pan_Number,
//							member_Number, approval_Number, system_Trace_Audit_Number, transaction_Date,
//							transaction_Time, merchant_Category_Code, card_Acceptor_Settlement_Date, card_Acceptor_ID,
//							card_Acceptor_Terminal_ID, card_Acceptor_Terminal_Location, acquirer_ID,
//							acquirer_Settlement_Date, transaction_Currency_code, transaction_Amount,
//							actual_Transaction_Amount, transaction_Acitivity_fee, acquirer_settlement_Currency_Code,
//							acquirer_settlement_Amount, acquirer_Settlement_Fee, acquirer_settlement_processing_fee,
//							transaction_Acquirer_Conversion_Rate, ForceMatch, clientid, cycle, fileDate, createdby);
				}

				return importFileNpciATMFilesACQ;
			} catch (Exception e) {
				return null;
			}
		} else if (fileName.contains("ISS")) {
			try {

				File convFile = new File(imps.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(imps.getBytes());
				fos.close();
				content = Files.readAllLines(convFile.toPath());
				String participant_ID = "";
				String transaction_Type = "";
				String from_Account_Type = "";
				String to_Account_Type = "";
				String transaction_Serial_Number = "";
				String response_Code = "";
				String pan_Number = "";
				String member_Number = "";
				String approval_Number = "";
				String system_Trace_Audit_Number = "";
				String transaction_Date = "";
				String transaction_Time = "";
				String merchant_Category_Code = "";
				String card_Acceptor_Settlement_Date = "";
				String card_Acceptor_ID = "";
				String card_Acceptor_Terminal_ID = "";
				String card_Acceptor_Terminal_Location = "";
				String acquirer_ID = "";

				String contentData = "";
				for (int i = 0; i < content.size(); i++) {
					contentData = content.get(i);
					participant_ID = contentData.substring(0, 3).trim();
					transaction_Type = contentData.substring(3, 5).trim();
					from_Account_Type = contentData.substring(5, 7).trim();
					to_Account_Type = contentData.substring(7, 9).trim();
					transaction_Serial_Number = contentData.substring(9, 12).trim();
					response_Code = contentData.substring(21, 23).trim();
					pan_Number = contentData.substring(23, 42).trim();
					member_Number = contentData.substring(42, 43).trim();
					approval_Number = contentData.substring(43, 49).trim();
					system_Trace_Audit_Number = contentData.substring(49, 61).trim();
					transaction_Date = contentData.substring(61, 67).trim();
					transaction_Time = contentData.substring(67, 73).trim();

					merchant_Category_Code = contentData.substring(73, 77).trim();

					card_Acceptor_Settlement_Date = contentData.substring(77, 83).trim();
					card_Acceptor_ID = contentData.substring(83, 98).trim();
					card_Acceptor_Terminal_ID = contentData.substring(98, 106).trim();
					card_Acceptor_Terminal_Location = contentData.substring(106, 146).trim();
					acquirer_ID = contentData.substring(146, 157).trim();
					String networkID = contentData.substring(157, 160);
					String accountNo1 = contentData.substring(160, 179);
					String account1BranchId = contentData.substring(179, 189);
					String accountNo2 = contentData.substring(189, 208);
					String account2BranchId = contentData.substring(208, 218);
					String transCurrencyCode = contentData.substring(218, 221);
					String txnAmount = contentData.substring(221, 236);
					String actualTransAmount = contentData.substring(236, 251);
					String tranActivityFee = contentData.substring(251, 266);
					String issuerSetCurrencyCode = contentData.substring(266, 269);
					String issuerSetAmount = contentData.substring(269, 284);
					String issuerSetFee = contentData.substring(284, 299);
					String issuerSetProcFee = contentData.substring(299, 314);
					String cardHolderBillcurnCode = contentData.substring(314, 317);
					String cardHolderBillAmount = contentData.substring(317, 332);
					String cardHolderBillActFee = contentData.substring(332, 347);
					String cardHolderBillProcFee = contentData.substring(347, 362);
					String cardHolderBillServiceFee = contentData.substring(362, 377);
					String tRAN_ISSUERCONVERSRATE = contentData.substring(377, 392);
					String tRANS_CARDHOLDERCONVERRATE = contentData.substring(392, 407);
					System.out.println("started");

					System.out.println("1participant_ID" + participant_ID);
					System.out.println("2transaction_Type" + transaction_Type);
					System.out.println("3from_Account_Type" + from_Account_Type);
					System.out.println("4to_Account_Type" + to_Account_Type);
					System.out.println("5transaction_Serial_Number" + transaction_Serial_Number);
					System.out.println("6response_Code" + response_Code);
					System.out.println("7pan_Number" + pan_Number);
					System.out.println("8member_Number" + member_Number);
					System.out.println("9approval_Number" + approval_Number);
					System.out.println("10system_Trace_Audit_Number" + system_Trace_Audit_Number);
					System.out.println("11transaction_Date" + transaction_Date);
					System.out.println("12transaction_Time" + transaction_Time);
					System.out.println("13merchant_Category_Code" + merchant_Category_Code);
					System.out.println("14card_Acceptor_Settlement_Date" + card_Acceptor_Settlement_Date);
					System.out.println("15card_Acceptor_ID" + card_Acceptor_ID);
					System.out.println("16card_Acceptor_Terminal_ID" + card_Acceptor_Terminal_ID);
					System.out.println("17card_Acceptor_Terminal_Location" + card_Acceptor_Terminal_Location);
					System.out.println("18acquirer_ID" + acquirer_ID);
					System.out.println("txnAmount" + txnAmount);
					System.out.println("networkID" + networkID);
					System.out.println("tRAN_ISSUERCONVERSRATE" + tRAN_ISSUERCONVERSRATE);
					System.out.println("tRANS_CARDHOLDERCONVERRATE" + tRANS_CARDHOLDERCONVERRATE);
					System.out.println();

					// String
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}
		return null;
	}

	@Override
	public List<JSONObject> importPosSettlementSummaryReportFiles(MultipartFile pos, String clientid,
			String createdby) {

		return traceDao.importPosSettlementSummaryReportFiles(pos, clientid, createdby);
	}

	@Override
	public List<JSONObject> importEJFileData(MultipartFile ej, String clientid, String createdby) {

		return traceDao.importEJFileData(ej, clientid, createdby);
	}

	@Override
	public List<JSONObject> importGlcbsFileData(MultipartFile glCbs, String clientid, String createdby,
			String fileTypeName) {

		return traceDao.importGlcbsFileData(glCbs, clientid, createdby, fileTypeName);
	}

	@Override
	public List<JSONObject> importSwitchFile(MultipartFile sw, String clientid, String createdby) {

		return traceDao.importSwitchFile(sw, clientid, createdby);
	}

	@Override
	public List<JSONObject> getchannelmodeinfo(String clientid) {

		return traceDao.getchannelmodeinfo(clientid);
	}

	@Override
	public List<JSONObject> getfieldidentification(String clientid, String vendorid, String channelid, String modeid,
			String formatid) {

		return traceDao.getfieldidentification(clientid, vendorid, channelid, modeid, formatid);
	}

	@Override
	public List<JSONObject> getfilevendordetails(String clientid) {

		return traceDao.getfilevendordetails(clientid);
	}

	@Override
	public String addfieldconfig(String p_CLIENTID, String p_VENDORID, String p_FORMATID, String p_TERMINALCODE,
			String p_BINNO, String p_ACQUIRERID, String p_REVCODE1, String p_REVCODE2, String p_REVTYPE,
			String p_REVENTRY, String p_TXNDATETIME, String p_TXNVALUEDATETIME, String p_TXNPOSTDATETIME,
			String p_ATMTYPE, String p_POSTYPE, String p_ECOMTYPE, String p_IMPSTYPE, String p_UPITYPE,
			String p_MICROATMTYPE, String p_MOBILERECHARGETYPE, String p_DEPOSIT, String p_BALENQ,
			String p_MINISTATEMENT, String p_PINCHANGE, String p_CHEQUEBOOKREQ, String p_RESPCODE1, String p_RESPCODE2,
			String p_RESPTPE, String p_EODCODE, String p_OFFLINECODE, String p_DEBITCODE, String p_CREDITCODE,
			String createdby) {

		return traceDao.addfieldconfig(p_CLIENTID, p_VENDORID, p_FORMATID, p_TERMINALCODE, p_BINNO, p_ACQUIRERID,
				p_REVCODE1, p_REVCODE2, p_REVTYPE, p_REVENTRY, p_TXNDATETIME, p_TXNVALUEDATETIME, p_TXNPOSTDATETIME,
				p_ATMTYPE, p_POSTYPE, p_ECOMTYPE, p_IMPSTYPE, p_UPITYPE, p_MICROATMTYPE, p_MOBILERECHARGETYPE,
				p_DEPOSIT, p_BALENQ, p_MINISTATEMENT, p_PINCHANGE, p_CHEQUEBOOKREQ, p_RESPCODE1, p_RESPCODE2, p_RESPTPE,
				p_EODCODE, p_OFFLINECODE, p_DEBITCODE, p_CREDITCODE, createdby);
	}

	@Override
	public List<JSONObject> getformatid(String clientid, String vendorid) {

		return traceDao.getformatid(clientid, vendorid);
	}

	@Override
	public List<JSONObject> getchanneldetails(String clientid) {

		return traceDao.getchanneldetails(clientid);
	}

	@Override
	public List<JSONObject> getchannelmodeinfo(String clientid, String channelid) {

		return traceDao.getchannelmodeinfo(clientid, channelid);
	}

	@Override
	public List<JSONObject> getmatchingmodeinfo(String clientid, String channelid) {

		return traceDao.getmatchingmodeinfo(clientid, channelid);
	}

	@Override
	public List<JSONObject> getMatchingRuleSetForClient(String clientid, String channelid, String ruleid) {

		return traceDao.getMatchingRuleSetForClient(clientid, channelid, ruleid);
	}

	@Override
	public List<JSONObject> getaddmatchingruleconfig(String clientid, String columnname, String channelid,
			String modeid, String ruletype, String createdby) {

		return traceDao.getaddmatchingruleconfig(clientid, columnname, channelid, modeid, ruletype, createdby);
	}

	@Override
	public List<JSONObject> getStatusMaster() {

		return traceDao.getStatusMaster();
	}

	@Override
	public List<JSONObject> getclientreportdetails(String clientid) {

		return traceDao.getclientreportdetails(clientid);
	}

	@Override
	public List<JSONObject> forcesettlementtxns(String clientid, String channelid, String modeid, String glstatus,
			String ejstatus, String nwstatus, String swstatus, String fromdatetxn, String todatetxn, String recontype,
			String settlementtype, String user, String tdrc, String branchid) {

		return traceDao.forcesettlementtxns(clientid, channelid, modeid, glstatus, ejstatus, nwstatus, swstatus,
				fromdatetxn, todatetxn, recontype, settlementtype, user, tdrc, branchid);
	}

	@Override
	public List<JSONObject> getchanneltypeall(String clientid, String userid) {

		return traceDao.getchanneltypeall(clientid, userid);
	}

	@Override
	public List<JSONObject> getFileType(String clientid) {

		return traceDao.getFileType(clientid);
	}

	@Override
	public List<JSONObject> getchanneltyperun(String clientid) {

		return traceDao.getchanneltyperun(clientid);
	}

	@Override
	public List<JSONObject> getModeTypeRun(String clientid, String channelid) {

		return traceDao.getModeTypeRun(clientid, channelid);
	}

	@Override
	public String changeUndefindToNull() throws InterruptedException {

		return traceDao.changeUndefindToNull();
	}

	@Override
	public List<JSONObject> getFileFormatDefualt(String p_FileExt, String p_SeparatorType, String p_ChannelID,
			String p_ModeID, String p_VendorID) {

		return traceDao.getFileFormatDefualt(p_FileExt, p_SeparatorType, p_ChannelID, p_ModeID, p_VendorID);
	}
}
