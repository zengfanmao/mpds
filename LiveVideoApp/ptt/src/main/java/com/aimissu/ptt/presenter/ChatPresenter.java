package com.aimissu.ptt.presenter;

import com.aimissu.basemvp.mvp.BasePresenter;
import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.model.ChatModel;
import com.aimissu.ptt.model.IChatModel;
import com.aimissu.ptt.view.IChatView;
import com.aimissu.ptt.view.widget.audio.FileUtils;

/**
 */
public class ChatPresenter extends BasePresenter<IChatView> implements IChatPresenter {
    private String tag = "ChatPresenter";

    IChatModel model;

    public ChatPresenter(IChatView baseView) {
        super(baseView);
        model = new ChatModel();
    }

    @Override
    public void getMyChatDetails(final boolean isLoadMore, String skip, String take, String firstMsgId, String msgFromType, String msgToCode, String msgFromUserCode) {
        model.getMyChatDetails(skip, take, firstMsgId, msgFromType, msgToCode, msgFromUserCode, new RxCallBack<DataChatMsg>() {
            @Override
            public void onSucessed(DataChatMsg dataChatMsg) {
                if (getView() != null) {
                    if (dataChatMsg.isIsSuccess()) {
                        getView().getMyChatDetailsSuccessed(isLoadMore, dataChatMsg);
                    } else {
                        getView().getMyChatDetailsFailed(isLoadMore, dataChatMsg.getMessage());
                    }

                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().getMyChatDetailsFailed(isLoadMore, e.message);
                }
            }
        });
    }


    @Override
    public void pushMsg(IMPamras imPamras) {
        LogUtil.i(tag, "IMPamras:" + imPamras.toString());
        model.pushMsg(imPamras, new RxCallBack<DataPushMsg>() {
            @Override
            public void onSucessed(DataPushMsg dataPushMsg) {
                if (getView() != null) {
                    if (dataPushMsg.isIsSuccess()) {
                        getView().pushMsgSuccessed(dataPushMsg, imPamras);
                        try {
                            AppManager.setMsgCountIncrease(imPamras.msgToCode, imPamras.msgFromType, false);
                            AppManager.setHadRedMsgCount(imPamras.msgToCode, imPamras.msgFromType, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        getView().pushMsgFailed(dataPushMsg.getMessage(), imPamras);
                    }

                    LogUtil.i(tag, "" + dataPushMsg.toString());
                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().pushMsgFailed(e.message, imPamras);
                }
            }
        });
    }

    @Override
    public void uploadFile(IMPamras imPamras) {
        model.uploadFile(imPamras, new RxCallBack<DataPostFile>() {
            @Override
            public void onSucessed(DataPostFile dataPostFile) {
                if (getView() != null) {
                    if (dataPostFile.isIsSuccess()) {
                        if (dataPostFile.getEntity() != null) {
                            FileUtils.addFile(new LocalMsgFile.Builder()
                                    .localFileUrl(imPamras.filePath)
                                    .fileDownloadUrll(RxConfig.getBaseApiUrl() + dataPostFile.getEntity().fRelativePath)
                                    .fCode(dataPostFile.getEntity().fCode)
                                    .build());
                            LogUtil.i(tag, "thread name " + Thread.currentThread().getName() + " ,uploadFile onSucessed addFile filecode:" + dataPostFile.getEntity().fCode + " ,localPath:" + imPamras.filePath + ",downfile:" + RxConfig.getBaseApiUrl() + dataPostFile.getEntity().fRelativePath);
                        }

                        getView().uploadFileSuccessed(dataPostFile, imPamras);
                    } else {
                        getView().uploadFileFailed(dataPostFile.getMessage(), imPamras);
                    }

                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().uploadFileFailed(e.message, imPamras);
                }
            }
        });
    }

    @Override
    public void updateFilePropertiy(final IMPamras imPamras) {
        model.updateFilePropertiy(imPamras, new RxCallBack<DataPostFile>() {
            @Override
            public void onSucessed(DataPostFile dataPostFile) {
                if (getView() != null) {
                    if (dataPostFile.isIsSuccess()) {
                        getView().updateFilePropertiySuccessed(dataPostFile, imPamras);
                    } else {
                        getView().updateFilePropertiyFailed(dataPostFile.getMessage(), imPamras);
                    }

                }
            }

            @Override
            public void onFailed(ResultExceptionUtils.ResponseThrowable e) {
                if (getView() != null) {
                    getView().updateFilePropertiyFailed(e.message, imPamras);
                }
            }
        });
    }


}
