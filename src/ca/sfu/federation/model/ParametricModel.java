/**
 * ParametricModel.java
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

import ca.sfu.federation.model.exception.GraphCycleException;
import ca.sfu.federation.model.ConfigManager;
import com.developer.rose.BeanProxy;
import gnu.trove.THashMap;
import java.awt.Image;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Vector;
import org.openide.util.Utilities;

/**
 * Parametric Model is the top level model object and acts as a container for
 * all other model objects and the view state.
 *
 * @author Davis Marques
 * @version 0.0.l
 */
public class ParametricModel extends Observable implements IContext, IUpdateable, Observer, Serializable {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private static ParametricModel instance; // singleton instance
    private THashMap modelParam;             // model parameters
    private THashMap viewState;              // view parameters
    private String path;                     // the path to the model working directory

    private String name;        // unique identifier for this object
    private Vector elements;    // a collection of parts for this object
    private Vector updateOrder; // the order by which systolic array elements are updated

    // metadata, visual properties
    private String description;              // model description
    private Image icon;         // icon representation of object
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * Parametric Model is the parent container for all model related objects.
     * Parametric Model maintains project metadata, application settings, and
     * viewer state.
     * @param Name The name of this model.
     */
    public ParametricModel(String Name) {
        // init
        this.elements = new Vector();
        this.updateOrder = new Vector();
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ConfigManager.APPLICATION_PROPERTIES);
        // set properties
        this.instance = this;
        this.path = "";
        this.name = Name;
        this.icon = Utilities.loadImage(config.getString("parametricmodel-icon"));
        // default view state parameters
        this.viewState = new THashMap();
        this.viewState.put(ConfigManager.VIEWER_CURRENT_CONTEXT,this);
        this.viewState.put(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS,new THashMap());
        this.viewState.put(ConfigManager.VIEWER_ICONTEXTVIEWER_SCENES,new THashMap());
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
    /**
     * Add a NamedObject.
     * @param Named NamedObject to be added.
     * @throws IllegalArgumentException An object identified by the same name already exists in the Context.
     */
    public void add(INamed Named) throws IllegalArgumentException {
        THashMap elementsByName = (THashMap) this.getElements();
        if (!elementsByName.containsKey(Named.getName())) {
            // add object
            this.elements.add(Named);
            // observe element for changes
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.addObserver(this);
            }
            // notify observers of change
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_ADD));
        } else {
            throw new IllegalArgumentException("An object identified by the same name already exists in the Context.");
        }
    }

    /**
     * Remove all Elements from the Systolic Array.
     */
    public void clearAll() {
        // stop listening for changes on elements
        Enumeration e = this.elements.elements();
        while (e.hasMoreElements()) {
            INamed named = (INamed) e.nextElement();
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
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_DELETE_REQUEST));
    }

    /**
     * Clear the Result cache for each Element.
     */
    public void clearResultCache() {
        Enumeration e = this.updateOrder.elements();
        while (e.hasMoreElements()) {
            Object object = e.nextElement();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.clearResultCache();
            }
        }
    }

    /**
     * Forces single instance only.
     * @throws CloneNotSupportedException The object can not be copied.
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
    /**
     * Delete the model.
     */
    public void delete() {
        System.err.println("WARNING: ParametricModel delete method called.  Not implemented yet.");
    }
    
    /**
     * Get canonical name.
     * @return Canonical name.
     */
    public String getCanonicalName() {
        return this.name;
    }

    /**
     * Get the current Context.
     * @return The current context.
     */
    public IContext getContext() {
        return null;
    }

    /**
     * Get the description. 
     * @return Description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Get the local Elements.
     * @return Collection of NamedObjects in this context.
     */
    public Map getElements() {
        THashMap elementsByName = new THashMap();
        Enumeration e = this.elements.elements();
        while (e.hasMoreElements()) {
            INamed named = (INamed) e.nextElement();
            elementsByName.put(named.getName(),named);
        }
        return elementsByName;
    }

    /**
     * Get elements in topological order.
     * @return Elements in order.
     */
    protected Vector getElementsInTopologicalOrder() throws GraphCycleException {
        // init
        Vector sorted = new Vector();
        // do topological sort
        Iterator iter = this.getElements().values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            if (named instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) named;
                // get dependancies for the node
                THashMap deps = (THashMap) graphobject.getDependancies();
                // for each dependancy, do a topological sort on its subgraph
                Iterator it = deps.values().iterator();
                while (it.hasNext()) {
                    INamed namedDep = (INamed) it.next();
                    this.getElementsInTopologicalOrder(namedDep,sorted);
                }
                // insert self into list
                if (!sorted.contains(named)) {
                    sorted.add(named);
                }
            } else {
                // what should we do with an object that does not have dependancies?
            }
        }
        // return result
        return sorted;
    }

    /**
     * Get elements in topological order.
     * @param Named Named object.
     * @param Sorted List of elements in topological order.
     * @throws GraphCycleException Graph contains a cycle and can not be updated.
     */
    private void getElementsInTopologicalOrder(INamed Named, Vector Sorted) throws GraphCycleException {
        // if the NamedObject is already in the list, then we expect that its dependancies 
        // are also already represented there and therefore we don't need to do anything
        if (Named instanceof IGraphable && !Sorted.contains(Named)) {
            IGraphable graphobject = (IGraphable) Named;
            // add nodes upon which the object is dependant first
            THashMap deps = (THashMap) graphobject.getDependancies();
            Iterator iter = deps.values().iterator();
            while (iter.hasNext()) {
                INamed namedDep = (INamed) iter.next();
                this.getElementsInTopologicalOrder(namedDep,Sorted);
            }
            // add myself
            if (!Sorted.contains(Named)) {
                Sorted.add(Named);
            } else {
                // graph has a cycle
                throw new GraphCycleException();
            }
        }
    }

    /**
     * Get icon.
     * @return Icon.
     */
    public Image getIcon() {
        return this.icon;
    }
    
    /**
     * Get the list of independant Elements.
     * @return Independant Elements of this Systlolic Array.
     */
    public List getIndependantElements() {
        // init
        Vector independant = new Vector();
        // get list of elements
        Enumeration e = this.elements.elements();
        while (e.hasMoreElements()) {
            Object object = e.nextElement();
            THashMap dep = null;
            // if the object can have dependancies
            if (object instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) object;
                dep = (THashMap) graphobject.getDependancies();
                // if the elements has no dependancies, then it is an independant elements
                if (dep.size()==0) {
                    independant.add(object);
                }
            }
        }
        // return results
        return (List) independant;
    }
    
    /**
     * Returns the singleton instance.
     * @return The instance.
     */
    public static synchronized ParametricModel getInstance() {
        if (instance == null) {
            instance = new ParametricModel("DefaultModel");
        }
        return instance;
    }
    
    /**
     * Get the name of this Object.
     * @return The name of this Object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get List of Parent Contexts, inclusive of the current element.  The list
     * is ordered from root context to the current element.  An instance of
     * Parametric Model will always be the first element.
     * @return List of Parent contexts.
     */
    public List getParents() {
        Vector parents = new Vector();
        parents.add(this);
        return (List) parents;
    }
    
    /**
     * Get a viewer parameter by Key value.
     * @param Key Key value.
     * @return Parameter value.
     */
    public Object getViewState(Object Key) {
        return this.viewState.get(Key);
    }
    
    /**
     * Get viewer state parameters.
     * @return Map of parameters.
     */
    public Map getViewState() {
        return this.viewState;
    }
    
    /**
     * Determine if a NamedObject exists in the Context.
     * @return True if object is in the local collection, false otherwise.
     */
    public boolean hasObject(String Name) {
        boolean result = false;
        THashMap elementsByName = (THashMap) this.getElements();
        if (elementsByName.containsKey(Name)) {
            result = true;
        }
        return result;
    } 
    
    /**
     * Retrieves a single object, or object property value in the local context.
     * @param Query NamedObject reference of the form objectname.propertyname
     * @throws IllegalArgumentException The referenced object could not be located, or the resultant value is null.
     */
    public Object lookup(String Query) throws IllegalArgumentException {
        // initialize
        INamed named = null;
        Object result = null;
        // check to see if the Query is well formed
        if (Query.length()<1) {
            // TODO: do checks for malformed names.  I think there is a Java library that checks names
            throw new IllegalArgumentException("An empty or null object reference was provided.");
        }
        // convert the query into a path
        String[] path = Query.split("\\.");
        // lookup the object in the local context
        if (this.getElements().containsKey(path[0])) {
            named = (INamed) this.getElements().get(path[0]);
        }
        // if not found, throw an error
        if (named == null) {
            throw new IllegalArgumentException("The referenced object could not be found in the current Context.");
        }
        // lookup object subparts
        if (path.length==1) {
            // objectname
            result = named;
        } else if (path.length==2) {
            // objectname.propertyname
            BeanProxy proxy = null;
            try {
                // create a bean proxy object to help us access properties
                proxy = new BeanProxy(named);
                // try to retrieve the property value
                result = proxy.get(path[1]);
                // if the value is null
                if (result == null) {
                    throw new IllegalArgumentException("Property value is null.");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("Named property could not be resolved.");
            }
        } else if (path.length>2) {
            // if the object supports IContext, then send the subquery to the object
            if (named instanceof IContext) {
                // convert path back to a normal query
                String query = "";
                for (int i=1;i<path.length;i++) {
                    if (i+1<path.length) {
                        query += path[i] + ".";
                    } else {
                        query += path[i];
                    }
                }
                // cast object as IContext
                IContext context = (IContext) named;
                // try to get the object value
                try {
                    result = context.lookup(query);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // the reference is malformed, throw an error
                throw new IllegalArgumentException("Object " + named.getName() + " does not support IContext.  Subparts of this object can not be resolved.");
            }
        } else {
            // the reference is malformed, throw an error
            throw new IllegalArgumentException("Object reference is malformed.");
        }
        // return result
        return result;
    }
    
    /**
     * Remove a NamedObject from the Context.
     * @param Named The NamedObject to be removed.
     * @throws IllegalArgumentException The NamedObject does not exist in the Context.
     */
    public void remove(INamed Named) throws IllegalArgumentException {
        String name = Named.getName();
        THashMap elementsByName = (THashMap) this.getElements();
        if (elementsByName.containsKey(name)) {
            // stop listening on the NamedObject
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.deleteObserver(this);
            }
            // remove the NamedObject from the collection
            this.elements.remove(Named);
            // notify observers
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_DELETED));            
        } else {
            throw new IllegalArgumentException("The object '" + name + "' does not exist in the current Context.");
        }
    }

    /**
     * Restore transient and non-serializable values.
     */
    public void restore() {
        // for each object, restore values
        THashMap elements = (THashMap) this.getElements();
        Iterator iter = elements.values().iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.restore();
            }
        }
    }
    
    /**
     * Set the current Context.
     * @param MyContext The current working Context.
     */
    public void setContext(IContext MyContext) {
        throw new IllegalArgumentException("ParametricModel does not support this method.");
    }
    
    /**
     * Set the description. 
     * @param Description Description.
     */
    public void setDescription(String Description) {
        this.description = Description;
    }
    
    /**
     * Set the icon.
     * @param MyIcon Icon.
     */
    public void setIcon(Image MyIcon) {
        this.icon = MyIcon;
    }

    /**
     * Set the name of this object.
     * @param Name Name of this object.
     * TODO: need to check that the name is not a reserved word, and that it is unique
     */
    public void setName(String Name) {
        this.name = Name;
        // send rename notification to parent context
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_NAME_CHANGE));
    }

    /**
     * Set a viewer parameter.
     * @param Key
     * @param Value
     */
    public void setViewState(Object Key, Object Value) {
        // get the old value
        Object oldValue = this.viewState.get(Key);
        // set the new value
        this.viewState.put(Key,Value);
        // fire property change notification
        // changes.firePropertyChange((String) Key, oldValue, Value);
        // fire state change notification
        if (Key.equals(ConfigManager.VIEWER_CURRENT_CONTEXT)) {
            System.out.println("INFO: ParametricModel updated view state current context to " + Value.toString());
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_CONTEXT_CHANGE));
        } else if (Key.equals(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS)) {
            System.out.println("INFO: ParametricModel updated IContext thumbnails.");
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_THUMBNAIL_CHANGE));
        } else if (Key.equals(ConfigManager.VIEWER_SELECTION)) {
            System.out.println("INFO: ParametricModel updated Selection list state.");
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_SELECTION_CHANGE));
        }
    }

    /**
     * Update the state of the AbstractContext.
     * @return True if updated successfully, false otherwise.
     */
    public boolean update() {
        // clearResult the SAE result caches
        this.clearResultCache();
        Vector elements = null;
        try {
            elements = (Vector) this.getElementsInTopologicalOrder();
        } catch (GraphCycleException ex) {
            ex.printStackTrace();
        }
        // print out the update order for debugging
        Enumeration en = elements.elements();
        System.out.print("INFO: " + this.name + " update event. Update sequence is: ");
        while (en.hasMoreElements()) {
            INamed named = (INamed) en.nextElement();
            System.out.print(named.getName() + " ");
        }
        System.out.print("\n");        
        // update nodes
        boolean updateSuccessful = true;
        Enumeration e = elements.elements();
        while (e.hasMoreElements() && updateSuccessful) {
            Object object = e.nextElement();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateSuccessful = updateable.update();
            }
        }
        // if update was successful, notify all observers
        if (updateSuccessful) {
            this.setChanged();
            this.notifyObservers();
        }
        // return result
        return updateSuccessful;
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update arguments.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ConfigManager.EVENT_ELEMENT_DELETE_REQUEST:
                    System.out.println("INFO: ParametricModel fired element delete.");
                    INamed named = (INamed) o;
                    this.remove(named);
                    break;
            }
        }
    }
    
} // end class