package com.example.findroom;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Summary extends AppCompatActivity {

    private EditText roomNameSumm;
    private TextView roomDetailsTextView;
    private TextView dayTextView;
    private TextView jamTextView;
    private TextView dateTextView;
    String days;

    private Button bookdetail;
    String roomId;
    String selectedTimeRange;
    DatabaseReference roomRef;
    RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_summary);

        // Get the Intent that started this activity
        roomNameSumm = findViewById(R.id.roomNamesumm);
        dayTextView = findViewById(R.id.sum_day);
        jamTextView = findViewById(R.id.sum_time);
        dateTextView = findViewById(R.id.detail_date);

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        // Create Calendar instance with WIB timezone
        Calendar calendar = Calendar.getInstance(timeZone);

        // Set the locale to Indonesian for formatting the day
        Locale indonesiaLocale = new Locale("id", "ID"); // Indonesian locale
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", indonesiaLocale);
        sdf.setTimeZone(timeZone);

        // Get current day and time
        days = sdf.format(calendar.getTime());
        dayTextView.setText(days);


        // Initialize views
        bookdetail = findViewById(R.id.btn_book_now);

        bookdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookRoom(roomId, selectedTimeRange);

                Intent intent = new Intent(Summary.this, MainActivity.class);
                startActivity(intent);
            }
        });

        roomId = getIntent().getStringExtra("roomId");
        roomNameSumm.setText("Room: " + roomId);


        selectedTimeRange = getIntent().getStringExtra("selectedTimeRange");
        jamTextView.setText(selectedTimeRange);
        Log.d("SummaryActivity", "Time Range Passed: " + selectedTimeRange);


        // Set the locale to Indonesian for formatting the day
        int currentDate = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int currentYear = calendar.get(Calendar.YEAR);


        dateTextView.setText(String.valueOf(currentDate) + "-" + String.valueOf(currentMonth) + "-" + String.valueOf(currentYear));

    }

    private void bookRoom(String roomId, String selectedTimeRange) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ruang")
                .child(roomId)
                .child(days)
                .child(selectedTimeRange);

        // Update availability to false when booked
        reference.child("available").setValue(false).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("SummaryActivity", "Room availability updated to false.");
                // Optionally, you can show a confirmation message or move to another screen
                Toast.makeText(Summary.this, "Room booked successfully!", Toast.LENGTH_SHORT).show();
                saveBookingDetails();

            } else {
                Log.d("SummaryActivity", "Failed to update room availability.");
            }
        });
    }
    private void saveBookingDetails() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(userID).child(roomId);

        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("date", days);
        bookingDetails.put("time", selectedTimeRange);
        bookingDetails.put("available", false);

        bookingRef.setValue(bookingDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SummaryActivity", "Booking saved successfully.");
                    } else {
                        Log.e("SummaryActivity", "Failed to save booking.");
                    }
                });
    }
        // Use the day and timeSlot passed from the previous activity

        // Fetch the data
    private void showUnavailableMessage() {
        // Show a message if the room is not available
        Toast.makeText(this, "Room is not available for booking.", Toast.LENGTH_SHORT).show();
    }

    private void showNoDataMessage() {
        // Show a message if no data exists
        Toast.makeText(this, "No data found for the selected room and time range.", Toast.LENGTH_SHORT).show();
    }
}