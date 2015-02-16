package com.shanshengyuan.client;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.shanshengyuan.client.service.PushService;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * Created by android on 13-12-12.
 */
public class PushMessageReceiver extends FrontiaPushMessageReceiver{
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
	public static final String BAIDU_PUSH_USER_ID = "bd_user_id";

	// 处理推�?成功后的逻辑controller
	//public static PushController mController = null;
	
	//public static UserController mUController;

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的�?绑定请求的结果�?过onBind返回�?如果您需要用单播推�?，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送�?
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode�?时为null
	 * @param userId
	 *            应用user id。errorCode�?时为null
	 * @param channelId
	 *            应用channel id。errorCode�?时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */

	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Log.d(TAG, "onBind:" + responseString);
		Log.d(TAG, "onBind:" + userId);
		// 把userid放入缓存
		Editor editor = context.getSharedPreferences(PushService.TAG,
				Context.MODE_PRIVATE).edit();
		if(!StringUtils.isEmpty(userId)){
			editor.putString(BAIDU_PUSH_USER_ID, userId);
			editor.commit();
			Log.d(TAG, "onBind:" + "111111111111111111111111");
			Config.BAIDU_USER = userId;
//			if(mUController==null){
//				mUController = new UserController(BaseApplication.context);
//			}
//			mUController.pushDeviceInfo();
		}
	}
	
	
	/**
	 * 接收透传消息的函数�?
	 * 
	 * @param context
	 *            上下�?	 * @param message
	 *            推�?的消�?	 * @param customContentString
	 *            自定义内�?为空或�?json字符�?	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.e(TAG, messageString);

		doAction(context, message);

	}

	/**
	 * 接收通知点击的函数�?注：推�?通知被用户点击前，应用无法�?过接口获取�?知的内容�?	 * 
	 * @param context
	 *            上下�?	 * @param title
	 *            推�?的�?知的标题
	 * @param description
	 *            推�?的�?知的描述
	 * @param customContentString
	 *            自定义内容，为空或�?json字符�?	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {

		Log.i(TAG, "clicked title:" + title);
		Log.i(TAG, "clicked description:" + description);
		Log.i(TAG, "clicked customContentString:" + customContentString);

		doAction(context, customContentString);
	}

	/**
	 * setTags() 的回调函数�?
	 * 
	 * @param context
	 *            上下�?	 * @param errorCode
	 *            错误码�?0表示某些tag已经设置成功；非0表示�?��tag的设置均失败�?	 * @param sucessTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {

		// Log.i(TAG, "onSetTags sucessTag:" + sucessTags.size());
		// Log.i(TAG, "onSetTags faildTag:" + failTags.size());

	}

	/**
	 * delTags() 的回调函数�?
	 * 
	 * @param context
	 *            上下�?	 * @param errorCode
	 *            错误码�?0表示某些tag已经删除成功；非0表示�?��tag均删除失败�?
	 * @param sucessTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {

		Log.i(TAG, "onDelTags");

	}

	/**
	 * listTags() 的回调函数�?
	 * 
	 * @param context
	 *            上下�?	 * @param errorCode
	 *            错误码�?0表示列举tag成功；非0表示失败�?	 * @param tags
	 *            当前应用设置的所有tag�?	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {

		Log.i(TAG, "onListTags");

	}
	
	/**
	 * PushManager.stopWork() 的回调函数�?
	 * 
	 * @param context
	 *            上下�?	 * @param errorCode
	 *            错误码�?0表示从云推�?解绑定成功；�?表示失败�?	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {

		Log.i(TAG, "onUnbind");

	}

	public void doAction(Context context, String message) {

//		if (mController == null) {
//			mController = new PushController(context);
//		}
//		Log.i("123123", "get message:" + message);
//		// 在此处理推�?类型
//		if (StringUtils.isEmpty(message)) {
//			mController.doPushDefault(context);
//		}
//		try {
//			JSONObject pushObject = new JSONObject(message);
//			JSONObject dataobj = pushObject.getJSONObject("data");
//			String pushType = dataobj.getString("type");
//			Log.i(TAG, "get type:" + pushType);
//
//			if (pushType.equals("1")) {
//				// 消息详情�?				mController.doPushDefault(context, pushObject);
//			} else if (pushType.equals("3")) {
//				// 网页
//				mController.doPushNotice(context, pushObject);
//
//			} else if (pushType.equals("2")) {
//				// 呼呼
//				mController.doWebIm(context, pushObject);
//
//			} else if (pushType.equals("8")) {
//				mController.doPushDefault(context, pushObject);
//			} else if(pushType.equals("5")){
//				mController.doServicePage(context, pushObject);
//				// Intent it = new Intent(getApplicationContext(),
//				// NoticeCenterActivity.class);
//				// JSONObject msgObject = pushObject.getJSONObject("aps");
//				// String msg = msgObject.getString("alert");
//				// showNotification(msg, it);
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}


}
