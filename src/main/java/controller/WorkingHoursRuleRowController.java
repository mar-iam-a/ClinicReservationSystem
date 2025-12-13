package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.WorkingHoursRule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkingHoursRuleRowController {
    @FXML
    private ComboBox<DayOfWeek> dayCombo;
    @FXML private ComboBox<LocalTime> fromCombo;
    @FXML private ComboBox<LocalTime> toCombo;
    @FXML private Button deleteBtn;

    private final Runnable onDelete;

    public WorkingHoursRuleRowController(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    @FXML
    public void initialize() {
        dayCombo.getItems().addAll(DayOfWeek.values());
        fromCombo.getItems().addAll(generateTimeOptions());
        toCombo.getItems().addAll(generateTimeOptions());

        deleteBtn.setOnAction(e -> onDelete.run());
    }

    public void setRule(WorkingHoursRule rule) {
        if (rule != null) {
            dayCombo.setValue(rule.getDay());
            fromCombo.setValue(rule.getStartTime());
            toCombo.setValue(rule.getEndTime());
        }
    }

    public WorkingHoursRule getRule() {
        DayOfWeek d = dayCombo.getValue();
        LocalTime from = fromCombo.getValue();
        LocalTime to = toCombo.getValue();
        if (d == null || from == null || to == null || !from.isBefore(to)) {
            return null; // invalid
        }
        return new WorkingHoursRule(0, d, from, to);
    }

    private List<LocalTime> generateTimeOptions() {
        List<LocalTime> times = new ArrayList<>();
        for (int h = 7; h <= 21; h++) {
            times.add(LocalTime.of(h, 0));
            times.add(LocalTime.of(h, 30));
        }
        return times;
    }
}