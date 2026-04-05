import java.awt.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HotelReservationSystem {

    // DB connection
    private static Connection connection;

    // UI Components
    private static JFrame frame;
    private static JTable table;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) throws Exception {
        // load config
        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));
        connection = DriverManager.getConnection(
                prop.getProperty("db.url"),
                prop.getProperty("db.user"),
                prop.getProperty("db.password"));

        // build UI on Event Dispatch Thread (Swing rule)
        SwingUtilities.invokeLater(() -> buildUI());
    }

    private static void buildUI() {
        // ── FRAME ──────────────────────────────────────
        frame = new JFrame("Hotel Reservation System");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ── TITLE ──────────────────────────────────────
        JLabel title = new JLabel("Hotel Reservation System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setOpaque(true);
        title.setBackground(new Color(44, 62, 80));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        frame.add(title, BorderLayout.NORTH);

        // ── SIDEBAR ────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(160, 0));

        String[] btnNames = {"Reserve", "View All", "Update", "Delete", "Search", "Exit"};
        for (String name : btnNames) {
            JButton btn = new JButton(name);
            btn.setBackground(new Color(41, 128, 185));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // button actions
            btn.addActionListener(e -> {
                switch (name) {
                    case "Reserve" -> reserveRoom();
                    case "View All" -> viewReservations();
                    case "Update"  -> updateReservation();
                    case "Delete"  -> deleteReservation();
                    case "Search"  -> searchReservation();
                    case "Exit"    -> System.exit(0);
                }
            });
            sidebar.add(btn);
        }
        frame.add(sidebar, BorderLayout.WEST);

        // ── TABLE ──────────────────────────────────────
        String[] columns = {"ID", "Guest Name", "Room No", "Contact", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // load data on startup
        viewReservations();

        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

    // ── RESERVE ────────────────────────────────────────
    private static void reserveRoom() {
        JTextField nameField    = new JTextField();
        JTextField roomField    = new JTextField();
        JTextField contactField = new JTextField();

        Object[] fields = {
            "Guest Name:",    nameField,
            "Room Number:",   roomField,
            "Contact Number:", contactField
        };

        int result = JOptionPane.showConfirmDialog(frame, fields,
                "Reserve a Room", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setInt(2, Integer.parseInt(roomField.getText()));
                ps.setString(3, contactField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Room reserved successfully!");
                viewReservations(); // refresh table
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        }
    }

    // ── VIEW ALL ───────────────────────────────────────
    private static void viewReservations() {
        try {
            tableModel.setRowCount(0); // clear table
            ResultSet rs = connection.createStatement()
                    .executeQuery("SELECT * FROM reservations");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("reservation_id"),
                    rs.getString("guest_name"),
                    rs.getInt("room_number"),
                    rs.getString("contact_number"),
                    rs.getString("reservation_date")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    // ── UPDATE ─────────────────────────────────────────
    private static void updateReservation() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Reservation ID to update:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            JTextField nameField    = new JTextField();
            JTextField roomField    = new JTextField();
            JTextField contactField = new JTextField();

            Object[] fields = {
                "New Guest Name:",    nameField,
                "New Room Number:",   roomField,
                "New Contact Number:", contactField
            };

            int result = JOptionPane.showConfirmDialog(frame, fields,
                    "Update Reservation", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String sql = "UPDATE reservations SET guest_name=?, room_number=?, contact_number=? WHERE reservation_id=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setInt(2, Integer.parseInt(roomField.getText()));
                ps.setString(3, contactField.getText());
                ps.setInt(4, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Updated successfully!");
                viewReservations();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    // ── DELETE ─────────────────────────────────────────
    private static void deleteReservation() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Reservation ID to delete:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete reservation " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM reservations WHERE reservation_id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Deleted successfully!");
                viewReservations();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    // ── SEARCH ─────────────────────────────────────────
    private static void searchReservation() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Reservation ID to search:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM reservations WHERE reservation_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0); // show only search result
            if (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("reservation_id"),
                    rs.getString("guest_name"),
                    rs.getInt("room_number"),
                    rs.getString("contact_number"),
                    rs.getString("reservation_date")
                });
            } else {
                JOptionPane.showMessageDialog(frame, "No reservation found with ID " + id);
                viewReservations(); // restore full table
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }
}