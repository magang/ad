package com.chanlin.ad.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.chanlin.ad.QDMainActivity;
import com.qmuiteam.qmui.arch.QMUILatestVisit;
import com.qmuiteam.qmui.arch.annotation.ActivityScheme;


/**
 * @author cginechen
 * @date 2016-12-08
 */

@ActivityScheme(name = "launcher")
public class LauncherActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_CODE = 10;
    private static final String[] PERMISSIONS_REQUIRED = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if (allPermissionsGranted()) {
            doAfterPermissionsGranted();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                doAfterPermissionsGranted();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void doAfterPermissionsGranted() {
        Intent intent = QMUILatestVisit.intentOfLatestVisit(this);
        if (intent == null) {
            intent = new Intent(this, QDMainActivity.class);
        }
        startActivity(intent);
        finish();
    }


    private boolean allPermissionsGranted() {
//        for (String permission : PERMISSIONS_REQUIRED) {
//            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
