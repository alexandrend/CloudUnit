/*
 * Copyright (c) 2002-2015 Universidade Federal de Campina Grande and Universidade Federal da Paraiba
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

import cloudunit.framework.CloudTestListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.smartfrog.services.junit.TestInfo;

/**
 * 
 * Description: A Class to graphically represent the test hierarchy
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class TestHierarchyTree extends JPanel implements CloudTestListener {

	private static final long serialVersionUID = 1L;

	private DefaultMutableTreeNode root;
    
    private JTree theTree;
    
    private Hashtable <String,TestCase> testCases;
    
    class MyRenderer extends DefaultTreeCellRenderer {
        
        private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            Object userValue = ((DefaultMutableTreeNode) value).getUserObject();
            
            if( userValue instanceof Class ) {
                setIcon(IconManager.getIcon( IconManager.classIcon));
                
                Class<?> c = (Class<?>) userValue;
                this.setText(c.getName().substring( c.getName().lastIndexOf( ".") + 1));
                
            } else if( userValue instanceof TreePair ) {
                TreePair p = (TreePair) userValue;
                this.setText(p.getTest().getName());
                
                switch( p.getState() ) { 
                	case TreePair.RUNNING: {
                	    setIcon( IconManager.getIcon( IconManager.runningIcon));
                	    break;
                	} 
                	case TreePair.FAILED: {
                	    setIcon( IconManager.getIcon( IconManager.failureIcon));
                	    break;
                	}
                	case TreePair.ERROR : {
                	    setIcon( IconManager.getIcon( IconManager.errorIcon));
                	    break;
                	}
                	case TreePair.FINISHED : {
                	    setIcon( IconManager.getIcon( IconManager.finishedIcon));
                	    break;
                	}
                	default : {
                	    setIcon( IconManager.getIcon( IconManager.testIcon));
                	}
                }
                
            } else if( userValue instanceof String ) {
                setIcon( IconManager.getIcon(IconManager.packageIcon));
            }
            
            return this;

        }
    };

    public TestHierarchyTree(TestSuite testSuite, TreeSelectionListener tsl ) {
   
        setBackground( Color.white );
        setLayout( new BorderLayout() );
        testCases = new Hashtable <String,TestCase>();
        createTree( testSuite, tsl );
        add(theTree , BorderLayout.WEST);
        
    }
  
    private void createTree( TestSuite testSuite , TreeSelectionListener tsl ) {
               
        root = new DefaultMutableTreeNode("Tests");

        theTree = new JTree(root);
        theTree.addTreeSelectionListener(tsl);
        
        synchronized (this) {

            theTree.setEditable(false);
            theTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            theTree.setShowsRootHandles(true);
            theTree.setCellRenderer(new MyRenderer());

        }
        
        Iterator <TestCase> it = getAllTestCases( testSuite).iterator();

        
        while (it.hasNext()) {
            TestCase tc = it.next();
            addTestToTree( tc);
            testCases.put( tc.getClass().getName() + "." + tc.getName() , tc );
        }
    }
    
    private Collection<TestCase> getAllTestCases( TestSuite testSuite ) {
        
        Enumeration <Test> t = testSuite.tests();
        Vector<TestCase> tests = new Vector <TestCase>();
        
        while( t.hasMoreElements()) {
            
            Test o = t.nextElement();
            if( o instanceof TestCase ) {
                tests.add( (TestCase) o );
        	} else if( o instanceof TestSuite ){
        	    tests.addAll( getAllTestCases( (TestSuite) o ));
        	}
    	}
        
        return tests;
        
    }


    private DefaultMutableTreeNode search(Object value, DefaultMutableTreeNode parent) {
        Enumeration <DefaultMutableTreeNode> t = parent.children();
        while (t.hasMoreElements()) {
            DefaultMutableTreeNode m = t.nextElement();
            if (m.getUserObject().equals(value)) {
                return m;
            } else if( value instanceof TestCase && m.getUserObject() instanceof TreePair ) {
                 if( ((TreePair) m.getUserObject() ).getTest().equals( value ) ) {
                     return m;
                 }
   	
            }
        }
        return null;

    }

  
    private void addTestToTree(TestCase test) {

        String className = test.getClass().getName();
               
        try {
            DefaultMutableTreeNode testNode = root;

            StringTokenizer st = new StringTokenizer( className.substring( 0 , className.lastIndexOf( ".") ), ".");
            
            while( st.hasMoreTokens() ) {
                
                String t = st.nextToken();
                DefaultMutableTreeNode aux = search(t, testNode);
                if (aux == null) {
                    DefaultMutableTreeNode node =  new DefaultMutableTreeNode(t);
                    testNode.add(node);
                    testNode = node;
                } else {
                    testNode = aux;
                }
            }
            
            DefaultMutableTreeNode aux = search(test.getClass(), testNode);
            if (aux == null) {
                DefaultMutableTreeNode node =  new DefaultMutableTreeNode(test.getClass());
                testNode.add(node);
                testNode = node;
            } else {
                testNode = aux;
            }
            
            aux = search( test , testNode );
            if( aux == null ) {
                DefaultMutableTreeNode node =  new DefaultMutableTreeNode(new TreePair(test));
                testNode.add(node);
                testNode = node;
                
                theTree.scrollPathToVisible( new TreePath(testNode.getPath()));
                              
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private TreePair getPair( TestCase test ) {
                        
        String className = test.getClass().getName();
        
        DefaultMutableTreeNode testNode = root;
        StringTokenizer st = new StringTokenizer( className.substring( 0 , className.lastIndexOf( ".") ), ".");
        
        while( st.hasMoreTokens() ) {
            String t = st.nextToken();
            testNode  = search(t, testNode);
        }
        
        testNode = search(test.getClass(), testNode);
        testNode = search(test , testNode );
        
        
        return (TreePair) testNode.getUserObject();
        
    }
    
    private TreePair getPair( TestInfo testInfo ) {
        return getPair( getTestCase( testInfo ));
    }
    
        
    /**
     * @param testInfo
     * @return
     */
    private TestCase getTestCase(TestInfo testInfo) {
        return testCases.get( testInfo.getClassname() + "." + testInfo.getText());
    }

    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public void testFinished( TestInfo testInfo ) {
        
        getPair( testInfo ).setState( TreePair.FINISHED );
        repaint();
        
    }
    
    /**
     * Called when a test has been started
     * @param testInfo The test info.
     */
    public void testStarted( Test test ) {
        getPair( (TestCase)test ).setState( TreePair.RUNNING ); 
        repaint();
    }
    
    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed( TestInfo testInfo ) {
        getPair( testInfo ).setState( TreePair.FAILED );
        repaint();
    }
    
    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public void testError( TestInfo testInfo ) {
        getPair(testInfo).setState( TreePair.ERROR );    
        
        repaint();
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
    
}