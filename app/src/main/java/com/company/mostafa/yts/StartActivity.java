package com.company.mostafa.yts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.company.mostafa.yts.Notifications.NotificationEventReceiver;
import com.flaviofaria.kenburnsview.KenBurnsView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class StartActivity extends AppCompatActivity {
    //GradienTextView txt_intro;
    KenBurnsView kenBurnsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_start);
        // txt_intro = (GradienTextView) findViewById(R.id.intro);
        kenBurnsView = (KenBurnsView) findViewById(R.id.brunsView);

        //txt_intro.start(Orientation.LEFT_TO_RIGHT_FORME_NONE, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, Home.class));
                finish();
            }
        }, 5000);
//        dailyNotifications();
    }

//    private void dailyNotifications() {
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.HOUR_OF_DAY,17);
////        calendar.set(Calendar.MINUTE,38);
////        calendar.set(Calendar.SECOND,22);
////        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//
//    }
}
