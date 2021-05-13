package ece465;
import ece465.service.Json.readJson;
import ece465.service.Json.searchJsonWriter;
import ece465.service.Json.storerequestWriter;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class client_gui extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JButton shareButton, searchButton;
    JTextArea log;
    JFileChooser fc;
    ArrayList<String> listing;
    ece465.node.client c= null;

    public client_gui() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser("./");

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setMultiSelectionEnabled(true);
        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        shareButton = new JButton("Share File",
                createImageIcon("share.png"));
        shareButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        searchButton = new JButton("Search File",
                createImageIcon("images/Save16.gif"));
        searchButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(shareButton);
        buttonPanel.add(searchButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        listing=new ArrayList<>();
        c = new ece465.node.client();
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == shareButton) {
            int returnVal = fc.showOpenDialog(client_gui.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] file = fc.getSelectedFiles();
                listing.clear();
                //This is where a real application would open the file.
                for (int i = 0; i < file.length;i++){
                    //log.append("Opening: " + file[i].getName() + "." + newline);
                    log.append("Adding file to database: " + file[i].getAbsolutePath() + "." + newline);
                    listing.add(file[i].getAbsolutePath());
                    try {
                        c.send(new Socket("0.0.0.0", 4567), storerequestWriter.generateJson(listing));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } else {
                log.setText(null);
                log.append("Share command cancelled by user." + newline);
                return;
            }
            log.setCaretPosition(log.getDocument().getLength());

            //Handle save button action.
        } else if (e.getSource() == searchButton) {
            String searchword= JOptionPane.showInputDialog("Please Enter Search Key: ");
            if(searchword!=null){
                System.out.println(searchword);
                try {
                    String search_result = c.send(new Socket("0.0.0.0", 4567), searchJsonWriter.generateJson(searchword));
                    System.out.println("search result: " + search_result);
                    ArrayList<readJson.returnInfo> returned = readJson.read(search_result);
                    JTextArea ids=new JTextArea(10,15);
                    tag:
                    for (int i = 0; i < returned.size(); i++) {
                        //System.out.println((i + 1) + " - " + returned.get(i).filename);
                        ids.append((i + 1) + " - " + returned.get(i).filename + newline);
                    }
                    JOptionPane.showMessageDialog(null, "Pleas select the index of files you want to retrieve, separated by <,>.\nUse <x-y> to indicate range of indices");

                    ids.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(ids);
                    String index=JOptionPane.showInputDialog(scrollPane);


                    if(index!=null&&index.length()!=0){

                        String[] parts=index.split(",");
                        String[] temp;
                        ArrayList<readJson.returnInfo> fetching = new ArrayList<>();
                        for(int i =0;i <parts.length;i++){
                            temp=parts[i].split("-");
                            if(temp.length>2){
                                JOptionPane.showMessageDialog(null,"Indices format error");
                                log.setText(null);
                                log.append("Fetch command cancelled by user." + newline);
                                return;
                            }
                            else if(temp.length>1){
                                int begin,end;
                                begin=Integer.parseInt(temp[0]);
                                end=Integer.parseInt(temp[1]);
                                for(int k =begin;k<=end;k++){
                                    fetching.add(returned.get(k - 1));
                                }
                            }
                            else{
                                fetching.add(returned.get(Integer.parseInt(temp[0]) - 1));
                            }
                        }
                        c.receive(fetching, 0);
                    }
                    else{
                        log.setText(null);
                        log.append("Fetch command cancelled by user." + newline);
                        return;
                    }

                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                log.setText(null);
                log.append("Search command cancelled by user." + newline);
                return;
            }

        }
        log.setText(null);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = client_gui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame= new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new client_gui());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}