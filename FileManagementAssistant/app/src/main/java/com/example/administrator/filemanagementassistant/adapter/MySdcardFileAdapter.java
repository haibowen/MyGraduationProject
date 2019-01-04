package com.example.administrator.filemanagementassistant.adapter;

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
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_file,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
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
