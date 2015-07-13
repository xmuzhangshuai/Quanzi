package com.quanzi.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants;
import com.quanzi.customewidget.MyMenuDialog;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanquan  
 * 类名称：MainExploreFragment  
 * 类描述：探索页面
 * @author zhangshuai
 * @date 创建时间：2015-4-12 下午8:54:17 
 *
 */
public class MainExploreFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// 根View
	private TextView leftTextView;//导航栏左侧文字
	private TextView schoolTextView;//学校
	private View filterBtn;//筛选按钮
	private View searchBtn;//查找按钮
	private View publishBtn;//发布按钮
	private UserPreference userPreference;
	private int index;
	private int currentTabIndex;

	private View[] mTabs;
	ViewPager mViewPager;
	MainExplorePagerAdapter mPagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_mainexplore, container, false);
		mPagerAdapter = new MainExplorePagerAdapter(getFragmentManager());
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();// 初始化views
		initView();

		mViewPager.setAdapter(mPagerAdapter);

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) rootView.findViewById(R.id.left_text1);
		schoolTextView = (TextView) rootView.findViewById(R.id.left_text2);
		filterBtn = rootView.findViewById(R.id.nav_right_btn3);
		searchBtn = rootView.findViewById(R.id.nav_right_btn2);
		publishBtn = rootView.findViewById(R.id.nav_right_btn1);
		mViewPager = (ViewPager) rootView.findViewById(R.id.main_explore_pager);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		filterBtn.setVisibility(View.VISIBLE);
		schoolTextView.setVisibility(View.VISIBLE);
		filterBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		publishBtn.setOnClickListener(this);

		mTabs = new View[2];
		mTabs[0] = (View) rootView.findViewById(R.id.postBtn);
		mTabs[1] = (View) rootView.findViewById(R.id.activityBtn);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

		for (View view : mTabs) {
			view.setOnClickListener(this);
		}

		// viewPager绑定事件
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position != currentTabIndex) {
					mTabs[currentTabIndex].setSelected(false);
				}
				currentTabIndex = position;
				mTabs[currentTabIndex].setSelected(true);

				if (position == 0) {
					searchBtn.setVisibility(View.VISIBLE);
				} else {
					searchBtn.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 显示发布帖子或活动菜单
	 */
	void showPublishDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("publish_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PublishDialogFragment newFragment = PublishDialogFragment.newInstance();
		newFragment.show(ft, "publish_dialog");
	}

	/**
	* 显示对话框，选择类型
	* 
	* @param context
	* @param isCrop
	*/
	private void showScreenActDialog() {
		final MyMenuDialog myMenuDialog = new MyMenuDialog(getActivity());
		myMenuDialog.setTitle("您想要浏览的活动类型");
		final ArrayList<String> list = new ArrayList<String>();
		list.add("全部");
		list.addAll(Constants.ActivityType.getList());

		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FragmentPagerAdapter f = (FragmentPagerAdapter) mViewPager.getAdapter();
				MainExploreActFragment mainExploreActFragment = (MainExploreActFragment) f.instantiateItem(mViewPager,
						1);
				mainExploreActFragment.screenType(list.get(position));
				myMenuDialog.dismiss();
			}
		};
		myMenuDialog.setListItemClickListener(listener);
		myMenuDialog.show();
	}

	/**
	 * 显示筛选菜单
	 */
	void showScreenPostDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("screen_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		ScreenDialogFragment newFragment = new ScreenDialogFragment();
		newFragment.show(ft, "screen_dialog");
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onSubTabClicked(View view) {
		switch (view.getId()) {
		case R.id.postBtn:
			index = 0;
			break;
		case R.id.activityBtn:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			mViewPager.setCurrentItem(index, true);
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nav_right_btn1://发布
			showPublishDialog();
			break;
		case R.id.nav_right_btn2://查找
			getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
			getActivity().overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
			break;
		case R.id.nav_right_btn3://筛选
			if (mViewPager.getCurrentItem() == 0) {
				showScreenPostDialog();
			} else {
				showScreenActDialog();
			}
			break;
		case R.id.postBtn:
			onSubTabClicked(v);
			break;
		case R.id.activityBtn:
			onSubTabClicked(v);
			break;

		default:
			break;
		}
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：MainExplorePagerAdapter  
	 * 类描述：探索页面的适配器
	 * @author zhangshuai
	 * @date 创建时间：2015-4-20 下午3:31:20 
	 *
	 */
	public class MainExplorePagerAdapter extends FragmentPagerAdapter {

		public MainExplorePagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Fragment fragment = null;

			switch (position) {
			case 0:
				fragment = MainExplorePostFragment.newInstance();
				break;
			case 1:
				fragment = MainExploreActFragment.newInstance();
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：ScreenDialogFragment  
	 * 类描述：在探索帖子页面进行用户筛选
	 * @author zhangshuai
	 * @date 创建时间：2015-4-29 下午11:37:45 
	 *
	 */
	class ScreenDialogFragment extends DialogFragment implements OnClickListener {

		private View rootView;

		private RadioButton allGenderBtn;//性别全部
		private RadioButton genderMaleBtn;//男性
		private RadioButton genderFemaleBtn;//女性
		private RadioButton allStateBtn;//状态全部
		private RadioButton stateSingleBtn;//单身
		private TextView confirmBtn;//确定
		private TextView cancleBtn;//取消

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setStyle(DialogFragment.STYLE_NO_TITLE, 0);

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_dialog_screen, container, false);
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			findViewById();
			initView();
			return rootView;
		}

		private void findViewById() {
			allGenderBtn = (RadioButton) rootView.findViewById(R.id.all_gender);
			genderMaleBtn = (RadioButton) rootView.findViewById(R.id.gender_male);
			genderFemaleBtn = (RadioButton) rootView.findViewById(R.id.gender_female);
			allStateBtn = (RadioButton) rootView.findViewById(R.id.all_statue);
			stateSingleBtn = (RadioButton) rootView.findViewById(R.id.single);
			confirmBtn = (TextView) rootView.findViewById(R.id.confirm);
			cancleBtn = (TextView) rootView.findViewById(R.id.cancle);
		}

		private void initView() {
			allGenderBtn.setOnClickListener(this);
			genderMaleBtn.setOnClickListener(this);
			genderFemaleBtn.setOnClickListener(this);
			allStateBtn.setOnClickListener(this);
			stateSingleBtn.setOnClickListener(this);
			confirmBtn.setOnClickListener(this);
			cancleBtn.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.confirm:
				FragmentPagerAdapter f = (FragmentPagerAdapter) mViewPager.getAdapter();
				MainExplorePostFragment mainExplorePostFragment = (MainExplorePostFragment) f.instantiateItem(
						mViewPager, 0);
				mainExplorePostFragment.refresh();
				ScreenDialogFragment.this.dismiss();
				break;
			case R.id.cancle:
				ScreenDialogFragment.this.dismiss();
				break;
			default:
				break;
			}
		}
	}
}
