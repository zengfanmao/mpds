package com.aimissu.basemvp.net.rx;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**

 * 动态url的通用Http请求接口
 */
public interface BaseApiService {

    @GET
    Flowable<Response<ResponseBody>> executeGet(@Url String url, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @GET
    Flowable<Response<ResponseBody>> executeGet(@Url String url, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Flowable<Response<ResponseBody>> executePost(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @POST
    Flowable<Response<ResponseBody>> executePostJson(@Url String url, @Body RequestBody jsonStr, @HeaderMap Map<String, String> headers);

    @POST
    Flowable<Response<ResponseBody>> executePostNoParams(@Url String url, @HeaderMap Map<String, String> headers);

    @DELETE
    Flowable<Response<ResponseBody>> executeDeleteNoParams(@Url String url, @HeaderMap Map<String, String> headers);

    @DELETE
    Flowable<Response<ResponseBody>> executeDelete(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);


    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     *
     * @param parts 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @FormUrlEncoded
    @POST()
    Flowable<Response<ResponseBody>> uploadFilesWithParts(@Url String url, @Part() List<MultipartBody.Part> parts);


    /**
     * 通过 MultipartBody和@body作为参数来上传
     *
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST()
    Flowable<Response<ResponseBody>> uploadFileWithRequestBody(@Url String url, @Body MultipartBody multipartBody);


    @Streaming
    @GET
    Flowable<Response<ResponseBody>> downloadFile(@Url String fileUrl, @HeaderMap Map<String, String> headers);


    @Streaming
    @GET
    Flowable<Response<ResponseBody>> downloadFile(@Url String fileUrl, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @POST
    Call<ResponseBody> postNoParams(@Url String url, @HeaderMap Map<String, String> headers);

}
