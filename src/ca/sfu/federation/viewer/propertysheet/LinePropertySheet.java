/**
 * ComponentLineSheet.java
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

package ca.sfu.federation.viewer.propertysheet;

import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.InputTable;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.exception.NonExistantMethodException;
import ca.sfu.federation.model.exception.NonExistantUpdateAnnotationException;
import ca.sfu.federation.model.geometry.IPoint;
import ca.sfu.federation.model.geometry.Line;
import ca.sfu.federation.ApplicationContext;
import com.developer.rose.BeanProxy;
import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.datadosen.component.RiverLayout;

/**
 * @author  Davis Marques
 * @version 0.1
 */
public class LinePropertySheet extends javax.swing.JPanel implements Observer {
    
    //--------------------------------------------------------------------------

    
    private ParametricModel model;
    private Component target;

    private JPanel jspUpdateMethodInputs;
    private JComboBox jcbUpdateMethod;
    private JTextField jtfCanonicalName;
    private JTextField jtfClass;
    private JTextField jtfDescription;
    private JTextField jtfEndPoint;
    private JTextField jtfLength;
    private JTextField jtfMidPoint;
    private JTextField jtfName;
    private JTextField jtfParentContext;
    private JTextField jtfStartPoint;
    private JTextField jtfDirection;
    private JLabel lblCanonicalName;
    private JLabel lblClass;
    private JLabel lblDescription;
    private JLabel lblEndPoint;
    private JLabel lblINamedObject;
    private JLabel lblLength;
    private JLabel lblMidPoint;
    private JLabel lblName;
    private JLabel lblParentContext;
    private JLabel lblStartPoint;
    private JLabel lblUpdateMethod;
    private JLabel lblDirection;
    
    //--------------------------------------------------------------------------

    
    /**
     * ParametricModelSheet constructor.
     */
    public LinePropertySheet() {
        lblINamedObject = new JLabel();
        lblName = new JLabel();
        lblDescription = new JLabel();
        jtfClass = new JTextField();
        jtfName = new JTextField();
        jtfCanonicalName = new JTextField();
        lblCanonicalName = new JLabel();
        lblClass = new JLabel();
        jtfDescription = new JTextField();
        lblParentContext = new JLabel();
        jtfParentContext = new JTextField();
        lblStartPoint = new JLabel();
        lblMidPoint = new JLabel();
        lblEndPoint = new JLabel();
        jtfStartPoint = new JTextField();
        jtfMidPoint = new JTextField();
        jtfEndPoint = new JTextField();
        lblUpdateMethod = new JLabel();
        jcbUpdateMethod = new JComboBox();
        jspUpdateMethodInputs = new JPanel();
        lblLength = new JLabel();
        jtfLength = new JTextField();
        lblDirection = new javax.swing.JLabel();
        jtfDirection = new javax.swing.JTextField();
        
        lblINamedObject.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblINamedObject.setText("jLabel1");
        lblName.setText("Name");
        lblDescription.setText("Description");
        lblCanonicalName.setText("Canonical Name");
        lblClass.setText("Class");
        lblParentContext.setText("Parent Context");
        lblStartPoint.setText("StartPoint");
        lblMidPoint.setText("MidPoint");
        lblEndPoint.setText("EndPoint");
        lblUpdateMethod.setText("Update Method");
        lblLength.setText("Length");
        lblDirection.setText("Direction");
        
        jtfClass.setEditable(false);
        jtfCanonicalName.setEditable(false);
        jtfParentContext.setEditable(false);
        jtfStartPoint.setEditable(false);
        jtfMidPoint.setEditable(false);
        jtfEndPoint.setEditable(false);
        jtfLength.setEditable(false);
        jtfDirection.setEditable(false);
        
        jspUpdateMethodInputs.setLayout(new BorderLayout());
        jspUpdateMethodInputs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jspUpdateMethodInputs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .add(lblINamedObject)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCanonicalName)
                            .add(lblClass)
                            .add(lblName)
                            .add(lblDescription)
                            .add(lblParentContext)
                            .add(lblStartPoint)
                            .add(lblEndPoint)
                            .add(lblMidPoint)
                            .add(lblDirection)
                            .add(lblLength))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jtfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfClass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                            .add(jtfParentContext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfStartPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfMidPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfEndPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfLength, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfDirection, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(lblUpdateMethod)
                        .add(6, 6, 6)
                        .add(jcbUpdateMethod, 0, 221, Short.MAX_VALUE)))
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
                    .add(lblStartPoint)
                    .add(jtfStartPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfMidPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMidPoint))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfEndPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblEndPoint))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblLength))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfDirection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblDirection))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jcbUpdateMethod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblUpdateMethod))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jspUpdateMethodInputs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        // get the current object
        this.model = ParametricModel.getInstance();
        
        // disable non editable fields
        this.jtfCanonicalName.setEditable(false);
        this.jtfClass.setEditable(false);
        this.jtfParentContext.setEditable(false);
        
        // set field values
        this.setValues();

        // set action listeners
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

    private void jcbUpdateMethodActionListener(java.awt.event.ActionEvent evt) {
        String item = (String) this.jcbUpdateMethod.getSelectedItem();
        System.out.println("INFO: ComponentSheet jcbUpdateMethodActionListener fired.");
        try {
            // set the update method
            this.target.setUpdateMethod(item);
            // update the input arguments panel
            this.buildUpdateMethodInputsPanel();
        } catch (NonExistantMethodException ex) {
            ex.printStackTrace();
        } catch (NonExistantUpdateAnnotationException ex) {
            ex.printStackTrace();
        }
    }    
    
    private void jtfDescriptionActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfDescriptionActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("description",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void jtfNameActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfNameActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("name",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void setValues() {
        // target
        this.target = (Line) this.model.getViewState(ApplicationContext.VIEWER_SELECTION);
        // set field values
        this.lblINamedObject.setText(this.target.getName());
        this.jtfClass.setText(this.target.getClass().toString());
        this.jtfName.setText(this.target.getName());
        // this.jtfDescription.setText(this.target.getDescription());
        this.jtfCanonicalName.setText(this.target.getCanonicalName());
        this.jtfParentContext.setText(this.target.getContext().getName());
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            IPoint startPoint = (IPoint) proxy.get("startPoint");
            IPoint midPoint = (IPoint) proxy.get("startPoint");
            IPoint endPoint = (IPoint) proxy.get("startPoint");
            Double length = (Double) proxy.get("length");
            this.jtfStartPoint.setText(String.valueOf(startPoint));
            this.jtfMidPoint.setText(String.valueOf(midPoint));
            this.jtfEndPoint.setText(String.valueOf(endPoint));
            this.jtfLength.setText(String.valueOf(length));
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            System.out.println("INFO: ComponentSheet received event notification id " + eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_PROPERTY_CHANGE:
                    System.out.println("INFO: ComponentSheet fired property change event.");
                    this.setValues();
                    break;
            }
        }
    }
    
} // end class