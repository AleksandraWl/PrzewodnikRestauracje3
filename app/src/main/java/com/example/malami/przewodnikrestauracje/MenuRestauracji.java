package com.example.malami.przewodnikrestauracje;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;

public class MenuRestauracji extends AppCompatActivity {

    String nazwa;
    private DatabaseReference mDatabase;
    DatabaseReference lubnaRef;
    DatabaseReference db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();
    private ListView listView;
    menuR menu;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restauracji);

        menu= new menuR();
        listView= (ListView)findViewById(R.id.listView);
        nazwa = getIntent().getStringExtra("nazwa");

        ActionBar actionBar = getSupportActionBar();

        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        toolbar.setTitle(nazwa);
        toolbar.setSubtitle("Menu restauracji");
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        lubnaRef = mDatabase.child("Menu").child(nazwa);
        db = FirebaseDatabase.getInstance().getReference("Menu").child(nazwa);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.menu, R.id.textView, list);
        lubnaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    menu = ds.getValue(menuR.class);
                    list.add(menu.getNazwa());
                    nazwa = menu.getNazwa();
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1){
                    final String nazwa = adapterView.getItemAtPosition(i).toString().trim();
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> lubna = (Map<String, Object>) dataSnapshot.getValue();

                            for (String childKey : lubna.keySet()) {
                                if (childKey.equals(nazwa)) {
                                    Map<String, Object> currentLubnaObject = (Map<String, Object>) lubna.get(childKey);

                                    String opis = (String) currentLubnaObject.get("opis");
                                    String cena = (String) currentLubnaObject.get("cena");


                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MenuRestauracji.this);
                                    dialogBuilder.setTitle(nazwa);
                                    final String[] options = {"Cena: " + cena + "\n\nOpis: \n" + opis};
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
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
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


    private void wylogowanie() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent(MenuRestauracji.this, Logowanie.class);
                startActivity(i);
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

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }
}