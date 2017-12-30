package com.anmol.wedza.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.anmol.wedza.Interfaces.ItemClickListener;
import com.anmol.wedza.Model.Storyimage;
import com.anmol.wedza.R;

import java.util.List;

/**
 * Created by anmol on 12/30/2017.
 */

public class StoryimageAdapter extends RecyclerView.Adapter<StoryimageAdapter.MyViewHolder> {
    Context c;
    List<Storyimage> storyimages;
    private ItemClickListener mitemClickListener;

    public StoryimageAdapter(Context c, List<Storyimage> storyimages, ItemClickListener mitemClickListener) {
        this.c = c;
        this.storyimages = storyimages;
        this.mitemClickListener = mitemClickListener;
    }

    @Override
    public StoryimageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.storymedia,parent,false);
        return new MyViewHolder(v,mitemClickListener);
    }

    @Override
    public void onBindViewHolder(StoryimageAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return storyimages.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        private ItemClickListener mitemClickListener;
        public MyViewHolder(View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            mitemClickListener = itemClickListener;
            //img = (ImageView)itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mitemClickListener.onItemClick(this.getAdapterPosition());
        }
    }
}
