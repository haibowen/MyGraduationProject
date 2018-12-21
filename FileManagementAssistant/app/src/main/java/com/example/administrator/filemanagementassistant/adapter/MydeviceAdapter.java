package com.example.administrator.filemanagementassistant.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.activity.MainActivity;

import java.util.List;

public class MydeviceAdapter extends RecyclerView.Adapter<MydeviceAdapter.ViewHolder> {
    private List<WifiP2pDevice> devicesList;
    private Context mContext;

  static   class ViewHolder extends RecyclerView.ViewHolder{

         TextView devicename;
         TextView deviceip;
         TextView devicestatus;
         CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            devicename=itemView.findViewById(R.id.device_name);
            deviceip=itemView.findViewById(R.id.device_ip);
            devicestatus=itemView.findViewById(R.id.device_status);

        }
    }


    public MydeviceAdapter(List<WifiP2pDevice> devicesList1){
        this.devicesList=devicesList1;


    }
    @NonNull
    @Override
    public MydeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

      if (mContext==null){
          mContext=viewGroup.getContext();
      }
       View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,viewGroup,false);

       return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.devicename.setText(devicesList.get(i).deviceName);
        viewHolder.deviceip.setText(devicesList.get(i).deviceAddress);
        viewHolder.devicestatus.setText(MainActivity.getDeviceStatus(devicesList.get(i).status));

    }


    @Override
    public int getItemCount() {
        return devicesList.size();
    }
}
