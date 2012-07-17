/**
 * Plane.java
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
import ca.sfu.federation.model.geometry.lightweight.LwPoint;
import ca.sfu.federation.model.annotations.Update;
import ca.sfu.federation.model.annotations.Default;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.ApplicationContext;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import java.util.ResourceBundle;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import org.openide.util.Utilities;

/**
 * A Plane in 3D space, defined by 4 points.  Also known as a 'Quad'.
 * @author Davis Marques
 * @version 0.1.0
 */
public class Plane extends Component implements IPlane, IPoint {

    //--------------------------------------------------------------------------

    
    private String basename = "Plane";
    private CoordinateSystem cs;        // embedding space
    
    private double p1_x, p1_y, p1_z;    // plane point 1
    private double p2_x, p2_y, p2_z;    // plane point 2
    private double p3_x, p3_y, p3_z;    // plane point 3
    private double p4_x, p4_y, p4_z;    // plane point 4
    
    private double o_x;                 // plane origin x coordinate
    private double o_y;                 // plane origin y coordinate
    private double o_z;                 // plane origin z coordinate
    
    //--------------------------------------------------------------------------

    
    /**
     * Point constructor.
     * @param MyContext The parent Context.
     */
    public Plane(IContext MyContext) {
        super(MyContext);
        // generate a name for the new scenario
        String basename = "Plane";
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
        this.setIcon(Utilities.loadImage(config.getString("plane-icon")));
    }

    /**
     * Point constructor.
     * @param Name
     * @param MyContext The parent Context.
     */
    public Plane(String Name,IContext MyContext) {
        super(Name,MyContext);
        // set the icon
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        this.setIcon(Utilities.loadImage(config.getString("plane-icon")));
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Get the plane origin.
     * @return  A point representing the plane origin.
     * TODO: stub
     */
    public IPoint getOrigin() {
        return (IPoint) new LwPoint(0,0,0);
    }

    /**
     * Get the plane normal.
     * @return A normalized point representing the plane normal as a bound vector.
     * TODO: stub
     */
    public IDirection getNormal() {
        return (IDirection) new LwPoint(0,0,0);
    }

    /**
     * Get Java3D renderable object.
     * @return Java3D renderable node.
     */
    public Node getRenderable() {
        // set appearance
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(new Color3f(1.0f,1.0f,1.0f));
        Appearance app = new Appearance();
        app.setColoringAttributes(colorAtt);
        // build geometry
        Box box = new Box(4.0f,4.0f,0.25f,Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY | Primitive.ENABLE_GEOMETRY_PICKING,app);
        // set capabilities
        box.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        box.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        box.setCapability(Node.ENABLE_PICK_REPORTING);
        // return result
        return (Node) box;
    }
     
    /**
     * Get the X coordinate.
     * @return X coordinate.
     */
    public Double getX() {
        return Double.valueOf(this.o_x);
    }
    
    /**
     * Get the Y coordinate.
     * @return Y coordinate.
     */
    public Double getY() {
        return Double.valueOf(this.o_y);
    }

    /**
     * Get the Z coordinate.
     * @return Z coordinate.
     */
    public Double getZ() {
        return Double.valueOf(this.o_z);
    }

    /**
     * Set the X coordinate.
     * @param X X coordinate.
     */
    public void setX(Double X) {
        this.o_x = X.doubleValue();
    }
    
    /**
     * Set the Y coordinate.
     * @param Y Y coordinate.
     */
    public void setY(Double Y) {
        this.o_y = Y.doubleValue();
    }

    /**
     * Set the Z coordinate.
     * @param Z Z coordinate.
     */
    public void setZ(Double Z) {
        this.o_z = Z.doubleValue();
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
        this.o_x = 1;
        this.o_y = 1;
        this.o_z = 1;
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
        this.o_x = X;
        this.o_y = Y;
        this.o_z = Z;
        // return result
        return true;
    }
   
} 
