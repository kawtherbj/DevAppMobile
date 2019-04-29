package com.example.devappmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<ProcessList> list;
   // private final OnListFragmentInteractionListener mListener;
    public interface OnItemClickListener {
        void onItemClick(ProcessList item);
    }


    private  OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

        }

        public void bind(final/* String title*/ ProcessList movie , final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }


    public MyAdapter(List<ProcessList> list,  OnItemClickListener listener) {
        this.list = list;
        this.listener=listener;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       /* String demand = list.get(position).getPro_title();
        holder.title.setText(demand);*/
        ProcessList movie = list.get(position);
        holder.title.setText(movie.getPro_title());
      //  holder.bind(list.get(position).getPro_title(), listener);
        holder.bind(movie, listener);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}