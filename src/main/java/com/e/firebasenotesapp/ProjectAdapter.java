package com.e.firebasenotesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;

public class ProjectAdapter extends FirestoreRecyclerAdapter<NoteModel, ProjectAdapter.NoteHolder> {

    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<NoteModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull NoteModel model) {
        holder.onBind(position, model);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteHolder(view);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private MaterialTextView title;
        private MaterialTextView description;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO finish this later
                }
            });
        }

        public void onBind(int position, NoteModel model) {
            title.setText(model.getTitle());
            description.setText(model.getDescription());
        }
    }
}
