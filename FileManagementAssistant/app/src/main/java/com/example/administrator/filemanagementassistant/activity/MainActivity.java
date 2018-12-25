package com.example.administrator.filemanagementassistant.activity;
import android.content.*;
import android.net.wifi.p2p.WifiP2pDevice;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import de.hdodenhof.circleimageview.CircleImageView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        ButterKnife.bind(this);

        View nav_header=navigationView.inflateHeaderView(R.layout.nav_header);
        circleImageView=nav_header.findViewById(R.id.image_header);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "我被点击了", Toast.LENGTH_SHORT).show();
                onTabSelected(2);
                drawerLayout.closeDrawers();

                /**
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                findFragment = new FindFragment();
                fragmentTransaction.replace(R.id.tb, myFragment);
                fragmentTransaction.commit();
                 **/

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
                .addItem(new BottomNavigationItem(R.drawable.ic_insert_drive_file_black_24dp, "File"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, "My"))
                .setFirstSelectedPosition(lastselection)
                .initialise();
        SetDefaultFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);

                break;

            case R.id.sendfile:

                Intent intent=new Intent(MainActivity.this,SendActivity.class);
                startActivity(intent);
                break;
            case R.id.receivefile:
                Intent intent1=new Intent(MainActivity.this,ReciveActivity.class);
                startActivity(intent1);
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

                findFragment = new FindFragment();
                fragmentTransaction.replace(R.id.tb, findFragment);
                break;
            case 1:

                fileFragment = new FileFragment();
                fragmentTransaction.replace(R.id.tb, fileFragment);
                break;
            case 2:

                myFragment = new MyFragment();
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
