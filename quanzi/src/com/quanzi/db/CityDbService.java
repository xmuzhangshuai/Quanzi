package com.quanzi.db;

import java.util.List;

import com.quanzi.base.BaseApplication;
import com.quanzi.dao.CityDao;
import com.quanzi.dao.DaoSession;
import com.quanzi.dao.CityDao.Properties;
import com.quanzi.entities.City;
import com.quanzi.entities.Province;

import android.content.Context;



public class CityDbService {
	private static final String TAG = CityDbService.class.getSimpleName();
	private static CityDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public CityDao cityDao;

	public CityDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static CityDbService getInstance(Context context) {
		if (instance == null) {
			instance = new CityDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.cityDao = instance.mDaoSession.getCityDao();
		}
		return instance;
	}

	/**
	 * 根据省份返回城市列表
	 * @return
	 */
	public List<City> getCityListByProvince(Province p) {
		return cityDao.queryBuilder().where(Properties.ProvinceID.eq(p.getProvinceID())).list();
	}

	public String getCityNameById(int cityId) {
		City city = null;
		if (cityId > -1) {
			city = cityDao.queryBuilder().where(Properties.CityID.eq(cityId)).unique();
		}

		if (city != null) {
			return city.getCityName();
		} else {
			return "";
		}
	}
}
