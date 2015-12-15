package com.rocky.utouu.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocky.utouu.R;

/**
 * Created by xiabaikui on 2015/12/15.
 */
public class CustomProgressDialog extends ProgressDialog {
    private Context context;
    private String content;
    private int id;
    private ImageView progress_iv;
    private TextView progress_tv;
    private AnimationDrawable animationDrawable;

    public CustomProgressDialog(Context context, String content, int id) {
        super(context);
        this.context = context;
        this.content = content;
        this.id = id;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        progress_iv.setBackgroundResource(id);
        animationDrawable = ((AnimationDrawable) progress_iv.getBackground());
        progress_iv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
        progress_tv.setText(content);
    }

    private void initView() {
        setContentView(R.layout.progress_layout);
        progress_iv = ((ImageView) findViewById(R.id.progress_iv));
        progress_tv = ((TextView) findViewById(R.id.progress_tv));
    }
}
