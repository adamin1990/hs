package com.iiijiaban.hairstyle.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.iiijiaban.com.db.DbHelper;
import com.iiijiaban.u56player.R;
import com.iiijiaban.menu.RayMenu;
import com.polites.android.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends Activity {
	public ArrayList<String> list;
	ImageView image;
	String url;
	String msg; 
	String rurl;//图片真正网址
	private String fileName;//图片下载后的文件名称
	private final static String DOWNLOAD_PATH  = Environment.getExternalStorageDirectory() + "/Hairstyle/download/";//下载文件夹
	final DateFormat time=DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL); //时间格式
	  private AdView mAdView;
	private ProgressBar pbloading;
	private static final int[] ITEM_DRAWABLES = {
			R.drawable.ic_collection,
			R.drawable.ic_share,
			R.drawable.ic_download,
			};

	@SuppressLint("NewApi") 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedetail);
		RayMenu ray = (RayMenu) findViewById(R.id.arcmenu);
		initmenu(ray, ITEM_DRAWABLES);
		LinearLayout layout = (LinearLayout) findViewById(R.id.detailad);
        mAdView = new AdView(this);
		mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
		mAdView.setAdSize(AdSize.BANNER);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		layout.addView(mAdView, params);
		mAdView.loadAd(new AdRequest.Builder().build());
		getActionBar().setHomeButtonEnabled(true);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getActionBar().setDisplayShowHomeEnabled(true);
		image = (ImageView) findViewById(R.id.imagedetail);
		pbloading = (ProgressBar) findViewById(R.id.loadingprogress);
		pbloading.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(
				getApplicationContext()).build());
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		msg = intent.getStringExtra("msg");
		getActionBar().setTitle(msg);
		int index = url.indexOf(".thumb");
		int isjpeg=url.indexOf(".png");
		int isgit=url.indexOf(".gif");
		String s=null;
		if(isjpeg==-1&&isgit==-1){
			s = url.substring(index, url.length() - 5);
		}else {
			s = url.substring(index, url.length() - 4);
		}
		    
		 
		rurl = url.replaceAll(s, "");

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				Picasso.with(getApplicationContext()).load(rurl)
						.config(Config.RGB_565).noFade()
						.into(image, new Callback() {
							@Override
							public void onSuccess() {
								pbloading.setVisibility(View.GONE);
							}
							@Override
							public void onError() {	
								Toast.makeText(getApplicationContext(),"加载图片失败！", 2000).show();
							}
						});
//			}
//		}).run();
//		Glide.with(this).load(rurl).into(image);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;

		}
		return super.onOptionsItemSelected(item);
	} 
	private void initmenu(RayMenu ray, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(getApplicationContext());
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			ray.addItem(item, new OnClickListener() { 
				@Override
				public void onClick(View v) {
					switch (position) {
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
							if(list.contains(rurl)){
								Toast.makeText(ImageDetailActivity.this,
										"已经收藏！",
										1000).show();
								dbh.close();
							}
                               else
								{
									ContentValues values=new ContentValues();
									values.put("isrc", rurl);
									values.put("msg", msg);
									dbh.insert(values);
									Toast.makeText(getApplicationContext(), "收藏成功！", 1000).show();
									break;
								}
						}
						else{
							ContentValues values=new ContentValues();
							values.put("isrc", rurl);
							values.put("msg", msg);
							dbh.insert(values);
							Toast.makeText(getApplicationContext(), "收藏成功！", 1000).show();
						}
						break;
					case 2:
						Toast.makeText(getApplication(), "图片下载", Toast.LENGTH_SHORT).show();
						 new Thread(new Runnable() {
								@Override
								public void run() {
									Looper.prepare();
				                	try { 
				        				URL	url = new URL(rurl);
				        					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				        	                conn.connect();
				                            conn.setConnectTimeout(5000);
				        	                InputStream in = conn.getInputStream();
				        	                Bitmap map = BitmapFactory.decodeStream(in);
				        	                File dirFile = new File(DOWNLOAD_PATH);  
				        	                if(!dirFile.exists()){  
				        	                    dirFile.mkdir();  
				        	                }
				        	                Date now =new Date(); 
				        	        		fileName=time.format(now)+".jpg";
				        	                File myCaptureFile = new File(DOWNLOAD_PATH + fileName);  
				        	                Toast.makeText(getApplication(), "下载完成!", Toast.LENGTH_SHORT).show();
				        	                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
				        	                map.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
				        	                bos.flush();  
				        	                bos.close();  
				        	                }
				                	catch (MalformedURLException e) {
				    					e.printStackTrace();
				    				} catch (IOException e) {
				    					e.printStackTrace(); 
				    				}
				                	Looper.loop();
				                	} 
							}).start();
					  break;
					case 1:

						  Intent sendIntent = new Intent();
		                    sendIntent.setAction(Intent.ACTION_SEND);
		                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "See pictute \"" + "test"+ "\" on YouTube");
		                    sendIntent.putExtra(Intent.EXTRA_TEXT, "嗨，发给你一张图片~" + rurl);
		                    sendIntent.setType("text/plain");
		                    startActivity(sendIntent);

					break;
					
					}
				}
			});
		}
	}

}
