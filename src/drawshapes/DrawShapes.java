/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package drawshapes;
import java.util.LinkedList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DrawShapes
{
  final static int WINX = 640;
  final static int WINY = 480;
  
  public static void main( String[] args ) 
  {
    JFrame drawshapes = new JFrame( "Draw Shapes" );
    drawshapes.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    drawshapes.setSize( WINX, WINY );
    JPanel wrapper = new JPanel();
    wrapper.setLayout( new BorderLayout() );
    
    CanvasPanel canvasP = new CanvasPanel();
    ToolPanel toolP = new ToolPanel( canvasP );
    
    wrapper.add( canvasP, BorderLayout.CENTER );
    wrapper.add( toolP, BorderLayout.NORTH );    
    drawshapes.add( wrapper );
    
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu( "File" );
    JMenuItem saveItem = new JMenuItem( "Save" );
    JMenuItem loadItem = new JMenuItem( "Load" );
    
    // add menu items to the file menu
    fileMenu.add( loadItem );
    fileMenu.add( saveItem );
    menuBar.add( fileMenu );
    drawshapes.setJMenuBar( menuBar );
    
    // Add action listeners for save and load
    saveItem.addActionListener( ( var e ) -> 
    {
      JFileChooser fileChooser = new JFileChooser();
      if ( fileChooser.showSaveDialog( drawshapes ) == JFileChooser.APPROVE_OPTION ) 
      {
        File file = fileChooser.getSelectedFile();
        if ( !file.getName().toLowerCase().endsWith( ".dat" ) );
          file = new File( file.getAbsolutePath() + ".dat" );
        canvasP.saveToFile( file );
      }
    });

    loadItem.addActionListener( ( ActionEvent e ) -> 
    {
      JFileChooser fileChooser = new JFileChooser();
      if ( fileChooser.showOpenDialog( drawshapes ) == JFileChooser.APPROVE_OPTION ) 
      {
        File file = fileChooser.getSelectedFile();
        canvasP.loadFromFile( file );
      }
    });
    
    drawshapes.setVisible( true );
  }
}

enum PointType { DOT, ELLIPSE, RECTANGLE, POLYGON };

class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener
{
  private final ArrayList< DrawNode > placedCoordinates;
  private final ArrayList< DrawNode > activeCoordinates;
          
  private final Stack< DrawSeqElement > actionSequence;
  private Integer dotIncrement;
  private float strokeWidth;
          
  private Color pointColor;
  private PointType shapePointRenderType;
  
  public CanvasPanel()
  {
    this.placedCoordinates = new ArrayList< >();
    this.activeCoordinates = new ArrayList< >();
    this.dotIncrement = 0;
    this.strokeWidth = 1.0f;
    this.actionSequence = new Stack< >();
    this.pointColor = Color.BLACK;
    this.shapePointRenderType = PointType.DOT;
    this.initMouseListeners();
  }
  
  // saves to specified location
  public void saveToFile( File file )
  {
    try ( ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( file ) ) )
    {
      oos.writeObject( placedCoordinates );
      oos.writeObject( actionSequence );
    }
    catch ( IOException e )
    {
      JOptionPane.showMessageDialog( this, "Error saving file: " + e.getMessage(),
                                    "Save Error", JOptionPane.ERROR_MESSAGE );
    }
  }
  
  // load state from a file
  @SuppressWarnings( "unchecked" )
  public void loadFromFile( File file ) 
  {
      try ( ObjectInputStream ois = new ObjectInputStream( new FileInputStream( file ) ) ) 
      {
          placedCoordinates.clear();
          actionSequence.clear();

          placedCoordinates.addAll( ( ArrayList< DrawNode > ) ois.readObject() );
          actionSequence.addAll( (Stack< DrawSeqElement > ) ois.readObject() );

          repaint();
      } 
      catch ( IOException | ClassNotFoundException e ) 
      {
          JOptionPane.showMessageDialog( this, "Error loading file: " + e.getMessage(),
                  "Load Error", JOptionPane.ERROR_MESSAGE );
      }
  }
  
  private void initMouseListeners()
  {
    this.addMouseListener( this );
    this.addMouseMotionListener( this );
  }
  
  protected Color getPointColor()
  {
    return this.pointColor;
  }
  
  protected void setPointColor( Color c )
  {
    this.pointColor = c;
  }
  
  protected void setShapePointRenderType( PointType pT )
  {
    this.shapePointRenderType = pT;
  }
  
  protected void setStrokeWidth( float width )
  {
    this.strokeWidth = width;
  }
  
  protected float getStrokeWidth()
  {
    return this.strokeWidth;
  }
  
  protected void clearCanvas()
  {
    this.placedCoordinates.clear();
    this.activeCoordinates.clear();
    this.actionSequence.clear();
    this.repaint();
  }
  
  protected void undoLast()
  {
    if ( !actionSequence.isEmpty() )
    {
      int pointsToRemove = actionSequence.pop().getPointCounts();
      for ( int i = 0; i < pointsToRemove; i++ )
        placedCoordinates.remove( placedCoordinates.size() - 1 );
      this.repaint();
    }
  }
  
  private DrawNode constructNode( Point pt )
  {
    return new DrawNode( pointColor, pt, shapePointRenderType );
  }
  
  private boolean isBoxDelimited( PointType pT )
  {
    return ( pT == PointType.ELLIPSE || pT == PointType.RECTANGLE );
  }
  
  @Override
  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    Graphics2D g2 = (Graphics2D) g;
    LinkedList< DrawNode > queue = new LinkedList<>( placedCoordinates );
    Iterator< DrawSeqElement > actionIterator = actionSequence.iterator();
    
    while ( !queue.isEmpty() && actionIterator.hasNext() )
    {
      DrawNode csp = queue.poll();
      g2.setColor( csp.getColor() );
      this.showGeom( queue, actionIterator, csp, g2 );
    }
    if ( !activeCoordinates.isEmpty() )
    {
      g2.setColor( pointColor );
      switch ( shapePointRenderType )
      {
        case RECTANGLE -> paintRectanglePreview( g2 );
        case ELLIPSE -> paintEllipsePreview( g2 );
        case POLYGON -> paintPolygonPreview( g2 );
      }
    }
  }
  
  private void showGeom( LinkedList<DrawNode> queue, Iterator<DrawSeqElement> i, 
  /* * * * * * * * * */  DrawNode pt, Graphics2D g2 )
  {
    Point p1 = pt.getPoint();
    Shape s;
    
    switch ( pt.getPointType() )
    {
      case ELLIPSE, RECTANGLE -> 
      {
        Point p2 = queue.poll().getPoint();
        int[] loc = parsePoints( p1, p2 );
        
        if ( pt.getPointType() == PointType.ELLIPSE )
          s = new Ellipse2D.Double( loc[0], loc[1], loc[2], loc[3] );
        else
          s = new Rectangle2D.Double( loc[0], loc[1], loc[2], loc[3] );
        g2.setStroke( new BasicStroke( i.next().getStrokeWidth() ) );
      }
      case DOT, POLYGON -> 
      {
        Path2D.Double path = new Path2D.Double();
        path.moveTo( p1.x, p1.y );
        DrawSeqElement e = i.next();
        int nPts = e.getPointCounts();
        g2.setStroke( new BasicStroke( e.getStrokeWidth() ) );
        
        if ( pt.getPointType() == PointType.DOT )
          path.lineTo( p1.x + 0.01, p1.y + 0.01 );
        
        for ( int n = 1; n < nPts; n++ )
        {
          DrawNode nodeN = queue.poll();
          Point p2 = nodeN.getPoint();
          path.lineTo( p2.x, p2.y );
        }
        if ( pt.getPointType() == PointType.POLYGON )
          path.lineTo( p1.x, p1.y );
        
        s = path;
      }
      default ->
      {
        throw new IllegalArgumentException( "Unknown: " + pt.getPointType() );
      }
    }
    g2.draw( s );
  }
  
  private void paintRectanglePreview( Graphics2D g2d )
  {
    if ( activeCoordinates.size() == 2 ) 
    {
      Point p1 = activeCoordinates.get( 0 ).getPoint();
      Point p2 = activeCoordinates.get( 1 ).getPoint();
      int[] loc = parsePoints( p1, p2 );
      g2d.setStroke( new BasicStroke( strokeWidth ) );
      Color c = this.pointColor;
      c = new Color( c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f );
      g2d.setColor( c );
      g2d.draw( new Rectangle( loc[0], loc[1], loc[2], loc[3] ) );
    }
  }
  
  private void paintEllipsePreview( Graphics2D g2d )
  {
    if ( activeCoordinates.size() == 2 ) 
    {
      Point p1 = activeCoordinates.get( 0 ).getPoint();
      Point p2 = activeCoordinates.get( 1 ).getPoint();
      int[] loc = parsePoints( p1, p2 );
      g2d.setStroke( new BasicStroke( strokeWidth ) );
      Color c = this.pointColor;
      c = new Color( c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f );
      g2d.setColor( c );
      g2d.draw( new Ellipse2D.Double(  loc[0], loc[1], loc[2], loc[3] ) );
    }
  }
  
  private void paintPolygonPreview( Graphics2D g2d )
  {
    int[] xPoints = activeCoordinates.stream().mapToInt( csp -> csp.getPoint().x ).toArray();
    int[] yPoints = activeCoordinates.stream().mapToInt( csp -> csp.getPoint().y ).toArray();
    g2d.setStroke( new BasicStroke( strokeWidth ) );
    Color c = this.pointColor;
    c = new Color( c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f );
    g2d.setColor( c );
    g2d.draw( new Polygon( xPoints, yPoints, activeCoordinates.size() ) );
  }
  
  private boolean polygonSnapDistance( Point a, Point b )
  {
    int snapDistance = 15; // Define a snap distance
    return Math.hypot( a.x - b.x, a.y - b.y ) <= snapDistance;
  }
  
  private int[] parsePoints( Point p1, Point p2 )
  {
    int x = Math.min( p1.x, p2.x );
    int y = Math.min( p1.y, p2.y );
    int width = Math.abs( p1.x - p2.x );
    int height = Math.abs( p1.y - p2.y );
    return new int[] { x, y, width, height };
  }
  
  @Override
  public void mouseClicked( MouseEvent e )
  { 
    if ( shapePointRenderType == PointType.POLYGON )
    {
      Point clkdPt = e.getPoint();
      boolean haveOrigin = !activeCoordinates.isEmpty();
            
      // if the polygon does not yet have a point in activeCoordinates, then
      // clear activeCoordinates and establish a point as the origin of polygon
      if ( !haveOrigin )
      {
        activeCoordinates.clear();
        activeCoordinates.add( constructNode( clkdPt ) );
      }
      else
      {
        // if has origin, store reference CSP for it
        DrawNode orig = activeCoordinates.getFirst();
        
        // if we're not at snap distance, continue to append new CSPs
        if ( !polygonSnapDistance( orig.getPoint(), clkdPt ) )
          activeCoordinates.add( constructNode( clkdPt ) );
        
        else
        {
          // else, we must have an origin, and our click is within snap
          // distance. thus, add CSP to active points, and finalize
          // System.out.printf( "CSP %s snapped to origin %s%n", constructNode( clkdPt ), orig );
          //  for ( DrawNode csp : activeCoordinates )
          // System.out.println( "Finalizing CSP{" + csp + "}" );
          placedCoordinates.addAll( activeCoordinates );
          actionSequence.add( new DrawSeqElement( activeCoordinates.size(), this.strokeWidth ) );
          activeCoordinates.clear();
        }
      }
    }
    this.repaint();
  }
  
  @Override
  public void mouseEntered( MouseEvent e ) {}
  @Override
  public void mouseExited( MouseEvent e ) {}
  
  @Override 
  public void mousePressed( MouseEvent e )
  {
    if ( isBoxDelimited( shapePointRenderType ) ) 
    {
      activeCoordinates.clear();
      activeCoordinates.add( constructNode( e.getPoint() ) );
      activeCoordinates.add( constructNode( e.getPoint() ) );
    }
    else if ( shapePointRenderType == PointType.DOT )
    {
      dotIncrement = 0;
      placedCoordinates.add( constructNode( e.getPoint() ) );
      actionSequence.add( new DrawSeqElement( ++dotIncrement, this.strokeWidth ) );
    }
  }
      
  @Override
  public void mouseReleased( MouseEvent e )
  {
    if ( isBoxDelimited( shapePointRenderType ) ) 
    {
      placedCoordinates.addAll( activeCoordinates );
      actionSequence.push( new DrawSeqElement( 2, this.strokeWidth ) );
      activeCoordinates.clear();
      this.repaint();
    }
  }
  
  @Override
  public void mouseMoved( MouseEvent e ) { }
  
  @Override
  public void mouseDragged( MouseEvent e )
  {
    if ( shapePointRenderType == PointType.DOT )
    {
      Integer ind = ( actionSequence.size() - 1 );
      placedCoordinates.add( constructNode( e.getPoint() ) );
      actionSequence.get( ind ).setPointCounts( ++dotIncrement );
    }
    else if ( isBoxDelimited( shapePointRenderType ) )
    {
      activeCoordinates.set( 1, constructNode( e.getPoint() ) );
    }
    this.repaint();
  }
  
  class DrawSeqElement implements Serializable
  {
    private Integer pointCount;
    private float strokeWidth;
    
    public DrawSeqElement( Integer cts, float width )
    {
      this.pointCount = cts;
      this.strokeWidth = width;
    }
    
    public void setPointCounts( Integer cts )
    {
      this.pointCount = cts;
    }
    
    public void setStrokeWidth( float width )
    {
      this.strokeWidth = width;
    }
    
    public Integer getPointCounts()
    {
      return this.pointCount;
    }
    
    public float getStrokeWidth()
    {
      return this.strokeWidth;
    }
  }
  
}