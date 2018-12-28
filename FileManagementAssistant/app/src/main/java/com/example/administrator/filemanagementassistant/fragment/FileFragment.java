package com.example.administrator.filemanagementassistant.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MySdcardFileAdapter;
import com.example.administrator.filemanagementassistant.bean.SdcardFile;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class FileFragment extends Fragment {

    private String mResult = new String();
    private String[] mFileList = null;

    private List<String> mydata=new ArrayList<String>();
    private List<SdcardFile> myfile=new ArrayList<SdcardFile>();
    private View mview;

    @BindView(R.id.display)
    public TextView textView;

    @BindView(R.id.bt_fabMenu)
    public FloatingActionsMenu  floatingActionsMenu;

    @BindView(R.id.recycler_show_file)
    public RecyclerView recyclerView;



   @OnClick({R.id.bt_fabMenu,R.id.bt_image,R.id.bt_audio,R.id.bt_file,R.id.bt_video})
   public void onClick(View view){
       switch (view.getId()){
           case R.id.bt_fabMenu:
              // textView.setVisibility(View.VISIBLE);
              // Toast.makeText(getActivity(), "我被点击了", Toast.LENGTH_SHORT).show();




               break;

           case R.id.bt_image:
               Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
               intent1.putExtra(IS_NEED_CAMERA, true);
               intent1.putExtra(Constant.MAX_NUMBER, 9);
               startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);


               break;

           case R.id.bt_file:

               Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
               intent4.putExtra(Constant.MAX_NUMBER, 9);
               intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
               startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);

               break;
           case R.id.bt_video:
               Intent  intent2 = new Intent(getActivity(), VideoPickActivity.class);
               intent2.putExtra(IS_NEED_CAMERA, true);
               intent2.putExtra(Constant.MAX_NUMBER, 9);
               startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
               break;
           case R.id.bt_audio:
               Intent intent3 = new Intent(getActivity(), AudioPickActivity.class);
               intent3.putExtra(IS_NEED_RECORDER, true);
               intent3.putExtra(Constant.MAX_NUMBER, 9);
               startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);


               break;

       }


   }

   //悬浮按钮菜单的点击事件

    /**
     *
     * 有问题待修复
     */

    public  void display(){

       floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
           @Override
           public void onMenuExpanded() {
               //textView.setVisibility(View.VISIBLE);
               //Toast.makeText(getActivity(), "我被点击了", Toast.LENGTH_SHORT).show();

           }

           @Override
           public void onMenuCollapsed() {
              // textView.setVisibility(View.GONE);

           }
       });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.file_fragment,null);
        ButterKnife.bind(this,mview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MySdcardFileAdapter adapter=new MySdcardFileAdapter(myfile);
        recyclerView.setAdapter(adapter);


        //权限申请

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {

            search();
        }



        return mview;



    }


    public void search() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File path = Environment.getExternalStorageDirectory();
                Log.e("333333", "onCreate: " + path);


                File flist = new File(String.valueOf(Environment.getRootDirectory()));
                mFileList = flist.list();

                for (String str : mFileList) {
                    mydata.add(str);
                    mResult += str;
                    mResult += "\n";


                }
                SdcardFile [] sdcardFiles=new SdcardFile[mydata.size()];
                for (int i=0;i<mydata.size();i++){

                   sdcardFiles[i]=new SdcardFile(mydata.get(i));
                    myfile.add(sdcardFiles[i]);
                }


                Log.e("333333", "onCreate: " + mResult);


            }
        }).start();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {



        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    //处理逻辑
                    search();

                }else {

                    Toast.makeText(getActivity(), "拒绝权限导致功能不可用", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       switch (requestCode){

           case Constant.REQUEST_CODE_PICK_IMAGE:
               if (resultCode == RESULT_OK) {
                   ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);

                   ArrayList<Uri> utilist=new ArrayList<>();
                   List<String> mylist=new ArrayList<>();

                   for (ImageFile file : list){
                       String path = file.getPath();
                       mylist.add(path);

                   }
                   for (int i=0;i<mylist.size();i++){
                       utilist.add(Uri.fromFile(new File(mylist.get(i))));
                   }
                   Intent shareIntent = new Intent();
                   //打开分享界面的Action
                   shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                   //分享传递的数据
                   shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, utilist);
                   //指定分享的类型
                   shareIntent.setType("image/*");
                   //打开分享
                   startActivity(Intent.createChooser(shareIntent, "分享到"));


               }


               break;
           case Constant.REQUEST_CODE_PICK_VIDEO:
               if (resultCode == RESULT_OK) {
                   ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);

                   ArrayList<Uri> utilist=new ArrayList<>();
                   List<String> mylist=new ArrayList<>();

                   for (VideoFile file : list){
                       String path = file.getPath();
                       mylist.add(path);

                   }
                   for (int i=0;i<mylist.size();i++){
                       utilist.add(Uri.fromFile(new File(mylist.get(i))));
                   }
                   Intent shareIntent = new Intent();
                   //打开分享界面的Action
                   shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                   //分享传递的数据
                   shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, utilist);
                   //指定分享的类型
                   shareIntent.setType("video/*");
                   //打开分享
                   startActivity(Intent.createChooser(shareIntent, "分享到"));


               }
               break;
           case Constant.REQUEST_CODE_PICK_AUDIO:
               if (resultCode == RESULT_OK) {
                   ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                   ArrayList<Uri> utilist=new ArrayList<>();
                   List<String> mylist=new ArrayList<>();

                   for (AudioFile file : list){
                       String path = file.getPath();
                       mylist.add(path);

                   }
                   for (int i=0;i<mylist.size();i++){
                       utilist.add(Uri.fromFile(new File(mylist.get(i))));
                   }

                   Intent shareIntent = new Intent();
                   //打开分享界面的Action
                   shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                   //分享传递的数据
                   shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, utilist);
                   //指定分享的类型
                   shareIntent.setType("audio/*");
                   //打开分享
                   startActivity(Intent.createChooser(shareIntent, "分享到"));


               }
               break;
           case Constant.REQUEST_CODE_PICK_FILE:
               if (resultCode == RESULT_OK) {
                   ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                   ArrayList<Uri> utilist=new ArrayList<>();
                   List<String> mylist=new ArrayList<>();

                   for (NormalFile file : list){
                       String path = file.getPath();
                       mylist.add(path);

                   }
                   for (int i=0;i<mylist.size();i++){
                       utilist.add(Uri.fromFile(new File(mylist.get(i))));
                   }

                   Intent shareIntent = new Intent();
                   //打开分享界面的Action
                   shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                   //分享传递的数据
                   shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, utilist);
                   //指定分享的类型
                   shareIntent.setType("text/*");
                   //打开分享
                   startActivity(Intent.createChooser(shareIntent, "分享到"));

               }
               break;
       }


       super.onActivityResult(requestCode, resultCode, data);
    }
}
