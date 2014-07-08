package com.synature.mpos;

import java.util.List;

import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MenuComment;
import com.synature.mpos.database.Transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuCommentFragment extends DialogFragment{
	
	/**
	 * selected order position
	 */
	private int mPosition;
	
	private int mTransactionId;
	private int mOrderDetailId;
	private String mMenuName;
	private String mOrderComment;
	
	private Formater mFormat;
	private Transaction mTrans;
	private MenuComment mComment;
	private List<MenuComment.CommentGroup> mCommentGroupLst;
	private List<MenuComment.Comment> mCommentLst;
	private MenuCommentAdapter mCommentAdapter;
	private ArrayAdapter<MenuComment.CommentGroup> mCommentGroupAdapter;

	private OnCommentDismissListener mListener;
	
	private Spinner mSpCommentGroup;
	private ListView mLvComment;
	private EditText mTxtComment;
	
	public static MenuCommentFragment newInstance(int position, int transactionId, 
			int orderDetailId, String menuName, String orderComment){
		MenuCommentFragment f = new MenuCommentFragment();
		Bundle b = new Bundle();
		b.putInt("position", position);
		b.putInt("transactionId", transactionId);
		b.putInt("orderDetailId", orderDetailId);
		b.putString("menuName", menuName);
		b.putString("orderComment", orderComment);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt("position");
		mTransactionId = getArguments().getInt("transactionId");
		mOrderDetailId = getArguments().getInt("orderDetailId");
		mMenuName = getArguments().getString("menuName");
		mOrderComment = getArguments().getString("orderComment");
		
		mFormat = new Formater(getActivity());
		mTrans = new Transaction(getActivity());
		mComment = new MenuComment(getActivity());
		mCommentLst = mComment.listMenuComment();
		mCommentGroupLst = mComment.listMenuCommentGroup();
		MenuComment.CommentGroup commentGroup = 
				new MenuComment.CommentGroup();
		commentGroup.setCommentGroupId(0);
		commentGroup.setCommentGroupName("-- ALL --");
		mCommentGroupLst.add(0, commentGroup);
		
		// create order comment temp
		mTrans.createOrderCommentTemp(mTransactionId, mOrderDetailId);
	}

	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
		if(activity instanceof OnCommentDismissListener){
			mListener = (OnCommentDismissListener) activity;
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = (LayoutInflater)
				getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.menu_comment, null, false);
		mSpCommentGroup = (Spinner) view.findViewById(R.id.spCommentGroup);
		mLvComment = (ListView) view.findViewById(R.id.lvComment);
		mTxtComment = (EditText) view.findViewById(R.id.txtComment);
		mTxtComment.setText(mOrderComment);
		setupCommentGroupAdapter();
		setupCommentAdapter();
		mSpCommentGroup.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				MenuComment.CommentGroup commentGroup = 
						(MenuComment.CommentGroup) parent.getItemAtPosition(position);
				if(commentGroup.getCommentGroupId() == 0)
					mCommentLst = mComment.listMenuComment();
				else
					mCommentLst = mComment.listMenuComment(commentGroup.getCommentGroupId());
				mCommentAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getActivity().getString(R.string.menu_comment) + ": " + mMenuName);
		builder.setView(view);
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!TextUtils.isEmpty(mTxtComment.getText())){
					mTrans.updateOrderComment(mTransactionId, mOrderDetailId, 
							mTxtComment.getText().toString());
				}
				mTrans.confirmOrderComment(mTransactionId, mOrderDetailId);
				mListener.onDismiss(mPosition, mOrderDetailId);
			}
		});
		return builder.create();
	}

	private void setupCommentGroupAdapter(){
		if(mCommentGroupAdapter == null){
			mCommentGroupAdapter = 
					new ArrayAdapter<MenuComment.CommentGroup>(getActivity(), 
							android.R.layout.simple_spinner_dropdown_item, 
							mCommentGroupLst);
			mSpCommentGroup.setAdapter(mCommentGroupAdapter);
		}
		mCommentGroupAdapter.notifyDataSetChanged();
	}
	
	private void setupCommentAdapter(){
		if(mCommentAdapter == null){
			mCommentAdapter = new MenuCommentAdapter();
			mLvComment.setAdapter(mCommentAdapter);
		}
		mCommentAdapter.notifyDataSetChanged();
	}
	
	private class MenuCommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mCommentLst.size();
		}

		@Override
		public MenuComment.Comment getItem(int position) {
			return mCommentLst.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.comment_list_template, parent, false);
				holder.chkComment = (CheckBox) convertView.findViewById(R.id.chkComment);
				holder.tvCommentNo = (TextView) convertView.findViewById(R.id.tvCommentNo);
				holder.tvCommentName = (TextView) convertView.findViewById(R.id.tvCommentName);
				holder.tvCommentPrice = (TextView) convertView.findViewById(R.id.tvCommentPrice);
				holder.commentInfoContent = (LinearLayout) convertView.findViewById(R.id.commentInfoContent);
				holder.txtCommentQty = (EditText) convertView.findViewById(R.id.txtCommentQty);
				holder.btnCommentMinus = (Button) convertView.findViewById(R.id.btnCommentMinus);
				holder.btnCommentPlus = (Button) convertView.findViewById(R.id.btnCommentPlus);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final MenuComment.Comment comment = mCommentLst.get(position);
			// get order comment if this comment already add to db
			// and update to view
			MenuComment.Comment orderComment = mTrans.getOrderComment(
					mTransactionId, mOrderDetailId, comment.getCommentId());
			// check added this comment
			if(orderComment.getCommentId() != 0){
				comment.setCommentQty(orderComment.getCommentQty());
				comment.setSelected(true);
				holder.btnCommentMinus.setEnabled(true);
				holder.btnCommentPlus.setEnabled(true);
			}else{
				holder.btnCommentMinus.setEnabled(false);
				holder.btnCommentPlus.setEnabled(false);
			}
			holder.chkComment.setChecked(comment.isSelected());
			holder.tvCommentNo.setText(String.valueOf((position + 1)) + ".");
			holder.tvCommentName.setText(comment.getCommentName());
			holder.tvCommentPrice.setText(mFormat.currencyFormat(comment.getCommentPrice()));
			holder.txtCommentQty.setText(mFormat.qtyFormat(comment.getCommentQty() == 0 ? 1 : comment.getCommentQty()));
			if(comment.getCommentPrice() > 0){
				holder.btnCommentMinus.setVisibility(View.VISIBLE);
				holder.btnCommentPlus.setVisibility(View.VISIBLE);
				holder.txtCommentQty.setVisibility(View.VISIBLE);
				holder.tvCommentPrice.setVisibility(View.VISIBLE);
			}else{
				holder.btnCommentMinus.setVisibility(View.GONE);
				holder.btnCommentPlus.setVisibility(View.GONE);
				holder.txtCommentQty.setVisibility(View.GONE);
				holder.tvCommentPrice.setVisibility(View.GONE);
			}
			holder.btnCommentMinus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					double qty = comment.getCommentQty() == 0 ? 1 : comment.getCommentQty();
					if(--qty > 0){
						comment.setCommentQty(qty);
						mTrans.updateOrderComment(mTransactionId, mOrderDetailId, comment.getCommentId(), qty);
						mCommentAdapter.notifyDataSetChanged();
					}
				}
				
			});
			holder.btnCommentPlus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					double qty = comment.getCommentQty() == 0 ? 1 : comment.getCommentQty();
					comment.setCommentQty(++qty);
					mTrans.updateOrderComment(mTransactionId, mOrderDetailId, comment.getCommentId(), qty);
					mCommentAdapter.notifyDataSetChanged();
				}
				
			});
			holder.commentInfoContent.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(comment.isSelected()){
						comment.setSelected(false);
						mTrans.deleteOrderCommentTemp(mTransactionId, mOrderDetailId, comment.getCommentId());
					}else{
						comment.setSelected(true);
						mTrans.addOrderComment(mTransactionId, mOrderDetailId, comment.getCommentId(), 
								1, comment.getCommentPrice());
					}
					mCommentAdapter.notifyDataSetChanged();
				}
				
			});
			return convertView;
		}
		
		private class ViewHolder{
			CheckBox chkComment;
			TextView tvCommentNo;
			TextView tvCommentName;
			TextView tvCommentPrice;
			LinearLayout commentInfoContent;
			EditText txtCommentQty;
			Button btnCommentMinus;
			Button btnCommentPlus;
		}	
	}
	
	public static interface OnCommentDismissListener{
		void onDismiss(int position, int orderDetailId);
	}
}
