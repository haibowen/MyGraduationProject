package com.example.administrator.filemanagementassistant;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    @BindView(R.id.btnagationbar)
    public BottomNavigationBar bottomNavigationBar;
    private FileFragment fileFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private  int lastselection=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /**
         *  setMode() 内的参数有三种模式类型：
         *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
         *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
         *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
         */

        bottomNavigationBar
                .setTabSelectedListener((BottomNavigationBar.OnTabSelectedListener) this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#3F51B5")//选中颜色
                .setInActiveColor("#807d7d");//未选中
               // .setBarBackgroundColor("#1ccbae");//导航栏颜色
        /**
         *  setBackgroundStyle() 内的参数有三种样式
         *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
         *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
         *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
         *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
         */

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp,"Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_insert_drive_file_black_24dp,"File"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp,"My"))
                .setFirstSelectedPosition(lastselection)
                .initialise();
        SetDefaultFragment();

    }
    public  void SetDefaultFragment(){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        findFragment=new FindFragment();
        fragmentTransaction.replace(R.id.tb,findFragment);
        fragmentTransaction.commit();


    }


    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager=this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (position){
            case 0:

                findFragment=new FindFragment();
                fragmentTransaction.replace(R.id.tb,findFragment);
                break;
            case 1:

                fileFragment=new FileFragment();
                fragmentTransaction.replace(R.id.tb,fileFragment);
                break;
            case 2:

                myFragment=new MyFragment();
                fragmentTransaction.replace(R.id.tb,myFragment);
                break;

                default:
                    break;
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
