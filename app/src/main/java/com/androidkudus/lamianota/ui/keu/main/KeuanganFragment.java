package com.androidkudus.lamianota.ui.keu.main;

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
import android.widget.TextView;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.ui.keu.adapter.AdapterKeu;
import com.androidkudus.lamianota.ui.keu.insert.InsertUpdateKeuActivity;
import com.androidkudus.lamianota.utils.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.math.MathUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KeuanganFragment extends Fragment {

    private RecyclerView rvKeu;
    private AdapterKeu adapter;
    private FloatingActionButton fabAdd;
    private int moneyStatusFalse;
    private int moneyStatusTrue;
    private TextView summarySpending, summaryIncome, tvSummaryMoney;

    public static KeuanganFragment newInstance() {
        return new KeuanganFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keuangan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeuanganViewModel mViewModel = obtainViewModel(requireActivity());
        mViewModel.getAllData().observe(getViewLifecycleOwner(), keuObserver);
        adapter = new AdapterKeu(getActivity());
        rvKeu = view.findViewById(R.id.rv_keu);
        fabAdd = view.findViewById(R.id.add_keu);
        summarySpending = view.findViewById(R.id.summary_all_spending);
        summaryIncome = view.findViewById(R.id.summary_all_income);
        tvSummaryMoney = view.findViewById(R.id.tv_summary_all_money);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvKeu.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvKeu.setAdapter(adapter);
        fabAdd.setOnClickListener((v) -> {
            if (v.getId() == R.id.add_keu){
                Intent intent = new Intent(getActivity(), InsertUpdateKeuActivity.class);
                requireActivity().startActivityForResult(intent, InsertUpdateKeuActivity.REQUEST_ADD);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == InsertUpdateKeuActivity.REQUEST_ADD){
                if (resultCode == InsertUpdateKeuActivity.RESULT_ADD){
                    showMessage(requireActivity().getResources().getString(R.string.message_add));
                }
            }else if (requestCode == InsertUpdateKeuActivity.REQUEST_UPDATE){
                if (resultCode == InsertUpdateKeuActivity.RESULT_UPDATE){
                    showMessage(requireActivity().getResources().getString(R.string.message_update));
                }else if (resultCode == InsertUpdateKeuActivity.RESULT_DELETE){
                    showMessage(requireActivity().getResources().getString(R.string.message_delete));
                }
            }
        }
    }

    @NonNull
    private static KeuanganViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(KeuanganViewModel.class);
    }

    private final Observer<List<Keu>> keuObserver = new Observer<List<Keu>>() {
        @Override
        public void onChanged(@Nullable List<Keu> keuList) {
            if (keuList != null) {
                adapter.setListNoteKeu(keuList);
                adapter.notifyDataSetChanged();
                List<Integer> listMoneyOut = new ArrayList<>();
                List<Integer> listMoneyIn = new ArrayList<>();
                for (int i =0; i<keuList.size(); i++){
                    int sumOut = 0;
                    int sumIn = 0;
                    if (keuList.get(i).isStatusIn()) {
                        sumIn = Integer.parseInt(keuList.get(i).getMoney());
                    }else {
                        sumOut = Integer.parseInt(keuList.get(i).getMoney());
                    }
                    listMoneyOut.add(sumOut);
                    listMoneyIn.add(sumIn);
                }
//                moneyStatusTrue = listMoneyIn.size();
                for (int i : listMoneyIn){
                    moneyStatusTrue += i;
                }
                for (int i : listMoneyOut){
                    moneyStatusFalse += i;
                }
                int summaryMoney = moneyStatusTrue - moneyStatusFalse;
                summaryIncome.setText(String.valueOf(moneyStatusTrue));
                summarySpending.setText(String.valueOf(moneyStatusFalse));
                tvSummaryMoney.setText(String.valueOf(summaryMoney));
            }
        }
    };

    private void showMessage(String message){
        Snackbar.make(rvKeu, message, Snackbar.LENGTH_SHORT).show();
    }
}
