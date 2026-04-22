package uk.ac.plymouth.maternityapi.ui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatientRoomsApp extends JFrame {

    private JTextField patientIdField;
    private JButton searchButton;
    private JButton clearButton;
    private JButton leastUsedButton;
    private JTextArea resultsArea;

    public PatientRoomsApp() {
        setTitle("Maternity Unit System");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Patient Room Lookup");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdField = new JTextField(10);

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);

        clearButton = new JButton("Clear");

        leastUsedButton = new JButton("Least Used Rooms");
        leastUsedButton.setBackground(new Color(100, 180, 100));
        leastUsedButton.setForeground(Color.WHITE);

        inputPanel.add(patientIdLabel);
        inputPanel.add(patientIdField);
        inputPanel.add(searchButton);
        inputPanel.add(clearButton);
        inputPanel.add(leastUsedButton);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(500, 240));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        searchButton.addActionListener(e -> searchPatientRooms());
        clearButton.addActionListener(e -> clearFields());
        leastUsedButton.addActionListener(e -> getLeastUsedRooms());
    }

    private void searchPatientRooms() {
        String patientIdText = patientIdField.getText().trim();

        if (patientIdText.isEmpty() || !patientIdText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric patient ID (e.g. 1, 2, 3)");
            return;
        }

        resultsArea.setText("Loading...");

        try {
            String apiUrl = "http://localhost:8080/api/patients/" + patientIdText + "/rooms";

            String jsonResponse = callApi(apiUrl);

            if (jsonResponse.equals("[]")) {
                resultsArea.setText("No rooms found.\n\n(This may be due to missing data.)");
            } else {
                resultsArea.setText("Rooms:\n\n" + formatJson(jsonResponse));
            }

        } catch (Exception ex) {
            resultsArea.setText("Error connecting to system.\n" + ex.getMessage());
        }
    }

    private void getLeastUsedRooms() {
        resultsArea.setText("Loading least used rooms...");

        try {
            String apiUrl = "http://localhost:8080/api/rooms/least-used";

            String jsonResponse = callApi(apiUrl);

            if (jsonResponse.equals("[]")) {
                resultsArea.setText("No room data available.");
            } else {
                resultsArea.setText("Least Used Rooms:\n\n" + formatJson(jsonResponse));
            }

        } catch (Exception ex) {
            resultsArea.setText("Error connecting to system.\n" + ex.getMessage());
        }
    }

    private String callApi(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return response.toString();
    }

    private String formatJson(String json) {
        return json.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace(",", "\n");
    }

    private void clearFields() {
        patientIdField.setText("");
        resultsArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientRoomsApp().setVisible(true);
        });
    }
}