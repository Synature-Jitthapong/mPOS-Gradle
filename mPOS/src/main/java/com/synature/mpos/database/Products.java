package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.ProductComponentGroupTable;
import com.synature.mpos.database.table.ProductComponentTable;
import com.synature.mpos.database.table.ProductDeptTable;
import com.synature.mpos.database.table.ProductGroupTable;
import com.synature.mpos.database.table.ProductTable;
import com.synature.pos.MenuGroups;
import com.synature.pos.ProductGroups;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class Products extends MPOSDatabase {
	
	public static final int NORMAL_TYPE = 0;
	public static final int SET_TYPE = 1;
	public static final int SIZE_TYPE = 2;
	public static final int OPEN_PRICE_TYPE = 5;
	public static final int SET_TYPE_CAN_SELECT = 7;
	public static final int VAT_TYPE_INCLUDED = 1;
	public static final int VAT_TYPE_EXCLUDE = 2;
	public static final int NO_VAT = 0;
	
	public static final String[] ALL_PRODUCT_COLS = {
		ProductTable.COLUMN_PRODUCT_ID, 
		ProductTable.COLUMN_PRODUCT_DEPT_ID,
		ProductTable.COLUMN_PRODUCT_CODE,
		ProductTable.COLUMN_PRODUCT_BAR_CODE,
		ProductTable.COLUMN_PRODUCT_NAME,
		ProductTable.COLUMN_PRODUCT_DESC,
		ProductTable.COLUMN_PRODUCT_TYPE_ID,
		ProductTable.COLUMN_PRODUCT_PRICE,
		ProductTable.COLUMN_PRODUCT_UNIT_NAME,
		ProductTable.COLUMN_DISCOUNT_ALLOW,
		ProductTable.COLUMN_VAT_TYPE,
		ProductTable.COLUMN_VAT_RATE,
		ProductTable.COLUMN_ISOUTOF_STOCK,
		ProductTable.COLUMN_IMG_URL
	};
	
	public static final String[] ALL_PRODUCT_GROUP_COLS = {
		ProductTable.COLUMN_PRODUCT_GROUP_ID,
		ProductGroupTable.COLUMN_PRODUCT_GROUP_CODE,
		ProductGroupTable.COLUMN_PRODUCT_GROUP_NAME,
	};
	
	public static final String[] ALL_PRODUCT_DEPT_COLS = {
		ProductTable.COLUMN_PRODUCT_GROUP_ID,
		ProductTable.COLUMN_PRODUCT_DEPT_ID,
		ProductDeptTable.COLUMN_PRODUCT_DEPT_CODE,
		ProductDeptTable.COLUMN_PRODUCT_DEPT_NAME
	};

	public Products(Context context){
		super(context);
	}
	
	/**
	 * @param productId
	 * @return List<ProductComponentGroup>
	 */
	public List<ProductComponentGroup> listProductComponentGroup(int productId){
		List<ProductComponentGroup> pCompGroupLst = null;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + ProductTable.COLUMN_PRODUCT_ID + ",a."
						+ ProductComponentTable.COLUMN_PGROUP_ID + ",a."
						+ ProductComponentGroupTable.COLUMN_SET_GROUP_NO
						+ ",a."
						+ ProductComponentGroupTable.COLUMN_SET_GROUP_NAME
						+ ",a." + ProductComponentGroupTable.COLUMN_REQ_AMOUNT
						+ ",a." + ProductTable.COLUMN_SALE_MODE + ",b."
						+ ProductTable.COLUMN_PRODUCT_NAME + ",b."
						+ ProductTable.COLUMN_IMG_URL + " FROM "
						+ ProductComponentGroupTable.TABLE_PCOMPONENT_GROUP + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a." + ProductTable.COLUMN_PRODUCT_ID + "=b."
						+ ProductTable.COLUMN_PRODUCT_ID + " WHERE a."
						+ ProductTable.COLUMN_PRODUCT_ID + "=?",
				new String[]{String.valueOf(productId)});
		if(cursor.moveToFirst()){
			pCompGroupLst = new ArrayList<ProductComponentGroup>();
			do{
				ProductComponentGroup pCompGroup = new ProductComponentGroup();
				pCompGroup.setProductId(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
				pCompGroup
						.setProductGroupId(cursor.getInt(cursor
								.getColumnIndex(ProductComponentTable.COLUMN_PGROUP_ID)));
				pCompGroup
						.setGroupNo(cursor.getInt(cursor
								.getColumnIndex(ProductComponentGroupTable.COLUMN_SET_GROUP_NO)));
				pCompGroup
						.setGroupName(cursor.getString(cursor
								.getColumnIndex(ProductComponentGroupTable.COLUMN_SET_GROUP_NAME)));
				pCompGroup
						.setRequireAmount(cursor.getDouble(cursor
								.getColumnIndex(ProductComponentGroupTable.COLUMN_REQ_AMOUNT)));
				pCompGroup.setSaleMode(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_SALE_MODE)));
				pCompGroup.setProductName(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
				pCompGroup.setImgUrl(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_IMG_URL)));
				pCompGroupLst.add(pCompGroup);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pCompGroupLst;
	}
	
	/**
	 * @param groupId
	 * @return List<ProductComponent>
	 */
	public List<ProductComponent> listProductComponent(int groupId){
		List<ProductComponent> pCompLst = null;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + ProductComponentTable.COLUMN_PGROUP_ID + ",a."
						+ ProductComponentTable.COLUMN_CHILD_PRODUCT_AMOUNT
						+ ",a."
						+ ProductComponentTable.COLUMN_FLEXIBLE_INCLUDE_PRICE
						+ ",a."
						+ ProductComponentTable.COLUMN_FLEXIBLE_PRODUCT_PRICE
						+ ",a." + ProductTable.COLUMN_SALE_MODE 
						+ ",b." + ProductTable.COLUMN_PRODUCT_ID 
						+ ",b."
						+ ProductTable.COLUMN_PRODUCT_NAME + ",b."
						+ ProductTable.COLUMN_PRODUCT_PRICE + ",b."
						+ ProductTable.COLUMN_IMG_URL + " FROM "
						+ ProductComponentTable.TABLE_PCOMPONENT + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a."
						+ ProductComponentTable.COLUMN_CHILD_PRODUCT_ID + "=b."
						+ ProductTable.COLUMN_PRODUCT_ID + " WHERE a."
						+ ProductComponentTable.COLUMN_PGROUP_ID + "=?",
				new String[]{String.valueOf(groupId)});
		if(cursor.moveToFirst()){
			pCompLst = new ArrayList<ProductComponent>();
			do{
				ProductComponent pComp = new ProductComponent();
				pComp.setProductGroupId(cursor.getInt(cursor
						.getColumnIndex(ProductComponentTable.COLUMN_PGROUP_ID)));
				pComp.setProductId(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
				pComp.setSaleMode(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_SALE_MODE)));
				pComp.setChildProductAmount(cursor.getInt(cursor
						.getColumnIndex(ProductComponentTable.COLUMN_CHILD_PRODUCT_AMOUNT)));
				pComp.setFlexibleIncludePrice(cursor.getInt(cursor
						.getColumnIndex(ProductComponentTable.COLUMN_FLEXIBLE_INCLUDE_PRICE)));
				pComp.setFlexibleProductPrice(cursor.getInt(cursor
						.getColumnIndex(ProductComponentTable.COLUMN_FLEXIBLE_PRODUCT_PRICE)));
				pComp.setProductName(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
				pComp.setImgUrl(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_IMG_URL)));
				pComp.setProductPrice(cursor.getDouble(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
				pCompLst.add(pComp);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pCompLst;
	}
	
	/**
	 * @return List<ProductGroup>
	 */
	public List<ProductGroup> listProductGroup(){
		List<ProductGroup> pgLst = new ArrayList<ProductGroup>();
		Cursor cursor = getReadableDatabase().query(ProductGroupTable.TABLE_PRODUCT_GROUP, 
				ALL_PRODUCT_GROUP_COLS, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				ProductGroup pg = toProductGroup(cursor);
				pgLst.add(pg);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pgLst;
	}

	/**
	 * @return List<ProductDept>
	 */
	public List<ProductDept> listProductDept(){
		List<ProductDept> pdLst = new ArrayList<ProductDept>();
		Cursor cursor = getReadableDatabase().query(ProductDeptTable.TABLE_PRODUCT_DEPT, 
				ALL_PRODUCT_DEPT_COLS,
				ProductTable.COLUMN_ACTIVATE + "=?", new String[]{"1"}, null, null, 
				COLUMN_ORDERING);
		if(cursor.moveToFirst()){
			do{
				ProductDept pd = toProductDept(cursor);
				pdLst.add(pd);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pdLst;
	}
	
	/**
	 * @param proId
	 * @return List<Product>
	 */
	public List<Product> listProductSize(int proId){
		List<Product> pLst = null;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT b." + ProductTable.COLUMN_PRODUCT_ID + ", " + " b."
						+ ProductTable.COLUMN_PRODUCT_TYPE_ID + ", " + " b."
						+ ProductTable.COLUMN_PRODUCT_CODE + ", " + " b."
						+ ProductTable.COLUMN_PRODUCT_BAR_CODE + ", " + " b."
						+ ProductTable.COLUMN_PRODUCT_NAME + ", " + " b."
						+ ProductTable.COLUMN_PRODUCT_PRICE + ", " + " b."
						+ ProductTable.COLUMN_VAT_TYPE + ", " + " b."
						+ ProductTable.COLUMN_VAT_RATE + " FROM "
						+ ProductComponentTable.TABLE_PCOMPONENT + " a "
						+ " INNER JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a."
						+ ProductComponentTable.COLUMN_CHILD_PRODUCT_ID + "=b."
						+ ProductTable.COLUMN_PRODUCT_ID + " WHERE a."
						+ ProductTable.COLUMN_PRODUCT_ID + "=?",
				new String[] { String.valueOf(proId) });
		if(cursor.moveToFirst()){
			pLst = new ArrayList<Product>();
			do{
				Product p = new Product();
				p.setProductId(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
				p.setProductTypeId(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_TYPE_ID)));
				p.setProductCode(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_CODE)));
				p.setProductBarCode(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_BAR_CODE)));
				p.setProductName(cursor.getString(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
				p.setProductPrice(cursor.getFloat(cursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
				p.setVatType(cursor.getInt(cursor
						.getColumnIndex(ProductTable.COLUMN_VAT_TYPE)));
				p.setVatRate(cursor.getFloat(cursor
						.getColumnIndex(ProductTable.COLUMN_VAT_RATE)));
				pLst.add(p);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pLst;
	}
	
	/**
	 * @param query
	 * @return List<Product>
	 */
	public List<Product> listProduct(String query){
		List<Product> pLst = new ArrayList<Product>();
		Cursor cursor = getReadableDatabase().query(
				ProductTable.TABLE_PRODUCT,
				ALL_PRODUCT_COLS,
				"(" + ProductTable.COLUMN_PRODUCT_CODE + " LIKE '%" + query
						+ "%' " + " OR " + ProductTable.COLUMN_PRODUCT_NAME
						+ " LIKE '%" + query + "%')", null, null, null, COLUMN_ORDERING);
		if(cursor.moveToFirst()){
			do{
				Product p = toProduct(cursor);
				pLst.add(p);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pLst;
	}
	
	/**
	 * @param deptId
	 * @return List<Product>
	 */
	public List<Product> listProduct(int deptId){
		List<Product> pLst = new ArrayList<Product>();
		Cursor cursor = getReadableDatabase().query(
				ProductTable.TABLE_PRODUCT,
				ALL_PRODUCT_COLS,
				ProductTable.COLUMN_PRODUCT_DEPT_ID + "=? " + " AND "
						+ ProductTable.COLUMN_ACTIVATE + "=?",
				new String[] { String.valueOf(deptId), "1" }, null, null, COLUMN_ORDERING);
		if(cursor.moveToFirst()){
			do{
				Product p = toProduct(cursor);
				pLst.add(p);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return pLst;
	}

	/**
	 * @param productId
	 * @return product vat rate
	 */
	public double getVatRate(int productId){
		double vatRate = 0.0f;
		Cursor cursor = queryProduct(
				new String[] { ProductTable.COLUMN_VAT_RATE },
				ProductTable.COLUMN_PRODUCT_ID + "=?",
				new String[] { String.valueOf(productId) });
		if(cursor.moveToFirst()){
			vatRate = cursor.getFloat(0);
		}
		cursor.close();
		return vatRate;
	}
	
	/**
	 * @param barCode
	 * @return Product
	 */
	public Product getProduct(String barCode){
		Product p = null;
		Cursor cursor = getReadableDatabase().query(ProductTable.TABLE_PRODUCT,
				ALL_PRODUCT_COLS, ProductTable.COLUMN_PRODUCT_BAR_CODE + "=?",
				new String[] { barCode }, null, null, "1");
		if(cursor.moveToFirst()){
			p = toProduct(cursor);
		}
		cursor.close();
		return p;
	}
	
	/**
	 * @param proId
	 * @return Product
	 */
	public Product getProduct(int proId){
		Product p = null;
		Cursor cursor = getReadableDatabase().query(ProductTable.TABLE_PRODUCT,
				ALL_PRODUCT_COLS, ProductTable.COLUMN_PRODUCT_ID + "=?",
				new String[] { String.valueOf(proId) }, null, null, null);
		if(cursor.moveToFirst()){
			p = toProduct(cursor);
		}
		cursor.close();
		return p;
	}
	
	/**
	 * @param deptId
	 * @return ProductDept
	 */
	public ProductDept getProductDept(int deptId){
		ProductDept pd = null;
		Cursor cursor = getReadableDatabase().query(
				ProductDeptTable.TABLE_PRODUCT_DEPT, ALL_PRODUCT_DEPT_COLS,
				ProductTable.COLUMN_PRODUCT_DEPT_ID + "=?",
				new String[] { String.valueOf(deptId) }, null, null, null);
		if(cursor.moveToFirst()){
			pd = toProductDept(cursor);
		}
		cursor.close();
		return pd;
	}
	
	/**
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor queryProduct(String[] columns, String selection, String[] selectionArgs){
		return getReadableDatabase().query(ProductTable.TABLE_PRODUCT, columns,
				selection, selectionArgs, null, null, null);
	}
	
	public Product toProduct(Cursor cursor){
		Product p = new Product();
		p.setProductId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
		p.setProductDeptId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_DEPT_ID)));
		p.setProductTypeId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_TYPE_ID)));
		p.setProductCode(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_CODE)));
		p.setProductBarCode(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_BAR_CODE)));
		p.setProductName(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
		p.setProductDesc(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_DESC)));
		p.setProductUnitName(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_UNIT_NAME)));
		p.setProductPrice(cursor.getFloat(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
		p.setVatType(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_VAT_TYPE)));
		p.setVatRate(cursor.getFloat(cursor
				.getColumnIndex(ProductTable.COLUMN_VAT_RATE)));
		p.setDiscountAllow(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_DISCOUNT_ALLOW)));
		p.setImgUrl(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_IMG_URL)));
		return p;
	}

	public ProductDept toProductDept(Cursor cursor){
		ProductDept pd = new ProductDept();
		pd.setProductDeptId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_DEPT_ID)));
		pd.setProductGroupId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_GROUP_ID)));
		pd.setProductDeptCode(cursor.getString(cursor
				.getColumnIndex(ProductDeptTable.COLUMN_PRODUCT_DEPT_CODE)));
		pd.setProductDeptName(cursor.getString(cursor
				.getColumnIndex(ProductDeptTable.COLUMN_PRODUCT_DEPT_NAME)));
		return pd;
	}

	public ProductGroup toProductGroup(Cursor cursor){
		ProductGroup pg = new ProductGroup();
		pg.setProductGroupId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_GROUP_ID)));
		pg.setProductGroupCode(cursor.getString(cursor
				.getColumnIndex(ProductGroupTable.COLUMN_PRODUCT_GROUP_CODE)));
		pg.setProductGroupName(cursor.getString(cursor
				.getColumnIndex(ProductGroupTable.COLUMN_PRODUCT_GROUP_NAME)));
		return pg;
	}
	
	/**
	 * @param pCompGroupLst
	 * @throws SQLException
	 */
	public void insertPComponentGroup(List<ProductGroups.PComponentGroup> pCompGroupLst) throws SQLException{
		getWritableDatabase().delete(ProductComponentGroupTable.TABLE_PCOMPONENT_GROUP, null, null);
		for(ProductGroups.PComponentGroup pCompGroup : pCompGroupLst){
			ContentValues cv = new ContentValues();
			cv.put(ProductComponentTable.COLUMN_PGROUP_ID, pCompGroup.getPGroupID());
			cv.put(ProductTable.COLUMN_PRODUCT_ID, pCompGroup.getProductID());
			cv.put(ProductTable.COLUMN_SALE_MODE, pCompGroup.getSaleMode());
			cv.put(ProductComponentGroupTable.COLUMN_SET_GROUP_NO, pCompGroup.getSetGroupNo());
			cv.put(ProductComponentGroupTable.COLUMN_SET_GROUP_NAME, pCompGroup.getSetGroupName());
			cv.put(ProductComponentGroupTable.COLUMN_REQ_AMOUNT, pCompGroup.getRequireAmount());
			getWritableDatabase().insertOrThrow(ProductComponentGroupTable.TABLE_PCOMPONENT_GROUP, null, cv);
		}
	}
	
	/**
	 * @param pCompLst
	 * @throws SQLException
	 */
	public void insertProductComponent(List<ProductGroups.ProductComponent> pCompLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(ProductComponentTable.TABLE_PCOMPONENT, null, null);
			for(ProductGroups.ProductComponent pCompSet : pCompLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductComponentTable.COLUMN_PGROUP_ID, pCompSet.getPGroupID());
				cv.put(ProductTable.COLUMN_PRODUCT_ID, pCompSet.getProductID());
				cv.put(ProductTable.COLUMN_SALE_MODE, pCompSet.getSaleMode());
				cv.put(ProductComponentTable.COLUMN_CHILD_PRODUCT_ID, pCompSet.getChildProductID());
				cv.put(ProductComponentTable.COLUMN_CHILD_PRODUCT_AMOUNT, pCompSet.getPGroupID());
				cv.put(ProductComponentTable.COLUMN_FLEXIBLE_PRODUCT_PRICE, pCompSet.getFlexibleProductPrice());
				cv.put(ProductComponentTable.COLUMN_FLEXIBLE_INCLUDE_PRICE, pCompSet.getFlexibleIncludePrice());
				getWritableDatabase().insertOrThrow(ProductComponentTable.TABLE_PCOMPONENT, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	/**
	 * @param pgLst
	 * @param mgLst
	 * @throws SQLException
	 */
	public void insertProductGroup(List<ProductGroups.ProductGroup> pgLst,
		List<MenuGroups.MenuGroup> mgLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(ProductGroupTable.TABLE_PRODUCT_GROUP, null, null);
			for(ProductGroups.ProductGroup pg : pgLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_PRODUCT_GROUP_ID, pg.getProductGroupId());
				cv.put(ProductGroupTable.COLUMN_PRODUCT_GROUP_CODE, pg.getProductGroupCode());
				cv.put(ProductGroupTable.COLUMN_PRODUCT_GROUP_NAME, pg.getProductGroupName());
				cv.put(ProductGroupTable.COLUMN_PRODUCT_GROUP_TYPE, pg.getProductGroupType());
				cv.put(ProductGroupTable.COLUMN_IS_COMMENT, pg.getIsComment());
				cv.put(COLUMN_ORDERING, pg.getProductGroupOrdering());
				cv.put(ProductTable.COLUMN_ACTIVATE, 0);
				getWritableDatabase().insertOrThrow(ProductGroupTable.TABLE_PRODUCT_GROUP, null, cv);
			}
			
			for(MenuGroups.MenuGroup mg : mgLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_ACTIVATE, mg.getActivate());
				getWritableDatabase().update(ProductGroupTable.TABLE_PRODUCT_GROUP, cv, 
						ProductTable.COLUMN_PRODUCT_GROUP_ID + "=?", 
						new String[]{
							String.valueOf(mg.getMenuGroupID())
						});
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	/**
	 * @param pdLst
	 * @param mdLst
	 * @throws SQLException
	 */
	public void insertProductDept(List<ProductGroups.ProductDept> pdLst,
		List<MenuGroups.MenuDept> mdLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(ProductDeptTable.TABLE_PRODUCT_DEPT, null, null);
			for(ProductGroups.ProductDept pd : pdLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_PRODUCT_DEPT_ID, pd.getProductDeptID());
				cv.put(ProductTable.COLUMN_PRODUCT_GROUP_ID, pd.getProductGroupID());
				cv.put(ProductDeptTable.COLUMN_PRODUCT_DEPT_CODE, pd.getProductDeptCode());
				cv.put(ProductDeptTable.COLUMN_PRODUCT_DEPT_NAME, pd.getProductDeptName());
				cv.put(COLUMN_ORDERING, pd.getProductDeptOrdering());
				cv.put(ProductTable.COLUMN_ACTIVATE, 0);
				getWritableDatabase().insertOrThrow(ProductDeptTable.TABLE_PRODUCT_DEPT, null, cv);
			}
			
			for(MenuGroups.MenuDept md : mdLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_ACTIVATE, md.getActivate());
				getWritableDatabase().update(ProductDeptTable.TABLE_PRODUCT_DEPT, cv, 
						ProductTable.COLUMN_PRODUCT_DEPT_ID + "=?",
						new String[]{
							String.valueOf(md.getMenuDeptID())
						}
				);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	/**
	 * @param pLst
	 * @param mLst
	 * @throws SQLException
	 */
	public void insertProducts(List<ProductGroups.Products> pLst,
		List<MenuGroups.MenuItem> mLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(ProductTable.TABLE_PRODUCT, null, null);
			for(ProductGroups.Products p : pLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_PRODUCT_ID, p.getProductID());
				cv.put(ProductTable.COLUMN_PRODUCT_DEPT_ID, p.getProductDeptID());
				cv.put(ProductTable.COLUMN_PRODUCT_CODE, p.getProductCode());
				cv.put(ProductTable.COLUMN_PRODUCT_BAR_CODE, p.getProductBarCode());
				cv.put(ProductTable.COLUMN_PRODUCT_TYPE_ID, p.getProductTypeID());
				cv.put(ProductTable.COLUMN_PRODUCT_PRICE, p.getProductPricePerUnit());
				cv.put(ProductTable.COLUMN_PRODUCT_UNIT_NAME, p.getProductUnitName());
				cv.put(ProductTable.COLUMN_PRODUCT_DESC, p.getProductDesc());
				cv.put(ProductTable.COLUMN_DISCOUNT_ALLOW, p.getDiscountAllow());
				cv.put(ProductTable.COLUMN_VAT_TYPE, p.getVatType());
				cv.put(ProductTable.COLUMN_VAT_RATE, p.getVatRate());
				cv.put(ProductTable.COLUMN_ISOUTOF_STOCK, p.getIsOutOfStock());
				getWritableDatabase().insertOrThrow(ProductTable.TABLE_PRODUCT, null, cv);
			}
			
			for(MenuGroups.MenuItem m : mLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_PRODUCT_NAME, m.getMenuName_0());
				cv.put(ProductTable.COLUMN_IMG_URL, m.getMenuImageLink());
				cv.put(COLUMN_ORDERING, m.getMenuItemOrdering());
				cv.put(ProductTable.COLUMN_ACTIVATE, m.getMenuActivate());
				getWritableDatabase().update(ProductTable.TABLE_PRODUCT, cv, ProductTable.COLUMN_PRODUCT_ID + "=?", 
						new String[]{String.valueOf(m.getProductID())});
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	public static class ProductComponentGroup extends ProductComponent{
		private int groupNo;
		private String groupName;
		private double requireAmount;
		
		public int getGroupNo() {
			return groupNo;
		}
		public void setGroupNo(int groupNo) {
			this.groupNo = groupNo;
		}
		public String getGroupName() {
			return groupName;
		}
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
		public double getRequireAmount() {
			return requireAmount;
		}
		public void setRequireAmount(double requireAmount) {
			this.requireAmount = requireAmount;
		}
	}
	
	public static class ProductComponent extends Product{
        private int childProductId;
        private double childProductAmount;
        private double flexibleProductPrice;
        private int flexibleIncludePrice;
		
        public int getChildProductId() {
			return childProductId;
		}
		public void setChildProductId(int childProductId) {
			this.childProductId = childProductId;
		}
		public double getChildProductAmount() {
			return childProductAmount;
		}
		public void setChildProductAmount(double childProductAmount) {
			this.childProductAmount = childProductAmount;
		}
		public double getFlexibleProductPrice() {
			return flexibleProductPrice;
		}
		public void setFlexibleProductPrice(double flexibleProductPrice) {
			this.flexibleProductPrice = flexibleProductPrice;
		}
		public int getFlexibleIncludePrice() {
			return flexibleIncludePrice;
		}
		public void setFlexibleIncludePrice(int flexibleIncludePrice) {
			this.flexibleIncludePrice = flexibleIncludePrice;
		}
	}
	
	public static class Product{
		private int productId;
		private int productGroupId;
		private int productDeptId;
		private String productCode;
		private String productBarCode;
		private String productName;
		private int productTypeId;
		private double productPrice;
		private String productUnitName;
		private String productDesc;
		private int discountAllow;
		private int vatType;
		private double vatRate;
		private int hasServiceCharge;
		private String imgUrl;
		private int saleMode;
		
		public int getSaleMode() {
			return saleMode;
		}
		public void setSaleMode(int saleMode) {
			this.saleMode = saleMode;
		}
		public int getProductGroupId() {
			return productGroupId;
		}
		public void setProductGroupId(int productGroupId) {
			this.productGroupId = productGroupId;
		}
		public int getProductDeptId() {
			return productDeptId;
		}
		public void setProductDeptId(int productDeptId) {
			this.productDeptId = productDeptId;
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
		public String getProductBarCode() {
			return productBarCode;
		}
		public void setProductBarCode(String productBarCode) {
			this.productBarCode = productBarCode;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getProductTypeId() {
			return productTypeId;
		}
		public void setProductTypeId(int productTypeId) {
			this.productTypeId = productTypeId;
		}
		public double getProductPrice() {
			return productPrice;
		}
		public void setProductPrice(double productPrice) {
			this.productPrice = productPrice;
		}
		public String getProductUnitName() {
			return productUnitName;
		}
		public void setProductUnitName(String productUnitName) {
			this.productUnitName = productUnitName;
		}
		public String getProductDesc() {
			return productDesc;
		}
		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}
		public int getDiscountAllow() {
			return discountAllow;
		}
		public void setDiscountAllow(int discountAllow) {
			this.discountAllow = discountAllow;
		}
		public int getVatType() {
			return vatType;
		}
		public void setVatType(int vatType) {
			this.vatType = vatType;
		}
		public double getVatRate() {
			return vatRate;
		}
		public void setVatRate(double vatRate) {
			this.vatRate = vatRate;
		}
		public int getHasServiceCharge() {
			return hasServiceCharge;
		}
		public void setHasServiceCharge(int hasServiceCharge) {
			this.hasServiceCharge = hasServiceCharge;
		}
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		@Override
		public String toString() {
			return productName;
		}
	}
	
	public static class ProductDept{
		private int productDeptId;
		private int productGroupId;
		private String productDeptCode;
		private String productDeptName;
		
		public int getProductDeptId() {
			return productDeptId;
		}
		public void setProductDeptId(int productDeptId) {
			this.productDeptId = productDeptId;
		}
		public int getProductGroupId() {
			return productGroupId;
		}
		public void setProductGroupId(int productGroupId) {
			this.productGroupId = productGroupId;
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
		
		@Override
		public String toString() {
			return productDeptName;
		}
	}
	
	public static class ProductGroup{
		private int productGroupId;
		private String productGroupCode;
		private String productGroupName;
		
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
}
