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

import com.developer.rose.BeanProxy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * A user specified expression that can be resolved to a value or object.  
 * Reference expressions are resolved "in context".  This is to say that value 
 * or object references are limited to a particular Context, or subset of the 
 * total collection of NamedObjects in the application environment.
 * @author Davis Marques
 * @version 0.0.2
 */
public class Expression implements Serializable {

    private static final Logger logger = Logger.getLogger(Expression.class.getName());

    private static final String[] OPERATORS = {"!","%","^","&&","||","*","/","+","-"};
    private static final int NUMBER_LITERAL = 0;
    private static final int STRING_LITERAL = 1;
    private static final int COLLECTION = 2;
    private static final int REFERENCE = 3;
    private static final int FUNCTION = 4;
    private static final int COMPOUND = 5; // literals, references, or function calls joined with an operator

    private String term;             // expression term (ie. the operator or function name)
    private Expression[] parameters; // expression parameters; n-ary operator model
    private int type;                // expression type
    private IContext context;        // the collection of NamedObjects available to the expression for performing computations
    
    //--------------------------------------------------------------------------
    
    /**
     * Expression constructor.  Context object will only be required when the 
     * expression is a reference, or when the expression is compound and we need 
     * to parseCompoundExpression it further.
     * @param MyExpression A user specified expression in the expression language.
     * @param Context The set of NamedObjects that are available for computation to this expression.
     * @throws IllegalArgumentException The user specified expression can not be parsed, or the references can not be resolved in context.
     */
    public Expression(String MyExpression, IContext Context) throws IllegalArgumentException {
        // classify the expression, set the term/parameter/type values
        if (isStringLiteral(MyExpression)) {
            MyExpression = MyExpression.substring(1); // clip the first quote character
            MyExpression = MyExpression.substring(0,MyExpression.length()-1); // clip the last quote character
            this.term = MyExpression;
            this.parameters = new Expression[0]; // no parameters required
            this.type = Expression.STRING_LITERAL;
        } else if (isNumberLiteral(MyExpression)) {
            this.term = MyExpression;
            this.parameters = new Expression[0]; // no parameters required
            this.type = Expression.NUMBER_LITERAL;
        } else if (isCollection(MyExpression)) {
            // TODO: collection expression stub
        } else if (isReference(MyExpression)) {
            this.term = MyExpression;
            this.parameters = new Expression[0]; // no parameters required
            this.context = Context;
            this.type = Expression.REFERENCE;
        } else if (isFunction(MyExpression)) {
            // TODO: function expression stub
        } else if (isCompound(MyExpression)) {
            this.type = Expression.COMPOUND;
            // parseCompoundExpression the expression into components
            ArrayList components = parseCompoundExpression(MyExpression,Context);
            // assign expression components to term and parameter fields
            this.term = (String) components.get(0);
            ArrayList params = (ArrayList) components.get(1);
            this.parameters = new Expression[params.size()];
            params.toArray(this.parameters);
        } else {
            // the expression can not be classified, throw an error
            throw new IllegalArgumentException("The expression type could not be determined.");
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Get dependancies for this expression.
     * @return Elements on which this Expression is dependant.
     */
    public List getDependancies() {
        // init
        ArrayList dependancies = new ArrayList();
        // get dependancies for subexpressions first
        for (int i=0;i<this.parameters.length;i++) {
            ArrayList v = (ArrayList) parameters[i].getDependancies();
            dependancies.addAll(v);
        }
        // get dependancies for self
        if (this.type == Expression.REFERENCE) {
            // get element reference
            INamed named = (INamed) this.context.lookup(this.term);
            if (named != null) {
                dependancies.add(named);
            }
        }
        // return result
        return (List) dependancies;
    }
    
    /**
     * Resolve the value for a particular parameter.
     * @param Parameter The parameter.
     * @return The result value.
     */
    private Object getParameterValue(Object Parameter) {
        // init
        Object result = null;
        // if the parameter is an expression
        if (Parameter.getClass().isInstance(Expression.class)) {
            // get the expression value
            Expression exp = (Expression) Parameter;
            try {
                result = exp.solve();
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"Could not solve expression\n\n{0}",stack);
            }
        } else {
            result = Parameter;
        }
        // return result
        return result;
    }
    
    /**
     * If any parameters are expressions or object references, resolve their values first.
     * @return Return an array with the resolved parameter values.
     */
    private Object[] getParameterValues() {
        // initialize
        Object[] result = new Object[this.parameters.length];
        // for each parameter in the current expression
        for (int i=0;i<parameters.length;i++) {
            result[i] = this.getParameterValue(parameters[i]);
        }
        // return result
        return result;
    }
    
    /**
     * Get the expression term.
     * @return The expression term, which is either an operator or a value reference.
     */
    public String getTerm() {
        return term;
    }
    
    /**
     * Determines if the user statement is a collection.
     * @param s User defined statement.
     * @return True if the statement is a collection, false otherwise.
     */
    private static boolean isCollection(String s) {
        // TODO: stub
        return false;
    }

    /**
     * Determines if the statement has an operator and is therefore a compound
     * statement.
     * @param Statement 
     * @return True if the statement contains an operator, false otherwise.
     */
    private static boolean isCompound(String Statement) {
        // check statement for operators
        boolean found = false;
        int index = 0;
        while (!found && index < Expression.OPERATORS.length) {
            if (Statement.contains(Expression.OPERATORS[index])) {
                found = true;
            }
            index++;
        }
        // return result
        return found;
    }

    /**
     * Determines if the user statement is a function call.
     * @param s User defined statement.
     * @return True if the statement is a function, false otherwise.
     */
    private static boolean isFunction(String s) {
        // TODO: stub
        return false;
    }

    /**
     * Determines if this expression term is a string literal value.
     * @return True if the user defined string can be resolved as a string or number value, false if not.
     * TODO: may want to check if the string can be converted into an integer before double conversion
     */
    public boolean isStringLiteral() {
        return isStringLiteral(this.term);
    }

    /**
     * Determines if the string represents a string literal value.
     * @return True if a string literal value, false otherwise.
     */
    private static boolean isStringLiteral(String s) {
        // initialize
        boolean result = false;
        // if bracketed by quotes
        if (s.startsWith("\"") && s.endsWith("\"") || 
            s.startsWith("'") && s.endsWith("'")) {
            result = true;
        }
        // return result
        return result;
    }

    /**
     * Determines if the expression term is a number value.
     * @return True if the value can be converted to a number.
     */
    private boolean isNumberLiteral() {
        return isNumberLiteral(this.term);
    }

    /**
     * Determines if a string value can be converted to a number.
     * @return True if the value can be converted to a number.
     */
    private static boolean isNumberLiteral(char c) {
        return isNumberLiteral(Character.toString(c));
    }

    /**
     * Determines if the string can be converted to a number. This code is from 
     * the Java Doc, and can be found at http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Double.html#valueOf(java.lang.String)
     * @param s User defined string.
     * @return True if the value can be converted to a number.
     */
    private static boolean isNumberLiteral(String s) {
        boolean result = false;
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    = ("[\\x00-\\x20]*" +  // Optional leading "whitespace"
                "[+-]?(" + // Optional sign character
                "NaN|" +           // "NaN" string
                "Infinity|" +      // "Infinity" string
                // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|" +
                // . Digits ExponentPart_opt FloatTypeSuffix_opt
                "(\\.("+Digits+")("+Exp+")?)|" +
                // Hexadecimal strings
                "((" +
                // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "(\\.)?)|" +
                // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
                ")[pP][+-]?" + Digits + "))" +
                "[fFdD]?))" +
                "[\\x00-\\x20]*");// Optional trailing "whitespace"
        if (Pattern.matches(fpRegex, s))
            result = true;
        else {
            result = false;
        }
        // return result
        return result;
    }

    /**
     * If the term is equal to any operator, then it is an operator.
     * @return True if the expression term is an operator.
     */
    public boolean isOperator() {
        return isOperator(this.term);
    }

    /**
     * If the term is equal to any operator, then it is an operator.
     * @param statement
     * @return True if statement is an operator.
     */
    private static boolean isOperator(String statement) {
        // initialize
        boolean result = false;
        // if the string matches an operator, stop at the first match
        int i = 0;
        while (!result && i < Expression.OPERATORS.length) {
            if (statement.equals(Expression.OPERATORS[i])) {
                result = true;
            }
            i++;
        }
        // return results
        return result;
    }

    /**
     * A statement is a reference if it is comprised of only alphanumeric, 
     * '.' and '_' characters, the first character is a char, and it has no 
     * parameters.  No guarantee is given that the reference can be resolved to 
     * a value.
     * @return True if this expression is a reference, false otherwise.
     */
    public boolean isReference() {
        boolean result = true;
        if (!isReference(this.term) || this.parameters.length > 0) {
            result = false;
        }
        return result;
    }

    /**
     * A statement has the form of a reference if it is comprised of only 
     * alphanumeric, ('.','_','[',']') characters, and the first character is a 
     * char or the @ symbol.  No guarantee is given that the reference can be 
     * resolved.
     * @param Statement User specified statement.
     * @return True if the statement is a reference, false otherwise.
     */
    private static boolean isReference(String Statement) {
        // init
        boolean result = true;
        // check if the statement is a proper reference
        char c = Statement.charAt(0);
        if (!Character.isJavaIdentifierStart(c)) {
            result = false;
        }
        if (Statement.substring(0,1).equals("@")) {
            result = true;
        }
        // if the statement contains any operators, then it is not a reference
        int i = 0;
        while (result && i < Expression.OPERATORS.length) {
            if (Statement.contains(Expression.OPERATORS[i])) {
                result = false;
            }
            i++;
        }
        // if the statement contains punctuation other than ('@','.','_','[',']'), then it is not a reference
        if (Statement.length()>1) {
            for (i=1;i<Statement.length();i++) {
                char ch = Statement.charAt(i);
                if (!Character.isJavaIdentifierPart(ch)) {
                    result = false;
                }
            }
        }
        // return result
        return result;
    }
    
    /**
     * Splits the user specified expression into left hand and right hand sides 
     * relative to the first operator found.  If there is a grouping within the 
     * expression, it splits the expression at the highlest level grouping, at 
     * the first operator found.  Returns a ArrayList with the operator as the 
     * first element, and any parameters as the remaining elements of the 
     * ArrayList ordered from left to right.
     *
     * TODO: should throw an exception if the statement structure is incorrect
     * TODO: need to take into account operator precedence, grouping
     *
     * @param MyExpression User defined expression.
     * @param Context The set of NamedObjects available to this expression for computation.
     * @return ArrayList with term as first element, and parameters as additional elements.
     */
    private static ArrayList parseCompoundExpression(String MyExpression, IContext Context) {
        // init
        String s, o;
        String lhs, rhs; // left, right hand side expressions
        String term; // operator
        int rhsStartIndex = 0;
        ArrayList result = new ArrayList();
        ArrayList params = new ArrayList();
        // if there are groupings in this expression
        if (MyExpression.contains("(")) {
            // TODO: grouping
        } else {
            // find the first operator in the expression
            boolean found = false;
            int index = 0;
            int opindex = 0;
            // for each char in the expression string
            while (!found && index < MyExpression.length()) {
                s = String.valueOf(MyExpression.charAt(index));
                opindex = 0;
                // check if the char is an operator
                while (!found && opindex<Expression.OPERATORS.length) {
                    o = Expression.OPERATORS[opindex];
                    // some operators are two chars long; just use the first char for the comparison
                    if (o.length()>1) {
                        o = String.valueOf(o.charAt(0));
                    }
                    // if the expression char equals the operator then we have found the first operator instance
                    if (s.equals(o)) {
                        found = true;
                    }
                    opindex++;
                }
                index++;
            }
            // TODO: in theory, we have already confirmed that this expression has an operator so we shouldn't have to have this here
            if (!found) {
                // throw an error
            }
            // need to decrement opindex to compensate for the trailing increment operation
            opindex--;
            index--;
            // split expression into left and right hand sides
            term = Expression.OPERATORS[opindex];
            lhs = MyExpression.substring(0,index);
            rhs = MyExpression.substring(index+term.length());
            try {
                Expression expLhs = new Expression(lhs,Context);
                Expression expRhs = new Expression(rhs,Context);
                params.add(expLhs);
                params.add(expRhs);
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"Could not solve expression\n\n{0}",stack);
            }
            result.add(term);
            result.add(params);
        }
        // return result
        return result;
    }

    /**
     * Executes a post-order traversal of the expression tree to solve the user
     * defined expression.
     * @return Result object.
     */
    public Object solve() {
        // intialize
        Object result = null;
        // resolution cases
        switch (type) {
            case Expression.STRING_LITERAL:
                result = this.term;
                break;
            case Expression.NUMBER_LITERAL:
                // TODO: determine if it should be an Integer or a Double; we will assume a Double for now
                result = Double.valueOf(this.term);
                break;
            case Expression.COLLECTION:
                break;
            case Expression.REFERENCE:
                // resolve the reference value, return it
                result = this.context.lookup(this.term);
                break;
            case Expression.FUNCTION:
                // TODO: call the function, return the result value
                try {
                    // lookup the user specified function and get a Method reference for it
                    // Method function = Expression.class.getMethod(this.term, null);
                    // TODO: invoke the function, get result value
                    // function.invoke(this, null);
                } catch (Exception ex) {
                    String stack = ExceptionUtils.getFullStackTrace(ex);
                    logger.log(Level.WARNING,"Could not invoke user specified function\n\n{0}",stack);
                }
                break;
            case Expression.COMPOUND:
                if (this.term.equals("*")) {
                    result = this.multiply(this.parameters);
                } else if (this.term.equals("/")) {
                    result = this.divide(this.parameters);
                } else if (this.term.equals("+")) {
                    result = this.add(this.parameters);
                } else if (this.term.equals("-")) {
                    result = this.subtract(this.parameters);
                } 
            } 
        // return result
        return result;
    }
    
    //..........................................................................

    /**
     * Add.
     * @return Result.
     * @throws IllegalArgumentException The number of parameters provided does not match the operator requirements.
     */
    private Object add(Expression[] Parameter) throws IllegalArgumentException {
        // init
        Object result = null;
        // add values
        if (Parameter.length == 2) {
            // add the rhs to the lhs
            result = add(Parameter[0],Parameter[1]);
        } else {
            // the number of parameters does not match the operator requirements
            throw new IllegalArgumentException("The number of parameters provided does not match the number required by the operator.");
        }
        // return results
        return result;
    }
    
    /**
     * Assign.
     * @throws IllegalArgumentException The number of parameters provided does not match the operator requirements.
     */
    private void assign(Expression[] Parameters) throws IllegalArgumentException {
        if (Parameters.length == 2) {
            // lhs is the object we will assign to
            Object lhs = this.parameters[0];
            // rhs is the value that will be assigned
            Object rhs = this.getParameterValue(this.parameters[1]);
            // assign the rhs to the lhs
            try {
                assign(lhs,rhs);
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"{0}",stack);
            }
        } else {
            // the number of parameters does not match the operator requirements
            throw new IllegalArgumentException("The number of parameters provided does not match the number required by the operator.");
        }
    }

    /**
     * Divide.
     * @return Result.
     * @throws IllegalArgumentException The number of parameters provided does not match the operator requirements.
     */
    private Object divide(Expression[] Parameter) throws IllegalArgumentException {
        // init
        Object result = null;
        // divide values
        if (Parameter.length == 2) {
            // divide the lhs by the rhs
            result = divide(Parameter[0],Parameter[1]);
        } else {
            // the number of parameters does not match the operator requirements
            throw new IllegalArgumentException("The number of parameters provided does not match the number required by the operator.");
        }
        // return result
        return result;
    }
    
    /**
     * Multiply.
     * @return Result.
     * @throws IllegalArgumentException The number of parameters provided does not match the operator requirements.
     */
    private Object multiply(Expression[] Parameter) throws IllegalArgumentException {
        // init
        Object result = null;
        // multiply values
        if (Parameter.length == 2) {
            // multiply the lhs by the rhs
            result = multiply(Parameter[0],Parameter[1]);
        } else {
            // the number of parameters does not match the operator requirements
            throw new IllegalArgumentException("The number of parameters provided does not match the number required by the operator.");
        }
        // return result
        return result;
    }

    /**
     * Subtract.
     * @return Result.
     * @throws IllegalArgumentException The number of parameters provided does not match the operator requirements.
     */
    private Object subtract(Expression[] Parameter) throws IllegalArgumentException {
        // init
        Object result = null;
        // multiply values
        if (Parameter.length == 2) {
            // subtract the rhs from the lhs
            result = subtract(Parameter[0],Parameter[1]);
        } else {
            // the number of parameters does not match the operator requirements
            throw new IllegalArgumentException("The number of parameters provided does not match the number required by the operator.");
        }
        // return results
        return result;
    }
    
    //..........................................................................
    // ARITHMETIC OPERATORS

    /**
     * Determines the type of the left and right hand side objects, converts
     * them to an operable type, performs the add calculation then returns the
     * result object.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Sum of left and right hand operands.
     * TODO: throw an exception if the add operation can not be performed
     * TODO: need to consider when objects are of different types, then convert them to a common type
     */
    private static Object add(Object lhs, Object rhs) {
        // initialize
        Object result = null;
        Double dblVal = new Double(0.0);
        Integer intVal = new Integer(0);
        // determine parameter type, do calculation based on object type
        if (lhs.getClass().isInstance(dblVal) && rhs.getClass().isInstance(dblVal)) {
            // convert values to double
            Double _lhs = (Double) lhs;
            Double _rhs = (Double) rhs;
            result = add(_lhs.doubleValue(),_rhs.doubleValue());
        } else if (lhs.getClass().isInstance(intVal) && rhs.getClass().isInstance(intVal)) {
            // convert values to int
            Integer _lhs = (Integer) lhs;
            Integer _rhs = (Integer) rhs;
            result = add(_lhs.intValue(),_rhs.intValue());
        } 
        // return result
        return result;
    }

    /**
     * Adds two double values and returns result as String.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Sum of left and right hand operands.
     */
    private static Double add(double lhs, double rhs) {
        return Double.valueOf(lhs + rhs);
    }

    /**
     * Adds two int values and returns result as String.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Sum of left and right hand operands.
     */
    private static Integer add(int lhs, int rhs) {
        int value = lhs + rhs;
        return Integer.valueOf(value);
    }

    /**
     * Assigns the value of the right hand operand to the object or property 
     * referenced by the left hand operand. Performs type checking to ensure 
     * that the rhs is of the same type as the lhs.
     * TODO: this method is a big mess ... clean it up!
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @throws IllegalArgumentException The operand classes do not match.
     */
    private Object assign(Object lhs, Object rhs) throws IllegalArgumentException {
        // if lhs is a reference to a named object
        if (lhs.getClass().getName().equals("research.expressionsolver.Expression")) {
            // get object and property name
            Expression exp = (Expression) lhs;
            String[] path = exp.getTerm().split("\\.");  // is that messed up or what?? :P
            String objname = path[0];
            String objproperty = path[1];
            // get the named object or object property, try to set its value
            try {
                // create a proxy to the bean
                INamed named = (INamed) this.context.lookup(objname);
                BeanProxy proxy = new BeanProxy(named);
                // if the property class is the same as the class of the value 
                // to be assigned, assign the value to the property
                // TODO: instead, I should just check to see if they are the same type or if rhs can be cast to property type
                Object property = proxy.get(objproperty);
                String p = property.getClass().getName();
                if (rhs instanceof Double && p.equals("java.lang.Double")) {
                    proxy.set(objproperty,rhs);
                } else if (rhs instanceof Integer && p.equals("java.lang.Integer")) {
                    proxy.set(objproperty,rhs);
                } else if (rhs instanceof String && p.equals("java.lang.String")) {
                    proxy.set(objproperty,rhs);
                } else {
                    throw new IllegalArgumentException("The assignment operation could not be performed because the objects provided are not of the same type.");
                }
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"{0}",stack);
            }
        } else if (isReference((String) lhs)) {
            // do something else
        } else {
            throw new IllegalArgumentException("Assignment operation could not be peformed. Case not implemented.");
        }
        // return result        
        return "Ok.";
    }

    /**
     * Divide the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Object divide(Object lhs, Object rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Divide the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Double divide(double lhs, double rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Divide the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Integer divide(int lhs, int rhs) {
        return Integer.valueOf(0);
    }

    /**
     * Multipliy the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Object multiply(Object lhs, Object rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Multipliy the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Double multiply(double lhs, double rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Multipliy the left hand value by the right hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Integer multiply(int lhs, int rhs) {
        return Integer.valueOf(0);
    }

    /**
     * Subtract the right hand value from the left hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Object subtract(Object lhs, Object rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Subtract the right hand value from the left hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Double subtract(double lhs, double rhs) {
        return Double.valueOf(0.0);
    }

    /**
     * Subtract the right hand value from the left hand value.
     * @param lhs Left hand operand.
     * @param rhs Right hand operand.
     * @return Result value.
     */
    private static Integer subtract(int lhs, int rhs) {
        return Integer.valueOf(0);
    }

    //..........................................................................
    // LOGICAL OPERATORS
    
    private static void difference() {
        // stub
    }
    
    private static void intersection() {
        // stub
    }
    
    private static void union() {
        // stub
    }
    
} 