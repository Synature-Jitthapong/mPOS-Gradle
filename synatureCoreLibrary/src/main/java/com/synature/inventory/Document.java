package com.synature.inventory;

/**
 * 
 * @author j1tth4
 *
 */
public class Document{
	private int documentId;
	private int shopId;
	private int documentTypeId;
	private int documentNo;
	private String documentNumber;		// running no
	private String DocumentDate;
	private int UpdateBy;
	private String updateDate;
	private int documentStatus;
	private String remark;
	private int isSendToHQ;
	private String SendToHQDateTime;
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public int getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public int getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(int documentNo) {
		this.documentNo = documentNo;
	}
	public String getDocumentDate() {
		return DocumentDate;
	}
	public void setDocumentDate(String documentDate) {
		DocumentDate = documentDate;
	}
	public int getUpdateBy() {
		return UpdateBy;
	}
	public void setUpdateBy(int updateBy) {
		UpdateBy = updateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public int getDocumentStatus() {
		return documentStatus;
	}
	public void setDocumentStatus(int documentStatus) {
		this.documentStatus = documentStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsSendToHQ() {
		return isSendToHQ;
	}
	public void setIsSendToHQ(int isSendToHQ) {
		this.isSendToHQ = isSendToHQ;
	}
	public String getSendToHQDateTime() {
		return SendToHQDateTime;
	}
	public void setSendToHQDateTime(String sendToHQDateTime) {
		SendToHQDateTime = sendToHQDateTime;
	}
	
	public static class DocDetail{
		private int docDetailId;
		private int documentId;
		private int shopId;
		private int productId;
		private String unitName;
		private double productQty;
		private double productUnitPrice;
		private double productNetPrice;
		private int productTaxType;
		private double productTaxPrice;
		
		public int getDocDetailId() {
			return docDetailId;
		}
		public void setDocDetailId(int docDetailId) {
			this.docDetailId = docDetailId;
		}
		public int getDocumentId() {
			return documentId;
		}
		public void setDocumentId(int documentId) {
			this.documentId = documentId;
		}
		public int getShopId() {
			return shopId;
		}
		public void setShopId(int shopId) {
			this.shopId = shopId;
		}
		public int getProductId() {
			return productId;
		}
		public void setProductId(int productId) {
			this.productId = productId;
		}
		public String getUnitName() {
			return unitName;
		}
		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}
		public double getProductQty() {
			return productQty;
		}
		public void setProductQty(double productQty) {
			this.productQty = productQty;
		}
		public double getProductUnitPrice() {
			return productUnitPrice;
		}
		public void setProductUnitPrice(double productUnitPrice) {
			this.productUnitPrice = productUnitPrice;
		}
		public double getProductNetPrice() {
			return productNetPrice;
		}
		public void setProductNetPrice(double productNetPrice) {
			this.productNetPrice = productNetPrice;
		}
		public int getProductTaxType() {
			return productTaxType;
		}
		public void setProductTaxType(int productTaxType) {
			this.productTaxType = productTaxType;
		}
		public double getProductTaxPrice() {
			return productTaxPrice;
		}
		public void setProductTaxPrice(double productTaxPrice) {
			this.productTaxPrice = productTaxPrice;
		}
	}
	
	public static class DocumentType {
		private int documentTypeId;
		private String documentTypeHeader;
		private String documentTypeName;
		private int movementInStock;
		
		public int getDocumentTypeId() {
			return documentTypeId;
		}
		public void setDocumentTypeId(int documentTypeId) {
			this.documentTypeId = documentTypeId;
		}
		public String getDocumentTypeHeader() {
			return documentTypeHeader;
		}
		public void setDocumentTypeHeader(String documentTypeHeader) {
			this.documentTypeHeader = documentTypeHeader;
		}
		public String getDocumentTypeName() {
			return documentTypeName;
		}
		public void setDocumentTypeName(String documentTypeName) {
			this.documentTypeName = documentTypeName;
		}
		public int getMovementInStock() {
			return movementInStock;
		}
		public void setMovementInStock(int movementInStock) {
			this.movementInStock = movementInStock;
		}
	}
	
	public class DocumentParam {
		private int documentId;
		private int shopId;
		private int staffId;
		private int documentStatus;
		private String documentDate;
		private String documentNumber;
		private String remark;
		
		public DocumentParam(){
			
		}
		
		public DocumentParam(int documentId, int shopId, int staffId, 
				String documentDate, String documentNumber, int documentStatus) {
			this.documentId = documentId;
			this.shopId = shopId;
			this.staffId = staffId;
			this.documentDate = documentDate;
			this.documentNumber = documentNumber;
			this.documentStatus = documentStatus;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getDocumentNumber() {
			return documentNumber;
		}

		public void setDocumentNumber(String documentNumber) {
			this.documentNumber = documentNumber;
		}

		public String getDocumentDate() {
			return documentDate;
		}

		public void setDocumentDate(String documentDate) {
			this.documentDate = documentDate;
		}

		public int getStaffId() {
			return staffId;
		}

		public void setStaffId(int staffId) {
			this.staffId = staffId;
		}

		public int getDocumentId() {
			return documentId;
		}
		public void setDocumentId(int documentId) {
			this.documentId = documentId;
		}
		public int getShopId() {
			return shopId;
		}
		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public int getDocumentStatus() {
			return documentStatus;
		}

		public void setDocumentStatus(int documentStatus) {
			this.documentStatus = documentStatus;
		}
	}
}