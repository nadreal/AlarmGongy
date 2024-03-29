package sg.com.alarmgongy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private TextView  tvAlarmTime, tvCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAlarmTime = findViewById(R.id.tv_alarm_time);
        tvCurrentTime = findViewById(R.id.tv_current_time);

        setAlarm();
    }

    public void setAlarm() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 17);
        c.set(Calendar.MINUTE, 37);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm is set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        tvAlarmTime.setText(timeText);

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        tvCurrentTime.setText(currentTime);

    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void setCustomTImeAlarm() {
        //setDefaultTimeAlarms();
        //Select default alarm time
    }

    private void setRoundHourAlarms() {
        setDefaultTimeAlarms(0, 0);
    }

    private void setHalfHourAlarms() {
        setDefaultTimeAlarms(0, 30);
    }

    private void setDefaultTimeAlarms(int hours, int minutes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 24; i++) {
            c.set(Calendar.HOUR_OF_DAY, hours);
            c.set(Calendar.MINUTE, minutes);

            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        tvAlarmTime.setText("Alarm canceled");
    }

    public void startTimer(Calendar c) {
        long test = Calendar.getInstance().getTimeInMillis() - c.getTimeInMillis();
        new CountDownTimer(test, 1000) {
            public void onTick(long millisUntilFinished) {
                tvAlarmTime.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tvAlarmTime.setText("done!");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        }.start();
    }
}