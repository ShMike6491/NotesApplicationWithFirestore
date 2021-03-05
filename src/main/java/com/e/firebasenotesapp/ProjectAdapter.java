package com.e.firebasenotesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProjectAdapter extends FirestoreRecyclerAdapter<NoteModel, ProjectAdapter.NoteHolder> {
    private HandleUpdateNote callback;

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

    public void setCallback(HandleUpdateNote callback) {
        this.callback = callback;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
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
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && callback != null) {
                        DocumentSnapshot ds = getSnapshots()
                                .getSnapshot(position);
                        callback.onItemClick(ds, position);
                    }
                }
            });
        }

        public void onBind(int position, NoteModel model) {
            title.setText(model.getTitle());
            description.setText(model.getDescription());
        }
    }
}
