package com.redray.khadoomhome.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Activities.Activition_Mobile_Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    String active_code;


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    //  String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    // String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                  //  String str2 = "<123456>   مرحبا، شريكنا العزيز .. نأمل إستخدام هذا الكود : لتفعيل حسابكم فى خدووم";
                 //   String str = "Welcome, Please Active your khadoom App Account using this code : <123456>";

                    Matcher m = Pattern.compile("<(.+?)>").matcher(message);
                    while(m.find()) {
                        active_code = m.group(1);
                        Log.d("tessst",active_code);
                    }


                    Log.d("SmsReceiver",  message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+   ", message: " + message, duration);
                    toast.show();

                    //check if message is for active 
                    if (message.contains(context.getString(R.string.active_sms_word))) {
                        // check if iam on activition activity
                        if (Activition_Mobile_Activity.active) {

//                            Intent local = new Intent();
//                            local.setAction("com.redray.khadoomhome.USER.Activities.Activition_Mobile_Activity");
//                            local.putExtra("Active_Num", active_code);
//                            context.sendBroadcast(local);

                        }
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}