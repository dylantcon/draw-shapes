/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package drawshapes;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

class DrawNode implements Serializable
{
  private Color color;
  private Point point;
  private final PointType pointtype;

  DrawNode( Color c, Point p, PointType pt )
  {
    this.color = c;
    this.point = p;
    this.pointtype = pt;
  }

  public void setColor( Color c )
  {
    this.color = c;
  }

  public void setPoint( Point p )
  {
    this.point = p;
  }

  public Color getColor()
  {
    return this.color;
  }

  public Point getPoint()
  {
    return this.point;
  }

  public PointType getPointType()
  {
    return this.pointtype;
  }

  @Override
  public String toString()
  {
    return String.format( "color:(%s),at:(%s),for:(%s);",
                          color.toString(),
                          point.toString(),
                          pointtype.toString() );
  }
}