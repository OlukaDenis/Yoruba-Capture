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
import com.app.plyss.ui.interfaces.ItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureAdapter extends FirestoreRecyclerAdapter<Form, CaptureAdapter.CaptureViewHolder> {
    private ItemClickListener itemClickListener;

    public CaptureAdapter(@NonNull FirestoreRecyclerOptions<Form> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CaptureViewHolder holder, int position, @NonNull Form model) {
        holder.bindFilterTo(model);
    }

    @NonNull
    @Override
    public CaptureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_capture, parent, false);
        return new CaptureViewHolder(view);
    }

    class CaptureViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.capture_name)
        TextView capture_name;

        @BindView(R.id.agent_id)
        TextView agent_id;

        @BindView(R.id.capture_date)
        TextView capture_date;

        @BindView(R.id.capture_time)
        TextView capture_time;

        Resources res;


        public CaptureViewHolder(@NonNull View itemView) {
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

        public void bindFilterTo(Form form) {
            String name = form.getLast_name() + " " + form.getLast_name();
            capture_name.setText(name);
            agent_id.setText(String.format(res.getString(R.string.captured_by), form.getAgentId()));
            capture_date.setText(form.getDate_of_capture());
            capture_time.setText(form.getTime_of_capture());
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
