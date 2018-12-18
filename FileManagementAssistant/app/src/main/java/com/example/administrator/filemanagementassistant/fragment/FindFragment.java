package com.example.administrator.filemanagementassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;
import static com.example.administrator.filemanagementassistant.activity.MainActivity.getDeviceStatus;

public class FindFragment extends Fragment implements DirectActionListener, View.OnClickListener {
    private View mview;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private List<WifiP2pDevice> mdevicelist;
    private MydeviceAdapter mydeviceAdapter;
    private BroadcastReceiver broadcastReceiver;
    private WifiP2pDevice wifiP2pDevice;
    private boolean wifiP2pEnabled = false;
    private WifiP2pInfo wifiP2pInfo;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.find_fragment, null);

        ButterKnife.bind(this, mview);

        //
        wifiP2pManager = (WifiP2pManager) getActivity().getSystemService(getActivity().WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(getActivity(), Looper.getMainLooper(), this);
        broadcastReceiver = new DirectBroadCastReceiver(wifiP2pManager, channel, this);

        getActivity().registerReceiver(broadcastReceiver, DirectBroadCastReceiver.getIntentFilter());

        //按钮点击事件注册
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        //设备列表
        mdevicelist = new ArrayList<>();
        mydeviceAdapter = new MydeviceAdapter(mdevicelist);
        recyclerView.setAdapter(mydeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return mview;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {

                    String path = getPath(getActivity(), uri);
                    if (path != null) {

                        File file = new File(path);
                        if (file.exists() && wifiP2pInfo != null) {
                            FileTransfer fileTransfer = new FileTransfer(file.getPath(), file.length());
                            new WifiClientTask(getActivity(), fileTransfer).execute(wifiP2pInfo.groupOwnerAddress.getHostAddress());

                        }
                    }

                }

            }
        }
    }

    public void connect() {

        WifiP2pConfig  config=new WifiP2pConfig();
        if (config.deviceAddress!=null&&mdevicelist!=null){
            config.deviceAddress=wifiP2pDevice.deviceAddress;
            config.wps.setup= WpsInfo.PBC;
            Toast.makeText(getActivity(),"正在连接"+wifiP2pDevice.deviceName,Toast.LENGTH_SHORT).show();
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getActivity(),"连接失败",Toast.LENGTH_SHORT).show();


                }
            });
        }
    }


    /**
     * 实现接口中的方法
     *
     * @param enabled
     */

    @Override
    public void wifiP2pEnabled(boolean enabled) {
        wifiP2pEnabled = enabled;


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

        this.mdevicelist.clear();
        this.mdevicelist.addAll(mdevicelist);
        mydeviceAdapter.notifyDataSetChanged();

    }

    @Override
    public void onChannelDisconnected() {

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
}
