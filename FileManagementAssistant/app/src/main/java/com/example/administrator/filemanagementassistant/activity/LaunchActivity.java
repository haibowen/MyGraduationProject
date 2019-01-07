package com.example.administrator.filemanagementassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.util.MyHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class LaunchActivity extends AppCompatActivity {
    private String url="http://guolin.tech/api/bing_pic";
    @BindView(R.id.image_launch)
    public ImageView imagView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        sharedPreferences=getPreferences(MODE_PRIVATE);

        //切记使用该三方库这句必须有，要不然程序容易空指针
        ButterKnife.bind(this);
        String bing_pic=sharedPreferences.getString("bing_pic",null);
        if (bing_pic!=null){
            Glide.with(this).load(bing_pic).into(imagView);
        }else {
            getImage();
        }


        delay();




    }
    //延时跳转
    public void delay(){

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                LaunchActivity.this.finish();

            }
        },3000);
    }

    //加载壁纸
    public  void getImage(){

        MyHttpUtil.SendRequestWithOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //切记这里是 response.body().string();    是 string();
                 final String result=response.body().string();
                 //sp临时存取一下图片链接
                SharedPreferences.Editor editor= PreferenceManager.
                        getDefaultSharedPreferences(LaunchActivity.this).edit();
                editor.putString("bing_pic",result);
                editor.apply();

                Log.e("5555555", "onResponse: "+result );
                Log.e("55", "onResponse: "+response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LaunchActivity.this).load(result).into(imagView);
                    }
                });
            }
        });



    }

}
