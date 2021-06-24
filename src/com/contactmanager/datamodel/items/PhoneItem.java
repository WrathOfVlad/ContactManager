package com.contactmanager.datamodel.items;

import java.util.Map;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;

public class PhoneItem extends DataItemHandler{

	public PhoneItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		this.dataType = DataType.PHONE;
		this.regex = PHONE_REGEX;
	}
	

	@Override
	public Boolean setValue(String value) {
		if (value == null) {this.dataValue = value; return true;};
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		
		try {
			PhoneNumber phone = phoneNumberUtil.parse(value, CountryCodeSource.UNSPECIFIED.name());
			if(!phoneNumberUtil.isValidNumber(phone)) return false;
			
			this.dataValue = value;
			return true;
			
		} catch (NumberParseException e) {
			//e.printStackTrace();
			return false;
		}	
	}
}
