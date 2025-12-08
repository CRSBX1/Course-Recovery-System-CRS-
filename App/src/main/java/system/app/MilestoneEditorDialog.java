package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MilestoneEditorDialog extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private boolean confirmed = false;

    public MilestoneEditorDialog(Frame parent, List<Milestone> initial) {
        super(parent, "Edit Plan Milestones", true);
        setSize(650, 400);
        setLocationRelativeTo(parent);

        model = new DefaultTableModel(
                new Object[]{"Task ID","Study Week","Task"}, 0
        );

        for (Milestone m : initial) {
            model.addRow(new Object[]{
                    m.getMilestoneID(),
                    m.getWeekRange(),
                    m.getTaskDescription()
            });
        }

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel();

        JButton add = new JButton("Add");
        JButton del = new JButton("Delete");
        JButton ok = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");

        add.addActionListener(e -> addNew());
        del.addActionListener(e -> deleteSelected());
        ok.addActionListener(e -> { confirmed = true; setVisible(false); });
        cancel.addActionListener(e -> { confirmed = false; setVisible(false); });

        btns.add(add); btns.add(del); btns.add(ok); btns.add(cancel);

        add(btns, BorderLayout.SOUTH);
    }

    private void addNew() {
        String id = "M" + String.format("%03d", model.getRowCount() + 1);
        model.addRow(new Object[]{id, "Week 1", "New task"});
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r >= 0) model.removeRow(r);
    }

    public boolean isConfirmed() { return confirmed; }

    public List<Milestone> getMilestones() {
        List<Milestone> list = new ArrayList<>();
        for (int r = 0; r < model.getRowCount(); r++) {
            list.add(new Milestone(
                    (String) model.getValueAt(r, 0),
                    (String) model.getValueAt(r, 1),
                    (String) model.getValueAt(r, 2),
                    false
            ));
        }
        return list;
    }
}