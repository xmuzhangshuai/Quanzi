package com.quanzi.db;

import android.content.Context;

import com.quanzi.base.BaseApplication;
import com.quanzi.dao.DaoSession;
import com.quanzi.dao.UserDao;
import com.quanzi.dao.UserDao.Properties;
import com.quanzi.entities.User;
import com.quanzi.jsonobject.JsonUser;

public class UserDbService {
	private static final String TAG = UserDbService.class.getSimpleName();
	private static UserDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public UserDao userDao;

	public UserDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static UserDbService getInstance(Context context) {
		if (instance == null) {
			instance = new UserDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.userDao = instance.mDaoSession.getUserDao();
		}
		return instance;
	}

	/**
	 * 插入User
	 * @param jsonUser
	 */
	public User saveUser(JsonUser jsonUser) {
		if (jsonUser != null) {
			User newuser = new User(Long.valueOf(jsonUser.getU_id()), jsonUser.getU_nickname(), jsonUser.getU_gender(),
					jsonUser.getU_tel(), jsonUser.getU_email(), jsonUser.getU_large_avatar(),
					jsonUser.getU_small_avatar(), jsonUser.getU_introduce(), jsonUser.getU_identity(),
					jsonUser.getU_love_state(), jsonUser.getU_age(), jsonUser.getU_birthday(),
					jsonUser.getU_schoolid(), jsonUser.getU_cityid(), jsonUser.getU_provinceid());
			userDao.insertOrReplace(newuser);
			return newuser;
		} else {
			return null;
		}
	}

	public User getUserById(int userId) {
		return userDao.queryBuilder().where(Properties.User_id.eq(userId)).unique();
	}
}
