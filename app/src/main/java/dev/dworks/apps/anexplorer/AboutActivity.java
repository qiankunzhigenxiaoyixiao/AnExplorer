/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.dworks.apps.anexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.dworks.apps.anexplorer.misc.Utils;
import dev.dworks.apps.anexplorer.setting.SettingsActivity;

import static dev.dworks.apps.anexplorer.DocumentsActivity.getStatusBarHeight;

public class AboutActivity extends ActionBarActivity implements View.OnClickListener {

	public static final String TAG = "About";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitleTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
		if(Utils.hasKitKat() && !Utils.hasLollipop()) {
			((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).setMargins(0, getStatusBarHeight(this), 0, 0);
			mToolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
		}
		int color = SettingsActivity.getActionBarColor(this);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(null);

		initControls();
	}

	@Override
	public String getTag() {
		return TAG;
	}

	private void initControls() {

		TextView logo = (TextView)findViewById(R.id.logo);
		logo.setTextColor(Utils.getComplementaryColor(SettingsActivity.getActionBarColor(this)));
		String header = logo.getText() + getSuffix() + (Utils.isTelevision(this)? " for Android TV" : "") + " v" + BuildConfig.VERSION_NAME;
		logo.setText(header);

		TextView action_rate = (TextView)findViewById(R.id.action_rate);
		TextView action_support = (TextView)findViewById(R.id.action_support);
		TextView action_share = (TextView)findViewById(R.id.action_share);
		TextView action_feedback = (TextView)findViewById(R.id.action_feedback);

		action_rate.setOnClickListener(this);
		action_support.setOnClickListener(this);
		action_share.setOnClickListener(this);
		action_feedback.setOnClickListener(this);

		if(isNonPlay()){
			action_rate.setVisibility(View.GONE);
			action_support.setVisibility(View.GONE);
		} else if(Utils.isTelevision(this)){
			action_share.setVisibility(View.GONE);
			action_feedback.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void startActivity(Intent intent) {
        if(Utils.isIntentAvailable(this, intent)) {
            super.startActivity(intent);
        }
    }

    private String getSuffix(){
        return Utils.isProVersion() ? " Pro" : "";
    }

    private boolean isNonPlay(){
        return BuildConfig.FLAVOR.contains("other");
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.action_github:
				startActivity(new Intent("android.intent.action.VIEW",
						Uri.parse("https://github.com/DWorkS")));
				break;
			case R.id.action_gplus:
				startActivity(new Intent("android.intent.action.VIEW",
						Uri.parse("https://plus.google.com/+HariKrishnaDulipudi")));
				break;
			case R.id.action_twitter:
				startActivity(new Intent("android.intent.action.VIEW",
						Uri.parse("https://twitter.com/1HaKr")));
				break;
			case R.id.action_feedback:
				ShareCompat.IntentBuilder
						.from(this)
						.setEmailTo(new String[]{"hakr@dworks.in"})
						.setSubject("AnExplorer Feedback" + getSuffix())
						.setType("text/email")
						.setChooserTitle("Send Feedback")
						.startChooser();
				break;
			case R.id.action_rate:
				Intent intentMarket = new Intent("android.intent.action.VIEW");
				intentMarket.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
				startActivity(intentMarket);
				break;
			case R.id.action_support:
				Intent intentMarketAll = new Intent("android.intent.action.VIEW");
				intentMarketAll.setData(Uri.parse("market://search?q=pub:DWorkS"));
				startActivity(intentMarketAll);
				break;
			case R.id.action_share:
				ShareCompat.IntentBuilder
						.from(this)
						.setText(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID).toString())
						.setType("text/plain")
						.setChooserTitle("Share AnExplorer")
						.startChooser();
				break;
		}
	}
}