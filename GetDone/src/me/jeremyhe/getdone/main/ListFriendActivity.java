package me.jeremyhe.getdone.main;

import java.util.List;

import com.zxing.activity.QRCaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.common.JPushUtils;
import me.jeremyhe.getdone.common.SettingUtils;
import me.jeremyhe.getdone.common.TaskUtils;
import me.jeremyhe.getdone.common.ui.BaseActivity;
import me.jeremyhe.getdone.dao.Friend;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.main.adapter.FriendListAdapter;
import me.jeremyhe.getdone.services.FriendService;
import me.jeremyhe.getdone.services.TaskService;
import me.jeremyhe.lib.androidutils.NetworkUtils;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.widget.SimpleCircleProgressDialog;

public class ListFriendActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	
	public static final String EXTRA_TASK_ID = "taskId";
	
	public static final int REQUEST_CODE_QR = 1;

	public static void navigateTo(Context c){
		Intent intent = new Intent(c,ListFriendActivity.class);
		c.startActivity(intent);
	}
	
	public static void navigateTo(Context c,long taskId){
		Intent intent = new Intent(c,ListFriendActivity.class);
		intent.putExtra(EXTRA_TASK_ID, taskId);
		c.startActivity(intent);
	}
	
	private TextView mMasterTv;
	
	private ListView mFriendLv;
	private FriendListAdapter mFriendListAdapter;
	
	private Task mTask = null;
	
	private Button mAddFriendBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		
		final long taskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
		if (taskId != -1) {
			mTask = TaskService.getInstance().queryTaskById(taskId);
		}

		findWidget();
		initWidget();
		setListener();
		
	}
	
	private void findWidget(){
		mMasterTv = (TextView)findViewById(R.id.friend_master_tv);
		mFriendLv = (ListView)findViewById(R.id.friend_list_lv);
		
		mAddFriendBtn = (Button)findViewById(R.id.friend_add_btn);
	}
	
	private void initWidget(){
		mMasterTv.setText(SettingUtils.getUserName()+"'s");
		
		List<Friend> friendList = FriendService.getInstance().listAllFriends();
		mFriendListAdapter = new FriendListAdapter(mContext, friendList);
		mFriendLv.setAdapter(mFriendListAdapter);
	}
	
	private void setListener(){
		mFriendLv.setOnItemClickListener(this);
		mAddFriendBtn.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mTask != null) {
			if (NetworkUtils.isNetworkAvailable(mContext)) {
				Friend friend = (Friend) parent.getItemAtPosition(position);
				new PushMessageTask(friend).execute();
			} else {
				ToastUtils.showLongToast(mContext, "网络不可用");
			}
		} else {
			// TODO: 进入好友的详细页面
		}
		
	}
	
	private class PushMessageTask extends AsyncTask<Void, Void, Boolean>{
		
		private Friend friend;
		private SimpleCircleProgressDialog dialog;
		
		public PushMessageTask(Friend friend){
			this.friend = friend;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new SimpleCircleProgressDialog(mContext);
			dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			final String userId = friend.getUserId();
			final String pushMsg = TaskUtils.generatePushMsg(mTask);
			
			return JPushUtils.pushMessage(userId, pushMsg);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (result) {
				ToastUtils.showLongToast(mContext, "推送成功");
			} else {
				ToastUtils.showLongToast(mContext, "推送失败，请稍候再试");
			}
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					ListFriendActivity.this.finish();
				}
			}, 1000);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_add_btn:
			QRCaptureActivity.navigateToForResult(this, REQUEST_CODE_QR);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_QR && resultCode == RESULT_OK) {
			if (resultCode == RESULT_OK) {
				// 解析数据，添加好友
				final String qrCode = data.getStringExtra(QRCaptureActivity.EXTRA_RESULT);
				final Friend friend = FriendService.parseQRCode(qrCode);
				FriendService.getInstance().addOrUpdateFriend(friend);
				
				// 更新当前显示的数据
				List<Friend> friendList = FriendService.getInstance().listAllFriends();
				mFriendListAdapter = new FriendListAdapter(mContext, friendList);
				mFriendLv.setAdapter(mFriendListAdapter);
			} 
		}
	}
}
