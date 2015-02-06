package com.iiijiaban.hairstyle.ui;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.utils.HackyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.polites.android.GestureImageView;
import com.squareup.picasso.Picasso;
 
public class LocalImagePagerActivity extends Activity{

	private static ViewPager pager;
	private ProgressDialog mSaveDialog = null; 
	private DisplayImageOptions options; 
	private ImageButton loadbutton;
	private ImageButton leftbutton;
	private ImageButton rightbutton;
	private static ArrayList<String> localpic;
	private String fileName; 
	private static int pagerPosition; 
	public ImageLoader loader = ImageLoader.getInstance();
	private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/Hairstyle/download/";  
	public static ImagePagerAdapter imagePagerAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_image_pager);
		Bundle bundle = getIntent().getExtras(); 
		pagerPosition = bundle.getInt("imagePosition", 0); 
		setData();
		pager= new HackyViewPager(this); 
		
		//获取每个图片的position 
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true).build();
		pager = (ViewPager) findViewById(R.id.paper);
		imagePagerAdapter = new ImagePagerAdapter(localpic);
		pager.setAdapter(imagePagerAdapter);
		pager.setCurrentItem(pagerPosition);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				pagerPosition = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		loadbutton=(ImageButton)findViewById(R.id.deletebtn);
	    leftbutton=(ImageButton)findViewById(R.id.left); 
	    rightbutton=(ImageButton)findViewById(R.id.right); 
	    //需要设置监听
	    leftbutton.setOnClickListener(new OnClickListener() {
			@Override
 
			public void onClick(View v) {
			
				pager.arrowScroll(v.FOCUS_LEFT);
					System.out.println("左边position的位置："+pagerPosition);
					pagerPosition=pagerPosition-1;
					if(pagerPosition<=0)
					{
						pagerPosition=0;
						pager.setCurrentItem(0);
					}			 
					else{
				pager.setCurrentItem(pagerPosition,true);}}
	 
		});
	    leftbutton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.left1);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                 v.setBackgroundResource(R.drawable.left);     
	         }     
				return false;
			}
		});
	    rightbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 
				pager.arrowScroll(v.FOCUS_RIGHT);
				pagerPosition++;
				System.out.println("右边position的位置："+pagerPosition);
				 if(pagerPosition>=0&&pagerPosition<=localpic.size()-1){
					 pager.setCurrentItem(pagerPosition);	
				}
				else if(pagerPosition>localpic.size()-1){
					pagerPosition=localpic.size()-1;
					Toast.makeText(LocalImagePagerActivity.this,"NO pictures", 2000).show();
				} 
			}
		});
	    rightbutton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.right1);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                 v.setBackgroundResource(R.drawable.right);     
	         }     				return false;
			}
		});
	    loadbutton.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) { 
				AlertDialog dialog = new AlertDialog.Builder(LocalImagePagerActivity.this).setTitle("删除图片").setMessage("确认删除？")
						.setPositiveButton("确认", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								File file = new File(localpic.get(pagerPosition));
								file.delete(); 
								finish();
							}
						})
						.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) { 
							}
				}).show();  
			}
		});
	    loadbutton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.deleting);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.fredelete);
				}
				return false;
			}
		});
	} 
	public void setData(){   
		localpic = new ArrayList<String>();
		File filedir = new File(ALBUM_PATH);
		if(!filedir.exists()){
			filedir.mkdirs();
		}
		File [] files = filedir.listFiles();
		for(File file : files){
			 try {
				 String path1 = file.getPath();
				 String path2 = file.getAbsolutePath(); 
				 File file2 = new File(path1);  
				 localpic.add(path1);
			}catch ( Exception e) { 
				System.out.println(e.getMessage().toString());
			} 
		} 
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	private class ImagePagerAdapter extends PagerAdapter {
		private List<String>  images; 
		ImagePagerAdapter(List<String> imgs) { 
			this.images = imgs ;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		} 
		public int getCount() {
			return images.size();
		}  
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(LocalImagePagerActivity.this,R.layout.local_item_pager_image, null);
			final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading); 
			if(images.get(position).equals("")||images.get(position)==null){
				position = 0;
			} 
//			loader.init(ImageLoaderConfiguration.createDefault(LocalImagePagerActivity.this));
			String url = "file://"+images.get(position);
//			loader.displayImage(url, imageView,	options); 
//			((ViewPager) container).addView(view, 0);
			Picasso.with(LocalImagePagerActivity.this).load(new File(images.get(position))).noFade().into(imageView);
			
			((ViewPager) container).addView(view);
			return view;
		}  
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		} 
	} 
}
 
 
 
 
 
 
