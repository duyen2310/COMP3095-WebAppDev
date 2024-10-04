package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

//tell springs boot to look for main config class (@springbootaplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
    //this annotation is used in combination with testcontained to automatically configure the connecttion to
    // the test mongodbcontainer
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;
    //http://localhost:port/api/product

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port=port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void createProductTest(){
        String requestBody = """
                {
                    "name" : "Samsung TV",
                    "description" : "Samsung TV - Model 2024",
                    "price" : "2000";   
                }            
                """;
        //BDD = 0 Behavioural driven development (given, when, then)

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
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));

    }

    @Test
    void getAllProductsTest(){
        String requestBody = """
                {
                    "name" : "Samsung TV",
                    "description" : "Samsung TV - Model 2024",
                    "price" : "2000";   
                }            
                """;
        //BDD = 0 Behavioural driven development (given, when, then)

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
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get()
                .then()
                .log().all().
                statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000));
    }
}

