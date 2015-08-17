package com.quanzi.config;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	//����
	public static final String PACKAGENAME = "com.quanzi";

	//	public static String testIP = "http://192.168.1.101/";

	//����������IP
	//	public static String AppliactionServerIP = "http://121.40.101.36/";

	public static String AppliactionServerDomain = "http://192.168.1.100:8080/XiaoYuanQuanQuan/";

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

	//��������
	public static class CommentType {
		//����
		public static final String COMMENT = "COMMENT";
		//�ظ�
		public static final String REPLY = "REPLY";
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

	//�����
	public static class ActivityType {
		public static final String JIANGZUO = "����";
		public static final String ZHAOPIN = "��Ƹ";
		public static final String SHETUAN = "����";
		public static final String YINYUE = "����";
		public static final String DIANYING = "��Ӱ";
		public static final String YUNDONG = "�˶�";
		public static final String JUHUI = "�ۻ�";
		public static final String ZHANLAN = "չ��";
		public static final String GONGYI = "����";
		public static final String YOUHUIQUAN = "�Ż�ȯ";

		public static List<String> getList() {
			List<String> temp = new ArrayList<String>();
			temp.add(JIANGZUO);
			temp.add(ZHAOPIN);
			temp.add(SHETUAN);
			temp.add(YINYUE);
			temp.add(DIANYING);
			temp.add(YUNDONG);
			temp.add(JUHUI);
			temp.add(ZHANLAN);
			temp.add(GONGYI);
			temp.add(YOUHUIQUAN);
			return temp;
		}
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
