package com.rocky.utouu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rocky.utouu.adapter.ADListViewAdapter;
import com.rocky.utouu.fragment.LeftFragment;
import com.rocky.utouu.fragment.RightFragment;
import com.rocky.utouu.javabean.ADdetials;
import com.rocky.utouu.utils.Constants;
import com.rocky.utouu.utils.HttpConnectionhelper;
import com.rocky.utouu.utils.JsonParams;
import com.rocky.utouu.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private PullToRefreshListView listView;
    private ImageView iv_menu;
    private ImageView iv_person;
    private TextView tv_title;
    private int currentPager = 1;
    private List<ADdetials> list;
    private ADListViewAdapter adapter;
    private SlidingMenu slidingMenu;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (listView.isRefreshing()) {
                        listView.onRefreshComplete();
                    }
                    Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    if (listView.isRefreshing()) {
                        listView.onRefreshComplete();
                    }
                    Toast.makeText(MainActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String obj = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(obj);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            list = JsonParams.getListAD(obj);
                            mHandler.sendEmptyMessage(3);
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 3:
                    progressDialog.dismiss();
                    adapter = new ADListViewAdapter(MainActivity.this, list, mHandler);
                    listView.setAdapter(adapter);
                    if (listView.isRefreshing()) {
                        listView.onRefreshComplete();
                    }
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    if (listView.isRefreshing()) {
                        listView.onRefreshComplete();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    if (listView.isRefreshing()) {
                        listView.onRefreshComplete();
                    }
                    Toast.makeText(MainActivity.this, "已经是最新数据", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    break;
                case 7:
                    String ob = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(ob);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            List<ADdetials> listAD = JsonParams.getListAD(ob);
                            list.addAll(listAD);
                            mHandler.sendEmptyMessage(8);
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    Log.i("tag", "---->list.size():" + list.size());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ListView lv;
    private boolean isDivPage;
    private CustomProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSlidingMenu();
    }


    /**
     * 添加左右扩展
     */
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置菜单模式
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置menu滑动的区域
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置淡入淡出背景，宽度和背景图片
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.mipmap.shadow_left);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindWidth(300);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        //0--1之间取值
        slidingMenu.setBehindScrollScale(0.7f);

        Fragment leftMenuFragment = new LeftFragment();
        slidingMenu.setMenu(R.layout.left_fragment);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.left_id_fragment, leftMenuFragment).commit();

        slidingMenu.setSecondaryShadowDrawable(R.mipmap.shadow_right);
        Fragment rightMenuFragment = new RightFragment();
        slidingMenu.setSecondaryMenu(R.layout.right_fragment);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.right_container_fragment, rightMenuFragment).commit();
        initListener();
    }

    /**
     * Slidingmenu的监听
     */
    private void initListener() {
        //监听menu打开后执行
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                if (slidingMenu.isSecondaryMenuShowing()) {

                } else {

                }
                Log.i("tag", "---------->opened");
            }
        });
        //当menu关闭后执行
        slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                Log.i("tag", "---------->closed");

            }
        });
        //打开时执行
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                Log.i("open", "---------->onpen");
            }
        });
        //关闭时执行
        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                Log.i("open", "---------->oclose");
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        listView = ((PullToRefreshListView) findViewById(R.id.main_pulltorefresh));
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv = listView.getRefreshableView();
        iv_menu = ((ImageView) findViewById(R.id.main_menu));
        iv_person = ((ImageView) findViewById(R.id.main_person));
        tv_title = ((TextView) findViewById(R.id.main_title));
        iv_menu.setOnClickListener(this);
        iv_person.setOnClickListener(this);
        list = new ArrayList<>();
        initNetData();
        pullLayout();
        initListViewListener();
    }

    /**
     * 下拉刷新，上拉加载
     */
    private void initListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ADdetials aDdetials = list.get(position);
                String id1 = aDdetials.getId();
                String picture = aDdetials.getPicture();
                String award_gold = aDdetials.getAward_gold();
                String name = aDdetials.getName();
                String start_date = aDdetials.getStart_date();
                Bundle bundle = new Bundle();
                bundle.putString("id", id1);
                bundle.putString("picture", picture);
                bundle.putString("name", name);
                bundle.putString("start_data", start_date);
                bundle.putString("award_gold", award_gold);
                Intent intent = new Intent(MainActivity.this, DetialActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        /**
         * 分页
         */
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**
                 * 当屏幕停止滚动时为
                 * SCROLL_STATE_IDLE = 0
                 * 当屏幕滚动且用户使用的触碰或手指还在屏幕上时
                 * SCROLL_STATE_TOUCH_SCROLL = 1
                 * 由于用户的操作，屏幕产生惯性滑动时
                 * SCROLL_STATE_FLING = 2
                 */
                if (isDivPage && SCROLL_STATE_IDLE == scrollState) {
                    //  分页请求
                    new Thread() {
                        @Override
                        public void run() {

                            if (HttpConnectionhelper.isNetWorkConntected(MainActivity.this)) {

                                Log.i("tag", "---->加载第" + currentPager + "页");
                                String result = HttpConnectionhelper.doGetSumbit(Constants.URL_listview + currentPager);
                                currentPager = currentPager + 1;
                                //分页新数据
                                if (result != null && result.length() > 0) {
                                    Log.i("tag", "------>下一页" + currentPager);
                                    Message message = Message.obtain();
                                    message.obj = result;
                                    message.what = 7;
                                    mHandler.sendMessage(message);
                                } else {
                                    mHandler.sendEmptyMessage(2);
                                }
                            } else {
                                mHandler.sendEmptyMessage(1);
                            }
                        }
                    }.start();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isDivPage = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });

        //绑定刷新监听事件
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //  更改数据源
                new Thread() {
                    @Override
                    public void run() {

                        if (HttpConnectionhelper.isNetWorkConntected(MainActivity.this)) {

                            String result = HttpConnectionhelper.doGetSumbit(Constants.URL_listview + 1);

                            if (result != null && result.length() > 0) {
                                List<ADdetials> pulltorefresh_list = JsonParams.getListAD(result);
                                if (list != null && list.size() > 0) {
                                    if (list.containsAll(pulltorefresh_list)) {
                                        mHandler.sendEmptyMessage(5);
                                    } else {
                                        list.clear();
                                        list.addAll(pulltorefresh_list);
                                        mHandler.sendEmptyMessage(4);
                                    }
                                }else{
                                    list.addAll(pulltorefresh_list);
                                    mHandler.sendEmptyMessage(4);
                                }
                            } else {
                                mHandler.sendEmptyMessage(1);
                            }
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                }.start();

            }
        });
    }

    /**
     * 设置下拉动画提醒
     */
    private void pullLayout() {
        ILoadingLayout loadingLayoutProxy = listView.getLoadingLayoutProxy(true, false);
        loadingLayoutProxy.setPullLabel("要刷新吗？");
        loadingLayoutProxy.setReleaseLabel("开始刷新");
        loadingLayoutProxy.setRefreshingLabel("加载中......");
        Drawable drawable = getResources().getDrawable(R.mipmap.wxcircel);
        loadingLayoutProxy.setLoadingDrawable(drawable);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        loadingLayoutProxy.setLastUpdatedLabel(sdf.format(new Date()));
    }

    /**
     * 加载首页的数据
     */
    private void initNetData() {
        showDialog();
        new Thread() {
            @Override
            public void run() {
                if (HttpConnectionhelper.isNetWorkConntected(MainActivity.this)) {
                    String result = HttpConnectionhelper.doGetSumbit(Constants.URL_listview + currentPager);
                    Log.i("tag", "---->" + result);
                    if (result != null && result.length() > 0) {
                        currentPager += 1;
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = result;
                        mHandler.sendMessage(message);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu:
                slidingMenu.showMenu();
                break;
            case R.id.main_person:
                slidingMenu.showSecondaryMenu();
                break;
        }
    }

    private int times = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            times++;
            if (times == 2) {
                finish();
            } else if (times == 1) {
                Toast.makeText(MainActivity.this, "再一次点击退出应用", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        times = 0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        times = 0;
    }
    private void showDialog(){
        progressDialog = new CustomProgressDialog(this, "正在加载...", R.anim.loading_anim);
        progressDialog.show();
    }
}
