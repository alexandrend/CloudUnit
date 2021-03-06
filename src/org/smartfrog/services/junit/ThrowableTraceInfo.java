/** (C) Copyright 1998-2004 Hewlett-Packard Development Company, LP

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 For more information: www.smartfrog.org

 */
package org.smartfrog.services.junit;

import java.io.Serializable;

/**
 * this is a serialization of a throwable, including any nested throwable.
 * Only messages, classnames and stack traces are retained.
 * We do not cache the tostring code, assuming that the message field contains
 * any message of relevance
 * created 15-Apr-2004 13:44:19
 */

public final class ThrowableTraceInfo implements Serializable, Cloneable {

    /**
     * the classname of the exception
     *
     * @serial
     */
    private String classname;

    /**
     * any message
     *
     * @serial
     */
    private String message;

    /**
     * any localized message
     *
     * @serial
     */
    private String localizedMessage;

    /**
     * stack trace
     *
     * @serial
     */
    private StackTraceElement[] stack;

    /**
     * nested cause (may be null)
     *
     * @serial
     */
    private ThrowableTraceInfo cause;

    public ThrowableTraceInfo() {
    }

    public ThrowableTraceInfo(Throwable fault) {
        fillInFromThrowable(fault);
    }

    /**
     * fill in our state from a fault
     *
     * @param fault
     */
    public void fillInFromThrowable(Throwable fault) {
        //assert fault != null;
        classname = fault.getClass().getName();
        message = fault.getMessage();
        localizedMessage = fault.getLocalizedMessage();
        stack = fault.getStackTrace();
        //maybe recurse down
        if (fault.getCause() != null) {
            cause = new ThrowableTraceInfo(fault.getCause());
        }
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    public StackTraceElement[] getStack() {
        return stack;
    }

    public void setStack(StackTraceElement[] stack) {
        this.stack = stack;
    }

    public ThrowableTraceInfo getCause() {
        return cause;
    }

    public void setCause(ThrowableTraceInfo cause) {
        this.cause = cause;
    }

    /**
     * test for this fault containing an assertion failure;
     * done by looking at the original classname
     *
     * @return
     */
    public boolean isAssertionFailure() {
        boolean b = "junit.framework.AssertionFailedError".equals(classname);
        b |= "junit.framework.ComparisionFailedError".equals(classname);
        return b;

    }

    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( !(o instanceof ThrowableTraceInfo) ) {
            return false;
        }

        final ThrowableTraceInfo throwableTraceInfo = (ThrowableTraceInfo) o;

        if ( !classname.equals(throwableTraceInfo.classname) ) {
            return false;
        }
        if ( message != null ? !message.equals(throwableTraceInfo.message) : throwableTraceInfo.message != null ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = classname.hashCode();
        result = 29 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    /**
     * deep clone
     * @return
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        ThrowableTraceInfo cloned=(ThrowableTraceInfo) super.clone();
        if(cause!=null) {
            cloned.cause=(ThrowableTraceInfo)cause.clone();
        }
        return cloned;
    }

    public String toString() {
        if(classname==null) {
            return "uninitialized";
        }
        String location;
        if(stack!=null &&  stack.length>0 && stack[0]!=null) {
            location="@"+stack[0].toString();
        } else {
            location="";
        }
        return classname+"::"+message+ " "+location;
    }
}
