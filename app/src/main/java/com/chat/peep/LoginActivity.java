package com.chat.peep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends ActionBarActivity {
    private SharedPreferences prefs;
    private String token;
    private User current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText mNumber = (EditText) findViewById(R.id.number);
        Button mSubmit = (Button) findViewById(R.id.submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrRegister(mNumber.getText().toString());
            }
        });

        // Set up values we already have
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        current = new User(token);

        // TODO: Should I background this in case the user has like 10k contacts or something?
        Cursor phonebook = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phonebook.moveToNext()) {
            String name = phonebook.getString(phonebook.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = phonebook.getString(phonebook.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            current.addContact(name, contactNumber);
        }
        phonebook.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loginOrRegister(String number) {
        PeepApi.PeepService peepService = PeepApi.getService();
        current.setNumber(number);

        peepService.login_or_register(current.getPerson(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d("lol", "succccesss");
                Log.d("lol", "uid: " + user.getUid());

                current.update(user);
                current.store(prefs.edit());

                // TODO: might wanna pass the current object as an Intent extra
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("lol", "faaaailure");
                Log.d("lol", error.getCause().toString());
            }
        });
    }
}
