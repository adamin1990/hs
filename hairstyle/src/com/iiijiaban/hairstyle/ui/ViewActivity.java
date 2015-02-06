package com.iiijiaban.hairstyle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.iiijiaban.u56player.R;

public class ViewActivity extends SherlockActivity {
    private MenuItem collect;
    private MenuItem share;
    private ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		image = (ImageView) findViewById(R.id.imageView);
		init();
	}
	private void init(){
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.viewactivityactionbar, menu);
		collect = (MenuItem) menu.findItem(R.id.collect);
		share = (MenuItem) menu.findItem(R.id.share);
		collect.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return false;
			}
			
		});
		share.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getTitle()));
				return false;
			}
			
		});
		
		return super.onCreateOptionsMenu(menu);
	}

}
