package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�MainExploreFragment  
 * ��������̽��ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-12 ����8:54:17 
 *
 */
public class MainExploreFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// ��View
	private TextView leftTextView;//�������������
	private TextView schoolTextView;//ѧУ
	private View filterBtn;//ɸѡ��ť
	private View searchBtn;//���Ұ�ť
	private View publishBtn;//������ť
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

		findViewById();// ��ʼ��views
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
		// �ѵ�һ��tab��Ϊѡ��״̬
		mTabs[0].setSelected(true);

		for (View view : mTabs) {
			view.setOnClickListener(this);
		}

		// viewPager���¼�
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
	 * ��ʾ�������ӻ��˵�
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
	 * ��ʾɸѡ�˵�
	 */
	void showScreenDialog() {

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
		ScreenDialogFragment newFragment = ScreenDialogFragment.newInstance();
		newFragment.show(ft, "screen_dialog");
	}

	/**
	 * button����¼�
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
		// �ѵ�ǰtab��Ϊѡ��״̬
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nav_right_btn1://����
			showPublishDialog();
			break;
		case R.id.nav_right_btn2://����
			getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
			getActivity().overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
			break;
		case R.id.nav_right_btn3://ɸѡ
			showScreenDialog();
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
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�MainExplorePagerAdapter  
	 * ��������̽��ҳ���������
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-4-20 ����3:31:20 
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
				fragment = new MainExplorePostFragment();
				break;
			case 1:
				fragment = new MainExploreActFragment();
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
