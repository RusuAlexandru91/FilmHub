package com.example.andoid.filmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.andoid.filmhub.DetailsActivity;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ArrayApi;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<ArrayApi> mlist;

    public RecyclerAdapter(List<ArrayApi> mlist) {
        this.mlist = mlist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ArrayApi item = mlist.get(position);

        String imageUrl = ApiClient.BASE_IMG_URL + item.getImage();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.mock)
                .into(holder.imageView);


        holder.textView.setText(item.getTitle());
        String cond = holder.textView.getText().toString();
        if( cond.isEmpty()){
            holder.textView.setText(item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.recycler_textview);
            imageView = itemView.findViewById(R.id.recycler_imageview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Context context=view.getContext();
            int itemPosition = getLayoutPosition();
            Toast.makeText(context, "Position: " + itemPosition, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("name","" + mlist.get(itemPosition).getImage());
            context.startActivity(intent);
        }
    }
    public void addPages(List<ArrayApi> array){
        for( ArrayApi nextPage : array) {
        mlist.add(nextPage);
        }
        notifyDataSetChanged();
    }
}
