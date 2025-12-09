package com.cdTester.restAssured.tests.tvmaze;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.cdTester.restAssured.tests.base.BaseTest;
import com.cdTester.restAssured.utils.ApiClient;
import com.cdTester.restAssured.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class TvApiTest extends BaseTest {

  @Test(groups = {"smoke", "tvApi", "get"}, priority = 1, description = "[200] GET shows/82")
  public void getShow() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the /shows/82 API is called");
    Response response = api.get("/shows/82");
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the status code should be 200");
    step2.info("Status code should be 200 for OK");
    extentAssertEquals(step2, response.statusCode(), 200, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the show details are returned");
    step3.info(response.body().asPrettyString());
    extentAssertNotNull(step3, response.body(),  "");
  }

  @Test(groups = {"smoke", "tvApi", "get"}, priority = 2, description = "[200] GET search for tv show and display poster")
  public void getShowPoster() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the /search/shows?q='the expanse' API is called");
    Response response = api.get("/search/shows?q='the expanse'");
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the status code should be 200");
    step2.info("Status code should be 200 for OK");
    extentAssertEquals(step2, response.statusCode(), 200, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the poster from the show details can be retrieved");
    String url = JsonUtils.deserializeList(response, JsonNode.class).get(0).get("show").get("image").get("original").asText();
    step3.info(MediaEntityBuilder.createScreenCaptureFromPath(url).build());
    extentAssertNotNull(step3, url, "original: " + url);
  }


  @Test(groups = {"smoke", "tvApi", "get"}, priority = 3, description = "[400] GET Bad Request response when the wrong query parameter is used")
  public void getBadRequestIncorrectParams() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the /search/shows API is call with wrongParam='lost'");
    Map<String,String> params = new HashMap<>();
    params.put("wrongParam","lost");
    Response response = api.get("/search/shows", params);
    step1.pass(api.request);

    ExtentTest step2 = extentStep("Then the response will have a status of 400");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 400, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'Bad Request'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("name").asText();
    extentAssertEquals(step3, errName, "Bad Request", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should say 'Missing required parameters: q'");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "Missing required parameters: q", "Body: " + response.body());
  }

  @Test(groups = {"smoke", "tvApi", "get"}, priority = 3, description = "[400] GET Bad Request response when the query parameter is missing")
  public void getBadRequestMissingParams() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the /search/shows API is call with wrongParam='lost'");
    Response response = api.get("/search/shows");
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the response will have a status of 400");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 400, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'Bad Request'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("name").asText();
    extentAssertEquals(step3, errName, "Bad Request", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should say 'Missing required parameters: q'");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "Missing required parameters: q", "Body: " + response.body());
  }


  @Test(groups = {"smoke", "tvApi", "get"}, priority = 4, description = "[404] GET Not Found response when an invalid endpoint is used")
  public void getNotFoundUrl() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When an invalid endpoint /noshows/82 is called");
    Response response = api.get("/noshows/82");
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the response will have a status of 404");
    step2.info("Status code should be 404 for Not Found");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 404, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'Not Found'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("name").asText();
    extentAssertEquals(step3, errName, "Not Found", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should say 'Page not found.'");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "Page not found.", "Body: " + response.body().asPrettyString());

    ExtentTest step5 = extentStep("And the previous error name should 'Invalid Route'");
    step5.info(api.response);
    String prevName = JsonUtils.deserialize(response, JsonNode.class).get("previous").get("name").asText();
    extentAssertEquals(step5, prevName, "Invalid Route", "Body: " + response.body().asPrettyString());
  }


  @Test(groups = {"smoke", "tvApi", "post"}, priority = 5, description = "[404] GET Not Found response when show id does not exist")
  public void getNotFoundValidRequest() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the endpoint /shows/ is called with a show id that does not exist");
    Response response = api.get("/shows/999999");
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the response will have a status of 404");
    step2.info("Status code should be 404 for Not Found");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 404, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'Not Found'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("name").asText();
    extentAssertEquals(step3, errName, "Not Found", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should be empty");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "", "Body: " + response.body().asPrettyString());
  }


  @Test(groups = {"smoke", "tvApi", "post"}, priority = 6, description = "[405] POST Method Not Allowed response when an invalid endpoint is used")
  public void getMethodNotAllowed() {
    ApiClient api = new ApiClient(config, "tv");

    ExtentTest step1 = extentStep("When the endpoint /shows/ is called with a POST method");
    Map<String,String> body = new HashMap<>();
    body.put("name","Game of Thrones");
    Response response = api.post("shows",body);
    step1.info(api.request);
    step1.pass(api.response);

    ExtentTest step2 = extentStep("Then the response will have a status of 405");
    step2.info("Status code should be 405 for Method Not Allowed");
    step2.info(api.response);
    extentAssertEquals(step2, response.statusCode(), 4405, "Status: " +response.statusCode());

    ExtentTest step3 = extentStep("And the error name should be 'Method Not Allowed'");
    step3.info(api.response);
    String errName = JsonUtils.deserialize(response, JsonNode.class).get("name").asText();
    extentAssertEquals(step3, errName, "Method Not Allowed", "Body: " + response.body().asPrettyString());

    ExtentTest step4 = extentStep("And the error message should indicate what methods are allowed");
    step4.info(api.response);
    String errMessage = JsonUtils.deserialize(response, JsonNode.class).get("message").asText();
    extentAssertEquals(step4, errMessage, "Method Not Allowed. This URL can only handle the following request methods: GET, HEAD.", "Body: " + response.body().asPrettyString());
  }

}