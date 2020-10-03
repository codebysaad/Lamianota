package com.androidkudus.lamianota.ui.keu.insert;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.utils.DateFormatter;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

public class InsertUpdateKeuActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextInputEditText edtAmountOfMoney, edtMoneySource;
    private RadioGroup radioGroup;
    private RadioButton rbTrue, rbFalse;
    private final Calendar calendar = Calendar.getInstance();
    private String dateSet;
    int year, month, day;
    private Button btnSave, btnDatePicker;
    private TextView tvDate;
    private InsertUpdateKeuViewModel viewModel;

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

    private Keu keu;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_update_keu);
        tvDate = findViewById(R.id.tv_date_picker_keu);
        edtAmountOfMoney = findViewById(R.id.input_amount_of_money);
        edtMoneySource = findViewById(R.id.input_source_or_from);
        radioGroup = findViewById(R.id.rg_in_out);
        rbTrue = findViewById(R.id.rb_true_money_in);
        rbFalse = findViewById(R.id.rb_false_money_out);
        btnSave = findViewById(R.id.btn_save_keu);
        btnDatePicker = findViewById(R.id.btn_date_picker_keu);

        viewModel = obtainViewModel(InsertUpdateKeuActivity.this);
        //mengambil intent parcel dan menetapkan status edit
        keu = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (keu != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            keu = new Keu();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String actionBarTitle, btnTitle, datPicker;

        if (isEdit) {
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);
            datPicker = getString(R.string.change_date);

            if (keu != null) {
                edtAmountOfMoney.setText(keu.getMoney());
                edtMoneySource.setText(keu.getFrom());
                tvDate.setText(DateFormatter.getFormatDate(getApplicationContext(), keu.getDateIn()));
                //rg insert
                if (keu.isStatusIn()) {
                    rbTrue.isChecked();
                } else {
                    rbFalse.isChecked();
                }
            }
        } else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
            datPicker = getString(R.string.set_date);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Set title button
        btnDatePicker.setText(datPicker);
        btnSave.setText(btnTitle);

        //Button set event click listener
        btnDatePicker.setOnClickListener(v -> datePicker());
        btnSave.setOnClickListener((v) -> {
            String amountMoney = Objects.requireNonNull(edtAmountOfMoney.getText()).toString().trim();
            String moneySource = Objects.requireNonNull(edtMoneySource.getText()).toString().trim();
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (amountMoney.isEmpty()) {
                edtAmountOfMoney.setError(getString(R.string.empty));
            } else if (moneySource.isEmpty()) {
                edtMoneySource.setError(getString(R.string.empty));
            } else if (tvDate.getText().length() == 0) {
                Toast.makeText(this, getString(R.string.please_choose_date), Toast.LENGTH_SHORT).show();
            } else {
                keu.setMoney(amountMoney);
                keu.setFrom(moneySource);
                keu.setDateIn(dateSet);
                //insert rg
                if (selectedId == rbTrue.getId()) {
                    keu.setStatusIn(true);
                } else if (selectedId == rbFalse.getId()) {
                    keu.setStatusIn(false);
                } else {
                    Toast.makeText(this, "Please choose category", Toast.LENGTH_SHORT).show();
                }
                Log.e("Insert Keu : ", "DateIn: " + dateSet + "," + "Money Rp. " + amountMoney + "," + "Source: " + moneySource + "," + "Category: " + keu.isStatusIn());

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTE, keu);
                intent.putExtra(EXTRA_POSITION, position);

                if (isEdit) {
                    keu.setDateIn(DateFormatter.getFallbackFormatDate(getApplicationContext(), tvDate.getText().toString()));
                    viewModel.update(keu);
                    setResult(RESULT_UPDATE, intent);
                } else {
                    viewModel.insert(keu);
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
                        viewModel.delete(keu);
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
    private static InsertUpdateKeuViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(InsertUpdateKeuViewModel.class);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateSet = dayOfMonth + "/" + (month + 1) + "/" + year;
        tvDate.setText(DateFormatter.getFormatDate(getApplicationContext(), dateSet));
    }

    private void datePicker() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertUpdateKeuActivity.this, this, year, month, day);
        datePickerDialog.setOnCancelListener(dialog -> {
            Toast.makeText(this, getString(R.string.cancel), Toast.LENGTH_SHORT).show();
        });
        datePickerDialog.show();
    }
}