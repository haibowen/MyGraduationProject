package com.example.administrator.filemanagementassistant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.activity.TaskActivity;
import com.example.administrator.filemanagementassistant.bean.DirFile;
import com.example.administrator.filemanagementassistant.util.MarqueeTextView;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyDirAdapter extends RecyclerView.Adapter<MyDirAdapter.ViewHolder> {

    private Context mcontext;
    private List<DirFile> mlist;

    private MyDirAdapter myDirAdapter;

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


       //myDirAdapter=new MyDirAdapter(mlist);
       final  ViewHolder holder=new ViewHolder(view);
       holder.textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int postion =holder.getAdapterPosition();
               DirFile dirFile=mlist.get(postion);
               TaskActivity taskActivity=new TaskActivity();
               String a=Environment.getExternalStorageDirectory().getPath();
               String path="";
               path+=a+"/"+dirFile.getName();
               Log.e("22222", "onClick: "+path );

               taskActivity.search(new File(path));
             //  myDirAdapter.notifyDataSetChanged();
               Toast.makeText(mcontext,"wobeidianjile ",Toast.LENGTH_SHORT).show();

           }
       });
       holder.textView1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int postion =holder.getAdapterPosition();
               DirFile dirFile=mlist.get(postion);
               //Toast.makeText(mcontext,"wobeidianjile ",Toast.LENGTH_SHORT).show();

           }
       });

       
          
       






       // return new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        DirFile dirFile=mlist.get(i);
        viewHolder.textView.setText(dirFile.getName());
        viewHolder.textView1.setText(simpleDateFormat.format(date));
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

    //
    public int getWidth(){

        DisplayMetrics displayMetrics= mcontext.getResources().getDisplayMetrics();


        int width=displayMetrics.widthPixels;
        int truewidth=width/3*2;
        return truewidth;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        ImageView imageView;

        TextView textView1;
        MarqueeTextView textView;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.image_dir);

            textView=itemView.findViewById(R.id.text_dir);
          //  textView.setWidth(getWidth());
            textView1=itemView.findViewById(R.id.textview_time);
           imageButton=itemView.findViewById(R.id.imagebutton_dir);


        }
    }
}
