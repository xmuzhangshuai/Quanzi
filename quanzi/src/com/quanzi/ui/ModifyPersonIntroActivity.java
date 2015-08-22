package com.quanzi.ui;

import org.apache.http.Header;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ModifyPersonIntroActivity  
 * 类描述：修改个人签名
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午6:26:24 
 *
 */
public class ModifyPersonIntroActivity extends BaseActivity implements OnClickListener {

	private EditText introEditText;
	private TextView saveBtn;
	private View backBtn;
	private UserPreference userPreference;
	View focusView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_personintro);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		introEditText = (EditText) findViewById(R.id.intro);
		saveBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		introEditText.setText(userPreference.getU_introduce());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.publish_btn:
			updateIntro(introEditText.getText().toString().trim());
			break;

		default:
			break;
		}
	}

	/**
	 * 修改个人简介
	 * @param name
	 */
	public void updateIntro(final String intro) {
		if (!intro.equals(userPreference.getU_introduce())) {
			// 重置错误
			introEditText.setError(null);

			// 存储用户值
			boolean cancel = false;
			if (TextUtils.isEmpty(intro)) {
				introEditText.setError(getString(R.string.error_field_required));
				focusView = introEditText;
				cancel = true;
			}

			if (cancel) {
				// 如果错误，则提示错误
				focusView.requestFocus();
			} else {
				// 没有错误，则修改
				RequestParams params = new RequestParams();
				params.put(UserTable.U_ID, userPreference.getU_id());
				params.put(UserTable.U_INTRODUCE, intro);

				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
					Dialog dialog;

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						dialog = showProgressDialog("请稍后...");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						super.onFinish();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, String response) {
						// TODO Auto-generated method stub
						if (statusCode == 200) {
							if (response.equals("1")) {
								ToastTool.showShort(ModifyPersonIntroActivity.this, "修改成功！");
								userPreference.setU_introduce(intro);
								finish();
								overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
							} else if (response.equals("-1")) {
								LogTool.e("修改个人介绍返回-1");
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("修改个人介绍错误");
					}
				};
				AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
			}
		} else {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
	}
}
