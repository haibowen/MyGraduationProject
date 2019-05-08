package com.example.administrator.filemanagementassistant.activity;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pDevice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.fragment.FileFragment;
import com.example.administrator.filemanagementassistant.fragment.FindFragment;
import com.example.administrator.filemanagementassistant.fragment.MyFragment;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.btnagationbar)
    public BottomNavigationBar bottomNavigationBar;
    @BindView(R.id.drawerlayout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    private CircleImageView circleImageView;
    private FileFragment fileFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private int lastselection = 0;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //夜间模式的设置
        mode=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        setContentView(R.layout.activity_header);
        ButterKnife.bind(this);

        //头像更换跳转页面

        View nav_header=navigationView.inflateHeaderView(R.layout.myfragment_header);
        circleImageView=nav_header.findViewById(R.id.image_header_myfragment);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "我被点击了", Toast.LENGTH_SHORT).show();
                onTabSelected(2);
                drawerLayout.closeDrawers();


            }
        });
        //侧滑菜单的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_fist:
                        Intent intent1 = new Intent(MainActivity.this, ImagePickActivity.class);
                        intent1.putExtra(IS_NEED_CAMERA, true);
                        intent1.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
                        break;
                    case R.id.nav_second:
                        Intent intent4 = new Intent(MainActivity.this, NormalFilePickActivity.class);
                        intent4.putExtra(Constant.MAX_NUMBER, 9);
                        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
                        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
                        break;
                    case R.id.nav_third:
                        Intent intent3 = new Intent(MainActivity.this, AudioPickActivity.class);
                        intent3.putExtra(IS_NEED_RECORDER, true);
                        intent3.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
                        break;
                    case R.id.nav_fourth:
                        Intent intent2 = new Intent(MainActivity.this, VideoPickActivity.class);
                        intent2.putExtra(IS_NEED_CAMERA, true);
                        intent2.putExtra(Constant.MAX_NUMBER, 9);
                        startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
                        break;

                    case R.id.zhuti:
                        if(mode == Configuration.UI_MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
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
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        /**
         *  setMode() 内的参数有三种模式类型：
         *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
         *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
         *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
         */

        bottomNavigationBar
                .setTabSelectedListener((BottomNavigationBar.OnTabSelectedListener) this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#3F51B5")//选中颜色
                .setInActiveColor("#807d7d");//未选中
        // .setBarBackgroundColor("#1ccbae");//导航栏颜色
        /**
         *  setBackgroundStyle() 内的参数有三种样式
         *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
         *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
         *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
         *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
         */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "Home"))
                //.addItem(new BottomNavigationItem(R.drawable.ic_insert_drive_file_black_24dp, "File"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, "My"))
                .setFirstSelectedPosition(lastselection)
                .initialise();
        SetDefaultFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_type_item, menu);
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
       // searchView.setQueryHint("搜索文件格式...");
        /**
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FileActivity.class);
                startActivity(intent);
            }
        });
         **/
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search:
                Intent intent=new Intent(MainActivity.this,FileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置默认显示的fragment
     */
    public void SetDefaultFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        findFragment = new FindFragment();
        fragmentTransaction.replace(R.id.tb, findFragment);
        fragmentTransaction.commit();
    }

    /**
     * 底部导航栏的显示和切换
     *实现接口的方法
     * @param position
     */

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:

                if (findFragment==null){
                    findFragment = new FindFragment();

                }
                fragmentTransaction.replace(R.id.tb, findFragment);
                break;
                /**
            case 1:

                if (fileFragment==null){
                    fileFragment = new FileFragment();
                }

                fragmentTransaction.replace(R.id.tb, fileFragment);
                break;
                 **/
            case 1:

                if (myFragment==null){

                    myFragment = new MyFragment();
                }

                fragmentTransaction.replace(R.id.tb, myFragment);
                break;

            default:
                break;
        }
        fragmentTransaction.commit();

    }
    @Override
    public void onTabUnselected(int position) {
    }
    @Override
    public void onTabReselected(int position) {
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static String getDeviceStatus(int deviceStatus) {

        switch (deviceStatus) {

            case WifiP2pDevice.AVAILABLE:
                return "可用的";
            case WifiP2pDevice.CONNECTED:
                return "已连接";
            case WifiP2pDevice.FAILED:
                return "失败的";
            case WifiP2pDevice.INVITED:
                return "邀请中";
            case WifiP2pDevice.UNAVAILABLE:
                return "不可用";
            default:
                return "未知";
        }

    }

}
