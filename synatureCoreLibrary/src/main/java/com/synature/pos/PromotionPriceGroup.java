package com.synature.pos;

public class PromotionPriceGroup {
	private int priceGroupId;
	private int typeId;
	private double percentDiscount;
	private double discontAmount;
	public int getPriceGroupId() {
		return priceGroupId;
	}
	public void setPriceGroupId(int priceGroupId) {
		this.priceGroupId = priceGroupId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public double getPercentDiscount() {
		return percentDiscount;
	}
	public void setPercentDiscount(double percentDiscount) {
		this.percentDiscount = percentDiscount;
	}
	public double getDiscontAmount() {
		return discontAmount;
	}
	public void setDiscontAmount(double discontAmount) {
		this.discontAmount = discontAmount;
	}
}
