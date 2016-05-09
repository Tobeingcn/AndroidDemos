package cn.tobeing.kugousdcard;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_REFRESH_RESULT=0x001;

    private static final int MSG_GET_MOUNTS=0x1001;

    private BackgroundHander backgroundHander;

    private UIhandler uIhandler;

    private TextView tvMessage;

    private TextView tvResult;

    private boolean isRunning=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandlerThread thread=new HandlerThread("s");
        thread.start();

        backgroundHander=new BackgroundHander(thread.getLooper());

        uIhandler=new UIhandler();

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundHander.obtainMessage(MSG_GET_MOUNTS).sendToTarget();
            }
        });
        findViewById(R.id.btnCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager)MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvResult.getText().toString());
                Toast.makeText(MainActivity.this,"信息已复制到剪切版，请发送给客服人员",Toast.LENGTH_LONG).show();
            }
        });
        tvMessage= (TextView) findViewById(R.id.tvMessa);
        tvResult=(TextView) findViewById(R.id.tvResult);
        tvResult.setMovementMethod(new ScrollingMovementMethod());
    }
    private class BackgroundHander extends Handler{
        public BackgroundHander(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_GET_MOUNTS:{
                    isRunning=true;
                    StorageMessage.clear();
                    uIhandler.sendEmptyMessage(MSG_REFRESH_RESULT);
                    MountsAndStorageUtil.getRepeatMountsAndStorage();
                    isRunning=false;
                    StorageMessage.setTips("检测结束");
                    uIhandler.sendEmptyMessage(MSG_REFRESH_RESULT);
                }
                break;
            }
        }
    }

    private class UIhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REFRESH_RESULT:{
                    tvResult.setText(StorageMessage.getMessage());
                    tvMessage.setText(StorageMessage.getTips());
                    if(isRunning) {
                        sendEmptyMessageDelayed(MSG_REFRESH_RESULT, 500);
                    }
                }
            }
        }
    }
}
