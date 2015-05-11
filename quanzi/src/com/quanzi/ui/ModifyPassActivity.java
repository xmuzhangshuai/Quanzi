package com.quanzi.ui;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.CommonTools;
import com.quanzi.utils.MD5For32;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 * �����ƣ�ModifyPassActivity
 * ���������޸�����
 * �����ˣ� ��˧
 * ����ʱ�䣺2014��7��27�� ����3:54:33
 *
 */
public class ModifyPassActivity extends BaseActivity implements OnClickListener {
	private TextView saveBtn;
	private View backBtn;
	private EditText mOldPassView;//������
	private EditText mNewPassView;//������
	private EditText mConformPassView;//ȷ������
	private UserPreference userPreference;

	private String oldPass;
	private String newPass;
	private String confirmPass;
	View focusView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_pass);
		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		saveBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
		mOldPassView = (EditText) findViewById(R.id.oldpass);
		mNewPassView = (EditText) findViewById(R.id.newpass);
		mConformPassView = (EditText) findViewById(R.id.conform_password);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	private void attemptModifyPass() {
		// ���ô���
		mOldPassView.setError(null);
		mNewPassView.setError(null);
		mConformPassView.setError(null);

		// �洢�û�ֵ
		oldPass = mOldPassView.getText().toString();
		newPass = mNewPassView.getText().toString();
		confirmPass = mConformPassView.getText().toString();
		boolean cancel = false;

		//��������
		if (TextUtils.isEmpty(oldPass)) {
			mOldPassView.setError(getString(R.string.error_field_required));
			focusView = mOldPassView;
			cancel = true;
		} else if (!CommonTools.isPassValid(oldPass)) {
			mOldPassView.setError(getString(R.string.error_pattern_password));
			focusView = mOldPassView;
			cancel = true;
		}

		// �������
		if (TextUtils.isEmpty(newPass)) {
			mNewPassView.setError(getString(R.string.error_field_required));
			focusView = mNewPassView;
			cancel = true;
		} else if (!CommonTools.isPassValid(newPass)) {
			mNewPassView.setError(getString(R.string.error_pattern_password));
			focusView = mNewPassView;
			cancel = true;
		}

		// ����ظ�����
		else if (TextUtils.isEmpty(confirmPass)) {
			mConformPassView.setError(getString(R.string.error_field_required));
			focusView = mConformPassView;
			cancel = true;
		} else if (!confirmPass.equals(newPass)) {
			mConformPassView.setError(getString(R.string.error_field_conform_pass));
			focusView = mConformPassView;
			cancel = true;
		}

		if (cancel) {
			// �����������ʾ����
			focusView.requestFocus();
		} else {
			// û�д������޸�
			RequestParams params = new RequestParams();
			params.put(UserTable.U_PASSWORD, MD5For32.GetMD5Code(oldPass));
			params.put(UserTable.U_NEW_PASSWORD, MD5For32.GetMD5Code(newPass));
			params.put(UserTable.U_ID, userPreference.getU_id());
			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("���Ժ�...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					if (arg0 == 200) {
						if (arg2.equals("1")) {
							ToastTool.showShort(ModifyPassActivity.this, "�޸ĳɹ���");
							reLogin();
							//							userPreference.setU_password(newPass);
							//							finish();
						} else if (arg2.equals("-1")) {
							mOldPassView.setError("�����벻��ȷ");
							focusView = mOldPassView;
							focusView.requestFocus();
						}
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					ToastTool.showLong(ModifyPassActivity.this, "����������");
				}
			};
			AsyncHttpClientTool.post("updateuserpwd", params, responseHandler);
		}
	}

	/**
	 * �޸���������µ�¼
	 */
	private void reLogin() {
		//�����û�������¼
		BaseApplication.getInstance().logout();
		userPreference.clear();
		Intent intent = new Intent(ModifyPassActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			break;
		case R.id.publish_btn:
			attemptModifyPass();
			break;
		default:
			break;
		}
	}
}
