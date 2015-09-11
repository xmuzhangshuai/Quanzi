package com.quanzi.utils;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.easemob.chat.EMChatManager;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.db.UserDbService;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.UserTable;
import com.quanzi.ui.LoginActivity;
import com.quanzi.ui.LoginOrRegisterActivity;
import com.quanzi.ui.PersonDetailActivity;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�ServerUtil  
 * ��������һЩ���õ����������
 * @author zhangshuai
 * @date ����ʱ�䣺2015-7-7 ����4:26:35 
 *
 */
public class ServerUtil {
	private static ServerUtil instance;
	private UserPreference userPreference;

	public ServerUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
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
	 * �û���¼
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
					if (response.equals("-1")) {
						LogTool.e("ServerUtil", "����-1���û������������");
						BaseApplication.getInstance().logout();
						userPreference.clear();
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);
						((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						((Activity) context).finish();
					} else {
						JsonUser jsonUser = FastJsonTool.getObject(response, JsonUser.class);
						if (jsonUser != null) {
							LogTool.i("ServerUtil", "��¼�ɹ���");
							saveUser(jsonUser);

							// �ӱ������ݿ���������¼���ڴ�
							try {
								EMChatManager.getInstance().loadAllConversations();
							} catch (Exception e) {
								// TODO: handle exception
							}
							Intent intent = new Intent(context, cls);
							context.startActivity(intent);
							((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							((Activity) context).finish();
						} else {
							LogTool.e("ServerUtil", "jsonUserΪ��");
							Intent intent = new Intent(context, LoginOrRegisterActivity.class);
							context.startActivity(intent);
							((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						}
					}

				} else {
					LogTool.e("ServerUtil", "��¼����Ϊ��");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("ServerUtil", "��¼������ʧ��");
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
	 * �洢�Լ�����Ϣ
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
		instance.userPreference.setU_love_state(user.getU_love_state());
		instance.userPreference.setU_provinceid(user.getU_provinceid());
		instance.userPreference.setU_cityid(user.getU_cityid());
		instance.userPreference.setU_schoolid(user.getU_schoolid());
		instance.userPreference.setU_interests(user.getU_interest_items());
		instance.userPreference.setU_skills(user.getU_skill_items());
		instance.userPreference.setU_industry(user.getU_industry_item());
		instance.userPreference.setU_introduce(user.getU_introduce());
		instance.userPreference.setU_student_number(user.getU_student_number());
		instance.userPreference.setU_student_pass(user.getU_student_pass());
		instance.userPreference.setMyConcerned_count(user.getU_my_concern_count());
		instance.userPreference.setMyFollower_count(user.getU_my_follower_count());
		instance.userPreference.setMyFavor_count(user.getU_my_favor_count());
		instance.userPreference.setNewMyFollower_count(user.getU_new_follower_count());
		instance.userPreference.setUserLogin(true);
	}

	public static void getUser(final Context context, int userId) {
		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("���ڼ���...");
		dialog.setCancelable(false);
		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userId);

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				dialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					JsonUser jsonUser = FastJsonTool.getObject(response, JsonUser.class);
					if (jsonUser != null) {
						UserDbService.getInstance(context).saveUser(jsonUser);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("��ȡ�û�����������");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		AsyncHttpClientTool.post("user/getInfoByID", params, responseHandler);
	}
}
