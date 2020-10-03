package com.androidkudus.lamianota.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.androidkudus.lamianota.database.keu.Keu;

import java.util.List;

public class KeuDiffCallback extends DiffUtil.Callback {
    private final List<Keu> mOldKeuList;
    private final List<Keu> mNewKeuList;

    public KeuDiffCallback(List<Keu> mOldKeuList, List<Keu> mNewKeuList){
        this.mOldKeuList = mOldKeuList;
        this.mNewKeuList = mNewKeuList;
    }
    @Override
    public int getOldListSize() {
        return mOldKeuList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewKeuList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldKeuList.get(oldItemPosition).getId() == mNewKeuList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Keu oldKeu = mOldKeuList.get(oldItemPosition);
        final Keu newKeu = mNewKeuList.get(newItemPosition);
        return oldKeu.getFrom().equals(newKeu.getFrom()) && oldKeu.getMoney().equals(newKeu.getMoney());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
