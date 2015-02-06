package com.iiijiaban.hairstyle.ui;

import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.iiijiaban.u56player.R;
import com.polites.android.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ScaleImageActivity extends SherlockActivity {

	private String url;
	private GestureImageView image;
	private ProgressBar loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scaleimageactivity);
		loading = (ProgressBar) findViewById(R.id.loading);
		loading.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getApplicationContext()).build());
		image = (GestureImageView) findViewById(R.id.scaleimage);
		url = getIntent().getStringExtra("url");
		new Thread(new Runnable(){

			@Override
			public void run() {
				Picasso.with(getApplicationContext()).load(url).config(Config.RGB_565).noFade()
				.into(image, new Callback(){

					@Override
					public void onError() {
						Toast.makeText(getApplicationContext(), "Õº∆¨º”‘ÿ ß∞‹", 2000);
					}

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
					}
					
				});
				
			}
			
		}).run();
	}
	
	

}
