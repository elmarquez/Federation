/**
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ca.sfu.federation.model.geometry;

import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.annotations.Default;
import ca.sfu.federation.model.annotations.Update;
import ca.sfu.federation.model.geometry.lightweight.LwPoint;
import ca.sfu.federation.utils.ImageIconUtils;
import ca.sfu.federation.viewer.modelviewer.DisplayObject3D;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * A Line in 3D space.
 * @author Davis Marques
 */
public class Line extends Component implements ILine {

    private static final String DEFAULT_NAME = Line.class.getName();
    private IPoint startPoint;
    private IPoint midPoint;
    private IPoint endPoint;
    private IPoint direction;
    private double length;
    
    //--------------------------------------------------------------------------
    
    /**
     * Line default constructor.
     */
    public Line() {
        super(DEFAULT_NAME);
        setIcon(ImageIconUtils.loadIconById("line-icon"));
    }
    
    /**
     * Line constructor.
     * @param Name Name of this object.
     */
    public Line(String Name) {
        super(Name);
        setIcon(ImageIconUtils.loadIconById("line-icon"));
    }
    
    //--------------------------------------------------------------------------

    /**
     * Calculate direction, length and midpoint properties.
     */
    private void calculateProperties() {
        // direction
        this.direction = Point.subtract(this.startPoint,this.endPoint);
        // line length
        this.length = Math.sqrt(Math.pow(direction.getX(),2.0) + Math.pow(direction.getY(),2.0) + Math.pow(direction.getZ(),2.0));
        // midpoint
        // LwPoint half = new LwPoint(this.direction.getX()/2,this.direction.getY()/2,this.direction.getZ()/2);
        // this.midPoint = Point.add(this.startPoint,half);
    }

    /**
     * Get direction.
     * @return IDirection.
     */
    public IPoint getDirection() {
        return this.direction;
    }
    
    /**
     * Get the end point.
     * @return End point.
     */
    public IPoint getEndPoint() {
        return this.endPoint;
    }
    
    /**
     * Get the length of the line.
     * @return Length of the line.
     */
    public Double getLength() {
        return this.length;
    }

    /**
     * Get the midpoint of the line.
     * @return Midpoint of the line.
     */
    public IPoint getMidPoint() {
        return this.midPoint;
    }
    
    /**
     * Get a Java3D renderable object.
     * @return Java3D renderable object.
     */
    @Override
    public Node getRenderable() {
        // create a new shape3d node
        DisplayObject3D shape = new DisplayObject3D(this);
        // set capabilities
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        // set appearance
        LineAttributes lineAtt = new LineAttributes();
        lineAtt.setLineWidth(1.0f);
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(new Color3f(1.0f,0.0f,0.0f));
        Appearance app = new Appearance();
        app.setColoringAttributes(colorAtt);
        app.setLineAttributes(lineAtt);
        shape.setAppearance(app);
        // build geometry
        Point3d[] points = new Point3d[2];
        double x1, y1, z1;
        double x2, y2, z2;
        x1 = this.startPoint.getX();
        y1 = this.startPoint.getY();
        z1 = this.startPoint.getZ();
        x2 = this.endPoint.getX();
        y2 = this.endPoint.getY();
        z2 = this.endPoint.getZ();
        points[0] = new Point3d(x1, y1, z1);
        points[1] = new Point3d(x2, y2, z2);
        LineArray line = new LineArray(2, LineArray.COORDINATES);
        line.setCoordinates(0, points);
        shape.setGeometry(line);
        // return result
        return (Node) shape;
    }
    
    /**
     * Get the start point.
     * @return Start point.
     */
    public IPoint getStartPoint() {
        return this.startPoint;
    }
    
    //--------------------------------------------------------------------------
    // UPDATE METHODS
    
    /**
     * Create a default line.
     * @return True if update was successful, false otherwise.
     */
    @Default
    @Update(description="Default line of unit length.",
            parameter={})
    public boolean updateDefault() {
        // TODO: create a line of unit length in the BaseCS
        // return result
        return true;
    }
    
    /**
     * Create a line by two points.
     * @param StartPoint Start point.
     * @param EndPoint End point.
     * @return True if update was successful, false otherwise.
     */
    @Update(description="Create Line by start and end points.",
            parameter={"StartPoint","EndPoint"})
    public boolean updateByPoints(Point StartPoint, Point EndPoint) {
        this.startPoint = (IPoint) StartPoint;
        this.endPoint = (IPoint) EndPoint;
        // update properties
        this.calculateProperties();
        // return result
        return true;
    }
    
    /**
     * Create line by start and end point coordinates.
     * @param X1 X start coordinate.
     * @param Y1 Y start coordinate.
     * @param Z1 Z start coordinate.
     * @param X2 X end coordinate.
     * @param Y2 Y end coordinate.
     * @param Z2 Z end coordinate.
     * @return True if the update was successful, false otherwise.
     */
    @Update(description="Create Line by start and end point coordinates.",
            parameter={"StartX","StartY","StartZ","EndX","EndY","EndZ"})
    public boolean updateByStartEndCoordinates(Double X1, Double Y1, Double Z1, Double X2, Double Y2, Double Z2) {
        // new lightweight points
        this.startPoint = new LwPoint(X1,Y1,Z1);
        this.endPoint = new LwPoint(X2,Y2,Z2);
        // calculate line properties
        this.calculateProperties();
        // return result
        return true;
    }

} 
