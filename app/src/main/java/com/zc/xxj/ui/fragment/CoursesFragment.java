package com.zc.xxj.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.xxj.R;
import com.zc.xxj.adapter.SeriesDetailedAdapter;
import com.zc.xxj.adapter.VideoAdapter;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressLint("ValidFragment")
public class CoursesFragment extends BaseFragment implements VideoAdapter.OnCompleteListener, View.OnClickListener {

    private TextView mBtnSeeAllTotal;
    private TextView mBtnSeeAllExpand;
    private RecyclerView mTotalCourseRecycleView;
    private RecyclerView mExpandCourseRecycleView;
    private RecyclerView mMoreVideoRecycleView;
    private TextView mBtnClose;
    private LinearLayout mTotalVideoView;
    public VideoAdapter mTotalAdapter;
    private VideoAdapter mExpandAdapter;
    private SeriesDetailedAdapter mSeriesDetailedAdapter;
    private LinearLayoutManager detailedLayoutManager;
    private ArrayList<ModuleInfo> totalVideoList = new ArrayList<>();
    private ArrayList<ModuleInfo> expandVideoList = new ArrayList<>();

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_see_all_total:
                //查看全部课程
                mSeriesDetailedAdapter.updateSeriesDetailed(totalVideoList);
                MoveToPosition(detailedLayoutManager,0);
                startMenuAnimation();
                break;
            case R.id.btn_see_all_expand:
                //查看拓展课程
                mSeriesDetailedAdapter.updateSeriesDetailed(expandVideoList);
                MoveToPosition(detailedLayoutManager,0);
                startMenuAnimation();
                break;
            case R.id.btn_close:
                stopMenuAnimation();
                break;
        }
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void resetVideoContent(ModuleInfo mModuleInfo) {
        listener.resetVideo(mModuleInfo);
    }

    public CoursesFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_courses, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mBtnSeeAllTotal = view.findViewById(R.id.btn_see_all_total);
        mBtnSeeAllExpand = view.findViewById(R.id.btn_see_all_expand);
        mTotalCourseRecycleView = view.findViewById(R.id.total_course_recycleview);
        mExpandCourseRecycleView = view.findViewById(R.id.expand_course_recycleview);
        mMoreVideoRecycleView = view.findViewById(R.id.more_video_recycle);
        mBtnClose = view.findViewById(R.id.btn_close);
        mTotalVideoView = view.findViewById(R.id.total_video_view);
        mBtnSeeAllTotal.setOnClickListener(this);
        mBtnSeeAllExpand.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mTotalCourseRecycleView.setNestedScrollingEnabled(false);
        mExpandCourseRecycleView.setNestedScrollingEnabled(false);
        initRecycle();
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mTotalCourseRecycleView.setLayoutManager(layoutManager);
        mTotalAdapter = new VideoAdapter(totalVideoList, false,getActivity());
        mTotalAdapter.setOnCompleteListener(this);
        mTotalCourseRecycleView.setAdapter(mTotalAdapter);

        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(RecyclerView.HORIZONTAL);
        mExpandCourseRecycleView.setLayoutManager(lManager);
        mExpandAdapter = new VideoAdapter(expandVideoList, false, getActivity());
        mExpandAdapter.setOnCompleteListener(this);
        mExpandCourseRecycleView.setAdapter(mExpandAdapter);

        detailedLayoutManager = new LinearLayoutManager(getActivity());
        mMoreVideoRecycleView.setLayoutManager(detailedLayoutManager);
        mSeriesDetailedAdapter = new SeriesDetailedAdapter(getActivity(), totalVideoList) {
            @Override
            public void currentSelectVideo(ModuleInfo info) {
                listener.resetVideo(info);
            }
        };
        mMoreVideoRecycleView.setAdapter(mSeriesDetailedAdapter);
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        totalVideoList = getVideoInfoList("");
        mTotalAdapter.update(totalVideoList);
        expandVideoList = getVideoInfoList("");
        mExpandAdapter.update(expandVideoList);
    }

    private ArrayList<ModuleInfo> getVideoInfoList(String videoId){
        ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();
        for (int i=0; i<18; i++){
            Random random = new Random();
            int index = random.nextInt(4)%4;
            if (index == 0) mModuleInfoList.add(new ModuleInfo("我们连觉也没睡决定连夜赶去拜访艾立克克莱普顿", R.mipmap.img_2,mUrls[0],true));
            else if (index == 1) mModuleInfoList.add(new ModuleInfo("如果写不出好的和弦就该在洒满阳光的钢琴前一起吃布丁",R.mipmap.img_3,mUrls[1],false));
            else if (index == 2) mModuleInfoList.add(new ModuleInfo("即使全世界都嫌弃这首歌肉麻又俗气我还是要把它献给你",R.mipmap.img_4,mUrls[2],false));
            else if (index == 3) mModuleInfoList.add(new ModuleInfo("遇见你的时候所有星星都落到我头上",R.mipmap.img_1,mUrls[0],false));
        }
        return mModuleInfoList;
    }

    private String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };

    private void startMenuAnimation() {
        if (mTotalVideoView.getVisibility() != View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    mTotalVideoView,
                    "TranslationY",
                    mTotalVideoView.getHeight(),
                    0);
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mTotalVideoView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
//                    mIsMenuAnimationStart = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mTotalVideoView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }

    private void stopMenuAnimation() {
        if (mTotalVideoView.getVisibility() == View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    mTotalVideoView,
                    "TranslationY",
                    0,
                    mTotalVideoView.getHeight());
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
//                    mIsMenuAnimationStart = false;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mTotalVideoView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mTotalVideoView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }

    private void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    public void setListener(OnResetListener listener){
        this.listener = listener;
    }
    private OnResetListener listener;

    public interface OnResetListener{
        void resetVideo(ModuleInfo info);
    }
}
