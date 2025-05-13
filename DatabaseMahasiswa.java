import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseMahasiswa extends JFrame {
    private JComboBox<String> jenisCombo;
    private JTextField namaField;
    private JTextField nimField;
    private JTextField universitasField;
    private JTextArea outputArea;
    private JButton submitButton;
    private JButton clearButton;

    private BufferedWriter writer;

    private JPanel homePanel;
    private JPanel inputPanel;

    private JButton startInputButton;
    private JButton menuButton;

    private JToggleButton darkLightToggle;
    private JButton lihatDataButton;

    private boolean darkMode = false;

    public DatabaseMahasiswa() {
        setTitle("DATABASE MAHASISWA");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            writer = new BufferedWriter(new FileWriter("mahasiswa_data.txt", true));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error opening file for writing: " + e.getMessage());
        }

        initHomePanel();
        initInputPanel();

        setContentPane(homePanel);
    }

    private void initHomePanel() {
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("DATABASE MAHASISWA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startInputButton = new JButton("Mulai Menginput");
        startInputButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton = new JButton("Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startInputButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(menuButton);
        buttonPanel.add(Box.createVerticalGlue());

        homePanel.add(Box.createVerticalGlue());
        homePanel.add(titleLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        homePanel.add(buttonPanel);
        homePanel.add(Box.createVerticalGlue());

        startInputButton.addActionListener(e -> switchToInputPanel());

        menuButton.addActionListener(e -> showMenuDialog());
    }

    private JButton backButton;

    private void initInputPanel() {
        inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel jenisLabel = new JLabel("Jenis Mahasiswa:");
        jenisCombo = new JComboBox<>(new String[]{"Reguler", "Transfer"});
        jenisCombo.addActionListener(e -> toggleUniversitasField());

        JLabel namaLabel = new JLabel("Nama:");
        namaField = new JTextField(15);
        namaField.addActionListener(e -> nimField.requestFocusInWindow());

        JLabel nimLabel = new JLabel("NIM:");
        nimField = new JTextField(15);
        nimField.addActionListener(e -> universitasField.requestFocusInWindow());

        JLabel universitasLabel = new JLabel("Universitas Asal:");
        universitasField = new JTextField(35);

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFields());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> switchToHomePanel());

        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;

        // tampilkan tombol: submit, clear, back
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        // Row 0: Jenis Mahasiswa label and combo (2 columns)
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(jenisLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(jenisCombo, gbc);

        // Row 1: Nama label and field (col 0,1), NIM label and field (col 2,3)
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(namaLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        inputPanel.add(namaField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(nimLabel, gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        inputPanel.add(nimField, gbc);

        // Row 2: Universitas Asal label and field (span all 4 columns)
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(universitasLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(universitasField, gbc);

        // Row 3: Buttons panel (span all 4 columns)
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(buttonPanel, gbc);

        // Row 4: Text area scroll pane (span all 4 columns)
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        inputPanel.add(scrollPane, gbc);

        toggleUniversitasField();
    }

    private void switchToInputPanel() {
        setContentPane(inputPanel);
        revalidate();
        repaint();
    }

    private void switchToHomePanel() {
        setContentPane(homePanel);
        revalidate();
        repaint();
    }

    private void showMenuDialog() {
        JDialog menuDialog = new JDialog(this, "Menu", true);
        menuDialog.setSize(300, 150);
        menuDialog.setLocationRelativeTo(this);
        menuDialog.setLayout(new FlowLayout());

        darkLightToggle = new JToggleButton("Light Mode");
        darkLightToggle.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                darkMode = true;
                darkLightToggle.setText("Dark Mode");
            } else {
                darkMode = false;
                darkLightToggle.setText("Light Mode");
            }
            applyTheme();
        });

        lihatDataButton = new JButton("Lihat Data");
        lihatDataButton.addActionListener(e -> openDataViewer());

        menuDialog.add(darkLightToggle);
        menuDialog.add(lihatDataButton);

        menuDialog.setVisible(true);
    }

    private void applyTheme() {
        Color bgColor = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fgColor = darkMode ? Color.WHITE : Color.BLACK;

        homePanel.setBackground(bgColor);
        inputPanel.setBackground(bgColor);

        // Perbarui komponen di homePanel
        for (Component comp : homePanel.getComponents()) {
            comp.setBackground(bgColor);
            comp.setForeground(fgColor);
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    innerComp.setBackground(bgColor);
                    innerComp.setForeground(fgColor);
                }
            }
        }

        // Perbarui komponen di inputPanel
        for (Component comp : inputPanel.getComponents()) {
            comp.setBackground(bgColor);
            comp.setForeground(fgColor);
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    innerComp.setBackground(bgColor);
                    innerComp.setForeground(fgColor);
                }
            }
        }

        outputArea.setBackground(bgColor);
        outputArea.setForeground(fgColor);

        repaint();
    }

    private void openDataViewer() {
        JFrame viewerFrame = new JFrame("Data Mahasiswa");
        viewerFrame.setSize(400, 300);
        viewerFrame.setLocationRelativeTo(this);

        JTextArea dataArea = new JTextArea();
        dataArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(dataArea);

        viewerFrame.add(scrollPane);

        try {
            String content = new String(Files.readAllBytes(Paths.get("mahasiswa_data.txt")));
            dataArea.setText(content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading data file: " + e.getMessage());
        }

        // Hapus modality untuk memungkinkan interaksi dengan window lain
        // Modality berarti window akan memblokir interaksi dengan window lain
        // hingga window tersebut ditutup. Dengan menghapus modalitas, maka
        // user bisa berinteraksi dengan window lain tanpa perlu menutup window ini.
        viewerFrame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        viewerFrame.setAlwaysOnTop(true);
        viewerFrame.toFront();
        viewerFrame.requestFocus();
        viewerFrame.setVisible(true);
    }

    private void toggleUniversitasField() {
        String jenis = (String) jenisCombo.getSelectedItem();
        universitasField.setEnabled("Transfer".equals(jenis));
    }

    private void handleSubmit() {
        String jenis = (String) jenisCombo.getSelectedItem();
        String namaInput = namaField.getText().trim();
        String nim = nimField.getText().trim();
        String universitasAsal = universitasField.getText().trim();

        if (namaInput.isEmpty() || nim.isEmpty() || ("Transfer".equals(jenis) && universitasAsal.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi semua field yang diperlukan.");
            return;
        }

        // Validasi NIM: harus diawali dengan huruf diikuti tepat 9 angka
        if (!nim.matches("^[A-Za-z]\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "NIM harus diawali huruf diikuti 9 angka, contoh: H071241035");
            return;
        }

        // Format nama: huruf besar pertama tiap kata, sisanya kecil
        String nama = formatName(namaInput);

        OverRiddingMahasiswa mahasiswa;
        if ("Reguler".equals(jenis)) {
            mahasiswa = new MahasiswaReguler(nama, nim);
        } else {
            mahasiswa = new MahasiswaTransfer(nama, nim, universitasAsal);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Jenis Mahasiswa: ").append(jenis).append("\n");
        sb.append("Nama: ").append(nama).append("\n");
        sb.append("NIM: ").append(nim).append("\n");

        sb.append("Output:\n");
        sb.append(mahasiswa.getPerkenalanText()).append("\n");
        sb.append(mahasiswa.getBelajarText()).append("\n");
        sb.append(mahasiswa.getTugasText()).append("\n");

        if (mahasiswa instanceof MahasiswaTransfer) {
            sb.append(((MahasiswaTransfer) mahasiswa).getInfoTransferText()).append("\n");
        }

        sb.append("-----------------------------------------------\n");

        outputArea.append(sb.toString());

        try {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
        }

        clearFields();
    }

    private void clearFields() {
        namaField.setText("");
        nimField.setText("");
        universitasField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseMahasiswa frame = new DatabaseMahasiswa();
            frame.setVisible(true);
        });
    }

    private String formatName(String name) {
        String[] words = name.split("\\s+");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                formatted.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    formatted.append(word.substring(1).toLowerCase());
                }
                formatted.append(" ");
            }
        }
        return formatted.toString().trim();
    }
}

// Abstract class dan subclasses with dengan method mengembalikan text

abstract class OverRiddingMahasiswa {
    protected String nama;
    protected String nim;

    public OverRiddingMahasiswa(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    public String getPerkenalanText() {
        return "Halo, nama saya " + nama + " dengan NIM " + nim;
    }

    public abstract String getBelajarText();
    public abstract String getTugasText();
}

class MahasiswaReguler extends OverRiddingMahasiswa {
    public MahasiswaReguler(String nama, String nim) {
        super(nama, nim);
    }

    @Override
    public String getBelajarText() {
        return nama + " sedang belajar di kampus..";
    }

    @Override
    public String getTugasText() {
        return nama + " mengerjakan tugas kuliah biasa..";
    }
}

class MahasiswaTransfer extends OverRiddingMahasiswa {
    private String universitasAsal;

    public MahasiswaTransfer(String nama, String nim, String universitasAsal) {
        super(nama, nim);
        this.universitasAsal = universitasAsal;
    }

    @Override
    public String getBelajarText() {
        return nama + " belajar sambil menyesuaikan diri dari transfer..";
    }

    @Override
    public String getTugasText() {
        return nama + " mengerjakan tugas tambahan untuk adaptasi..";
    }

    public String getInfoTransferText() {
        return nama + " berasal dari universitas " + universitasAsal + ".";
    }

    public String getUniversitasAsal() {
        return universitasAsal;
    }
}
