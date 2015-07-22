package com.quanzi.jsonobject;

import java.io.Serializable;
import java.util.Date;

import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.LogTool;

/**
 * 
* @Description: TODO(User.java��) 
* @author lks   
* @date 2014��7��3�� ����11:43:53 
* @version V1.0
 */

public class JsonUser implements Serializable {

	private int u_id;
	private String u_nickname;
	private String u_password;
	private String u_gender;
	private String u_tel;
	private String u_email;
	private Date u_birthday;
	private int u_age;
	private String u_large_avatar;
	private String u_small_avatar;
	private String u_identity;
	private String u_love_state;
	private int u_provinceid;
	private int u_cityid;
	private int u_schoolid;
	private String u_interest_items;
	private String u_skill_items;
	private String u_industry_item;
	private String u_introduce;
	private int u_post_amount;
	private int u_act_amount;
	private int u_my_concern_count;
	private int u_my_follower_count;
	private int u_my_favor_count;
	private int u_new_follower_count;

	private String u_student_number;//ѧ��
	private String u_stundet_pass;//����

	//��ӡ�û���Ϣ
	public void printUserInfo() {
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
		LogTool.i("����ID: " + getU_cityid());
		LogTool.i("ѧУID: " + getU_schoolid());
		LogTool.i("���: " + getU_introduce());
		LogTool.i("ѧ��: " + getU_student_number());
		LogTool.i("��������: " + getU_stundet_pass());
		LogTool.i("���: " + getU_identity());
		LogTool.i("��ҵ: " + getU_industry_item());
		LogTool.i("����: " + getU_skill_items());
		LogTool.i("���״̬: " + getU_love_state());
		LogTool.i("��Ȥ: " + getU_interest_items());
		LogTool.i("��ע������: " + getU_my_concern_count());
		LogTool.i("׷��������: " + getU_my_follower_count());
		LogTool.i("�޹������ӵ�����: " + getU_my_favor_count());
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getU_nickname() {
		return u_nickname;
	}

	public void setU_nickname(String u_nickname) {
		this.u_nickname = u_nickname;
	}

	public String getU_password() {
		return u_password;
	}

	public void setU_password(String u_password) {
		this.u_password = u_password;
	}

	public String getU_gender() {
		return u_gender;
	}

	public void setU_gender(String u_gender) {
		this.u_gender = u_gender;
	}

	public String getU_tel() {
		return u_tel;
	}

	public void setU_tel(String u_tel) {
		this.u_tel = u_tel;
	}

	public String getU_email() {
		return u_email;
	}

	public void setU_email(String u_email) {
		this.u_email = u_email;
	}

	public Date getU_birthday() {
		return u_birthday;
	}

	public void setU_birthday(Date u_birthday) {
		this.u_birthday = u_birthday;
	}

	public int getU_age() {
		return u_age;
	}

	public void setU_age(int u_age) {
		this.u_age = u_age;
	}

	public String getU_large_avatar() {
		return u_large_avatar;
	}

	public void setU_large_avatar(String u_large_avatar) {
		this.u_large_avatar = u_large_avatar;
	}

	public String getU_small_avatar() {
		return u_small_avatar;
	}

	public void setU_small_avatar(String u_small_avatar) {
		this.u_small_avatar = u_small_avatar;
	}

	public String getU_identity() {
		return u_identity;
	}

	public void setU_identity(String u_identity) {
		this.u_identity = u_identity;
	}

	public int getU_provinceid() {
		return u_provinceid;
	}

	public void setU_provinceid(int u_provinceid) {
		this.u_provinceid = u_provinceid;
	}

	public int getU_cityid() {
		return u_cityid;
	}

	public void setU_cityid(int u_cityid) {
		this.u_cityid = u_cityid;
	}

	public int getU_schoolid() {
		return u_schoolid;
	}

	public void setU_schoolid(int u_schoolid) {
		this.u_schoolid = u_schoolid;
	}

	public String getU_interest_items() {
		return u_interest_items;
	}

	public void setU_interest_items(String u_interest_items) {
		this.u_interest_items = u_interest_items;
	}

	public String getU_skill_items() {
		return u_skill_items;
	}

	public void setU_skill_items(String u_skill_items) {
		this.u_skill_items = u_skill_items;
	}

	public String getU_industry_item() {
		return u_industry_item;
	}

	public void setU_industry_item(String u_industry_item) {
		this.u_industry_item = u_industry_item;
	}

	public String getU_introduce() {
		return u_introduce;
	}

	public void setU_introduce(String u_introduce) {
		this.u_introduce = u_introduce;
	}

	public int getU_post_amount() {
		return u_post_amount;
	}

	public void setU_post_amount(int u_post_amount) {
		this.u_post_amount = u_post_amount;
	}

	public int getU_act_amount() {
		return u_act_amount;
	}

	public void setU_act_amount(int u_act_amount) {
		this.u_act_amount = u_act_amount;
	}

	public String getU_student_number() {
		return u_student_number;
	}

	public void setU_student_number(String u_student_number) {
		this.u_student_number = u_student_number;
	}

	public String getU_stundet_pass() {
		return u_stundet_pass;
	}

	public void setU_stundet_pass(String u_stundet_pass) {
		this.u_stundet_pass = u_stundet_pass;
	}

	public String getU_love_state() {
		return u_love_state;
	}

	public void setU_love_state(String u_love_state) {
		this.u_love_state = u_love_state;
	}

	public int getU_my_concern_count() {
		return u_my_concern_count;
	}

	public void setU_my_concern_count(int u_my_concern_count) {
		this.u_my_concern_count = u_my_concern_count;
	}

	public int getU_my_follower_count() {
		return u_my_follower_count;
	}

	public void setU_my_follower_count(int u_my_follower_count) {
		this.u_my_follower_count = u_my_follower_count;
	}

	public int getU_my_favor_count() {
		return u_my_favor_count;
	}

	public void setU_my_favor_count(int u_my_favor_count) {
		this.u_my_favor_count = u_my_favor_count;
	}

	public int getU_new_follower_count() {
		return u_new_follower_count;
	}

	public void setU_new_follower_count(int u_new_follower_count) {
		this.u_new_follower_count = u_new_follower_count;
	}

	public JsonUser() {
		super();
	}

	public JsonUser(String u_nickname, String u_password, String u_gender, String u_tel, int u_provinceid,
			int u_cityid, int u_schoolid, String u_student_number, String u_stundet_pass) {
		super();
		this.u_nickname = u_nickname;
		this.u_password = u_password;
		this.u_gender = u_gender;
		this.u_tel = u_tel;
		this.u_provinceid = u_provinceid;
		this.u_cityid = u_cityid;
		this.u_schoolid = u_schoolid;
		this.u_student_number = u_student_number;
		this.u_stundet_pass = u_stundet_pass;
	}

}
