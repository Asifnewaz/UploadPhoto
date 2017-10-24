package com.securepenny.camerapermissionandsave;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;
import static android.R.attr.thumbnail;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int REQUEST_GALLERY_CODE = 44;
    Button btnTakePhoto,btnGalary,btnUpload,btnDownload,btnSave,btnClear;
    ImageView ivShowImage;
    Bitmap thumbnail;
    Uri selectedImageUri;
    String imagepath,filePath,finalFile;
    int x =0,y=0;
    private static final String SERVER_PATH = "http://192.168.40.130:6081/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnGalary = (Button) findViewById(R.id.btnGalary);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnClear = (Button) findViewById(R.id.btnClear);
        ivShowImage = (ImageView) findViewById(R.id.ivShowImage);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission(Manifest.permission.CAMERA,REQUEST_IMAGE_CAPTURE);
            }
        });
        btnGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission(Manifest.permission.READ_EXTERNAL_STORAGE,REQUEST_GALLERY_CODE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(y==1){
                    uploadImageGalary();
                    y=0;
                }
                else if(y==2){
                    uploadImageCamera();
                    y=0;
                }else
                {
                    Toast.makeText(MainActivity.this,"Select An Image",Toast.LENGTH_LONG).show();
                }

            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ivShowImage.setImageResource(0);
                x=0;
                y=0;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x!=0){
                    // Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AsifNewaz"+System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Toast.makeText(MainActivity.this,"Saved Baby",Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Capture an Image",Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    // This is for asking permission
    private void askPermission(String permission, int reqCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, reqCode); // if do not  have permission
        }else {
            // if have permission
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();

            if(reqCode == REQUEST_IMAGE_CAPTURE){
                clickpic();
            }
            else if(reqCode == REQUEST_GALLERY_CODE){
                openGalary();
            }
        }
    }
    // Ract on if granted permission or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                clickpic();
            }
            else if(requestCode == REQUEST_GALLERY_CODE){
                openGalary();
            }
        }
        else {
            Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_LONG).show();
        }
    }

    private void clickpic() {
        // Check Camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGalary() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == RESULT_OK) {
            if(requestCode == REQUEST_IMAGE_CAPTURE){

                thumbnail = (Bitmap) data.getExtras().get("data");
                ivShowImage.setImageBitmap(thumbnail);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
               // Uri tempUri = data.getData();

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = getRealPathFromURI(tempUri);
                x=1;
                y=2;
// Here you have the ImagePath which you can set to you image view
//                Bitmap myBitmap = (Bitmap) data.getExtras().get("data");
//                ivShowImage.setImageBitmap(myBitmap);
            }
            else if(requestCode == REQUEST_GALLERY_CODE){
                try {
                    selectedImageUri = data.getData();
                    imagepath = getPath(selectedImageUri);
                   // imagepath = selectedImageUri.getPath();

                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ivShowImage.setImageBitmap(selectedImage);
                    y=1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void uploadImageGalary() {

        File file = new File(imagepath);
        Log.d(TAG, "Filename " + file.getName());
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)//*****************************************************************************************************************
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
        Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                try {
                    UploadObject uploadObject = response.body();
                    Log.e(TAG, "uploadObject: " + new Gson().toJson(uploadObject));
                    if (uploadObject!=null) {
                        if(uploadObject.getSuccess()) {
                            Toast.makeText(MainActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Error " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Error " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
//                Toast.makeText(MainActivity.this, "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Log.d(TAG, "Error " + t.getMessage());
            }
        });
    }

    private void uploadImageCamera() {
       // Toast.makeText(MainActivity.this,"Blank",Toast.LENGTH_LONG).show();
        File file = new File(finalFile);
        Log.d(TAG, "Filename " + file.getName());
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)//*****************************************************************************************************************
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
        Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                try {
                    UploadObject uploadObject = response.body();
                    Log.e(TAG, "uploadObject: " + new Gson().toJson(uploadObject));
                    if (uploadObject!=null) {
                        if(uploadObject.getSuccess()) {
                            Toast.makeText(MainActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Error " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Error " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Toast.makeText(MainActivity.this, "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Log.d(TAG, "Error " + t.getMessage());
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
