package com.admin.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.hibernate.type.BlobType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.admin.model.BranchEntry;
import com.admin.model.EjModel;
import com.admin.model.MenuModel;
import com.admin.model.NpciAcqModel;
import com.admin.model.SubMenuModel;
import com.admin.model.User;

@Repository
public class Trace_DAO_Imp implements Trace_DAO {

	User eq = null;
	int count=0;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DataSource datasource;
	
	
	Connection con=null;
	
	public Trace_DAO_Imp() {

	}

	public Trace_DAO_Imp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<User> getData(String username, String password) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("splogindetails");

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, username);
		query.setParameter(2, password);
		query.setParameter(3, "1");

		query.execute();

		List<Object[]> res = query.getResultList();
		List<User> list = new ArrayList<User>();
		Iterator it = res.iterator();
		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();
			User eq = new User();

			eq.setUsername(String.valueOf(line[0]));
			eq.setPassword(String.valueOf(line[1]));
			eq.setRoleID(String.valueOf(line[2]));
			eq.setClientID(String.valueOf(line[3]));
			eq.setBranchID(String.valueOf(line[4]));

			list.add(eq);
		}

		return list;
	}

	public String getMenuData(String userName, String roleID, String clientID) {

		MenuModel menu = null;

		JSONArray menuArray = new JSONArray();

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetmenuaccess");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userName.toUpperCase());
		query.setParameter(2, Integer.parseInt(roleID));
		query.setParameter(3, 1);
		query.setParameter(4, Integer.parseInt(clientID));

		query.execute();
		List<Object[]> res = query.getResultList();
		List<MenuModel> menuList = new ArrayList<MenuModel>();

		Iterator it = res.iterator();
		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();
			menu = new MenuModel();

			menu.setMenuName(String.valueOf(line[0]));
			menu.setMenuID(String.valueOf(line[1]));
			menu.setMenuLevel(String.valueOf(line[2]));
			menu.setFilePath(String.valueOf(line[3]));
			menu.setParentMenuID(String.valueOf(line[4]));
			menu.setSequenceNo(String.valueOf(line[5]));
			menuList.add(menu);

			JSONObject object = new JSONObject();

			object.put("MenuName", String.valueOf(line[0]));
			object.put("MenuID", String.valueOf(line[1]));
			object.put("MenuLevel", String.valueOf(line[2]));
			object.put("FilePath", String.valueOf(line[3]));
			object.put("ParentMenuID", String.valueOf(line[4]));
			object.put("SequenceNo", String.valueOf(line[5]));
			menuArray.add(object);
		}
		return menuArray.toString();
	}

	public String getSubMenuData(String userName, String roleID, String clientID) {

		JSONObject subMenuobject = null;
		SubMenuModel menu = null;
		JSONArray subMenuArray = new JSONArray();
		System.out.println("In 2");
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetmenuaccess");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userName.toUpperCase());
		query.setParameter(2, Integer.parseInt(roleID));
		query.setParameter(3, 2);
		query.setParameter(4, Integer.parseInt(clientID));
		query.execute();

		List<Object[]> res = query.getResultList();
		System.out.println("in 2" + res);

		List<SubMenuModel> subMenuList = new ArrayList<SubMenuModel>();

		Iterator it = res.iterator();
		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();

			menu = new SubMenuModel();

			menu.setMenuName(String.valueOf(line[0]));
			menu.setMenuID(String.valueOf(line[1]));
			menu.setMenuLevel(String.valueOf(line[2]));
			menu.setFilePath(String.valueOf(line[3]));
			menu.setParentMenuID(String.valueOf(line[4]));
			menu.setSequenceNo(String.valueOf(line[5]));

			subMenuList.add(menu);

			subMenuobject = new JSONObject();

			subMenuobject.put("MenuName", String.valueOf(line[0]));
			subMenuobject.put("MenuID", String.valueOf(line[1]));
			subMenuobject.put("MenuLevel", String.valueOf(line[2]));
			subMenuobject.put("FilePath", String.valueOf(line[3]));
			subMenuobject.put("ParentMenuID", String.valueOf(line[4]));
			subMenuobject.put("SequenceNo", String.valueOf(line[5]));
			subMenuArray.add(subMenuobject);
		}
		List<User> list = new ArrayList<>();

		return subMenuArray.toString();

	}

	@Override
	public String getRoleDetails(String clientID) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GETROLEDETAILS");

		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientID));
		query.execute();
		List<Object[]> res = query.getResultList();
		Iterator it = res.iterator();

		JSONArray roleArray = new JSONArray();

		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();

			System.out.println(String.valueOf(line[0]));
			System.out.println(String.valueOf(line[1]));
			System.out.println(String.valueOf(line[2]));
			System.out.println(String.valueOf(line[3]));

			JSONObject roleObject = new JSONObject();
			roleObject.put("roleID", String.valueOf(line[0]));
			roleObject.put("roleNAme", String.valueOf(line[1]));
			roleObject.put("clientID", String.valueOf(line[2]));
			roleObject.put("homePAge", String.valueOf(line[3]));
			roleArray.add(roleObject);

		}

		return roleArray.toString();

	}

	@Override
	public String getRoleMaster(String roleName, String homePage, String mode, String roleID, String clientID) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sprolemaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, Integer.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, String.valueOf(mode));
		query.setParameter(2, Integer.parseInt(roleID));
		query.setParameter(3, Integer.parseInt(clientID));
		query.setParameter(4, String.valueOf(roleName));
		query.setParameter(5, String.valueOf(homePage));
		query.setParameter(6, "ROY");
		query.execute();

		String rowsInserted = null;

		int count = query.getMaxResults();

		if (count != 0) {
			rowsInserted = String.valueOf(count);

		} else {

		}

		return rowsInserted;

	}

	@Override
	public String getRoleAccessRights(String roleID, String clientID) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetroleaccessrights");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(roleID));
		query.setParameter(2, Integer.parseInt(clientID));

		query.execute();

		List<Object[]> res = query.getResultList();

		JSONArray roleArray = new JSONArray();
		Iterator it = res.iterator();

		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();

			JSONObject roleObject = new JSONObject();
			roleObject.put("menuName", String.valueOf(line[0]));
			roleObject.put("menuID", String.valueOf(line[1]));
			roleObject.put("isActivie", String.valueOf(line[2]));
			roleObject.put("menuLevel", String.valueOf(line[3]));
			roleObject.put("filePath", String.valueOf(line[4]));
			roleObject.put("parentMenuID", String.valueOf(line[5]));
			roleObject.put("menuType", String.valueOf(line[6]));
			roleObject.put("menuType", String.valueOf(line[7]));
			roleArray.add(roleObject);

		}

		return roleArray.toString();

	}

	@Override
	public String getBranchDetails(String clientID, String branchID) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetbranchdetails");

		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientID));
		query.setParameter(2, Integer.parseInt(branchID));

		query.execute();

		List<Object[]> res = query.getResultList();

		JSONArray roleArray = new JSONArray();
		Iterator it = res.iterator();

		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();

			JSONObject roleObject = new JSONObject();
			roleObject.put("branchCode", String.valueOf(line[0]));
			roleObject.put("branchName", String.valueOf(line[1]));
			roleArray.add(roleObject);

		}

		return roleArray.toString();

	}

	@Override
	public String AssignRoleAccessRights(String roleID, String clientID, String menuString, String username) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spassignroleaccessrights");

		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(roleID));
		query.setParameter(2, Integer.parseInt(clientID));
		query.setParameter(3, menuString);
		query.setParameter(4, username);

		boolean result = query.execute();

		List<Object[]> res = query.getResultList();

		return res.toString();
	}

	@Override
	public String getUserDetails(String username, String clientID, String branchID, String roleID) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetuserdetails");

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, username);
		query.setParameter(2, Integer.parseInt(clientID));
		query.setParameter(3, branchID);
		query.setParameter(4, roleID);

		query.execute();

		List<Object[]> res = query.getResultList();

		System.out.println("res" + res);

		JSONArray roleArray = new JSONArray();
		Iterator it = res.iterator();

		while (it.hasNext()) {
			Object[] line = (Object[]) it.next();

			JSONObject roleObject = new JSONObject();
			roleObject.put("ID", String.valueOf(line[0]));
			roleObject.put("userID", String.valueOf(line[1]));
			roleObject.put("EmailID", String.valueOf(line[2]));
			roleObject.put("roleName", String.valueOf(line[3]));
			roleObject.put("BranchName", String.valueOf(line[4]));
			roleObject.put("contactNo", String.valueOf(line[5]));
			roleObject.put("clientName", String.valueOf(line[6]));
			roleObject.put("clientID", String.valueOf(line[7]));
			roleObject.put("isLocked", String.valueOf(line[8]));

			roleArray.add(roleObject);

		}

		return roleArray.toString();

	}

	@Override
	public String resetPassword() {

		return null;
	}

	@Override
	public String AddUser(String userName, String password, String firstName, String lastName, String roleID,
			String clientID, String branchID, String emailID, String contactNo, String securityq, String securitya,
			String createBy, String p_salt, String channel) {

		return null;
	}

	@Override
	public List<JSONObject> getUploadFiletype() {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetuploadfiletype");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();

		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("fileType", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getChannelID() {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELMASTER");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		System.out.println("result" + result);
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("channeltype", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public String addUser(String userid, String password, String firstname, String lastname, String roleid,
			String clientid, String branchid, String emailid, String contactno, String securityq, String securitya,
			String createdby, String salt, String channel) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spadduser");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(15, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userid);
		query.setParameter(2, Long.parseLong(password));
		query.setParameter(3, firstname);
		query.setParameter(4, lastname);
		query.setParameter(5, Integer.parseInt(roleid));
		query.setParameter(6, Integer.parseInt(clientid));
		query.setParameter(7, Integer.parseInt(branchid));
		query.setParameter(8, emailid);
		query.setParameter(9, contactno);
		query.setParameter(10, securityq);
		query.setParameter(11, securitya);
		query.setParameter(12, createdby);
		query.setParameter(13, Long.parseLong(salt));
		query.setParameter(14, channel);

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();

	}

	@Override
	public String deleteUser(String userid, String roleid, String branchid, String createdby) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spadduserdelete");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userid);
		query.setParameter(2, Integer.parseInt(roleid));
		query.setParameter(3, Integer.parseInt(branchid));
		query.setParameter(4, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();
	}

	@Override
	public String addUserRole(String userid, String roleid, String clientid, String createdby) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spadduserrole");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userid);
		query.setParameter(2, Integer.parseInt(roleid));
		query.setParameter(3, Integer.parseInt(clientid));
		query.setParameter(4, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();

		return result.toString();
	}

	@Override
	public String addUserUpdate(String userid, String roleid, String branchid, String emailid, String createdby,
			String channel) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spadduserupdate");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userid);
		query.setParameter(2, Integer.parseInt(roleid));
		query.setParameter(3, Integer.parseInt(branchid));
		query.setParameter(4, emailid);
		query.setParameter(5, createdby);
		query.setParameter(6, channel);

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();
	}

	@Override
	public List<JSONObject> getDomainTypeList() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetDomainMaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("domaintype", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getModuleTypeList() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetModuleMaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("moduletype", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getClientNameList() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgeclientdetails");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("clientNameList", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getVendorTypeList() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetVendorType");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("id", fields[0]);
			obj.put("vendorType", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getVendorDetails() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetVendorDetails");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ID", fields[0]);
			obj.put("VendorName", fields[1]);
			obj.put("VendorTypeID", fields[2]);
			obj.put("VendorType", fields[3]);
			obj.put("VendorID", fields[4]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public String vendorMasterModes(String mode, String vendorid, String vendorname, String vendortypeid,
			String createdby, String vendor) {
		// TODO Auto-generated method stub
		System.out.println(mode);
		System.out.println(vendorid);
		System.out.println(vendorname);
		System.out.println(vendortypeid);
		System.out.println(createdby);

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spvendormaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, mode);
		query.setParameter(2, Integer.parseInt(vendorid));
		query.setParameter(3, vendorname);
		query.setParameter(4, Integer.parseInt(vendortypeid));
		query.setParameter(5, createdby);
		query.setParameter(6, vendor);

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();
	}

	@Override
	public List<JSONObject> getContryTypeList() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetCountry");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ID", fields[0]);
			obj.put("Country", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getCurrencyDetails() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetcurrencydetails");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		System.out.println("resultsize" + result.size());
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("CurrencyID", fields[0]);
			obj.put("ID", fields[1]);
			obj.put("Country", fields[2]);
			// obj.put("CurrencyID", fields[3]);
			obj.put("CurrencyCode", fields[3]);
			obj.put("CurrencyDescription", fields[4]);
			obj.put("NumericCode", fields[5]);
			// obj.put("CurrencyID", fields[7]);
			obj.put("Scale", fields[6]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public String currencyMasterModes(String mode, String currencyid, String currencycode, String currencydescription,
			String countryid, String countryname, String numericcode, String createdby, String country, String scale) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spcurrencymaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, mode);
		query.setParameter(2, Integer.parseInt(countryid));
		query.setParameter(3, currencycode);
		query.setParameter(4, currencydescription);
		query.setParameter(5, Integer.parseInt(countryid));
		query.setParameter(6, countryname);
		query.setParameter(7, numericcode);
		query.setParameter(8, createdby);
		query.setParameter(9, country);
		query.setParameter(10, Integer.parseInt(scale));

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();
	}

	@Override
	public String clientConfigMaster(String clientid, String mode, String domainid, String moduleid, String clientcode,
			String clientname, String address, String contactno, String emailid, String concernperson, String cpemailid,
			String cpcontactno, String isbank, String isactive, String countryid, String currencyid, String ftp_ip,
			String ftpusername, String ftppassword, String ftpport, MultipartFile logofile, String userlimit,
			String terminalcount, String reporttime, String username, String colorcode, String logoName) {

		Blob blobValue = null;
		try {
			blobValue = new SerialBlob(logofile.getBytes());
		} catch (SQLException | IOException e1) {

			e1.printStackTrace();
		}
		if (mode.equalsIgnoreCase("ADD")) {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPCLIENTCONFIGMASTERADD");
			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(13, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(14, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(15, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(16, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(21, BlobType.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(22, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(23, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(28, String.class, ParameterMode.REF_CURSOR);

			query.setParameter(1, Integer.parseInt(clientid));
			query.setParameter(2, mode);
			query.setParameter(3, Integer.parseInt(domainid));
			query.setParameter(4, Integer.parseInt(moduleid));
			query.setParameter(5, clientcode);
			query.setParameter(6, clientname);
			query.setParameter(7, address);
			query.setParameter(8, contactno);
			query.setParameter(9, emailid);
			query.setParameter(10, concernperson);
			query.setParameter(11, cpemailid);
			query.setParameter(12, cpcontactno);
			query.setParameter(13, Integer.parseInt(isbank));
			query.setParameter(14, Integer.parseInt(isactive));
			query.setParameter(15, Integer.parseInt(countryid));
			query.setParameter(16, Integer.parseInt(currencyid));
			query.setParameter(17, ftp_ip);
			query.setParameter(18, ftpusername);
			query.setParameter(19, ftppassword);
			query.setParameter(20, ftpport);

			query.setParameter(21, blobValue);

			query.setParameter(22, Integer.parseInt(userlimit));
			query.setParameter(23, Integer.parseInt(terminalcount));
			query.setParameter(24, reporttime);
			query.setParameter(25, username);
			query.setParameter(26, colorcode);
			query.setParameter(27, logoName);

			query.execute();
			List<Object[]> result = query.getResultList();
			return result.toString();
		} else if (mode.equalsIgnoreCase("UPDATE")) {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPCLIENTCONFIGMASTERUPDATE");
			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(13, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(14, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(15, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(16, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(21, BlobType.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(22, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(23, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(28, String.class, ParameterMode.REF_CURSOR);

			query.setParameter(1, Integer.parseInt(clientid));
			query.setParameter(2, mode);
			query.setParameter(3, Integer.parseInt(domainid));
			query.setParameter(4, Integer.parseInt(moduleid));
			query.setParameter(5, clientcode);
			query.setParameter(6, clientname);
			query.setParameter(7, address);
			query.setParameter(8, contactno);
			query.setParameter(9, emailid);
			query.setParameter(10, concernperson);
			query.setParameter(11, cpemailid);
			query.setParameter(12, cpcontactno);
			query.setParameter(13, Integer.parseInt(isbank));
			query.setParameter(14, Integer.parseInt(isactive));
			query.setParameter(15, Integer.parseInt(countryid));
			query.setParameter(16, Integer.parseInt(currencyid));
			query.setParameter(17, ftp_ip);
			query.setParameter(18, ftpusername);
			query.setParameter(19, ftppassword);
			query.setParameter(20, ftpport);

			query.setParameter(21, blobValue);

			query.setParameter(22, Integer.parseInt(userlimit));
			query.setParameter(23, Integer.parseInt(terminalcount));
			query.setParameter(24, reporttime);
			query.setParameter(25, username);
			query.setParameter(26, colorcode);
			query.setParameter(27, logoName);

			query.execute();
			List<Object[]> result = query.getResultList();
			return result.toString();
		} else {
			return null;
		}
	}

	@Override
	public List<JSONObject> getclientcurrency(String countryid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCLIENTCURRENCY");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(countryid));
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("CurrencyID", fields[0]);
			obj.put("CurrencyCode", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getClientCode(String clientcode) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPCHECKCLIENTCODE");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, clientcode);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			JSONObject obj = new JSONObject();
			obj.put("ClientCode", result.get(0));
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getclientmastermodeget(String clientid, String mode, String domainid, String moduleid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCLIENTCONFIGMASTER");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, mode);
		query.setParameter(3, Integer.parseInt(domainid));
		query.setParameter(4, Integer.parseInt(moduleid));
		query.execute();
		List<Object[]> result = query.getResultList();
		System.out.println("resultsize" + result.size());
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ClientID", fields[0]);
			obj.put("ClientCode", fields[1]);
			obj.put("ClientName", fields[2]);
			obj.put("Address", fields[3]);
			obj.put("ContactNo", fields[4]);
			obj.put("EmailID", fields[5]);
			obj.put("ConcernPerson", fields[6]);
			obj.put("CPEmailID", fields[7]);
			obj.put("CPContactNo", fields[8]);
			obj.put("IsBank", fields[9]);
			obj.put("CounrtyID", fields[10]);
			obj.put("CurrencyID", fields[11]);
			obj.put("DomainID", fields[12]);
			obj.put("ModuleID", fields[13]);
			obj.put("FTP_IP", fields[14]);
			obj.put("FTPUserName", fields[15]);
			obj.put("FTPPassword", fields[16]);
			obj.put("FTPPort", fields[17]);
			obj.put("UserLimit", fields[18]);
			obj.put("TerminalCount", fields[19]);
			obj.put("ReportCutoffTime", fields[20]);
			obj.put("IsActive", fields[21]);
			obj.put("CreatedBy", fields[22]);
			obj.put("CreatedOn", fields[23]);
			obj.put("logoName", fields[24]);
			obj.put("clientColor", fields[25]);
			JSONObjects.add(obj);
		}
		return JSONObjects;

	}

	@Override
	public byte[] getBranchFile(String clientid) {
		int para = Integer.parseInt(clientid);
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spbranchtemplate");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, para);
		query.execute();
		List<Object[]> result1 = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result1.size());
		int rownum = 0;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		Workbook workbook = new XSSFWorkbook();
		Cell cell = null;
		Row row = null;

		Sheet sheet = workbook.createSheet("BranchDetails");
		CellStyle cs = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		cs.setFont(font);
		String[] headers = new String[] { "BRANCHCODE", "BRANCHNAME", "ADDRESS", "CONTACTNO", "EMAILID",
				"CONCERNPERSON" };
		row = sheet.createRow(rownum++);
		row.createCell(0).setCellValue(headers[0]);
		row.getCell(0).setCellStyle(cs);
		sheet.autoSizeColumn(0);
		row.createCell(1).setCellValue(headers[1]);
		row.getCell(1).setCellStyle(cs);
		sheet.autoSizeColumn(1);
		row.createCell(2).setCellValue(headers[2]);
		row.getCell(2).setCellStyle(cs);
		sheet.autoSizeColumn(2);
		row.createCell(3).setCellValue(headers[3]);
		row.getCell(3).setCellStyle(cs);
		sheet.autoSizeColumn(3);
		row.createCell(4).setCellValue(headers[4]);
		row.getCell(4).setCellStyle(cs);
		sheet.autoSizeColumn(4);
		row.createCell(5).setCellValue(headers[5]);
		row.getCell(5).setCellStyle(cs);
		sheet.autoSizeColumn(5);
		for (Object record : result1) {
			Object[] fields = (Object[]) record;
			int colnum = 0;
			row = sheet.createRow(rownum++);

			for (Object value : fields) {
				cell = row.createCell(colnum++);
				if (value instanceof String) {
					cell.setCellValue((String) value);
					sheet.autoSizeColumn(colnum);
				} else if (value instanceof Integer) {
					cell.setCellValue((Integer) value);
					sheet.autoSizeColumn(colnum);
				}
			}
		}

		try {
			workbook.write(bo);
		} catch (IOException e) {

			e.printStackTrace();
		}
		byte[] bytes = bo.toByteArray();

		return bytes;
	}

	@Override
	public byte[] getTerminalFile(String clientid) {
		// TODO Auto-generated method stub
		int para = Integer.parseInt(clientid);
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spterminaltemplate");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, para);
		query.execute();
		List<Object[]> result1 = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result1.size());
		int rownum = 0;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		Workbook workbook = new XSSFWorkbook();
		Cell cell = null;
		Row row = null;

		Sheet sheet = workbook.createSheet("TerminalDetails");
		CellStyle cs = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		cs.setFont(font);
		String[] headers = new String[] { "TERMINALID", "TERMINALLOCATION", "GLACCOUNTNO", "CHANNEL", "BRANCHCODE",
				"TERMINALTYPE", "CONCERNPERSON" };
		row = sheet.createRow(rownum++);
		row.createCell(0).setCellValue(headers[0]);
		row.getCell(0).setCellStyle(cs);
		sheet.autoSizeColumn(0);
		row.createCell(1).setCellValue(headers[1]);
		row.getCell(1).setCellStyle(cs);
		sheet.autoSizeColumn(1);
		row.createCell(2).setCellValue(headers[2]);
		row.getCell(2).setCellStyle(cs);
		sheet.autoSizeColumn(2);
		row.createCell(3).setCellValue(headers[3]);
		row.getCell(3).setCellStyle(cs);
		sheet.autoSizeColumn(3);
		row.createCell(4).setCellValue(headers[4]);
		row.getCell(4).setCellStyle(cs);
		sheet.autoSizeColumn(4);
		row.createCell(5).setCellValue(headers[5]);
		row.getCell(5).setCellStyle(cs);
		sheet.autoSizeColumn(5);
		row.createCell(6).setCellValue(headers[6]);
		row.getCell(6).setCellStyle(cs);
		sheet.autoSizeColumn(6);
		for (Object record : result1) {
			Object[] fields = (Object[]) record;
			row = sheet.createRow(rownum++);
			int colnum = 0;
			for (Object value : fields) {
				cell = row.createCell(colnum++);
				if (value instanceof String) {
					cell.setCellValue((String) value);
					sheet.autoSizeColumn(colnum);
				} else if (value instanceof Integer) {
					cell.setCellValue((Integer) value);
					sheet.autoSizeColumn(colnum);
				}
			}
		}

		try {
			workbook.write(bo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = bo.toByteArray();

		return bytes;

	}

	@Override
	public List mapBranchMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user) {
		String ext = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());

		if (ext.equalsIgnoreCase("xls")) {
			System.out.println("xls");
			try {
				String branchID = null, clientID = null, branchName = null, address = null, contactNO = null,
						emailID = null, concernPerson = null, branchCode = null, isHO = null, isRemoved = null,
						createdBy = null, createdOn = null, modifiedOn = null, modifiedBy = null, xlsresult = null;
				List xlsList = new ArrayList();
				HSSFWorkbook wb = new HSSFWorkbook(reapExcelDataFile.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				System.out.println(sheet.getLastRowNum());

				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				while (itr.hasNext()) {
					Row row = itr.next();
					if (row.getRowNum() == 0) {
						continue;
					} else {
						branchID = row.getCell(0).toString();
						Float f = Float.parseFloat(branchID);
						int branchInNum = f.intValue();
						System.out.println("branchid" + branchID + " " + f);

						clientID = row.getCell(1).toString();
						Float cid = Float.parseFloat(clientID);
						int clientInNum = f.intValue();
						branchName = row.getCell(2).toString();
						address = row.getCell(3).toString();
						contactNO = row.getCell(4).toString();
						emailID = row.getCell(5).toString();
						concernPerson = row.getCell(6).toString();
						branchCode = row.getCell(7).toString();

						StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPBRANCHMARSTERFILEOPT");
						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(10, String.class, ParameterMode.REF_CURSOR);

						query.setParameter(1, branchInNum);
						query.setParameter(2, clientInNum);
						query.setParameter(3, branchName);
						query.setParameter(4, address);
						query.setParameter(5, contactNO);
						query.setParameter(6, emailID);
						query.setParameter(7, concernPerson);
						query.setParameter(8, branchCode);
						query.setParameter(9, user);

						query.execute();

						System.out.println();
						List<Object[]> resultxls = query.getResultList();
						if (resultxls.toString().equalsIgnoreCase("2")) {
							xlsList.add(branchName);
							xlsList.add(clientInNum);
							xlsList.add(branchName);
							xlsList.add(address);
							xlsList.add(contactNO);
							xlsList.add(emailID);
							xlsList.add(concernPerson);
							xlsList.add(branchCode);
							xlsList.add(user);
							xlsList.add("||");

						}

					}

				}
				return xlsList;
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		} else if (ext.equalsIgnoreCase("xlsx")) {
			System.out.println("xlsx");
			try {
				String branchID = null;
				String clientID = null, branchName = null, address = null, contactNO = null, emailID = null,
						concernPerson = null, branchCode = null, isHO = null, isRemoved = null, createdBy = null,
						createdOn = null, modifiedOn = null, modifiedBy = null;
				List xlsxList = new ArrayList();
				XSSFWorkbook wb = new XSSFWorkbook(reapExcelDataFile.getInputStream());
				XSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				while (itr.hasNext()) {
					Row row = itr.next();

					if (row.getRowNum() == 0) {
						continue;
					} else {
						branchID = row.getCell(0).toString();
						Float f = Float.parseFloat(branchID);
						int branchInNum = f.intValue();
						System.out.println("branchid" + branchID + " " + f);

						clientID = row.getCell(1).toString();
						Float cid = Float.parseFloat(clientID);
						int clientInNum = cid.intValue();
						System.out.println("clientID" + clientID + " " + cid);
						branchName = row.getCell(2).toString();
						address = row.getCell(3).toString();
						contactNO = row.getCell(4).toString();
						emailID = row.getCell(5).toString();
						concernPerson = row.getCell(6).toString();
						branchCode = row.getCell(7).toString();

						StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPBRANCHMARSTERFILEOPT");
						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(10, String.class, ParameterMode.REF_CURSOR);

						query.setParameter(1, branchInNum);
						query.setParameter(2, clientInNum);
						query.setParameter(3, branchName);
						query.setParameter(4, address);
						query.setParameter(5, contactNO);
						query.setParameter(6, emailID);
						query.setParameter(7, concernPerson);
						query.setParameter(8, branchCode);
						query.setParameter(9, user);

						query.execute();

						List<Object[]> xlsxresult = query.getResultList();

						if (xlsxresult.toString().equalsIgnoreCase("[2]")) {
							xlsxList.add(branchID);
							xlsxList.add(clientID);
							xlsxList.add(branchName);
							xlsxList.add(address);
							xlsxList.add(contactNO);
							xlsxList.add(emailID);
							xlsxList.add(concernPerson);
							xlsxList.add(branchCode);
							xlsxList.add(user);
						}
					}

				}
				return xlsxList;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			return null;
		}

		return null;

	}

	@Override
	public List mapTerminalMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user) {

		String ext = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());
		if (ext.equalsIgnoreCase("xls")) {

			try {
				String termID = null, clientID = null, terminalID = null, terminalLocation = null, glAccountNo = null,
						channel = null, branchCode = null, terminalType = null, contactNo = null, emailID = null,
						concernPerson = null, isRemoved = null, createdBy = null, checkerBy = null, createdOn = null,
						modifiedOn = null, modifiedBy = null;
				List xlstermList = new ArrayList();

				HSSFWorkbook wb = new HSSFWorkbook(reapExcelDataFile.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				while (itr.hasNext()) {
					Row row = itr.next();

					if (row.getRowNum() == 0) {
						continue;
					} else {
						termID = row.getCell(0).toString();
						Float t = Float.parseFloat(termID);
						int termInNum = t.intValue();

						clientID = row.getCell(1).toString();
						Float cid = Float.parseFloat(clientID);
						int clientInNum = cid.intValue();
						terminalID = row.getCell(2).toString();
						terminalLocation = row.getCell(3).toString();
						glAccountNo = row.getCell(4).toString();
						channel = row.getCell(5).toString();
						branchCode = row.getCell(6).toString();
						terminalType = row.getCell(7).toString();
						contactNo = row.getCell(8).toString();
						emailID = row.getCell(9).toString();
						concernPerson = row.getCell(10).toString();
						StoredProcedureQuery query = entityManager
								.createStoredProcedureQuery("SPTERMINALMASTERFILEOPT");
						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(13, String.class, ParameterMode.REF_CURSOR);

						query.setParameter(1, termInNum);
						query.setParameter(2, clientInNum);
						query.setParameter(3, terminalID);
						query.setParameter(4, terminalLocation);
						query.setParameter(5, glAccountNo);
						query.setParameter(6, channel);
						query.setParameter(7, branchCode);
						query.setParameter(8, terminalType);
						query.setParameter(9, contactNo);
						query.setParameter(10, emailID);
						query.setParameter(11, concernPerson);
						query.setParameter(12, user);

						query.execute();

						List<Object[]> xlstermResult = query.getResultList();
						if (xlstermResult.toString().equalsIgnoreCase("2")) {
							xlstermList.add(termID);
							xlstermList.add(clientID);
							xlstermList.add(terminalID);
							xlstermList.add(terminalLocation);
							xlstermList.add(glAccountNo);
							xlstermList.add(channel);
							xlstermList.add(branchCode);
							xlstermList.add(terminalType);
							xlstermList.add(contactNo);
							xlstermList.add(emailID);
							xlstermList.add(concernPerson);
							xlstermList.add(user);
						}
					}
					System.out.println();

				}
				return xlstermList;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (ext.equalsIgnoreCase("xlsx")) {
			System.out.println("xlsx");
			try {
				String termID = null, clientID = null, terminalID = null, terminalLocation = null, glAccountNo = null,
						channel = null, branchCode = null, terminalType = null, contactNo = null, emailID = null,
						concernPerson = null, isRemoved = null, createdBy = null, checkerBy = null, createdOn = null,
						modifiedOn = null, modifiedBy = null;
				List xlsxTermList = new ArrayList();
				XSSFWorkbook wb = new XSSFWorkbook(reapExcelDataFile.getInputStream());
				XSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				while (itr.hasNext()) {
					Row row = itr.next();
					// if(row.)
					if (row.getRowNum() == 0) {
						continue;
					} else {
						termID = row.getCell(0).toString();
						Float t = Float.parseFloat(termID);
						int termInNum = t.intValue();

						clientID = row.getCell(1).toString();
						Float cid = Float.parseFloat(clientID);
						int clientInNum = cid.intValue();

						terminalID = row.getCell(2).toString();
						terminalLocation = row.getCell(3).toString();
						glAccountNo = row.getCell(4).toString();
						channel = row.getCell(5).toString();
						branchCode = row.getCell(6).toString();
						terminalType = row.getCell(7).toString();
						contactNo = row.getCell(8).toString();
						emailID = row.getCell(9).toString();
						concernPerson = row.getCell(10).toString();

						StoredProcedureQuery query = entityManager
								.createStoredProcedureQuery("SPTERMINALMASTERFILEOPT");
						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(13, String.class, ParameterMode.REF_CURSOR);

						query.setParameter(1, termInNum);
						query.setParameter(2, clientInNum);
						query.setParameter(3, terminalID);
						query.setParameter(4, terminalLocation);
						query.setParameter(5, glAccountNo);
						query.setParameter(6, channel);
						query.setParameter(7, branchCode);
						query.setParameter(8, terminalType);
						query.setParameter(9, contactNo);
						query.setParameter(10, emailID);
						query.setParameter(11, concernPerson);
						query.setParameter(12, user);

						query.execute();

						List<Object[]> xlsxtermresult = query.getResultList();
						if (xlsxtermresult.toString().equalsIgnoreCase("2")) {
							xlsxTermList.add(termID);
							xlsxTermList.add(clientID);
							xlsxTermList.add(terminalID);
							xlsxTermList.add(glAccountNo);
							xlsxTermList.add(channel);
							xlsxTermList.add(branchCode);
							xlsxTermList.add(terminalType);
							xlsxTermList.add(contactNo);
							xlsxTermList.add(emailID);
							xlsxTermList.add(concernPerson);
							xlsxTermList.add(user);
						}
					}
					System.out.println();

				}
				return xlsxTermList;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	@Override
	public List<JSONObject> getClientCode() {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCLIENTCODE");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {

			JSONObject obj = new JSONObject();
			obj.put("ClientCode", result.get(0));
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public String clientchannelmodeinsert(String channelid, String modeid, String clientcode, String user) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPCLIENTMODECHANNELINSERTION");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(channelid));
		query.setParameter(2, Integer.parseInt(modeid));
		query.setParameter(3, Integer.parseInt(clientcode));
		query.setParameter(4, user);

		query.execute();

		List<Object[]> result = query.getResultList();
		return result.toString();

	}

	@Override
	public List<JSONObject> getChannelData(String clientid) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GETCHAINNELDATA");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ID", fields[0]);
			obj.put("ChannelID", fields[1]);
			obj.put("ModeID", fields[2]);
			obj.put("ClientID", fields[3]);
			obj.put("CreatedOn", fields[4]);
			obj.put("CreatedBy", fields[5]);
			obj.put("ModifiedOn", fields[6]);
			obj.put("ModifiedBy", fields[7]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> updateChangePwdwithsalt(String userid, String clientcode, String oldpassword,
			String securePassword, String confirmpassword, String createdby) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spupdatechangepwd");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);

		query.setParameter(1, userid);
		query.setParameter(2, clientcode);
		query.setParameter(3, oldpassword);
		query.setParameter(4, securePassword);
		query.setParameter(5, confirmpassword);
		query.setParameter(6, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("updatePwdResult", fields[0]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getLogList(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETLOGLIST");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ID", fields[0]);
			obj.put("LogType", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getModeType(String clientid, String channelid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetmodetypeall");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("modeid", fields[0]);
			obj.put("modename", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getinsertfileformat(String p_CLIENTID, String p_VENDORID, String p_FILEEXT,
			String p_FILEXML, String p_CUTOFFTIME, String p_USER, String p_FILEPREFIX, String p_VENDORTYPE,
			String p_CHANNELID, String p_MODEID, String p_SEPARATORTYPE) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPINSERTFILEFORMAT1");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(p_VENDORID));
		query.setParameter(2, Integer.parseInt(p_CLIENTID));
		query.setParameter(3, p_FILEEXT);
		query.setParameter(4, p_FILEXML);
		query.setParameter(5, p_CUTOFFTIME);
		query.setParameter(6, p_USER);
		query.setParameter(7, p_FILEPREFIX);
		query.setParameter(8, p_VENDORTYPE);
		query.setParameter(9, Integer.parseInt(p_CHANNELID));
		query.setParameter(10, Integer.parseInt(p_MODEID));
		query.setParameter(11, p_SEPARATORTYPE);
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ClientName", fields[0]);
			obj.put("VendorTypeID", fields[1]);
			obj.put("VendorName", fields[2]);
			obj.put("FileExtention", fields[3]);
			obj.put("CutOffTime", fields[4]);
			obj.put("ModeID", fields[5]);
			obj.put("FilePrefix", fields[6]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchanneltype(String clientid, String userid) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELTYPE");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));

		query.execute();
		List<Object[]> result = query.getResultList();
		System.out.println("resultsize" + result.size());
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ChannelID", fields[0]);
			obj.put("ChannelName", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getVendorDetailsByType(String vendortype) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetVendorDetailsByType");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, vendortype);
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("VendorID", fields[0]);
			obj.put("VendorName", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getfileformatclient(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFILEFORMATCLIENT");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("clientName", fields[0]);
			obj.put("vendorType", fields[1]);
			obj.put("channelName", fields[2]);
			obj.put("filePrefix", fields[3]);
			obj.put("transactionMode", fields[4]);
			obj.put("vendorName", fields[5]);
			obj.put("fileExtension", fields[6]);
			obj.put("cutofftime", fields[7]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getFileFormatHistory(String p_VendorType, String p_ClientID, String p_ChannelID,
			String p_ModeID, String p_VendorID) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetFileFormatHistory1");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, p_VendorType);
		query.setParameter(2, Integer.parseInt(p_ClientID));
		query.setParameter(3, Integer.parseInt(p_ChannelID));
		query.setParameter(4, Integer.parseInt(p_ModeID));
		query.setParameter(5, Integer.parseInt(p_VendorID));
		query.execute();
		List<Object[]> result = query.getResultList();
		String tempStr = result.toString();
		System.out.println("tempStr: " + tempStr);
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		if (tempStr.equals("[not exist]")) {
			for (Object record : result) {
//				Object[] fields = (Object[]) record;
				JSONObject obj = new JSONObject();
				obj.put("Status", result.toString());
				JSONObjects.add(obj);
			}
		} else {
			for (Object record : result) {
				Object[] fields = (Object[]) record;
				JSONObject obj = new JSONObject();
				obj.put("FormatID", fields[0]);
				obj.put("ClientName", fields[1]);
				obj.put("FileExtention", fields[2]);
				obj.put("VendorName", fields[3]);
				obj.put("FormatDescriptionXml", fields[4]);
				obj.put("CutOffTime", fields[5]);
				obj.put("StartIndex", fields[6]);
				obj.put("EndIndex", fields[7]);
				obj.put("FormatStatus", fields[8]);
				obj.put("VendorType", fields[9]);
				obj.put("FormatStatus", fields[10]);
				obj.put("FilePrefix", fields[11]);
				obj.put("ChannelID", fields[12]);
				obj.put("ModeID", fields[13]);

				JSONObjects.add(obj);
			}
		}
		return JSONObjects;

	}

	@Override
	public List<JSONObject> getfileformat(String p_VENDORID, String p_CLIENTID, String p_FILEPREFIX, String p_FILEEXT,
			String p_SEPARATORTYPE, String p_MODEID, String p_CHANNELID) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetFileFormatHistory");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(p_VENDORID));
		query.setParameter(2, Integer.parseInt(p_CLIENTID));
		query.setParameter(3, p_FILEPREFIX);
		query.setParameter(4, p_FILEEXT);
		query.setParameter(5, p_SEPARATORTYPE);
		query.setParameter(6, Integer.parseInt(p_MODEID));
		query.setParameter(7, Integer.parseInt(p_CHANNELID));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("FormatID", fields[0]);
			obj.put("ClientName", fields[1]);
			obj.put("FileExtention", fields[2]);
			obj.put("VendorName", fields[3]);
			obj.put("FormatDescriptionXml", fields[4]);
			obj.put("CutOffTime", fields[5]);
			obj.put("StartIndex", fields[6]);
			obj.put("EndIndex", fields[7]);
			obj.put("FormatStatus", fields[8]);
			obj.put("FilePrefix", fields[9]);
			obj.put("ChannelID", fields[10]);
			obj.put("ModeID", fields[10]);
			obj.put("SeparatorType", fields[11]);
			JSONObjects.add(obj);
		}
		return JSONObjects;

	}

	@Override
	public void save(NpciAcqModel npciAcqModel) {
		System.out.println("in dao");
		sessionFactory.getCurrentSession().save(npciAcqModel);
	}

	@Override
	public List<JSONObject> importFileNpciATMFilesISS(JSONObject obj, String clientid, String fileDate, String cycle,
			String forceMatch, String createdby, NodeList nodeList) {

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
		String fileName = null;
		String nodeName = null;

		nodeName = nodeList.item(0).getNodeName();
		if (obj.containsKey(nodeName)) {
			participant_ID = obj.get(nodeName).toString();
		}

		nodeName = nodeList.item(1).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Type = obj.get(nodeName).toString();
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
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPIMPORTNPCIISSUERFILE");

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(34, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(35, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(36, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(37, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(38, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(39, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(40, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(41, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(42, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(43, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(44, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, clientid);
		query.setParameter(2, participant_ID);
		query.setParameter(3, transaction_Type);
		query.setParameter(4, from_Account_Type);
		query.setParameter(5, to_Account_Type);
		query.setParameter(6, RRN);
		query.setParameter(7, response_Code);
		query.setParameter(8, card_number);
		query.setParameter(9, member_Number);
		query.setParameter(10, approval_Number);
		query.setParameter(11, system_Trace_Audit_Number);
		query.setParameter(12, transaction_Date);
		query.setParameter(13, transaction_Time);
		query.setParameter(14, merchant_Category_Code);
		query.setParameter(15, card_Acceptor_Settlement_Date);
		query.setParameter(16, card_Acceptor_ID);
		query.setParameter(17, card_Acceptor_Terminal_ID);
		query.setParameter(18, card_Acceptor_Terminal_Location);
		query.setParameter(19, acquirer_ID);
		query.setParameter(20, networkID);
		query.setParameter(21, accountNo1);
		query.setParameter(22, account1BranchId);
		query.setParameter(23, accountNo2);
		query.setParameter(24, account2BranchId);
		query.setParameter(25, transCurrencyCode);
		query.setParameter(26, txnAmount);
		query.setParameter(27, actualTransAmount);
		query.setParameter(28, tranActivityFee);
		query.setParameter(29, issuerSetCurrencyCode);
		query.setParameter(30, issuerSetAmount);
		query.setParameter(31, issuerSetFee);
		query.setParameter(32, issuerSetProcFee);
		query.setParameter(33, cardHolderBillcurnCode);
		query.setParameter(34, cardHolderBillAmount);
		query.setParameter(35, cardHolderBillActFee);
		query.setParameter(36, cardHolderBillProcFee);
		query.setParameter(37, cardHolderBillServiceFee);
		query.setParameter(38, tRAN_ISSUERCONVERSRATE);
		query.setParameter(39, tRANS_CARDHOLDERCONVERRATE);
		query.setParameter(40, cycle);
		query.setParameter(41, fileDate);
		query.setParameter(42, forceMatch);
		query.setParameter(43, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			JSONObject obj1 = new JSONObject();
			obj1.put("resultFromImportFile", result.toString());
			JSONObjects.add(obj1);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> importPosSettlementSummaryReportFiles(MultipartFile pos, String clientid,
			String createdby) {
		String ext = FilenameUtils.getExtension(pos.getOriginalFilename());
		if (pos.getOriginalFilename().contains("DSRSummaryReport")) {
			if (ext.equalsIgnoreCase("xls")) {
				try {
					String Settlement_Date = null, Product_Name = null, Bank_Name = null, Settlement_Bin = null,
							Acq_ID_ISS_Bin = null, Inward_Outward = null, Status_Approved_Declined = null,
							Transaction_Cycle = null, Transaction_Type = null, Channel = null, TXNCOUNT = null,
							TXN_CCY = null, Txn_Amt_DR = null, Txn_Amt_Cr = null, SET_CCY = null, SETAMTDR = null,
							SETAMTCR = null, Int_Fee_Amt_DR = null, Int_Fee_Amt_CR = null, Mem_Inc_Fee_Amt_DR = null,
							Mem_Inc_Fee_Amt_CR = null, Customer_Compensation_Dr = null, Customer_Compensation_Cr = null,
							Oth_Fee_Amt_DR = null, Oth_Fee_Amt_CR = null, Oth_Fee_GST_DR = null, Oth_Fee_GST_CR = null,
							Final_Sum_Cr = null, Final_Sum_Dr = null, Final_Net = null;
					List xlstermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							Product_Name = row.getCell(1).toString();
							Bank_Name = row.getCell(2).toString();
							Settlement_Bin = row.getCell(3).toString();
							Acq_ID_ISS_Bin = row.getCell(4).toString();
							Inward_Outward = row.getCell(5).toString();
							Status_Approved_Declined = row.getCell(6).toString();
							Transaction_Cycle = row.getCell(7).toString();
							Transaction_Type = row.getCell(8).toString();
							Channel = row.getCell(9).toString();
							TXNCOUNT = row.getCell(10).toString();
							TXN_CCY = row.getCell(11).toString();
							Txn_Amt_DR = row.getCell(12).toString();
							Txn_Amt_Cr = row.getCell(13).toString();
							SET_CCY = row.getCell(14).toString();
							SETAMTDR = row.getCell(15).toString();
							SETAMTCR = row.getCell(16).toString();
							Int_Fee_Amt_DR = row.getCell(17).toString();
							Int_Fee_Amt_CR = row.getCell(18).toString();
							Mem_Inc_Fee_Amt_DR = row.getCell(19).toString();
							Mem_Inc_Fee_Amt_CR = row.getCell(20).toString();
							Customer_Compensation_Dr = row.getCell(21).toString();
							Customer_Compensation_Cr = row.getCell(22).toString();
							Oth_Fee_Amt_DR = row.getCell(23).toString();
							Oth_Fee_Amt_CR = row.getCell(24).toString();
							Oth_Fee_GST_DR = row.getCell(25).toString();
							Oth_Fee_GST_CR = row.getCell(26).toString();
							Final_Sum_Cr = row.getCell(27).toString();
							Final_Sum_Dr = row.getCell(28).toString();
							Final_Net = row.getCell(29).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTDSRSUMMARYREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(34, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, Product_Name);
							query.setParameter(3, Bank_Name);
							query.setParameter(4, Settlement_Bin);
							query.setParameter(5, Acq_ID_ISS_Bin);
							query.setParameter(6, Inward_Outward);
							query.setParameter(7, Status_Approved_Declined);
							query.setParameter(8, Transaction_Cycle);
							query.setParameter(9, Transaction_Type);
							query.setParameter(10, Channel);
							query.setParameter(11, TXNCOUNT);
							query.setParameter(12, TXN_CCY);
							query.setParameter(13, Txn_Amt_DR);
							query.setParameter(14, Txn_Amt_Cr);
							query.setParameter(15, SET_CCY);
							query.setParameter(16, SETAMTDR);
							query.setParameter(17, SETAMTCR);
							query.setParameter(18, Int_Fee_Amt_DR);
							query.setParameter(19, Int_Fee_Amt_CR);
							query.setParameter(20, Mem_Inc_Fee_Amt_DR);
							query.setParameter(21, Mem_Inc_Fee_Amt_CR);
							query.setParameter(22, Customer_Compensation_Dr);
							query.setParameter(23, Customer_Compensation_Cr);
							query.setParameter(24, Oth_Fee_Amt_DR);
							query.setParameter(25, Oth_Fee_Amt_CR);
							query.setParameter(26, Oth_Fee_GST_DR);
							query.setParameter(27, Oth_Fee_GST_CR);
							query.setParameter(28, Final_Sum_Cr);
							query.setParameter(29, Final_Sum_Dr);
							query.setParameter(30, Final_Net);
							query.setParameter(31, clientid);
							query.setParameter(32, pos.getOriginalFilename().substring(60, 61));
							query.setParameter(33, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlstermList.add(Settlement_Date);
								xlstermList.add(Product_Name);
								xlstermList.add(Bank_Name);
								xlstermList.add(Settlement_Bin);
								xlstermList.add(Acq_ID_ISS_Bin);
								xlstermList.add(Inward_Outward);
								xlstermList.add(Status_Approved_Declined);
								xlstermList.add(Transaction_Cycle);
								xlstermList.add(Transaction_Type);
								xlstermList.add(Channel);
								xlstermList.add(TXNCOUNT);
								xlstermList.add(TXN_CCY);
								xlstermList.add(Txn_Amt_DR);
								xlstermList.add(Txn_Amt_Cr);
								xlstermList.add(SET_CCY);
								xlstermList.add(SETAMTDR);
								xlstermList.add(SETAMTCR);
								xlstermList.add(Int_Fee_Amt_DR);
								xlstermList.add(Int_Fee_Amt_CR);
								xlstermList.add(Mem_Inc_Fee_Amt_DR);
								xlstermList.add(Mem_Inc_Fee_Amt_CR);
								xlstermList.add(Customer_Compensation_Dr);
								xlstermList.add(Customer_Compensation_Cr);
								xlstermList.add(Oth_Fee_Amt_DR);
								xlstermList.add(Oth_Fee_Amt_CR);
								xlstermList.add(Oth_Fee_GST_DR);
								xlstermList.add(Oth_Fee_GST_CR);
								xlstermList.add(Final_Sum_Cr);
								xlstermList.add(Final_Sum_Dr);
								xlstermList.add(Final_Net);

							}
						}
						System.out.println();

					}
					return xlstermList;
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (ext.equalsIgnoreCase("xlsx")) {
				System.out.println("xlsx");
				try {
					String Settlement_Date = null, Product_Name = null, Bank_Name = null, Settlement_Bin = null,
							Acq_ID_ISS_Bin = null, Inward_Outward = null, Status_Approved_Declined = null,
							Transaction_Cycle = null, Transaction_Type = null, Channel = null, TXNCOUNT = null,
							TXN_CCY = null, Txn_Amt_DR = null, Txn_Amt_Cr = null, SET_CCY = null, SETAMTDR = null,
							SETAMTCR = null, Int_Fee_Amt_DR = null, Int_Fee_Amt_CR = null, Mem_Inc_Fee_Amt_DR = null,
							Mem_Inc_Fee_Amt_CR = null, Customer_Compensation_Dr = null, Customer_Compensation_Cr = null,
							Oth_Fee_Amt_DR = null, Oth_Fee_Amt_CR = null, Oth_Fee_GST_DR = null, Oth_Fee_GST_CR = null,
							Final_Sum_Cr = null, Final_Sum_Dr = null, Final_Net = null;
					List xlsxTermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							Product_Name = row.getCell(1).toString();
							Bank_Name = row.getCell(2).toString();
							Settlement_Bin = row.getCell(3).toString();
							Acq_ID_ISS_Bin = row.getCell(4).toString();
							Inward_Outward = row.getCell(5).toString();
							Status_Approved_Declined = row.getCell(6).toString();
							Transaction_Cycle = row.getCell(7).toString();
							Transaction_Type = row.getCell(8).toString();
							Channel = row.getCell(9).toString();
							TXNCOUNT = row.getCell(10).toString();
							TXN_CCY = row.getCell(11).toString();
							Txn_Amt_DR = row.getCell(12).toString();
							Txn_Amt_Cr = row.getCell(13).toString();
							SET_CCY = row.getCell(14).toString();
							SETAMTDR = row.getCell(15).toString();
							SETAMTCR = row.getCell(16).toString();
							Int_Fee_Amt_DR = row.getCell(17).toString();
							Int_Fee_Amt_CR = row.getCell(18).toString();
							Mem_Inc_Fee_Amt_DR = row.getCell(19).toString();
							Mem_Inc_Fee_Amt_CR = row.getCell(20).toString();
							Customer_Compensation_Dr = row.getCell(21).toString();
							Customer_Compensation_Cr = row.getCell(22).toString();
							Oth_Fee_Amt_DR = row.getCell(23).toString();
							Oth_Fee_Amt_CR = row.getCell(24).toString();
							Oth_Fee_GST_DR = row.getCell(25).toString();
							Oth_Fee_GST_CR = row.getCell(26).toString();
							Final_Sum_Cr = row.getCell(27).toString();
							Final_Sum_Dr = row.getCell(28).toString();
							Final_Net = row.getCell(29).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTDSRSUMMARYREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(34, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, Product_Name);
							query.setParameter(3, Bank_Name);
							query.setParameter(4, Settlement_Bin);
							query.setParameter(5, Acq_ID_ISS_Bin);
							query.setParameter(6, Inward_Outward);
							query.setParameter(7, Status_Approved_Declined);
							query.setParameter(8, Transaction_Cycle);
							query.setParameter(9, Transaction_Type);
							query.setParameter(10, Channel);
							query.setParameter(11, TXNCOUNT);
							query.setParameter(12, TXN_CCY);
							query.setParameter(13, Txn_Amt_DR);
							query.setParameter(14, Txn_Amt_Cr);
							query.setParameter(15, SET_CCY);
							query.setParameter(16, SETAMTDR);
							query.setParameter(17, SETAMTCR);
							query.setParameter(18, Int_Fee_Amt_DR);
							query.setParameter(19, Int_Fee_Amt_CR);
							query.setParameter(20, Mem_Inc_Fee_Amt_DR);
							query.setParameter(21, Mem_Inc_Fee_Amt_CR);
							query.setParameter(22, Customer_Compensation_Dr);
							query.setParameter(23, Customer_Compensation_Cr);
							query.setParameter(24, Oth_Fee_Amt_DR);
							query.setParameter(25, Oth_Fee_Amt_CR);
							query.setParameter(26, Oth_Fee_GST_DR);
							query.setParameter(27, Oth_Fee_GST_CR);
							query.setParameter(28, Final_Sum_Cr);
							query.setParameter(29, Final_Sum_Dr);
							query.setParameter(30, Final_Net);
							query.setParameter(31, clientid);
							query.setParameter(32, pos.getOriginalFilename().substring(60, 61));
							query.setParameter(33, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlsxTermList.add(Settlement_Date);
								xlsxTermList.add(Product_Name);
								xlsxTermList.add(Bank_Name);
								xlsxTermList.add(Settlement_Bin);
								xlsxTermList.add(Acq_ID_ISS_Bin);
								xlsxTermList.add(Inward_Outward);
								xlsxTermList.add(Status_Approved_Declined);
								xlsxTermList.add(Transaction_Cycle);
								xlsxTermList.add(Transaction_Type);
								xlsxTermList.add(Channel);
								xlsxTermList.add(TXNCOUNT);
								xlsxTermList.add(TXN_CCY);
								xlsxTermList.add(Txn_Amt_DR);
								xlsxTermList.add(Txn_Amt_Cr);
								xlsxTermList.add(SET_CCY);
								xlsxTermList.add(SETAMTDR);
								xlsxTermList.add(SETAMTCR);
								xlsxTermList.add(Int_Fee_Amt_DR);
								xlsxTermList.add(Int_Fee_Amt_CR);
								xlsxTermList.add(Mem_Inc_Fee_Amt_DR);
								xlsxTermList.add(Mem_Inc_Fee_Amt_CR);
								xlsxTermList.add(Customer_Compensation_Dr);
								xlsxTermList.add(Customer_Compensation_Cr);
								xlsxTermList.add(Oth_Fee_Amt_DR);
								xlsxTermList.add(Oth_Fee_Amt_CR);
								xlsxTermList.add(Oth_Fee_GST_DR);
								xlsxTermList.add(Oth_Fee_GST_CR);
								xlsxTermList.add(Final_Sum_Cr);
								xlsxTermList.add(Final_Sum_Dr);
								xlsxTermList.add(Final_Net);
							}
						}
						System.out.println();

					}
					return xlsxTermList;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return null;
			}
			return null;
		} else if (pos.getOriginalFilename().contains("InterchangeSummaryReport")) {
			if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) {
				System.out.println("xls");
				try {
					String Settlement_Date = null, Product_Name = null, Bank_Name = null, Settlement_Bin = null,
							Acq_ID_ISS_Bin = null, Inward_Outward = null, Transaction_Cycle = null,
							Transaction_Type = null, Channel = null, InterchangeCategory = null, TXNCOUNT = null,
							SET_CCY = null, SETAMTCR = null, SETAMTDR = null, Int_Fee_Amt_CR = null,
							Int_Fee_Amt_DR = null;
					List xlstermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							Product_Name = row.getCell(1).toString();
							Bank_Name = row.getCell(2).toString();
							Settlement_Bin = row.getCell(3).toString();
							Acq_ID_ISS_Bin = row.getCell(4).toString();
							Inward_Outward = row.getCell(5).toString();
							Transaction_Cycle = row.getCell(6).toString();
							Transaction_Type = row.getCell(7).toString();
							Channel = row.getCell(8).toString();
							InterchangeCategory = row.getCell(9).toString();
							TXNCOUNT = row.getCell(10).toString();
							SET_CCY = row.getCell(11).toString();
							SETAMTCR = row.getCell(12).toString();
							SETAMTDR = row.getCell(13).toString();
							Int_Fee_Amt_CR = row.getCell(14).toString();
							Int_Fee_Amt_DR = row.getCell(15).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTINTERCHANGESUMMARYREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(20, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, Product_Name);
							query.setParameter(3, Bank_Name);
							query.setParameter(4, Settlement_Bin);
							query.setParameter(5, Acq_ID_ISS_Bin);
							query.setParameter(6, Inward_Outward);
							query.setParameter(7, Transaction_Cycle);
							query.setParameter(8, Transaction_Type);
							query.setParameter(9, Channel);
							query.setParameter(10, InterchangeCategory);
							query.setParameter(11, TXNCOUNT);
							query.setParameter(12, SET_CCY);
							query.setParameter(13, SETAMTCR);
							query.setParameter(14, SETAMTDR);
							query.setParameter(15, Int_Fee_Amt_CR);
							query.setParameter(16, Int_Fee_Amt_DR);
							query.setParameter(17, clientid);
							query.setParameter(18, pos.getOriginalFilename().substring(61, 62));
							query.setParameter(19, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlstermList.add(Settlement_Date);
								xlstermList.add(Product_Name);
								xlstermList.add(Bank_Name);
								xlstermList.add(Settlement_Bin);
								xlstermList.add(Acq_ID_ISS_Bin);
								xlstermList.add(Inward_Outward);
								xlstermList.add(Transaction_Cycle);
								xlstermList.add(Transaction_Type);
								xlstermList.add(Channel);
								xlstermList.add(InterchangeCategory);
								xlstermList.add(TXNCOUNT);
								xlstermList.add(SET_CCY);
								xlstermList.add(SETAMTCR);
								xlstermList.add(SETAMTDR);
								xlstermList.add(Int_Fee_Amt_CR);
								xlstermList.add(Int_Fee_Amt_DR);
							}
						}
						System.out.println();

					}
					return xlstermList;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else if (pos.getOriginalFilename().contains("NetSettlementReport")) {
			if (ext.equalsIgnoreCase("xls")) {
				System.out.println("xls");
				try {
					String Settlement_Date = null, RTGS_Bank_Name = null, Member_Name = null, Member_Bank_PID = null,
							Member_Bank_Type = null, DRCR = null, Final_Sum_Cr = null, Final_Sum_Dr = null, net = null;
					List xlstermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							RTGS_Bank_Name = row.getCell(1).toString();
							Member_Name = row.getCell(2).toString();
							Member_Bank_PID = row.getCell(3).toString();
							Member_Bank_Type = row.getCell(4).toString();
							DRCR = row.getCell(5).toString();
							Final_Sum_Cr = row.getCell(6).toString();
							Final_Sum_Dr = row.getCell(7).toString();
							net = row.getCell(8).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTNETSETTLEMENTREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, RTGS_Bank_Name);
							query.setParameter(3, Member_Name);
							query.setParameter(4, Member_Bank_PID);
							query.setParameter(5, Member_Bank_Type);
							query.setParameter(6, DRCR);
							query.setParameter(7, Final_Sum_Cr);
							query.setParameter(8, Final_Sum_Dr);
							query.setParameter(9, net);
							query.setParameter(10, clientid);
							query.setParameter(11, pos.getOriginalFilename().substring(68, 69));
							query.setParameter(12, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlstermList.add(Settlement_Date);
								xlstermList.add(RTGS_Bank_Name);
								xlstermList.add(Member_Name);
								xlstermList.add(Member_Bank_PID);
								xlstermList.add(Member_Bank_Type);
								xlstermList.add(DRCR);
								xlstermList.add(Final_Sum_Cr);
								xlstermList.add(Final_Sum_Dr);
								xlstermList.add(net);
							}
						}
						System.out.println();

					}
					return xlstermList;
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (ext.equalsIgnoreCase("xlsx")) {
				System.out.println("xlsx");
				try {
					String Settlement_Date = null, RTGS_Bank_Name = null, Member_Name = null, Member_Bank_PID = null,
							Member_Bank_Type = null, DRCR = null, Final_Sum_Cr = null, Final_Sum_Dr = null, net = null;
					List xlsxTermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							RTGS_Bank_Name = row.getCell(1).toString();
							Member_Name = row.getCell(2).toString();
							Member_Bank_PID = row.getCell(3).toString();
							Member_Bank_Type = row.getCell(4).toString();
							DRCR = row.getCell(5).toString();
							Final_Sum_Cr = row.getCell(6).toString();
							Final_Sum_Dr = row.getCell(7).toString();
							net = row.getCell(8).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTNETSETTLEMENTREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, RTGS_Bank_Name);
							query.setParameter(3, Member_Name);
							query.setParameter(4, Member_Bank_PID);
							query.setParameter(5, Member_Bank_Type);
							query.setParameter(6, DRCR);
							query.setParameter(7, Final_Sum_Cr);
							query.setParameter(8, Final_Sum_Dr);
							query.setParameter(9, net);
							query.setParameter(10, clientid);
							query.setParameter(11, pos.getOriginalFilename().substring(68, 69));
							query.setParameter(12, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlsxTermList.add(Settlement_Date);
								xlsxTermList.add(RTGS_Bank_Name);
								xlsxTermList.add(Member_Name);
								xlsxTermList.add(Member_Bank_PID);
								xlsxTermList.add(Member_Bank_Type);
								xlsxTermList.add(DRCR);
								xlsxTermList.add(Final_Sum_Cr);
								xlsxTermList.add(Final_Sum_Dr);
								xlsxTermList.add(net);
							}
						}
						System.out.println();

					}
					return xlsxTermList;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (pos.getOriginalFilename().contains("NPCIBillingSummary")) {
			if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) {
				System.out.println("xls");
				try {
					String Settlement_Date = null, Product_Name = null, Bank_Name = null, Settlement_Bin = null,
							Inward_Outward = null, Fee_Code = null, Fee_Desc = null, Fee_Dr_Cr = null, Txn_Count = null,
							SET_CCY = null, Fee_Amt = null, Fee_Amt_GST = null, Fee = null;
					List xlstermList = new ArrayList();
					XSSFWorkbook wb = new XSSFWorkbook(pos.getInputStream());
					XSSFSheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							Settlement_Date = row.getCell(0).toString();
							Product_Name = row.getCell(1).toString();
							Bank_Name = row.getCell(2).toString();
							Settlement_Bin = row.getCell(3).toString();
							Inward_Outward = row.getCell(4).toString();
							Fee_Code = row.getCell(5).toString();
							Fee_Desc = row.getCell(6).toString();
							Fee_Dr_Cr = row.getCell(7).toString();
							Txn_Count = row.getCell(8).toString();
							SET_CCY = row.getCell(9).toString();
							Fee_Amt = row.getCell(10).toString();
							Fee_Amt_GST = row.getCell(11).toString();
							Fee = row.getCell(12).toString();

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTNPCIBILLINGSUMMARY");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(17, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, Settlement_Date);
							query.setParameter(2, Product_Name);
							query.setParameter(3, Bank_Name);
							query.setParameter(4, Settlement_Bin);
							query.setParameter(5, Inward_Outward);
							query.setParameter(6, Fee_Code);
							query.setParameter(7, Fee_Desc);
							query.setParameter(8, Fee_Dr_Cr);
							query.setParameter(9, Txn_Count);
							query.setParameter(10, SET_CCY);
							query.setParameter(11, Fee_Amt);
							query.setParameter(12, Fee_Amt_GST);
							query.setParameter(13, Fee);
							query.setParameter(14, clientid);
							query.setParameter(15, pos.getOriginalFilename().substring(66, 67));
							query.setParameter(16, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
							if (xlstermResult.toString().equalsIgnoreCase("2")) {
								xlstermList.add(Settlement_Date);
								xlstermList.add(Product_Name);
								xlstermList.add(Bank_Name);
								xlstermList.add(Settlement_Bin);
								xlstermList.add(Inward_Outward);
								xlstermList.add(Fee_Code);
								xlstermList.add(Fee_Desc);
								xlstermList.add(Fee_Dr_Cr);
								xlstermList.add(Txn_Count);
								xlstermList.add(SET_CCY);
								xlstermList.add(Fee_Amt);
								xlstermList.add(Fee_Amt_GST);
								xlstermList.add(Fee);
							}
						}
						System.out.println();

					}
					return xlstermList;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else if (pos.getOriginalFilename().contains("Presentment_Report")) {
			if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx") || ext.equalsIgnoreCase("csv")) {
				try {
					String ReportDate = null, PresentmentRaiseDate = null, PresentmentSettlementDate = null,
							FunctionCodeandDescription = null, PrimaryAccountNumber = null, DateLocaTransaction = null,
							AcquirerReferenceDataRRN = null, ProcessingCode = null, CurrencyCodeTransaction = null,
							ECommerceIndicator = null, AmountTransaction = null, AmountAdditional = null,
							SettlementAmountTransaction = null, SettlementAmountAdditional = null, ApprovalCode = null,
							OriginatorPoint = null, POSEntryMode = null, POSConditionCode = null,
							AcquirerInstitutionIDcode = null, TransactionOriginatorInstitutionIDcode = null,
							AcquirerNameandCountry = null, IssuerInstitutionIDcode = null,
							TransactionDestinationInstitutionIDcode = null, IssuerNameandCountry = null,
							CardType = null, CardBrand = null, CardAcceptorTerminalID = null, CardAcceptorName = null,
							CardAcceptorLocationaddress = null, CardAcceptorCountryCode = null,
							CardAcceptorBusinessCode = null, CardAcceptorIDCode = null, CardAcceptorStateName = null,
							CardAcceptorCity = null, DaysAged = null, MTI = null;
					List xlstermList = new ArrayList();
					File convFile = new File(pos.getOriginalFilename());
					convFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(pos.getBytes());
					fos.close();
					Workbook wb = WorkbookFactory.create(convFile);
					Sheet sheet = wb.getSheetAt(0);
					BranchEntry be = new BranchEntry();
					Iterator<Row> itr = sheet.iterator();
					while (itr.hasNext()) {
						Row row = itr.next();
						if (row.getRowNum() == 0) {
							continue;
						} else {
							ReportDate = row.getCell(0).toString();
							if (ReportDate.contains("End")) {

							} else {
								PresentmentRaiseDate = row.getCell(1).toString();
								PresentmentSettlementDate = row.getCell(2).toString();
								FunctionCodeandDescription = row.getCell(3).toString();
								PrimaryAccountNumber = row.getCell(4).toString();
								DateLocaTransaction = row.getCell(5).toString();
								AcquirerReferenceDataRRN = row.getCell(6).toString();
								ProcessingCode = row.getCell(7).toString();
								CurrencyCodeTransaction = row.getCell(8).toString();
								ECommerceIndicator = row.getCell(9).toString();
								AmountTransaction = row.getCell(10).toString();
								AmountAdditional = row.getCell(11).toString();
								SettlementAmountTransaction = row.getCell(12).toString();
								SettlementAmountAdditional = row.getCell(13).toString();
								ApprovalCode = row.getCell(14).toString();
								OriginatorPoint = row.getCell(15).toString();
								POSEntryMode = row.getCell(16).toString();
								POSConditionCode = row.getCell(17).toString();
								AcquirerInstitutionIDcode = row.getCell(18).toString();
								TransactionOriginatorInstitutionIDcode = row.getCell(19).toString();
								AcquirerNameandCountry = row.getCell(20).toString();
								IssuerInstitutionIDcode = row.getCell(21).toString();
								TransactionDestinationInstitutionIDcode = row.getCell(22).toString();
								IssuerNameandCountry = row.getCell(23).toString();
								CardType = row.getCell(24).toString();
								CardBrand = row.getCell(25).toString();
								CardAcceptorTerminalID = row.getCell(26).toString();
								CardAcceptorName = row.getCell(27).toString();
								CardAcceptorLocationaddress = row.getCell(28).toString();
								CardAcceptorCountryCode = row.getCell(29).toString();
								CardAcceptorBusinessCode = row.getCell(30).toString();
								CardAcceptorIDCode = row.getCell(31).toString();
								CardAcceptorStateName = row.getCell(32).toString();
								CardAcceptorCity = row.getCell(33).toString();
								DaysAged = row.getCell(34).toString();
								MTI = row.getCell(35).toString();
							}

							StoredProcedureQuery query = entityManager
									.createStoredProcedureQuery("SPIMPORTPRESENTMENTREPORT");
							query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(34, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(35, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(36, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(37, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(38, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(39, String.class, ParameterMode.IN);
							query.registerStoredProcedureParameter(40, String.class, ParameterMode.REF_CURSOR);

							query.setParameter(1, ReportDate);
							query.setParameter(2, PresentmentRaiseDate);
							query.setParameter(3, PresentmentSettlementDate);
							query.setParameter(4, FunctionCodeandDescription);
							query.setParameter(5, PrimaryAccountNumber);
							query.setParameter(6, DateLocaTransaction);
							query.setParameter(7, AcquirerReferenceDataRRN);
							query.setParameter(8, ProcessingCode);
							query.setParameter(9, CurrencyCodeTransaction);
							query.setParameter(10, ECommerceIndicator);
							query.setParameter(11, AmountTransaction);
							query.setParameter(12, AmountAdditional);
							query.setParameter(13, SettlementAmountTransaction);
							query.setParameter(14, SettlementAmountAdditional);
							query.setParameter(15, ApprovalCode);
							query.setParameter(16, OriginatorPoint);
							query.setParameter(17, POSEntryMode);
							query.setParameter(18, POSConditionCode);
							query.setParameter(19, AcquirerInstitutionIDcode);
							query.setParameter(20, TransactionOriginatorInstitutionIDcode);
							query.setParameter(21, AcquirerNameandCountry);
							query.setParameter(22, IssuerInstitutionIDcode);
							query.setParameter(23, TransactionDestinationInstitutionIDcode);
							query.setParameter(24, IssuerNameandCountry);
							query.setParameter(25, CardType);
							query.setParameter(26, CardBrand);
							query.setParameter(27, CardAcceptorTerminalID);
							query.setParameter(28, CardAcceptorName);
							query.setParameter(29, CardAcceptorLocationaddress);
							query.setParameter(30, CardAcceptorCountryCode);
							query.setParameter(31, CardAcceptorBusinessCode);
							query.setParameter(32, CardAcceptorIDCode);
							query.setParameter(33, CardAcceptorStateName);
							query.setParameter(34, CardAcceptorCity);
							query.setParameter(35, DaysAged);
							query.setParameter(36, MTI);
							query.setParameter(37, clientid);
							query.setParameter(38, pos.getOriginalFilename().substring(27, 28));
							query.setParameter(39, createdby);

							query.execute();

							List<Object[]> xlstermResult = query.getResultList();
						}

					}
					return xlstermList;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (EncryptedDocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return null;
	}

	@Override
	public List<JSONObject> importEJFileData(MultipartFile ej, String clientid, String createdby) {
		String EjFileName = ej.getOriginalFilename();
		List<String> content = null;
		String contentData = null;
		int startIndex1 = 0, endIndex1 = 0, startIndex = 0, endIndex = 0;
		double TxnsAmount = 0.0;
		String TxnsSubType = null;
		try {
			File convFile = new File(ej.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(ej.getBytes());
			fos.close();
			content = Files.readAllLines(convFile.toPath());
			int a = -1, b = -1;
			boolean flag = false;
			for (int i = 0; i < content.size(); i++) {
				startIndex1 = GetIndex(content, i, "ATM ID");
				endIndex1 = GetIndex(content, i, "------------------------------------");

				startIndex = GetIndex(content, i, "TRANSACTION START");
				endIndex = GetIndex(content, i, "TRANSACTION END");

				if (startIndex > -1) {
					a = startIndex;
				}
				if (endIndex > -1) {
					b = endIndex;
				}

				if (a != -1 && b != -1) {

					for (int k = a; k <= b; k++) {
						contentData = content.get(k);
						if (contentData.contains("ATM ID")) {
							String terminal_id = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("REF NO") || contentData.contains("RRN NO")) {
							String reference_number = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("DATE     :")) {
							String TxnsDate = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("TIME     :") || contentData.contains("Timeout")
								|| contentData.contains("TIMEOUT")) {
							String TxnsTime = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("CARD NO")) {
							String CardNumber = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("A/C NO") || contentData.contains("ACCOUNT NO")) {
							String account_number = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("TRANSTYPE")) {
							TxnsSubType = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("RESP CODE")) {
							String reference_code = contentData.substring(contentData.indexOf(":") + 1);

						}

						if (contentData.contains("RESP CODE")) {

							String error = content.get(k + 1).toString();
							if (!error.contains("TRANS AMOUNT") && TxnsSubType == "CASH WITHDRAWAL") {
								String ErrorCode = error;
							}
						}

						if (contentData.contains("TRANS AMOUNT") || (contentData.contains("WDL AMT"))) {
							String amountstr = contentData.substring(contentData.indexOf(":") + 1).replace("RS.", "")
									.trim();
							TxnsAmount = Double.parseDouble(amountstr);

						}

						if (contentData.contains("TRANS SEQ NUMBER")) {
							String TransSeqNo = contentData.substring(contentData.indexOf("[")).trim();

						}

						if (contentData.contains("OPCode") || (contentData.contains("OPCODE"))) {
							String Opcode = contentData.substring(contentData.indexOf("=") + 2).trim();

						}

						if (contentData.contains("Function ID") || contentData.contains("FUNCTION ID")) {
							String FunctionId = contentData.substring(contentData.indexOf("[")).trim();

						}

						if (contentData.contains("DENOMINATION")) {
							String Denomination = contentData.substring(contentData.indexOf(" ")).trim();
						}

						if (contentData.contains("DISPENSED")) {
							String[] Dis = contentData.split(" ");
							String DispenseCount = Dis[1] + "," + Dis[2] + "," + Dis[3] + "," + Dis[4];
							DispenseCount = contentData.substring(contentData.indexOf(" ")).trim();
						}

						if (contentData.contains("REMAINING")) {
							String[] Rem = contentData.split(" ");
							String RemainCount = Rem[1] + "," + Rem[2] + "," + Rem[3] + "," + Rem[4];
						}

						if (contentData.contains("REJECTED")) {
							String[] Rem = contentData.split(" ");
							String RejectCount = Rem[1] + "," + Rem[2] + "," + Rem[3] + "," + Rem[4];
						}

						if (contentData.contains("NOTES PRESENTED")) {
							String[] Note = contentData.split(" ");
							String[] Notes = Note[4].split(",");

							String Cassette1 = Notes[0];
							String Cassette2 = Notes[1];
							String Cassette3 = Notes[2];
							String Cassette4 = Notes[3];
						}

					}
					i = b;
					a = -1;
					b = -1;
				}

			}
		} catch (Exception e) {
		}
		return null;
	}

	private int GetIndex(List<String> content, int i, String searchStr) {
		// TODO Auto-generated method stub
		int idx = -1;
		if (content != null && content.size() > 0) {
			boolean found = false;
			for (idx = i; idx < content.size(); idx++) {
				if (content.get(idx).contains(searchStr)) {
					found = true;
					break;
				}
			}
			if (!found)
				idx = -1;
		}
		return idx;
	}

	@Override
	public List<JSONObject> importGlcbsFileData(MultipartFile glCbs, String clientid, String createdby,String fileTypeName) {

		try {
			
			Connection con=datasource.getConnection();
			Map<String, Integer>hm=new HashMap<String, Integer>();
			org.json.JSONObject jsonObj=new org.json.JSONObject();
			List<JSONObject> cbsfileformatxml = getcbsformatfileinxml(clientid, fileTypeName);
			JSONObject xmlFormatDescription = cbsfileformatxml.get(0);
			String tempStr = xmlFormatDescription.get("FormatDescriptionXml").toString();
			System.out.println("tempStr:"+tempStr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(tempStr)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			System.out.println("nodelistLength"+nodeList.getLength());
			for(int i=0;i<nodeList.getLength();i++)
			{
				String nodeName=nodeList.item(i).getNodeName();
				Node startPosNode = nodeList.item(i);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String nodeValue = startPosNodeValue.item(0).getNodeValue();
				System.out.println("nodeName  "+nodeName+" "+"nodeValue "+nodeValue);
//				hm.put(NodeName, nodeValue);
				jsonObj.append(nodeName, nodeValue.toString());
			}
			System.out.println("Json:"+jsonObj.toString());
			System.out.println("Terminal : "+jsonObj.getJSONArray("TerminalID").getString(0));			

			
			HSSFWorkbook wb = new HSSFWorkbook(glCbs.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
			Row row1 = sheet.getRow(0);
			int tempcolindex = -1;
			String tempstr = null;

			
			
			String ATMAccountNo = "";
			String Amount1 = "";
			String Amount2 = "";
			String Amount3 = "";
			String ResponseCode2="";
			String ReversalCode2="";
			String FeeAmount="";
			String CurrencyCode="";
			String CustBalance="";
			String InterchangeBalance=null;
			String ATMBalance=null;
			String BranchCode=null;
			String InterchangeAccountNo=null;
			String AcquirerID=null;
			String AuthCode=null;
			String ReserveField5=null;
			String ReserveField1=null;
			String ProcessingCode=null;
			String TxnsValueDateTime=null;
			String TxnsDateTime=null;
			String ReserveField3=null;
			String ReserveField4=null;
			String TxnsNumber=null;
			String CustAccountNo=null;
			String TerminalID=null;
			String TxnsPostDateTime=null;
			String TxnsDate=null;
			String TxnsTime=null;
			String ReferenceNumber=null;
			String CardNumber=null;
			String TxnsAmount=null;
			String TxnsSubType=null;
			String ReserveField2=null;
			String ResponseCode1=null;
			String ReversalCode1=null;
			String ChannelType=null;
			String DrCrType=null;
			String TxnsPerticulars=null;
			
			CallableStatement stmt=con.prepareCall("call spbulkinsertcbsdatadbbl");
			Iterator<Row> itr = sheet.iterator();
			HSSFRow temprow=null;
			while(itr.hasNext())
			{
				Row row=itr.next();
				temprow=sheet.getRow(row.getRowNum());
				if(row.getRowNum()<1)
				{
					continue;
				}
				else
				{
					if(jsonObj.getJSONArray("ATMAccountNo").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0))-1)==null)
						{
							ATMAccountNo=null;
						}
						else
						{
							ATMAccountNo=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("Amount2").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0))-1)==null)
						{
							Amount2=null;
						}
						else
						{
							Amount2=row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("Amount3").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0))-1)==null)
						{
							Amount3=null;
						}
						else
						{
							Amount3=row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ResponseCode2").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0))-1)==null)
						{
							ResponseCode2=null;
						}
						else
						{
							ResponseCode2=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReversalCode2").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0))-1)==null)
						{
							ReversalCode2=null;
						}
						else
						{
							ReversalCode2=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("FeeAmount").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0))-1)==null)
						{
							FeeAmount=null;
						}
						else
						{
							FeeAmount=row.getCell(Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("CurrencyCode").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0))-1)==null)
						{
							CurrencyCode=null;
						}
						else
						{
							CurrencyCode=row.getCell(Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("CustBalance").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0))-1)==null)
						{
							CustBalance=null;
						}
						else
						{
							CustBalance=row.getCell(Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("InterchangeBalance").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0))-1)==null)
						{
							InterchangeBalance=null;
						}
						else
						{
							InterchangeBalance=row.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ATMBalance").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0))-1)==null)
						{
							ATMBalance=null;
						}
						else
						{
							ATMBalance=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("BranchCode").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0))-1)==null)
						{
							BranchCode=null;
						}
						else
						{
							BranchCode=row.getCell(Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("InterchangeAccountNo").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0))-1)==null)
						{
							InterchangeAccountNo=null;
						}
						else
						{
							InterchangeAccountNo=row.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("AcquirerID").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0))-1)==null)
						{
							AcquirerID=null;
						}
						else
						{
							AcquirerID=row.getCell(Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("AuthCode").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0))-1)==null)
						{
							AuthCode=null;
						}
						else
						{
							AuthCode=row.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("Amount1").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0))-1)==null)
						{
							Amount1=null;
						}
						else
						{
							Amount1=row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReserveField5").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0))-1)==null)
						{
							ReserveField5=null;
						}
						else
						{
							ReserveField5=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReserveField1").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0))-1)==null)
						{
							ReserveField1=null;
						}
						else
						{
							ReserveField1=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ProcessingCode").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0))-1)==null)
						{
							ProcessingCode=null;
						}
						else
						{
							ProcessingCode=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsValueDateTime").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0))-1)==null)
						{
							TxnsValueDateTime=null;
						}
						else
						{
							TxnsValueDateTime=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsDateTime").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0))-1)==null)
						{
							TxnsDateTime=null;
						}
						else
						{
							TxnsDateTime=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReserveField3").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0))-1)==null)
						{
							ReserveField3=null;
						}
						else
						{
							ReserveField3=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReserveField4").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0))-1)==null)
						{
							ReserveField4=null;
						}
						else
						{
							ReserveField4=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsNumber").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0))-1)==null)
						{
							TxnsNumber=null;
						}
						else
						{
							TxnsNumber=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("CustAccountNo").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0))-1)==null)
						{
							CustAccountNo=null;
						}
						else
						{
							CustAccountNo=row.getCell(Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TerminalID").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0))-1)==null)
						{
							TerminalID=null;
						}
						else
						{
							TerminalID=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsPostDateTime").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0))-1)==null)
						{
							TxnsPostDateTime=null;
						}
						else
						{
							TxnsPostDateTime=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsDate").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0))-1)==null)
						{
							TxnsDate=null;
						}
						else
						{
							TxnsDate=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsTime").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0))-1)==null)
						{
							TxnsTime=null;
						}
						else
						{
							TxnsTime=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReferenceNumber").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0))-1)==null)
						{
							ReferenceNumber=null;
						}
						else
						{
							ReferenceNumber=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("CardNumber").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0))-1)==null)
						{
							CardNumber=null;
						}
						else
						{
							CardNumber=row.getCell(Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsAmount").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0))-1)==null)
						{
							TxnsAmount=null;
						}
						else
						{
							TxnsAmount=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsSubType").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0))-1)==null)
						{
							TxnsSubType=null;
						}
						else
						{
							TxnsSubType=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReserveField2").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField2").getString(0))-1)==null)
						{
							ReserveField2=null;
						}
						else
						{
							ReserveField2=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ResponseCode1").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0))-1)==null)
						{
							ResponseCode1=null;
						}
						else
						{
							ResponseCode1=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ReversalCode1").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0))-1)==null)
						{
							ReversalCode1=null;
						}
						else
						{
							ReversalCode1=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("ChannelType").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0))-1)==null)
						{
							ChannelType=null;
						}
						else
						{
							ChannelType=row.getCell(Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("DrCrType").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0))-1)==null)
						{
							DrCrType=null;
						}
						else
						{
							DrCrType=row.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0))-1).toString();
						}
					}
					if(jsonObj.getJSONArray("TxnsPerticulars").getString(0)!="0")
					{
						if(temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0))-1)==null)
						{
							TxnsPerticulars=null;
						}
						else
						{
							TxnsPerticulars=row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0))-1).toString();
						}
					}
					System.out.println("Terminal_ID   :"+TerminalID);
					System.out.println("TxnsSubType   :"+TxnsSubType);
					System.out.println("ReferenceNumber   :"+ReferenceNumber);
				}
//				StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spbulkinsertcbsdatadbbl");
//				query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(34, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(35, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(36, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(37, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(38, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(39, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(40, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(41, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(42, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(43, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(44, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(45, String.class, ParameterMode.IN);
//				query.registerStoredProcedureParameter(46, String.class, ParameterMode.IN);
//
//				query.setParameter(1, clientid);
//				query.setParameter(2, Transdate);
//				query.setParameter(3, Time);
//				query.setParameter(4, Terminal_ID);
//				query.setParameter(5, AccountNo);
//				query.setParameter(6, CardNumber);
//				query.setParameter(7, TraceNo);
//				query.setParameter(8, Amount);
//				query.setParameter(9, WithdrawalFlag);
//				query.setParameter(10, Response);
//				query.setParameter(11, ReversalFlag);
//				query.setParameter(12, Db_Cr);
//				query.setParameter(13, TransType);
//				query.setParameter(14, MerchantType);
//				query.setParameter(15, glCbs.getOriginalFilename());
//				query.setParameter(16, FileDate);
//				query.setParameter(17, createdby);
//				query.setParameter(18, ChannelID);
//				query.setParameter(19, ModeID);
//				query.setParameter(20, CardType);
//				query.setParameter(21, InterChangeAccNO);
//				query.setParameter(22, ATMAccountNo);
//				query.setParameter(23, Amount1);
//				query.setParameter(24, Amount2);
//				query.setParameter(25, Amount3);
//				query.setParameter(26, TxnsStatus);
//				query.setParameter(27, TxnsEntryType);
//				query.setParameter(28, TxnsNumber);
//				query.setParameter(29, TxnsPostDateTime);
//				query.setParameter(30, AuthCode);
//				query.setParameter(31, FeeAmount);
//				query.setParameter(32, CurrencyCode);
//				query.setParameter(33, CustBalance);
//				query.setParameter(34, InterchangeBalance);
//				query.setParameter(35, ATMBalance);
//				query.setParameter(36, BranchCode);
//				query.setParameter(37, ReserveField1);
//				query.setParameter(38, ReserveField2);
//				query.setParameter(39, ReserveField3);
//				query.setParameter(40, ReserveField4);
//				query.setParameter(41, ReserveField5);
//				query.setParameter(42, RevEntryLeg);
//				query.setParameter(43, NoOfDuplicate);
//				query.setParameter(44, stan);
//				query.setParameter(45, FilePath);
//				query.setParameter(46, ProcessingCode);
//
//				query.execute();

			
			}
		}
		catch (Exception e) {

		}
		return null;
	}
	

	@Override
	public List<JSONObject> importSwitchFile(MultipartFile sw, String clientid, String createdby) {

		String ext = FilenameUtils.getExtension(sw.getOriginalFilename());
		List ejTemp = new ArrayList();
		List<EjModel> temp = new ArrayList<>();

		String FileDate = sw.getOriginalFilename().substring(sw.getOriginalFilename().length() - 14,
				sw.getOriginalFilename().length() - 4);
		if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx") || ext.equalsIgnoreCase("csv")) {

			try {
				String Trans_Source = null, Trans_Mode = null, Trans_Type = null, Processing_Code = null,
						Trans_Amt = null, CardNumber = null, Stan = null, Trans_Date_Time = null,
						Acquirer_Inst_Code = null, Rec_Inst_Code = null, RRN = null, Auth_Code = null, Resp_Code = null,
						Terminal_ID = null, Terminal_Location = null, From_Account_Number = null,
						To_Account_Number = null, Trans_Cycle = null, Trans_Status = null, file_Description1 = null,
						file_Description2 = null;
				List xlstermList = new ArrayList();

				String ChannelID = null;
				String ModeID = null;
				String TerminalId = null, ReferenceNumber = null, CustAccountNo = null;
				String ATMAccountNo = null, TxnsDateTime = null, TxnsAmount = null, Amount1 = null, Amount2 = null;
				String Amount3 = null, TxnsStatus = null, TxnsType = null, TxnsSubType = null, TxnsEntryType = null;
				String TxnsNumber = null, TxnsPerticulars = null, DrCrType = null, ResponseCode = null,
						ReversalFlag = null;
				String TxnsPostDateTime = null, TxnsValueDateTime = null, AuthCode = null, ProcessingCode = null,
						FeeAmount = null;
				String CurrencyCode = null, CustBalance = null, ATMBalance = null, BranchCode = null;
				String ReserveField1 = null, ReserveField2 = null, ReserveField3 = null, ReserveField4 = null,
						ReserveField5 = null;
				String RevEntryLeg = null, NoOfDuplicate = null, FileName = null, FilePath = null;
				String CreatedOn = null, ModifiedOn = null, CreatedBy = null, ModifiedBy = null, stan = null;
				String InterchangeAccountNo = null, InterchangeBalance = null;
				HSSFWorkbook wb = new HSSFWorkbook(sw.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				HSSFCell cell = null;
				HSSFRow tempRow = null;
				while (itr.hasNext()) {
					Row row = itr.next();

					tempRow = sheet.getRow(row.getRowNum());
					int tempCountRow = row.getPhysicalNumberOfCells();
					if (row.getRowNum() < 3) {
						continue;
					} else {
						if (tempRow.getCell(0) == null) {
							Trans_Source = null;
						} else {
							Trans_Source = row.getCell(0).toString();
						}
						if (tempRow.getCell(1) == null) {
							Trans_Mode = null;
						} else {
							Trans_Mode = row.getCell(1).toString();
						}
						if (tempRow.getCell(2) == null) {
							Trans_Type = null;
						} else {
							Trans_Type = row.getCell(2).toString();
						}
						if (tempRow.getCell(3) == null) {
							Processing_Code = null;
						} else {
							Processing_Code = row.getCell(3).toString();
						}
						if (tempRow.getCell(4) == null) {
							Trans_Amt = null;
						} else {
							Trans_Amt = row.getCell(4).toString();
						}
						if (tempRow.getCell(5) == null) {
							CardNumber = null;
						} else {
							CardNumber = row.getCell(5).toString();
						}
						if (tempRow.getCell(6) == null) {
							Stan = null;
						} else {
							Stan = row.getCell(6).toString();
						}
						if (tempRow.getCell(7) == null) {
							Trans_Date_Time = null;
						} else {
							Trans_Date_Time = row.getCell(7).toString();
						}
						if (tempRow.getCell(8) == null) {
							Acquirer_Inst_Code = null;
						} else {
							Acquirer_Inst_Code = row.getCell(8).toString();
						}
						if (tempRow.getCell(9) == null) {
							Rec_Inst_Code = null;
						} else {
							Rec_Inst_Code = row.getCell(9).toString();
						}
						if (tempRow.getCell(10) == null) {
							RRN = null;
						} else {
							RRN = row.getCell(10).toString();
						}
						if (tempRow.getCell(11) == null) {
							Auth_Code = null;
						} else {
							Auth_Code = row.getCell(11).toString();
						}
						if (tempRow.getCell(12) == null) {
							Resp_Code = null;
						} else {
							Resp_Code = row.getCell(12).toString();
						}
						if (tempRow.getCell(13) == null) {
							Terminal_ID = null;
						} else {
							Terminal_ID = row.getCell(13).toString();
						}
						if (tempRow.getCell(14) == null) {
							Terminal_Location = null;
						} else {
							Terminal_Location = row.getCell(14).toString();
						}
						if (tempRow.getCell(15) == null) {
							From_Account_Number = null;
						} else {
							From_Account_Number = row.getCell(15).toString();
						}
						if (tempRow.getCell(16) == null) {
							To_Account_Number = null;
						} else {
							To_Account_Number = row.getCell(16).toString();
						}
						if (tempRow.getCell(17) == null) {
							Trans_Cycle = null;
						} else {
							Trans_Cycle = row.getCell(17).toString();
						}
						if (tempRow.getCell(18) == null) {
							Trans_Status = null;
						} else {
							Trans_Status = row.getCell(18).toString();
						}

						StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spinsertswitchdata");
						query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
						query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);

						query.setParameter(1, clientid);
						query.setParameter(2, Trans_Source);
						query.setParameter(3, Trans_Mode);
						query.setParameter(4, Trans_Type);
						query.setParameter(5, Processing_Code);
						query.setParameter(6, Trans_Amt);
						query.setParameter(7, CardNumber);
						query.setParameter(8, Stan);
						query.setParameter(9, Trans_Date_Time);
						query.setParameter(10, Acquirer_Inst_Code);
						query.setParameter(11, Rec_Inst_Code);
						query.setParameter(12, RRN);
						query.setParameter(13, Auth_Code);
						query.setParameter(14, Resp_Code);
						query.setParameter(15, Terminal_ID);
						query.setParameter(16, Terminal_Location);
						query.setParameter(17, From_Account_Number);
						query.setParameter(18, To_Account_Number);
						query.setParameter(19, Trans_Cycle);
						query.setParameter(20, Trans_Status);
						query.setParameter(21, sw.getOriginalFilename());
						query.setParameter(22, FileDate);
						query.setParameter(23, createdby);

						query.execute();

					}

				}
			} catch (Exception e) {

			}
		}
		return ejTemp;
	}

	@Override
	public List<JSONObject> importFile(String participant_ID, String transaction_Type, String from_Account_Type,
			String to_Account_Type, String transaction_Serial_Number, String response_Code, String pan_Number,
			String member_Number, String approval_Number, String system_Trace_Audit_Number, String transaction_Date,
			String transaction_Time, String merchant_Category_Code, String card_Acceptor_Settlement_Date,
			String card_Acceptor_ID, String card_Acceptor_Terminal_ID, String card_Acceptor_Terminal_Location,
			String acquirer_ID, String acquirer_Settlement_Date, String transaction_Currency_code,
			String transaction_Amount, String actual_Transaction_Amount, String transaction_Acitivity_fee,
			String acquirer_settlement_Currency_Code, String acquirer_settlement_Amount, String acquirer_Settlement_Fee,
			String acquirer_settlement_processing_fee, String transaction_Acquirer_Conversion_Rate, String forceMatch,
			String clientid, String cycle, String fileDate, String createdby) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ntsAtmFile(String description, double noOftxn, double credit, double debit, MultipartFile file,
			String date, String clientid, String createdby) {
		String fileName = file.getOriginalFilename();
		String cycle = fileName.substring(fileName.length() - 6, fileName.length() - 4);
		String remarks = "NA";

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPIMPORTNPCIATMSETTLEMENT");

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, date);
		query.setParameter(2, cycle);
		query.setParameter(3, description);
		query.setParameter(4, String.valueOf(noOftxn));
		query.setParameter(5, String.valueOf(debit));
		query.setParameter(6, String.valueOf(credit));
		query.setParameter(7, remarks);
		query.setParameter(8, clientid);
		query.setParameter(9, fileName);
		query.setParameter(10, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();

	}

	@Override
	public List<JSONObject> getchannelmodeinfo(String clientid) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELMODEINFO");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ChannelID", fields[0]);
			obj.put("ChnnelName", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getfieldidentification(String clientid, String vendorid, String channelid, String modeid,
			String formatid) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFIELDIDENTIFICATIONDETAILS");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));

		query.setParameter(2, Integer.parseInt(vendorid));

		query.setParameter(3, Integer.parseInt(channelid));

		query.setParameter(4, Integer.parseInt(modeid));

		query.setParameter(5, Integer.parseInt(formatid));

		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ClientID", fields[0]);
			obj.put("VendorID", fields[1]);
			obj.put("FormatID", fields[2]);
			obj.put("TerminalCode", fields[3]);
			obj.put("BIN_No", fields[4]);
			obj.put("AcquirerID", fields[5]);
			obj.put("ReversalCode1", fields[6]);
			obj.put("ReversalCode2", fields[7]);
			obj.put("ReversalCode3", fields[8]);
			obj.put("ReversalType", fields[9]);
			obj.put("TxnDateTime", fields[10]);
			obj.put("TxnValueDateTime", fields[11]);
			obj.put("TxnPostDateTime", fields[12]);
			obj.put("ATMType", fields[13]);
			obj.put("CDMType", fields[14]);
			obj.put("POSType", fields[15]);
			obj.put("ECOMType", fields[16]);
			obj.put("IMPType", fields[17]);
			obj.put("UPIType", fields[18]);
			obj.put("MicroATMType", fields[19]);
			obj.put("MobileRechargeType", fields[20]);
			obj.put("BalanceEnquiry", fields[21]);
			obj.put("MiniStatement", fields[22]);
			obj.put("PinChange", fields[23]);
			obj.put("ChequeBookReq", fields[24]);
			obj.put("ResponseCode1", fields[25]);
			obj.put("ResponseCode2", fields[26]);
			obj.put("ResponseType", fields[27]);
			obj.put("EODCode", fields[28]);
			obj.put("OfflineCode", fields[29]);
			obj.put("DebitCode", fields[30]);
			obj.put("CreditCode", fields[31]);
			obj.put("RevEntryLeg", fields[32]);
			JSONObjects.add(obj);
		}
		return JSONObjects;

	}

	@Override
	public List<JSONObject> getfilevendordetails(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFILEVENDORDETAILS");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("vendorid", fields[0]);
			obj.put("vendorname", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
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
		// TODO Auto-generated method stub

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPADDFIELDCONFIG");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(26, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(28, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(29, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(34, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(p_CLIENTID));
		query.setParameter(2, Integer.parseInt(p_VENDORID));
		query.setParameter(3, Integer.parseInt(p_FORMATID));
		query.setParameter(4, p_TERMINALCODE);
		query.setParameter(5, p_BINNO);
		query.setParameter(6, p_ACQUIRERID);
		query.setParameter(7, p_REVCODE1);
		query.setParameter(8, p_REVCODE2);
		query.setParameter(9, p_REVTYPE);
		query.setParameter(10, Integer.parseInt(p_REVENTRY));
		query.setParameter(11, p_TXNDATETIME);
		query.setParameter(12, p_TXNVALUEDATETIME);
		query.setParameter(13, p_TXNPOSTDATETIME);
		query.setParameter(14, p_ATMTYPE);
		query.setParameter(15, p_POSTYPE);
		query.setParameter(16, p_ECOMTYPE);
		query.setParameter(17, p_IMPSTYPE);
		query.setParameter(18, p_UPITYPE);
		query.setParameter(19, p_MICROATMTYPE);
		query.setParameter(20, p_MOBILERECHARGETYPE);
		query.setParameter(21, p_DEPOSIT);
		query.setParameter(22, p_BALENQ);
		query.setParameter(23, p_MINISTATEMENT);
		query.setParameter(24, p_PINCHANGE);
		query.setParameter(25, p_CHEQUEBOOKREQ);
		query.setParameter(26, p_RESPCODE1);
		query.setParameter(27, p_RESPCODE2);
		query.setParameter(28, p_RESPTPE);
		query.setParameter(29, p_EODCODE);
		query.setParameter(30, p_OFFLINECODE);
		query.setParameter(31, p_DEBITCODE);
		query.setParameter(32, p_CREDITCODE);
		query.setParameter(33, createdby);

		query.execute();

		List<Object[]> result = query.getResultList();

		return result.toString();

	}

	@Override
	public List<JSONObject> getformatid(String clientid, String vendorid) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFORMATID");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(vendorid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			JSONObject obj = new JSONObject();
			obj.put("formatid", result.get(0));
			
			System.out.println("formatid :"+result.get(0));
			JSONObjects.add(obj);

		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchanneldetails(String clientid) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELDETAILS");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("channelid", fields[0]);
			obj.put("channelName", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchannelmodeinfo(String clientid, String channelid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELMODEINFO");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ModeID", fields[0]);
			obj.put("TransactionMode", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getmatchingmodeinfo(String clientid, String channelid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spgetmatchingmode");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ModeID", fields[0]);
			obj.put("TransactionMode", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getMatchingRuleSetForClient(String clientid, String channelid, String ruleid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetMatchingRuleSetForClient");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.setParameter(3, ruleid);
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("MatchingID", fields[0]);
			obj.put("ChannelID", fields[1]);
			obj.put("ColumnName", fields[2]);
			obj.put("ModeID", fields[3]);
			obj.put("RuleType", fields[4]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getaddmatchingruleconfig(String clientid, String columnname, String channelid,
			String modeid, String ruletype, String createdby) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPADDMATCHINGRULECONFIG1");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);

		query.registerStoredProcedureParameter(7, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, columnname);
		query.setParameter(3, channelid);
		query.setParameter(4, modeid);
		query.setParameter(5, ruletype);
		query.setParameter(6, createdby);
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			// Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("Status", result.toString());
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getStatusMaster() {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetStatusMaster");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.REF_CURSOR);

		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("statusid", fields[0]);
			obj.put("statusname", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getclientreportdetails(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGECLIENTREPORTDETAILS");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("clientid", fields[0]);
			obj.put("clientname", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> forcesettlementtxns(String clientid, String channelid, String modeid, String glstatus,
			String ejstatus, String nwstatus, String swstatus, String fromdatetxn, String todatetxn, String recontype,
			String settlementtype, String user, String tdrc, String branchid) {
		// TODO Auto-generated method stub
		branchid = "0";
		System.out.println("fromdatetxn" + fromdatetxn + " " + "todatetxn" + todatetxn);
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spforcesettlementtxns");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(15, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, clientid);
		query.setParameter(2, channelid);
		query.setParameter(3, modeid);
		query.setParameter(4, glstatus);
		query.setParameter(5, ejstatus);
		query.setParameter(6, nwstatus);
		query.setParameter(7, swstatus);
		query.setParameter(8, fromdatetxn);
		query.setParameter(9, todatetxn);
		query.setParameter(10, recontype);
		query.setParameter(11, settlementtype);
		query.setParameter(12, user);
		query.setParameter(13, tdrc);
		query.setParameter(14, branchid);
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("Status", fields[0]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchanneltypeall(String clientid, String userid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELTYPEALLINTABLE59MENTIONED");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, userid);
		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("channelid", fields[0]);
			obj.put("channelname", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getFileType(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetFileType");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));

		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("ID", fields[0]);
			obj.put("channelID", fields[1]);
			obj.put("modeid", fields[2]);
			obj.put("vendorid", fields[3]);
			obj.put("filename", fields[4]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchanneltyperun(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELTYPERUN");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));

		query.execute();

		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();

			obj.put("channelID", fields[0]);
			obj.put("channelName", fields[1]);

			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getModeTypeRun(String clientid, String channelid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETMODETYPERUN");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("modeid", fields[0]);
			obj.put("modename", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public String changeUndefindToNull() throws InterruptedException {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPREPLACEUNDEFINDVALUETONULL");
		query.execute();
		return "ok";
	}

	@Override
	public List<JSONObject> getFileFormatDefualt(String p_FileExt, String p_SeparatorType, String p_ChannelID,
			String p_ModeID, String p_VendorID) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFILEFORMATDEFUALT1");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, p_FileExt);
		query.setParameter(2, p_SeparatorType);
		query.setParameter(3, Integer.parseInt(p_ChannelID));
		query.setParameter(4, Integer.parseInt(p_ModeID));
		query.setParameter(4, Integer.parseInt(p_VendorID));
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("FormatID", fields[0]);
			obj.put("ClientName", fields[1]);
			obj.put("FileExtention", fields[2]);
			obj.put("VendorName", fields[3]);
			obj.put("FormatDescriptionXml", fields[4]);
			obj.put("CutOffTime", fields[5]);
			obj.put("StartIndex", fields[6]);
			obj.put("EndIndex", fields[7]);
			obj.put("FormatStatus", fields[8]);
			obj.put("VendorType", fields[9]);
			obj.put("FormatStatus", fields[10]);
			obj.put("FilePrefix", fields[11]);
			obj.put("ChannelID", fields[12]);
			obj.put("ModeID", fields[13]);
			obj.put("SepratorType", fields[14]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getformatfileinxml(String clientid, int i) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETFORMATXMLDESCIPTION");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, i);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			JSONObject obj = new JSONObject();
			obj.put("FormatDescriptionXml", result.get(0));
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}
	
	public List<JSONObject> getcbsformatfileinxml(String clientid, String fileTypeName) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCBSFORMATDESCRIPTIONXML");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
//		query.setParameter(2, i);
		query.setParameter(2, fileTypeName);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("clientid", fields[0]);
			obj.put("FormatDescriptionXml", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> importFileNpciATMFilesACQ(JSONObject obj, String clientid, String fileDate, String cycle,
			String forceMatch, String createdby, NodeList nodeList) {

		String participant_ID = null, nodeName = null, transaction_Type = null, from_Account_Type = null,
				to_Account_Type = null, RRN = null, response_Code = null, card_number = null, member_Number = null,
				approval_Number = null, system_Trace_Audit_Number = null, transaction_Date = null,
				transaction_Time = null, merchant_Category_Code = null, card_Acceptor_Settlement_Date = null,
				card_Acceptor_ID = null, card_Acceptor_Terminal_ID = null, card_Acceptor_Terminal_Location = null,
				acquirer_ID = null, acquirer_Settlement_Date = null, transaction_Currency_code = null,
				transaction_Amount = null, actual_Transaction_Amount = null, transaction_Acitivity_fee = null,
				acquirer_settlement_Currency_Code = null, acquirer_settlement_Amount = null,
				acquirer_Settlement_Fee = null, acquirer_settlement_processing_fee = null,
				transaction_Acquirer_Conversion_Rate = null;
		nodeName = nodeList.item(0).getNodeName();
		if (obj.containsKey(nodeName)) {
			participant_ID = obj.get(nodeName).toString();
		}

		nodeName = nodeList.item(1).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Type = obj.get(nodeName).toString();
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
			acquirer_Settlement_Date = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(19).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Currency_code = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(20).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Amount = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(21).getNodeName();
		if (obj.containsKey(nodeName)) {
			actual_Transaction_Amount = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(22).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Acitivity_fee = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(23).getNodeName();
		if (obj.containsKey(nodeName)) {
			acquirer_settlement_Currency_Code = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(24).getNodeName();
		if (obj.containsKey(nodeName)) {
			acquirer_settlement_Amount = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(25).getNodeName();
		if (obj.containsKey(nodeName)) {
			acquirer_Settlement_Fee = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(26).getNodeName();
		if (obj.containsKey(nodeName)) {
			acquirer_settlement_processing_fee = obj.get(nodeName).toString();
		}
		nodeName = nodeList.item(27).getNodeName();
		if (obj.containsKey(nodeName)) {
			transaction_Acquirer_Conversion_Rate = obj.get(nodeName).toString();
		}
		
		
		
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPIMPORTNPCIACQUIEREFILE");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(15, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(20, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(22, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(23, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(24, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(26, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(27, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(28, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(29, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(30, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(31, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(32, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(33, String.class, ParameterMode.IN);
//		query.registerStoredProcedureParameter(34, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, participant_ID);
		query.setParameter(3, transaction_Type);
		query.setParameter(4, from_Account_Type);
		query.setParameter(5, to_Account_Type);
		query.setParameter(6, RRN);
		query.setParameter(7, response_Code);
		query.setParameter(8, card_number);
		query.setParameter(9, member_Number);
		query.setParameter(10, approval_Number);
		query.setParameter(11, Long.parseLong(system_Trace_Audit_Number));
		query.setParameter(12, transaction_Date);
		query.setParameter(13, Long.parseLong(transaction_Time));
		query.setParameter(14, Long.parseLong(merchant_Category_Code));
		query.setParameter(15, Long.parseLong(card_Acceptor_Settlement_Date));
		query.setParameter(16, card_Acceptor_ID);
		query.setParameter(17, card_Acceptor_Terminal_ID);
		query.setParameter(18, card_Acceptor_Terminal_Location);
		query.setParameter(19, acquirer_ID);
		query.setParameter(20, Long.parseLong(acquirer_Settlement_Date));
		query.setParameter(21, transaction_Currency_code);
		query.setParameter(22, Long.parseLong(transaction_Amount));

		query.setParameter(23, Long.parseLong(actual_Transaction_Amount));
		query.setParameter(24, Long.parseLong(transaction_Acitivity_fee));

		query.setParameter(25, acquirer_settlement_Currency_Code);

		query.setParameter(26, Long.parseLong(acquirer_settlement_Amount));

		query.setParameter(27, acquirer_Settlement_Fee);
		query.setParameter(28, Long.parseLong(acquirer_settlement_processing_fee));

		query.setParameter(29, Long.parseLong(transaction_Acquirer_Conversion_Rate));
		query.setParameter(30, forceMatch);
		query.setParameter(31, cycle);
		query.setParameter(32, fileDate);
		query.setParameter(33, createdby);

		query.execute();
		
		count++;
		System.out.println("count:  "+count);
//		List<Object[]> result = query.getResultList();
//		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
//		for (Object record : result) {
//			JSONObject obj1 = new JSONObject();
//			obj1.put("resultFromImportFile", result.toString());
//			JSONObjects.add(obj1);
//		}
		return null;
	}

	@Override
	public List<String> getData() {
		return null;
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
}
