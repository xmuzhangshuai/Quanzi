package com.quanzi.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�PublishPostActivity  
 * ���������������ӵ�ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-29 ����7:56:30 
 *
 */
public class PublishPostActivity extends BaseActivity implements OnClickListener {
	private EditText publishEditeEditText;
	private TextView publishBtn;
	private View backBtn;
	private ImageView[] publishImageViews;
	private ImageView[] addPublishImageViews;

	private String photoUri;//ͼƬ��ַ
	private UserPreference userPreference;

	/**************�û�����**************/
//	public static final int NUM = 250;
//	private int minCount = 10;
//	Dialog dialog;
//	
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
		publishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_image1),
				(ImageView) findViewById(R.id.publish_image2), (ImageView) findViewById(R.id.publish_image3),
				(ImageView) findViewById(R.id.publish_image4), (ImageView) findViewById(R.id.publish_image5),
				(ImageView) findViewById(R.id.publish_image5) };
		addPublishImageViews = new ImageView[] { (ImageView) findViewById(R.id.publish_addiamge1),
				(ImageView) findViewById(R.id.publish_addiamge2), (ImageView) findViewById(R.id.publish_addiamge3),
				(ImageView) findViewById(R.id.publish_addiamge4), (ImageView) findViewById(R.id.publish_addiamge5),
				(ImageView) findViewById(R.id.publish_addiamge6) };
		publishEditeEditText = (EditText) findViewById(R.id.publish_content);
		publishBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
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

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		giveUpPublish();
	}

	/**
	 * ��������
	 */
	private void giveUpPublish() {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
		myAlertDialog.setTitle("��ʾ");
		myAlertDialog.setMessage("����������  ");
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
		myAlertDialog.setPositiveButton("ȷ��", comfirm);
		myAlertDialog.setNegativeButton("ȡ��", cancle);
		myAlertDialog.show();
	}
	/**
	 * ��ʾͼƬ
	 */
	//	public void showPicture() {
	//		String tempPath = Environment.getExternalStorageDirectory() + "/yixianqian/image";
	//		String photoName = "loveBridge.jpeg";
	//		File file = ImageTools.compressForFile(tempPath, photoName, photoUri, 100);
	//
	//		Bitmap uploadBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
	//		publishImageView.setImageBitmap(uploadBitmap);
	//
	//		if (file != null) {
	//			photoUri = file.getAbsolutePath();
	//		}
	//		publishImageView.setVisibility(View.VISIBLE);
	//	}
	//
	//	/**
	//	 * �����ѡ��ͼƬ
	//	 */
	//	private void choosePhoto() {
	//		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	//		startActivityForResult(intent, 2);
	//	}
	//
	//	/**
	//	 * ����
	//	 */
	//	private void takePhoto() {
	//		try {
	//			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/yixianqian/image");
	//			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	//			if (!uploadFileDir.exists()) {
	//				uploadFileDir.mkdirs();
	//			}
	//			File picFile = new File(uploadFileDir, "yixianqian.jpeg");
	//
	//			if (!picFile.exists()) {
	//				picFile.createNewFile();
	//			}
	//
	//			photoUri = picFile.getAbsolutePath();
	//			Uri takePhotoUri = Uri.fromFile(picFile);
	//			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
	//			startActivityForResult(cameraIntent, 1);
	//		} catch (ActivityNotFoundException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
	//
	//	@Override
	//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	//		// TODO Auto-generated method stub
	//		super.onActivityResult(requestCode, resultCode, data);
	//		if (resultCode != Activity.RESULT_OK)
	//			return;
	//
	//		switch (requestCode) {
	//		case 1://����
	//			showPicture();
	//			break;
	//		case 2://�����ѡ��
	//			try {
	//				Uri selectedImage = data.getData();
	//				String[] filePathColumn = { MediaStore.Images.Media.DATA };
	//				Cursor cursor = PublishLoveBridgeActivity.this.getContentResolver().query(selectedImage,
	//						filePathColumn, null, null, null);
	//				cursor.moveToFirst();
	//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	//				photoUri = cursor.getString(columnIndex);
	//				cursor.close();
	//				showPicture();
	//			} catch (Exception e) {
	//				// TODO: handle exception   
	//				e.printStackTrace();
	//			}
	//			break;
	//		case REQUEST_CODE_PREVIEW_PICTURE:
	//			photoUri = null;
	//			publishImageView.setVisibility(View.GONE);
	//			break;
	//		}
	//	}
	//
	//	/**
	//	 * �ϴ�ͼƬ
	//	 */
	//	private void uploadImage() {
	//		File photoFile = null;
	//		if (!TextUtils.isEmpty(photoUri)
	//				&& photoUri.equals(Environment.getExternalStorageDirectory() + "/yixianqian/image/loveBridge.jpeg")) {
	//			photoFile = new File(photoUri);
	//		}
	//
	//		dialog = showProgressDialog("���ڷ��������Ժ�...");
	//		dialog.setCancelable(false);
	//
	//		RequestParams params = new RequestParams();
	//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
	//
	//			@Override
	//			public void onSuccess(int statusCode, Header[] headers, String response) {
	//				// TODO Auto-generated method stub
	//				if (statusCode == 200) {
	//					publish(response);
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
	//				// TODO Auto-generated method stub
	//				ToastTool.showShort(PublishLoveBridgeActivity.this, "����ʧ�ܣ�");
	//			}
	//		};
	//		params.put(LoveBridgeItemTable.N_USERID, userPreference.getU_id());
	//
	//		if (photoFile != null && photoFile.exists()) {
	//			try {
	//				params.put(LoveBridgeItemTable.N_IAMGE, photoFile);
	//			} catch (FileNotFoundException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			}
	//			AsyncHttpClientImageSound.post("lovebridgeitemimage", params, responseHandler);
	//		} else {
	//			publish("");
	//		}
	//	}
	//
	//	/**
	//	 * ����
	//	 */
	//	private void publish(String imageUrl) {
	//		LogTool.e("���뷢����");
	//		RequestParams params = new RequestParams();
	//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
	//
	//			@Override
	//			public void onSuccess(int statusCode, Header[] headers, String response) {
	//				// TODO Auto-generated method stub
	//				if (statusCode == 200) {
	//					ToastTool.showShort(PublishLoveBridgeActivity.this, "�����ɹ���");
	//					finish();
	//					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
	//				// TODO Auto-generated method stub
	//				ToastTool.showShort(PublishLoveBridgeActivity.this, "����ʧ�ܣ�");
	//				LogTool.e("����ʱʧ�ܣ�" + statusCode + "\n");
	//			}
	//
	//			@Override
	//			public void onFinish() {
	//				// TODO Auto-generated method stub
	//				super.onFinish();
	//				if (dialog != null) {
	//					dialog.dismiss();
	//				}
	//			}
	//
	//			@Override
	//			public void onCancel() {
	//				// TODO Auto-generated method stub
	//				super.onCancel();
	//				if (dialog != null) {
	//					dialog.dismiss();
	//				}
	//			}
	//		};
	//		params.put(LoveBridgeItemTable.N_USERID, userPreference.getU_id());
	//		params.put(LoveBridgeItemTable.N_CONTENT, publishEditeEditText.getText().toString().trim());
	//		params.put(LoveBridgeItemTable.N_IAMGE, imageUrl);
	//		AsyncHttpClientTool.post("addlovebridgeitemrecord", params, responseHandler);
	//	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			giveUpPublish();
			break;
		case R.id.publish_btn:

			break;
		case R.id.publish_addiamge1:

			break;
		case R.id.publish_addiamge2:

			break;
		case R.id.publish_addiamge3:

			break;
		case R.id.publish_addiamge4:

			break;
		case R.id.publish_addiamge5:

			break;
		case R.id.publish_addiamge6:

			break;
		case R.id.publish_image1:

			break;
		case R.id.publish_image2:

			break;
		case R.id.publish_image3:

			break;
		case R.id.publish_image4:

			break;
		case R.id.publish_image5:

			break;
		case R.id.publish_image6:

			break;
		default:
			break;
		}
	}

}
