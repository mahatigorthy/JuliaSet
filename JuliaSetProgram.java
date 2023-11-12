import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

//make an undo button?
public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener, MouseListener {
    JFrame frame;
    Color color; 
    int red, green, blue; 
    JScrollBar ABar, BBar, redBar, blueBar, greenBar, brightnesScrollBar, saturationScrollBar, hueScrollBar, zoomScrollBar, multiScrollBar, eyeHueScrollBar, eyeBrightnessScrollBar; 
    JPanel scrollPanel, labelPanel, queenPanel, buttonPanel; 
    JLabel ALabel, BLabel, rLabel, gLabel, bLabel, brightnessLabel, saturationLabel, hueLabel, zoomLabel, multiLabel, eyeHueLabel, eyeBrightnessLabel;
    BufferedImage juliaImage;
    JFileChooser fileChooser; 
    JScrollPane juliaPane; 
    int maxIter=300;
    double a=0,b=0;
    float brightness = 0.0f , eyeBrightness = 0.0f; 
    float saturation; 
    float hue; 
    float eyeHue; 
    int multiplier; 
    JButton save, clear, undo; 
    Stack<ActionVariables> actions = new Stack<>(); 
    double zoom = 1.0;
    int pixelCount=1;
    public JuliaSetProgram() {
        frame = new JFrame("Julia Set Program");
        frame.setSize(1000, 850);
        
            //orinetation, initialValue, size of dodad
            //min value, max value

        save = new JButton("save");
        save.addActionListener(this);

        clear = new JButton("clear");
        clear.addActionListener(this); //stopped here 

        /*undo = new JButton("undo");
        undo.addActionListener(this);*/

        ABar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -6000, 6000);
        ABar.addAdjustmentListener(this);
        ABar.addMouseListener(this);

        BBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -1000, 1000);
        BBar.addAdjustmentListener(this);
        BBar.addMouseListener(this);

        brightnesScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 100);
        brightnesScrollBar.addAdjustmentListener(this);
        brightnesScrollBar.addMouseListener(this);

        eyeBrightnessScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 100);
        eyeBrightnessScrollBar.addAdjustmentListener(this);
        eyeBrightnessScrollBar.addMouseListener(this);

        saturationScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 100);
        saturationScrollBar.addAdjustmentListener(this);
        saturationScrollBar.addMouseListener(this);

        hueScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 360); 
        hueScrollBar.addAdjustmentListener(this);
        hueScrollBar.addMouseListener(this);

        eyeHueScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 360); 
        eyeHueScrollBar.addAdjustmentListener(this);
        eyeHueScrollBar.addMouseListener(this);

        zoomScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 50, 1000); 
        zoomScrollBar.addAdjustmentListener(this);         
        zoomScrollBar.addMouseListener(this);

        multiScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 2, 0, 1, 15); 
        multiScrollBar.addAdjustmentListener(this);         
        multiScrollBar.addMouseListener(this);

        String currDir=System.getProperty("user.dir");
        fileChooser=new JFileChooser(currDir);

        /*redBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255); 
        redBar.addAdjustmentListener(this);

        blueBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255); 
        blueBar.addAdjustmentListener(this);

        greenBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255); 
        greenBar.addAdjustmentListener(this);*/

        scrollPanel = new JPanel(); 
        scrollPanel.setLayout(new GridLayout(9, 1));
        scrollPanel.add(ABar); 
        scrollPanel.add(BBar);
        scrollPanel.add(brightnesScrollBar);
        scrollPanel.add(eyeBrightnessScrollBar); 
        scrollPanel.add(saturationScrollBar); 
        scrollPanel.add(hueScrollBar);
        scrollPanel.add(eyeHueScrollBar);  
        scrollPanel.add(zoomScrollBar); 
        scrollPanel.add(multiScrollBar); 
    
        /*scrollPanel.add(redBar);  
        scrollPanel.add(blueBar); 
        scrollPanel.add(greenBar); */        

        a = ABar.getValue()/1000.0f; 
        b = BBar.getValue()/1000.0f; 
        brightness = brightnesScrollBar.getValue()/100.0f; 
        eyeBrightness = eyeBrightnessScrollBar.getValue()/100.0f; 
        saturation = saturationScrollBar.getValue()/100.0f;   
        hue = hueScrollBar.getValue()/360.0f;  
        eyeHue = eyeHueScrollBar.getValue()/360.0f;  
        zoom = zoomScrollBar.getValue()/100.0f;
        multiplier = multiScrollBar.getValue();  
        /*red = redBar.getValue(); 
        blue = blueBar.getValue();
        green = greenBar.getValue();*/

        /*red = (int)(Math.random()*256); 
        green = (int)(Math.random()*256); 
        blue = (int)(Math.random()*256); */
        color = new Color(red, blue, green); 

        ALabel = new JLabel("A: "+a); 
        BLabel = new JLabel("B: "+b); 
        brightnessLabel = new JLabel("brightness: "+brightness); 
        eyeBrightnessLabel = new JLabel("eye brightness: "+eyeBrightness); 
        saturationLabel = new JLabel("saturation: "+saturation); 
        hueLabel = new JLabel("hue: "+hue); 
        eyeHueLabel = new JLabel("eye hue: "+eyeHue); 
        zoomLabel = new JLabel("zoom: "+zoom); 
        multiLabel = new JLabel("multiplier: "+multiplier); 

        /*rLabel = new JLabel("red: "+red); 
        bLabel = new JLabel("blue: "+blue); 
        gLabel = new JLabel("green: "+green); */

        labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(9, 1));
        labelPanel.setPreferredSize(new Dimension(105, 80));
        labelPanel.add(ALabel);
        labelPanel.add(BLabel);
        labelPanel.add(brightnessLabel); 
        labelPanel.add(eyeBrightnessLabel); 
        labelPanel.add(saturationLabel); 
        labelPanel.add(hueLabel); 
        labelPanel.add(eyeHueLabel); 
        labelPanel.add(zoomLabel); 
        labelPanel.add(multiLabel); 
        

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.setPreferredSize(new Dimension(90, 40));
        buttonPanel.add(save); 
        buttonPanel.add(clear); 
        //buttonPanel.add(undo); 
        /*labelPanel.add(rLabel);
        labelPanel.add(bLabel);
        labelPanel.add(gLabel); */

        queenPanel = new JPanel();
        queenPanel.setLayout(new BorderLayout());
        queenPanel.add(labelPanel, BorderLayout.WEST);
        queenPanel.add(scrollPanel, BorderLayout.CENTER);
        queenPanel.add(buttonPanel, BorderLayout.EAST); 

        frame.add(this);
        frame.add(queenPanel, BorderLayout.SOUTH); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setVisible(true);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        //g.setColor(color);        
        //g.fillRect(0, 0, frame.getWidth(), frame.getHeight()); 
        g.drawImage(drawJulia(), 0, 0, null); 
    }
    public static void main(String[]args) {
        JuliaSetProgram app = new JuliaSetProgram(); 
    }
    public BufferedImage drawJulia() {
        int w = frame.getWidth();
        int h = frame.getHeight()-105;

        juliaImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); 
        //my code
        for(int width = 0; width < w; width+=pixelCount) {
            for(int height = 0; height < h; height+=pixelCount) {
                
                int val = maxIter;
                double zx, zy; 
                zx = 1.5*((width-0.5*w)/((0.5)*zoom*w)); 
                zy = (height-(0.5*h))/(0.5*zoom*h); 
                
                while (zx*zx+zy*zy<6 && val>0 )
                {
                    double i = (zx*zx - zy*zy) + a;
                    zy = (multiplier*zx*zy) + b; 
                    zx = i; 
                    val--; 
                }
                int c;
                if(val>0)
                    c = Color.HSBtoRGB(hue + (float)(1.0*val/maxIter), saturation, brightness);
                else c = Color.HSBtoRGB(eyeHue + (float)(1.0*val/maxIter), saturation, eyeBrightness);
                juliaImage.setRGB(width,height,c);
            }      
        }

       

        return juliaImage; 
    }
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == ABar) {
            a = ABar.getValue()/1000.0f; 
            ALabel.setText("A: "+a);
        }
        if(e.getSource() == BBar) {
            b = BBar.getValue()/1000.0f; 
            BLabel.setText("B: "+b);
        }
        if(e.getSource() == brightnesScrollBar) {
            brightness = brightnesScrollBar.getValue()/100.0f;
            brightnessLabel.setText("brightness: "+brightness);
        }
        if(e.getSource() == eyeBrightnessScrollBar) {
            eyeBrightness = eyeBrightnessScrollBar.getValue()/100.0f;
            eyeBrightnessLabel.setText("eye brightness: "+eyeBrightness);
        }
        if(e.getSource() == saturationScrollBar) {
            saturation = saturationScrollBar.getValue()/100.0f;
            saturationLabel.setText("saturation: "+saturation);
        }
        if(e.getSource() == hueScrollBar) {
            hue = hueScrollBar.getValue()/360.0f;
            hueLabel.setText("hue: "+hue);
        }
        if(e.getSource() == eyeHueScrollBar) {
            eyeHue = eyeHueScrollBar.getValue()/360.0f;
            eyeHueLabel.setText("eye hue: "+hue);
        }
        if(e.getSource() == zoomScrollBar) {
            zoom = zoomScrollBar.getValue()/100.0f;
            zoomLabel.setText("zoom: "+zoom);
        }
        if(e.getSource() == multiScrollBar) {
            multiplier = multiScrollBar.getValue(); 
            multiLabel.setText("multiplier: "+multiplier); 
        }
        /*if(e.getSource() == BBar) {
            red = redBar.getValue(); 
            rLabel.setText("red: "+red);
        }
        if(e.getSource() == blueBar) {
            blue = blueBar.getValue(); 
            bLabel.setText("blue: "+blue);
        }
        if(e.getSource() == greenBar) {
            green = greenBar.getValue(); 
            gLabel.setText("green: "+green);
        }*/
        color = new Color(red, green, blue); 
        repaint(); 
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == save) {
            saveImage();
        }
        if(e.getSource() == clear) {
            ABar.setValue(0);
            a = ABar.getValue()/1000.0f; 
            ALabel.setText("A: "+a); 

            BBar.setValue(0);
            b = BBar.getValue()/1000.0f; 
            BLabel.setText("B: "+b);

            brightnesScrollBar.setValue(100);
            brightness = brightnesScrollBar.getValue()/100.0f;
            brightnessLabel.setText("brightness: "+brightness);

            eyeBrightnessScrollBar.setValue(0);
            eyeBrightness = eyeBrightnessScrollBar.getValue()/100.0f;
            eyeBrightnessLabel.setText("eye brightness: "+eyeBrightness);


            saturationScrollBar.setValue(100);
            saturation = saturationScrollBar.getValue()/100.0f;
            saturationLabel.setText("saturation: "+saturation);

            hueScrollBar.setValue(0);
            hue = hueScrollBar.getValue()/360.0f;
            hueLabel.setText("hue: "+hue);

            eyeHueScrollBar.setValue(0);
            eyeHue = eyeHueScrollBar.getValue()/360.0f;
            eyeHueLabel.setText("eye hue: "+eyeHue);

            zoomScrollBar.setValue(100);
            zoom = zoomScrollBar.getValue()/100.0f;
            zoomLabel.setText("zoom: "+zoom);

            multiScrollBar.setValue(2);
            multiplier = multiScrollBar.getValue();
            multiLabel.setText("multiplier: "+multiplier);

            color = new Color(red, green, blue); 
            repaint(); 
        }
        /*if(e.getSource() == undo) {
            System.out.println("here"); 

            ActionVariables var = actions.pop(); 

            ABar.setValue((int) var.getA());
            a = ABar.getValue()/1000.0f; 
            ALabel.setText("A: "+a); 

            BBar.setValue((int) var.getB());
            b = BBar.getValue()/1000.0f; 
            BLabel.setText("B: "+b);

            brightnesScrollBar.setValue((int) var.getBrightness());
            brightness = brightnesScrollBar.getValue()/100.0f;
            brightnessLabel.setText("brightness: "+brightness);

            saturationScrollBar.setValue((int) var.getSaturation());
            saturation = saturationScrollBar.getValue()/100.0f;
            saturationLabel.setText("saturation: "+saturation);

            hueScrollBar.setValue((int) var.getHue());
            hue = hueScrollBar.getValue()/100.0f;
            hueLabel.setText("hue: "+hue);

            zoomScrollBar.setValue((int) var.getZoom());
            zoom = zoomScrollBar.getValue()/100.0f;
            zoomLabel.setText("zoom: "+zoom);

            color = new Color(red, green, blue); 
            repaint(); 
        }*/

    }

    public void saveImage()
    {
        if(juliaImage!=null) //juliaImage is the BufferedImage I declared globally (and used in
        //the drawJulia method)
        {
            FileFilter filter=new FileNameExtensionFilter("*.png","png");
            fileChooser.setFileFilter(filter);
            if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            {
                File file=fileChooser.getSelectedFile();
                try
                {
                    String st=file.getAbsolutePath();
                    if(st.indexOf(".png")>=0)
                        st=st.substring(0,st.length()-4);
                    ImageIO.write(juliaImage,"png",new File(st+".png"));
                }catch(IOException e)
                {
                }

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub       
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub    
        pixelCount=3;
        repaint();  
    }    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub       
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub      
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub    
        //System.out.println(" "+e.getValue()); 
        /*actions.push(new ActionVariables(ABar.getValue(), BBar.getValue(), zoomScrollBar.getValue(), brightnesScrollBar.getValue(), saturationScrollBar.getValue(), hueScrollBar.getValue()));
        System.out.println(actions);  */ 
        pixelCount=1;
    }


    class ActionVariables {
        double a, b, zoom; 
        float brightness, saturation, hue; 

        public ActionVariables(double a, double b, double zoom, float brightness, float saturation, float hue) {
            this.a = a;
            this.b = b;
            this.zoom = zoom; 
            this.brightness = brightness;
            this.saturation = saturation; 
            this.hue = hue; 
        }

        public double getA() {
            return a; 
        }
        public double getB() {
            return b; 
        }
        public double getZoom() {
            return zoom; 
        }
        public float getBrightness() {
            return brightness; 
        }
        public float getSaturation() {
            return saturation; 
        }
        public float getHue() {
            return hue; 
        }
    }

    
}


