### **Pendahuluan**

File ini berisi program untuk mengelola data mahasiswa, dengan tampilan antarmuka grafis (GUI) menggunakan Java Swing. Program ini memungkinkan pengguna untuk memasukkan data mahasiswa, melihat data yang telah dimasukkan, serta mengubah tema tampilan antara mode terang (light mode) dan mode gelap (dark mode).

### **Struktur Kode**

Program ini terdiri dari beberapa bagian utama:

1. **Inisialisasi dan Konfigurasi GUI**
2. **Panel Input Data Mahasiswa**
3. **Panel Tampilan Data Mahasiswa**
4. **Fungsi untuk Menyimpan dan Membaca Data**
5. **Fungsi untuk Mengatur Tema (Light/Dark Mode)**

### **1. Inisialisasi dan Konfigurasi GUI**

Pada bagian ini, kita mendefinisikan elemen-elemen GUI yang akan digunakan.

* `JFrame` digunakan sebagai jendela utama aplikasi.
* `JButton`, `JTextField`, `JComboBox`, dan `JTextArea` adalah komponen yang digunakan untuk menerima input dari pengguna dan menampilkan output.
* `BufferedWriter` digunakan untuk menyimpan data yang dimasukkan ke dalam file teks.

```java
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
```

### **2. Panel Input Data Mahasiswa**

Panel ini berisi elemen untuk mengisi data mahasiswa seperti nama, NIM (Nomor Induk Mahasiswa), jenis mahasiswa (reguler atau transfer), dan universitas asal (untuk mahasiswa transfer).

* `JComboBox` digunakan untuk memilih jenis mahasiswa.
* `JTextField` digunakan untuk mengisi nama, NIM, dan universitas asal.
* `JTextArea` digunakan untuk menampilkan data yang sudah dimasukkan.

```java
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
    
    // Menambahkan tombol untuk submit, clear, dan kembali ke panel utama
    ...
}
```

### **3. Panel Tampilan Data Mahasiswa**

Panel ini memungkinkan pengguna untuk melihat data mahasiswa yang telah dimasukkan. Data tersebut akan disimpan dalam file teks dan dapat dibaca kembali ke dalam aplikasi.

```java
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
    viewerFrame.setVisible(true);
}
```

### **4. Fungsi untuk Menyimpan dan Membaca Data**

Setiap data yang dimasukkan oleh pengguna akan disimpan ke dalam file `mahasiswa_data.txt`. Program ini juga memungkinkan untuk membaca data tersebut dan menampilkannya kembali dalam aplikasi.

```java
private void handleSubmit() {
    String jenis = (String) jenisCombo.getSelectedItem();
    String namaInput = namaField.getText().trim();
    String nim = nimField.getText().trim();
    String universitasAsal = universitasField.getText().trim();

    // Validasi input
    ...

    OverRiddingMahasiswa mahasiswa;
    if ("Reguler".equals(jenis)) {
        mahasiswa = new MahasiswaReguler(nama, nim);
    } else {
        mahasiswa = new MahasiswaTransfer(nama, nim, universitasAsal);
    }

    // Menambahkan data ke outputArea dan menyimpan ke file
    sb.append("Jenis Mahasiswa: ").append(jenis).append("\n");
    writer.write(sb.toString());
    writer.flush();
}
```

### **5. Fungsi untuk Mengatur Tema (Light/Dark Mode)**

Fitur ini memungkinkan pengguna untuk mengubah tema tampilan aplikasi, dengan menambahkan toggle button untuk memilih antara mode terang atau gelap.

```java
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

    menuDialog.add(darkLightToggle);
    menuDialog.setVisible(true);
}

private void applyTheme() {
    Color bgColor = darkMode ? Color.DARK_GRAY : Color.WHITE;
    Color fgColor = darkMode ? Color.WHITE : Color.BLACK;

    homePanel.setBackground(bgColor);
    inputPanel.setBackground(bgColor);
    // Update komponen lainnya
    repaint();
}
```

### **Kesimpulan**

Kode ini adalah aplikasi sederhana untuk mengelola data mahasiswa dengan menggunakan Java Swing. Program ini mendemonstrasikan bagaimana menggunakan komponen GUI seperti `JTextField`, `JButton`, dan `JComboBox` untuk menerima input, serta cara menulis dan membaca data dari file.

Jika ada pertanyaan atau bagian yang masih belum jelas, jangan ragu untuk bertanya lebih lanjut!
