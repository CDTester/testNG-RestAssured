package com.cdTester.restAssured.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.cdTester.restAssured.config.ConfigManager;
import java.util.Map;

public class ApiClient {
  public String api;
  private final ConfigManager config;
  public String request;
  public String response;
  public String environment;

  public ApiClient(ConfigManager config, String api) {
    this.api = api;
    this.config = config;
    RestAssured.baseURI = config.getBaseUrl(api);
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    this.environment = config.getEnvironment();
  }

  public RequestSpecification getRequestSpec() {
    RequestSpecification requestSpec = RestAssured.given()
          .contentType(ContentType.JSON)
          .header("Accept", "application/json");
    request += "content-type: json\n<br/>Headers: Accept: application/json\n<br/>";
    request += "Timeout: " + config.getTimeout(this.api) + "\n<br/>";

    String authType = config.getAuthType(this.api);
    String authKey = config.getAuthKey(this.api);
    if (authType != null) {
      if (authType.equals("X-API-Key")) {
        requestSpec.header(authKey, authKey);
        request += "X-API-Key: " + authKey + "\n<br/>";
      }
      if (authType.equals("basic")) {
        String userName = config.getUsername(this.api);
        String password = config.getPassword(this.api);
        requestSpec.auth().basic(userName, password);
        request += "Authorization: Basic" + requestSpec.auth().toString() + "\n<br/>";
      }
      if (authType.equals("bearer")) {
        requestSpec.header("Authorization", "Bearer " + authKey);
        request += "Authorization: Bearer " + authKey + "\n<br/>";
      }
    }
    return requestSpec;
  }

  private void responseToString (Response resp) {
    response = "Headers: \n<br/>" +
          "<nbsp/><nbsp/>Date: " + resp.getHeader("Date") + "\n<br/>" +
          "<nbsp/><nbsp/>Content-Type: " + resp.getContentType() + "\n<br/>" +
          "Cookies: " + resp.cookies().toString() + "\n<br/>" +
          "Status: " + resp.statusCode() + " " + resp.statusLine() + "\n<br/>" +
          "Response Time: " + resp.getTime() + "ms\n<br/>" +
          "Body: " + resp.getBody().asString() + "\n<br/>";
  }

  public Response get(String endpoint) {
    request = "GET :" + RestAssured.baseURI + endpoint + "\n<br/>";

    Response resp = getRequestSpec()
          .when()
          .get(endpoint)
          .then()
          .extract()
          .response();

    responseToString(resp);
    return resp;
  }

  public Response get(String endpoint, Map<String, String> params) {
    request = "GET :" + RestAssured.baseURI + endpoint + "?";
    for (String keys: params.keySet()) {
      if (request.endsWith("?")) {
        request += keys + "=" + params.get(keys);
      }
      else {
        request += "&" + keys + "=" + params.get(keys);
      }
    }
    request += "\n<br/>";

    Response resp = getRequestSpec()
          .queryParams(params)
          .when()
          .get(endpoint)
          .then()
          .extract()
          .response();
    responseToString(resp);

    return resp;
  }

  public Response post(String endpoint, Object body) {
    request = "POST: " + RestAssured.baseURI + endpoint + "\n<br/>" +
              "      Body:" + body.toString() + "\n<br/>";

    Response resp = getRequestSpec()
          .body(body)
          .when()
          .post(endpoint)
          .then()
          .extract()
          .response();
    responseToString(resp);

    return resp;
  }

  public Response put(String endpoint, Object body) {
    request = "PUT: " + RestAssured.baseURI + endpoint + "\n<br/>" +
          "      Body:" + body.toString() + "\n<br/>";

    Response resp = getRequestSpec()
          .body(body)
          .when()
          .put(endpoint)
          .then()
          .extract()
          .response();
    responseToString(resp);

    return resp;
  }

  public Response delete(String endpoint) {
    request = "DELETE: " + RestAssured.baseURI + endpoint + "\n<br/>";

    Response resp = getRequestSpec()
          .when()
          .delete(endpoint)
          .then()
          .extract()
          .response();
    responseToString(resp);

    return resp;
  }

  public Response patch(String endpoint, Object body) {
    request = "PATCH: " + RestAssured.baseURI + endpoint + "\n<br/>" +
          "      Body:" + body.toString() + "\n<br/>";

    Response resp = getRequestSpec()
          .body(body)
          .when()
          .patch(endpoint)
          .then()
          .extract()
          .response();
    responseToString(resp);

    return resp;
  }
}
