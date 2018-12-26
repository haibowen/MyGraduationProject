package com.example.administrator.filemanagementassistant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.activity.MainActivity;
import com.example.administrator.filemanagementassistant.bean.Historydata;

import java.util.List;

public class MyHistroyAdapter extends RecyclerView.Adapter<MyHistroyAdapter.ViewHolder> {


    private Context context;
    private List<Historydata> mlists;

    public  MyHistroyAdapter(List<Historydata> lists,Context mcontext){

        mlists=lists;
        context=mcontext;
    }

    static class  ViewHolder extends  RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.item_image);
            textView=itemView.findViewById(R.id.item_textview);


        }
    }

    @NonNull
    @Override
    public MyHistroyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context==null){
            context=viewGroup.getContext();

        }
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_history,viewGroup,false);
        final  ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion=holder.getAdapterPosition();
                Historydata historydata=mlists.get(postion);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("历史上的今天");
                builder.setMessage(historydata.getContent());
                builder.create().show();



            }
        });

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Historydata news=mlists.get(i);
        viewHolder.textView.setText(news.getTitle());
        Log.e("wenhaibo", "onBindViewHolder: "+news.getTitle() );
        Glide.with(context).load(news.getImage()).into(viewHolder.imageView);

    }



    @Override
    public int getItemCount() {
        return mlists.size();
    }
}


