package com.quanzi.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants.Config;
import com.quanzi.utils.CommonTools;
import com.quanzi.utils.MD5For32;
import com.quanzi.utils.SIMCardInfo;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�RegAccountFragment  
 * ���������˺�����ҳ�棬�ֻ����롢��֤�������
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-7 ����2:51:08 
 *
 */
public class RegAccountFragment extends BaseV4Fragment {

	/*************Views************/
	private View rootView;// ��View
	private TextView topNavigation;//����������
	private View leftImageButton;//��������ఴť
	private View rightImageButton;//�������Ҳఴť
	private EditText mPhoneView;//�ֻ���
	private EditText mPasswordView;//����
	private EditText mConformPassView;//ȷ������
	private UserPreference userPreference;
	private TextView leftNavigation;//����

	private String mName;
	private String mPhone;
	private String mPassword;
	private String mConformPass;
	private boolean phoneAvailable;//�ֻ����Ƿ����
	private String mAuthcode;
	private String responseAuthcode;//�ֻ���ȡ������֤��

	private int recLen;
	private Button authCodeButton;
	private EditText authCodeView;
	private Timer timer;
	private String huanxinUsername;
	private String huanxinaPassword;
	ProgressDialog dialog;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler;
	private String strContent;

	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

	private CheckPhoneTask mCheckPhoneTask = null;//���绰����

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//���ض�������
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
					// ���ŵ�����
					String message = sms.getMessageBody();
					// ��Ϣ���ֻ��š���+86��ͷ��
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

		findViewById();// ��ʼ��views
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
		//��ʾ�û��ֻ���
		SIMCardInfo siminfo = new SIMCardInfo(getActivity());
		mPhoneView.setText(siminfo.getNativePhoneNumber());
		mPhone = mPhoneView.getText().toString();

		if ((!mPhone.isEmpty()) && mPhone.length() == 11) {
			//����ֻ����Ƿ�ע��
			mCheckPhoneTask = new CheckPhoneTask();
			mCheckPhoneTask.execute();
		}

		topNavigation.setText("�˻�");
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

		mPhoneView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mPhone = mPhoneView.getText().toString();
				if ((!mPhone.isEmpty()) && mPhone.length() == 11) {
					//����ֻ����Ƿ�ע��
					mCheckPhoneTask = new CheckPhoneTask();
					mCheckPhoneTask.execute();
				}
			}
		});

		authCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recLen = Config.AUTN_CODE_TIME;
				authCodeButton.setEnabled(false);
				getAuthCode();
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
		});

	}

	/**
	 * ��֤����
	 */
	private void attepmtAccount() {
		// ���ô���
		mPasswordView.setError(null);
		mPhoneView.setError(null);
		mConformPassView.setError(null);
		authCodeView.setError(null);

		// �洢�û�ֵ
		mPassword = mPasswordView.getText().toString();
		mPhone = mPhoneView.getText().toString();
		mConformPass = mConformPassView.getText().toString();
		mAuthcode = authCodeView.getText().toString();
		boolean cancel = false;
		View focusView = null;

		// ����ֻ���
		if (TextUtils.isEmpty(mPhone)) {
			mPhoneView.setError(getString(R.string.error_field_required));
			focusView = mPhoneView;
			cancel = true;
		} else if (!CommonTools.isMobileNO(mPhone)) {
			mPhoneView.setError(getString(R.string.error_phone));
			focusView = mPhoneView;
			cancel = true;
		}

		// �������
		else if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!CommonTools.isPassValid(mPassword)) {
			mPasswordView.setError(getString(R.string.error_pattern_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// ����ظ�����
		else if (TextUtils.isEmpty(mConformPass)) {
			mConformPassView.setError(getString(R.string.error_field_required));
			focusView = mConformPassView;
			cancel = true;
		} else if (!mConformPass.equals(mPassword)) {
			mConformPassView.setError(getString(R.string.error_field_conform_pass));
			focusView = mConformPassView;
			cancel = true;
		}

		//�����֤��
		else if (TextUtils.isEmpty(mAuthcode)) {
			authCodeView.setError(getString(R.string.error_field_required));
			focusView = authCodeView;
			cancel = true;
		} else if (mAuthcode.length() != 6) {
			authCodeView.setError("��֤�볤��Ϊ6λ");
			focusView = authCodeView;
			cancel = true;
		} else if (!vertifyAuthCode(mAuthcode, responseAuthcode)) {
			authCodeView.setError("��֤�벻��ȷ");
			focusView = authCodeView;
			cancel = true;
		}

		else if (!phoneAvailable) {
			mPhoneView.setError("���ֻ����ѱ�ע�ᣡ");
			focusView = mPhoneView;
			cancel = true;
		}

		if (cancel) {
			// �����������ʾ����
			focusView.requestFocus();
		} else {
			//			//��ȡ��֤��
			//			getAuthCode();

			// û�д�����洢ֵ
			userPreference.setU_tel(mPhone);
			userPreference.setU_password(MD5For32.GetMD5Code(mPassword));
			userPreference.setU_nickname(mName);
			next();

		}
	}

	/**
	 * ��һ��
	 */
	private void next() {
		RegSchoolFragment regSchoolFragment = new RegSchoolFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
				R.anim.push_right_out);
		transaction.replace(R.id.fragment_container, regSchoolFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * ��ȡ��֤��
	 * @return
	 */
	private void getAuthCode() {
		//		RequestParams params = new RequestParams();
		//		params.put(UserTable.U_TEL, mPhone);
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
		//			ProgressDialog dialog = new ProgressDialog(getActivity());
		//
		//			@Override
		//			public void onStart() {
		//				// TODO Auto-generated method stub
		//				super.onStart();
		//				rightImageButton.setEnabled(false);
		//				dialog.setMessage("������֤�����Ժ�...");
		//				dialog.setCancelable(false);
		//				dialog.show();
		//			}
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//
		//				if (response.length() == 6) {
		//					RegAuthCodeFragment fragment = new RegAuthCodeFragment();
		//					Bundle bundle = new Bundle();
		//					bundle.putString(RegAuthCodeFragment.AUTHCODE, response);
		//					fragment.setArguments(bundle);
		//					FragmentTransaction transaction = getFragmentManager().beginTransaction();
		//					transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
		//							R.anim.push_right_out);
		//					transaction.replace(R.id.fragment_container, fragment);
		//					transaction.addToBackStack(null);
		//					transaction.commit();
		//					ToastTool.showShort(RegPhoneFragment.this.getActivity(), "��֤���ѷ���");
		//					rightImageButton.setEnabled(true);
		//
		//				} else if (response.endsWith("-1")) {
		//					ToastTool.showLong(RegPhoneFragment.this.getActivity(), "�����������쳣�����Ժ�����");
		//				} else if (response.endsWith("1")) {
		//					ToastTool.showShort(RegPhoneFragment.this.getActivity(), "�ֻ�����Ϊ��");
		//				} else {
		//					ToastTool.showShort(RegPhoneFragment.this.getActivity(), "����������");
		//				}
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				ToastTool.showShort(RegPhoneFragment.this.getActivity(), "����������");
		//				LogTool.e("��֤��", "����������,�������" + statusCode + "��  ԭ��" + errorResponse);
		//			}
		//
		//			@Override
		//			public void onFinish() {
		//				// TODO Auto-generated method stub
		//				super.onFinish();
		//				dialog.dismiss();
		//			}
		//		};
		//		AsyncHttpClientTool.post("getmessage", params, responseHandler);
	}

	/**
	 * 
	 * �����ƣ�CheckPhoneTask
	 * ������������ֻ����Ƿ��Ѿ���ע��
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014��7��13�� ����11:25:31
	 *
	 */
	public class CheckPhoneTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			phoneAvailable = false;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//			try {
			//				String url = "existtel";
			//				Map<String, String> map = new HashMap<String, String>();
			//				map.put(UserTable.U_TEL, mPhoneView.getText().toString());
			//
			//				String result = HttpUtil.postRequest(url, map);
			//				if (result.equals(DefaultKeys.TEL_OK)) {
			//					return true;
			//				}
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//				return false;
			//			}
			//			return false;
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			mCheckPhoneTask = null;
			phoneAvailable = result;
		}

		@Override
		protected void onCancelled() {
			mCheckPhoneTask = null;
		}

	}

	/**
	 * ���Ƽ�ʱ
	 */
	final Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				authCodeButton.setText("ʣ��" + recLen + "s");
				if (recLen < 0) {
					timer.cancel();
					authCodeButton.setText("���»�ȡ");
					authCodeButton.setEnabled(true);
					rightImageButton.setEnabled(false);
				}
			}
		}
	};

	/**
	 * ��ʱ��
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
	 * ��֤��֤��
	 * @return
	 */
	private boolean vertifyAuthCode(String myAuthCode, String response) {
		//		if (!TextUtils.isEmpty(response)) {
		//			if (response.equals(myAuthCode)) {
		//				return true;
		//			} else {
		//				return false;
		//			}
		//		} else {
		//			return false;
		//		}
		return true;
	}

	/**
	 * ƥ������м��6�����֣���֤��ȣ�
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
