package com.quanzi.config;

public class Constants {

	//包名
	public static final String PACKAGENAME = "com.quanzi";

	//	public static String testIP = "http://192.168.1.101/";

	//域名或者是IP
	//	public static String AppliactionServerIP = "http://121.40.101.36/";

	public static String AppliactionServerDomain = "http://192.168.1.109:8080/XiaoYuanQuanQuan/";

	//	public static String ImageServerIP = "http://121.40.92.222/";

	public static String ImageServerDomain = "http://image.yixianqian.me/YXQServer/";

	public static class Config {
		//是否处于开发模式
		public static final boolean DEVELOPER_MODE = true;

		//接受验证码时间为120s
		public static int AUTN_CODE_TIME = 120;

		//照片缩小比例
		public static final int SCALE = 5;

		// 总共有多少页
		public static final int NUM_PAGE = 6;

		// 每页20个表情,还有最后一个删除button
		public static int NUM = 20;

		public static int PAGE_NUM = 20;

		//聊天每次刷新纪录条数
		public static int LOAD_MESSAGE_COUNT = 20;
	}

	//情感状态
	public static class LoveState {
		//单身
		public static final String SINGLE = "single";
		//恋爱
		public static final String INLOVE = "in_love";
		//已婚
		public static final String MARRIED = "married";
	}

	//身份
	public static class Identity {
		//学生
		public static final String STUDENT = "student";
		//校友
		public static final String SCHOOLMATE = "schoolmate";
	}

	//审核状态
	public static class VertifyState {
		//通过
		public static final int PASSED = 2;
		//正在审核
		public static final int VERTIFING = 1;
		//未通过
		public static final int NOTPASSED = -1;
		//未提交
		public static final int NOTSUBMIT = 0;
	}

	public static class BaiduPushConfig {
		public final static String API_KEY = "bcB8dCkZh0GVtMTdpyTTabj3";
		public final static String SECRIT_KEY = "y75ge4lbEEGth1nwbTveiGr7yHARKnm2";
	}

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public static class Gender {
		public static final String MALE = "男";
		public static final String FEMALE = "女";
	}

}
