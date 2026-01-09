package restassured;

import dto.ContactDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContactTestsRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzMTk1MjUsImlhdCI6MTc2NzcxOTUyNX0.YIT3QPatbBWpg9XeYSqdYFj0Hu6yUFoQCkGvz__BJqI";

    String id;

    ContactDTO contact = ContactDTO.builder()
            .name("Donna")
            .lastName("Dowww")
            .email("donna@gmail.com")
            .phone("123500005555")
            .address("TA")
            .description("Friend").build();

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

      String message =  given()
                .body(contact)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
      String []all = message.split(": ");
      id = all[1];
    }


    @Test
    public void updateExistsContactSuccess(){
        String name = contact.getName();
        contact.setId(id);
        contact.setName("wwwwwww");

        given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));

    }
}
