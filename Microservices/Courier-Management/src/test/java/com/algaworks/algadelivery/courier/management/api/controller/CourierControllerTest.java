package com.algaworks.algadelivery.courier.management.api.controller;

import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/couriers";
    }

    @Test
    void ShouldReturn201WhenRegisteringCourier() {
        // Given
        String courierInputJson = """
                            {
                                "name": "John Doe",
                                "phone": "1234567890"
                            }
                """;
        // When
        RestAssured.given()
                .body(courierInputJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("John Doe"))
                .body("phone", Matchers.equalTo("1234567890"));
    }

    @Test
    void shouldReturn200WhenGettingCouriers() {
        // Given
        String courierInputJson = """
                            {
                                "name": "Jane Doe",
                                "phone": "0987654321"
                            }
                """;
        UUID courierId = RestAssured.given()
                .body(courierInputJson)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .getBody()
                .as(Courier.class).getId();

        // When
        RestAssured.given()
                .pathParam("courierId", courierId)
                .accept(ContentType.JSON)
                .when()
                .get("/{courierId}")
                .then()
                .statusCode(OK.value())
                .body("id", Matchers.equalTo(courierId.toString()))
                .body("name", Matchers.equalTo("Jane Doe"));
    }
}