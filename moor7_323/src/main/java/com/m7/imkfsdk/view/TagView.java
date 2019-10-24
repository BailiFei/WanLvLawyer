package com.m7.imkfsdk.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.model.Option;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * @author 张小五先森
 * @date 2018/8/3
 * @description 多选的tag
 */
public class TagView extends LinearLayout {

    private RecyclerView rvTagName;
    private Context context;
    private TagSelectAdapter adapter;
    private TagSingleSelectAdapter tagSingleSelectAdapter;
    private OnSelectedChangeListener onSelectedChangeListener;
    private List<Option> optionLists = new ArrayList<>();

    public int SelectedPosition = -1;

    public static final int SINGLE = 0;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.kf_tag_view, this);
        rvTagName = (RecyclerView) findViewById(R.id.rv_tagName);
    }

    public void initTagView(List<Option> optionList, int type) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        rvTagName.setLayoutManager(layoutManager);
        switch (type) {
            case 0://单选Tag
                tagSingleSelectAdapter = new TagSingleSelectAdapter(context, optionList);
                rvTagName.setAdapter(tagSingleSelectAdapter);
                break;
            case 1://多选
                adapter = new TagSelectAdapter(context, optionList);
                rvTagName.setAdapter(adapter);
                break;
        }
    }

    /**
     * 多选的adapter
     */
    class TagSelectAdapter extends RecyclerView.Adapter<TagSelectAdapter.MineContactViewHolder> {

        private final Context mContext;
        private final LayoutInflater layoutInflater;
        private List<Option> list;
        private Set<Option> broadcas = new LinkedHashSet<>();

        public TagSelectAdapter(Context mActivity, List<Option> list) {
            this.mContext = mActivity;
            this.list = list;
            layoutInflater = LayoutInflater.from(mActivity);
        }

        @Override
        public MineContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MineContactViewHolder(layoutInflater.inflate(R.layout.kf_textview_flowlayout, parent, false));
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(MineContactViewHolder holder, final int position) {
            final Option bean = list.get(position);
            if (bean.isSelected) {
                broadcas.add(bean);
                holder.tv_title.setBackground(ContextCompat.getDrawable(mContext, R.drawable.kf_bg_my_label_selected));
                holder.tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.kf_tag_select));
            } else {
                holder.tv_title.setBackground(ContextCompat.getDrawable(mContext, R.drawable.kf_bg_my_label_unselected));
                holder.tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.kf_tag_unselect));
            }
            holder.tv_title.setText(bean.name);

            holder.tv_title.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bean.isSelected()) {
                        //可以在这里控制最多选几个zts
                        broadcas.add(bean);
                    } else {
                        for (Iterator it = broadcas.iterator(); it.hasNext(); ) {
                            Option value = (Option) it.next();
                            if (value.name.equals(bean.name)) {
                                it.remove();
                            }
                        }
                        broadcas.remove(bean);
                    }
                    bean.setSelected(!bean.isSelected());
                    notifyItemChanged(position);
                    if (broadcas.size() > 0) {
                        optionLists.clear();
                        optionLists.addAll(broadcas);
                        onSelectedChangeListener.getTagList(optionLists);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        public class MineContactViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title;

            public MineContactViewHolder(View inflate) {
                super(inflate);
                tv_title = (TextView) inflate.findViewById(R.id.tv_title);

            }
        }
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    /**
     * 单选的Adapter
     */

    class TagSingleSelectAdapter extends RecyclerView.Adapter<TagSingleSelectAdapter.SingleTagViewHolder> {

        private final Context mContext;
        private final LayoutInflater layoutInflater;
        private List<Option> list;
        private Set<Option> broadcas = new LinkedHashSet<>();
        private SingleTagViewHolder holder1;

        public TagSingleSelectAdapter(Context mActivity, List<Option> list) {
            this.mContext = mActivity;
            this.list = list;
            layoutInflater = LayoutInflater.from(mActivity);
        }


        @Override
        public SingleTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SingleTagViewHolder(layoutInflater.inflate(R.layout.kf_textview_flowlayout, parent, false));
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final SingleTagViewHolder holder, final int position, List payloads) {
            holder1 = holder;
            final Option bean = list.get(position);
            if (payloads.isEmpty()) {
                setTagView(holder1, position, bean);
                holder.tv_title.setText(bean.name);
                holder.tv_title.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //拿到正确的position
                        int position1 = (int) holder.tv_title.getTag();
                        Option bean = list.get(position1);
                        if (bean.isSelected) {//点击的是选中的
                            bean.isSelected = false;
                            notifyItemChanged(position1, bean);
                            SelectedPosition = -1;
                            onSelectedChangeListener.getTagList(optionLists);
                        } else {//点击的如果是没有选中的
                            if (SelectedPosition != -1) { //说明肯定是有选中的
                                Option option = list.get(SelectedPosition);
                                option.isSelected = false;
                                notifyItemChanged(SelectedPosition, option);//同时将之前选中的变成非选中
                                onSelectedChangeListener.getTagList(optionLists);
                            }
                            SelectedPosition = position1;//记录将要选中的位置
                            bean.isSelected = true;
                            broadcas.clear();
                            broadcas.add(bean);
                            notifyItemChanged(position1, bean);//更新新选中的状态
                            optionLists.clear();
                            optionLists.addAll(broadcas);
                            onSelectedChangeListener.getTagList(optionLists);

                        }
                    }
                });
            } else {
                if (payloads.get(0) instanceof Option) {
                    setTagView(holder1, position, (Option) payloads.get(0));
                }
            }
        }

        @Override
        public void onBindViewHolder(SingleTagViewHolder holder, final int position) {
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        void setTagView(SingleTagViewHolder holder, int position, Option option) {
            // Option option = list.get(position);
            holder.tv_title.setTag(position);//为了拿到正确的position
            if (option.isSelected) {
                broadcas.clear();
                broadcas.add(option);
                holder.tv_title.setBackground(ContextCompat.getDrawable(mContext, R.drawable.kf_bg_my_label_selected));
                holder.tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.kf_tag_select));
            } else {
                holder.tv_title.setBackground(ContextCompat.getDrawable(mContext, R.drawable.kf_bg_my_label_unselected));
                holder.tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.kf_tag_unselect));
            }
        }

        public class SingleTagViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title;

            public SingleTagViewHolder(View inflate) {
                super(inflate);
                tv_title = (TextView) inflate.findViewById(R.id.tv_title);

            }
        }
    }


    public interface OnSelectedChangeListener {
        void getTagList(List<Option> options);
    }

    public void clearSelcted() {
        for (Option option : optionLists) {
            option.isSelected = false;
        }
        if (tagSingleSelectAdapter != null) {
            tagSingleSelectAdapter.notifyDataSetChanged();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

}
