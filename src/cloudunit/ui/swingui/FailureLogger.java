/*
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande and Universidade Federal da Paraiba
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 * 
 */
package cloudunit.ui.swingui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import junit.framework.Test;
import junit.framework.TestCase;

import org.smartfrog.services.junit.TestInfo;
import org.smartfrog.services.junit.ThrowableTraceInfo;

import cloudunit.framework.CloudTestListener;

/**
 * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
 * 
 * Description: A graphical panel which register failures generated framework execution.
 * 
 * @version 1.0 Date: 14/03/2005
 */
public class FailureLogger extends JPanel implements CloudTestListener {
    
	
	private static final long serialVersionUID = 1L;

	/**
	 * A TableModel.
	 */
    protected MyTableModel model;
    
    /**
     * A table.
     */
    private JTable table;
    
    /**
     * Component in which this panel will be coupled.
     */
    private JFrame parent;
    
    /**
     * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
     * 
     * Description: An inner class that implements a customized TableModel.
     * 
     * @version 1.0 Date: 23/05/2005
     * 
     * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
     */
   	class MyTableModel extends AbstractTableModel {

   		
		private static final long serialVersionUID = 1L;
		/**
   		 * A Vector to store data.
   		 */
        private Vector<JComponent> data = new Vector<JComponent>();
                
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return data.size();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 1;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
        public String getColumnName( int col ) {
            return "Details";
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable( int row, int col ) {
            return false;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            
            return data.elementAt( row );
            
        }
        
        /**
         * Inserts a row to this table.
         * @param comp A component.
         */
        public void insertRow( JComponent comp ) {
            
            data.add( comp );
            this.fireTableRowsInserted( data.size() - 2 , data.size() - 1 );

        }
        
        /**
         * Select
         * @param test
         */
        public void select( TestCase test ) {
            String key = test.getClass().getName() + "." + test.getName();
            
            synchronized( data ) {
                for( int i = 0 ; i < data.size();i++ ) {
                	
                    JTextPane tp = (JTextPane) data.elementAt(i);
                    if( tp.getText().startsWith(key)) {
                        
                        table.setRowSelectionInterval(i,i);
                        table.scrollRectToVisible( table.getCellRect(i,0,true));
                        break;

                    }
                
                }
                
            }
            
        }
        
        public Class<?> getColumnClass( int col ) {
            return data.elementAt(col).getClass();
        }
    };
    
    /**
     * Full constructor for FailureLogger.
     * @param parent A component in which this panel will be coupled.
     */
    public FailureLogger( JFrame parent ) {
        
        model = new MyTableModel();
        table = new JTable(model);
        this.parent = parent;
        
        
        table.setDefaultRenderer( JTextPane.class,  new TableCellRenderer() {
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                JTextPane tp = (JTextPane)value;
                
                if( isSelected) {
                    Color bg = ( new JDesktopPane()).getBackground();
                    tp.setBackground( bg);
                } else {
                    tp.setBackground( Color.white );
                }
                
                table.setRowHeight(row, tp.getPreferredSize().height);
                
                return tp;
                
            }
            
        });
        
        
        setLayout(new BorderLayout());
        JScrollPane js = new JScrollPane(table);
        js.setAutoscrolls(true);
        add( js , BorderLayout.CENTER);
        add( js , BorderLayout.CENTER);
        add( createSaveButton(), BorderLayout.SOUTH );
    }

    
    /**
     * Creates the save button.
     * @return The component.
     */
    private JPanel createSaveButton() {
        
        JButton save = new JButton("Save");
            
        save.addActionListener( new ActionListener() {
            
            
            public void actionPerformed( ActionEvent e ) {
                new SaveDialog(table , parent);
            }
            
        });
        
        
        JPanel p = new JPanel();
        p.setLayout( new GridLayout(1,5) );
        p.add( new JPanel());
        p.add( new JPanel());
        p.add( save , BorderLayout.CENTER );
        p.add( new JPanel());
        p.add( new JPanel());
        return p;
        
        
    }
    
    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public void testFinished( TestInfo testInfo ) {

    }

    /**
     * Called when a test has been started
     * @param testInfo The test info.
     */
    public void testStarted( Test test) {

    }

    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed( TestInfo testInfo ) {
                
        addException(testInfo);

    }

    /**
     * Adds an exception report to this logger.
     * @param testInfo The test info.
     */
    protected void addException( TestInfo testInfo ) {
        
        ThrowableTraceInfo cause = testInfo.getFault();
        StringBuffer stackTrace = new StringBuffer();
        
        while( cause != null ) {
            stackTrace.append( cause.toString() + "\n");
            cause = cause.getCause();
        }
                
       	JTextPane t = new JTextPane();
        t.setText( testInfo.getClassname() + "." + testInfo.getText() + "\n" + stackTrace.toString() );
        
        model.insertRow(t);
        repaint();
    }

    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public void testError( TestInfo testInfo ) {

    }

    /**
     * Called when the test phase has been started.
     *
     */
    public void testPhaseFinished() {
               
    }

    /**
     * Called when all tests where executed (successfully or unsuccessfully).
     *
     */
    public void testPhaseStarted() {
               
    }
    
    /**
     * @param test
     */
    public void select( TestCase test ) {
        model.select( test );
    }

};