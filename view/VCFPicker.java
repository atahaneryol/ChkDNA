package chkdna.view;

import chkdna.ChkDNA;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VCFPicker extends JFrame {
    
    private final Object lock;
    private JTextField pathTextField;
    private JCheckBox snpeffTick;

    public VCFPicker(final Object lock) {
        super(ChkDNA.windowTitle);
        this.lock = lock;
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(new Dimension(500, 500));
        pathTextField = new JTextField();
        setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setResizable(false);
        
        //VCFPicker.enableOSXFullscreen(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addUpperPanel();
        
        add(createLowerPanel());
        pack();
        setVisible(true);
    }
    
    private void addUpperPanel() {
        Box upperPanel = new Box(BoxLayout.Y_AXIS);
        upperPanel.setPreferredSize(new Dimension(500,150));
        upperPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        upperPanel.add(Box.createVerticalGlue());
        DropZone dz = new DropZone(pathTextField);
        upperPanel.add(dz);
        upperPanel.add(Box.createVerticalGlue());
        add(upperPanel);
    }
    
    private JComponent createLowerPanel() {
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(createBrowsePanel(), BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
         JButton configButton = new JButton("Config");
        configButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigWindow cw = new ConfigWindow();
            }
        });
        
        JButton submitButton = new JButton("Submit");
        final VCFPicker instance = this;
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(pathTextField.getText());
                if(f.exists() && f.isFile()) {
                    
                    ChkDNA.getInstance().setVCFFile(f);
                    if(snpeffTick.getSelectedObjects() != null) ChkDNA.getInstance().setRunSnpEff();
                    synchronized(lock) {
                        lock.notify();
                    }
                    instance.dispose();
                }
            }
        });
        south.add(configButton);
        south.add(cancelButton);
        south.add(submitButton);
        lowerPanel.add(south,BorderLayout.SOUTH);
        
        return lowerPanel;
    }

    private JComponent createBrowsePanel() {
        
        JLabel label = new JLabel("VCF File Location:");
        final JButton browseButton = new JButton("Browse");
        snpeffTick = new JCheckBox("Run SnpEff?");
                //new JRadioButton("Run SnpEff?", false);
        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String path = pathTextField.getText();
                JFileChooser fc = new JFileChooser();
                if(!path.isEmpty())
                    fc.setCurrentDirectory(new File(path));
                int result = fc.showOpenDialog(browseButton);
                if(result == JFileChooser.APPROVE_OPTION) {
                    pathTextField.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        JPanel browsePanel = new JPanel();
        browsePanel.setPreferredSize(new Dimension(500, 100));
        //browsePanel.setBorder(new WindowsBorders.DashedBorder(Color.black));
        GroupLayout layout = new GroupLayout(browsePanel);
        browsePanel.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(label)
                        .addComponent(pathTextField)
                        .addComponent(snpeffTick))
                    .addComponent(browseButton));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(label)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(pathTextField,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(browseButton))
                    .addComponent(snpeffTick));
        
        return browsePanel;
    }
    
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException e1) {
        } catch (Exception e) {
            Logger.getLogger("HomeScreen").log(Level.WARNING, "OS X Fullscreen FAIL", e);
        }
    }

    public static void toggleFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method method = util.getMethod("requestToggleFullScreen", Window.class);
            method.invoke(util, window);
        } catch (Exception ex) {
            Logger.getLogger("HomeScreen").log(Level.WARNING, "OS X Fullscreen FAIL", ex);
        }
    }
}