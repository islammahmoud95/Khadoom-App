package com.redray.khadoomhome.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.redray.khadoomhome.PROVIDER.Activities.Order_Details;
import com.redray.khadoomhome.PROVIDER.Activities.Rating_View_Prov;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.Technical.Activities.Order_Details_tech;
import com.redray.khadoomhome.USER.Activities.Bill_Details_user;
import com.redray.khadoomhome.USER.Activities.Order_View_Details;
import com.redray.khadoomhome.USER.Activities.Post_Rating_Order;
import com.redray.khadoomhome.all_users.Activites.Genral_Notify_detail_activ;
import com.redray.khadoomhome.tickets.Activities.Ticket_details;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMsgService";

    String userType;
    String id_detail;
    String messageBody, title, notifytype;   // , type ;
    Intent intent;
    JSONObject obj;


    /*
     1 => TICKETS.
     2 => ORDER DONE FOR USER [Tech. finish order].
     3 => ORDER DONE FOR SP.
     4 => your initial bill No.1 has been created, please review it. [NORMAL USER] [Number 2 in Doc].
     5 => you have a new order number 1, please review it and accept it through 5 minutes [Number 1 in Doc].
     6 => after provider ASSIGN ORDER TO technical [3 IN DOC SECTION 2] & after provider change the technical [NUMBER six IN DOCS].
     7 => AFTER USER RATING HIS ORDER NOTIFY SP HAS THIS ORDER [YOUR ORDER NO. 1 HAS BEEN RATING].
     Zero => GENERAL NOTIFICATION BACK-END [REDIRECT ORDER 0 [NO REDIRECTION]].
     8 => WHEN ORDER ACCEPTED VIA SP USER NOTIFY (YOUR ORDER HAS BEEN ACCEPTED).
     */


    SessionManager sessionManager;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "From: " + remoteMessage.getData());


        sessionManager = new SessionManager(getApplicationContext());


        try {
            obj = new JSONObject(remoteMessage.getData());
            Log.d("responsenoti", remoteMessage.getData().toString());


            title = obj.getString("title");
            messageBody = obj.getString("msg");
            notifytype = obj.getString("notifyType");

            id_detail = obj.getString("id");
            userType = obj.getString("userType");


            if (userType.equals(sessionManager.getUser_Type()) && sessionManager.isLoggedIn()) {

                sendJobRequestNotification();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    private void sendJobRequestNotification() {

        int num = (int) System.currentTimeMillis();

        //if request vip accepted
        // type 1 for tickets from admin response
        switch (notifytype) {
            case "1": {

                intent = new Intent(this, Ticket_details.class);
                intent.putExtra("Ticket_ID", id_detail);

                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num /* ID of notification */, notificationBuilder.build());

                // for user to rate service after done from tech
                break;
            }
            case "2": {

                intent = new Intent(this, Post_Rating_Order.class);
                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());

                // for provider to view order details
                break;
            }
            case "3": {


                intent = new Intent(this, Order_Details.class);
                //  intent.putExtra("menuFragment", "favoritesMenuItem");
                //   intent.putExtra("CATEGORY_ID","0");
                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                // for user to view successful creation order details
                break;
            }
            case "4": {

                intent = new Intent(this, Bill_Details_user.class);
                //  intent.putExtra("menuFragment", "favoritesMenuItem");
                //   intent.putExtra("CATEGORY_ID","0");

                //notiform here for comment id
                intent.putExtra("BILL_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                // for provider to view order details
                break;
            }
            case "5": {


                intent = new Intent(this, Order_Details.class);
                //  intent.putExtra("menuFragment", "favoritesMenuItem");
                //   intent.putExtra("CATEGORY_ID","0");
                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                // sent to technical to notify him about receiving new order
                break;
            }
            case "6": {


                intent = new Intent(this, Order_Details_tech.class);

                // here notify form to get review text and send to activity to see review with rate
                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                // sent to provider to view rating has user did
                break;
            }
            case "7": {


                intent = new Intent(this, Rating_View_Prov.class);


                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                // when provider accepted  order user get notify to view order details
                break;
            }
            case "8": {


                intent = new Intent(this, Order_View_Details.class);

                // here notify form to get review text and send to activity to see review with rate
                intent.putExtra("ORDER_ID", id_detail);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());


                //generic notifications
                break;
            }
            case "0": {


                intent = new Intent(this, Genral_Notify_detail_activ.class);

                // here notify form to get review text and send to activity to see review with rate
                intent.putExtra("MSG_CONTENT", messageBody);


                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(title);
                notificationBuilder.setContentText(messageBody);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                    notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
                }

                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(defaultSoundUri);
                notificationBuilder.setContentIntent(pendingIntent2);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(num/* ID of notification */, notificationBuilder.build());

                break;
            }
        }


       // check if app opened call counters in app and if closed not call it
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            Intent intent = new Intent("MyData");
            intent.putExtra("send_count", "counter");

            broadcaster.sendBroadcast(intent);

        }
    }

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }







    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, MyFirebaseMessagingService.class));
    }





//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
//
//
//        }


    //new code --------------------------------------

//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage == null)
//            return;
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }


//    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        }
////        else{
////            // If the app is in background, firebase itself handles the notification
////        }
//    }
//
//    /**
//     * Showing notification with text only
//     */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//
//    /**
//     * Showing notification with text and image
//     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
//
//
//    private void handleChatMessage(JSONObject json) {
//        Log.e(TAG, "push json: " + json.toString());
//
//        try {
//            //   JSONObject data = json.getJSONObject("data");
//
//
//            String text = obj.getString("text"); //message
//            String title = obj.getString("title"); // name of sender
//            String uid = obj.getString("uid");  // email chat with
//            String messageID = obj.getString("messageID");
//
//
//            //   String fcm_token = obj.getString("fcm_token");
//
//
//            if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//
//
////                // app is in background, show the notification in notification tray
////                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
////                resultIntent.putExtra("message", messageBody);
////
////                // check for image attachment
////                if (TextUtils.isEmpty("test")) {
////                    showNotificationMessage(getApplicationContext(), title, messageBody, "111", resultIntent);
////                } else {
////                    // image is present, show notification with image
////                    showNotificationMessageWithBigImage(getApplicationContext(), title, messageBody, "111", resultIntent, "test");
////                }
//
//
//                int num = (int) System.currentTimeMillis();
//
//                intent = new Intent(this, MainActivity.class);
//
//                intent.putExtra("email_chat_with", uid);
//
////                intent.putExtra(Constants.MESSAGE_ID, messageID);
////                intent.putExtra(Constants.CHAT_NAME, "");
//
//
//                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//                notificationBuilder.setContentTitle(title);
//                notificationBuilder.setContentText(text);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
//                } else {
//                    notificationBuilder.setSmallIcon(R.drawable.splash_logo_shape);
//                }
//                notificationBuilder.setAutoCancel(true);
//                notificationBuilder.setSound(defaultSoundUri);
//                notificationBuilder.setContentIntent(pendingIntent2);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager.notify(num /* ID of notification */, notificationBuilder.build());
//
//
//            }
//
////            else {
////
////                // app is in foreground, broadcast the push message
////                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
////                pushNotification.putExtra("message", messageBody);
////                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
////
////                // play notification sound
////                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
////                notificationUtils.playNotificationSound();
////
////
////
////            }
//
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }
}
