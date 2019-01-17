package com.example.administrator.filemanagementassistant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
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

         View   view= LayoutInflater.from(mcontext).inflate(R.layout.recycler_dir_item02,viewGroup,false);


       
          
       






        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        DirFile dirFile=mlist.get(i);
        viewHolder.textView.setText(dirFile.getName());
        viewHolder.imageView.setImageResource(R.drawable.ic_folder_black_24dp);
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                PopupMenu  popupMenu=new PopupMenu(mcontext,v);

                popupMenu.getMenuInflater().inflate(R.menu.poup,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){

                            case R.id.popup_add:

                                break;

                            case R.id.popup_delete:

                                break;
                            case R.id.popup_more:

                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        ImageView imageView;

        TextView textView;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.image_dir);
            textView=itemView.findViewById(R.id.text_dir);
           imageButton=itemView.findViewById(R.id.imagebutton_dir);


        }
    }
}
