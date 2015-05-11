package com.quanzi.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.config.Constants;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.customewidget.MyMenuDialog;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageTools;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 * 类名称：ModifyDataActivity
 * 类描述：修改个人资料页面
 * 创建人： 张帅
 * 创建时间：2014年7月26日 下午8:47:52
 *
 */
public class ModifyDataActivity extends BaseFragmentActivity implements OnClickListener {
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮

	/***头像***/
	private ImageView headImage;

	/***昵称***/
	private View nicknameView;
	private TextView nickNameTextView;

	/***性别***/
	private View genderView;
	private TextView genderText;

	/***生日***/
	private View birthdayView;
	private TextView birthdayTextView;

	/***学校***/
	private View schoolView;
	private TextView schoolTextView;

	/***当前身份***/
	private View statusView;
	private TextView statusTextView;

	/***情感状态***/
	private View loveStatusView;
	private TextView loveStatusTextView;

	/***兴趣爱好***/
	private View interestView;
	private TextView interestTextView;

	/***擅长技能***/
	private View skillView;
	private TextView skillTextView;

	/***所属行业***/
	private View industryView;
	private TextView industryTextView;

	/***个人签名***/
	private View introView;
	private TextView introTextView;

	//	private TextView provinceTextView;
	//	private TextView cityTextView;
	//	private TextView telEditText;//电话
	//	private View ageView;
	//	private View heightView;
	//	private View weightView;
	//	private View constellView;
	//	private ImageView vertifyView;
	//	private EditText ageEditText;
	//	private EditText heightEditText;
	//	private EditText weightEditText;
	//	private TextView constellEditText;
	//	private View passView;
	//	private View phoneView;
	//	private TextView waitCheckView;
	//	private String age;
	//	private String weight;
	//	private String height;
	//	private String constell;

	private String personIntro;
	private UserPreference userPreference;
	private File picFile;
	private Uri photoUri;
	private InputMethodManager imm;
	private String nickname;
	private final int activity_result_camara_with_data = 1006;
	private final int activity_result_cropimage_with_data = 1007;
	boolean nickNameChanged = false;
	boolean ageChanged = false;
	boolean weightChanged = false;
	boolean heightChanged = false;
	boolean constellChanged = false;
	boolean personIntroChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_data);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		telEditText.setText(userPreference.getU_tel());
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		headImage = (ImageView) findViewById(R.id.headimage);
		nicknameView = findViewById(R.id.nicknameview);
		nickNameTextView = (TextView) findViewById(R.id.nickname);
		genderView = findViewById(R.id.genderview);
		genderText = (TextView) findViewById(R.id.gender);
		birthdayView = findViewById(R.id.birthdayview);
		birthdayTextView = (TextView) findViewById(R.id.birthday);
		schoolView = findViewById(R.id.schoolview);
		schoolTextView = (TextView) findViewById(R.id.school);
		statusView = findViewById(R.id.statusView);
		statusTextView = (TextView) findViewById(R.id.status);
		loveStatusView = findViewById(R.id.loveStatusView);
		loveStatusTextView = (TextView) findViewById(R.id.lovestatus);
		interestView = findViewById(R.id.interestView);
		interestTextView = (TextView) findViewById(R.id.interest);
		skillView = findViewById(R.id.skillView);
		skillTextView = (TextView) findViewById(R.id.skill);
		industryView = findViewById(R.id.industryView);
		industryTextView = (TextView) findViewById(R.id.industry);
		introView = findViewById(R.id.personIntroview);
		introTextView = (TextView) findViewById(R.id.personIntro);

		//		provinceTextView = (TextView) findViewById(R.id.province);
		//		cityTextView = (TextView) findViewById(R.id.city);
		//		telEditText = (TextView) findViewById(R.id.phone);
		//		passView = findViewById(R.id.passview);
		//		phoneView = findViewById(R.id.phoneview);
		//		waitCheckView = (TextView) findViewById(R.id.waitcheck);
		//		ageView = findViewById(R.id.ageview);
		//		heightView = findViewById(R.id.heightview);
		//		weightView = findViewById(R.id.weightview);
		//		constellView = findViewById(R.id.constellview);
		//		ageEditText = (EditText) findViewById(R.id.age);
		//		heightEditText = (EditText) findViewById(R.id.height);
		//		weightEditText = (EditText) findViewById(R.id.weight);
		//		constellEditText = (TextView) findViewById(R.id.constell);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("编辑资料");
		leftButton.setOnClickListener(this);
		headImage.setOnClickListener(this);
		nicknameView.setOnClickListener(this);
		genderView.setOnClickListener(this);
		birthdayView.setOnClickListener(this);
		introView.setOnClickListener(this);
		statusView.setOnClickListener(this);
		skillView.setOnClickListener(this);
		industryView.setOnClickListener(this);
		interestView.setOnClickListener(this);
		loveStatusView.setOnClickListener(this);
		schoolView.setOnClickListener(this);
		headImage.setImageResource(R.drawable.headimage2);

		//设置生日
		if (userPreference.getU_birthday() != null) {
			birthdayTextView.setText(DateTimeTools.getDateString(userPreference.getU_birthday()));
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case activity_result_camara_with_data: // 拍照
			try {
				cropImageUriByTakePhoto();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case activity_result_cropimage_with_data:
			try {
				if (photoUri != null) {
					uploadImage(photoUri.getPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		}
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/yixianqian");
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			picFile = new File(uploadFileDir, "yixianqian.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(cameraIntent, activity_result_camara_with_data);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从相册中获取
	 */
	protected void doCropPhoto() {
		try {
			File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/yixianqian/image");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir, "yixianqian.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			final Intent intent = getCropImageIntent();
			startActivityForResult(intent, activity_result_cropimage_with_data);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  调用图片剪辑程序
	 */
	public Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	private void cropImageUriByTakePhoto() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, activity_result_cropimage_with_data);
	}

	/**
	 * 上传头像
	 * @param filePath
	 */
	public void uploadImage(final String filePath) {
		//		final Bitmap largeAvatar = BitmapFactory.decodeFile(filePath);
		//		if (largeAvatar != null) {
		//			final Bitmap smallBitmap = ImageTools.zoomBitmap(largeAvatar, largeAvatar.getWidth() / 4,
		//					largeAvatar.getHeight() / 4);
		//			final String smallAvatarPath = Environment.getExternalStorageDirectory() + "/yixianqian/image";
		//
		//			RequestParams params = new RequestParams();
		//			int userId = userPreference.getU_id();
		//			if (userId > -1) {
		//				params.put(UserTable.U_ID, String.valueOf(userId));
		//				try {
		//					params.put("large_avatar", picFile);
		//					params.put("small_avatar",
		//							ImageTools.savePhotoToSDCard(smallBitmap, smallAvatarPath, "smallAvatar.jpeg", 100));
		//				} catch (FileNotFoundException e1) {
		//					// TODO Auto-generated catch block
		//					e1.printStackTrace();
		//				}
		//				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
		//					@Override
		//					public void onSuccess(int statusCode, Header[] headers, String response) {
		//						// TODO Auto-generated method stub
		//						if (statusCode == 200) {
		//							ToastTool.showLong(ModifyDataActivity.this, "头像上传成功！请等待审核");
		//							largeAvatar.recycle();
		//							smallBitmap.recycle();
		//							//删除本地头像
		//							ImageTools.deleteImageByPath(filePath);
		//							ImageTools.deletePhotoAtPathAndName(smallAvatarPath, "smallAvatar.jpeg");
		//							//获取新头像地址
		//							ServerUtil.getInstance().getHeadImage(headImage, waitCheckView);
		//						}
		//					}
		//
		//					@Override
		//					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//						// TODO Auto-generated method stub
		//						ToastTool.showLong(ModifyDataActivity.this, "头像上传失败！" + errorResponse);
		//						//删除本地头像
		//						ImageTools.deleteImageByPath(filePath);
		//						ImageTools.deletePhotoAtPathAndName(smallAvatarPath, "smallAvatar.jpeg");
		//					}
		//				};
		//				AsyncHttpClientImageSound.post(AsyncHttpClientImageSound.HEADIMAGE_URL, params, responseHandler);
		//			}
		//		} else {
		//			ImageTools.deleteImageByPath(filePath);
		//		}
	}

	/**
	 * 登录环信
	 */
	//	private void loginHuanXin() {
	//		EMChatManager.getInstance().login(userPreference.getHuanXinUserName(), userPreference.getHuanXinPassword(),
	//				new EMCallBack() {
	//					@Override
	//					public void onSuccess() {
	//						LogTool.i("ModifyDataActivity", "登录环信成功");
	//						//更新环信昵称
	//						if (EMChatManager.getInstance().updateCurrentUserNick(userPreference.getName())) {
	//							LogTool.i("ModifyDataActivity", "更新环信昵称成功");
	//						} else {
	//							LogTool.e("ModifyDataActivity", "更新环信昵称失败");
	//						}
	//					}
	//
	//					@Override
	//					public void onProgress(int progress, String status) {
	//					}
	//
	//					@Override
	//					public void onError(int code, final String message) {
	//						LogTool.e("ModifyDataActivity", "登录环信失败：code:" + code + "   message:" + message);
	//					}
	//				});
	//	}

	/**
	 * 显示生日菜单
	 */
	private void showDatePicker() {
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		OnDateSetListener callBack = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, final int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				calendar.set(year, monthOfYear, dayOfMonth);
				final Date date = calendar.getTime();

				RequestParams params = new RequestParams();
				params.put(UserTable.U_ID, userPreference.getU_id());
				params.put(UserTable.U_BIRTHDAY, date.getTime());
				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						if (arg0 == 200) {
							userPreference.setU_birthday(date);
							birthdayTextView.setText(DateTimeTools.getDateString(date));
							//							constellEditText.setText(ConstellUtil.date2Constellation(calendar));
							int tempAge = Calendar.getInstance().get(Calendar.YEAR) - year;
							//							ageEditText.setText("" + tempAge);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						// TODO Auto-generated method stub
					}
				};
				AsyncHttpClientTool.post("updateuserbirthday", params, responseHandler);
			}
		};
		DatePickerDialog datePickerDialog;
		if (userPreference.getU_birthday() != null) {
			Date date = userPreference.getU_birthday();
			calendar.setTime(date);
			datePickerDialog = new DatePickerDialog(ModifyDataActivity.this, callBack, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialog = new DatePickerDialog(ModifyDataActivity.this, callBack, 1993, 6, 15);
		}
		datePickerDialog.show();
	}

	/**
	* 显示对话框，从拍照和相册选择图片来源
	* 
	* @param context
	* @param isCrop
	*/
	private void showPicturePicker(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("图片来源");
		ArrayList<String> list = new ArrayList<String>();
		list.add("拍照");
		list.add("相册");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					myMenuDialog.dismiss();
					String status = Environment.getExternalStorageState();
					if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
						doTakePhoto();// 用户点击了从照相机获取
					}
					break;
				case 1:
					myMenuDialog.dismiss();
					doCropPhoto();// 从相册中去获取
					break;

				default:
					break;
				}
			}
		};
		myMenuDialog.setListItemClickListener(listener);

		myMenuDialog.show();
	}

	/**
	* 显示对话框，修改性别
	* 
	* @param context
	* @param isCrop
	*/
	private void showgenderModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("修改性别");
		ArrayList<String> list = new ArrayList<String>();
		list.add("男");
		list.add("女");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					break;
				case 1:
					break;

				default:
					break;
				}
			}
		};
		myMenuDialog.setListItemClickListener(listener);

		myMenuDialog.show();
	}

	/**
	* 显示对话框，修改身份
	* 
	* @param context
	* @param isCrop
	*/
	private void showStatusModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("修改身份");
		ArrayList<String> list = new ArrayList<String>();
		list.add("学生");
		list.add("校友");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					break;
				case 1:
					break;

				default:
					break;
				}
			}
		};
		myMenuDialog.setListItemClickListener(listener);

		myMenuDialog.show();
	}

	/**
	* 显示对话框，修改情感状况
	* 
	* @param context
	* @param isCrop
	*/
	private void showLoveStatusModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("修改情感状况");
		ArrayList<String> list = new ArrayList<String>();
		list.add("单身");
		list.add("恋爱中");
		list.add("已婚");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					break;
				case 1:
					break;

				default:
					break;
				}
			}
		};
		myMenuDialog.setListItemClickListener(listener);

		myMenuDialog.show();
	}

	/**
	 * 确认修改学校
	 */
	private void vertifyToModifySchool() {
		final MyAlertDialog dialog = new MyAlertDialog(ModifyDataActivity.this);
		dialog.setTitle("修改教育背景");
		dialog.setMessage("你只有一次修改学校的机会，确定要改吗？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
				BaseApplication.getInstance().getUserPreference().clear();
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		dialog.setPositiveButton("确定", comfirm);
		dialog.setNegativeButton("取消", cancle);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.headimage:
			showPicturePicker(ModifyDataActivity.this);
			break;
		case R.id.nicknameview:
			intent = new Intent(ModifyDataActivity.this, ModifyNameActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.birthdayview:
			showDatePicker();
			break;
		case R.id.statusView:
			showStatusModify(ModifyDataActivity.this);
			break;
		case R.id.schoolview:
			vertifyToModifySchool();
			break;
		case R.id.loveStatusView:
			showLoveStatusModify(ModifyDataActivity.this);
			break;
		case R.id.skillView:
			intent = new Intent(ModifyDataActivity.this, ModifySkillActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.industryView:
			intent = new Intent(ModifyDataActivity.this, ModifyIndustryActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.interestView:
			intent = new Intent(ModifyDataActivity.this, ModifyInterestActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.personIntroview:
			intent = new Intent(ModifyDataActivity.this, ModifyPersonIntroActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.genderview:
			showgenderModify(ModifyDataActivity.this);
			break;
		default:
			break;
		}
	}

}
