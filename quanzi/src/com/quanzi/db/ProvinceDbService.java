package com.quanzi.db;

import com.quanquan.dao.DaoSession;
import com.quanquan.dao.ProvinceDao;
import com.quanquan.dao.ProvinceDao.Properties;
import com.quanquan.entities.Province;
import com.quanzi.base.BaseApplication;

import android.content.Context;


public class ProvinceDbService {
	private static final String TAG = ProvinceDbService.class.getSimpleName();
	private static ProvinceDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public ProvinceDao provinceDao;

	public ProvinceDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static ProvinceDbService getInstance(Context context) {
		if (instance == null) {
			instance = new ProvinceDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.provinceDao = instance.mDaoSession.getProvinceDao();
		}
		return instance;
	}

	/**
	 * 根据省份名称返回省份
	 */
	public Province getProvinceByname(String pName) {
		return provinceDao.queryBuilder().where(Properties.ProvinceName.like(pName)).unique();
	}

	/**
	 * 根据ID返回名称
	 * @return
	 */
	public String getProNameById(int provinceID) {
		return provinceDao.queryBuilder().where(Properties.ProvinceID.eq(provinceID)).unique().getProvinceName();
	}
}
