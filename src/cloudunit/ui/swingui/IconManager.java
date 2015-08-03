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

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


/**
 * 
 * Description: This is an IconManager :-)
 * 
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class IconManager {
    
    private static Map<String, ImageIcon> icons;
    
    public static final String classIcon = "class";
    public static final String packageIcon = "package";
    public static final String gridunitIcon = "gridunit";
    public static final String testIcon = "test";
    public static final String errorIcon = "error";
    public static final String failureIcon = "failure";
    public static final String hierarchyIcon = "hierarchy";
    public static final String infoIcon = "info";
    public static final String traceIcon = "trace";
    public static String finishedIcon = "ok";
    public static String runningIcon = "running";
    
    
    public static synchronized ImageIcon getIcon( String name ) {

        if( icons == null ) {
            createMapIcon();
        }
        
        return icons.get(name);
        
    }
    
    private static void createMapIcon() {

        icons =  new HashMap<String, ImageIcon>();
        icons.put( classIcon , new ImageIcon("resources/icons/class.gif") );
        icons.put( packageIcon, new ImageIcon("resources/icons/package.gif"));
        icons.put( gridunitIcon , new ImageIcon("resources/icons/gridunit.gif"));
        icons.put( testIcon, new ImageIcon("resources/icons/gridtest.gif"));
        icons.put( errorIcon , new ImageIcon("resources/icons/error.gif"));
        icons.put( failureIcon , new ImageIcon("resources/icons/failure.gif"));
        icons.put( hierarchyIcon , new ImageIcon("resources/icons/hierarchy.gif"));
        icons.put( infoIcon , new ImageIcon("resources/icons/info.gif"));
        icons.put( traceIcon , new ImageIcon("resources/icons/trace.gif"));
        icons.put( finishedIcon , new ImageIcon("resources/icons/ok.gif"));
        icons.put( runningIcon , new ImageIcon("resources/icons/running.gif"));
        
    }
    
}
