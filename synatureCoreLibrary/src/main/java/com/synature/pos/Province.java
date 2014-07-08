package com.synature.pos;

public class Province {
	private int provinceId;
	private String provinceName;
	private int langId;
	
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.provinceName;
	}
}
