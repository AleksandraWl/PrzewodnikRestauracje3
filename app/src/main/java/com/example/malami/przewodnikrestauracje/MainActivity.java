package com.example.malami.przewodnikrestauracje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener muAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        muAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Auth", user.getUid());
                }
            }
        };


    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(muAuthListener);
    }

    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(muAuthListener);
    }




    public void login(View view) {
        Intent intent;
        intent= new Intent(this, Logowanie.class);
        startActivity(intent);
    }

    public void rejestruj(View view) {
        Intent intent;
        intent= new Intent(this, Rejestracja.class);
        startActivity(intent);
    }
}