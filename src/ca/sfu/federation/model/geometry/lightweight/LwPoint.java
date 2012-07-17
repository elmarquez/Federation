/**
 * LWPoint.java
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

package ca.sfu.federation.model.geometry.lightweight;

import ca.sfu.federation.model.geometry.IPoint;

/**
 * A lightweight Point object that supports IPoint.
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public class LwPoint extends LightWeightObject implements IPoint {
    
    //--------------------------------------------------------------------------

    
    private double x;
    private double y;
    private double z;
    
    //--------------------------------------------------------------------------

    
    /**
     * LwPoint constructor.
     */
    public LwPoint() {
    }
    
    /**
     * LwPoint constructor.
     * 
     * @param X X coordinate.
     * @param Y Y coordinate.
     * @param Z Z coordinate.
     */
    public LwPoint(double X, double Y, double Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }
    
    //--------------------------------------------------------------------------


    public Double getX() {
        return Double.valueOf(this.x);
    }

    public Double getY() {
        return Double.valueOf(this.y);
    }

    public Double getZ() {
        return Double.valueOf(this.z);
    }

    public void setX(Double X) {
        this.x = X.doubleValue();
    }

    public void setY(Double Y) {
        this.y = Y.doubleValue();
    }

    public void setZ(Double Z) {
        this.z = Z.doubleValue();
    }
    
} 
