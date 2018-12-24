package com.example.administrator.filemanagementassistant.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.ListPreloader;
import com.example.administrator.filemanagementassistant.R;
import com.example.administrator.filemanagementassistant.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends Fragment {
    private View mview;
    @BindView(R.id.banner)
    public Banner banner;
    private List<Integer>images=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.find_fragment, null);

        ButterKnife.bind(this, mview);
        images.add(R.drawable.first);
        images.add(R.drawable.second);
        images.add(R.drawable.third);
        banner  .setImages( images)

                .setDelayTime( 4000 )
                .isAutoPlay( true )
                .setBannerAnimation( Transformer.Tablet )
                .setImageLoader( new GlideImageLoader() ).start();

        return mview;


    }

}
