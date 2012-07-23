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
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Action to create a new parametric model.
 * @author Davis Marques
 */
public class CreateProjectAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(CreateProjectAction.class.getName());

    //--------------------------------------------------------------------------

    /**
     * CreateProjectAction default constructor.
     */
    public CreateProjectAction() {
        super("New Project", null);
        Icon icon = ImageIconUtils.loadIconById("file-new-project-icon");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift N"));
        this.putValue(Action.LONG_DESCRIPTION, "Create a new project");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        this.putValue(Action.SHORT_DESCRIPTION, "Create a new project");
        this.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * CreateProjectAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public CreateProjectAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        this.putValue(Action.LONG_DESCRIPTION,ToolTip);
        this.putValue(Action.MNEMONIC_KEY,MnemonicId);
        this.putValue(Action.SHORT_DESCRIPTION,ToolTip);
        this.putValue(Action.SMALL_ICON, MyIcon);
    }

    //--------------------------------------------------------------------------

    /**
     * Handle action performed event.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ParametricModel model = initDemoModel();
        Application.getContext().setModel(model);
        // updating the model for the first time
        // logger.log(Level.INFO,"Updating the model state");
        // model.update();
    }

    /**
     * Create a demo model.
     */
    private ParametricModel initDemoModel() {
        logger.log(Level.INFO,"Creating a sample model");
        ParametricModel model = new ParametricModel();
        model.setName("mymodel");
        try {
            // scenario 0
            Scenario sc0 = new Scenario("scenario0");
            sc0.registerInContext(model);

            Assembly a0 = new Assembly("myAssembly0");
            a0.registerInContext(sc0);

            CoordinateSystem cs1 = new CoordinateSystem("baseCS");
            Point p11 = new Point("Point01");
            Point p12 = new Point("Point02");
            Line line11 = new Line("Line01");
            cs1.registerInContext(a0);
            p11.registerInContext(a0);
            p12.registerInContext(a0);
            line11.registerInContext(a0);

            cs1.setUpdateMethod("updateDefault");
            p11.setUpdateMethod("updateByCSAndCoordinates");
            p12.setUpdateMethod("updateByCSAndCoordinates");
            line11.setUpdateMethod("updateByPoints");

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

            Assembly a1 = new Assembly("myAssembly1");
            CoordinateSystem cs2 = new CoordinateSystem("baseCS");
            Point p21 = new Point("Point01");
            Point p22 = new Point("Point02");
            Line line21 = new Line("Line01");

            a1.registerInContext(sc0);
            cs2.registerInContext(a1);
            p21.registerInContext(a1);
            p22.registerInContext(a1);
            line21.registerInContext(a1);

            cs2.setUpdateMethod("updateDefault");
            p21.setUpdateMethod("updateByCSAndCoordinates");
            p22.setUpdateMethod("updateByCSAndCoordinates");
            line21.setUpdateMethod("updateByPoints");

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

            // scenario1
            Scenario sc1 = new Scenario("scenario1");
            sc1.registerInContext(model);
            sc1.addContextual(a0);
            sc1.addContextual(a1);

            Assembly a2 = new Assembly("myAssembly1");
            a2.registerInContext(sc1);

            // scenario2
            Scenario sc2 = new Scenario("scenario2");
            sc2.registerInContext(model);
            sc2.addContextual(a2);

            Assembly a3 = new Assembly("myAssembly3");
            a3.registerInContext(sc2);

            CoordinateSystem cs3 = new CoordinateSystem("baseCS");
            Point p31 = new Point("Point01");
            Point p32 = new Point("Point02");
            Line line31 = new Line("Line01");
            cs3.registerInContext(a3);
            p31.registerInContext(a3);
            p32.registerInContext(a3);
            line31.registerInContext(a3);

            cs3.setUpdateMethod("updateDefault");
            p31.setUpdateMethod("updateByCSAndCoordinates");
            p32.setUpdateMethod("updateByCSAndCoordinates");
            line31.setUpdateMethod("updateByPoints");

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

            Assembly a4 = new Assembly("myAssembly4");
            a4.registerInContext(sc2);

            CoordinateSystem cs4 = new CoordinateSystem("baseCS");
            Point p41 = new Point("Point01");
            Point p42 = new Point("Point02");
            Line line41 = new Line("Line01");

            cs4.registerInContext(a4);
            p41.registerInContext(a4);
            p42.registerInContext(a4);
            line41.registerInContext(a4);

            cs4.setUpdateMethod("updateDefault");
            p41.setUpdateMethod("updateByCSAndCoordinates");
            p42.setUpdateMethod("updateByCSAndCoordinates");
            line41.setUpdateMethod("updateByPoints");

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

            // behaviors
            Behavior behavior = new Behavior();
            // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' LIMIT 1");
            // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' OR X>5 AND Y<3 LIMIT 1");
            behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4'");
            // behavior.setUpdateAction("");
            // Step 13: Remove the target assembly.  Display the assembly state.

        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not set update method\n{0}", stack);
        }

        return model;
    }

}