package bhc.tours;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

public class Utility {

	/**
	 * Utility method for opening a URL.
	 * @param url
	 * @return
	 */
	public static boolean openWebpage(URL url) {
	    try {
	        return openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(null, "An invalid URI was specified " + e.getMessage(), 
					"Invalid URI", JOptionPane.ERROR_MESSAGE);
	    }
	    return false;
	}
	
	/** 
	 * Open a webpage via URI.  
	 * @param uri
	 * @return
	 */
	public static boolean openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	            return true;
	        } catch (Exception e) {
				JOptionPane.showMessageDialog(null, "An exception occurred when opening the URI " + uri + " Error Message: " + e.getMessage(), 
						"Invalid URI", JOptionPane.ERROR_MESSAGE);	        
			}
	    }
	    return false;
	}
}
