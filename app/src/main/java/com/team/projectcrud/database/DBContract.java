package com.team.projectcrud.database;

import android.provider.BaseColumns;

public class DBContract {

    static String TABLE_USER = "user";

    static final class UserColumns implements BaseColumns{
        static String EMAIL = "email";
        static String NAME = "name";
        static String PHONE = "phone";
        static String ADDRESS = "address";
    }
}
