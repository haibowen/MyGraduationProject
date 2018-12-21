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
import android.util.Log;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.callback.DirectActionListener;

import java.util.ArrayList;
import java.util.List;

public class DirectBroadCastReceiver  extends BroadcastReceiver {

    private static final String TAG = "DirectBroadcastReceiver";

    private WifiP2pManager mWifiP2pManager;

    private WifiP2pManager.Channel mChannel;
    /**是一个自定义监听 回调接口**/
    private DirectActionListener mWifiDirectActionListener;

    public DirectBroadCastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, DirectActionListener directActionListener) {
        mWifiP2pManager = wifiP2pManager;
        mChannel = channel;
        mWifiDirectActionListener = directActionListener;
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "接收到广播： " + intent.getAction());
        if (!TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                /**用于指示 Wifi P2P 是否可用**/
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        mWifiDirectActionListener.wifiP2pEnabled(true);
                    } else {
                        mWifiDirectActionListener.wifiP2pEnabled(false);
                        List<WifiP2pDevice> wifiP2pDeviceList = new ArrayList<>();
                        mWifiDirectActionListener.onPeersAvailable(wifiP2pDeviceList);
                    }
                    break;

                /**表明可用的对等点的列表发生了改变 **/
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            mWifiDirectActionListener.onPeersAvailable(peers.getDeviceList());
                        }
                    });
                    break;

                /**表示Wi-Fi对等网络的连接状态发生了改变 **/
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                            @Override
                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                if (info != null) {
                                    Log.i(TAG, "确实获取到WiFip2pinfo");
                                    //！！！这里很关键，只有真正的走到这里，才是真正的建立了P2P连接。拿到了准备建立Socket通道的必要信息。
                                } else {
                                    Log.i(TAG, "WiFip2pinfo 为null");
                                }
                                mWifiDirectActionListener.onConnectionInfoAvailable(info);
                            }
                        });
                        Log.i(TAG, "已连接P2P");
                    } else {
                        mWifiDirectActionListener.onDisconnection();
                        Log.i(TAG, "与P2P设备已断开连接");
                    }
                    break;

                /**表示该设备的配置信息发生了改变**/
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    mWifiDirectActionListener.onSelfDeviceAvailable((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
                    break;
                default:

                    break;
            }
        }
    }

}
