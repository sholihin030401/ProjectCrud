package com.team.projectcrud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.projectcrud.database.UserHelper;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail, editName, editPhone, editAddress;
    Button btnSubmit;

    public static String EXTRA_USER = "extra_user";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private Item item;
    private int pos;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editEmail = findViewById(R.id.email);
        editName = findViewById(R.id.nama);
        editPhone = findViewById(R.id.notelp);
        editAddress = findViewById(R.id.address);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(this);

        userHelper = new UserHelper(this);
        userHelper.open();

        item = getIntent().getParcelableExtra(EXTRA_USER);
        if (item != null){
            pos = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit){
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            editEmail.setText(item.getEmail());
            editName.setText(item.getName());
            editPhone.setText(item.getPhone());
            editAddress.setText(item.getAddress());
        }else{
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userHelper != null){
            userHelper.close();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnsubmit){
            String email = editEmail.getText().toString().trim();
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String address = editAddress.getText().toString().trim();

            boolean isEmpty = false;

            if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()){
                isEmpty = true;
                Toast.makeText(DetailActivity.this, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
            }

            if (!isEmpty){
                Item newItem = new Item();
                newItem.setEmail(email);
                newItem.setName(name);
                newItem.setPhone(phone);
                newItem.setAddress(address);

                Intent intent = new Intent();

                /*
                Jika merupakan edit setresultnya UPDATE, dan jika bukan maka setresultnya ADD
                 */
                if (isEdit){
                    newItem.setId(item.getId());
                    userHelper.update(newItem);

                    intent.putExtra(EXTRA_POSITION, pos);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                }else{
                    newItem.setEmail(email);
                    newItem.setName(name);
                    newItem.setPhone(phone);
                    newItem.setAddress(address);
                    userHelper.insert(newItem);

                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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

    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
     */
    private void showAlertDialog(int type){
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        }else{
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (isDialogClose){
                            finish();
                        }else{
                            userHelper.delete(item.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, pos);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}