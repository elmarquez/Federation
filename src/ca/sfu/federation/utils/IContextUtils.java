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
package ca.sfu.federation.utils;

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IGraphable;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.IUpdateable;
import ca.sfu.federation.model.exception.GraphCycleException;
import com.developer.rose.BeanProxy;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Utility methods for context classes.
 * @author dmarques
 */
public class IContextUtils {

    private static final Logger logger = Logger.getLogger(IContextUtils.class.getName());

    public static Map<String,INamed> getDependancies(Map<String,INamed> ElementMap) {
        // init
        LinkedHashMap<String,INamed> results = new LinkedHashMap<String,INamed>();
        HashSet dependancies = new HashSet();
        // scenario dependancies come from contextual object references
        Iterator iter = ElementMap.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            dependancies.add(named.getContext());
        }
        // convert set to map
        iter = dependancies.iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            results.put(named.getName(),named);
        }
        // return result
        return results;
    }
    
    public static Map<String, INamed> getElementMap(List<INamed> Elements) {
        LinkedHashMap<String, INamed> map = new LinkedHashMap<String, INamed>();
        Iterator e = Elements.iterator();
        while (e.hasNext()) {
            INamed named = (INamed) e.next();
            map.put(named.getName(), named);
        }
        return map;
    }
    
    public static ArrayList<INamed> getElementsInTopologicalOrder(Map<String,INamed> ElementMap) 
            throws GraphCycleException {
        ArrayList<INamed> sorted = new ArrayList<INamed>();
        Iterator iter = ElementMap.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            if (named instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) named;
                // get dependancies for the node
                LinkedHashMap deps = (LinkedHashMap) graphobject.getDependancies();
                // for each dependancy, do a topological sort on its subgraph
                Iterator it = deps.values().iterator();
                while (it.hasNext()) {
                    INamed namedDep = (INamed) it.next();
                    getElementsInTopologicalOrder(namedDep, sorted);
                }
                // insert self into list
                if (!sorted.contains(named)) {
                    sorted.add(named);
                }
            } else {
                // what should we do with an object that does not have dependancies?
            }
        }
        // return result
        return sorted;
    }

    /**
     * Get elements in topological order.
     * @param Named Named object.
     * @param Sorted List of elements in topological order.
     * @throws GraphCycleException Graph contains a cycle and can not be updated.
     */
    private static void getElementsInTopologicalOrder(INamed Named, ArrayList Sorted) throws GraphCycleException {
        // if the NamedObject is already in the list, then we expect that its dependancies
        // are also already represented there and therefore we don't need to do anything
        if (Named instanceof IGraphable && !Sorted.contains(Named)) {
            IGraphable graphobject = (IGraphable) Named;
            // add nodes upon which the object is dependant first
            LinkedHashMap deps = (LinkedHashMap) graphobject.getDependancies();
            Iterator iter = deps.values().iterator();
            while (iter.hasNext()) {
                INamed namedDep = (INamed) iter.next();
                getElementsInTopologicalOrder(namedDep, Sorted);
            }
            // add myself
            if (!Sorted.contains(Named)) {
                Sorted.add(Named);
            } else {
                // graph has a cycle
                throw new GraphCycleException();
            }
        }
    }

    /**
     * Get the list of independant elements.
     * @return List of elements 
     */
    public static List<INamed> getIndependantElements(List<INamed> Elements) {
        ArrayList<INamed> independant = new ArrayList<INamed>();
        // get list of elements
        Iterator<INamed> e = Elements.iterator();
        while (e.hasNext()) {
            INamed named = e.next();
            // if the object can have dependancies
            if (named instanceof IGraphable) {
                IGraphable graphobject = (IGraphable) named;
                LinkedHashMap dep = (LinkedHashMap) graphobject.getDependancies();
                // if the elements has no dependancies, then it is an independant elements
                if (dep.size() == 0) {
                    independant.add(named);
                }
            }
        }
        // return results
        return independant;
    }
    
    /**
     * Get the next available element name for the given base name.
     * @param Context Context
     * @param Name Base name
     * @return 
     */
    public static String getNextName(IContext Context, String Name) {
        Map<String, INamed> map = Context.getElementMap();
        if (map.containsKey(Name)) {
            int count = 1;
            while (count>0 && count<100000) {
                String nextname = Name + "_" + count;
                if (!map.containsKey(nextname)) {
                    return nextname;
                }                     
                count++;
            }

        }
        return Name;
    }

    public static void logUpdateOrder(INamed Parent, List<INamed> Elements) {
        if (Parent != null && Elements != null) {
            Iterator en = Elements.iterator();
            StringBuilder sb = new StringBuilder();
            sb.append(Parent.getName());
            sb.append(" update event. Update sequence is: ");
            while (en.hasNext()) {
                INamed named = (INamed) en.next();
                sb.append(named.getName());
                sb.append(" ");
            }
            logger.log(Level.FINE,sb.toString()); 
        }
    }
    
    /**
     * Retrieves a single object, or object property value in the local context.
     *
     * @param Query NamedObject reference of the form objectname.propertyname
     * @throws IllegalArgumentException The referenced object could not be
     * located, or the resultant value is null.
     */
    public static Object lookup(Map<String,INamed> ElementMap, String Query) throws IllegalArgumentException {
        // initialize
        INamed named = null;
        Object result = null;
        // check to see if the Query is well formed
        if (Query.length() < 1) {
            // TODO: do checks for malformed names.  I think there is a Java library that checks names
            throw new IllegalArgumentException("An empty or null object reference was provided.");
        }
        // convert the query into a path
        String[] path = Query.split("\\.");
        // lookup the object in the local context
        if (ElementMap.containsKey(path[0])) {
            named = ElementMap.get(path[0]);
        }
        // if not found, throw an error
        if (named == null) {
            throw new IllegalArgumentException("The referenced object could not be found in the current Context.");
        }
        // lookup object subparts
        if (path.length == 1) {
            // objectname
            result = named;
        } else if (path.length == 2) {
            // objectname.propertyname
            BeanProxy proxy = null;
            try {
                // create a bean proxy object to help us access properties
                proxy = new BeanProxy(named);
                // try to retrieve the property value
                result = proxy.get(path[1]);
                // if the value is null
                if (result == null) {
                    throw new IllegalArgumentException("Property value is null.");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("Named property could not be resolved.");
            }
        } else if (path.length > 2) {
            // if the object supports IContext, then send the subquery to the object
            if (named instanceof IContext) {
                // convert path back to a normal query
                String query = "";
                for (int i = 1; i < path.length; i++) {
                    if (i + 1 < path.length) {
                        query += path[i] + ".";
                    } else {
                        query += path[i];
                    }
                }
                // cast object as IContext
                IContext context = (IContext) named;
                // try to get the object value
                try {
                    result = context.lookup(query);
                } catch (Exception ex) {
                    String stack = ExceptionUtils.getFullStackTrace(ex);
                    logger.log(Level.WARNING, "{0}", stack);
                }
            } else {
                // the reference is malformed, throw an error
                throw new IllegalArgumentException("Object " + named.getName() + " does not support IContext.  Subparts of this object can not be resolved.");
            }
        } else {
            // the reference is malformed, throw an error
            throw new IllegalArgumentException("Object reference is malformed.");
        }
        // return result
        return result;
    }

    /**
     * Restore transient and non-serializable values.
     */
    public static void restore(IContext Context) {
        // for each object, restore values
        Map map = Context.getElementMap();
        Iterator iter = map.values().iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof IUpdateable) {
                IUpdateable updateable = (IUpdateable) object;
                updateable.restore();
            }
        }
    }
    
    
}
