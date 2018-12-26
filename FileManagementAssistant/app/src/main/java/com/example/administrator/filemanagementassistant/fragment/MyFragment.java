package com.example.administrator.filemanagementassistant.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.example.administrator.filemanagementassistant.R;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class MyFragment extends Fragment {
    private  View mview;

    @BindView(R.id.image_header_myfragment)
    public CircleImageView  circleImageView;
    @BindView(R.id.text_sign)
    public TextView textView;


    @OnClick({R.id.image_header_myfragment,R.id.text_sign})
    public void onClick(View v){
        switch (v.getId()){

            case R.id.image_header_myfragment:
                Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(Constant.MAX_NUMBER, 9);
                startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

                break;
            case R.id.text_sign:
                final EditText editText=new EditText(getActivity());

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("请输入你的个性签名");
                builder.setView(editText);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(editText.getText().toString())){
                            textView.setText("换个签名");
                        }
                        else {
                            textView.setText(editText.getText().toString());
                            dialog.dismiss();
                        }




                    }
                });
                builder.setNegativeButton("取消",null);

                builder.create().show();
        }
    }





    private String [] data={"设置一","设置二","设置三","设置四","设置无","设置刘"};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.my_fragment,null);
        ButterKnife.bind(this, mview);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);





        return mview;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);

                    for (ImageFile file : list) {
                        String path = file.getPath();

                        //更换头像

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap bm = BitmapFactory.decodeFile(path, options);
                        Log.e("22222", "onActivityResult: "+bm.toString() );


                        circleImageView.setImageBitmap(bm);


                    }
                }


                break;





        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        circleImageView.setDrawingCacheEnabled(true);
        Bitmap obmp = Bitmap.createBitmap(circleImageView.getDrawingCache());
        Log.e("333333", "onSaveInstanceState: "+obmp.toString() );
        outState.putParcelable("image",obmp);
        circleImageView.setDrawingCacheEnabled(false);



    }


    /**
     *
     * 图像裁剪
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

}
