package com.example.projekt2;

import android.app.Activity;
import android.content.Context;
import android.sax.Element;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<Phone> mListPhones;
    private LayoutInflater mInflater;

    public ListAdapter() {
    }

    public ListAdapter(List<Phone> mListPhones) {
        this.mListPhones = mListPhones;
    }

    public ListAdapter(Activity context, List<Phone> listPhones){
        mInflater = context.getLayoutInflater();
        this.mListPhones = listPhones;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View row = mInflater.inflate(R.layout.list_row, null);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Phone currentPhone = mListPhones.get(position);
        holder.producerTextView.setText(currentPhone.getPhoneProducer() + "                                       " );
        holder.modelTextView.setText(currentPhone.getPhoneModel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(currentPhone);
                }
            }
        });
         }

    @Override
    public int getItemCount() {
        if (mListPhones == null) {
            return 0;
        }
        return mListPhones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView producerTextView;
        TextView modelTextView;
        public MyViewHolder(View itemView){
            super(itemView);

            producerTextView = itemView.findViewById(R.id.producer);
            modelTextView = itemView.findViewById(R.id.model);

        }


    }
    public void submitList(List<Phone> phoneList) {
        if (phoneList == null) {
            throw new IllegalArgumentException("Phone list cannot be null");
        }
        mListPhones = phoneList;
        notifyDataSetChanged();
    }
    public List<Phone> getCurrentList() {
        return mListPhones;
    }

    public interface OnItemClickListener {
        void onItemClick(Phone phone);
    }
    private OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


}