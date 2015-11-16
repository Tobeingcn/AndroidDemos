package cn.tobeing.threadtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

import cn.tobeing.threadtest.testunit.AbstractTestUnit;

/**
 * Created by sunzheng on 15/11/16.
 */
public class TestUnitAdapter extends BaseAdapter{
    private Context mContext;
    private List<AbstractTestUnit> testUnits;
    public TestUnitAdapter(Context context,List<AbstractTestUnit> testUnits){
        this.mContext=context;
        this.testUnits=testUnits;
    }
    @Override
    public int getCount() {
        return testUnits==null?0:testUnits.size();
    }

    @Override
    public AbstractTestUnit getItem(int i) {
        return testUnits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button button;
        if(view==null){
            view=new Button(mContext);
        }
        button=(Button)view;
        AbstractTestUnit testUnit=getItem(i);
        button.setText(testUnit.getName());
        button.setTag(testUnit);
        button.setOnClickListener(listener);
        return view;
    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                AbstractTestUnit testUnit=(AbstractTestUnit)view.getTag();
                testUnit.startTest();
        }
    };
}
