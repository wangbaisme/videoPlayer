package com.zc.xxj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zc.player.util.LoggerUtil;
import com.zc.xxj.R;
import com.zc.xxj.bean.CommentDetailBean;
import com.zc.xxj.bean.ReplyDetailBean;
import com.zc.xxj.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentDetailBean> commentBeanList;
    private List<ReplyDetailBean> replyBeanList;
    private Context context;
    private int pageIndex = 1;
    private boolean isLoadMore;

    public CommentExpandAdapter(Context context, List<CommentDetailBean> commentBeanList, boolean isLoadMore) {
        this.context = context;
        this.commentBeanList = commentBeanList;
        this.isLoadMore = isLoadMore;
    }

    @Override
    public int getGroupCount() {
        return commentBeanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (commentBeanList.get(i).getReplyList() == null) {
            return 0;
        } else {
            return commentBeanList.get(i).getReplyList().size() > 0 ? commentBeanList.get(i).getReplyList().size() : 0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return commentBeanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentBeanList.get(i).getReplyList().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Glide.with(context).load(R.mipmap.user_other)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);
        groupHolder.tv_name.setText(commentBeanList.get(groupPosition).getNickName());
        groupHolder.tv_time.setText(commentBeanList.get(groupPosition).getCreateDate());
        groupHolder.tv_content.setText(commentBeanList.get(groupPosition).getContent());
        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLike) {
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#aaaaaa"));
                } else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });

        return convertView;
    }

    @SuppressLint("NewApi")
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout, viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        if (childHolder.comment_view.getVisibility() != View.VISIBLE) childHolder.comment_view.setVisibility(View.VISIBLE);
        if (childHolder.cView.getVisibility() != View.VISIBLE) childHolder.cView.setVisibility(View.VISIBLE);
        if (childHolder.reply_view.getVisibility() != View.VISIBLE) childHolder.reply_view.setVisibility(View.VISIBLE);
        if (childHolder.tv_content.getVisibility() != View.VISIBLE) childHolder.tv_content.setVisibility(View.VISIBLE);
        childHolder.tv_content.setTextColor(context.getColor(R.color.black));

        if (childPosition > 3 && !isLoadMore) {
            childHolder.cView.setVisibility(View.GONE);
            childHolder.comment_view.setVisibility(View.GONE);
            return convertView;
        } else if (childPosition == 3 && !isLoadMore) {
            childHolder.reply_view.setVisibility(View.GONE);
            childHolder.tv_content.setText("查看更多 ( " + commentBeanList.get(groupPosition).getReplyTotal() + " ) ");
            childHolder.tv_content.setTextColor(context.getColor(R.color.color_green));
            childHolder.tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.loadMore(groupPosition);
                }
            });
            return convertView;
        }

        childHolder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showChildReply(groupPosition, childPosition);
            }
        });
        String replyUser="", objectUser="";
        try {
            replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
            objectUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getObjectName();

        if (!TextUtils.isEmpty(replyUser)) {
            childHolder.tv_name.setText(replyUser);
        } else {
            childHolder.tv_name.setText(" ");
        }

        if (TextUtils.isEmpty(objectUser)) {
            childHolder.reply_object.setVisibility(View.GONE);
            childHolder.reply_tool.setVisibility(View.GONE);
        } else {
            childHolder.reply_object.setVisibility(View.VISIBLE);
            childHolder.reply_tool.setVisibility(View.VISIBLE);
            childHolder.reply_object.setText(objectUser);
        }

        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());

        }catch (NullPointerException e){
            LoggerUtil.showDebugLog(TAG, " ---- group == "+groupPosition+" child == "+childPosition);
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder {
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        public GroupHolder(View view) {
            logo = (CircleImageView) view.findViewById(R.id.comment_item_logo);
            tv_content = (TextView) view.findViewById(R.id.comment_item_content);
            tv_name = (TextView) view.findViewById(R.id.comment_item_userName);
            tv_time = (TextView) view.findViewById(R.id.comment_item_time);
            iv_like = (ImageView) view.findViewById(R.id.comment_item_like);
        }
    }

    private class ChildHolder {
        private TextView tv_name, tv_content, reply_tool, reply_object;
        private LinearLayout reply_view;
        private LinearLayout comment_view;
        private View cView;
        public ChildHolder(View view) {
            cView = view.findViewById(R.id.c_view);
            reply_view = view.findViewById(R.id.reply_view);
            comment_view = view.findViewById(R.id.comment_view);
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            reply_tool = view.findViewById(R.id.reply_tool);
            reply_object = view.findViewById(R.id.reply_item_object);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }


    /**
     * func:评论成功后插入一条数据
     *
     * @param commentDetailBean 新的评论数据
     */
    public void addTheCommentData(CommentDetailBean commentDetailBean) {
        if (commentDetailBean != null) {
            commentBeanList.add(commentDetailBean);

            for (int i = commentBeanList.size() - 1; i >= 1; i--) {
                commentBeanList.set(i, commentBeanList.get(i - 1));
            }
            commentBeanList.set(0, commentDetailBean);

            notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * func:回复成功后插入一条数据
     *
     * @param replyDetailBean 新的回复数据
     */
    public void addTheReplyData(ReplyDetailBean replyDetailBean, int groupPosition) {
        if (replyDetailBean != null) {
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:" + replyDetailBean.toString());
            if (commentBeanList.get(groupPosition).getReplyList() != null) {
                commentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
                for (int i = commentBeanList.get(groupPosition).getReplyList().size() - 1; i >= 1; i--) {
                    commentBeanList.get(groupPosition).getReplyList().set(i, commentBeanList.get(groupPosition).getReplyList().get(i - 1));
                }
                commentBeanList.get(groupPosition).getReplyList().set(0, replyDetailBean);
            } else {
                List<ReplyDetailBean> replyList = new ArrayList<>();
                replyList.add(replyDetailBean);
                commentBeanList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * func:添加和展示所有回复
     *
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition) {
        if (commentBeanList.get(groupPosition).getReplyList() != null) {
            commentBeanList.get(groupPosition).getReplyList().clear();
            commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
        } else {
            commentBeanList.get(groupPosition).setReplyList(replyBeanList);
        }

        notifyDataSetChanged();
    }

    private OnLoadMoreListener listener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore(int groupPosition);
        void showChildReply(int groupPosition, int childPosition);
    }

    /**
     * func:显示所有回评
     */
    public void showDetailedReply(List<CommentDetailBean> list) {
        commentBeanList = list;
        notifyDataSetChanged();
    }

}
