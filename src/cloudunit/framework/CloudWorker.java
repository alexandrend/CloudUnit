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
 * An interface to define a test worker able to run a test on a distributed infrastructure.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public interface CloudWorker extends Runnable {

    /**
     * Set the test to be executed on the cloud.
     * 
     */
    public void setTest(TestCase t);


    /**
     * Sets the listener to be notified about the execution details
     * 
     * */
    public void setCloudTestListener(CloudTestListener listener);


    /**
     * Returns true when the test execution was concluded.
     */
    public boolean workIsDone();


    /**
     * Interrrupts the execution.
     */
    public void stop();
        

}
