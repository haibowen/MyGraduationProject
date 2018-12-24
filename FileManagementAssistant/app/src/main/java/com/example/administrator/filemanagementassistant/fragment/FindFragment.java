package com.example.administrator.filemanagementassistant.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.ListPreloader;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MyHistroyAdapter;
import com.example.administrator.filemanagementassistant.bean.Historydata;
import com.example.administrator.filemanagementassistant.util.GlideImageLoader;
import com.example.administrator.filemanagementassistant.util.MyHttpUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FindFragment extends Fragment {
    private View mview;
    @BindView(R.id.banner)
    public Banner banner;

    @BindView(R.id.recycler_history)
    public RecyclerView recyclerview;

    @BindView(R.id.history_data)
    public TextView textView;

    private Historydata historydata;
    private List<Integer>images=new ArrayList<>();

    private List<String> mtitle=new ArrayList<>();
    private List<String> mimage=new ArrayList<>();
    private List<Historydata> mydata=new ArrayList<>();
   private String url="http://api.juheapi.com/japi/toh?key=46f212b179d15ddd8b2a28a004e8fe79&v=1.0&month=11&day=1";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.find_fragment, null);

        ButterKnife.bind(this, mview);
        images.add(R.drawable.first);
        images.add(R.drawable.second);
        images.add(R.drawable.third);
        //轮播图
        banner  .setImages( images)

                .setDelayTime( 4000 )
                .isAutoPlay( true )
                .setBannerAnimation( Transformer.Tablet )
                .setImageLoader( new GlideImageLoader() ).start();

        //recyclerview
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(layoutManager);
        //获取系统时间
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        textView.setText(year+"年"+month+"月"+day+"日"+"  "+"历史上的今天");
        Log.e("2222", "onCreateView: "+year );
        Log.e("2222", "onCreateView: "+month );
        Log.e("2222", "onCreateView: "+day );



        //请求数据
       // String url="http://api.juheapi.com/japi/toh?key=46f212b179d15ddd8b2a28a004e8fe79&v=1.0"+"&"+month+"&"+day;
        String url="http://api.juheapi.com/japi/toh?key=46f212b179d15ddd8b2a28a004e8fe79&v=1.0&month=11&day=1";
        GetInternetData(url);

        return mview;


    }

    //请求网络数据
    public  void  GetInternetData(String url){


        MyHttpUtil.SendRequestWithOkHttp(url,
                new okhttp3.Callback()  {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                        String ResponseData=response.body().string();
                        Log.e("6666", "onResponse: "+ResponseData );

                        DealWithResponseData(ResponseData);

                    }
                });
    }



    //解析数据
    public  void DealWithResponseData(String ResponseData){
        JSONObject jsonObject= null;
        if (mtitle!=null){
            //mcontent.clear();
            mtitle.clear();
            mimage.clear();
            mydata.clear();
        }
        try {
            jsonObject = new JSONObject(ResponseData);
            Log.e("2222", "DealWithResponseData: "+jsonObject );


            JSONArray jsonArray=jsonObject.getJSONArray("result");


            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject2= (JSONObject) jsonArray.get(i);
                String title=jsonObject2.getString("title");
                Log.d("0000000", "DealWithResponseData: "+title);

                String image=jsonObject2.getString("pic");

                Log.e("2222", "DealWithResponseData: "+image );


               // mcontent.add(url);
                mtitle.add(title);
                mimage.add(image);






            }
            GetDataSource(mtitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    //适配器的数据源
    public  void GetDataSource(final List<String> mtitle){


        Historydata [] news=new Historydata[mtitle.size()];
        for (int i=0;i<mtitle.size();i++){


            news[i]=new Historydata(mtitle.get(i),mimage.get(i));

            Log.e("888888", "GetDataSource: "+news[i].getTitle() );
            //Log.e("99999", "GetDataSource: "+news[i].getImageid() );
            Log.d("dong", "GetDataSource: "+news[i].getTitle());


            mydata.add(news[i]);


        }



        ShowResult();

    }
    public void ShowResult(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MyHistroyAdapter myAdapter=new MyHistroyAdapter(mydata);
                myAdapter.notifyDataSetChanged();
                recyclerview.setAdapter(myAdapter);
            }
        });

    }

}
