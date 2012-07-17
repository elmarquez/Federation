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

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.exception.GraphCycleException;
import ca.sfu.federation.utils.ImageIconUtils;
import com.developer.rose.BeanProxy;
import java.awt.Image;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openide.util.Utilities;

/**
 * A parametric object.
 * @author Davis Marques
 */
public class Assembly extends Observable implements IContext, IViewable, IUpdateable, Observer, Serializable {
    
    private String name;                // unique identifier for this object
    private IContext context;           // the parent context
    private ArrayList elements;         // a collection of parts for this object
    private ArrayList updateOrder;      // the order by which systolic array elements are updated

    // metadata, visual properties
    private String description;         // description of this assembly
    private Image thumbnail;            // thumbnail representation of the assembly
    private ImageIcon icon;             // icon representation of object
    private boolean visible;            // visibility state

    private ArrayList behaviors;        // behaviors that are bound to this assembly. this is to be removed shortly
    
    private static final Logger logger = Logger.getLogger(Assembly.class.getName());
    
    //--------------------------------------------------------------------------
    
    /**
     * Assembly constructor. Generates a unique name in context.
     * @param Context The parent Context.
     */
    public Assembly(IContext Context) {
        // init
        this.elements = new ArrayList();
        this.updateOrder = new ArrayList();
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        // generate a name for the new scenario
        String basename = "Assembly";
        int index = 0;
        boolean match = false;
        while (!match) {
            String newname = basename + index;
            if (!Context.hasObject(newname)) {
                name = newname;
                match = true;
            }
            index++;
        }
        // set properties
        context = Context;
        behaviors = new ArrayList();
        description = null;
        this.icon = ImageIconUtils.loadIconById("assembly-icon");
        thumbnail = Utilities.loadImage(config.getString("assembly-thumbnail"));
        visible = true;
        // register in the context
        if (Context != null) {
            try {
                Context.add(this);
            } catch (IllegalArgumentException ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.SEVERE,"Could not register in context\n\n{0}",stack);
            }
        }
    }
    
    /**
     * Assembly constructor.
     * @param Name The Assembly name.
     * @param Context The Context to make the Assembly in.
     */
    public Assembly(String Name, IContext Context) {
        // init
        this.elements = new ArrayList();
        this.updateOrder = new ArrayList();
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        // set properties
        this.name = Name;
        this.context = Context;
        this.behaviors = new ArrayList();
        this.description = null;
        this.icon = ImageIconUtils.loadIconById("assembly-icon");
        this.thumbnail = Utilities.loadImage(config.getString("assembly-thumbnail"));
        this.visible = true;
        // register in the context
        if (Context != null) {
            try {
                Context.add(this);
            } catch (IllegalArgumentException ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.SEVERE,"Could not register in context\n\n{0}",stack);
            }
        }
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Add a Behavior to this object.
     * @param MyBehavior Behavior to bind with this object.
     */
    public void add(Behavior MyBehavior) {
        this.behaviors.add(MyBehavior);
    }

    /**
     * Add a NamedObject.
     * @param Named NamedObject to be added.
     * @throws IllegalArgumentException An object identified by the same name already exists in the Context.
     */
    @Override
    public void add(INamed Named) throws IllegalArgumentException {
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElements();
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
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            INamed named = (INamed) it.next();
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
     * Clear the Result cache for each Element.
     */
    @Override
    public void clearResultCache() {
        Iterator it = this.updateOrder.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.clearResultCache();
            }
        }
    }

    /**
     * Delete the object from its context.
     */
    @Override
    public void delete() {
        logger.log(Level.INFO,"Assembly signalled Observers to release and delete object");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }
    
    /**
     * Get canonical name.
     * @return Canonical name.
     */
    @Override
    public String getCanonicalName() {
        String can = "";
        if (this.context != null) {
            can = this.context.getCanonicalName() + ".";
        }
        can += this.name;
        return can;
    }

    /**
     * Get the Context.
     * @return Context of this object.
     */
    @Override
    public IContext getContext() {
        return this.context;
    }

    /**
     * Get the Description of this object.
     * @return Description of this object.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the local Elements.
     * @return Collection of NamedObjects in this context.
     */
    @Override
    public Map getElements() {
        LinkedHashMap elementsByName = new LinkedHashMap();
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            INamed named = (INamed) it.next();
            elementsByName.put(named.getName(),named);
        }
        return elementsByName;
    }
    
    /**
     * Get elements in topological order.
     * @return Elements in order.
     */
    protected ArrayList getElementsInTopologicalOrder() throws GraphCycleException {
        // init
        ArrayList sorted = new ArrayList();
        // do topological sort
        Iterator iter = this.getElements().values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            if (named instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) named;
                // get dependancies for the node
                LinkedHashMap deps = (LinkedHashMap) graphobject.getDependancies();
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
    private void getElementsInTopologicalOrder(INamed Named, ArrayList Sorted) throws GraphCycleException {
        // if the NamedObject is already in the list, then we expect that its dependancies 
        // are also already represented there and therefore we don't need to do anything
        if (Named instanceof IGraphable && !Sorted.contains(Named)) {
            IGraphable graphobject = (IGraphable) Named;
            // add nodes upon which the object is dependant first
            LinkedHashMap deps = (LinkedHashMap) graphobject.getDependancies();
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
    public ImageIcon getIcon() {
        return this.icon;
    }
    
    /**
     * Get the list of independant Elements.
     * @return Independant Elements of this Systlolic Array.
     */
    public List getIndependantElements() {
        // init
        ArrayList independant = new ArrayList();
        // get list of elements
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            // if the object can have dependancies
            if (object instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) object;
                LinkedHashMap dep = (LinkedHashMap) graphobject.getDependancies();
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
     * Get the name of this Object.
     * @return The name of this Object.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get List of Parent Contexts, inclusive of the current element.  The list 
     * is ordered from root context to the current element.  An instance of 
     * ParametricModel will always be the first element.
     * @return List of Parent contexts.
     */
    @Override
    public List getParents() {
        ArrayList parents = new ArrayList();
        // add predecessors first
        if (this.context != null) {
            parents.addAll(this.context.getParents());
        }
        // add self
        parents.add(this);
        return (List) parents;
    }

    /**
     * Get list of renderable objects.
     * @return List of renderable objects.
     */
    @Override
    public Node getRenderable() {
        // init
        BranchGroup group = new BranchGroup();
        Shape3D shape3d = new Shape3D();
        // set capabilities
        group.setCapability(Group.ALLOW_CHILDREN_READ);
        group.setCapability(Group.ALLOW_CHILDREN_WRITE);
        group.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        ArrayList list = null;
        try {
            list = (ArrayList) this.getElementsInTopologicalOrder();
        } catch (GraphCycleException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not get elements in topological order\n\n",stack);
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof IViewable) {
                IViewable displayobject = (IViewable) object;
                Node node = displayobject.getRenderable();
                group.addChild(node);
            }
        }
        // return results
        return (Node) group;
    }

    /**
     * Subclasses of Assembly should override this method to provide their own thumbnail.
     * @return Thumbnail representation of this Assembly.
     */
    @Override
    public Image getThumbnail() {
        return this.thumbnail;
    }
    
    /**
     * Get visibility state.
     * @return True if visible, false otherwise.
     */
    @Override
    public boolean getVisible() {
        return this.visible;
    }
    
    /**
     * Determine if a NamedObject exists in the Context.
     * @return True if object is in the local collection, false otherwise.
     */
    @Override
    public boolean hasObject(String Name) {
        boolean result = false;
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElements();
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
    @Override
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
                IContext thecontext = (IContext) named;
                // try to get the object value
                try {
                    result = thecontext.lookup(query);
                } catch (Exception ex) {
                    String stack = ExceptionUtils.getFullStackTrace(ex);
                    logger.log(Level.SEVERE,"Could not complete lookup\n\n{0}",stack);
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
     * Detach a Behavior from this object.
     * @param MyBehavior Behavior to detach from the object.
     */
    public void remove(Behavior MyBehavior) {
        this.behaviors.remove(MyBehavior);
    }
    
    /**
     * Remove a NamedObject from the Context.
     * @param Named The NamedObject to be removed.
     * @throws IllegalArgumentException The NamedObject does not exist in the Context.
     */
    @Override
    public void remove(INamed Named) throws IllegalArgumentException {
        String childname = Named.getName();
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElements();
        if (elementsByName.containsKey(childname)) {
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
            throw new IllegalArgumentException("The object '" + childname + "' does not exist in the current Context.");
        }
    }

    /**
     * Restore transient and non-serializable objects following deserialization.
     */
    @Override
    public void restore() {
        Iterator iter = this.getElements().values().iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.restore();
            }
        }
    }
    
    /**
     * Set the Context for this object.
     * @param MyContext The Context for this object.
     */
    @Override
    public void setContext(IContext MyContext) {
        this.context = MyContext;
    }

    /**
     * Set the Description of this object.
     * @param Description Description of this object.
     */
    public void setDescription(String Description) {
        this.description = Description;
    }
    
    /**
     * Set the icon.
     * @param MyIcon Icon.
     */
    public void setIcon(ImageIcon MyIcon) {
        this.icon = MyIcon;
    }
    
    /**
     * Set the name of this object.
     * @param Name Name of this object.
     * TODO: need to check that the name is not a reserved word, and that it is unique
     */
    @Override
    public void setName(String Name) {
        this.name = Name;
        // send rename notification to parent context
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_NAME_CHANGE));
    }

    /**
     * Set visibility state.
     * @param Visible True if visible, false otherwise.
     */
    @Override
    public void setVisible(boolean Visible) {
        this.visible = Visible;
    }
    
    /**
     * Update the state of the AbstractContext.
     * @return True if updated successfully, false otherwise.
     */
    @Override
    public boolean update() {
        // clearResult the SAE result caches
        this.clearResultCache();
        ArrayList childelements = new ArrayList();
        try {
            childelements = (ArrayList) this.getElementsInTopologicalOrder();
        } catch (GraphCycleException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not get elements in topological order\n\n{0}",stack);
        }
        // print out the update order for debugging
        Iterator it = childelements.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        while (it.hasNext()) {
            INamed named = (INamed) it.next();
            sb.append(named.getName());
            sb.append(" ");
        }
        logger.log(Level.FINE,"{0} update event. Update sequence is: ", new Object[]{this.name,sb.toString()});
        // update nodes
        boolean updateSuccessful = true;
        Iterator it2 = childelements.iterator();
        while (it2.hasNext() && updateSuccessful) {
            Object object = it2.next();
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
     * @param arg Update argument.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST:
                    logger.log(Level.ALL,"Assembly fired element delete");
                    INamed named = (INamed) o;
                    this.remove(named);
                    break;
                case ApplicationContext.EVENT_PROPERTY_CHANGE:
                case ApplicationContext.EVENT_UPDATEMETHOD_CHANGE:
                    logger.log(Level.ALL,"Assembly fired local update");
                    this.update();
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_CHANGE));
                    break;
            }
        }
    }

} 
