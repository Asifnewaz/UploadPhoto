package com.securepenny.camerapermissionandsave;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by R041708040 on 10/24/2017.
 */

public class UploadImageToServer { // extends AsyncTask<Void, Integer, String> {
//    @Override
//    protected void onPreExecute() {
//        // setting progress bar to zero
//        //  progressBar.setProgress(0);
//        super.onPreExecute();
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        return uploadFile();
//    }
//
//    @SuppressWarnings("deprecation")
//    private String uploadFile() {
//        String responseString = null;
//
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(AppConfig.URL_PHOTO);
//
//        try {
//            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                    new AndroidMultiPartEntity.ProgressListener() {
//
//
//                        @Override
//                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
//                        }
//                    });
//
//
//            File sourceFile = new File(filePath);
//            ;
//            // Adding file data to http body
//            entity.addPart("image", new FileBody(sourceFile));
//
//            // Extra parameters if you want to pass to server
//            entity.addPart("userid",
//                    new StringBody(session.getuid()));
//
//
//            totalSize = entity.getContentLength();
//            httppost.setEntity(entity);
//
//            // Making server call
//
//            HttpResponse response = httpclient.execute(httppost);
//
//            HttpEntity r_entity = response.getEntity();
//
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            if (statusCode == 200) {
//                // Server response
//
//                responseString = EntityUtils.toString(r_entity);
//
//            } else {
//                responseString = "Error occurred! Http Status Code: "
//                        + statusCode;
//            }
//
//        } catch (ClientProtocolException e) {
//            responseString = e.toString();
//        } catch (IOException e) {
//            responseString = e.toString();
//        }
//
//        return responseString;
//
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        Log.e(TAG, "Response from server: " + result);
//
//        // showing the server response in an alert dialog
//
//
//        super.onPostExecute(result);
//
//    }
//
//


}