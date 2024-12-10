package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

// Tell Springboot ot look for a main configuration class (@springbootApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    // This annotation is used in combination with TestContainers to
    // automatically configure the connection to the test mongodb
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static{
        mongoDBContainer.start();
    }

    @Test
    void createProductTest(){
        String requestBody = """
                {
                 "name":"Samsung TV",
                 "description" : "Samsung TV - Model 2024",
                 "price" : "2000"
                }
                """;
        //BDD -0  Behavioural Driven Development
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name",Matchers.equalTo("Samsung TV"))
                .body("description",Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price",Matchers.equalTo(2000));
    }

    @Test
    void getAllProductsTest(){
        String requestBody = """
                {
                    "name":"Samsung TV",
                    "description":"Samsung LCD TV 2016 Model",
                    "price":"2000"
                }
                """;

        //BDD - 0 Behavioural Driven Development(Given, When, Then)
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung LCD TV 2016 Model"))
                .body("price", Matchers.equalTo(2000));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung LCD TV 2016 Model"))
                .body("[0].price", Matchers.equalTo(2000));

    }

    @Test
    void updateProductTest() {
        String createRequestBody = """
            {
                "name": "Old TV",
                "description": "Old TV - Model 2023",
                "price": "1500"
            }
            """;

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(createRequestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .extract()
                .path("id");

        String updateRequestBody = """
            {
                "name": "Updated TV",
                "description": "Updated TV - Model 2024",
                "price": "2000"
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(updateRequestBody)
                .when()
                .put("/api/product/" + productId)
                .then()
                .log().all()
                .statusCode(204);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Updated TV"))
                .body("[0].description", Matchers.equalTo("Updated TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000));
    }

    @Test
    void deleteProductTest() {
        String createRequestBody = """
            {
                "name": "Temporary TV",
                "description": "Temporary TV - Model 2023",
                "price": "1200"
            }
            """;

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(createRequestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .extract()
                .path("id");

        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/product/" + productId)
                .then()
                .log().all()
                .statusCode(204);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("find { it.id == '" + productId + "' }", Matchers.nullValue());
    }


}
