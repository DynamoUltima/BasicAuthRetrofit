package com.example.joel.basicauthretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://salty-garden-58363.herokuapp.com/v1/users/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSecret();
            }
        });

    }
    private static String token;

    private void login(){
        Login login = new Login("dynamo.joey@gmail.com","Community53");
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                    token = response.body().getToken();
                }else{
                    Toast.makeText(MainActivity.this, "login not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error:(", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getSecret(){

     Call<ResponseBody> call = userClient.getSecret(token);

     call.enqueue(new Callback<ResponseBody>() {
         @Override
         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             if (response.isSuccessful()) {
                 try {
                     Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }else{
                 Toast.makeText(MainActivity.this, "token is not correct", Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onFailure(Call<ResponseBody> call, Throwable t) {

         }
     });

    }

}
