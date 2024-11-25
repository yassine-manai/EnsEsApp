package dev.mobile.ensapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.ProgressBar;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recentAbsencesRecyclerView;
    private AbsenceAdapter absenceAdapter;
    private List<AbsenceItem> absenceList;
    private FirebaseFirestore db;

    // UI Elements
    private TextView presentCount;
    private TextView absentCount;
    private TextView totalCount;
    private TextView presentPercentage;
    private TextView absentPercentage;
    private TextView totalChange;
    private ProgressBar attendanceProgress;
    private TextView dateRangeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        initializeViews();

        // Initialize RecyclerView
        initializeRecyclerView();

        // Initialize FAB
        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddAbsence);
        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddAbsenceActivity.class)));

        // Load data
        loadDashboardData();
    }

    private void initializeViews() {
        presentCount = findViewById(R.id.presentCount);
        absentCount = findViewById(R.id.absentCount);
        totalCount = findViewById(R.id.totalCount);
        presentPercentage = findViewById(R.id.presentPercentage);
        absentPercentage = findViewById(R.id.absentPercentage);
        totalChange = findViewById(R.id.totalChange);
        attendanceProgress = findViewById(R.id.attendanceProgress);
        dateRangeText = findViewById(R.id.dateRangeText);

        // Initialize date filter click listener
        findViewById(R.id.dateFilterButton).setOnClickListener(v -> showDateFilterDialog());
    }

    private void initializeRecyclerView() {
        recentAbsencesRecyclerView = findViewById(R.id.recentAbsencesRecyclerView);
        recentAbsencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        absenceList = new ArrayList<>();
        absenceAdapter = new AbsenceAdapter(absenceList);
        recentAbsencesRecyclerView.setAdapter(absenceAdapter);
    }

    private void loadDashboardData() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_WEEK, -7); // Get data for last week
        Date weekAgo = cal.getTime();

        db.collection("teachers")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int totalTeachers = 0;
                    int absentTeachers = 0;
                    List<AbsenceItem> recentAbsences = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : value) {
                        Teacher teacher = doc.toObject(Teacher.class);
                        teacher.setId(doc.getId());
                        totalTeachers++;

                        // Check for current absences
                        boolean isCurrentlyAbsent = false;
                        for (Absence absence : teacher.getAbsences()) {
                            if (isAbsenceActive(absence, currentDate)) {
                                isCurrentlyAbsent = true;
                                absentTeachers++;
                            }

                            // Add to recent absences if within last week
                            if (absence.getStartDate().after(weekAgo)) {
                                recentAbsences.add(new AbsenceItem(teacher.getName(), absence));
                            }
                        }
                    }

                    updateDashboardUI(totalTeachers, absentTeachers, recentAbsences);
                });
    }

    private boolean isAbsenceActive(Absence absence, Date currentDate) {
        return !currentDate.before(absence.getStartDate()) &&
                !currentDate.after(absence.getEndDate());
    }

    private void updateDashboardUI(int total, int absent, List<AbsenceItem> recentAbsences) {
        int present = total - absent;

        // Update counts
        totalCount.setText(String.valueOf(total));
        presentCount.setText(String.valueOf(present));
        absentCount.setText(String.valueOf(absent));

        // Update percentages
        float presentPercentageValue = total > 0 ? (present * 100f / total) : 0;
        float absentPercentageValue = total > 0 ? (absent * 100f / total) : 0;

        presentPercentage.setText(String.format("%.1f%%", presentPercentageValue));
        absentPercentage.setText(String.format("%.1f%%", absentPercentageValue));

        // Update progress bar
        attendanceProgress.setProgress((int) presentPercentageValue);

        // Update recent absences
        absenceList.clear();
        absenceList.addAll(recentAbsences);
        absenceAdapter.notifyDataSetChanged();
    }

    private void showDateFilterDialog() {
        // Implement date filter dialog here
        // This could show a custom dialog with date range options
    }
}