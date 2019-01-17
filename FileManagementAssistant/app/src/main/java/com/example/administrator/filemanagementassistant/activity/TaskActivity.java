package com.example.administrator.filemanagementassistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MyAdapter;
import com.example.administrator.filemanagementassistant.adapter.MyDirAdapter;
import com.example.administrator.filemanagementassistant.bean.DirFile;
import com.example.administrator.filemanagementassistant.util.DividerItemDecorations;
import com.example.administrator.filemanagementassistant.util.SdCardUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class TaskActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    private MyAdapter myAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView1;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private FloatingActionsMenu floatingActionsMenu;
    private SwipeRefreshLayout swipeRefreshLayout;


    private View view;
    private View view1;

    private ArrayList<View> pageview;

    private ImageView[] tips = new ImageView[3];
    private ImageView imageView;

    private ViewGroup viewGroup;
    private int mode;
    private List<DirFile> Dirlist=new ArrayList<>();
    private List<String> mydata=new ArrayList<>();

    private MyDirAdapter myDirAdapter;
    private TextView mview;

    private boolean isopen=false;

    private int distance;
    private boolean visible=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);




       

        //初始化控件
        init();
        //申请权限
        requestpermission();
        //下拉刷新
        getRefresh();



        //侧滑菜单的点击事件
        navOnclick();
        //界面2的nav点击事件
        nav1Onclick();
        //侧滑展开关闭的监听
        DrawlayerOnclick();
        //viewpager底部小圆点的显示
        showGroup();


    }


    //控件初始化
    public void init() {

        //夜间模式的设置
        mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;



        viewPager = findViewById(R.id.viewpager);

        view = getLayoutInflater().inflate(R.layout.view01, null);
        view1 = getLayoutInflater().inflate(R.layout.view02, null);
        toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.drawerlayout);

        navigationView = view.findViewById(R.id.nav_view);
        navigationView1 = view1.findViewById(R.id.nav_view);


        floatingActionsMenu=view.findViewById(R.id.bt_fabMenu);
        mview=view.findViewById(R.id.display);

        displaygray();

        swipeRefreshLayout=view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);




        //toolbar设置
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }


        pageview = new ArrayList<View>();
        pageview.add(view);
        pageview.add(view1);

        myAdapter = new MyAdapter(pageview);

        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(this);

        //recyclerview
        recyclerView=view.findViewById(R.id.recyclerview);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    //recyclerview的滑动监听事件
    //有问题待修复
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void RecyclerViewScrollerListener(){

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                if(distance < -ViewConfiguration.getTouchSlop() && !visible){
                   floatingActionsMenu.setVisibility(View.VISIBLE);

                    distance = 0;
                    visible = true;
                }else if(distance > ViewConfiguration.getTouchSlop() && visible){
                    //隐藏
                   floatingActionsMenu.setVisibility(View.GONE);

                    distance = 0;
                    visible = false;
                }

            }



        });
    }

    //下拉刷新
    public void getRefresh(){

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.setLayoutAnimation(  AnimationUtils.loadLayoutAnimation(TaskActivity.this,
                        R.anim.layout_animation_fall_down));
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    //雾板的点击事件
    public  void displayfogdismiss(){
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isopen){
                    mview.setVisibility(View.GONE);
                    floatingActionsMenu.collapse();
                    searchFile();
                    //SearchSdCard();

                }
            }
        });


    }

    //fabmenu的点击事件
    public void  displaygray(){
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mview.setVisibility(View.VISIBLE);
                isopen=true;
                displayfogdismiss();


            }

            @Override
            public void onMenuCollapsed() {
                mview.setVisibility(View.GONE);

            }
        });
    }

    //小圆点的显示
    public void showGroup() {


        viewGroup = findViewById(R.id.linearlayout);
        tips = new ImageView[pageview.size()];

        for (int i = 0; i < pageview.size(); i++) {

            imageView = new ImageView(TaskActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(16, 16));
            imageView.setPadding(16, 0, 16, 0);

            tips[i] = imageView;

            //morenxuanzhong
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.cir_fouce);
            } else {

                tips[i].setBackgroundResource(R.mipmap.cir_unfouce);

            }
            viewGroup.addView(tips[i]);


        }


    }
   //侧滑的点击事件

    public void navOnclick() {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_fist:
                        Intent intent1 = new Intent(TaskActivity.this, ImagePickActivity.class);
                        intent1.putExtra(IS_NEED_CAMERA, true);
                        intent1.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

                        break;

                    case R.id.nav_second:
                        Intent intent4 = new Intent(TaskActivity.this, NormalFilePickActivity.class);
                        intent4.putExtra(Constant.MAX_NUMBER, 9);
                        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
                        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);


                        break;
                    case R.id.nav_third:
                        Intent intent3 = new Intent(TaskActivity.this, AudioPickActivity.class);
                        intent3.putExtra(IS_NEED_RECORDER, true);
                        intent3.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);


                        break;
                    case R.id.nav_fourth:
                        Intent intent2 = new Intent(TaskActivity.this, VideoPickActivity.class);
                        intent2.putExtra(IS_NEED_CAMERA, true);
                        intent2.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
                        break;

                    case R.id.zhuti:
                        if (mode == Configuration.UI_MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            // blah blah
                        }

                        recreate();
                        break;

                    case R.id.tuichu:

                        finish();
                        break;

                    default:
                        break;
                }


                return true;
            }
        });

    }

    //二界面的点击事件
    public void nav1Onclick() {
        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_sendfile:

                        Intent intent = new Intent(TaskActivity.this, SendActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.nav_receivefile:
                        Intent intent1 = new Intent(TaskActivity.this, ReciveActivity.class);
                        startActivity(intent1);

                        break;

                    case R.id.nav_sdcard:
                        /**

                         File sdDir = null;
                         boolean sdCardExist = Environment.getExternalStorageState()
                         .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
                         if(sdCardExist)
                         {
                         sdDir = Environment.getExternalStorageDirectory();//获取跟目录
                         Log.e("77777", "onNavigationItemSelected: "+sdDir );
                         }
                         **/


                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                        String a = "";
                        if (SdCardUtil.isExternalStorageAvailable()) {
                            a = "存在";
                        } else {
                            a = "不存在";
                        }
                        builder.setTitle("存储空间");
                        builder.setMessage("SD卡状态：" + a + "\n" + "SD卡总空间:" + SdCardUtil.getInternalMemorySize(TaskActivity.this) + "\n"
                                + "SD卡可用空间:" + SdCardUtil.getAvailableInternalMemorySize(TaskActivity.this) + "\n" + "手机存储空间大小:" + SdCardUtil.getExternalMemorySize(TaskActivity.this) + "\n"
                                + "手机剩余存储空间:" + SdCardUtil.getAvailableExternalMemorySize(TaskActivity.this));
                        builder.create().show();


                        break;
                    case R.id.nav_overfile:
                        break;
                }
                return false;
            }
        });


    }

    //侧滑展开收起的监听事件  描述不太准确
    public void DrawlayerOnclick() {


        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }

    //菜单的创建

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.file_type_item, menu);


        return super.onCreateOptionsMenu(menu);
    }

    //菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_search:

                Intent intent = new Intent(TaskActivity.this, FileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

                break;
            case R.id.home:

                recyclerView.setLayoutAnimation(  AnimationUtils.loadLayoutAnimation(this,
                        R.anim.layout_animation_fall_down));
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();

                break;
            case R.id.table:

                GridLayoutManager layoutManager=new GridLayoutManager(this,2);




                recyclerView.setLayoutManager(layoutManager);

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {


    }

    @Override
    public void onPageSelected(int i) {


        tips[i].setBackgroundResource(R.mipmap.cir_fouce);
        for (int j = 0; j < pageview.size(); j++) {
            if (i != j) {

                tips[j].setBackgroundResource(R.mipmap.cir_unfouce);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    //权限的判断
    @AfterPermissionGranted(22)
    public void requestpermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {


            search();
            //...
        } else {
            //...

            EasyPermissions.requestPermissions(this, "应用需要照相机和sdcard权限",
                    22, perms);

        }

    }

    //文件夹的搜索
    public void search() {

        File flist = new File("/mnt/sdcard");

        FileFilter ff = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] fileDir = flist.listFiles(ff);
        DirFile[] dirFiles=new DirFile[fileDir.length];
        for (int i = 0; i < fileDir.length; i++) {
            String str = fileDir[i].getName();
            Log.e("wenhaibo", "search: " + str);
            mydata.add(str);
            dirFiles[i]=new DirFile(mydata.get(i));
            Dirlist.add(dirFiles[i]);
            // mResult += str;
            //mResult += "\n";
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myDirAdapter=new MyDirAdapter(Dirlist);


                recyclerView.setAdapter(myDirAdapter);

            }
        });
    }
    /**
    public void searchFile(){
        File flist = new File("/mnt/sdcard");

        FileFilter ff = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] fileDir = flist.listFiles(ff);


        DirFile[] dirFiles=new DirFile[fileDir.length];
        for (int i = 0; i < fileDir.length; i++) {
            String str = fileDir[i].getName();
            Log.e("wenhaibo", "search: " + str);
            mydata.add(str);
            dirFiles[i]=new DirFile(mydata.get(i));
            Dirlist.add(dirFiles[i]);

            String[] mFileList = null;
            mFileList=fileDir[i].list();
            Log.e("999999", "searchFile: "+mFileList );

            // mResult += str;
            //mResult += "\n";
        }
    }

**/

    public void searchFile(){

        File flist = new File("/mnt/sdcard");

        FileFilter ff = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] fileDir = flist.listFiles(ff);

        DirFile[] dirFiles=new DirFile[fileDir.length];
        for (int i = 0; i < fileDir.length; i++) {
            String str = fileDir[i].getName();
            Log.e("wenhaibo", "search: " + str);
            mydata.add(str);
            dirFiles[i]=new DirFile(mydata.get(i));
            Dirlist.add(dirFiles[i]);
            // mResult += str;
            //mResult += "\n";
        }
        if (fileDir!=null){
            for (File file:fileDir){
                if (file.isFile()){
                    //Toast.makeText(this, "已经是文件", Toast.LENGTH_SHORT).show();
                    Log.e("8888", "searchFile: "+"已经是文件了" );
                }else {
                    file.listFiles();
                    Log.e("8888", "searchFile: "+file.listFiles() );


                }
            }
        }
    }





    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }









private List<File>   list_file;

    private void SearchSdCard() {
        // TODO Auto-generated method stub
        // 判断是否挂载
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 获取sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // 创建集合对象
            list_file = new ArrayList<File>();
            // 获取该文件夹的所有子文件以及子文件假(过滤)
            getWant(sdcard);
            // 遍历输出集合list
            for (File ff : list_file) {
               // System.out.println(ff.getAbsolutePath());
                Log.e("7777", "SearchSdCard: "+ff.getAbsolutePath() );
            }
        }

    }

    private void getWant(File sdcard) {
        // TODO Auto-generated method stub
        File[] files = sdcard.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    // 取出文件的名字
                    String filename = file.getName();
                    // 返回后缀名是文本以及MP3的
                    return filename.endsWith(".mp3");

                }

            }
        });

        // 将查询的结果添加到集合中
        if (files != null) {
            for (File f : files) {
                if (f.isFile())// 若是文件，就直接保存
                {
                    list_file.add(f);
                } else {
                    // 若是文件夹就继续扫描mp3,调用自己，递归
                    getWant(f);

                }
            }
        }

    }
}
