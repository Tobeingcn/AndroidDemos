package cn.tobeing.notificationdemos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int ID_NOTIFICATION=1;
    private static final int ID_NOTIFICATIONV11=11;
    private static final int ID_NOTIFICATIONV16=16;
    private static final int ID_NOTIFICATIONSelf='s';
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        startService(new Intent(this,TBService.class));
    }
    private int[] viewIds=new int[]{
            R.id.btnSimpleNotification,
            R.id.btnSimpleNotificationV11,
            R.id.btnSimpleNotificationV16,
            R.id.btnSimpleNotificationVSelf,
            R.id.btnShowKugou,
            R.id.btnSimpleNotificationVSelfCPX,
            };
    int i=0;
    private void initView(){
        findViewById(R.id.btnSimpleNotification).setOnClickListener(this);
        findViewById(R.id.btnSimpleNotificationV11).setOnClickListener(this);
        findViewById(R.id.btnSimpleNotificationV16).setOnClickListener(this);
        findViewById(R.id.btnSimpleNotificationVSelf).setOnClickListener(this);
        findViewById(R.id.btnClearAll).setOnClickListener(this);
        findViewById(R.id.btnPrintALl).setOnClickListener(this);
        findViewById(R.id.btnSimpleNotificationVSelfCPX).setOnClickListener(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    if(i++>200){
//                        return;
//                    }
//                    if (MainActivity.this.isFinishing()) {
//                        Log.d("TBNotificationUtil", "what?");
//                        return;
//                    }
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    MainActivity.this.onClick(MainActivity.this.findViewById(viewIds[(int) (System.nanoTime()% viewIds.length)]));
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                            //MainActivity.this.onClick(MainActivity.this.findViewById(viewIds[(int) (System.nanoTime()% viewIds.length)]));
//                            //((Button)MainActivity.this.findViewById(R.id.btnSimpleNotification)).setText("TEST"+ UUID.randomUUID());
//                        }
//                });
//                }
//            }
//        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private int lastId=-1;
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (view.getId()){
            case R.id.btnSimpleNotification:{
                TBNotificationUtil.showSimpleNotification(this,"我是一个兵","造福老百姓",id);
            }
                break;
            case R.id.btnSimpleNotificationV11:{
                TBNotificationUtil.showSimpleNotificationV11(this, "我是一个兵", "造福老百姓V11", id);
            }
                break;
            case R.id.btnSimpleNotificationV16:{
                TBNotificationUtil.showSimpleNotificationV16(this, "我是一个兵", "造福老百姓V16", id);
            }
            break;
            case R.id.btnSimpleNotificationVSelf:{
                TBNotificationUtil.showSimpleNotificationVSelf(this,"我是一个兵","造福老百姓Self",id);
            }
            break;
            case R.id.btnClearAll:{
                    TBNotificationUtil.clearAll(this);
            }
            break;
            case R.id.btnSimpleNotificationVSelfCPX:{
                TBNotificationUtil.showSimpleNotificationVSelfCPX(this,"我是一个兵","造福老百姓Self",id);
            }
            break;
            case R.id.btnPrintALl:{
                TimeComsuerUtil.getInstance().printResultAll();
            }
            break;
            case R.id.btnLast:{
                if(lastId!=-1) {
                    TBNotificationUtil.clear(this, lastId);
                }
            }
            break;
            case R.id.btnShowKugou:{
                if(TBService.KGNotificationOperation!=null){
                    TimeComsuerUtil.getInstance().start("showNotification");
                    TBService.KGNotificationOperation.showNotification(id, 11);
                    TimeComsuerUtil.getInstance().end("showNotification");
                }
            }
            default:
                break;
        }
        lastId=id;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
