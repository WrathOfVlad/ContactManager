package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

public class Contact {
	
	public static final String NAME_FIELD = "Name"; 
	public static final String SURNAME_FIELD = "Surname"; 
	public static final String EMAIL1_FIELD = "Email 1"; 
	public static final String EMAIL2_FIELD = "Email 2"; 
	public static final String EMAIL3_FIELD = "Email 3"; 
	public static final String PHONE1_FIELD = "Phone 1"; 
	public static final String PHONE2_FIELD = "Phone 2"; 
	public static final String COMPANY_FIELD = "Company"; 
	public static final String LOCATION_FIELD = "Location"; 
	public static final String ADDRESS_FIELD = "Address"; 
	public static final String ROLE_FIELD = "Role"; 
	public static final String LINKEDIN_FIELD = "LinkedIn"; 
	public static final String FACEBOOK_FIELD = "Facebook"; 
	public static final String COMPANYURL_FIELD = "Company URL"; 
	public static final String SKYPE_FIELD = "Skype"; 
	public static final String CONTACTSTATUS_FIELD = "Contact Status"; 
	public static final String BIRTHDAY_FIELD = "Birthday"; 
	public static final String LASTCONTACT_FIELD = "Last Contact"; 
	public static final String NEXTCONTACT_FIELD = "Next Contact"; 
	public static final String ID_FIELD = "Id"; 
	public static final String FULLNAME_FIELD = "Full Name";
	
	public static final List<String> getColumnNames(boolean includeFullName){
		List<String> columns = new ArrayList<>();
		columns.add(ID_FIELD);
		if(includeFullName) {
			columns.add(FULLNAME_FIELD);
		}
		columns.add(NAME_FIELD);
		columns.add(SURNAME_FIELD);
		columns.add(EMAIL1_FIELD);
		columns.add(EMAIL2_FIELD);
		columns.add(EMAIL3_FIELD);
		columns.add(PHONE1_FIELD);
		columns.add(PHONE2_FIELD);
		columns.add(BIRTHDAY_FIELD);
		columns.add(LINKEDIN_FIELD);
		columns.add(FACEBOOK_FIELD);
		
		columns.add(COMPANY_FIELD);
		columns.add(ROLE_FIELD);
		columns.add(LOCATION_FIELD);
		columns.add(CONTACTSTATUS_FIELD);
		columns.add(LASTCONTACT_FIELD);
		columns.add(NEXTCONTACT_FIELD);
		columns.add(COMPANYURL_FIELD);
		columns.add(SKYPE_FIELD);    
		columns.add(ADDRESS_FIELD);
		return columns;
	}

	private String id;
	private String name;
	private String surname;
	private String email1;
	private String email2;
	private String email3;
	private String phone1;
	private String phone2;
	private String birthday;
	private String linkedIn;
	private String facebook;
	private String company;
	private String role;
	private String location;
	private String contactStatus;
	private String lastContact;
	private String nextContact;
	private String companyURL;
	private String skype;
	private String fullName;
	private String address;
	
	private static List<String> visibleColumns;
	
	public Contact(String[] rowData, List<String> columnNames) {
		if(rowData == null || columnNames == null) {
			return;
		}
		
		setId(rowData[columnNames.indexOf(ID_FIELD)]);;
		setName(rowData[columnNames.indexOf(NAME_FIELD)]);
		setSurname(rowData[columnNames.indexOf(SURNAME_FIELD)]);
		setEmail1(rowData[columnNames.indexOf(EMAIL1_FIELD)]);
		setEmail2(rowData[columnNames.indexOf(EMAIL2_FIELD)]);
		setEmail3(rowData[columnNames.indexOf(EMAIL3_FIELD)]);
		setPhone1(rowData[columnNames.indexOf(PHONE1_FIELD)]);
		setPhone2(rowData[columnNames.indexOf(PHONE2_FIELD)]);
		setBirthday(rowData[columnNames.indexOf(BIRTHDAY_FIELD)]);
		setLinkedIn(rowData[columnNames.indexOf(LINKEDIN_FIELD)]);
		setFacebook(rowData[columnNames.indexOf(FACEBOOK_FIELD)]);
		setCompany(rowData[columnNames.indexOf(COMPANY_FIELD)]);
		setRole(rowData[columnNames.indexOf(ROLE_FIELD)]);
		setLocation(rowData[columnNames.indexOf(LOCATION_FIELD)]);
		//this.contactStatus = rowData[columnNames.indexOf(CONTACTSTATUS_FIELD)];
		setLastContact(rowData[columnNames.indexOf(LASTCONTACT_FIELD)]);
		setNextContact(rowData[columnNames.indexOf(NEXTCONTACT_FIELD)]);
		setCompanyURL(rowData[columnNames.indexOf(COMPANYURL_FIELD)]);
		setSkype(rowData[columnNames.indexOf(SKYPE_FIELD)]);
		setAddress(rowData[columnNames.indexOf(ADDRESS_FIELD)]);
		
		setFullName();
	}
	
	public List<String> getElementsAsList(boolean includeFullName) {
		List<String> listElements = new ArrayList<>();
		listElements.add(id);
		
		if (includeFullName) {
			listElements.add(fullName);
		}
		listElements.add(getName());
		listElements.add(getSurname());
		listElements.add(getEmail1());
		listElements.add(getEmail2());
		listElements.add(getEmail3());
		listElements.add(getPhone1());
		listElements.add(getPhone2());
		listElements.add(getBirthday());
		listElements.add(getLinkedIn());
		listElements.add(getFacebook());
		listElements.add(getCompany());
		listElements.add(getRole());
		listElements.add(getLocation());
		listElements.add(getContactStatus());		
		listElements.add(getLastContact());		
		listElements.add(getNextContact());		
		listElements.add(getCompanyURL());
		listElements.add(getSkype());
		listElements.add(getAddress());
		return listElements;
	
	}

	
	public void loadFromContactDetail(Map<String, JTextField> textFieldMap) {
		setName(textFieldMap.get(NAME_FIELD).getText());
		setSurname(textFieldMap.get(SURNAME_FIELD).getText());
		setEmail1(textFieldMap.get(EMAIL1_FIELD).getText());
		setEmail2(textFieldMap.get(EMAIL2_FIELD).getText());
		setEmail3(textFieldMap.get(EMAIL3_FIELD).getText());
		setPhone1(textFieldMap.get(PHONE1_FIELD).getText());
		setPhone2(textFieldMap.get(PHONE2_FIELD).getText());
		setLinkedIn(textFieldMap.get(LINKEDIN_FIELD).getText());
		setFacebook(textFieldMap.get(FACEBOOK_FIELD).getText());
		setCompany(textFieldMap.get(COMPANY_FIELD).getText());
		setRole(textFieldMap.get(ROLE_FIELD).getText());
		setLocation(textFieldMap.get(LOCATION_FIELD).getText());
		setContactStatus(textFieldMap.get(CONTACTSTATUS_FIELD).getText());
		setAddress(textFieldMap.get(ADDRESS_FIELD).getText());
		setCompanyURL(textFieldMap.get(COMPANYURL_FIELD).getText());
		setSkype(textFieldMap.get(SKYPE_FIELD).getText());
		
		((JFormattedTextField)textFieldMap.get(BIRTHDAY_FIELD)).getValue();
		setBirthday(((JFormattedTextField)textFieldMap.get(BIRTHDAY_FIELD)).getValue());
		setLastContact(((JFormattedTextField)textFieldMap.get(LASTCONTACT_FIELD)).getValue());
		setNextContact(((JFormattedTextField)textFieldMap.get(NEXTCONTACT_FIELD)).getValue());
		
		

		getFullName();
	}
	
	public void setNextContact(Object value) {
		this.nextContact = dateFormatToString(value);
	}

	public void setLastContact(Object value) {
		this.lastContact = dateFormatToString(value);
	}

	public void setBirthday(Object value) {
		this.birthday = dateFormatToString(value);
		
	}
	
	public Object getBirthdayObject() {
		return getDateAsObject(birthday);
	}
	
	public Object getLastContactObject() {
		return getDateAsObject(lastContact);
	}
	public Object getNextContactObject() {
		return getDateAsObject(nextContact);
	}
	
	public Object getDateAsObject(String string) {
		if (string == "") {
			return null;
		}
		else {
			return string;
		}
	}
	

	private String dateFormatToString(Object value) {
		if(value == null) {
			return "";
		}
		else {
			return value.toString();
		}
	}
	
	public void writeToContactDetail(Map<String, JTextField> textFieldMap) {
		textFieldMap.get(NAME_FIELD).setText(getName());
		textFieldMap.get(SURNAME_FIELD).setText(getSurname());
		textFieldMap.get(EMAIL1_FIELD).setText(getEmail1());
		textFieldMap.get(EMAIL2_FIELD).setText(getEmail2());
		textFieldMap.get(EMAIL3_FIELD).setText(getEmail3());
		textFieldMap.get(PHONE1_FIELD).setText(getPhone1());
		textFieldMap.get(PHONE2_FIELD).setText(getPhone2());
		textFieldMap.get(LINKEDIN_FIELD).setText(getLinkedIn());
		textFieldMap.get(FACEBOOK_FIELD).setText(getFacebook());
		textFieldMap.get(COMPANY_FIELD).setText(getCompany());
		textFieldMap.get(ROLE_FIELD).setText(getRole());
		textFieldMap.get(LOCATION_FIELD).setText(getLocation());
		textFieldMap.get(CONTACTSTATUS_FIELD).setText(getContactStatus());
		textFieldMap.get(ADDRESS_FIELD).setText(getAddress());
		textFieldMap.get(COMPANYURL_FIELD).setText(getCompanyURL());
		textFieldMap.get(SKYPE_FIELD).setText(getSkype());
		
		((JFormattedTextField)textFieldMap.get(BIRTHDAY_FIELD)).setValue(getBirthdayObject());
		((JFormattedTextField)textFieldMap.get(LASTCONTACT_FIELD)).setValue(getLastContactObject());
		((JFormattedTextField)textFieldMap.get(NEXTCONTACT_FIELD)).setValue(getNextContactObject());	
	}
	
	public static void setVisibleColumns(List<String> list) {
		visibleColumns = list;
	}
	public static List<String> getVisibleColumns() {
		return visibleColumns;
	}
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getFullName() {
		return fullName;
	}
	public void setFullName() {
		this.fullName = this.name + " " + this.surname;
	}
	
	public int getIdAsInt() {
		return Integer.parseInt(this.id);
	}
	public String getIdAsString() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = String.format("%05d", id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getEmail3() {
		return email3;
	}
	public void setEmail3(String email3) {
		this.email3 = email3;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getLinkedIn() {
		return linkedIn;
	}
	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContactStatus() {
		return contactStatus;
	}
	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}
	public String getLastContact() {
		return lastContact;
	}
	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}
	public String getNextContact() {
		return nextContact;
	}
	public void setNextContact(String nextContact) {
		this.nextContact = nextContact;
	}
	public String getCompanyURL() {
		return companyURL;
	}
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
	}
	public String getSkype() {
		return skype;
	}
	public void setSkype(String skype) {
		this.skype = skype;
	}
	
	
}
