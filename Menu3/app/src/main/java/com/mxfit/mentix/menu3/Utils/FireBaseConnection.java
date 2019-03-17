package com.mxfit.mentix.menu3.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseConnection {
    public static FirebaseAuth auth;
    public static DatabaseReference dbAccount;
    public static DatabaseReference dbTreatmentDetails;
    public static DatabaseReference dbUnits;
    public static FirebaseUser user;
    public static FirebaseFirestore db;

    public static void instatiate(){
        if(auth==null)
            auth = FirebaseAuth.getInstance();
        if(user==null)
            user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void refreshUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}
