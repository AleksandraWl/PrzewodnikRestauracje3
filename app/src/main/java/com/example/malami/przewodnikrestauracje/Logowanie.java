package com.example.malami.przewodnikrestauracje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class Logowanie extends AppCompatActivity {

    EditText email;
    EditText haslo;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_logowanie);
        email=(findViewById(R.id.email));
        haslo = (findViewById(R.id.haslo));
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }


    public void logowanie(View view) {
        final String semail = email.getText().toString().trim();
        String shaslo = haslo.getText().toString().trim();

        if (TextUtils.isEmpty((semail)) && TextUtils.isEmpty(shaslo)) {
            Toast.makeText(this, "Podaj e-mail i hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(semail)) {
            //email pusty
            Toast.makeText(this, "Podaj e-mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(shaslo)) {
            //haslo puste
            Toast.makeText(this, "Podaj hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logowanie, proszę czekać.. ");
        progressDialog.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        firebaseAuth.signInWithEmailAndPassword(semail, shaslo)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (user.isEmailVerified() == true) {
                            if (task.isSuccessful()) {
                                // CzyAdmin(semail);
                                Toast.makeText(Logowanie.this, "Logowanie udało się", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Logowanie.this, WyborJedzenia.class);
                                startActivity(i);
                                email.setText("");
                                haslo.setText("");
                            } else {
                                Toast.makeText(Logowanie.this, "Logowanie nie udało się", Toast.LENGTH_LONG).show();
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(Logowanie.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Logowanie.this, "E-mail weryfikacyjny nie został potwierdzony.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    public void BrakHasla(View view) {
        String semail = email.getText().toString().trim();
        String shaslo = haslo.getText().toString().trim();
        if(TextUtils.isEmpty(semail))
        {
            Toast.makeText(Logowanie.this, "Podaj e-mail", Toast.LENGTH_LONG).show();
        }
        else
        {
            firebaseAuth.sendPasswordResetEmail(semail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Logowanie.this, "Wysłano e-mail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

   /* public void CzyAdmin(final String emailAdmin)
    {
        databaseReference= FirebaseDatabase.getInstance().getReference("Administratorzy");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    admin Admin = s.getValue(admin.class);
                    if (Admin.getEmail().equals(emailAdmin))
                    {
                        Intent i = new Intent(Logowanie.this, WyborJedzeniaAdmin.class);
                        startActivity(i);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

}