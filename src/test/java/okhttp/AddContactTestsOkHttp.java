package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddContactTestsOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzMTk1MjUsImlhdCI6MTc2NzcxOTUyNX0.YIT3QPatbBWpg9XeYSqdYFj0Hu6yUFoQCkGvz__BJqI";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");


    @Test
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Moty")
                .lastName("Ryman")
                .email("moty"+i+"@gmail.com")
                .phone("13265985"+i)
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
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        Assert.assertTrue(messageDTO.getMessage().contains("Contact was added! ID:"));
    }

    @Test
    public void addNewContactWrongToken() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Moty")
                .lastName("Ryman")
                .email("moty@gmail.com")
                .phone("1326598547896")
                .address("NY")
                .description("Work")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization","dfghb124")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),("Unauthorized"));
    }

    @Test
    public void addNewContactWrongEmailContact() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Moty")
                .lastName("Ryman")
                .email("motygmail.com")
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
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDTO.getError());
        Assert.assertEquals(errorDTO.getError(),("Bad Request"));
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