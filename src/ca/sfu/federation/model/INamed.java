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

package ca.sfu.federation.model;

import javax.swing.ImageIcon;

/**
 * A model object that is identified by a unique name.
 * TODO: delete operation should throw an exception if the object can not be deleted
 * TODO: set name should throw an exception if the object can not be renamed
 * @author Davis Marques
 */
public interface INamed {
    
    /**
     * Delete the object from the context.
     */
    public void delete();
    
    /**
     * Get the parent context of the object.
     * @return Parent context
     */
    public IContext getContext();
    
    /**
     * Get the fully qualified object name.
     * @return Canonical name.
     */
    public String getCanonicalName();
    
    /**
     * Get an icon representation of the object.
     * @return Image.
     */
    public abstract ImageIcon getIcon();
    
    /*
     * Get the name
     * @return Name
     */
    public String getName();

    /**
     * Try to register the object in the parent context.
     * @param Context
     * @throws Exception 
     */
    public void registerInContext(IContext Context) throws Exception;
    
    /**
     * Set the parent context.
     * @param Context Parent context
     */
    public void setContext(IContext Context);

    /* 
     * Set the name
     * @param Name Name
     */
    public void setName(String Name);
    
} // end interface
