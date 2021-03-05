package com.e.firebasenotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment fragment = new NotesListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_container, fragment)
                    .commit();
        }
    }
}
