package com.team.projectcrud.database;

import static android.provider.BaseColumns._ID;
import static com.team.projectcrud.database.DBContract.TABLE_USER;
import static com.team.projectcrud.database.DBContract.UserColumns.ADDRESS;
import static com.team.projectcrud.database.DBContract.UserColumns.EMAIL;
import static com.team.projectcrud.database.DBContract.UserColumns.NAME;
import static com.team.projectcrud.database.DBContract.UserColumns.PHONE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.team.projectcrud.Item;

import java.util.ArrayList;

public class UserHelper {
    private static String DB_TABLE = TABLE_USER;
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public UserHelper(Context context) {
        this.context = context;
    }

    public UserHelper open() throws SQLException{
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<Item> query(){
        ArrayList<Item> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DB_TABLE,null,null,null,null,null,_ID +" DESC",null);
        cursor.moveToFirst();
        Item item;
        if (cursor.getCount() > 0){
            do {
                item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                item.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(PHONE)));
                item.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)));

                arrayList.add(item);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public long insert(Item item){
        ContentValues inValues = new ContentValues();
        inValues.put(EMAIL, item.getEmail());
        inValues.put(NAME, item.getName());
        inValues.put(PHONE, item.getPhone());
        inValues.put(ADDRESS, item.getAddress());
        return database.insert(DB_TABLE,null,inValues);
    }

    public int update(Item item){
        ContentValues args = new ContentValues();
        args.put(EMAIL, item.getEmail());
        args.put(NAME, item.getName());
        args.put(PHONE, item.getPhone());
        args.put(ADDRESS, item.getAddress());
        return database.update(DB_TABLE, args, _ID + "= '"+item.getId() + "'",null);
    }

    public int delete(int id){
        return database.delete(TABLE_USER, _ID + "= '"+id+"'",null);
    }
}
