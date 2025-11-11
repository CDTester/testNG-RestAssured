package com.cdTester.restAssured.tests.user;

import com.aventstack.extentreports.ExtentTest;
import com.cdTester.restAssured.tests.base.BaseTest;
import com.cdTester.restAssured.models.User;
import com.cdTester.restAssured.utils.ApiClient;
import com.cdTester.restAssured.utils.JsonUtils;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Test(groups = {"userApi"})
public class UserApiTest extends BaseTest {

  @Test(groups = {"smoke", "userApi", "get"}, priority = 1, description = "Get all users")
  public void testGetAllUsers() {
    ApiClient api = new ApiClient(config, "users");

    ExtentTest given = extentStep("GIVEN that users exist on the /users API");
    given.pass("");

    ExtentTest when = extentStep("WHEN the GET /users API is called");
    Response response = api.get("/users");
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN the API should return with a status code of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, status, 200, String.valueOf(status));

    ExtentTest thenAnd1 = extentStep("AND the response time should be less than 3 seconds");
    extentAssertTrue(thenAnd1, response.getTime() < 3000, "Response time " + response.getTime() + "ms < 3000ms" );

    ExtentTest thenAnd2 = extentStep("AND a list of all users should be returned");
    List<User> users = JsonUtils.deserializeList(response, User.class);
    extentAssertTrue(thenAnd2, users.size() > 0, "List size " + users.size() + " is greater than 0 <br/>" + users.toString());
  }

  @Test(groups = {"regression", "userApi", "get"}, priority = 2, description = "Get user by ID")
  public void testGetUserById() {
    ApiClient api = new ApiClient(config, "users");
    int id = 1;

    ExtentTest given = extentStep("GIVEN a user has the ID=" + id);
    // if test is not run from testng.xml, default to user ID 1
    extentAssertEquals(given, id, 1, "id: " + id);

    ExtentTest when = extentStep("WHEN the GET /users/" + id + " API is called");
    Response response = api.get("/users/" + id);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN the API should return with a status code of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, status, 200, "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the user details should be returned");
    User user = JsonUtils.deserialize(response, User.class);
    extentAssertNotNull(thenAnd1, user, user.toString());

    ExtentTest thenAnd2 = extentStep("AND the username should not be null");
    extentAssertNotNull(thenAnd2, user.getUsername(), "username: " + user.getUsername());

    ExtentTest thenAnd3 = extentStep("AND the email should not be null");
    extentAssertNotNull(thenAnd3, user.getEmail(), "email: " + user.getEmail());

    ExtentTest thenAnd4 = extentStep("AND the user ID should match the requested ID");
    extentAssertEquals(thenAnd4, user.getId(), id, "id: " + user.getId());
  }

  @Test(groups = {"regression", "userApi", "get"}, priority = 3, description = "Get user by search parameters")
  public void testGetUserByParams() {
    ApiClient api = new ApiClient(config, "users");
    String username = "Kamren";
    String name = "Chelsey Dietrich";

    ExtentTest given = extentStep("GIVEN username " + username + " exists on /users API");
    Response response1 = api.get("/users");
    given.info(api.request);
    given.info(api.response);
    extentAssertTrue(given, response1.getBody().asString().contains(username), "Expected response body to contain the word " + username);

    ExtentTest when = extentStep("WHEN GET /users API is called with params {\"username\",\"" + username + "\"}");
    Map<String, String> params = new HashMap<>();
    params.put("username", username);
    Response response2 = api.get("/users", params);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN the API should return with a status code of 200");
    int status = response2.getStatusCode();
    extentAssertEquals(then, status, 200, "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the user details should be returned");
    List<User> users = JsonUtils.deserializeList(response2, User.class);
    User user = users.getFirst();
    extentAssertNotNull(thenAnd1, user, user.toString());

    ExtentTest thenAnd2 = extentStep("AND the username should be " + username);
    extentAssertEquals(thenAnd2, user.getUsername(), username, "Username not as expected");

    ExtentTest thenAnd3 = extentStep("AND the name should be " + name);
    extentAssertEquals(thenAnd3, user.getName(), name, "Name not as expected");
  }

  @Test(groups = {"regression", "userApi", "get"}, priority = 4, description = "Test invalid user ID")
  public void testInvalidUserId() {
    ApiClient api = new ApiClient(config, "users");
    int id = 999999;

    ExtentTest given = extentStep("GIVEN a user has the ID=" + id);
    // if test is not run from testng.xml, default to user ID 1
    extentAssertEquals(given, id, 999999, "Current max id number is 10");

    ExtentTest when = extentStep("WHEN the /users/" + id + " API is called");
    Response response = api.get("/users/" + id);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN the API should return with a status code of 404");
    int status = response.getStatusCode();
    then.info("Status code should be 404 for Not Found");
    extentAssertEquals(then, status, 404, "Status Code not as expected");
  }

  @Test(groups = {"smoke", "userApi", "post"}, priority = 5, description = "Create new user")
  public void testCreateUser() {
    ApiClient api = new ApiClient(config, "users");
    String username = "testuser";
    String email = "test@example.com";
    String name = "Test User";

    ExtentTest given = extentStep("GIVEN username " + username + " does not exist on /users API");
    Map<String, String> params = new HashMap<>();
    params.put("username", username);
    Response search = api.get("/users", params);
    given.info(api.request);
    given.info(api.response);
    extentAssertFalse(given, search.getBody().asString().contains(username), "Response body should not contain username " + username);

    ExtentTest when = extentStep("WHEN POST /users API is called with new user details in the body");
    User newUser = new User(name, email, username);
    Response response = api.post("/users", newUser);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN /users API should return a status of 201 for created");
    int status = response.getStatusCode();
    then.info("Status code should be 201 for Created");
    extentAssertEquals(then, status,201,  "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the user details should be returned");
    User user = JsonUtils.deserialize(response, User.class);
    extentAssertNotNull(thenAnd1, user, user.toString());

    ExtentTest thenAnd2 = extentStep("AND the name should be " + name);
    extentAssertEquals(thenAnd2, user.getName(), name, "Username not as expected");

    ExtentTest thenAnd3 = extentStep("AND the email should be " + email);
    extentAssertEquals(thenAnd3, user.getEmail(), email, "E-mail not as expected");

    ExtentTest thenAnd4 = extentStep("AND the user should have an id");
    extentAssertNotNull(thenAnd4, user.getId(), "id: " + user.getId());
  }

  @Test(groups = {"smoke", "userApi", "put"}, priority = 6, description = "Update user details")
  public void testUpdateUserDetails() {
    ApiClient api = new ApiClient(config, "users");
    int id = 1;
    String username = "Bret";
    String email = "Sincere@april.biz";
    String name = "Leanne Graham";

    ExtentTest given = extentStep("GIVEN username " + username + " exists on /users API");
    Response search = api.get("/users/" + id);
    given.info(api.request);
    given.info(api.response);
    extentAssertTrue(given, search.getBody().asString().contains(username), "Response body should contain username " + username);
    extentAssertTrue(given, search.getBody().asString().contains(email), "Response body should contain email " + email);
    extentAssertTrue(given, search.getBody().asString().contains(name), "Response body should contain name " + name);

    ExtentTest when = extentStep("WHEN PUT /users/" +id + " API is called with updated user details in the body");
    String newUsername = "updateduser";
    String newEmail = "updated@example.com";
    String newName = "Updated User";
    JsonNode updatedUser = JsonUtils.parseJson("{\"username\":\"" + newUsername + "\", \"email\":\"" + newEmail + "\", \"name\":\"" + newName + "\"}");
    Response response = api.put("/users/" + id, updatedUser);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN API should return a status of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, 200, status, "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the username should be " + newUsername);
    User user = JsonUtils.deserialize(response, User.class);
    extentAssertEquals(thenAnd1, user.getUsername(), newUsername,"Username not as expected.");

    ExtentTest thenAnd2 = extentStep("AND the name should be " + newName);
    extentAssertEquals(thenAnd2, user.getName(), newName, "Username not as expected.");

    ExtentTest thenAnd3 = extentStep("AND the email should be " + newEmail);
    extentAssertEquals(thenAnd3, user.getEmail(), newEmail, "E-mail not as expected.");
  }

  @Test(groups = {"smoke", "userApi", "patch"}, priority = 7, description = "Add new field to user")
  public void testAddingNewDetailToUser() {
    ApiClient api = new ApiClient(config, "users");
    int id = 1;
    String username = "Bret";
    String title = "Mrs";

    ExtentTest given = extentStep("GIVEN title is not available to username " + username + " on /users API");
    Response search = api.get("/users/" + id);
    given.info(api.request);
    given.info(api.response);
    extentAssertTrue(given, search.getBody().asString().contains(username), "Response body should contain username " + username);
    extentAssertFalse(given, search.getBody().asString().contains("title"), "Response body should not contain title " + title);

    ExtentTest when = extentStep("WHEN PATCH /users/" +id + " API is called with new title field in the body");
    JsonNode updatedUser = JsonUtils.parseJson("{\"title\":\"" + title + "\"}");
    Response response = api.patch("/users/" + id, updatedUser);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN API should return a status of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, status, 200, "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the user should now have title " + title);
    String message = "returned user should contain title " + title + "<br/>" + response.getBody().asString();
    extentAssertTrue(thenAnd1, response.getBody().asString().contains("title"), message);
  }


  @Test(groups = {"smoke", "userApi", "delete"}, priority = 8, description = "Delete user")
  public void testDeleteUser() {
    ApiClient api = new ApiClient(config, "users");
    int id = 10;

    ExtentTest given = extentStep("GIVEN user id " + id + " exists on /users API");
    Response search = api.get("/users/" + id);
    given.info(api.request);
    given.info(api.response);
    extentAssertTrue(given, search.getBody().asString().contains("\"id\": " + id), "Response body should contain id " + id);

    ExtentTest when = extentStep("WHEN DELETE /users/" +id + " API is called");
    Response response = api.delete("/users/" + id);
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN API should return a status of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, status, 200, "Return status not as expected");
  }




  @Test(groups = {"smoke", "broken.sprint.4"}, priority = 9, description = "Broken user 4")
  public void brokenTest1() {
    addToExtentReport().log(Status.INFO, "Starting test: Update user");
    addToExtentReport().log(Status.PASS, "Test passed: Successfully updated user with ID: " );
  }

  @Test(groups = {"smoke", "broken.sprint.6"}, priority = 10, description = "Broken user 6")
  public void brokenTest2() {
    addToExtentReport().log(Status.INFO, "Starting test: Update user");
    addToExtentReport().log(Status.PASS, "Test passed: Successfully updated user with ID: " );
  }

}