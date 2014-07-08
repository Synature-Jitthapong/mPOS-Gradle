package com.synature.pos;

import java.util.List;

public class ShopData {
	public List<ShopProperty> ShopProperty;
	public List<ComputerProperty> ComputerProperty;
	public List<Staff> Staffs;
	public List<StaffPermission> StaffPermission;
	public List<GlobalProperty> GlobalProperty;
	public List<Language> Language;
	public List<ProgramFeature> ProgramFeature;
	public List<HeaderFooterReceipt> HeaderFooterReceipt;
	public List<BankName> BankName;
	public List<CreditCardType> CreditCardType;
	public List<Payment.PaymentAmountButton> PaymentAmountButton;
	public List<Payment.PayType> PayType; 
	
	public List<BankName> getBankName() {
		return BankName;
	}

	public void setBankName(List<BankName> bankName) {
		BankName = bankName;
	}

	public List<CreditCardType> getCreditCardType() {
		return CreditCardType;
	}

	public void setCreditCardType(List<CreditCardType> creditCardType) {
		CreditCardType = creditCardType;
	}

	public List<Payment.PaymentAmountButton> getPaymentAmountButton() {
		return PaymentAmountButton;
	}

	public void setPaymentAmountButton(
			List<Payment.PaymentAmountButton> paymentAmountButton) {
		PaymentAmountButton = paymentAmountButton;
	}

	public List<Payment.PayType> getPayType() {
		return PayType;
	}

	public void setPayType(List<Payment.PayType> payType) {
		PayType = payType;
	}

	public List<HeaderFooterReceipt> getHeaderFooterReceipt() {
		return HeaderFooterReceipt;
	}

	public void setHeaderFooterReceipt(List<HeaderFooterReceipt> headerFooterReceipt) {
		HeaderFooterReceipt = headerFooterReceipt;
	}

	public List<ProgramFeature> getProgramFeature() {
		return ProgramFeature;
	}

	public void setProgramFeature(List<ProgramFeature> programFeature) {
		ProgramFeature = programFeature;
	}

	public List<ShopProperty> getShopProperty() {
		return ShopProperty;
	}

	public void setShopProperty(List<ShopProperty> shopProperty) {
		ShopProperty = shopProperty;
	}

	public List<ComputerProperty> getComputerProperty() {
		return ComputerProperty;
	}

	public void setComputerProperty(List<ComputerProperty> computerProperty) {
		ComputerProperty = computerProperty;
	}

	public List<Staff> getStaffs() {
		return Staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		Staffs = staffs;
	}

	public List<GlobalProperty> getGlobalProperty() {
		return GlobalProperty;
	}

	public void setGlobalProperty(List<GlobalProperty> globalProperty) {
		GlobalProperty = globalProperty;
	}

	public List<Language> getLanguage() {
		return Language;
	}

	public void setLanguage(List<Language> language) {
		Language = language;
	}

	public static class ShopProperty {
		private int ShopID;
		private String ShopCode;
		private String ShopName;
		private int ShopType;
		private int FastFoodType;
		private int TableType;
		private int VatType;
		private double ServiceCharge;
		private int ServiceChargeType;
		private String OpenHour;
		private String CloseHour;
		private int CalculateServiceChargeWhenFreeBill;
		private String CompanyName;
		private String CompanyAddress1;
		private String CompanyAddress2;
		private String CompanyCity;
		private String CompanyProvince;
		private String CompanyZipCode;
		private String CompanyTelephone;
		private String CompanyFax;
		private String CompanyTaxID;
		private String CompanyRegisterID;
		private double CompanyVat;
		private int FeatureQueue;
		private int CommentType;

		public int getFeatureQueue() {
			return FeatureQueue;
		}

		public void setFeatureQueue(int featureQueue) {
			FeatureQueue = featureQueue;
		}

		public int getShopID() {
			return ShopID;
		}

		public void setShopID(int shopID) {
			ShopID = shopID;
		}

		public String getShopCode() {
			return ShopCode;
		}

		public void setShopCode(String shopCode) {
			ShopCode = shopCode;
		}

		public String getShopName() {
			return ShopName;
		}

		public void setShopName(String shopName) {
			ShopName = shopName;
		}

		public int getShopType() {
			return ShopType;
		}

		public void setShopType(int shopType) {
			ShopType = shopType;
		}

		public int getFastFoodType() {
			return FastFoodType;
		}

		public void setFastFoodType(int fastFoodType) {
			FastFoodType = fastFoodType;
		}

		public int getTableType() {
			return TableType;
		}

		public void setTableType(int tableType) {
			TableType = tableType;
		}

		public int getVatType() {
			return VatType;
		}

		public void setVatType(int vatType) {
			VatType = vatType;
		}

		public double getServiceCharge() {
			return ServiceCharge;
		}

		public void setServiceCharge(double serviceCharge) {
			ServiceCharge = serviceCharge;
		}

		public int getServiceChargeType() {
			return ServiceChargeType;
		}

		public void setServiceChargeType(int serviceChargeType) {
			ServiceChargeType = serviceChargeType;
		}

		public String getOpenHour() {
			return OpenHour;
		}

		public void setOpenHour(String openHour) {
			OpenHour = openHour;
		}

		public String getCloseHour() {
			return CloseHour;
		}

		public void setCloseHour(String closeHour) {
			CloseHour = closeHour;
		}

		public int getCalculateServiceChargeWhenFreeBill() {
			return CalculateServiceChargeWhenFreeBill;
		}

		public void setCalculateServiceChargeWhenFreeBill(
				int calculateServiceChargeWhenFreeBill) {
			CalculateServiceChargeWhenFreeBill = calculateServiceChargeWhenFreeBill;
		}

		public int getCommentType() {
			return CommentType;
		}

		public void setCommentType(int commentType) {
			CommentType = commentType;
		}

		public String getCompanyName() {
			return CompanyName;
		}

		public void setCompanyName(String companyName) {
			CompanyName = companyName;
		}

		public String getCompanyAddress1() {
			return CompanyAddress1;
		}

		public void setCompanyAddress1(String companyAddress1) {
			CompanyAddress1 = companyAddress1;
		}

		public String getCompanyAddress2() {
			return CompanyAddress2;
		}

		public void setCompanyAddress2(String companyAddress2) {
			CompanyAddress2 = companyAddress2;
		}

		public String getCompanyCity() {
			return CompanyCity;
		}

		public void setCompanyCity(String companyCity) {
			CompanyCity = companyCity;
		}

		public String getCompanyProvince() {
			return CompanyProvince;
		}

		public void setCompanyProvince(String companyProvince) {
			CompanyProvince = companyProvince;
		}

		public String getCompanyZipCode() {
			return CompanyZipCode;
		}

		public void setCompanyZipCode(String companyZipCode) {
			CompanyZipCode = companyZipCode;
		}

		public String getCompanyTelephone() {
			return CompanyTelephone;
		}

		public void setCompanyTelephone(String companyTelephone) {
			CompanyTelephone = companyTelephone;
		}

		public String getCompanyFax() {
			return CompanyFax;
		}

		public void setCompanyFax(String companyFax) {
			CompanyFax = companyFax;
		}

		public String getCompanyTaxID() {
			return CompanyTaxID;
		}

		public void setCompanyTaxID(String companyTaxID) {
			CompanyTaxID = companyTaxID;
		}

		public String getCompanyRegisterID() {
			return CompanyRegisterID;
		}

		public void setCompanyRegisterID(String companyRegisterID) {
			CompanyRegisterID = companyRegisterID;
		}

		public double getCompanyVat() {
			return CompanyVat;
		}

		public void setCompanyVat(double companyVat) {
			CompanyVat = companyVat;
		}
	}
	
	public static class ComputerProperty{
		private int ComputerID;
		private String ComputerName;
		private String DeviceCode;
		private String RegistrationNumber;
		private int IsMainComputer;
		private int IsQueueOrder;
		public int getComputerID() {
			return ComputerID;
		}
		public void setComputerID(int computerID) {
			ComputerID = computerID;
		}
		public String getComputerName() {
			return ComputerName;
		}
		public void setComputerName(String computerName) {
			ComputerName = computerName;
		}
		public String getDeviceCode() {
			return DeviceCode;
		}
		public void setDeviceCode(String deviceCode) {
			DeviceCode = deviceCode;
		}
		public String getRegistrationNumber() {
			return RegistrationNumber;
		}
		public void setRegistrationNumber(String registrationNumber) {
			RegistrationNumber = registrationNumber;
		}
		public int getIsMainComputer() {
			return IsMainComputer;
		}
		public void setIsMainComputer(int isMainComputer) {
			IsMainComputer = isMainComputer;
		}
		public int getIsQueueOrder() {
			return IsQueueOrder;
		}
		public void setIsQueueOrder(int isQueueOrder) {
			IsQueueOrder = isQueueOrder;
		}
	}

	public static class StaffPermission{
		private int StaffRoleID;
		private int PermissionItemID;
		
		public int getStaffRoleID() {
			return StaffRoleID;
		}
		public void setStaffRoleID(int staffRoleID) {
			StaffRoleID = staffRoleID;
		}
		public int getPermissionItemID() {
			return PermissionItemID;
		}
		public void setPermissionItemID(int permissionItemID) {
			PermissionItemID = permissionItemID;
		}
	}

	public static class GlobalProperty{
		private String CurrencySymbol;
		private String CurrencyCode;
		private String CurrencyName;
		private String CurrencyFormat;
		private String DateFormat;
		private String TimeFormat;
		private String QtyFormat;
		private String PrefixTextTW;
		private int PositionPrefix;
		
		public String getPrefixTextTW() {
			return PrefixTextTW;
		}
		public void setPrefixTextTW(String prefixTextTW) {
			PrefixTextTW = prefixTextTW;
		}
		public int getPositionPrefix() {
			return PositionPrefix;
		}
		public void setPositionPrefix(int positionPrefix) {
			PositionPrefix = positionPrefix;
		}
		public String getCurrencySymbol() {
			return CurrencySymbol;
		}
		public void setCurrencySymbol(String currencySymbol) {
			CurrencySymbol = currencySymbol;
		}
		public String getCurrencyCode() {
			return CurrencyCode;
		}
		public void setCurrencyCode(String currencyCode) {
			CurrencyCode = currencyCode;
		}
		public String getCurrencyName() {
			return CurrencyName;
		}
		public void setCurrencyName(String currencyName) {
			CurrencyName = currencyName;
		}
		public String getCurrencyFormat() {
			return CurrencyFormat;
		}
		public void setCurrencyFormat(String currencyFormat) {
			CurrencyFormat = currencyFormat;
		}
		public String getDateFormat() {
			return DateFormat;
		}
		public void setDateFormat(String dateFormat) {
			DateFormat = dateFormat;
		}
		public String getTimeFormat() {
			return TimeFormat;
		}
		public void setTimeFormat(String timeFormat) {
			TimeFormat = timeFormat;
		}
		public String getQtyFormat() {
			return QtyFormat;
		}
		public void setQtyFormat(String qtyFormat) {
			QtyFormat = qtyFormat;
		}
		
	}

	public static class Language{
		private int LangID;
		private String LangName;
		private String LangCode;
		private int IsDefault;
		
		
		public int getIsDefault() {
			return IsDefault;
		}
		public void setIsDefault(int isDefault) {
			IsDefault = isDefault;
		}
		public int getLangID() {
			return LangID;
		}
		public void setLangID(int langID) {
			LangID = langID;
		}
		public String getLangName() {
			return LangName;
		}
		public void setLangName(String langName) {
			LangName = langName;
		}
		public String getLangCode() {
			return LangCode;
		}
		public void setLangCode(String langCode) {
			LangCode = langCode;
		}
		
		@Override
		public String toString() {
			return LangName;
		}
		
	}
	
	public static class Staff {
		public Staff(){
			
		}
		
		public Staff(int staffId, String staffCode, String staffName, String staffPassword){
			this.StaffID = staffId;
			this.StaffCode = staffCode;
			this.StaffName = staffName;
			this.StaffPassword = staffPassword;
		}
		
		private int StaffID;
		private String StaffCode;
		private String StaffName;
		private String StaffPassword;
		
		public int getStaffID() {
			return StaffID;
		}

		public void setStaffID(int staffID) {
			StaffID = staffID;
		}

		public String getStaffCode() {
			return StaffCode;
		}

		public void setStaffCode(String staffCode) {
			StaffCode = staffCode;
		}

		public String getStaffName() {
			return StaffName;
		}

		public void setStaffName(String staffName) {
			StaffName = staffName;
		}

		public String getStaffPassword() {
			return StaffPassword;
		}

		public void setStaffPassword(String staffPassword) {
			StaffPassword = staffPassword;
		}	
	}
	
	public static class ProgramFeature{
		private int FeatureID;
        private String FeatureName;
        private int FeatureValue;
        private String FeatureText;
        private String FeatureDesc;
		public int getFeatureID() {
			return FeatureID;
		}
		public void setFeatureID(int featureID) {
			FeatureID = featureID;
		}
		public String getFeatureName() {
			return FeatureName;
		}
		public void setFeatureName(String featureName) {
			FeatureName = featureName;
		}
		public int getFeatureValue() {
			return FeatureValue;
		}
		public void setFeatureValue(int featureValue) {
			FeatureValue = featureValue;
		}
		public String getFeatureText() {
			return FeatureText;
		}
		public void setFeatureText(String featureText) {
			FeatureText = featureText;
		}
		public String getFeatureDesc() {
			return FeatureDesc;
		}
		public void setFeatureDesc(String featureDesc) {
			FeatureDesc = featureDesc;
		}
	}
	
	public static class HeaderFooterReceipt{
		private String TextInLine;
		private int LineType;
		private int LineOrder;
		
		public String getTextInLine() {
			return TextInLine;
		}
		public void setTextInLine(String textInLine) {
			TextInLine = textInLine;
		}
		public int getLineType() {
			return LineType;
		}
		public void setLineType(int lineType) {
			LineType = lineType;
		}
		public int getLineOrder() {
			return LineOrder;
		}
		public void setLineOrder(int lineOrder) {
			LineOrder = lineOrder;
		}
	}
}
