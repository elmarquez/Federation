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

package ca.sfu.federation.viewer.propertysheet;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.InputTable;
import ca.sfu.federation.model.geometry.Point;
import com.developer.rose.BeanProxy;
import java.awt.BorderLayout;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import se.datadosen.component.RiverLayout;

/**
 * @author  Davis Marques
 */
public class PointPropertySheet extends javax.swing.JPanel implements Observer {

    private static final Logger logger = Logger.getLogger(PointPropertySheet.class.getName());
    
    private JComboBox jcbUpdateMethod;
    private JPanel jspUpdateMethodInputs;
    private JTextField jtfCanonicalName;
    private JTextField jtfClass;
    private JTextField jtfDescription;
    private JTextField jtfName;
    private JTextField jtfParentContext;
    private JTextField jtfX;
    private JTextField jtfY;
    private JTextField jtfZ;
    private JLabel lblCanonicalName;
    private JLabel lblClass;
    private JLabel lblDescription;
    private JLabel lblINamedObject;
    private JLabel lblName;
    private JLabel lblParentContext;
    private JLabel lblUpdateMethod;
    private JLabel lblX;
    private JLabel lblY;
    private JLabel lblZ;
    
    private Component target;
    
    //--------------------------------------------------------------------------

    
    /**
     * ParametricModelSheet constructors.
     */
    public PointPropertySheet() {
        lblINamedObject = new javax.swing.JLabel();
        lblClass = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblCanonicalName = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblParentContext = new javax.swing.JLabel();
        lblX = new javax.swing.JLabel();
        lblY = new javax.swing.JLabel();
        lblZ = new javax.swing.JLabel();
        lblUpdateMethod = new javax.swing.JLabel();
        
        jtfClass = new javax.swing.JTextField();
        jtfName = new javax.swing.JTextField();
        jtfCanonicalName = new javax.swing.JTextField();
        jtfDescription = new javax.swing.JTextField();
        jtfParentContext = new javax.swing.JTextField();
        jtfX = new javax.swing.JTextField();
        jtfY = new javax.swing.JTextField();
        jtfZ = new javax.swing.JTextField();
        jcbUpdateMethod = new javax.swing.JComboBox();
        // jspUpdateMethodInputs = new javax.swing.JScrollPane();
        jspUpdateMethodInputs = new JPanel();
        jspUpdateMethodInputs.setLayout(new BorderLayout());
        
        lblINamedObject.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblINamedObject.setText("jLabel1");
        lblName.setText("Name");
        lblDescription.setText("Description");
        lblCanonicalName.setText("Canonical Name");
        lblClass.setText("Class");
        lblParentContext.setText("Parent Context");
        lblX.setText("X Coordinate");
        lblY.setText("Y Coordinate");
        lblZ.setText("Z Coordinate");
        lblUpdateMethod.setText("Update Method");
        
        jtfClass.setEditable(false);
        jtfCanonicalName.setEditable(false);
        jtfParentContext.setEditable(false);
        jtfX.setEditable(false);
        jtfY.setEditable(false);
        jtfZ.setEditable(false);
        
        jspUpdateMethodInputs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(jspUpdateMethodInputs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.LEADING, lblINamedObject)
                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(lblCanonicalName)
                .add(lblClass)
                .add(lblName)
                .add(lblDescription)
                .add(lblParentContext)
                .add(lblX)
                .add(lblZ)
                .add(lblY)
                .add(lblUpdateMethod))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jcbUpdateMethod, 0, 221, Short.MAX_VALUE)
                .add(jtfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(jtfClass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(jtfDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .add(jtfParentContext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(jtfX, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(jtfY, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .add(jtfZ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))
                .addContainerGap())
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblINamedObject)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jtfClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblClass))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(lblCanonicalName)
                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(lblDescription)
                .add(jtfDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(lblParentContext)
                .add(jtfParentContext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(lblX)
                .add(jtfX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jtfY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblY))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jtfZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblZ))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jcbUpdateMethod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblUpdateMethod))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jspUpdateMethodInputs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
                );
        
        // set field values
        this.setValues();
        // add action listeners
        jtfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNameActionListener(evt);
            }
        });
        jtfDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescriptionActionListener(evt);
            }
        });
        jtfX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfXActionListener(evt);
            }
        });
        jtfY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfYActionListener(evt);
            }
        });
        
        jtfZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfZActionListener(evt);
            }
        });
        jcbUpdateMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbUpdateMethodActionListener(evt);
            }
        });
    }
    
    //-------------------------------------------------------------------------
    
    /**
     * Build an Update Method user input panel.
     * @return Panel.
     */
    private JPanel buildUpdateMethodInputsPanel() {
        // init
        JPanel panel = new JPanel();
        // build panel
        InputTable inputTable = this.target.getInputTable();
        ArrayList keys = (ArrayList) inputTable.getInputKeys();
        if (keys.size()>0) {
            panel.setLayout(new RiverLayout());
            boolean first = true;
            Iterator e = keys.iterator();
            while (e.hasNext()) {
                String key = (String) e.next();
                if (first) {
                    panel.add("p left",new JLabel(key));
                    panel.add("tab hfill",new InputTextField(inputTable,key));
                    first = false;
                } else {
                    panel.add("br",new JLabel(key));
                    panel.add("tab hfill",new InputTextField(inputTable,key));
                }
            }
        }
        // clear the scrollpane, add the new panel, validate
        this.jspUpdateMethodInputs.removeAll();
        this.jspUpdateMethodInputs.add(panel,BorderLayout.CENTER);
        this.jspUpdateMethodInputs.validate();
        // return result
        return panel;
    }
    
    private void jtfZActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        logger.log(Level.INFO,"ComponentSheet jtfZActionListener fired {0}", command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Z",evt.getActionCommand());
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
    }
    
    private void jtfYActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        logger.log(Level.INFO,"ComponentSheet jtfYActionListener fired {0}", command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Y",evt.getActionCommand());
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
        
    }
    
    private void jtfXActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        logger.log(Level.INFO,"ComponentSheet jtfXActionListener fired {0}", command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("X",evt.getActionCommand());
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
        
    }
    
    private void jtfDescriptionActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        logger.log(Level.INFO,"ComponentSheet jtfDescriptionActionListener fired {0}", command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("description",evt.getActionCommand());
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
        
    }
    
    private void jtfNameActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        logger.log(Level.INFO,"ComponentSheet jtfNameActionListener fired");
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("name",evt.getActionCommand());
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
    }
    
    private void jcbUpdateMethodActionListener(java.awt.event.ActionEvent evt) {
        String item = (String) this.jcbUpdateMethod.getSelectedItem();
        logger.log(Level.INFO,"ComponentSheet jcbUpdateMethodActionListener fired");
        try {
            // set the update method
            this.target.setUpdateMethod(item);
            // update the input arguments panel
            this.buildUpdateMethodInputsPanel();
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
    }
    
    private void setValues() {
        // target
        this.target = (Point) Application.getContext().getViewState(ApplicationContext.VIEWER_SELECTION);
        // set field values
        this.lblINamedObject.setText(this.target.getName());
        this.jtfClass.setText(this.target.getClass().toString());
        this.jtfName.setText(this.target.getName());
        this.jtfCanonicalName.setText(this.target.getCanonicalName());
        this.jtfParentContext.setText(this.target.getContext().getName());
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            Double x = (Double) proxy.get("x");
            Double y = (Double) proxy.get("y");
            Double z = (Double) proxy.get("z");
            this.jtfX.setText(String.valueOf(x));
            this.jtfY.setText(String.valueOf(y));
            this.jtfZ.setText(String.valueOf(z));
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"{0}",stack);
        }
        
        Method[] methods = this.target.getUpdateMethods();
        Vector methodNameList = new Vector();
        for (int i=0;i<methods.length;i++) {
            Method method = methods[i];
            methodNameList.add(method.getName());
        }
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(methodNameList);
        jcbUpdateMethod.setModel(comboBoxModel);
        String selected = this.target.getUpdateMethodName();
        jcbUpdateMethod.setSelectedItem(selected);
        
        // build the input panel
        this.buildUpdateMethodInputsPanel();
        
        // listen for changes on the target
        if (this.target instanceof Observable) {
            Observable o = (Observable) this.target;
            o.addObserver(this);
        }
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"ComponentSheet received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_PROPERTY_CHANGE:
                    logger.log(Level.INFO,"ComponentSheet fired property change event");
                    this.setValues();
                    break;
            }
        }
    }
    
} 