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
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�MainActivity  
 * ����������¼����ҳ�������ĸ�Fragments,MainHomeFragment��MainExploreFragment��MainChatFragment��MainPernalFragment��
 *        �����ֱ���粻ͬ����ҳ�档
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-12 ����9:34:48 
 *
 */
public class MainActivity extends BaseFragmentActivity {
	private View[] mTabs;
	private MainHomeFragment homeFragment;//Ȧ�Ӷ�̬ҳ��
	private MainExploreFragment exploreFragment;//̽��ҳ��
	private MainChatFragment chatFragment;//��Ϣҳ��
	private MainPersonalFragment personalFragment;//��������ҳ��
	private int index;
	// ��ǰfragment��index
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
		// �ѵ�һ��tab��Ϊѡ��״̬
		mTabs[0].setSelected(true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// �����ʾ��һ��fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment)
				.commit();
	}

	/**
	 * button����¼�
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
		// �ѵ�ǰtab��Ϊѡ��״̬
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * �����η��ؼ��˳�
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
			CommonTools.showShortToast(MainActivity.this, "�ٰ�һ���˳�����");
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
