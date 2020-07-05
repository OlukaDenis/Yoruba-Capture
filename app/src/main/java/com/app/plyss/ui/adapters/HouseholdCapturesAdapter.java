package com.app.plyss.ui.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.plyss.R;
import com.app.plyss.data.model.Form;
import com.app.plyss.data.model.Household;
import com.app.plyss.ui.interfaces.ItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HouseholdCapturesAdapter extends FirestoreRecyclerAdapter<Household, HouseholdCapturesAdapter.HouseholdViewHolder> {
    private ItemClickListener itemClickListener;

    public HouseholdCapturesAdapter(@NonNull FirestoreRecyclerOptions<Household> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HouseholdViewHolder holder, int position, @NonNull Household model) {
        holder.bindFilterTo(model);
    }

    @NonNull
    @Override
    public HouseholdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_capture, parent, false);
        return new HouseholdViewHolder(view);
    }

    class HouseholdViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.capture_name)
        TextView capture_name;

        @BindView(R.id.agent_id)
        TextView agent_id;

        @BindView(R.id.capture_date)
        TextView capture_date;

        @BindView(R.id.capture_time)
        TextView capture_time;

        Resources res;


        public HouseholdViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            res = itemView.getResources();
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }

        public void bindFilterTo(Household household) {
            String name = household.getFather_name();
            capture_name.setText(name);
            agent_id.setText(String.format(res.getString(R.string.captured_by), household.getAgentId()));
            capture_date.setText(household.getDate_of_capture());
            capture_time.setText(household.getTime_of_capture());
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
