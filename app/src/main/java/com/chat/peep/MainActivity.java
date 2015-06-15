package com.chat.peep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
    private SharedPreferences prefs;
    private NameAdapter nameAdapter;
    private User current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if User instance is loaded in SharedPreferences
        // If so, I'm logged in and good to go
        // If not, I need to log in
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        current = User.load(prefs);
        if (current == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        nameAdapter = new NameAdapter(getApplicationContext(), R.layout.name_list_item, current.getNames());
        ListView mNames = (ListView) findViewById(R.id.names);
        mNames.setAdapter(nameAdapter);

        mNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openChat(current.getNames().get(position));
            }
        });

        Button mLogout = (Button) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openChat(Name name) {
        Log.d("lol", name.getName() + " => " + name.getUid());

        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        i.putExtra("name", name.getName());
        i.putExtra("uid", name.getUid());
        startActivity(i);
    }

    public void logout() {
        User.unload(prefs.edit());

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}
