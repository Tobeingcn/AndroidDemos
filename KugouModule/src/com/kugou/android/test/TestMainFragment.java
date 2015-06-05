package com.kugou.android.test;

import com.kugou.android.R;
import com.kugou.common.base.AbsFrameworkFragment;
import com.kugou.common.module.fm.resource.ResourceFileManager;
import com.kugou.common.utils.ToastUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class TestMainFragment extends AbsFrameworkFragment implements OnClickListener{
    private Button buttonFM; 
    // 解压FM资源包
    private boolean result =false;
    private boolean hasInited=false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ting_main, null);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnFm).setOnClickListener(this);
        view.findViewById(R.id.btnRingtone).setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                result=new ResourceFileManager(getActivity()).UnzipResource();
                hasInited=true;
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFm:{
                if(!hasInited){
                    ToastUtil.showToastLong(getActivity(), "FM资源正在加载，请稍等一会重试");
                    return;
                }
                NavigationUtils.startFMMainFragment(this);
            }
                break;
            case R.id.btnRingtone:{
                NavigationUtils.startRingtoneMainFragment(this);
            }
            break;
            default:
                break;
        }
    }
}
