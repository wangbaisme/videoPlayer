package com.zc.xxj.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.player.util.LoggerUtil;
import com.zc.xxj.R;
import com.zc.xxj.adapter.CommentExpandAdapter;
import com.zc.xxj.bean.CommentDetailBean;
import com.zc.xxj.ui.activity.VideoPlayActivity;
import com.zc.xxj.view.CommentExpandableListView;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class CommentFragment extends BaseFragment {
    private static final String TAG = CommentFragment.class.getSimpleName();

    public CommentExpandableListView expandableListView;
    public CommentExpandableListView commentlistView;
    public CommentExpandAdapter adapter;
    public CommentExpandAdapter commentAdapter;
    public List<CommentDetailBean> commentsList;
    public List<CommentDetailBean> childList = new ArrayList<>();
    private LinearLayout commentView;
    private TextView mBtnClose;

    public CommentFragment(ArrayList<CommentDetailBean> commentsList){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("comment_list", commentsList);
        setArguments(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_comment, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        commentsList = getArguments().getParcelableArrayList("comment_list");
        mBtnClose = view.findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMenuAnimation();
            }
        });
        expandableListView = view.findViewById(R.id.detail_page_lv_comment);
        commentlistView = view.findViewById(R.id.detail_page_comment);
        commentView = view.findViewById(R.id.comment_view);
        initExpandableListView(commentsList);
        initCommentListView();
    }

    private void initCommentListView(){
        commentlistView.setGroupIndicator(null);
        commentAdapter = new CommentExpandAdapter(getActivity(), childList, true);
        commentlistView.setAdapter(commentAdapter);
        for(int i = 0; i<childList.size(); i++){
            commentlistView.expandGroup(i);
        }
        commentlistView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Log.e(TAG, "onGroupClick: 当前的评论>>>"+ " group == "+groupPosition);
                listener.showDetailedReply(groupPosition);
                return true;
            }
        });

        commentlistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Log.e(TAG, "onGroupClick: 当前的评论>>>"+ " group == "+groupPosition+" child == "+childPosition);
                listener.showChildDetailedReply(groupPosition, childPosition);
                return false;
            }
        });
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(getActivity(), commentList, false);
        adapter.setOnLoadMoreListener(new CommentExpandAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore(int groupPosition) {
                startMenuAnimation();
                childList.clear();
                childList.add(commentList.get(groupPosition));
                commentAdapter.showDetailedReply(childList);
                commentlistView.expandGroup(0);
            }

            @Override
            public void showChildReply(int groupPosition, int childPosition) {
                listener.showChildReply(groupPosition, childPosition);
            }
        });
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                listener.showReply(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                listener.showChildReply(groupPosition, childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    private ReplyListener listener;

    public void setListener(ReplyListener listener){
        this.listener = listener;
    }

    public interface ReplyListener{
        void showReply(int groupPosition);
        void showChildReply(int groupPosition, int childPosition);
        void showDetailedReply(int groupPosition);
        void showChildDetailedReply(int groupPosition, int childPosition);
    }

    private void startMenuAnimation() {
        if (commentView.getVisibility() != View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    commentView,
                    "TranslationY",
                    commentView.getHeight(),
                    0);
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    commentView.setVisibility(View.VISIBLE);
                    VideoPlayActivity activity = (VideoPlayActivity) getActivity();
                    activity.dissShowCommentTool();
                }

                @Override
                public void onAnimationEnd(Animator animator) {
//                    mIsMenuAnimationStart = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    commentView.setVisibility(View.INVISIBLE);
                    VideoPlayActivity activity = (VideoPlayActivity) getActivity();
                    activity.showCommentTool();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }

    private void stopMenuAnimation() {
        if (commentView.getVisibility() == View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    commentView,
                    "TranslationY",
                    0,
                    commentView.getHeight());
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
//                    mIsMenuAnimationStart = false;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    commentView.setVisibility(View.INVISIBLE);
                    VideoPlayActivity activity = (VideoPlayActivity) getActivity();
                    activity.showCommentTool();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    commentView.setVisibility(View.INVISIBLE);
                    VideoPlayActivity activity = (VideoPlayActivity) getActivity();
                    activity.showCommentTool();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }

}
