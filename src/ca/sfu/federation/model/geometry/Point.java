/**
 * Point.java
 * * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
 *
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
import ca.sfu.federation.viewer.modelviewer.DisplayObject3D;
import ca.sfu.federation.model.annotations.Default;
import ca.sfu.federation.model.annotations.Update;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.geometry.lightweight.LwPoint;
import ca.sfu.federation.ApplicationContext;
import java.util.ResourceBundle;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import org.openide.util.Utilities;

/**
 * A Point in 3D space.
 * @author Davis Marques
 * @version 0.1.0
 */
public class Point extends Component implements IPoint {

    //--------------------------------------------------------------------------

    
    private CoordinateSystem cs;
    private double x;
    private double y;
    private double z;
    
    //--------------------------------------------------------------------------

    
    /**
     * Point constructor.
     * @param MyContext The parent Context.
     */
    public Point(IContext MyContext) {
        super(MyContext);
        // generate a name for the new object
        String basename = "Point";
        int index = 0;
        boolean match = false;
        while (!match) {
            String newname = basename + index;
            if (!MyContext.hasObject(newname)) {
                this.setName(newname);
                match = true;
            }
            index++;
        }
        // set the icon
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        this.setIcon(Utilities.loadImage(config.getString("point-icon")));
    }

    /**
     * Point constructor.
     * @param Name
     * @param MyContext The parent Context.
     */
    public Point(String Name,IContext MyContext) {
        super(Name,MyContext);
        // set the icon
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        this.setIcon(Utilities.loadImage(config.getString("point-icon")));
    }
    
    //--------------------------------------------------------------------------


    /**
     * Add Point2 to Point1.
     * @param Point1
     * @param Point2
     * @return Sum of points.
     */
    public static IPoint add(Point Point1, Point Point2) {
        double x1, y1, z1;
        double x2, y2, z2;
        x1 = Point1.getX();
        y1 = Point1.getY();
        z1 = Point1.getZ();
        x2 = Point2.getX();
        y2 = Point2.getY();
        z2 = Point2.getZ();
        return (IPoint) new LwPoint(x2+x1,y2+y1,z2+z1);
    }

    /**
     * Get renderable objects.
     * @return List of renderable objects.
     */
    public Node getRenderable() {
        // create a new shape3d node
        DisplayObject3D shape = new DisplayObject3D(this);
        // set capabilities
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        // set appearance
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(new Color3f(1.0f,1.0f,1.0f));
        Appearance app = new Appearance();
        app.setColoringAttributes(colorAtt);
        shape.setAppearance(app);
        // build geometry
        PointArray array = new PointArray(1, PointArray.COORDINATES);
        array.setCoordinates(0, new double[] {this.x, this.y, this.z});
        shape.setGeometry(array);
        // return result
        return (Node) shape;
    }
     
    /**
     * Get the X coordinate.
     * @return X coordinate.
     */
    public Double getX() {
        return Double.valueOf(this.x);
    }
    
    /**
     * Get the Y coordinate.
     * @return Y coordinate.
     */
    public Double getY() {
        return Double.valueOf(this.y);
    }

    /**
     * Get the Z coordinate.
     * @return Z coordinate.
     */
    public Double getZ() {
        return Double.valueOf(this.z);
    }

    /**
     * Set the X coordinate.
     * @param X X coordinate.
     */
    public void setX(Double X) {
        this.x = X.doubleValue();
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_PROPERTY_CHANGE));
    }
    
    /**
     * Set the Y coordinate.
     * @param Y Y coordinate.
     */
    public void setY(Double Y) {
        this.y = Y.doubleValue();
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_PROPERTY_CHANGE));
    }

    /**
     * Set the Z coordinate.
     * @param Z Z coordinate.
     */
    public void setZ(Double Z) {
        this.z = Z.doubleValue();
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_PROPERTY_CHANGE));
    }
    
    /**
     * Subtract Point1 from Point2.
     * @param Point1
     * @param Point2
     * @return Difference of points.
     */
    public static IPoint subtract(IPoint Point1, IPoint Point2) {
        double x1, y1, z1;
        double x2, y2, z2;
        x1 = Point1.getX();
        y1 = Point1.getY();
        z1 = Point1.getZ();
        x2 = Point2.getX();
        y2 = Point2.getY();
        z2 = Point2.getZ();
        return (IPoint) new LwPoint(x2-x1,y2-y1,z2-z1);
    }
    
    //--------------------------------------------------------------------------
    // UPDATE METHODS
    
    /**
     * Default update method. Places point in BaseCS at 1,1,1.
     * @return True if successful, false otherwise.
     */
    @Default
    @Update(description="Default update method.  Places point in BaseCS at 1,1,1.",
            parameter={})
    public boolean updateDefault() {
        this.x = 1;
        this.y = 1;
        this.z = 1;
        return true;
    }
    
    /**
     * Creates a point object by name and coordinate values.
     * @param MyCoordinateSystem Coordinate system that the point exists in.
     * @param X X coordinate.
     * @param Y Y coordinate.
     * @param Z Z coordinate.
     * @return True if update was successful, false otherwise.
     */
    @Update(description="Point by coordinate system and coordinates.",
            parameter={"MyCoordinateSystem","X","Y","Z"})
    public boolean updateByCSAndCoordinates(CoordinateSystem MyCoordinateSystem, double X, double Y, double Z) {
        // set coordinates
        this.x = X;
        this.y = Y;
        this.z = Z;
        // return result
        return true;
    }
   
} 
