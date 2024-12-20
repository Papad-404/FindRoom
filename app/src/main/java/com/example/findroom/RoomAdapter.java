package com.example.findroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    Context context;
    List<DataRoom> roomList;

    public RoomAdapter(Context context, List<DataRoom> roomList) {
        this.context = context;
        this.roomList = roomList;
    }



    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listruang, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        DataRoom room = roomList.get(position);

        holder.recImage.setImageResource(R.drawable.ic_launcher_background);
        holder.recRuang.setText(room.getNomor());


        if (room.isAvailable()) {
            holder.recAvailable.setText("Available Now");
            holder.recAvailable.setTextColor(Color.GREEN);
        }
            else {
                holder.recAvailable.setText("Not Available");
                holder.recAvailable.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

class RoomViewHolder extends RecyclerView.ViewHolder {
    ImageView recImage;
    TextView recRuang;
    TextView recAvailable;
    CardView recCard;


    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.recCard);
        recImage = itemView.findViewById(R.id.recImage);
        recRuang = itemView.findViewById(R.id.recRuang);
        recAvailable = itemView.findViewById(R.id.recAvailable);
    }
}
