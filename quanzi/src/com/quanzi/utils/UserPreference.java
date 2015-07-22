package com.quanzi.utils;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

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
	public static final String USER_SHAREPREFERENCE = "userSharePreference";//�û�SharePreference
	private Context context;

	public UserPreference(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(USER_SHAREPREFERENCE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	//��ӡ�û���Ϣ
	public void printUserInfo() {
		LogTool.i("�Ƿ��¼: " + getUserLogin());
		LogTool.i("�û�ID: " + getU_id());
		LogTool.i("�ǳ�: " + getU_nickname());
		LogTool.i("����: " + getU_password());
		LogTool.i("�Ա�: " + getU_gender());
		LogTool.i("�ֻ���: " + getU_tel());
		LogTool.i("����: " + getU_email());
		LogTool.i("����: " + DateTimeTools.DateToString(getU_birthday()));
		LogTool.i("����: " + getU_age());
		LogTool.i("��ͷ��: " + getU_large_avatar());
		LogTool.i("Сͷ��: " + getU_small_avatar());
		LogTool.i("ʡ��ID: " + getU_provinceid());
		LogTool.i("ʡ��: " + getProvinceName());
		LogTool.i("����ID: " + getU_cityid());
		LogTool.i("����: " + getCityName());
		LogTool.i("ѧУID: " + getU_schoolid());
		LogTool.i("ѧУ: " + getSchoolName());
		LogTool.i("���: " + getU_introduce());
		LogTool.i("ѧ��: " + getU_student_number());
		LogTool.i("��������: " + getU_student_pass());
		LogTool.i("���: " + getU_identity());
		LogTool.i("��ҵ: " + getU_industry());
		LogTool.i("����: " + getU_skills());
		LogTool.i("���״̬: " + getU_love_state());
		LogTool.i("��Ȥ: " + getU_interests());
		LogTool.i("��ע������: " + getMyConcerned_count());
		LogTool.i("׷��������: " + getMyFollower_count());
		LogTool.i("�޹������ӵ�����: " + getMyFavor_count());
	}

	/**
	 * �������
	 */
	public void clear() {
		String tel = getU_tel();
		editor.clear();
		setU_tel(tel);
		editor.commit();
	}

	//��¼�û��Ƿ��¼
	public boolean getUserLogin() {
		return sp.getBoolean("login", false);
	}

	public void setUserLogin(boolean login) {
		editor.putBoolean("login", login);
		editor.commit();
	}

	//�û�ID
	public int getU_id() {
		return sp.getInt(UserTable.U_ID, -1);
	}

	public void setU_id(int u_id) {
		editor.putInt(UserTable.U_ID, u_id);
		editor.commit();
	}

	//�û��ǳ�
	public String getU_nickname() {
		return sp.getString(UserTable.U_NICKNAME, "");
	}

	public void setU_nickname(String u_nickname) {
		editor.putString(UserTable.U_NICKNAME, u_nickname);
		editor.commit();
	}

	//����
	public String getU_password() {
		return sp.getString(UserTable.U_PASSWORD, "");
	}

	public void setU_password(String u_password) {
		editor.putString(UserTable.U_PASSWORD, u_password);
		editor.commit();
	}

	//�Ա�
	public String getU_gender() {
		return sp.getString(UserTable.U_GENDER, "");
	}

	public void setU_gender(String u_gender) {
		editor.putString(UserTable.U_GENDER, u_gender);
		editor.commit();
	}

	//�ֻ���
	public String getU_tel() {
		return sp.getString(UserTable.U_TEL, "");
	}

	public void setU_tel(String u_tel) {
		editor.putString(UserTable.U_TEL, u_tel);
		editor.commit();
	}

	//����
	public String getU_email() {
		return sp.getString(UserTable.U_EMAIL, "");
	}

	public void setU_email(String u_email) {
		editor.putString(UserTable.U_EMAIL, u_email);
		editor.commit();
	}

	//����
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

	//����
	public int getU_age() {
		return sp.getInt(UserTable.U_AGE, -1);
	}

	public void setU_age(int u_age) {
		editor.putInt(UserTable.U_AGE, u_age);
		editor.commit();
	}

	//��ͷ��
	public String getU_large_avatar() {
		return sp.getString(UserTable.U_LARGE_AVATAR, "");
	}

	public void setU_large_avatar(String u_large_avatar) {
		editor.putString(UserTable.U_LARGE_AVATAR, u_large_avatar);
		editor.commit();
	}

	//Сͷ��
	public String getU_small_avatar() {
		return sp.getString(UserTable.U_SMALL_AVATAR, "");
	}

	public void setU_small_avatar(String u_small_avatar) {
		editor.putString(UserTable.U_SMALL_AVATAR, u_small_avatar);
		editor.commit();
	}

	//ʡ��
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

	//����
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

	//ѧУ
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

	//�û����
	public String getU_introduce() {
		return sp.getString(UserTable.U_INTRODUCE, "");
	}

	public void setU_introduce(String u_introduce) {
		editor.putString(UserTable.U_INTRODUCE, u_introduce);
		editor.commit();
	}

	//ѧ��
	public String getU_student_number() {
		return sp.getString(UserTable.U_STUDENT_NUMBER, "");
	}

	public void setU_student_number(String stundet_number) {
		editor.putString(UserTable.U_STUDENT_NUMBER, stundet_number);
		editor.commit();
	}

	//��������
	public String getU_student_pass() {
		return sp.getString(UserTable.U_STUDENT_PASS, "");
	}

	public void setU_student_pass(String stundet_pass) {
		editor.putString(UserTable.U_STUDENT_PASS, stundet_pass);
		editor.commit();
	}

	//���
	public String getU_identity() {
		return sp.getString(UserTable.U_IDENTITY, "");
	}

	public void setU_identity(String identity) {
		editor.putString(UserTable.U_IDENTITY, identity);
		editor.commit();
	}

	//��ҵ
	public String getU_industry() {
		return sp.getString(UserTable.U_INDUSTRY_ITEM, "");
	}

	public void setU_industry(String industry) {
		editor.putString(UserTable.U_INDUSTRY_ITEM, industry);
		editor.commit();
	}

	//����
	public String getU_skills() {
		return sp.getString(UserTable.U_SKILL_ITEMS, "");
	}

	public void setU_skills(String skills) {
		editor.putString(UserTable.U_SKILL_ITEMS, skills);
		editor.commit();
	}

	//���״̬
	public String getU_love_state() {
		return sp.getString(UserTable.U_LOVE_STATE, "");
	}

	public void setU_love_state(String love_state) {
		editor.putString(UserTable.U_LOVE_STATE, love_state);
		editor.commit();
	}

	//��Ȥ
	public String getU_interests() {
		return sp.getString(UserTable.U_INTEREST_ITEMS, "");
	}

	public void setU_interests(String interests) {
		editor.putString(UserTable.U_INTEREST_ITEMS, interests);
		editor.commit();
	}

	//�ҹ�ע������
	public int getMyConcerned_count() {
		return sp.getInt("my_concerned_count", -1);
	}

	public void setMyConcerned_count(int count) {
		editor.putInt("my_concerned_count", count);
		editor.commit();
	}

	//�ҵ�׷��������
	public int getMyFollower_count() {
		return sp.getInt("my_follower_count", -1);
	}

	public void setMyFollower_count(int count) {
		editor.putInt("my_follower_count", count);
		editor.commit();
	}

	//�µ�׷��������
	public int getNewMyFollower_count() {
		return sp.getInt("new_my_follower_count", -1);
	}

	public void setNewMyFollower_count(int count) {
		editor.putInt("new_my_follower_count", count);
		editor.commit();
	}

	//���޹������ӵ�����
	public int getMyFavor_count() {
		return sp.getInt("my_favor_count", -1);
	}

	public void setMyFavor_count(int count) {
		editor.putInt("my_favor_count", count);
		editor.commit();
	}

}
