package com.example.administrator.filemanagementassistant.broadcast;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;

import java.util.ArrayList;
import java.util.List;

public class DirectBroadCastReceiver  extends BroadcastReceiver {

    private WifiP2pManager mwifiP2pManager;
    private WifiP2pManager.Channel  mchannel;

    private DirectActionListener mdirectActionListener;

    public DirectBroadCastReceiver(WifiP2pManager wifiP2pManager,WifiP2pManager.Channel channel,DirectActionListener directActionListener){

        mwifiP2pManager=wifiP2pManager;
        mchannel=channel;
        mdirectActionListener=directActionListener;



    }

    public static IntentFilter getIntentFilter(){

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        return intentFilter;


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.isEmpty(intent.getAction())){

            switch (intent.getAction()){
                //用于指示wifip2p 是否可用

                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:

                    int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
                    if (state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){

                        mdirectActionListener.wifiP2pEnabled(true);
                    }else {
                        mdirectActionListener.wifiP2pEnabled(false);
                        List<WifiP2pDevice> listdevices=new ArrayList<>();
                        mdirectActionListener.onPeersAvailable(listdevices);
                    }
                    break;

                    //对等节点列表发生变化
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    mwifiP2pManager.requestPeers(mchannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            mdirectActionListener.onPeersAvailable(peers.getDeviceList());

                        }
                    });
                    break;
                    //wifiP2p的连接状态发生了变化
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:

                    NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()){

                        mwifiP2pManager.requestConnectionInfo(mchannel, new WifiP2pManager.ConnectionInfoListener() {
                            @Override
                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                mdirectActionListener.onConnectionInfoAvailable(info);

                            }
                        });
                    }else {
                        mdirectActionListener.onDisconnection();
                    }
                    //本设备的设备信息发生了变化
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:

                    mdirectActionListener.onSelfDeviceAvailable((WifiP2pDevice)intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

                    break;





            }
        }

    }
}
