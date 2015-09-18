package com.quanzi.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.customewidget.MyMenuDialog;
import com.quanzi.table.IndustryTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.ImageTools;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 类名称：ModifyDataActivity
 * 类描述：修改个人资料页面
 * 创建人： 张帅
 * 创建时间：2014年7月26日 下午8:47:52
 *
 */
public class ModifyDataActivity extends BaseActivity implements OnClickListener {
	private TextView leftTextView;// 导航栏左侧文字
	private View leftButton;// 导航栏左侧按钮

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

	private UserPreference userPreference;
	boolean nickNameChanged = false;
	boolean ageChanged = false;
	boolean weightChanged = false;
	boolean heightChanged = false;
	boolean constellChanged = false;
	boolean personIntroChanged = false;
	private Uri lastPhotoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_data);

		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/***学校***/
		schoolTextView.setText(userPreference.getSchoolName());
		/***昵称***/
		if (userPreference.getU_nickname().isEmpty()) {
			nickNameTextView.setText("未填写");
		} else {
			nickNameTextView.setText(userPreference.getU_nickname());
		}

		/***个人签名***/
		if (userPreference.getU_introduce().isEmpty()) {
			introTextView.setText("未填写");
		} else {
			introTextView.setText(userPreference.getU_introduce());
		}

		/***行业***/
		if (userPreference.getU_industry().isEmpty()) {
			industryTextView.setText("未填写");
		} else {
			industryTextView.setText(userPreference.getU_industry());
			industryTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ModifyDataActivity.this, MyPeopleActivity.class);
					intent.putExtra(MyPeopleActivity.TYPE, MyPeopleActivity.SAME_IN_INDUSTRY);
					intent.putExtra(IndustryTable.I_NAME, userPreference.getU_industry());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
		}

		/***技能***/
		if (userPreference.getU_skills().isEmpty()) {
			skillTextView.setText("未填写");
		} else {
			skillTextView.setText(userPreference.getU_skills().replace("|", "\n"));
		}
		/***爱好***/
		if (userPreference.getU_interests().isEmpty()) {
			interestTextView.setText("未填写");
		} else {
			interestTextView.setText(userPreference.getU_interests().replace("|", "\n"));
		}
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

		/***性别***/
		genderText.setText(userPreference.getU_gender());

		/***当前身份***/
		statusTextView.setText(userPreference.getU_identity());

		/***情感状况***/
		loveStatusTextView.setText(userPreference.getU_love_state());

		// 显示头像
		ImageLoader.getInstance().displayImage(AsyncHttpClientTool.getAbsoluteUrl(userPreference.getU_small_avatar()), headImage, ImageLoaderTool.getHeadImageOptions(3));

		// 设置生日
		if (userPreference.getU_birthday() != null) {
			birthdayTextView.setText(DateTimeTools.getDateString(userPreference.getU_birthday()));
		} else {
			birthdayTextView.setText("未填写");
		}
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
				Intent intent;
				switch (position) {
				case 0:// 如果是拍照
					myMenuDialog.dismiss();
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getNewImagePath())));
					startActivityForResult(intent, 2);
					break;
				case 1:// 从相册选取
					myMenuDialog.dismiss();
					intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, 1);
					break;

				default:
					break;
				}
			}
		};
		myMenuDialog.setListItemClickListener(listener);
		myMenuDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取 
		case 1:
			if (data != null) {
				startPhotoCrop(data.getData());
			}
			break;
		// 如果是调用相机拍照时 
		case 2:
			startPhotoCrop(Uri.fromFile(new File(getImagePath())));
			break;
		// 取得裁剪后的图片并上传 
		case 3:
			uploadImage(lastPhotoUri.getPath());
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 
	 * 裁剪图片方法实现 
	 * @param uri 
	 */
	public void startPhotoCrop(Uri uri) {
		lastPhotoUri = Uri.fromFile(new File(getImagePath()));

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪 
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, lastPhotoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		startActivityForResult(intent, 3);
	}

	//获得新的路径
	private String getNewImagePath() {
		// 删除上一次截图的临时文件
		String path = null;
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
		ImageTools.deleteImageByPath(sharedPreferences.getString("tempPath", ""));

		// 保存本次截图临时文件名字
		File file = new File(Environment.getExternalStorageDirectory() + "/quanzi");
		if (!file.exists()) {
			file.mkdirs();
		}

		path = file.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		Editor editor = sharedPreferences.edit();
		editor.putString("tempPath", path);
		editor.commit();
		return path;
	}

	//获得路径
	private String getImagePath() {
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
		return sharedPreferences.getString("tempPath", "");
	}

	/**
	 * 上传头像
	 * @param filePath
	 */
	public void uploadImage(final String filePath) {
		File file = new File(filePath);
		LogTool.e(filePath);
		if (file.exists()) {
			RequestParams params = new RequestParams();
			int userId = userPreference.getU_id();
			if (userId > -1) {
				params.put(UserTable.U_ID, String.valueOf(userId));
				try {
					params.put(UserTable.U_LARGE_AVATAR, new File(filePath));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String response) {
						// TODO Auto-generated method stub
						if (statusCode == 200) {
							ToastTool.showLong(ModifyDataActivity.this, "头像上传成功！");

							// 获取新头像地址
							Map<String, String> map = FastJsonTool.getObject(response, Map.class);
							if (map != null) {
								userPreference.setU_large_avatar(map.get(UserTable.U_LARGE_AVATAR));
								userPreference.setU_small_avatar(map.get(UserTable.U_SMALL_AVATAR));
								// 显示头像
								ImageLoader.getInstance().displayImage(AsyncHttpClientTool.getAbsoluteUrl(map.get(UserTable.U_SMALL_AVATAR)), headImage,
										ImageLoaderTool.getHeadImageOptions(3));
							} else {
								LogTool.e("上传服务器出错" + response);
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("头像上传失败！");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						// 删除本地头像
						ImageTools.deleteImageByPath(filePath);
					}
				};
				AsyncHttpClientTool.post("user/uploadHeadImg", params, responseHandler);
			}
		} else {
			LogTool.e("头像文件不存在");
		}
	}

	/**
	 * 修改生日
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
				params.put(UserTable.U_BIRTHDAY, DateTimeTools.DateToString(date));
				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers, String response) {
						// TODO Auto-generated method stub
						if (statusCode == 200 && response.equals("1")) {
							userPreference.setU_birthday(date);
							birthdayTextView.setText(DateTimeTools.getDateString(date));
							int tempAge = Calendar.getInstance().get(Calendar.YEAR) - year;
							userPreference.setU_age(tempAge);
						} else {
							LogTool.e("修改生日返回错误" + response);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("修改生日错误");
					}
				};
				AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
			}
		};
		DatePickerDialog datePickerDialog;
		if (userPreference.getU_birthday() != null) {
			Date date = userPreference.getU_birthday();
			calendar.setTime(date);
			datePickerDialog = new DatePickerDialog(ModifyDataActivity.this, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialog = new DatePickerDialog(ModifyDataActivity.this, callBack, 1993, 6, 15);
		}
		datePickerDialog.show();
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
					updateGender("男");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateGender("女");
					myMenuDialog.dismiss();
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
					updateIdentity("学生");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateIdentity("校友");
					myMenuDialog.dismiss();
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
					updateLoveState("单身");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateLoveState("恋爱中");
					myMenuDialog.dismiss();
					break;
				case 2:
					updateLoveState("已婚");
					myMenuDialog.dismiss();
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
				Intent intent = new Intent(ModifyDataActivity.this, ModifySchoolActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

	/**
	 * 修改性别
	 * @param name
	 */
	public void updateGender(final String gender) {
		if (!gender.equals(userPreference.getU_gender())) {
			// 没有错误，则修改
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_GENDER, gender);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("请稍后...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						if (response.equals("1")) {
							ToastTool.showShort(ModifyDataActivity.this, "修改成功！");
							userPreference.setU_gender(gender);
							genderText.setText(gender);
						} else if (response.equals("-1")) {
							LogTool.e("修改性别返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改性别错误");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

		}
	}

	/**
	 * 修改身份
	 * @param name
	 */
	public void updateIdentity(final String identity) {
		if (!identity.equals(userPreference.getU_identity())) {
			// 没有错误，则修改
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_IDENTITY, identity);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("请稍后...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						if (response.equals("1")) {
							ToastTool.showShort(ModifyDataActivity.this, "修改成功！");
							userPreference.setU_identity(identity);
							statusTextView.setText(identity);
						} else if (response.equals("-1")) {
							LogTool.e("修改身份返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改身份错误");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

		}
	}

	/**
	 * 修改情感状况
	 * @param name
	 */
	public void updateLoveState(final String state) {
		if (!state.equals(userPreference.getU_love_state())) {
			// 没有错误，则修改
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_LOVE_STATE, state);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("请稍后...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						if (response.equals("1")) {
							ToastTool.showShort(ModifyDataActivity.this, "修改成功！");
							userPreference.setU_love_state(state);
							loveStatusTextView.setText(state);
						} else if (response.equals("-1")) {
							LogTool.e("修改情感状况返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改情感状况错误");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

		}
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
