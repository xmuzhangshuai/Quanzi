package com.quanzi.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.utils.FileSizeUtil;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�SettingAboutFragment  
 * ������������ҳ���еĹ���
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����7:51:04 
 *
 */
public class SettingAboutActivity extends BaseActivity implements OnClickListener {
	private TextView topNavigation;// ����������
	private View leftImageButton;// ��������ఴť

	private View settingClearCache;// ��ջ���
	private View settingFeedback;// ����
	private View settingCheckUpdate;// ������
	private View settingUserContact;// ����
	private View settingAboutUs;// �˳�
	private UserPreference userPreference;
	private TextView cacheSize;
	private TextView version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_about);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		topNavigation = (TextView) findViewById(R.id.nav_text);
		leftImageButton = (View) findViewById(R.id.left_btn_bg);
		settingClearCache = findViewById(R.id.setting_clear_cache);
		settingFeedback = findViewById(R.id.setting_feedback);
		settingCheckUpdate = findViewById(R.id.setting_check_update);
		settingUserContact = findViewById(R.id.user_contact);
		settingAboutUs = findViewById(R.id.about_us);
		cacheSize = (TextView) findViewById(R.id.cache_size);
		version = (TextView) findViewById(R.id.version);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topNavigation.setText("����");
		leftImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});

		cacheSize.setText("" + FileSizeUtil.getFileOrFilesSize(imageLoader.getDiskCache().getDirectory().getAbsolutePath(), FileSizeUtil.SIZETYPE_MB) + "MB");

		version.setText(getVersion());

		settingUserContact.setOnClickListener(this);
		settingAboutUs.setOnClickListener(this);
		settingClearCache.setOnClickListener(this);
		settingFeedback.setOnClickListener(this);
		settingCheckUpdate.setOnClickListener(this);
	}

	/**
	 * �������
	 */
	private void clearCache() {

		final MyAlertDialog myAlertDialog = new MyAlertDialog(SettingAboutActivity.this);
		myAlertDialog.setTitle("��ʾ");
		myAlertDialog.setMessage("�Ƿ�������棿");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				imageLoader.clearMemoryCache();
				imageLoader.clearDiskCache();
				cacheSize.setText("" + FileSizeUtil.getFileOrFilesSize(imageLoader.getDiskCache().getDirectory().getAbsolutePath(), FileSizeUtil.SIZETYPE_MB)
						+ "MB");
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("ȷ��", comfirm);
		myAlertDialog.setNegativeButton("ȡ��", cancle);
		myAlertDialog.show();
	}

	/**
	 * ��ȡ�汾��
	 * @return ��ǰӦ�õİ汾��
	 */
	public String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version_name) + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_clear_cache:
			clearCache();
			break;
		case R.id.setting_feedback:
			FeedbackAgent agent = new FeedbackAgent(SettingAboutActivity.this);
			agent.startFeedbackActivity();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.setting_check_update:
			/**********�����Զ��������**************/
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(SettingAboutActivity.this, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						ToastTool.showShort(SettingAboutActivity.this, "��ǰ�����°汾");
						break;
					case UpdateStatus.NoneWifi: // none wifi
						ToastTool.showShort(SettingAboutActivity.this, "û��wifi���ӣ� ֻ��wifi�¸���");
						break;
					case UpdateStatus.Timeout: // time out
						ToastTool.showShort(SettingAboutActivity.this, "��ʱ");
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(SettingAboutActivity.this);
			break;
		case R.id.about_us:

			break;
		case R.id.user_contact:
			break;
		case R.id.left_btn_bg:
			finish();
		default:
			break;
		}
	}

}
