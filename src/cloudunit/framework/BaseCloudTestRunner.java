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


import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.smartfrog.services.junit.TestInfo;

import java.util.concurrent.*;

/**
 * 
 * Description: This abstract class provides an easy start up to build a real grid test runner.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public abstract class BaseCloudTestRunner implements CloudTestRunner, CloudTestListener {

	/**
	 * A collection of test listeners.
	 */
    private Collection <CloudTestListener> gridTestListeners;

    /**
     * A Thread pool to control the execution.
     */
    private ExecutorService pool;
            
    /**
     * A collection of GridWorkers.
     */
    private Collection <CloudWorker> workers;
    
    /**
     * A Factory of test workers.
     */
    private CloudWorkerFactory factory;
    
    
    /**
     * Full constructor
     */
    public BaseCloudTestRunner( CloudWorkerFactory factory ) {
        
        gridTestListeners = new Vector <CloudTestListener>();
        workers = new Vector <CloudWorker>();
        pool = Executors.newFixedThreadPool(100);
        this.factory = factory;
        
    }

    /**
     * Runs all tests in a given test suite.
     * @param suite The test suite.
     */
    public void run(TestSuite suite) {
                
        notifyStarted();
               
        innerRun( suite );
        
        waitUntilWorkIsDone();
        notifyFinished();
        
    }
    
    /**
     * Accessory run method.
     * @param suite The test suite.
     */
    private void innerRun( TestSuite suite ) {
        
        Enumeration<Test> tests = suite.tests();
        while (tests.hasMoreElements()) {
            
            Test o = tests.nextElement();
            
            if( o instanceof TestCase ) {
            
                TestCase t = (TestCase) o;

                CloudWorker worker = factory.createCloudWorker();
                worker.setTest( t );
                worker.setCloudTestListener( this );
                workers.add(worker);

                pool.execute(worker);
            } else if( o instanceof TestSuite ) {
                innerRun( (TestSuite) o );
            }
                
        }

    }

    /**
     * Blocks application until work is done.
     */
    private void waitUntilWorkIsDone() {
        
        boolean done = false;
        while (!done) {

            try {
                Thread.sleep((long) (2000 * Math.random()));
            } catch (InterruptedException e) {
            }
            done = true;

            synchronized (workers) {
                Iterator <CloudWorker> it = workers.iterator();
                while (it.hasNext()) {
                    if (!(it.next()).workIsDone()) {
                        done = false;
                        break;
                    }
                }
            }
        }

    }

   /**
    * Adds a GridTestListener interested in this GridTestRunner events.
    */
    public void addCloudTestListener(CloudTestListener aGridTestListener) {
        
        synchronized (gridTestListeners) {
            gridTestListeners.add(aGridTestListener);
        }
    }

    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public void testFinished( TestInfo testInfo ) {
        synchronized (gridTestListeners) {
            Iterator <CloudTestListener> it = gridTestListeners.iterator();
            while (it.hasNext()) {

                CloudTestListener gt = it.next();
                gt.testFinished(testInfo );

            }
        }

    }

    /**
     * Called when a test has been started
     * @param test The test.
     */
    public void testStarted( Test test) {
        
        synchronized (gridTestListeners) {
            Iterator <CloudTestListener> it = gridTestListeners.iterator();
            while (it.hasNext()) {

                CloudTestListener gt = it.next();
                gt.testStarted(test);

            }
        }
        
    }

    /**
     * Called when a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public void testError( TestInfo testInfo ){
        
        synchronized (gridTestListeners) {
            Iterator <CloudTestListener>it = gridTestListeners.iterator();
            while (it.hasNext()) {

                CloudTestListener gt =  it.next();
                gt.testError(testInfo);
            }
        }
        
    }
    

    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed( TestInfo testInfo ) {
        
        synchronized (gridTestListeners) {
            Iterator <CloudTestListener> it = gridTestListeners.iterator();
            while (it.hasNext()) {

                CloudTestListener gt = it.next();
                gt.testFailed(testInfo);

            }
        }

    }

    /**
     * Called when the test phase has been started.
     */
    public void testPhaseStarted(){
    }
    
    
    /**
     * Called when all tests were executed (either successfully or unsuccessfully).
     */
    public void testPhaseFinished() {
    }
  
    /**
     *  Notifies all listeners a test phase started.
     */
    private void notifyStarted() {
        
        Iterator <CloudTestListener>it = gridTestListeners.iterator();
        while( it.hasNext() ) {
            CloudTestListener gtl = it.next();
            gtl.testPhaseStarted();
        }
      
    }
    
    /**
     * Notifies all listeners a test phase finished.
     */
    private void notifyFinished() {
        Iterator <CloudTestListener> it = gridTestListeners.iterator();
        while( it.hasNext() ) {
            CloudTestListener gtl = it.next();
            gtl.testPhaseFinished();
        }
    }

    /**
     * Stops the test execution.
     */
    public void stop() {
        synchronized( workers ) {
            Iterator <CloudWorker> it = workers.iterator();
            while( it.hasNext() ) {
                CloudWorker worker = it.next();
                worker.stop();
            }
        }
    }

}