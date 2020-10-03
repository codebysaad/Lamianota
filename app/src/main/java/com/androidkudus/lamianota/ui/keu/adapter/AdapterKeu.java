package com.androidkudus.lamianota.ui.keu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.ui.keu.insert.InsertUpdateKeuActivity;
import com.androidkudus.lamianota.ui.keu.insert.InsertUpdateKeuViewModel;
import com.androidkudus.lamianota.utils.DateFormatter;
import com.androidkudus.lamianota.utils.KeuDiffCallback;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AdapterKeu extends RecyclerView.Adapter<AdapterKeu.ViewHolder> {
    private Activity activity;
    private ArrayList<Keu> list = new ArrayList<>();

    public AdapterKeu(Activity activity) {
        this.activity = activity;
    }

    public void setListNoteKeu(List<Keu> list){
        final KeuDiffCallback diffCallback = new KeuDiffCallback(this.list, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.list.clear();
        this.list.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public AdapterKeu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_keu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKeu.ViewHolder holder, int position) {
        holder.tvDate.setText(DateFormatter.getFormatDate(activity, list.get(position).getDateIn()));
        holder.tvSource.setText(list.get(position).getFrom());
        holder.tvAmountOfMoney.setText(list.get(position).getMoney());
        if (list.get(position).isStatusIn()) {
            holder.tvStateMoney.setText(activity.getResources().getString(R.string.income));
            holder.imgInOut.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_income));
            holder.titleState.setText(activity.getResources().getString(R.string.source));
        }else {
            holder.tvStateMoney.setText(activity.getResources().getString(R.string.spending));
            holder.imgInOut.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_spending));
            holder.titleState.setText(activity.getResources().getString(R.string.needs));
        }
        holder.btnEdit.setOnClickListener((v) -> {
            Intent in = new Intent(activity, InsertUpdateKeuActivity.class);
            in.putExtra(InsertUpdateKeuActivity.EXTRA_POSITION, holder.getAdapterPosition());
            in.putExtra(InsertUpdateKeuActivity.EXTRA_NOTE, list.get(holder.getAdapterPosition()));
            activity.startActivityForResult(in, InsertUpdateKeuActivity.REQUEST_UPDATE);
        });
        holder.btnDelete.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.delete_task));
            builder.setMessage(R.string.message_delete);
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            builder.setPositiveButton(R.string.yes, (dialog, id) -> delete(position));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate, tvAmountOfMoney, tvSource, tvStateMoney, titleState;
        private final AppCompatImageView imgInOut;
        private final MaterialButton btnEdit, btnDelete;
        ViewHolder(@NonNull View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_keu_date);
            tvAmountOfMoney = v.findViewById(R.id.tv_amount_of_money);
            tvSource = v.findViewById(R.id.tv_money_source);
            tvStateMoney = v.findViewById(R.id.tv_state_money);
            imgInOut = v.findViewById(R.id.img_state_money);
            btnEdit = v.findViewById(R.id.btn_edit_keu);
            btnDelete = v.findViewById(R.id.btn_delete_keu);
            titleState = v.findViewById(R.id.tv_title_money_from);
        }
    }
    @NonNull
    private static InsertUpdateKeuViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(InsertUpdateKeuViewModel.class);
    }

    private void delete(int position) {
        InsertUpdateKeuViewModel viewModel = obtainViewModel((AppCompatActivity) activity);
        viewModel.delete(list.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        list.remove(position);
    }
}
