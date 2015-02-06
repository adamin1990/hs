package com.iiijiaban.hairstyle.ui;
 

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;





import com.etsy.android.grid.StaggeredGridView;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.beans.DuitangInfo; 
import com.iiijiaban.hairstyle.utils.ScaleImageView;
import com.ijiaban.actionbar.pulltorefreshlib.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalImgActivity extends Activity{

	private PullToRefreshLayout ptrl;
	private StaggeredGridView sgv;
	private DisplayImageOptions options;
	public ImageLoader loader = ImageLoader.getInstance();
	private Madapter adapter;
	private ArrayList<String> adapterlists;
	private String localpicturepath = Environment.getExternalStorageDirectory() + "/Hairstyle/download/";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staggergrid);
		sgv = (StaggeredGridView)findViewById(R.id.staggeredGridView1);
		// sgv.setItemMargin(15);
		sgv.setPadding(15, 0, 15, 0);
		sgv.setSelector(R.drawable.griditemselector);
		sgv.setDrawSelectorOnTop(true);
		ptrl = (PullToRefreshLayout)findViewById(R.id.pulltorefresh);
		sgv.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LocalImgActivity.this,LocalImagePagerActivity.class); 
				ArrayList list = new ArrayList();
				list.add(adapterlists);
				intent.putExtra("imageUrls", list);
				intent.putExtra("imagePosition", position);
				startActivity(intent);
			}
		}); 
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.hs2)
			   .cacheInMemory(true)
				.cacheOnDisc(true).build();
		setData(); 
		adapter  = new Madapter(adapterlists);
		sgv.setAdapter(adapter); 
	} 

	public void setData(){   
		adapterlists = new ArrayList<String>();
		File filedir = new File(localpicturepath);
		if(!filedir.exists()){
			filedir.mkdirs();
		}
		File [] files = filedir.listFiles();
		for(File file : files){
			 try {
				 String path1 = file.getPath();
				 adapterlists.add(path1);
			}catch ( Exception e) { 
				System.out.println(e.getMessage().toString());
			} 
		} 
	}
	private class Madapter extends BaseAdapter {
		private ArrayList<String> duitangs;

		private class Viewholder {
			public TextView text;
			public ImageView image;
//			public ProgressBar bar;
		}
		public void updatedata(List<String> result){
			this.duitangs = (ArrayList<String>) result;
			this.notifyDataSetChanged();
		} 
		public Madapter(List<String> result) { 
			this.duitangs = (ArrayList<String>) result;
			
		}
		@Override
		public int getCount() {
			return duitangs.size();
		}
		@Override
		public Object getItem(int position) {
			return duitangs.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		} 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final Viewholder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.fuckgrid, null);
				holder = new Viewholder();
				holder.image = (ImageView) view.findViewById(R.id.fuckimage);
				holder.text = (TextView) view.findViewById(R.id.fucktext); 
				view.setTag(holder);
			} else
				holder = (Viewholder) view.getTag();
			List<Integer> colors = null;
			colors = new ArrayList<Integer>();
			colors.add(R.color.a);
			colors.add(R.color.b);
			colors.add(R.color.c);
			colors.add(R.color.d);
			Random random = new Random();
//			holder.text.setText(duitangs.get(position).getMsg());
			holder.text.setVisibility(View.GONE);
			loader.init(ImageLoaderConfiguration.createDefault(LocalImgActivity.this));
			String url = "file:///"+duitangs.get(position);
			loader.displayImage(url,holder.image,	options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					holder.image.setImageResource(R.drawable.hs2);					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
//			Picasso.with(getApplicationContext()).load(url).into(holder.image);
			System.out.print("");
			view.setBackgroundResource(colors.get(random.nextInt(4)));
			return view;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setData(); 
		adapter.updatedata(adapterlists);
		
	}
}
