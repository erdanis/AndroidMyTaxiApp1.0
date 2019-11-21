package com.example.mytaxiapp10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeStatusDialogFragment extends DialogFragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private String cabnum;

    public ChangeStatusDialogFragment setMessage(String cab) {
        cabnum = cab;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        myRef = database.getReference().child("drivers").child(cabnum+"");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_status_title)
                .setItems(R.array.change_status, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        myRef.child("status").setValue(which);
                        Log.d("TESTINGSTATUS",Integer.toString(which));
                    }
                });
        return builder.create();
    }

}

