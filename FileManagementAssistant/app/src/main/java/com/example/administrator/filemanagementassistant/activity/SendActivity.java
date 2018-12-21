package com.example.administrator.filemanagementassistant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.broadcast.DirectBroadCastReceiver;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;
import com.example.administrator.filemanagementassistant.service.WifiServerService;

import java.util.Collection;

public class SendActivity extends AppCompatActivity implements DirectActionListener {

    @BindView(R.id.toolbar_send)
    public Toolbar toolbar;


    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private WifiServerService wifiServerService;
    private boolean isWifiEnable=false;

    private boolean WifiEnable=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        channel = wifiP2pManager.initialize(this, getMainLooper(), this);
        broadcastReceiver = new DirectBroadCastReceiver(wifiP2pManager, channel, (DirectActionListener) this);
        registerReceiver(broadcastReceiver, DirectBroadCastReceiver.getIntentFilter());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

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

    }

    @Override
    public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {


        Log.e("01010", "onPeersAvailable: "+wifiP2pDeviceList.toString() );
    }

    @Override
    public void onChannelDisconnected() {

    }
}
