package com.letv.recorder;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.letv.recorder.data.CreateStreamData;
import com.letv.recorder.data.LetvStreamData;
import com.letv.recorder.data.StreamData;
import com.letv.recorder.util.MD5Utls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 无皮肤版和有皮肤版的区别
 * 无皮肤:SDK不去猜测你的业务逻辑。只是提供最基本的接口:比如开始推流、切换滤镜、切换摄像头等。
 * 有皮肤:SDK提供一种业务逻辑的使用办法。比如DEMO中展示的,你点击按钮就可以开始推流,点击下方各个图标就能实现功能切换等等
 *
 *
 *
 * 1、所有推流SDK功能和最简单的DEMO,可以直接查看RecorderTestActivity
 * 其中:如果移动直播查看CameraView
 * 	云直播查看LeCameraView
 *
 * 	2、在AndroidManifest.xml 中,我们默认设置MainActivity的启动方式,MainAcivity提供了漂亮的UI界面。
 * 	如果你需要看看RecorderTestActivity 的运行效果,需要自己在AndroidManifest.xml中进行设置,使用RecorderTestActivity直接启动就行了
 *
 * 	3、对于我们提供的UI层,理论不建议修改,但是如果你们确实需要,也可以自己修改
 * 	 	但是出现任何BUG,需要自己调试。
 */
public class MainActivity extends Activity {
	// 移动直播推流域名，在官网移动直播创建应用后可拿到
	private static final String DEFAULT_DOMAINNAME = "216.mpush.live.lecloud.com";
	// 移动直播推流签名密钥，在官网移动直播创建应用后可拿到
	private static final String DEFAULT_APPKEY = "KIVK8X67PSPU9518B1WA";
	// 移动直播推流地址， 当用户知道自己需要推流的地址后可以使用
	private static final String DEFAULT_PUSHSTREAM = "rtmp://216.mpush.live.lecloud.com/live/demo";
	// 乐视云直播推流用户userID,用户可以在官网用户中心拿到
	private static final String DEFAULT_LETV_USERID = "800053";
	//乐视云直播推流用户私钥，用户可以在官网用户中心拿到
	private static final String DEFAULT_LETV_APPKEY = "60ca65970dc1a15ad421d46f524b99b7";
	//乐视云直播推流ID，用户开通云直播功能，可以在创建活动后拿到
	private static final String DEFAULT_LETV_STREAMID = "A2016120500000gx";

	private String default_streamid = "test1";

	private View currButton;
	private RelativeLayout meddle;
	private LinearLayout llNoStream;
	private LinearLayout llStream;
	private LinearLayout llLetvStream;
	private Button btnIsScreen;
	private CreateStreamData createStreamData;
	private StreamData streamData;
	private LetvStreamData letvStreamData;
	private ClipboardManager myClipboard;

	private EditText etDomainName;
	private EditText etAppKey;
	private EditText etStreamId;
	private Button btnNoCreateStream;
	private RelativeLayout rlPlayerUrl;
	private TextView tvPlayerUrl;
	private TextView tvCopy;
	private TextView tvCopySuccess;

	private EditText etStreamUrl;

	private EditText etLetvUserId;
	private EditText etLetvAppKey;
	private EditText etLetvStreamID;

	private int type = 0;
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
			finish();
			return;
		}
		setContentView(R.layout.activity_main);
		//移动直播生成推流地址时用到的Model
		createStreamData = new CreateStreamData(this);
		//移动直播有推流地址时用到的Model
		streamData = new StreamData(this);
		//乐视云直播 Model
		letvStreamData = new LetvStreamData(this);
		//初始化视图
		initView();
		//动态申请权限
		checkSelfPermission();
	}

	/**
	 * 检查权限,获取所有需要的权限
	 * 	当targetSdkVersion大于23并且打算在6.0手机上运行时,请动态申请SDK所需要的权限
	 */
	public void checkSelfPermission() {
		if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 0);
		}
   	}
	/**
	 * 初始化视图
	 */
	private void initView(){
		currButton =findViewById(R.id.btn_no_push_stream);
		currButton.setOnClickListener(onClick);
		findViewById(R.id.btn_push_stream).setOnClickListener(onClick);
		findViewById(R.id.btn_letv_stream).setOnClickListener(onClick);
		findViewById(R.id.btn_submit_push).setOnClickListener(onClick);
		
		meddle = (RelativeLayout) findViewById(R.id.rl_middle);
		llNoStream = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_no_push_stream, null);
		etDomainName = (EditText) llNoStream.findViewById(R.id.et_domain_name);
		etAppKey = (EditText) llNoStream.findViewById(R.id.et_app_key);
		etStreamId = (EditText) llNoStream.findViewById(R.id.et_no_stream_id);
		btnNoCreateStream = (Button) llNoStream.findViewById(R.id.btn_create_stream);
		btnNoCreateStream.setOnClickListener(onClick);
		rlPlayerUrl = (RelativeLayout) llNoStream.findViewById(R.id.rl_player_url);
		tvPlayerUrl = (TextView) llNoStream.findViewById(R.id.tv_line);
		tvCopy = (TextView) llNoStream.findViewById(R.id.tv_copy);
		tvCopy.setText(Html.fromHtml("<u>"+"复制"+"</u>"));
		tvCopy.setOnClickListener(onClick);
		tvCopySuccess = (TextView) llNoStream.findViewById(R.id.tv_copy_success);
		
		llStream = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_push_stream, null);
		etStreamUrl = (EditText) llStream.findViewById(R.id.et_stream_url);
		
		llLetvStream = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_letv_stream, null);
		etLetvUserId = (EditText) llLetvStream.findViewById(R.id.ed_user_id);
		etLetvAppKey = (EditText) llLetvStream.findViewById(R.id.ed_letv_app_key);
		etLetvStreamID = (EditText) llLetvStream.findViewById(R.id.ed_letv_stream_id);
		// 默认展示无推流地址界面 ，可以在安装发布包中的APK文件中看到效果
		addNoStreamView(currButton);
	}
	// 界面中所有的按钮点击是事件
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 展现 无推流地址界面
			case R.id.btn_no_push_stream:
				addNoStreamView(v);
				break;
			// 展现有推流地址界面
			case R.id.btn_push_stream:
				addStreamView(v);
				break;
			// 展现乐视云直播界面
			case R.id.btn_letv_stream:
				addLetvView(v);
				break;
			// 开始直播 点击事件
			case R.id.btn_submit_push:
				pushStream();
				break;
			// 无推流地址中 生成播放地址 按钮点击事件
			case R.id.btn_create_stream: // 生成播放地址
				createPlayerUrl();
				break;
			// 生成播放地址后，复制 按钮点击事件
			case R.id.tv_copy:
				copyUtil();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 复制功能，把播放地址复制到剪贴板
	 */
	private void copyUtil() {
		tvCopySuccess.setVisibility(View.VISIBLE);
		btnNoCreateStream.setTextColor(0xffffffff);
		// 复制功能
		myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		String text = createStreamData.getPlayUrl();
		ClipData myClip = ClipData.newPlainText("text", text);
		myClipboard.setPrimaryClip(myClip);
		// 复制成功 提示框1s 后隐藏
		tvCopySuccess.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				tvCopySuccess.setVisibility(View.INVISIBLE);
			}
		}, 1000);
	}
	/**
	 * 生成播放地址，点击生成播放地址按钮后调用这个方法
	 */
	private void createPlayerUrl(){
		// 按照拼接规则，拼接出一个播放地址
		String pullStream = createStreamUrl(false);
		// 保存成功的播放地址
		if(pullStream != null && !pullStream.equals("")){
			createStreamData.setPlayUrl(pullStream);
		}else{
			createStreamData.setPlayUrl("无法生成正确的播放地址");
		}
		btnNoCreateStream.setBackgroundColor(0xffb2b2b2);
		btnNoCreateStream.setClickable(false);
		rlPlayerUrl.setVisibility(View.VISIBLE);
		tvPlayerUrl.setText(createStreamData.getPlayUrl());
	}
	/**
	 * 点击开始推流按钮后调用该方法
	 */
	private void pushStream(){
		Intent intent = null;
		switch (type) {
		case 0: // 无推流地址
			// 无推流地址界面中，获取 推流域名 签名密钥 流名称 三个参数
			intent = new Intent(this,RecorderActivity.class);
			if(etDomainName.getText().toString() != null && !"".equals(etDomainName.getText().toString())){
				createStreamData.setDomainName(etDomainName.getText().toString());
			}
			if(etAppKey.getText().toString() != null && !"".equals(etAppKey.getText().toString())){
				createStreamData.setAppKey(etAppKey.getText().toString());
			}
			if(etStreamId.getText().toString() != null && !"".equals(etStreamId.getText().toString())){
				createStreamData.setStreamId(etStreamId.getText().toString());
				intent.putExtra("pushName", createStreamData.getStreamId());
			}else{
				intent.putExtra("pushName", default_streamid);
			}
			// 生成一个推流地址，并且把推流地址 传递到 RecorderActivity 中去
			intent.putExtra("streamUrl", createStreamUrl(true));
			break;
		case 1: // 有推流地址
			//有推流地址界面中，直接获取界面中的推流地址 。然后把推流地址传递给 RecorderActivity 中去
			intent = new Intent(this,RecorderActivity.class);
			if(etStreamUrl.getText().toString() != null && !"".equals(etStreamUrl.getText().toString())){
				streamData.setPushStream(etStreamUrl.getText().toString());
				intent.putExtra("streamUrl", streamData.getPushStream());
				intent.putExtra("pushName", streamData.getPushStream());
			}else{
				intent.putExtra("streamUrl", DEFAULT_PUSHSTREAM);
				intent.putExtra("pushName", DEFAULT_PUSHSTREAM);
			}
			break;
		case 2: 
			// 乐视云直播界面
			intent = new Intent(this,RecorderLetvActivity.class);
			// 获取用户ID，APP KEY ， 活动ID 这三个参数，并且直接把这三个参数传递给RecorderLetvActivity 中去
			if(etLetvAppKey.getText().toString() != null && !"".equals(etLetvAppKey.getText().toString())){
				letvStreamData.setAppKey(etLetvAppKey.getText().toString());
				intent.putExtra("letvAppKey", letvStreamData.getAppKey());
			}else{
				intent.putExtra("letvAppKey", DEFAULT_LETV_APPKEY);
			}
			if(etLetvStreamID.getText().toString() != null && !"".equals(etLetvStreamID.getText().toString())){
				letvStreamData.setStreamId(etLetvStreamID.getText().toString());
				intent.putExtra("letvStreamID", letvStreamData.getStreamId());
			}else{
				intent.putExtra("letvStreamID", DEFAULT_LETV_STREAMID);
			}
			if(etLetvUserId.getText().toString() != null && !"".equals(etLetvUserId.getText().toString())){
				letvStreamData.setUserID(etLetvUserId.getText().toString());
				intent.putExtra("letvUserId", letvStreamData.getUserID());
			}else{
				intent.putExtra("letvUserId", DEFAULT_LETV_USERID);
			}
			break;
		}
//		intent = new Intent(this,RecorderSurfaceViewActivity.class);
		// 获取当前 横屏还是竖屏推流。并且把参数传递给推流界面
		intent.putExtra("isVertical", letvStreamData.isVertical());
		// 启动推流界面
		startActivity(intent);
	}
	/**
	 * **移动直播 **    生成一个 推流地址/播放地址 。这两个地址生成规则特别像
	 * @param isPush 当前需要生成的是推流地址还是播放地址，true 推流地址 ，false 播放地址
	 * @return 返回生成的地址
	 */
	private String createStreamUrl(boolean isPush) {
		// 格式化，获取时间参数 。注意，当你在创建移动直播应用时，如果开启推流防盗链或播放防盗链 。那么你必须保证这个时间和中国网络时间一致
		String tm = format.format(new Date());
		// 获取无推流地址时 流名称，推流域名，签名密钥 三个参数
		String streamName = etStreamId.getText().toString().trim();
		String domainName = etDomainName.getText().toString().trim();
		String appkey = etAppKey.getText().toString().trim();
		etStreamId.setText(streamName);
		etDomainName.setText(domainName);
		etAppKey.setText(appkey);

		if(domainName == null || domainName.equals("")){
			domainName = DEFAULT_DOMAINNAME;
		}
		if(streamName == null || streamName.equals("")){
			streamName = default_streamid;
		}
		if(appkey == null || appkey.equals("")){
			appkey = DEFAULT_APPKEY;
		}
		// 生成 sign值,在播放和推流时生成的sign值不一样
		String sign ;
		if(isPush){
			// 生成推流的 sign 值 。把流名称 ，时间，签名密钥 通过MD5 算法加密
			sign = MD5Utls.stringToMD5(streamName+tm+appkey);
		}else{
			// 生成播放 的sign 值，把流名称，时间，签名密钥，和"lecloud" 通过MD5 算法加密
			sign = MD5Utls.stringToMD5(streamName+tm+appkey+"lecloud");
			// 获取到播放域名。现在播放域名的获取规则是 把推流域名中的 push 替换为pull
			domainName = domainName.replaceAll("push", "pull");
		}
		// 拼接出一个rtmp 的地址
		return "rtmp://"+domainName+"/live/"+streamName+"?tm="+tm+"&sign="+sign;
	}
	/**
	 * 添加 无推流地址的界面
	 * @param v  无推流地址  按钮   button
	 */
	private void addNoStreamView(View v){
		chackColor();
		currButton = v;
		currButton.setBackgroundResource(R.drawable.main_title_button_lift_push);
		((Button)currButton).setTextColor(0xff333333);
		meddle.removeAllViews();
		meddle.addView(llNoStream);
		type = 0;
		btnIsScreen = (Button) llNoStream.findViewById(R.id.btn_turn);
		btnIsScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(createStreamData.isVertical()){
					btnIsScreen.setBackgroundResource(R.drawable.turn_on);
					createStreamData.setVertical(false);
				}else{
					btnIsScreen.setBackgroundResource(R.drawable.turn_off);
					createStreamData.setVertical(true);
				}
			}
		});

		etDomainName.setHint(DEFAULT_DOMAINNAME);
		etAppKey.setHint(DEFAULT_APPKEY);
		default_streamid = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		etStreamId.setHint(default_streamid);
		if(createStreamData.getDomainName() != null )  {
			etDomainName.setText(createStreamData.getDomainName());
		}
		if(createStreamData.getAppKey() != null) {
			etAppKey.setText(createStreamData.getAppKey());
		}
		if(createStreamData.getStreamId() != null){
			etStreamId.setText(createStreamData.getStreamId());
		}
		if(!createStreamData.isVertical()){
			btnIsScreen.setBackgroundResource(R.drawable.turn_on);
		}else{
			btnIsScreen.setBackgroundResource(R.drawable.turn_off);
		}
	}
	/**
	 * 添加有推流地址界面
	 * @param v “有推流地址” button
	 */
	private void addStreamView(View v){
		chackColor();
		currButton = v;
		currButton.setBackgroundResource(R.drawable.main_title_button_middle_push);
		((Button)currButton).setTextColor(0xff333333);
		meddle.removeAllViews();
		meddle.addView(llStream);
		type = 1;
		btnIsScreen = (Button) llStream.findViewById(R.id.btn_turn);
		btnIsScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(createStreamData.isVertical()){
					btnIsScreen.setBackgroundResource(R.drawable.turn_on);
					createStreamData.setVertical(false);
				}else{
					btnIsScreen.setBackgroundResource(R.drawable.turn_off);
					createStreamData.setVertical(true);
				}
			}
		});

		etStreamUrl.setHint(DEFAULT_PUSHSTREAM);
		if(streamData.getPushStream() != null )  {
			etStreamUrl.setText(streamData.getPushStream());
		}
		if(!streamData.isVertical()){
			btnIsScreen.setBackgroundResource(R.drawable.turn_on);
		}else{
			btnIsScreen.setBackgroundResource(R.drawable.turn_off);
		}
	}
	/**
	 * 添加乐视云直播 界面
	 * @param v "乐视云直播" 按钮 button
	 */
	private void addLetvView(View v){
		chackColor();
		currButton = v;
		currButton.setBackgroundResource(R.drawable.main_title_button_right_push);
		((Button)currButton).setTextColor(0xff333333);
		meddle.removeAllViews();
		meddle.addView(llLetvStream);
		type = 2;
		btnIsScreen = (Button) llLetvStream.findViewById(R.id.btn_turn);
		btnIsScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(createStreamData.isVertical()){
					btnIsScreen.setBackgroundResource(R.drawable.turn_on);
					createStreamData.setVertical(false);
				}else{
					btnIsScreen.setBackgroundResource(R.drawable.turn_off);
					createStreamData.setVertical(true);
				}
			}
		});

		etLetvUserId.setHint(DEFAULT_LETV_USERID);
		etLetvAppKey.setHint(DEFAULT_LETV_APPKEY);
		etLetvStreamID.setHint(DEFAULT_LETV_STREAMID);
		if(letvStreamData.getUserID() != null ) {
			etLetvUserId.setText(letvStreamData.getUserID());
		}
		if(letvStreamData.getAppKey() != null) {
			etLetvAppKey.setText(letvStreamData.getAppKey());
		}
		if(letvStreamData.getStreamId() != null) {
			etLetvStreamID.setText(letvStreamData.getStreamId());
		}
		if(!createStreamData.isVertical()){
			btnIsScreen.setBackgroundResource(R.drawable.turn_on);
		}else{
			btnIsScreen.setBackgroundResource(R.drawable.turn_off);
		}
	}
	/**
	 *  无推流地址 ， 有推流地址 ，乐视云直播    三个按钮的颜色变化
	 */
	private void chackColor(){
		if(currButton != null){
			switch (currButton.getId()) {
			case R.id.btn_no_push_stream:
				currButton.setBackgroundResource(R.drawable.main_title_button_lift);
				((Button)currButton).setTextColor(0xffffffff);
				break;
			case R.id.btn_push_stream:
				currButton.setBackgroundResource(R.drawable.main_title_button_middle);
				((Button)currButton).setTextColor(0xffffffff);
				break;
			case R.id.btn_letv_stream:
				currButton.setBackgroundResource(R.drawable.main_title_button_right);
				((Button)currButton).setTextColor(0xffffffff);
				break;
			}
		}
	}

}
