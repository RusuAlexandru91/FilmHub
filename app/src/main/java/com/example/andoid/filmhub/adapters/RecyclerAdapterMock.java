package com.example.andoid.filmhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.util.HelperClass;

import java.util.ArrayList;

public class RecyclerAdapterMock extends RecyclerView.Adapter<RecyclerAdapterMock.ViewHolder> {

    ArrayList<HelperClass> mList;
    public RecyclerAdapterMock(ArrayList<HelperClass> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_layout_round_mock, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelperClass item = mList.get(position);

        holder.textView.setText(item.getmTitle());
        holder.textView2.setText(item.getmDate());
        holder.imageView.setImageResource(item.getmImage());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        TextView textView2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_person);
            textView = itemView.findViewById(R.id.textView_real_name);
            textView2 = itemView.findViewById(R.id.textView_movie_name);


        }
    }
}
