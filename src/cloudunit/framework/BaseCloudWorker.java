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
package cloudunit.framework;

import junit.framework.TestCase;

/**
 * Description: This class facilitates the creation of new test workers.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public abstract class BaseCloudWorker implements CloudWorker {

    /**
     * A test case to be executed.
     */
	protected TestCase test;
    
	/**
	 * The test listener to be notified about the execution status.
	 */
    protected CloudTestListener listener;
    
    
    /* (non-Javadoc)
     * @see gridunit.testrunners.GridWorker#setTest(junit.framework.TestCase)
     */
    public void setTest(TestCase t) {
        this.test = t;
        
    }

    /*
     * (non-Javadoc)
     * @see gridunit.testrunners.GridWorker#setGridTestListener(gridunit.framework.GridTestListener)
     */
    public void setCloudTestListener(CloudTestListener listener) {
        this.listener = listener;
        
    }

}
