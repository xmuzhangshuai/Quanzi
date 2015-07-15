package com.quanzi.jsonobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonActItem implements Serializable {
	private int a_id;
	private int a_actid;

	private String a_title;
	private int a_userid;
	private String a_username;
	private String a_small_avatar;
	private String a_large_avatar;
	private String a_gender;
	private Date a_time;
	private String a_detail_content;
	private String a_thumbnail;
	private String a_big_photo;
	private Date a_act_date;
	private String a_act_location;
	private String a_act_target;
	private String a_act_type;
	private int a_comment_count;
	private int a_favor_count;
	private boolean like;

	private List<Map<String, String>> commentList;

	public JsonActItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonActItem(int a_id, String a_title, int a_userid, String a_username, String a_small_avatar,
			String a_large_avatar, String a_gender, Date a_time, String a_detail_content, String a_thumbnail,
			String a_big_photo, Date a_act_date, String a_act_location, String a_act_target, String a_act_type,
			int a_comment_count, int a_favor_count) {
		super();
		this.a_id = a_id;
		this.a_title = a_title;
		this.a_userid = a_userid;
		this.a_username = a_username;
		this.a_small_avatar = a_small_avatar;
		this.a_large_avatar = a_large_avatar;
		this.a_gender = a_gender;
		this.a_time = a_time;
		this.a_detail_content = a_detail_content;
		this.a_thumbnail = a_thumbnail;
		this.a_big_photo = a_big_photo;
		this.a_act_date = a_act_date;
		this.a_act_location = a_act_location;
		this.a_act_target = a_act_target;
		this.a_act_type = a_act_type;
		this.a_comment_count = a_comment_count;
		this.a_favor_count = a_favor_count;
	}

	public int getA_id() {
		return a_id;
	}

	public void setA_id(int a_id) {
		this.a_id = a_id;
	}

	public int getA_actid() {
		return a_actid;
	}

	public void setA_actid(int a_actid) {
		this.a_actid = a_actid;
	}

	public String getA_title() {
		return a_title;
	}

	public void setA_title(String a_title) {
		this.a_title = a_title;
	}

	public int getA_userid() {
		return a_userid;
	}

	public void setA_userid(int a_userid) {
		this.a_userid = a_userid;
	}

	public String getA_username() {
		return a_username;
	}

	public void setA_username(String a_username) {
		this.a_username = a_username;
	}

	public String getA_small_avatar() {
		return a_small_avatar;
	}

	public void setA_small_avatar(String a_small_avatar) {
		this.a_small_avatar = a_small_avatar;
	}

	public String getA_large_avatar() {
		return a_large_avatar;
	}

	public void setA_large_avatar(String a_large_avatar) {
		this.a_large_avatar = a_large_avatar;
	}

	public String getA_gender() {
		return a_gender;
	}

	public void setA_gender(String a_gender) {
		this.a_gender = a_gender;
	}

	public Date getA_time() {
		return a_time;
	}

	public void setA_time(Date a_time) {
		this.a_time = a_time;
	}

	public String getA_detail_content() {
		return a_detail_content;
	}

	public void setA_detail_content(String a_detail_content) {
		this.a_detail_content = a_detail_content;
	}

	public String getA_thumbnail() {
		return a_thumbnail;
	}

	public void setA_thumbnail(String a_thumbnail) {
		this.a_thumbnail = a_thumbnail;
	}

	public String getA_big_photo() {
		return a_big_photo;
	}

	public void setA_big_photo(String a_big_photo) {
		this.a_big_photo = a_big_photo;
	}

	public Date getA_act_date() {
		return a_act_date;
	}

	public void setA_act_date(Date a_act_date) {
		this.a_act_date = a_act_date;
	}

	public String getA_act_location() {
		return a_act_location;
	}

	public void setA_act_location(String a_act_location) {
		this.a_act_location = a_act_location;
	}

	public String getA_act_target() {
		return a_act_target;
	}

	public void setA_act_target(String a_act_target) {
		this.a_act_target = a_act_target;
	}

	public String getA_act_type() {
		return a_act_type;
	}

	public void setA_act_type(String a_act_type) {
		this.a_act_type = a_act_type;
	}

	public int getA_comment_count() {
		return a_comment_count;
	}

	public void setA_comment_count(int a_comment_count) {
		this.a_comment_count = a_comment_count;
	}

	public int getA_favor_count() {
		return a_favor_count;
	}

	public void setA_favor_count(int a_favor_count) {
		this.a_favor_count = a_favor_count;
	}

	public List<Map<String, String>> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Map<String, String>> commentList) {
		this.commentList = commentList;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

}
