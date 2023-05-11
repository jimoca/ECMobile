package com.demo.ecclient;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.ecclient.model.EdgeInfo;
import com.demo.ecclient.databinding.FragmentEdgeBinding;

import java.util.List;

public class EdgeInfoRecyclerViewAdapter extends RecyclerView.Adapter<EdgeInfoRecyclerViewAdapter.ViewHolder> {

    private final List<EdgeInfo> mValues;

    public EdgeInfoRecyclerViewAdapter(List<EdgeInfo> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentEdgeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getEdgeId().toString());
        holder.mContentView.setText(mValues.get(position).getIpAddress());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public EdgeInfo mItem;

        public ViewHolder(FragmentEdgeBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}