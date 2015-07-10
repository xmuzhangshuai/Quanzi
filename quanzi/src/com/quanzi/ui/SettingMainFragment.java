package com.quanzi.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.utils.UserPreference;

/**
 * 
 * 类名称：SettingMainFragment
 * 类描述：设置主页面
 * 创建人： 张帅
 * 创建时间：2014年8月17日 下午4:26:06
 *
 */
public class SettingMainFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// 根View
	private TextView topNavigation;//导航栏文字
	private View leftImageButton;//导航栏左侧按钮

	private View settingNewMsg;//新消息提醒
	private View settingChat;//聊天设置
	private View settingPass;//修改密码
	private View settingBlackList;//黑名单
	private View settingAbout;//关于
	private View settingLogout;//退出
	private FragmentTransaction transaction;
	private UserPreference userPreference;
	//	private FriendPreference friendPreference;
	private TextView cacheSize;
	private TextView version;

	//	FeedbackAgent agent;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		transaction = getFragmentManager().beginTransaction();
		userPreference = BaseApplication.getInstance().getUserPreference();
		//		friendPreference = BaseApplication.getInstance().getFriendPreference();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_setting_main, container, false);

		findViewById();
		initView();
		return rootView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		transaction = null;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		topNavigation = (TextView) rootView.findViewById(R.id.nav_text);
		leftImageButton = (View) rootView.findViewById(R.id.left_btn_bg);
		settingNewMsg = rootView.findViewById(R.id.setting_newmsg);
		settingChat = rootView.findViewById(R.id.setting_chat);
		settingPass = rootView.findViewById(R.id.setting_password);
		settingBlackList = rootView.findViewById(R.id.setting_blacklist);
		settingAbout = rootView.findViewById(R.id.setting_about);
		settingLogout = rootView.findViewById(R.id.setting_logout);
		cacheSize = (TextView) rootView.findViewById(R.id.cache_size);
		version = (TextView) rootView.findViewById(R.id.version);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topNavigation.setText("设置");
		leftImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});

		//		cacheSize.setText(""
		//				+ FileSizeUtil.getFileOrFilesSize(imageLoader.getDiskCache().getDirectory().getAbsolutePath(),
		//						FileSizeUtil.SIZETYPE_MB) + "MB");

		settingNewMsg.setOnClickListener(this);
		settingChat.setOnClickListener(this);
		settingAbout.setOnClickListener(this);
		settingLogout.setOnClickListener(this);
		settingBlackList.setOnClickListener(this);
		settingPass.setOnClickListener(this);
	}

	/**
	 * 退出登录
	 */
	private void logout() {
		//设置用户不曾登录

		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否退出登录？退出登录后将不能接受消息。");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();

				Intent intent;
				BaseApplication.getInstance().logout();
				userPreference.clear();
				intent = new Intent(getActivity(), LoginOrRegisterActivity.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("退出", comfirm);
		myAlertDialog.setNegativeButton("取消", cancle);
		myAlertDialog.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_newmsg:
			transaction.setCustomAnimations(R.anim.zoomin2, R.anim.zoomout);
			transaction.replace(R.id.container, new SettingNewMsgFragment());
			transaction.addToBackStack("setting");
			transaction.commit();
			break;
		case R.id.setting_chat:
			transaction.setCustomAnimations(R.anim.zoomin2, R.anim.zoomout);
			transaction.replace(R.id.container, new SettingChatFragment());
			transaction.addToBackStack("setting");
			transaction.commit();
			break;
		case R.id.setting_password:
			getActivity().startActivity(new Intent(getActivity(), ModifyPassActivity.class));
			getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			break;
		case R.id.setting_blacklist:
			getActivity().startActivity(new Intent(getActivity(), BlackListActivity.class));
			getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			break;
		case R.id.setting_about:
			transaction.setCustomAnimations(R.anim.zoomin2, R.anim.zoomout);
			transaction.replace(R.id.container, new SettingAboutFragment());
			transaction.addToBackStack("setting");
			transaction.commit();
			break;
		case R.id.setting_logout:
			logout();
			break;
		default:
			break;
		}
	}

}
