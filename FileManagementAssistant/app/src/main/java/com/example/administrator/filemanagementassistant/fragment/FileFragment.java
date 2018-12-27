package com.example.administrator.filemanagementassistant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.filemanagementassistant.R;
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

    private View mview;



   @OnClick({R.id.bt_image,R.id.bt_audio,R.id.bt_file,R.id.bt_video})
   public void onClick(View view){
       switch (view.getId()){
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
     


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.file_fragment,null);
        ButterKnife.bind(this,mview);
        return mview;



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
