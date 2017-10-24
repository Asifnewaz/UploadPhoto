package com.securepenny.camerapermissionandsave;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by R041708040 on 10/22/2017.
 */

public interface UploadImageInterface {
    @Multipart
    @POST("Registration/CustomerProfilePicture")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);

}
