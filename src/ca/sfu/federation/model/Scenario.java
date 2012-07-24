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

    public static final String DEFAULT_NAME = "Scenario";

    private String name;                    // object name
    private ImageIcon icon;                 // icon
    private Image thumbnail;                // generated thumbnail
    private IContext context;               // the parent context
    private boolean isVisible;              // visibility state

    // collection of objects from external contexts
    private LinkedHashMap<String,INamed> contextual = new LinkedHashMap<String,INamed>();

    // collection of objects in the local context
    private LinkedHashMap<String,INamed> transactional = new LinkedHashMap<String,INamed>();

    private static final Logger logger = Logger.getLogger(Scenario.class.getName());

    //--------------------------------------------------------------------------

    /**
     * Scenario constructor
     * @param Name Name
     */
    public Scenario(String Name) {
        name = Name;
        icon = ImageIconUtils.loadIconById("scenario-icon");
        thumbnail = ImageIconUtils.loadIconById("scenario-thumbnail").getImage();
    }

    //--------------------------------------------------------------------------

    /**
     * Add a transactional element to the context.
     * @param Named Named object
     * @throws DuplicateObjectException An object with the same name already exists in the context.
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
     * Delete the object from its context.
     */
    public void delete() {
        // tell observers to release me
        logger.log(Level.INFO,"Scenario signalled Observers to release and delete object");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }

    /**
     * Draw model object.
     */
    public void draw(Graphics g) {
    }

    /**
     * Get canonical name.
     * @return Fully qualified name.
     */
    public String getCanonicalName() {
        return INamedUtils.getCanonicalName(this);
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
     * Get the dependencies for the scenario.
     * @return List of Scenarios and other objects upon which this Scenario is dependant.
     */
    public Map<String,INamed> getDependancies() {
        return IContextUtils.getDependancies(this.contextual);
    }

    public List<INamed> getElements() {
        ArrayList<INamed> elements = new ArrayList<INamed>();
        elements.addAll(this.contextual.values());
        elements.addAll(this.transactional.values());
        return elements;
    }
    
   /**
    * Get the local Element collection.
    * @return Collection of NamedObjects in this context.
    */
    public Map<String,INamed> getElementMap() {
        LinkedHashMap<String,INamed> results = new LinkedHashMap<String,INamed>();
        results.putAll(this.contextual);
        results.putAll(this.transactional);
        return results;
    }

    /**
     * Get elements in topological order.
     * @return Elements in order
     */
    private List<INamed> getElementsInTopologicalOrder() throws GraphCycleException {
        Map map = getElementMap();
        return IContextUtils.getElementsInTopologicalOrder(map);
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

    public String getNextName(String Name) {
        return IContextUtils.getNextName(this, Name);
    }

    /**
     * Get List of Parent Contexts, inclusive of the current element.  The list
     * is ordered from root context to the current element.  An instance of
     * ParametricModel will always be the first element.
     * @return List of Parent contexts.
     */
    public List<IContext> getParents() {
        ArrayList<IContext> parents = new ArrayList<IContext>();
        // add predecessors first
        if (this.context != null) {
            parents.addAll(this.context.getParents());
        }
        // add self
        parents.add(this);
        return parents;
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
    public List<INamed> getVersions(String Name) {
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
     * Determine if the context has the named object. Transactional (ie. local)
     * elements supercede or mask Contextual elements.
     * @return True if found, false otherwise.
     */
    public boolean hasObject(String Name) {
        if (this.transactional.containsKey(Name)) {
            return true;
        } else if (this.contextual.containsKey(Name)) {
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
        Map map = getElementMap();
        return IContextUtils.lookup(map, Query);
    }

    public void registerInContext(IContext Context) throws Exception {
        INamedUtils.registerInContext(Context, this);
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
        IContextUtils.restore(this);
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
     * Update the state of the scenario.
     * @return True if updated successfully, false otherwise.
     */
    public boolean update() {
        List<INamed> elements;
        try {
            elements = getElementsInTopologicalOrder();
        } catch (GraphCycleException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
            return false;
        }
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
     * Update a named object in our context.
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