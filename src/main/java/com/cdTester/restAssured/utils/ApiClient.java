package com.cdTester.restAssured.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.cdTester.restAssured.config.ConfigManager;

import java.io.File;
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
    request += "Timeout: " + config.getTimeout(this.api) + "<br/>";
    request += "content-type: json<br/>";
    request += "Headers:<ul><li>Accept: application/json</li>";

    Map<String, String> headers = config.getHeaders(this.api);
    if (!headers.isEmpty()) {
      for (String key: headers.keySet()) {
        requestSpec.header(key, headers.get(key));
        request += "<li>" + key + ": " + headers.get(key) + "\n</li>";
      }
    }

    String authType = config.getAuthType(this.api);
    String authKey = config.getAuthKey(this.api);
    if (authType != null) {
      if (authType.equals("X-API-Key")) {
        requestSpec.header("X-API-Key", authKey);
        request += "<li>X-API-Key: " + authKey + "</li>";
        request += "</ul>";
      }
      else if (authType.equals("basic")) {
        String userName = config.getUsername(this.api);
        String password = config.getPassword(this.api);
        requestSpec.auth().basic(userName, password);
        request += "</ul>";
        request += "Authorization: Basic" + requestSpec.auth().toString() + "<br/>";
      }
      else if (authType.equals("bearer")) {
        requestSpec.header("Authorization", "Bearer " + authKey);
        request += "</ul>";
        request += "Authorization: Bearer " + authKey + "<br/>";
      }
      else {
        request += "</ul>";
      }
    }
    return requestSpec;
  }

  private void responseToString (Response resp) {
    response = "Headers: <br/><ul>" +
          "<li>Date: " + resp.getHeader("Date") + "</li>" +
          "<li>Content-Type: " + resp.getContentType() + "</li></ul>" +
          "Cookies: " + resp.cookies().toString() + "<br/>" +
          "Status: " + resp.statusCode() + " " + resp.statusLine() + "<br/>" +
          "Response Time: " + resp.getTime() + "ms<br/>" +
          "Body: " + resp.getBody().asString() + "<br/>";
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


  public Response post(String endpoint, String multiFormData) {
    request = "POST: " + RestAssured.baseURI + endpoint + "\n<br/>" +
          "      Multiform:" + multiFormData + "\n<br/>";

    Response resp = getRequestSpec()
          .multiPart(new File(multiFormData))
          .log().all()
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
