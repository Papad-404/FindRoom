package com.example.findroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Booked extends Fragment {

   RecyclerView recyclerView;
    BookingAdapter bookingAdapter;
    List<Booking> bookingList;

    private DatabaseReference bookingRef;

    public Booked() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_booked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewbook);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Get user ID and fetch bookings
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(userID);

        fetchUserBookings();

    }

    private void fetchUserBookings() {
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    String roomName = bookingSnapshot.getKey();
                    String date = bookingSnapshot.child("date").getValue(String.class);
                    String time = bookingSnapshot.child("time").getValue(String.class);
                    boolean available = bookingSnapshot.child("available").getValue(Boolean.class);

                    Booking booking = new Booking(roomName, date, time, available);
                    bookingList.add(booking);
                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BookingFragment", "Failed to fetch bookings.", error.toException());
            }
        });
    }
}