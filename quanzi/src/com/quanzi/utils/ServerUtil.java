package com.quanzi.utils;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.UserTable;
import com.quanzi.ui.LoginOrRegisterActivity;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ServerUtil  
 * 类描述：一些公用的网络操作类
 * @author zhangshuai
 * @date 创建时间：2015-7-7 下午4:26:35 
 *
 */
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
	public <T> void login(final Context context, final Class<T> cls) {
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
						Intent intent = new Intent(context, cls);
						context.startActivity(intent);
						((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						((Activity) context).finish();
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
		if (NetworkUtils.isNetworkAvailable(context)) {
			AsyncHttpClientTool.post("user/login", params, responseHandler);
		} else {
			Intent intent = new Intent(context, cls);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			((Activity) context).finish();
		}

	}

	/**
	 * 存储自己的信息
	 */
	private static void saveUser(final JsonUser user) {
		// TODO Auto-generated method stub
		instance.userPreference.setU_id(user.getU_id());
		instance.userPreference.setU_nickname(user.getU_nickname());
		instance.userPreference.setU_password(user.getU_password());
		instance.userPreference.setU_gender(user.getU_gender());
		instance.userPreference.setU_tel(user.getU_tel());
		instance.userPreference.setU_email(user.getU_email());
		instance.userPreference.setU_birthday(user.getU_birthday());
		instance.userPreference.setU_age(user.getU_age());
		instance.userPreference.setU_large_avatar(user.getU_large_avatar());
		instance.userPreference.setU_small_avatar(user.getU_small_avatar());
		instance.userPreference.setU_identity(user.getU_identity());
		instance.userPreference.setU_love_state(user.getU_love_tate());
		instance.userPreference.setU_provinceid(user.getU_provinceid());
		instance.userPreference.setU_cityid(user.getU_cityid());
		instance.userPreference.setU_schoolid(user.getU_schoolid());
		instance.userPreference.setU_interest_ids(user.getU_interest_ids());
		instance.userPreference.setU_skill_ids(user.getU_skill_ids());
		instance.userPreference.setU_industry_id(user.getU_industry_id());
		instance.userPreference.setU_introduce(user.getU_introduce());
		instance.userPreference.setU_student_number(user.getU_student_number());
		instance.userPreference.setU_student_pass(user.getU_stundet_pass());
		instance.userPreference.setUserLogin(true);
	}
}
