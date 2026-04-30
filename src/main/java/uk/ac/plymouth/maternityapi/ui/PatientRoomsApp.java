package uk.ac.plymouth.maternityapi.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatientRoomsApp extends JFrame {

    private JTextField patientIdField;
    private JButton searchButton;
    private JButton clearButton;
    private JButton leastUsedButton;
    private JTextArea resultsArea;

    // Demo data for Part B visualisation
    private final Map<String, List<String>> demoRoomsByPatient = new LinkedHashMap<>();
    private final List<String> demoLeastUsedRooms = List.of("C2", "D3");

    public PatientRoomsApp() {
        setTitle("Maternity Unit System");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadDemoData();
        initComponents();
    }

    private void loadDemoData() {
        demoRoomsByPatient.put("1", List.of("B1"));
        demoRoomsByPatient.put("2", List.of("A1"));
        demoRoomsByPatient.put("5", List.of("C2", "B1", "D3"));
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Patient Room Lookup");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        patientIdField = new JTextField(10);
        patientIdField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        clearButton = new JButton("Clear");

        leastUsedButton = new JButton("Least Used Rooms");
        leastUsedButton.setBackground(new Color(100, 180, 100));
        leastUsedButton.setForeground(Color.WHITE);
        leastUsedButton.setFocusPainted(false);

        inputPanel.add(patientIdLabel);
        inputPanel.add(patientIdField);
        inputPanel.add(searchButton);
        inputPanel.add(clearButton);
        inputPanel.add(leastUsedButton);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        resultsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(700, 420));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        searchButton.addActionListener(e -> searchPatientRooms());
        clearButton.addActionListener(e -> clearFields());
        leastUsedButton.addActionListener(e -> showLeastUsedRooms());
    }

    private void searchPatientRooms() {
        String patientIdText = patientIdField.getText().trim();

        if (patientIdText.isEmpty() || !patientIdText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric patient ID (e.g. 1, 2, 5)");
            return;
        }

        List<String> rooms = demoRoomsByPatient.get(patientIdText);

        if (rooms != null && !rooms.isEmpty()) {
            resultsArea.setText(
                    "DEMO DATA ACTIVE\n\n" +
                            "Rooms used by patient " + patientIdText + ":\n\n" +
                            String.join("\n", rooms)
            );
        } else {
            resultsArea.setText(
                    "DEMO DATA ACTIVE\n\n" +
                            "No rooms found for patient ID " + patientIdText + "."
            );
        }
    }

    private void showLeastUsedRooms() {
        resultsArea.setText(
                "DEMO DATA ACTIVE\n\n" +
                        "Least Used Rooms:\n\n" +
                        String.join("\n", demoLeastUsedRooms)
        );
    }

    private void clearFields() {
        patientIdField.setText("");
        resultsArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientRoomsApp().setVisible(true));
    }
}