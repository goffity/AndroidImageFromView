package com.goffity.mobile.android.androidimageforview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private Button btnCreateImage;
    private TextView textViewTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimeStamp = findViewById(R.id.textViewTimeStamp);

        button = findViewById(R.id.button);
        btnCreateImage = findViewById(R.id.btnCreateImage);

        final String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(
                new Date());

        final String fileName = "Screenshots_" + timeStamp + ".png";

        store(createClusterBitmap(getApplicationContext()), fileName);
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

        final String fileName = "Screenshots_" + timeStamp + ".png";

        textViewTimeStamp.setText(timeStamp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store(getScreenShot(view), fileName);
            }
        });

        btnCreateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store(createClusterBitmap(getApplicationContext()), fileName);
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
        Log.d(TAG, "view: " + view);
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Log.d(TAG, "screenView: " + screenView);
        Log.d(TAG, "screenView.getDrawingCache(): " + screenView.getDrawingCache());
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

    private Bitmap createClusterBitmap(Context context) {
        View cluster = LayoutInflater.from(context).inflate(R.layout.receipt, null, false);

        RelativeLayout.LayoutParams layoutParams;

        RelativeLayout relativeLayout = cluster.findViewById(R.id.relativeLayoutReceipt);
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

        ImageView imageView = cluster.findViewById(R.id.imgLogo);
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);

        TextView txtViewShop = cluster.findViewById(R.id.txtShop);
        txtViewShop.setText(getResources().getString(R.string.textview_shop_th));
        txtViewShop.setGravity(Gravity.END);
        RelativeLayout.LayoutParams textViewShopLayoutParams = (RelativeLayout.LayoutParams) txtViewShop.getLayoutParams();
        textViewShopLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, RelativeLayout.TRUE);
        txtViewShop.setLayoutParams(textViewShopLayoutParams);

        TextView textViewShopAddr = cluster.findViewById(R.id.txtShopAddr);
        textViewShopAddr.setText(getResources().getString(R.string.textview_shop_addr_th));

        EditText editText = cluster.findViewById(R.id.editText);
        editText.setText(getResources().getString(R.string.textedit_shop_th));
        layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.txtShopAddr);
        editText.setLayoutParams(layoutParams);

        ImageView imageViewFooter = cluster.findViewById(R.id.imgFooter);
        imageViewFooter.setImageResource(R.mipmap.ic_launcher_foreground);
        layoutParams = (RelativeLayout.LayoutParams) imageViewFooter.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.editText);
        imageViewFooter.setLayoutParams(layoutParams);
        imageViewFooter.setAdjustViewBounds(true);

        TextView txtFooter = cluster.findViewById(R.id.txtFooter);
        layoutParams = (RelativeLayout.LayoutParams) txtFooter.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtFooter.setLayoutParams(layoutParams);

        cluster.setLayoutParams(
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                RelativeLayout.LayoutParams.MATCH_PARENT));

        cluster.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        cluster.layout(0, 0, cluster.getMeasuredWidth(), cluster.getMeasuredHeight());

        Log.d(TAG, "cluster.getMeasuredWidth(): " + cluster.getMeasuredWidth());
        Log.d(TAG, "cluster.getMeasuredHeight(): " + cluster.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(cluster.getMeasuredWidth(),
                                                         cluster.getMeasuredHeight(),
                                                         Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(clusterBitmap);
        cluster.draw(canvas);

        return clusterBitmap;
    }
}
