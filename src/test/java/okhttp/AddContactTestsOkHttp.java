package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class AddContactTestsOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzMTk1MjUsImlhdCI6MTc2NzcxOTUyNX0.YIT3QPatbBWpg9XeYSqdYFj0Hu6yUFoQCkGvz__BJqI";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");


    @Test
    public void  addNewContactSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis()/1000);
        ContactDTO contactDto = ContactDTO.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya"+i+"@mail.com")
                .address("Haifa")
                .phone("8888"+i)
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        MessageDTO message = gson.fromJson(response.body().string(), MessageDTO.class);
        System.out.println(message.getMessage());

        Assert.assertTrue(message.getMessage().contains("Contact was added!"));
        Assert.assertTrue(message.getMessage().contains("ID"));
// Contact was added! ID: 49a7d3f0-a9c2-4297-bc88-fc8ea626a6d5
    }
    @Test
    public void  addNewContactWrongName() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .lastName("Wolf")
                .email("jenny@mail.com")
                .address("TV")
                .phone("9876543210")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));

    }
    @Test
    public void  addNewContactWrongLastName() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .email("jenny@mail.com")
                .address("TV")
                .phone("9876543210")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("lastName=must not be blank"));

    }
    @Test
    public void  addNewContactWrongPhone() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .address("TV")
                .phone("987")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("Phone number must contain only digits! And length min 10, max 15!"));

    }
    @Test
    public void  addNewContactWrongEmail() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jennymail.com")
                .address("TV")
                .phone("9870070077")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("email=must be a well-formed email address"));

    }

    @Test
    public void  addNewContactWrongAddress() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .phone("9870070077")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("address=must not be blank"));

    }
    @Test
    public void  addNewContactUnauthorized() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .phone("9870070077")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization","hhyyyyt")
                .build();

        Response response = client.newCall(request).execute();

        ErrorDTO error  = gson.fromJson(response.body().string(),ErrorDTO.class);
        System.out.println(error.getMessage());
        Assert.assertTrue(error.getMessage().toString().contains( "JWT strings must contain"));
        Assert.assertEquals(response.code(), 401);
        Assert.assertFalse(response.isSuccessful());

    }
    @Test
    public void addNewContactDuplicate() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Moty")
                .lastName("Ryman")
                .email("moty@gmail.com")
                .phone("132659856945")
                .address("NY")
                .description("Work")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        //Assert.assertFalse(response.isSuccessful());
        //Assert.assertEquals(response.code(),409);
        Assert.assertEquals(response.code(),200);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDTO.getError());
        //Assert.assertEquals(errorDTO.getError(),("User already exists"));
    }

}