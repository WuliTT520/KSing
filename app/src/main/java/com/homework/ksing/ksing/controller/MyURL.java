package com.homework.ksing.ksing.controller;



public class MyURL {
    private String URL="http://10.11.181.29:8080";

    public String getURL() {
        return URL;
    }

    public String login(){
        return URL+"/login";
    }
    public String logout(){
        return URL+"/logout";
    }
    public String getMsg(){
        return URL+"/getMsg";
    }
    public String getAllSong(){
        return URL+"/getListOfAllSong";
    }
}