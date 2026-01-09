package restassured;

import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AddNewContactTestsRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzMTk1MjUsImlhdCI6MTc2NzcxOTUyNX0.YIT3QPatbBWpg9XeYSqdYFj0Hu6yUFoQCkGvz__BJqI";
    String endpoint = "contacts";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }


    @Test
    public void addNewContactSuccess(){
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Dowww")
                .email("donna"+i+"@gmail.com")
                .phone("12350000"+i)
                .address("TA")
                .description("Friend").build();
     MessageDTO messageDTO =    given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(MessageDTO.class);
        System.out.println(messageDTO);
        Assert.assertTrue(messageDTO.getMessage().contains("Contact was added!"));

    }

    @Test
    public void addNewContactWrongName(){
        ContactDTO contactDTO = ContactDTO.builder()

                .lastName("Dowww")
                .email("donna@gmail.com")
                .phone("123500005555")
                .address("TA")
                .description("Friend").build();
        ErrorDTO errorDTO =    given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .extract()
                .response()
                .as(ErrorDTO.class);
        System.out.println(errorDTO);
        Assert.assertTrue(errorDTO.getMessage().toString().contains("name=must not be blank"));

    }
}
