package com.quanzi.jsonobject;

import java.io.Serializable;

public class JsonQuanZiItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int i_id;
	private String i_name;
	private int amount;
	public int getI_id() {
		return i_id;
	}
	public void setI_id(int i_id) {
		this.i_id = i_id;
	}
	public String getI_name() {
		return i_name;
	}
	public void setI_name(String i_name) {
		this.i_name = i_name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
