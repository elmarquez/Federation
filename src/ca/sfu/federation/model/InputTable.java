/**
 * InputTable.java
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

import ca.sfu.federation.model.annotations.Update;
import ca.sfu.federation.model.exception.DuplicatePropertyException;
import ca.sfu.federation.model.exception.ParameterCountMismatchException;
import ca.sfu.federation.model.ConfigManager;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

/**
 * A table to hold Input arguments for an Update method.  Arguments are stored
 * and retrieved in the same order they appear in the Update method signature.
 * TODO: need to investigate what happens when we delete this input table, or when the parent creates and assigns a new input table to itself.  Does that create a memory leak?
 * @author Davis Marques
 * @version 0.2.0
 */
public class InputTable extends Observable implements Serializable {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private Vector inputs;       // the inputs, in order
    private String description;  // description of the update method
    private INamed parent; // the parent object
    private boolean primed;      // all inputs have the required user input values
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * InputTable constructor.
     * @param Parent The parent object.
     */
    public InputTable(INamed Parent) {
        this.inputs = new Vector();
        this.description = "";
        this.parent = Parent;
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
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
        THashMap index = this.getInputIndex();
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
                } catch (DuplicatePropertyException dpe) {
                    dpe.printStackTrace();
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
        THashSet dep = new THashSet();
        // add dependancies for each input to the set
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
            dep.addAll((THashSet) input.getDependancies());
        }
        // convert to map
        // should revisit this at some point .. doesn't make sense any more
        THashMap result = new THashMap();
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
        Vector deps = new Vector();
        THashMap index = this.getInputIndex();
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
        THashMap index = this.getInputIndex();
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
    private THashMap getInputIndex() {
        // init
        THashMap index = new THashMap();
        // add all inputs to the index
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
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
        Vector keys = new Vector();
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
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
        Input[] inputs = new Input[this.inputs.size()];
        // copy inputs into array
        Enumeration e = this.inputs.elements();
        int count = 0;
        while (e.hasMoreElements()) {
            inputs[count] = (Input) e.nextElement();
            count++;
        }
        // return result
        return inputs;
    }
    
    /**
     * Get named input value.
     * @param Name
     */
    public Object getInputValue(String Name) {
        Enumeration e = this.inputs.elements();
        boolean found = false;
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
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
        Vector values = new Vector();
        // put Input result values in vector
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input myInput = (Input) e.nextElement();
            values.add(myInput.getResult());
        }
        // copy result values into an array
        Object[] result = new Object[values.size()];
        values.copyInto(result);
        // return result
        return result;
    }
    
    /**
     * Get Input classes.
     * @return Array of Input classes.
     */
    public Class[] getInputClass() {
        // init
        Vector inputclass = new Vector();
        Class[] result = null;
        // add class for each input to the list
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
            inputclass.add(input.getInputClass());
        }
        // copy results into a class array
        result = new Class[inputclass.size()];
        inputclass.copyInto(result);
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
        THashMap index = this.getInputIndex();
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
        Enumeration e = this.inputs.elements();
        while (e.hasMoreElements()) {
            Input input = (Input) e.nextElement();
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
        THashMap index = this.getInputIndex();
        Input myInput = (Input) index.get(InputName);
        try {
            myInput.setUserInput(UserInputValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // notify parent object of change
        this.setChanged();
        this.notifyObservers(Integer.valueOf(ConfigManager.EVENT_INPUT_CHANGE));
    }
    
} // end class