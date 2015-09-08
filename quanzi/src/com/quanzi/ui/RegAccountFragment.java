package com.quanzi.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants.Config;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.CommonTools;
import com.quanzi.utils.HttpUtil;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.MD5For32;
import com.quanzi.utils.SIMCardInfo;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanquan  
 * 类名称：RegAccountFragment  
 * 类描述：账号设置页面，手机号码、验证码和密码
 * @author zhangshuai
 * @date 创建时间：2015-4-7 下午2:51:08 
 *
 */
public class RegAccountFragment extends BaseV4Fragment {

	/*************Views************/
	private View rootView;// 根View
	private TextView topNavigation;// 导航栏文字
	private View leftImageButton;// 导航栏左侧按钮
	private View rightImageButton;// 导航栏右侧按钮
	private EditText mPhoneView;// 手机号
	private EditText mPasswordView;// 密码
	private EditText mConformPassView;// 确认密码
	private UserPreference userPreference;
	private TextView leftNavigation;// 步骤

	private String mPhone;
	private String mPassword;
	private String mConformPass;
	private String mAuthcode;
	private String responseAuthcode;// 手机获取到的验证码

	private int recLen;
	private Button authCodeButton;
	private EditText authCodeView;
	private Timer timer;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler;
	private String strContent;
	View focusView = null;

	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

	private CheckPhoneTask mCheckPhoneTask = null;// 检查电话号码

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 拦截短信内容
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				authCodeView.setText(strContent);
			};
		};

		filter2 = new IntentFilter();
		filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter2.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					// Time time = new Time();
					// time.set(sms.getTimestampMillis());
					// String time2 = time.format3339(true);
					// Log.d("logo", from + "   " + message + "  " + time2);
					// strContent = from + "   " + message;
					// handler.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		getActivity().registerReceiver(smsReceiver, filter2);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(smsReceiver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_reg_account, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		topNavigation = (TextView) getActivity().findViewById(R.id.nav_text);
		leftImageButton = (View) getActivity().findViewById(R.id.left_btn_bg);
		rightImageButton = (View) getActivity().findViewById(R.id.right_btn_bg);
		mPhoneView = (EditText) rootView.findViewById(R.id.phone);
		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mConformPassView = (EditText) rootView.findViewById(R.id.conform_password);
		authCodeButton = (Button) rootView.findViewById(R.id.again_authcode);
		authCodeView = (EditText) rootView.findViewById(R.id.autncode);
		leftNavigation = (TextView) getActivity().findViewById(R.id.left_text);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// 显示用户手机号
		SIMCardInfo siminfo = new SIMCardInfo(getActivity());
		mPhoneView.setText(siminfo.getNativePhoneNumber());
		mPhone = mPhoneView.getText().toString();

		topNavigation.setText("账户");
		leftNavigation.setText("2/4");
		leftImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});

		rightImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attepmtAccount();
			}
		});

		authCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptAuthCode();
			}
		});

	}

	private void attemptAuthCode() {
		mPhoneView.setError(null);
		mPhone = mPhoneView.getText().toString();
		boolean cancel = false;
		// 检查手机号
		if (TextUtils.isEmpty(mPhone)) {
			mPhoneView.setError(getString(R.string.error_field_required));
			focusView = mPhoneView;
			cancel = true;
		} else if (!CommonTools.isMobileNO(mPhone)) {
			mPhoneView.setError(getString(R.string.error_phone));
			focusView = mPhoneView;
			cancel = true;
		}
		if (cancel) {
			// 如果错误，则提示错误
			focusView.requestFocus();
			return;
		}

		getAuthCode();// 获取验证码

		recLen = Config.AUTN_CODE_TIME;
		authCodeButton.setEnabled(false);

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				recLen--;
				Message message = new Message();
				message.what = 1;
				timeHandler.sendMessage(message);
			}
		}, 1000, 1000);
	}

	/**
	 * 验证输入
	 */
	private void attepmtAccount() {
		// 重置错误
		mPasswordView.setError(null);
		mPhoneView.setError(null);
		mConformPassView.setError(null);
		authCodeView.setError(null);

		// 存储用户值
		mPassword = mPasswordView.getText().toString();
		mPhone = mPhoneView.getText().toString();
		mConformPass = mConformPassView.getText().toString();
		mAuthcode = authCodeView.getText().toString();
		boolean cancel = false;

		// 检查手机号
		if (TextUtils.isEmpty(mPhone)) {
			mPhoneView.setError(getString(R.string.error_field_required));
			focusView = mPhoneView;
			cancel = true;
		} else if (!CommonTools.isMobileNO(mPhone)) {
			mPhoneView.setError(getString(R.string.error_phone));
			focusView = mPhoneView;
			cancel = true;
		}

		// 检查密码
		else if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!CommonTools.isPassValid(mPassword)) {
			mPasswordView.setError(getString(R.string.error_pattern_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// 检查重复密码
		else if (TextUtils.isEmpty(mConformPass)) {
			mConformPassView.setError(getString(R.string.error_field_required));
			focusView = mConformPassView;
			cancel = true;
		} else if (!mConformPass.equals(mPassword)) {
			mConformPassView.setError(getString(R.string.error_field_conform_pass));
			focusView = mConformPassView;
			cancel = true;
		}

		// 检查验证码
		else if (TextUtils.isEmpty(mAuthcode)) {
			authCodeView.setError(getString(R.string.error_field_required));
			focusView = authCodeView;
			cancel = true;
		} else if (mAuthcode.length() != 6) {
			authCodeView.setError("验证码长度为6位");
			focusView = authCodeView;
			cancel = true;
		} else if (!vertifyAuthCode(mAuthcode, responseAuthcode)) {
			authCodeView.setError("验证码不正确");
			focusView = authCodeView;
			cancel = true;
		}

		if (cancel) {
			// 如果错误，则提示错误
			focusView.requestFocus();
		} else {
			// 检查手机号是否被注册
			mCheckPhoneTask = new CheckPhoneTask();
			mCheckPhoneTask.execute();
		}
	}

	/**
	 * 下一步
	 */
	private void next() {
		RegSchoolFragment regSchoolFragment = new RegSchoolFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out);
		transaction.replace(R.id.fragment_container, regSchoolFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 获取验证码
	 * @return
	 */
	private void getAuthCode() {
		RequestParams params = new RequestParams();
		params.put(UserTable.U_TEL, mPhone);
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub

				if (response.length() == 6) {
					responseAuthcode = response;
				} else if (response.endsWith("-1")) {
					ToastTool.showLong(getActivity(), "服务器出现异常，请稍后再试");
					LogTool.e("获取验证码服务器返回-1");
				} else if (response.endsWith("1")) {
					ToastTool.showShort(getActivity(), "手机号码为空");
				} else {
					LogTool.e("获取验证码服务器错误");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("验证码", "服务器错误,错误代码" + statusCode + "，  原因" + errorResponse);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
		};
		AsyncHttpClientTool.post("user/getValidateCode", params, responseHandler);
	}

	/**
	 * 
	 * 类名称：CheckPhoneTask
	 * 类描述：检查手机号是否已经被注册
	 * 创建人： 张帅
	 * 创建时间：2014年7月13日 上午11:25:31
	 *
	 */
	public class CheckPhoneTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String url = "regist/telrepeat";
				Map<String, String> map = new HashMap<String, String>();
				map.put(UserTable.U_TEL, mPhoneView.getText().toString());

				String result = HttpUtil.postRequest(url, map);
				if (result.equals("1")) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			mCheckPhoneTask = null;

			if (result) {
				// 没有错误，则存储值
				userPreference.setU_tel(mPhone);
				userPreference.setU_password(MD5For32.GetMD5Code(mPassword));
				next();
			} else {
				mPhoneView.setError("该手机号码已被注册");
				focusView = mPhoneView;
				focusView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mCheckPhoneTask = null;
		}

	}

	/**
	 * 控制计时
	 */
	final Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				authCodeButton.setText("剩余" + recLen + "s");
				if (recLen < 0) {
					timer.cancel();
					authCodeButton.setText("重新获取");
					authCodeButton.setEnabled(true);
					rightImageButton.setEnabled(false);
				}
			}
		}
	};

	/**
	 * 计时器
	 */
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			recLen--;
			Message message = new Message();
			message.what = 1;
			timeHandler.sendMessage(message);
		}
	};

	/**
	 * 验证验证码
	 * @return
	 */
	private boolean vertifyAuthCode(String myAuthCode, String response) {
		if (!TextUtils.isEmpty(response)) {
			if (response.equals(myAuthCode)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

}
