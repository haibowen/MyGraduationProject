package com.example.administrator.filemanagementassistant.adapter;

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


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView devicename;
        private TextView deviceip;
        private TextView devicestatus;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            devicename=itemView.findViewById(R.id.device_name);
            deviceip=itemView.findViewById(R.id.device_ip);
            devicestatus=itemView.findViewById(R.id.device_status);

        }
    }

    private List<WifiP2pDevice> devicesList;
    public MydeviceAdapter(List<WifiP2pDevice> devicesList1){
        this.devicesList=devicesList1;


    }
    @NonNull
    @Override
    public MydeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item,viewGroup,false);

       final ViewHolder viewHolder=new ViewHolder(view);
       viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int postion=viewHolder.getAdapterPosition();

           }
       });



        return null;
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
