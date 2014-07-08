package com.synature.mpos;

import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;
import com.synature.util.ImageLoader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImageViewPinchZoom extends DialogFragment{
	private String mImgName;
	private String mMenuName;
	private String mMenuPrice;
	private ImageLoader mImgLoader;
	
	public static ImageViewPinchZoom newInstance(String imgName, String menuName, String menuPrice){
		ImageViewPinchZoom frag = new ImageViewPinchZoom();
		Bundle b = new Bundle();
		b.putString("imgName", imgName);
		b.putString("menuName", menuName);
		b.putString("menuPrice", menuPrice);
		frag.setArguments(b);
		return frag;
	}

    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mImgLoader = new ImageLoader(getActivity(), 0,
				Utils.IMG_DIR, ImageLoader.IMAGE_SIZE.LARGE);
		
		mImgName = getArguments().getString("imgName");
		mMenuName = getArguments().getString("menuName");
		mMenuPrice = getArguments().getString("menuPrice");
		
		LayoutInflater inflater = (LayoutInflater)
				getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View menuImgView = inflater.inflate(R.layout.menu_image_layout, null);
		final ImageView imgMenu = (ImageView) menuImgView.findViewById(R.id.imgMenu);
		final TextView tvMenuName = (TextView) menuImgView.findViewById(R.id.textView1);
		final ProgressBar progress = (ProgressBar) menuImgView.findViewById(R.id.loadImgProgress);
		final ImageButton btnClose = (ImageButton) menuImgView.findViewById(R.id.imgBtnClose);
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
			
		});

		imgMenu.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				mImgLoader.displayImage(Utils.getImageUrl(getActivity()) + mImgName, imgMenu);
				imgMenu.setVisibility(View.VISIBLE);
				tvMenuName.setText(mMenuName + "\n" + mMenuPrice);
				progress.setVisibility(View.GONE);
			}
			
		}, 500);
		
		ImageAttacher mAttacher = new ImageAttacher(imgMenu);
        ImageAttacher.MAX_ZOOM = 2.0f; // Double the current Size
        ImageAttacher.MIN_ZOOM = 1.0f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(menuImgView);
		return builder.create();
	}
}
