package com.e.firebasenotesapp;

import com.google.firebase.firestore.DocumentSnapshot;

@FunctionalInterface
public interface HandleUpdateNote {
    void onItemClick(DocumentSnapshot documentSnapshot, int position);
}
