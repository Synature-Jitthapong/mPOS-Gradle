package com.synature.mpos.database;

public class StockProduct {
	private int id;
	private int proId;
	private String code;
	private String name;
	private double init;
	private double receive;
	private double sale;
	private double variance;
	private double diff;
	private double currQty;
	private double countQty;
	private double unitPrice;
	private String unitName;

	public StockProduct() {

	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getInit() {
		return init;
	}

	public void setInit(double init) {
		this.init = init;
	}

	public double getReceive() {
		return receive;
	}

	public void setReceive(double receive) {
		this.receive = receive;
	}

	public double getSale() {
		return sale;
	}

	public void setSale(double sale) {
		this.sale = sale;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCurrQty() {
		return currQty;
	}

	public void setCurrQty(double currQty) {
		this.currQty = currQty;
	}

	public double getCountQty() {
		return countQty;
	}

	public void setCountQty(double countQty) {
		this.countQty = countQty;
	}
}
