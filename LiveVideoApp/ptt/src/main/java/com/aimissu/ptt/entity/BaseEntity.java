package com.aimissu.ptt.entity;

import com.aimissu.basemvp.base.BaseJsonData;

/**
 */
public class BaseEntity  extends BaseJsonData{


    /**
     * Exception :
     * IsSuccess : false
     * Message : 账号不存在
     * ResultCode : -1
     * Entity : null
     */

    private String Exception;
    private boolean IsSuccess;
    private String Message;
    private String ResultCode;

    public String getException() {
        return Exception;
    }

    public void setException(String Exception) {
        this.Exception = Exception;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "Exception='" + Exception + '\'' +
                ", IsSuccess=" + IsSuccess +
                ", Message='" + Message + '\'' +
                ", ResultCode='" + ResultCode + '\'' +
                '}';
    }
}
