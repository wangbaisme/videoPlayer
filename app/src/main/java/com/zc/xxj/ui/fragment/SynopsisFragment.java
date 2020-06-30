package com.zc.xxj.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.xxj.R;

@SuppressLint("ValidFragment")
public class SynopsisFragment extends BaseFragment {

    public SynopsisFragment(String module_name){
        Bundle bundle = new Bundle();
        bundle.putString("module_name", module_name);
        setArguments(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_synopsis, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        Log.e("test",getArguments().getString("module_name"));
//        ((TextView)view.findViewById(R.id.tv)).setText(getArguments().getString("module_name"));
        TextView tv = view.findViewById(R.id.tv);
        tv.setText(getArguments().getString("module_name"));
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }
}
