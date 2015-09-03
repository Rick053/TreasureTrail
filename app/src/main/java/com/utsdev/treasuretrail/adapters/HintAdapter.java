package com.utsdev.treasuretrail.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.utsdev.treasuretrail.R;

import java.util.ArrayList;
import java.util.List;

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.HintViewHolder> {

    private List<String> data = new ArrayList<>();
    private Context context;

    public class HintViewHolder extends RecyclerView.ViewHolder {

        TextView hint;

        public HintViewHolder(View view) {
            super(view);

            hint = (TextView) view.findViewById(R.id.hint_text);
        }
    }

    public HintAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HintViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if ( viewGroup instanceof RecyclerView ) {
            int layoutId = -1;
            layoutId = R.layout.hint;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new HintViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(HintViewHolder hintViewHolder, int i) {
        String hint = data.get(i);

        hintViewHolder.hint.setText(hint);
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
