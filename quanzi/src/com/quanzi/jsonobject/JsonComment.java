package com.quanzi.jsonobject;

import java.util.Date;

public class JsonComment {

	private int c_id;//评论ID
	private int p_id;//帖子或活动的ID
	private int p_userid;
	private String comment_type;

	//主动评论者信息
	private int c_user_id;
	private String c_user_avatar;
	private String c_user_nickname;
	private String c_user_gender;

	//被动评论者信息
	private String to_user_nickname;
	private String to_user_id;

	//评论内容
	private String c_content;
	private Date c_time;

	public JsonComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonComment(int c_id, int p_id, int p_userid, String comment_type, int c_user_id, String c_user_avatar,
			String c_user_nickname, String c_user_gender, String to_user_nickname, String to_user_id, String c_content,
			Date c_time) {
		super();
		this.c_id = c_id;
		this.p_id = p_id;
		this.p_userid = p_userid;
		this.comment_type = comment_type;
		this.c_user_id = c_user_id;
		this.c_user_avatar = c_user_avatar;
		this.c_user_nickname = c_user_nickname;
		this.c_user_gender = c_user_gender;
		this.to_user_nickname = to_user_nickname;
		this.to_user_id = to_user_id;
		this.c_content = c_content;
		this.c_time = c_time;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public int getP_userid() {
		return p_userid;
	}

	public void setP_userid(int p_userid) {
		this.p_userid = p_userid;
	}

	public String getComment_type() {
		return comment_type;
	}

	public void setComment_type(String comment_type) {
		this.comment_type = comment_type;
	}

	public int getC_user_id() {
		return c_user_id;
	}

	public void setC_user_id(int c_user_id) {
		this.c_user_id = c_user_id;
	}

	public String getC_user_avatar() {
		return c_user_avatar;
	}

	public void setC_user_avatar(String c_user_avatar) {
		this.c_user_avatar = c_user_avatar;
	}

	public String getC_user_nickname() {
		return c_user_nickname;
	}

	public void setC_user_nickname(String c_user_nickname) {
		this.c_user_nickname = c_user_nickname;
	}

	public String getC_user_gender() {
		return c_user_gender;
	}

	public void setC_user_gender(String c_user_gender) {
		this.c_user_gender = c_user_gender;
	}

	public String getTo_user_nickname() {
		return to_user_nickname;
	}

	public void setTo_user_nickname(String to_user_nickname) {
		this.to_user_nickname = to_user_nickname;
	}

	public String getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(String to_user_id) {
		this.to_user_id = to_user_id;
	}

	public String getC_content() {
		return c_content;
	}

	public void setC_content(String c_content) {
		this.c_content = c_content;
	}

	public Date getC_time() {
		return c_time;
	}

	public void setC_time(Date c_time) {
		this.c_time = c_time;
	}

}
