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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;

import cloudunit.framework.CloudTestRunner;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Description: This class implements a Graphical User Interface (GUI) for CloudUnit.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class CloudUnit implements TreeSelectionListener {

	/**
	 * A GridTestRunner.
	 */
    private CloudTestRunner testRunner;

    /**
     * A GridTestSuite.
     */
    private TestSuite testSuite;

    /**
     * Application's main frame. 
     */
    private JFrame mainFrame;

    /**
     * Application's status bar.
     */
    private StatusBar statusBar;

    /**
     * A logger to register all errors occurred during application execution. 
     */
    private ErrorLogger errorLogger;
    
    /**
     * A logger to register all failures occurred during application execution.
     */
    private FailureLogger failureLogger;

    /**
     * A logger to register all operations performed during application execution.
     */
    private ExecutionLogger executionLogger;

    /**
     * A graphical tree to dispose all tests.
     */
    private TestHierarchyTree testTree;

    /**
     * A Swing TabbedPane.
     */
    private JTabbedPane tabbedPane;
    
    /**
     * The main method. Through it, application may be launched by using of a console. 
     * @param args The test class followed by testrunner class.
     * @throws InstantiationException An InstantiationException may be thrown.
     * @throws IllegalAccessException An IllegalAccessException may be thrown.
     * @throws ClassNotFoundException A ClassNotFoundException may be thrown.
     * @throws SecurityException A SecurityException may be thrown.
     * @throws IOException An IOException may be thrown.
     */
    public static void main( String args[] ) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IOException {
                               
        Object test = Class.forName(args[0]).newInstance();
        CloudTestRunner testRunner = (CloudTestRunner) Class.forName(args[1]).newInstance();
        TestSuite suite = null;
        
        if( test instanceof TestSuite ) {
            suite = (TestSuite) test;
        } else if( test instanceof TestCase ) {
            suite = new TestSuite();
            suite.addTest((TestCase) test);
        } else {
            
            try {
                Method m = test.getClass().getMethod( "suite");
                suite = (TestSuite) m.invoke( test.getClass());
                
            } catch( Exception e ) {
                	e.printStackTrace();
            }
        }
        
        
        if( suite != null ) {
            CloudUnit gunit = new CloudUnit( suite, testRunner);
            gunit.runTests();
        }
        
        
    }

    /**
     * Full constructor for CloudUnit GUI.
     * @param testSuite A suite of tests.
     * @param testRunner A test runner.
     */
    public CloudUnit(TestSuite testSuite, CloudTestRunner testRunner) {
        this.testSuite = testSuite;
        this.testRunner = testRunner;
        
        buildGUI();
    }

    /**
     * Performs tests.
     */
    public void runTests() {
        testRunner.run(testSuite);
    }

    /**
     * Builds graphical interface.
     */
    private void buildGUI() {
        buildMainFrame();
               
        mainFrame.setVisible(true);
    }

    /**
     * Creates main frame.
     */
    private void buildMainFrame() {

        mainFrame = new JFrame("GridUnit 1.1");
        
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.addWindowListener(  new WindowAdapter() {

            public void windowClosing(WindowEvent arg0) {
                testRunner.stop();
                System.exit(0);
            }
            
        });
        mainFrame.getContentPane().setLayout(new BorderLayout());

        mainFrame.setJMenuBar( createMenuBar() );
        mainFrame.getContentPane().add( createTestHierarchyPanel(), BorderLayout.WEST );
        mainFrame.getContentPane().add(createTabbedPane(), BorderLayout.CENTER);
        mainFrame.getContentPane().add(createStatusBar(), BorderLayout.PAGE_END);

        this.adjustFont(mainFrame);
        
    }


    /**
     * Creates menu bar.
     * @return A component.
     */
    private JMenuBar createMenuBar() {

        JMenuBar menu = new JMenuBar();

        JMenu gridunit = new JMenu("CloudUnit");

        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem stop = new JMenuItem("Stop");

        gridunit.add(stop);

        gridunit.add(new JSeparator());
        gridunit.add(exit);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        
        about.addActionListener( new ActionListener() {
           
            public void actionPerformed( ActionEvent arg0 ) {
                
                showAboutWindow();
                
            }
            
            
        });
        
        
        help.add(about);

        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                testRunner.stop();
                System.exit(0);
            }

        });

        stop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                testRunner.stop();
            }
        });

        menu.add(gridunit);
        menu.add(help);

        return menu;

    }


    /**
     * Creates status bar.
     * @return A component.
     */
    private JComponent createStatusBar() {

        statusBar = new StatusBar(testSuite.countTestCases());
        statusBar.setDoubleBuffered(true);
        testRunner.addCloudTestListener(statusBar);

        return statusBar;

    }
    
    public void showAboutWindow() {
        
        JDialog window = new JDialog( mainFrame , "About CloudUnit");
       
        JEditorPane pane = new JEditorPane();
      
        pane.setEditable(false);
        pane.setEditorKit( new HTMLEditorKit());
        
        try {
        	//@TODO fix this.
        	pane.setPage("resources/about.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        window.setSize(500 , 450 );
        
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = window.getSize();
        window.setLocation( screensize.width/2 - size.width/2 , screensize.height/2 - size.height/2);
        
        pane.setBorder( BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        window.getContentPane().setLayout( new BorderLayout() );
      	window.getContentPane().add( pane , BorderLayout.CENTER );
      	window.setVisible(true);
        
    }

    /**
     * Creates painel containing tabs.
     * @return A TabbedPane component.
     */
    private JTabbedPane createTabbedPane() {

        tabbedPane = new JTabbedPane();

        //tabbedPane.addTab("Test Hierarchy", IconManager.getIcon(IconManager.hierarchyIcon), createTestHierarchyPanel());
        tabbedPane.addTab("Failures", IconManager.getIcon(IconManager.failureIcon), createFailurePanel());
        tabbedPane.addTab("Errors", IconManager.getIcon(IconManager.errorIcon), createErrorPane());
        tabbedPane.addTab("Execution Trace", IconManager.getIcon(IconManager.traceIcon), createExecutionDetailsPanel());

        tabbedPane.setDoubleBuffered(true);
        
        tabbedPane.setSelectedComponent( tabbedPane.getComponentAt(2));
        
        return tabbedPane;
        
        

    }

    /**
     * Creates error panel;
     * @return A component.
     */
    private Component createErrorPane() {
        errorLogger = new ErrorLogger(mainFrame);
        testRunner.addCloudTestListener(errorLogger);
        errorLogger.setDoubleBuffered(true);
        return errorLogger;
    }

    /**
     * Creates failure panel.
     * @return A component.
     */
    private Component createFailurePanel() {
        failureLogger = new FailureLogger(mainFrame);
        failureLogger.setDoubleBuffered(true);
        testRunner.addCloudTestListener(failureLogger);
        return failureLogger;
    }

    /**
     * Creates execution details panel.
     * @return A component.
     */
    private Component createExecutionDetailsPanel() {
        executionLogger = new ExecutionLogger(mainFrame);
        executionLogger.setDoubleBuffered(true);
        testRunner.addCloudTestListener(executionLogger);
        return executionLogger;
    }

    /**
     * Create a panel where tests will be disposed.
     * @return A component.
     */
    private Component createTestHierarchyPanel() {
        testTree = new TestHierarchyTree(testSuite, this);
        testTree.setDoubleBuffered(true);
        testRunner.addCloudTestListener(testTree);

        JScrollPane js = new JScrollPane(testTree);
        js.setPreferredSize( new Dimension(280, 200));
       
        return js;
    }

    /**
     * Adjusts fonts format from a frame.
     * @param main A frame. 
     */
    private void adjustFont(JFrame main) {
        Object[] objs = UIManager.getLookAndFeel().getDefaults().keySet().toArray();
        for (int i = 0; i < objs.length; i++) {
            if (objs[i].toString().toUpperCase().indexOf(".FONT") != -1) {
                Font font = UIManager.getFont(objs[i]);
                font = font.deriveFont(Font.PLAIN);
                UIManager.put(objs[i], new FontUIResource(font));
            }
        }
        SwingUtilities.updateComponentTreeUI(main);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        
        if( e.getNewLeadSelectionPath() != null ) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
            Object o = node.getUserObject();
            if( o instanceof  TreePair ) {

                TreePair pair = (TreePair) o; 
            
                if( pair.getState() == TreePair.FAILED ) {
                    selectFailure( pair.getTest() );
                } else if( pair.getState() == TreePair.ERROR ) {
                    selectError( pair.getTest() );
                }
            
            }
        }     
    }
    
    /**
     * Selects a failure occurred.  
     * @param test A test.
     */
    private void selectFailure( TestCase test ) {
        tabbedPane.setSelectedComponent( tabbedPane.getComponentAt(0) );
        failureLogger.select( test );
        
    }
    
    /**
     * Selects an error occurred.
     * @param test A test.
     */
    private void selectError( TestCase test ) {
        tabbedPane.setSelectedComponent( tabbedPane.getComponentAt(1));
        errorLogger.select( test );
        
        
    }
 
    

}