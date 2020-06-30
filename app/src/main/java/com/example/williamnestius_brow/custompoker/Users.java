package com.example.williamnestius_brow.custompoker;

public class Users {
    public String uid;
    public String username;
    public int age;
    public Friends friends;

    public Users(){}

    public Users(String Uid, String userName, int Age){
        this.uid = Uid;
        this.username = userName;
        this.age = Age;
    }

    public Users(String userName, int Age){
        this.username = userName;
        this.age = Age;
    }

    public Users(String userName, int Age, Friends Friend){
        this.username = userName;
        this.age = Age;
        this.friends = Friend;
    }
    public Users(String Uid, String userName){
        this.uid = Uid;
        this.username = userName;
    }



    public void setUid(String Uid){
        uid = Uid;
    }
    public String getUid() {
        return uid;
    }

    public void setUsername(String userName){
        username = userName;
    }
    public String getUsername() {
        return username;
    }
}

