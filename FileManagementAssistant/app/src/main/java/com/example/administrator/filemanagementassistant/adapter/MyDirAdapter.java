package com.example.administrator.filemanagementassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.bean.DirFile;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.List;

public class MyDirAdapter extends RecyclerView.Adapter<MyDirAdapter.ViewHolder> {

    private Context mcontext;
    private List<DirFile> mlist;

    public MyDirAdapter(List<DirFile> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       if (mcontext==null){

           mcontext=viewGroup.getContext();
       }
       View view= LayoutInflater.from(mcontext).inflate(R.layout.recycler_dir_item02,viewGroup,false);





        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        DirFile dirFile=mlist.get(i);
        viewHolder.textView.setText(dirFile.getName());
        viewHolder.imageView.setImageResource(R.drawable.fab_bg_mini);


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.image_dir);
            textView=itemView.findViewById(R.id.text_dir);

        }
    }
}
