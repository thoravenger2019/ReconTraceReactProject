package com.admin.dao;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.EncryptedDocumentException;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
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
import com.admin.model.MenuModel;
import com.admin.model.NpciAcqModel;
import com.admin.model.SubMenuModel;
import com.admin.model.User;

@Repository
public class Trace_DAO_Imp implements Trace_DAO {

	User eq = null;
	int count = 0;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DataSource datasource;

	public Trace_DAO_Imp() {

	}

	public Trace_DAO_Imp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public static File csvToexcel(MultipartFile csvFile, String sepratorType) throws Exception {

		byte[] bytes = csvFile.getBytes();
		String completeData = new String(bytes);
		String[] rows = completeData.split(",");
		// String[] columns = rows[0].split(",");
		ArrayList arList = null;
		ArrayList al = null;
		arList = new ArrayList();
		System.out.println("completeData  " + completeData.length());
//        for(int i=0;i<rows.length;i++) {  
//        	System.out.println(rows[i]);
//        }
		BufferedReader br;
		List<String> result = new ArrayList<>();
		try {

			String line;
			InputStream is = csvFile.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			arList = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String strar[] = line.split(",");
				al = new ArrayList<>();
				for (int i = 0; i < strar.length; i++) {
					System.out.println(strar[i]);
					al.add(strar[i]);
				}
				arList.add(al);
			}

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

//		ArrayList arList = null;
//		ArrayList al = null;
//		String thisLine=null;
//		int count = 0;
		File convFile = new File(csvFile.getOriginalFilename());
		convFile.createNewFile();
//		File convFile1 = new File(csvFile.getOriginalFilename());
//		convFile.createNewFile();
////		 Path pathToFile = Paths.get(convFile.getPath());
////		FileInputStream fis = new FileInputStream(convFile);
////		BufferedReader br = new BufferedReader(new InputStreamReader(fis,));
////		FileInputStream fis = new FileInputStream(convFile);
////		DataInputStream myInput = new DataInputStream(fis);
////		String line = br.readLine();
//		
//		int i = 0;
//		arList = new ArrayList();
//		FileReader fr=new FileReader(convFile);
//		BufferedReader br=new BufferedReader(fr);
//		StringBuffer sb=new StringBuffer();
////		while ((thisLine = myInput.readLine()) != null) {
////			al = new ArrayList();
////			String strar[] = thisLine.split(sepratorType);
////			for (int j = 0; j < strar.length; j++) {
////				al.add(strar[j]);
////			}
////			arList.add(al);
////			System.out.println();
////			i++;
////		}
//		while( (thisLine=br.readLine())!=null )
//		{
//			sb.append(thisLine);
//			al = new ArrayList();
//			String temp=sb.toString();
//			String strar[] = temp.split(sepratorType);
//			for (int j = 0; j < strar.length; j++) {
//				al.add(strar[j]);
//				System.out.println(strar[j]);
//			}
//			arList.add(al);
//			
//		}
//
		try {
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("new sheet");
			for (int k = 0; k < arList.size(); k++) {
				ArrayList ardata = (ArrayList) arList.get(k);
				System.out.println("ardata " + ardata.size());
				XSSFRow row = sheet.createRow((short) 0 + k);
				for (int p = 0; p < ardata.size(); p++) {
					System.out.print(ardata.get(p));
					XSSFCell cell = row.createCell((short) p);
					cell.setCellValue(ardata.get(p).toString());
				}
				System.out.println();
			}
			FileOutputStream fileOut = new FileOutputStream(convFile);
			FileOutputStream fileOut1 = new FileOutputStream("C:\\Users\\suyog.mate.MAXIMUS\\Desktop\\azizi\\s1.xls");
			hwb.write(fileOut);
			hwb.write(fileOut1);
			fileOut.close();
			fileOut1.close();
			System.out.println("Your excel file has been generated");
		} catch (Exception ex) {
		}
		return convFile;
	}

	public static String checkDateFormat(String format, String strDate) throws ParseException {
		System.out.println("strDate   " + strDate);
		String d = null;
		String MM = null;
		String dd = null;
		String HH = "00";
		String mm = "00";
		String ss = "00";
		String HHmmss = null;
		String FullTime = "00";
		String yyyyHHmmss = null;
		String yyyy = null;
//		String splitFormatString=strDate.replaceAll("\\p{Punct}", "");
//		String splitFormatString1=format.replaceAll("\\p{Punct}", "");
//		char[] ch = new char[splitFormatString.length()]; 
//		char[] ch1 = new char[format.length()]; 
		String[] split = format.split(" ?(?<!\\G)((?<=[^\\p{Punct}])(?=\\p{Punct})|\\b) ?");
		String[] split1 = strDate.split(" ?(?<!\\G)((?<=[^\\p{Punct}])(?=\\p{Punct})|\\b) ?");

		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
			if (split[i].equals("yyyy")) {
				yyyy = split1[i];
			}
			if (split[i].equals("yy")) {
				yyyy = "20" + split1[i];
			}
			if (split[i].equals("MM")) {
				MM = split1[i];
			}
			if (split[i].equals("MMM")) {
//				SimpleDateFormat simpleformat = new SimpleDateFormat("MMM");
				Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(split1[i]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int month = cal.get(Calendar.MONTH);
				MM = String.valueOf(month + 1);
			}
			if (split[i].equals("dd")) {
				dd = split1[i];
			}
			if (split[i].equals("HH")) {
				HH = split1[i];
			}
			if (split[i].equals("hh")) {
				HH = split1[i];
			}
			if (split[i].equals("mm")) {
				mm = split1[i];
			}
			if (split[i].equals("ss")) {
				ss = split1[i];
			}
			if (split[i].equals("HHmmss")) {
				HHmmss = split1[i];
				FullTime = HHmmss.substring(0, 2);
			}

			if (split[i].equals("yyyyHHmmss")) {
				yyyyHHmmss = split1[i];
			}
		}
		for (int i = 0; i < split.length; i++) {
			if (split[i].equals("MM") && (split1[i].length()) < 2) {
				MM = "0" + split1[i];
			}
			System.out.println("  " + split[i]);
			if (split[i].equals("MMM") && (MM.length()) < 2) {
				MM = "0" + MM;
			}
			if (split[i].equals("dd") && (split1[i].length()) < 2) {
				dd = "0" + split1[i];
			}
			if (split[i].equals("HH") && (split1[i].length()) < 2) {
				HH = "0" + split1[i];
			}
			if (split[i].equals("hh") && (split1[i].length()) < 2) {
				HH = "0" + split1[i];
				System.out.println(HH);
			}
			if (split[i].equals("mm") && (split1[i].length()) < 2) {
				mm = "0" + split1[i];
			}
			if (split[i].equals("ss") && (split1[i].length()) < 2) {
				ss = "0" + split1[i];
			}
		}

		if (ss == null) {
			ss = "00";
		}
		System.out.println(HH);
//		if (Integer.parseInt(HH) <= 12 && Integer.parseInt(FullTime) <= 12) {
		if (HHmmss != null) {
			String tempstr = dd + MM + yyyy + HHmmss;
			System.out.println("tempstr: " + tempstr);
			DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date tempDate = formatter.parse(tempstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = sdf.format(tempDate);
		} else {
			String tempstr = dd + MM + yyyy + HH + mm + ss;
			DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date tempDate = formatter.parse(tempstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = sdf.format(tempDate);
		}

//		}
		System.out.println("d   " + d);
		return d;
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
			String createdby, String salt) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spadduser");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, userid);
		query.setParameter(2, password);
		query.setParameter(3, firstname);
		query.setParameter(4, lastname);
		query.setParameter(5, Integer.parseInt(roleid));
		query.setParameter(6, Integer.parseInt(clientid));
		query.setParameter(7, branchid);
		query.setParameter(8, emailid);
		query.setParameter(9, contactno);
		query.setParameter(10, securityq);
		query.setParameter(11, securitya);
		query.setParameter(12, createdby);
		query.setParameter(13, salt);
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
	public List mapBranchMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user, String clientid) {
		String ext = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());

		if (ext.equalsIgnoreCase("xls")) {
			System.out.println("xls");
			try {
				String branchName = null, address = null, contactNO = null, emailID = null, concernPerson = null,
						branchCode = null, isHO = null, isRemoved = null, createdBy = null, createdOn = null,
						modifiedOn = null, modifiedBy = null, xlsresult = null;
				List xlsList = new ArrayList();
				HSSFWorkbook wb = new HSSFWorkbook(reapExcelDataFile.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				System.out.println(sheet.getLastRowNum());
				Connection con = datasource.getConnection();
				CallableStatement stmt = con.prepareCall("{call SPBRANCHMARSTERFILEOPT(?,?,?,?,?,?,?,?)}");
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				Boolean status = false;
				HSSFRow hr = null;
				while (itr.hasNext()) {
					Row row = itr.next();
					hr = sheet.getRow(row.getRowNum());
					if (row.getRowNum() == 0) {
						continue;
					} else {
//						branchID = row.getCell(0).toString();
//						Float f = Float.parseFloat(branchID);
//						int branchInNum = f.intValue();
//						System.out.println("branchid" + branchID + " " + f);
						status = false;
//						clientID = row.getCell(1).toString();
//						Float cid = Float.parseFloat(clientID);
//						int clientInNum = f.intValue();
						if (hr.getCell(0) == null) {
							branchCode = null;
						} else {
							branchCode = row.getCell(0).toString();
						}
						if (hr.getCell(1) == null) {
							branchName = null;
						} else {
							branchName = row.getCell(1).toString();
						}
						if (hr.getCell(2) == null) {
							address = null;
						} else {
							address = row.getCell(2).toString();
						}
						if (hr.getCell(3) == null) {
							contactNO = null;
						} else {
							contactNO = row.getCell(3).toString();
						}
						if (hr.getCell(4) == null) {
							emailID = null;
						} else {
							emailID = row.getCell(4).toString();
						}

						if (hr.getCell(5) == null) {
							concernPerson = null;
						} else {
							concernPerson = row.getCell(4).toString();
						}

//						StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPBRANCHMARSTERFILEOPT");
//						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(10, String.class, ParameterMode.REF_CURSOR);
						stmt.setInt(1, Integer.parseInt(clientid));
						stmt.setString(2, branchName);
						stmt.setString(3, address);
						stmt.setString(4, contactNO);
						stmt.setString(5, emailID);
						stmt.setString(6, concernPerson);
						stmt.setString(7, branchCode);
						stmt.setString(8, user);

						stmt.addBatch();
						stmt.executeBatch();

//						System.out.println();
//						List<Object[]> resultxls = query.getResultList();
//						if (resultxls.toString().equalsIgnoreCase("2")) {
//							xlsList.add(branchName);
//							xlsList.add(clientInNum);
//							xlsList.add(branchName);
//							xlsList.add(address);
//							xlsList.add(contactNO);
//							xlsList.add(emailID);
//							xlsList.add(concernPerson);
//							xlsList.add(branchCode);
//							xlsList.add(user);
//							xlsList.add("||");
//
//						}

					}

				}
				stmt.close();
				con.close();
				xlsList.add("Branch Operation Done");
				return xlsList;
			}

			catch (IOException | SQLException e) {
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
				Connection con = datasource.getConnection();
				CallableStatement stmt = con.prepareCall("{call SPBRANCHMARSTERFILEOPT(?,?,?,?,?,?,?,?)}");
				Iterator<Row> itr = sheet.iterator();
				XSSFRow xr = null;
				while (itr.hasNext()) {
					Row row = itr.next();
					xr = sheet.getRow(row.getRowNum());
					if (row.getRowNum() == 0) {
						continue;
					} else {
						if (xr.getCell(0) == null) {
							branchCode = null;
						} else {
							branchCode = row.getCell(0).toString();
						}
						if (xr.getCell(1) == null) {
							branchName = null;
						} else {
							branchName = row.getCell(1).toString();
						}
						if (xr.getCell(2) == null) {
							address = null;
						} else {
							address = row.getCell(2).toString();
						}
						if (xr.getCell(3) == null) {
							contactNO = null;
						} else {
							contactNO = row.getCell(3).toString();
						}
						if (xr.getCell(4) == null) {
							emailID = null;
						} else {
							emailID = row.getCell(4).toString();
						}

						if (xr.getCell(5) == null) {
							concernPerson = null;
						} else {
							concernPerson = row.getCell(4).toString();
						}

//						StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPBRANCHMARSTERFILEOPT");
//						query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
//						query.registerStoredProcedureParameter(10, String.class, ParameterMode.REF_CURSOR);

						stmt.setInt(1, Integer.parseInt(clientid));
						stmt.setString(2, branchName);
						stmt.setString(3, address);
						stmt.setString(4, contactNO);
						stmt.setString(5, emailID);
						stmt.setString(6, concernPerson);
						stmt.setString(7, branchCode);
						stmt.setString(8, user);

						stmt.addBatch();
						stmt.executeBatch();
//						List<Object[]> xlsxresult = query.getResultList();
//
//						if (xlsxresult.toString().equalsIgnoreCase("[2]")) {
//							xlsxList.add(branchID);
//							xlsxList.add(clientID);
//							xlsxList.add(branchName);
//							xlsxList.add(address);
//							xlsxList.add(contactNO);
//							xlsxList.add(emailID);
//							xlsxList.add(concernPerson);
//							xlsxList.add(branchCode);
//							xlsxList.add(user);
//						}
					}

				}
				stmt.close();
				con.close();
				xlsxList.add("Branch Operation Done");
				return xlsxList;
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

		} else {
			return null;
		}

		return null;

	}

	@Override
	public List mapTerminalMasterReapExcelDatatoDB(MultipartFile reapExcelDataFile, String user, String clientid) {

		String ext = FilenameUtils.getExtension(reapExcelDataFile.getOriginalFilename());
		if (ext.equalsIgnoreCase("xls")) {

			try {
				String terminalID = null, terminalLocation = null, glAccountNo = null, channel = null,
						branchCode = null, terminalType = null, contactNo = null, emailID = null, concernPerson = null,
						isRemoved = null, createdBy = null, checkerBy = null, createdOn = null, modifiedOn = null,
						modifiedBy = null;
				List xlstermList = new ArrayList();
				Connection con = datasource.getConnection();
				CallableStatement stmt = con.prepareCall("{call SPTERMINALMASTERFILEOPT(?,?,?,?,?,?,?,?,?)}");
				HSSFWorkbook wb = new HSSFWorkbook(reapExcelDataFile.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				Iterator<Row> itr = sheet.iterator();
				HSSFRow hr = null;
				while (itr.hasNext()) {
					Row row = itr.next();
					hr = sheet.getRow(row.getRowNum());
					if (row.getRowNum() == 0) {
						continue;
					} else {
						if (hr.getCell(0) == null) {
							terminalID = null;
						} else {
							terminalID = row.getCell(0).toString();
						}
						if (hr.getCell(1) == null) {
							terminalLocation = null;
						} else {
							terminalLocation = row.getCell(1).toString();
						}
						if (hr.getCell(2) == null) {
							glAccountNo = null;
						} else {
							glAccountNo = row.getCell(2).toString();
						}
						if (hr.getCell(3) == null) {
							channel = null;
						} else {
							channel = row.getCell(3).toString();
						}
						if (hr.getCell(4) == null) {
							branchCode = null;
						} else {
							branchCode = row.getCell(4).toString();
						}
						if (hr.getCell(5) == null) {
							terminalType = null;
						} else {
							terminalType = row.getCell(4).toString();
						}
						if (hr.getCell(6) == null) {
							concernPerson = null;
						} else {
							concernPerson = row.getCell(6).toString();
						}

						stmt.setInt(1, Integer.parseInt(clientid));
						stmt.setString(2, terminalID);
						stmt.setString(3, terminalLocation);
						stmt.setString(4, glAccountNo);
						stmt.setString(5, channel);
						stmt.setString(6, branchCode);
						stmt.setString(7, terminalType);
						stmt.setString(8, concernPerson);
						stmt.setString(9, user);
						stmt.addBatch();
						stmt.executeBatch();

					}

				}
				stmt.close();
				con.close();
				xlstermList.add("Terminal Operation Done");
				return xlstermList;
			} catch (IOException | NumberFormatException | SQLException e) {
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
				Connection con = datasource.getConnection();
				CallableStatement stmt = con.prepareCall("{call SPTERMINALMASTERFILEOPT(?,?,?,?,?,?,?,?,?)}");
				XSSFWorkbook wb = new XSSFWorkbook(reapExcelDataFile.getInputStream());
				XSSFSheet sheet = wb.getSheetAt(0);
				BranchEntry be = new BranchEntry();
				XSSFRow xr = null;
				Iterator<Row> itr = sheet.iterator();
				while (itr.hasNext()) {

					Row row = itr.next();
					xr = sheet.getRow(row.getRowNum());
					// if(row.)
					if (row.getRowNum() == 0) {
						continue;
					} else {
						if (xr.getCell(0) == null) {
							terminalID = null;
						} else {
							terminalID = row.getCell(0).toString();
						}
						if (xr.getCell(1) == null) {
							terminalLocation = null;
						} else {
							terminalLocation = row.getCell(1).toString();
						}
						if (xr.getCell(2) == null) {
							glAccountNo = null;
						} else {
							glAccountNo = row.getCell(2).toString();
						}
						if (xr.getCell(3) == null) {
							channel = null;
						} else {
							channel = row.getCell(3).toString();
						}
						if (xr.getCell(4) == null) {
							branchCode = null;
						} else {
							branchCode = row.getCell(4).toString();
						}
						if (xr.getCell(5) == null) {
							terminalType = null;
						} else {
							terminalType = row.getCell(4).toString();
						}
						if (xr.getCell(6) == null) {
							concernPerson = null;
						} else {
							concernPerson = row.getCell(6).toString();
						}

						stmt.setInt(1, Integer.parseInt(clientid));
						stmt.setString(2, terminalID);
						stmt.setString(3, terminalLocation);
						stmt.setString(4, glAccountNo);
						stmt.setString(5, channel);
						stmt.setString(6, branchCode);
						stmt.setString(7, terminalType);
						stmt.setString(8, concernPerson);
						stmt.setString(9, user);

						stmt.addBatch();
						stmt.executeBatch();

					}

				}
				stmt.close();
				con.close();
				xlsxTermList.add("Terminal Operation Done");
				return xlsxTermList;
			} catch (IOException | NumberFormatException | SQLException e) {
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
	public List<JSONObject> importEJFileData(MultipartFile ej, String clientid, String createdby, String fileTypeName)
			throws ParseException {
		String EjFileName = ej.getOriginalFilename();
		String extFile = FilenameUtils.getExtension(EjFileName);
		List<String> content = null;
		String contentData = null;

		List<JSONObject> cbsIdentificationfileformatxml = getcbsswitchejIdentificationfileformatxml(clientid,
				fileTypeName, "." + extFile);
		System.out.println(cbsIdentificationfileformatxml);
		try {

			Connection con = datasource.getConnection();
			CallableStatement stmt = con.prepareCall(
					"{call SPINSERTEJDATA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			String ATMAccountNo = "";
			String Amount1 = "";
			String Amount2 = "";
			String Amount3 = "";
			String Amount = null;
			String ReqCount = null;
			String PickupCount = null;
			String ResponseCode2 = "";
			String ReversalCode2 = "";
			String FeeAmount = "";
			String CurrencyCode = "";
			String CustBalance = "";
			String InterchangeBalance = null;
			String ATMBalance = null;
			String BranchCode = null;
			String InterchangeAccountNo = null;
			String AcquirerID = null;
			String AuthCode = null;
			String ReserveField5 = null;
			String ReserveField1 = null;
			String ProcessingCode = null;
			String TxnsValueDateTime = null;
			String TxnsDateTime = null;
			String ReserveField3 = null;
			String ReserveField4 = null;
			String TxnsNumber = null;
			String CustAccountNo = null;
			String TerminalID = null;
			String TxnsPostDateTime = null;
			String TxnsDate = null;
			String TxnsTime = null;
			String ReferenceNumber = null;
			String CardNumber = null;
			String TxnsAmount = null;
			String TxnsSubType = null;
			String ReserveField2 = null;
			String ResponseCode1 = null;
			String ReversalCode1 = null;
			String ChannelType = null;
			String DrCrType = null;
			String TxnsPerticulars = null;
			String ResultCode = null;
			String TCode = null;
			String TCode1 = null;
			String CardType = null;
			File convFile = new File(ej.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(ej.getBytes());
			fos.close();
			content = Files.readAllLines(convFile.toPath());
			int a = -1, b = -1;
			boolean flag = false;
			int startIndex1 = 0, endIndex1 = 0, startIndex = 0, endIndex = 0, incr = 0, batchSize = 30000;

			String ErrorCode = null, TransSeqNo = null, Opcode = null, FunctionId = null, ResponseCodeRaw = null,
					Denomination = null, RemainCount = null, RejectCount = null, Cassette1 = null, Cassette2 = null,
					Cassette3 = null, Cassette4 = null, DispenseCount = null;
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
							TerminalID = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("REF NO") || contentData.contains("RRN NO")) {
							ReferenceNumber = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("DATE     :")) {
							TxnsDate = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("TIME     :")) {
							TxnsTime = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("CARD NO")) {
							CardNumber = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("A/C NO") || contentData.contains("ACCOUNT NO")) {
							CustAccountNo = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("TRANSTYPE")) {
							TxnsSubType = contentData.substring(contentData.indexOf(":") + 1);

						}
						if (contentData.contains("RESP CODE")) {
							ResponseCodeRaw = contentData.substring(contentData.indexOf(":") + 1);

						}

						if (contentData.contains("RESP CODE")) {

							String error = content.get(k + 1).toString();
							if (!error.contains("TRANS AMOUNT") && TxnsSubType == "CASH WITHDRAWAL") {
								ErrorCode = error;
							}
						}

						if (contentData.contains("TRANS AMOUNT") || (contentData.contains("WDL AMT"))) {
							String amountstr = contentData.substring(contentData.indexOf(":") + 1).replace("RS.", "")
									.trim();
							TxnsAmount = amountstr;

						}

						if (contentData.contains("TRANS SEQ NUMBER")) {
							TransSeqNo = contentData.substring(contentData.indexOf("[")).trim();

						}

						if (contentData.contains("OPCode") || (contentData.contains("OPCODE"))) {
							Opcode = contentData.substring(contentData.indexOf("=") + 2).trim();

						}

						if (contentData.contains("Function ID") || contentData.contains("FUNCTION ID")) {
							FunctionId = contentData.substring(contentData.indexOf("[")).trim();

						}

						if (contentData.contains("DENOMINATION")) {
							Denomination = contentData.substring(contentData.indexOf(" ")).trim();
						}

						if (contentData.contains("DISPENSED")) {
							String[] Dis = contentData.split(" ");
							DispenseCount = Dis[1] + "," + Dis[2] + "," + Dis[3] + "," + Dis[4];
							DispenseCount = contentData.substring(contentData.indexOf(" ")).trim();
						}

						if (contentData.contains("REMAINING")) {
							String[] Rem = contentData.split(" ");
							RemainCount = Rem[1] + "," + Rem[2] + "," + Rem[3] + "," + Rem[4];
						}

						if (contentData.contains("REJECTED")) {
							String[] Rem = contentData.split(" ");
							RejectCount = Rem[1] + "," + Rem[2] + "," + Rem[3] + "," + Rem[4];
						}

						if (contentData.contains("NOTES PRESENTED")) {
							String[] Note = contentData.split(" ");
							String[] Notes = Note[4].split(",");

							Cassette1 = Notes[0];
							Cassette2 = Notes[1];
							Cassette3 = Notes[2];
							Cassette4 = Notes[3];
						}

					}
					i = b;
					a = -1;
					b = -1;

					Boolean card = false;
					Boolean Terminal = false;
					Boolean Acquirer = false;
					Boolean Rev1 = false;
					Boolean Rev2 = false;
					Boolean ATM = false;
					Boolean CDM = false;
					Boolean POS = false;
					Boolean ECOM = false;
					Boolean IMPS = false;
					Boolean UPI = false;
					Boolean MicroATM = false;
					Boolean MobileRecharge = false;
					Boolean BAL = false;
					Boolean MS = false;
					Boolean PC = false;
					Boolean CB = false;
					Boolean RCA1 = false;
					Boolean RCA2 = false;
					Boolean MC = false;
					Boolean VC = false;
					Boolean OC = false;
					Boolean D = false;
					Boolean C = false;
					Boolean RevFlag = false;
					String ModeID = null;
					String TxnsSubTypeMain = null;
					String TxnsType = null;
					String TxnsEntryType = null;
					String DebitCreditType = null;
					String TxnsDateTimeMain = null, TxnsPostDateTimeMain = null;
					String ChannelID = null;
					String ResponseCode = null;
					String TxnsStatus = null;
					JSONObject cbsxmlFormatDescription = cbsIdentificationfileformatxml.get(0);

					String txnDateTimeFormat = null;
					if (cbsxmlFormatDescription.get("TxnDateTime") == null) {

					} else {
						txnDateTimeFormat = cbsxmlFormatDescription.get("TxnDateTime").toString();
					}
					String txnPostDateTimeFormat = null;

					if (cbsxmlFormatDescription.get("TxnPostDateTime") == null) {

					} else {
						txnPostDateTimeFormat = cbsxmlFormatDescription.get("TxnPostDateTime").toString();
					}
					String terminalCode = null;

					if (cbsxmlFormatDescription.get("TerminalCode") == null) {

					} else {
						terminalCode = cbsxmlFormatDescription.get("TerminalCode").toString();
					}
					String acqID = null;

					if (cbsxmlFormatDescription.get("AcquirerID") == null) {

					} else {
						acqID = cbsxmlFormatDescription.get("AcquirerID").toString();
					}
					String binNum = null;

					if (cbsxmlFormatDescription.get("BIN_No") == null) {

					} else {
						binNum = cbsxmlFormatDescription.get("BIN_No").toString();
					}
					String reversaltype = null;

					if (cbsxmlFormatDescription.get("ReversalType") == null) {

					} else {
						reversaltype = cbsxmlFormatDescription.get("ReversalType").toString();
					}
					String reversalcode1 = null;

					if (cbsxmlFormatDescription.get("ReversalCode1") == null) {

					} else {
						reversalcode1 = cbsxmlFormatDescription.get("ReversalCode1").toString();
					}
					String reversalcode2 = null;

					if (cbsxmlFormatDescription.get("ReversalCode2") == null) {

					} else {
						reversalcode2 = cbsxmlFormatDescription.get("ReversalCode2").toString();
					}
					String CDMType = null;

					if (cbsxmlFormatDescription.get("CDMType") == null) {

					} else {
						CDMType = cbsxmlFormatDescription.get("CDMType").toString();
					}
					String atmType = null;

					if (cbsxmlFormatDescription.get("ATMType") == null) {

					} else {
						atmType = cbsxmlFormatDescription.get("ATMType").toString();
					}
					String microAtmType = null;

					if (cbsxmlFormatDescription.get("MicroATMType") == null) {

					} else {
						microAtmType = cbsxmlFormatDescription.get("MicroATMType").toString();
					}
					String posType = null;

					if (cbsxmlFormatDescription.get("POSType") == null) {

					} else {
						posType = cbsxmlFormatDescription.get("POSType").toString();
					}
					String ecomType = null;

					if (cbsxmlFormatDescription.get("ECOMType") == null) {

					} else {
						ecomType = cbsxmlFormatDescription.get("ECOMType").toString();
					}
					String impType = null;

					if (cbsxmlFormatDescription.get("IMPType") == null) {

					} else {
						impType = cbsxmlFormatDescription.get("IMPType").toString();
					}
					String upiType = null;

					if (cbsxmlFormatDescription.get("UPIType") == null) {

					} else {
						upiType = cbsxmlFormatDescription.get("UPIType").toString();
					}

					String mobileRecharge = null;

					if (cbsxmlFormatDescription.get("MobileRechargeType") == null) {

					} else {
						mobileRecharge = cbsxmlFormatDescription.get("MobileRechargeType").toString();
					}
					String balanceInq = null;

					if (cbsxmlFormatDescription.get("BalanceEnquiry") == null) {

					} else {
						balanceInq = cbsxmlFormatDescription.get("BalanceEnquiry").toString();
					}
					String miniStatement = null;

					if (cbsxmlFormatDescription.get("MiniStatement") == null) {

					} else {
						miniStatement = cbsxmlFormatDescription.get("MiniStatement").toString();
					}
					String pinChange = null;

					if (cbsxmlFormatDescription.get("PinChange") == null) {

					} else {
						pinChange = cbsxmlFormatDescription.get("PinChange").toString();
					}
					String checkBookReq = null;

					if (cbsxmlFormatDescription.get("ChequeBookReq") == null) {

					} else {
						checkBookReq = cbsxmlFormatDescription.get("ChequeBookReq").toString();
					}
					String responseType = null;

					if (cbsxmlFormatDescription.get("ResponseType") == null) {

					} else {
						responseType = cbsxmlFormatDescription.get("ResponseType").toString();
					}
					String responsecode1 = null;

					if (cbsxmlFormatDescription.get("ResponseCode1") == null) {

					} else {
						responsecode1 = cbsxmlFormatDescription.get("ResponseCode1").toString();
					}
					String responsecode2 = null;

					if (cbsxmlFormatDescription.get("ResponseCode2") == null) {

					} else {
						responsecode2 = cbsxmlFormatDescription.get("ResponseCode2").toString();
					}
					String debitCode = null;

					if (cbsxmlFormatDescription.get("DebitCode") == null) {

					} else {
						debitCode = cbsxmlFormatDescription.get("DebitCode").toString();
					}
					String creditCode = null;

					if (cbsxmlFormatDescription.get("CreditCode") == null) {

					} else {
						creditCode = cbsxmlFormatDescription.get("CreditCode").toString();
					}

//					System.out.println("txnDateTimeFormat:  " + txnDateTimeFormat);
					// creditCode
//					System.out.println("txnPostDateTimeFormat:  " + txnPostDateTimeFormat);
					//
//					System.out.println("terminalCode:  " + terminalCode);
					//
//					System.out.println("acqID:  " + acqID);
					//
//					System.out.println("binNum:  " + binNum);
//					;
					System.out.println("reversaltype:  " + reversaltype);

					try {
						if (TxnsDate != null && TxnsTime != null) {
							if (txnDateTimeFormat.isEmpty() || txnDateTimeFormat == null) {

							} else {
								String concatTxnDateTime = TxnsDate + TxnsTime;
								TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, concatTxnDateTime);
								System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
							}
						}
					} catch (ParseException p) {

					}
					if (txnDateTimeFormat != null && TxnsDateTime != null) {

						TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, TxnsDateTime);
						System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
					}

					if (txnPostDateTimeFormat != null && TxnsPostDateTime != null) {

						TxnsPostDateTimeMain = checkDateFormat(txnPostDateTimeFormat, TxnsPostDateTime);
					}

					if (terminalCode != null && TerminalID != null) {
						if (TerminalID.contains(terminalCode)) {
							Terminal = true;
							System.out.println("Terminal:  " + Terminal);
						}
					}

					if (acqID != null && AcquirerID != null) {
						if (acqID.equals(AcquirerID)) {
							Acquirer = true;
							System.out.println("Acquirer:  " + Acquirer);
						}
					}

					if (binNum != null && CardNumber != null) {
						if (binNum.equals(CardNumber.substring(0, 6))) {
							card = true;
							System.out.println("card:  " + card);
						}
					}

					if (reversaltype != null) {
						if (reversaltype.equals("1") && reversalcode1 != null && ReversalCode1 != null) {
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
						}

						if (reversaltype.equals("2") && reversalcode1 != null && reversalcode2 != null
								&& ReversalCode1 != null) {
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
							if (reversalcode2.equals(ReversalCode2)) {
								Rev2 = true;
							}
						}
					}

					if (ChannelType == null && TxnsSubType != null) {
						if (CDMType != null) {
							if (CDMType.equals(TxnsSubType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(TxnsSubType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(TxnsSubType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(TxnsSubType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(TxnsSubType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(TxnsSubType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == TxnsSubType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == TxnsSubType) {
								MobileRecharge = true;
							}
						}
					} else {
						if (CDMType != null) {
							if (CDMType.equals(ChannelType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(ChannelType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(ChannelType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(ChannelType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(ChannelType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(ChannelType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == ChannelType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == ChannelType) {
								MobileRecharge = true;
							}
						}

						if (Integer.parseInt(clientid) == 12) {
							if (Terminal == true && card == true) {
								ATM = true;
							} else if (Terminal == true && card == false) {
								ATM = true;
							} else if (Terminal == false && card == true) {
								ATM = true;
							}
						}
					}
					if (balanceInq != null) {
						if (balanceInq.equals(TxnsSubType)) {
							BAL = true;
						}
					}
					if (miniStatement != null) {
						if (miniStatement.equals(TxnsSubType)) {
							MS = true;
						}
					}
					if (pinChange != null) {
						if (pinChange.equals(TxnsSubType)) {
							PC = true;
						}
					}
					if (checkBookReq != null) {
						if (checkBookReq.equals(TxnsSubType)) {
							CB = true;
						}
					}
					if (responseType.equals("1") && responsecode1 != null && ResponseCode1 != null) {
						if (responsecode1.equals(ResponseCode1)) {
							RCA1 = true;
						}
					}
					if (responseType.equals("2") && responsecode1 != null && responsecode2 != null
							&& ResponseCode2 != null) {
						if (responsecode1.equals(ResponseCode1)) {
							RCA1 = true;
						}
						if (responsecode2.equals(ResponseCode2)) {
							RCA2 = true;
						}
					}
					if (ResponseCode1 == null) {
						RCA1 = true;
					}

					if (debitCode != null) {
						if (debitCode.equals(DrCrType)) {
							D = true;
						}
					}

					if (creditCode != null) {
						if (creditCode.equals(DrCrType)) {
							C = true;
						}
					}
					if (AcquirerID == null || acqID == null) {
						if (Terminal == true && card == true) {
							ModeID = "1";
						}
						if (Terminal == true && card == false) {
							ModeID = "2";
						}
						if (Terminal == false && card == true) {
							ModeID = "3";
						}
					} else {
						if (Acquirer == true && card == true) {
							ModeID = "1";
						}
						if (Acquirer == true && card == false) {
							ModeID = "2";
						}
						if (Acquirer == false && card == true) {
							ModeID = "3";
						}
					}

					if (Rev1 == true || Rev1 == true && Rev2 == true) {
						RevFlag = true;
					} else {
						RevFlag = false;
					}

					if (ATM) {
						TxnsSubTypeMain = "Withdrawal";
						ChannelID = "1";
					}
					if (CDM) {
						TxnsSubTypeMain = "Deposit";
						ChannelID = "1";
					}
					if (POS) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (ECOM) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (IMPS) {
						ChannelID = "4";
						TxnsSubTypeMain = "Transfer";
					}
					if (MicroATM) {
						ChannelID = "5";
						TxnsSubTypeMain = "Withdrawal";
					}
					if (MobileRecharge) {
						ChannelID = "6";
						TxnsSubTypeMain = "Transfer";
					}
					if (UPI) {
						ChannelID = "7";
						TxnsSubTypeMain = "Transfer";
					}
					if (RCA1 == true || RCA1 == true && RCA2 == true) {
						ResponseCode = "00";
						TxnsStatus = "Sucessfull";
					} else {
						ResponseCode = ResponseCode1;
						TxnsStatus = "Unsucessfull";
					}

					if (BAL) {
						TxnsSubTypeMain = "Balance enquiry";
					}

					if (MS) {
						TxnsSubTypeMain = "Mini statement";
					}

					if (PC) {
						TxnsSubTypeMain = "Pin change";
					}

					if (CB) {
						TxnsSubTypeMain = "Cheque book request";
					}
					if (BAL || MS || PC || CB) {
						TxnsType = "Non-Financial";
					} else {
						TxnsType = "Financial";
					}
					if (OC) {
						TxnsEntryType = "Manual";
					} else {
						TxnsEntryType = "Auto";
					}
					if (D) {
						DebitCreditType = "D";
					}

					if (C) {
						DebitCreditType = "C";
					}

					String E_CardNumber = null;
					if (CardNumber != "") {
						E_CardNumber = CardNumber;
					}
					stmt.setString(1, clientid);
					stmt.setString(2, ChannelID);
					stmt.setString(3, ModeID);
					stmt.setString(4, TerminalID);
					stmt.setString(5, ReferenceNumber);
					stmt.setString(6, E_CardNumber);
					stmt.setString(7, CardType);
					stmt.setString(8, CustAccountNo);
					stmt.setString(9, InterchangeAccountNo);
					stmt.setString(10, ATMAccountNo);
					stmt.setString(11, TxnsDateTimeMain);
					stmt.setString(12, TxnsAmount);
					stmt.setString(13, Amount1);
					stmt.setString(14, Amount2);
					stmt.setString(15, Amount3);
					stmt.setString(16, TxnsStatus);
					stmt.setString(17, TxnsType);
					stmt.setString(18, TxnsSubTypeMain);
					stmt.setString(19, TxnsEntryType);
					stmt.setString(20, TxnsNumber);
					stmt.setString(21, TxnsPerticulars);
					stmt.setString(22, DebitCreditType);
					stmt.setString(23, ResponseCode);
					stmt.setString(24, RevFlag.toString());
					stmt.setString(25, TxnsPostDateTimeMain);
					stmt.setString(26, TxnsValueDateTime);
					stmt.setString(27, AuthCode);
					stmt.setString(28, ProcessingCode);
					stmt.setString(29, FeeAmount);
					stmt.setString(30, CurrencyCode);
					stmt.setString(31, CustBalance);
					stmt.setString(32, InterchangeBalance);
					stmt.setString(33, ATMBalance);
					stmt.setString(34, BranchCode);
					stmt.setString(35, TransSeqNo);
					stmt.setString(36, Opcode);
					stmt.setString(37, ResultCode);
					stmt.setString(38, ErrorCode);
					stmt.setString(39, TCode);
					stmt.setString(40, TCode1);
					stmt.setString(41, FunctionId);
					stmt.setString(42, Amount);
					stmt.setString(43, Denomination);
					stmt.setString(44, ReqCount);
					stmt.setString(45, DispenseCount);
					stmt.setString(46, RemainCount);
					stmt.setString(47, PickupCount);
					stmt.setString(48, RejectCount);
					stmt.setString(49, Cassette1);
					stmt.setString(50, Cassette2);
					stmt.setString(51, Cassette3);
					stmt.setString(52, Cassette4);
					stmt.setString(53, ReserveField1);
					stmt.setString(54, ReserveField2);
					stmt.setString(55, ReserveField3);
					stmt.setString(56, ReserveField4);
					stmt.setString(57, ReserveField5);
					stmt.setString(58, null);
					stmt.setString(59, "0");
					stmt.setString(60, ej.getOriginalFilename());
					stmt.setString(61, null);
					stmt.setString(62, null);
					stmt.setString(63, null);
					stmt.setString(64, null);
					stmt.setString(65, null);
					stmt.setString(66, createdby);
					stmt.setString(67, null);
					stmt.addBatch();
					incr++;
					System.out.println("add batch");
					System.out.println(incr + "       " + content.size());
//					if (incr % batchSize == 0 || incr==content.size()) {

					stmt.executeBatch();
					System.out.println("exec batch");

//					}

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
	public List<JSONObject> importGlcbsFileData(MultipartFile glCbs, String clientid, String createdby,
			String fileTypeName) {

		try {
			String extFile = FilenameUtils.getExtension(glCbs.getOriginalFilename());
			Connection con = datasource.getConnection();
			CallableStatement stmt = con.prepareCall(
					"{call spbulkinsertcbsdatadbbl(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			Map<String, Integer> hm = new HashMap<String, Integer>();
			org.json.JSONObject jsonObj = new org.json.JSONObject();
			List<JSONObject> cbsfileformatxml = getcbsswitchformatfileinxml(clientid, fileTypeName, "." + extFile);
			List<JSONObject> cbsIdentificationfileformatxml = getcbsswitchejIdentificationfileformatxml(clientid,
					fileTypeName, "." + extFile);
			JSONObject xmlFormatDescription = cbsfileformatxml.get(0);
			String tempStr = xmlFormatDescription.get("FormatDescriptionXml").toString();
			System.out.println("tempStr:" + tempStr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(tempStr)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			System.out.println("nodelistLength" + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				String nodeName = nodeList.item(i).getNodeName();
				Node startPosNode = nodeList.item(i);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String nodeValue = startPosNodeValue.item(0).getNodeValue();
//				System.out.println("nodeName  " + nodeName + " " + "nodeValue " + nodeValue);
//				hm.put(NodeName, nodeValue);
				jsonObj.append(nodeName, nodeValue.toString());
			}
//			System.out.println("Json:" + jsonObj.toString());
//			System.out.println("Terminal : " + jsonObj.getJSONArray("TerminalID").getString(0));
			String ext = FilenameUtils.getExtension(glCbs.getOriginalFilename());
			XSSFWorkbook wb = null;
			HSSFWorkbook wb1 = null;
			if (ext.equalsIgnoreCase("csv")) {
				String sepratorType = xmlFormatDescription.get("sepratorType").toString();
				File csvglCbs = csvToexcel(glCbs, sepratorType);
				FileInputStream input = new FileInputStream(csvglCbs);
				wb = new XSSFWorkbook(input);
			} else if (ext.equalsIgnoreCase("xlsx")) {
				wb = new XSSFWorkbook(glCbs.getInputStream());
			} else if (ext.equalsIgnoreCase("xls")) {
				wb1 = new HSSFWorkbook(glCbs.getInputStream());
			}

			if (ext.equalsIgnoreCase("csv") || ext.equalsIgnoreCase("xlsx")) {
				XSSFSheet sheet = wb.getSheetAt(0);
				FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
				Row row1 = sheet.getRow(0);
				int tempcolindex = -1;
//			String tempstr = null;

				String ATMAccountNo = "";
				String Amount1 = "";
				String Amount2 = "";
				String Amount3 = "";
				String ResponseCode2 = "";
				String ReversalCode2 = "";
				String FeeAmount = "";
				String CurrencyCode = "";
				String CustBalance = "";
				String InterchangeBalance = null;
				String ATMBalance = null;
				String BranchCode = null;
				String InterchangeAccountNo = null;
				String AcquirerID = null;
				String AuthCode = null;
				String ReserveField5 = null;
				String ReserveField1 = null;
				String ProcessingCode = null;
				String TxnsValueDateTime = null;
				String TxnsDateTime = null;
				String ReserveField3 = null;
				String ReserveField4 = null;
				String TxnsNumber = null;
				String CustAccountNo = null;
				String TerminalID = null;
				String TxnsPostDateTime = null;
				String TxnsDate = null;
				String TxnsTime = null;
				String ReferenceNumber = null;
				String CardNumber = null;
				String TxnsAmount = null;
				String TxnsSubType = null;
				String ReserveField2 = null;
				String ResponseCode1 = null;
				String ReversalCode1 = null;
				String ChannelType = null;
				String DrCrType = null;
				String TxnsPerticulars = null;
				Iterator<Row> itr = sheet.iterator();
				XSSFRow temprow = null;
				int incr = 0, batchSize = 30000;
				long start = System.currentTimeMillis();
				while (itr.hasNext()) {

					Row row = itr.next();
					temprow = sheet.getRow(row.getRowNum());
					if (row.getRowNum() < 1) {
						continue;
					} else {
						if (jsonObj.getJSONArray("ATMAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1) == null) {
								ATMAccountNo = null;
							} else {
								ATMAccountNo = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1) == null) {
								Amount2 = null;
							} else {
								Amount2 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount3").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1) == null) {
								Amount3 = null;
							} else {
								Amount3 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ResponseCode2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1) == null) {
								ResponseCode2 = null;
							} else {
								ResponseCode2 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReversalCode2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1) == null) {
								ReversalCode2 = null;
							} else {
								ReversalCode2 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("FeeAmount").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1) == null) {
								FeeAmount = null;
							} else {
								FeeAmount = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CurrencyCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1) == null) {
								CurrencyCode = null;
							} else {
								CurrencyCode = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CustBalance").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1) == null) {
								CustBalance = null;
							} else {
								CustBalance = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("InterchangeBalance").getString(0).equals("0")) {
						} else {
							if (temprow
									.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0))
											- 1) == null) {
								InterchangeBalance = null;
							} else {
								InterchangeBalance = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ATMBalance").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1) == null) {
								ATMBalance = null;
							} else {
								ATMBalance = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("BranchCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1) == null) {
								BranchCode = null;
							} else {
								BranchCode = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("InterchangeAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow
									.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0))
											- 1) == null) {
								InterchangeAccountNo = null;
							} else {
								InterchangeAccountNo = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("AcquirerID").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1) == null) {
								AcquirerID = null;
							} else {
								AcquirerID = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("AuthCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1) == null) {
								AuthCode = null;
							} else {
								AuthCode = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1) == null) {
								Amount1 = null;
							} else {
								Amount1 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField5").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1) == null) {
								ReserveField5 = null;
							} else {
								ReserveField5 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1) == null) {
								ReserveField1 = null;
							} else {
								ReserveField1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ProcessingCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0))
									- 1) == null) {
								ProcessingCode = null;
							} else {
								ProcessingCode = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsValueDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0))
									- 1) == null) {
								TxnsValueDateTime = null;
							} else {
								TxnsValueDateTime = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1) == null) {
								TxnsDateTime = null;
							} else {
								TxnsDateTime = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1)
										.toString();
								TxnsDateTime = TxnsDateTime.replace("AM", "").replace("PM", "");
							}

						}
						if (jsonObj.getJSONArray("ReserveField3").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1) == null) {
								ReserveField3 = null;
							} else {
								ReserveField3 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField4").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1) == null) {
								ReserveField4 = null;
							} else {
								ReserveField4 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1) == null) {
								TxnsNumber = null;
							} else {
								TxnsNumber = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CustAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1) == null) {
								CustAccountNo = null;
							} else {
								CustAccountNo = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TerminalID").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1) == null) {
								TerminalID = null;
							} else {
								TerminalID = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1)
										.toString();
								if (TerminalID.length() < 8) {
									String concatStr = "00000000" + TerminalID;
									TerminalID = concatStr.substring(concatStr.length() - 8);
								}
							}
						}
						if (jsonObj.getJSONArray("TxnsPostDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0))
									- 1) == null) {
								TxnsPostDateTime = null;
							} else {
								TxnsPostDateTime = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0)) - 1)
										.toString();
								TxnsPostDateTime = TxnsPostDateTime.replace("AM", "").replace("PM", "");
							}

						}
						if (jsonObj.getJSONArray("TxnsDate").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1) == null) {
								TxnsDate = null;
							} else {
								TxnsDate = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1)
										.toString();
								System.out.println("TxnsDate1" + TxnsDate);

								if (TxnsDate.length() <= 11) {
									DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
									Date date = formatter.parse(TxnsDate);
									SimpleDateFormat sdfmt1 = new SimpleDateFormat("ddMMyyyy");
									TxnsDate = sdfmt1.format(date);
								}
								System.out.println("TxnsDate2" + TxnsDate);
							}
						}
						if (jsonObj.getJSONArray("TxnsTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1) == null) {
								TxnsTime = null;
							} else {
								TxnsTime = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReferenceNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0))
									- 1) == null) {
								ReferenceNumber = null;
							} else {
								ReferenceNumber = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0)) - 1)
										.toString();
							}

							if (ReferenceNumber.length() < 6) {
							} else {
								String concatStr = "000000" + ReferenceNumber;
								ReferenceNumber = concatStr.substring(concatStr.length() - 6);
							}

						}
						if (jsonObj.getJSONArray("CardNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1) == null) {
								CardNumber = null;
							} else {
								CardNumber = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsAmount").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1) == null) {
								TxnsAmount = null;
							} else {
								TxnsAmount = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsSubType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1) == null) {
								TxnsSubType = null;
							} else {
								TxnsSubType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField2").getString(0)) - 1) == null) {
								ReserveField2 = null;
							} else {
								ReserveField2 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ResponseCode1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1) == null) {
								ResponseCode1 = null;
							} else {
								ResponseCode1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReversalCode1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1) == null) {
								ReversalCode1 = null;
							} else {
								ReversalCode1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ChannelType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1) == null) {
								ChannelType = null;
							} else {
								ChannelType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("DrCrType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1) == null) {
								DrCrType = null;
							} else {
								DrCrType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsPerticulars").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0))
									- 1) == null) {
								TxnsPerticulars = null;
							} else {
								TxnsPerticulars = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0)) - 1)
										.toString();
							}
						}

						System.out.println("TxnsSubType   :" + TxnsSubType);
//					System.out.println("TxnsPerticulars   :" + TxnsPerticulars);
//					System.out.println("TxnsDateTime   :" + TxnsDateTime);
//					System.out.println("ReferenceNumber   :" + ReferenceNumber);
					}
					Boolean card = false;
					Boolean Terminal = false;
					Boolean Acquirer = false;
					Boolean Rev1 = false;
					Boolean Rev2 = false;
					Boolean ATM = false;
					Boolean CDM = false;
					Boolean POS = false;
					Boolean ECOM = false;
					Boolean IMPS = false;
					Boolean UPI = false;
					Boolean MicroATM = false;
					Boolean MobileRecharge = false;
					Boolean BAL = false;
					Boolean MS = false;
					Boolean PC = false;
					Boolean CB = false;
					Boolean RCA1 = false;
					Boolean RCA2 = false;
					Boolean MC = false;
					Boolean VC = false;
					Boolean OC = false;
					Boolean D = false;
					Boolean C = false;
					Boolean RevFlag = false;
					String ModeID = null;
					String TxnsSubTypeMain = null;
					String TxnsType = null;
					String TxnsEntryType = null;
					String DebitCreditType = null;
					String TxnsDateTimeMain = null, TxnsPostDateTimeMain = null;
					String ChannelID = null;
					String ResponseCode = null;
					String TxnsStatus = null;
					JSONObject cbsxmlFormatDescription = cbsIdentificationfileformatxml.get(0);
					System.out.println("cbsxmlFormatDescription:    " + cbsxmlFormatDescription.toJSONString());
					String txnDateTimeFormat = null;
					if (cbsxmlFormatDescription.get("TxnDateTime") == null) {

					} else {
						txnDateTimeFormat = cbsxmlFormatDescription.get("TxnDateTime").toString();
					}
					String txnPostDateTimeFormat = null;

					if (cbsxmlFormatDescription.get("TxnPostDateTime") == null) {

					} else {
						txnPostDateTimeFormat = cbsxmlFormatDescription.get("TxnPostDateTime").toString();
					}
					String terminalCode = null;

					if (cbsxmlFormatDescription.get("TerminalCode") == null) {

					} else {
						terminalCode = cbsxmlFormatDescription.get("TerminalCode").toString();
					}
					String acqID = null;

					if (cbsxmlFormatDescription.get("AcquirerID") == null) {

					} else {
						acqID = cbsxmlFormatDescription.get("AcquirerID").toString();
					}
					String binNum = null;

					if (cbsxmlFormatDescription.get("BIN_No") == null) {

					} else {
						binNum = cbsxmlFormatDescription.get("BIN_No").toString();
					}
					String reversaltype = null;

					if (cbsxmlFormatDescription.get("ReversalType") == null) {

					} else {
						reversaltype = cbsxmlFormatDescription.get("ReversalType").toString();
					}
					String reversalcode1 = null;

					if (cbsxmlFormatDescription.get("ReversalCode1") == null) {

					} else {
						reversalcode1 = cbsxmlFormatDescription.get("ReversalCode1").toString();
					}
					String reversalcode2 = null;

					if (cbsxmlFormatDescription.get("ReversalCode2") == null) {

					} else {
						reversalcode2 = cbsxmlFormatDescription.get("ReversalCode2").toString();
					}
					String CDMType = null;

					if (cbsxmlFormatDescription.get("CDMType") == null) {

					} else {
						CDMType = cbsxmlFormatDescription.get("CDMType").toString();
					}
					String atmType = null;

					if (cbsxmlFormatDescription.get("ATMType") == null) {

					} else {
						atmType = cbsxmlFormatDescription.get("ATMType").toString();
					}
					String microAtmType = null;

					if (cbsxmlFormatDescription.get("MicroATMType") == null) {

					} else {
						microAtmType = cbsxmlFormatDescription.get("MicroATMType").toString();
					}
					String posType = null;

					if (cbsxmlFormatDescription.get("POSType") == null) {

					} else {
						posType = cbsxmlFormatDescription.get("POSType").toString();
					}
					String ecomType = null;

					if (cbsxmlFormatDescription.get("ECOMType") == null) {

					} else {
						ecomType = cbsxmlFormatDescription.get("ECOMType").toString();
					}
					String impType = null;

					if (cbsxmlFormatDescription.get("IMPType") == null) {

					} else {
						impType = cbsxmlFormatDescription.get("IMPType").toString();
					}
					String upiType = null;

					if (cbsxmlFormatDescription.get("UPIType") == null) {

					} else {
						upiType = cbsxmlFormatDescription.get("UPIType").toString();
					}

					String mobileRecharge = null;

					if (cbsxmlFormatDescription.get("MobileRechargeType") == null) {

					} else {
						mobileRecharge = cbsxmlFormatDescription.get("MobileRechargeType").toString();
					}
					String balanceInq = null;

					if (cbsxmlFormatDescription.get("BalanceEnquiry") == null) {

					} else {
						balanceInq = cbsxmlFormatDescription.get("BalanceEnquiry").toString();
					}
					String miniStatement = null;

					if (cbsxmlFormatDescription.get("MiniStatement") == null) {

					} else {
						miniStatement = cbsxmlFormatDescription.get("MiniStatement").toString();
					}
					String pinChange = null;

					if (cbsxmlFormatDescription.get("PinChange") == null) {

					} else {
						pinChange = cbsxmlFormatDescription.get("PinChange").toString();
					}
					String checkBookReq = null;

					if (cbsxmlFormatDescription.get("ChequeBookReq") == null) {

					} else {
						checkBookReq = cbsxmlFormatDescription.get("ChequeBookReq").toString();
					}
					String responseType = null;

					if (cbsxmlFormatDescription.get("ResponseType") == null) {

					} else {
						responseType = cbsxmlFormatDescription.get("ResponseType").toString();
					}
					String responsecode1 = null;

					if (cbsxmlFormatDescription.get("ResponseCode1") == null) {

					} else {
						responsecode1 = cbsxmlFormatDescription.get("ResponseCode1").toString();
					}
					String responsecode2 = null;

					if (cbsxmlFormatDescription.get("ResponseCode2") == null) {

					} else {
						responsecode2 = cbsxmlFormatDescription.get("ResponseCode2").toString();
					}
					String debitCode = null;

					if (cbsxmlFormatDescription.get("DebitCode") == null) {

					} else {
						debitCode = cbsxmlFormatDescription.get("DebitCode").toString();
					}
					String creditCode = null;

					if (cbsxmlFormatDescription.get("CreditCode") == null) {

					} else {
						creditCode = cbsxmlFormatDescription.get("CreditCode").toString();
					}

//				System.out.println("txnDateTimeFormat:  " + txnDateTimeFormat);
//creditCode
//				System.out.println("txnPostDateTimeFormat:  " + txnPostDateTimeFormat);
//
//				System.out.println("terminalCode:  " + terminalCode);
//
//				System.out.println("acqID:  " + acqID);
//
//				System.out.println("binNum:  " + binNum);
//				;
					System.out.println("reversaltype:  " + reversaltype);

					if (TxnsDate != null && TxnsTime != null) {
						if (txnDateTimeFormat.isEmpty() || txnDateTimeFormat == null) {

						} else {
							String concatTxnDateTime = TxnsDate + TxnsTime;
							TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, concatTxnDateTime);
							System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
						}
					}
					if (txnDateTimeFormat != null && TxnsDateTime != null) {

						TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, TxnsDateTime);
						System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);

					}

					if (txnPostDateTimeFormat != null && TxnsPostDateTime != null) {

						TxnsPostDateTimeMain = checkDateFormat(txnPostDateTimeFormat, TxnsPostDateTime);
					}

					if (terminalCode != null && TerminalID != null) {
						if (TerminalID.contains(terminalCode)) {
							Terminal = true;
							System.out.println("Terminal:  " + Terminal);
						}
					}

					if (acqID != null && AcquirerID != null) {
						if (acqID.equals(AcquirerID)) {
							Acquirer = true;
							System.out.println("Acquirer:  " + Acquirer);
						}
					}

					if (binNum != null && CardNumber != null) {
						if (binNum.equals(CardNumber.substring(0, 6))) {
							card = true;
							System.out.println("card:  " + card);
						}
					}
					if (reversaltype != null) {
						if (reversaltype.equals("1") && reversalcode1 != null && ReversalCode1 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							int responsecode1Int = Integer.parseInt(responsecode1);
							responsecode1 = String.valueOf(responsecode1Int);

							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
						}
						if (reversaltype.equals("2") && reversalcode1 != null && reversalcode2 != null
								&& ReversalCode1 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							int responsecode1Int = Integer.parseInt(responsecode1);
							responsecode1 = String.valueOf(responsecode1Int);

							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);

							responsecode2 = responsecode2.replaceAll("\\p{Punct}", "");
							int responsecode2Int = Integer.parseInt(responsecode2);
							responsecode2 = String.valueOf(responsecode2Int);

							ResponseCode2 = ResponseCode2.replaceAll("\\p{Punct}", "");
							int ResponseCode2Int = Integer.parseInt(ResponseCode2);
							ResponseCode2 = String.valueOf(ResponseCode2);
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
							if (reversalcode2.equals(ReversalCode2)) {
								Rev2 = true;
							}
						}
					}

					if (ChannelType == null && TxnsSubType != null) {
						if (CDMType != null) {
							if (CDMType.equals(TxnsSubType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(TxnsSubType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(TxnsSubType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(TxnsSubType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(TxnsSubType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(TxnsSubType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == TxnsSubType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == TxnsSubType) {
								MobileRecharge = true;
							}
						}
					} else {
						if (CDMType != null) {
							if (CDMType.equals(ChannelType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(ChannelType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(ChannelType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(ChannelType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(ChannelType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(ChannelType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == ChannelType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == ChannelType) {
								MobileRecharge = true;
							}
						}

						if (Integer.parseInt(clientid) == 12) {
							if (Terminal == true && card == true) {
								ATM = true;
							} else if (Terminal == true && card == false) {
								ATM = true;
							} else if (Terminal == false && card == true) {
								ATM = true;
							}
						}
					}
					if (balanceInq != null) {
						if (balanceInq.equals(TxnsSubType)) {
							BAL = true;
						}
					}
					if (miniStatement != null) {
						if (miniStatement.equals(TxnsSubType)) {
							MS = true;
						}
					}
					if (pinChange != null) {
						if (pinChange.equals(TxnsSubType)) {
							PC = true;
						}
					}
					if (checkBookReq != null) {
						if (checkBookReq.equals(TxnsSubType)) {
							CB = true;
						}
					}
					if (responseType != null) {

						if (responseType.equals("1") && responsecode1 != null && ResponseCode1 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							int responsecode1Int = Integer.parseInt(responsecode1);
							responsecode1 = String.valueOf(responsecode1Int);
							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);
							if (responsecode1.equals(ResponseCode1)) {
								RCA1 = true;
							}
						}
						if (responseType.equals("2") && responsecode1 != null && responsecode2 != null
								&& ResponseCode2 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							int responsecode1Int = Integer.parseInt(responsecode1);
							responsecode1 = String.valueOf(responsecode1Int);

							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);

							responsecode2 = responsecode2.replaceAll("\\p{Punct}", "");
							int responsecode2Int = Integer.parseInt(responsecode2);
							responsecode2 = String.valueOf(responsecode2Int);

							ResponseCode2 = ResponseCode2.replaceAll("\\p{Punct}", "");
							int ResponseCode2Int = Integer.parseInt(ResponseCode2);
							ResponseCode2 = String.valueOf(ResponseCode2);

							if (responsecode1.equals(ResponseCode1)) {
								RCA1 = true;
							}
							if (responsecode2.equals(ResponseCode2)) {
								RCA2 = true;
							}
						}
					}
					if (ResponseCode1 == null) {
						RCA1 = true;
					}

					if (debitCode != null) {
						if (debitCode.equals(DrCrType)) {
							D = true;
						}
					}

					if (creditCode != null) {
						if (creditCode.equals(DrCrType)) {
							C = true;
						}
					}
					if (AcquirerID == null || acqID == null) {
						if (Terminal == true && card == true) {
							ModeID = "1";
						}
						if (Terminal == true && card == false) {
							ModeID = "2";
						}
						if (Terminal == false && card == true) {
							ModeID = "3";
						}
					} else {
						if (Acquirer == true && card == true) {
							ModeID = "1";
						}
						if (Acquirer == true && card == false) {
							ModeID = "2";
						}
						if (Acquirer == false && card == true) {
							ModeID = "3";
						}
					}

					if (Rev1 == true || Rev1 == true && Rev2 == true) {
						RevFlag = true;
					} else {
						RevFlag = false;
					}

					if (ATM) {
						TxnsSubTypeMain = "Withdrawal";
						ChannelID = "1";
					}
					if (CDM) {
						TxnsSubTypeMain = "Deposit";
						ChannelID = "1";
					}
					if (POS) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (ECOM) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (IMPS) {
						ChannelID = "4";
						TxnsSubTypeMain = "Transfer";
					}
					if (MicroATM) {
						ChannelID = "5";
						TxnsSubTypeMain = "Withdrawal";
					}
					if (MobileRecharge) {
						ChannelID = "6";
						TxnsSubTypeMain = "Transfer";
					}
					if (UPI) {
						ChannelID = "7";
						TxnsSubTypeMain = "Transfer";
					}
					if (RCA1 == true || RCA1 == true && RCA2 == true) {
						ResponseCode = "00";
						TxnsStatus = "Sucessfull";
					} else {
						ResponseCode = ResponseCode1;
						TxnsStatus = "Unsucessfull";
					}

					if (BAL) {
						TxnsSubTypeMain = "Balance enquiry";
					}

					if (MS) {
						TxnsSubTypeMain = "Mini statement";
					}

					if (PC) {
						TxnsSubTypeMain = "Pin change";
					}

					if (CB) {
						TxnsSubTypeMain = "Cheque book request";
					}
					if (BAL || MS || PC || CB) {
						TxnsType = "Non-Financial";
					} else {
						TxnsType = "Financial";
					}
					if (OC) {
						TxnsEntryType = "Manual";
					} else {
						TxnsEntryType = "Auto";
					}
					if (D) {
						DebitCreditType = "D";
					}

					if (C) {
						DebitCreditType = "C";
					}

					String E_CardNumber = null;
					if (CardNumber != "") {
						E_CardNumber = CardNumber;
					}
					stmt.setString(1, clientid);
					stmt.setString(2, ChannelID);
					stmt.setString(3, ModeID);
					stmt.setString(4, TerminalID);
					stmt.setString(5, ReferenceNumber);
					stmt.setString(6, E_CardNumber);
					stmt.setString(7, null);
					stmt.setString(8, CustAccountNo);
					stmt.setString(9, InterchangeAccountNo);
					stmt.setString(10, ATMAccountNo);
					stmt.setString(11, TxnsDateTimeMain);
					stmt.setString(12, TxnsAmount);
					stmt.setString(13, Amount1);
					stmt.setString(14, Amount2);
					stmt.setString(15, Amount3);
					stmt.setString(16, TxnsStatus);
					stmt.setString(17, TxnsType);
					stmt.setString(18, TxnsSubTypeMain);
					stmt.setString(19, TxnsEntryType);
					stmt.setString(20, TxnsNumber);
					stmt.setString(21, TxnsPerticulars);
					stmt.setString(22, DebitCreditType);
					stmt.setString(23, ResponseCode);
					stmt.setString(24, RevFlag.toString());
					stmt.setString(25, TxnsPostDateTimeMain);
					stmt.setString(26, TxnsValueDateTime);
					stmt.setString(27, AuthCode);
					stmt.setString(28, ProcessingCode);
					stmt.setString(29, FeeAmount);
					stmt.setString(30, CurrencyCode);
					stmt.setString(31, CustBalance);
					stmt.setString(32, InterchangeBalance);
					stmt.setString(33, ATMBalance);
					stmt.setString(34, BranchCode);
					stmt.setString(35, ReserveField1);
					stmt.setString(36, ReserveField2);
					stmt.setString(37, ReserveField3);
					stmt.setString(38, ReserveField4);
					stmt.setString(39, ReserveField5);
					stmt.setString(40, null);
					stmt.setString(41, "0");
					stmt.setString(42, glCbs.getOriginalFilename());
					stmt.setString(43, null);
					stmt.setString(44, null);
					stmt.setString(45, null);
					stmt.setString(46, null);
					stmt.setString(47, createdby);
					stmt.setString(48, null);
					stmt.setString(49, null);
					stmt.addBatch();
					incr++;
					System.out.println("incr:" + incr + "    " + "ROWS == " + sheet.getPhysicalNumberOfRows());
					if (incr % batchSize == 0 || incr == (sheet.getPhysicalNumberOfRows()) - 1) {
						stmt.executeBatch();
						long end = System.currentTimeMillis();
						System.out.println("TIME:  " + (end - start));
					}

				}
			} else if (ext.equalsIgnoreCase("xls")) {
				HSSFSheet sheet = wb1.getSheetAt(0);
				FormulaEvaluator formulaEvaluator = wb1.getCreationHelper().createFormulaEvaluator();
				Row row1 = sheet.getRow(0);
				int tempcolindex = -1;
//			String tempstr = null;

				String ATMAccountNo = "";
				String Amount1 = "";
				String Amount2 = "";
				String Amount3 = "";
				String ResponseCode2 = "";
				String ReversalCode2 = "";
				String FeeAmount = "";
				String CurrencyCode = "";
				String CustBalance = "";
				String InterchangeBalance = null;
				String ATMBalance = null;
				String BranchCode = null;
				String InterchangeAccountNo = null;
				String AcquirerID = null;
				String AuthCode = null;
				String ReserveField5 = null;
				String ReserveField1 = null;
				String ProcessingCode = null;
				String TxnsValueDateTime = null;
				String TxnsDateTime = null;
				String ReserveField3 = null;
				String ReserveField4 = null;
				String TxnsNumber = null;
				String CustAccountNo = null;
				String TerminalID = null;
				String TxnsPostDateTime = null;
				String TxnsDate = null;
				String TxnsTime = null;
				String ReferenceNumber = null;
				String CardNumber = null;
				String TxnsAmount = null;
				String TxnsSubType = null;
				String ReserveField2 = null;
				String ResponseCode1 = null;
				String ReversalCode1 = null;
				String ChannelType = null;
				String DrCrType = null;
				String TxnsPerticulars = null;
				Iterator<Row> itr = sheet.iterator();
				HSSFRow temprow = null;
				int incr = 0, batchSize = 30000;
				long start = System.currentTimeMillis();
				while (itr.hasNext()) {

					Row row = itr.next();
					temprow = sheet.getRow(row.getRowNum());
					if (row.getRowNum() < 1) {
						continue;
					} else {
						if (jsonObj.getJSONArray("ATMAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1) == null) {
								ATMAccountNo = null;
							} else {
								ATMAccountNo = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1) == null) {
								Amount2 = null;
							} else {
								Amount2 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount3").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1) == null) {
								Amount3 = null;
							} else {
								Amount3 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ResponseCode2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1) == null) {
								ResponseCode2 = null;
							} else {
								ResponseCode2 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReversalCode2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1) == null) {
								ReversalCode2 = null;
							} else {
								ReversalCode2 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("FeeAmount").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1) == null) {
								FeeAmount = null;
							} else {
								FeeAmount = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CurrencyCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1) == null) {
								CurrencyCode = null;
							} else {
								CurrencyCode = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CustBalance").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1) == null) {
								CustBalance = null;
							} else {
								CustBalance = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("InterchangeBalance").getString(0).equals("0")) {
						} else {
							if (temprow
									.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0))
											- 1) == null) {
								InterchangeBalance = null;
							} else {
								InterchangeBalance = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ATMBalance").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1) == null) {
								ATMBalance = null;
							} else {
								ATMBalance = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("BranchCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1) == null) {
								BranchCode = null;
							} else {
								BranchCode = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("InterchangeAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow
									.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0))
											- 1) == null) {
								InterchangeAccountNo = null;
							} else {
								InterchangeAccountNo = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("AcquirerID").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1) == null) {
								AcquirerID = null;
							} else {
								AcquirerID = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("AuthCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1) == null) {
								AuthCode = null;
							} else {
								AuthCode = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("Amount1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1) == null) {
								Amount1 = null;
							} else {
								Amount1 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField5").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1) == null) {
								ReserveField5 = null;
							} else {
								ReserveField5 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1) == null) {
								ReserveField1 = null;
							} else {
								ReserveField1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ProcessingCode").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0))
									- 1) == null) {
								ProcessingCode = null;
							} else {
								ProcessingCode = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsValueDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0))
									- 1) == null) {
								TxnsValueDateTime = null;
							} else {
								TxnsValueDateTime = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1) == null) {
								TxnsDateTime = null;
							} else {
								TxnsDateTime = row
										.getCell(
												Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1)
										.toString();
								TxnsDateTime = TxnsDateTime.replace("AM", "").replace("PM", "");
							}

						}
						if (jsonObj.getJSONArray("ReserveField3").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1) == null) {
								ReserveField3 = null;
							} else {
								ReserveField3 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField4").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1) == null) {
								ReserveField4 = null;
							} else {
								ReserveField4 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1) == null) {
								TxnsNumber = null;
							} else {
								TxnsNumber = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("CustAccountNo").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1) == null) {
								CustAccountNo = null;
							} else {
								CustAccountNo = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TerminalID").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1) == null) {
								TerminalID = null;
							} else {
								TerminalID = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1)
										.toString();
								if (TerminalID.length() < 8) {
									String concatStr = "00000000" + TerminalID;
									TerminalID = concatStr.substring(concatStr.length() - 8);
								}
							}
						}
						if (jsonObj.getJSONArray("TxnsPostDateTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0))
									- 1) == null) {
								TxnsPostDateTime = null;
							} else {
								TxnsPostDateTime = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0)) - 1)
										.toString();
								TxnsPostDateTime = TxnsPostDateTime.replace("AM", "").replace("PM", "");
							}

						}
						if (jsonObj.getJSONArray("TxnsDate").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1) == null) {
								TxnsDate = null;
							} else {
								TxnsDate = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1)
										.toString();
								System.out.println("TxnsDate1" + TxnsDate);
							}
						}
						if (jsonObj.getJSONArray("TxnsTime").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1) == null) {
								TxnsTime = null;
							} else {
								TxnsTime = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReferenceNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0))
									- 1) == null) {
								ReferenceNumber = null;
							} else {
								ReferenceNumber = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0)) - 1)
										.toString();
							}

							if (ReferenceNumber.length() < 6) {
							} else {
								String concatStr = "000000" + ReferenceNumber;
								ReferenceNumber = concatStr.substring(concatStr.length() - 6);
							}

						}
						if (jsonObj.getJSONArray("CardNumber").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1) == null) {
								CardNumber = null;
							} else {
								CardNumber = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsAmount").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1) == null) {
								TxnsAmount = null;
							} else {
								TxnsAmount = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsSubType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1) == null) {
								TxnsSubType = null;
							} else {
								TxnsSubType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReserveField2").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReserveField2").getString(0)) - 1) == null) {
								ReserveField2 = null;
							} else {
								ReserveField2 = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ResponseCode1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1) == null) {
								ResponseCode1 = null;
							} else {
								ResponseCode1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ReversalCode1").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1) == null) {
								ReversalCode1 = null;
							} else {
								ReversalCode1 = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("ChannelType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1) == null) {
								ChannelType = null;
							} else {
								ChannelType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("DrCrType").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(
									Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1) == null) {
								DrCrType = null;
							} else {
								DrCrType = row
										.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1)
										.toString();
							}
						}
						if (jsonObj.getJSONArray("TxnsPerticulars").getString(0).equals("0")) {
						} else {
							if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0))
									- 1) == null) {
								TxnsPerticulars = null;
							} else {
								TxnsPerticulars = row.getCell(
										Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0)) - 1)
										.toString();
							}
						}

						System.out.println("TxnsSubType   :" + TxnsSubType);
//					System.out.println("TxnsPerticulars   :" + TxnsPerticulars);
//					System.out.println("TxnsDateTime   :" + TxnsDateTime);
//					System.out.println("ReferenceNumber   :" + ReferenceNumber);
					}
					Boolean card = false;
					Boolean Terminal = false;
					Boolean Acquirer = false;
					Boolean Rev1 = false;
					Boolean Rev2 = false;
					Boolean ATM = false;
					Boolean CDM = false;
					Boolean POS = false;
					Boolean ECOM = false;
					Boolean IMPS = false;
					Boolean UPI = false;
					Boolean MicroATM = false;
					Boolean MobileRecharge = false;
					Boolean BAL = false;
					Boolean MS = false;
					Boolean PC = false;
					Boolean CB = false;
					Boolean RCA1 = false;
					Boolean RCA2 = false;
					Boolean MC = false;
					Boolean VC = false;
					Boolean OC = false;
					Boolean D = false;
					Boolean C = false;
					Boolean RevFlag = false;
					String ModeID = null;
					String TxnsSubTypeMain = null;
					String TxnsType = null;
					String TxnsEntryType = null;
					String DebitCreditType = null;
					String TxnsDateTimeMain = null, TxnsPostDateTimeMain = null;
					String ChannelID = null;
					String ResponseCode = null;
					String TxnsStatus = null;
					JSONObject cbsxmlFormatDescription = cbsIdentificationfileformatxml.get(0);

					String txnDateTimeFormat = null;
					if (cbsxmlFormatDescription.get("TxnDateTime") == null) {

					} else {
						txnDateTimeFormat = cbsxmlFormatDescription.get("TxnDateTime").toString();
					}
					String txnPostDateTimeFormat = null;

					if (cbsxmlFormatDescription.get("TxnPostDateTime") == null) {

					} else {
						txnPostDateTimeFormat = cbsxmlFormatDescription.get("TxnPostDateTime").toString();
					}
					String terminalCode = null;

					if (cbsxmlFormatDescription.get("TerminalCode") == null) {

					} else {
						terminalCode = cbsxmlFormatDescription.get("TerminalCode").toString();
					}
					String acqID = null;

					if (cbsxmlFormatDescription.get("AcquirerID") == null) {

					} else {
						acqID = cbsxmlFormatDescription.get("AcquirerID").toString();
					}
					String binNum = null;

					if (cbsxmlFormatDescription.get("BIN_No") == null) {

					} else {
						binNum = cbsxmlFormatDescription.get("BIN_No").toString();
					}
					String reversaltype = null;

					if (cbsxmlFormatDescription.get("ReversalType") == null) {

					} else {
						reversaltype = cbsxmlFormatDescription.get("ReversalType").toString();
					}
					String reversalcode1 = null;

					if (cbsxmlFormatDescription.get("ReversalCode1") == null) {

					} else {
						reversalcode1 = cbsxmlFormatDescription.get("ReversalCode1").toString();
					}
					String reversalcode2 = null;

					if (cbsxmlFormatDescription.get("ReversalCode2") == null) {

					} else {
						reversalcode2 = cbsxmlFormatDescription.get("ReversalCode2").toString();
					}
					String CDMType = null;

					if (cbsxmlFormatDescription.get("CDMType") == null) {

					} else {
						CDMType = cbsxmlFormatDescription.get("CDMType").toString();
					}
					String atmType = null;

					if (cbsxmlFormatDescription.get("ATMType") == null) {

					} else {
						atmType = cbsxmlFormatDescription.get("ATMType").toString();
					}
					String microAtmType = null;

					if (cbsxmlFormatDescription.get("MicroATMType") == null) {

					} else {
						microAtmType = cbsxmlFormatDescription.get("MicroATMType").toString();
					}
					String posType = null;

					if (cbsxmlFormatDescription.get("POSType") == null) {

					} else {
						posType = cbsxmlFormatDescription.get("POSType").toString();
					}
					String ecomType = null;

					if (cbsxmlFormatDescription.get("ECOMType") == null) {

					} else {
						ecomType = cbsxmlFormatDescription.get("ECOMType").toString();
					}
					String impType = null;

					if (cbsxmlFormatDescription.get("IMPType") == null) {

					} else {
						impType = cbsxmlFormatDescription.get("IMPType").toString();
					}
					String upiType = null;

					if (cbsxmlFormatDescription.get("UPIType") == null) {

					} else {
						upiType = cbsxmlFormatDescription.get("UPIType").toString();
					}

					String mobileRecharge = null;

					if (cbsxmlFormatDescription.get("MobileRechargeType") == null) {

					} else {
						mobileRecharge = cbsxmlFormatDescription.get("MobileRechargeType").toString();
					}
					String balanceInq = null;

					if (cbsxmlFormatDescription.get("BalanceEnquiry") == null) {

					} else {
						balanceInq = cbsxmlFormatDescription.get("BalanceEnquiry").toString();
					}
					String miniStatement = null;

					if (cbsxmlFormatDescription.get("MiniStatement") == null) {

					} else {
						miniStatement = cbsxmlFormatDescription.get("MiniStatement").toString();
					}
					String pinChange = null;

					if (cbsxmlFormatDescription.get("PinChange") == null) {

					} else {
						pinChange = cbsxmlFormatDescription.get("PinChange").toString();
					}
					String checkBookReq = null;

					if (cbsxmlFormatDescription.get("ChequeBookReq") == null) {

					} else {
						checkBookReq = cbsxmlFormatDescription.get("ChequeBookReq").toString();
					}
					String responseType = null;

					if (cbsxmlFormatDescription.get("ResponseType") == null) {

					} else {
						responseType = cbsxmlFormatDescription.get("ResponseType").toString();
					}
					String responsecode1 = null;

					if (cbsxmlFormatDescription.get("ResponseCode1") == null) {

					} else {
						responsecode1 = cbsxmlFormatDescription.get("ResponseCode1").toString();
					}
					String responsecode2 = null;

					if (cbsxmlFormatDescription.get("ResponseCode2") == null) {

					} else {
						responsecode2 = cbsxmlFormatDescription.get("ResponseCode2").toString();
					}
					String debitCode = null;

					if (cbsxmlFormatDescription.get("DebitCode") == null) {

					} else {
						debitCode = cbsxmlFormatDescription.get("DebitCode").toString();
					}
					String creditCode = null;

					if (cbsxmlFormatDescription.get("CreditCode") == null) {

					} else {
						creditCode = cbsxmlFormatDescription.get("CreditCode").toString();
					}

//				System.out.println("txnDateTimeFormat:  " + txnDateTimeFormat);
//creditCode
//				System.out.println("txnPostDateTimeFormat:  " + txnPostDateTimeFormat);
//
//				System.out.println("terminalCode:  " + terminalCode);
//
//				System.out.println("acqID:  " + acqID);
//
//				System.out.println("binNum:  " + binNum);
//				;
					System.out.println("reversaltype:  " + reversaltype);

					if (TxnsDate != null && TxnsTime != null) {
						if (txnDateTimeFormat.isEmpty() || txnDateTimeFormat == null) {

						} else {
							String concatTxnDateTime = TxnsDate + " " + TxnsTime;
							TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, concatTxnDateTime);
							System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
						}
					}
					if (txnDateTimeFormat != null && TxnsDateTime != null) {

						TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, TxnsDateTime);
						System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);

					}

					if (txnPostDateTimeFormat != null && TxnsPostDateTime != null) {

						TxnsPostDateTimeMain = checkDateFormat(txnPostDateTimeFormat, TxnsPostDateTime);
					}

					if (terminalCode != null && TerminalID != null) {
						if (TerminalID.contains(terminalCode)) {
							Terminal = true;
							System.out.println("Terminal:  " + Terminal);
						}
					}

					if (acqID != null && AcquirerID != null) {
						if (acqID.equals(AcquirerID)) {
							Acquirer = true;
							System.out.println("Acquirer:  " + Acquirer);
						}
					}

					if (binNum != null && CardNumber != null) {
						if (binNum.equals(CardNumber.substring(0, 6))) {
							card = true;
							System.out.println("card:  " + card);
						}
					}
					if (reversaltype != null) {
						if (reversaltype.equals("1") && reversalcode1 != null && ReversalCode1 != null) {
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
						}
						if (reversaltype.equals("2") && reversalcode1 != null && reversalcode2 != null
								&& ReversalCode1 != null) {
							if (reversalcode1.equals(ReversalCode1)) {
								Rev1 = true;
							}
							if (reversalcode2.equals(ReversalCode2)) {
								Rev2 = true;
							}
						}
					}

					if (ChannelType == null && TxnsSubType != null) {
						if (CDMType != null) {
							if (CDMType.equals(TxnsSubType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(TxnsSubType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(TxnsSubType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(TxnsSubType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(TxnsSubType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(TxnsSubType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == TxnsSubType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == TxnsSubType) {
								MobileRecharge = true;
							}
						}
					} else {
						if (CDMType != null) {
							if (CDMType.equals(ChannelType)) {
								CDM = true;
							}
						}
						if (atmType != null) {
							if (atmType.equals(ChannelType) && TerminalID.substring(0, 2) != microAtmType) {
								ATM = true;
							}
						}
						if (posType != null) {
							if (posType.equals(ChannelType)) {
								POS = true;
							}
						}
						if (ecomType != null) {
							if (ecomType.equals(ChannelType)) {
								ECOM = true;
							}
						}
						if (impType != null) {
							if (impType.equals(ChannelType)) {
								IMPS = true;
							}
						}
						if (upiType != null) {
							if (upiType.equals(ChannelType)) {
								UPI = true;
							}
						}
						if (microAtmType != null) {
							if (microAtmType == ChannelType || TerminalID.substring(0, 2).equals(microAtmType)) {
								MicroATM = true;
							}
						}
						if (mobileRecharge != null) {
							if (mobileRecharge == ChannelType) {
								MobileRecharge = true;
							}
						}

						if (Integer.parseInt(clientid) == 12) {
							if (Terminal == true && card == true) {
								ATM = true;
							} else if (Terminal == true && card == false) {
								ATM = true;
							} else if (Terminal == false && card == true) {
								ATM = true;
							}
						}
					}
					if (balanceInq != null) {
						if (balanceInq.equals(TxnsSubType)) {
							BAL = true;
						}
					}
					if (miniStatement != null) {
						if (miniStatement.equals(TxnsSubType)) {
							MS = true;
						}
					}
					if (pinChange != null) {
						if (pinChange.equals(TxnsSubType)) {
							PC = true;
						}
					}
					if (checkBookReq != null) {
						if (checkBookReq.equals(TxnsSubType)) {
							CB = true;
						}
					}
					if (responseType != null) {

						if (responseType.equals("1") && responsecode1 != null && ResponseCode1 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							if(responsecode1 != null || responsecode1 != "")
							{
								int responsecode1Int = Integer.parseInt(responsecode1);
								responsecode1 = String.valueOf(responsecode1Int);
							}

							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							if(ResponseCode1 != null || ResponseCode1 !="") {
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);
							if (responsecode1.equals(ResponseCode1)) {
								RCA1 = true;
							}
							}
						}
						if (responseType.equals("2") && responsecode1 != null && responsecode2 != null
								&& ResponseCode2 != null) {
							responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
							int responsecode1Int = Integer.parseInt(responsecode1);
							responsecode1 = String.valueOf(responsecode1Int);

							ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);

							responsecode2 = responsecode2.replaceAll("\\p{Punct}", "");
							int responsecode2Int = Integer.parseInt(responsecode2);
							responsecode2 = String.valueOf(responsecode2Int);

							ResponseCode2 = ResponseCode2.replaceAll("\\p{Punct}", "");
							int ResponseCode2Int = Integer.parseInt(ResponseCode2);
							ResponseCode2 = String.valueOf(ResponseCode2);
							if (responsecode1.equals(ResponseCode1)) {
								RCA1 = true;
							}
							if (responsecode2.equals(ResponseCode2)) {
								RCA2 = true;
							}
						}
					}
					if (ResponseCode1 == null) {
						RCA1 = true;
					}

					if (debitCode != null) {
						if (debitCode.equals(DrCrType)) {
							D = true;
						}
					}

					if (creditCode != null) {
						if (creditCode.equals(DrCrType)) {
							C = true;
						}
					}
					if (AcquirerID == null || acqID == null) {
						if (Terminal == true && card == true) {
							ModeID = "1";
						}
						if (Terminal == true && card == false) {
							ModeID = "2";
						}
						if (Terminal == false && card == true) {
							ModeID = "3";
						}
					} else {
						if (Acquirer == true && card == true) {
							ModeID = "1";
						}
						if (Acquirer == true && card == false) {
							ModeID = "2";
						}
						if (Acquirer == false && card == true) {
							ModeID = "3";
						}
					}

					if (Rev1 == true || Rev1 == true && Rev2 == true) {
						RevFlag = true;
					} else {
						RevFlag = false;
					}

					if (ATM) {
						TxnsSubTypeMain = "Withdrawal";
						ChannelID = "1";
					}
					if (CDM) {
						TxnsSubTypeMain = "Deposit";
						ChannelID = "1";
					}
					if (POS) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (ECOM) {
						ChannelID = "2";
						TxnsSubTypeMain = "Purchase";
					}
					if (IMPS) {
						ChannelID = "4";
						TxnsSubTypeMain = "Transfer";
					}
					if (MicroATM) {
						ChannelID = "5";
						TxnsSubTypeMain = "Withdrawal";
					}
					if (MobileRecharge) {
						ChannelID = "6";
						TxnsSubTypeMain = "Transfer";
					}
					if (UPI) {
						ChannelID = "7";
						TxnsSubTypeMain = "Transfer";
					}
					if (RCA1 == true || RCA1 == true && RCA2 == true) {
						ResponseCode = "00";
						TxnsStatus = "Sucessfull";
					} else {
						ResponseCode = ResponseCode1;
						TxnsStatus = "Unsucessfull";
					}

					if (BAL) {
						TxnsSubTypeMain = "Balance enquiry";
					}

					if (MS) {
						TxnsSubTypeMain = "Mini statement";
					}

					if (PC) {
						TxnsSubTypeMain = "Pin change";
					}

					if (CB) {
						TxnsSubTypeMain = "Cheque book request";
					}
					if (BAL || MS || PC || CB) {
						TxnsType = "Non-Financial";
					} else {
						TxnsType = "Financial";
					}
					if (OC) {
						TxnsEntryType = "Manual";
					} else {
						TxnsEntryType = "Auto";
					}
					if (D) {
						DebitCreditType = "D";
					}

					if (C) {
						DebitCreditType = "C";
					}

					String E_CardNumber = null;
					if (CardNumber != "") {
						E_CardNumber = CardNumber;
					}
					stmt.setString(1, clientid);
					stmt.setString(2, ChannelID);
					stmt.setString(3, ModeID);
					stmt.setString(4, TerminalID);
					stmt.setString(5, ReferenceNumber);
					stmt.setString(6, E_CardNumber);
					stmt.setString(7, null);
					stmt.setString(8, CustAccountNo);
					stmt.setString(9, InterchangeAccountNo);
					stmt.setString(10, ATMAccountNo);
					stmt.setString(11, TxnsDateTimeMain);
					stmt.setString(12, TxnsAmount);
					stmt.setString(13, Amount1);
					stmt.setString(14, Amount2);
					stmt.setString(15, Amount3);
					stmt.setString(16, TxnsStatus);
					stmt.setString(17, TxnsType);
					stmt.setString(18, TxnsSubTypeMain);
					stmt.setString(19, TxnsEntryType);
					stmt.setString(20, TxnsNumber);
					stmt.setString(21, TxnsPerticulars);
					stmt.setString(22, DebitCreditType);
					stmt.setString(23, ResponseCode);
					stmt.setString(24, RevFlag.toString());
					stmt.setString(25, TxnsPostDateTimeMain);
					stmt.setString(26, TxnsValueDateTime);
					stmt.setString(27, AuthCode);
					stmt.setString(28, ProcessingCode);
					stmt.setString(29, FeeAmount);
					stmt.setString(30, CurrencyCode);
					stmt.setString(31, CustBalance);
					stmt.setString(32, InterchangeBalance);
					stmt.setString(33, ATMBalance);
					stmt.setString(34, BranchCode);
					stmt.setString(35, ReserveField1);
					stmt.setString(36, ReserveField2);
					stmt.setString(37, ReserveField3);
					stmt.setString(38, ReserveField4);
					stmt.setString(39, ReserveField5);
					stmt.setString(40, null);
					stmt.setString(41, "0");
					stmt.setString(42, glCbs.getOriginalFilename());
					stmt.setString(43, null);
					stmt.setString(44, null);
					stmt.setString(45, null);
					stmt.setString(46, null);
					stmt.setString(47, createdby);
					stmt.setString(48, null);
					stmt.setString(49, null);
					stmt.addBatch();
					incr++;
					System.out.println("incr:" + incr + "    " + "ROWS == " + sheet.getPhysicalNumberOfRows());
					if (incr % batchSize == 0 || incr == (sheet.getPhysicalNumberOfRows()) - 1) {
						stmt.executeBatch();
						long end = System.currentTimeMillis();
						System.out.println("TIME:  " + (end - start));
					}

				}
			}
			stmt.close();
			con.close();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public List<JSONObject> importSwitchFile(MultipartFile sw, String clientid, String createdby, String fileTypeName) {

		try {
			int count = 0;
			String extFile = FilenameUtils.getExtension(sw.getOriginalFilename());
			int w = 0;
			System.out.println("suyog");
			Connection con = datasource.getConnection();
			CallableStatement stmt = con.prepareCall(
					"{call spinsertswitchdata(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			Map<String, Integer> hm = new HashMap<String, Integer>();
			org.json.JSONObject jsonObj = new org.json.JSONObject();
			List<JSONObject> switchfileformatxml = getcbsswitchformatfileinxml(clientid, fileTypeName, "." + extFile);
			List<JSONObject> cbsIdentificationfileformatxml = getcbsswitchejIdentificationfileformatxml(clientid,
					fileTypeName, "." + extFile);
			JSONObject xmlFormatDescription = switchfileformatxml.get(0);
			String tempStr = xmlFormatDescription.get("FormatDescriptionXml").toString();
			System.out.println("tempStr:" + tempStr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(tempStr)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			System.out.println("nodelistLength" + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				String nodeName = nodeList.item(i).getNodeName();
				Node startPosNode = nodeList.item(i);

				NodeList startPosNodeValue = startPosNode.getChildNodes();
				String nodeValue = startPosNodeValue.item(0).getNodeValue();
				System.out.println("nodeName  " + nodeName + " " + "nodeValue " + nodeValue);
//				hm.put(NodeName, nodeValue);
				jsonObj.append(nodeName, nodeValue.toString());
			}
//			System.out.println("Json:" + jsonObj.toString());
//			System.out.println("Terminal : " + jsonObj.getJSONArray("TerminalID").getString(0));

			HSSFWorkbook wb = new HSSFWorkbook(sw.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
			Row row1 = sheet.getRow(0);
			int tempcolindex = -1;
//			String tempstr = null;

			String ATMAccountNo = "";
			String Amount1 = "";
			String Amount2 = "";
			String Amount3 = "";
			String ResponseCode2 = "";
			String ReversalCode2 = "";
			String FeeAmount = "";
			String CurrencyCode = "";
			String CustBalance = "";
			String InterchangeBalance = null;
			String ATMBalance = null;
			String BranchCode = null;
			String InterchangeAccountNo = null;
			String AcquirerID = null;
			String AuthCode = null;
			String ReserveField5 = null;
			String ReserveField1 = null;
			String ProcessingCode = null;
			String TxnsValueDateTime = null;
			String TxnsDateTime = null;
			String ReserveField3 = null;
			String ReserveField4 = null;
			String TxnsNumber = null;
			String CustAccountNo = null;
			String TerminalID = null;
			String TxnsPostDateTime = null;
			String TxnsDate = null;
			String TxnsTime = null;
			String ReferenceNumber = null;
			String CardNumber = null;
			String TxnsAmount = null;
			String TxnsSubType = null;
			String ReserveField2 = null;
			String ResponseCode1 = null;
			String ReversalCode1 = null;
			String ChannelType = null;
			String DrCrType = null;
			String TxnsPerticulars = null;
			Iterator<Row> itr = sheet.iterator();
			HSSFRow temprow = null;
			int incr = 0, batchSize = 30000;
			long start = System.currentTimeMillis();
			while (itr.hasNext()) {

				Row row = itr.next();
				temprow = sheet.getRow(row.getRowNum());

				if (row.getRowNum() < 3) {
					incr++;
					continue;
				} else {
					if (jsonObj.getJSONArray("ATMAccountNo").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1) == null) {
							ATMAccountNo = null;
						} else {
							ATMAccountNo = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMAccountNo").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("Amount2").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1) == null) {
							Amount2 = null;
						} else {
							Amount2 = row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount2").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("Amount3").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1) == null) {
							Amount3 = null;
						} else {
							Amount3 = row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount3").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ResponseCode2").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1) == null) {
							ResponseCode2 = null;
						} else {
							ResponseCode2 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode2").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReversalCode2").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1) == null) {
							ReversalCode2 = null;
						} else {
							ReversalCode2 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode2").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("FeeAmount").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1) == null) {
							FeeAmount = null;
						} else {
							FeeAmount = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("FeeAmount").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("CurrencyCode").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1) == null) {
							CurrencyCode = null;
						} else {
							CurrencyCode = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("CurrencyCode").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("CustBalance").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1) == null) {
							CustBalance = null;
						} else {
							CustBalance = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("CustBalance").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("InterchangeBalance").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0))
								- 1) == null) {
							InterchangeBalance = null;
						} else {
							InterchangeBalance = row.getCell(
									Integer.parseInt(jsonObj.getJSONArray("InterchangeBalance").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ATMBalance").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1) == null) {
							ATMBalance = null;
						} else {
							ATMBalance = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ATMBalance").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("BranchCode").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1) == null) {
							BranchCode = null;
						} else {
							BranchCode = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("BranchCode").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("InterchangeAccountNo").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0))
								- 1) == null) {
							InterchangeAccountNo = null;
						} else {
							InterchangeAccountNo = row.getCell(
									Integer.parseInt(jsonObj.getJSONArray("InterchangeAccountNo").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("AcquirerID").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1) == null) {
							AcquirerID = null;
						} else {
							AcquirerID = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("AcquirerID").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("AuthCode").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1) == null) {
							AuthCode = null;
						} else {
							AuthCode = row.getCell(Integer.parseInt(jsonObj.getJSONArray("AuthCode").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("Amount1").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1) == null) {
							Amount1 = null;
						} else {
							Amount1 = row.getCell(Integer.parseInt(jsonObj.getJSONArray("Amount1").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReserveField5").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1) == null) {
							ReserveField5 = null;
						} else {
							ReserveField5 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField5").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReserveField1").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1) == null) {
							ReserveField1 = null;
						} else {
							ReserveField1 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField1").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ProcessingCode").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0)) - 1) == null) {
							ProcessingCode = null;
						} else {
							ProcessingCode = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ProcessingCode").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsValueDateTime").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0)) - 1) == null) {
							TxnsValueDateTime = null;
						} else {
							TxnsValueDateTime = row.getCell(
									Integer.parseInt(jsonObj.getJSONArray("TxnsValueDateTime").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsDateTime").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1) == null) {
							TxnsDateTime = null;
						} else {
							TxnsDateTime = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDateTime").getString(0)) - 1)
									.toString();
							TxnsDateTime = TxnsDateTime.replace("AM", "").replace("PM", "");
							System.out.println("TxnsDateTime   " + TxnsDateTime);
						}

					}
					if (jsonObj.getJSONArray("ReserveField3").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1) == null) {
							ReserveField3 = null;
						} else {
							ReserveField3 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField3").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReserveField4").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1) == null) {
							ReserveField4 = null;
						} else {
							ReserveField4 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReserveField4").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsNumber").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1) == null) {
							TxnsNumber = null;
						} else {
							TxnsNumber = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsNumber").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("CustAccountNo").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1) == null) {
							CustAccountNo = null;
						} else {
							CustAccountNo = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("CustAccountNo").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TerminalID").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1) == null) {
							TerminalID = null;
						} else {
							TerminalID = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TerminalID").getString(0)) - 1)
									.toString();
							if (TerminalID.length() < 8) {
								String concatStr = "00000000" + TerminalID;
								TerminalID = concatStr.substring(concatStr.length() - 8);
							}
						}
					}
					if (jsonObj.getJSONArray("TxnsPostDateTime").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0)) - 1) == null) {
							TxnsPostDateTime = null;
						} else {
							TxnsPostDateTime = row
									.getCell(
											Integer.parseInt(jsonObj.getJSONArray("TxnsPostDateTime").getString(0)) - 1)
									.toString();
							TxnsPostDateTime = TxnsPostDateTime.replace("AM", "").replace("PM", "");
						}

					}
					if (jsonObj.getJSONArray("TxnsDate").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1) == null) {
							TxnsDate = null;
						} else {
							TxnsDate = row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsDate").getString(0)) - 1)
									.toString();
							System.out.println("TxnsDate1" + TxnsDate);

							if (TxnsDate.length() <= 11) {
								DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
								Date date = formatter.parse(TxnsDate);
								SimpleDateFormat sdfmt1 = new SimpleDateFormat("ddMMyyyy");
								TxnsDate = sdfmt1.format(date);
							}
							System.out.println("TxnsDate2" + TxnsDate);
						}
					}
					if (jsonObj.getJSONArray("TxnsTime").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1) == null) {
							TxnsTime = null;
						} else {
							TxnsTime = row.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsTime").getString(0)) - 1)
									.toString();
							TxnsTime = TxnsTime.replace("AM", "").replace("PM", "");
						}
					}
					if (jsonObj.getJSONArray("ReferenceNumber").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0)) - 1) == null) {
							ReferenceNumber = null;
						} else {
							ReferenceNumber = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReferenceNumber").getString(0)) - 1)
									.toString();
						}

						if (ReferenceNumber.length() < 6) {
						} else {
							String concatStr = "000000" + ReferenceNumber;
							ReferenceNumber = concatStr.substring(concatStr.length() - 6);
						}

					}
					if (jsonObj.getJSONArray("CardNumber").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1) == null) {
							CardNumber = null;
						} else {
							CardNumber = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("CardNumber").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsAmount").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1) == null) {
							TxnsAmount = null;
						} else {
							TxnsAmount = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsAmount").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsSubType").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1) == null) {
							TxnsSubType = null;
						} else {
							TxnsSubType = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReserveField2").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReserveField2").getString(0)) - 1) == null) {
							ReserveField2 = null;
						} else {
							ReserveField2 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsSubType").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ResponseCode1").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1) == null) {
							ResponseCode1 = null;
						} else {
							ResponseCode1 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ResponseCode1").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ReversalCode1").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1) == null) {
							ReversalCode1 = null;
						} else {
							ReversalCode1 = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ReversalCode1").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("ChannelType").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1) == null) {
							ChannelType = null;
						} else {
							ChannelType = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("ChannelType").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("DrCrType").getString(0).equals("0")) {
					} else {
						if (temprow
								.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1) == null) {
							DrCrType = null;
						} else {
							DrCrType = row.getCell(Integer.parseInt(jsonObj.getJSONArray("DrCrType").getString(0)) - 1)
									.toString();
						}
					}
					if (jsonObj.getJSONArray("TxnsPerticulars").getString(0).equals("0")) {
					} else {
						if (temprow.getCell(
								Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0)) - 1) == null) {
							TxnsPerticulars = null;
						} else {
							TxnsPerticulars = row
									.getCell(Integer.parseInt(jsonObj.getJSONArray("TxnsPerticulars").getString(0)) - 1)
									.toString();
						}
					}

					System.out.println("TxnsSubType   :" + TxnsSubType);
//					System.out.println("TxnsPerticulars   :" + TxnsPerticulars);
//					System.out.println("TxnsDateTime   :" + TxnsDateTime);
//					System.out.println("ReferenceNumber   :" + ReferenceNumber);
				}
				Boolean card = false;
				Boolean Terminal = false;
				Boolean Acquirer = false;
				Boolean Rev1 = false;
				Boolean Rev2 = false;
				Boolean ATM = false;
				Boolean CDM = false;
				Boolean POS = false;
				Boolean ECOM = false;
				Boolean IMPS = false;
				Boolean UPI = false;
				Boolean MicroATM = false;
				Boolean MobileRecharge = false;
				Boolean BAL = false;
				Boolean MS = false;
				Boolean PC = false;
				Boolean CB = false;
				Boolean RCA1 = false;
				Boolean RCA2 = false;
				Boolean MC = false;
				Boolean VC = false;
				Boolean OC = false;
				Boolean D = false;
				Boolean C = false;
				Boolean RevFlag = false;
				String ModeID = null;
				String TxnsSubTypeMain = null;
				String TxnsType = null;
				String TxnsEntryType = null;
				String DebitCreditType = null;
				String TxnsDateTimeMain = null, TxnsPostDateTimeMain = null;
				String ChannelID = null;
				String ResponseCode = null;
				String TxnsStatus = null;
				JSONObject cbsxmlFormatDescription = cbsIdentificationfileformatxml.get(0);

				String txnDateTimeFormat = null;
				if (cbsxmlFormatDescription.get("TxnDateTime") == null) {

				} else {
					txnDateTimeFormat = cbsxmlFormatDescription.get("TxnDateTime").toString();
				}
				String txnPostDateTimeFormat = null;

				if (cbsxmlFormatDescription.get("TxnPostDateTime") == null) {

				} else {
					txnPostDateTimeFormat = cbsxmlFormatDescription.get("TxnPostDateTime").toString();
				}
				String terminalCode = null;

				if (cbsxmlFormatDescription.get("TerminalCode") == null) {

				} else {
					terminalCode = cbsxmlFormatDescription.get("TerminalCode").toString();
				}
				String acqID = null;

				if (cbsxmlFormatDescription.get("AcquirerID") == null) {

				} else {
					acqID = cbsxmlFormatDescription.get("AcquirerID").toString();
				}
				String binNum = null;

				if (cbsxmlFormatDescription.get("BIN_No") == null) {

				} else {
					binNum = cbsxmlFormatDescription.get("BIN_No").toString();
				}
				String reversaltype = null;

				if (cbsxmlFormatDescription.get("ReversalType") == null) {

				} else {
					reversaltype = cbsxmlFormatDescription.get("ReversalType").toString();
				}
				String reversalcode1 = null;

				if (cbsxmlFormatDescription.get("ReversalCode1") == null) {

				} else {
					reversalcode1 = cbsxmlFormatDescription.get("ReversalCode1").toString();
				}
				String reversalcode2 = null;

				if (cbsxmlFormatDescription.get("ReversalCode2") == null) {

				} else {
					reversalcode2 = cbsxmlFormatDescription.get("ReversalCode2").toString();
				}
				String CDMType = null;

				if (cbsxmlFormatDescription.get("CDMType") == null) {

				} else {
					CDMType = cbsxmlFormatDescription.get("CDMType").toString();
				}
				String atmType = null;

				if (cbsxmlFormatDescription.get("ATMType") == null) {

				} else {
					atmType = cbsxmlFormatDescription.get("ATMType").toString();
				}
				String microAtmType = null;

				if (cbsxmlFormatDescription.get("MicroATMType") == null) {

				} else {
					microAtmType = cbsxmlFormatDescription.get("MicroATMType").toString();
				}
				String posType = null;

				if (cbsxmlFormatDescription.get("POSType") == null) {

				} else {
					posType = cbsxmlFormatDescription.get("POSType").toString();
				}
				String ecomType = null;

				if (cbsxmlFormatDescription.get("ECOMType") == null) {

				} else {
					ecomType = cbsxmlFormatDescription.get("ECOMType").toString();
				}
				String impType = null;

				if (cbsxmlFormatDescription.get("IMPType") == null) {

				} else {
					impType = cbsxmlFormatDescription.get("IMPType").toString();
				}
				String upiType = null;

				if (cbsxmlFormatDescription.get("UPIType") == null) {

				} else {
					upiType = cbsxmlFormatDescription.get("UPIType").toString();
				}

				String mobileRecharge = null;

				if (cbsxmlFormatDescription.get("MobileRechargeType") == null) {

				} else {
					mobileRecharge = cbsxmlFormatDescription.get("MobileRechargeType").toString();
				}
				String balanceInq = null;

				if (cbsxmlFormatDescription.get("BalanceEnquiry") == null) {

				} else {
					balanceInq = cbsxmlFormatDescription.get("BalanceEnquiry").toString();
				}
				String miniStatement = null;

				if (cbsxmlFormatDescription.get("MiniStatement") == null) {

				} else {
					miniStatement = cbsxmlFormatDescription.get("MiniStatement").toString();
				}
				String pinChange = null;

				if (cbsxmlFormatDescription.get("PinChange") == null) {

				} else {
					pinChange = cbsxmlFormatDescription.get("PinChange").toString();
				}
				String checkBookReq = null;

				if (cbsxmlFormatDescription.get("ChequeBookReq") == null) {

				} else {
					checkBookReq = cbsxmlFormatDescription.get("ChequeBookReq").toString();
				}
				String responseType = null;

				if (cbsxmlFormatDescription.get("ResponseType") == null) {

				} else {
					responseType = cbsxmlFormatDescription.get("ResponseType").toString();
				}
				String responsecode1 = null;

				if (cbsxmlFormatDescription.get("ResponseCode1") == null) {

				} else {
					responsecode1 = cbsxmlFormatDescription.get("ResponseCode1").toString();
				}
				String responsecode2 = null;

				if (cbsxmlFormatDescription.get("ResponseCode2") == null) {

				} else {
					responsecode2 = cbsxmlFormatDescription.get("ResponseCode2").toString();
				}
				String debitCode = null;

				if (cbsxmlFormatDescription.get("DebitCode") == null) {

				} else {
					debitCode = cbsxmlFormatDescription.get("DebitCode").toString();
				}
				String creditCode = null;

				if (cbsxmlFormatDescription.get("CreditCode") == null) {

				} else {
					creditCode = cbsxmlFormatDescription.get("CreditCode").toString();
				}

//				System.out.println("txnDateTimeFormat:  " + txnDateTimeFormat);
//creditCode
//				System.out.println("txnPostDateTimeFormat:  " + txnPostDateTimeFormat);
//
//				System.out.println("terminalCode:  " + terminalCode);
//
//				System.out.println("acqID:  " + acqID);
//
//				System.out.println("binNum:  " + binNum);
//				;
				System.out.println("reversaltype:  " + reversaltype);

				if (TxnsDate != null && TxnsTime != null) {
					if (txnDateTimeFormat.isEmpty() || txnDateTimeFormat == null) {

					} else {
						String concatTxnDateTime = TxnsDate + " " + TxnsTime;
						TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, concatTxnDateTime);
						System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
					}
				}
				if (txnDateTimeFormat != null && TxnsDateTime != null) {

					TxnsDateTimeMain = checkDateFormat(txnDateTimeFormat, TxnsDateTime);
					System.out.println("TxnsDateTimeMain  " + TxnsDateTimeMain);
				}

				if (txnPostDateTimeFormat != null && TxnsPostDateTime != null) {

					TxnsPostDateTimeMain = checkDateFormat(txnPostDateTimeFormat, TxnsPostDateTime);
				}

				if (terminalCode != null && TerminalID != null) {
					if (TerminalID.contains(terminalCode)) {
						Terminal = true;
						System.out.println("Terminal:  " + Terminal);
					}
				}

				if (acqID != null && AcquirerID != null) {
					if (acqID.equals(AcquirerID)) {
						Acquirer = true;
						System.out.println("Acquirer:  " + Acquirer);
					}
				}

				
				if (binNum != null && CardNumber != null) {
					if (binNum.equals(CardNumber.substring(0, 6))) {
						card = true;
						System.out.println("card:  " + card);
					}
				}
				if (reversaltype != null) {
					if (reversaltype.equals("1") && reversalcode1 != null && ReversalCode1 != null) {
						if (reversalcode1.equals(ReversalCode1)) {
							Rev1 = true;
						}
					}
					if (reversaltype.equals("2") && reversalcode1 != null && reversalcode2 != null
							&& ReversalCode1 != null) {
						if (reversalcode1.equals(ReversalCode1)) {
							Rev1 = true;
						}
						if (reversalcode2.equals(ReversalCode2)) {
							Rev2 = true;
						}
					}
				}

				if (ChannelType == null && TxnsSubType != null) {
					if (CDMType != null) {
						if (CDMType.equals(TxnsSubType)) {
							CDM = true;
						}
					}
					if (atmType != null) {
						if (atmType.equals(TxnsSubType) && TerminalID.substring(0, 2) != microAtmType) {
							ATM = true;
						}
					}
					if (posType != null) {
						if (posType.equals(TxnsSubType)) {
							POS = true;
						}
					}
					if (ecomType != null) {
						if (ecomType.equals(TxnsSubType)) {
							ECOM = true;
						}
					}
					if (impType != null) {
						if (impType.equals(TxnsSubType)) {
							IMPS = true;
						}
					}
					if (upiType != null) {
						if (upiType.equals(TxnsSubType)) {
							UPI = true;
						}
					}
					if (microAtmType != null) {
						if (microAtmType == TxnsSubType || TerminalID.substring(0, 2).equals(microAtmType)) {
							MicroATM = true;
						}
					}
					if (mobileRecharge != null) {
						if (mobileRecharge == TxnsSubType) {
							MobileRecharge = true;
						}
					}
				} else {
					if (CDMType != null) {
						if (CDMType.equals(ChannelType)) {
							CDM = true;
						}
					}
					if (atmType != null) {
						if (atmType.equals(ChannelType) && TerminalID.substring(0, 2) != microAtmType) {
							ATM = true;
						}
					}
					if (posType != null) {
						if (posType.equals(ChannelType)) {
							POS = true;
						}
					}
					if (ecomType != null) {
						if (ecomType.equals(ChannelType)) {
							ECOM = true;
						}
					}
					if (impType != null) {
						if (impType.equals(ChannelType)) {
							IMPS = true;
						}
					}
					if (upiType != null) {
						if (upiType.equals(ChannelType)) {
							UPI = true;
						}
					}
					if (microAtmType != null) {
						if (microAtmType == ChannelType || TerminalID.substring(0, 2).equals(microAtmType)) {
							MicroATM = true;
						}
					}
					if (mobileRecharge != null) {
						if (mobileRecharge == ChannelType) {
							MobileRecharge = true;
						}
					}

					if (Integer.parseInt(clientid) == 12) {
						if (Terminal == true && card == true) {
							ATM = true;
						} else if (Terminal == true && card == false) {
							ATM = true;
						} else if (Terminal == false && card == true) {
							ATM = true;
						}
					}
				}
				if (balanceInq != null) {
					if (balanceInq.equals(TxnsSubType)) {
						BAL = true;
					}
				}
				if (miniStatement != null) {
					if (miniStatement.equals(TxnsSubType)) {
						MS = true;
					}
				}
				if (pinChange != null) {
					if (pinChange.equals(TxnsSubType)) {
						PC = true;
					}
				}
				if (checkBookReq != null) {
					if (checkBookReq.equals(TxnsSubType)) {
						CB = true;
					}
				}
				if (responseType != null) {
					if (responseType.equals("1") && responsecode1 != null && ResponseCode1 != null) {
						responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
						int responsecode1Int = Integer.parseInt(responsecode1);
						responsecode1 = String.valueOf(responsecode1Int);

						ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
						if(ResponseCode1.equalsIgnoreCase(""))
						{
							
						}
						else
						{
							int ResponseCode1Int = Integer.parseInt(ResponseCode1);
							ResponseCode1 = String.valueOf(ResponseCode1Int);
							if (responsecode1.equals(ResponseCode1)) {
								RCA1 = true;
							}
						}
					}
					if (responseType.equals("2") && responsecode1 != null && responsecode2 != null
							&& ResponseCode2 != null) {
						responsecode1 = responsecode1.replaceAll("\\p{Punct}", "");
						
						
						int responsecode1Int = Integer.parseInt(responsecode1);
						responsecode1 = String.valueOf(responsecode1Int);

						ResponseCode1 = ResponseCode1.replaceAll("\\p{Punct}", "");
						
						if(ResponseCode1.equalsIgnoreCase(""))
						{
							
						}
						else {
						int ResponseCode1Int = Integer.parseInt(ResponseCode1);
						ResponseCode1 = String.valueOf(ResponseCode1Int);
						}
						
						responsecode2 = responsecode2.replaceAll("\\p{Punct}", "");
						if(responsecode2.equalsIgnoreCase(""))
						{
							
						}
						else {
						int responsecode2Int = Integer.parseInt(responsecode2);
						responsecode2 = String.valueOf(responsecode2Int);
						}
						
						ResponseCode2 = ResponseCode2.replaceAll("\\p{Punct}", "");
						if(ResponseCode2.equalsIgnoreCase(""))
						{
							
						}
						else {
						int ResponseCode2Int = Integer.parseInt(ResponseCode2);
						ResponseCode2 = String.valueOf(ResponseCode2);
						if (responsecode1.equals(ResponseCode1)) {
							RCA1 = true;
						}
						if (responsecode2.equals(ResponseCode2)) {
							RCA2 = true;
						}
						}
					}
				}
				if (ResponseCode1 == null) {
					RCA1 = true;
				}

				if (debitCode != null) {
					if (debitCode.equals(DrCrType)) {
						D = true;
					}
				}

				if (creditCode != null) {
					if (creditCode.equals(DrCrType)) {
						C = true;
					}
				}
				if (AcquirerID == null || acqID == null) {
					if (Terminal == true && card == true) {
						ModeID = "1";
					}
					if (Terminal == true && card == false) {
						ModeID = "2";
					}
					if (Terminal == false && card == true) {
						ModeID = "3";
					}
				} else {
					if (Acquirer == true && card == true) {
						ModeID = "1";
					}
					if (Acquirer == true && card == false) {
						ModeID = "2";
					}
					if (Acquirer == false && card == true) {
						ModeID = "3";
					}
				}

				if (Rev1 == true || Rev1 == true && Rev2 == true) {
					RevFlag = true;
				} else {
					RevFlag = false;
				}

				if (ATM) {
					TxnsSubTypeMain = "Withdrawal";
					ChannelID = "1";
				}
				if (CDM) {
					TxnsSubTypeMain = "Deposit";
					ChannelID = "1";
				}
				if (POS) {
					ChannelID = "2";
					TxnsSubTypeMain = "Purchase";
				}
				if (ECOM) {
					ChannelID = "2";
					TxnsSubTypeMain = "Purchase";
				}
				if (IMPS) {
					ChannelID = "4";
					TxnsSubTypeMain = "Transfer";
				}
				if (MicroATM) {
					ChannelID = "5";
					TxnsSubTypeMain = "Withdrawal";
				}
				if (MobileRecharge) {
					ChannelID = "6";
					TxnsSubTypeMain = "Transfer";
				}
				if (UPI) {
					ChannelID = "7";
					TxnsSubTypeMain = "Transfer";
				}
				if (RCA1 == true || RCA1 == true && RCA2 == true) {
					ResponseCode = "00";
					TxnsStatus = "Sucessfull";
				} else {
					ResponseCode = ResponseCode1;
					TxnsStatus = "Unsucessfull";
				}

				if (BAL) {
					TxnsSubTypeMain = "Balance enquiry";
				}

				if (MS) {
					TxnsSubTypeMain = "Mini statement";
				}

				if (PC) {
					TxnsSubTypeMain = "Pin change";
				}

				if (CB) {
					TxnsSubTypeMain = "Cheque book request";
				}
				if (BAL || MS || PC || CB) {
					TxnsType = "Non-Financial";
				} else {
					TxnsType = "Financial";
				}
				if (OC) {
					TxnsEntryType = "Manual";
				} else {
					TxnsEntryType = "Auto";
				}
				if (D) {
					DebitCreditType = "D";
				}

				if (C) {
					DebitCreditType = "C";
				}

				String E_CardNumber = null;
				if (CardNumber != "") {
					E_CardNumber = CardNumber;
				}
				count++;
				System.out.println("count: " + count);
				stmt.setString(1, clientid);
				stmt.setString(2, ChannelID);
				stmt.setString(3, ModeID);
				stmt.setString(4, TerminalID);
				stmt.setString(5, ReferenceNumber);
				stmt.setString(6, E_CardNumber);
				stmt.setString(7, null);
				stmt.setString(8, CustAccountNo);
				stmt.setString(9, InterchangeAccountNo);
				stmt.setString(10, ATMAccountNo);
				stmt.setString(11, TxnsDateTimeMain);
				stmt.setString(12, TxnsAmount);
				stmt.setString(13, Amount1);
				stmt.setString(14, Amount2);
				stmt.setString(15, Amount3);
				stmt.setString(16, TxnsStatus);
				stmt.setString(17, TxnsType);
				stmt.setString(18, TxnsSubTypeMain);
				stmt.setString(19, TxnsEntryType);
				stmt.setString(20, TxnsNumber);
				stmt.setString(21, TxnsPerticulars);
				stmt.setString(22, DebitCreditType);
				stmt.setString(23, ResponseCode);
				stmt.setString(24, RevFlag.toString());
				stmt.setString(25, TxnsPostDateTimeMain);
				stmt.setString(26, TxnsValueDateTime);
				stmt.setString(27, AuthCode);
				stmt.setString(28, ProcessingCode);
				stmt.setString(29, FeeAmount);
				stmt.setString(30, CurrencyCode);
				stmt.setString(31, CustBalance);
				stmt.setString(32, InterchangeBalance);
				stmt.setString(33, ATMBalance);
				stmt.setString(34, BranchCode);
				stmt.setString(35, ReserveField1);
				stmt.setString(36, ReserveField2);
				stmt.setString(37, ReserveField3);
				stmt.setString(38, ReserveField4);
				stmt.setString(39, ReserveField5);
				stmt.setString(40, null);
				stmt.setString(41, "0");
				stmt.setString(42, sw.getOriginalFilename());
				stmt.setString(43, null);
				stmt.setString(44, null);
				stmt.setString(45, null);
				stmt.setString(46, null);
				stmt.setString(47, createdby);
				stmt.setString(48, null);
				stmt.setString(49, null);
				stmt.addBatch();
				System.out.println(incr + "        " + sheet.getPhysicalNumberOfRows());
				int diff = sheet.getPhysicalNumberOfRows() - incr;
				if (incr % batchSize == 0 || diff < 10) {

					stmt.executeBatch();
					w++;
					long end = System.currentTimeMillis();
					System.out.println("EXECUTE TIME:  " + (end - start) + "   " + w);
				}
				incr++;
			}
		} catch (Exception e) {

		}
		return null;
	}

	private List<JSONObject> getcbsswitchejIdentificationfileformatxml(String clientid, String fileTypeName,
			String extFile) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPTESTFIELDDATA");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
//		query.setParameter(2, i);
		query.setParameter(2, fileTypeName);
		query.setParameter(3, extFile);
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

//	@Override
//	public List<JSONObject> getchannelmodeinfo(String clientid) {
//
//		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCHANNELMODEINFO");
//		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
//
//		query.setParameter(1, Integer.parseInt(clientid));
//		query.execute();
//
//		List<Object[]> result = query.getResultList();
//		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
//		for (Object record : result) {
//			Object[] fields = (Object[]) record;
//			JSONObject obj = new JSONObject();
//			obj.put("ChannelID", fields[0]);
//			obj.put("ChnnelName", fields[1]);
//			JSONObjects.add(obj);
//		}
//		return JSONObjects;
//	}

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
			String createdby, String p_CHANNELID) {
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
		query.registerStoredProcedureParameter(34, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(35, String.class, ParameterMode.REF_CURSOR);

		query.setParameter(1, Integer.parseInt(p_CLIENTID));
		query.setParameter(2, Integer.parseInt(p_VENDORID));
		query.setParameter(3, Integer.parseInt(p_FORMATID));
		query.setParameter(4, p_TERMINALCODE);
		query.setParameter(5, p_BINNO);
		query.setParameter(6, p_ACQUIRERID);
		query.setParameter(7, p_REVCODE1);
		query.setParameter(8, p_REVCODE2);
		query.setParameter(9, p_REVTYPE);
		query.setParameter(10, p_REVENTRY);
		query.setParameter(11, p_TXNDATETIME);
		query.setParameter(12, p_TXNVALUEDATETIME);
		query.setParameter(13, p_TXNPOSTDATETIME);
		System.out.println("p_TXNPOSTDATETIME:  " + p_TXNPOSTDATETIME);
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
		query.setParameter(34, Integer.parseInt(p_CHANNELID));

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
//			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("formatid", result.get(0));
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

	public List<JSONObject> getcbsswitchformatfileinxml(String clientid, String fileTypeName, String extFile) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETCBSFORMATDESCRIPTIONXML");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
//		query.setParameter(2, i);
		query.setParameter(2, fileTypeName);
		query.setParameter(3, extFile);
		query.execute();
		List<Object[]> result = query.getResultList();
		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("clientid", fields[0]);
			obj.put("sepratorType", fields[1]);
			obj.put("FormatDescriptionXml", fields[2]);
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
		System.out.println("count:  " + count);
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

	@Override
	public List<JSONObject> getbranchname(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETBRANCHCODEPOPMAXDETAILS");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("branchcode", fields[0]);
			obj.put("branchname", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getterminaldetailschannelwise(String clientid, String channelid, String userid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPGETTERMINALDETAILSCHANNELWISE");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.setParameter(2, Integer.parseInt(channelid));
		query.setParameter(3, userid);
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("termid", fields[0]);
			obj.put("terminalID", fields[1]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getchannelmodedetailsremodify(String clientid) {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("spGetChannelModeDetails1");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, Integer.parseInt(clientid));
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
			obj.put("modeID", fields[0]);
			obj.put("modeName", fields[1]);
			obj.put("onus", fields[2]);
			obj.put("acq", fields[3]);
			obj.put("iss", fields[4]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}

	@Override
	public List<JSONObject> getdispensesummaryreport(String clientID, String channelID, String modeID,
			String terminalID, String fromDateTxns, String toDateTxns, String txnType) throws ParseException {
		// TODO Auto-generated method stub
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPDISPENSESUMMARYREPORT");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.REF_CURSOR);
		query.setParameter(1, clientID);
		query.setParameter(2, channelID);
		query.setParameter(3, modeID);
		query.setParameter(4, terminalID);
		query.setParameter(5, fromDateTxns);
		query.setParameter(6, toDateTxns);
		query.setParameter(7, txnType);
		query.execute();
		List<Object[]> result = query.getResultList();

		List<JSONObject> JSONObjects = new ArrayList<JSONObject>(result.size());
		for (Object record : result) {
			Object[] fields = (Object[]) record;
			JSONObject obj = new JSONObject();
//			obj.put("TXNDATE", fields[0]);
			Date d=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fields[0].toString());
			SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
			String tempDate=format1.format(d);
			obj.put("TXNDATE", tempDate);
			obj.put("TERMINALID", fields[1]);
			obj.put("GLONUS", fields[2]);
			obj.put("GLACQUIRER", fields[3]);
			obj.put("GLISSUER", fields[4]);
			obj.put("GLTOTAL", fields[5]);
			obj.put("SWISSUER", fields[6]);
			obj.put("SWITCHTOTAL", fields[7]);
			obj.put("EJTOTAL", fields[8]);
			obj.put("NFSACQUIRER", fields[9]);
			obj.put("NFSISSUER", fields[10]);
			obj.put("NFSACQUIRERDIFF", fields[11]);
			obj.put("NFSISSUERDIFF", fields[12]);
			obj.put("ATMDIFF", fields[13]);
			obj.put("UNSETTLEDAMOUNT", fields[14]);
			JSONObjects.add(obj);
		}
		return JSONObjects;
	}
}
