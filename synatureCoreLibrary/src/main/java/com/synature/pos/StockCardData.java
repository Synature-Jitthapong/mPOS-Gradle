package com.synature.pos;

public class StockCardData {
	private String productCode;
	private String productName;
	private double begin;
	private double receipt;
	private double sale;
	private double endding;
	private double variance;
	private double summary;
	private String productUnitName;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getBegin() {
		return begin;
	}
	public void setBegin(double begin) {
		this.begin = begin;
	}
	public double getReceipt() {
		return receipt;
	}
	public void setReceipt(double receipt) {
		this.receipt = receipt;
	}
	public double getSale() {
		return sale;
	}
	public void setSale(double sale) {
		this.sale = sale;
	}
	public double getEndding() {
		return endding;
	}
	public void setEndding(double endding) {
		this.endding = endding;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public double getSummary() {
		return summary;
	}
	public void setSummary(double summary) {
		this.summary = summary;
	}
	public String getProductUnitName() {
		return productUnitName;
	}
	public void setProductUnitName(String productUnitName) {
		this.productUnitName = productUnitName;
	}
}
