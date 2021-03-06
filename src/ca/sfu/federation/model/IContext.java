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

import java.util.List;
import java.util.Map;

/**
 * A model object that acts as a namespace for resolving INamed objects and 
 * values.  We say that an object implementing IContext is the <i>Context</i> of 
 * the child objects contained within it.
 * @author Davis Marques
 */
public interface IContext extends INamed {

    /** 
     * Add an INamed object to the Context.
     *
     * @param Named An object implementing INamed to be added to the Context.
     * @throws IllegalArgumentException An object identified by the same name already exists in the Context.
     */
    public void add(INamed Named) throws IllegalArgumentException;

    /**
     * Get a list of elements in the context.
     * @return 
     */
    public List<INamed> getElements();
    
    /**
     * Get a map of elements in the context.
     * @return Name, object element map.
     */
    public Map<String,INamed> getElementMap();

    /**
     * Get the next valid name.
     * @param Name
     * @return 
     */
    public String getNextName(String Name);
    
    /**
     * Get List of Parent Contexts, inclusive of the current element.  The list 
     * is ordered from root context to the current element.  An instance of 
     * ParametricModel will always be the first element.
     *
     * @return List of Parent contexts.
     */
    public List<IContext> getParents();
    
    /**
     * Determine if a NamedObject exists in the local collection.
     *
     * @param Name 
     * @return True if NamedObject is in the collection, false otherwise.
     */
    public boolean hasObject(String Name);
    
    /**
     * Looks up an object or object property value of the form object.property
     *
     * @param Query A NamedObject reference of the form objectname.propertyname
     * @return Object
     * @throws IllegalArgumentException The object reference could not be resolved.
     */
    public Object lookup(String Query) throws IllegalArgumentException;

    /**
     * Remove a NamedObject from the Context.
     *
     * @param Named NamedObject to be removed from the Context.
     */
    public void remove(INamed Named);
    
}