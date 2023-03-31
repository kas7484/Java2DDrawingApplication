/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{
    
    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel panel;
    // create the widgets for the firstLine Panel.
    private JButton undo;
    private JButton clear;
    private JLabel shape;
    private final JComboBox<String> dropdown;
    private JButton color1;
    private JButton color2;
    //create the widgets for the secondLine Panel.
    
    private final JCheckBox cb_filled;
    private final JCheckBox cb_gradient;
    private final JCheckBox cb_dashed;
    private JLabel Lwidth;
    private JLabel Dwidth;
    private JLabel options;
    private JSpinner lineWidth;
    private JSpinner dashLength;
    // Variables for drawPanel.
    private final JPanel drawPanel;
    private Color first_color;
    private Color second_color;
    ArrayList<MyShapes> shape_list = new ArrayList<MyShapes>();
    private Point start;
    private Point end;
    private Paint paint;
    private Stroke stroke;
    private MyShapes current;
    // add status label
    private final JPanel statusPanel;
    private JLabel status;


    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {   
        
        super("Java 2D Drawings");
        // add widgets to panels
        setLayout(new BorderLayout());
        // firstLine widgets
        panel1 = new JPanel();
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        shape = new JLabel("Shape: ");
        dropdown = new JComboBox<>(new String[] {"Line","Rectangle","Oval"});
        color1 = new JButton("1st Color...");
        color2 = new JButton("2nd Color...");  
        dropdown.setVisible(true);
        panel1.add(shape);
        panel1.add(dropdown);
        panel1.add(color1);
        panel1.add(color2);        
        panel1.add(undo);
        panel1.add(clear);
        
        // secondLine widgets
        panel2 = new JPanel();
        cb_filled = new JCheckBox("Filled");
        cb_gradient = new JCheckBox("Gradient");
        cb_dashed = new JCheckBox("Dashed");
        Lwidth = new JLabel("Line width:");
        Dwidth = new JLabel("Dash Length:");
        lineWidth = new JSpinner();
        dashLength = new JSpinner();
        options = new JLabel("Options: ");
        
        panel2.setLayout(new FlowLayout());
        panel2.add(options);
        panel2.add(cb_filled);
        panel2.add(cb_gradient);
        panel2.add(cb_dashed);
        panel2.add(Lwidth);
        panel2.add(lineWidth);
        panel2.add(Dwidth);
        panel2.add(dashLength);
        
        
        // add top panel of two panels
        panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        Color verylightblue = new Color(51,204,255);
        panel1.setBackground(verylightblue);
        panel2.setBackground(verylightblue);
        first_color = Color.black;
        second_color = Color.black;
        
        panel.add(panel1);
        panel.add(panel2);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(panel, BorderLayout.NORTH);
        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.white);
        add(drawPanel, BorderLayout.CENTER);
        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        
        status = new JLabel("(x,y)");
        statusPanel.add(status);
        add(statusPanel, BorderLayout.SOUTH);
        JColorChooser ch = new JColorChooser();
        //add listeners and event handlers
        
        ButtonHandler handle = new ButtonHandler();
        undo.addActionListener(handle);
        clear.addActionListener(handle);
        color1.addActionListener(handle);
        color2.addActionListener(handle);
        
        
        
    }

    // Create event handlers, if needed
    
    private class ButtonHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == undo){
                // add action for undo
                shape_list.remove(shape_list.size()-1);
                repaint();
            }
            else if (e.getSource() == clear){
                // add action for clear
                shape_list = new ArrayList<MyShapes>();
                repaint();
            } 
            else if (e.getSource() == color1){
                // add action for color1
                first_color = JColorChooser.showDialog(color1,"Select Color 1 ",Color.black);
                
            }  
            else if (e.getSource() == color2){
                // add action for color2
                second_color = JColorChooser.showDialog(color2,"Select Color 2",Color.black);
            }
        }
    }

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
       
        
        public DrawPanel()
        {
            MouseHandler mouse = new MouseHandler();
            addMouseListener(mouse);
            addMouseMotionListener(mouse);

            
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (int i = 0; i <shape_list.size(); i++ ){
                shape_list.get(i).draw(g2d);
            }

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            @Override
            public void mousePressed(MouseEvent event)
                    
            {  
                float [] dashes = {(int)dashLength.getValue()}; 
                if ((int)dashLength.getValue() == 0) {
                    
                }
                
                start = event.getPoint();
                end = event.getPoint();
                if (cb_gradient.isSelected()){
                    paint = new GradientPaint(0, 0, first_color, 50, 50, second_color, true);
                }
                else{
                    paint = first_color;
                }
                if (cb_dashed.isSelected())
                    {   
                    stroke = new BasicStroke((int)lineWidth.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
                } else
                    {
                    stroke = new BasicStroke((int)lineWidth.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                
                if (dropdown.getSelectedItem() == "Rectangle"){
                    MyRectangle current = new MyRectangle(start, end, paint, stroke,cb_filled.isSelected());
                    shape_list.add(current);                    
                    
                }
                else if (dropdown.getSelectedItem() == "Oval"){
                    MyOval current = new MyOval(start, end, paint, stroke,cb_filled.isSelected());
                    shape_list.add(current);
                    
                }
                else if (dropdown.getSelectedItem() == "Line"){
                    //add code for rectangle
                    MyLine current = new MyLine(start, end, paint, stroke);
                    shape_list.add(current);
                    
                }
                

            }
            public void mouseReleased(MouseEvent event)
            {
                end = event.getPoint();
                status.setText(String.format("(%d,%d)",event.getX(),event.getY()));
                shape_list.get(shape_list.size()-1).setEndPoint(end);
                repaint();  
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                end = event.getPoint();
                status.setText(String.format("(%d,%d)",event.getX(),event.getY()));
                shape_list.get(shape_list.size()-1).setEndPoint(end);
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                status.setText(String.format("(%d,%d)",event.getX(),event.getY()));   
            }
        }

    }
}
