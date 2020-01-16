package com.aimissu.basemvp.net.rx;

/**

 * 为了带回rxbuilder，一些请求过程中的信息，如请求码之类的
 */

public class EntityResponse<T> implements IParseResponse<T> {

    RxBuilder.Builder<T> builder;
    public T entity;

    public RxBuilder.Builder<T> getBuilder() {
        return builder;
    }

    public void setBuilder(RxBuilder.Builder<T> builder) {
        this.builder = builder;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getHttpCode() {
        if (builder != null)
            return String.valueOf(builder.getCode());
        return "";
    }

    public EntityResponse(T entity, RxBuilder.Builder<T> builder) {
        this.builder = builder;
        this.entity = entity;
    }
}
