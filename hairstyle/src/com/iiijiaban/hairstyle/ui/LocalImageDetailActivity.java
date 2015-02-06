package com.iiijiaban.hairstyle.ui;

import java.io.File;
import java.util.ArrayList;





import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.utils.CommonPagerAdapter;
import com.iiijiaban.menu.RayMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class LocalImageDetailActivity extends Activity { 
	 
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true).build();
	private int curpos;//当前图片在viewpager中的位置
	MenuItem                    collection; 
	private ViewPager           viewpager;
	public CommonPagerAdapter   adapter; 
	private static ArrayList<String> localpic;
	private final static String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/Hairstyle/download/";// 下载文件夹
	private static final int[] ITEM_DRAWABLES = {R.drawable.ic_share,R.drawable.ic_download};

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localimagedetail);
		RayMenu ray = (RayMenu) findViewById(R.id.arcmenu);
		viewpager = (ViewPager) findViewById(R.id.paper);
		initmenu(ray, ITEM_DRAWABLES); 
		curpos = getIntent().getIntExtra("imagePosition", 0);
		getActionBar().setHomeButtonEnabled(true); 
		getActionBar().setDisplayHomeAsUpEnabled(true); 
		getActionBar().setDisplayShowHomeEnabled(true);
		getData();
		initialAdapter();
		viewpager.setAdapter(adapter);  
		viewpager.setCurrentItem(curpos);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) { 
				curpos = arg0;
			} 
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { 
				
			} 
			@Override
			public void onPageScrollStateChanged(int arg0) {  
			}
		});
	} 
	public void initialAdapter(){
		adapter = new CommonPagerAdapter(localpic,this) {
			@Override
			public Object setinstantiateItem(View container, int position) {
				// TODO 初始化viewpager
				View view = common_inflater.inflate(R.layout.item_localimagepager, null);
				final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.image); 
				if(common_datas.get(position).equals("")||common_datas.get(position)==null){
					position = 0;
				} 
				String url = "file://"+common_datas.get(position);
				ImageLoader.getInstance().displayImage(url, imageView,options); 
				((ViewPager) container).addView(view);
				return view;
			}
		};
	}
	
	public void getData() {
		localpic = new ArrayList<String>();
		File filedir = new File(DOWNLOAD_PATH);
		if (!filedir.exists()) {
			filedir.mkdirs();
		}
		File[] files = filedir.listFiles();
		for (File file : files) {
			try {
				String path1 = file.getPath();
				localpic.add(path1);
			} catch (Exception e) {
				System.out.println(e.getMessage().toString());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflat = new MenuInflater(this);
		inflat.inflate(R.menu.actionbarmenu, menu);
		collection = (MenuItem) menu.findItem(R.id.collecttions);
		collection.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				Intent intent = new Intent(LocalImageDetailActivity.this,
						CollectionActivity.class);
				startActivity(intent); 
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
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
					case 1: 
						//TODO 删除图片
						AlertDialog dialog = new AlertDialog.Builder(LocalImageDetailActivity.this).setTitle("删除图片").setMessage("确认删除？")
								.setPositiveButton("确认", new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										File file = new File(localpic.get(curpos));
										file.delete(); 
										finish();
									}
								})
								.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) { 
									}
						}).show();   
						break;
					case 0:
						//TODO 分享图片
						Toast.makeText(getApplication(), "分享图片",	Toast.LENGTH_SHORT).show(); 
						Intent sendIntent = new Intent();
						sendIntent.setAction(Intent.ACTION_SEND); 
						sendIntent.putExtra(Intent.EXTRA_STREAM,"file://"+localpic.get(curpos));
//						sendIntent.putExtra(Intent.EXTRA_SUBJECT,"See pictute \"" + "test" + "\" on YouTube");
//						sendIntent.putExtra(Intent.EXTRA_TEXT, "嗨，发给你一张图片~"+ url);
						sendIntent.setType("image/*"); 
						startActivity(sendIntent);
						break;

					}
				}
			});
		}
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

}
