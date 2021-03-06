package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            System.exit(0);
        }

        if (!this.checkCurrentUser()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /**
     * TODO CHECK THAT THE USER HAS A REAL OPEN SESSION,
     *  and do not check which update data is less recent.
     *
     * @return boolean, true if the user is logged in, false otherwise.
     */
    public boolean checkCurrentUser() {
        List<User> users = new UserDbManager(this).getAllUsers();
        User currentUser = null;
        for (User user : users) {
            if (user.getCurrent() == UtilProj.CURRENT) {
                // TODO check the current one (this is temporary)
                currentUser = user;
            }
        }

        if (currentUser == null) {
            return false;
        } else {
            AccountDirectory.getInstance().setUser(currentUser);
            //TODO: Task with aim to download all db related to the user logged in.
            try {
                new UserDbManager(getApplicationContext()).addUser(currentUser);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        }
    }
}
