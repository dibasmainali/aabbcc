package Final_Project;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import uk.ac.leedsbeckett.oop.LBUGraphics;

public class GraphicsSystem extends LBUGraphics {
    
    private static final Component MainFrame = null;
    private boolean fill;
    private ArrayList<String> list = new ArrayList<String>();
    
    public static void main(String[] args) {
        new GraphicsSystem();
    }
    
    public GraphicsSystem() {
        JFrame MainFrame = new JFrame();
        MainFrame.setLayout(new FlowLayout());
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.add(this);
        MainFrame.setSize(820, 445);
        
        // Set up menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem createItem = new JMenuItem("Create");

        createItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                String fileName = selectedFile.getName().toLowerCase();

                if (fileName.endsWith(".txt")) {
                    try (Scanner scanner = new Scanner(selectedFile)) {
                        while (scanner.hasNextLine()) {
                            String command = scanner.nextLine();
                            processCommand(command);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error reading file");
                    }
                } else if (fileName.endsWith(".png")) {
                    try {
                        BufferedImage image = ImageIO.read(selectedFile);
                        this.setBufferedImage(image);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading image");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Unsupported file type");
                }
            }
        });

        fileMenu.add(createItem);
        menuBar.add(fileMenu);
        MainFrame.setJMenuBar(menuBar);
        MainFrame.setVisible(true);
        
        // Convert OK button text to uppercase
        for (Component comp : this.getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) comp;
                if (btn.getText().equalsIgnoreCase("ok")) {
                    btn.setText("OK");
                }
            }
        }

        drawOn();
        displayMessage("I AM SACHET NEPAL");
    }
    
    @Override
    public void processCommand(String command) {
        try {
            if (command.contains(" ")) {
                String allCommand[] = command.split(" ");
                
                if (allCommand[0].equals("left") && Integer.parseInt(allCommand[1]) > 0) {
                    left(Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
                else if (allCommand[0].equalsIgnoreCase("flag") && allCommand.length == 2) {
                    drawFlag(allCommand[1]);
                    list.add(command);
                }
                else if (allCommand[0].equals("rignt") && Integer.parseInt(allCommand[1]) > 0) {
                    right(Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
                else if ((allCommand[0].equals("forward") || allCommand[0].equals("move")) && Integer.parseInt(allCommand[1]) > 0) {
                    forward(Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
                else if ((allCommand[0].equals("backward") || allCommand[0].equals("reverse")) && Integer.parseInt(allCommand[1]) > 0) {
                    forward(-Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
                else if (allCommand[0].equals("square") && Integer.parseInt(allCommand[1]) > 0) {
                    square(Integer.parseInt(allCommand[1]));
                    list.add(command);
                    forward(0);
                }
                else if (allCommand[0].equals("pencolor") && allCommand.length == 4 && 
                         Integer.parseInt(allCommand[1]) >= 0 && Integer.parseInt(allCommand[2]) >= 0 && 
                         Integer.parseInt(allCommand[3]) >= 0) {
                    if (Integer.parseInt(allCommand[1]) > 255 || Integer.parseInt(allCommand[2]) > 255 || 
                        Integer.parseInt(allCommand[3]) > 255) {
                        JOptionPane.showMessageDialog(null, "Color value range from 0 to 255!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        setPenColour(new Color(Integer.parseInt(allCommand[1]), Integer.parseInt(allCommand[2]), 
                                    Integer.parseInt(allCommand[3])));
                        list.add(command);
                    }
                }
                else if (allCommand[0].equals("penwidth") && Integer.parseInt(allCommand[1]) > 0) {
                    setStroke(Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
//                else if (allCommand[0].equals("triangle") && allCommand.length == 2 && Integer.parseInt(allCommand[1]) > 0) {
//                    triangle(Integer.parseInt(allCommand[1]));
//                    list.add(command);
//                    forward(0);
//                }
                else if (allCommand[0].equals("triangle") && allCommand.length == 4 && 
                         Integer.parseInt(allCommand[1]) > 0 && Integer.parseInt(allCommand[2]) > 0 && 
                         Integer.parseInt(allCommand[3]) > 0) {
                    triangle(Integer.parseInt(allCommand[1]), Integer.parseInt(allCommand[2]), 
                            Integer.parseInt(allCommand[3]));
                    list.add(command);
                    forward(0);
                }
                else if (allCommand[0].equals("circle") && Integer.parseInt(allCommand[1]) > 0) {
                    circle(Integer.parseInt(allCommand[1]));
                    list.add(command);
                }
                else if (allCommand[0].equals("rectangle") && Integer.parseInt(allCommand[1]) > 0 && 
                         Integer.parseInt(allCommand[2]) > 0) {
                    rectangle(Integer.parseInt(allCommand[1]), Integer.parseInt(allCommand[2]));
                    list.add(command);
                    forward(0);
                }
                else if (Integer.parseInt(allCommand[1]) < 0) {
                    JOptionPane.showMessageDialog(null, "Parameter Not Bounded!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, command + " is not a valid command", "Error", 
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                if (command.equals("left")) {
                    left(90);
                    list.add(command);
                }
                else if (command.equals("rignt")) {
                    right(90);
                    list.add(command);
                }
                else if (command.equals("black")) {
                    setPenColour(Color.black);
                    list.add(command);
                }
                else if (command.equals("green")) {
                    setPenColour(Color.green);
                    list.add(command);
                }
                else if (command.equals("red")) {
                    setPenColour(Color.red);
                    list.add(command);
                }
                else if (command.equals("white")) {
                    setPenColour(Color.white);
                    list.add(command);
                }
                else if (command.equals("drawOff")) {
                    drawOff();
                    list.add(command);
                }
                else if (command.equals("drawOn")) {
                    drawOn();
                    list.add(command);
                }
                else if (command.equals("about")) {
                    about();
                    list.add(command);
                }
                else if (command.equals("reset")) {
                    reset();
                    drawOn();
                    list.add(command);
                }
                else if (command.equals("clear")) {
                    clear();
                    list.add(command);
                }
                else if (command.equals("randomcolor")) {
                    randomColor();
                    list.add(command);
                }
                else if (command.equals("help")) {
                    help();
                    list.add(command);
                }
                else if (command.equals("save")) {
                    save();
                }
                else if (command.equals("load")) {
                    load();
                }
                else if (command.equals("forward") || command.equals("backward") || 
                         command.equals("square") || command.equals("pencolor") || 
                         command.equals("penwidth") || command.equals("circle") || 
                         command.equals("rectangle")) {
                    JOptionPane.showMessageDialog(null, "No Parameter Passed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, command + " is not a valid command!", "Error", 
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please, Provide Numerical Value!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fileloader() {
        int box = JOptionPane.showConfirmDialog(MainFrame, "Want to load file?");
        if (box == JOptionPane.YES_OPTION) {
            String confirm = JOptionPane.showInputDialog(MainFrame, "1. Load Image\n2. Load text commands");
            if (confirm.equals("1")) {
                loadPhoto();
            } else if (confirm.equals("2")) {
                loadInstruct();
            } else {
                JOptionPane.showMessageDialog(MainFrame, "Instruct Not Valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void load() {
        int warn = JOptionPane.showConfirmDialog(MainFrame, "Want to save this file first?");
        if (warn == JOptionPane.YES_OPTION) {
            save();
            fileloader();
        }
        else if (warn == JOptionPane.NO_OPTION) {
            fileloader();
        }
    }
    
    private void loadInstruct() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save As");
        int cfile = chooseFile.showOpenDialog(this);
        Path location = Paths.get(chooseFile.getSelectedFile().getAbsolutePath());
        if (cfile == JFileChooser.APPROVE_OPTION) {
            try {
                reset();
                drawOn();
                Scanner scan = new Scanner(location);
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    processCommand(line);
                }
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(MainFrame, "Error while loading Instruct", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadPhoto() {
        JFileChooser chooseFile = new JFileChooser();
        JLabel label = new JLabel();
        JFrame frame = new JFrame();
        chooseFile.setDialogTitle("Save as");
        int cfile = chooseFile.showOpenDialog(this);
        String location = chooseFile.getSelectedFile().getAbsolutePath();
        File file = new File(location);
        if (cfile == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIcon icon = new ImageIcon(location);
                label = new JLabel(icon);
                frame = new JFrame();
                frame.setTitle(file.getName());
                frame.setPreferredSize(new Dimension(800, 400));
                frame.setLayout(new FlowLayout());
                frame.getContentPane().add(label);
                frame.pack();
                frame.setVisible(true);
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(MainFrame, "Error while loading image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void save() {
        int dbox = JOptionPane.showConfirmDialog(MainFrame, "Want to save file?");
        if (dbox == JOptionPane.YES_OPTION) {
            String opt = JOptionPane.showInputDialog(MainFrame, "1. Save Photo\n2. Save text command:");
            int cmd = Integer.parseInt(opt);
            if (cmd == 1) {
                savePhoto();
            } else if (cmd == 2) {
                instructSave();
            } else {
                JOptionPane.showMessageDialog(MainFrame, "Instruct Not Valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void instructSave() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save as");
        int cmd = chooseFile.showSaveDialog(this);
        if (cmd == JFileChooser.APPROVE_OPTION) {
            Path location = Paths.get(chooseFile.getSelectedFile().getAbsolutePath());
            try {
                Files.write(location, list);
                JOptionPane.showMessageDialog(MainFrame, "All command saved.", "Success", JOptionPane.PLAIN_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(MainFrame, "Error in saving command.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void savePhoto() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save as");
        int img = chooseFile.showSaveDialog(this);
        if (img == JFileChooser.APPROVE_OPTION) {
            try {
                Thread.sleep(1000);
                String path = chooseFile.getSelectedFile().getAbsolutePath();
                File file = new File(path);
                ImageIO.write(getBufferedImage(), "png", file);
                JOptionPane.showMessageDialog(MainFrame, "Screenshot saved.", "Success", JOptionPane.PLAIN_MESSAGE);
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(MainFrame, "Error in saving Photo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rectangle(int b, int h) {
        Graphics canvas = getGraphicsContext();
        canvas.setColor(Color.orange);
        if (fill) {
            canvas.fillRect(xPos - b/2, yPos, b, h);
        } else {
            canvas.drawRect(xPos - b/2, yPos, b, h);
            forward(0);
        }
    }

//    private void triangle(int side) {
//        int halfside = side/2;
//        int z = (int) Math.round(Math.sqrt(Math.pow(side, 2) - Math.pow(halfside, 2));
//        
//        Graphics canvas = getGraphicsContext();
//        int[] xAxis = {xPos, xPos + halfside, xPos - halfside};
//        int[] yAxis = {yPos, yPos + z, yPos + z};
//        
//        if (fill) {
//            canvas.fillPolygon(xAxis, yAxis, 3);
//        } else {
//            canvas.drawPolygon(xAxis, yAxis, 3);
//        }
//    }
//    
    private void triangle(int sideA, int sideB, int sideC) {
        Graphics canvas = getGraphicsContext();
        int[] xAxis = {xPos, xPos + sideA, xPos - sideB};
        int[] yAxis = {yPos, yPos + sideC, yPos + sideA};
        
        if (fill) {
            canvas.fillPolygon(xAxis, yAxis, 3);
        } else {
            canvas.drawPolygon(xAxis, yAxis, 3);
        }
    }

    private void randomColor() {
        Random rgb = new Random();
        setPenColour(new Color(rgb.nextInt(256), rgb.nextInt(256), rgb.nextInt(256)));
    }

    private void square(int side) {
        Graphics canvas = getGraphicsContext();
        canvas.setColor(Color.cyan);
        
        if (fill) {
            canvas.fillRect(xPos - side/2, yPos, side, side);
        } else {
            canvas.drawRect(xPos - side/2, yPos, side, side);
        }
    }
    
    private void drawFlag(String country) {
        Graphics g = getGraphicsContext();
        clear();
        reset();
        
        switch (country.toLowerCase()) {
            case "nepal":
                g.setColor(Color.red);
                g.fillPolygon(new int[]{xPos, xPos + 50, xPos}, new int[]{yPos, yPos, yPos - 50}, 3);
                g.fillPolygon(new int[]{xPos, xPos + 50, xPos}, new int[]{yPos, yPos, yPos + 50}, 3);
                break;
                
            case "japan":
                g.setColor(Color.white);
                g.fillRect(xPos - 75, yPos - 50, 150, 100);
                g.setColor(Color.red);
                g.fillOval(xPos - 25, yPos - 25, 50, 50);
                break;
                
            case "france":
                g.setColor(Color.blue);
                g.fillRect(xPos - 75, yPos - 50, 50, 100);
                g.setColor(Color.white);
                g.fillRect(xPos - 25, yPos - 50, 50, 100);
                g.setColor(Color.red);
                g.fillRect(xPos + 25, yPos - 50, 50, 100);
                break;
                
            case "germany":
                g.setColor(Color.black);
                g.fillRect(xPos - 75, yPos - 50, 150, 33);
                g.setColor(Color.red);
                g.fillRect(xPos - 75, yPos - 17, 150, 33);
                g.setColor(Color.yellow);
                g.fillRect(xPos - 75, yPos + 16, 150, 33);
                break;
                
            default:
                JOptionPane.showMessageDialog(null, "Unsupported country: " + country);
        }
    }
    
    public void about() {
        super.about();
        getGraphicsContext().drawString("Sachet Nepal", 200, 300);
    }
    
    private void help() {
        String detail = "about: Display the turtle dance moving round and the name of the author\n" +
                       "drawOff: Lifts the pen from the canvas, so that movement does not get shown\n" +
                       "drawOn: Places the pen down on the canvas so movement gets shown as a drawn line\n" +
                       "black: Make the pen color black\n" +
                       "green: Makes the pen color green\n" +
                       "red: Makes the pen color red\n" +
                       "white: Makes the pen color white\n" +
                       "clear: Clears the whole output screen\n" +
                       "reset: Resets the canvas to its initial state with turtle pointing down but does not clear the display\n" +
                       "save: It provides options to save command or to save image\n" +
                       "load: It provides options to load command or to load image\n" +
                       "pencolor <int><int><int>: It takes three diferent color value to make RGB color\n" +
                       "penwidth: It takes one parameter and sets the pen stroke\n" +
                       "help: Display this menu!\n" +
                       "randomcolor: sets the color of the pen to random color\n" +
                       "\n" +
                       "DRAWINGS\n" +
                       "circle <radius>: It draws the circle of the radius user enters\n" +
                       "rectangle <BREADTH> AND <HEIGHT>: Draws a rectangle\n" +
                       "square <SIDE>: Draws a square with the same length of all sides\n" +
                       "triangle <int>: Equilateral triangle is drawn\n" +
                       "triangle <int><int><int>: Three parameter is passed it draws normal traingle\n" +
                       "\n" +
                       "LINES BY PASSING PARAMETERS\n" +
                       "forward <int>: Forwards the turtle by the units passed\n" +
                       "backward <int>: Trutle will move backwards by the units passed\n" +
                       "left <int>: Turn the turtle to right by degrees passed\n" +
                       "rignt <int>: Turn the turtle to left by degree passed\n";

        JOptionPane.showMessageDialog(null, detail);
    }
}