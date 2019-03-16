package com.mxfit.mentix.menu3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mxfit.mentix.menu3.LoginPackage.LoginActivity;
import com.mxfit.mentix.menu3.Utils.CopyDatabase;
import com.mxfit.mentix.menu3.Utils.DatabasePremadesHelper;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        DatabasePremadesHelper myDb = new DatabasePremadesHelper(this);
        File database = this.getDatabasePath(DatabasePremadesHelper.DATABASE_NAME);
        if(!database.exists())
        {
            myDb.getReadableDatabase();
            new CopyDatabase(this);
        }
        finish();
    }
}
