/*
 * IUpdateable.java
 * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
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

package ca.sfu.federation.model;

/**
 * <p>
 * A model object whose state may be depedant upon the state of other model 
 * objects, and may consequently require updating when changes occur in the 
 * model or the application environment itself.
 * </p>
 * <p>
 * Objects that implement IUpdateable may retain properties that require
 * Properties of type Method can not be serialized, and must be restored when 
 * the object is retrieved from persistent storage.
 * </p>
 * @author Davis Marques
 * @version 0.1.0
 */
public interface IUpdateable extends INamed {

    /**
     * Force the object to clear its local result cache.
     */
    public void clearResultCache();

//    /**
//     * Get the current update method.
//     * @return Update method name.
//     */
//    public String getUpdateMethodName();
    
    /**
     * Get the list of available update methods.
     * @return List of update methods.
     */
    // public List getUpdateMethods();
    
    /**
     * Restore transient and non-serializable values after deserialization.
     */
    public void restore();
    
    /**
     * Update the state of this object.
     * @return True if the object has updated successfully, false otherwise.
     */
    public boolean update();
    
} // end interface