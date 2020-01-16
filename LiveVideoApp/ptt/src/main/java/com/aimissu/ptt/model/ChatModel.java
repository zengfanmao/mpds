package com.aimissu.ptt.model;

import android.net.Uri;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RetrofitClient;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.GetPathFromUri4kitkat;
import com.aimissu.basemvp.utils.RxMapBuild;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataChatMsg;
import com.aimissu.ptt.entity.data.DataPostFile;
import com.aimissu.ptt.entity.data.DataPostHeadImage;
import com.aimissu.ptt.entity.data.DataPushMsg;
import com.aimissu.ptt.entity.im.IMPamras;
import com.aimissu.ptt.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class ChatModel implements IChatModel {

    @Override
    public void getMyChatDetails(String skip, String take, String firstMsgId, String msgFromType, String msgToCode, String msgFromUserCode, RxCallBack<DataChatMsg> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataChatMsg.class,
                RxConfig.getMethodApiUrl("api/do/getMyChatDetails"),
                RxMapBuild.created()
                        .put("Skip", skip)
                        .put("Take", take)
                        .put("FirstMsgId", firstMsgId)
                        .put("MsgFromType", msgFromType)
                        .put("MsgToCode", msgToCode)
                        .put("MsgFromUserCode", msgFromUserCode)
                        .put("ApiToken", AppManager.getApiToken())
                        .put("UserCode", AppManager.getLoginName())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void pushMsg(IMPamras imPamras, RxCallBack<DataPushMsg> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPushMsg.class,
                RxConfig.getMethodApiUrl("api/do/pushMsg"),
                RxMapBuild.created()
                        .put("UserCode", imPamras.userCode)
                        .put("MsgType", String.valueOf(imPamras.msgType))
                        .put("MsgContent", imPamras.msgContent)
                        .put("MsgFile", imPamras.msgFile)
                        .put("UserLatitude", imPamras.latitude)
                        .put("UserLongitude", imPamras.longitude)
                        .put("UserPositionName", imPamras.positionName)
                        .put("MsgFromType", imPamras.msgFromType)
                        .put("MsgToCode", imPamras.msgToCode)
                        .put("ApiToken", AppManager.getApiToken())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void uploadFile(IMPamras imPamras, RxCallBack<DataPostFile> rxCallBack) {
        Uri uri = Uri.fromFile(new File(imPamras.filePath));
        List<File> files = new ArrayList<>();
        File file = new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri));
        files.add(file);
        LogUtil.i("hexiang", "uploadFile file:" + imPamras.filePath);
        LogUtil.i("hexiang", "file===null" + String.valueOf(file == null) + ",uploadFile after file:" + GetPathFromUri4kitkat.getPath(RxConfig.context, uri));

        RetrofitClient.getInstance().upload(DataPostFile.class,
                RxConfig.getMethodApiUrl("api/do/PostFile" + "?virtualId=" + imPamras.virtualId),
                RxUtils.filesToMultipartBody(files)
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

    @Override
    public void updateFilePropertiy(IMPamras imPamras, RxCallBack<DataPostFile> rxCallBack) {
        RetrofitClient.getInstance().postAsync(DataPostFile.class,
                RxConfig.getMethodApiUrl("api/do/updateVideoFile"),
                RxMapBuild.created()
                        .put("MsgFile", CommonUtils.emptyIfNull(imPamras.msgFile))
                        .put("PicCode", CommonUtils.emptyIfNull(imPamras.picCode))
                        .put("Duration", CommonUtils.emptyIfNull(imPamras.duration))
//                        .put("StartTime", CommonUtils.emptyIfNull(startTime))
//                        .put("EndTime", CommonUtils.emptyIfNull(endTime))
                        .put("UserCode", AppManager.getLoginName())
                        .put("ApiToken", AppManager.getApiToken())
                        .build()
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }


    public void uploadHeadImage(IMPamras imPamras, RxCallBack<DataPostHeadImage> rxCallBack) {
        String uCode = AppManager.getUserCode();
        String baseApiUrl = RxConfig.getBaseApiUrl().substring(0,RxConfig.getBaseApiUrl().lastIndexOf("/"));
        Uri uri = Uri.fromFile(new File(imPamras.filePath));
        List<File> files = new ArrayList<>();
        File file = new File(GetPathFromUri4kitkat.getPath(RxConfig.context, uri));
        files.add(file);
        LogUtil.i("hexiang", "uploadFile file:" + imPamras.filePath);
        LogUtil.i("hexiang", "file===null" + String.valueOf(file == null) + ",uploadFile after file:" + GetPathFromUri4kitkat.getPath(RxConfig.context, uri));
        RetrofitClient.getInstance().upload(DataPostHeadImage.class,
                baseApiUrl+":808/tools/webservice.ashx?action=portrait&"+"uCode="+uCode,
                RxUtils.headFilesToMultipartBody(files)
        ).subscribe(RxUtils.getDefaultSubscriber(rxCallBack));
    }

}
