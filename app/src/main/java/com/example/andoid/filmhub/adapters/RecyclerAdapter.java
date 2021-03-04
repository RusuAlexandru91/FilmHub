package com.example.andoid.filmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.andoid.filmhub.DetailsActivity;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.MainDetailsApi;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final List<MainDetailsApi> mlist;


    public RecyclerAdapter(List<MainDetailsApi> mlist) {
        this.mlist = mlist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainDetailsApi item = mlist.get(position);

        String imageUrl = ApiClient.BASE_IMG_URL + item.getImage();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);


        holder.textView.setText(item.getTitle());
        String cond = holder.textView.getText().toString();
        if (cond.isEmpty()) {
            holder.setIsMovie(true);
            holder.textView.setText(item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;

        private boolean isMovie = false;


        public void setIsMovie(boolean show) {
            isMovie = show;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.recycler_textview);
            imageView = itemView.findViewById(R.id.recyclerLayoutImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Context context = view.getContext();
            int itemPosition = getLayoutPosition();

            Intent intent = new Intent(context, DetailsActivity.class);
            MainDetailsApi currentItem = mlist.get(itemPosition);
            intent.putExtra("typeValidation", isMovie);
            intent.putExtra("titleId", currentItem.getId());
            context.startActivity(intent);
        }
    }

    public void addPages(List<MainDetailsApi> array) {
        mlist.addAll(array);
        notifyDataSetChanged();
    }
}
