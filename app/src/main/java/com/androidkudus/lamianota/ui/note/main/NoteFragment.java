package com.androidkudus.lamianota.ui.note.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.ui.note.adapter.AdapterNote;
import com.androidkudus.lamianota.ui.note.insert.InsertUpdateNoteActivity;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class NoteFragment extends Fragment {

    private NoteViewModel mViewModel;
    private RecyclerView rvNote;
    private AdapterNote adapterNote;
    private FloatingActionButton fabAdd;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = obtainViewModel(requireActivity());
        mViewModel.getAllData().observe(getViewLifecycleOwner(), noteObserver);

        adapterNote = new AdapterNote(getActivity());
        rvNote = view.findViewById(R.id.rv_note);
        fabAdd = view.findViewById(R.id.add_note);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvNote.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNote.setAdapter(adapterNote);
        fabAdd.setOnClickListener((v) -> {
            if (v.getId() == R.id.add_note){
                Intent intent = new Intent(getActivity(), InsertUpdateNoteActivity.class);
                requireActivity().startActivityForResult(intent, InsertUpdateNoteActivity.REQUEST_ADD);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == InsertUpdateNoteActivity.REQUEST_ADD){
                if (resultCode == InsertUpdateNoteActivity.RESULT_ADD){
                    showMessage(requireActivity().getResources().getString(R.string.message_add));
                }
            }else if (requestCode == InsertUpdateNoteActivity.REQUEST_UPDATE){
                if (resultCode == InsertUpdateNoteActivity.RESULT_UPDATE){
                    showMessage(requireActivity().getResources().getString(R.string.message_update));
                }else if (resultCode == InsertUpdateNoteActivity.RESULT_DELETE){
                    showMessage(requireActivity().getResources().getString(R.string.message_delete));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @NonNull
    private static NoteViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(NoteViewModel.class);
    }

    private final Observer<List<Note>> noteObserver = new Observer<List<Note>>() {
        @Override
        public void onChanged(@Nullable List<Note> noteList) {
            if (noteList != null) {
                adapterNote.setListNote(noteList);
                adapterNote.notifyDataSetChanged();
            }
        }
    };

    private void showMessage(String message){
        Snackbar.make(rvNote, message, Snackbar.LENGTH_SHORT).show();
    }
}
