package com.cdTester.restAssured.tests.user;

import com.aventstack.extentreports.ExtentTest;
import com.cdTester.restAssured.models.User;
import com.cdTester.restAssured.tests.base.BaseTest;
import com.cdTester.restAssured.utils.ApiClient;
import com.cdTester.restAssured.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testUtils.TestDataProvider;


@Test(groups = {"userApi"})
public class UserApiDataProviderTest extends BaseTest {


  //This method will provide data to any test method that declares that its Data Provider
  //is named "test1"
  @DataProvider(name = "simpleData")
  public Object[][] getSimpleData() {
    return new Object[][] {
          {"user1", "pass1"},
          {"user2", "pass2"},
          {"user3", "pass3"}
    };
  }

  @Test(dataProvider = "simpleData" ,groups = {"dataProvider"}, priority = 1, description = "Use Data Provider within same class")
  public void dataProviderSimpleTest(String username, String password) {
    ExtentTest step = extentStep("Print password for " + username);
    step.pass("Password: " + password);
  }

  @Test(dataProvider = "userTestData", dataProviderClass = TestDataProvider.class, groups = {"dataProvider"}, priority = 2, description = "Use Data Provider loaded from user.json")
  public void dataProviderUserTest(JsonNode user) {
    ApiClient api = new ApiClient(config, "users");

    ExtentTest given = extentStep("GIVEN a user has the ID=" + user.get("id").asText());
    // if test is not run from testng.xml, default to user ID 1
    extentAssertEquals(given, user.get("id"), user.get("id"), "id: " + user.get("id").asText());

    ExtentTest when = extentStep("WHEN the GET /users/" + user.get("id").asText() + " API is called");
    Response response = api.get("/users/" + user.get("id").asText());
    when.info(api.request);
    when.pass(api.response);

    ExtentTest then = extentStep("THEN the API should return with a status code of 200");
    int status = response.getStatusCode();
    extentAssertEquals(then, status, 200, "Return status not as expected");

    ExtentTest thenAnd1 = extentStep("AND the users name details should be " + user.get("name").asText());
    User userActual = JsonUtils.deserialize(response, User.class);
    extentAssertEquals(thenAnd1, userActual.getName(), user.get("name").asText(), "name: " + userActual.getName());

    ExtentTest thenAnd2 = extentStep("AND the username should be " + user.get("username").asText());
    extentAssertEquals(thenAnd2, userActual.getUsername(), user.get("username").asText(), "username: " + userActual.getUsername());

    ExtentTest thenAnd3 = extentStep("AND the email should be " + user.get("email").asText());
    extentAssertEquals(thenAnd3, userActual.getEmail(), user.get("email").asText(), "email: " + userActual.getEmail());

    ExtentTest thenAnd4 = extentStep("AND the phone number should be " + user.get("phone").asText());
    extentAssertEquals(thenAnd4, userActual.getPhone(), user.get("phone").asText(), "phone: " + userActual.getPhone());
  }



}