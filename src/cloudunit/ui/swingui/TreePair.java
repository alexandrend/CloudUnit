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

import junit.framework.TestCase;

/**
 * 
 * Description: A node in the tree.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class TreePair {
    
    public static final int UNSTARTED = 0;
    public static final int RUNNING = 1;
    public static final int FAILED = 2;
    public static final int ERROR = 3;
    public static final int FINISHED = 4;
    
    private TestCase test;
    private int state = -1;
    
    public TreePair( TestCase test ) {
        this.test = test;
        setState( UNSTARTED);
    }
    
    public TestCase getTest() {
        return test;
    }
    
    public void setState( int state ) {
        this.state = state;
    }
  
    /**
     * @return The state of this tree pair.
     */
    public int getState() {
        return state;
       
    }
    
}
