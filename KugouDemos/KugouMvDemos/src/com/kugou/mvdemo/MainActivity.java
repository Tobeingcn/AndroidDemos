package com.kugou.mvdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnItemClickListener{
	private  String  ROOT_DIR = Environment
            .getExternalStorageDirectory().toString();
//    public   String APP_NAME="Kugoumv";
	public   String APP_NAME="UCDownloads";
    public   String MV_PTAH=ROOT_DIR+File.separator+APP_NAME;
	public   String MP4_SUFFIX=".mp4";
	private static final String KEY_MEDIA_PATH="key_media_path";
	private  TextView tvInfo;
	private ListView lvMvs;
	private Button btnScan;
	private EditText etPath;
	private PathAdapter mPathAdapter;
//    type = PlayerConfig.isLowCPU() ? TYPE_LOW : TYPE_NORMAL;
//    sb.append("&quality=" + (type == TYPE_LOW ? "3" : "2"));
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(CPUUtil.isHighCPU(this)){
			setTitle("默认音质：TYPE_NORMAL"+3);
		}else{
			setTitle("默认音质：TYPE_LOW"+2);
		}
		etPath=(EditText) findViewById(R.id.etPath);
		SharedPreferences sb=getSharedPreferences(getPackageName(), 0);
		String path=sb.getString(KEY_MEDIA_PATH, MV_PTAH);
		etPath.setText(path);
		etPath.clearFocus();
		lvMvs=(ListView) findViewById(R.id.lvMvs);
		tvInfo=(TextView) findViewById(R.id.tvInfo);
		btnScan=(Button) findViewById(R.id.btnScan);
		mPathAdapter=new PathAdapter(this);
		lvMvs.setAdapter(mPathAdapter);
		lvMvs.setOnItemClickListener(this);
		btnScan.setOnClickListener(this);
		onClick(btnScan);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String path=(String) mPathAdapter.getItem(arg2);
		Intent intent=new Intent(this,MVPlayActivity.class);
		intent.putExtra(MVPlayActivity.EXTRA_PATH,path);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnScan:
			final String path=etPath.getText().toString();
			if(TextUtils.isEmpty(path)){
				Toast.makeText(getApplication(), "路径未空", Toast.LENGTH_LONG).show();
			}else if(!new File(path).exists()){
				Toast.makeText(getApplication(), "文件不存在", Toast.LENGTH_LONG).show();
			}else{
				SharedPreferences sb=getSharedPreferences(getPackageName(), 0);
				sb.edit().putString(KEY_MEDIA_PATH, path).commit();
				etPath.setEnabled(false);
				btnScan.setEnabled(false);
				tvInfo.setText("正在扫描文件夹中的媒体文件");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						final List<String> list=new ArrayList<String>();
						scanMp4Files(new File(path), list);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mPathAdapter.setPathList(list);
								mPathAdapter.notifyDataSetChanged();
								etPath.setEnabled(true);
								btnScan.setEnabled(true);
								tvInfo.setText("扫描结束，共有"+list.size()+"个文件");
							}
						});
					}
				}).start();
			}
			break;
		default:
			break;
		}
	}
	
	private void scanMp4Files(File file, List<String> results) {
		if (file == null) {
			return;
		} else if (file.isFile()) {
			if (file.getAbsolutePath().toString().toLowerCase()
					.endsWith(MP4_SUFFIX)) {
				results.add(file.getAbsolutePath());
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				scanMp4Files(file2, results);
			}
		}
	}
		private class PathAdapter extends BaseAdapter {
		private List<String> pathList;
		private Context mContext;
		public PathAdapter(Context context){
			mContext=context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if(convertView==null){
				convertView=new TextView(mContext);
				convertView.setPadding(5, 5, 5, 5);
			}
			tv=(TextView) convertView;
			String path=(String) getItem(position);
			tv.setText(new File(path).getName()+":"+path);
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return pathList.get(position);
		}
		
		@Override
		public int getCount() {
			if(pathList==null){
				return 0;
			}
			return pathList.size();
		}

		public List<String> getPathList() {
			return pathList;
		}

		public void setPathList(List<String> pathList) {
			this.pathList = pathList;
		}
	}
}
