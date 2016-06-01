/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.devices.hedgehog;

import android.os.AsyncTask;
import android.util.Log;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.ui.SettingsActivity;
import org.zeromq.ZMQ;

import java.util.concurrent.TimeUnit;

import at.pria.hedgehog.client.HedgehogClient;

public final class HedgehogClientWrapper {
	private static final String TAG = HedgehogClientWrapper.class.getSimpleName();

	private static HedgehogClientWrapper instance = null;

	public static HedgehogClientWrapper getInstance() {
		if (instance == null) {
			instance = new HedgehogClientWrapper();
		}

		return instance;
	}

	public static boolean checkHedgehogAvailability() {
		int requiredResources = ProjectManager.getInstance().getCurrentProject().getRequiredResources();
		boolean isHedgehogAvailable = (((requiredResources & Brick.HEDGEHOG) > 0) && BuildConfig.FEATURE_HEDGEHOG_ENABLED);
		Log.d(TAG, "Hedgehog pref enabled? " + isHedgehogSharedPreferenceEnabled());
		return isHedgehogAvailable; // isDroneSharedPreferenceEnabled()
	}

	public static boolean isHedgehogSharedPreferenceEnabled() {
		return SettingsActivity.isHedgehogSharedPreferenceEnabled(CatroidApplication.getAppContext());
	}

	private HedgehogClient client;

	private HedgehogClientWrapper() {
		connect();
	}

	public HedgehogClient getClient() {
		return client;
	}

	private void connect() {
		try {
			client = new AsyncConnectTask("tcp://192.168.43.86:42907").execute().get(2000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Log.e(TAG, "Hedgehog connection faliled");
		}
	}

	private class AsyncConnectTask extends AsyncTask<Void, Void, HedgehogClient> {
		private String endpoint;

		public AsyncConnectTask(String endpoint) {
			this.endpoint = endpoint;
		}

		protected HedgehogClient doInBackground(Void... args) {
			return new HedgehogClient(endpoint, ZMQ.context(1));
		}
	}
}
