package restassured;

import dto.ContactDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzMTk1MjUsImlhdCI6MTc2NzcxOTUyNX0.YIT3QPatbBWpg9XeYSqdYFj0Hu6yUFoQCkGvz__BJqI";
    String id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Dowww")
                .email("donna"+i+"@gmail.com")
                .phone("12350000"+i)
                .address("TA")
                .description("Friend").build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        // Contact was added! ID: 49a7d3f0-a9c2-4297-bc88-fc8ea626a6d5
        String[]all = message.split(" ");
        id = all[4];
    }

   @Test
   public void deleteContactByIDSuccess(){
     MessageDTO messageDTO =   given()
                .header("Authorization", token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(MessageDTO.class);
       Assert.assertEquals(messageDTO.getMessage(), "Contact was deleted!");

   }

    @Test
    public void deleteContactByIDSuccess2(){
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was deleted!"));


    }

    @Test
    public void deleteContactByIDWrongToken(){
        given()
                .header("Authorization", "vhjkl")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401);


    }

}
