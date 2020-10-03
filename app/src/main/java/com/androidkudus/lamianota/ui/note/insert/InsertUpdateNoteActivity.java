package com.androidkudus.lamianota.ui.note.insert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.reminder.NoteReminderManager;
import com.androidkudus.lamianota.utils.DateFormatter;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

public class InsertUpdateNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextInputEditText edtTitle, edtDesc;
    private final Calendar calendar = Calendar.getInstance();
    private SwitchMaterial switchReminder;
    private boolean isReminder;
    private String dateSet, timeSet;
    int year, month, day, hour, minute;
    private Button btnSave, btnDatePicker, btnTimePicker;
    private TextView tvDate, tvTime;
    private InsertUpdateNoteViewModel viewModel;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private static final int ALERT_DIALOG_CLOSE = 10;
    private static final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    private int position;

    private NoteReminderManager reminderManager;
    private final String REMINDER_TAG = "Reminder_Tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_update_note);
        edtTitle = findViewById(R.id.note_input_title);
        edtDesc = findViewById(R.id.note_input_description);
        btnSave = findViewById(R.id.btn_save_note);
        btnDatePicker = findViewById(R.id.btn_date_picker);
        btnTimePicker = findViewById(R.id.btn_time_picker);
        tvDate = findViewById(R.id.tv_date_picker);
        tvTime = findViewById(R.id.tv_time_picker);
        switchReminder = findViewById(R.id.switch_set_reminder);

        viewModel = obtainViewModel(InsertUpdateNoteActivity.this);
        //mengambil intent parcel dan menetapkan status edit
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        reminderManager = new NoteReminderManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String actionBarTitle, btnTitle, datPicker, timePicker;

        if (isEdit) {
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);
            timePicker = getString(R.string.change_time);
            datPicker = getString(R.string.change_date);

            if (note != null) {
                edtTitle.setText(note.getTitle());
                edtDesc.setText(note.getDescription());
                tvDate.setText(DateFormatter.getFormatDate(getApplicationContext(), note.getDate()));
//                tvDate.setText(note.getDate());
                tvTime.setText(note.getTime());
                switchReminder.setChecked(note.isReminder());
            }
        } else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
            timePicker = getString(R.string.set_time);
            datPicker = getString(R.string.set_date);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setReminder();

        //Set title button
        btnDatePicker.setText(datPicker);
        btnTimePicker.setText(timePicker);
        btnSave.setText(btnTitle);

        //Button set event click listener
        btnDatePicker.setOnClickListener(v -> datePicker());
        btnTimePicker.setOnClickListener(v -> timePicker());
        btnSave.setOnClickListener((v) -> {
            String title = Objects.requireNonNull(edtTitle.getText()).toString().trim();
            String description = Objects.requireNonNull(edtDesc.getText()).toString().trim();

            if (title.isEmpty()) {
                edtTitle.setError(getString(R.string.empty));
            } else if (description.isEmpty()) {
                edtDesc.setError(getString(R.string.empty));
            } else if (tvDate.getText().length() == 0) {
                Toast.makeText(this, "Please choose date", Toast.LENGTH_SHORT).show();
            } else {
                note.setTitle(title);
                note.setDescription(description);
                note.setDate(dateSet);
                note.setTime(timeSet);
                note.setReminder(isReminder);
                Log.e("Insert Note : ", "Title: " + title + "," + "Desc: " + description + "," + "Date: " + dateSet + "," + "Time: " + timeSet + "," + "isReminder? " + isReminder);
                if (dateSet != null && timeSet != null && switchReminder.isChecked()) {
                    reminderManager.setReminder(this, NoteReminderManager.TYPE_NOTE_REMINDER, dateSet, timeSet, description);
                }

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTE, note);
                intent.putExtra(EXTRA_POSITION, position);

                if (isEdit) {
                    note.setDate(DateFormatter.getFallbackFormatDate(getApplicationContext(), tvDate.getText().toString()));
                    note.setTime(tvTime.getText().toString());
                    viewModel.update(note);
                    setResult(RESULT_UPDATE, intent);
                    if (switchReminder.isChecked()) {
//                        if (dateSet == null) {
                        reminderManager.setReminder(this, NoteReminderManager.TYPE_NOTE_REMINDER, note.getDate(), note.getTime(), description);
                        Log.e("Update Reminder : ", "Title: " + title + "," + "Desc: " + description + "," + "Date: " + note.getDate() + "," + "Time: " + note.getTime() + "," + "isReminder? " + isReminder);
//                        }
                    } else {
                        reminderManager.cancelReminder(this, NoteReminderManager.TYPE_NOTE_REMINDER);
                    }
                    finish();
                } else {
//                    note.setDate(DateHelper.getCurrentDate());
                    viewModel.insert(note);
                    setResult(RESULT_ADD, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    if (isDialogClose) {
                        finish();
                    } else {
                        viewModel.delete(note);
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_POSITION, position);
                        setResult(RESULT_DELETE, intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @NonNull
    private static InsertUpdateNoteViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(InsertUpdateNoteViewModel.class);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateSet = dayOfMonth + "/" + (month + 1) + "/" + year;
        tvDate.setText(DateFormatter.getFormatDate(getApplicationContext(), dateSet));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeSet = hourOfDay + ":" + minute;
        tvTime.setText(timeSet);
    }

    private void datePicker() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertUpdateNoteActivity.this, this, year, month, day);
        datePickerDialog.setOnCancelListener(dialog -> {
            Toast.makeText(this, getString(R.string.cancel), Toast.LENGTH_SHORT).show();
        });
        datePickerDialog.show();
    }

    private void timePicker() {
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(InsertUpdateNoteActivity.this, this, hour, minute, true);
        timePickerDialog.show();
    }

    private void setReminder() {
        switchReminder.setOnCheckedChangeListener((view, isChecked) -> {
//            if (tvDate != null && tvTime != null) {
            if (isChecked) {
                isReminder = true;
            } else {
                isReminder = false;
            }
//            } else {
//                Toast.makeText(this, "Please choose date & time", Toast.LENGTH_SHORT).show();
//            }
        });

    }
}
