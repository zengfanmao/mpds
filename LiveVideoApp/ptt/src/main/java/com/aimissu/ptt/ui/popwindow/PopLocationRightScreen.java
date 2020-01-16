package com.aimissu.ptt.ui.popwindow;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.utils.ScreenUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.R;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.data.DataDepartment;
import com.aimissu.ptt.entity.event.PopScreenConditionEvent;
import com.aimissu.ptt.entity.ui.ChildEntity;
import com.aimissu.ptt.entity.ui.DepartmentEntity;
import com.aimissu.ptt.entity.ui.PopScreenCondition;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.utils.Global;
import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */

public class PopLocationRightScreen extends PopupWindow {
    private final static String TAG = PopLocationRightScreen.class.getSimpleName();
    private boolean _CanMiss = true;
    private View mMenuView;
    WeakReference<View> viewWeakReference;
    private DataDepartment mDataBillClassify;

    private final String testJSon = "{\n" +
            "    \"Exception\": \"\",\n" +
            "    \"IsSuccess\": true,\n" +
            "    \"Message\": \"获取部门信息成功\",\n" +
            "    \"ResultCode\": \"0\",\n" +
            "    \"Entity\": {\n" +
            "        \"dept\": {\n" +
            "            \"isDel\": false,\n" +
            "            \"recSN\": null,\n" +
            "            \"dCode\": \"10001\",\n" +
            "            \"dName\": \"总公司\",\n" +
            "            \"dFather\": \"0\",\n" +
            "            \"dDesc\": \"0\",\n" +
            "            \"ID\": 1,\n" +
            "            \"sort_id\": 1,\n" +
            "            \"createtime\": \"2018-09-02T07:02:11\",\n" +
            "            \"updatetime\": \"2018-09-02T07:02:15\",\n" +
            "            \"farther\": \"0\"\n" +
            "        },\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"dept\": {\n" +
            "                    \"isDel\": false,\n" +
            "                    \"recSN\": \"\",\n" +
            "                    \"dCode\": \"10002\",\n" +
            "                    \"dName\": \"子公司\",\n" +
            "                    \"dFather\": \"10001\",\n" +
            "                    \"dDesc\": \"0\",\n" +
            "                    \"ID\": 2,\n" +
            "                    \"sort_id\": 1,\n" +
            "                    \"createtime\": \"2018-09-02T07:02:11\",\n" +
            "                    \"updatetime\": \"2018-09-02T07:02:15\",\n" +
            "                    \"farther\": \"10001\"\n" +
            "                },\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"dept\": {\n" +
            "                            \"isDel\": false,\n" +
            "                            \"recSN\": \"\",\n" +
            "                            \"dCode\": \"10003\",\n" +
            "                            \"dName\": \"孙公司\",\n" +
            "                            \"dFather\": \"10002\",\n" +
            "                            \"dDesc\": \"0\",\n" +
            "                            \"ID\": 3,\n" +
            "                            \"sort_id\": 1,\n" +
            "                            \"createtime\": \"2018-09-02T07:02:11\",\n" +
            "                            \"updatetime\": \"2018-09-02T07:02:15\",\n" +
            "                            \"farther\": \"10002\"\n" +
            "                        },\n" +
            "                        \"children\": []\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}\n";

    public void setCanMiss(boolean canMiss) {
        _CanMiss = canMiss;
    }

    private WeakReference<Context> mContext;
    private ViewHolder mViewHolder;
    private PopScreenCondition popScreenCondition;

    public PopLocationRightScreen(final Context context, DataDepartment dataBillClassify, PopScreenCondition popScreenCondition) {
        super(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        initBaserView(context, dataBillClassify, popScreenCondition);
    }


    private void initBaserView(Context context, DataDepartment dataBillClassify, PopScreenCondition popScreenCondition) {
        this.popScreenCondition = popScreenCondition;
        mContext = new WeakReference<Context>(context);
        mDataBillClassify = dataBillClassify;
//        try {
//            mDataBillClassify = new Gson().fromJson(testJSon, DataDepartment.class);
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.location_pop_user_right_1, null);
        this.setContentView(mMenuView);

        this.setWidth(ScreenUtils.getScreenWidth(context) * 2 / 3);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimList);
        mViewHolder = new ViewHolder(mMenuView);
        mMenuView.setOnTouchListener(onTouchListener);


        List<String> dataSets = new LinkedList<>();
        dataSets.add("未选择");
        int defaultSelectedIndex = 0;
        if (LocalCache.getInstance().getUserGroups() != null) {
            List<UserGroup> userGroups = LocalCache.getInstance().getUserGroups();
            for (int i = 0; i < userGroups.size(); i++) {
                UserGroup userGroup = userGroups.get(i);

                //条件筛选中只显示常用组 by cuizh,0402
                if (userGroup != null && userGroup.getIsdefault()!=null && userGroup.isdefault.equals("1")) {
                    if (this.popScreenCondition != null && this.popScreenCondition.getDiscussionCode().equals(userGroup.getGroupid())) {
                        defaultSelectedIndex = i + 1;
                    }
                    dataSets.add(userGroup.getGroupName());
                }
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataSets.toArray(new String[dataSets.size()]));
        mViewHolder.spinner.setAdapter(adapter);
        mViewHolder.spinner.setSelection(defaultSelectedIndex);
        buildTreeView(mDataBillClassify);
        mViewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    popScreenCondition.setDiscussionCode("");
                    popScreenCondition.setDiscussionName("");
                } else {
                    if (position - 1 < LocalCache.getInstance().getUserGroups().size()) {
                        UserGroup userGroup = LocalCache.getInstance().getUserGroups().get(position - 1);
                        if (userGroup != null) {
                            popScreenCondition.setDiscussionCode(userGroup.getGroupid());
                            popScreenCondition.setDiscussionName(userGroup.getGroupName());
                        } else {
                            popScreenCondition.setDiscussionCode("");
                            popScreenCondition.setDiscussionName("");
                        }
                    } else {
                        popScreenCondition.setDiscussionCode("");
                        popScreenCondition.setDiscussionName("");
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public TreeNode root;

    private void buildTreeView(DataDepartment dataDepartment) {
        if (dataDepartment == null || dataDepartment.getEntity() == null)
            return;
        mViewHolder.niceDepartmentCode.removeAllViews();

        root = TreeNode.root();
        getTreeNode(root, dataDepartment.getEntity());
        AndroidTreeView tView = new AndroidTreeView(mContext.get(), root);
//        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
//        tView.setSelectionModeEnabled(true);
//        tView.setDefaultAnimation(true);
        mViewHolder.niceDepartmentCode.addView(tView.getView());
        tView.setUseAutoToggle(true);
        //展开tree一级 by cuizh,0319
        tView.expandLevel(1);
//        tView.expandAll();

    }

    private TreeNode getTreeNode(@NonNull TreeNode parentNode, ChildEntity childEntity) {
        if (childEntity != null && childEntity.getDept() != null) {
            TreeNode child = new TreeNode(childEntity).setViewHolder(new MyHolder(mContext.get()));
            if (childEntity.getChildren() != null) {
                for (ChildEntity subChildEntity : childEntity.getChildren()) {
                    if (subChildEntity != null) {
//                        child.addChild(getTreeNode(child, subChildEntity));
                        getTreeNode(child, subChildEntity);
                    }
                }
            }
            String searchLocalDeptName = mViewHolder.et_search_depart.getText().toString();
            //筛选部门
//            if (TextUtils.isEmpty(searchLocalDeptName)// 没有筛选直接所有
//                    ||
//                    ((childEntity.getChildren() == null || childEntity.getChildren().size() <= 0) && childEntity.getDept().getDName().contains(searchLocalDeptName))//有筛选,并且是最小的没有子项才筛选
//                    ||
//                    (childEntity.getChildren() != null && childEntity.getChildren().size() > 0)
//                    ) {
//                parentNode.addChild(child);
//            }

            //修正部门筛选问题 by cuizh,0316
            if (TextUtils.isEmpty(searchLocalDeptName)// 没有筛选直接所有
                    ||
                    (child.isLeaf() && childEntity.getDept().getDName().contains(searchLocalDeptName))//有筛选,并且是最小的没有子项才筛选
                    ||
                    (child.getChildren() != null && child.getChildren().size() > 0)
                    ) {
                LogUtil.i(TAG,"过滤部门···");
                parentNode.addChild(child);
            }

        }
        return parentNode;
    }

    private void setNodeAllChecked(TreeNode rootNode, boolean isChecked) {
        if (rootNode != null) {
            if (rootNode.getViewHolder() != null && rootNode.getViewHolder() instanceof MyHolder) {
                MyHolder myHolder = (MyHolder) rootNode.getViewHolder();
                if (myHolder != null&&myHolder.getNodeSelector()!=null)
                    myHolder.getNodeSelector().setChecked(isChecked);
            }
            if (rootNode.getChildren() != null) {
                for (TreeNode sbNode : rootNode.getChildren()) {
                    if (sbNode != null) {
                        setNodeAllChecked(sbNode, isChecked);
                    }
                }
            }
        }
    }

    public  class MyHolder extends TreeNode.BaseNodeViewHolder<ChildEntity> {
        public CheckBox getNodeSelector() {
            return nodeSelector;
        }

        public MyHolder(Context context) {
            super(context);
        }

        private CheckBox nodeSelector;
        public TextView tvTitle;
        public PrintView arrowView;


        @Override
        public void toggle(boolean active) {
            arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        }
        @Override
        public View createNodeView(TreeNode node, ChildEntity value) {
            final LayoutInflater inflater = LayoutInflater.from(context);

            final View view = inflater.inflate(R.layout.layout_profile_node, null, false);
            DepartmentEntity departmentEntity = value.getDept();
            nodeSelector = view.findViewById(R.id.checktextview);
            tvTitle = view.findViewById(R.id.tv_title);
            arrowView = view.findViewById(R.id.arrow_icon);
            if (departmentEntity != null) {
                tvTitle.setText(departmentEntity.getDName());
                LogUtil.i(TAG, "部门名称departmentEntity.getDName():" + departmentEntity.getDName());
                if (popScreenCondition != null && !TextUtils.isEmpty(departmentEntity.getDCode())) {
                    if (departmentEntity.getDCode().equals(popScreenCondition.dCode)) {
                        nodeSelector.setChecked(true);
                    } else {
                        nodeSelector.setChecked(false);
                    }
                }
            }

//            nodeSelector.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean isChecked = !nodeSelector.isChecked();
//                    setNodeAllChecked(root, false);
//                    nodeSelector.setChecked(isChecked);
//                    popScreenCondition.setdCode(isChecked ? departmentEntity.getDCode() : "");
//                    popScreenCondition.setdName(isChecked ? departmentEntity.getDName() : "");
//                }
//            });

            arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
            arrowView.setPadding(20,10,10,10);
            if (node.isLeaf()) {

                //修正部门名称显示靠右的问题 by cuizh,0319
                arrowView.setVisibility(View.INVISIBLE);
//                arrowView.setVisibility(View.GONE);

            }
            arrowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tView.toggleNode(node);
                }
            });



            nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setNodeAllChecked(root, false);
                    nodeSelector.setChecked(isChecked);
                    popScreenCondition.setdCode(isChecked ? departmentEntity.getDCode() : "");
                    popScreenCondition.setdName(isChecked ? departmentEntity.getDName() : "");
                    node.setSelected(isChecked);
                    for (TreeNode n : node.getChildren()) {
                        getTreeView().selectNode(n, isChecked);
                    }
                }
            });


            //显示当前所在部门 by cuizh,0319
//            nodeSelector.setChecked(node.isSelected());

            return view;
        }

    }


    class ViewHolder {
        @BindView(R.id.spinner)
        Spinner spinner;

        @BindView(R.id.nice_depart_code)
        FrameLayout niceDepartmentCode;

        @BindView(R.id.btn_action)
        Button btn_action;

        //筛选部门
        @BindView(R.id.et_search_depart)
        EditText et_search_depart;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.btn_action, R.id.tv_search_local})
        void onCLick(View view) {

            switch (view.getId()) {
                case R.id.btn_action:
                    EventBus.getDefault().post(new PopScreenConditionEvent(popScreenCondition));
                    LogUtil.i(TAG, "popScreenCondition:" + popScreenCondition.toString());
                    dismiss();
                    break;
                case R.id.tv_search_local:
                    //筛选部门
                    Global.hideInputMethod(view);
                    buildTreeView(mDataBillClassify);
                    ToastUtils.showLocalToast("搜索部门完成");
                    break;
            }
        }
    }

    public boolean isInclude(String dName) {
        return _CanMiss;
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
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View backbroundView = mMenuView.findViewById(R.id.li_container);
            int height = backbroundView.getTop();
            int y = (int) event.getY();
            int x = (int) event.getX();

            //点击窗口其他地方时收起软键盘 by cuizh,0320
            if (v != null && !(v instanceof EditText)) {
                Global.hideInputMethod(getContentView());
            }

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
            return true;
        }
    };

}
