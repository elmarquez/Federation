/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.sfu.federation.model;

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.exception.GraphCycleException;
import ca.sfu.federation.utils.IContextUtils;
import ca.sfu.federation.utils.ImageIconUtils;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Parametric Model is the top level model object and acts as a container for
 * all other model objects and the view state.
 *
 * @author Davis Marques
 */
public class ParametricModel extends Observable implements IContext, IUpdateable, Observer, Serializable {

    public static final String DEFAULT_NAME = "Model";

    private static final Logger logger = Logger.getLogger(ParametricModel.class.getName());
    
    private String name;                // unique identifier for this object
    private ImageIcon icon;             // icon representation of object
    private HashMap<String,Object> params = new HashMap<String,Object>();   // model parameters
    private ArrayList<INamed> elements = new ArrayList<INamed>();         // a collection of parts for this object
    private ArrayList<INamed> updateOrder;      // the order by which systolic array elements are updated

    //--------------------------------------------------------------------------
    
    /**
     * ParametricModel default constructor
     */
    public ParametricModel() {
        init(ParametricModel.DEFAULT_NAME);
    }

    //--------------------------------------------------------------------------
    
    /**
     * Add a named object to the context.
     * @param Named Named object
     * @throws IllegalArgumentException An object identified by the same name already exists in the Context.
     */
    public void add(INamed Named) throws IllegalArgumentException {
        Map elementsByName = getElementMap();
        if (!elementsByName.containsKey(Named.getName())) {
            // add object
            elements.add(Named);
            // observe element for changes
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.addObserver(this);
            }
            // notify observers of change
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_ADD));
        } else {
            throw new IllegalArgumentException("An object identified by the same name already exists in the Context.");
        }
    }

    /**
     * Remove all Elements from the Systolic Array.
     */
    public void clearAll() {
        // stop listening for changes on elements
        Iterator e = this.elements.iterator();
        while (e.hasNext()) {
            INamed named = (INamed) e.next();
            if (named instanceof Observable) {
                Observable o = (Observable) named;
                o.deleteObserver(this);
            }
        }
        // clear collections
        this.elements.clear();
        this.updateOrder.clear();
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }

    /**
     * Delete the model.
     */
    public void delete() {
        logger.log(Level.WARNING, "ParametricModel delete method called.  Not implemented yet.");
    }

    /**
     * Get canonical name.
     * @return Canonical name.
     */
    public String getCanonicalName() {
        return this.name;
    }

    /**
     * Get the parent context.
     * @return The parent of a parametric model is always null.
     */
    public IContext getContext() {
        return null;
    }

    /**
     * Get a list of elements in the context.
     * @return 
     */
    public List<INamed> getElements() {
        return elements;
    }

    /**
     * Get a map of the elements in the context.
     * @return Name, object map of elements in this context
     */
    public Map<String,INamed> getElementMap() {
        return IContextUtils.getElementMap(elements);
    }

    /**
     * Get elements in topological order.
     * @return Elements in order.
     */
    private ArrayList<INamed> getElementsInTopologicalOrder() throws GraphCycleException {
        Map<String,INamed> map = getElementMap();
        return IContextUtils.getElementsInTopologicalOrder(map);
    }

    /**
     * Get icon.
     * @return Icon.
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Get the list of independent elements.
     * @return List of elements 
     */
    public List<INamed> getIndependantElements() {
        return IContextUtils.getIndependantElements(elements);
    }

    /**
     * Get the name of this object.
     * @return The name of this Object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the next available object name.
     * @param Name Name
     * @return
     */
    public String getNextName(String Name) {
        return IContextUtils.getNextName(this, Name);
    }

    /**
     * Get a list of parent contexts, inclusive of the current element. The list
     * is ordered from root context to the current element. An instance of
     * Parametric Model will always be the first element.
     * @return List of Parent contexts.
     */
    public List<IContext> getParents() {
        ArrayList<IContext> parents = new ArrayList<IContext>();
        parents.add(this);
        return parents;
    }

    /**
     * Determine if a named object exists in the context.
     * @return True if object is in the local collection, false otherwise.
     */
    public boolean hasObject(String Name) {
        Map map = getElementMap();
        if (map.containsKey(Name)) {
            return true;
        }
        return false;
    }

    /**
     * Initialize the model.
     *
     * @param Name
     */
    private void init(String Name) {
        this.elements = new ArrayList();
        this.updateOrder = new ArrayList();
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        // set properties
        this.name = Name;
        this.icon = ImageIconUtils.loadIconById("parametricmodel-icon");
    }

    /**
     * Retrieves a single object, or object property value in the local context.
     *
     * @param Query NamedObject reference of the form objectname.propertyname
     * @throws IllegalArgumentException The referenced object could not be
     * located, or the resultant value is null.
     */
    public Object lookup(String Query) throws IllegalArgumentException {
        return IContextUtils.lookup(getElementMap(), Query);
    }

    /**
     * Instances of ParametricModel may not be added to any other context
     * @param Context Context
     * @throws Exception 
     */
    public void registerInContext(IContext Context) throws Exception {
        throw new Exception("Instances of ParametricModel may not be added to any other context");
    }
    
    /**
     * Remove a NamedObject from the Context.
     *
     * @param Named The NamedObject to be removed.
     * @throws IllegalArgumentException The NamedObject does not exist in the
     * Context.
     */
    public void remove(INamed Named) throws IllegalArgumentException {
        String itemName = Named.getName();
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElementMap();
        if (elementsByName.containsKey(itemName)) {
            // stop listening on the NamedObject
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.deleteObserver(this);
            }
            // remove the NamedObject from the collection
            this.elements.remove(Named);
            // notify observers
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETED));
        } else {
            throw new IllegalArgumentException("The object '" + itemName + "' does not exist in the current Context.");
        }
    }

    /**
     * Restore transient and non-serializable values.
     */
    public void restore() {
        IContextUtils.restore(this);
    }

    /**
     * Set the current Context.
     *
     * @param MyContext The current working Context.
     */
    public void setContext(IContext MyContext) {
        throw new IllegalArgumentException("ParametricModel does not support this method.");
    }

    /**
     * Set the icon.
     *
     * @param MyIcon Icon.
     */
    public void setIcon(ImageIcon MyIcon) {
        this.icon = MyIcon;
    }

    /**
     * Set the name of this object.
     *
     * @param Name Name of this object. TODO: need to check that the name is not
     * a reserved word, and that it is unique
     */
    public void setName(String Name) {
        this.name = Name;
        // send rename notification to parent context
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_NAME_CHANGE));
    }

    /**
     * Update the state of the context.
     * @return True if updated successfully, false otherwise.
     */
    public boolean update() {
        ArrayList<INamed> elementsInOrder = null; 
        try {
            elementsInOrder = getElementsInTopologicalOrder();
        } catch (GraphCycleException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING, "{0}", stack);
        }
        // print out the update order for debugging
        IContextUtils.logUpdateOrder(this,elementsInOrder);
        // update nodes
        boolean success = false;
        if (elementsInOrder != null) {
            Iterator e = elementsInOrder.iterator();
            while (e.hasNext() && success) {
                Object object = e.next();
                if (object instanceof IUpdateable) {
                    IUpdateable updateable = (IUpdateable) object;
                    success = updateable.update();
                }
            }
        }
        // if update was successful, notify all observers
        if (success) {
            this.setChanged();
            this.notifyObservers();
        }
        // return result
        return success;
    }

    /**
     * Update event.
     *
     * @param o Observable object.
     * @param arg Update arguments.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST:
                    logger.log(Level.INFO, "ParametricModel fired element delete");
                    INamed named = (INamed) o;
                    this.remove(named);
                    break;
            }
        }
    }
}