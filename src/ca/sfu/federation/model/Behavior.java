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

import ca.sfu.federation.model.util.Selection;
import ca.sfu.federation.model.util.exception.MalformedSelectionRuleException;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.Image;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Behaviors are concerned with the question of what should happen to a 'host' 
 * object, or what a host object should do when the world around that object 
 * changes.  Behaviors decouple the interaction between an object and its 
 * environment and provide facilities for describing what aspects of the 
 * environment should be observed and what should happen when changes of
 * interest occur.
 *
 * TODO: need to implement action
 * TODO: need to identify behaviors that are assigned to an object
 *
 * @author Davis Marques
 */
public class Behavior implements Observer, Serializable {

    private static final Logger logger = Logger.getLogger(Behavior.class.getName());
    
    private IContext context;       // the context which the behavior monitors
    private INamed host;            // the object to which this behavior is bound
    private String updateCondition; // host selection rule
    private String updateAction;    // action to take when an update event occurs
    private Selection selection;    // selector
    private boolean active;         // active if true
    
    private ImageIcon icon;             // icon representation of this object
    private Image thumbnail;        // thumbnail representation of this object
    
    //--------------------------------------------------------------------------

    /** 
     * Behavior default constructor.
     */
    public Behavior() {
        this.icon = ImageIconUtils.loadIconById("behavior-icon");
        this.thumbnail = ImageIconUtils.loadIconById("behavior-thumbnail").getImage();
    }

    //--------------------------------------------------------------------------

    /**
     * Stop listening for changes from the environment. 
     */
    public void release() {
        if (context instanceof Observable) {
            Observable observable = (Observable) this.context;
            observable.deleteObserver(this);
        }
    }

    public void setHost(INamed Named) {
        this.host = Named;
        this.context = Named.getContext();
        if (context instanceof Observable) {
            Observable observable = (Observable) context;
            observable.addObserver(this);
        }
    }
    
    /**
     * Set the update action.
     * @param Action Action to take when an update event occurs.
     */
    public void setUpdateAction(String Action) {
        this.updateAction = Action;
    }
    
    /**
     * Set the update condition rule.
     * @param Condition Update condition rule.
     */
    public void setUpdateCondition(String Condition) {
        try {
            this.selection = new Selection(Condition,this.context);
        } catch (MalformedSelectionRuleException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Malformed selection rule\n\n{0}",stack);
        }
    }
    
    /**
     * On changes to the Context, take some action based on the user provided rules.
     * @param o Observable object.
     * @param arg Update message.
     */
    public void update(Observable o, Object arg) {
        logger.log(Level.INFO,"Behavior update: on object {0}", o.toString());
        // filter the list using the user provided rules
        LinkedHashMap outputset = (LinkedHashMap) this.selection.update();
        if (outputset.size()>0) {
            logger.log(Level.INFO,"Behavior {0} found targets: ", this.toString());
        } 
        Iterator iter = outputset.values().iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            sb.append(" ");
            sb.append(named.getName());
        }
        logger.log(Level.INFO,sb.toString());
        /*
        if (this.updateCondition != null && this.updateAction != null) {
            // determine the type of the update
            System out println("INFO: Behavior update: "+o.toString()+", "+arg.toString());
            // get the set of elements in the environment; remove bound assembly self from list
            LinkedHashMap inputset = (LinkedHashMap) this.context.getElementMap();
            // filter the list using the user provided rules
            LinkedHashMap outputset = (LinkedHashMap) this.selection.update();
            // take action based on the user rules
            if (outputset.size()>0) {
                // do something
            }
        }
         */
    }

} 
