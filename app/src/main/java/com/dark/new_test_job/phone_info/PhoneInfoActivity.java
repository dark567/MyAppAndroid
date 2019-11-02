package com.dark.new_test_job.phone_info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;
import com.dark.new_test_job.R;

public class PhoneInfoActivity extends Activity {
	private TextView text;
	private TelephonyManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.menu_info);

		this.text = (TextView)findViewById(R.id.text_phoneinfo);

		manager = (TelephonyManager)getSystemService(
				Context.TELEPHONY_SERVICE);

//		TextView helloTextView = (TextView)findViewById(R.id.textView2);
//		helloTextView.setText("555!");

		text.append("\nCallState:\t" +
				convertCallStateToString(manager.getCallState()));
//        text.append("\nDevice ID:\t" +
//        		manager.getDeviceId());
//		text.append("\nDevice Software Version:\t" +
//        		manager.getDeviceSoftwareVersion());
//        text.append("\nLine1 Number:\t" +
//        		manager.getLine1Number());
		text.append("\nNetwork Type:\t" +
				convertNetworkTypeToString(manager.getNetworkType()));
		text.append("\nNetwork Country ISO:\t" +
				manager.getNetworkCountryIso());
		text.append("\nNetwork Operator:\t" +
				manager.getNetworkOperator());
		text.append("\nNetwork Operator Name:\t" +
				manager.getNetworkOperatorName());
//		text.append("\nPhone Type:\t" +
//        		convertPhoneTypeToString(manager.getPhoneType()));
		text.append("\nData Activity:\t" +
				convertDataActivityToString(manager.getDataActivity()));
		text.append("\nData State:\t" +
				convertDataConnStateToString(manager.getDataState()));
//		text.append("\nSubscriber ID:\t" +
//        		manager.getSubscriberId());
//		text.append("\nVoice Mail Alpha Tag:\t" +
//        		manager.getVoiceMailAlphaTag());
//        text.append("\nVoice Mail Number:\t" +
//        		manager.getVoiceMailNumber());
		text.append("\nIcc Card:\t" +
				manager.hasIccCard());
		text.append("\nNetwork Roaming:\t" +
				manager.isNetworkRoaming());
//
//        GsmCellLocation gsmCell = (GsmCellLocation)manager.getCellLocation();
//        if (gsmCell != null) {
//        	text.append("\nGSM Cell Location:");
//        	text.append("\n\tCID:\t" + gsmCell.getCid());
//        	text.append("\n\tLAC:\t" + gsmCell.getLac());
//        }
	}

	private String convertCallStateToString(int callState) {
		switch (callState) {
			case TelephonyManager.CALL_STATE_IDLE:
				return "IDLE";
			case TelephonyManager.CALL_STATE_OFFHOOK:
				return "OFFHOOK";
			case TelephonyManager.CALL_STATE_RINGING:
				return "RINGING";
			default:
				return "Not defined";
		}
	}

	private String convertNetworkTypeToString(int networkType) {
		switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "1xRTT";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "EVDO revision 0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "EVDO revision A";
			//case TelephonyManager.NETWORK_TYPE_EVDO_B:
			//    return "EVDO revision B";
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "HSUPA";
			//case TelephonyManager.NETWORK_TYPE_IDEN:
			//    return "iDen";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return "Unknown";
			default:
				return "Not defined";
		}
	}

	private String convertDataActivityToString(int dataActivity) {
		switch (dataActivity) {
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				return "Dormant";
			case TelephonyManager.DATA_ACTIVITY_IN:
				return "In";
			case TelephonyManager.DATA_ACTIVITY_INOUT:
				return "In-out";
			case TelephonyManager.DATA_ACTIVITY_NONE:
				return "None";
			case TelephonyManager.DATA_ACTIVITY_OUT:
				return "Out";
			default:
				return "Not defined";
		}
	}

	private String convertDataConnStateToString(int dataConnState) {
		switch (dataConnState) {
			case TelephonyManager.DATA_CONNECTED:
				return "Data connected";
			case TelephonyManager.DATA_CONNECTING:
				return "Data connecting";
			case TelephonyManager.DATA_DISCONNECTED:
				return "Data suspended";
			case TelephonyManager.DATA_SUSPENDED:
				return "Data suspended";
			default:
				return "Not defined";
		}
	}

	private String convertPhoneTypeToString(int phoneType) {
		switch (phoneType) {
			case TelephonyManager.PHONE_TYPE_GSM:
				return "GSM";
			case TelephonyManager.PHONE_TYPE_CDMA:
				return "CDMA";
			case TelephonyManager.PHONE_TYPE_NONE:
				return "NONE";
			default:
				return "Not defined";
		}
	}
}