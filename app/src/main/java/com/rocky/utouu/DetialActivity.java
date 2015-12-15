package com.rocky.utouu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.rocky.utouu.utils.BitmapSinglton;
import com.rocky.utouu.utils.Constants;
import com.rocky.utouu.utils.HttpConnectionhelper;
import com.rocky.utouu.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DetialActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private ImageView iv_play;
    private ImageView iv_share;
    private RelativeLayout re_bg;
    private TextView tv_title;
    private BitmapSinglton singlton;
    private BitmapUtils bitmapUtils;
    private String id;
    private String picture;
    private String name;
    private String start_data;
    private String award_gold;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Toast.makeText(DetialActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(DetialActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String result = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(result).getJSONObject("data");
                        Log.i("tag", "------result" + object);
                        String video = object.getString("video");
                        video_path = Constants.URL_video + video;
                        Intent intent = new Intent(DetialActivity.this, PlayerActivity.class);
                        Bundle bundle = new Bundle();
                        Log.i("tag", "-------->vedio_path" + video_path);
                        bundle.putString("video_path", video_path);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private String video_path;
    private TextView tv_title_show;
    private TextView tv_time;
    private TextView tv_gold;
    private TextView tv_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            picture = bundle.getString("picture");
            name = bundle.getString("name");
            start_data = bundle.getString("start_data");
            award_gold = bundle.getString("award_gold");
            Log.i("tag", "------>bundle" + id + picture + name + start_data + award_gold);
        } else {
            Log.i("tag", "------>bundle为空");
        }
        initView();
    }

    private void initView() {
        singlton = BitmapSinglton.getSinglton();
        bitmapUtils = singlton.getBitmapUtils();
        iv_back = ((ImageView) findViewById(R.id.detial_iv_back));
        iv_play = ((ImageView) findViewById(R.id.detial_iv_play));
        iv_share = ((ImageView) findViewById(R.id.detial_iv_share));
        re_bg = ((RelativeLayout) findViewById(R.id.detial_re_bg));
        tv_title = ((TextView) findViewById(R.id.detial_tv_title));
        tv_title_show = ((TextView) findViewById(R.id.detial_tv_title_show));
        tv_time = ((TextView) findViewById(R.id.detial_tv_time));
        tv_gold = ((TextView) findViewById(R.id.detial_tv_gold));
        tv_play = ((TextView) findViewById(R.id.detial_iv_showplay));
        iv_play.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_play.setOnClickListener(this);
        initData();
    }

    private void initData() {
        tv_title.setText(name);
        tv_title_show.setText(name);
        tv_gold.setText("任务奖励：" + award_gold + "银子");
        Long milliseconds = Long.valueOf(start_data);
        Date date = new Date(milliseconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd");
        String dateStr = sdf.format(date);
        tv_time.setText("开始时间：" + dateStr);
        String path = Constants.URL_image + picture;
        bitmapUtils.display(re_bg, path);
    }

    private CustomProgressDialog progressDialog;

    private void showDialog() {
        progressDialog = new CustomProgressDialog(this, "正在加载...", R.anim.loading_anim);
        progressDialog.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.detial_iv_play:
                showDialog();
                startThread();
                break;
            case R.id.detial_iv_showplay:
                showDialog();
                startThread();
                break;
            case R.id.detial_iv_back:
                Intent intent = new Intent(DetialActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void startThread() {
        new Thread() {
            @Override
            public void run() {
                if (HttpConnectionhelper.isNetWorkConntected(DetialActivity.this)) {
                    String play_path = Constants.URL_detials + id;
                    String result = HttpConnectionhelper.doGetSumbit(play_path);
                    if (result != null && result.length() > 0) {
                        Message message = Message.obtain();
                        message.obj = result;
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }
}
