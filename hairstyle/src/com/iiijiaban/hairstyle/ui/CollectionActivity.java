package com.iiijiaban.hairstyle.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.iiijiaban.com.db.DbHelper;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.utils.ScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class CollectionActivity extends  SherlockActivity{
	public static List<Map<String, String>> lists;//装有图片的地址 描述 
	private StaggeredGridView sgv;
	private DisplayImageOptions options; 
	public ImageLoader loader=ImageLoader.getInstance();
	private MyAdapter adapter;
	DbHelper dh;
	public static final SparseArray<Double> common_sPositionHeightRatios = new SparseArray<Double>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staggergrid);
	
		LoadData();
		initView();
		
	}
     Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			dh=(DbHelper) msg.obj;
			LoadData();
			initView();
			adapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}
    	 
     };
	@SuppressLint("NewApi") private void initView() {
        getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		sgv=(StaggeredGridView)findViewById(R.id.staggeredGridView1);
		sgv.setPadding(15, 0, 15, 0);
		sgv.setSelector(R.drawable.griditemselector);
		sgv.setDrawSelectorOnTop(true);
		adapter=new MyAdapter(getApplicationContext(), lists);
		sgv.setAdapter(adapter);
    sgv.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			        Intent intent=new Intent();
			        intent.putExtra("url", lists.get(position).get("isrc"));
			        intent.putExtra("msg", lists.get(position).get("msg"));
			        intent.setClass(getApplicationContext(), CollectionDetailActivity.class);
			        startActivity(intent);
		}
	});
    sgv.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog log =new AlertDialog.Builder(CollectionActivity.this).setMessage("删除本条？")
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).setPositiveButton("删除", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 dh=new DbHelper(getApplicationContext());
							dh.del(lists.get(position).get("isrc"));
							Message message=Message.obtain();
							message.obj=dh;
							handler.sendMessage(message);
							
						}
					}).create();
			log.show();
			return false;
		}
	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.collection,  menu);
		MenuItem  clear=(MenuItem)menu.findItem(R.id.clearall);
		clear.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				AlertDialog log =new AlertDialog.Builder(CollectionActivity.this).setMessage("确定要清除所有收藏吗？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						}).setPositiveButton("删除", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dh=new DbHelper(getApplicationContext());
								dh.delAll();
								Message message=Message.obtain();
								message.obj=dh;
								handler.sendMessage(message);
								
							}
						}).create();
				log.show();
				
                 Toast.makeText(getApplicationContext(), "清除所有", 2000).show();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
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

	private void LoadData() {
		 dh = new DbHelper(this);
		Cursor c = dh.query();
		/**
		 * lists=new ArrayList<Map<String,String>>();
		 * 不能加到while循环内 否则 next一次初始化一次
		 */
		lists=new ArrayList<Map<String,String>>();
		while (c.moveToNext()) {
			
			Map<String, String> map = new HashMap<String, String>();
			String s = c.getString(c.getColumnIndex("isrc"));
			String s1 = c.getString(c.getColumnIndex("msg"));
			map.put("isrc", s);
			map.put("msg", s1);
			lists.add(map);
		}
		c.close();		
	}
	public class MyAdapter extends BaseAdapter{
      private ArrayList<Map<String, String>> ts;
      private LayoutInflater inflater;
  	public MyAdapter(Context context, List<Map<String, String>> list1) {
		super();
		this.ts = (ArrayList<Map<String, String>>) list1;
		inflater = LayoutInflater.from(context);
	}
		@Override
		public int getCount() {
			return ts.size();
		}

		@Override
		public Object getItem(int position) {
			return ts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final Viewholder holder;
			if(convertView==null){
				view=getLayoutInflater().inflate(R.layout.fuckgrid, null);
				holder=new Viewholder();
				holder.image=(ImageView)view.findViewById(R.id.fuckimage);
				holder.text=(TextView)view.findViewById(R.id.fucktext);
				view.setTag(holder);
			}
			else 
				holder=(Viewholder)view.getTag();
			   List<Integer> colors=null;
			colors=new ArrayList<Integer>();
			colors.add(R.color.a);
			colors.add(R.color.b);
			colors.add(R.color.c);
			colors.add(R.color.d);
			Random  random=new Random();
			holder.text.setText(ts.get(position).get("msg"));
			loader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
			loader.displayImage(ts.get(position).get("isrc"), holder.image,options,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

					// TODO 获取图片的位置，并设置宽高比
					DynamicHeightImageView myview = (DynamicHeightImageView) view;
					double newratio = common_sPositionHeightRatios.get(position, 0.0);
					if (newratio == 0) {
						newratio = (double) loadedImage.getHeight() / (double) loadedImage.getWidth();
						if (newratio > 3.0) {
							newratio = 3.0;
						}
						myview.setHeightRatio(newratio);
						common_sPositionHeightRatios.append(position, newratio);
					} 
				
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
			System.out.print("");
			view.setBackgroundResource(colors.get(random.nextInt(4)));
			return view;
		}
		  private class Viewholder{
			   public TextView text;
				public ImageView image;
				public ProgressBar bar;
		   }
		
	}
	

}
