package com.example.activetechproject;
import java.util.Map;

public class Event {
    private String id;
    private String Date;
    private String Description;
    private Map<String, Object> Location; // Location as a Map for latitude, longitude, and isOnline
    private String Name;
    private String organization;
    private String Time;
    private String imageUrl;


    // Default constructor required for Firestore
    public Event() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDate() { return Date; }
    public void setDate(String date) { this.Date = date; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { this.Description = description; }

    // Location getter and setter for Map structure
    public Map<String, Object> getLocation() { return Location; }
    public void setLocation(Map<String, Object> location) { this.Location = location; }

    public String getName() { return Name; }
    public void setName(String name) { this.Name = name; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getTime() { return Time; }
    public void setTime(String time) { this.Time = time; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}