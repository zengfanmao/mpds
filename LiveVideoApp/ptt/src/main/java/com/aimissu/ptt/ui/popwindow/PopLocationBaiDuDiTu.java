package com.aimissu.ptt.ui.popwindow;//package com.aimissu.ptt.ui.popwindow;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxConfig;
import com.aimissu.basemvp.utils.FileUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.basemvp.view.RecycleViewDivider;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataGps;
import com.aimissu.ptt.entity.event.ManageUserEvent;
import com.aimissu.ptt.entity.event.PersonalCallEvent;
import com.aimissu.ptt.entity.im.ChatMsg;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.ui.GpsEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.ui.activity.LoginActivity;
import com.aimissu.ptt.ui.activity.MainActivity;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.PageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.grgbanking.video.VideoCore;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions2.RxPermissions;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 */

public class PopLocationBaiDuDiTu extends PopupWindow {
    private final static String TAG = PopLocationBaiDuDiTu.class.getSimpleName();
    private boolean _CanMiss = true;
    private View mMenuView;
    WeakReference<View> viewWeakReference;
    private DataGps mDataBillClassify;
    public ScreenAdapter screenAdapter;
    //    private String mIncomeCode;
    private TextView headView;
    private AlertDialog.Builder dialog;
    private String mBaiducfgPath;
    private String downloadName = "DVUserdat.cfg";
    private boolean isLoading = false;



    public void setCanMiss(boolean canMiss) {
        _CanMiss = canMiss;
    }

    private WeakReference<Context> mContext;
    private ViewHolder mViewHolder;
    private Context mcontext;

    public PopLocationBaiDuDiTu(final Context context, List<String> baiduItems, String incomeCode) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        initBaserView(context, baiduItems);
    }


    private void initBaserView(Context context, List<String> baiduItems) {
        mContext = new WeakReference<Context>(context);
        mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.downditu, null);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimList);
        mViewHolder = new ViewHolder(mMenuView);
        mMenuView.setOnTouchListener(onTouchListener);

        screenAdapter = new ScreenAdapter(R.layout.baiduditu_item, baiduItems);
        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext.get(), LinearLayoutManager.VERTICAL, false));
        mViewHolder.recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, 2, ContextCompat.getColor(context, R.color.c_d7f0eff5)));
        mViewHolder.recyclerView.setAdapter(screenAdapter);

        mBaiducfgPath = context.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp/" + downloadName;

        if (!Global.fileIsExists(mBaiducfgPath)) {
            downloadBaiducfg(mcontext);
        }
    }


    static class ViewHolder {
        @BindView(R.id.recycleView)
        RecyclerView recyclerView;
//        @BindView(R.id.li_container)
//        LinearLayout liContainer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private View.OnClickListener onActionClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };


    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        ActivityLuanch.viewMainActivity(mcontext);
    }

    public class ScreenAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {
        private List<String> newData;

        public ScreenAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
            this.newData = data;
            this.setOnItemChildClickListener(this);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

            LogUtil.i(TAG,"城市名称item: "+item);
            helper.setText(R.id.tv_chengshi, item);

            helper.addOnClickListener(R.id.tv_download);
            helper.addOnClickListener(R.id.tv_delete);

            if (Global.fileIsExists(getDownLoadNamePath(item))) {
                helper.getView(R.id.tv_download).setClickable(false);
                helper.getView(R.id.tv_delete).setClickable(true);

                //显示白色 by cuizh,0320
                helper.setTextColor(R.id.tv_chengshi,mcontext.getResources().getColor(R.color.white));

                helper.setTextColor(R.id.tv_download, mcontext.getResources().getColor(R.color.c_acacac));
                helper.setTextColor(R.id.tv_delete, mcontext.getResources().getColor(R.color.white));
                helper.getView(R.id.tv_progress).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_progress, "100%");
            }
//            else if(Global.fileIsLoading(getDownLoadName(item))){
//                helper.getView(R.id.tv_download).setClickable(false);
//                helper.getView(R.id.tv_delete).setClickable(true);
//                helper.setTextColor(R.id.tv_download, mcontext.getResources().getColor(R.color.white));
//                helper.setTextColor(R.id.tv_delete, mcontext.getResources().getColor(R.color.c_acacac));
//                helper.getView(R.id.tv_progress).setVisibility(View.VISIBLE);
//                helper.setText(R.id.tv_progress, "下载中");
//            }
            else {
                helper.getView(R.id.tv_download).setClickable(true);
                helper.getView(R.id.tv_delete).setClickable(false);

                //显示灰色 by cuizh,0320
                helper.setTextColor(R.id.tv_chengshi,mcontext.getResources().getColor(R.color.c_acacac));

                helper.setTextColor(R.id.tv_download, mcontext.getResources().getColor(R.color.white));
                helper.setTextColor(R.id.tv_delete, mcontext.getResources().getColor(R.color.c_acacac));
                helper.getView(R.id.tv_progress).setVisibility(View.INVISIBLE);
                helper.setText(R.id.tv_progress, "0%");
            }

        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            switch (view.getId()) {
                case R.id.tv_download:
                    download(getItem(position), adapter, position, view);
                    break;
                case R.id.tv_delete:

                    dialog = new AlertDialog.Builder(mcontext);
                    //获取AlertDialog对象
                    dialog.setTitle("刪除" + getItem(position) + "的地图?");//设置标题
                    dialog.setCancelable(true);//设置是否可取消
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override//设置ok的事件
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //在此处写入ok的逻辑
                            deleteBaidu(adapter, position, view);
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override//设置取消事件
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //在此写入取消的事件
                            dialogInterface.dismiss();
                        }
                    });

                    dialog.show();

                    break;
            }
        }

        /**
         * 删除百度地图
         *
         * @param adapter
         * @param position
         * @param view
         */
        private void deleteBaidu(BaseQuickAdapter adapter, int position, View view) {
            String filePath = getDownLoadNamePath(getItem(position));
            LogUtil.i(TAG, "filePath:" + filePath);
            File file = new File(filePath);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
//                    adapter.getViewByPosition(position, R.id.tv_progress).setVisibility(View.INVISIBLE);
//                    TextView downLoad = (TextView) adapter.getViewByPosition(position, R.id.tv_download);
//                    TextView delete = (TextView) adapter.getViewByPosition(position, R.id.tv_delete);
//                    downLoad.setTextColor(mcontext.getResources().getColor(R.color.white));
//                    delete.setTextColor(mcontext.getResources().getColor(R.color.c_acacac));
                    View viewParent = (View) view.getParent();
                    if (viewParent != null && viewParent.getParent() != null) {
                        viewParent = ((View) viewParent.getParent());
                        TextView progress = (TextView) viewParent.findViewById(R.id.tv_progress);
                        TextView delete = (TextView) viewParent.findViewById(R.id.tv_download);
                        TextView downLoad = (TextView) viewParent.findViewById(R.id.tv_delete);

                        progress.setVisibility(View.INVISIBLE);
                        progress.setText("0%");
                        downLoad.setTextColor(mcontext.getResources().getColor(R.color.white));
                        delete.setTextColor(mcontext.getResources().getColor(R.color.c_acacac));
                        ToastUtils.showToast("删除成功");
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToast("删除失败");
                }
            } else {
                ToastUtils.showToast("文件不存在");
            }
        }

        /**
         * 下载
         *
         * @param item
         * @param adapter
         * @param position
         * @param view
         */
        private void download(String item, BaseQuickAdapter adapter, int position, View view) {
            if (!Global.fileIsExists(mBaiducfgPath)) {
                downloadBaiducfg(mcontext);
            }

            String downloadName = getDownLoadName(item);
            String downloadUrl = RxConfig.getMethodApiUrl("baiduditu/vmp/" + downloadName + ".dat");

            TextView progress = null;
            TextView delete = null;
            TextView downLoad = null;
            View viewParent = (View) view.getParent();
            if (viewParent != null && viewParent.getParent() != null) {
                viewParent = ((View) viewParent.getParent());
                progress = (TextView) viewParent.findViewById(R.id.tv_progress);

                //这里delete 和 download写反了 by cuizh,0320
//                delete = (TextView) viewParent.findViewById(R.id.tv_download);
//                downLoad = (TextView) viewParent.findViewById(R.id.tv_delete);

                downLoad = (TextView) viewParent.findViewById(R.id.tv_download);
                delete = (TextView) viewParent.findViewById(R.id.tv_delete);
            }

            String localPath = mcontext.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp/" + downloadName + ".dat";
            TextView finalProgress = progress;
            TextView finalDownLoad = downLoad;
            TextView finalDelete = delete;
            FileDownloader.getImpl().create(downloadUrl)
                    .setPath(localPath)
                    .setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            isLoading = true;
                            LogUtil.i(TAG, "下载百度地图中····soFarBytes：" + soFarBytes + "     totalBytes:" + totalBytes);
                            if (finalProgress != null) {
                                finalProgress.setVisibility(View.VISIBLE);
                                int percent = ((Float) ((float) soFarBytes * 100 / (float) totalBytes)).intValue();
                                finalProgress.setText(percent + "%");
//                                adapter.setNewData(newData);
//                                adapter.notifyDataSetChanged();

                                finalDownLoad.setEnabled(false);
                                finalDownLoad.setText("下载中...");
                            }

                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {
                        }

                        @Override
                        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            if (finalProgress != null) {
                                finalDownLoad.setTextColor(mcontext.getResources().getColor(R.color.c_acacac));
                                finalDelete.setTextColor(mcontext.getResources().getColor(R.color.white));
                                adapter.notifyDataSetChanged();
                                finalProgress.setText(100 + "%");
                                fileScan(localPath);
                                showReLoginAPP();
                                isLoading = false;

                                finalDownLoad.setEnabled(true);
                                finalDownLoad.setText("下载");
                            }

                        }


                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            LogUtil.i(TAG, "下载百度地图出错：" + e.getMessage());
                            isLoading = false;
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                        }
                    }).start();
        }

        /**
         * 重启app页面
         */
        private void showReLoginAPP() {

            dialog = new AlertDialog.Builder(mcontext);
            //获取AlertDialog对象
            dialog.setTitle("重启APP才能加载地图数据，是否现在重启?");//设置标题

            dialog.setCancelable(false);//设置是否可取消
            dialog.setPositiveButton("重启", new DialogInterface.OnClickListener() {
                @Override//设置ok的事件
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    final Intent intent = mcontext.getPackageManager().getLaunchIntentForPackage(mcontext.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mcontext.startActivity(intent);
                    //关闭程序  by cuizh,0523
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            dialog.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
                @Override//设置取消事件
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                }
            });
            dialog.show();
//        mAlertDialog = dialog.show();
        }

    }




    private String getDownLoadName(String item) {
        if (item.equals("广州市")) {
            return "guangzhou_257";
        } else if (item.equals("东莞市")) {
            return "dongguan_119";
        } else if (item.equals("韶关市")) {
            return "shaoguan_137";
        } else if (item.equals("东莞市")) {
            return "dongguan_119";
        } else if (item.equals("全国基础")) {
            return "quanguogailue";
        } else if (item.equals("潮州市")) {
            return "chaozhou_201";
        } else if (item.equals("佛山市")) {
            return "foshan_138";
        } else if (item.equals("河源市")) {
            return "heyuan_200";
        } else if (item.equals("惠州市")) {
            return "huizhou_301";
        } else if (item.equals("江门市")) {
            return "jiangmen_302";
        } else if (item.equals("揭阳市")) {
            return "jieyang_259";
        } else if (item.equals("茂名市")) {
            return "maoming_139";
        } else if (item.equals("梅州市")) {
            return "meizhou_141";
        } else if (item.equals("清远市")) {
            return "qingyuan_197";
        } else if (item.equals("汕头市")) {
            return "shantou_303";
        } else if (item.equals("汕尾市")) {
            return "shanwei_339";
        } else if (item.equals("深圳市")) {
            return "shenzhen_340";
        } else if (item.equals("阳江市")) {
            return "yangjiang_199";
        } else if (item.equals("云浮市")) {
            return "yunfu_258";
        } else if (item.equals("湛江市")) {
            return "zhanjiang_198";
        } else if (item.equals("肇庆市")) {
            return "zhaoqing_338";
        } else if (item.equals("中山市")) {
            return "zhongshan_187";
        } else if (item.equals("珠海市")) {
            return "zhuhai_140";
        }

        return "";
    }

    private String getDownLoadNamePath(String name) {
        return mcontext.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp/" + getDownLoadName(name) + ".dat";

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View backbroundView = mMenuView.findViewById(R.id.li_container);
            if (backbroundView != null) {
                int height = backbroundView.getTop();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        if (_CanMiss) {
                            dismiss();
                        }
                    }
                    if (y > backbroundView.getBottom()) {
                        if (_CanMiss) {
                            dismiss();
                        }
                    }

                    if (x < backbroundView.getLeft()) {
                        if (_CanMiss) {
                            dismiss();
                        }
                    }
                    if (x > backbroundView.getRight()) {
                        if (_CanMiss) {
                            dismiss();
                        }
                    }
                }
            }

            return true;
        }
    };

    /**
     * 下载离线地图的配置文件
     */
    public void downloadBaiducfg(Context context) {
        String downloadUrl = RxConfig.getMethodApiUrl("baiduditu/vmp/" + downloadName);
        mBaiducfgPath = context.getExternalFilesDir("") + "/BaiduMapSDKNew/vmp/" + downloadName;

        File file = new File(mBaiducfgPath);
        if (file.exists() && file.isFile()) {
            file.delete();
            com.grgbanking.video.utils.LogUtil.i(TAG, "删除原文件····");
        }
        com.grgbanking.video.utils.LogUtil.i(TAG, "····mBaiducfgPath： " + mBaiducfgPath + "     downloadUrl:" + downloadUrl);
        FileDownloader.getImpl().create(downloadUrl)
                .setPath(mBaiducfgPath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        com.grgbanking.video.utils.LogUtil.i(TAG, "开始下载配置文件soFarBytes:" + soFarBytes + " totalBytes:" + totalBytes);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        com.grgbanking.video.utils.LogUtil.i(TAG, "开始下载配置完成····:");
                        fileScan(mBaiducfgPath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        com.grgbanking.video.utils.LogUtil.i(TAG, "下载错误····:" + e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    public void fileScan(String filePath) {
//        Uri data = Uri.parse("file://" + file);
//        mcontext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));

        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        mcontext.sendBroadcast(scanIntent);
    }

}
