package com.example.malami.przewodnikrestauracje;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;



public class WyborJedzenia extends AppCompatActivity {


    Spinner spinner;
    DatabaseReference db;
    final ArrayList<String> lista = new ArrayList<>();

    EditText NowyAdministrator;
    FirebaseAuth firebaseAuth;
    String genere;
    String dlugosc, szerokosc, Nazwa, Adres;
    private static final int RECORD_REQUEST_CODE = 101;
    private SensorManager manager;
    private static final String TAG = "";
    private long backPressedTime = 0;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_jedzenia);


        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance().getReference("Restauracje");


        spinner = (findViewById(R.id.spinner));
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fetchData()));
        genere = spinner.getSelectedItem().toString();


        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(this,//pozwolenie
                Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 !=
                PackageManager.PERMISSION_GRANTED || permissionCheck3 !=
                PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }
    }

   protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                RECORD_REQUEST_CODE);
    }

    private ArrayList<String> fetchData(){
        lista.clear();
        lista.add("Restauracje");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    restauracje res = ds.getValue(restauracje.class);
                    lista.add(res.getNazwa());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return lista;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.wyloguj:
                wylogowanie();

        }

        return super.onOptionsItemSelected(item);
    }


   public void zatwierdz(View view) {
        genere = spinner.getSelectedItem().toString();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> lubna = (Map<String, Object>) dataSnapshot.getValue();

                for (String childKey : lubna.keySet()) {
                    if (genere.equals("Restauracje")) {
                        Toast.makeText(WyborJedzenia.this, "Wybierz restauracje", Toast.LENGTH_LONG).show();
                    } else if (childKey.equals(genere)) {

                        Map<String, Object> currentLubnaObject = (Map<String, Object>) lubna.get(childKey);

                        String adresRes = (String) currentLubnaObject.get("adres");
                        String Telefon = (String) currentLubnaObject.get("telefon");
                        String email= (String)currentLubnaObject.get("email");
                        String godziny= (String)currentLubnaObject.get("otwarcie");
                        kontakt(genere, adresRes, Telefon,godziny,email);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    private void kontakt(String nazwa,String adres, String telefon, String godziny, String email) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(nazwa);
        final String[] options = {"Godziny otwarcia: " + godziny, "Adres: "+ adres, "Telefon: "+telefon, "E-mail: "+email
        };
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        dialogBuilder.create();
        dialogBuilder.show();
    }


    public void mapa(View view) {
        genere = spinner.getSelectedItem().toString();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> lubna = (Map<String, Object>) dataSnapshot.getValue();


                for (String childKey : lubna.keySet()) {
                    if (genere.equals("Restauracje")) {
                        Toast.makeText(WyborJedzenia.this, "Wybierz restauracje", Toast.LENGTH_LONG).show();
                    } else if (childKey.equals(genere)) {
                        //childKey is your "-LQka.. and so on"
                        //Your current object holds all the variables in your picture.
                        Map<String, Object> currentLubnaObject = (Map<String, Object>) lubna.get(childKey);

                        szerokosc = (String) currentLubnaObject.get("szerokosc");
                        dlugosc = (String) currentLubnaObject.get("dlugosc");
                        Nazwa = (String) currentLubnaObject.get("nazwa");
                        Adres = (String) currentLubnaObject.get("adres");
                    }
                    //You can access each variable like so: String variableName = (String) currentLubnaObject.get("INSERT_VARIABLE_HERE"); //data, description, taskid, time, title
                }

                if (!genere.equals("Restauracje")) {
                    Intent i = new Intent(WyborJedzenia.this, MapsActivity.class);
                    i.putExtra("szerokosc", szerokosc);
                    i.putExtra("dlugosc", dlugosc);
                    i.putExtra("adres", Adres);
                    i.putExtra("nazwa", Nazwa);
                    startActivity(i);
                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void menu(View view) {
        genere = spinner.getSelectedItem().toString();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> lubna = (Map<String, Object>) dataSnapshot.getValue();

                for (String childKey : lubna.keySet()) {
                    if (genere.equals("Restauracje")) {
                        Toast.makeText(WyborJedzenia.this, "Wybierz restauracje", Toast.LENGTH_LONG).show();
                    } else if (childKey.equals(genere))
                    {
                        Map<String, Object> currentLubnaObject = (Map<String, Object>) lubna.get(childKey);

                        szerokosc = (String) currentLubnaObject.get("szerokosc");
                        dlugosc = (String) currentLubnaObject.get("dlugosc");
                        Nazwa = (String) currentLubnaObject.get("nazwa");
                        Adres = (String) currentLubnaObject.get("adres");
                    }
                    //You can access each variable like so: String variableName = (String) currentLubnaObject.get("INSERT_VARIABLE_HERE"); //data, description, taskid, time, title
                }

                if(!genere.equals("Restauracje")) {
                    Intent i = new Intent(WyborJedzenia.this, MenuRestauracji.class);
                    i.putExtra("nazwa", Nazwa);
                    startActivity(i);
                }
            }


            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void wylogowanie() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Wychodzę");
                finish();
            }
        });
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Anulowano");

            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }


    @Override
    public void onBackPressed() {

        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }
}
