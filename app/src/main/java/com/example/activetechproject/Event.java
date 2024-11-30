package com.example.activetechproject;
import java.util.Map;
public class Event {
    private String id; // Unique identifier for the event
    private String Date; // Date of the event
    private String Description; // Description of the event
    private Map<String, Object> Location; // Location details (latitude, longitude, and isOnline status)
    private String Name; // Name of the event
    private String organization; // Organization hosting the event
    private String Time; // Time of the event
    private String imageUrl; // URL for the event's image

    // Default constructor required for Firestore
    public Event() {}

    // Getters and setters for each field
    public String getId() { return id; } // Get the event ID
    public void setId(String id) { this.id = id; } // Set the event ID

    public String getDate() { return Date; } // Get the event date
    public void setDate(String date) { this.Date = date; } // Set the event date

    public String getDescription() { return Description; } // Get the event description
    public void setDescription(String description) { this.Description = description; } // Set the event description

    // Get and set the location details (stored as a map)
    public Map<String, Object> getLocation() { return Location; } // Get the event location
    public void setLocation(Map<String, Object> location) { this.Location = location; } // Set the event location

    public String getName() { return Name; } // Get the event name
    public void setName(String name) { this.Name = name; } // Set the event name

    public String getOrganization() { return organization; } // Get the organization name
    public void setOrganization(String organization) { this.organization = organization; } // Set the organization name

    public String getTime() { return Time; } // Get the event time
    public void setTime(String time) { this.Time = time; } // Set the event time

    public String getImageUrl() { return imageUrl; } // Get the event image URL
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; } // Set the event image URL
}
