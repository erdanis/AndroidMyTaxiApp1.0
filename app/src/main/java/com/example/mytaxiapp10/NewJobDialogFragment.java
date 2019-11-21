package com.example.mytaxiapp10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewJobDialogFragment extends DialogFragment {

    private String message = "message";
    private String genero;
    private String cabnum;

    DatabaseReference refjobs;
    DatabaseReference refdriver;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(NewJobActivity.class.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public NewJobDialogFragment setMessage(String newjobmessage, String clientgen, String cab) {
        message = newjobmessage;
        genero = clientgen;
        cabnum = cab;
        return this;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        refjobs = FirebaseDatabase.getInstance().getReference().child("recentjobs").child(cabnum+"");
        refdriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(cabnum+"");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("CARRERA: "+message+"\nCliente: "+genero)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.d("TESTINGDIALOG","CARRERA ACEPTADA");

                        refjobs.child("status").setValue(1);
                        refdriver.child("status").setValue(5);
                        listener.onDialogPositiveClick(NewJobDialogFragment.this);

                    }
                })
                .setNegativeButton("RECHAZAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        refdriver.child("status").setValue(2);
                        refjobs.child("status").setValue(5);
                        listener.onDialogNegativeClick(NewJobDialogFragment.this);
                        //Log.d("TESTINGDIALOG","CARRERA RECHAZADA");
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
