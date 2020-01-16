package com.aimissu.basemvp.net.rx;

/**

 * 下载回调
 */
public abstract class DownloadCallBack<T> {

    public DownloadCallBack(T data) {
        Data = data;
    }

    public DownloadCallBack() {
    }

    T Data;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    /**
     * 开始
     */
    public void onStart() {
    }

    /**
     * pop_pwd_dismiss
     *
     * @param e
     */
    abstract public void onError(Throwable e);

    /**
     * 完成
     */
    public void onCompleted() {
    }

    /**
     * 进度条
     *
     * @param fileSizeDownloaded
     */
    public void onProgress(long fileSizeDownloaded) {
    }

    /**
     * 下载成功
     *
     * @param path
     * @param name
     * @param fileSize
     */
    abstract public void onSucess(String path, String name, long fileSize);
}
