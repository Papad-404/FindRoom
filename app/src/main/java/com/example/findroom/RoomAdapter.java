package com.example.findroom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
    private String selectedTimeRange;

    private String currentDays;

    public RoomAdapter(Context context, List<DataRoom> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    public void setSelectedTimeRange(String selectedTimeRange) {
        this.selectedTimeRange = selectedTimeRange;
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

        holder.recImage.setImageResource(R.drawable.wp34299063);
        holder.recRuang.setText(room.getNomor());

        if (room.isAvailable()) {
            holder.recAvailable.setText("Available Now");
            holder.recAvailable.setTextColor(Color.GREEN);
        }
            else {
                holder.recAvailable.setText("Not Available");
                holder.recAvailable.setTextColor(Color.RED);
        }
        holder.recCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, Summary.class);
            intent.putExtra("roomId", room.getNomor());
            intent.putExtra("selectedTimeRange", selectedTimeRange);
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateList(ArrayList<DataRoom> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
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
