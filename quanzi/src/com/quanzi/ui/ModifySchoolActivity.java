package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.config.DefaultKeys;
import com.quanzi.db.CityDbService;
import com.quanzi.db.ProvinceDbService;
import com.quanzi.db.SchoolDbService;
import com.quanzi.entities.City;
import com.quanzi.entities.Province;
import com.quanzi.entities.School;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ModifySchoolActivity  
 * 类描述：修改学校的页面
 * @author zhangshuai
 * @date 创建时间：2015-7-13 上午11:16:25 
 *
 */
public class ModifySchoolActivity extends BaseActivity implements OnClickListener {

	/*************Views************/
	private Spinner mProvinceView;//省
	private Spinner mCityView;//城市
	private Spinner mSchoolView;//学校
	private View backBtn;
	private TextView saveBtn;

	public SharedPreferences locationPreferences;// 记录用户位置
	private UserPreference userPreference;
	private String mProvince = "";
	private String mCity = "";
	private Province currentProvince;
	private City currentCity;
	private School currentSchool;

	private List<Province> provinceList;//省份列表
	private List<String> provinceNameList;//省份名称列表
	private List<City> cityList;//城市列表
	private List<String> cityNameList;//城市名称列表
	private List<School> schoolList;//学校列表
	private List<String> schoolNameList;//学校名称列表

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_school);
		userPreference = BaseApplication.getInstance().getUserPreference();

		//获取用户位置
		locationPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
		mProvince = locationPreferences.getString(DefaultKeys.USER_PROVINCE, "");
		mCity = locationPreferences.getString(DefaultKeys.USER_CITY, "");

		currentProvince = new Province();
		currentCity = new City();
		currentSchool = new School();

		findViewById();// 初始化views
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mProvinceView = (Spinner) findViewById(R.id.province);
		mCityView = (Spinner) findViewById(R.id.city);
		mSchoolView = (Spinner) findViewById(R.id.school);
		backBtn = findViewById(R.id.left_btn_bg);
		saveBtn = (TextView) findViewById(R.id.publish_btn);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		saveBtn.setEnabled(false);

		//初始化省份信息
		initProvinceData();

		//点击省份时初始化对应城市
		mProvinceView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				cityList = CityDbService.getInstance(ModifySchoolActivity.this).getCityListByProvince(
						provinceList.get(position));
				cityNameList = new ArrayList<String>();
				if (cityList != null) {
					for (City city : cityList) {
						cityNameList.add(city.getCityName());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifySchoolActivity.this,
							android.R.layout.simple_spinner_dropdown_item, cityNameList);
					mCityView.setAdapter(adapter);
				}
				currentProvince = provinceList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		//点击城市时初始化对应学校
		mCityView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				currentCity = cityList.get(position);

				schoolList = new ArrayList<School>();
				schoolNameList = new ArrayList<String>();
				schoolList = SchoolDbService.getInstance(ModifySchoolActivity.this).getSchoolListByCity(
						cityList.get(position));
				int currentPostion = 0;

				if (schoolList != null) {
					for (int i = 0; i < schoolList.size(); i++) {
						schoolNameList.add(schoolList.get(i).getSchoolName());
						if (currentSchool != null && currentSchool.getId() != null
								&& currentSchool.getId().intValue() == schoolList.get(i).getId().intValue()) {
							currentPostion = i;
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifySchoolActivity.this,
							android.R.layout.simple_spinner_dropdown_item, schoolNameList);
					mSchoolView.setAdapter(adapter);
					mSchoolView.setSelection(currentPostion, true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		//选学校时
		mSchoolView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				currentSchool = schoolList.get(position);
				saveBtn.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		//初始化城市信息
		initCityData();
	}

	/**
	 * 初始化省份信息
	 */
	private void initProvinceData() {
		int currentPostion = 0;//当前位置
		provinceList = new ArrayList<Province>();
		provinceNameList = new ArrayList<String>();
		provinceList = ProvinceDbService.getInstance(ModifySchoolActivity.this).provinceDao.loadAll();
		int PID = userPreference.getU_provinceid();

		if (provinceList != null) {
			for (int i = 0; i < provinceList.size(); i++) {
				provinceNameList.add(provinceList.get(i).getProvinceName());
				if (PID > -1) {
					if (provinceList.get(i).getProvinceID().intValue() == PID) {
						currentProvince = provinceList.get(i);
						currentPostion = i;
					}
				} else {
					if (provinceList.get(i).getProvinceName().contains(mProvince)
							|| mProvince.contains(provinceList.get(i).getProvinceName())) {
						currentProvince = provinceList.get(i);
						currentPostion = i;
					}
				}
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifySchoolActivity.this,
				android.R.layout.simple_spinner_dropdown_item, provinceNameList);
		mProvinceView.setAdapter(adapter);
		mProvinceView.setSelection(currentPostion, true);
	}

	/**
	 * 初始化城市信息
	 */
	private void initCityData() {
		int currentPostion = 0;//当前位置
		if (currentProvince != null) {
			cityList = CityDbService.getInstance(ModifySchoolActivity.this).getCityListByProvince(currentProvince);
			cityNameList = new ArrayList<String>();
			if (cityList != null) {
				for (int i = 0; i < cityList.size(); i++) {
					cityNameList.add(cityList.get(i).getCityName());
					//如果已经有保存
					if (userPreference.getU_cityid() > -1) {
						if (cityList.get(i).getCityID().intValue() == userPreference.getU_cityid()) {
							currentCity = cityList.get(i);
							currentPostion = i;
						}
					} else {
						if (cityList.get(i).getCityName().contains(mCity)
								|| mCity.contains(cityList.get(i).getCityName())) {
							currentCity = cityList.get(i);
							currentPostion = i;
						}
					}
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifySchoolActivity.this,
						android.R.layout.simple_spinner_dropdown_item, cityNameList);
				mCityView.setAdapter(adapter);
				mCityView.setSelection(currentPostion, true);
			}
		}
	}

	/**
	 * 检查学校
	 */
	private void attemptSchool() {

		boolean cancel = false;

		//检查是否选择省
		if (mProvinceView.getSelectedItem().toString().length() == 0) {
			cancel = true;
			ToastTool.showShort(ModifySchoolActivity.this, "请选择省");
		}

		//检查是否选城市
		else if (mCityView.getSelectedItem().toString().length() == 0) {
			cancel = true;
			ToastTool.showShort(ModifySchoolActivity.this, "请选择所在城市");
		}
		//检查是否选学校
		else if (mSchoolView.getSelectedItem().toString().length() == 0) {
			cancel = true;
			ToastTool.showShort(ModifySchoolActivity.this, "请选择所在学校");
		}

		if (!cancel) {
			// 没有错误
			userPreference.setU_provinceid(currentProvince.getProvinceID().intValue());
			userPreference.setU_cityid(currentCity.getCityID().intValue());
			userPreference.setU_schoolid(currentSchool.getId().intValue());

			updateSchool();
		}
	}

	/**
	 * 更新学校
	 */
	private void updateSchool() {
		if (currentSchool != null) {
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_SCHOOLID, currentSchool.getId());

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
							ToastTool.showShort(ModifySchoolActivity.this, "修改成功！");
							userPreference.setU_provinceid(currentProvince.getProvinceID().intValue());
							userPreference.setU_cityid(currentCity.getCityID().intValue());
							userPreference.setU_schoolid(currentSchool.getId().intValue());
							finish();
							overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
						} else if (response.equals("-1")) {
							LogTool.e("修改学校返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改学校错误");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.publish_btn:
			attemptSchool();
			break;

		default:
			break;
		}
	}
}
