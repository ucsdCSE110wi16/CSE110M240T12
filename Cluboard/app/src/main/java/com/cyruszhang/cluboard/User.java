package com.cyruszhang.cluboard;

import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhangxinyuan on 2/4/16.
 */
public class User extends ParseUser {
    /* store user created club */
    public void setMyclubs(Club myClub) {
        this.addUnique("myClubs", myClub);
    }
    /* store bookmarked club */
    public void setBookmarkedClubs(Club bookmarkedClub) {
        this.addUnique("bookmarkedClubs", bookmarkedClub);
    }
    /* store followed event */
    public void setFollowedEvents(Event followedEvent) {
        this.addUnique("followedEvents", followedEvent);
    }
    public void checkPermission(Club club) {

    }

}
