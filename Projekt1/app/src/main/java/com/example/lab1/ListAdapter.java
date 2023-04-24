package com.example.lab1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    public List<ListForAdapter> listaOcen;
    private LayoutInflater Inflater;

    public ListAdapter() {
    }

    public ListAdapter(List<ListForAdapter> listaOcen, LayoutInflater inflater) {
        this.listaOcen = listaOcen;
        Inflater = inflater;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View row = Inflater.inflate(R.layout.list_row, null);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        ListForAdapter przedmiot = listaOcen.get(position);
        holder.przedmiotyTextView.setText(przedmiot.getNazwa());

        Integer liczbaocen = 4;
        for (int i = 1; i <= liczbaocen; i++) {
            RadioButton radioButton = new RadioButton(Inflater.getContext());
            radioButton.setText(String.valueOf(i + 1));
            radioButton.setId(i + 1);
            holder.ocenyRadio.addView(radioButton);
        }

        holder.ocenyRadio.check(przedmiot.getOcena());
        holder.ocenyRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                przedmiot.setOcena(checkedId);
            }
        });

        if (przedmiot.isChecked()) {
            holder.ocenyRadio.check(przedmiot.getOcena());
        } else {
            holder.ocenyRadio.clearCheck();
        }


    }


    @Override
    public int getItemCount() {
        return listaOcen.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView przedmiotyTextView;
        RadioGroup ocenyRadio;
        public MyViewHolder(View itemView){
            super(itemView);

            przedmiotyTextView = itemView.findViewById(R.id.textSubject);
            ocenyRadio = itemView.findViewById(R.id.RadioGroup);
            ocenyRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ListForAdapter przedmiot = listaOcen.get(adapterPosition);
                        przedmiot.setOcena(checkedId);
                    }
                }
            });
        }
    }

    // zapisanie stanu radiobutton po obróceniu ekranu
    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition != RecyclerView.NO_POSITION) {
            ListForAdapter przedmiot = listaOcen.get(adapterPosition);
            przedmiot.setOcena(holder.ocenyRadio.getCheckedRadioButtonId());
        }
        super.onViewRecycled(holder);
    }



}
