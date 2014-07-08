package com.synature.pos;

public class MemberGroup {
	private int memberGroupId;
	private String memberGroupCode;
	private String memberGroupName;
	
	public String getMemberGroupCode() {
		return memberGroupCode;
	}
	public void setMemberGroupCode(String memberGroupCode) {
		this.memberGroupCode = memberGroupCode;
	}
	public int getMemberGroupId() {
		return memberGroupId;
	}
	public void setMemberGroupId(int memberGroupId) {
		this.memberGroupId = memberGroupId;
	}
	public String getMemberGroupName() {
		return memberGroupName;
	}
	public void setMemberGroupName(String memberGroupName) {
		this.memberGroupName = memberGroupName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.memberGroupName;
	}
	
	public static class Members {
		 private int iMemberID;
		 private int iMemberGroupID;
		 private String szMemberGroupName;
		 private String szMemberCode;
		 private String szMemberPassword;
		 private int iMemberGender;
		 private String szMemberFirstName;
		 private String szMemberLastName;
		 private String szMemberFullName;
		 private String szMemberAddress1;
		 private String szMemberAddress2;
		 private String szMemberCity;
		 private int iMemberProvinceID;
		 private String szMemberProvinceName;
		 private String szMemberZipCode;
		 private String szMemberPhone;
		 private String szMemberMobile;
		 private String szMemberFax;
		 private String szMemberEmail;
		 private String szMemberAdditional;
		 private String szMemberBirthday;
		 private String szMemberExpiration;
		 private String szMemberImageFileName;
		 private double fTotalPoint;
		 private String szUpdatePoint;
		 
		public int getiMemberID() {
			return iMemberID;
		}
		public void setiMemberID(int iMemberID) {
			this.iMemberID = iMemberID;
		}
		public int getiMemberGroupID() {
			return iMemberGroupID;
		}
		public void setiMemberGroupID(int iMemberGroupID) {
			this.iMemberGroupID = iMemberGroupID;
		}
		public String getSzMemberGroupName() {
			return szMemberGroupName;
		}
		public void setSzMemberGroupName(String szMemberGroupName) {
			this.szMemberGroupName = szMemberGroupName;
		}
		public String getSzMemberCode() {
			return szMemberCode;
		}
		public void setSzMemberCode(String szMemberCode) {
			this.szMemberCode = szMemberCode;
		}
		public String getSzMemberPassword() {
			return szMemberPassword;
		}
		public void setSzMemberPassword(String szMemberPassword) {
			this.szMemberPassword = szMemberPassword;
		}
		public int getiMemberGender() {
			return iMemberGender;
		}
		public void setiMemberGender(int iMemberGender) {
			this.iMemberGender = iMemberGender;
		}
		public String getSzMemberFirstName() {
			return szMemberFirstName;
		}
		public void setSzMemberFirstName(String szMemberFirstName) {
			this.szMemberFirstName = szMemberFirstName;
		}
		public String getSzMemberLastName() {
			return szMemberLastName;
		}
		public void setSzMemberLastName(String szMemberLastName) {
			this.szMemberLastName = szMemberLastName;
		}
		public String getSzMemberFullName() {
			return szMemberFullName;
		}
		public void setSzMemberFullName(String szMemberFullName) {
			this.szMemberFullName = szMemberFullName;
		}
		public String getSzMemberAddress1() {
			return szMemberAddress1;
		}
		public void setSzMemberAddress1(String szMemberAddress1) {
			this.szMemberAddress1 = szMemberAddress1;
		}
		public String getSzMemberAddress2() {
			return szMemberAddress2;
		}
		public void setSzMemberAddress2(String szMemberAddress2) {
			this.szMemberAddress2 = szMemberAddress2;
		}
		public String getSzMemberCity() {
			return szMemberCity;
		}
		public void setSzMemberCity(String szMemberCity) {
			this.szMemberCity = szMemberCity;
		}
		public int getiMemberProvinceID() {
			return iMemberProvinceID;
		}
		public void setiMemberProvinceID(int iMemberProvinceID) {
			this.iMemberProvinceID = iMemberProvinceID;
		}
		public String getSzMemberProvinceName() {
			return szMemberProvinceName;
		}
		public void setSzMemberProvinceName(String szMemberProvinceName) {
			this.szMemberProvinceName = szMemberProvinceName;
		}
		public String getSzMemberZipCode() {
			return szMemberZipCode;
		}
		public void setSzMemberZipCode(String szMemberZipCode) {
			this.szMemberZipCode = szMemberZipCode;
		}
		public String getSzMemberPhone() {
			return szMemberPhone;
		}
		public void setSzMemberPhone(String szMemberPhone) {
			this.szMemberPhone = szMemberPhone;
		}
		public String getSzMemberMobile() {
			return szMemberMobile;
		}
		public void setSzMemberMobile(String szMemberMobile) {
			this.szMemberMobile = szMemberMobile;
		}
		public String getSzMemberFax() {
			return szMemberFax;
		}
		public void setSzMemberFax(String szMemberFax) {
			this.szMemberFax = szMemberFax;
		}
		public String getSzMemberEmail() {
			return szMemberEmail;
		}
		public void setSzMemberEmail(String szMemberEmail) {
			this.szMemberEmail = szMemberEmail;
		}
		public String getSzMemberAdditional() {
			return szMemberAdditional;
		}
		public void setSzMemberAdditional(String szMemberAdditional) {
			this.szMemberAdditional = szMemberAdditional;
		}
		public String getSzMemberBirthday() {
			return szMemberBirthday;
		}
		public void setSzMemberBirthday(String szMemberBirthday) {
			this.szMemberBirthday = szMemberBirthday;
		}
		public String getSzMemberExpiration() {
			return szMemberExpiration;
		}
		public void setSzMemberExpiration(String szMemberExpiration) {
			this.szMemberExpiration = szMemberExpiration;
		}
		public String getSzMemberImageFileName() {
			return szMemberImageFileName;
		}
		public void setSzMemberImageFileName(String szMemberImageFileName) {
			this.szMemberImageFileName = szMemberImageFileName;
		}
		public double getfTotalPoint() {
			return fTotalPoint;
		}
		public void setfTotalPoint(double fTotalPoint) {
			this.fTotalPoint = fTotalPoint;
		}
		public String getSzUpdatePoint() {
			return szUpdatePoint;
		}
		public void setSzUpdatePoint(String szUpdatePoint) {
			this.szUpdatePoint = szUpdatePoint;
		}	 
	}
}
