package com.synature.mpos;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.Transaction;
import com.synature.mpos.database.Products.Product;
import com.synature.util.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ProductSetActivity extends Activity{

	public static final String EDIT_MODE = "edit";
	
	public static final String ADD_MODE = "add";
	
	private Products mProduct;
	private Formater mFormat;
	
	private Transaction mTrans;
	
	private int mTransactionId;
	private int mComputerId;
	private int mProductId;
	private int mOrderDetailId;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Register ExceptinHandler for catch error when application crash.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, 
				Utils.LOG_DIR, Utils.LOG_FILE_NAME));
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_product_set);
		
		mProduct = new Products(getApplicationContext());
		mFormat = new Formater(getApplicationContext());
		mTrans = new Transaction(getApplicationContext());
		mTrans.getWritableDatabase().beginTransaction();
		
		Intent intent = getIntent();
		mTransactionId = intent.getIntExtra("transactionId", 0);
		mComputerId = intent.getIntExtra("computerId", 0);
		mProductId = intent.getIntExtra("productId", 0);
		
		if(intent.getStringExtra("mode").equals(ADD_MODE)){
			final Product p = mProduct.getProduct(mProductId);
			if(p.getProductPrice() > -1){
				mOrderDetailId = mTrans.addOrderDetail(mTransactionId, 
						mComputerId, mProductId, p.getProductTypeId(), 
						p.getVatType(), p.getVatRate(), 1, p.getProductPrice());
				if (savedInstanceState == null) {
					getFragmentManager().beginTransaction()
							.replace(R.id.container, 
									PlaceholderFragment.newInsance(mTransactionId, mOrderDetailId, mProductId)).commit();
				}
			}else{
				final EditText txtProductPrice = new EditText(this);
				txtProductPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
				txtProductPrice.setOnEditorActionListener(new OnEditorActionListener(){
			
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						return false;
					}
					
				});
				new AlertDialog.Builder(this)
				.setTitle(R.string.enter_price)
				.setView(txtProductPrice)
				.setCancelable(false)
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelOrderSet();
					}
					
				})
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						double openPrice = 0.0f;
						try {
							openPrice = Utils.stringToDouble(txtProductPrice.getText().toString());
							mOrderDetailId = mTrans.addOrderDetail(mTransactionId, 
									mComputerId, p.getProductId(), p.getProductTypeId(), 
									p.getVatType(), p.getVatRate(), 1, openPrice);
							if (savedInstanceState == null) {
								getFragmentManager().beginTransaction()
										.add(R.id.container, 
												PlaceholderFragment.newInsance(mTransactionId, mOrderDetailId, mProductId)).commit();
							}
						} catch (ParseException e) {
							new AlertDialog.Builder(ProductSetActivity.this)
							.setTitle(R.string.enter_price)
							.setMessage(R.string.enter_valid_numeric)
							.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {	
								}
							})
							.show();
							e.printStackTrace();
						}
					}
				})
				.show();
			}	
		}else if(intent.getStringExtra("mode").equals(EDIT_MODE)){
			mOrderDetailId = intent.getIntExtra("orderDetailId", 0);
			if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
						.replace(R.id.container, 
								PlaceholderFragment.newInsance(mTransactionId, mOrderDetailId, mProductId)).commit();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_set, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			cancelOrderSet();
			return true;
		case R.id.itemConfirm:
			confirmOrderSet();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void confirmOrderSet(){
		List<Products.ProductComponentGroup> productCompGroupLst 
			= mProduct.listProductComponentGroup(mProductId);
		boolean canDone = true;
		String selectGroup = "";
		if(productCompGroupLst != null){
			Iterator<Products.ProductComponentGroup> it = 
					productCompGroupLst.iterator();
			while(it.hasNext()){
				Products.ProductComponentGroup pCompGroup = it.next();
				if(pCompGroup.getRequireAmount() > 0){
					if(pCompGroup.getRequireAmount() - mTrans.getOrderSetTotalQty(mTransactionId, mOrderDetailId, 
							pCompGroup.getProductGroupId()) > 0){
						canDone = false;
						selectGroup = pCompGroup.getGroupName();
						break;
					}
				}
			}
		}
		
		if(canDone){
			mTrans.getWritableDatabase().setTransactionSuccessful();
			mTrans.getWritableDatabase().endTransaction();
			finish();
		}else{
			new AlertDialog.Builder(ProductSetActivity.this)
			.setTitle(R.string.set_menu)
			.setMessage(ProductSetActivity.this.getString(R.string.please_select) + " " + selectGroup)
			.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
	}
	
	private void cancelOrderSet(){
		if(getIntent().getStringExtra("mode").equals(ADD_MODE)){
			mTrans.cancelOrder(mTransactionId);
			mTrans.getWritableDatabase().setTransactionSuccessful();
		}
		mTrans.getWritableDatabase().endTransaction();
		finish();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment{
		
		private int mTransactionId;
		private int mOrderDetailId;
		private int mProductId;
		
		private List<Products.ProductComponent> mProductCompLst;
		private List<MPOSOrderTransaction.OrderSet> mOrderSetLst;
		private OrderSetAdapter mOrderSetAdapter;
		
		private ExpandableListView mLvOrderSet;
		private GridView mGvSetItem;
		private HorizontalScrollView mScroll;
		
		public static PlaceholderFragment newInsance(int transactionId, int orderDetailId, int productId) {
			PlaceholderFragment f = new PlaceholderFragment();
			Bundle b = new Bundle();
			b.putInt("transactionId", transactionId);
			b.putInt("orderDetailId", orderDetailId);
			b.putInt("productId", productId);
			f.setArguments(b);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mTransactionId = getArguments().getInt("transactionId");
			mOrderDetailId = getArguments().getInt("orderDetailId");
			mProductId = getArguments().getInt("productId");
			
			mProductCompLst = new ArrayList<Products.ProductComponent>();
			mOrderSetLst = new ArrayList<MPOSOrderTransaction.OrderSet>();
			mOrderSetAdapter = new OrderSetAdapter();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			createSetGroupButton();
			loadOrderSet();
		}

		private void updateBadge(int groupId, double requireAmount){
			double totalQty = ((ProductSetActivity) getActivity()).mTrans.getOrderSetTotalQty(
				mTransactionId, mOrderDetailId, groupId);
			final LinearLayout scrollContent = (LinearLayout) mScroll.findViewById(R.id.LinearLayout1);
			View groupBtn = scrollContent.findViewById(groupId);
			TextView tvBadge = (TextView) groupBtn.findViewById(R.id.textView1);
			tvBadge.setText(NumberFormat.getInstance().format(requireAmount - totalQty));
		}
		
		@SuppressLint("NewApi")
		private void createSetGroupButton(){
			List<Products.ProductComponentGroup> productCompGroupLst;
			productCompGroupLst = ((ProductSetActivity) getActivity()).mProduct.listProductComponentGroup(mProductId);
			if(productCompGroupLst != null){
				final LinearLayout scrollContent = (LinearLayout) mScroll.findViewById(R.id.LinearLayout1);
				for(int i = 0; i < productCompGroupLst.size(); i++){
					final Products.ProductComponentGroup pCompGroup = productCompGroupLst.get(i);
					LayoutInflater inflater = (LayoutInflater)
							getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View setGroupView = inflater.inflate(R.layout.set_group_button_layout, null);
					setGroupView.setId(pCompGroup.getProductGroupId());
					TextView tvGroupName = (TextView) setGroupView.findViewById(R.id.textView2);
					TextView tvBadge = (TextView) setGroupView.findViewById(R.id.textView1);
					tvGroupName.setText(pCompGroup.getGroupName());
					
					if(pCompGroup.getGroupNo() == 0){
						setGroupView.setEnabled(false);
						if(((ProductSetActivity) getActivity()).mTrans.checkAddedOrderSet(mTransactionId, 
								mOrderDetailId, pCompGroup.getProductGroupId()) == 0){
							List<Products.ProductComponent> pCompLst = 
									((ProductSetActivity) getActivity()).mProduct.listProductComponent(pCompGroup.getProductGroupId());
							if(pCompLst != null){
								for(Products.ProductComponent pComp : pCompLst){
									((ProductSetActivity) getActivity()).mTrans.addOrderSet(mTransactionId, mOrderDetailId, pComp.getProductId(), 
											pCompGroup.getChildProductAmount() > 0 ? pCompGroup.getChildProductAmount() : 1,
													pComp.getFlexibleProductPrice() > 0 ? pComp.getFlexibleProductPrice() : 0.0d,
															pCompGroup.getProductGroupId(), pCompGroup.getRequireAmount());
								}
							}
						}
					}else{
						setGroupView.setOnClickListener(new OnClickListener(){
	
							@Override
							public void onClick(View v) {
								mProductCompLst = ((ProductSetActivity) getActivity()).mProduct.listProductComponent(pCompGroup.getProductGroupId());
								
								// i use Products.ProductGroupId instead ProductComponent.PGroupId
								SetItemAdapter adapter = new SetItemAdapter(
										pCompGroup.getProductGroupId(), pCompGroup.getRequireAmount());
								mGvSetItem.setAdapter(adapter);
	
								v.setSelected(true);
								for(int j = 0; j < scrollContent.getChildCount(); j++){
									View child = scrollContent.getChildAt(j);
									if(child.getId() != pCompGroup.getProductGroupId()){
										child.setSelected(false);
									}
								}
							}
							
						});	
						if(i == 0){
							try {
								setGroupView.callOnClick();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if(pCompGroup.getRequireAmount() > 0){
						tvBadge.setVisibility(View.VISIBLE);
					}else{
						tvBadge.setVisibility(View.GONE);
					}
					scrollContent.addView(setGroupView, 
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
					
					if(pCompGroup.getRequireAmount() > 0){
						updateBadge(pCompGroup.getProductGroupId(), pCompGroup.getRequireAmount());
					}
				}
			}
		}
		
		/**
		 * load order set
		 */
		private void loadOrderSet(){
			mOrderSetLst =
					((ProductSetActivity) getActivity()).mTrans.listOrderSet(mTransactionId, mOrderDetailId); 
			mOrderSetAdapter.notifyDataSetChanged();
			mLvOrderSet.setSelectedGroup(mOrderSetAdapter.getGroupCount());
			for(int i = 0; i < mOrderSetAdapter.getGroupCount(); i++){
				mLvOrderSet.expandGroup(i);
			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_product_set, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			mLvOrderSet = (ExpandableListView) view.findViewById(R.id.lvOrderSet);
			mGvSetItem = (GridView) view.findViewById(R.id.gvSetItem);
			mScroll = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView1);
			mLvOrderSet.setGroupIndicator(null);
			mLvOrderSet.setAdapter(mOrderSetAdapter);
		}

		/**
		 * @author j1tth4
		 * Order Set Adapter
		 */
		private class OrderSetAdapter extends BaseExpandableListAdapter{

			@Override
			public int getGroupCount() {
				return mOrderSetLst.size();
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return mOrderSetLst.get(groupPosition).getOrderSetDetailLst().size();
			}

			@Override
			public MPOSOrderTransaction.OrderSet getGroup(int groupPosition) {
				return mOrderSetLst.get(groupPosition);
			}

			@Override
			public MPOSOrderTransaction.OrderSet.OrderSetDetail getChild(int groupPosition, int childPosition) {
				return mOrderSetLst.get(groupPosition).getOrderSetDetailLst().get(childPosition);
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return 0;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				ViewSetGroupHolder holder;
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.order_set_template, null);
					holder = new ViewSetGroupHolder();
					holder.tvSetGroupName = (TextView) convertView.findViewById(R.id.tvSetGroupName);
					holder.btnDel = (ImageButton) convertView.findViewById(R.id.btnSetGroupDel);
					convertView.setTag(holder);
				}else{
					holder = (ViewSetGroupHolder) convertView.getTag();
				}
				final MPOSOrderTransaction.OrderSet set = mOrderSetLst.get(groupPosition);
				holder.tvSetGroupName.setText(set.getGroupName());
				holder.btnDel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(getActivity())
						.setTitle(R.string.delete)
						.setMessage(R.string.confirm_delete_item)
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((ProductSetActivity) getActivity()).mTrans.deleteOrderSetByGroup(mTransactionId, 
										mOrderDetailId, set.getProductGroupId());
								updateBadge(set.getProductGroupId(), set.getRequireAmount());
								loadOrderSet();
							}
							
						}).show();
					}
					
				});
				return convertView;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				ViewSetDetailHolder holder;
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.order_set_detail_template, null);
					holder = new ViewSetDetailHolder();
					holder.tvSetNo = (TextView) convertView.findViewById(R.id.tvSetNo);
					holder.tvSetName = (TextView) convertView.findViewById(R.id.tvSetName);
					holder.tvSetPrice = (TextView) convertView.findViewById(R.id.tvSetPrice);
					holder.txtSetQty = (EditText) convertView.findViewById(R.id.txtSetQty);
					holder.btnSetMinus = (Button) convertView.findViewById(R.id.btnSetMinus);
					holder.btnSetPlus = (Button) convertView.findViewById(R.id.btnSetPlus);
					convertView.setTag(holder);
				}else{
					holder = (ViewSetDetailHolder) convertView.getTag();
				}
				final MPOSOrderTransaction.OrderSet setGroup = mOrderSetLst.get(groupPosition);
				final MPOSOrderTransaction.OrderSet.OrderSetDetail detail = 
						mOrderSetLst.get(groupPosition).getOrderSetDetailLst().get(childPosition);
				holder.tvSetNo.setText(String.valueOf(childPosition + 1) + ".");
				holder.tvSetName.setText(detail.getProductName());
				holder.txtSetQty.setText(((ProductSetActivity) getActivity()).mFormat.qtyFormat(detail.getOrderSetQty()));
				holder.tvSetPrice.setText(((ProductSetActivity) getActivity()).mFormat.currencyFormat(detail.getProductPrice()));
				holder.btnSetMinus.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						double qty = detail.getOrderSetQty();
						if(qty > 0){
							if(--qty == 0){
								new AlertDialog.Builder(getActivity())
								.setTitle(R.string.delete)
								.setMessage(R.string.confirm_delete_item)
								.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								}).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										((ProductSetActivity) getActivity()).mTrans.deleteOrderSet(
												mTransactionId, mOrderDetailId, detail.getOrderSetId());
										updateBadge(setGroup.getProductGroupId(), setGroup.getRequireAmount());
										loadOrderSet();
									}
								}).show();
							}else{
								detail.setOrderSetQty(qty);
								((ProductSetActivity) getActivity()).mTrans.updateOrderSet(mTransactionId, 
										mOrderDetailId, detail.getOrderSetId(), detail.getProductId(), qty);
								updateBadge(setGroup.getProductGroupId(), setGroup.getRequireAmount());
								mOrderSetAdapter.notifyDataSetChanged();
							}
						}
					}
					
				});
				holder.btnSetPlus.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						double qty = detail.getOrderSetQty();
						if(setGroup.getRequireAmount() > 0){
							// count total group qty from db
							double totalQty = ((ProductSetActivity) getActivity()).mTrans.getOrderSetTotalQty(
									mTransactionId, mOrderDetailId, setGroup.getProductGroupId());
							if(totalQty < setGroup.getRequireAmount()){
								detail.setOrderSetQty(++qty);
								((ProductSetActivity) getActivity()).mTrans.updateOrderSet(mTransactionId, 
										mOrderDetailId, detail.getOrderSetId(), detail.getProductId(), qty);
								updateBadge(setGroup.getProductGroupId(), setGroup.getRequireAmount());
								mOrderSetAdapter.notifyDataSetChanged();
							}
						}else{
							detail.setOrderSetQty(++qty);
							((ProductSetActivity) getActivity()).mTrans.updateOrderSet(mTransactionId, 
									mOrderDetailId, detail.getOrderSetId(), detail.getProductId(), qty);
							updateBadge(setGroup.getProductGroupId(), setGroup.getRequireAmount());
							mOrderSetAdapter.notifyDataSetChanged();
						}
					}
					
				});
				return convertView;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return false;
			}

			private class ViewSetGroupHolder{
				TextView tvSetGroupName;
				ImageButton btnDel;
			}
			
			private class ViewSetDetailHolder{
				TextView tvSetNo;
				TextView tvSetName;
				TextView tvSetPrice;
				EditText txtSetQty;
				Button btnSetMinus;
				Button btnSetPlus;
			}
		}
		
		/**
		 * @author j1tth4
		 * set menu item adapter
		 */
		public class SetItemAdapter extends BaseAdapter{
			
			private int mPcompGroupId;
			private double mRequireAmount;
			
			private ImageLoader mImgLoader;
			
			/**
			 * @param pcompGroupId
			 * @param requireAmount
			 */
			public SetItemAdapter(int pcompGroupId, double requireAmount){
				mPcompGroupId = pcompGroupId;
				mRequireAmount = requireAmount;
				
				mImgLoader = new ImageLoader(getActivity(), 0,
						Utils.IMG_DIR, ImageLoader.IMAGE_SIZE.SMALL);
			}

			@Override
			public int getCount() {
				return mProductCompLst != null ? mProductCompLst.size() : 0;
			}

			@Override
			public Products.ProductComponent getItem(int position) {
				return mProductCompLst.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final MenuItemViewHolder holder;
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater)
							getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.menu_template, null);
					holder = new MenuItemViewHolder();
					holder.tvMenu = (TextView) convertView.findViewById(R.id.textViewMenuName);
					holder.tvPrice = (TextView) convertView.findViewById(R.id.textViewMenuPrice);
					holder.imgMenu = (ImageView) convertView.findViewById(R.id.imageViewMenu);
					convertView.setTag(holder);
				}else{
					holder = (MenuItemViewHolder) convertView.getTag();
				}
				
				final Products.ProductComponent pComp = mProductCompLst.get(position);
				holder.tvMenu.setText(pComp.getProductName());
				holder.tvPrice.setText(((ProductSetActivity) getActivity()).mFormat.currencyFormat(pComp.getFlexibleProductPrice()));
				if(pComp.getFlexibleProductPrice() > 0)
					holder.tvPrice.setVisibility(View.VISIBLE);
				else
					holder.tvPrice.setVisibility(View.GONE);

				if(Utils.isShowMenuImage(getActivity())){
					mImgLoader.displayImage(Utils.getImageUrl(
							getActivity().getApplicationContext()) + pComp.getImgUrl(), holder.imgMenu);
				}
				
				convertView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						double price = pComp.getFlexibleIncludePrice() == 1 ? 
								pComp.getFlexibleProductPrice() : 0;
						if(mRequireAmount > 0){
							// count total group qty from db
							double totalQty = ((ProductSetActivity) getActivity()).mTrans.getOrderSetTotalQty(
									mTransactionId, mOrderDetailId, mPcompGroupId);
							
							if(totalQty < mRequireAmount){
								((ProductSetActivity) getActivity()).mTrans.addOrderSet(mTransactionId, mOrderDetailId, pComp.getProductId(), 
										1, price, mPcompGroupId, mRequireAmount);
								updateBadge(mPcompGroupId, mRequireAmount);
							}
						}else{
							((ProductSetActivity) getActivity()).mTrans.addOrderSet(mTransactionId, mOrderDetailId, pComp.getProductId(), 
									1, price, mPcompGroupId, mRequireAmount);
						}
						loadOrderSet();
					}
					
				});
				return convertView;
			}
		}
	}
}
