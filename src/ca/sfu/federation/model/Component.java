/**
 * Component.java
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

import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.exception.NonExistantMethodException;
import ca.sfu.federation.model.exception.NonExistantUpdateAnnotationException;
import ca.sfu.federation.model.annotations.Update;
import java.awt.Image;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import org.openide.util.Utilities;

/**
 * <p>
 * Component is an implementation of a Processing Element of the Systolic Array.
 * Component performs user defined units of computing, such as creating geometric
 * objects, performing logical operations on objects or sets of objects, and 
 * selecting objects.  Component may produce side effects on other Components or 
 * on the Systolic Array itself.
 * </p>
 * <p>
 * The user may instantiate a Component without needing to provide any 
 * parameters except the Context in which the Component should be registered.  
 * This is called an Anonymous Processing Element.  However, to
 * employ the Processing Element for any purpose, one of ???
 * the SAE's existing "Update Methods" must be selected for execution whenever a
 * call to the primary update method is received.  Update methods define some
 * arbitrary unit of computing and may produce side effects in the environment.
 * </p>
 * <p>
 * When the user selects the update method to be employed for the SAE instance,
 * the selected update method signature is examined and a Hashtable is created
 * to hold the required input values for the method.  The user provides the
 * values required by the update method, and those values are stored in the
 * Hashtable.  The user provided values may come in the form of object name
 * references or expressions.  The SAE parses the user input, confirms whether
 * the user input produces a value of the correct type, and assigns the value
 * into the Hashtable.
 * </p>
 * <p>
 * When the SystolicArray calls the SAE's main update method ("Update"), Update
 * maps the user defined values in the Hashtable to the method parameters and
 * then invokes the update method.  SAE's may have a result object associated
 * with them.  That result object (either single or composite) is stored in the
 * object storage and a reference to that object is maintained in the SAE. When
 * the update call is complete, the SAE returns a value of 'true' to inform the
 * SystolicArray that it has updated successfully.
 * </p>
 *
 * TODO: need to throw an exception if the user attempts to set an input property but the update method has not been set
 * TODO: need to study the cycle of setting the update method/method name, serializing/deserializing the object, etc. to ensure that it is always set
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public class Component extends Observable implements IViewable, IGraphable, IUpdateable, Observer, Serializable {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private String name;                    // the name for this node
    private String basename;                // default name prefix
    
    private IContext context;               // the parent context
    private String updateMethodName;        // the name of the user selected update method
    private transient Method updateMethod;  // the user selected update method
    private InputTable inputTable;          // inputTable corresponding with the user selected update method signature
    private IViewable result;          // displayable result object .. in theory the result may not be displayable

    // display properties
    private Image icon;                     // icon representation of component
    private Image thumbnail;                // thumbnail representation of component
    private boolean visible;                // true if the result object should be displayed
    
    //--------------------------------------------------------------------------
    // CONSTRUCTOR

   /**
     * Component constructor.
     * @param MyContext The parent Context.
     */
    public Component(IContext MyContext) {
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ConfigManager.APPLICATION_PROPERTIES);
        // generate a name for the new scenario
        basename = "Component";
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
        this.updateMethodName = null;
        this.updateMethod = null;
        this.inputTable = new InputTable(this);
        this.result = null;
        this.icon = Utilities.loadImage(config.getString("component-icon"));
        this.thumbnail = Utilities.loadImage(config.getString("component-thumbnail"));
        // observe the input table for changes
        Observable o = (Observable) this.inputTable;
        o.addObserver(this);
        // try to register in the parent context
        try {
            this.context.add(this);            
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Component constructor.
     * @param Name The name of this object.
     * @param MyContext The parent Context.
     */
    public Component(String Name, IContext MyContext) {
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ConfigManager.APPLICATION_PROPERTIES);
        // set properties
        this.name = Name;
        this.context = MyContext;
        this.updateMethodName = null;
        this.updateMethod = null;
        this.inputTable = new InputTable(this);
        this.result = null;
        this.icon = Utilities.loadImage(config.getString("component-icon"));
        this.thumbnail = Utilities.loadImage(config.getString("component-thumbnail"));
        // observe the input table for changes
        Observable o = (Observable) this.inputTable;
        o.addObserver(this);
        // try to register in the parent context
        try {
            MyContext.add(this);            
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------
    // METHODS
   
    /**
     * Clear the Result cache.
     */
    public void clearResultCache() {
        this.result = null;
    }
    
    /**
     * Delete the object from its context.
     */
    public void delete() {
        // tell observers to release me
        System.out.println("INFO: Component signalled Observers to release and delete object.");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_DELETE_REQUEST));
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
     * Get the context for this Element.
     * @return The context for this Element.
     */
    public IContext getContext() {
        return this.context;
    }
    
    /**
     * Get the Elements on which this Element is dependant.
     * @return Elements upon which this Element is dependant.
     */
    public Map getDependancies() {
        return this.inputTable.getDependancies();
    }
    
    /**
     * Get icon representation for this Component.
     * @return Icon
     */
    public Image getIcon() {
        return this.icon;
    }

    /**
     * Get Update Method inputs.
     * @return Update Method inputs.
     */
    public List getInputKeys() {
        Vector keys = new Vector();
        Input[] inputs = this.inputTable.getInputs();
        for (int i=0;i<inputs.length;i++) {
            Input input = inputs[i];
            keys.add(input.getName());
        }
        return keys;
    }
    
    /**
     * Get the input table.
     * @return InputTable
     */
    public InputTable getInputTable() {
        return this.inputTable;
    }
    
    /**
     * Get the name of this object.
     * @return The name of this object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get 3D renderable object representation of this component. 
     * Subclasses of Component must override this method.
     * @return Java3D Node
     */
    public Node getRenderable() {
        return new Group();
    }

    /**
     * Get thumbnail image representation for this component.
     * @return Image.
     */
    public Image getThumbnail() {
        return this.thumbnail;
    }

    /**
     * Get the Update Method description.
     * @return Update Method description.
     */
    public String getUpdateMethodDescription() {
        return this.inputTable.getUpdateMethodDescription();
    }
    
    /**
     * Get the current Update method name.
     * @return Update method name.
     */
    public String getUpdateMethodName() {
        return this.updateMethodName;
    }
    
    /**
     * Get the available Update Methods.
     * @return Update Methods.
     * TODO: reimplent to use annotations to locate update methods
     */
    public Method[] getUpdateMethods() {
        // init
        Vector updatemethods = new Vector();
        Method[] methods = this.getClass().getDeclaredMethods();
        // add update methods to the update methods list
        for (int i=0;i<methods.length;i++) {
            if (methods[i].isAnnotationPresent(Update.class)) {
                updatemethods.add(methods[i]);
            }
        }
        // copy vector into array
        Method[] result = new Method[updatemethods.size()];
        updatemethods.copyInto(result);
        // return result
        return result;
    }
    
    /**
     * Get the visibility state of this object.
     * @return The visibility state of this object. True if visible, false otherwise.
     */
    public boolean getVisible() {
        return this.visible;
    }
    
    /**
     * Restore the update method field value after deserialization.
     */
    public void restore() {
        if (!this.updateMethodName.equals("")) {
            try {
                this.setUpdateMethod(this.updateMethodName);
            } catch (NonExistantMethodException ex) {
                ex.printStackTrace();
            } catch (NonExistantUpdateAnnotationException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Set the Context.
     * @param MyContext The Context for the object.
     */
    public void setContext(IContext MyContext) {
        this.context = MyContext;
    }
    
    /**
     * Set the icon.
     * @param MyIcon Icon image.
     */
    protected void setIcon(Image MyIcon) {
        this.icon = MyIcon;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ICON_CHANGE));
    }
    
    /**
     * Set the user input value for a particular input property.  Listen for 
     * changes on any referenced object properties.
     * @param InputName The name of the input property.
     * @param UserInput The user value for the property.
     * TODO: need to consider here what happens if there is no input table, or the named input does not exist
     */
    public void setInput(String InputName, String UserInput) {
        if (this.inputTable != null) {
            if (this.inputTable.hasInput(InputName)) {
                this.inputTable.setInput(InputName,UserInput);
                if (this.inputTable.isPrimed()) {
                    if (this.context instanceof IUpdateable) {
                        IUpdateable updateable = (IUpdateable) this.context;
                        updateable.update();
                    }
                } else {
                    // just update this element
                    this.update();
                }
            }
        }
    }
    
    /**
     * Set the name for this object.
     * @param Name Name for this object.
     * TODO: need to make sure that the new name does not already exist in the context.  there should be a vetoable change listener in play here
     */
    public void setName(String Name) {
        String old = this.name;
        this.name = Name;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_NAME_CHANGE));
    }
    
    /**
     * Set the update method for this element, create an input table to hold
     * the required parameter input fields.
     * @param UpdateMethodName The update method name.
     * @throws NonExistantMethodException The named method does not exist for this SAE.
     * @throws NonExistantUpdateAnnotationException The named method does not have an Update annotation.
     */
    public void setUpdateMethod(String UpdateMethodName) throws NonExistantMethodException, NonExistantUpdateAnnotationException {
        // init
        Method[] method = this.getClass().getDeclaredMethods();
        boolean found = false;
        int i = 0;
        // for each method
        while (!found && i<method.length) {
            // if it is the named method
            if (method[i].getName().equals(UpdateMethodName)) {
                // if the method has an update annotation
                if (method[i].isAnnotationPresent(Update.class)) {
                    // set it as the current update method
                    this.updateMethod = method[i];
                    this.updateMethodName = UpdateMethodName;
                    found = true;
                } else {
                    throw new NonExistantUpdateAnnotationException();
                }
            }
            i++;
        }
        // if the update method was not found, throw an exception
        if (!found) {
            throw new NonExistantMethodException();
        }
        // create an input property table for the new update method
        try {
            this.inputTable.generateInputs(this.updateMethod);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_CHANGE));
    }
    
    /**
     * Set the visibility state of the object.
     * @param State Visibility state of the object.  True if the object should be displayed, false otherwise.
     */
    public void setVisible(boolean State) {
        this.visible = State;
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_CHANGE));
    }
    
    /**
     * Update the state of this Element.
     * @return True if the update succeeded, false otherwise.
     */
    public boolean update() {
        // init
        boolean result = false;
        // if an update method has been set
        if (this.updateMethod != null) {
            // the update method has been set but the input table has not been primed;
            // we can not update, so return false to signal that the update has not succeeded
            if (!this.inputTable.isPrimed()) {
                return false;
            }
            // get update method arguements
            Object[] args = this.inputTable.getInputValues();
            // invoke the update method
            try {
                Object updateresult = this.updateMethod.invoke(this,args);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // no update method set so just pass by for now
            result = true;
        }
        System.out.println("INFO: Update on " + this.toString() + ": " + result);
        // return result
        return result;
    }   

    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ConfigManager.EVENT_INPUT_CHANGE:
                    // check to see if our view state parameters should be changed
                    // signal viewers to redraw
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_ELEMENT_CHANGE));
                    break;
            }
        }
    }
    
} // end class
