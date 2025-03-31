package com.example.e_koteapplication;

public class User {
    private String username;
    private String idCard;
    private String mobileNumber;
    private String email;
    private String password;
    private String roles;



    public User(String username,String idCard,String mobileNumber,String email,String password,String role){
        this.username = username;
        this.idCard = idCard;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.roles =roles;
    }

    public String getusername(){
        return username;
    }
    public void setUsername(String username){this.username=username;}
    public String getIdCard(){return idCard;}
    public void setIdCard(String idCard){this.idCard=idCard;}
    public String getMobileNumber(){
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber){this.mobileNumber = mobileNumber;}
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){this.email = email;}
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){this.password = password;}
    public String getRole(){return roles;}
    public void setRoles(String roles){this.roles=roles;}
}
