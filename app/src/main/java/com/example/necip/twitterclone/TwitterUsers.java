package com.example.necip.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    private String followedUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        setTitle("Twitter Users");

        FancyToast.makeText(this, "Welcome " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG, FancyToast.INFO, true).show();

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers); // 2. is layout raw

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);   //for check & uncheck
        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects != null && e == null) {
                        if (objects.size() > 0) {
                            for (ParseUser user : objects) {
                                tUsers.add(user.getUsername());
                            }

                            listView.setAdapter(adapter);

                            for (String twitterUser : tUsers) {
                                if (ParseUser.getCurrentUser().getList("fanOf") != null) { // for not below contains is null
                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                        listView.setItemChecked(tUsers.indexOf(twitterUser), true);

                                        followedUser = followedUser + twitterUser + "\n";

                                        FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername() + " is now following; \n\n" + followedUser, Toast.LENGTH_LONG, FancyToast.INFO, true).show();
                                    }
                                }
                            }

                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout_item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(TwitterUsers.this, SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.send_tweet_item:

                Intent intent = new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {

            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now followed!", Toast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));  //add a user to FAN list

        } else {

            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now unfollowed", Toast.LENGTH_LONG, FancyToast.WARNING, true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));  //remove a user from FAN list
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this, "Saved", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                } else
                    FancyToast.makeText(TwitterUsers.this, e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
            }
        });

    }
}