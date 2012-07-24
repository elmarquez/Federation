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
import ca.sfu.federation.utils.IContextUtils;
import ca.sfu.federation.utils.INamedUtils;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * A parametric object.
 * @author Davis Marques
 */
public class Assembly extends Observable implements IContext, IViewable, IUpdateable, Observer, Serializable {

    public static final String DEFAULT_NAME = Assembly.class.getName();
    
    private String name;                // unique identifier for this object
    private IContext context;           // the parent context
    private Image thumbnail;            // thumbnail representation of the assembly
    private ImageIcon icon;             // icon representation of object
    private boolean visible;            // visibility state

    // a collection of parts for this object
    private ArrayList<INamed> elements = new ArrayList<INamed>();
    
    // behaviors that are bound to this assembly. this is to be removed shortly
    private ArrayList<Behavior> behaviors = new ArrayList<Behavior>();        

    private static final Logger logger = Logger.getLogger(Assembly.class.getName());

    //--------------------------------------------------------------------------

    /**
     * Assembly constructor.
     * @param Name Name
     */
    public Assembly(String Name) {
        name = Name;
        // set properties
        icon = ImageIconUtils.loadIconById("assembly-icon");
        thumbnail = ImageIconUtils.loadIconById("assembly-thumbnail").getImage();
        visible = true;
    }

    //--------------------------------------------------------------------------

    /**
     * Add a behavior to this object.
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
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElementMap();
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
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }

    /**
     * Delete the object from its context.
     */
    public void delete() {
        logger.log(Level.INFO,"Assembly signalled Observers to release and delete object");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }

    /**
     * Draw object.
     */
    public void draw(Graphics g) {
    }

    /**
     * Get canonical name.
     * @return Canonical name.
     */
    public String getCanonicalName() {
        return INamedUtils.getCanonicalName(this);
    }

    /**
     * Get the Context.
     * @return Context of this object.
     */
    public IContext getContext() {
        return this.context;
    }

    /**
     * Get the local Elements.
     * @return Collection of NamedObjects in this context.
     */
    public Map<String,INamed> getElementMap() {
        LinkedHashMap<String,INamed> map = new LinkedHashMap<String,INamed>();
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            INamed named = (INamed) it.next();
            map.put(named.getName(),named);
        }
        return map;
    }

    public List<INamed> getElements() {
        return this.elements;
    }
    
    /**
     * Get elements in topological order.
     * @return Elements in order.
     */
    protected ArrayList getElementsInTopologicalOrder() throws GraphCycleException {
        Map map = getElementMap();
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
     * Get the name of this Object.
     * @return The name of this Object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the next available element name for the given base name.
     * @param Name Base name
     * @return 
     */
    public String getNextName(String Name) {
        return IContextUtils.getNextName(this, Name);
    }
    
    /**
     * Get List of Parent Contexts, inclusive of the current element.  The list
     * is ordered from root context to the current element.  An instance of
     * ParametricModel will always be the first element.
     * @return List of Parent contexts.
     */
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
     * Subclasses of Assembly should override this method to provide their own thumbnail.
     * @return Thumbnail representation of this Assembly.
     */
    public Image getThumbnail() {
        return this.thumbnail;
    }

    /**
     * Get visibility state.
     * @return True if visible, false otherwise.
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Determine if named object exists in the context.
     * @return True if found, false otherwise.
     */
    public boolean hasObject(String Name) {
        Map map = getElementMap();
        if (map.containsKey(Name)) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves a single object, or object property value in the local context.
     * @param Query NamedObject reference of the form objectname.propertyname
     * @throws IllegalArgumentException The referenced object could not be located, or the resultant value is null.
     */
    public Object lookup(String Query) throws IllegalArgumentException {
        return IContextUtils.lookup(this.getElementMap(), Query);
    }

    public void registerInContext(IContext Context) throws Exception {
        INamedUtils.registerInContext(Context, this);
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
        LinkedHashMap elementsByName = (LinkedHashMap) this.getElementMap();
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
    public void restore() {
        IContextUtils.restore(this);
    }

    /**
     * Set the Context for this object.
     * @param MyContext The Context for this object.
     */
    public void setContext(IContext MyContext) {
        this.context = MyContext;
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
    public void setVisible(boolean Visible) {
        this.visible = Visible;
    }

    /**
     * Update the state of the AbstractContext.
     * @return True if updated successfully, false otherwise.
     */
    public boolean update() {
        ArrayList childelements = new ArrayList();
        try {
            childelements = getElementsInTopologicalOrder();
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
     * Handle update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
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
