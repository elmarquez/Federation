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
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.Color;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 * Coordinate System in 3D space.
 * @author Davis Marques
 * @version 0.0.2
 */
public class CoordinateSystem extends Component implements IPoint {
    
    private static final String DEFAULT_NAME = CoordinateSystem.class.getName();
    private static final float ORIGIN_AXIS_LENGTH = 1.0f;
    private static final boolean ORIGIN_NEGATIVE_AXES = false;

    private boolean isBaseCS; 
    private double x;
    private double y;
    private double z;
    
    //--------------------------------------------------------------------------

    /**
     * CoordinateSystem constructor.
     */
    public CoordinateSystem() {
        super(DEFAULT_NAME);
        this.setIcon(ImageIconUtils.loadIconById("coordinatesystem-icon"));
    }
    
    /**
     * CoordinateSystem constructor.
     * @param Name Coordinate System name.
     * @param MyContext Parent context.
     */
    public CoordinateSystem(String Name) {
        super(Name);
        this.setIcon(ImageIconUtils.loadIconById("coordinatesystem-icon"));
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Get 3d renderable icon for coordinate system.
     * @return Renderable object.
     */
    @Override
    public Node getRenderable() {
        // axis shape
        LineArray axis = null;
        if (ORIGIN_NEGATIVE_AXES) {
            // show positive and negative axes
            axis = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            // X-Axis
            axis.setCoordinate(0,new Point3f(-(ORIGIN_AXIS_LENGTH),0.0f,0.0f));
            axis.setCoordinate(1,new Point3f(ORIGIN_AXIS_LENGTH,0.0f,0.0f));
            axis.setColor(0,new Color3f(Color.red));
            axis.setColor(1,new Color3f(Color.red));
            // Y-Axis
            axis.setCoordinate(2,new Point3f(0.0f,-(ORIGIN_AXIS_LENGTH),0.0f));
            axis.setCoordinate(3,new Point3f(0.0f,ORIGIN_AXIS_LENGTH,0.0f));
            axis.setColor(2,new Color3f(Color.green));
            axis.setColor(3,new Color3f(Color.green));
            // Z-Axis
            axis.setCoordinate(4,new Point3f(0.0f,0.0f,-(ORIGIN_AXIS_LENGTH)));
            axis.setCoordinate(5,new Point3f(0.0f,0.0f,ORIGIN_AXIS_LENGTH));
            axis.setColor(4,new Color3f(Color.blue));
            axis.setColor(5,new Color3f(Color.blue));
        } else {
            // show positive axes
            axis = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            // X-Axis
            axis.setCoordinate(0,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(1,new Point3f(ORIGIN_AXIS_LENGTH,0.0f,0.0f));
            axis.setColor(0,new Color3f(Color.red));
            axis.setColor(1,new Color3f(Color.red));
            // Y-Axis
            axis.setCoordinate(2,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(3,new Point3f(0.0f,ORIGIN_AXIS_LENGTH,0.0f));
            axis.setColor(2,new Color3f(Color.green));
            axis.setColor(3,new Color3f(Color.green));
            // Z-Axis
            axis.setCoordinate(4,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(5,new Point3f(0.0f,0.0f,ORIGIN_AXIS_LENGTH));
            axis.setColor(4,new Color3f(Color.blue));
            axis.setColor(5,new Color3f(Color.blue));
        }
        // create a shape
        Shape3D shape = new Shape3D(axis);
        // return result
        return shape;
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
     * Determine if this is the base coordinate system.
     * @return True if this is the base coordinate system, false otherwise.
     */
    public boolean isBaseCS() {
        return this.isBaseCS;
    }
    
    /**
     * Set the X coordinate.
     * @param X X coordinate.
     */
    public void setX(Double X) {
        this.x = X.doubleValue();
    }
    
    /**
     * Set the Y coordinate.
     * @param Y Y coordinate.
     */
    public void setY(Double Y) {
        this.y = Y.doubleValue();
    }
    
    /**
     * Set the Z coordinate.
     * @param Z Z coordinate.
     */
    public void setZ(Double Z) {
        this.z = Z.doubleValue();
    }
    
    /**
     * Set the Base Coordinate System.
     * @param IsBaseCS True if the base coordinate system, false otherwise.
     */
    public void setBaseCS(boolean IsBaseCS) {
        this.isBaseCS = IsBaseCS;
    }
    
    //--------------------------------------------------------------------------
    // UPDATE METHODS
    
    /**
     * Create a default coordinate system.
     * @return True if the update succeeds, false otherwise.
     */
    @Default
    @Update(description="Create a default coordinate system.",
    parameter={})
    public boolean updateDefault() {
        // set base cs value
        this.isBaseCS = true;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        // return result
        return true;
    }
    
    /**
     * Create a coordinate system by coordinate values.
     * @param X X coordinate value.
     * @param Y Y coordinate value.
     * @param Z Z coordinate value.
     * @return True if the update succeeds, false otherwise.
     */
    @Update(description="Create a coordinate system by coordinate values.",
    parameter={"X","Y","Z"})
    public boolean updateByCoordinates(Double X, Double Y, Double Z) {
        // set values
        this.x = X.doubleValue();
        this.y = Y.doubleValue();
        this.z = Z.doubleValue();
        // return result
        return true;
    }
    
} 