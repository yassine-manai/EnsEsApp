package dev.mobile.ensapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {
    private final List<AbsenceItem> absenceList;

    public AbsenceAdapter(List<AbsenceItem> absenceList) {
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        AbsenceItem absenceItem = absenceList.get(position);
        holder.teacherName.setText(absenceItem.getTeacherName());
        holder.absenceDate.setText(absenceItem.getAbsenceDateRange());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName;
        TextView absenceDate;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherName);
            absenceDate = itemView.findViewById(R.id.absenceDate);
        }
    }
}
