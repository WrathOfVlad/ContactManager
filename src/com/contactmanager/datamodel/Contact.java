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
		
		this.id = rowData[columnNames.indexOf(ID_FIELD)];
		this.name = rowData[columnNames.indexOf(NAME_FIELD)];
		this.surname = rowData[columnNames.indexOf(SURNAME_FIELD)];
		this.email1 = rowData[columnNames.indexOf(EMAIL1_FIELD)];
		this.email2 = rowData[columnNames.indexOf(EMAIL2_FIELD)];
		this.email3 = rowData[columnNames.indexOf(EMAIL3_FIELD)];
		this.phone1 = rowData[columnNames.indexOf(PHONE1_FIELD)];
		this.phone2 = rowData[columnNames.indexOf(PHONE2_FIELD)];
		this.birthday = rowData[columnNames.indexOf(BIRTHDAY_FIELD)];
		this.linkedIn = rowData[columnNames.indexOf(LINKEDIN_FIELD)];
		this.facebook = rowData[columnNames.indexOf(FACEBOOK_FIELD)];
		this.company = rowData[columnNames.indexOf(COMPANY_FIELD)];
		this.role = rowData[columnNames.indexOf(ROLE_FIELD)];
		this.location = rowData[columnNames.indexOf(LOCATION_FIELD)];
		//this.contactStatus = rowData[columnNames.indexOf(CONTACTSTATUS_FIELD)];
		this.lastContact = rowData[columnNames.indexOf(LASTCONTACT_FIELD)];
		this.nextContact = rowData[columnNames.indexOf(NEXTCONTACT_FIELD)];
		this.companyURL = rowData[columnNames.indexOf(COMPANYURL_FIELD)];
		this.skype = rowData[columnNames.indexOf(SKYPE_FIELD)];
		this.address = rowData[columnNames.indexOf(ADDRESS_FIELD)];
		
		setFullName();
	}
	
	public List<String> getElementsAsList(boolean includeFullName) {
		List<String> listElements = new ArrayList<>();
		listElements.add(id);
		
		if (includeFullName) {
			listElements.add(fullName);
		}
		
		listElements.add(name);
		listElements.add(surname);
		listElements.add(email1);
		listElements.add(email2);
		listElements.add(email3);
		listElements.add(phone1);
		listElements.add(phone2);
		listElements.add(birthday);
		listElements.add(linkedIn);
		listElements.add(facebook);
		listElements.add(company);
		listElements.add(role);
		listElements.add(location);
		listElements.add(contactStatus);		
		listElements.add(lastContact);		
		listElements.add(nextContact);		
		listElements.add(companyURL);
		listElements.add(skype);
		listElements.add(address);
		
		
		return listElements;
		
		
	}

	
	public void loadFromContactDetail(Map<String, JTextField> textFieldMap) {
		this.name = textFieldMap.get(NAME_FIELD).getText();
		this.surname = textFieldMap.get(SURNAME_FIELD).getText();
		this.email1 = textFieldMap.get(EMAIL1_FIELD).getText();
		this.email2 = textFieldMap.get(EMAIL2_FIELD).getText();
		this.email3 = textFieldMap.get(EMAIL3_FIELD).getText();
		this.phone1 = textFieldMap.get(PHONE1_FIELD).getText();
		this.phone2 = textFieldMap.get(PHONE2_FIELD).getText();
		this.linkedIn = textFieldMap.get(LINKEDIN_FIELD).getText();
		this.facebook = textFieldMap.get(FACEBOOK_FIELD).getText();
		this.company = textFieldMap.get(COMPANY_FIELD).getText();
		this.role = textFieldMap.get(ROLE_FIELD).getText();
		this.location = textFieldMap.get(LOCATION_FIELD).getText();
		this.contactStatus = textFieldMap.get(CONTACTSTATUS_FIELD).getText();
		this.address = textFieldMap.get(ADDRESS_FIELD).getText();
		this.companyURL = textFieldMap.get(COMPANYURL_FIELD).getText();
		this.skype = textFieldMap.get(SKYPE_FIELD).getText();
		
		this.birthday = textFieldMap.get(BIRTHDAY_FIELD).getText();
		this.lastContact = textFieldMap.get(LASTCONTACT_FIELD).getText();
		this.nextContact = textFieldMap.get(NEXTCONTACT_FIELD).getText();
		
		getFullName();
	}
	
	public void writeToContactDetail(Map<String, JTextField> textFieldMap) {
		textFieldMap.get(NAME_FIELD).setText(this.name);
		textFieldMap.get(SURNAME_FIELD).setText(this.surname);
		textFieldMap.get(EMAIL1_FIELD).setText(this.email1);
		textFieldMap.get(EMAIL2_FIELD).setText(this.email2);
		textFieldMap.get(EMAIL3_FIELD).setText(this.email3);
		textFieldMap.get(PHONE1_FIELD).setText(this.phone1);
		textFieldMap.get(PHONE2_FIELD).setText(this.phone2);
		textFieldMap.get(LINKEDIN_FIELD).setText(this.linkedIn);
		textFieldMap.get(FACEBOOK_FIELD).setText(this.facebook);
		textFieldMap.get(COMPANY_FIELD).setText(this.company);
		textFieldMap.get(ROLE_FIELD).setText(this.role);
		textFieldMap.get(LOCATION_FIELD).setText(this.location);
		textFieldMap.get(CONTACTSTATUS_FIELD).setText(this.contactStatus);
		textFieldMap.get(ADDRESS_FIELD).setText(this.address);
		textFieldMap.get(COMPANYURL_FIELD).setText(this.companyURL);
		textFieldMap.get(SKYPE_FIELD).setText(this.skype);
		
		((JFormattedTextField)textFieldMap.get(BIRTHDAY_FIELD)).setValue(this.birthday);
		((JFormattedTextField)textFieldMap.get(LASTCONTACT_FIELD)).setValue(this.lastContact);
		((JFormattedTextField)textFieldMap.get(NEXTCONTACT_FIELD)).setValue(this.nextContact);	
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
