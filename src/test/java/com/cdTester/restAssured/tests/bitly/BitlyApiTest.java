package com.cdTester.restAssured.tests.bitly;

import com.aventstack.extentreports.ExtentTest;
import com.cdTester.restAssured.tests.base.BaseTest;
import com.cdTester.restAssured.utils.ApiClient;
import com.cdTester.restAssured.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BitlyApiTest extends BaseTest {

  @Test(groups = {"smoke", "bitlyApi", "get"}, priority = 1, description = "[403] GET Forbidden response when incorrect bearer auth token is provided")
  public void getShow() {
    ApiClient api = new ApiClient(config, "bitly");

    ExtentTest step1 = extentStep("When the /user endpoint is called with an incorrect bearer auth token");
    Response response = api.get("/user");
    step1.pass(api.request);

    ExtentTest step2 = extentStep("Then the status code should be 403");
    step2.info("Status code should be 403 for Forbidden");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 403, "Status: " +response.statusCode());

    ExtentTest step4 = extentStep("And the error message should say 'FORBIDDEN'");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "FORBIDDEN", "Body: " + response.body());

  }



}