package com.e.firebasenotesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteFragment extends BottomSheetDialogFragment {

    private TextInputEditText title;
    private TextInputEditText description;
    private Button button;
    private NoteModel note;
    private String noteID;

    private CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection("Notebook");

    public static BottomSheetDialogFragment newInstance(NoteModel model, String noteID) {
        BottomSheetDialogFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.SAVE_FRAGMENT_INSTANCE, model);
        args.putString(Constants.SAVE_ID_INSTANCE, noteID);
        fragment.setArguments(args);
        return fragment;
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

    private void initView(View view) {
        title = view.findViewById(R.id.et_note_details_title);
        description = view.findViewById(R.id.et_note_details_description);
        button = view.findViewById(R.id.mb_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
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
            Toast.makeText(getActivity(), "Title and Descriptions fields should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (notExistingNote()) {
            saveNewNote(name, desc);
        } else {
            editNote(name, desc);
        }

        goBack();
    }

    private void saveNewNote(String name, String desc) {
        notebookRef.add(new NoteModel(name, desc));
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
}
