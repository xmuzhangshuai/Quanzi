package com.quanzi.config;

public class Constants {

	//����
	public static final String PACKAGENAME = "com.quanzi";

	//	public static String testIP = "http://192.168.1.101/";

	//����������IP
	//	public static String AppliactionServerIP = "http://121.40.101.36/";

	public static String AppliactionServerDomain = "http://192.168.1.109:8080/XiaoYuanQuanQuan/";

	//	public static String ImageServerIP = "http://121.40.92.222/";

	public static String ImageServerDomain = "http://image.yixianqian.me/YXQServer/";

	public static class Config {
		//�Ƿ��ڿ���ģʽ
		public static final boolean DEVELOPER_MODE = true;

		//������֤��ʱ��Ϊ120s
		public static int AUTN_CODE_TIME = 120;

		//��Ƭ��С����
		public static final int SCALE = 5;

		// �ܹ��ж���ҳ
		public static final int NUM_PAGE = 6;

		// ÿҳ20������,�������һ��ɾ��button
		public static int NUM = 20;

		public static int PAGE_NUM = 20;

		//����ÿ��ˢ�¼�¼����
		public static int LOAD_MESSAGE_COUNT = 20;
	}

	//���״̬
	public static class LoveState {
		//����
		public static final String SINGLE = "single";
		//����
		public static final String INLOVE = "in_love";
		//�ѻ�
		public static final String MARRIED = "married";
	}

	//���
	public static class Identity {
		//ѧ��
		public static final String STUDENT = "student";
		//У��
		public static final String SCHOOLMATE = "schoolmate";
	}

	//���״̬
	public static class VertifyState {
		//ͨ��
		public static final int PASSED = 2;
		//�������
		public static final int VERTIFING = 1;
		//δͨ��
		public static final int NOTPASSED = -1;
		//δ�ύ
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
		public static final String MALE = "��";
		public static final String FEMALE = "Ů";
	}

}
