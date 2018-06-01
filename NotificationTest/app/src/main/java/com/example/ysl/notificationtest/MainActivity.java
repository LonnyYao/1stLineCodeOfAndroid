package com.example.ysl.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btNotification = (Button)findViewById(R.id.bt_notification);
        btNotification.setOnClickListener(this);

        Button btBigTxNotification = (Button)findViewById(R.id.bt_notification_big_tx);
        btBigTxNotification.setOnClickListener(this);

        Button btBigPicNotification = (Button)findViewById(R.id.bt_notification_big_pic);
        btBigPicNotification.setOnClickListener(this);

        Button btLightNotification = (Button)findViewById(R.id.bt_notification_light);
        btLightNotification.setOnClickListener(this);

        Button btRingNotification = (Button)findViewById(R.id.bt_notification_ring);
        btRingNotification.setOnClickListener(this);

        Button btVibrateNotification = (Button)findViewById(R.id.bt_notification_vibrate);
        btVibrateNotification.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_notification:
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                /**
                 * ---> NotificationCompat.Builder (Context context) <---
                 * This constructor was deprecated in API level 26.1.0.
                 *
                 * Use NotificationCompat.Builder(Context, String) instead.
                 * All posted Notifications must specify a NotificationChannel Id.
                 */
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("Normal")
                        .setContentText("This is a normal notification.")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent)
                        //.setAutoCancel(true) // the way to disappear the Notification in System StateBar
                        .build();

                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1000, notification); // request code is 1
                break;
            case R.id.bt_notification_ring:
                Notification ringNF = createNotification(1);
                NotificationManager nfManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nfManager.notify(1001, ringNF);
                break;
            case R.id.bt_notification_vibrate:
                Notification vibrateNF = createNotification(2);
                NotificationManager nfManager2 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nfManager2.notify(1002, vibrateNF);
                break;
            case R.id.bt_notification_light:
                Notification lightsNF = createNotification(3);
                NotificationManager nfManager3 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nfManager3.notify(1003, lightsNF);
                break;
            case R.id.bt_notification_big_tx:
                Notification bigTextNF = createNotification(4);
                NotificationManager nfManager4 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nfManager4.notify(1004, bigTextNF);
                break;
            case R.id.bt_notification_big_pic:
                Notification bigPicNF = createNotification(5);
                NotificationManager nfManager5 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nfManager5.notify(1005, bigPicNF);
                break;
            default:
                break;
        }
    }

    // type: 0-Normal, 1-Ring, 2-Vibrate, 3-Lights, 4-BigText, 5-BigPicture
    private Notification createNotification(int type) {
        Intent intent1 = new Intent(this, NotificationActivity.class);
        int requestCode = 1000 + type;
        PendingIntent pi = PendingIntent.getActivity(this, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * ---> NotificationCompat.Builder (Context context) <---
         * This constructor was deprecated in API level 26.1.0.
         *
         * Use NotificationCompat.Builder(Context, String) instead.
         * All posted Notifications must specify a NotificationChannel Id.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(pi);

        switch (type) {
            case 0:
                // Normal, add nothing
                break;
            case 1:
                // Ring, add Sound
                builder.setContentTitle("Ring Notification")
                        .setContentText("This is a Ring notification.")
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Journey.ogg")));
                break;
            case 2:
                // Vibrate, add Vibrate, param long[] {stop, vibrate, stop, vibrate, ... ...}
                builder.setContentTitle("Vibrate Notification")
                        .setContentText("This is a Vibrate notification.")
                        .setVibrate(new long[] {0, 1000, 1000, 1000, 1000});
                break;
            case 3:
                // Lights, add Lights
                builder.setContentTitle("Lights Notification")
                        .setContentText("This is a Lights notification.")
                        .setLights(Color.RED, 1500, 1000);
                break;
            case 4:
                // BigText
                builder.setContentTitle("BigText Notification")
                        //.setContentText("xxx");
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                "0>This is a BigText notification. "
                                + "1>This is a BigText notification. "
                                + "2>This is a BigText notification. "
                                + "3>This is a BigText notification. "
                                + "4>This is a BigText notification. "
                                + "5>This is a BigText notification. "
                                + "6>This is a BigText notification."));
                // If not use setStyle(), the Notification text above will not be show completely
                break;
            case 5:
                // BigPic
                builder.setContentTitle("BigPic Notification")
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(
                                BitmapFactory.decodeResource(getResources(), R.drawable.big_pic_exmaple)
                        ));
                break;
            default:
                builder.setContentTitle("Default Notification")
                        .setContentText("This is a default notification.")
                        .setDefaults(NotificationCompat.DEFAULT_ALL);
                break;
        }

        return builder.build();
    }
}
