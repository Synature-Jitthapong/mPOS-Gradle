package com.synature.mpos.database;

public class StockDocument {

	public static final int SALE_DOC = 20;
	public static final int VOID_DOC = 21;
	public static final int DAILY_DOC = 24;
	public static final int DAILY_ADD_DOC = 18;
	public static final int DAILY_REDUCE_DOC = 19;
	public static final int DIRECT_RECEIVE_DOC = 39;
	public static final int DOC_STATUS_NEW = 0;
	public static final int DOC_STATUS_SAVE = 1;
	public static final int DOC_STATUS_APPROVE = 2;
	public static final int DOC_STATUS_CANCLE = 99;

	public static abstract class DocumentTable{
		public static final String TABLE_DOCUMENT = "Document";
		public static final String COLUMN_DOC_ID = "document_id";
		public static final String COLUMN_REF_DOC_ID = "ref_doc_id";
		public static final String COLUMN_REF_SHOP_ID = "ref_shop_id";
		public static final String COLUMN_DOC_NO = "document_no";
		public static final String COLUMN_DOC_DATE = "document_date";
		public static final String COLUMN_DOC_YEAR = "document_year";
		public static final String COLUMN_DOC_MONTH = "document_month";
		public static final String COLUMN_UPDATE_BY = "update_by";
		public static final String COLUMN_UPDATE_DATE = "update_date";
		public static final String COLUMN_DOC_STATUS = "document_status_id";
		public static final String COLUMN_REMARK = "remark";
		public static final String COLUMN_IS_SEND_TO_HQ = "is_send_to_hq";
		public static final String COLUMN_IS_SEND_TO_HQ_DATE = "is_send_to_hq_date_time";	
	}
	
	public static abstract class DocumentTypeTable{
		public static final String TABLE_DOCUMENT_TYPE = "DocumentType";
		public static final String COLUMN_DOC_TYPE = "document_type_id";
		public static final String COLUMN_DOC_TYPE_HEADER = "document_type_header";
		public static final String COLUMN_DOC_TYPE_NAME = "document_type_name";
		public static final String COLUMN_MOVE_MENT = "movement_in_stock";
	}
}
