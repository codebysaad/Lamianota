package com.androidkudus.lamianota.ui.note.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.database.note.NoteRoomDatabase;
import com.androidkudus.lamianota.repository.NoteRepository;
import com.androidkudus.lamianota.ui.note.insert.InsertUpdateNoteActivity;
import com.androidkudus.lamianota.ui.note.insert.InsertUpdateNoteViewModel;
import com.androidkudus.lamianota.utils.DateFormatter;
import com.androidkudus.lamianota.utils.NoteDiffCallback;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder> {
    private final ArrayList<Note> list = new ArrayList<>();
    private final Activity activity;

    public AdapterNote(Activity activity){
        this.activity = activity;
    }

    public void setListNote(List<Note> listNote){
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.list, listNote);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.list.clear();
        this.list.addAll(listNote);
        diffResult.dispatchUpdatesTo(this);
    }
    @NonNull
    @Override
    public AdapterNote.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNote.ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDescription.setText(list.get(position).getDescription());
        holder.tvDate.setText(DateFormatter.getFormatDate(activity, list.get(position).getDate()));
        holder.tvTime.setText(list.get(position).getTime());
        holder.btnEdit.setOnClickListener((v) -> {
            Intent in = new Intent(activity, InsertUpdateNoteActivity.class);
            in.putExtra(InsertUpdateNoteActivity.EXTRA_POSITION, holder.getAdapterPosition());
            in.putExtra(InsertUpdateNoteActivity.EXTRA_NOTE, list.get(holder.getAdapterPosition()));
            activity.startActivityForResult(in, InsertUpdateNoteActivity.REQUEST_UPDATE);
        });
        holder.btnDelete.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Delete this task?");
            builder.setMessage(R.string.message_delete);
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            builder.setPositiveButton(R.string.yes, (dialog, id) -> delete(position));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        if (list.get(position).isReminder()){
            holder.btnState.setIcon(activity.getResources().getDrawable(R.drawable.ic_reminder_on));
        }else {
            holder.btnState.setIcon(activity.getResources().getDrawable(R.drawable.ic_reminder_off));
        }
        holder.btnShare.setOnClickListener(v -> {
            String sendText = "TASK TITLE: " + list.get(position).getTitle() + "\n" +
                    "DESCRIPTION TASK: " + list.get(position).getDescription() + "\n" +
                    "TASK WILL APPLY AT: " + DateFormatter.getFormatDate(activity, list.get(position).getDate()) + "," +
                    list.get(position).getTime();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            activity.startActivity(shareIntent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvDate, tvTime;//, tvReminder;
        final MaterialCardView cardView;
        final MaterialButton btnEdit, btnDelete, btnState, btnShare;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_note_date);
            tvDescription = itemView.findViewById(R.id.tv_note_description);
            tvTime = itemView.findViewById(R.id.tv_note_time);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            cardView = itemView.findViewById(R.id.cv_item_note);
            btnDelete = itemView.findViewById(R.id.btn_delete_note);
            btnEdit = itemView.findViewById(R.id.btn_edit_note);
            btnState = itemView.findViewById(R.id.btn_state);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }

    @NonNull
    private static InsertUpdateNoteViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(InsertUpdateNoteViewModel.class);
    }

    private void delete(int position) {
        InsertUpdateNoteViewModel viewModel = obtainViewModel((AppCompatActivity) activity);
        viewModel.delete(list.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        list.remove(position);
    }
}
