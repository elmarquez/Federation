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
import ca.sfu.federation.model.exception.DuplicatePropertyException;
import ca.sfu.federation.model.exception.ParameterCountMismatchException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * A table to hold Input arguments for an Update method.  Arguments are stored
 * and retrieved in the same order they appear in the Update method signature.
 * TODO: need to investigate what happens when we delete this input table, or when the parent creates and assigns a new input table to itself.  Does that create a memory leak?
 * @author Davis Marques
 * @version 0.2.0
 */
public class InputTable extends Observable implements Serializable {
    
    private static final Logger logger = Logger.getLogger(InputTable.class.getName());
    
    private ArrayList inputs;       // the inputs, in order
    private String description;  // description of the update method
    private INamed parent; // the parent object
    private boolean primed;      // all inputs have the required user input values
    
    //--------------------------------------------------------------------------
    
    /**
     * InputTable constructor.
     * @param Parent The parent object.
     */
    public InputTable(INamed Parent) {
        this.inputs = new ArrayList();
        this.description = "";
        this.parent = Parent;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Add a property to the table.
     * @param Name Input name.
     * @param Description Input description.
     * @param Clazz Input class.
     * @throws DuplicatePropertyException A property with the same name already exists in the table.
     */
    private void addInput(String Name, String Description, Class Clazz) throws DuplicatePropertyException {
        // create the new input
        Input myInput = new Input(Name, Description, Clazz, this.parent.getContext());
        // add the input to the list and index
        String name = myInput.getName();
        LinkedHashMap index = this.getInputIndex();
        if (!index.containsKey(name)) {
            this.inputs.add(myInput);
        } else {
            throw new DuplicatePropertyException();
        }
    }
    
    /**
     * Clear all automatically generated inputs.
     */
    public void clearInputs() {
        this.inputs.clear();
    }
    
    /**
     * Generate inputs for the UpdateMethod.
     * TODO: need to add a description field in the update annotation to describe each method parameter
     * @param UpdateMethod The update method.
     * @throws ParameterCountMismatchException The number of parameters in the Update annotation does not correspond with the number of parameters in the Update method signature.
     */
    public void generateInputs(Method UpdateMethod) throws ParameterCountMismatchException {
        // clear the current inputs
        this.inputs.clear();
        // get method properties
        Update updateAnnotation = UpdateMethod.getAnnotation(Update.class);
        this.description = updateAnnotation.description();
        String[] param = updateAnnotation.parameter();
        Class[] type = UpdateMethod.getParameterTypes();
        // if the number of parameters in the method signature matches the number of parameters in the annotation
        if (param.length == type.length) {
            // for each parameter, add an input to the table
            for (int i=0;i<param.length;i++) {
                try {
                    this.addInput(param[i],"Input description pulled from Update method annotation.",type[i]);
                } catch (DuplicatePropertyException ex) {
                    String stack = ExceptionUtils.getFullStackTrace(ex);
                    logger.log(Level.WARNING,"{0}",stack);
                }
            }
        } else {
            // the number of parameters in the user's Update annotation does not correspond with the method signature
            throw new ParameterCountMismatchException();
        }
    }
    
    /**
     * Get all dependancies for the Inputs in this InputTable.
     * @return SystolicArrayElements upon which this InputTable is dependant.
     * TODO: need to think about caching the dependancy list and how updates on the user input on Inputs can propagate dependancy updates to the SAE
     * TODO: need to think about whether order of dep list is significant, and therefore should be changed to a List output
     */
    public Map getDependancies() {
        // init
        HashSet dep = new HashSet();
        // add dependancies for each input to the set
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input input = (Input) e.next();
            dep.addAll((HashSet) input.getDependancies());
        }
        // convert to map
        // should revisit this at some point .. doesn't make sense any more
        LinkedHashMap result = new LinkedHashMap();
        Iterator iter = dep.iterator();
        while(iter.hasNext()) {
            INamed named = (INamed) iter.next();
            result.put(named.getName(),named);
        }
        // return result
        return (Map) result;
    }
    
    /**
     * Get dep for a particular named input.
     * @param InputName The input name.
     */
    public List getDependancies(String InputName) {
        ArrayList deps = new ArrayList();
        LinkedHashMap index = this.getInputIndex();
        Input input = (Input) index.get(InputName);
        if (input != null) {
            deps.addAll(input.getDependancies());
        }
        return (List) deps;
    }
    
    /**
     * Get Input by name.
     * @return Input
     */
    public Input getInput(String Name) {
        // init
        Input result = null;
        // if the named Input exists in the table
        LinkedHashMap index = this.getInputIndex();
        if (index.containsKey(Name)) {
            result = (Input) index.get(Name);
        }
        // return result
        return result;
    }
    
    /**
     * Create an index by name for the current inputs.
     * @return Map.
     */
    private LinkedHashMap getInputIndex() {
        // init
        LinkedHashMap index = new LinkedHashMap();
        // add all inputs to the index
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input input = (Input) e.next();
            index.put(input.getName(),input);
        }
        // return result
        return index;
    }
    
    /**
     * Get input keys.
     * @return List of keys.
     */
    public List getInputKeys() {
        ArrayList keys = new ArrayList();
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input input = (Input) e.next();
            keys.add(input.getName());
        }
        return keys;
    }
    
    /**
     * Get Inputs in order.
     * @return An array of Inputs.
     */
    public Input[] getInputs() {
        // init
        Input[] theinputs = new Input[this.inputs.size()];
        // copy inputs into array
        Iterator e = this.inputs.iterator();
        int count = 0;
        while (e.hasNext()) {
            theinputs[count] = (Input) e.next();
            count++;
        }
        // return result
        return theinputs;
    }
    
    /**
     * Get named input value.
     * @param Name
     */
    public Object getInputValue(String Name) {
        Iterator e = this.inputs.iterator();
        boolean found = false;
        while (e.hasNext()) {
            Input input = (Input) e.next();
            if (Name.equals(input.getName())) {
                return input.getUserInput();
            }
        }
        return null;
    }
    
    /**
     * Get Input values in order, cast to their proper types.
     * @return Array of property values.
     */
    public Object[] getInputValues() {
        // init
        ArrayList values = new ArrayList();
        // put Input result values in vector
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input myInput = (Input) e.next();
            values.add(myInput.getResult());
        }
        // copy result values into an array
        Object[] result = new Object[values.size()];
        values.addAll(Arrays.asList(result));
        // return result
        return result;
    }
    
    /**
     * Get Input classes.
     * @return Array of Input classes.
     */
    public Class[] getInputClass() {
        // init
        ArrayList inputclass = new ArrayList();
        Class[] result = null;
        // add class for each input to the list
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input input = (Input) e.next();
            inputclass.add(input.getInputClass());
        }
        // copy results into a class array
        result = new Class[inputclass.size()];
        inputclass.addAll(Arrays.asList(result));
        // return results
        return result;
    }
    
    /**
     * Get update method description.
     * @return Update method description.
     */
    public String getUpdateMethodDescription() {
        return this.description;
    }
    
    /**
     * Determine if the InputTable contains the named Input.
     * @return True if the input table contains the named property, false otherwise.
     */
    public boolean hasInput(String PropertyName) {
        // init
        boolean result = false;
        // check if index contains propertyname
        LinkedHashMap index = this.getInputIndex();
        if (index.containsKey(PropertyName)) {
            result = true;
        }
        // return result
        return result;
    }
    
    /**
     * Check to see if each Input in the InputTable has the required values to
     * proceed with Expression resolution.
     * @return True if primed, false otherwise.
     */
    public boolean isPrimed() {
        boolean inputIsPrimed = true;
        Iterator e = this.inputs.iterator();
        while (e.hasNext()) {
            Input input = (Input) e.next();
            if (!input.isPrimed()) {
                return false;
            }
        }
        return inputIsPrimed;
    }
    
    /**
     * Set the Input user value.
     * @param InputName The Input name.
     * @param UserInputValue The user specified input value.
     */
    public void setInput(String InputName, String UserInputValue) {
        LinkedHashMap index = this.getInputIndex();
        Input myInput = (Input) index.get(InputName);
        try {
            myInput.setUserInput(UserInputValue);
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
        // notify parent object of change
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_INPUT_CHANGE));
    }
    
} 