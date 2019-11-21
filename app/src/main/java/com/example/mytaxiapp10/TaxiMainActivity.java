package com.example.mytaxiapp10;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class TaxiMainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String capnumber;

    public String[] tstatus = {"Desconectado","Activo","Ocupado","Receso","Problemas","Aceptado","Positivo","Asignado"};

    private static final String TAG = "Debugger";

        @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.taxiactivity_main);

            TextView bienvenido = findViewById(R.id.textViewLabel1);
            final TextView posiciontext = findViewById(R.id.positioncapid);
            final TextView statustext = findViewById(R.id.textstatusid);
            final TextView latitudtext = findViewById(R.id.latitudtextid);
            final TextView longitudtext = findViewById(R.id.longitudtextid);
            final Button changestatusbtn = findViewById(R.id.changestatusbt);

            String user = getIntent().getStringExtra("user");
            //String userid = getIntent().getStringExtra("userid");

            capnumber = user.substring(6, user.indexOf("@"));
            bienvenido.setText(getString(R.string.bienvenida_id, capnumber));

            myRef = database.getReference().child("drivers").child(capnumber);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                drivers value = dataSnapshot.getValue(drivers.class);
                if (dataSnapshot.exists()) {

                    posiciontext.setText(getString(R.string.positioncap,value.ranking+""));
                    statustext.setText(getString(R.string.textstatusid,tstatus[value.status]));
                    latitudtext.setText(getString(R.string.textlatidud,Double.toString(value.latitud)));
                    longitudtext.setText(getString(R.string.textlongitud,Double.toString(value.longitud)));

                }
                else
                {
                    Log.d(TAG, "NO USER DATA " );
                }
                }


            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

     }

    public void onChangeButtonPress(View v){

        changestatus();

    }
    public void changestatus() {
        DialogFragment newFragment = new ChangeStatusDialogFragment().setMessage(capnumber);
        newFragment.show(getSupportFragmentManager(), "change_status");
    }

}

