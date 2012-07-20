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

package ca.sfu.federation.model.util;

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.util.exception.MalformedSelectionRuleException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * <p>
 * Select objects in a particular context based on a set of user specified 
 * conditions. Selection conditions are described using an SQL like syntax.
 * </p>
 * <pre>
 * SELECT <i>scope</i> WHERE <i>condition1 AND condition2 OR condition3 ...</i> {Optional Arguments}<br/>
 * </pre>
 * <p>
 * A selection statement begins with the SELECT directive and is followed by a 
 * scope designation.  The scope designation specifies which Context, that is 
 * which collection or subcollection of objects, should be considered in the 
 * selection operation.  The WHERE directive follows the scope designiation, 
 * and includes one or more selection conditions.  Selection conditions are 
 * described by comparison operations, function calls and logical operations.
 * Valid comparison operations include: ... 
 * </p>
 *
 * @author Davis Marques
 *
 * TODO: the scope setup is not correct.  it gets the right objects when parsing, but then doesn't consider the scope during update
 */
public class Selection implements Serializable {

    private static final Logger logger = Logger.getLogger(Selection.class.getName());
    
    private static final String[] REQUIRED_DIRECTIVES = {"SELECT","WHERE"};
    private static final String[] POSTPROCESSING_DIRECTIVES = {"ORDEREDBY","NEAR","LIMIT"};
    
    private String rule;            // user specified selection rule
    private IContext context;       // selection input
    private SelectionFilter filter; // selection filter
    private String scope;           // user specified scope
    private LinkedHashMap scopeobjects;  // objects in scope
    private LinkedHashMap result;        // selection result
    
    //--------------------------------------------------------------------------


    /**
     * Selection constructor.
     * @param Query A selection Query given in the selection syntax.
     * @param MyContext The context in which the selection operation should occur.
     * @throws MalformedSelectionRuleException The selection query is not well formed.
     */
    public Selection(String Query, IContext MyContext) throws MalformedSelectionRuleException {
        this.context = MyContext;
        // create the selection filter tree
        this.parse(Query,MyContext);
    }
    
    //--------------------------------------------------------------------------


    /**
     * Parse the input Query.
     * @param Query A selection Query given in the selection syntax.
     * @param MyContext The context in which the selection operation should occur.
     * @throws IllegalArgumentException sThe selection query is not well formed.
     */
    private void parse(String Query, IContext MyContext) throws IllegalArgumentException {
        // init
        this.rule = Query.trim();
        // split rule string into atoms; statement should be atleast 4 atoms long to be valid
        // ex. SELECT * WHERE name=*
        String[] atoms = rule.split(" ");
        if (atoms.length<4) {
            throw new IllegalArgumentException("Required SELECT or WHERE directives missing.");
        }
        // SELECT directive must be the first atom, WHERE must be third
        if (!atoms[0].equals("SELECT") && !atoms[2].equals("WHERE")) {
            throw new IllegalArgumentException("Required SELECT or WHERE directive malformed.");
        }
        // SCOPE (aka context) is second atom; try to get the collection of objects in the scope
        this.scope = atoms[1].trim();
        if (this.scope.equals("*")) {
            this.scopeobjects = (LinkedHashMap) MyContext.getElementMap();
        } else {
            // scope not in the current context, so try to get the named context
            int selectall = this.scope.lastIndexOf(".*");
            if (selectall != -1) {
                scope = this.scope.substring(0,selectall);
            }
            try {
                Object obj = MyContext.lookup(scope);
                if (selectall != -1) {
                    if (obj instanceof IContext) {
                        IContext context = (IContext) obj;
                        this.scopeobjects = (LinkedHashMap) context.getElementMap();
                    } else {
                        // TODO: can not do a subselection on this object. throw error
                    }
                } else {
                    if (obj instanceof INamed) {
                        INamed named = (INamed) obj;
                        LinkedHashMap objs = new LinkedHashMap();
                        objs.put(named.getName(),named);
                        this.scopeobjects = objs;
                    } else {
                        // TODO: reference must have been to a property, so throw an error
                    }
                }
            } catch (IllegalArgumentException ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"{0}",stack);
            }
        }
        // CONDITIONS go from third atom to the next directive in the list, if one is present
        // The tree is built in units of Boolean statements, from the leaf nodes to the root.  
        // The last statement in the input string becomes the root of the tree.
        
        // identify the subsegment of the query that has the conditions and post processing directives
        ArrayList conditionsv = new ArrayList();
        ArrayList postprocessingv = new ArrayList();
        int index = 3; // start from WHERE directive
        boolean found = false;
        while (!found && index < atoms.length) {
            int in = 0;
            while (!found && in < Selection.POSTPROCESSING_DIRECTIVES.length) {
                if (Selection.POSTPROCESSING_DIRECTIVES[in].equals(atoms[index])) {
                    found = true;
                }
                in++;
            }
            if (!found) {
                index++;
            }
        }
        for (int i=3;i<index && i<atoms.length;i++) {
            conditionsv.add(atoms[i]);
        }
        String[] conditions = new String[conditionsv.size()];
        conditionsv.toArray(conditions);
        
        // Build the selection filter tree from the conditions list
        found = false;
        int j = 0;
        if (conditions.length==1) {
            // single condition
            this.filter = new SelectionFilter(conditions[0]);
        } else {
            // multiple conditions
            while (j < conditions.length) {
                // find the first boolean statement in the remaining input string.  
                // the elements to the left and right become the leaf nodes of the first tree.
                String statement = conditions[j];
                boolean isBool = SelectionFilter.isBoolean(statement);
                if (isBool && this.filter == null) {
                    // this is the first boolean statement in the list
                    String[] sub = new String[3];
                    sub[0] = conditions[j-1];
                    sub[1] = conditions[j];
                    sub[2] = conditions[j+1];
                    this.filter = new SelectionFilter(sub);
                    j += 2;
                } else if (isBool) {
                    // any following boolean statement
                    String[] sub = new String[2];
                    sub[0] = conditions[j];
                    sub[1] = conditions[j+1];
                    this.filter = new SelectionFilter(this.filter,sub);
                    j += 2;
                }
                j++;
            }
        }
        
        // POST-PROCESSING DIRECTIVES follow the selection conditions.
        // identify the subsegment of the query that has post processing directives
        for (int i=index;i<atoms.length;i++) {
            postprocessingv.add(atoms[i]);
        }
        String[] postprocessing = new String[postprocessingv.size()];
        postprocessingv.toArray(postprocessing);
        // TODO: post processing
    }
    
    /**
     * Update the 
     * @return Collection of objects which meet the user specified selection criteria.
     */
    public Map update() {
        // get the list of objects from the context
        LinkedHashMap inputset = (LinkedHashMap) this.scopeobjects;
        // filter the inputset
        LinkedHashMap outputset = (LinkedHashMap) this.filter.filter(inputset);
        // return results
        return outputset;
    }
    
} 
