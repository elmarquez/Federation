/**
 * ExpressionSolver.java
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Takes a user defined expression string, builds an expression tree to 
 * represent the expression, and attempts to resolve the expression value.
 * Determines dependancies for the expression tree.
 * 
 * @author Davis Marques
 * @version 0.1.0
 */
public class ExpressionSolver implements Serializable {
    
    //--------------------------------------------------------------------------


    private Expression expressionTree; // expressionTree tree
    private IContext context;          // the collection of NamedObjects accessible by this solver
    private Class  resultClass;        // the class of the expressionTree result object
    private Object resultObj;          // the expressionTree result
    private HashSet dependancies;     // SystolicArrayElements that the expressionTree is dependant upon
    
    //--------------------------------------------------------------------------


    /**
     * ExpressionSolver constructor.
     * TODO: this is problematic because the user may call an uninitialized solver, resulting in an uncaught exception
     */
    public ExpressionSolver() {
        this.expressionTree = null;
        this.context = null;
        this.resultClass = null;
        this.resultObj = null;
        this.dependancies = new HashSet();
    }

    /**
     * ExpressionSolver constructor.
     * @param MyExpression User defined expressionTree.
     * @param Context The collection of NamedObjects that are accessible to this solver.
     */
    public ExpressionSolver(String MyExpression, IContext Context) {
        // remove all whitespace from the expression
        MyExpression = MyExpression.replace(" ","");
        // build the expression tree
        expressionTree = buildExpressionTree(MyExpression, Context);
        // build the list of unique dependancies for the expression tree
        this.dependancies = new HashSet();
        Iterator iter = this.expressionTree.getDependancies().iterator();
        while (iter.hasNext()) {
           this.dependancies.add(iter.next());
        }
    }

    //--------------------------------------------------------------------------


    /**
     * Build an expression tree from the user specified statement.
     * @param Statement A user specifed statement.
     * @param Context The NamedObjects that are accessible to this solver.
     * @return An expression tree.
     */
    private Expression buildExpressionTree(String Statement, IContext Context) {
        // init
        Expression result = null;
        // build expression tree
        if (Statement.length() > 0 && Context != null) {
            result = new Expression(Statement, Context);
        }
        // return result
        return result;
    }

    /**
     * Get the list of dependancies for this expressionTree tree. Dependacies 
     * occur whenever an expressionTree is atomic and of type reference.
     * @return SystolicArrayElements upon which this expressionTree solver is dependant.
     */
    public Set getDependancies() {
        return (Set) this.dependancies;
    }

    /**
     * Returns the expressionTree result.
     * @return The expressionTree result object.
     */
    public Object getResult() {
        // init
        Object result = null;
        // if the result has not been set
        if (this.resultObj == null) {
            this.solve();
        }
        // return result
        return this.resultObj;
    }
    
    /**
     * Return the class corresponding with the expressionTree result type.
     * @return The class corresponding with the expressionTree result type.
     */
    public Class getResultClass() {
       return this.resultClass; 
    }

    /**
     * Executes a post-order traversal of the Expression tree to solve the 
     * expressionTree and set the result object.
     */
    public void solve() {
        this.resultObj = this.expressionTree.solve();
    }

} // end class
