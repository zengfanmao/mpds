package com.aimissu.basemvp.net.rx;


/**

 * 获取数据之后的回回及错误回调
 */
public abstract class RxCallBack<T> {
    /**
     * t一定不会空，如果过解析实体错误会抛出异常走onFailed
     *
     * @param t
     */
    public abstract void onSucessed(T t);

    /**
     * 错误回调
     *
     * @param e
     */
    public abstract void onFailed(ResultExceptionUtils.ResponseThrowable e);

    /**
     * 成功或者失败之后，想完成之后进行一些额外的处理，需要一些请求过程的数据在builder，如请求参数，headers，httpcode等
     * 默认取消了处理，
     * @param model
     * @param builder
     * @param isSuccessed
     */
    public  void handResult(T model, RxBuilder.Builder builder, boolean isSuccessed)
    {}

}
