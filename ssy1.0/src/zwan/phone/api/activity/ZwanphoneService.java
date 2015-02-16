package zwan.phone.api.activity;

import java.io.FileInputStream;
import java.io.IOException;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.preference.PreferenceManager;
import cn.com.zwan.phone.ZwanphoneManager;
import cn.com.zwan.phone.listeners.ZwanphoneOnRegistrationStateChangedListener;
import cn.com.zwan.phone.listeners.ZwanphoneServiceListener;
import cn.com.zwan.phone.mediastream.Log;


public final class ZwanphoneService extends Service implements ZwanphoneServiceListener,ZwanphoneOnRegistrationStateChangedListener{

	private WifiManager mWifiManager ;
	private WifiLock mWifiLock ;
	private static ZwanphoneService instance;

	@Override
	public void onCreate() {
		super.onCreate();

		ZwanphoneManager.createAndStart(this);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = mWifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, this.getPackageName()+"-wifi-call-lock");
		mWifiLock.setReferenceCounted(false);
		instance = this; 

		ZwanphoneManager.addListener(this);

	}

	public static boolean isReady() {
		return instance!=null;
	}

	public static ZwanphoneService instance()  {
		if (isReady()) return instance;
		throw new RuntimeException("LinphoneService not instantiated yet");
	}

	@Override
	public synchronized void onDestroy() {
		instance = null;
		ZwanphoneManager.destroy();

		mWifiLock.release();
		super.onDestroy();
	}

	@Override
	public void onGlobalStateChanged(GlobalState state, String message) {

	}

	@Override
	public void onCallStateChanged(LinphoneCall call, State state,
			String message) {

	}

	@Override
	public void onCallEncryptionChanged(LinphoneCall call, boolean encrypted,
			String authenticationToken) {

	}

	@Override
	public void tryingNewOutgoingCallButCannotGetCallParameters() {

	}

	@Override
	public void tryingNewOutgoingCallButWrongDestinationAddress() {
		System.out.println("------tryingNewOutgoingCallButWrongDestinationAddress------------");
	}

	@Override
	public void tryingNewOutgoingCallButAlreadyInCall() {
		System.out.println("------tryingNewOutgoingCallButAlreadyInCall------------");
	}

	@Override
	public void onRegistrationStateChanged(RegistrationState state,
			String message) {

	}

	@Override
	public void onRingerPlayerCreated(MediaPlayer mRingerPlayer) {
		String uriString = android.provider.Settings.System.DEFAULT_RINGTONE_URI.toString();
		try {
			if (uriString.startsWith("content://")) {
				mRingerPlayer.setDataSource(this, Uri.parse(uriString));
			} else {
				FileInputStream fis = new FileInputStream(uriString);
				mRingerPlayer.setDataSource(fis.getFD());
				fis.close();
			}
		} catch (IOException e) {
			Log.e(e, "Cannot set ringtone");
		}
	}

	@Override
	public void onDisplayStatus(String message) {

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onRegistrationStateChanged(RegistrationState state) {
		if (state == RegistrationState.RegistrationOk) {
			System.out.println("------ZwanphoneService-----RegistrationState.RegistrationOk");
		}
		if (state == RegistrationState.RegistrationFailed || state == RegistrationState.RegistrationCleared) {
			System.out.println("------ZwanphoneService-----RegistrationState.RegistrationFailed");
		}
		if (state == RegistrationState.RegistrationNone) {
			System.out.println("------ZwanphoneService-----RegistrationState.RegistrationNone");
		}
	}

}

