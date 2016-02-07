package com.cyruszhang.cluboard;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.List;

public class ClubDetail extends AppCompatActivity {
    private static final int MENU_ITEM_LOGOUT = 1001;
    private static final int MENU_ITEM_BOOKMARK = 1002;
    private CoordinatorLayout coordinatorLayout;
    private Club thisClub;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final TextView clubName = (TextView) this.findViewById(R.id.club_detail_name);
        final TextView clubDetail = (TextView) this.findViewById(R.id.club_detail_detail);
        final Button createEventBtn = (Button) this.findViewById(R.id.new_event_btn);




        ParseQuery<Club> query = Club.getQuery();
        query.getInBackground(getIntent().getStringExtra("OBJECT_ID"), new GetCallback<Club>() {
            @Override
            public void done(Club object, ParseException e) {
                if (e == null) {
                    thisClub = (Club) object;
                    Log.d(getClass().getSimpleName(), "got club object" + thisClub.getClubName());
                    clubName.setText(thisClub.getClubName());
                    clubDetail.setText(thisClub.getClubDetail());
                    //User currentUser = (User)ParseUser.getCurrentUser();

                 /*   String roleName = thisClub.getClubName()+" "+"Moderator";
                    ParseQuery<ParseRole> roleQuery = ParseRole.getQuery();
                    roleQuery.whereEqualTo("name", roleName);
                    roleQuery.whereEqualTo("users", currentUser);
                    roleQuery.findInBackground(new FindCallback<ParseRole>() {
                        @Override
                        public void done(List<ParseRole> objects, ParseException e) {
                            if(e==null) {
                                Log.d("permission", "verified");
                                createEventBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                Log.d("permission", "denied");
                            }
                        }
                    }); */
                    User currentUser = (User)ParseUser.getCurrentUser();
                    ParseACL clubAcl = thisClub.getACL();
                    //if user is owner, show create event button
                    if(clubAcl.getWriteAccess(currentUser)) {
                        createEventBtn.setVisibility(View.VISIBLE);
                    }



                } else {
                    Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        createEventBtn.setOnClickListener(new View.OnClickListener() {
            //click and go to create club view
            public void onClick(View arg0) {
                Intent intent = new Intent(ClubDetail.this, NewEvent.class);
                intent.putExtra("OBJECT_ID", getIntent().getStringExtra("OBJECT_ID"));
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, MENU_ITEM_LOGOUT, 102, "Logout");
        MenuItem bookmark = menu.add(0, MENU_ITEM_BOOKMARK, 103, "Bookmark");
        bookmark.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        bookmark.setIcon(R.drawable.ic_action_bookmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Snackbar.make(coordinatorLayout,
                        "You selected settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_about:
                Snackbar.make(coordinatorLayout,
                        "You selected About", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            case MENU_ITEM_LOGOUT:
                // Logout current user
                ParseUser.logOut();
                Intent intent = new Intent(ClubDetail.this, Login.class);
                startActivity(intent);
                Snackbar.make(coordinatorLayout,
                        "You are logged out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            case MENU_ITEM_BOOKMARK:
                thisClub.addBookmarkUser(ParseUser.getCurrentUser());
                Snackbar.make(coordinatorLayout,
                        "Bookmark Seleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
        return true;
    }

}
