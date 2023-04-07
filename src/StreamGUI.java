import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import static java.nio.file.StandardOpenOption.CREATE;

public class StreamGUI extends JFrame {
    Vector<String> lines = new Vector<>();
    JFrame frame;

    JPanel middleSection;
    JTextArea original;
    JScrollPane scroll1;
    JTextArea filtered;
    JScrollPane scroll2;
    JTextField searchField;

    JPanel bottomSection;
    JButton load;
    JButton quit;
    JButton compileSearch;

    public StreamGUI() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        frame = new JFrame();
        frame.setLayout(null);

        //Finds the users screen height and width
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        //Centers the frame in the middle of the user's screen
        frame.setSize(screenWidth / 2, screenHeight / 2);
        frame.setLocation(screenWidth / 4, screenHeight / 4);

        textLocation();
        middleSection.setBounds(20,10,frame.getWidth() - 40,280);
        frame.add(middleSection);

        buttons();
        bottomSection.setBounds((frame.getWidth() - 270) / 2,frame.getHeight() - 100,250,35);
        frame.add(bottomSection);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void textLocation() {
        middleSection = new JPanel();

        searchField = new JTextField(50);
        searchField.setEnabled(false);
        middleSection.add(searchField);

        original = new JTextArea("Original File text goes here.",15, 21);
        original.setEditable(false);
        scroll1 = new JScrollPane(original);
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        middleSection.add(scroll1);

        filtered = new JTextArea("Filtered Text will Go here.",15,21);
        filtered.setEditable(false);
        scroll2 = new JScrollPane(filtered);
        scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        middleSection.add(scroll2);
    }

    private void buttons() {
        bottomSection = new JPanel();

        load = new JButton("Load File");
        load.addActionListener((ActionEvent ae) -> fileLoading(original, searchField, compileSearch));
        load.setFocusable(false);
        bottomSection.add(load);

        compileSearch = new JButton("Search");
        compileSearch.addActionListener((ActionEvent ae) -> searchResults(searchField, filtered, lines));
        compileSearch.setFocusable(false);
        compileSearch.setEnabled(false);
        bottomSection.add(compileSearch);

        quit = new JButton("Quit");
        quit.addActionListener((ActionEvent ae) -> System.exit(0));
        quit.setFocusable(false);
        bottomSection.add(quit);
    }

    private void fileLoading(JTextArea original, JTextField searchField, JButton compileSearch) {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;

        try {
            //Creates and sets a directory
            File workingDirectory= new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            //If they selected a file, it will run the code
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                //Gets the file path
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                //Weird code for reading files
                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));

                //Empties text from box
                original.setText("File Text:\n\n");

                //While there is still a line to read
                while (reader.ready()) {
                    String rec = reader.readLine();
                    lines.add(rec);
                    //If a line is too long, it will go out of the box
                    original.append(rec + "\n");
                }

                searchField.setEnabled(true);
                searchField.setText("Search for a string in the file.");
                compileSearch.setEnabled(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchResults(JTextField searchField, JTextArea filtered, Vector<String> lines) {
        String strSearch = searchField.getText();

        filtered.setText("What was found:\n\n");

        for (String line : lines) {
            if (line.contains(strSearch)) {
                filtered.append(line + "\n");
            }
        }
    }
}
