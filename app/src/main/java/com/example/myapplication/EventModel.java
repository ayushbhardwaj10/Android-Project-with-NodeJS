package com.example.myapplication;

public class EventModel {
 String eventName;
 String venueName;
 String categoryName;
 String date;
 String time;
 String imgUrl;

 String id;

 public EventModel(String eventName, String venueName, String categoryName, String date, String time, String imgUrl, String id) {
  this.eventName = eventName;
  this.venueName = venueName;
  this.categoryName = categoryName;
  this.date = date;
  this.time = time;
  this.imgUrl = imgUrl;
  this.id = id;
 }


 public String getEventName() {
  return eventName;
 }

 public String getVenueName() {
  return venueName;
 }

 public String getCategoryName() {
  return categoryName;
 }

 public String getDate() {
  return date;
 }

 public String getTime() {
  return time;
 }

 public String getImgUrl() {
  return imgUrl;
 }

 public String getId() {
  return id;
 }
}
