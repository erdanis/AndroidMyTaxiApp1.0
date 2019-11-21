package com.example.mytaxiapp10;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


public class NewJobActivity extends FragmentActivity implements NewJobDialogFragment.NoticeDialogListener, ChangeJobStatusDialogFragment.NoticeDialogListener2 {

    public String[] tstatus = {"Asignada","Aceptada","Positivo","Completada","Negativa","Rechazada"};

    public void showdialognewjob() {
        DialogFragment newFragment = new NewJobDialogFragment().setMessage(address,genero,cab);
        newFragment.show(getSupportFragmentManager(), "newjob");
    }

    String address;
    String fulladdress;
    String genero;
    String comment;
    String numtelf;
    String cab;
    public int status;
    int postcont=0;


    private TextView mAddressTextView;
    private TextView mFullAddressTextView;
    private TextView mlast4numbertextview;
    private TextView mgenerotextview;
    private TextView mcommenttextview;
    private TextView mstatustextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        address = getIntent().getStringExtra("msgaddress");
        fulladdress = getIntent().getStringExtra("msgfulladdress");
        genero = getIntent().getStringExtra("msggen");
        comment = getIntent().getStringExtra("msgcomment");
        numtelf = getIntent().getStringExtra("msgnumtelf");
        cab = getIntent().getStringExtra("msgcab");
        status = getIntent().getIntExtra("msgstatus",1);

        mAddressTextView = findViewById(R.id.textView);
        mFullAddressTextView = findViewById(R.id.textView2);
        mlast4numbertextview = findViewById(R.id.textView3);
        mgenerotextview = findViewById(R.id.textView4);
        mcommenttextview = findViewById(R.id.textView5);
        mstatustextview = findViewById(R.id.textView7);

        mAddressTextView.setText(getString(R.string.addressname,address));
        mFullAddressTextView.setText(getString(R.string.fulladress,fulladdress));
        mlast4numbertextview.setText(getString(R.string.last4number,numtelf));
        mgenerotextview.setText(getString(R.string.genero,genero));
        mcommenttextview.setText(getString(R.string.comment,comment));
        mstatustextview.setText(getString(R.string.jobstatus,tstatus[status]));
       // getString(R.string.dialogmessage,address);
        if (status==0) showdialognewjob();

    }

    public void changejobstatus(View v) {
        dialogchangejobstatus();
    }


    public void dialogchangejobstatus() {
        DialogFragment newFragment = new ChangeJobStatusDialogFragment().setMessage(numtelf,cab,status);
        newFragment.show(getSupportFragmentManager(), "change_job_status");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        status =1;
        mstatustextview.setText(getString(R.string.jobstatus,tstatus[status]));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        status =5;
        mstatustextview.setText(getString(R.string.jobstatus,tstatus[status]));
    }

    @Override
    public void onDialogPositive2Click(DialogFragment dialog) {
        if (status!=5) {
            postcont++;
            if (postcont == 1) {
                status = 2;
                mstatustextview.setText(getString(R.string.jobstatus, tstatus[status]));
            } else if (postcont == 2) {
                status = 3;
                mstatustextview.setText(getString(R.string.jobstatus, tstatus[status]));
            }
        }

    }

    @Override
    public void onDialogNegative2Click(DialogFragment dialog) {
        //
    }
}
