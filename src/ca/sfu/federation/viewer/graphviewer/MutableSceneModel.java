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

package ca.sfu.federation.viewer.graphviewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IGraphable;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Builds and displays a dependancy graph for IContext objects.  Some display 
 * objects may provide thumbnails or icon views which then appear in the graph.
 *
 * TODO: when restoring an existing scene from disk or otherwise, we need to make sure we have restablish listeners on model objects otherwise we can't delete, etc. objects
 *
 * @author Davis Marques
 */
public class MutableSceneModel extends Scene implements Observer {

    private static final Logger logger = Logger.getLogger(MutableSceneModel.class.getName());
    
    private IContext context;             // the context we are representing
    
    private LayerWidget nodeLayer;        // layer for nodes
    private LayerWidget edgeLayer;        // layer for edges
    private LayerWidget annotationLayer;  // layer for annotations

    private ArrayList nodes;                 // nodes
    private ArrayList edges;                 // edges
    private LinkedHashMap annotations;         // annotations
    private int annotationId;             // for generating annotation id
    
    private int x = 15;                   // new widget x coordinate
    private int y = 65;                   // new widget y coordinate
    
    private boolean init = true;
    
    //--------------------------------------------------------------------------

    
    /**
     * MutableSceneModel constructor.
     */
    public MutableSceneModel(IContext MyContext) {
        // init
        this.context = MyContext;
        this.nodes = new ArrayList();
        this.edges = new ArrayList();
        this.annotations = new LinkedHashMap();
        this.annotationId = 0;
        // set visual properties
        this.setBackground(ApplicationContext.BACKGROUND_MEDIUM2);
        // add layers to scene
        this.nodeLayer = new LayerWidget(this);
        this.edgeLayer = new LayerWidget(this);
        this.annotationLayer = new LayerWidget(this);
        this.addChild(this.nodeLayer);
        this.addChild(this.edgeLayer);
        this.addChild(this.annotationLayer);
        // add zoom/pan actions to the scene
        this.getActions().addAction(ActionFactory.createZoomAction());
        this.getActions().addAction(ActionFactory.createPanAction());
        // add popup menu to the scene
        WidgetAction popup = ActionFactory.createPopupMenuAction(new StaticSceneModelPopupProvider((Scene)this,this.context));
        this.getActions().addAction(popup);
        // initialize the scene
        this.initScene();
        // observe the model for changes
        ParametricModel model = Application.getContext().getModel();
        if (model instanceof Observable) {
            Observable o = (Observable) model;
            o.addObserver(this);
        }
        // observe the context for changes
        if (this.context instanceof Observable) {
            Observable o = (Observable) this.context;
            o.addObserver(this);
        }
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Create an annotation at the mouse release position.
     * @param Text Annotation text.
     * @param Location Scene location to add new annotation.
     */
    public void addAnnotation(String Text, Point Location) {
        // create a new annotation Id
        this.annotationId++;
        boolean found = false;
        while(!found) {
            if (this.annotations.containsKey(this.annotationId)) {
                this.annotationId++;
            } else {
                found = true;
            }
        }
        // create the annotation
        AnnotationWidget annotation = new AnnotationWidget(this,Text,annotationId);
        // add the annotation to the scene
        this.annotationLayer.addChild(annotation);
        // add the annotation to the index
        this.annotations.put(annotation.getId(),annotation);
        // set the annotation location
        Point p = (Point) Application.getContext().getViewState(ApplicationContext.VIEWER_LAST_MOUSERELEASE);
        annotation.setPreferredLocation(p);
        // revalidate the scene model
        this.validate();
    }
    
    /**
     * Create edges for each of the NamedObject's dependancies.
     * @param Named NamedObject.
     */
    private void addDependancyEdges(INamed Named) {
        // if the element can have dependancies
        if (Named instanceof IGraphable) {
            // get the dependancies
            IGraphable graphObj = (IGraphable) Named;
            LinkedHashMap dependancies = (LinkedHashMap) graphObj.getDependancies();
            // create edges for each of the dependancies
            Iterator it = dependancies.values().iterator();
            while (it.hasNext()) {
                INamed namedDep = (INamed) it.next();
                this.addEdge(namedDep,Named);
            }
        }
        // revalidate the scene model
        this.validate();
    }
    
    /**
     * Add edge between nodes.
     * @param Source NamedObject edge source.
     * @param Target NamedObject edge target.
     */
    private void addEdge(INamed Source, INamed Target) {
        LinkedHashMap nodes = (LinkedHashMap) this.getNodesByTargetName();
        // create a new empty edge
        ConnectionWidget edge = new ConnectionWidget(this);
        this.edges.add(edge);
        // edges display the "flow" of data and go from source (anchor) to target
        Widget sourceWidget = (Widget) nodes.get(Source.getName());
        Widget targetWidget = (Widget) nodes.get(Target.getName());
        edge.setSourceAnchor(AnchorFactory.createRectangularAnchor(sourceWidget));
        edge.setTargetAnchor(AnchorFactory.createRectangularAnchor(targetWidget));
        // set visual attributes
        edge.setCheckClipping(true);
        edge.setForeground(ApplicationContext.BACKGROUND_LIGHT);
        edge.setStroke(new BasicStroke(1.0f));
        edge.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        // add edge to edge layer
        this.edgeLayer.addChild(edge);
        // revalidate the scene model
        this.validate();
    }
    
    /**
     * Add new node.
     * @param Target
     */
    private void addNode(INamed Target) {
        if (Target instanceof IContext) {
            ContainerVisualWidget node = new ContainerVisualWidget((Scene)this,Target);
            this.nodes.add(node);
            this.nodeLayer.addChild(node);
            if (init) {
                node.setPreferredLocation(new Point(this.x,this.y));
                this.x += 180;
            } else {
                Point p = (Point) Application.getContext().getViewState(ApplicationContext.VIEWER_LAST_MOUSERELEASE);
                node.setPreferredLocation(p);
            }
        } else {
            ComponentVisualWidget node = new ComponentVisualWidget((Scene)this,Target);
            this.nodes.add(node);
            this.nodeLayer.addChild(node);
            if (init) {
                node.setPreferredLocation(new Point(this.x,this.y));
                this.x += 180;
            } else {
                Point p = (Point) Application.getContext().getViewState(ApplicationContext.VIEWER_LAST_MOUSERELEASE);
                node.setPreferredLocation(p);
            }
        }
        // validate the scene model
        this.validate();
    }
    
    /**
     * Delete all edges attached to a particular NamedObject.
     * @param Named NamedObject.
     */
    private void deleteEdges(INamed Named) {
        // TODO: need to rethink the whole edge indexing approach to take care of cardinality issues ... there may be more than one edge whose source is a named object
    }
    
    /**
     * Delete a node from the scene and indicies.
     * @param Named NamedObject to be deleted.
     */
    private void deleteNode(INamed Named) {
//        LinkedHashMap nodes = (LinkedHashMap) this.getNodesByTargetName();
//        Widget widget = (Widget) nodes.get(Named.getName());
//        this.nodes.remove(widget);
//        // remove edges
        
        // temp hard search
        Iterator e = this.nodes.iterator();
        boolean found = false;
        while (e.hasNext() && !found) {
            Widget widget = (Widget) e.next();
            if (widget instanceof ContainerVisualWidget) {
                ContainerVisualWidget cvw = (ContainerVisualWidget) widget;
                if (cvw.getTarget().getName().equals(Named.getName())) {
                    this.nodes.remove(widget);
                }
            } else if (widget instanceof ComponentVisualWidget) {
                ComponentVisualWidget cvw = (ComponentVisualWidget) widget;
                if (cvw.getTarget().getName().equals(Named.getName())) {
                    this.nodes.remove(widget);
                }
            }
        }
        // validate the scene model
        this.validate();
    }
    
    private LayerWidget getAnnotationLayer() {
        return this.annotationLayer;
    }
    
    private Map getAnnotationsById() {
        return (Map) this.annotations;
    }
    
    private LayerWidget getEdgeLayer() {
        return this.edgeLayer;
    }
    
    /**
     * Get edges indexed by source node name.
     * @return Map where source node name is the key, and the value is a list of edges.
     */
    private Map getEdgesBySourceName() {
        // init
        LinkedHashMap mapping = new LinkedHashMap();
        // for each edge
        Iterator e = this.edges.iterator();
        while (e.hasNext()) {
            // get the edge
            ConnectionWidget edge = (ConnectionWidget) e.next();
            // get its source widget
            Widget widget = edge.getSourceAnchor().getRelatedWidget();
            // get the NamedObject referenced by the widget
            String name = "";
            if (widget instanceof ContainerVisualWidget) {
                ContainerVisualWidget cvw = (ContainerVisualWidget) widget;
                name = cvw.getTarget().getName();
            } else if (widget instanceof ComponentVisualWidget) {
                ComponentVisualWidget cvw = (ComponentVisualWidget) widget;
                name = cvw.getTarget().getName();
            }
            // add the named as key for the edge list if it is not yet there
            if (!mapping.containsKey(name)) {
                mapping.put(name,new ArrayList());
            }
            // add the edge to the list for the namedobject
            ArrayList edgelist = (ArrayList) mapping.get(name);
            edgelist.add(edge);
        }
        // return result
        return (Map) mapping;
    }
    
    /**
     * Get edges indexed by target node name.
     * @return Map where target node name is the key, and the value is a list of edges.
     */
    private Map getEdgesByTargetName() {
        // init
        LinkedHashMap mapping = new LinkedHashMap();
        // for each edge
        Iterator e = this.edges.iterator();
        while (e.hasNext()) {
            // get the edge
            ConnectionWidget edge = (ConnectionWidget) e.next();
            // get its target widget
            Widget widget = edge.getTargetAnchor().getRelatedWidget();
            // get the NamedObject referenced by the widget
            String name = "";
            if (widget instanceof ContainerVisualWidget) {
                ContainerVisualWidget cvw = (ContainerVisualWidget) widget;
                name = cvw.getTarget().getName();
            } else if (widget instanceof ComponentVisualWidget) {
                ComponentVisualWidget cvw = (ComponentVisualWidget) widget;
                name = cvw.getTarget().getName();
            }
            // add the named as key for the edge list if it is not yet there
            if (!mapping.containsKey(name)) {
                mapping.put(name,new ArrayList());
            }
            // add the edge to the list for the namedobject
            ArrayList edges = (ArrayList) mapping.get(name);
            edges.add(edge);
        }
        // return result
        return (Map) mapping;
    }

    /**
     * Get the node layer.
     * @return Node layer widget.
     */
    public LayerWidget getNodeLayer() {
        return this.nodeLayer;
    }
    
    /**
     * Get mapping from NamedObject to VisualWidget.
     */
    public Map getNodesByTargetName() {
        LinkedHashMap mapping = new LinkedHashMap();
        Iterator e = this.nodes.iterator();
        while (e.hasNext()) {
            Object object = e.next();
            if (object instanceof ContainerVisualWidget) {
                ContainerVisualWidget cvw = (ContainerVisualWidget) object;
                mapping.put(cvw.getTarget().getName(),cvw);                
            } else if (object instanceof ComponentVisualWidget) {
                ComponentVisualWidget cvw = (ComponentVisualWidget) object;
                mapping.put(cvw.getTarget().getName(),cvw);                
            }
        }
        // return result
        return (Map) mapping;
    }
    
    /**
     * Build a new scene to depict the Parametric model.
     */
    public void initScene() {
        // add viewable elements to the scene
        LinkedHashMap elements = (LinkedHashMap) this.context.getElements();
        Iterator iter = elements.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            this.addNode(named);
        }
        // add edges to show dependancies between widgets
        iter = elements.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            this.addDependancyEdges(named);
        }
        // validate and repaint the scene
        this.init = false;
        this.validate();
    }
    
    /**
     * Paint canvas.
     * TODO: the validity check is a crutch for a proper implementation. Need to look at the processing of adding and removing objects to ensure that the scene is revalidated following all major update events.
     */
    @Override
    public void paintChildren() {
        if (this.isValidated()) {
            Object anti = getGraphics().getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Object textAnti = getGraphics().getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.paintChildren();
            getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, anti);
            getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAnti);
        } else {
            logger.log(Level.WARNING,"SceneModel invalidated. Validating before repainting.");
            this.validate();
            Object anti = getGraphics().getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Object textAnti = getGraphics().getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.paintChildren();
            getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, anti);
            getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAnti);
        }
    }
    
    /**
     * Remove all edges connected to a particular IVisualWidget.
     * @param MyWidget Widget.
     */
    private void removeEdgesConnectedToWidget(Widget MyWidget) {
        // edges to be removed
        ArrayList removeList = new ArrayList();
        // build list of edges to be removed
        Iterator e = this.edges.iterator();
        while (e.hasNext()) {
            ConnectionWidget edge = (ConnectionWidget) e.next();
            Widget source = edge.getSourceAnchor().getRelatedWidget();
            Widget target = edge.getTargetAnchor().getRelatedWidget();
            if (MyWidget == source || MyWidget == target) {
                removeList.add(edge);
            }
        }
        // remove edges from the scene
        if (removeList.size()>0) {
            this.edges.removeAll(removeList);
            this.edgeLayer.removeChildren(removeList);
        }
    }
    
    /**
     * Update the Scene.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"MutableSceneModel received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_ELEMENT_ADD:
                    logger.log(Level.INFO,"MutableSceneModel fired element add");
                    this.updateAddElements();
                    break;
                case ApplicationContext.EVENT_ELEMENT_CHANGE:
                    logger.log(Level.INFO,"MutableSceneModel fired element change");
                    // update object visual state

                    // update edges
                    this.updateEdges();
                    break;
                case ApplicationContext.EVENT_ELEMENT_DELETED:
                    logger.log(Level.INFO,"MutableSceneModel fired element deleted event");
                    this.updateDeleteElements();
                    break;
                case ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST:
                    logger.log(Level.INFO,"MutableSceneModel fired element delete");
                    INamed named = (INamed) o;
                    this.updateDeleteElement(named);
                    break;
                case ApplicationContext.EVENT_THUMBNAIL_CHANGE:
                    logger.log(Level.INFO,"MutableSceneModel fired thumbnail change event");
                    this.updateThumbnails();
                    break;
            }
        }
    }
    
    /**
     * Update the graph state to correspond with the current context.
     */
    private void updateAddElements() {
        // get the list of objects in the context
        LinkedHashMap elements = (LinkedHashMap) this.context.getElements();
        LinkedHashMap nodesByName = (LinkedHashMap) this.getNodesByTargetName();
        // determine which elements are new
        ArrayList newElements = new ArrayList();
        Iterator iter = elements.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            // if the scene does not have the named object, then it has been added
            if (!nodesByName.containsKey(named.getName())) {
                newElements.add(named);
            }
        }
        // create new widget
        Iterator e = newElements.iterator();
        while (e.hasNext()) {
            INamed named = (INamed) e.next();
            this.addNode(named);
            this.addDependancyEdges(named);
            if (named instanceof Observable) {
                Observable ob = (Observable) named;
                ob.addObserver(this);
            }
        }
        // validate the scene
        this.validate();
    }

    /**
     * Update the graph to correspond with the current context.
     * 
     * @param Named INamed.
     */
    private void updateDeleteElement(INamed Named) {
        // determine which element has been deleted
        LinkedHashMap nodesByName = (LinkedHashMap) this.getNodesByTargetName();
        if (nodesByName.containsKey(Named.getName())) {
            // get the widget that corresponds with the NamedObject
            Iterator e = null;
            Widget widget = (Widget) nodesByName.get(Named.getName());
            if (widget != null) {
                this.removeEdgesConnectedToWidget(widget);
                // remove the widget
                this.nodes.remove(widget);
                this.nodeLayer.removeChild(widget);
            }        
            // validate the scene
            this.validate();
        }
    }
    
    /**
     * Find out which element has been deleted and remove its corresponding
     * visual widget and edges.
     */
    private void updateDeleteElements() {
        // get the list of context objects
        LinkedHashMap elements = (LinkedHashMap) this.context.getElements();
        // get the list of visual widgets
        LinkedHashMap widgets = (LinkedHashMap) this.getNodesByTargetName();
        // find out which widget does not have a corresponding model element
        Iterator iter = widgets.values().iterator();
        boolean found = false;
        IVisualWidget widget = null;
        while (iter.hasNext() && !found) {
            widget = (IVisualWidget) iter.next();
            // if the context does not contain the target
            if (!elements.containsKey(widget.getTarget().getName())) {
                found = true;
            }
        }
        // delete the widget and corresponding edges
        if (widget != null) {
            this.removeEdgesConnectedToWidget((Widget)widget);
            this.nodes.remove(widget);
            this.nodeLayer.removeChild((Widget)widget);
        }
    }

    /**
     * Update the edge list.
     */
    private void updateEdges() {
        // clear all current edges
        this.edgeLayer.removeChildren();
        // get the list of namedobjects in the scene
        LinkedHashMap elements = (LinkedHashMap) this.context.getElements();
        Iterator iter = elements.values().iterator();
        while (iter.hasNext()) {
            INamed named = (INamed) iter.next();
            this.addDependancyEdges(named);
        }
    }
    
    /**
     * Update widget thumbnails.
     */
    private void updateThumbnails() {
        // for each visual widget, update its thumbnail
        Iterator e = this.nodes.iterator();
        while (e.hasNext()) {
            Widget widget = (Widget) e.next();
            if (widget instanceof ContainerVisualWidget) {
                ContainerVisualWidget vw = (ContainerVisualWidget) widget;
                vw.updateThumbnail();
            }
        }
        // validate the scene
        this.validate();
    }

    /**
     * Update the color, visibility, etc. of the visual widget to correspond 
     * with the model object state.
     */
    private void updateVisualState() {
        logger.log(Level.WARNING,"updateVisualState not implemented");
    }
    
} // end class
