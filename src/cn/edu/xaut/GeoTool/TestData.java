/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.GeoTool;

/**
 * @author Administrator
 */

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;


/**
 * Provides access to test-data directories associated
 * with junit tests.
 * <p>
 * We have chosen test-data to follow the javadoc "doc-files" convention
 * of ensuring that data directories don't look anything like normal
 * java packages.
 * </p>
 * <p>
 * Example:
 * <pre><code>
 * class MyClass {
 *   public void example(){
 *     Image testImage =
 *       new ImageIcon( TestData.getResource( this, "test.png" ) ).getImage();
 *     Reader reader = TestData.getReader( this, "script.xml" );
 *   }
 * }
 * </code></pre>
 * Where:
 * <ul>
 * <li>MyClass.java<li>
 * <li>test-data/test.png</li>
 * <li>test-data/script.xml</li>
 * </ul>
 * </ul>
 * </p>
 * <p>
 * By convention you should try and locate test-data near the junit test
 * cases that uses it.
 * </p>
 *
 * @since 2.0
 * @version $Id$
 * @author James McGill
 * @author Simone Giannecchiin (simboss)
 */
public class TestData {
/**
 * Provided a {@link BufferedReader} for named test data.
 * It is the caller responsability to close this reader after usage.
 *
 * @param caller The class of the object associated with named data.
 * @param name of test data to load.
 * @return The reader, or {@code null} if the named test data are not found.
 * @throws IOException if an error occurs during an input operation.
 */
public static final BufferedReader getReader(final Class caller, final String name)
        throws IOException {
    final URL url = getResource(caller, name);
    if (url == null) {
        return null; // echo handling of getResource( ... )
    }
    return new BufferedReader(new InputStreamReader(url.openStream()));
}

/**
 * Provided a {@link BufferedReader} for named test data.
 * It is the caller responsability to close this reader after usage.
 *
 * @param host Object associated with named data
 * @param name of test data to load
 * @return The reader, or {@code null} if the named test data are not found.
 * @throws IOException if an error occurs during an input operation.
 */
public static final BufferedReader getReader(final Object host, final String name)
        throws IOException {
    return getReader(host.getClass(), name);
}

/**
 * Locate named test-data resource for caller.
 *
 * @param caller Class used to locate test-data.
 * @param name name of test-data.
 * @return URL or null of named test-data could not be found.
 *
 * @todo Should this be getURL() - or simply url?
 *       I tend to save getX method for accessors.
 */
public static URL getResource(final Class caller, String name) {
    if (name == null) {
        name = "test-data";
    } else {
        name = "test-data/" + name;
    }
    return caller.getResource(name);
}

/**
 * Locate named test-data resource for caller.
 *
 * @param caller Object used to locate test-data
 * @param name name of test-data
 * @return URL or null of named test-data could not be found
 *
 * @todo Should this be getURL() - or simply url?
 *       I tend to save getX method for accessors.
 */
public static final URL getResource(final Object caller, final String name) {
    return getResource(caller.getClass(), name);
}

/**
 * Access to {@code getResource(caller, path)} as a {@link File}.
 * <p>
 * You can access the test-data directory with:
 * <pre><code>
 * TestData.file( this, null )
 * </code></pre>
 * </p>
 *
 * @param caller Calling object used to locate test-data
 * @param path Path to file in testdata
 * @return File from test-data
 * @throws IOException if the file is not found.
 */
public static final File file(final Object caller, final String path) throws IOException {
    final URL url = getResource(caller, path);
    if (url != null) {
        // Based SVGTest
        final File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        if (file.exists()) {
            return file;
        }
    }
    throw new FileNotFoundException("Could not locate test-data: " + path);
}

/**
 * Access to {@code getResource(caller, path)} as a {@link File}.
 * <p>
 * You can access the test-data directory with:
 * <pre><code>
 * TestData.file( MyClass.class, null )
 * </code></pre>
 * </p>
 *
 * @param caller Calling class used to locate test-data
 * @param path Path to file in testdata
 * @return File from test-data
 * @throws IOException if the file is not found.
 */
public static final File file(final Class caller, final String path) throws IOException {
    final URL url = getResource(caller, path);
    if (url != null) {
        // Based SVGTest
        final File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        if (file.exists()) {
            return file;
        }
    }
    throw new FileNotFoundException("Could not locate test-data: " + path);
}

/**
 * Creates a temporary file with the given name.
 */
public static final File temp(final Object caller, final String name) throws IOException {
    File testData = file(caller, null);
    int split = name.lastIndexOf('.');
    String prefix = split == -1 ? name : name.substring(0, split);
    String suffix = split == -1 ? null : name.substring(split + 1);
    File tmp = File.createTempFile(prefix, "." + suffix, testData);
    tmp.deleteOnExit();
    return tmp;
}
}
