/*
 * Copyright (c) 2016
 *
 * This file, SettingsDetailActivity.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import xyz.jamescarroll.genipass.Async.AsyncTestVector;
import xyz.jamescarroll.genipass.Crypto.TestManager;


public class SettingsDetailActivity extends AppCompatActivity implements AsyncTestVector.OnResult {
    private static final String TAG = "SettingsDetailActivity";
    public static final String kExtraOpenLicense = TAG + ".OPEN_LICENSE";
    public static final String kExtraTestVector = TAG + "TEST_VECTOR";

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TestManager tm = TestManager.getInstance();

        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

        findTextView().setMovementMethod(new ScrollingMovementMethod());

        if (getIntent() != null && getIntent().getAction() != null) {
            if (getIntent().getAction().equals(kExtraTestVector)) {
                if (!tm.ismStartTest()) {
                    showProgress();
                    tm.startTest();
                    tm.setmAsyncTestVector(new AsyncTestVector(this, this));
                    tm.getmAsyncTestVector().execute();
                }
            }
        }

        if (tm.ismStartTest() && !tm.ismEndTest()) {
            showProgress();
            tm.getmAsyncTestVector().setmContext(this);
            tm.getmAsyncTestVector().setmListener(this);
            mProgress.setProgress(tm.getmAsyncTestVector().getmProgress());
        }

        if (tm.ismEndTest()) {
            handleProgressOnResult();
        }
    }

    private void createProgress() {
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Running Crypto Test");
        mProgress.setMessage("Please wait.");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.setProgress(0);
        mProgress.setMax(5);
        mProgress.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTestVector test = TestManager.getInstance().getmAsyncTestVector();

                if (test != null && !test.isCancelled()) {
                    test.cancel(true);
                }

                handleProgressOnResult();
            }
        });
    }

    private void showProgress() {
        if (mProgress == null) {
            createProgress();
        }

        mProgress.show();
    }

    private TextView findTextView() {
        return (TextView) findViewById(R.id.tv_settings_detail);
    }

    private void handleProgressOnResult() {
        TestManager.getInstance().setmAsyncTestVector(null);

        if (mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onResult(String result) {
        findTextView().setText(result);
        handleProgressOnResult();
    }

    @Override
    public void onUpdate(int i) {
        mProgress.setProgress(i);
    }
}
