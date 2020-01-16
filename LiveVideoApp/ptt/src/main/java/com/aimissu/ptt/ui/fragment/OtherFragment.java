package com.aimissu.ptt.ui.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.net.rx.ResultExceptionUtils;
import com.aimissu.basemvp.net.rx.RxCallBack;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.basemvp.view.GridSpacingItemDecoration;
import com.aimissu.ptt.R;
import com.aimissu.ptt.adapter.OtherAdapter;
import com.aimissu.ptt.entity.OtherTabEntity;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.presenter.IGroupPresenter;
import com.aimissu.ptt.service.LocationManger;
import com.aimissu.ptt.ui.activity.ActivityLuanch;
import com.aimissu.ptt.utils.AnimationUtil;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.LogHelper;
import com.aimissu.ptt.utils.webRequestUtil;
import com.aimissu.ptt.view.FloatingDraftButton;
import com.aimissu.ptt.view.IOtherView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 其他
 */
public class OtherFragment extends BaseMvpFragment<IGroupPresenter> implements IOtherView {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_quit)
    Button mQuit;

    @BindView(R.id.floatingDraftButton)
    FloatingDraftButton floatingDraftButton;
    @BindView(R.id.floatingActionButton_liveness)
    FloatingDraftButton floatingActionButton_liveness;
    @BindView(R.id.floatingDraftButton2)
    FloatingDraftButton floatingDraftButton2;
    @BindView(R.id.floatingDraftButton3)
    FloatingDraftButton floatingDraftButton3;
    @BindView(R.id.floatingDraftButton4)
    FloatingDraftButton floatingDraftButton4;
    @BindView(R.id.floatingDraftButton5)
    FloatingDraftButton floatingDraftButton5;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other;
    }

    @Override
    protected IGroupPresenter createPresenter() {
        return null;
    }

    @Override
    protected void lazyInitData() {

        try {

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setAdapter(new OtherAdapter(R.layout.other_item, OtherTabEntity.buidData()));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(getActivity(), 2), false));

            testFloatingDraftButton();

        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }
    }

    @Override
    public void onBack() {

    }

    @OnClick({R.id.btn_quit,})
    void OnClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_quit:
                    webRequestUtil.logOut(new RxCallBack<DataPdtGroup>() {
                        @Override
                        public void onSucessed(DataPdtGroup dataPdtGroup) {
                            if (dataPdtGroup.isIsSuccess()) {
                            }
                        }

                        @Override
                        public void onFailed(ResultExceptionUtils.ResponseThrowable e) {

                        }
                    });

//                ActivityLuanch.stopMqttService(getActivity());
//                System.exit(0);

                    ActivityLuanch.stopMqttService(getActivity());
                    ((Activity) getContext()).finish();
                    ((NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE)).cancel(1);

                    LocationManger.getInstance().stopLocation();

                    //延时exit，防止logOut指令发不出去  by cuizh,0408
                    Global.getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            System.exit(0);
                        }
                    }, 100);

                    break;

            }
        } catch (Exception e) {
            LogHelper.sendErrorLog(e);
        }

    }


    private void testFloatingDraftButton(){

        floatingDraftButton.registerButton(floatingActionButton_liveness);
        floatingDraftButton.registerButton(floatingDraftButton2);
        floatingDraftButton.registerButton(floatingDraftButton3);
        floatingDraftButton.registerButton(floatingDraftButton4);
        floatingDraftButton.registerButton(floatingDraftButton5);

        floatingDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.slideButtons(getActivity(),floatingDraftButton);
            }
        });

    }
}
