/**
 * SelectionFilter.java
 * * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
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

package ca.sfu.federation.model.util;

import ca.sfu.federation.model.INamed;
import com.developer.rose.BeanProxy;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Filters a collection of objects based on user specified conditions.
 *
 * @author Davis Marques
 * @version 0.1.0
 * 
 * TODO: this was a fast sketch implementation.  We need to revisit this class to evaluate its performance.
 * TODO: in condition statements, we need to differentiate between a property name and a value
 */
public class SelectionFilter implements Serializable {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    public static final int BOOLEAN_OPERATOR = 0;
    public static final int COMPARISON_OPERATOR = 1;
    public static final int FUNCTION = 2;
    public static final int NUMBER_LITERAL = 3;
    public static final int STRING_LITERAL = 4;
    public static final int REFERENCE = 5;
    
    public static final String[] BOOLEAN_OPERATORS = {"AND","OR","NOT","!"};
    public static final String[] COMPARISON_OPERATORS = {"==","!=",">","<",">=","<="};
    public static final String[] FUNCTIONS = {"INSTANCEOF","NEAREST","RANGE","HALFSPACE"};
    
    private int type;        // filter type
    private String operator; // filter operator
    private Object left;     // left operand
    private Object right;    // right operand
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * SelectionFilter constructor.
     * @param Statement User specified selection condition.  We expect that the statement is either a comparison operation or a function.
     */
    public SelectionFilter(String Statement) {
        if (isComparison(Statement)) {
            // ex. X>3
            this.parseComparison(Statement);
        } else if (isFunction(Statement)) {
            // ex. INSTANCEOF('classname')
            this.parseFunction(Statement);
            // TODO: need to check that the number of arguments matches the number of parameters of the function
        } else {
            // throw error
        }
    }
    
    /**
     * SelectionFilter constructor.
     * @param Atoms A parsed list of selection conditions.  We expect that the list begins with a Boolean operator, and is followed by a comparison operation or a function.
     */
    public SelectionFilter(String[] Atoms) {
        if (Atoms.length==3) {
            // ex. X>3 AND Y<3
            this.type = SelectionFilter.BOOLEAN_OPERATOR;
            this.operator = Atoms[1];
            this.left = new SelectionFilter(Atoms[0]);
            this.right = new SelectionFilter(Atoms[2]);
        } else {
            // throw error
        }
    }
    
    /**
     * SelectionFilter constructor.
     * @param Left A SelectionFilter.
     * @param Atoms A parsed list of selection conditions.  We expect that the list begins with a Boolean operator, and is followed by a comparison operation or a function.
     */
    public SelectionFilter(SelectionFilter Left, String[] Atoms) {
        if (Atoms.length==2) {
            // ex. AND Y<3
            this.type = SelectionFilter.BOOLEAN_OPERATOR;
            this.operator = Atoms[0];
            this.left = Left;
            this.right = new SelectionFilter(Atoms[1]);
        } else {
            // throw error
        }
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
    /**
     * Filter a collection of objects.
     * @param InputSet A Collection of objects to be filtered.
     */
    public Map filter(Map InputSet) {
        // if there are no objects to process, return nothing right away
        if (InputSet.size()==0) return InputSet;
        // init
        THashMap result = new THashMap();
        // filtering cases
        switch(this.type) {
            case SelectionFilter.BOOLEAN_OPERATOR:
                // init
                THashMap rightSet = new THashMap();
                THashMap leftSet = new THashMap();
                THashSet outputset = new THashSet();
                // solve left and right subparts individually
                if (this.right instanceof SelectionFilter) {
                    SelectionFilter filter = (SelectionFilter) this.right;
                    rightSet = (THashMap) filter.filter(InputSet);
                }
                if (this.left instanceof SelectionFilter) {
                    SelectionFilter filter = (SelectionFilter) this.left;
                    leftSet = (THashMap) filter.filter(InputSet);
                }
                // perform set operations
                if (this.operator.equals("AND")) {
                    // add to result set if object is in both left and right sets
                    Iterator iter = rightSet.values().iterator();
                    while (iter.hasNext()) {
                        INamed named = (INamed) iter.next();
                        if (leftSet.contains(named)) {
                            result.put(named.getName(),named);
                        }
                    }
                } else if (this.operator.equals("OR")) {
                    // add to result set if object is in either left or right set
                    outputset.addAll((Collection) rightSet);
                    outputset.addAll((Collection) leftSet);
                    result.putAll((Map) outputset);
                } else if (this.operator.equals("XOR")) {
                    // add to result set if object is in only one of either left or right sets
                    // TODO: XOR operator
                }
                break;
            case SelectionFilter.COMPARISON_OPERATOR:
                Iterator iter = InputSet.values().iterator();
                while (iter.hasNext()) {
                    Object object = iter.next();
                    if (meetsCondition(object)) {
                        INamed named = (INamed) object;
                        result.put(named.getName(),named);
                    }
                }
                break;
            case SelectionFilter.FUNCTION:
                // operator is the name of the method
                // need to determine if the argument is a reference, or literal value
                // solve this
                break;
        }
        // return results
        return (Map) result;
    }
    
    /**
     * Determine if a statement is a boolean operation.
     * @param Statement User statement.
     * @return True if the Statement is a boolean operation, false otherwise.
     */
    public static boolean isBoolean(String Statement) {
        // init
        boolean result = false;
        // look for operator in the statement
        int index = 0;
        boolean match = false;
        while(!match && index < SelectionFilter.BOOLEAN_OPERATORS.length) {
            // compare the operator against the statement
            String op = SelectionFilter.BOOLEAN_OPERATORS[index];
            if (Statement.contains(op)) {
                result = true;
                match = true;
            }
            index++;
        }
        // return result
        return result;
    }
    
    /**
     * Determine if a statement is a logical comparison.
     * @param Statement User statement.
     * @return True if the Statement is a logical comparison, false otherwise.
     */
    public static boolean isComparison(String Statement) {
        // init
        boolean result = false;
        // look for operator in the statement
        int index = 0;
        boolean match = false;
        while(!match && index<SelectionFilter.COMPARISON_OPERATORS.length) {
            // compare the operator against the statement
            String op = SelectionFilter.COMPARISON_OPERATORS[index];
            if (Statement.contains(op)) {
                result = true;
                match = true;
            }
            index++;
        }
        // return result
        return result;
    }
    
    /**
     * Determine if a statement is a function call.
     * @param Statement User statement.
     * @return True if the Statement is a function call, false otherwise.
     */
    public static boolean isFunction(String Statement) {
        // init
        boolean result = false;
        // look for operator in the statement
        int index = 0;
        boolean match = false;
        while(!match && index<SelectionFilter.FUNCTIONS.length) {
            // compare the operator against the statement
            String op = SelectionFilter.FUNCTIONS[index];
            if (Statement.contains(op)) {
                // TODO: if the function call is followed by parameters enclosed in braces then it is a properly formed statement
                result = true;
                match = true;
            }
            index++;
        }
        // return result
        return result;
    }
    
    /**
     * Returns true if the string represents a legal Java identifier.
     * Source: http://javaalmanac.com/egs/java.lang/IsJavaId.html
     * @param S An identifier string.
     * @return True if S is a legal Java identifier, false otherwise.
     */
    public static boolean isJavaIdentifier(String S) {
        if (S.length() == 0 || !Character.isJavaIdentifierStart(S.charAt(0))) {
            return false;
        }
        for (int i=1; i<S.length(); i++) {
            if (!Character.isJavaIdentifierPart(S.charAt(i))) {
                return false;
            }
        }
        return true;
    }    
    
    /**
     * Determine whether operand is a property name reference.
     * @return True if the operand is a property name reference, false otherwise.
     */
    private static boolean isReference(Object Obj) {
        // init
        boolean result = false;
        if (Obj instanceof String) {
            String s = (String) Obj;
            if (isJavaIdentifier(s)) {
                result = true;
            }
        }
        // return result
        return true;
    }
    
    /**
     * Determine whether operand can be converted to a number. This code is from the
     * Java Doc, and can be found at http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Double.html#valueOf(java.lang.String)
     * @param Obj Value.
     * @return True if the value can be converted to a number.
     */
    private static boolean isNumberLiteral(Object Obj) {
        // cast obj to string
        String s = String.valueOf(Obj);
        // init
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
     * Check if Obj is a literal string representation.
     * @param Obj The object to be evaluated.
     * @return True if the object is a string literal, false otherwise.
     */
    private static boolean isStringLiteral(Object Obj) {
        // init
        boolean result = false;
        // check if it is a literal string representation
        if (Obj instanceof String) {
            String s = (String) Obj;
            if (s.startsWith("\'") && s.endsWith("\'")) {
                result = true;
            }
        }
        // return results
        return result;
    }
    
    /**
     * Determine whether operand is a string literal.
     * @return True if the operand is a string literal, false otherwise.
     */
    private static boolean isStringLiteral(String S) {
        // init
        boolean result = false;
        // check if it is a literal
        if (S.startsWith("\'") && S.endsWith("\'")) {
            result = true;
        }
        // return results
        return result;
    }
    
    /**
     * Check whether the object meets the selection condition.
     * @param MyObject The object to evaluate.
     * @return True if the object meets the specified condition, false otherwise.
     */
    private boolean meetsCondition(Object MyObject) {
        // init
        boolean result = false;
        // get the named property.  If the object does not have the property, return false.
        Object leftval = null;
        try {
            // create a proxy to the bean
            BeanProxy proxy = new BeanProxy(MyObject);
            leftval = proxy.get(String.valueOf(this.left)); // the object property
        } catch (Exception ex) {
            System.out.println("ERROR: Object does not have property " + this.right);
            return result;
        }
        // if comparison value is a string, strip the quotes
        if (isStringLiteral(this.right)) {
            String s = String.valueOf(this.right);
            if (s.startsWith("\'") || s.startsWith("\"")) {
                s = s.substring(1);
            }
            if (s.endsWith("\'") || s.startsWith("\"")) {
               s = s.substring(0,s.length()-1);
            }
            String rightval = s;
            // TODO: we are only implementing equality comparison here.  need to implement the remainder
            if (leftval instanceof String) {
                if (leftval.equals(rightval)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (isNumberLiteral(String.valueOf(this.right))) {
            // number value
        } else {
            // arbitrary object
            if (leftval.getClass().equals(this.right.getClass())) {
            }
        }
        // return result
        return result;
    }
    
    /**
     * Parse a comparison statement into parts.
     * @param Statement
     */
    private void parseComparison(String Statement) {
        // init
        String[] result = new String[3];
        // locate boolean operator
        boolean found = false;
        int index = 0;
        int i = -1;
        while (!found) {
            String op = SelectionFilter.COMPARISON_OPERATORS[index];
            if (Statement.contains(op)) {
                i = Statement.indexOf(op);
                this.type = SelectionFilter.COMPARISON_OPERATOR;
                this.operator = op;
                this.left = Statement.substring(0,i); // left hand arg
                this.right = Statement.substring(i+op.length(),Statement.length()); // right hand arg
                /*
                if (this.isStringLiteral(this.right)) {
                    String s = String.valueOf(this.right);
                    if (s.startsWith("\'") || s.startsWith("\"")) {
                        s = s.substring(1);
                    }
                    if (s.endsWith("\'") || s.startsWith("\"")) {
                       s = s.substring(0,s.length()-1);
                    }
                    this.right = s;
                }
                */
                found = true;
            }
            index++;
        }
    }
    
    /**
     * Parse a function statement into parts.
     * @param Statement
     * @return Array with operator, left hand argument, right hand argument.
     * TODO: stub implementation
     */
    private String[] parseFunction(String Statement) {
        // init
        String[] result = new String[3];
        int i = Statement.indexOf("(");
        this.type = SelectionFilter.FUNCTION;
        this.operator = Statement.substring(0,i);
        this.left = Statement.substring(i+1,Statement.length()-1);
        // return result
        return result;
    }
    
} // end class
