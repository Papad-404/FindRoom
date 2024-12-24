package com.example.findroom;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

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

public class SearchAct extends Fragment {
    RecyclerView recyclerView;
    RoomAdapter roomAdapter;
    ArrayList<DataRoom> roomList;
    ImageView imageView;

    boolean isAvailable = true;


    public SearchAct() {
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

        roomAdapter = new RoomAdapter(getContext(), roomList);
        recyclerView.setAdapter(roomAdapter);

        Spinner spinnerTimeRange = view.findViewById(R.id.timeSpinner);
        ImageButton btnSearch = view.findViewById(R.id.searchButton);
        ArrayAdapter<CharSequence> timeAdapter;
        // 3 SKS selected
        timeAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.time_range_3_sks, android.R.layout.simple_spinner_item);

        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeRange.setAdapter(timeAdapter);


        // Search button click listener
        btnSearch.setOnClickListener(v -> {
            String selectedTimeRange = spinnerTimeRange.getSelectedItem().toString();
            filterRooms(selectedTimeRange);
            Log.d("SelectedTimeRange", "Selected Time Range: " + selectedTimeRange);

            roomAdapter.setSelectedTimeRange(selectedTimeRange);

        });


    }

    private void filterRooms(String selectedTimeRange) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Ruang");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();  // Clear the list for filtered data
                String[] times = selectedTimeRange.split("-");
                String filterStartTime = times[0];
                String filterEndTime = times[1];

                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    String roomNumber = roomSnapshot.getKey();
                    boolean isAvailable = false;

                    // Check availability for the selected time range
                    for (DataSnapshot daySnapshot : roomSnapshot.getChildren()) {
                        for (DataSnapshot timeSlotSnapshot : daySnapshot.getChildren()) {
                            String timeSlot = timeSlotSnapshot.getKey();
                            Boolean available = timeSlotSnapshot.child("available").getValue(Boolean.class);

                            if (available != null && available && isCurrentTimeWithinRange(timeSlot, filterStartTime, filterEndTime)) {
                                isAvailable = true;
                                break;
                            }
                        }
                    }

                    // Add only available rooms
                    if (isAvailable) {
                        roomList.add(new DataRoom(roomNumber, true));
                    }
                }
                roomAdapter.notifyDataSetChanged();  // Refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private boolean isCurrentTimeWithinRange(String currentTime, String startTime, String endTime) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        // Create Calendar instance with WIB timezone
        Calendar calendar = Calendar.getInstance(timeZone);

        // Set the locale to Indonesian for formatting the day
        Locale indonesiaLocale = new Locale("id", "ID"); // Indonesian locale
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", indonesiaLocale);
        sdf.setTimeZone(timeZone);

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


}