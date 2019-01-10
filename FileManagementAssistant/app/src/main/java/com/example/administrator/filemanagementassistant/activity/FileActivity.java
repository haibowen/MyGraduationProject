package com.example.administrator.filemanagementassistant.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MySdcardFileAdapter;
import io.haydar.filescanner.FileInfo;
import io.haydar.filescanner.FileScanner;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {
    private String TAG="wenhaibo";

    private List<FileInfo> mylist = new ArrayList<>();

    private String type=".jpg";


    @BindView(R.id.file_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.text_count)
    public TextView textViewcount;

    @BindView(R.id.recycler_show_file)
    public RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //权限申请

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {


            //fileScanner();
            filesearch(type);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //处理逻辑

                    //fileScanner();
                    filesearch(type);

                } else {

                    Toast.makeText(this, "拒绝权限导致功能不可用", Toast.LENGTH_SHORT).show();

                }
                break;



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.file_menu_search, menu);

        //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search_file);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);

        searchView.setQueryHint("请输入要搜索的文件后缀...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               type=s;
                Toast.makeText(FileActivity.this, "正在搜索后缀为:"+type+"的文件,请耐心等待..", Toast.LENGTH_SHORT).show();


               filesearch(type);





                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

                break;

        }



        return super.onOptionsItemSelected(item);
    }


    private void filesearch(String s){

        FileScanner.getInstance(this).clear();
        FileScanner.getInstance(this).setType(s).start(new FileScanner.ScannerListener() {
            /**
             * 扫描开始
             */
            @Override
            public void onScanBegin() {
                Log.d(TAG, "onScanBegin: ");
            }

            /**
             * 扫描结束
             */
            @Override
            public void onScanEnd() {

                Log.d(TAG, "onScanEnd: ");
                // ArrayList<FileInfo> fileInfoArrayList= FileScanner.getInstance(getActivity()).getAllFiles();
                mylist = FileScanner.getInstance(FileActivity.this).getAllFiles();
                Log.e(TAG, "onScanEnd: "+mylist.size() );

                for (FileInfo fileInfo : mylist) {
                    Log.d(TAG, "fileScanner: " + fileInfo.getFilePath());

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mylist.size()!=0){

                            textViewcount.setText("该文件类型共有:" + mylist.size() + "个");


                            MySdcardFileAdapter adapter = new MySdcardFileAdapter(mylist);
                            recyclerView.setAdapter(adapter);
                        }else {
                            Toast.makeText(FileActivity.this,"暂未发现该格式的文件",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }

            /**
             * 扫描进行中
             * @param paramString 文件夹地址
             * @param progress  扫描进度
             */
            @Override
            public void onScanning(String paramString, int progress) {
                Log.d(TAG, "onScanning: " + progress);
            }

            /**
             * 扫描进行中，文件的更新
             * @param info
             * @param type  SCANNER_TYPE_ADD：添加；SCANNER_TYPE_DEL：删除
             */
            @Override
            public void onScanningFiles(FileInfo info, int type) {
                Log.d(TAG, "onScanningFiles: info=" + info.toString());
            }
        });


    }


}
