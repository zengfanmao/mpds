package com.aimissu.ptt.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aimissu.basemvp.mvp.BaseMvpFragment;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.entity.data.DataPdtGroup;
import com.aimissu.ptt.presenter.ISetPwdPresenter;
import com.aimissu.ptt.presenter.SetPwdPresenter;
import com.aimissu.ptt.utils.PageUtils;
import com.aimissu.ptt.view.IResetPwdView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置密码
 */
public class SetPasswordFragment extends BaseMvpFragment<ISetPwdPresenter> implements IResetPwdView {

    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.et_oldPwd)
    EditText etOldPwd;
    @BindView(R.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirmPwd)
    EditText etconfirmPwd;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set_password;
    }

    @Override
    protected ISetPwdPresenter createPresenter() {
        return new SetPwdPresenter(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTile.setText("密码设置");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBack() {

        PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_SETTINGS);
    }

    @OnClick({R.id.li_back, R.id.btn_save})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.li_back:
                onBack();
                break;
            case R.id.btn_save:
                resetPwd();
                break;
        }

    }

    /**
     * 修改密码
     */
    private void resetPwd() {
        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String confirmNewPwd = etconfirmPwd.getText().toString().trim();

        if (!newPwd.equals(confirmNewPwd)) {
            ToastUtils.showLocalToast("两次密码不一致");
            return;
        }
        if (oldPwd.equals(newPwd)) {
            ToastUtils.showLocalToast("新密码和旧密码不能一样");
            return;
        }

        mPresenter.resetPwd(oldPwd, newPwd);
    }

    @Override
    public void getResetPwdSuccessed(DataPdtGroup mode) {
        ToastUtils.showLocalToast("密码修改成功");
        etOldPwd.setText("");
        etNewPwd.setText("");
        etconfirmPwd.setText("");
        AppManager.setNeedSedPwd(false);
    }

    @Override
    public void getUserGroupFailed(String msg) {
        ToastUtils.showLocalToast(msg);
    }
}
