package com.iiijiaban.hairstyle.fragment;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.iiijiaban.u56player.R;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.tabslide.PagerSlidingTabStrip;

/**
 * 主fragment 包含N個子fragment
 * 
 * @author adamin
 * 
 */
public class MainFragment extends SherlockFragment implements OnRefreshListener {
	private MyPagerAdapter adapter;
	private ActionBarHelper mActionBar;
	/**
	 * viewpager相关
	 */
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
  private AdView mAdView;
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mainfragment_layout, container,
				false);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		 LinearLayout layout = (LinearLayout) view.findViewById(R.id.adlayoutmainfragment);
	        mAdView = new AdView(getActivity());
			mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
			mAdView.setAdSize(AdSize.BANNER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			layout.addView(mAdView, params);
			mAdView.loadAd(new AdRequest.Builder().build());
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.maintabs);
		tabs.setIndicatorColor(getResources().getColor(R.color.actionbar));
		tabs.setBackgroundColor(getResources().getColor(R.color.white));
		tabs.setDividerColor(getResources().getColor(R.color.actionbar));
		tabs.setTextColor(getResources().getColor(R.color.actionbar));
		tabs.setTextSize(30);
		tabs.setUnderlineHeight(6);
		tabs.setIndicatorHeight(15);
		pager = (ViewPager) view.findViewById(R.id.apager);
		pager.setOffscreenPageLimit(1);
		adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.getAdapter().notifyDataSetChanged();
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
		mActionBar = createActionBarHelper();
		mActionBar.init();
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	/**
	 * 實例化重新
	 * 
	 * @return
	 */
	public static Fragment newInstance() {
		Fragment fr = new MainFragment();
		return fr;
	}

	/**
	 * OnRefreshListene 接口的方法
	 */
	@Override
	public void onRefreshStarted(View view) {

	}

	/**
	 * Drawerlayout listener
	 * 
	 * @author adamin
	 * 
	 */
	public class MainDrawerListener implements DrawerLayout.DrawerListener {

		@Override
		public void onDrawerOpened(View drawerView) {
			// mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			// mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			// mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			// mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	/**
	 * actionbar 类帮助
	 * 
	 * @author adamin
	 * 
	 */
	private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		private ActionBarHelper() {
			mActionBar = ((SherlockFragmentActivity) getActivity())
					.getSupportActionBar();
		}

		public void init() {
			// mActionBar.setDisplayHomeAsUpEnabled(true);
			// mActionBar.setHomeButtonEnabled(true);
			mTitle = mDrawerTitle = getActivity().getTitle();
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}

	/**
	 * viewpager适配器
	 * 
	 * @author adamin
	 * 
	 */
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<Fragment> fragmentList;
//		TestFragment tj1 = new TestFragment();
		TestFragment2 tj1=new TestFragment2();
		LocalimgFragment tj2 = new LocalimgFragment();
		// TestFragment tj2 = new TestFragment();
		// TestFragment tj3 = new TestFragment();
		private final String[] TITLES = { "美发", "下载" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Fragment getItem(int position) {

			fragmentList = new ArrayList<Fragment>();
			fragmentList.add(tj1);
			fragmentList.add(tj2);
			// fragmentList.add(tj2);
			// fragmentList.add(tj3);
			return fragmentList.get(position);
		}

	}

}
