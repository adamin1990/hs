package com.iiijiaban.hairstyle.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.ui.LocalImageDetailActivity;
import com.iiijiaban.hairstyle.ui.LocalImagePagerActivity;
import com.iiijiaban.hairstyle.ui.LocalImgActivity;
import com.iiijiaban.hairstyle.utils.ScaleImageView;
import com.iiijiaban.hairstyle.utils.WidgetUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

public class LocalimgFragment extends SherlockFragment {
	private StaggeredGridView sgv;
	private DisplayImageOptions options;
	public ImageLoader loader = ImageLoader.getInstance();
	private Madapter adapter;
	private ArrayList<String> adapterlists;
	private String localpicturepath = Environment.getExternalStorageDirectory()
			+ "/Hairstyle/download/";

	public static final SparseArray<Double> common_sPositionHeightRatios = new SparseArray<Double>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.staggergrid, container, false);
		sgv = (StaggeredGridView) view.findViewById(R.id.staggeredGridView1);
		sgv.setPadding(15, 0, 15, 0);
//		sgv.setSelector(R.drawable.griditemselector);
		sgv.setDrawSelectorOnTop(true);
		sgv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DynamicHeightImageView imageview = (DynamicHeightImageView) view.findViewById(R.id.fuckimage);
				WidgetUtils.setImageColor(imageview);
				Intent intent = new Intent(getActivity(),LocalImageDetailActivity.class);
				intent.putExtra("imagePosition", position);
				startActivity(intent);
			}
		});
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.hs2)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		setData();
		adapter = new Madapter(adapterlists);
		sgv.setAdapter(adapter);
		return view;
	}

	

	public void setData() {
		adapterlists = new ArrayList<String>();
		File filedir = new File(localpicturepath);
		if (!filedir.exists()) {
			filedir.mkdirs();
		}
		File[] files = filedir.listFiles();
		for (File file : files) {
			try {
				String path1 = file.getPath();
				adapterlists.add(path1);
			} catch (Exception e) {
				System.out.println(e.getMessage().toString());
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setData();
		adapter.updatedata(adapterlists);
	}

	
	private class Madapter extends BaseAdapter {
		private ArrayList<String> duitangs;

		private class Viewholder {
			public TextView text;
			public DynamicHeightImageView image;
		}

		public void updatedata(List<String> result) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final Viewholder viewholder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.fuckgrid, null);
				viewholder = new Viewholder();
				viewholder.image = (DynamicHeightImageView) view
						.findViewById(R.id.fuckimage);
				viewholder.text = (TextView) view.findViewById(R.id.fucktext);
				view.setTag(viewholder);
			} else
				viewholder = (Viewholder) view.getTag();
			List<Integer> colors = null;
			colors = new ArrayList<Integer>();
			colors.add(R.color.a);
			colors.add(R.color.b);
			colors.add(R.color.c);
			colors.add(R.color.d);
			Random random = new Random();
			viewholder.text.setVisibility(View.GONE);
			loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			String url = "file:///" + duitangs.get(position);
			loader.displayImage(url, viewholder.image,options, new ImageLoadingListener() {
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							// TODO 获取图片的位置，并设置宽高比
							DynamicHeightImageView myview = (DynamicHeightImageView) arg1;
							double ratio = common_sPositionHeightRatios.get(
									position, 0.0);
							if (ratio == 0) {
								ratio = (double) arg2.getHeight()
										/ (double) arg2.getWidth();
								if (ratio < 0.5) {
									ratio = 0.5;
								}
								common_sPositionHeightRatios.append(position,
										ratio);
								myview.setHeightRatio(ratio);
							}
						}
						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {

						}
					});
			double ratio = common_sPositionHeightRatios.get(position, 0.0);
			if (ratio != 0) {
				viewholder.image.setHeightRatio(common_sPositionHeightRatios
						.get(position, 0.0));
			} 
			return view;
		}
	}
}
