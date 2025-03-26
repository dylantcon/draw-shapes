/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package drawshapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.border.Border;

public class ToolPanel extends JPanel implements ActionListener
{
  private final CanvasPanel delegatePanel;
  
  private final JRadioButton sketchButton;
  private final JRadioButton rectButton;
  private final JRadioButton ellipseButton;
  private final JRadioButton polyButton;
  
  private final ButtonGroup drawingModes;
  private final Icon colorD;
  private final Icon colorL;
  
  private final PaintWidthPanel pwP;
  private final CustomButton colorButton;
  private final JButton trashButton;
  private final JButton undoButton;
  
  private final AbstractButton[] buttons;
  
  ToolPanel( CanvasPanel delegatePanel )
  {
    this.delegatePanel = delegatePanel;
    this.setLayout( new GridLayout( 1, 9 ) );
    
    float initialStrokeWidth = delegatePanel.getStrokeWidth();
    int sliderInitValue = Math.round( initialStrokeWidth * 10 );
    
    Icon sketch = new ImageIcon( getClass().getResource( "/images/sketch.png" ) );
    Icon rect = new ImageIcon( getClass().getResource( "/images/rectangle.png" ) );
    Icon ellipse = new ImageIcon( getClass().getResource( "/images/circle.png" ) );
    Icon poly = new ImageIcon( getClass().getResource( "/images/polygon.png" ) );
    
    Icon sketchS = new ImageIcon( getClass().getResource( "/images/sketchsel.png" ) );
    Icon rectS = new ImageIcon( getClass().getResource( "/images/rectanglesel.png" ) );
    Icon ellipseS = new ImageIcon( getClass().getResource( "/images/circlesel.png" ) );
    Icon polyS = new ImageIcon( getClass().getResource( "/images/polygonsel.png" ) );
    
    colorL = new ImageIcon( getClass().getResource( "/images/color.png" ) );
    colorD = new ImageIcon( getClass().getResource( "/images/colorwt.png" ) );
    Icon trash = new ImageIcon( getClass().getResource( "/images/trash.png" ) );
    Icon undo = new ImageIcon( getClass().getResource( "/images/undo.png" ) );
    
    sketchButton = new JRadioButton( sketch );
    rectButton = new JRadioButton( rect );
    ellipseButton = new JRadioButton( ellipse );
    polyButton = new JRadioButton( poly );
    
    colorButton = new CustomButton( colorD, delegatePanel.getPointColor() );
    trashButton = new JButton( trash );
    undoButton = new JButton( undo );
    pwP = new PaintWidthPanel( sliderInitValue, 300, 10 );
    pwP.getSlider().addChangeListener( ( ChangeEvent e ) -> 
    {
      float sW = pwP.getSlider().getValue() / 10.0f;
      delegatePanel.setStrokeWidth( sW );
      pwP.repaint();
    });
    
    sketchButton.setSelectedIcon( sketchS );
    rectButton.setSelectedIcon( rectS );
    ellipseButton.setSelectedIcon( ellipseS );
    polyButton.setSelectedIcon( polyS );
    
    buttons = new AbstractButton[]
    {
      undoButton,     // 0
      trashButton,    // 1
      colorButton,    // 2
      sketchButton,   // 3
      rectButton,     // 4
      ellipseButton,  // 5
      polyButton      // 6
    };
    
    
    for ( int i = 3; i < buttons.length; i++ )
    {
      buttons[i].setHorizontalAlignment( SwingConstants.CENTER );
      buttons[i].setVerticalAlignment( SwingConstants.CENTER );
    }
    
    sketchButton.setToolTipText( "Free draw" );
    rectButton.setToolTipText( "Rectangle (drag mouse and release)" );
    ellipseButton.setToolTipText( "Ellipse (drag mouse and release)" );
    polyButton.setToolTipText( "Polygon (click to add points, click initial point to finalize)" );
    
    sketchButton.setSelectedIcon( sketchS );
    rectButton.setSelectedIcon( rectS );
    ellipseButton.setSelectedIcon( ellipseS );
    polyButton.setSelectedIcon( polyS );

    drawingModes = new ButtonGroup();
    for ( int i = 3; i < buttons.length; i++ )
      drawingModes.add( buttons[i] );
    sketchButton.setSelected( true );
    
    colorButton.setToolTipText( "Color palette" );
    trashButton.setToolTipText( "Erase all" );
    undoButton.setToolTipText( "Undo" );
    
    this.initButtonListeners();
    this.applyButtonLuminanceUpdate();
    
    this.add( undoButton );
    this.add( trashButton );
    this.add( colorButton );
    this.add( pwP );
    this.add( sketchButton );
    this.add( rectButton );
    this.add( ellipseButton );
    this.add( polyButton );
  }
  
  private void initButtonListeners()
  {
    for ( AbstractButton b : buttons )
      b.addActionListener( this );
  }
  
  private void applyButtonLuminanceUpdate()
  {
    colorButton.setBackgroundColor( delegatePanel.getPointColor() );
    
    float r = delegatePanel.getPointColor().getRed() / 255.0f;
    float g = delegatePanel.getPointColor().getGreen() / 255.0f;
    float b = delegatePanel.getPointColor().getBlue() / 255.0f;
    
    float luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b;
    
    if ( luminance >= 0.5f )
      colorButton.setIcon( colorL );
    else
      colorButton.setIcon( colorD );
    this.revalidate();
    this.repaint();
  }
  
  @Override
  public void actionPerformed( ActionEvent evt )
  {
    Object source = evt.getSource();
    
    if ( source == sketchButton )
      delegatePanel.setShapePointRenderType( PointType.DOT );
    else if ( source == rectButton )
      delegatePanel.setShapePointRenderType( PointType.RECTANGLE );
    else if ( source == ellipseButton )
      delegatePanel.setShapePointRenderType( PointType.ELLIPSE );
    else if ( source == polyButton )
      delegatePanel.setShapePointRenderType( PointType.POLYGON );
    else if ( source == colorButton )
    {
      Color slColor = JColorChooser.showDialog( this, "Choose a Color", 
                                                delegatePanel.getPointColor() );
      if ( slColor != null )
      {
        delegatePanel.setPointColor( slColor );
        this.applyButtonLuminanceUpdate();
      }
    }
    else if ( source == trashButton )
      delegatePanel.clearCanvas();
    else if ( source == undoButton )
      delegatePanel.undoLast();
  }
  
  class CustomButton extends JButton 
  {
    private Color backgroundColor;

    public CustomButton( Icon i, Color initialColor ) 
    {
        super();
        this.backgroundColor = initialColor;

        // preserve original design
        setOpaque( false );
        setContentAreaFilled( false );
        setFocusPainted( false );
        setBorderPainted( true );

        // apply icon choice
        setIcon( i );
    }

    public void setBackgroundColor( Color newColor ) 
    {
        this.backgroundColor = newColor;
        repaint(); // Repaint to reflect the new color
    }

    @Override
    protected void paintComponent( Graphics g ) 
    {
        if ( backgroundColor != null ) 
        {
            Graphics2D g2 = ( Graphics2D ) g.create();

            // enable anti-aliasing for smooth edges
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

            // draw the background color
            g2.setColor( backgroundColor );
            g2.fillRoundRect( 0, 0, getWidth(), getHeight(), 10, 10 ); // rounded corners

            g2.dispose();
        }
        super.paintComponent( g ); // Preserve text and other decorations
    }  
  }
   private class PaintWidthPanel extends JPanel implements ChangeListener
  {
    private final JSlider stroke;
    private final ResizableLabel strokeLabel;
    private int strokeWidth;
    
    public PaintWidthPanel( int min, int max, int initial )
    {
      this.setLayout( new BorderLayout() );
      
      // init the slider
      stroke = new JSlider( JSlider.HORIZONTAL, min, max, initial );
      stroke.setMajorTickSpacing( 5 );
      stroke.setMinorTickSpacing( 1 );
      stroke.setPaintTicks( true );
      stroke.setPaintLabels( true );
      stroke.setLabelTable( this.createLabelTable( max, min ) );
      
      strokeWidth = initial;
      strokeLabel = new ResizableLabel( "size: " + strokeWidth + " px", 
                                        JLabel.CENTER, this );
      Border lB = BorderFactory.createLineBorder( Color.GRAY, 1 );
      Border pB = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
      
      this.initSliderChangeListener();
      this.setBorder( BorderFactory.createCompoundBorder( lB, pB ) );
      this.setBackground( new Color( 214/255.0f, 214/255.0f, 214/255.0f ) );
      this.add( stroke, BorderLayout.NORTH );
      this.add( strokeLabel, BorderLayout.SOUTH );
    }
    
    private Hashtable< Integer, ResizableLabel > createLabelTable( int max, int min )
    {
      Hashtable< Integer, ResizableLabel > lTab; 
      lTab = new Hashtable< >();
      
      lTab.put( min, new ResizableLabel( Integer.toString( min / 10 ), JLabel.CENTER, this ) );
      lTab.put( ( min + max ) / 2, 
                new ResizableLabel( Integer.toString( max / 20 ), 
                JLabel.CENTER, this ) );
      lTab.put( max, new ResizableLabel( Integer.toString( max / 10 ), JLabel.CENTER, this ) );
      
      return lTab;
    }
    
    private void initSliderChangeListener()
    {
      stroke.addChangeListener( this );
    }
    
    
    // changelistener callback
    @Override
    public void stateChanged( ChangeEvent e )
    {
      strokeWidth = stroke.getValue() / 10;
      strokeLabel.setText( "size: " + strokeWidth + " px" );
    }
    
    public int getStrokeWidth()
    {
      return this.strokeWidth;
    }
    
    public JSlider getSlider()
    {
      return this.stroke;
    }
    
    public int getPWPHeight()
    {
      return this.getHeight();
    }
    
    public int getPWPWidth()
    {
      return this.getWidth();
    }
    
    class ResizableLabel extends JLabel
    {
      PaintWidthPanel pwp;
      
      public ResizableLabel( String txt, int align, PaintWidthPanel pwp )
      {
        super( txt, align );
        this.pwp = pwp;
      }
      
      @Override
      protected void paintComponent( Graphics g )
      {
        super.paintComponent( g );
        
        // adjust font size based on panel width and height
        int panW = pwp.getPWPWidth();
        int panH = pwp.getPWPHeight();
        
        // calculate font size based on smaller dimension
        int fontSz = Math.min( panW / 9, panH / 6 );
        setFont( new Font( Font.MONOSPACED, Font.BOLD, fontSz ) );
      }
    }
  }
  
  
}










































