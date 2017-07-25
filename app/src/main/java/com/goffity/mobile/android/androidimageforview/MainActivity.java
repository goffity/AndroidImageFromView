package com.goffity.mobile.android.androidimageforview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final String TAG = MainActivity.class.getSimpleName();

    private Button button;
    private TextView textViewTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimeStamp = findViewById(R.id.textViewTimeStamp);

        button = findViewById(R.id.button);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission not grant");
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    6);
        }

        final String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(
                new Date());

        textViewTimeStamp.setText(timeStamp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store(getScreenShot(view), "Screenshots_" + timeStamp + ".png");
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 6: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                } else {
                    Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE Denied",
                                   Toast.LENGTH_SHORT).show();
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Bitmap getScreenShot(View view) {
        Log.i(TAG, "getScreenShot()");
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Log.d(TAG, "getScreenShot: " + screenView);
        Log.d(TAG, "getScreenShot: " + screenView.getDrawingCache());
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bm, String fileName) {
        Log.i(TAG, "store()");
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Goffity";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean createDir = dir.mkdirs();
            Log.d(TAG, "store: create dir: " + createDir);
        }
        File file = new File(dirPath, fileName);
        Log.d(TAG, "store: " + file.getAbsolutePath());
        Log.d(TAG, "store: " + file.getPath());
        Log.d(TAG, "store: " + fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void shareImage(File file) {
//        Uri uri = Uri.fromFile(file);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        try {
//            startActivity(Intent.createChooser(intent, "Share Screenshot"));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
//        }
//    }
}
