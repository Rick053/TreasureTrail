package com.utsdev.treasuretrail.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.HintViewHolder> {

    private List<String> data;
    private Context context;
    private View empty;

    public class HintViewHolder extends RecyclerView.ViewHolder {

        public HintViewHolder(View view) {
            super(view);
        }
    }

    public HintAdapter(Context context, View emptyView) {
        this.context = context;
        this.empty = emptyView;
    }

    @Override
    public HintViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(HintViewHolder hintViewHolder, int i) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addHint(String hint) {
        data.add(hint);
        notifyDataSetChanged();
    }
}
