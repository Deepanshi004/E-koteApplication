package com.example.e_koteapplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Issue {
    private String gunId;
    private String userId;
    private String issueId;
    private String issueDate;
    private String returnDate;
    private String status;


    public Issue(){}

    public Issue(String gunId, String userId,String issueId, String issueDate, String returnDate, String status) {
        this.gunId = gunId;
        this.userId = userId;
        this.issueId= issueId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }


    // Getters and Setters
    public String getGunId() {return gunId;}
    public String getUserId() {return userId;}
    public void setIssueId(String issueId) {this.issueId = issueId;}
    public String getIssueId() {return issueId;}
    public String getIssueDate() {return issueDate;}
    public String getReturnDate() {return returnDate;}
    public String getStatus() {return status;}


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("gunId", gunId);
        map.put("userId", userId);
        map.put("issuedId",issueId);
        map.put("issueDate", issueDate);
        map.put("returnDate", returnDate);
        map.put("status", status);
        return map;
    }
}
