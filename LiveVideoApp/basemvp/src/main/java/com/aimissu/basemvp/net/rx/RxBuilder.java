package com.aimissu.basemvp.net.rx;

import com.google.gson.Gson;

import io.reactivex.Flowable;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**

 * 日志打印构建
 */

public class RxBuilder<T> {

    private static final String TAG = "RxLog";

    private Class<T> parseClass;
    private String requestUrl;
    private Object requestJsons;
    private Headers requestHeaders;
    private String responseData;
    private ResultExceptionUtils.ResponseThrowable responseThrowable;
    private boolean isSuccessed;
    public int code;
    public String message;
    public boolean requestFromH5 = false;
    private String requestMethod;

    private Flowable flowable;

    private String requestID;

    public static class Builder<T> {
        private Class<T> parseClass;
        private String requestUrl;
        private Object requestJsons;
        private Headers requestHeaders;
        private String responseData;
        private ResultExceptionUtils.ResponseThrowable responseThrowable;
        private boolean isSucessed = true;
        private int code;
        private String message;
        public boolean requestFromH5;
        private String requestMethod;
        private Flowable flowable;
        private String requestID;

        public Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public Builder setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder setRequestFromH5(boolean requestFromH5) {
            this.requestFromH5 = requestFromH5;
            return this;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setParseClass(Class<T> parseClass) {
            this.parseClass = parseClass;
            return this;
        }

        public Builder setSucessed(boolean sucessed) {
            isSucessed = sucessed;
            return this;
        }


        public Builder setResponseData(String responseData) {
            this.responseData = responseData;
            return this;
        }

        public Builder setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public Builder setRequestJsons(Object requestJsons) {
            this.requestJsons = requestJsons;
            return this;
        }

        public Builder setRequestHeaders(Headers requestHeaders) {
            this.requestHeaders = requestHeaders;
            return this;
        }

        public String getRequestID() {
            return requestID;
        }

        public Builder setRequestID(String requestID) {
            this.requestID = requestID;
            return this;
        }

        public Builder setResponseThrowable(ResultExceptionUtils.ResponseThrowable responseThrowable) {
            this.responseThrowable = responseThrowable;
            return this;
        }

        public Headers getRequestHeaders() {
            return requestHeaders;
        }

        public void setFlowable(Flowable flowable) {
            this.flowable = flowable;
        }

        public Flowable getFlowable() {
            return flowable;
        }

        public RxBuilder build() {
            return new RxBuilder<T>(this);
        }
    }

    private RxBuilder(Builder builder) {
        this.parseClass = builder.parseClass;
        this.requestHeaders = builder.requestHeaders;
        this.requestJsons = builder.requestJsons;
        this.requestUrl = builder.requestUrl;
        this.responseData = builder.responseData;
        this.responseThrowable = builder.responseThrowable;
        this.isSuccessed = builder.isSucessed;
        this.code = builder.code;
        this.message = builder.message;
        this.requestFromH5 = builder.requestFromH5;
        this.requestMethod = builder.requestMethod;
        this.flowable = builder.flowable;
        this.requestID = builder.requestID;
    }

    public Flowable getFlowable() {
        return flowable;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private Gson mGson = new Gson();


    public String buildLogMsg() {
        if (getResponseThrowable() != null && getResponseThrowable().getCause() instanceof HttpException) {
            HttpException httpException = (HttpException) getResponseThrowable().getCause();
            try {
                Response response = httpException.response();
                if (response != null) {
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
                        setResponseData(new String(responseBody.bytes()));
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            } catch (Error e3) {
                e3.printStackTrace();
            }
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.requestUrl));
        try {
            stringBuilder.append(" -->> http get result ");
            if (isSuccessed()) {
                stringBuilder.append("Successed !");
            } else {
                stringBuilder.append("Failed !");
            }
            stringBuilder.append(" http status code:");
            stringBuilder.append(String.valueOf(getCode()));
            stringBuilder.append(", msg:");
            stringBuilder.append(String.valueOf(getMessage()));
            stringBuilder.append(requestFromH5 ? " , requestFrom: H5 chromium" : " , requestFrom: App");
            stringBuilder.append(" , requestMethod : ");
            stringBuilder.append(String.valueOf(getRequestMethod()));
//            stringBuilder.append(" ,requestFromH5 : ");
//            stringBuilder.append(String.valueOf(requestFromH5));
            stringBuilder.append(" ,");
            if (getParseClass() != null) {
                stringBuilder.append(" parseTo  ");
                stringBuilder.append(getParseClass().getSimpleName());
            }
            stringBuilder.append("  ： -->>   {\n\"headers\":\"");
            stringBuilder.append(String.valueOf(requestHeaders));
            stringBuilder.append("\",\n\"requestBody\":\"");
            stringBuilder.append(requestJsons != null ? mGson.toJson(requestJsons) : String.valueOf(requestJsons));
            stringBuilder.append("\",\n\"response\":");
            stringBuilder.append(String.valueOf(responseData));
            stringBuilder.append("}");

        } catch (Exception ex) {
            LogUtil.i(TAG, " build logMsg err", ex);
        }
        return stringBuilder.toString();
    }


    /**
     * 统一打印日志
     */
    public void printLog() {
        LogUtil.i(TAG, buildLogMsg());
    }

    private boolean isSuccessed() {
        return isSuccessed;
    }

    public Class<T> getParseClass() {
        return parseClass;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Object getRequestJsons() {
        return requestJsons;
    }

    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    public String getResponseData() {
        return responseData;
    }

    public ResultExceptionUtils.ResponseThrowable getResponseThrowable() {
        return responseThrowable;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }


}
