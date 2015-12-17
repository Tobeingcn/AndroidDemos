package cn.tobeing.threadtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.tobeing.threadtest.testunit.AbstractTestUnit;
import cn.tobeing.threadtest.testunit.FinalParaTestUnit;
import cn.tobeing.threadtest.testunit.GuantityRunningThreadUnit;
import cn.tobeing.threadtest.testunit.GuantityThreadUnit;
import cn.tobeing.threadtest.testunit.RecursiveLockTest;
import cn.tobeing.threadtest.testunit.ReentrantLockTest;
import cn.tobeing.threadtest.testunit.ResultPrint;
import cn.tobeing.threadtest.testunit.ThreadLocalIndexTest;
import cn.tobeing.threadtest.testunit.ThreadLocalTest;
import cn.tobeing.threadtest.testunit.ThreadPriorityTest;

public class MainActivity extends AppCompatActivity {
    private ListView lvTestUnits;
    private List<AbstractTestUnit> abstractTestUnits;
    private TestUnitAdapter mAdapter;
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
    }
    private void initView(){
        lvTestUnits=(ListView)findViewById(R.id.lvTestUnits);
        abstractTestUnits=new ArrayList<>();
        abstractTestUnits.add(new FinalParaTestUnit());
        abstractTestUnits.add(new RecursiveLockTest());
        abstractTestUnits.add(new ResultPrint());
        abstractTestUnits.add(new ThreadPriorityTest());
        abstractTestUnits.add(new GuantityThreadUnit());
        abstractTestUnits.add(new GuantityRunningThreadUnit());
        abstractTestUnits.add(new ThreadLocalTest());
        abstractTestUnits.add(new ThreadLocalIndexTest());
        mAdapter=new TestUnitAdapter(this,abstractTestUnits);
        lvTestUnits.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
