package com.quanzi.utils;

import java.util.Date;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.quanzi.dao.CityDao.Properties;
import com.quanzi.db.CityDbService;
import com.quanzi.db.ProvinceDbService;
import com.quanzi.db.SchoolDbService;
import com.quanzi.entities.City;
import com.quanzi.entities.School;
import com.quanzi.table.UserTable;

public class UserPreference {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USER_SHAREPREFERENCE = "userSharePreference";//用户SharePreference
	private Context context;

	public UserPreference(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(USER_SHAREPREFERENCE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		String tel = getU_tel();
		editor.clear();
		setU_tel(tel);
		editor.commit();
	}

	//环信用户名和密码
	public void setHuanXinUserName(String username) {
		editor.putString("huanxin_username", username);
		editor.commit();
	}

	public String getHuanXinUserName() {
		return sp.getString("huanxin_username", "");
	}

	public void setHuanXinPassword(String password) {
		editor.putString("huanxin_password", password);
		editor.commit();
	}

	public String getHuanXinPassword() {
		return sp.getString("huanxin_password", "");
	}

	//记录用户是否登录
	public boolean getUserLogin() {
		return sp.getBoolean("login", false);
	}

	public void setUserLogin(boolean login) {
		editor.putBoolean("login", login);
		editor.commit();
	}

	//用户ID
	public int getU_id() {
		return sp.getInt(UserTable.U_ID, -1);
	}

	public void setU_id(int u_id) {
		editor.putInt(UserTable.U_ID, u_id);
		editor.commit();
	}

	//用户昵称
	public String getU_nickname() {
		return sp.getString(UserTable.U_NICKNAME, "");
	}

	public void setU_nickname(String u_nickname) {
		editor.putString(UserTable.U_NICKNAME, u_nickname);
		editor.commit();
	}

	//密码
	public String getU_password() {
		return sp.getString(UserTable.U_PASSWORD, "");
	}

	public void setU_password(String u_password) {
		editor.putString(UserTable.U_PASSWORD, u_password);
		editor.commit();
	}

	//性别
	public String getU_gender() {
		return sp.getString(UserTable.U_GENDER, "");
	}

	public void setU_gender(String u_gender) {
		editor.putString(UserTable.U_GENDER, u_gender);
		editor.commit();
	}

	//手机号
	public String getU_tel() {
		return sp.getString(UserTable.U_TEL, "");
	}

	public void setU_tel(String u_tel) {
		editor.putString(UserTable.U_TEL, u_tel);
		editor.commit();
	}

	//邮箱
	public String getU_email() {
		return sp.getString(UserTable.U_EMAIL, "");
	}

	public void setU_email(String u_email) {
		editor.putString(UserTable.U_EMAIL, u_email);
		editor.commit();
	}

	//生日
	public Date getU_birthday() {
		Long time = sp.getLong(UserTable.U_BIRTHDAY, 0);
		if (time != 0) {
			return new Date(time);
		} else {
			return null;
		}
	}

	public void setU_birthday(Date u_birthday) {
		if (u_birthday != null) {
			editor.putLong(UserTable.U_BIRTHDAY, u_birthday.getTime());
			editor.commit();
		}
	}

	//年龄
	public int getU_age() {
		return sp.getInt(UserTable.U_AGE, -1);
	}

	public void setU_age(int u_age) {
		editor.putInt(UserTable.U_AGE, u_age);
		editor.commit();
	}

	//大头像
	public String getU_large_avatar() {
		return sp.getString(UserTable.U_LARGE_AVATAR, null);
	}

	public void setU_large_avatar(String u_large_avatar) {
		editor.putString(UserTable.U_LARGE_AVATAR, u_large_avatar);
		editor.commit();
	}

	//小头像
	public String getU_small_avatar() {
		return sp.getString(UserTable.U_SMALL_AVATAR, null);
	}

	public void setU_small_avatar(String u_small_avatar) {
		editor.putString(UserTable.U_SMALL_AVATAR, u_small_avatar);
		editor.commit();
	}

	//省份
	public int getU_provinceid() {
		return sp.getInt(UserTable.U_PROVINCEID, -1);
	}

	public void setU_provinceid(int u_provinceid) {
		ProvinceDbService provinceDbService = ProvinceDbService.getInstance(context);
		setPeovinceName(provinceDbService.getProNameById(u_provinceid));
		editor.putInt(UserTable.U_PROVINCEID, u_provinceid);
		editor.commit();
	}

	public String getProvinceName() {
		return sp.getString("ProvinceName", "");
	}

	public void setPeovinceName(String name) {
		editor.putString("ProvinceName", name);
		editor.commit();
	}

	//城市
	public int getU_cityid() {
		return sp.getInt(UserTable.U_CITYID, -1);
	}

	public void setU_cityid(int u_cityid) {
		CityDbService cityDbService = CityDbService.getInstance(context);
		City city = cityDbService.cityDao.queryBuilder().where(Properties.CityID.eq(u_cityid)).unique();
		if (city != null) {
			setCityName(city.getCityName());
		}
		editor.putInt(UserTable.U_CITYID, u_cityid);
		editor.commit();
	}

	public String getCityName() {
		return sp.getString("cityName", "");
	}

	public void setCityName(String name) {
		editor.putString("cityName", name);
		editor.commit();
	}

	//学校
	public int getU_schoolid() {
		return sp.getInt(UserTable.U_SCHOOLID, -1);
	}

	public void setU_schoolid(int u_schoolid) {
		SchoolDbService schoolDbService = SchoolDbService.getInstance(context);
		School school = schoolDbService.schoolDao.load((long) u_schoolid);
		setSchoolName(school.getSchoolName());
		editor.putInt(UserTable.U_SCHOOLID, u_schoolid);
		editor.commit();
	}

	public String getSchoolName() {
		return sp.getString("schoolName", "");
	}

	public void setSchoolName(String name) {
		editor.putString("schoolName", name);
		editor.commit();
	}

	//用户简介
	public String getU_introduce() {
		return sp.getString(UserTable.U_INTRODUCE, "");
	}

	public void setU_introduce(String u_introduce) {
		editor.putString(UserTable.U_INTRODUCE, u_introduce);
		editor.commit();
	}

	//学号
	public String getU_student_number() {
		return sp.getString(UserTable.U_STUDENT_NUMBER, "");
	}

	public void setU_student_number(String stundet_number) {
		editor.putString(UserTable.U_STUDENT_NUMBER, stundet_number);
		editor.commit();
	}

	//教务处密码
	public String getU_student_pass() {
		return sp.getString(UserTable.U_STUDENT_PASS, "");
	}

	public void setU_student_pass(String stundet_pass) {
		editor.putString(UserTable.U_STUDENT_PASS, stundet_pass);
		editor.commit();
	}

	//身份
	public String getU_identity() {
		return sp.getString(UserTable.U_IDENTITY, "");
	}

	public void setU_identity(String identity) {
		editor.putString(UserTable.U_IDENTITY, identity);
		editor.commit();
	}

	//行业
	public int getU_industry_id() {
		return sp.getInt(UserTable.U_INDUSTRY_ID, -1);
	}

	public void setU_industry_id(int industry_id) {
		editor.putInt(UserTable.U_INDUSTRY_ID, industry_id);
		editor.commit();
	}

	//技能
	public String getU_skill_ids() {
		return sp.getString(UserTable.U_SKILL_IDS, "");
	}

	public void setU_skill_ids(String skill_ids) {
		editor.putString(UserTable.U_SKILL_IDS, skill_ids);
		editor.commit();
	}

	//情感状态
	public String getU_love_state() {
		return sp.getString(UserTable.U_LOVE_STATE, "");
	}

	public void setU_love_state(String love_state) {
		editor.putString(UserTable.U_LOVE_STATE, love_state);
		editor.commit();
	}

	//兴趣
	public String getU_interest_ids() {
		return sp.getString(UserTable.U_INTEREST_IDS, "");
	}

	public void setU_interest_ids(String interest_ids) {
		editor.putString(UserTable.U_INTEREST_IDS, interest_ids);
		editor.commit();
	}
}
