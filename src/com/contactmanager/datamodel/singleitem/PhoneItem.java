package com.contactmanager.datamodel.singleitem;

import java.util.Map;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;

public class PhoneItem extends DefaultItem{

	public PhoneItem(String dataId, Map<String, Object> metaData) throws Exception {
		super(dataId,metaData);
		this.dataType = DataType.PHONE;
		this.regex = PHONE_REGEX;
	}
	

	@Override
	public Boolean setValue(String value) {
		if (value == null || value.equals("")) {this.dataValue = value; return true;};
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
