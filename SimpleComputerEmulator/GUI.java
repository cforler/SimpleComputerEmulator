package SimpleComputerEmulator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.lang.Exception;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import javax.swing.filechooser.FileFilter;
   
/*****************************************************************/

public class GUI {
    private static String filedesc = "Assembler Textfile";
    
    private static void saveAction(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(filedesc, "txt"));
        
        int returnVal = chooser.showSaveDialog(parent);
        if(returnVal != JFileChooser.APPROVE_OPTION) return;
      
        
        String path = chooser.getCurrentDirectory() +
            File.separator + chooser.getSelectedFile().getName();

        if (filedesc == chooser.getFileFilter().getDescription() &&
            !path.endsWith(".txt")) path +=".txt";
        
        try {
            MasterPanel.getInstance().save(new FileOutputStream(path));
        } catch(FileNotFoundException fnfe) {
            System.out.println(fnfe);
        }
    }
    
    
    
    /*********************************************************************/

    private static void openAction(Component parent) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter =
            new FileNameExtensionFilter("Assembler Textfile", "txt");
        chooser.setFileFilter(filter);
        
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal != JFileChooser.APPROVE_OPTION) return;
        
        String path = chooser.getCurrentDirectory() +
            File.separator + chooser.getSelectedFile().getName();
        try {
            MasterPanel.getInstance().load(new FileInputStream(path));
        } catch(FileNotFoundException fnfe) {
            System.out.println(fnfe);
        }
    }

    
    
/*********************************************************************/
    
    private static JMenuBar genMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        JMenuItem  save = new JMenuItem("Save Program", KeyEvent.VK_S);
        menu.add(save);
        save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { saveAction(menu); }
            });
        
        
        JMenuItem  load = new JMenuItem("Load Program", KeyEvent.VK_L);
        menu.add(load);
        load.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { openAction(menu); }
            });


        
        return menuBar;
    }
    

   /********************************************************************/
    
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUIFont(new FontUIResource("System Regular",java.awt.Font.BOLD,15));

        frame.setJMenuBar(genMenuBar());
        frame.add(MasterPanel.getInstance().getPanel(), BorderLayout.CENTER);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }
    
    /*****************************************************************/
    
    public static void setUIFont (FontUIResource f){
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}
