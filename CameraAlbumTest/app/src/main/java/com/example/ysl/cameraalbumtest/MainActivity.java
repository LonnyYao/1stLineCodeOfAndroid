package com.example.ysl.cameraalbumtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int TAKE_PHOTO = 1;
    private static final int PICKUP_PICTURE = 2;

    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btPhoto = findViewById(R.id.bt_take_photo);
        picture = findViewById(R.id.iv_picture);

        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create File object to store the picture by camera
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                Log.i(TAG, "onClick: outputImage.path: " + outputImage.getAbsolutePath());
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    // Need to use FileProvider to get "content:// URI" instead of "file:// URI"
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                Log.i(TAG, "onClick: imageUri = " + imageUri.toString());

                // start camera application
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        Button btPickup = findViewById(R.id.bt_pickup_pic);
        btPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check the permission at first
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission.", Toast.LENGTH_SHORT).show();
                }
            break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // Use "getSystemService(Context.ACTIVITY_SERVICE)" get the the ActivityManager
                        ActivityManager activityManager =
                                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        // Find the version of GLES from ConfigurationInfo
                        ConfigurationInfo configurationInfo =
                                activityManager.getDeviceConfigurationInfo();
                        if (configurationInfo != null) {
                            String glesVersion = configurationInfo.getGlEsVersion();
                            Log.i(TAG, "onActivityResult: getGlEsVersion = " + glesVersion);
                        }

                        // How to get the maxHeight, maxWidth to protect overloading texture
                        // in OpenGLRenderer from GLES 2.0, 3.0 ???

                        // Show the picture in the ImageView
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);

                        /**
                         * Some Warning level Exception occurs when the photo is too big from camera
                         * like : " W/OpenGLRenderer: Bitmap too large to be uploaded into a texture (5312x2988, max=4096x4096) "
                         *
                         * So, we need to scale the bitmap before set it to the ImageView
                         */
                        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap,
                                bitmap.getWidth()/2, bitmap.getHeight()/2, true);
                        picture.setImageBitmap(bitmapScaled);
                        // bitmapScaleInit(...)
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PICKUP_PICTURE:
                if (resultCode == RESULT_OK) {
                    // Check the OS Version
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        hanldeImageBeforeKitKat(data);
                    }
                }
                break;

            default:
                break;
        }

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, PICKUP_PICTURE);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent intent) {

        Uri uri = intent.getData();
        String imagePath = null;

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                Log.i(TAG, "handleImageOnKitKat: Uri media.documents");
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                Log.i(TAG, "handleImageOnKitKat: downloads.documents. ");
            } else {
                Log.w(TAG, "handleImageOnKitKat: ---> Other unknown documents...");
            }
        } else if (uri.getScheme().equalsIgnoreCase("content")) {
            Log.i(TAG, "handleImageOnKitKat: Uri content.");
            imagePath = getImagePath(uri, null);
        } else if (uri.getScheme().equalsIgnoreCase("file")) {
            Log.i(TAG, "handleImageOnKitKat: Uri file.");
            imagePath = uri.getPath();
        } else {
            Log.w(TAG, "handleImageOnKitKat: ---> Other unknown Uri type...");
        }

        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    private void displayImage(String path) {
        if (path != null) {
            Log.i(TAG, "displayImage: path = " + path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Log.i(TAG, "displayImage: bitmap size = " + bitmap.getWidth() + " x " + bitmap.getHeight());
            //picture.setImageBitmap(bitmap);
            /**
             * Some Warning level Exception occurs when the photo is too big from camera
             * like : " W/OpenGLRenderer: Bitmap too large to be uploaded into a texture (5312x2988, max=4096x4096) "
             *
             * So, we need to scale the bitmap before set it to the ImageView
             */
            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth()/2, bitmap.getHeight()/2, true);
            picture.setImageBitmap(bitmapScaled);
        } else {
            Toast.makeText(this, "Failed to get image...", Toast.LENGTH_SHORT).show();
        }
    }

    private void hanldeImageBeforeKitKat(Intent intent) {
        Uri uri = intent.getData();
        String imagePath = uri.getPath();

        displayImage(imagePath);
    }

    private void bitmapScaleInit(Bitmap bitmap, InputStream inputStream) {

        /**
         * google map的实现：将图片分成不同的块，每次加载需要的块。android提供了一个方法：
         * http://developer.android.com/reference/android/graphics/BitmapRegionDecoder.html
         *
         * public void drawBitmap (Bitmap bitmap, Rect src, RectF dst, Paint paint)
         *
         * public Bitmap decodeRegion (Rect rect, BitmapFactory.Options options)
         *
         */

    }
}
