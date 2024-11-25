package dev.mobile.ensapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAbsenceActivity extends AppCompatActivity {
    private EditText dateInput, timeInput, roomInput, classInput, agentIdInput;
    private Button submitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_absence);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        roomInput = findViewById(R.id.roomInput);
        classInput = findViewById(R.id.classInput);
        agentIdInput = findViewById(R.id.agentIdInput);
        submitButton = findViewById(R.id.submitButton);

        // Date Picker
        dateInput.setOnClickListener(v -> showDatePicker());

        // Time Picker
        timeInput.setOnClickListener(v -> showTimePicker());

        // Submit Button
        submitButton.setOnClickListener(v -> submitAbsence());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dateInput.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                    timeInput.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void submitAbsence() {
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();
        String room = roomInput.getText().toString();
        String className = classInput.getText().toString();
        String agentId = agentIdInput.getText().toString();

        if (date.isEmpty() || time.isEmpty() || room.isEmpty() || className.isEmpty() || agentId.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create absence object
        Map<String, Object> absenceData = new HashMap<>();
        absenceData.put("date", date);
        absenceData.put("time", time);
        absenceData.put("room", room);
        absenceData.put("class", className);
        absenceData.put("agentId", agentId);

        // Save to Firestore
        db.collection("absences")
                .add(absenceData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
