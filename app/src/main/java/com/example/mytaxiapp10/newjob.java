package com.example.mytaxiapp10;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class newjob {

    public String numTelf;
    public String addressname;
    public String fulladdress;
    public String comentario;
    public String genero;
    public String cab;
    public int status;

      public newjob() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public newjob(String numTelf, String addressname, String fulladdress, String comentario, String genero, String cab, int status) {
        this.numTelf = numTelf;
        this.addressname = addressname;
        this.fulladdress = fulladdress;
        this.comentario = comentario;
        this.genero = genero;
        this.cab = cab;
        this.status = status;
    }

}
