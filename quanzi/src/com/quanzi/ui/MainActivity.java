package com.quanzi.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.quanzi.R;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.utils.CommonTools;

/**
 *
 * 项目名称：quanquan  
 * 类名称：MainActivity  
 * 类描述：登录后主页，包含四个Fragments,MainHomeFragment、MainExploreFragment、MainChatFragment、MainPernalFragment。
 *        点击后分别进如不同的子页面。
 * @author zhangshuai
 * @date 创建时间：2015-4-12 下午9:34:48 
 *
 */
public class MainActivity extends BaseFragmentActivity {
	private View[] mTabs;
	private MainHomeFragment homeFragment;//圈子动态页面
	private MainExploreFragment exploreFragment;//探索页面
	private MainChatFragment chatFragment;//消息页面
	private MainPersonalFragment personalFragment;//个人中心页面
	private int index;
	// 当前fragment的index
	private int currentTabIndex = 0;
	private Fragment[] fragments;
	boolean isExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		homeFragment = new MainHomeFragment();
		exploreFragment = new MainExploreFragment();
		chatFragment = new MainChatFragment();
		personalFragment = new MainPersonalFragment();
		fragments = new Fragment[] { homeFragment, exploreFragment, chatFragment, personalFragment };

		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mTabs = new View[4];
		mTabs[0] = (View) findViewById(R.id.btn_container_home);
		mTabs[1] = (View) findViewById(R.id.btn_container_explore);
		mTabs[2] = (View) findViewById(R.id.btn_container_chat);
		mTabs[3] = (View) findViewById(R.id.btn_container_personal);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment)
				.commit();
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_container_home:
			index = 0;
			break;
		case R.id.btn_container_explore:
			index = 1;
			break;
		case R.id.btn_container_chat:
			index = 2;
			break;
		case R.id.btn_container_personal:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			CommonTools.showShortToast(MainActivity.this, "再按一次退出程序");
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			close();
		}
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub   
			super.handleMessage(msg);
			isExit = false;
		}
	};
}
