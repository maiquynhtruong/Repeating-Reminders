package com.example.maiquynhtruong.heathyreminders.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Receivers.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    Spinner frequencySpinner;
    EditText name;
    EditText description;
    TextView atTime, onDate;
    int hour, minute, month, day, year, repeatNumber;
    boolean repeat;
    String repeatType;
    ReminderDatabase database;
    Calendar calendar;
    public static final String TAG = "AddActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        frequencySpinner = (Spinner) findViewById(R.id.reminder_frequency_spinner);
        name = (EditText) findViewById(R.id.reminder_title);
        description = (EditText) findViewById(R.id.reminder_description);
        atTime = (TextView) findViewById(R.id.timePicker);
        onDate = (TextView) findViewById(R.id.datePicker);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(this);

        calendar = Calendar.getInstance();

        // set the current time as the default time when first showed
        atTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
        onDate.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));

        database = new ReminderDatabase(getBaseContext());

        // recover states on device rotation
        if (savedInstanceState != null) {

        }
    }

    public void showDatePicker(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void showTimePicker(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(this));
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence frequency = (CharSequence) adapterView.getItemAtPosition(i);
        Toast.makeText(adapterView.getContext(), frequency, Toast.LENGTH_LONG).show();
        repeatType = frequency.toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        onDate.setText(month + "/" + dayOfMonth + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        atTime.setText(hourOfDay + ":" + minute);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void saveReminder(View view) {
        String title = name.getText().toString();
        long reminderID = database.setReminder(new Reminder(title, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), repeat, repeatNumber, repeatType));
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.DAY_OF_MONTH, day);
//        calendar.set(Calendar.MONTH, month);
//        calendar.set(Calendar.YEAR, year);

        Toast.makeText(this, "Save reminder as " + title + " at " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " on " + calendar.get(Calendar.MONTH) +
        "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR), Toast.LENGTH_SHORT);

        if (repeatType.equals(Reminder.MONTHLY) || repeatType.equals(Reminder.YEARLY)) new ReminderReceiver().setReminderMonthOrYear(this, calendar.getTimeInMillis(),
                reminderID, repeatType);
        else if (repeatType.equals(Reminder.HOURLY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_HOUR);
        else if (repeatType.equals(Reminder.DAILY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_DAY);
        else if (repeatType.equals(Reminder.WEEKLY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_DAY*7);

        onBackPressed();
    }

    public void cancelReminder(View view) {
        onBackPressed();
    }

}
