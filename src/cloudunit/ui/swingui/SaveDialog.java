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

 * For more information: http://gridunit.sourceforge.net
 */
package cloudunit.ui.swingui;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextPane;


/**
 * 
 * Description: This is a Save Dialog. :-)
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class SaveDialog extends FileDialog {

    
	private static final long serialVersionUID = 1L;
	private JTable table;
    
   	public SaveDialog( JTable table, JFrame parent ){
        super(parent, "Save CloudUnit report" , FileDialog.SAVE );
        
        this.table = table;
        
        
        setFilenameFilter( new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith( ".cur");
            }
            
        });
        
        setDirectory(".\\");
        setFile("*.ur");
        
        show();
        

        String dir =  getDirectory();
        String file = getFile();
        
        
        if( file != null ) {
            saveTable( new File( dir +  System.getProperty( "file.separator" ).charAt(0) + file ));
        }
        
        
    }
    
    private void saveTable( File file ) {
     
        try {
            BufferedWriter writer = new BufferedWriter( new FileWriter( file ));
            
            int row = table.getModel().getRowCount();
            int col = table.getModel().getColumnCount();
            
            for( int i = 0 ; i < row; i++ ) {
                
                for( int j = 0; j < col; j++ ) {
                
                    Object o = table.getModel().getValueAt( i , j );
                    if( o instanceof JLabel ) {
                        writer.write( ( ( JLabel) o ).getText() + " "); 
                    } else if( o instanceof JTextPane ) {
                        writer.write( ( ( JTextPane ) o ).getText() + " ");
                        
                    } else {
                        writer.write( o.toString() + " " );
                    }
                    
                }
                
                writer.newLine();
            }
        
            
            writer.close();
            
        } catch (IOException e) {

            e.printStackTrace();
        }
        
        
        
    }
    
    
}
