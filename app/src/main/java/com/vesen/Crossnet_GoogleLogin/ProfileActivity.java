package com.vesen.Crossnet_GoogleLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.vesen.Crossnet_GoogleLogin.models.DataModal;
import com.vesen.Crossnet_GoogleLogin.services.RetrofitAPI;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity  {
    Button logoutBtn;
    TextView userName,userEmail;
    private ImageView profileImage;
    private ProgressBar loadingPB;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;

    //the path to api files
    String _baseURL = "https://crossnet.com/api/google_signin";
    DataModal currentUser = new DataModal("", "");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn=(Button)findViewById(R.id.logoutBtn);
        userName=(TextView)findViewById(R.id.name);
        userEmail=(TextView)findViewById(R.id.email);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        loadingPB = findViewById(R.id.idLoadingPB);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();





        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            handleSignInResult(acct);


    }
    private void handleSignInResult(GoogleSignInAccount account){
        if(account != null){

            // calling a method to post the data and passing our name and email for authentication.
            //or send user ID token to backend for verification depending on authentication logic at the backend
            postData(account.getDisplayName(), account.getEmail());


            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }


        }else{
            gotoMainActivity();
        }
    }
    private void gotoMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoMainActivity();
                            }
                        });
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                            }
                        });

                        // ...
                    }
                });


    }


    private void postData(String name, String email) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(_baseURL)
                // as we are sending data in json format
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing user's data from google account.
        DataModal modal = new DataModal(name, email);

        // calling a method to create a post and passing our modal class.
        Call<DataModal> call = retrofitAPI.createPost(modal);


        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                loadingPB.setVisibility(View.GONE);


                // we are getting response from our body
                // and passing it to our modal class as current user
                 currentUser = response.body();

            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {

                // we get error response from API.
                Toast.makeText(getApplicationContext(),"Error! Could not authenticate user",Toast.LENGTH_LONG).show();

                signOut();

            }
        });
    }

}