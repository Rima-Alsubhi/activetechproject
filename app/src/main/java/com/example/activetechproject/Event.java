package com.example.activetechproject;
public class Event {
    private String id;
    private String Date;
    private String Description;
    private String Location;
    private String Name;
    private String The_organization;
    private String Time;
    private String imageUrl;

    // Default constructor required for Firestore
    public Event() {}

    public String getId(){return id;}
    public void setId(String id){this.id=id;}
    // Getters and setters
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getThe_organization() {
        return The_organization;
    }

    public void setThe_organization(String the_organization) {
        this.The_organization = the_organization;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}