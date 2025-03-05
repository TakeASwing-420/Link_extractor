import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.*;
import java.util.ArrayList;

public class Link extends JFrame {
    final private JTextArea textArea;
    final private JButton browseButton;
    final private JFileChooser fileChooser;

    public Link() {
        setTitle("HTML Link Extractor");
        setSize(500, 400); //Initial Size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        browseButton = new JButton("Select HTML File");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAndProcessFile();
            }
        });

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html", "htm"));

        add(browseButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void selectAndProcessFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            processHTMLFile(selectedFile);
        }
    }

    private void processHTMLFile(File file) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            ArrayList<String> links = extractLinks(content);
            displayLinks(links);
        } catch (IOException e) {
            textArea.setText("Error reading file: " + e.getMessage());
        }
    }

    private ArrayList<String> extractLinks(String htmlContent) {
        ArrayList<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?i)<a[^>]+href\\s*=\\s*['\"](https?://[^'\"]+)['\"]");
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            String link = matcher.group(1);
            if (link != null && (link.contains("amazon.com") || link.contains("openai.com") || link.contains("google.com"))) {
                links.add(link);
            }
        }
        return links;
    }

    private void displayLinks(ArrayList<String> links) {
        if (links.isEmpty()) {
            textArea.setText("No matching links found.");
        } else {
            textArea.setText("Extracted Links:\n");
            for (String link : links) {
                textArea.append(link + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Link extractor = new Link();
            extractor.setVisible(true);
        });
    }
}
