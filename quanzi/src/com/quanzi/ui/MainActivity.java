package com.quanzi.ui;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.quanzi.R;
import com.quanzi.base.AppManager;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.config.DefaultKeys;
import com.quanzi.huanxin.HXSDKHelper;
import com.quanzi.huanxin.MyHXSDKHelper;
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
public class MainActivity extends BaseFragmentActivity implements EMEventListener {
	private View[] mTabs;
	private MainMyQuanziFragment homeFragment;// 圈子动态页面
	private MainExploreFragment exploreFragment;// 探索页面
	private MainChatFragment chatFragment;// 消息页面
	private MainPersonalFragment personalFragment;// 个人中心页面

	// 未读消息textview
	private TextView unreadLabel;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	private int index;
	// 当前fragment的index
	private int currentTabIndex = 0;
	private Fragment[] fragments;
	boolean isExit;

	private MyConnectionListener connectionListener = null;// 监听连接状态
	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.getBoolean(DefaultKeys.ACCOUNT_REMOVED, false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			BaseApplication.getInstance().logout();
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		homeFragment = new MainMyQuanziFragment();
		exploreFragment = new MainExploreFragment();
		chatFragment = new MainChatFragment();
		personalFragment = new MainPersonalFragment();
		fragments = new Fragment[] { homeFragment, exploreFragment, chatFragment, personalFragment };

		findViewById();
		initView();

		if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(DefaultKeys.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		// 注册一个监听连接状态的listener
		connectionListener = new MyConnectionListener();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
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
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment, MainMyQuanziFragment.TAG).show(homeFragment).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			EMChatManager.getInstance().activityResumed();
		}

		// unregister this event listener when this activity enters the
		// background
		MyHXSDKHelper sdkHelper = (MyHXSDKHelper) MyHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		MyHXSDKHelper sdkHelper = (MyHXSDKHelper) MyHXSDKHelper.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(DefaultKeys.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
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
				if (index == 0) {
					trx.add(R.id.fragment_container, fragments[index], MainMyQuanziFragment.TAG);
				} else if (index == 1) {
					trx.add(R.id.fragment_container, fragments[index], MainExploreFragment.TAG);
				} else {
					trx.add(R.id.fragment_container, fragments[index]);
				}
			}
			trx.show(fragments[index]).commit();
		} else {
			if (currentTabIndex == 0) {
				MainMyQuanziFragment myQuanziFragment = (MainMyQuanziFragment) getSupportFragmentManager().findFragmentByTag(MainMyQuanziFragment.TAG);
				if (myQuanziFragment != null) {
					myQuanziFragment.refreshData();
				}
			} else if (currentTabIndex == 1) {
				MainExploreFragment mainExploreFragment = (MainExploreFragment) getSupportFragmentManager().findFragmentByTag(MainExploreFragment.TAG);
				if (mainExploreFragment != null) {
					mainExploreFragment.refresh();
				}
			}
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		BaseApplication.getInstance().logout();
		String st = "下线通知";
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		BaseApplication.getInstance().logout();
		String st5 = "移除通知";
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage("此用户已被移除");
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}
		}
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
			CommonTools.showShortToast(getBaseContext(), "再按一次退出程序");
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			// close();
			AppManager.getInstance().AppExit(getApplicationContext());
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

	// 监听事件
	@Override
	public void onEvent(EMNotifierEvent event) {
		// TODO Auto-generated method stub
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{
			refreshUI();
			EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			break;
		}
		case EventOfflineMessage: {
			refreshUI();
			break;
		}
		case EventConversationListChanged: {
			refreshUI();
			break;
		}
		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				updateUnreadLabel();
				if (currentTabIndex == 2) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatFragment != null) {
						chatFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(connectionListener);
		}

		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		// for (EMConversation conversation :
		// EMChatManager.getInstance().getAllConversations().values()) {
		// if (conversation.getType() == EMConversationType.ChatRoom)
		// chatroomUnreadMsgCount = chatroomUnreadMsgCount +
		// conversation.getUnreadMsgCount();
		// }
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnecting(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDisConnected(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReConnected() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReConnecting() {
			// TODO Auto-generated method stub

		}

		// @Override
		// public void onConnected() {
		// homeFragment.errorItem.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onDisConnected(String errorString) {
		// if (errorString != null && errorString.contains("conflict")) {
		// // 显示帐号在其他设备登陆dialog
		// showConflictDialog();
		// } else {
		// homeFragment.errorItem.setVisibility(View.VISIBLE);
		// if (NetUtils.hasNetwork(MainActivity.this))
		// homeFragment.errorText.setText("连接不到聊天服务器");
		// else
		// homeFragment.errorText.setText("当前网络不可用，请检查网络设置");
		//
		// }
		// }
		//
		// @Override
		// public void onReConnected() {
		// homeFragment.errorItem.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onReConnecting() {
		// }
		//
		// @Override
		// public void onConnecting(String progress) {
		// }

	}
}
