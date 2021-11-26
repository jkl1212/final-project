package com.example.sookmyungchat;

import java.util.Locale;

public class MessageItem {
    //채팅 내용 데이터 저장용
    String name;
    String message;
    String time;
    String profileUrl;

    public MessageItem(String name, String message, String time, String profileUrl){
        this.name = name;
        this.message = message;
        this.time = time;
        this.profileUrl = profileUrl;
    }
    public MessageItem(){
        //DB 로 객체 값을 읽어올 때 사용될 빈 생성자
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getProfileUrl(){
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
