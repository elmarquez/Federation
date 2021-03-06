/*
 * IPoint.java
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

/**
 * Interface to a point-like object.
 * @author Davis Marques
 */
public interface IPoint {
   
    /**
     * Get the X coordinate value.
     */
    public Double getX();
    
    /**
     * Get the Y coordinate value.
     */
    public Double getY();

    /**
     * Get the Z coordinate value.
     */
    public Double getZ();

    /**
     * Set the X coordinate value.
     */
    public void setX(Double X);
    
    /**
     * Set the Y coordinate value.
     */
    public void setY(Double Y);

    /**
     * Set the Z coordinate value.
     */
    public void setZ(Double Z);
    
} // end interface