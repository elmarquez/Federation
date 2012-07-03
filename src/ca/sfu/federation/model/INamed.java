/**
 * INamedObject.java
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

import java.awt.Image;

/**
 * A model object that is identified by a unique name.
 *
 * TODO: delete operation should throw an exception if the object can not be deleted
 * TODO: set name should throw an exception if the object can not be renamed
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public interface INamed {
    
    /**
     * Delete the object from the model.
     */
    public void delete();
    
    /**
     * Get Context.
     * @return Context of this object.
     */
    public IContext getContext();
    
    /**
     * Get fully qualified name.
     * @return Canonical name.
     */
    public String getCanonicalName();
    
    /**
     * Get an icon representation of the object.
     * @return Image.
     */
    public abstract Image getIcon();
    
    /*
     * Retreives name value.
     * @return Name of this object.
     */
    public String getName();
    
    /**
     * Set Context.
     * @param MyContext Context of this object.
     */
    public void setContext(IContext MyContext);

    /* 
     * Sets name value.
     * @param name Name of this object.
     */
    public void setName(String name);
    
} // end interface
