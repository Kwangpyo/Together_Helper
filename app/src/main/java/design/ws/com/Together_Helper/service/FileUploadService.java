package design.ws.com.Together_Helper.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface FileUploadService {

    @Multipart
    @PUT("/helper/photo")
    Call<ResponseBody> upload(
            @Part("userId") RequestBody user_phone,
            @Part MultipartBody.Part userfile
    );


}
