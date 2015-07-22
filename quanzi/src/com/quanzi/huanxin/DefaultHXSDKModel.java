/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quanzi.huanxin;

/**
 * UI Demo HX Model implementation
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.quanzi.dao.UserDao;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.PreferenceUtils;

/**
 * HuanXin default SDK Model implementation
 * @author easemob
 *
 */
public class DefaultHXSDKModel extends HXSDKModel {
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PWD = "pwd";
	UserDao dao = null;
	protected Context context = null;
	protected Map<Key, Object> valueCache = new HashMap<Key, Object>();

	public DefaultHXSDKModel(Context ctx) {
		context = ctx;
	}

	@Override
	public boolean getSettingMsgNotification() {
		return PreferenceUtils.getInstance(context).getSettingMsgNotification();
	}

	@Override
	public boolean getSettingMsgSound() {

		return PreferenceUtils.getInstance(context).getSettingMsgSound();
	}

	@Override
	public boolean getSettingMsgVibrate() {
		return PreferenceUtils.getInstance(context).getSettingMsgVibrate();
	}

	@Override
	public boolean getSettingMsgSpeaker() {
		return PreferenceUtils.getInstance(context).getSettingMsgSpeaker();
	}

	@Override
	public boolean getUseHXRoster() {
		return false;
	}

	@Override
	public boolean saveHXId(String hxId) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_USERNAME, hxId).commit();
	}

	@Override
	public String getHXId() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_USERNAME, null);
	}

	@Override
	public boolean savePassword(String pwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_PWD, pwd).commit();
	}

	@Override
	public String getPwd() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_PWD, null);
	}

	@Override
	public String getAppProcessName() {
		return context.getPackageName();
	}

	enum Key {
		VibrateAndPlayToneOn, VibrateOn, PlayToneOn, SpakerOn, DisabledGroups, DisabledIds
	}
}
