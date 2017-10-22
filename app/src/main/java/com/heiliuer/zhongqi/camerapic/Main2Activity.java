package com.heiliuer.zhongqi.camerapic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main2Activity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        findViewById(R.id.select_img).setOnClickListener((v) -> {
//            pickImg();
//        });
        reP();
        pickImg();

    }

    private void pickImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE) {
            // 从相册返回的数据
            if (data != null) {
                setResultImage2(data);
//                setResultImage(data);
            }
        }
    }


    private void saveImageToExtraOutput(Uri pickUri) {

        Uri uri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        InputStream image_stream;
        try {
            image_stream = getContentResolver().openInputStream(pickUri);
            OutputStream outputStream = getContentResolver().openOutputStream(uri);

            byte[] buffer = new byte[1444];
            int byteread;
            while ((byteread = image_stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteread);
            }
            image_stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void setResultImage2(Intent data) {
        Intent resultIntent = new Intent();
        Uri pickUri = data.getData();
        saveImageToExtraOutput(pickUri);
        resultIntent.setData(pickUri);
        resultIntent.putExtra(MediaStore.EXTRA_OUTPUT, pickUri);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void setResultImage(Intent data) {
        // 得到图片的全路径
        Uri uri = data.getData();
        Log.v("myLog", "uri:" + uri.toString());

        InputStream image_stream;
        try {
            image_stream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
            Intent data1 = new Intent();
            data1.putExtra("data", bitmap);
            setResult(Activity.RESULT_OK, data1);
            finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
