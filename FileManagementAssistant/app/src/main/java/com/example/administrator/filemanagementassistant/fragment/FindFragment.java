package com.example.administrator.filemanagementassistant.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
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
    private DrawerLayout drawerLayout;
    private Historydata historydata;
    private List<Integer>images=new ArrayList<>();
    private List<String> mtitle=new ArrayList<>();
    private List<String> mimage=new ArrayList<>();
    private List<String> mcontent=new ArrayList<>();
    private List<Historydata> mydata=new ArrayList<>();
    private String url="http://api.juheapi.com/japi/toh?key=46f212b179d15ddd8b2a28a004e8fe79&v=1.0&month=11&day=1";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //mview = inflater.inflate(R.layout.find_fragment, null);
        drawerLayout=getActivity().findViewById(R.id.drawerlayout);
        setHasOptionsMenu(true);
        /**
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);

        }
         **/
        //避免ui重新绘制
        if(mview==null){
            mview = inflater.inflate(R.layout.find_fragment, null);
            ButterKnife.bind(this, mview);
            //获取系统时间
            Calendar calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            int day =calendar.get(Calendar.DAY_OF_MONTH);
            textView.setText(year+"年"+month+"月"+day+"日"+"  "+"历史上的今天");
            //请求数据
            String url="http://api.juheapi.com/japi/toh?key=46f212b179d15ddd8b2a28a004e8fe79&v=1.0&month="+month+"&day="+day;
           GetInternetData(url);
        }
        ViewGroup parent= (ViewGroup) mview.getParent();
        if (parent!=null){
            parent.removeView(mview);
        }
        images.add(R.drawable.ic_launcher_background);
        images.add(R.drawable.ic_launcher_background);
        images.add(R.drawable.ic_launcher_background);
        //轮播图
        banner  .setImages( images)
                .setDelayTime( 4000 )
                .isAutoPlay( true )
                .setBannerAnimation( Transformer.Tablet )
                .setImageLoader( new GlideImageLoader() ).start();
        //recyclerview
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(layoutManager);
        return mview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        //inflater.inflate(R.menu.toolbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    **/

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
            mcontent.clear();
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
                String content=jsonObject2.getString("des");
                if (image.equals("")){
                   mtitle.remove(title);
                   mimage.remove(image);
                }else {
                    mtitle.add(title);
                    mimage.add(image);
                    mcontent.add(content);
                }
                Log.e("2222", "DealWithResponseData: "+image );
               // mcontent.add(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetDataSource();
    }
    //适配器的数据源
    public  void GetDataSource(){
        Historydata [] news=new Historydata[mtitle.size()];
        for (int i=0;i<mtitle.size();i++){
            news[i]=new Historydata(mtitle.get(i),mimage.get(i),mcontent.get(i));
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
                MyHistroyAdapter myAdapter=new MyHistroyAdapter(mydata,getActivity());
                myAdapter.notifyDataSetChanged();
                recyclerview.setAdapter(myAdapter);
            }
        });
    }
}
