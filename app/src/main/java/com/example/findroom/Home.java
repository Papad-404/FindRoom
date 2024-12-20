package com.example.findroom;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Home extends Fragment {
    RecyclerView recyclerView;
    RoomAdapter roomAdapter;
    ArrayList<DataRoom> roomList;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this.getContext(), roomList);
        recyclerView.setAdapter(roomAdapter);


        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        // Create Calendar instance with WIB timezone
        Calendar calendar = Calendar.getInstance(timeZone);

        // Set the locale to Indonesian for formatting the day
        Locale indonesiaLocale = new Locale("id", "ID"); // Indonesian locale
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", indonesiaLocale);
        sdf.setTimeZone(timeZone);



    // Get current day and time
        String currentDay = sdf.format(calendar.getTime());  // Example: "Kamis" (Thursday)
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);  // Current hour in 24-hour format
        int currentMinute = calendar.get(Calendar.MINUTE);  // Current minute
        String currentTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Ruang");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();
                Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.toString());

                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    String nomor = roomSnapshot.getKey();
                    Log.d("RoomName", "Room: " + nomor);

                    DataSnapshot daySnapshot = roomSnapshot.child(currentDay);
                    if (daySnapshot.exists()) {
                        Log.d("DaySnapshot", "Day snapshot exists for room " + nomor);

                        boolean isAvailable = true;

                        for (DataSnapshot timeSlotSnapshot : daySnapshot.getChildren()) {
                            String timeSlot = timeSlotSnapshot.getKey();
                            Boolean available = timeSlotSnapshot.child("available").getValue(Boolean.class);

                            Log.d("TimeSlot", "Timeslot: " + timeSlot + ", Available: " + available);

                            // Split the timeSlot into start and end times (e.g., "07:30" and "10:00")
                            String[] times = timeSlot.split("-");
                            String startTime = times[0];
                            String endTime = times[1];

                            if (available != null && available && isCurrentTimeWithinRange(currentTime, startTime, endTime)) {
                                Log.d("AvailableRoom", "Room " + nomor + " is available from " + startTime + " to " + endTime);
                                // Add the available room to the list
                                isAvailable = false;
                                break;
                            }
                        }

                        roomList.add(new DataRoom(nomor, isAvailable));
                    } else {
                        Log.d("DaySnapshot", "No data for day " + currentDay + " for room " + nomor);
                    }
                }

                // Log the size of the room list
                Log.d("RoomListSize", "Room List Size: " + roomList.size());

                // Notify the adapter that the data has changed
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }

            private boolean isCurrentTimeWithinRange(String currentTime, String startTime, String endTime) {
                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", indonesiaLocale);
                    timeFormat.setTimeZone(timeZone);
                    Log.d("TimeFormat", "Time Format: " + timeFormat.toString());
                    Log.d("CurrentTime", "Current Time: " + currentTime);
                    Log.d("StartTime", "Start Time: " + startTime);
                    Log.d("EndTime", "End Time: " + endTime);

                    Date currentDate = timeFormat.parse(currentTime);
                    Date startDate = timeFormat.parse(startTime);
                    Date endDate = timeFormat.parse(endTime);

                    // Check if current time is within the start and end time range
                    return currentDate != null && startDate != null && endDate != null &&
                            currentDate.after(startDate) && currentDate.before(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }

        });


    }
}