package com.quanzi.jsonobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonPostItem implements Serializable {
	private int p_id;
	private int p_postid;
	private int p_userid;
	private String p_username;
	private String p_small_avatar;
	private String p_large_avatar;
	private String p_gender;
	private String p_text_content;
	private String p_thumbnail;
	private String p_big_photo;
	private Date p_time;
	private int p_comment_count;
	private int p_favor_count;

	private List<Map<String, String>> commentList;

	public JsonPostItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public int getP_postid() {
		return p_postid;
	}

	public void setP_postid(int p_postid) {
		this.p_postid = p_postid;
	}

	public int getP_userid() {
		return p_userid;
	}

	public void setP_userid(int p_userid) {
		this.p_userid = p_userid;
	}

	public String getP_username() {
		return p_username;
	}

	public void setP_username(String p_username) {
		this.p_username = p_username;
	}

	public String getP_small_avatar() {
		return p_small_avatar;
	}

	public void setP_small_avatar(String p_small_avatar) {
		this.p_small_avatar = p_small_avatar;
	}

	public String getP_large_avatar() {
		return p_large_avatar;
	}

	public void setP_large_avatar(String p_large_avatar) {
		this.p_large_avatar = p_large_avatar;
	}

	public String getP_gender() {
		return p_gender;
	}

	public void setP_gender(String p_gender) {
		this.p_gender = p_gender;
	}

	public String getP_text_content() {
		return p_text_content;
	}

	public void setP_text_content(String p_text_content) {
		this.p_text_content = p_text_content;
	}

	public String getP_thumbnail() {
		return p_thumbnail;
	}

	public void setP_thumbnail(String p_thumbnail) {
		this.p_thumbnail = p_thumbnail;
	}

	public String getP_big_photo() {
		return p_big_photo;
	}

	public void setP_big_photo(String p_big_photo) {
		this.p_big_photo = p_big_photo;
	}

	public Date getP_time() {
		return p_time;
	}

	public void setP_time(Date p_time) {
		this.p_time = p_time;
	}

	public int getP_comment_count() {
		return p_comment_count;
	}

	public void setP_comment_count(int p_comment_count) {
		this.p_comment_count = p_comment_count;
	}

	public int getP_favor_count() {
		return p_favor_count;
	}

	public void setP_favor_count(int p_favor_count) {
		this.p_favor_count = p_favor_count;
	}

	public List<Map<String, String>> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Map<String, String>> commentList) {
		this.commentList = commentList;
	}

}
