/**
 * LwLine.java
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

import ca.sfu.federation.model.geometry.ILine;
import ca.sfu.federation.model.geometry.IPoint;

/**
 * A lightweight line object that supports the ILine interface.
 * @author Davis Marques
 * @version 0.1.0
 */
public class LwLine extends LightWeightObject implements ILine {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private double x1, y1, z1;
    private double x2, y2, z2;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * LwLine constructor.
     */
    public LwLine() {
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
    /**
     * Get the end point.
     * @return End point.
     */
    public IPoint getEndPoint() {
        return (IPoint) new LwPoint(x2,y2,z2);
    }

    /**
     * Get the length of the line.
     * @return Length of the line.
     */
    public Double getLength() {
        double dist = Math.sqrt(Math.pow(x2-x1,2.0) + Math.pow(y2-y1,2.0) + Math.pow(z2-z1,2.0));
        return Double.valueOf(dist);
    }

    /**
     * Get the start point.
     * @return Start point.
     */
    public IPoint getStartPoint() {
        return (IPoint) new LwPoint(x1,y1,z1);
    }
    
} // end class
