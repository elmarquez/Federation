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

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.utils.ImageIconUtils;
import com.developer.rose.BeanProxy;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openide.util.Utilities;

/**
 * Scenario is a SystolicArray that manages collections of object instances and 
 * their instance parameters, peforms operations on collections of instances, 
 * and maintains metadata describing the context of the Scenario object.
 * Scenario is comprised of objects that fall into one of two categories.  
 * Contextual objects are those which can be referenced but that can not be 
 * modified by local objects.  Transactional objects are those which can be 
 * referenced and modified by local objects.
 *
 * @author Davis Marques
 */
public class Scenario extends Observable implements IContext, IViewable, IGraphable, IUpdateable, Observer, Serializable {

    private static final Logger logger = Logger.getLogger(Scenario.class.getName());
    
    private String name;                    // object name
    private IContext context;               // the parent context
    private boolean isVisible;              // visibility state
    
    private LinkedHashMap contextual;       // collection of objects from external contexts
    private LinkedHashMap transactional;    // collection of objects in the local context
    private ArrayList updateOrder;          // the order by which objects are updated
    
    // metadata
    private String description;             // description of this scenario
    private ImageIcon icon;                 // icon
    private Image thumbnail;                // generated thumbnail
    
    //--------------------------------------------------------------------------

    /**
     * Scenario constructor.
     * @param MyContext The parent Context.
     */
    public Scenario(IContext MyContext) {
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        // generate a name for the new scenario
        String basename = "Scenario";
        int index = 0;
        boolean match = false;
        while (!match) {
            String newname = basename + index;
            if (!MyContext.hasObject(newname)) {
                this.name = newname;
                match = true;
            }
            index++;
        }
        // set properties
        this.context = MyContext;
        this.contextual = new LinkedHashMap();
        this.transactional = new LinkedHashMap();
        this.updateOrder = new ArrayList();
        this.description = "Empty Scenario description.";
        this.icon = ImageIconUtils.loadIconById("view-scenario-icon");
        this.thumbnail = Utilities.loadImage(config.getString("scenario-thumbnail"));
        // register in the context
        try {
            MyContext.add(this);
        } catch (IllegalArgumentException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
    }
    
    /**
     * Scenario constructor.
     * @param Name Scenario name.
     * @param MyContext The parent Context.
     */
    public Scenario(String Name, IContext MyContext) {
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        // set properties
        this.name = Name;
        this.context = MyContext;
        this.contextual = new LinkedHashMap();
        this.transactional = new LinkedHashMap();
        this.updateOrder = new ArrayList();
        this.description = "Empty Scenario description.";
        this.icon = ImageIconUtils.loadIconById("scenario-icon");
        this.thumbnail = Utilities.loadImage(config.getString("scenario-thumbnail"));
        // register in the context
        try {
            MyContext.add(this);
        } catch (IllegalArgumentException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Add a NamedObject to the Scenario.
     * @param Named NamedObject to add to the Scenario.
     * @throws DuplicateObjectException An object with the same name already exists in the Scenario.
     */
    public void add(INamed Named) throws IllegalArgumentException {
        if (!this.transactional.containsKey(Named.getName())) {
            // put the object in the local store
            this.transactional.put(Named.getName(),Named);
            // set the object context to this scenario
            Named.setContext(this);
            // listen for changes on the object
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.addObserver(this);
            }
        } else {
            throw new IllegalArgumentException("An object identified by the same name already exists in the Context.");
        }
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_ADD));
    }

    /**
     * Add an INamed as a Contextual Element of the Scenario.
     * 
     * @param Named INamed to add as a Contextual object.
     * @throws DuplicateObjectException An object by the same name already exists in this Scenario.
     */
    public void addContextual(INamed Named) throws IllegalArgumentException {
        if (!this.contextual.containsKey(Named.getName())) {
            // add to list of contextual elements
            this.contextual.put(Named.getName(),Named);
            // listen for changes on the object
            // TODO: we should listen for changes on the object's parent context instead!
            // in this situation, we will receive an update from the object before it has been updated _in context_
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.addObserver(this);
            }
        } else {
            throw new IllegalArgumentException("An object identified by the same name already exists in the Context.");
        }
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_ADD));
    }

    /**
     * Clear the Result cache for each local Element.
     */
    public void clearResultCache() {
        Iterator iter = this.transactional.values().iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.clearResultCache();
            }
        }
        // generate change event
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Delete the object from its context.
     */
    public void delete() {
        // tell observers to release me
        logger.log(Level.INFO,"Scenario signalled Observers to release and delete object");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }
    
    /**
     * Get canonical name.
     * @return Fully qualified name.
     */
    public String getCanonicalName() {
        String fqn = "";
        if (this.context != null) {
            fqn = this.context.getCanonicalName() + ".";
        }
        fqn += this.name;
        return fqn;
    }
    
    /** 
     * Get the Context.
     * @return The parent Context.
     */
    public IContext getContext() {
        return this.context;
    }
    
    /**
     * Get the collection of Contextual elements in this Scenario.
     * @return Collection of Contextual elements in this Scenario.
     */
    public LinkedHashMap getContextualElements() {
        return this.contextual;
    }

    /**
     * Get the dependancies for the Scenario.
     * @return List of Scenarios and other objects upon which this Scenario is dependant.
     */
    public Map getDependancies() {
        // init
        LinkedHashMap results = new LinkedHashMap();
        HashSet dependancies = new HashSet();
        // scenario dependancies come from contextual object references
        Iterator iter = this.contextual.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            dependancies.add(named.getContext());
        }
        // convert set to map
        iter = dependancies.iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            results.put(named.getName(),named);
        }
        // return result
        return (Map) results;
    }

    /**
     * Get description.
     * @return Description.
     */
    public String getDescription() {
        return this.description;
    }
    
   /**
    * Get the local Element collection.
    * @return Collection of NamedObjects in this context.
    */
    public Map getElements() {
        // init
        LinkedHashMap results = new LinkedHashMap();
        // put all objects into result table
        results.putAll(this.contextual);
        results.putAll(this.transactional);
        // return results
        return (Map) results;
    }    
    
    /**
     * Get Elements in topological order.
     * @return SystolicArrayElements in Update order.
     */
    private List getElementsInTopologicalOrder() {
        // init
        ArrayList ordered = new ArrayList();
        // create the update order
        Iterator iter = this.transactional.values().iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            // if the object can have dependancies
            if (object instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) object;
                LinkedHashMap d = (LinkedHashMap) graphobject.getDependancies();
                // add dependencies first, then add object
                Iterator it = d.values().iterator();
                while (it.hasNext()) {
                    Object independant = it.next();
                    if (!ordered.contains(independant)) {
                        ordered.add(independant);
                    }
                }
                // add self
                if (!ordered.contains(object)) {
                    ordered.add(object);
                }
            } else {
                // add the object
                ordered.add(object);
            }
        }
        // return results
        return (List) ordered;
    }

    /**
     * Get an icon representing this object.
     * @return Image.
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Get the object Name.
     * @return The object name.
     */
    public String getName() {
        return this.name;
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
     * Get 3D renderable object representation.  Scenario does not currently
     * return any object for itself or its children.
     * @return Scene graph node.
     */
    public Node getRenderable() {
        return new Group();
    }

    /**
     * Get thumbnail representation of this object.
     * @return Thumbnail image.
     */
    public Image getThumbnail() {
        Map thumbnails = (Map) Application.getContext().getViewState(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS);
        String canonicalname = this.getCanonicalName();
        if (thumbnails != null && thumbnails.containsKey(canonicalname)) {
            return (Image) thumbnails.get(canonicalname);
        }
        // return default thumbnail
        return this.thumbnail;
    }

    /**
     * Get all referenced versions of an object.
     * @param Name Name of object.
     * @return Version stack for a NamedObject, ordered from most recent to oldest.
     * TODO: develop more so that the complete version stack for an object can be returned
     */
    public List getVersions(String Name) {
        // init
        ArrayList version = new ArrayList();
        // get versions recursively
        if (this.transactional.containsKey(Name)) {
            version.add(this.transactional.get(Name));
        }
        if (this.contextual.containsKey(Name)) {
            INamed named = (INamed) this.contextual.get(Name);
            version.add(named);
            // TODO: get the whole version stack
        }
        // return result
        return (List) version;
    }

    /** 
     * Get the visibility state of this object.
     * @return True if visible, false otherwise.
     */
    public boolean getVisible() {
        return this.isVisible;
    }

    /**
     * True if the Scenario has the NamedObject.  Transactional (ie. local) 
     * elements supercede or mask Contextual elements.
     * @return True if the NamedObject exists in the Scenario, false otherwise.
     */
    public boolean hasObject(String Name) {
        boolean result = false;
        if (this.transactional.containsKey(Name)) {
            result = true;
        } else if (this.contextual.containsKey(Name)) {
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
        if (this.transactional.containsKey(path[0])) {
            named = (INamed) this.transactional.get(path[0]);
        } else if (this.contextual.containsKey(path[0])) {
            named = (INamed) this.contextual.get(path[0]);
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
                // create a bean proxy object to help us access properties, then try to retrieve the value
                proxy = new BeanProxy(named);
                result = proxy.get(path[1]);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Named property '" + path[1] + "' could not be resolved.");
            }
            if (result == null) {
                if (named instanceof IContext) {
                    // try to lookup the subpart
                    IContext thecontext = (IContext) named;
                    result = thecontext.lookup(path[1]);
                }
            }
            if (result == null) {
                throw new IllegalArgumentException("Property value is null.");
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
                    logger.log(Level.WARNING,"{0}",stack);
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
     * Remove a NamedObject from the Scenario.
     * @param Named NamedObject to be removed.
     */
    public void remove(INamed Named) {
        boolean found = false;
        // if the object exists in the collection
        if (this.transactional.containsKey(Named.getName())) {
            found = true;
            // stop observing the object
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.deleteObserver(this);
            }
            // remove the object from the collection
            this.transactional.remove(Named.getName());
        } else if (this.contextual.containsKey(Named.getName())) {
            found = true;
            // stop observing the object
            if (Named instanceof Observable) {
                Observable o = (Observable) Named;
                o.deleteObserver(this);
            }
            // remove the object from the collection
            this.contextual.remove(Named.getName());
        }
        // notify observers
        if (found) {
            this.setChanged();
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETED));
        }
    }

    /**
     * Restore non-serializable objects following deserialization.
     */
    public void restore() {
       Iterator iter = this.transactional.values().iterator();
       while (iter.hasNext()) {
           Object object = iter.next();
           if (object instanceof IUpdateable) {
               IUpdateable updateable = (IUpdateable) object;
               updateable.restore();
           }
       }
    }
    
    /**
     * Set the parent Context.
     * @param MyContext The parent Context.
     */
    public void setContext(IContext MyContext) {
        this.context = MyContext;
        // generate change event
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Set the description.
     * @param Description Scenario description.
     */
    public void setDescription(String Description) {
        this.description = Description;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_PROPERTY_CHANGE));
    }
    
    /**
     * Set the object Name.
     * @param Name The object name.
     */
    public void setName(String Name) {
        this.name = Name;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_NAME_CHANGE));
    }

    /**
     * Set visibility state.
     */
    public void setVisible(boolean state) {
        this.isVisible = state;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_PROPERTY_CHANGE));
    }

    /**
    * Update the state of the SystolicArray.
    * @return True if updated successfully, false otherwise.
    */
    public boolean update() {
        // clearResult the SAE result caches
        this.clearResultCache();
        // get nodes in topological order
        ArrayList elements = (ArrayList) this.getElementsInTopologicalOrder();
        // update nodes
        boolean updateSuccessful = true;
        Iterator e = elements.iterator();
        while (e.hasNext() && updateSuccessful) {
            Object object = e.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateSuccessful = updateable.update();
            }
        }
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_CHANGE));
        // return result
        return updateSuccessful;
    }

    /**
     * Observer update method is called whenever the observer object is changed.
     * @param o The object that has notified us.
     * @param arg Update message.
     */
    public void update(Observable o, Object arg) {
        INamed named = null;
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"Scenario received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_CHANGE:
                    break;
                case ApplicationContext.EVENT_DESCRIPTION_CHANGE:
                    break;
                case ApplicationContext.EVENT_ELEMENT_ADD:
                    break;
                case ApplicationContext.EVENT_ELEMENT_CHANGE:
                    this.update();
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_CHANGE));
                    logger.log(Level.INFO,"Scenario fired local update");
                    break;
                case ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST:
                    logger.log(Level.INFO,"Scenario fired element delete");
                    named = (INamed) o;
                    this.remove(named);
                    break;
                case ApplicationContext.EVENT_ELEMENT_RENAME:
                    break;
                case ApplicationContext.EVENT_ICON_CHANGE:
                    break;
                case ApplicationContext.EVENT_NAME_CHANGE:
                    named = (INamed) o;
                    this.updateElementName(named);
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_RENAME));
                    logger.log(Level.INFO,"Scenario fired name change update");
                    break;
                case ApplicationContext.EVENT_THUMBNAIL_CHANGE:
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_THUMBNAIL_CHANGE));
                    logger.log(Level.INFO,"Scenario fired thumbnail change");
                    break;
                default:
                    // TODO: this is temporary and should be removed
                    this.setChanged();
                    this.notifyObservers();
            }
        }
    }
    
    /**
     * Update a NamedObject in our context.
     * 
     * @param Named INamed
     */
    private void updateElementName(INamed Named) {
        // find the old key, remove the entry, then add a new entry for the updated object
        if (this.contextual.containsValue(Named)) {
            Set keys = this.contextual.keySet();
            Iterator iter = keys.iterator();
            boolean match = false;
            String key = null;
            while (iter.hasNext() && !match) {
                key = (String) iter.next();
                INamed target = (INamed) this.contextual.get(key);
                if (Named == target) {
                    match = true;
                }
            }
            this.contextual.remove(key); // remove the old reference
            this.contextual.put(Named.getName(),Named); // put the updated reference in
        } else if (this.transactional.containsValue(Named)) {
            Set keys = this.transactional.keySet();
            Iterator iter = keys.iterator();
            boolean match = false;
            String key = null;
            while (iter.hasNext() && !match) {
                key = (String) iter.next();
                INamed target = (INamed) this.transactional.get(key);
                if (Named == target) {
                    match = true;
                }
            }
            this.transactional.remove(key); // remove the old reference
            this.transactional.put(Named.getName(),Named); // put the updated reference in
        }
    }
    
} 