package com.aimissu.ptt.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aimissu.ptt.R;
import com.aimissu.ptt.entity.OtherTabEntity;
import com.aimissu.ptt.entity.PageConfig;
import com.aimissu.ptt.utils.PageUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 */
public class OtherAdapter extends BaseQuickAdapter<OtherTabEntity, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    public OtherAdapter(int layoutResId, @Nullable List<OtherTabEntity> data) {
        super(layoutResId, data);
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, OtherTabEntity item) {
        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_type_icon));
        helper.setText(R.id.tv_title, item.getTitle());
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 0:
                PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_USER_INFO);
                break;
            case 1:
                PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_ABOUT);
                break;
            case 2:
                PageUtils.turnPage(PageConfig.PAGE_FIVE, PageConfig.PAGE_ID_SETTINGS);
                break;
        }
    }
}
