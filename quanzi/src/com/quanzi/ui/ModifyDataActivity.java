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
 * �����ƣ�ModifyDataActivity
 * ���������޸ĸ�������ҳ��
 * �����ˣ� ��˧
 * ����ʱ�䣺2014��7��26�� ����8:47:52
 *
 */
public class ModifyDataActivity extends BaseActivity implements OnClickListener {
	private TextView leftTextView;// �������������
	private View leftButton;// ��������ఴť

	/***ͷ��***/
	private ImageView headImage;

	/***�ǳ�***/
	private View nicknameView;
	private TextView nickNameTextView;

	/***�Ա�***/
	private View genderView;
	private TextView genderText;

	/***����***/
	private View birthdayView;
	private TextView birthdayTextView;

	/***ѧУ***/
	private View schoolView;
	private TextView schoolTextView;

	/***��ǰ���***/
	private View statusView;
	private TextView statusTextView;

	/***���״̬***/
	private View loveStatusView;
	private TextView loveStatusTextView;

	/***��Ȥ����***/
	private View interestView;
	private TextView interestTextView;

	/***�ó�����***/
	private View skillView;
	private TextView skillTextView;

	/***������ҵ***/
	private View industryView;
	private TextView industryTextView;

	/***����ǩ��***/
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
		/***ѧУ***/
		schoolTextView.setText(userPreference.getSchoolName());
		/***�ǳ�***/
		if (userPreference.getU_nickname().isEmpty()) {
			nickNameTextView.setText("δ��д");
		} else {
			nickNameTextView.setText(userPreference.getU_nickname());
		}

		/***����ǩ��***/
		if (userPreference.getU_introduce().isEmpty()) {
			introTextView.setText("δ��д");
		} else {
			introTextView.setText(userPreference.getU_introduce());
		}

		/***��ҵ***/
		if (userPreference.getU_industry().isEmpty()) {
			industryTextView.setText("δ��д");
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

		/***����***/
		if (userPreference.getU_skills().isEmpty()) {
			skillTextView.setText("δ��д");
		} else {
			skillTextView.setText(userPreference.getU_skills().replace("|", "\n"));
		}
		/***����***/
		if (userPreference.getU_interests().isEmpty()) {
			interestTextView.setText("δ��д");
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
		leftTextView.setText("�༭����");
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

		/***�Ա�***/
		genderText.setText(userPreference.getU_gender());

		/***��ǰ���***/
		statusTextView.setText(userPreference.getU_identity());

		/***���״��***/
		loveStatusTextView.setText(userPreference.getU_love_state());

		// ��ʾͷ��
		ImageLoader.getInstance().displayImage(AsyncHttpClientTool.getAbsoluteUrl(userPreference.getU_small_avatar()), headImage, ImageLoaderTool.getHeadImageOptions(3));

		// ��������
		if (userPreference.getU_birthday() != null) {
			birthdayTextView.setText(DateTimeTools.getDateString(userPreference.getU_birthday()));
		} else {
			birthdayTextView.setText("δ��д");
		}
	}

	/**
	* ��ʾ�Ի��򣬴����պ����ѡ��ͼƬ��Դ
	* 
	* @param context
	* @param isCrop
	*/
	private void showPicturePicker(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("ͼƬ��Դ");
		ArrayList<String> list = new ArrayList<String>();
		list.add("����");
		list.add("���");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				switch (position) {
				case 0:// ���������
					myMenuDialog.dismiss();
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getNewImagePath())));
					startActivityForResult(intent, 2);
					break;
				case 1:// �����ѡȡ
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
		// �����ֱ�Ӵ�����ȡ 
		case 1:
			if (data != null) {
				startPhotoCrop(data.getData());
			}
			break;
		// ����ǵ����������ʱ 
		case 2:
			startPhotoCrop(Uri.fromFile(new File(getImagePath())));
			break;
		// ȡ�òü����ͼƬ���ϴ� 
		case 3:
			uploadImage(lastPhotoUri.getPath());
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 
	 * �ü�ͼƬ����ʵ�� 
	 * @param uri 
	 */
	public void startPhotoCrop(Uri uri) {
		lastPhotoUri = Uri.fromFile(new File(getImagePath()));

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü� 
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

	//����µ�·��
	private String getNewImagePath() {
		// ɾ����һ�ν�ͼ����ʱ�ļ�
		String path = null;
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
		ImageTools.deleteImageByPath(sharedPreferences.getString("tempPath", ""));

		// ���汾�ν�ͼ��ʱ�ļ�����
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

	//���·��
	private String getImagePath() {
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
		return sharedPreferences.getString("tempPath", "");
	}

	/**
	 * �ϴ�ͷ��
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
							ToastTool.showLong(ModifyDataActivity.this, "ͷ���ϴ��ɹ���");

							// ��ȡ��ͷ���ַ
							Map<String, String> map = FastJsonTool.getObject(response, Map.class);
							if (map != null) {
								userPreference.setU_large_avatar(map.get(UserTable.U_LARGE_AVATAR));
								userPreference.setU_small_avatar(map.get(UserTable.U_SMALL_AVATAR));
								// ��ʾͷ��
								ImageLoader.getInstance().displayImage(AsyncHttpClientTool.getAbsoluteUrl(map.get(UserTable.U_SMALL_AVATAR)), headImage,
										ImageLoaderTool.getHeadImageOptions(3));
							} else {
								LogTool.e("�ϴ�����������" + response);
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("ͷ���ϴ�ʧ�ܣ�");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						// ɾ������ͷ��
						ImageTools.deleteImageByPath(filePath);
					}
				};
				AsyncHttpClientTool.post("user/uploadHeadImg", params, responseHandler);
			}
		} else {
			LogTool.e("ͷ���ļ�������");
		}
	}

	/**
	 * �޸�����
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
							LogTool.e("�޸����շ��ش���" + response);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("�޸����մ���");
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
	* ��ʾ�Ի����޸��Ա�
	* 
	* @param context
	* @param isCrop
	*/
	private void showgenderModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("�޸��Ա�");
		ArrayList<String> list = new ArrayList<String>();
		list.add("��");
		list.add("Ů");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					updateGender("��");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateGender("Ů");
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
	* ��ʾ�Ի����޸����
	* 
	* @param context
	* @param isCrop
	*/
	private void showStatusModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("�޸����");
		ArrayList<String> list = new ArrayList<String>();
		list.add("ѧ��");
		list.add("У��");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					updateIdentity("ѧ��");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateIdentity("У��");
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
	* ��ʾ�Ի����޸����״��
	* 
	* @param context
	* @param isCrop
	*/
	private void showLoveStatusModify(Context context) {

		final MyMenuDialog myMenuDialog = new MyMenuDialog(ModifyDataActivity.this);
		myMenuDialog.setTitle("�޸����״��");
		ArrayList<String> list = new ArrayList<String>();
		list.add("����");
		list.add("������");
		list.add("�ѻ�");
		myMenuDialog.setMenuList(list);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					updateLoveState("����");
					myMenuDialog.dismiss();
					break;
				case 1:
					updateLoveState("������");
					myMenuDialog.dismiss();
					break;
				case 2:
					updateLoveState("�ѻ�");
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
	 * ȷ���޸�ѧУ
	 */
	private void vertifyToModifySchool() {
		final MyAlertDialog dialog = new MyAlertDialog(ModifyDataActivity.this);
		dialog.setTitle("�޸Ľ�������");
		dialog.setMessage("��ֻ��һ���޸�ѧУ�Ļ��ᣬȷ��Ҫ����");
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
		dialog.setPositiveButton("ȷ��", comfirm);
		dialog.setNegativeButton("ȡ��", cancle);
		dialog.show();
	}

	/**
	 * �޸��Ա�
	 * @param name
	 */
	public void updateGender(final String gender) {
		if (!gender.equals(userPreference.getU_gender())) {
			// û�д������޸�
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_GENDER, gender);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("���Ժ�...");
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
							ToastTool.showShort(ModifyDataActivity.this, "�޸ĳɹ���");
							userPreference.setU_gender(gender);
							genderText.setText(gender);
						} else if (response.equals("-1")) {
							LogTool.e("�޸��Ա𷵻�-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("�޸��Ա����");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

		}
	}

	/**
	 * �޸����
	 * @param name
	 */
	public void updateIdentity(final String identity) {
		if (!identity.equals(userPreference.getU_identity())) {
			// û�д������޸�
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_IDENTITY, identity);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("���Ժ�...");
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
							ToastTool.showShort(ModifyDataActivity.this, "�޸ĳɹ���");
							userPreference.setU_identity(identity);
							statusTextView.setText(identity);
						} else if (response.equals("-1")) {
							LogTool.e("�޸���ݷ���-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("�޸���ݴ���");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

		}
	}

	/**
	 * �޸����״��
	 * @param name
	 */
	public void updateLoveState(final String state) {
		if (!state.equals(userPreference.getU_love_state())) {
			// û�д������޸�
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_LOVE_STATE, state);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("���Ժ�...");
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
							ToastTool.showShort(ModifyDataActivity.this, "�޸ĳɹ���");
							userPreference.setU_love_state(state);
							loveStatusTextView.setText(state);
						} else if (response.equals("-1")) {
							LogTool.e("�޸����״������-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("�޸����״������");
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
