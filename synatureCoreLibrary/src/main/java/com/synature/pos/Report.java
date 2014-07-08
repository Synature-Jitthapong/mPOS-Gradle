package com.synature.pos;

import java.util.ArrayList;
import java.util.List;

public class Report {
	private List<GroupOfProduct> groupOfProductLst = 
			new ArrayList<GroupOfProduct>();
	private List<ReportDetail> reportDetail = 
			new ArrayList<ReportDetail>();
	
	public List<GroupOfProduct> getGroupOfProductLst() {
		return groupOfProductLst;
	}

	public void setGroupOfProductLst(List<GroupOfProduct> groupOfProductLst) {
		this.groupOfProductLst = groupOfProductLst;
	}

	public List<ReportDetail> getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(List<ReportDetail> reportDetail) {
		this.reportDetail = reportDetail;
	}

	public static class GroupOfProduct{
		private int productGroupId;
		private String productGroupCode;
		private String productGroupName;
		private int productDeptId;
		private String productDeptCode;
		private String productDeptName;
		private List<ReportDetail> reportDetail = 
				new ArrayList<ReportDetail>();
		
		public List<ReportDetail> getReportDetail() {
			return reportDetail;
		}
		public void setReportDetail(List<ReportDetail> reportDetail) {
			this.reportDetail = reportDetail;
		}
		public int getProductDeptId() {
			return productDeptId;
		}
		public void setProductDeptId(int productDeptId) {
			this.productDeptId = productDeptId;
		}
		public String getProductDeptCode() {
			return productDeptCode;
		}
		public void setProductDeptCode(String productDeptCode) {
			this.productDeptCode = productDeptCode;
		}
		public String getProductDeptName() {
			return productDeptName;
		}
		public void setProductDeptName(String productDeptName) {
			this.productDeptName = productDeptName;
		}
		public int getProductGroupId() {
			return productGroupId;
		}
		public void setProductGroupId(int productGroupId) {
			this.productGroupId = productGroupId;
		}
		public String getProductGroupCode() {
			return productGroupCode;
		}
		public void setProductGroupCode(String productGroupCode) {
			this.productGroupCode = productGroupCode;
		}
		public String getProductGroupName() {
			return productGroupName;
		}
		public void setProductGroupName(String productGroupName) {
			this.productGroupName = productGroupName;
		}	
	}
	
	public static class ReportDetail{
		private int transactionId;
		private int computerId;
		private int productId;
		private int transStatus;
		private String productGroupCode;
		private String productGroupName;
		private String productDeptCode;
		private String productDeptName;
		private String productCode;
		private String productName;
		private double pricePerUnit;
		private double qty;
		private double qtyPercent;
		private double discount;
		private double totalPrice;
		private double subTotal;
		private double subTotalPercent;
		private double totalSale;
		private double totalPricePercent;
		private String vat;
		private String receiptNo;
		private double serviceCharge;
		private double vatable;
		private double totalVat;
		private double cash;
		private double credit;
		private double totalPayment;
		private double diff;
		private double vatExclude;
		private int sendStatus;

		public int getSendStatus() {
			return sendStatus;
		}
		public void setSendStatus(int sendStatus) {
			this.sendStatus = sendStatus;
		}
		public double getCredit() {
			return credit;
		}
		public void setCredit(double credit) {
			this.credit = credit;
		}
		public String getProductGroupCode() {
			return productGroupCode;
		}
		public void setProductGroupCode(String productGroupCode) {
			this.productGroupCode = productGroupCode;
		}
		public String getProductGroupName() {
			return productGroupName;
		}
		public void setProductGroupName(String productGroupName) {
			this.productGroupName = productGroupName;
		}
		public String getProductDeptCode() {
			return productDeptCode;
		}
		public void setProductDeptCode(String productDeptCode) {
			this.productDeptCode = productDeptCode;
		}
		public String getProductDeptName() {
			return productDeptName;
		}
		public void setProductDeptName(String productDeptName) {
			this.productDeptName = productDeptName;
		}
		public double getSubTotalPercent() {
			return subTotalPercent;
		}
		public void setSubTotalPercent(double subTotalPercent) {
			this.subTotalPercent = subTotalPercent;
		}
		public int getTransStatus() {
			return transStatus;
		}
		public void setTransStatus(int transStatus) {
			this.transStatus = transStatus;
		}
		public int getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(int transactionId) {
			this.transactionId = transactionId;
		}
		public int getComputerId() {
			return computerId;
		}
		public void setComputerId(int computerId) {
			this.computerId = computerId;
		}
		public double getVatExclude() {
			return vatExclude;
		}
		public void setVatExclude(double vatExclude) {
			this.vatExclude = vatExclude;
		}
		public String getReceiptNo() {
			return receiptNo;
		}
		public void setReceiptNo(String receiptNo) {
			this.receiptNo = receiptNo;
		}
		public double getPricePerUnit() {
			return pricePerUnit;
		}
		public void setPricePerUnit(double pricePerUnit) {
			this.pricePerUnit = pricePerUnit;
		}
		public double getQty() {
			return qty;
		}
		public void setQty(double qty) {
			this.qty = qty;
		}
		public double getQtyPercent() {
			return qtyPercent;
		}
		public void setQtyPercent(double qtyPercent) {
			this.qtyPercent = qtyPercent;
		}
		public double getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
		}
		public double getSubTotal() {
			return subTotal;
		}
		public void setSubTotal(double subTotal) {
			this.subTotal = subTotal;
		}
		public double getTotalSale() {
			return totalSale;
		}
		public void setTotalSale(double totalSale) {
			this.totalSale = totalSale;
		}
		public double getTotalVat() {
			return totalVat;
		}
		public void setTotalVat(double totalVat) {
			this.totalVat = totalVat;
		}
		public int getProductId() {
			return productId;
		}
		public void setProductId(int productId) {
			this.productId = productId;
		}
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
		public double getDiscount() {
			return discount;
		}
		public void setDiscount(double discount) {
			this.discount = discount;
		}
		public double getTotalPricePercent() {
			return totalPricePercent;
		}
		public void setTotalPricePercent(double totalPricePercent) {
			this.totalPricePercent = totalPricePercent;
		}
		public String getVat() {
			return vat;
		}
		public void setVat(String vat) {
			this.vat = vat;
		}
		public double getServiceCharge() {
			return serviceCharge;
		}
		public void setServiceCharge(double serviceCharge) {
			this.serviceCharge = serviceCharge;
		}
		public double getVatable() {
			return vatable;
		}
		public void setVatable(double vatable) {
			this.vatable = vatable;
		}
		public double getCash() {
			return cash;
		}
		public void setCash(double cash) {
			this.cash = cash;
		}
		public double getTotalPayment() {
			return totalPayment;
		}
		public void setTotalPayment(double totalPayment) {
			this.totalPayment = totalPayment;
		}
		public double getDiff() {
			return diff;
		}
		public void setDiff(double diff) {
			this.diff = diff;
		}
	}
	
}
