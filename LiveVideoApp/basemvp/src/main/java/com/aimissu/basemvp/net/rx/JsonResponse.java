package com.aimissu.basemvp.net.rx;

/**

 * 对应html javascript 封装json对象
 */

public class JsonResponse implements IParseResponse {

    public String status;
    public String data;

    public JsonResponse(String status, String data) {
        this.status = status;
        this.data = data;
    }
}
