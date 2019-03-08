package com.example.administrator.filemanagementassistant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Environment;
import android.os.Parcelable;
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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class MyDirAdapter extends RecyclerView.Adapter<MyDirAdapter.ViewHolder> {

    private Context mcontext;
    private List<DirFile> mlist;

    private MyDirAdapter myDirAdapter;
    private String path;

    public MyDirAdapter(List<DirFile> mlist) {
        this.mlist = mlist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mcontext == null) {

            mcontext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mcontext).inflate(R.layout.recycler_dir_item02, viewGroup, false);


        //myDirAdapter=new MyDirAdapter(mlist);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int postion = holder.getAdapterPosition();
                DirFile dirFile = mlist.get(postion);

                String a=Environment.getExternalStorageDirectory().getPath();
                path=a;

                path += "/" + dirFile.getName();


                Log.e("22222", "onClick: " + path);
                Intent intent=new Intent(mcontext,TaskActivity.class);
                intent.putExtra("path",path);

                mcontext.startActivity(intent);



                Log.e("45454545", "onClick: "+mlist.size() );



            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = holder.getAdapterPosition();
                DirFile dirFile = mlist.get(postion);

                String a=Environment.getExternalStorageDirectory().getPath();
                path=a;

                path += "/" + dirFile.getName();


                Log.e("22222", "onClick: " + path);
                Intent intent=new Intent(mcontext,TaskActivity.class);
                intent.putExtra("path",path);

                mcontext.startActivity(intent);



                Log.e("45454545", "onClick: "+mlist.size() );





            }
        });
        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = holder.getAdapterPosition();
                DirFile dirFile = mlist.get(postion);
                TaskActivity taskActivity = new TaskActivity();
                String a = Environment.getExternalStorageDirectory().getPath();
                 path = a;
                path +=  "/" + dirFile.getName();
                Log.e("00000000000000", "onClick: " + path);


                Intent intent=new Intent(mcontext,TaskActivity.class);

                intent.putExtra("path",path);

                mcontext.startActivity(intent);


                Log.e("45454545", "onClick: "+mlist.size() );



                //Toast.makeText(mcontext, "wobeidianjile ", Toast.LENGTH_SHORT).show();

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
        final DirFile dirFile = mlist.get(i);
        viewHolder.textView.setText(dirFile.getName());
        viewHolder.textView1.setText(simpleDateFormat.format(date));
        viewHolder.imageView.setImageResource(R.drawable.ic_folder_black_24dp);
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mcontext, v);

                popupMenu.getMenuInflater().inflate(R.menu.poup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.popup_add:

                                File file=new File(dirFile.getName());
                                if (!file.exists()){

                                    file.mkdir();

                                }



                                break;

                            case R.id.popup_delete:

                                //deleteDirs("/storage/emulated/0/Music");


                                break;
                            case R.id.popup_more:

                                File file1=new File(dirFile.getName());
                                if (!file1.exists()){

                                   // file1.

                                }


                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });


    }

    //wenhaibo add 20190219


    /**
     * 删除某个目录
     * @param path 要删除的目录路径
     * @return
     */
    private boolean deleteDirs(String path){

        File file = new File(path);
        if (!file.exists()){
            return true;
        }
        if (file.isDirectory()){
            File[] childs = file.listFiles();
            if (null == childs){
                return false;
            }
            boolean result = true;
            for (File child : childs){
                result = result && deleteDirs(child.getAbsolutePath());
            }

            try{
                boolean ret = file.delete();
                return result && ret;
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }

        }else {

            try {
                boolean ret = file.delete();
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
    }



//复制文件
    public static int copyFile(String fromFilePath, String toFilePath) {
        File fromFile = new File(fromFilePath);
        if (!fromFile.exists()) {
            return -1;
        }
        //获取子文件
        File[] fromFiles = fromFile.listFiles();
        File toFile = new File(toFilePath);
        if (!toFile.exists()) {
            toFile.mkdirs();
        }
        for (int i = 0; i < fromFiles.length; i++) {
            if (fromFiles[i].isDirectory()) {
                // 子文件是目录，循环
                copyFile(fromFiles[i].getPath(), toFilePath + "/" + fromFiles[i].getName());
            } else {
                //复制文件
                copyDirFile(fromFiles[i].getPath(), toFilePath + "/" + fromFiles[i].getName());
            }
        }
        return 0;
    }

    //复制目录
    public static int copyDirFile(String fromFilePath, String toFilePath) {
        try {
            InputStream inStream = new FileInputStream(fromFilePath);
            OutputStream outStream = new FileOutputStream(toFilePath);
            byte[] bytes = new byte[1024];
            int i = 0;
            while ((i = inStream.read(bytes)) > 0) {
                outStream.write(bytes, 0, i);
            }
            inStream.close();
            outStream.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    //wenhaibo add 20190219





    //
    public int getWidth() {

        DisplayMetrics displayMetrics = mcontext.getResources().getDisplayMetrics();


        int width = displayMetrics.widthPixels;
        int truewidth = width / 3 * 2;
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
            cardView = (CardView) itemView;
            imageView = itemView.findViewById(R.id.image_dir);

            textView = itemView.findViewById(R.id.text_dir);
            //  textView.setWidth(getWidth());
            textView1 = itemView.findViewById(R.id.textview_time);
            imageButton = itemView.findViewById(R.id.imagebutton_dir);


        }
    }
}
