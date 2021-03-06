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
package ca.sfu.federation;

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application model, view state and related run time settings.
 * @author Davis Marques
 */
public class ApplicationContext extends Observable {

    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

    private static ParametricModel model;
    private static HashMap viewstate = new HashMap();
    private static File projectDir;

    // STRINGS
    public static final String APPLICATION_PROPERTIES = "ca/sfu/federation/resources/application";

    public static final String PROJECT_WEBSITE_URL = "https://github.com/elmarquez/Federation";
    public static final String PROJECT_HELP_WEBSITE_URL = "https://github.com/elmarquez/Federation";

    // APPLICATION APPEARANCE
    public static final Color BACKGROUND_DARK = new Color(40,40,40);
    public static final Color BACKGROUND_MEDIUM = new Color(48,48,48);
    public static final Color BACKGROUND_MEDIUM2 = new Color(75,75,75);
    public static final Color BACKGROUND_LIGHT = new Color(102,102,102);
    public static final Color BACKGROUND_LIGHT2 = new Color(135,135,135);
    public static final Color BACKGROUND_WORKSPACE = new Color(93,103,115);
    public static final Color BACKGROUND_BRIGHT = new Color(244,157,28);

    public static final Color TEXT_DARK = new Color(40,40,40);
    public static final Color TEXT_MEDIUM = new Color(197,200,205);
    public static final Color TEXT_LIGHT = new Color(255,255,255);

    //..........................................................................
    // PARAMETER KEYS

    // model parameter keys

    // viewer parameter keys
    public static final String VIEWER_CURRENT_LAYOUT = "VIEWER_CURRENT_LAYOUT";
    public static final String VIEWER_LAYOUT_PARAMETRICMODELGRAPHVIEW = "VIEWER_LAYOUT_PARAMETRICMODELGRAPHVIEW";
    public static final String VIEWER_LAYOUT_ICONTEXTGRAPHVIEW = "VIEWER_LAYOUT_ICONTEXTGRAPHVIEW";
    public static final String VIEWER_LAYOUT_ICONTEXTMODELVIEW = "VIEWER_LAYOUT_ICONTEXTMODELVIEW";
    public static final String VIEWER_ABSTRACT_TREE_EXPLORER = "VIEWER_ABSTRACT_TREE_EXPLORER";
    public static final String VIEWER_ICONTEXTVIEWER = "VIEWER_CONTEXTVIEWER";
    public static final String VIEWER_PROPERTYSHEET = "VIEWER_PROPERTYSHEET";
    public static final String VIEWER_CONSOLE = "VIEWER_CONSOLE";
    public static final String VIEWER_ICONTEXT_THUMBNAILS = "VIEWER_ICONTEXT_THUMBNAILS";

    public static final String VIEWER_LAST_MOUSERELEASE = "VIEWER_LAST_MOUSERELEASE"; //
    public static final String VIEWER_LIST = "VIEWER_LIST"; // list of open viewers
    public static final String VIEWER_CURRENT_CONTEXT = "VIEWER_CURRENT_CONTEXT"; // the current context. Not all viewers will respect the global context setting.
    public static final String VIEWER_PARAMETRICMODEL_SCENE = "VIEWER_PARAMETRICMODEL_SCENE"; //
    public static final String VIEWER_ICONTEXTVIEWER_SCENES = "VIEWER_ICONTEXTVIEWER_SCENES";
    public static final String VIEWER_SELECTION = "VIEWER_SELECTION"; // selected model objects

    //..........................................................................
    // EVENT IDS

    public static final int MODEL_LOADED = 900;
    public static final int MODEL_SAVED = 910;
    public static final int MODEL_RENAMED = 920;
    public static final int MODEL_CLOSED = 930;

    // model event ids (10.1.0999)
    public static final int EVENT_CHANGE = 1000;
    public static final int EVENT_NAME_CHANGE = 1001;
    public static final int EVENT_DESCRIPTION_CHANGE = 1002;
    public static final int EVENT_PROPERTY_CHANGE = 1003;
    public static final int EVENT_ICON_CHANGE = 1004;
    public static final int EVENT_THUMBNAIL_CHANGE = 1005;
    public static final int EVENT_INPUT_CHANGE = 1006;
    public static final int EVENT_UPDATEMETHOD_CHANGE = 1007;

    public static final int EVENT_ELEMENT_ADD = 1100;     // used only when element has been added
    public static final int EVENT_ELEMENT_RENAME = 1101;  // used only when element has been renamed
    public static final int EVENT_ELEMENT_DELETE_REQUEST = 1102;  // used by object requesting to be deleted from a context
    public static final int EVENT_ELEMENT_DELETED = 1103; // used to notify observers that an element has been deleted from the context
    public static final int EVENT_ELEMENT_CHANGE = 1104;  // used to notify observers of any changes in element collection

    // viewer event ids (2000-2999)
    public static final int EVENT_CONTEXT_CHANGE = 2000;
    public static final int EVENT_REPAINT_REQUEST = 2001;
    public static final int EVENT_STATE_CHANGE = 2002;
    public static final int EVENT_SELECTION_CHANGE = 2003;

    //--------------------------------------------------------------------------

    /**
     * ApplicationContext constructor
     */
    public ApplicationContext() {
        registerPropertyEditors();
        // default view state parameters
        viewstate = new HashMap();
        viewstate.put(ApplicationContext.VIEWER_CURRENT_CONTEXT,null);
        viewstate.put(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS,new HashMap());
        viewstate.put(ApplicationContext.VIEWER_ICONTEXTVIEWER_SCENES,new HashMap());
    }

    //--------------------------------------------------------------------------

    /**
     * Get the model.
     * @return
     */
    public ParametricModel getModel() {
        return model;
    }

    public File getProjectDirectory() {
        return projectDir;
    }
    
    /**
     * Get viewer state parameters.
     * @return Map of parameters.
     */
    public Map getViewState() {
        return viewstate;
    }

    /**
     * Get a viewer parameter by Key value.
     * @param Key Key value.
     * @return Parameter value.
     */
    public Object getViewState(Object Key) {
        return viewstate.get(Key);
    }

    /**
     * Register specialized property editors.
     */
    public static void registerPropertyEditors() {
//        PropertyEditorManager.registerEditor(InputTable.class,InputTableEditor.class);
//        PropertyEditorManager.registerEditor(Input.class,InputEditor.class);
    }

    /**
     * Set the model. If the model is set to null, then it is considered closed.
     * @param Model Model
     */
    public void setModel(ParametricModel Model) {
        model = Model;
        setView(model);
        setChanged();
        if (model == null) {
            notifyObservers(ApplicationContext.MODEL_CLOSED);
        } else {
            notifyObservers(ApplicationContext.MODEL_LOADED);
        }
    }

    public void setProjectDir(File Dir) {
        projectDir = Dir;
    }
    
    /**
     * Set the current context view.
     * @param Context 
     */
    public void setView(IContext Context) {
        viewstate.put(ApplicationContext.VIEWER_CURRENT_CONTEXT,Context);
    }

    /**
     * Set a viewer parameter.
     * @param Key
     * @param Value
     */
    public void setViewState(Object Key, Object Value) {
        // get the old value
        Object oldValue = viewstate.get(Key);
        // set the new value
        viewstate.put(Key,Value);
        setChanged();
        // fire property change notification
        if (Key.equals(ApplicationContext.VIEWER_CURRENT_CONTEXT)) {
            logger.log(Level.INFO,"ParametricModel updated view state current context to {0}", Value.toString());
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_CONTEXT_CHANGE));
        } else if (Key.equals(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS)) {
            logger.log(Level.INFO,"ParametricModel updated IContext thumbnails");
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_THUMBNAIL_CHANGE));
        } else if (Key.equals(ApplicationContext.VIEWER_SELECTION)) {
            logger.log(Level.INFO,"ParametricModel updated Selection list state");
            this.notifyObservers(Integer.valueOf(ApplicationContext.EVENT_SELECTION_CHANGE));
        }
    }

}
