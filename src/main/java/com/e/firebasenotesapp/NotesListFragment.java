package com.e.firebasenotesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesListFragment extends Fragment {
    private FloatingActionButton addNoteBtn;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private ProjectAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddFragment();
            }
        });

        setUpRecyclerView();
        handleSwipeDelete();

        adapter.setCallback(new HandleUpdateNote() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                gotoEditFragment(documentSnapshot, position);
            }
        });
    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    private void initView(View view) {
        addNoteBtn = view.findViewById(R.id.fab_add_new);
        recyclerView = view.findViewById(R.id.rv_notes);
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("title");

        FirestoreRecyclerOptions<NoteModel> options = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query, NoteModel.class)
                .build();

        adapter = new ProjectAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void handleSwipeDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void goToAddFragment() {
        Fragment fragment = new NoteFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void gotoEditFragment(DocumentSnapshot documentSnapshot, int position) {
        NoteModel note = documentSnapshot.toObject(NoteModel.class);
        String id = documentSnapshot.getId();

        Fragment fragment = NoteFragment.newInstance(note, id);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
