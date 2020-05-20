package com.zch.tooldemos.okhttp_demo;

/**
 * Created by Zch on 2019/3/25 17:04.
 */

public interface MyCallBackListener {

    /**
     * 请求成功
     *
     * @param code
     * @param response
     */
    public void onComplete(int code, String response);

    /**
     * 请求发生IO异常
     *
     * @param code
     * @param e
     */
//    public void onIOException(int code, IOException e);

    /**
     * 请求发生错误
     *
     * @param code
     * @param response
     */
    public void onError(int code, String response);

    /**
     * 服务器发生错误
     *
     * @param code
     * @param error
     */

//    public void onVolleryError(int code, Error error);
}
