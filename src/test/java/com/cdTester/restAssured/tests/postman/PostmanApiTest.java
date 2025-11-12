package com.cdTester.restAssured.tests.postman;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.cdTester.restAssured.tests.base.BaseTest;
import com.cdTester.restAssured.utils.ApiClient;
import com.cdTester.restAssured.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class PostmanApiTest extends BaseTest {

  @Test(groups = {"smoke", "postmanApi", "get"}, priority = 1, description = "[401] GET Unauthorized response when incorrect API key is provided")
  public void getShow() {
    ApiClient api = new ApiClient(config, "postman");

    ExtentTest step1 = extentStep("When the /me endpoint is called with an incorrect X-API-Key");
    Response response = api.get("/me");
    step1.pass(api.request);

    ExtentTest step2 = extentStep("Then the status code should be 401");
    step2.info("Status code should be 401 for Unauthorized");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 401, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'AuthenticationError'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("error").get("name").asText();
    extentAssertEquals(step3, errName, "AuthenticationError", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should say 'Invalid API Key. Every request requires a valid API Key to be sent.'");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("error").get("message").asText();
    extentAssertEquals(step4, errMessage, "Invalid API Key. Every request requires a valid API Key to be sent.", "Body: " + response.body());

  }



}