import javax.swing.*;
import java.awt.GridLayout;
import java.util.*;


enum UserRole {
    STUDENT, FACULTY, RESPONDER, ADMIN
}

enum EmergencyType {
    MEDICAL, FIRE, NATURAL_DISASTER, SECURITY_THREAT, INFRA_FAILURE
}

enum IncidentStatus {
    REPORTED, IN_PROGRESS, RESOLVED
}

class User {
    String userId;
    String name;
    UserRole role;

    public User(String userId, String name, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}

class IncidentReport {
    String reportId;
    User reportedBy;
    EmergencyType type;
    String location;
    Date timestamp;
    IncidentStatus status;
    ResponseTeam assignedTeam;
    String resolutionNotes = "";

    public IncidentReport(String reportId, User reportedBy, EmergencyType type, String location) {
        this.reportId = reportId;
        this.reportedBy = reportedBy;
        this.type = type;
        this.location = location;
        this.timestamp = new Date();
        this.status = IncidentStatus.REPORTED;
    }

    public void assignTeam(ResponseTeam team) {
        this.assignedTeam = team;
        this.status = IncidentStatus.IN_PROGRESS;
    }

    public void resolve(String notes) {
        this.status = IncidentStatus.RESOLVED;
        this.resolutionNotes = notes;
    }
}

class ResponseTeam {
    String teamId;
    String teamType;
    List<User> members;
    boolean isAvailable;
    String location;

    public ResponseTeam(String teamId, String teamType, List<User> members, String location) {
        this.teamId = teamId;
        this.teamType = teamType;
        this.members = members;
        this.location = location;
        this.isAvailable = true;
    }

    public void assign() {
        isAvailable = false;
    }

    public void release() {
        isAvailable = true;
    }
}

class IncidentLog {
    List<IncidentReport> resolvedIncidents = new ArrayList<>();

    public void addLog(IncidentReport report) {
        resolvedIncidents.add(report);
    }

    public String getLogText() {
        StringBuilder sb = new StringBuilder();
        for (IncidentReport report : resolvedIncidents) {
            sb.append("Resolved Incident: ").append(report.reportId)
              .append(" | Type: ").append(report.type)
              .append(" | Location: ").append(report.location).append("\n");
        }
        return sb.toString();
    }
}

class CommunicationSystem {
    Map<String, List<String>> messages = new HashMap<>();

    public void sendMessage(User sender, User receiver, String message) {
        String key = sender.userId + "->" + receiver.userId;
        messages.putIfAbsent(key, new ArrayList<>());
        messages.get(key).add(sender.name + ": " + message);
    }

    public String getMessages(String key) {
        if (messages.containsKey(key)) {
            StringBuilder sb = new StringBuilder("\n--- Messages ---\n");
            for (String msg : messages.get(key)) {
                sb.append(msg).append("\n");
            }
            return sb.toString();
        }
        return "\n(No messages found.)\n";
    }
}

public class MCERCSApp {
    private static JFrame frame;
    private static JTextField nameField, locationField;
    private static JComboBox<String> roleBox, typeBox;
    private static JTextArea outputArea;
    private static IncidentLog incidentLog = new IncidentLog();
    private static IncidentReport currentIncident;
    private static ResponseTeam currentTeam;
    private static User responder;
    private static CommunicationSystem comms = new CommunicationSystem();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> buildGUI());
    }

    private static void buildGUI() {
        frame = new JFrame("MCERCS App");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1));

        JPanel form = new JPanel(new GridLayout(0, 2));

        form.add(new JLabel("Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"STUDENT", "FACULTY", "RESPONDER", "ADMIN"});
        form.add(roleBox);

        form.add(new JLabel("Emergency Type:"));
        typeBox = new JComboBox<>(new String[]{"MEDICAL", "FIRE", "NATURAL_DISASTER", "SECURITY_THREAT", "INFRA_FAILURE"});
        form.add(typeBox);

        form.add(new JLabel("Location:"));
        locationField = new JTextField();
        form.add(locationField);

        JButton reportButton = new JButton("Report Incident");
        form.add(reportButton);

        JButton clearButton = new JButton("Clear");
        form.add(clearButton);

        JButton feedbackButton = new JButton("Mark Resolved & Give Feedback");
        feedbackButton.setEnabled(false);
        form.add(feedbackButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        frame.add(form);
        frame.add(scrollPane);
        frame.setVisible(true);

        reportButton.addActionListener(e -> {
            handleReport();
            feedbackButton.setEnabled(true);
        });

        feedbackButton.addActionListener(e -> handleFeedback(feedbackButton));

        clearButton.addActionListener(e -> outputArea.setText(""));
    }

    private static void handleReport() {
        String name = nameField.getText();
        String location = locationField.getText();
        UserRole role = UserRole.valueOf(roleBox.getSelectedItem().toString());
        EmergencyType type = EmergencyType.valueOf(typeBox.getSelectedItem().toString());

        User user = new User("U" + new Random().nextInt(1000), name, role);
        currentIncident = new IncidentReport("R" + new Random().nextInt(1000), user, type, location);

        responder = new User("R1", "Responder One", UserRole.RESPONDER);
        List<User> teamMembers = new ArrayList<>();
        teamMembers.add(responder);
        currentTeam = new ResponseTeam("T1", type.toString(), teamMembers, location);

        if (currentTeam.isAvailable) {
            currentIncident.assignTeam(currentTeam);
            currentTeam.assign();
            appendOutput("🚨 Incident Reported!");
            appendOutput("🛠️ Team assigned: " + currentTeam.teamType);

            comms.sendMessage(user, responder, "Incident reported at: " + location);
            appendOutput(comms.getMessages(user.userId + "->" + responder.userId));
        } else {
            appendOutput("⚠️ No team available.");
        }
    }

    private static void handleFeedback(JButton feedbackButton) {
        String feedback = JOptionPane.showInputDialog(frame, "Please provide feedback about the resolution:");
        if (feedback != null && !feedback.trim().isEmpty()) {
            String location = locationField.getText();
            currentIncident.resolve("Issue handled successfully at " + location + " by " + responder.name + ". Feedback: " + feedback);
            currentTeam.release();
            incidentLog.addLog(currentIncident);

            appendOutput("✅ Incident Resolved: " + currentIncident.reportId);
            appendOutput("--- Incident Logs ---\n" + incidentLog.getLogText());

            feedbackButton.setEnabled(false);
        } else {
            appendOutput("⚠️ Feedback required to mark incident as resolved.");
        }
    }

    private static void appendOutput(String text) {
        outputArea.append(text + "\n");
    }
}
