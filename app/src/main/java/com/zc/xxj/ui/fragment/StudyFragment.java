package com.zc.xxj.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zc.xxj.R;
import com.zc.xxj.bean.nodechild;
import com.zc.xxj.view.DrawGeometryView;
import com.zc.xxj.view.HVScrollView;
import com.zc.xxj.view.OneView;

import java.util.Random;

public class StudyFragment extends BaseFragment {

    private int[] tree_xnum = new int[100];
    private RelativeLayout insertLayout;
    private DrawGeometryView view;

    private HVScrollView hv;
    private OneView[] bt = new OneView[15];
    private RelativeLayout.LayoutParams[] layoutParams = new RelativeLayout.LayoutParams[15];
    private RelativeLayout.LayoutParams[] layoutParams1 = new RelativeLayout.LayoutParams[15];
    private Mystack mstack = new Mystack();
    private boolean model = true;
    private int bt_width = 300;

    public StudyFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_study, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        insertLayout = (RelativeLayout) view.findViewById(R.id.layout_zone);

        hv = (HVScrollView) view.findViewById(R.id.hvscrollview);

        nodechild[] nc = new nodechild[1];
        nc[0] = new nodechild("2", "思维导图\n20分");
        drawbutton(width - 200, 50, 50, 1, nc, "1");
    }
    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    public void drawbutton(int button_y, int button_x, int line_x, final int tree_current, final nodechild[] nc, String nodeid) {
//        存储线的起点y坐标
        int line_y = button_y;
//        这个只是为了区分业务中偶数层button宽度为300，齐数层为200
//        button_x = tree_current % 2 == 1 ? button_x : button_x - 100;
//        得到下一层级需要绘制的数量
        int num = 1;
        if (tree_current != 1) num = nc.length;// 下一层个数
//        得到下一级第一个按钮的y坐标
        button_y = button_y - (num - 1) * bt_width / 2;
        if (button_y < tree_xnum[tree_current]) {
            button_y = tree_xnum[tree_current] + 100;
        }
//        移动当前布局到页面中心
        if (tree_current > 2) hv.scrollTo(button_x - 400, button_y - 100);
        if (tree_xnum[tree_current] < button_y + 200 + (num - 1) * bt_width)
            tree_xnum[tree_current] = button_y + 200 + (num - 1) * bt_width;
//        存储下一级首个button坐标
        final int button_y_f = button_y;
        final int button_x_f = button_x;
        for (int i = 0; i < num; i++) {
            final int bt_paly_y = bt_width;
//            int bt_w = tree_current % 2 == 0 ? bt_width : 200;
            int bt_w = bt_width;
            int bt_h = 200;
            final int i1 = i;
            final String nc_id = nc[i].getId();
//            定义及设置button属性
            bt[i] = new OneView(getActivity()) {
                @Override
                public void chickImg() {
                    if (model) mstack.pop(tree_current);
//                    防止多次点击，偷懒的解决办法
//                    if (((Button) v).getHint() != null) {
//                        Toast.makeText(getApplicationContext(), ((Button) v).getText(), Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    ((Button) v).setHint("1");
                    insertLayout.setEnabled(false);
                    int w = button_y_f + i1 * bt_paly_y;
                    int h = button_x_f + bt_paly_y / 2 * 3;

                    getRemoteInfo(w, h, button_y_f + i1 * bt_paly_y, button_x_f, tree_current + 1, nc_id,
                            nc[i1].getButeid());
                }

                @Override
                public void chickText(String text) {
                    Toast.makeText(getActivity(), text + " -- " + i1, Toast.LENGTH_SHORT).show();
                }
            };
            if (tree_current % 2 != 0) {
                bt[i].setBackgroundResource(R.mipmap.allokbutton);
            } else {
                bt[i].setBackgroundResource(R.mipmap.button33);
            }
            bt[i].setTextColor(Color.WHITE);
            bt[i].setTextSize(15 - (int) Math.sqrt(nc[i].getName().length() - 1));
            bt[i].setText(nc[i].getName());
//            定义及设置出场动画
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setInterpolator(new BounceInterpolator());
            animation.setStartOffset(tree_current == 1 ? 1050 : 50);// 动画秒数。
            animation.setFillAfter(true);
            animation.setDuration(700);
            bt[i].startAnimation(animation);
            layoutParams[i] = new RelativeLayout.LayoutParams(bt_w, bt_h);
            layoutParams[i].topMargin = button_y + i * bt_paly_y;
            layoutParams[i].leftMargin = button_x;
            insertLayout.addView(bt[i], layoutParams[i]);

//            把线绘制到页面里
            if (tree_current != 1) {
                if (button_y + 100 + i * 300 - (line_y + 100) >= 0) {
                    view = new DrawGeometryView(getActivity(), 50, 50, button_x + 100 - (line_x + bt_paly_y) + 50 + (tree_current % 2 == 0 ? 100 : 0), button_y + 100 + i * 300
                            - (line_y + 100) + 50, nc[i].getButetype(), tree_current);
                    layoutParams1[i] = new RelativeLayout.LayoutParams(Math.abs(line_x - button_x) + 500, 100 + button_y + i * 300 - line_y);
                    view.invalidate();
                    layoutParams1[i].topMargin = (line_y + 100) - 50;// line_y-600;//Math.min(line_y+100,button_y+100
                    layoutParams1[i].leftMargin = (line_x + bt_paly_y) - 50;// line_x+300;
                    if (tree_current % 2 == 0) layoutParams1[i].leftMargin -= 100;
                    insertLayout.addView(view, layoutParams1[i]);
                } else {
                    view = new DrawGeometryView(getActivity(), 50, -(button_y + 100 + i * 300 - (line_y + 100)) + 50, button_x - line_x - 150 + (tree_current % 2 == 0 ? 100 : 0), 50,
                            nc[i].getButetype(), tree_current);
                    layoutParams1[i] = new RelativeLayout.LayoutParams(Math.abs(line_x - button_x) + 500, 100 + Math.abs(button_y + i * 300
                            - line_y));
                    view.invalidate();
                    layoutParams1[i].topMargin = (button_y + 100 + i * 300) - 50;// line_y-600;//Math.min(line_y+100,button_y+100
                    layoutParams1[i].leftMargin = (line_x + bt_paly_y) - 50;// line_x+300;
                    if (tree_current % 2 == 0) layoutParams1[i].leftMargin -= 100;
                    insertLayout.addView(view, layoutParams1[i]);
                }
//                line入栈
                mstack.push(view, tree_current);
            }
//            button入栈
            mstack.push(bt[i], tree_current);
        }
    }

    public synchronized void getRemoteInfo(int paly_y, int paly_x, int ppaly_y, int ppaly_x, int tree_h,
                                           String nodeid, String buteid) {
        int n = 1;
        Random random = new Random();
        n = random.nextInt(4) + 1;
        nodechild[] nc = new nodechild[n];
        for (int i = 0; i < n; i++) {
            nc[i] = new nodechild("1", "你好");
        }
        drawbutton(paly_y, paly_x, ppaly_x, tree_h, nc, nodeid);
    }

    public class Mystack {
        View[] v = new View[1500];
        int[] treehigh = new int[1500];
        int size = 0;

        public void push(View view, int treecurrent) {
            size++;
            v[size] = view;
            treehigh[size] = treecurrent;
        }

        public void pop(int treecurrent) {
            while (treehigh[size] > treecurrent && size > 0) {
                if (size > 0) insertLayout.removeView(v[size]);
                size--;
            }
            for (int j = 49; j > treecurrent; j--) {
                tree_xnum[j] = 0;
            }
            for (int x = size; x > 0; x--) {
                if (treehigh[x] > treecurrent) {
                    insertLayout.removeView(v[x]);
                }
                if (treehigh[x] == treecurrent) {
                    try {
                        ((Button) v[x]).setHint(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
