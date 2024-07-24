import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;

public class SkiCompetitionGUI extends JFrame {
    private JButton startButton;
    private JButton addCompetitorButton;
    private JButton buildArenaButton;
    private JTextArea infoArea;
    private JPanel competitionPanel;
    private boolean competitionRunning = false;
    private List<Competitor> competitors = new ArrayList<>();
    private int arenaLength = 700;
    private int maxCompetitors = 10;
    private BufferedImage arenaImage;
    private Image[] competitorImages = new Image[10]; // Array to hold competitor images

    public SkiCompetitionGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("Ski Competition");
        setLayout(new BorderLayout());

        // Create a control panel and place it on the right
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1));
        controlPanel.setPreferredSize(new Dimension(200, getHeight())); // Adjust width as needed

        buildArenaButton = new JButton("Build Arena");
        addCompetitorButton = new JButton("Add Competitor");
        startButton = new JButton("Start Competition");

        controlPanel.add(buildArenaButton);
        controlPanel.add(addCompetitorButton);
        controlPanel.add(startButton);

        // Create a competition panel with a large background
        competitionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (arenaImage != null) {
                    g.drawImage(arenaImage, 0, 0, this);
                }
                // Draw competitors
                for (Competitor competitor : competitors) {
                    g.drawImage(competitor.getImage(), competitor.getPosition(), 50, this);
                }
            }
        };
        competitionPanel.setBackground(Color.WHITE);
        competitionPanel.setLayout(null); // Absolute layout for custom positioning

        // Create a scroll pane for the info area
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(infoArea);

        // Add components to the frame
        add(competitionPanel, BorderLayout.CENTER); // Place the competition panel in the center
        add(infoScrollPane, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST); // Place the control panel on the right

        buildArenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildArena();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCompetition();
            }
        });

        addCompetitorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCompetitor();
            }
        });

        setSize(1000, 700); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildArena() {
        String weather = "Sunny"; // Example; get actual weather from user input or settings
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
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                arenaImage = ImageIO.read(imageUrl);
                competitionPanel.repaint(); // Refresh the panel to show the arena image
            } else {
                throw new IOException("Arena image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image", "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startCompetition() {
        if (competitionRunning) {
            JOptionPane.showMessageDialog(this, "Competition already running.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (competitors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No competitors added.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        competitionRunning = true;
        infoArea.append("Competition started\n");

        // Create and start competitor threads
        for (Competitor competitor : competitors) {
            new Thread(competitor).start();
        }

        // Timer to update GUI every 30 milliseconds
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateArena();
            }
        });
        timer.start();
    }

    private void addCompetitor() {
        if (competitionRunning) {
            JOptionPane.showMessageDialog(this, "Cannot add competitors during the competition.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (competitors.size() >= maxCompetitors) {
            JOptionPane.showMessageDialog(this, "Maximum number of competitors reached.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = "Competitor " + (competitors.size() + 1); // Example name
        // Example competitor image path
        String imagePath = "/Competitor" + (competitors.size() + 1) + ".png";

        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                BufferedImage competitorImage = ImageIO.read(imageUrl);
                Image scaledImage = competitorImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Scale image as needed
                Competitor competitor = new Competitor(name, scaledImage);
                competitors.add(competitor);
                infoArea.append("Competitor added: " + name + "\n");
                competitionPanel.repaint(); // Refresh the panel to show the new competitor
            } else {
                throw new IOException("Competitor image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image", "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateArena() {
        // Update competitor positions and redraw the arena
        competitionPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SkiCompetitionGUI gui = new SkiCompetitionGUI();
            gui.setVisible(true);
        });
    }

    // Define Competitor as a Runnable and Observable
    class Competitor extends Observable implements Runnable {
        private String name;
        private Image image;
        private int position;

        public Competitor(String name, Image image) {
            this.name = name;
            this.image = image;
            this.position = 0; // Initial position
        }

        public Image getImage() {
            return image;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public void run() {
            while (position < arenaLength) {
                position += 5; // Move right
                setChanged();
                notifyObservers();
                try {
                    Thread.sleep(100); // Delay for competitor movement
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            competitionRunning = false;
            JOptionPane.showMessageDialog(SkiCompetitionGUI.this, name + " has finished the race!", "Race Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Define Competition as an Observer
    class Competition implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            // Update competition results based on competitor updates
        }
    }
}
