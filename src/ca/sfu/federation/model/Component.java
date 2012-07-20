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
import ca.sfu.federation.model.annotations.Update;
import ca.sfu.federation.model.exception.NonExistantMethodException;
import ca.sfu.federation.model.exception.NonExistantUpdateAnnotationException;
import ca.sfu.federation.utils.INamedUtils;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.Image;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;
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
 */
public class Component extends Observable implements IViewable, IGraphable, IUpdateable, Observer, Serializable {

    public static String DEFAULT_NAME = "Component";
    
    private static final Logger logger = Logger.getLogger(Component.class.getName());
    
    private String name;                    // the name for this node
    private String basename;                // default name prefix
    
    private IContext context;               // the parent context
    private String updateMethodName;        // the name of the user selected update method
    private transient Method updateMethod;  // the user selected update method
    private InputTable inputTable;          // inputTable corresponding with the user selected update method signature
    private IViewable result;               // displayable result object .. in theory the result may not be displayable

    // display properties
    private ImageIcon icon;                     // icon representation of component
    private Image thumbnail;                // thumbnail representation of component
    private boolean visible;                // true if the result object should be displayed
    
    //--------------------------------------------------------------------------

   /**
     * Component constructor.
     * @param Name Name
     */
    public Component(String Name) {
        // load configuration settings
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        this.name = Name;
        this.updateMethodName = null;
        this.updateMethod = null;
        this.inputTable = new InputTable(this);
        this.result = null;
        this.icon = ImageIconUtils.loadIconById("component-icon");
        this.thumbnail = Utilities.loadImage(config.getString("component-thumbnail"));
        // observe the input table for changes
        Observable o = (Observable) this.inputTable;
        o.addObserver(this);
    }
        
    //--------------------------------------------------------------------------
    
    /**
     * Delete the object from its context.
     */
    @Override
    public void delete() {
        // tell observers to release me
        logger.log(Level.INFO,"Component signalled Observers to release and delete object");
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST));
    }
    
    /**
     * Get canonical name.
     * @return Fully qualified name.
     */
    @Override
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
    @Override
    public IContext getContext() {
        return this.context;
    }
    
    /**
     * Get the Elements on which this Element is dependant.
     * @return Elements upon which this Element is dependant.
     */
    @Override
    public Map getDependancies() {
        return this.inputTable.getDependancies();
    }
    
    /**
     * Get icon representation for this Component.
     * @return Icon
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Get Update Method inputs.
     * @return Update Method inputs.
     */
    public List getInputKeys() {
        ArrayList keys = new ArrayList();
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
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get 3D renderable object representation of this component. 
     * Subclasses of Component must override this method.
     * @return Java3D Node
     */
    @Override
    public Node getRenderable() {
        return new Group();
    }

    /**
     * Get thumbnail image representation for this component.
     * @return Image.
     */
    @Override
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
     * Get the list of update methods.
     * @return Update methods
     */
    public List<Method> getUpdateMethods() {
        ArrayList<Method> updatemethods = new ArrayList<Method>();
        Method[] methods = this.getClass().getDeclaredMethods();
        // is method is annotated as an update method, add it to the list
        for (int i=0;i<methods.length;i++) {
            if (methods[i].isAnnotationPresent(Update.class)) {
                updatemethods.add(methods[i]);
            }
        }
        return updatemethods;
    }
    
    /**
     * Get the visibility state of this object.
     * @return The visibility state of this object. True if visible, false otherwise.
     */
    @Override
    public boolean getVisible() {
        return this.visible;
    }
    
    public void registerInContext(IContext Context) throws Exception {
        INamedUtils.registerInContext(Context, this);
    }

    /**
     * Restore the update method field value after deserialization.
     */
    @Override
    public void restore() {
        if (!this.updateMethodName.equals("")) {
            try {
                this.setUpdateMethod(this.updateMethodName);
            } catch (NonExistantMethodException ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"Could not set update method\n\n{0}",stack);
            } catch (NonExistantUpdateAnnotationException ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"Could not set update method\n\n{0}",stack);
            }
        }
    }
    
    /**
     * Set the Context.
     * @param MyContext The Context for the object.
     */
    @Override
    public void setContext(IContext MyContext) {
        this.context = MyContext;
    }
    
    /**
     * Set the icon.
     * @param MyIcon Icon image.
     */
    protected void setIcon(ImageIcon MyIcon) {
        this.icon = MyIcon;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ICON_CHANGE));
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
    @Override
    public void setName(String Name) {
        String old = this.name;
        this.name = Name;
        // generate change event
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_NAME_CHANGE));
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
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not get inputs\n\n{0}",stack);
        }
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_CHANGE));
    }
    
    /**
     * Set the visibility state of the object.
     * @param State Visibility state of the object.  True if the object should be displayed, false otherwise.
     */
    @Override
    public void setVisible(boolean State) {
        this.visible = State;
        // notify observers
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_CHANGE));
    }
    
    /**
     * Update the state of this Element.
     * @return True if the update succeeded, false otherwise.
     */
    @Override
    public boolean update() {
        // init
        boolean theresult = false;
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
                theresult = true;
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"Could not update state\n\n{0}",stack);
            }
        } else {
            // no update method set so just pass by for now
            theresult = true;
        }
        logger.log(Level.INFO,"Update on {0}:{1}", new Object[]{this.toString(), theresult});
        // return result
        return theresult;
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
                case ApplicationContext.EVENT_INPUT_CHANGE:
                    // check to see if our view state parameters should be changed
                    // signal viewers to redraw
                    this.setChanged();
                    this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_ELEMENT_CHANGE));
                    break;
            }
        }
    }
    
} 
