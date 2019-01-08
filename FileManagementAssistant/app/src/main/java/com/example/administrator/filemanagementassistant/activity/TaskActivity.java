package com.example.administrator.filemanagementassistant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageView;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.adapter.MyAdapter;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    private MyAdapter myAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;


    private View view;
    private View view1;
    private View view2;
    private ArrayList<View> pageview;

    private ImageView[] tips=new ImageView[3];
    private ImageView imageView;

    private ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        viewPager=findViewById(R.id.viewpager);

        view=getLayoutInflater().inflate(R.layout.view01,null);
        view1=getLayoutInflater().inflate(R.layout.view02,null);
        toolbar=view.findViewById(R.id.toolbar);
        drawerLayout=view.findViewById(R.id.drawerlayout);


        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        pageview=new ArrayList<View>();
        pageview.add(view);
        pageview.add(view1);




        myAdapter=new MyAdapter(pageview);

        viewGroup=findViewById(R.id.linearlayout);
        tips=new ImageView[pageview.size()];

        for (int i=0;i<pageview.size();i++){

            imageView=new ImageView(TaskActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(12,12));
            imageView.setPadding(12,0,12,0);
            tips[i]=imageView;

            //morenxuanzhong
            if (i==0){
                tips[i].setBackgroundResource(R.mipmap.cir_fouce);
            }else {

                tips[i].setBackgroundResource(R.mipmap.cir_unfouce);

            }
            viewGroup.addView(tips[i]);


        }

        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(this);




    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.file_type_item, menu);


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_search:

                Intent intent=new Intent(TaskActivity.this,FileActivity.class);
                startActivity(intent);

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {



    }

    @Override
    public void onPageSelected(int i) {



        tips[i].setBackgroundResource(R.mipmap.cir_fouce);
        for (int j=0;j<pageview.size();j++){
            if (i!=j){

                tips[j].setBackgroundResource(R.mipmap.cir_unfouce);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


}
