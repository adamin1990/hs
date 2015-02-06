package com.iiijiaban.hairstyle.fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import org.apache.http.client.utils.URIUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.iiijiaban.u56player.R;
import com.iiijiaban.hairstyle.beans.DuitangImageBean;
import com.iiijiaban.hairstyle.beans.DuitangInfo;
import com.iiijiaban.hairstyle.ui.FoldingCirclesDrawable;
import com.iiijiaban.hairstyle.ui.ImageDetailActivity;
import com.iiijiaban.hairstyle.ui.NexusRotationCrossDrawable;
import com.iiijiaban.hairstyle.utils.Helper;
import com.ijiaban.actionbar.pulltorefreshlib.ActionBarPullToRefresh;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.actionbar.pulltorefreshlib.PullToRefreshLayout;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

public class TestFragment extends Fragment implements OnRefreshListener {
	private PullToRefreshLayout ptrl;
	private StaggeredGridView sgv; //瀑布流
	private DisplayImageOptions options;
	public ImageLoader loader = ImageLoader.getInstance();
	private int pageindex = 1;
	private Madapter adapter;
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
				intend.putExtra("url", adapterlists.get(position).getIsrc());
				intend.putExtra("msg", adapterlists.get(position).getMsg());
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
					String url = "http://www.duitang.com/album/6452436/masn/p/"
							+ pageindex + "/10/";
					ContentTask task = new ContentTask(getActivity());
					task.execute(url);
				}
			}
		});
		adapterlists = new ArrayList<DuitangInfo>();
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.hs2)
				.displayer(new RoundedBitmapDisplayer(10))
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		// 6452436
		String url = "http://www.duitang.com/album/6452436/masn/p/"
				+ pageindex + "/10/";
		ContentTask task = new ContentTask(getActivity());
		task.execute(url);
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

	private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

		private Context mContext;

		public ContentTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected List<DuitangInfo> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<DuitangInfo> result) {
			// 这个备注的方法可以5条5条的更新
			// adapter=new Madapter((ArrayList<DuitangInfo>)result);
			adapter = new Madapter(adapterlists);
			adapter.resh(result);

			sgv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			for (DuitangInfo info : result) {
			}
		}

		@Override
		protected void onPreExecute() {
		}

		public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
			List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);

				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			Log.d("MainActiivty", "json:" + json);
			try {
				if (null != json) {
					JSONObject newsObject = new JSONObject(json);
					JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray blogsJson = jsonObject.getJSONArray("blogs");
					for (int i = 0; i < blogsJson.length(); i++) {
						JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
						DuitangInfo newsInfo1 = new DuitangInfo();
						newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "": newsInfoLeftObject.getString("albid"));
						newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "": newsInfoLeftObject.getString("isrc"));
						newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "": newsInfoLeftObject.getString("msg"));
						newsInfo1.setAva(newsInfoLeftObject.isNull("ava") ? "": newsInfoLeftObject.getString("ava"));
						duitangs.add(newsInfo1);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}

	private class Madapter extends BaseAdapter {
		private ArrayList<DuitangInfo> duitangs;

		private class Viewholder {
			public TextView text; 
			public ProgressBar bar;
			public DynamicHeightImageView imageView;
		}

		public Madapter(List<DuitangInfo> result) {
			super();
			this.duitangs = (ArrayList<DuitangInfo>) result;
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

			holder.text.setText(duitangs.get(position).getMsg());
			ImageLoader.getInstance().displayImage(duitangs.get(position).getIsrc(),holder.imageView, options,new ImageLoadingListener() {
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
									if (newratio > 4.0) {
										newratio = 4.0;
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

		public void resh(List<DuitangInfo> beans) {
			this.duitangs.addAll(beans);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		pageindex += 1;
		String url = "http://www.duitang.com/album/6452436/masn/p/" + pageindex + "/10/";
		ContentTask task = new ContentTask(getActivity());
		task.execute(url);
		ptrl.setRefreshComplete();
	}
}
