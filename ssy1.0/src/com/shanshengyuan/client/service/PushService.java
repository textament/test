package com.shanshengyuan.client.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;

import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttPersistence;
import com.ibm.mqtt.MqttPersistenceException;
import com.ibm.mqtt.MqttSimpleCallback;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.utils.Log;

/* 
 * PushService that does all of the work.
 * Most of the logic is borrowed from KeepAliveService.
 * http://code.google.com/p/android-random/source/browse/trunk/TestKeepAlive/src/org/devtcg/demo/keepalive/KeepAliveService.java?r=219
 */
public class PushService extends Service {
//	// this is the log tag
	public static final String TAG = PushService.class.getSimpleName();
//
	// the IP address, where your MQTT broker is running.
	private static final String MQTT_HOST = "push.api.zhubajie.com"; // "192.168.5.73";
	// //;
	// the port at which the broker is running.
	private static int MQTT_BROKER_PORT_NUM = 1883;
	// Let's not use the MQTT persistence.
	private static MqttPersistence MQTT_PERSISTENCE = null;
	// We don't need to remember any state between the connections, so we use a
	// clean start.
	private static boolean MQTT_CLEAN_START = true;
	// Let's set the internal keep alive for MQTT to 15 mins. I haven't tested
	// this value much. It could probably be increased.
	private static short MQTT_KEEP_ALIVE = 60 ;
	// Set quality of services to 0 (at most once delivery), since we don't want
	// push notifications
	// arrive more than once. However, this means that some messages might get
	// lost (delivery is not guaranteed)
	private static int[] MQTT_QUALITIES_OF_SERVICE = { 0 };
	private static int MQTT_QUALITY_OF_SERVICE = 0;
	// The broker should not retain any messages.
	private static boolean MQTT_RETAINED_PUBLISH = false;

	// MQTT client ID, which is given the broker. In this example, I also use
	// this for the topic header.
	// You can use this to run push notifications for multiple apps with one
	// MQTT broker.
	public static String MQTT_CLIENT_ID = "tokudu";

	// These are the actions for the service (name are descriptive enough)
	private static final String ACTION_START = MQTT_CLIENT_ID + ".START";
	private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
	private static final String ACTION_KEEPALIVE = MQTT_CLIENT_ID
			+ ".KEEP_ALIVE";
	private static final String ACTION_RECONNECT = MQTT_CLIENT_ID
			+ ".RECONNECT";

	// Connectivity manager to determining, when the phone loses connection
	private ConnectivityManager mConnMan;
	// Notification manager to displaying arrived push notifications
	private NotificationManager mNotifMan;

	// Whether or not the service has been started.
	private boolean mStarted;

	// This the application level keep-alive interval, that is used by the
	// AlarmManager
	// to keep the connection active, even when the device goes to sleep.
	private static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;

	// Retry intervals, when the connection is lost.
	private static final long INITIAL_RETRY_INTERVAL = 1000 * 10;
	private static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;

	// Preferences instance
	private SharedPreferences mPrefs;
	// We store in the preferences, whether or not the service has been started
	public static final String PREF_STARTED = "isStarted";
	// We also store the deviceID (target)
	public static final String PREF_DEVICE_ID = "deviceID";
	// We store the last retry interval
	public static final String PREF_RETRY = "retryInterval";

	// Notification title
	public static String NOTIF_TITLE = "猪八戒买家版";
	// Notification id
	private static final int NOTIF_CONNECTED = 2;
	private static final int NOTIF_CONNECTED_JIAOGAO = 3;

	// This is the instance of an MQTT connection.
	private MQTTConnection mConnection;
	private long mStartTime;

	private final static int MSG_FINISH = 6; // 威客确认工作完成
	private final static int MSG_RELEASE_SUCCESS = 7; // 需求发布成功
	private final static int MSG_RELEASE_FAILED = 8; // 需求发布失败
	private final static int MSG_SUBMIT_END = 9; // 交稿即将结束

	private final static int MSG_IM = 2; // 呼呼

	// Static method to start the service
	public static void actionStart(Context ctx) {
		Intent i = new Intent(ctx, PushService.class);
		i.setAction(ACTION_START);
		ctx.startService(i);
	}

	// Static method to stop the service
	public static void actionStop(Context ctx) {
		Intent i = new Intent(ctx, PushService.class);
		i.setAction(ACTION_STOP);
		ctx.startService(i);
	}

	// Static method to send a keep alive message
	public static void actionPing(Context ctx) {
		Intent i = new Intent(ctx, PushService.class);
		i.setAction(ACTION_KEEPALIVE);
		ctx.startService(i);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		log("Creating service");
		mStartTime = System.currentTimeMillis();

		// Get instances of preferences, connectivity manager and notification
		// manager
		mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
		mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		/*
		 * If our process was reaped by the system for any reason we need to
		 * restore our state with merely a call to onCreate. We record the last
		 * "started" value and restore it here if necessary.
		 */
		handleCrashedService();

	}

	// This method does any necessary clean-up need in case the server has been
	// destroyed by the system
	// and then restarted
	private void handleCrashedService() {
		if (wasStarted() == true) {
			log("Handling crashed service...");
			// stop the keep alives
			stopKeepAlives();

			// Do a clean start
			start();
		}
	}

	@Override
	public void onDestroy() {
		log("Service destroyed (started=" + mStarted + ")");

		// Stop the services, if it has been started
		if (mStarted == true) {
			stop();
		}

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		log("Service started with intent=" + intent);
 
		if (intent == null) {
			stop();
			stopSelf();
			return;
		}

		// Do an appropriate action based on the intent.
		if (intent.getAction().equals(ACTION_STOP) == true) {
			stop();
			stopSelf();
		} else if (intent.getAction().equals(ACTION_START) == true) {
			start();
		} else if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
			keepAlive();
		} else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
			if (isNetworkAvailable()) {
				reconnectIfNecessary();
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		log("Service started with intent=" + intent);

		if (intent == null) {
			stop();
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}

		// Do an appropriate action based on the intent.
		if (intent.getAction().equals(ACTION_STOP) == true) {
			stop();
			stopSelf();
		} else if (intent.getAction().equals(ACTION_START) == true) {
			start();
		} else if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
			keepAlive();
		} else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
			if (isNetworkAvailable()) {
				reconnectIfNecessary();
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	

	// log helper function
	private void log(String message) {
		log(message, null);
	}

	private void log(String message, Throwable e) {
		if (e != null) {
			Log.e(TAG, message, e);

		} else {
			Log.i(TAG, message);
		}

	}

	// Reads whether or not the service has been started from the preferences
	private boolean wasStarted() {
		return mPrefs.getBoolean(PREF_STARTED, false);
	}

	// Sets whether or not the services has been started in the preferences.
	private void setStarted(boolean started) {
		mPrefs.edit().putBoolean(PREF_STARTED, started).commit();
		mStarted = started;
	}

	private synchronized void start() {
		log("Starting service...");

		// Do nothing, if the service is already running.
		if (mStarted == true) {
			Log.w(TAG, "Attempt to start connection that is already active");
			return;
		}

		// Establish an MQTT connection
		connect();

		// Register a connectivity listener
		registerReceiver(mConnectivityChanged, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private synchronized void stop() {
		// Do nothing, if the service is not running.
		if (mStarted == false) {
			Log.w(TAG, "Attempt to stop connection not active.");
			return;
		}

		// Save stopped state in the preferences
		setStarted(false);

		// Remove the connectivity receiver
		unregisterReceiver(mConnectivityChanged);
		// Any existing reconnect timers should be removed, since we explicitly
		// stopping the service.
		cancelReconnect();

		// Destroy the MQTT connection if there is one
		if (mConnection != null) {
			mConnection.disconnect();
			mConnection = null;
		}
	}

	//
	private synchronized void connect() {
		log("Connecting...");
		// fetch the device ID from the preferences.
		String deviceID = mPrefs.getString(PREF_DEVICE_ID, null);
		// Create a new connection only if the device id is not NULL
		if (deviceID == null) {
			log("Device ID not found.");
		} else {
			try {

				mConnection = new MQTTConnection(MQTT_HOST, deviceID);
			} catch (MqttException e) {
				// Schedule a reconnect, if we failed to connect
				log("MqttException: "
						+ (e.getMessage() != null ? e.getMessage() : "NULL"));
				if (isNetworkAvailable()) {
					scheduleReconnect(mStartTime);
				}
			}
			setStarted(true);
		}
	}

	private synchronized void keepAlive() {
		try {
			// Send a keep alive, if there is a connection.
			if (mStarted == true && mConnection != null) {
				mConnection.sendKeepAlive();
			}
		} catch (MqttException e) {
			log("MqttException: "
					+ (e.getMessage() != null ? e.getMessage() : "NULL"), e);

			mConnection.disconnect();
			mConnection = null;
			cancelReconnect();
		}
	}

	// Schedule application level keep-alives using the AlarmManager
	private void startKeepAlives() {
		Intent i = new Intent();
		i.setClass(this, PushService.class);
		i.setAction(ACTION_KEEPALIVE);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + KEEP_ALIVE_INTERVAL,
				KEEP_ALIVE_INTERVAL, pi);
	}

	// Remove all scheduled keep alives
	private void stopKeepAlives() {
		Intent i = new Intent();
		i.setClass(this, PushService.class);
		i.setAction(ACTION_KEEPALIVE);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.cancel(pi);
	}

	// We schedule a reconnect based on the starttime of the service
	public void scheduleReconnect(long startTime) {
		// the last keep-alive interval
		long interval = mPrefs.getLong(PREF_RETRY, INITIAL_RETRY_INTERVAL);

		// Calculate the elapsed time since the start
		long now = System.currentTimeMillis();
		long elapsed = now - startTime;

		// Set an appropriate interval based on the elapsed time since start
		if (elapsed < interval) {
			interval = Math.min(interval * 4, MAXIMUM_RETRY_INTERVAL);
		} else {
			interval = INITIAL_RETRY_INTERVAL;
		}

		log("Rescheduling connection in " + interval + "ms.");

		// Save the new internval
		mPrefs.edit().putLong(PREF_RETRY, interval).commit();

		// Schedule a reconnect using the alarm manager.
		Intent i = new Intent();
		i.setClass(this, PushService.class);
		i.setAction(ACTION_RECONNECT);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
	}

	// Remove the scheduled reconnect
	public void cancelReconnect() {
		Intent i = new Intent();
		i.setClass(this, PushService.class);
		i.setAction(ACTION_RECONNECT);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.cancel(pi);
	}

	private synchronized void reconnectIfNecessary() {
		if (mStarted == true && mConnection == null) {
			log("Reconnecting...");
			connect();
		}
	}

	// This receiver listeners for network changes and updates the MQTT
	// connection
	// accordingly
	private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get network info
			NetworkInfo info = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

			// Is there connectivity?
			boolean hasConnectivity = (info != null && info.isConnected()) ? true
					: false;

			log("Connectivity changed: connected=" + hasConnectivity);

			if (hasConnectivity) {
				reconnectIfNecessary();
			} else if (mConnection != null) {
				// if there no connectivity, make sure MQTT connection is
				// destroyed
				mConnection.disconnect();
				cancelReconnect();
				mConnection = null;
			}
		}
	};

	// Display the topbar notification
	private void showNotification(String text, Intent it) {
		Notification n = new Notification();

		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;

		//n.defaults = Notification.DEFAULT_ALL;

		n.icon = R.drawable.ic_launcher;
		n.when = System.currentTimeMillis();
		// n.sound = Uri.parse("android.resource://" + this.getPackageName() +"/" + R.raw.push);
		 n.vibrate = null;
		 n.tickerText = text;

		// Simply open the parent activity
		PendingIntent pi = PendingIntent.getActivity(this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Change the name of the notification here
		n.setLatestEventInfo(this, NOTIF_TITLE, text, pi);

		mNotifMan.notify(NOTIF_CONNECTED, n);
	}
	
	private void showNotificationNoLast(String text, Intent it) {
		Notification n = new Notification();

		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;

		//n.defaults = Notification.DEFAULT_ALL;

		n.icon = R.drawable.ic_launcher;
		n.when = System.currentTimeMillis();
//		 n.sound = Uri.parse("android.resource://" + this.getPackageName() +
//		 "/" + R.raw.push);
		 n.vibrate = null;
		 n.tickerText = text;

		// Simply open the parent activity
		PendingIntent pi = PendingIntent.getActivity(this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Change the name of the notification here
		n.setLatestEventInfo(this, NOTIF_TITLE, text, pi);

		mNotifMan.notify(NOTIF_CONNECTED_JIAOGAO, n);
	}

	// Check if we are online
	private boolean isNetworkAvailable() {
		NetworkInfo info = mConnMan.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}

		return info.isConnected();
	}

	private void doWebIm(JSONObject pushObject) {
		JSONObject msgObject;
//		try {
//			if(UserController.getUser()!=null){
//				msgObject = pushObject.getJSONObject("aps");
//				String msg = msgObject.getString("alert");
//				 String callUserId = msgObject.getString("id");
//				 String callName = msgObject.getString("fromname");
//				 if(StringUtils.isEmpty(callName)){
//					 callName ="未知名称";
//				 }
//				Intent it = new Intent(getApplicationContext(), BuyerIMActivity.class);
//				Bundle bundle = new Bundle();
//				
//				bundle.putString(BuyerIMActivity.KEY_TO_USERID, callUserId);
//				bundle.putString(BuyerIMActivity.KEY_TO_NICK, callName);
//				bundle.putString(BuyerIMActivity.KEY_FROM_USERID, Settings
//						.loadUserInfo().getUser_id());
//				bundle.putString(BuyerIMActivity.KEY_FROM_NICK, Settings.loadUserInfo()
//						.getNickname());
//				bundle.putString(BuyerIMActivity.KEY_USER_KEY, "das");
//				it.putExtras(bundle);
//				showNotification(msg, it);
//			}
			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

	}

	private void doPushDefault(JSONObject pushObject) {

		try {
//			BaseApplication.isPushGaoJian = true;
//			JSONObject msgObject = pushObject.getJSONObject("aps");
//
//			String msg = msgObject.getString("alert");
//			// String userId = msgObject.getString("user_id");
//			String workId = msgObject.getString("work_id");
//			String taskId = msgObject.getString("task_id");
//			Intent it = new Intent(getApplicationContext(),
//					WorkFinalActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("work_id", workId);
//			bundle.putString("task_id", taskId);
//			bundle.putBoolean("from_push", true);
//			it.putExtras(bundle);
//			showNotificationNoLast(msg, it);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doPushTaskInfo(JSONObject pushObject) {
		// String taskId = "";
		// try {
		// JSONObject msgObject = pushObject.getJSONObject("aps");
		// taskId = msgObject.getString("task_id");
		// ActivityManager manager = (ActivityManager)
		// getSystemService(Context.ACTIVITY_SERVICE);
		// String runClassName =
		// manager.getRunningTasks(1).get(0).topActivity.getShortClassName();
		//
		// if (".activity.DesktopActivity".equals(runClassName)) {
		// Intent intent = new
		// Intent().setAction(BaseApplication.RECEIVER_NEW_RIGHT)
		// .putExtra("type", 1);
		// this.sendBroadcast(intent);
		// } else {
		// String msg = msgObject.getString("alert");
		// Intent it = new Intent(getApplicationContext(),
		// TaskInfoActivity.class);
		// it.putExtra("task_id", taskId);
		// it.putExtra("is_bid", true);
		// showNotification(msg, it);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	private void doPushHireInfo(JSONObject pushObject) {

		// String taskId = "";
		// try {
		// JSONObject msgObject = pushObject.getJSONObject("aps");
		// taskId = msgObject.getString("task_id");
		// ActivityManager manager = (ActivityManager)
		// getSystemService(Context.ACTIVITY_SERVICE);
		// String runClassName =
		// manager.getRunningTasks(1).get(0).topActivity.getShortClassName();
		//
		// if (".activity.DesktopActivity".equals(runClassName)) {
		// Intent intent = new
		// Intent().setAction(BaseApplication.RECEIVER_NEW_RIGHT)
		// .putExtra("type", 2);
		// this.sendBroadcast(intent);
		// }
		//
		// else {
		// String msg = msgObject.getString("alert");
		// Intent it = new Intent(getApplicationContext(),
		// HireInfoActivity.class);
		// it.putExtra("task_id", taskId);
		// showNotification(msg, it);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	// This inner class is a wrapper on top of MQTT client.
	private class MQTTConnection extends AsyncTask<Object, Object, Object>
			implements MqttSimpleCallback {
		IMqttClient mqttClient = null;

		private String brokerHostName = null;
		private String initTopic = null;

		// Creates a new connection given the broker address and initial topic
		public MQTTConnection(String brokerHostName, String initTopic)
				throws MqttException {
			this.brokerHostName = brokerHostName;
			this.initTopic = initTopic;
			execute(0);
		}

		// Disconnect
		public void disconnect() {
			try {
				stopKeepAlives();
				if (mqttClient != null) {
					mqttClient.disconnect();
				}
			} catch (MqttPersistenceException e) {
				log("MqttException"
						+ (e.getMessage() != null ? e.getMessage() : " NULL"),
						e);
			}
		}

		/*
		 * Send a request to the message broker to be sent messages published
		 * with the specified topic name. Wildcards are allowed.
		 */
		private void subscribeToTopic(String topicName) throws MqttException {

			if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
				// quick sanity check - don't try and subscribe if we don't have
				// a connection
				log("Connection error" + "No connection");
			} else {
				String[] topics = { topicName };
				mqttClient.subscribe(topics, MQTT_QUALITIES_OF_SERVICE);
			}
		}

		/*
		 * Sends a message to the message broker, requesting that it be
		 * published to the specified topic.
		 */
		private void publishToTopic(String topicName, String message)
				throws MqttException {
			if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
				// quick sanity check - don't try and publish if we don't have
				// a connection
				log("No connection to public to");
			} else {
				mqttClient.publish(topicName, message.getBytes(),
						MQTT_QUALITY_OF_SERVICE, MQTT_RETAINED_PUBLISH);
			}
		}

		/*
		 * Called if the application loses it's connection to the message
		 * broker.
		 */
		public void connectionLost() throws Exception {
			log("Loss of connection" + "connection downed");
			stopKeepAlives();
			// null itself
			mConnection = null;
			if (isNetworkAvailable() == true) {
				reconnectIfNecessary();
			}
		}

		/*
		 * Called when we receive a message from the message broker.
		 */
		public void publishArrived(String topicName, byte[] payload, int qos,
				boolean retained) {
			// Show a notification
			String s = new String(payload);

			log("topicName:" + topicName);
			log("message: " + s);
			log("qos: " + qos);
			log("retained: " + retained);

			// 在此处理推送类型
			try {
				JSONObject pushObject = new JSONObject(s);
				String pushType = pushObject.getString("type");

				log("get type:" + pushType);
				log("get message:" + s);

				if (pushType.equals("2")) {
						doWebIm(pushObject);
					
				} else if(pushType.equals("8")) {
					doPushDefault(pushObject);
				}else {
//					Intent it = new Intent(getApplicationContext(), NoticeCenterActivity.class);
//		            JSONObject msgObject = pushObject.getJSONObject("aps");
//		            String msg = msgObject.getString("alert");
//		            showNotification(msg, it);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
    
		public void sendKeepAlive() throws MqttException {
			log("Sending keep alive");
			// publish to a keep-alive topic
			publishToTopic(MQTT_CLIENT_ID + "/keepalive",
					mPrefs.getString(PREF_DEVICE_ID, ""));
		}

		@Override
		protected Object doInBackground(Object... params) {

			log("create connection");
			String mqttConnSpec = "tcp://" + brokerHostName + "@"
					+ MQTT_BROKER_PORT_NUM;
			// Create the client and connect
			try {
				mqttClient = MqttClient.createMqttClient(mqttConnSpec,
						MQTT_PERSISTENCE);

				String clientID = MQTT_CLIENT_ID + "/"
						+ mPrefs.getString(PREF_DEVICE_ID, "");

				mqttClient.connect(clientID, MQTT_CLEAN_START, MQTT_KEEP_ALIVE);
				// register this client app has being able to receive messages
				mqttClient.registerSimpleHandler(this);
				// Subscribe to an initial topic, which is combination of client
				// ID
				// and device ID.
				initTopic = MQTT_CLIENT_ID + "/" + initTopic;
				subscribeToTopic(initTopic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
			log("Connection established to " + brokerHostName + " on topic "
					+ initTopic);

			// Save start time
			mStartTime = System.currentTimeMillis();
			// Star the keep-alives
			startKeepAlives();
			return null;

		}
	}

@Override
public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
}
}