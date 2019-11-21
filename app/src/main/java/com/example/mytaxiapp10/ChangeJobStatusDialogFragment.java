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

public class ChangeJobStatusDialogFragment extends DialogFragment {

    DatabaseReference refjobs;
    DatabaseReference refdriver;
    private String numclient;
    private String cabnum;
    private int statusnum;

    public interface NoticeDialogListener2 {
        public void onDialogPositive2Click(DialogFragment dialog);
        public void onDialogNegative2Click(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener2 listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener2) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(NewJobActivity.class.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    public ChangeJobStatusDialogFragment setMessage(String numtelf, String cab, int status) {
        numclient = numtelf;
        cabnum = cab;
        statusnum = status;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        refjobs = FirebaseDatabase.getInstance().getReference().child("recentjobs").child(cabnum+"");
        refdriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(cabnum+"");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (statusnum==1) {
            builder.setMessage(getString(R.string.updatejobstatus_1, numclient))
                    .setPositiveButton("CORRECTO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            refjobs.child("status").setValue(2);
                            refdriver.child("status").setValue(6);
                            listener.onDialogPositive2Click(ChangeJobStatusDialogFragment.this);

                        }
                    })
                    .setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            listener.onDialogNegative2Click(ChangeJobStatusDialogFragment.this);


                        }
                    });
        }
        else if (statusnum==2) {

            builder.setMessage("¿Carrera Finalizada?")
                    .setPositiveButton("CORRECTO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            refjobs.child("status").setValue(3);
                            refdriver.child("status").setValue(1);
                            listener.onDialogPositive2Click(ChangeJobStatusDialogFragment.this);

                        }
                    })
                    .setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            listener.onDialogNegative2Click(ChangeJobStatusDialogFragment.this);


                        }
                    });

        }
            // Create the AlertDialog object and return it
            return builder.create();

    }


}
