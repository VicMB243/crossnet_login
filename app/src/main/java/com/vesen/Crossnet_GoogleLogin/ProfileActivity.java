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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ProfileActivity extends AppCompatActivity  {
    Button logoutBtn;
    TextView userName,userEmail;
    private ImageView profileImage;
    //private GoogleApiClient googleApiClient;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn=(Button)findViewById(R.id.logoutBtn);
        userName=(TextView)findViewById(R.id.name);
        userEmail=(TextView)findViewById(R.id.email);
        profileImage = (ImageView) findViewById(R.id.profileImage);

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

            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }
            
            
            
            //send user ID token to backend for verification
            //replace yourbackend.example.com/tokensignin with the right path to backend services
            
//             String idToken = account.getIdToken();
//             HttpClient httpClient = new DefaultHttpClient();
//             HttpPost httpPost = new HttpPost("https://yourbackend.example.com/tokensignin");

//             try {
//               List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//               nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
//               httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//               HttpResponse response = httpClient.execute(httpPost);
//               int statusCode = response.getStatusLine().getStatusCode();
//               final String responseBody = EntityUtils.toString(response.getEntity());
              
                
//             } catch (ClientProtocolException e) {
              
//             } catch (IOException e) {
              
//             }

//         }else{
//             gotoMainActivity();
//         }
    }
    private void gotoMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
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

}
