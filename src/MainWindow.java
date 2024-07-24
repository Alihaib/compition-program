import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class MainWindow extends JFrame {

    private JComboBox<String> competitionTypeCombo;
    private JComboBox<String> genderCombo;
    private JComboBox<String> weatherCombo;
    private JTextField arenaLengthField;
    private JTextField racerNameField;
    private JTextField racerAgeField;
    private JTextField racerMaxSpeedField;
    private JTextField racerAccelerationField;
    private JTextArea logArea;
    private JButton createArenaButton;
    private JButton addRacerButton;
    private JButton startCompetitionButton;
    private JButton showRacersInfoButton;
    private JLabel imageLabel;
    private BufferedImage arenaImage;
    private Image[] racerImages = new Image[10];
    private Point[] racerPositions = new Point[10];
    private double[] racerSpeeds = new double[10];
    private String[] racerNames = new String[10];
    private boolean[] racerFinished = new boolean[10];
    private int racerCount = 0;
    private Timer raceTimer;

    public MainWindow() {
        initUI();
    }

    private void initUI() {
        setTitle("Winter Competition");
        setLayout(new BorderLayout());

        // Panels
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(11, 2));
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        // Initialize components
        competitionTypeCombo = new JComboBox<>(new String[]{"Ski", "Snowboard"});
        genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        weatherCombo = new JComboBox<>(new String[]{"Sunny", "Cloudy", "Stormy"});
        arenaLengthField = new JTextField();
        racerNameField = new JTextField();
        racerAgeField = new JTextField();
        racerMaxSpeedField = new JTextField();
        racerAccelerationField = new JTextField();
        logArea = new JTextArea();
        createArenaButton = new JButton("Create Arena");
        addRacerButton = new JButton("Add Racer");
        startCompetitionButton = new JButton("Start Competition");
        showRacersInfoButton = new JButton("Show Racers Info");

        // Add components to control panel
        controlPanel.add(new JLabel("Competition Type:"));
        controlPanel.add(competitionTypeCombo);
        controlPanel.add(new JLabel("Gender:"));
        controlPanel.add(genderCombo);
        controlPanel.add(new JLabel("Weather:"));
        controlPanel.add(weatherCombo);
        controlPanel.add(new JLabel("Arena Length:"));
        controlPanel.add(arenaLengthField);
        controlPanel.add(new JLabel("Racer Name:"));
        controlPanel.add(racerNameField);
        controlPanel.add(new JLabel("Racer Age:"));
        controlPanel.add(racerAgeField);
        controlPanel.add(new JLabel("Racer Max Speed:"));
        controlPanel.add(racerMaxSpeedField);
        controlPanel.add(new JLabel("Racer Acceleration:"));
        controlPanel.add(racerAccelerationField);
        controlPanel.add(createArenaButton);
        controlPanel.add(addRacerButton);
        controlPanel.add(startCompetitionButton);
        controlPanel.add(showRacersInfoButton);

        // Set up the log panel
        logPanel.add(new JLabel("Log:"), BorderLayout.NORTH);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        // Initialize and add the image label
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Add panels to the frame
        add(logPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
        add(imagePanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        createArenaButton.addActionListener(e -> createArena());
        addRacerButton.addActionListener(e -> addRacer());
        startCompetitionButton.addActionListener(e -> startCompetition());
        showRacersInfoButton.addActionListener(e -> showRacersInfo());

        // Add a ComponentListener to the imageLabel to detect when it is resized
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImage();
            }
        });

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createArena() {
        String competitionType = (String) competitionTypeCombo.getSelectedItem();
        String weather = (String) weatherCombo.getSelectedItem();
        int arenaLength;

        try {
            arenaLength = Integer.parseInt(arenaLengthField.getText());
            if (arenaLength < 700 || arenaLength > 900) {
                JOptionPane.showMessageDialog(this, "Arena length must be between 700 and 900", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for arena length", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        logArea.append("Arena created: " + competitionType + ", " + weather + ", Length: " + arenaLength + "\n");
        updateImage();
    }

    private void updateImage() {
        String weather = (String) weatherCombo.getSelectedItem();
        String imagePath = "";
        switch (weather) {
            case "Sunny":
                imagePath = "/Sunny.jpg";
                break;
            case "Cloudy":
                imagePath = "/Cloudy.jpg";
                break;
            case "Stormy":
                imagePath = "/Stormy.jpg";
                break;
        }

        try {
            BufferedImage img = ImageIO.read(getClass().getResource(imagePath));
            if (img != null) {
                Image scaledImage = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                arenaImage = img;
                updateArena(); // Redraw the arena with any existing racers
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image", "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRacer() {
        if (racerCount >= racerImages.length) {
            JOptionPane.showMessageDialog(this, "Maximum number of racers reached", "Limit Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = racerNameField.getText();
        int age;
        double maxSpeed;
        double acceleration;
        String gender = (String) genderCombo.getSelectedItem();
        String competitionType = (String) competitionTypeCombo.getSelectedItem();
        String imagePath = "";

        try {
            age = Integer.parseInt(racerAgeField.getText());
            maxSpeed = Double.parseDouble(racerMaxSpeedField.getText());
            acceleration = Double.parseDouble(racerAccelerationField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age, max speed, and acceleration", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("Ski".equals(competitionType)) {
            if ("Male".equals(gender)) {
                imagePath = "/SkiMale.png";
            } else if ("Female".equals(gender)) {
                imagePath = "/SkiFemale.png";
            }
        } else if ("Snowboard".equals(competitionType)) {
            if ("Male".equals(gender)) {
                imagePath = "/SnowboardMale.png";
            } else if ("Female".equals(gender)) {
                imagePath = "/SnowboardFemale.png";
            }
        }

        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                BufferedImage racerImage = ImageIO.read(imageUrl);
                racerImages[racerCount] = racerImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                racerPositions[racerCount] = new Point(50 + (racerCount * 60), 0); // Start at the top with space between racers
                racerSpeeds[racerCount] = Double.parseDouble(racerMaxSpeedField.getText());
                racerNames[racerCount] = name;
                racerFinished[racerCount] = false;
                racerCount++;
                updateArena();
                logArea.append("Racer added: " + name + ", Age: " + age + ", Max Speed: " + maxSpeed + ", Acceleration: " + acceleration + "\n");
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading racer image", "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateArena() {
        if (arenaImage == null) {
            return;
        }

        BufferedImage combinedImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(arenaImage, 0, 0, null);

        for (int i = 0; i < racerCount; i++) {
            if (racerImages[i] != null && racerPositions[i] != null) {
                g2d.drawImage(racerImages[i], racerPositions[i].x, racerPositions[i].y, null);
            }
        }

        g2d.dispose();
        imageLabel.setIcon(new ImageIcon(combinedImage));
    }

    private void startCompetition() {
        if (raceTimer != null && raceTimer.isRunning()) {
            raceTimer.stop();
        }

        raceTimer = new Timer(30, new ActionListener() {
            private final int finishLine = imageLabel.getHeight() - 50; // Finish line position

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean raceOver = false;
                for (int i = 0; i < racerCount; i++) {
                    if (racerPositions[i] != null) {
                        racerPositions[i].y += racerSpeeds[i] / 10.0; // Move racers down with their speed
                        if (racerPositions[i].y >= finishLine) {
                            racerFinished[i] = true; // Mark racer as finished
                            JOptionPane.showMessageDialog(MainWindow.this, "Racer " + racerNames[i] + " wins!", "Race Over", JOptionPane.INFORMATION_MESSAGE);
                            raceTimer.stop();
                            raceOver = true;
                            break;
                        }
                    }
                }
                if (!raceOver) {
                    updateArena();
                }
            }
        });
        raceTimer.start();
    }

    private void showRacersInfo() {
        String[] columnNames = {"Name", "Max Speed", "Location", "Finished"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < racerCount; i++) {
            String name = racerNames[i];
            double maxSpeed = racerSpeeds[i];
            Point location = racerPositions[i];
            boolean finished = racerFinished[i];

            model.addRow(new Object[]{name, maxSpeed, location, finished ? "Yes" : "No"});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showMessageDialog(this, scrollPane, "Racers Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow ex = new MainWindow();
            ex.setVisible(true);
        });
    }
}
