package com.androidkudus.lamianota.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.androidkudus.lamianota.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NoteReminderManager extends BroadcastReceiver {
    public static final String TYPE_NOTE_REMINDER = "NoteReminder";
    public static final String TYPE_REPEATING_NOTE_REMINDER = "NoteReminderRepeating";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private static int ID_REMINDER = 100;
    private static int ID_REMINDER_REPEATING = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        assert type != null;
        String title = type.equalsIgnoreCase(TYPE_NOTE_REMINDER) ? TYPE_NOTE_REMINDER : TYPE_REPEATING_NOTE_REMINDER;
        int notifId = type.equalsIgnoreCase(TYPE_NOTE_REMINDER) ? ID_REMINDER : ID_REMINDER_REPEATING;

        Toast.makeText(context, title + " : " + message, Toast.LENGTH_SHORT).show();

        showNotificationReminder(context, title, message, notifId);
    }

    public void setReminder(Context context, String type, String date, String time, String message){
        String DATE_FORMAT = context.getString(R.string.date_format_in);
        String TIME_FORMAT = "HH:mm";

        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NoteReminderManager.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Log.e("Alarm Start at ", date + " " + time);
        String[] dateArray = date.split("/");
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0);
        if (alarmManager != null){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(context, "Reminder setup", Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(context, NoteReminderManager.class);
        int requestCode = type.equalsIgnoreCase(TYPE_NOTE_REMINDER) ? ID_REMINDER : ID_REMINDER_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, in, 0);
        pendingIntent.cancel();

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void showNotificationReminder(Context context, String title, String message, int notifId){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Note Reminder";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(notificationSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null){
            notificationManager.notify(notifId, notification);
        }
    }

    public boolean isDateInvalid(String date, String format){
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return false;
        }catch (ParseException e){
            return true;
        }
    }
}
