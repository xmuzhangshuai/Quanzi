package com.quanzi.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.customewidget.MyMenuDialog;
import com.quanzi.table.PostTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.ImageTools;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PublishPostActivity  
 * 类描述：发布帖子的页面
 * @author zhangshuai
 * @date 创建时间：2015-4-29 下午7:56:30 
 *
 */
public class PublishPostActivity extends BaseActivity implements OnClickListener {
	private EditText publishEditeEditText;
	private TextView publishBtn;
	private View backBtn;
	private ImageView[] publishImageViews;
	private ImageView[] addPublishImageViews;
	private TextView textCountTextView;

	private String[] photoUris;// 图片地址
	private UserPreference userPreference;

	/**************用户变量**************/
	public static final int NUM = 200;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_post);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		publishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_image1), (ImageView) findViewById(R.id.publish_image2),
				(ImageView) findViewById(R.id.publish_image3), (ImageView) findViewById(R.id.publish_image4), (ImageView) findViewById(R.id.publish_image5),
				(ImageView) findViewById(R.id.publish_image6) };
		addPublishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_addiamge1), (ImageView) findViewById(R.id.publish_addiamge2),
				(ImageView) findViewById(R.id.publish_addiamge3), (ImageView) findViewById(R.id.publish_addiamge4),
				(ImageView) findViewById(R.id.publish_addiamge5), (ImageView) findViewById(R.id.publish_addiamge6) };
		photoUris = new String[6];
		publishEditeEditText = (EditText) findViewById(R.id.publish_content);
		publishBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
		textCountTextView = (TextView) findViewById(R.id.count);
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

		// 设置编辑框事件
		publishEditeEditText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = NUM - s.length();
				textCountTextView.setText("" + number + "字");
				selectionStart = publishEditeEditText.getSelectionStart();
				selectionEnd = publishEditeEditText.getSelectionEnd();
				if (temp.length() > NUM) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					publishEditeEditText.setText(s);
					publishEditeEditText.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
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

		final MyMenuDialog myMenuDialog = new MyMenuDialog(PublishPostActivity.this);
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
		if (requestCode < 6) {// 拍照
			showPicture(requestCode);
		} else if (requestCode < 12 && requestCode > 5) {// 相册
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = PublishPostActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				photoUris[requestCode - 6] = cursor.getString(columnIndex);
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
	 * 发布
	 */
	private void publish() {
		String content = publishEditeEditText.getText().toString().trim();
		List<File> photoFiles = new ArrayList<File>();
		// File[] photoFiles = new File[] {};
		for (int i = 0; i < 6; i++) {
			if (!TextUtils.isEmpty(photoUris[i])) {
				photoFiles.add(new File(photoUris[i]));
			}
		}

		dialog = showProgressDialog("正在发布，请稍后...");
		dialog.setCancelable(false);

		RequestParams params = new RequestParams();
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("gbk") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						ToastTool.showShort(PublishPostActivity.this, "发布成功！");
						finish();
						overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					} else {
						LogTool.e("服务器处理失败" + response);
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				ToastTool.showShort(PublishPostActivity.this, "发布失败！");
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
		params.put(PostTable.P_USERID, userPreference.getU_id());
		params.put(PostTable.P_CONTENT, content);
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
		if (!content.isEmpty() || photoFiles.size() > 0) {
			if (photoFiles.size() > 0) {
				AsyncHttpClientTool.post("post/add", params, responseHandler);
			} else {
				AsyncHttpClientTool.post("post/add_no_pic", params, responseHandler);
			}
		} else {
			ToastTool.showLong(PublishPostActivity.this, "请填写内容或图片");
			dialog.dismiss();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			giveUpPublish();
			break;
		case R.id.publish_btn:
			publish();
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
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[0]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image2:
			if (!photoUris[1].isEmpty()) {
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[1]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image3:
			if (!photoUris[2].isEmpty()) {
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[2]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image4:
			if (!photoUris[3].isEmpty()) {
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[3]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image5:
			if (!photoUris[4].isEmpty()) {
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[4]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		case R.id.publish_image6:
			if (!photoUris[5].isEmpty()) {
				Intent intent = new Intent(PublishPostActivity.this, ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "file://" + photoUris[5]);
				startActivity(intent);
				PublishPostActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
			break;
		default:
			break;
		}
	}

}
