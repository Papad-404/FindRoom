package com.example.findroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final Context context;
    private final List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookingitem, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.roomName.setText(booking.getRoomName());
        holder.bookImage.setImageResource(R.drawable.wp34299063);
        //holder.date.setText("Date: " + booking.getDate());
        //holder.time.setText("Time: " + booking.getTime());
        holder.availabook.setText("Reserved");

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView roomName, date, time, availabook;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.bookRuang);
            availabook = itemView.findViewById(R.id.bookAvailable);
            bookImage = itemView.findViewById(R.id.bookImage);

        }
    }
}

