package com.e.firebasenotesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteFragment extends Fragment {

    private TextInputEditText title;
    private TextInputEditText description;
    private NoteModel note;
    private String noteID;

    private CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection("Notebook");

    public static NoteFragment newInstance(NoteModel model, String noteID) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.SAVE_FRAGMENT_INSTANCE, model);
        args.putString(Constants.SAVE_ID_INSTANCE, noteID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            updateViews();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(View view) {
        title = view.findViewById(R.id.et_note_details_title);
        description = view.findViewById(R.id.et_note_details_description);
    }

    private void updateViews() {
        note = (NoteModel) getArguments()
                .getSerializable(Constants.SAVE_FRAGMENT_INSTANCE);
        noteID = getArguments().getString(Constants.SAVE_ID_INSTANCE);

        title.setText(note.getTitle());
        description.setText(note.getDescription());
    }

    private void saveNote() {
        String name = title.getText().toString();
        String desc = description.getText().toString();

        if(isEmpty(name,desc)) {
            showToast("Title and Descriptions fields should not be empty");
            return;
        } else if (notExistingNote()) {
            saveNewNote(name, desc);
        } else {
            editNote(name, desc);
        }

        goBack();
    }

    private void saveNewNote(String name, String desc) {
        notebookRef.add(new NoteModel(name, desc)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                showToast("Note added successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Something went wrong");
            }
        });
    }

    private void editNote(String name, String desc) {
        NoteModel note = new NoteModel(name, desc);

        notebookRef.document(noteID).set(note);
    }

    private void goBack() {
        Fragment fragment = new NotesListFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();
    }

    private boolean notExistingNote() {
        return note == null;
    }

    private boolean isEmpty(String name, String desc) {
        return name.trim().isEmpty() || desc.trim().isEmpty();
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
