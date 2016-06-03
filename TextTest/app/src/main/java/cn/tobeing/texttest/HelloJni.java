package cn.tobeing.texttest;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.UUID;

public class HelloJni extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateTest();
    }
    private void updateTest(){
        ((TextView)findViewById(R.id.tvTest)).setText("Hellow"+UUID.randomUUID().hashCode()+stringFromJNI("haha"));
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTest();
            }
        },3000);
    }
    /* A native method that is implemented by the
   * 'hello-jni' native library, which is packaged
   * with this application.
   */
    public native String  stringFromJNI(String key);

    static {
        System.loadLibrary("hello-jni");
    }
}
