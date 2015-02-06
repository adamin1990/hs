package com.iiijiaban.hairstyle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.fragment.MainFragment;

public class MainActivity extends SherlockFragmentActivity{
	private static final int CONTENT_VIEW_ID = 11111;

	private MenuItem collection;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		FrameLayout frame = new FrameLayout(this);
		frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		if (arg0 == null) {
			setInitialFragment();
		}
	}
	/**
	 * ÔOÖÃÖ÷fragment
	 */
	private void setInitialFragment() {

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(CONTENT_VIEW_ID, MainFragment.newInstance()).commit();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflat = new MenuInflater(this);
		inflat.inflate(R.menu.actionbarmenu, menu);
		

		collection = (MenuItem) menu.findItem(R.id.collecttions);

		
		collection.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
				startActivity(intent);
				return false;
			}
			
		});
		return super.onCreateOptionsMenu(menu);
	}

}
