package com.iiijiaban.hairstyle.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iiijiaban.com.db.DbHelper;
import com.iiijiaban.u56player.R;
import com.iiijiaban.menu.RayMenu;
import com.polites.android.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CollectionDetailActivity extends Activity{

	public ArrayList<String> list;
	GestureImageView image;
	String url;
	String msg;
//	 private ProgressBar pbloading;
	 private static final int[] ITEM_DRAWABLES = {R.drawable.ic_collection,R.drawable.ic_share};
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedetail);
		RayMenu ray=(RayMenu)findViewById(R.id.arcmenu);
		initmenu(ray,ITEM_DRAWABLES);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		image=(GestureImageView)findViewById(R.id.imagedetail);
//		pbloading=(ProgressBar)findViewById(R.id.loadingprogress);
//		pbloading.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getApplicationContext()).build());
       Intent intent=getIntent();
       url=intent.getStringExtra("url");
       msg=intent.getStringExtra("msg");
       getActionBar().setTitle(msg);

       new Thread(new Runnable() {
		
		@Override
		public void run() {

Picasso.with(getApplicationContext()).load(url).config(Config.RGB_565).noFade().into(image,new Callback() {
	
	@Override
	public void onSuccess() {
//		pbloading.setVisibility(View.GONE);
	}
	
	@Override
	public void onError() {
		Toast.makeText(getApplicationContext(), "加载图片失败！", 2000).show();
	}
});
		}
	}).run();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		  switch(item.getItemId()){
		  case android.R.id.home:
			  finish();
			  break;
			  default:
				  break;
				  
			  
		  }
		return super.onOptionsItemSelected(item);
	}
	
	public void scale_onclick(View v){
		Intent intent = new Intent(CollectionDetailActivity.this, ScaleImageActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	}
/**
 * 初始化底部菜单 
 * @param ray
 * @param itemDrawables
 */
	private void initmenu(RayMenu ray, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(getApplicationContext());
            item.setImageResource(itemDrawables[i]);

            final int position = i;		
            ray.addItem(item, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch(position){
					case 0:
						DbHelper dbh=new DbHelper(getApplicationContext());
						if(dbh.query().getCount()!=0){
							list=new ArrayList<String>();
							Cursor c=dbh.query();
							while(c.moveToNext()){
								String s=c.getString(c.getColumnIndex("isrc"));
								list.add(s);
							}
							c.close();
							if(list.contains(url)){
								Toast.makeText(CollectionDetailActivity.this,
										"已经收藏！",
										1000).show();
								dbh.close();
							}
                               else
								{
									ContentValues values=new ContentValues();
									values.put("isrc", url);
									values.put("msg", msg);
									dbh.insert(values);
									Toast.makeText(getApplicationContext(), "收藏成功！", 1000).show();
									break;
								}
//							}
						}
						else{
							ContentValues values=new ContentValues();
							values.put("isrc", url);
							values.put("msg", msg);
							dbh.insert(values);
							Toast.makeText(getApplicationContext(), "收藏成功！", 1000).show();
						}
						
						
					  break;
					case 1:
						Intent i = null;
					    String msg ="http://appstore.huawei.com/app/C10208485";

					    i = new Intent(Intent.ACTION_SEND);
					    i.setType("text/plain");

					    i.putExtra(Intent.EXTRA_TEXT, msg);
					    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.abs__action_bar_home_description) + " \""
					            + "Share title"+ "\" : " + getResources().getString(R.string.app_name));

					    startActivity(i);
					}
				}
			});
	}
	}



}
