package com.team.projectcrud;

import static com.team.projectcrud.DetailActivity.REQUEST_UPDATE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.team.projectcrud.database.UserHelper;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    ProgressBar progressBar;

    private LinkedList<Item> list;
    private AdapterItem adapter;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Data");

        recyclerView = findViewById(R.id.rvmain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.pb);
        fab = findViewById(R.id.fabadd);
        fab.setOnClickListener(this);

        userHelper = new UserHelper(this);
        userHelper.open();

        list = new LinkedList<>();
        adapter = new AdapterItem(this);
        adapter.setListItem(list);
        recyclerView.setAdapter(adapter);

        new LoadUserAsync().execute();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabadd){
            startActivityForResult(new Intent(MainActivity.this,DetailActivity.class),DetailActivity.REQUEST_ADD);
        }
    }

    private class LoadUserAsync extends AsyncTask<Void,Void, ArrayList<Item>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0){
                list.clear();
            }
        }

        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            return userHelper.query();
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            progressBar.setVisibility(View.GONE);

            list.addAll(items);
            adapter.setListItem(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DetailActivity.REQUEST_ADD){
            if (resultCode == DetailActivity.RESULT_ADD){
                new LoadUserAsync().execute();
                showSnackbarMessage("Satu data berhasil ditambahkan");
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(),0);
            }
        }

        else if (requestCode == REQUEST_UPDATE) {
            /*
            Akan dipanggil jika result codenya UPDATE
            Semua data di load kembali dari awal
            */
            if (resultCode == DetailActivity.RESULT_UPDATE) {
                new LoadUserAsync().execute();
                showSnackbarMessage("Satu item berhasil diubah");
                int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, new RecyclerView.State(), position);
            }
            /*
            Akan dipanggil jika result codenya DELETE
            Delete akan menghapus data dari list berdasarkan dari position
            */
            else if (resultCode == DetailActivity.RESULT_DELETE) {
                int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                list.remove(position);
                adapter.setListItem(list);
                adapter.notifyDataSetChanged();
                showSnackbarMessage("Satu item berhasil dihapus");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userHelper != null){
            userHelper.close();
        }
    }

    private void showSnackbarMessage(String mes) {
        Snackbar.make(recyclerView,mes,Snackbar.LENGTH_SHORT).show();
    }
}