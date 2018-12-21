package com.example.administrator.filemanagementassistant.activity;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import butterknife.BindView;
import butterknife.ButterKnife;
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

import static com.example.administrator.filemanagementassistant.activity.MainActivity.getDeviceStatus;

public class SendActivity extends AppCompatActivity implements DirectActionListener, View.OnClickListener {

    @BindView(R.id.toolbar_send)
    public Toolbar toolbar;

    @BindView(R.id.text_device)
    public TextView textView;

    @BindView(R.id.text_address)
    public TextView textView1;

    @BindView(R.id.text_status)
    public TextView textView2;

    @BindView(R.id.bt_disconnect)
    public Button button;

    @BindView(R.id.bt_find)
    public Button button1;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;


    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiServerService wifiServerService;
    private WifiP2pDevice wifiP2pDevice;
    private boolean isWifiEnable=false;
    private WifiP2pInfo wifiP2pInfo;

    private List<WifiP2pDevice> mdevicelist;
    private MydeviceAdapter mydeviceAdapter;

    private boolean WifiEnable=false;

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
        mydeviceAdapter = new MydeviceAdapter(mdevicelist);
        recyclerView.setAdapter(mydeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){



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
        getMenuInflater().inflate(R.menu.send_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;

            case R.id.search:
                if (!isWifiEnable){
                    AlertDialog.Builder builder=new AlertDialog.Builder(SendActivity.this);
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
                Toast.makeText(SendActivity.this,"正在搜索附近的设备",Toast.LENGTH_SHORT).show();

                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SendActivity.this,"成功",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(SendActivity.this,"失败",Toast.LENGTH_SHORT).show();


                    }
                });

                break;

        }


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {

                    String path = getPath(this, uri);
                    if (path != null) {

                        File file = new File(path);
                        if (file.exists() && wifiP2pInfo != null) {
                            FileTransfer fileTransfer = new FileTransfer(file.getPath(), file.length());
                            new WifiClientTask(this, fileTransfer).execute(wifiP2pInfo.groupOwnerAddress.getHostAddress());

                        }
                    }

                }

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public void connect() {

        WifiP2pConfig config=new WifiP2pConfig();
        if (config.deviceAddress!=null&&mdevicelist!=null){
            config.deviceAddress=wifiP2pDevice.deviceAddress;
            config.wps.setup= WpsInfo.PBC;
            Toast.makeText(this,"正在连接"+wifiP2pDevice.deviceName,Toast.LENGTH_SHORT).show();
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(SendActivity.this,"连接失败",Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

    private String getPath(Context context,Uri uri){

        if ("context".equalsIgnoreCase(uri.getScheme())){

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
     *
     * Directionlistener接口中的回调方法的实现
     * @param enabled
     */

    @Override
    public void wifiP2pEnabled(boolean enabled) {
        isWifiEnable=enabled;

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {

        textView.setText(wifiP2pDevice.deviceName);
        textView1.setText(wifiP2pDevice.deviceAddress);
        textView2.setText(getDeviceStatus(wifiP2pDevice.status));

    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {


        Log.e("01010", "onPeersAvailable: "+wifiP2pDeviceList.toString() );

        this.mdevicelist.clear();
        this.mdevicelist.addAll(mdevicelist);
        mydeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChannelDisconnected() {

    }


}
