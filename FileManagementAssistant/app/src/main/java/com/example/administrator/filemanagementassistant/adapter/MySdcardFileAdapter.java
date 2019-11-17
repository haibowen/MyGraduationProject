package com.example.administrator.filemanagementassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.ListPreloader;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.bean.SdcardFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import io.haydar.filescanner.FileInfo;

import java.util.List;

public class MySdcardFileAdapter extends RecyclerView.Adapter<MySdcardFileAdapter.ViewHolder> {

    private List<FileInfo> sdcardFileslist;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_file);
            textView=itemView.findViewById(R.id.text_file);
        }
    }
    public MySdcardFileAdapter(List<FileInfo> sdcardFileslist) {
        this.sdcardFileslist = sdcardFileslist;
    }

    @NonNull
    @Override
    public MySdcardFileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context==null){
            context=viewGroup.getContext();
        }
        final View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_file,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion=viewHolder.getAdapterPosition();
                FileInfo sdcardinfo=sdcardFileslist.get(postion);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(Uri.parse(sdcardinfo.getFilePath()), "image/*");
                context.startActivity(intent);
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion=viewHolder.getAdapterPosition();
                FileInfo sdcardinfo=sdcardFileslist.get(postion);
            }
        });
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MySdcardFileAdapter.ViewHolder viewHolder, int i) {
        FileInfo sdcardFile=sdcardFileslist.get(i);
        viewHolder.imageView.setImageResource(R.drawable.fab_bg_mini);
        viewHolder.textView.setText(sdcardFile.getFilePath());
    }
    @Override
    public int getItemCount() {
        return sdcardFileslist.size();
    }
}
