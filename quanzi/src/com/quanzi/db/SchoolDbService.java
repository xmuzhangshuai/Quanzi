package com.quanzi.db;

import java.util.List;

import android.content.Context;

import com.quanquan.dao.DaoSession;
import com.quanquan.dao.SchoolDao;
import com.quanquan.dao.SchoolDao.Properties;
import com.quanquan.entities.City;
import com.quanquan.entities.School;
import com.quanzi.base.BaseApplication;

public class SchoolDbService {
	private static final String TAG = SchoolDbService.class.getSimpleName();
	private static SchoolDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SchoolDao schoolDao;

	public SchoolDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static SchoolDbService getInstance(Context context) {
		if (instance == null) {
			instance = new SchoolDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.schoolDao = instance.mDaoSession.getSchoolDao();
		}
		return instance;
	}

	/**
	 * 根据城市返回学校列表
	 * @return
	 */
	public List<School> getSchoolListByCity(City c) {
		return schoolDao.queryBuilder().where(Properties.CityID.eq(c.getCityID())).list();
	}

	public String getSchoolNameById(int schoolid) {
		School school = null;
		if (schoolid > -1) {
			school = schoolDao.load((long) schoolid);
		}
		if (school != null) {
			return school.getSchoolName();
		} else {
			return "";
		}
	}
}
