import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GradeApiTest {

    @BeforeClass
    public void setup() {
        // Configure REST Assured to use the required base URI
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetAllPosts() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Asserts it is a non-empty array
    }

    @Test
    public void testGetSinglePost() {
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", not(emptyOrNullString())); // Asserts title is not empty
    }

    @Test
    public void testCreatePost() {
        // JSON body with title, body, and userId
        String requestBody = "{\n" +
                "  \"title\": \"my-test-title\",\n" +
                "  \"body\": \"my-test-body\",\n" +
                "  \"userId\": 1\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("my-test-title")); // Asserts the title matches
    }
    @Test
    public void testGetNonExistentPost() {
        // Sends a GET request to a non-existent resource and expects a 404 Not Found
        given()
                .when()
                .get("/posts/99999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeletePost() {
        // Sends a DELETE request and expects a 200 OK
        given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200);
    }
}