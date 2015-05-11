package com.quanzi.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.quanzi.R;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�CommonTools   
* ��������   ͨ�õĹ�����
* �����ˣ���˧  
* ����ʱ�䣺2013-12-22 ����7:26:29   
* �޸��ˣ���˧    
* �޸�ʱ�䣺2013-12-22 ����7:26:29   
* �޸ı�ע��   
* @version    
*    
*/
public class CommonTools {

	/**
	* �жϳ����Ƿ�������
	* @param context
	* @return
	*/
//	public static boolean isAppRunning(Context context) {
//		boolean isAppRunning = false;
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningTaskInfo> list = am.getRunningTasks(100);
//		for (RunningTaskInfo info : list) {
//			if (info.topActivity.getPackageName().equals(Constants.PACKAGENAME)
//					&& info.baseActivity.getPackageName().equals(Constants.PACKAGENAME)) {
//				isAppRunning = true;
//				//find it, break 
//				break;
//			}
//		}
//		return isAppRunning;
//	}

	/**
	 * ���Sdcard�Ƿ����
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * ������ʾToast��Ϣ
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		Toast toast = new Toast(context);
		toast.setDuration(2000);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}

	/**
	* �ж�ѧ�Ÿ�ʽ
	*/
	public static boolean isStudentNO(String stuNumber) {
		Pattern pattern = Pattern.compile("^[0-9]{6,18}$");
		Matcher matcher = pattern.matcher(stuNumber);
		return matcher.matches();
	}
	
	/**
	* �ж��ֻ�����
	*/
	public static boolean isMobileNO(String mobiles) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(mobiles);
		return matcher.matches();
	}

	/**
	 * �ж��������ʽ
	 * @return
	 */
	public static boolean isInviteCode(String code){
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6}$");
		Matcher matcher = pattern.matcher(code);
		return matcher.matches();
	}
	
	/**
	 * ��֤�����Ƿ����Ҫ��
	 * ����ĸ��ͷ��������6~18֮�䣬ֻ�ܰ����ַ������ֺ��»���
	 * @return
	 */
	public static boolean isPassValid(String pass) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,18}$");
		Matcher matcher = pattern.matcher(pass);
		return matcher.matches();
	}

}
