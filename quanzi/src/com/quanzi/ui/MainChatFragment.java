package com.quanzi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.ui.MainExploreFragment.MainExplorePagerAdapter;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanquan  
 * 类名称：MainChatFragment  
 * 类描述：消息页面
 * @author zhangshuai
 * @date 创建时间：2015-4-12 下午8:55:13 
 *
 */
public class MainChatFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// 根View
	private TextView leftTextView;//导航栏左侧文字
	private UserPreference userPreference;
	private int index;
	private int currentTabIndex;

	private View[] mTabs;
	ViewPager mViewPager;
	MainChatPagerAdapter mPagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_mainchat, container, false);

		mPagerAdapter = new MainChatPagerAdapter(getFragmentManager());
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
		mViewPager = (ViewPager) rootView.findViewById(R.id.mian_chat_pager);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("消息");
		mTabs = new View[2];
		mTabs[0] = (View) rootView.findViewById(R.id.chatBtn);
		mTabs[1] = (View) rootView.findViewById(R.id.msgBtn);
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
			}
		});
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onSubTabClicked(View view) {
		switch (view.getId()) {
		case R.id.chatBtn:
			index = 0;
			break;
		case R.id.msgBtn:
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
		case R.id.chatBtn:
			onSubTabClicked(v);
			break;
		case R.id.msgBtn:
			onSubTabClicked(v);
			break;

		default:
			break;
		}
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：MainChatPagerAdapter  
	 * 类描述：
	 * @author zhangshuai
	 * @date 创建时间：2015-4-27 下午12:29:40 
	 *
	 */
	public class MainChatPagerAdapter extends FragmentPagerAdapter {

		public MainChatPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Fragment fragment = null;

			switch (position) {
			case 0:
				fragment = new MainChatContactListFragment();
				break;
			case 1:
				fragment = new MainChatMsgFragment();
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
}
