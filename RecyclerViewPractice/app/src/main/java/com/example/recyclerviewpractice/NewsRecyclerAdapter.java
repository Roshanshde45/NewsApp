package com.example.recyclerviewpractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Model> list;

    public NewsRecyclerAdapter(Context context, List<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = list.get(position);
        Picasso.get()
                .load(model.getImageURL()).placeholder(R.drawable.noimageavailable).fit()
                .into(holder.techImage);
        holder.titleCard.setText(model.getTitle());
        holder.descriptionCard.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView techImage;
        TextView titleCard, descriptionCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            techImage = itemView.findViewById(R.id.techImage);
            titleCard = itemView.findViewById(R.id.titleCard);
            descriptionCard = itemView.findViewById(R.id.descriptionCard);

        }
    }
}
