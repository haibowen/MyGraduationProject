package com.example.administrator.filemanagementassistant.activity;

import android.app.ProgressDialog;
import android.content.*;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.broadcast.DirectBroadCastReceiver;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;
import com.example.administrator.filemanagementassistant.service.WifiServerService;

import java.io.File;
import java.util.Collection;
import java.util.Locale;

public class ReciveActivity extends AppCompatActivity implements DirectActionListener, View.OnClickListener {

    @BindView(R.id.toolbar_receive)
    public Toolbar toolbar;

    @BindView(R.id.bt_create)
    public Button button;


    @BindView(R.id.bt_disrecreate)
    public Button button1;

    @BindView(R.id.text_view_receive)
    public TextView textView0;
    @BindView(R.id.text_device_receive)
    public TextView textView1;
    @BindView(R.id.text_address_receive)
    public TextView textView2;
    @BindView(R.id.text_status_receive)
    public TextView textView3;



    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiServerService wifiServerService;
    private boolean WifiEnable = false;

    private ProgressDialog progressDialog;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            WifiServerService.MyBinder binder = (WifiServerService.MyBinder) service;
            wifiServerService = binder.getService();
            wifiServerService.setOnprogressChangListener(progressChangListener);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            wifiServerService = null;
            bindService();

        }
    };
    private WifiServerService.OnprogressChangListener progressChangListener = new WifiServerService.OnprogressChangListener() {
        @Override
        public void onProgressChanged(final FileTransfer fileTransfer, final int progress) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage("文件名" + new File(fileTransfer.getFilePath()).getName());

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
                    if (file != null && file.exists()) {

                        openFile(file.getPath());
                    }
                }
            });

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        ButterKnife.bind(this);//这句必须要有，否则控件找不到。
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        channel = wifiP2pManager.initialize(this, getMainLooper(), this);
        broadcastReceiver = new DirectBroadCastReceiver(wifiP2pManager, channel, (DirectActionListener) this);
        registerReceiver(broadcastReceiver, DirectBroadCastReceiver.getIntentFilter());
        bindService();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在接收文件");
        progressDialog.setMax(100);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);

    }

    //wenhaibo add 20190214[start]




    //wenhaibo add 20190214[end]

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (wifiServerService != null) {

            wifiServerService.setOnprogressChangListener(null);
            unbindService(serviceConnection);

        }
        unregisterReceiver(broadcastReceiver);

        removeGroup();

        stopService(new Intent(this, WifiServerService.class));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                break;
        }

        return true;
    }


    public void CreateGroup() {
        Toast.makeText(ReciveActivity.this, "正在创建群组", Toast.LENGTH_SHORT).show();
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ReciveActivity.this, "创建成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(ReciveActivity.this, "创建失败", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void removeGroup() {

        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ReciveActivity.this, "解除成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(ReciveActivity.this, "解除失败", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void bindService() {

        Intent intent = new Intent(ReciveActivity.this, WifiServerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }


    public void openFile(String filePath) {

        String ext = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);


        try {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mime = mimeTypeMap.getMimeTypeFromExtension(ext.substring(1));
            //String mime = mimeTypeMap.getExtensionFromMimeType(ext.substring(1));
            Log.e("1111111", "openFile: "+mime );
            mime = TextUtils.isEmpty(mime) ? "" : mime;
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), mime);
            Log.e("11111", "openFile: "+Uri.fromFile(new File(filePath)) );
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("222222", "openFile: " );
            Toast.makeText(ReciveActivity.this, "文件打开异常", Toast.LENGTH_SHORT).show();


        }

    }


    /**
     * 接口中待实现的方法
     *
     * @param enabled
     */

    @Override
    public void wifiP2pEnabled(boolean enabled) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        Log.e("55555", "onConnectionInfoAvailable: "+wifiP2pInfo.isGroupOwner );
        Log.e("5555", "onConnectionInfoAvailable: "+wifiP2pInfo.groupOwnerAddress.getHostAddress() );
        Log.e("55555", "onConnectionInfoAvailable: "+wifiP2pInfo.groupFormed );
        //textView2.setText("设备地址:"+"  "+wifiP2pInfo.groupOwnerAddress.getHostAddress());
        //textView3.setText("是否群组:"+"  "+wifiP2pInfo.isGroupOwner);

        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            if (wifiServerService != null) {
                startService(new Intent(this, WifiServerService.class));
            }
        }


    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {

        textView1.setText("设备名称:"+"  "+wifiP2pDevice.deviceName);

        textView2.setText("设备地址:"+"  "+wifiP2pDevice.deviceAddress);
        textView3.setText("设备状态:"+"  "+getDeviceStatus(wifiP2pDevice.status));


        Log.e("0000000", "onSelfDeviceAvailable: "+wifiP2pDevice.deviceName);

    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {





    }

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_create:
                CreateGroup();

                break;

            case R.id.bt_disrecreate:
                removeGroup();

                break;

            default:
                break;

        }

    }

    public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "可用";
            case WifiP2pDevice.INVITED:
                return "邀请中";
            case WifiP2pDevice.CONNECTED:
                return "已连接";
            case WifiP2pDevice.FAILED:
                return "失败的";
            case WifiP2pDevice.UNAVAILABLE:
                return "不可用";
            default:
                return "未知";
        }
    }
}
