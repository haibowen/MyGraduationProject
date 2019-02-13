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
import android.widget.Toast;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.activity.MainActivity;
import com.example.administrator.filemanagementassistant.activity.SendActivity;

import java.util.List;

public class MydeviceAdapter extends RecyclerView.Adapter<MydeviceAdapter.ViewHolder> {
    private List<WifiP2pDevice> devicesList;
    private Context mContext;
    private SendActivity senContext;

    private OnClickListener clickListener;

    public interface OnClickListener {

        void onItemClick(int position);

    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

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


    public MydeviceAdapter(List<WifiP2pDevice> devicesList1,SendActivity asenContext){
        this.devicesList=devicesList1;
        this.senContext=asenContext;


    }
    @NonNull
    @Override
    public MydeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

      if (mContext==null){
          mContext=viewGroup.getContext();
      }
       View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,viewGroup,false);


      final  ViewHolder viewHolder=new ViewHolder(view);
      viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (clickListener!=null){
                  clickListener.onItemClick((Integer) v.getTag());
              }
              //int postion=viewHolder.getAdapterPosition();
              //devicesList.get(postion+1);
              //Toast.makeText(mContext, "正在连接"+devicesList.get(postion).deviceName, Toast.LENGTH_SHORT).show();
              //senContext.connect(devicesList.get(postion+1));
             // senContext.connect();

          }
      });

       return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.devicename.setText("设备名称:"+"  "+devicesList.get(i).deviceName);
        viewHolder.deviceip.setText("设备地址:"+"  "+devicesList.get(i).deviceAddress);
        viewHolder.devicestatus.setText("设备状态:"+"  "+MainActivity.getDeviceStatus(devicesList.get(i).status));

        viewHolder.itemView.setTag(i);

    }


    @Override
    public int getItemCount() {
        return devicesList.size();
    }
}
