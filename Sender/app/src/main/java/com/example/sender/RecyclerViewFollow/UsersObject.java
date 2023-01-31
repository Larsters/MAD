package com.example.sender.RecyclerViewFollow;

//recyclerview bs
public class UsersObject {
    private String email;
    private String uid;

    public UsersObject(String email, String uid){
        this.email = email;
        this.uid = uid;
    }

    public String getUid() {     //for getting info from user
        return uid;
    }
        public void setUid(String uid){
            this.uid = uid;
        }

    public String getEmail() {     //for getting info from user
        return email;
    }
    public void setEmail(String uid){
        this.email = email;
    }
    }

