package com.example.administrator.filemanagementassistant.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MydeviceAdapter;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.broadcast.DirectBroadCastReceiver;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;
import com.example.administrator.filemanagementassistant.client.WifiClientTask;
import com.example.administrator.filemanagementassistant.service.WifiServerService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendActivity extends AppCompatActivity implements DirectActionListener, View.OnClickListener {

    @BindView(R.id.toolbar_send)
    public Toolbar toolbar;

    @BindView(R.id.text_device)
    public TextView textView;

    @BindView(R.id.text_address)
    public TextView textView1;

    @BindView(R.id.text_status)
    public TextView textView2;

    @BindView(R.id.text_devicetarget)
    public TextView textView3;
    @BindView(R.id.text_addresstarget)
    public TextView textView4;
    @BindView(R.id.text_statustarget)
    public TextView textView5;

    @BindView(R.id.bt_disconnect)
    public Button button;

    @BindView(R.id.bt_find)
    public Button button1;

    @BindView(R.id.recycler_view_send)
    public RecyclerView recyclerView;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiServerService wifiServerService;
    private WifiP2pDevice wifiP2pDevice;
    private boolean isWifiEnable = false;
    private WifiP2pInfo wifiP2pInfo;
    private List<WifiP2pDevice> mdevicelist;
    private MydeviceAdapter mydeviceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), this);
        broadcastReceiver = new DirectBroadCastReceiver(wifiP2pManager, channel, (DirectActionListener) this);
        registerReceiver(broadcastReceiver, DirectBroadCastReceiver.getIntentFilter());
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        mdevicelist = new ArrayList<>();
        mydeviceAdapter = new MydeviceAdapter(mdevicelist,this);
        //wenhaibo add 20190213[start]
        mydeviceAdapter.setClickListener(new MydeviceAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                wifiP2pDevice = mdevicelist.get(position);
                //showToast(wifiP2pDeviceList.get(position).deviceName);
                connect();
            }
        });
        //wenhaibo add 20190213[end]
        recyclerView.setAdapter(mydeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_disconnect:
                wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        textView2.setText(null);
                        button.setEnabled(false);
                        button1.setEnabled(false);
                    }
                    @Override
                    public void onFailure(int reason) {
                    }
                });
                break;
            case R.id.bt_find:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                if (!isWifiEnable) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                    builder.setMessage("wifi还没有打开,请打开wifi");
                    builder.setCancelable(false);
                    builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();
                    return true;
                }
                Toast.makeText(SendActivity.this, "正在搜索附近的设备", Toast.LENGTH_SHORT).show();
                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SendActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(SendActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.e("444444", "onActivityResult: "+"wozhixingle" );
                Uri uri = data.getData();
                Log.e("44444", "onActivityResult: "+uri);
                if (uri != null) {
                    String path = getPath(this, uri);
                    Log.e("444448888", "onActivityResult: "+path );
                    if (path != null) {
                        File file = new File(path);
                        if (file.exists() && wifiP2pInfo != null) {
                            FileTransfer fileTransfer = new FileTransfer(file.getPath(), file.length());
                            Log.e("44444", "onActivityResult: "+fileTransfer );
                            new WifiClientTask(this, fileTransfer).execute(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                        }
                    }
                }
                else {
                    Toast.makeText(SendActivity.this, "没有找dao到路径", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public  void connect() {
        WifiP2pConfig config = new WifiP2pConfig();
       // Log.e("33333333333", "connect: "+wifiP2pDevice.deviceAddress );
        if (config.deviceAddress != null && wifiP2pDevice != null) {
            //config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            Log.e("hahahhhahhha", "connect: "+wifiP2pDevice.deviceAddress );
            config.wps.setup = WpsInfo.PBC;
            Toast.makeText(this, "正在连接" + wifiP2pDevice.deviceName, Toast.LENGTH_SHORT).show();
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onFailure(int reason) {
                    Toast.makeText(SendActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "没有设备信息", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));
                    cursor.close();
                    return data;
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Directionlistener接口中的回调方法的实现
     *
     * @param enabled
     */

    @Override
    public void wifiP2pEnabled(boolean enabled) {
        isWifiEnable = enabled;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        //textView1.setText("设备地址:"+"  "+wifiP2pInfo.groupOwnerAddress.getHostAddress());
        mdevicelist.clear();
        mydeviceAdapter.notifyDataSetChanged();
        button.setEnabled(true);
        button1.setEnabled(true);
        StringBuilder stringBuilder = new StringBuilder();
        if (wifiP2pDevice != null) {
            textView3.setText("设备名称:" + wifiP2pDevice.deviceName);
            textView4.setText("设备地址:" + wifiP2pDevice.deviceAddress);
            textView5.setText("设备状态:" + stringBuilder.append(wifiP2pInfo.isGroupOwner ? "是群主" : "非群主"));
        }
        if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
            this.wifiP2pInfo = wifiP2pInfo;
        }
    }

    @Override
    public void onDisconnection() {
        button1.setEnabled(false);
        button.setEnabled(false);
        Toast.makeText(SendActivity.this, "已断开连接", Toast.LENGTH_SHORT).show();
        mdevicelist.clear();
        mydeviceAdapter.notifyDataSetChanged();
        textView2.setText(null);
        this.wifiP2pInfo = null;
    }

    @Override
    public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
        textView.setText("设备名称:" + "  " + wifiP2pDevice.deviceName);
        textView1.setText("设备地址:" + "  " + wifiP2pDevice.deviceAddress);
        textView2.setText("设备状态:" + "  " + getDeviceStatus(wifiP2pDevice.status));
    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
        Log.e("01010", "onPeersAvailable: " + wifiP2pDeviceList.toString());
        this.mdevicelist.clear();
        this.mdevicelist.addAll(wifiP2pDeviceList);
        Log.e("77777", "onPeersAvailable: "+mdevicelist.toString() );
        mydeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChannelDisconnected() {

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
