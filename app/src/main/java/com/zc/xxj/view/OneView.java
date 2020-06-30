package com.zc.xxj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.xxj.R;

public abstract class OneView extends FrameLayout implements View.OnClickListener {
    private TextView text;
    private ImageView img;
    private LinearLayout view;


    @SuppressLint("WrongViewCast")
    public OneView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.one_view, this);
        view = findViewById(R.id.view);
        img = findViewById(R.id.img);
        text = findViewById(R.id.text);
        text.setOnClickListener(this);
        img.setOnClickListener(this);
    }

    public void setText(String content){
        text.setText(content);
    }

    public void setImg(int imgRes){
        img.setImageResource(imgRes);
    }

    public void setBackgroundResource(int bgRes){
        view.setBackgroundResource(bgRes);
    }

    public void setTextColor(int color){
        text.setTextColor(color);
    }

    public void setTextSize(int size){
        text.setTextSize(size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text:
                chickText(text.getText().toString());
                break;
            case R.id.img:
                chickImg();
                break;
            default:
                break;
        }
    }

    public abstract void chickImg();
    public abstract void chickText(String text);
}
