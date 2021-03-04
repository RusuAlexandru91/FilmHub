package com.example.andoid.filmhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.CreditsDetailsApi;

import java.util.List;

public class RecyclerAdapterCast extends RecyclerView.Adapter<RecyclerAdapterCast.ViewHolder> {

    private final List<CreditsDetailsApi> mList;

    public RecyclerAdapterCast(List<CreditsDetailsApi> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_layout_round, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreditsDetailsApi item = mList.get(position);

        String imageUrl = ApiClient.BASE_IMG_URL + item.getActorPhoto();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.person_mock)
                .into(holder.imageView);

        holder.textView.setText(item.getActorName());
        holder.textView2.setText(item.getActorMovieName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.personPlaceHolderImageView);
            textView = itemView.findViewById(R.id.realNameTextView);
            textView2 = itemView.findViewById(R.id.actorNameTextView);


        }
    }
}
