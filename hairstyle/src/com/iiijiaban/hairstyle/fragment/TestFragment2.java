package com.iiijiaban.hairstyle.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.beans.DuitangImageBean;
import com.iiijiaban.hairstyle.beans.DuitangInfo;
import com.iiijiaban.hairstyle.dao.PictureDao;
import com.iiijiaban.hairstyle.ui.FoldingCirclesDrawable;
import com.iiijiaban.hairstyle.ui.ImageDetailActivity;
import com.iiijiaban.hairstyle.utils.Helper;
import com.ijiaban.actionbar.pulltorefreshlib.ActionBarPullToRefresh;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.actionbar.pulltorefreshlib.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TestFragment2 extends Fragment implements OnRefreshListener {
	private PullToRefreshLayout ptrl;
	private StaggeredGridView sgv; //瀑布流
	private DisplayImageOptions options;
	public ImageLoader loader = ImageLoader.getInstance();
	private int pageindex = 1;
	private Madapter2 adapter2;
	private ProgressBar bar;
	DynamicHeightImageView imageview;
	Drawable mydrawable;
	private ArrayList<DuitangImageBean> images;
	private ArrayList<DuitangInfo> adapterlists;
	public static final SparseArray<Double> common_sPositionHeightRatios = new SparseArray<Double>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.staggergrid, container, false);
		sgv = (StaggeredGridView) v.findViewById(R.id.staggeredGridView1);
		sgv.setPadding(15, 0, 15, 0);
		
		sgv.setSelector(R.drawable.griditemselector);
		sgv.setDrawSelectorOnTop(true);
		bar = new ProgressBar(getActivity());
		
		bar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getActivity()).build());
		ptrl = (PullToRefreshLayout) v.findViewById(R.id.pulltorefresh);
		sgv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) { 
				Intent intend = new Intent();
				intend.putExtra("url", images.get(position).getThumburl());
				intend.putExtra("msg", images.get(position).getDesc());
				intend.setClass(getActivity(), ImageDetailActivity.class);
				startActivity(intend);

			}
		});
		sgv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int a = firstVisibleItem;
				int b = visibleItemCount;
				int c = totalItemCount;
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					pageindex += 1;
//					String url = "http://www.duitang.com/album/6452436/masn/p/"
//							+ pageindex + "/10/";
					ContentTask2 task = new ContentTask2(getActivity());
					task.execute(pageindex);
				}
			}
		});
//		adapterlists = new ArrayList<DuitangInfo>();
		images=new ArrayList<DuitangImageBean>();
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.hs2)
				.displayer(new RoundedBitmapDisplayer(10))
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		// 6452436
//		String url = "http://www.duitang.com/album/6452436/masn/p/"
//				+ pageindex + "/10/";
//		ContentTask task = new ContentTask(getActivity());
//		task.execute(url);
		ContentTask2 task=new ContentTask2(getActivity());
		task.execute(pageindex);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) view;
		// As we're using a ListFragment we create a PullToRefreshLayout
		// manually
		ptrl = new PullToRefreshLayout(viewGroup.getContext());
		// We can now setup the PullToRefreshLayout
		ActionBarPullToRefresh
				.from(getActivity())
				// We need to insert the PullToRefreshLayout into the Fragment's
				// ViewGroup
				.insertLayoutInto(viewGroup)
				.theseChildrenArePullable(R.id.staggeredGridView1)
				.listener(this).setup(ptrl);
	}



	@Override
	public void onRefreshStarted(View view) {
		pageindex += 1;
		ContentTask2 task = new ContentTask2(getActivity());
		task.execute(pageindex);
		ptrl.setRefreshComplete();
	}
	private class ContentTask2 extends AsyncTask<Integer, Integer, List<DuitangImageBean>> {

		private Context mContext;

		public ContentTask2(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected List<DuitangImageBean> doInBackground(Integer... params) {
		return PictureDao.getDuitangsbyPage(params[0]);
//			return PictureDao.getymtImages();
		}

		@Override
		protected void onPostExecute(List<DuitangImageBean> result) {
			// 这个备注的方法可以5条5条的更新
			// adapter=new Madapter((ArrayList<DuitangInfo>)result);
			adapter2 = new Madapter2(images);
			adapter2.resh(result);

			sgv.setAdapter(adapter2);
			adapter2.notifyDataSetChanged();
			for (DuitangImageBean info : result) {
			}
		}

		@Override
		protected void onPreExecute() {
		}

	}
	private class Madapter2 extends BaseAdapter {
		private ArrayList<DuitangImageBean> duitangs;

		private class Viewholder {
			public TextView text; 
			public ProgressBar bar;
			public DynamicHeightImageView imageView;
		}

		public Madapter2(List<DuitangImageBean> result) {
			super();
			this.duitangs = (ArrayList<DuitangImageBean>) result;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Viewholder holder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.fuckgrid, null);
				holder = new Viewholder();
				holder.imageView =  (DynamicHeightImageView) view.findViewById(R.id.fuckimage);
				holder.text = (TextView) view.findViewById(R.id.fucktext);
				view.setTag(holder);
			} else
				holder = (Viewholder) view.getTag();

			holder.text.setText(duitangs.get(position).getDesc());
			loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			loader.displayImage(duitangs.get(position).getThumburl(),holder.imageView, options,new ImageLoadingListener() {
							@Override
							public void onLoadingCancelled(String arg0,	View arg1) {
							}
							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								// TODO 获取图片的位置，并设置宽高比
								DynamicHeightImageView myview = (DynamicHeightImageView) arg1;
								double newratio = common_sPositionHeightRatios.get(position, 0.0);
								if (newratio == 0) {
									newratio = (double) arg2.getHeight() / (double) arg2.getWidth();
									if (newratio > 3.0) {
										newratio = 3.0;
									}
									myview.setHeightRatio(newratio);
									common_sPositionHeightRatios.append(position, newratio);
								} 
							} 
							@Override
							public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {
							} 
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
							}
						});
//			Glide.with(getActivity()).load(duitangs.get(position).getIsrc()).into(holder.imageView);
				double ratio = common_sPositionHeightRatios.get(position, 0.0);
				if (ratio != 0) {
					holder.imageView.setHeightRatio(ratio);
				} 
			return view;
		}

		public void resh(List<DuitangImageBean> beans) {
			this.duitangs.addAll(beans);
			adapter2.notifyDataSetChanged();
		}
	}
}

