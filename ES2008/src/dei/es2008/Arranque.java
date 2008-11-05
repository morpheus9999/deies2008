/*
 * @(#)Main.java
 *
 * This work is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package dei.es2008;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * The main class of the Tetris game. This class contains the
 * necessary methods to run the game either as a stand-alone
 * application or as an applet inside a web page.
 *
 * @version  1.2
 * @author   Per Cederberg, per@percederberg.net
 */
public class Arranque extends Applet {

    /**
     * The Tetris game being played (in applet mode)
     */
    private Gui game = null;

    public static void main (String[] args) {
        JFrame frame = new JFrame("Tetris");
        Gui game = new Gui(false);

        // Set up frame
        frame.add(game.getComponent());
        frame.pack();

        // Add frame window listener
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Show frame (and start game)
		center(frame);
        frame.show();
	}
	
	 static private void center (JFrame frame) {
	    //center window
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = frame.getSize();

	    if (frameSize.height > screenSize.height) {
	        frameSize.height = screenSize.height;
	    }

	    if (frameSize.width > screenSize.width) {
	        frameSize.width = screenSize.width;
	    }

	    frame.setLocation((screenSize.width - frameSize.width) / 2,
	        (screenSize.height - frameSize.height) / 2);
	}

    /**
     * Initializes the game in applet mode.
     */
    public void init() {
        // Create game object
        game = new Gui(true);
		game.setAppletContext(getAppletContext());

        // Initialize applet component
        setLayout(new BorderLayout());
        add(game.getComponent(), "Center");
    }

    /**
     * Stops the game in applet mode.
     */
    public void stop() {
        game.quit();
    }
 
    /**
     * A dummy COM object wrapper. This class has been created only to
     * avoid the erroneous HTTP lookup for it when the Tetris game is
     * run as an applet in some browsers.
     * 
     * @version  1.0
     * @author   Per Cederberg, per@percederberg.net
     */
    
}
