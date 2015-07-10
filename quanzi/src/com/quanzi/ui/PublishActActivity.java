package com.quanzi.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.config.Constants;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.customewidget.MyMenuDialog;
import com.quanzi.table.ActivityTable;
import com.quanzi.table.PostTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageTools;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PublishActActivity  
 * 类描述：发布活动页面
 * @author zhangshuai
 * @date 创建时间：2015-4-29 下午7:56:55 
 *
 */
public class PublishActActivity extends BaseActivity implements OnClickListener {
	private View timeContainerView;
	private TextView timeTextView;
	private EditText localtionEditText;
	private View targetContainerView;
	private TextView targetTextView;
	private View typeContainerView;
	private TextView typeTextView;
	private TextView publishBtn;
	private EditText titleEditText;
	private EditText detailEditText;
	private View backBtn;
	private ImageView[] publishImageViews;
	private ImageView[] addPublishImageViews;

	private String[] photoUris;//图片地址
	private UserPreference userPreference;
	Calendar choosenCalendar = Calendar.getInstance(Locale.CHINA);

	/**************用户变量**************/
	public static final int NUM = 250;
	private int minCount = 10;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_act);
		userPreference = BaseApplication.getInstance().getUserPreference();
		photoUris = new String[6];

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		publishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_image1),
				(ImageView) findViewById(R.id.publish_image2), (ImageView) findViewById(R.id.publish_image3),
				(ImageView) findViewById(R.id.publish_image4), (ImageView) findViewById(R.id.publish_image5),
				(ImageView) findViewById(R.id.publish_image5) };
		addPublishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_addiamge1),
				(ImageView) findViewById(R.id.publish_addiamge2), (ImageView) findViewById(R.id.publish_addiamge3),
				(ImageView) findViewById(R.id.publish_addiamge4), (ImageView) findViewById(R.id.publish_addiamge5),
				(ImageView) findViewById(R.id.publish_addiamge6) };

		publishBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
		timeContainerView = findViewById(R.id.time_container);
		timeTextView = (TextView) findViewById(R.id.publish_act_time);
		localtionEditText = (EditText) findViewById(R.id.publish_act_location);
		targetContainerView = findViewById(R.id.target_container);
		typeContainerView = findViewById(R.id.type_container);
		targetTextView = (TextView) findViewById(R.id.publish_act_target);
		typeTextView = (TextView) findViewById(R.id.publish_act_type);
		titleEditText = (EditText) findViewById(R.id.publish_act_title);
		detailEditText = (EditText) findViewById(R.id.publish_content);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		for (int i = 0; i < publishImageViews.length; i++) {
			publishImageViews[i].setOnClickListener(this);
			addPublishImageViews[i].setOnClickListener(this);
		}

		publishBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		timeContainerView.setOnClickListener(this);
		targetContainerView.setOnClickListener(this);
		typeContainerView.setOnClickListener(this);

		choosenCalendar.setTime(new Date());
		timeTextView.setText(DateTimeTools.DateToString(new Date()));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		giveUpPublish();
	}

	/**
	 * 放弃发布
	 */
	private void giveUpPublish() {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("放弃发布？  ");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("确定", comfirm);
		myAlertDialog.setNegativeButton("取消", cancle);
		myAlertDialog.show();
	}

	/**
	* 显示对话框，从拍照和相册选择图片来源
	* 
	* @param context
	* @param isCrop
	*/
	private void showPicturePicker(final int index) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(PublishActActivity.this);
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
						takePhoto(index);// 用户点击了从照相机获取
					}
					break;
				case 1:
					myMenuDialog.dismiss();
					choosePhoto(index);// 从相册中去获取
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
	 * 显示图片
	 */
	public void showPicture(int index) {
		String tempPath = Environment.getExternalStorageDirectory() + "/quanzi/image";
		String photoName = "temp" + index + ".jpeg";
		File file = ImageTools.compressForFile(tempPath, photoName, photoUris[index], 100);

		Bitmap uploadBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		publishImageViews[index].setImageBitmap(uploadBitmap);
		if (file != null) {
			photoUris[index] = file.getAbsolutePath();
		}
		addPublishImageViews[index].setVisibility(View.GONE);
		publishImageViews[index].setVisibility(View.VISIBLE);
		if (index < 5) {
			addPublishImageViews[index + 1].setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 从相册选择图片
	 */
	private void choosePhoto(int index) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, index + 6);
	}

	/**
	 * 拍照
	 */
	private void takePhoto(int index) {
		try {
			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/quanzi/image");
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			File picFile = new File(uploadFileDir, "temp" + index + ".jpeg");

			if (!picFile.exists()) {
				picFile.createNewFile();
			}

			photoUris[index] = picFile.getAbsolutePath();
			Uri takePhotoUri = Uri.fromFile(picFile);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
			startActivityForResult(cameraIntent, index);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode < 6) {//拍照
			showPicture(requestCode);
		} else if (requestCode < 12 && requestCode > 5) {//相册
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = PublishActActivity.this.getContentResolver().query(selectedImage, filePathColumn, null,
						null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				photoUris[requestCode - 6] = cursor.getString(columnIndex);
				LogTool.i(cursor.getString(columnIndex));
				cursor.close();
				showPicture(requestCode - 6);
			} catch (Exception e) {
				// TODO: handle exception   
				e.printStackTrace();
			}
		} else {

		}
	}

	/**
	 * 显示选择日期菜单
	 */
	private void showDatePicker() {
		Date date = new Date();
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		OnDateSetListener callBack = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
				// TODO Auto-generated method stub
				calendar.setTime(new Date());
				TimePickerDialog timePickerDialog = new TimePickerDialog(PublishActActivity.this,
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								choosenCalendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
								timeTextView.setText(DateTimeTools.DateToString(choosenCalendar.getTime()));
							}
						}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
				timePickerDialog.show();
			}
		};
		DatePickerDialog datePickerDialog;
		calendar.setTime(date);
		datePickerDialog = new DatePickerDialog(PublishActActivity.this, callBack, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	/**
	* 显示对话框，选择对象
	* 
	* @param context
	* @param isCrop
	*/
	private void showTargetPicker() {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(PublishActActivity.this);
		myMenuDialog.setTitle("选择对象");
		final ArrayList<String> list = new ArrayList<String>();
		list.add("男");
		list.add("女");
		list.add("男女不限");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				targetTextView.setText(list.get(position));
				myMenuDialog.dismiss();
			}
		};
		myMenuDialog.setListItemClickListener(listener);
		myMenuDialog.show();
	}

	/**
	* 显示对话框，选择类型
	* 
	* @param context
	* @param isCrop
	*/
	private void showTypePicker() {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(PublishActActivity.this);
		myMenuDialog.setTitle("选择类型");
		final ArrayList<String> list = new ArrayList<String>();
		list.addAll(Constants.ActivityType.getList());
		list.add("其他");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				typeTextView.setText(list.get(position));
				myMenuDialog.dismiss();
			}
		};
		myMenuDialog.setListItemClickListener(listener);
		myMenuDialog.show();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptPublish() {

		localtionEditText.setError(null);
		titleEditText.setError(null);
		detailEditText.setError(null);

		String location = localtionEditText.getText().toString();
		String title = titleEditText.getText().toString();
		String detail = detailEditText.getText().toString();

		boolean cancel = false;
		View focusView = null;
		if (TextUtils.isEmpty(title)) {
			titleEditText.setError(getString(R.string.error_field_required));
			focusView = titleEditText;
			cancel = true;
		}

		else if (TextUtils.isEmpty(location)) {
			localtionEditText.setError(getString(R.string.error_field_required));
			focusView = localtionEditText;
			cancel = true;
		}

		else if (TextUtils.isEmpty(detail)) {
			detailEditText.setError(getString(R.string.error_field_required));
			focusView = detailEditText;
			cancel = true;
		}

		else if (!publishImageViews[0].isShown()) {
			ToastTool.showLong(PublishActActivity.this, "请至少上传一张图片");
			focusView = addPublishImageViews[0];
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			publish();
		}
	}

	/**
	 * 发布
	 */
	private void publish() {
		List<File> photoFiles = new ArrayList<File>();
		for (int i = 0; i < 6; i++) {
			if (!TextUtils.isEmpty(photoUris[i])) {
				LogTool.i("地址", photoUris[i]);
				photoFiles.add(new File(photoUris[i]));
			}
		}

		dialog = showProgressDialog("正在发布，请稍后...");
		dialog.setCancelable(false);

		RequestParams params = new RequestParams();
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200 && response.equals("1")) {
					ToastTool.showShort(PublishActActivity.this, "发布成功！");
					finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				} else {
					LogTool.e("发布失败" + response);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				ToastTool.showShort(PublishActActivity.this, "发布失败！");
				LogTool.e("发布时失败！" + statusCode + "\n");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				super.onCancel();
				if (dialog != null) {
					dialog.dismiss();
				}
			}

		};
		params.put(ActivityTable.A_USER_ID, userPreference.getU_id());
		params.put(ActivityTable.A_ACT_TITLE, titleEditText.getText().toString().trim());
		params.put(ActivityTable.A_ACTTIME, DateTimeTools.DateToString(choosenCalendar.getTime()));
		params.put(ActivityTable.A_ADDRESS, localtionEditText.getText().toString().trim());
		params.put(ActivityTable.A_TARGET, targetTextView.getText().toString().trim());
		params.put(ActivityTable.A_ACT_TYPE, typeTextView.getText().toString().trim());
		params.put(ActivityTable.A_CONTENT, detailEditText.getText().toString().trim());
		for (int i = 0; i < photoFiles.size(); i++) {
			File file = photoFiles.get(i);
			if (file != null && file.exists()) {
				try {
					params.put(PostTable.P_BIG_PHOTO + i, file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		AsyncHttpClientTool.post("activity/add", params, responseHandler);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			giveUpPublish();
			break;
		case R.id.publish_btn:
			attemptPublish();
			break;
		case R.id.publish_addiamge1:
			showPicturePicker(0);
			break;
		case R.id.publish_addiamge2:
			showPicturePicker(1);
			break;
		case R.id.publish_addiamge3:
			showPicturePicker(2);
			break;
		case R.id.publish_addiamge4:
			showPicturePicker(3);
			break;
		case R.id.publish_addiamge5:
			showPicturePicker(4);
			break;
		case R.id.publish_addiamge6:
			showPicturePicker(5);
			break;
		case R.id.publish_image1:
			if (!photoUris[0].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[0]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image2:
			if (!photoUris[1].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[1]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image3:
			if (!photoUris[2].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[2]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image4:
			if (!photoUris[3].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[3]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image5:
			if (!photoUris[4].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[4]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image6:
			if (!photoUris[5].isEmpty()) {
				Intent intent = new Intent(PublishActActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[5]);
				startActivity(intent);
				PublishActActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.time_container:
			showDatePicker();
			break;
		case R.id.target_container:
			showTargetPicker();
			break;
		case R.id.type_container:
			showTypePicker();
			break;
		default:
			break;
		}
	}

}
