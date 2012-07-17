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

import ca.sfu.federation.model.exception.ReturnTypeMismatchException;
import java.io.Serializable;
import java.util.Set;

/**
 * User input object.
 * @author Davis Marques
 */
public class Input implements Serializable {

    private static final int STATE_INVALID = 0;
    private static final int STATE_VALID = 1;
    private static final int STATE_WARN = 2;
    
    private String name;             // input name
    private String description;      // input description
    private Class inputClass;        // input class type
    private IContext context;        // the collection of objects available to this Input
    
    private String userInput;        // user input value
    private ExpressionSolver solver; // expression solver

    //--------------------------------------------------------------------------

    /**
     * Input constructor.  The Input is not ready to use until it has been 
     * provided with the required input values. 
     * TODO: it may be possible to make an annotation that can provide the input with a default input value
     * @param Name Input name.
     * @param Description Input description.
     * @param InputClass Input value class.
     * @param Context The collection of NamedObjects available to this object.
     */
    public Input(String Name, String Description, Class InputClass, IContext Context) {
       this.name = Name;
       this.description = Description;
       this.inputClass = InputClass;
       this.context = Context;
       this.userInput = "";
       this.solver = new ExpressionSolver();
    }

    //--------------------------------------------------------------------------

    /**
     * Get expression dependancies for this Input.
     * @return List of SystolicArrayElements on which this Input is dependant.
     */
    public Set getDependancies() {
        return this.solver.getDependancies();
    }
    
    /**
     * Get Input description.
     * @return Input description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Get the Input value class.
     * @return The class of the property value.
     */
    public Class getInputClass() {
        return this.inputClass;
    }

    /**
     * Get the Input name.
     * @return The Input name.
     */
    public String getName() {
       return this.name; 
    }

    /**
     * Get the result object.
     * @return The result object.
     */
    public Object getResult() {
        this.solver.solve();
        return this.solver.getResult();
    }
    
    /**
     * Get the user defined input value.
     * @return The user defined input value.
     */
    public String getUserInput() {
        return this.userInput;
    }

    /**
     * If the Input has been provided with the required User Input value, then it is
     * primed and ready for use.
     * @return True if the Input is primed, false otherwise.
     */
    public boolean isPrimed() {
        if (this.userInput.equals("")) {
            return false;
        }
        return true;
    }
    
    /**
     * Set the user defined input value.
     * @param UserInput The user defined input value.
     * @throws ReturnTypeMismatchException The user input string resolves to a value inputClass that does not correspond with the required value inputClass.
     */
    public void setUserInput(String UserInput) throws IllegalArgumentException {
        // set the user input value
        this.userInput = UserInput;
        // create an expression solver for the user input, solve the expression
        ExpressionSolver es = new ExpressionSolver(UserInput, this.context);
        es.solve();
        // if the expression result inputClass corresponds with the required result 
        // inputClass, then assign the solver to this property, otherwise throw an error
        if (es.getResultClass() != this.inputClass) {
            this.solver = es;
        } else {
            throw new IllegalArgumentException("The expression does not resolve to the class required by the Update method.");
        }
    }

} 