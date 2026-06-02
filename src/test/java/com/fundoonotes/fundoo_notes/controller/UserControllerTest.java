//package com.fundoonotes.fundoo_notes.controller;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;  // for validation
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//
//
//
//
//@TestMethodOrder(OrderAnnotation.class)    //@TestMethodOrder is a type-level annotation that define test order wise run hoga..
//public class UserControllerTest {
//
//    @BeforeEach
//    public void setup() {
//        RestAssured.baseURI = "http://localhost:8080";
//    }
//
//    // REGISTER API TEST
//    @Test
//    @Order(1)   //order m run krayega sbse phle register fr login..
//    public void registerTest() {
//        String requestBody = """
//                {
//                  "name":"Naveen",
//                  "email":"test@gmail.com",
//                  "password":"123456"
//                }
//                """;
//
//                given()                 // ye request preparation block hai
//                .log().all()   //console m req. print krega..
//                .contentType(ContentType.JSON) //server ko data json format m chahiye
//                .body(requestBody)  // request body attach kr ra hai jisse json ko object m print krayega
//
//                .when()    // Actual request send karne wala block.
//                .post("/api/users/register")
//
//                .then()
//                .log().all()
//                .statusCode(201)
//                .body("success", equalTo(true));
//    }
//
//    // LOGIN API TEST
//
//    @Test
//    @Order(2)
//    public void loginTest() {
//
//        String requestBody = """
//                {
//                  "email":"test@gmail.com",
//                  "password":"123456"
//                }
//                """;
//
//        given()
//                .log().all()
//                .contentType(ContentType.JSON)
//                .body(requestBody)
//
//                .when()
//                .post("/api/users/login")
//
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("success", equalTo(true));
//    }
//}