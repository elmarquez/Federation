/*
 * IPlane.java
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
 * Interface to a plane-like object.
 * @author Davis Marques
 */
public interface IPlane {
   
    /**
     * A direction representing a normal to the surface of the plane.
     * @return Normal to the surface of the plane.
     */
    public IDirection getNormal();
    
    /**
     * Get the origin of the plane.
     * @return An IPoint representing the origin of the plane in the embedding space.
     */
    public IPoint getOrigin();
    
} // end interface