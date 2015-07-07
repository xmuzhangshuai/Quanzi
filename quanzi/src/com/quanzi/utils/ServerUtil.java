package com.quanzi.utils;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.UserTable;
import com.quanzi.ui.LoginOrRegisterActivity;
import com.quanzi.ui.MainActivity;

public class ServerUtil {
	private static ServerUtil instance;
	private UserPreference userPreference;

	public ServerUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static ServerUtil getInstance() {
		if (instance == null) {
			instance = new ServerUtil();
			instance.userPreference = BaseApplication.getInstance().getUserPreference();
		}
		return instance;
	}

	/**
	 * 用户登录
	 */
	public void login(final Context context) {
		String mPhone = userPreference.getU_tel();
		final String mPassword = userPreference.getU_password();

		RequestParams params = new RequestParams();
		params.put(UserTable.U_TEL, mPhone);
		params.put(UserTable.U_PASSWORD, mPassword);

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200 && !TextUtils.isEmpty(response)) {
					JsonUser jsonUser = FastJsonTool.getObject(response, JsonUser.class);
					if (jsonUser != null) {
						LogTool.i("ServerUtil", "登录成功！");
						saveUser(jsonUser);
						//登录环信
						//attempLoginHuanXin(1);
						Intent intent = new Intent(context, MainActivity.class);
						context.startActivity(intent);
						((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

					} else {
						LogTool.e("ServerUtil", "jsonUsers长度为0");
						Intent intent = new Intent(context, LoginOrRegisterActivity.class);
						context.startActivity(intent);
						((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				} else {
					LogTool.e("ServerUtil", "登录返回为空");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
			}
		};
		AsyncHttpClientTool.post("login", params, responseHandler);
	}

	/**
	 * 存储自己的信息
	 */
	private void saveUser(final JsonUser user) {
		// TODO Auto-generated method stub
		LogTool.i("ServerUtil", "存储自身信息");
		userPreference.setU_id(user.getU_id());
		userPreference.setU_nickname(user.getU_nickname());
		userPreference.setU_password(user.getU_password());
		userPreference.setU_gender(user.getU_gender());
		userPreference.setU_tel(user.getU_tel());
		userPreference.setU_email(user.getU_email());
		userPreference.setU_birthday(user.getU_birthday());
		userPreference.setU_age(user.getU_age());
		userPreference.setU_large_avatar(user.getU_large_avatar());
		userPreference.setU_small_avatar(user.getU_small_avatar());
		userPreference.setU_identity(user.getU_identity());
		userPreference.setU_love_state(user.getU_love_tate());
		userPreference.setU_provinceid(user.getU_provinceid());
		userPreference.setU_cityid(user.getU_cityid());
		userPreference.setU_schoolid(user.getU_schoolid());
		userPreference.setU_interest_ids(user.getU_interest_ids());
		userPreference.setU_skill_ids(user.getU_skill_ids());
		userPreference.setU_industry_id(user.getU_industry_id());
		userPreference.setU_introduce(user.getU_introduce());
		userPreference.setU_student_number(user.getU_student_number());
		userPreference.setU_student_pass(user.getU_stundet_pass());
		userPreference.setUserLogin(true);
	}
}
