package com.sildev.musiclocal.dialog;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.sildev.musiclocal.R;
import com.sildev.musiclocal.broadcast.TimerReceiver;
import com.sildev.musiclocal.databinding.DialogTimerBinding;

public class TimerDialog extends Dialog {
    private DialogTimerBinding timerBinding;
    private Context context;

    public TimerDialog(@NonNull Context context) {
        super(context);
        timerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_timer, null, false);
        setContentView(timerBinding.getRoot());
        this.context = context;
        setOnClickSetTime();
    }

    private void setOnClickSetTime() {
        timerBinding.btnSetTime.setOnClickListener(v -> {
            int time = getTime();
            if (time <= 0) {
                Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
            } else {
                time *= 60000;
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(context, TimerReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
                Toast.makeText(context, "Successful timer", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public int getTime() {
        String time = timerBinding.edtTimer.getText().toString().trim();
        try {
            return Integer.parseInt(time);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void show() {
        super.show();

    }
}
