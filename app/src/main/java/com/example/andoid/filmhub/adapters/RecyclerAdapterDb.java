package com.example.andoid.filmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.andoid.filmhub.DetailsActivity;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.roomdb.Items;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapterDb extends RecyclerView.Adapter<RecyclerAdapterDb.ViewHolder> {

    private List<Items> mList;

    public RecyclerAdapterDb(List<Items> mList) {
        this.mList = mList;
    }

    public void setmList(List<Items> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_layout_db, parent, false);
        return new RecyclerAdapterDb.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Items item = mList.get(position);

        String imageUrl = ApiClient.BASE_IMG_URL + item.getBackdrop();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .into(holder.imageView);

        holder.textView.setText(item.getItemTitle());
        holder.expandableTextView.setText(item.getOverview());

        if (item.isMovie()) {
            holder.setIsMovie(false);
        } else if (!item.isMovie()) {
            holder.setIsMovie(true);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public String getMovieOrShowId(int position) {
        Items currentElement = mList.get(position);
        return currentElement.getItemid();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private boolean isMovie;

        public void setIsMovie(boolean show) {
            isMovie = show;
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) itemView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        ImageView imageView;
        TextView textView;
        ExpandableTextView expandableTextView;

        public ViewHolder( View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.favoriteTitleImageView);
            imageView.setOnClickListener(v -> {
                if(isNetworkConnected()){
                    Context context = itemView.getContext();
                    int itemPosition = getLayoutPosition();

                    Intent intent = new Intent(context, DetailsActivity.class);
                    Items currentItem = mList.get(itemPosition);
                    intent.putExtra("typeValidation", isMovie);
                    intent.putExtra("titleId", currentItem.getItemid());
                    context.startActivity(intent);
                }else{
                    Toast.makeText(itemView.getContext(), "Please connect to internet !", Toast.LENGTH_SHORT).show();
                }

            });
            textView = itemView.findViewById(R.id.favoriteTitleTextView);
            expandableTextView = itemView.findViewById(R.id.expand_text_view);
        }
    }

}
