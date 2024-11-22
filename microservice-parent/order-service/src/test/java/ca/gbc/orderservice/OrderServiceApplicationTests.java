package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static{
        postgreSQLContainer.start();
    }

    @Test
    void shouldSubmitOrder() {
        String order = """
                {
                "skuCode":"apple_tablet_2024",
                "price":500,
                "quantity":16
                }
                """;

        // Week 10
        // Mock a call to inventory service
        InventoryClientStub.stubInventoryCall("apple_tablet_2024", 10);

        var responseBody = RestAssured.given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();
        assertThat(responseBody, Matchers.is("Order placed successfully"));
    }
}
