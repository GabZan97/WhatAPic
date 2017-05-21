package com.gabrielezanelli.whatapic;

/**
 * Class representing user
 */

public class InstagramUser {
    public String id;
    public String username;
    public String fullName;
    public String profilePictureUrl;
    public String accessToken;


    public InstagramUser() {

    }

    public void setUserInformation(String id, String username, String fullName, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }


}