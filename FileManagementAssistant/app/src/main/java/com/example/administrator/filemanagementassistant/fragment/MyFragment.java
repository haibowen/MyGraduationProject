package com.example.administrator.filemanagementassistant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.filemanagementassistant.R;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {
    private  View mview;

    @BindView(R.id.lv_mylist)
    public ListView listView;

    private String [] data={"设置一","设置二","设置三","设置四","设置无","设置刘"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.my_fragment,null);
        ButterKnife.bind(this, mview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);

        listView.setAdapter(adapter);




        return mview;

    }
}
