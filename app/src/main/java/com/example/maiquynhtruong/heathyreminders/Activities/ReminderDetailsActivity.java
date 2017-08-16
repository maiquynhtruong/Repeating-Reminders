package com.example.maiquynhtruong.heathyreminders.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Receivers.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;


public class ReminderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText reminderTitle;
    TextView date, time;
    Reminder reminder;
    FancyButton cancelBtn, saveBtn;
    ReminderDatabase database;
    Calendar calendar;
    Toolbar toolbar;
    long reminderID;
    public static final String REMINDER_ID = "reminder-id";
    public static final int EDIT_REMINDER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        date = (TextView) findViewById(R.id.datePicker);
        time = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (TextInputEditText) findViewById(R.id.reminder_title);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);
        saveBtn = (FancyButton) findViewById(R.id.btn_save);
        database = new ReminderDatabase(this);


        getSupportActionBar().setTitle(getString(R.string.app_edit_reminder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // get stuff from intent calling this activity
        reminderID = getIntent().getIntExtra(ReminderDetailsActivity.REMINDER_ID, 0);
        reminder = database.getReminder(reminderID);
        if (reminder != null) {
            reminderTitle.setText(reminder.getTitle());
            date.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
            time.setText(reminder.getHour() + ":" + reminder.getMinute());
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
    }
}
