package com.example.devappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://process.isiforge.tn/")
            .addConverterFactory(GsonConverterFactory.create());
     Retrofit retrofit= builder.build();
     UserClient userClient= retrofit.create(UserClient.class);
     private  static  String token;

    EditText emailET;
    EditText pwdET;
   // EditText workspaceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs= this.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String t = prefs.getString("token", "");
        if(!t.equals("")) {

            Intent intent = new Intent(MainActivity.this, NavActivity.class);
            startActivity(intent);
        }
        emailET = (EditText)findViewById(R.id.loginEmail);
        pwdET = (EditText)findViewById(R.id.loginPassword);


    }

    public  void loginUser(View view ){

        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();

        Login login = new Login("password" ,  "*", "SJGZDWXOPLJZLBDQGACCAGAVWSHORHJK", "6734914665b5258c7a2eb01077382585" , email , password );
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
                    System.out.println("dateBEFORE" + calendar.getTime().toString());
                    calendar.add(Calendar.SECOND, Integer.parseInt(response.body().getExpires_in()));

                    token ="Bearer "+ response.body().getAccess_token();

                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE).edit();
                    editor.putString("token",token);
                    editor.putString("refresh", response.body().getRefresh_token());
                    editor.putString("Expires_in",calendar.getTime().toString()) ;
                    editor.apply();


                     navigatetoHomeActivity(token);
                }
                else {


                    Toast.makeText(MainActivity.this, "Login is not correct", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login or password incorrect ", Toast.LENGTH_SHORT).show();

            }
        });




    }


    public void navigatetoHomeActivity(String token){

        Intent homeIntent = new Intent(getApplicationContext(),NavActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("token", token);
        homeIntent.putExtras(mBundle);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

}
