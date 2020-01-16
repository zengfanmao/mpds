package com.aimissu.basemvp.net.rx;

import okhttp3.ResponseBody;

/**

 * 对应html javascript 封装json对象
 */

public class BodyResponse implements IParseResponse {


    public ResponseBody body;

    public BodyResponse(ResponseBody body) {
        this.body = body;
    }
}
