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
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�MainActivity  
 * ����������¼����ҳ�������ĸ�Fragments,MainHomeFragment��MainExploreFragment��MainChatFragment��MainPernalFragment��
 *        �����ֱ���粻ͬ����ҳ�档
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-12 ����9:34:48 
 *
 */
public class MainActivity extends BaseFragmentActivity implements EMEventListener {
	private View[] mTabs;
	private MainMyQuanziFragment homeFragment;// Ȧ�Ӷ�̬ҳ��
	private MainExploreFragment exploreFragment;// ̽��ҳ��
	private MainChatFragment chatFragment;// ��Ϣҳ��
	private MainPersonalFragment personalFragment;// ��������ҳ��

	// δ����Ϣtextview
	private TextView unreadLabel;
	// �˺��ڱ𴦵�¼
	public boolean isConflict = false;
	// �˺ű��Ƴ�
	private boolean isCurrentAccountRemoved = false;

	private int index;
	// ��ǰfragment��index
	private int currentTabIndex = 0;
	private Fragment[] fragments;
	boolean isExit;

	private MyConnectionListener connectionListener = null;// ��������״̬
	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * ��鵱ǰ�û��Ƿ�ɾ��
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.getBoolean(DefaultKeys.ACCOUNT_REMOVED, false)) {
			// ��ֹ���Ƴ���û��ȷ����ťȻ����home���������ں�̨�ֽ�app���µ�crash
			// ����fragment��ӵ��ж�ͬ��
			BaseApplication.getInstance().logout();
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			// ��ֹ��T��û��ȷ����ťȻ����home���������ں�̨�ֽ�app���µ�crash
			// ����fragment��ӵ��ж�ͬ��
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

		// ע��һ����������״̬��listener
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
		// �ѵ�һ��tab��Ϊѡ��״̬
		mTabs[0].setSelected(true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// �����ʾ��һ��fragment
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
		// �ѵ�ǰtab��Ϊѡ��״̬
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * ��ʾ�ʺ��ڱ𴦵�¼dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		BaseApplication.getInstance().logout();
		String st = "����֪ͨ";
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
	 * �ʺű��Ƴ���dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		BaseApplication.getInstance().logout();
		String st5 = "�Ƴ�֪ͨ";
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage("���û��ѱ��Ƴ�");
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
			CommonTools.showShortToast(getBaseContext(), "�ٰ�һ���˳�����");
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

	// �����¼�
	@Override
	public void onEvent(EMNotifierEvent event) {
		// TODO Auto-generated method stub
		switch (event.getEvent()) {
		case EventNewMessage: // ��ͨ��Ϣ
		{
			refreshUI();
			EMMessage message = (EMMessage) event.getData();
			// ��ʾ����Ϣ
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
				// ˢ��bottom bar��Ϣδ����
				updateUnreadLabel();
				if (currentTabIndex == 2) {
					// ��ǰҳ�����Ϊ������ʷҳ�棬ˢ�´�ҳ��
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
	 * ˢ��δ����Ϣ��
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
	 * ��ȡδ����Ϣ��
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
	 * ���Ӽ���listener
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
		// // ��ʾ�ʺ��������豸��½dialog
		// showConflictDialog();
		// } else {
		// homeFragment.errorItem.setVisibility(View.VISIBLE);
		// if (NetUtils.hasNetwork(MainActivity.this))
		// homeFragment.errorText.setText("���Ӳ������������");
		// else
		// homeFragment.errorText.setText("��ǰ���粻���ã�������������");
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
