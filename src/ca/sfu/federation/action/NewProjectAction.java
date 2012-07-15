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
package ca.sfu.federation.action;

import ca.sfu.federation.Application;
import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.Behavior;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.geometry.CoordinateSystem;
import ca.sfu.federation.model.geometry.Line;
import ca.sfu.federation.model.geometry.Point;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Action to create a new parametric model.
 * @author Davis Marques
 */
public class NewProjectAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(NewProjectAction.class.getName());
    
    //--------------------------------------------------------------------------
    
    /**
     * NewProjectAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public NewProjectAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        this.putValue(Action.LONG_DESCRIPTION,ToolTip);
        this.putValue(Action.MNEMONIC_KEY,MnemonicId);
        this.putValue(Action.SHORT_DESCRIPTION,ToolTip);
        Icon icon = new ImageIcon("/ca/sfu/federation/resources/icons/behavior-icon.gif");
        this.putValue(Action.SMALL_ICON, icon);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Action performed event handler.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        logger.log(Level.INFO,"Creating a sample model");
        ParametricModel model = initDemoModel();
        Application.getContext().setModel(model);
        // updating the model for the first time
        logger.log(Level.INFO,"Updating the model state");
        model.update();
    }
    
    /**
     * Create a demo model.
     */
    private ParametricModel initDemoModel() {
        ParametricModel model = new ParametricModel();
        // scenario 0
        Scenario sc0 = new Scenario("scenario0",model);
        Assembly a0 = new Assembly("myAssembly0",sc0);
        CoordinateSystem cs1 = new CoordinateSystem("baseCS",a0);
        Point p11 = new Point("Point01",a0);
        Point p12 = new Point("Point02",a0);
        Line line11 = new Line("Line01",a0);
        try {
            cs1.setUpdateMethod("updateDefault");
            p11.setUpdateMethod("updateByCSAndCoordinates");
            p12.setUpdateMethod("updateByCSAndCoordinates");
            line11.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update method\n{0}", stack);
        }
        p11.setInput("MyCoordinateSystem","baseCS");
        p11.setInput("X","0.5");
        p11.setInput("Y","0.5");
        p11.setInput("Z","0.5");
        p12.setInput("MyCoordinateSystem","baseCS");
        p12.setInput("X","3.0");
        p12.setInput("Y","0.5");
        p12.setInput("Z","0.5");
        line11.setInput("StartPoint","Point01");
        line11.setInput("EndPoint","Point02");
        Assembly a1 = new Assembly("myAssembly1",sc0);
        CoordinateSystem cs2 = new CoordinateSystem("baseCS",a1);
        Point p21 = new Point("Point01",a1);
        Point p22 = new Point("Point02",a1);
        Line line21 = new Line("Line01",a1);
        try {
            cs2.setUpdateMethod("updateDefault");
            p21.setUpdateMethod("updateByCSAndCoordinates");
            p22.setUpdateMethod("updateByCSAndCoordinates");
            line21.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update method\n\n{0}", stack);
        }
        p21.setInput("MyCoordinateSystem","baseCS");
        p21.setInput("X","0.5");
        p21.setInput("Y","2.0");
        p21.setInput("Z","0.5");
        p22.setInput("MyCoordinateSystem","baseCS");
        p22.setInput("X","3.0");
        p22.setInput("Y","2.0");
        p22.setInput("Z","0.5");
        line21.setInput("StartPoint","Point01");
        line21.setInput("EndPoint","Point02");
        boolean success = a0.update();
        success = a1.update();
        // scenario1
        Scenario sc1 = new Scenario("scenario1",model);
        try {
            sc1.addContextual(a0);
            sc1.addContextual(a1);
        } catch (IllegalArgumentException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not add contextual elements\n\n{0}", stack);
        }
        Assembly a2 = new Assembly("myAssembly1",sc1);
        // scenario2
        Scenario sc2 = new Scenario("scenario2",model);
        try {
            sc2.addContextual(a2);
        } catch (IllegalArgumentException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not add contextual element\n\n{0}", stack);
        }
        Assembly a3 = new Assembly("myAssembly3",sc2);
        CoordinateSystem cs3 = new CoordinateSystem("baseCS",a3);
        Point p31 = new Point("Point01",a3);
        Point p32 = new Point("Point02",a3);
        Line line31 = new Line("Line01",a3);
        try {
            cs3.setUpdateMethod("updateDefault");
            p31.setUpdateMethod("updateByCSAndCoordinates");
            p32.setUpdateMethod("updateByCSAndCoordinates");
            line31.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update methods\n\n{0}", stack);
        }
        p31.setInput("MyCoordinateSystem","baseCS");
        p31.setInput("X","1.0");
        p31.setInput("Y","2.0");
        p31.setInput("Z","3.0");
        p32.setInput("MyCoordinateSystem","baseCS");
        p32.setInput("X","6.0");
        p32.setInput("Y","7.0");
        p32.setInput("Z","8.0");
        line31.setInput("StartPoint","Point01");
        line31.setInput("EndPoint","Point02");
        Assembly a4 = new Assembly("myAssembly4",sc2);
        CoordinateSystem cs4 = new CoordinateSystem("baseCS",a4);
        Point p41 = new Point("Point01",a4);
        Point p42 = new Point("Point02",a4);
        Line line41 = new Line("Line01",a4);
        try {
            cs4.setUpdateMethod("updateDefault");
            p41.setUpdateMethod("updateByCSAndCoordinates");
            p42.setUpdateMethod("updateByCSAndCoordinates");
            line41.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update methods\n\n{0}", stack);
        }
        p41.setInput("MyCoordinateSystem","baseCS");
        p41.setInput("X","1.0");
        p41.setInput("Y","2.0");
        p41.setInput("Z","3.0");
        p42.setInput("MyCoordinateSystem","baseCS");
        p42.setInput("X","6.0");
        p42.setInput("Y","7.0");
        p42.setInput("Z","8.0");
        line41.setInput("StartPoint","Point01");
        line41.setInput("EndPoint","Point02");
        Behavior behavior = new Behavior(a3);
        // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' LIMIT 1");
        // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' OR X>5 AND Y<3 LIMIT 1");
        behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4'");
        // behavior.setUpdateAction("");
        // Step 13: Remove the target assembly.  Display the assembly state.
        sc2.remove(a4);
        // Step 14: Add the target assembly back in.  Display the assembly state.
        a4 = new Assembly("myAssembly4",sc2);
        cs4 = new CoordinateSystem("baseCS",a4);
        p41 = new Point("Point01",a4);
        p42 = new Point("Point02",a4);
        line41 = new Line("Line01",a4);
        try {
            cs4.setUpdateMethod("updateDefault");
            p41.setUpdateMethod("updateByCSAndCoordinates");
            p42.setUpdateMethod("updateByCSAndCoordinates");
            line41.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update methods\n\n{0}", stack);
        }
        p41.setInput("MyCoordinateSystem","baseCS");
        p41.setInput("X","1.0");
        p41.setInput("Y","2.0");
        p41.setInput("Z","3.0");
        p42.setInput("MyCoordinateSystem","baseCS");
        p42.setInput("X","6.0");
        p42.setInput("Y","7.0");
        p42.setInput("Z","8.0");
        line41.setInput("StartPoint","Point01");
        line41.setInput("EndPoint","Point02");
        // scenario3
        Scenario sc3 = new Scenario("scenario3",model);
        try {
            sc3.addContextual(a2);
        } catch (IllegalArgumentException ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not add element\n\n{0}", stack);
        }
        
        return model;
    }

} 