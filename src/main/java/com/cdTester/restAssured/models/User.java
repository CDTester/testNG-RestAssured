package com.cdTester.restAssured.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * User model representing the user entity in the API.
 * This is a POJO class with necessary fields and annotations for JSON serialization/deserialization.
 * A POJO (Plain Old Java Object) is a simple Java object, although a true POJO should not import any libraries
 * By using getter and setter methods, we can control access to the fields and maintain encapsulation.
 */

//@JsonPropertyOrder({"id", "username", "name", "email",  "phone", "address", "website"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
  @JsonProperty("id")
  private int id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonProperty("username")
  private String username;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("website")
  private String website;

  @JsonProperty("address")
  private JsonNode address;

  @JsonProperty("company")
  private JsonNode company;


  // Constructors
  public User() {}

  public User(int id, String name, String email, String username) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.username = username;
  }

  public User(String name, String email, String username) {
    this.name = name;
    this.email = email;
    this.username = username;
  }

  // Getters and Setters
  @JsonGetter("id")
  public int getId() { return id; }
  @JsonSetter("id")
  public void setId(int id) { this.id = id; }

  @JsonGetter("name")
  public String getName() { return name; }
  @JsonSetter("name")
  public void setName(String name) { this.name = name; }

  @JsonGetter("email")
  public String getEmail() { return email; }
  @JsonSetter("email")
  public void setEmail(String email) { this.email = email; }

  @JsonGetter("username")
  public String getUsername() { return username; }
  @JsonSetter("username")
  public void setUsername(String username) { this.username = username; }

  @JsonGetter("phone")
  public String getPhone() { return phone; }
  @JsonSetter("phone")
  public void setPhone(String phone) { this.phone = phone; }

  @JsonGetter("website")
  public String getWebsite() { return website; }
  @JsonSetter("website")
  public void setWebsite(String website) { this.website = website; }

  @JsonGetter("address")
  public JsonNode getAddress() { return address; }
  @JsonSetter("website")
  public void setAddress(JsonNode address) { this.address = address; }

  @JsonGetter("company")
  public JsonNode getCompany() { return company; }
  @JsonSetter("company")
  public void setCompany(JsonNode company) { this.company = company; }

  /**
   * Override toString() method for better readability when printing User objects.
   * User returns this value
   */
  @Override
  public String toString() {
    return "{id:" + id +
          ", name:'" + name +
          "', email:'" + email +
          "', phone:'" + phone +
          "', address:" + address +
          "', website:'" + website +
          "', company:'" + company +
          "}";
  }
}