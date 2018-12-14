package com.example.administrator.filemanagementassistant.activity;

import android.app.ProgressDialog;
import android.content.*;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.broadcast.DirectBroadCastReceiver;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;
import com.example.administrator.filemanagementassistant.fragment.FileFragment;
import com.example.administrator.filemanagementassistant.fragment.FindFragment;
import com.example.administrator.filemanagementassistant.fragment.MyFragment;
import com.example.administrator.filemanagementassistant.service.WifiServerService;

import java.io.File;
import java.util.Collection;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,DirectActionListener{

    @BindView(R.id.btnagationbar)
    public BottomNavigationBar bottomNavigationBar;

    @BindView(R.id.drawerlayout)
    public DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiServerService wifiServerService;
    private ProgressDialog progressDialog;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            WifiServerService.MyBinder binder= (WifiServerService.MyBinder) service;
            wifiServerService=binder.getService();
            wifiServerService.setOnprogressChangListener( progressChangListener);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            wifiServerService=null;
           bindService();

        }
    };
    private WifiServerService.OnprogressChangListener progressChangListener=new WifiServerService.OnprogressChangListener() {
        @Override
        public void onProgressChanged(final FileTransfer fileTransfer, final int progress) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage("文件名"+new File(fileTransfer.getFilePath()).getName());

                    progressDialog.setProgress(progress);
                    progressDialog.show();



                }
            });
        }

        @Override
        public void onTransferFinshed(final File file) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                    if (file!=null&&file.exists()){

                        openFile(file.getPath());
                    }
                }
            });

        }
    };

    private FileFragment fileFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private  int lastselection=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        ButterKnife.bind(this);

        wifiP2pManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        channel=wifiP2pManager.initialize(this,getMainLooper(),this);
        broadcastReceiver=new DirectBroadCastReceiver(wifiP2pManager,channel, (DirectActionListener) this);
        registerReceiver(broadcastReceiver,DirectBroadCastReceiver.getIntentFilter());
        bindService();
        progressDialog =new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在接收文件");
        progressDialog.setMax(100);


        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
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
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp,"Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_insert_drive_file_black_24dp,"File"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp,"My"))
                .setFirstSelectedPosition(lastselection)
                .initialise();
        SetDefaultFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);

                break;

            case R.id.search:

                break;

            case R.id.openWifi:

                break;

            case R.id.creat:

                break;

            case R.id.discreat:

                break;
                default:
                    break;
        }
        return true;
    }

    /**
     *
     * 设置默认显示的fragment
     */
    public  void SetDefaultFragment(){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        findFragment=new FindFragment();
        fragmentTransaction.replace(R.id.tb,findFragment);
        fragmentTransaction.commit();

    }

    /**
     *
     *底部导航栏的显示和切换
     * @param position
     */

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager=this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (position){
            case 0:

                findFragment=new FindFragment();
                fragmentTransaction.replace(R.id.tb,findFragment);
                break;
            case 1:

                fileFragment=new FileFragment();
                fragmentTransaction.replace(R.id.tb,fileFragment);
                break;
            case 2:

                myFragment=new MyFragment();
                fragmentTransaction.replace(R.id.tb,myFragment);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (wifiServerService!=null){

            wifiServerService.setOnprogressChangListener(null);
            unbindService(serviceConnection);

        }
        unregisterReceiver(broadcastReceiver);

        stopService(new Intent(this,WifiServerService.class));


    }

    /**
     *
     * 实现接口重写的方法
     * @param enabled
     */

    @Override
    public void wifiP2pEnabled(boolean enabled) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {

    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {

    }

    @Override
    public void onChannelDisconnected() {

    }

    public void  CreateGroup(View view){
        Toast.makeText(MainActivity.this,"正在创建群组",Toast.LENGTH_SHORT).show();
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"创建成功",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this,"创建失败",Toast.LENGTH_SHORT).show();

            }
        });





    }
    public  void  removeGroup(View view){
        removeGroup();


    }

    public  void  removeGroup(){

        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"解除成功",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this,"解除失败",Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void  bindService(){

        Intent intent=new Intent(MainActivity.this,WifiServerService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);

    }


    public  void  openFile(String filePath){

        String ext=filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);


        try {
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            String mime=mimeTypeMap.getExtensionFromMimeType(ext.substring(1));
            mime= TextUtils.isEmpty(mime)?"":mime;
            Intent intent=new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)),mime);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(MainActivity.this,"文件打开异常",Toast.LENGTH_SHORT).show();


        }







    }



}
