package com.quanzi.jsonobject;

import java.io.Serializable;
import java.util.Date;

public class JsonConcern implements Serializable{
	
	private int user_id;
	private String user_name;
	private String user_small_avatar;
	private String user_gender;
	
	private Date concern_date;
	private boolean is_concern;
	
	private boolean is_read;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_small_avatar() {
		return user_small_avatar;
	}

	public void setUser_small_avatar(String user_small_avatar) {
		this.user_small_avatar = user_small_avatar;
	}

	public Date getConcern_date() {
		return concern_date;
	}

	public void setConcern_date(Date concern_date) {
		this.concern_date = concern_date;
	}

	public boolean isIs_concern() {
		return is_concern;
	}

	public void setIs_concern(boolean is_concern) {
		this.is_concern = is_concern;
	}

	public boolean isIs_read() {
		return is_read;
	}

	public void setIs_read(boolean is_read) {
		this.is_read = is_read;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	
	
}
