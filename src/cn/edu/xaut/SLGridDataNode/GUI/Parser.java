package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * Created by IntelliJ IDEA.
 * User: HeTing
 * Date: 11-12-21
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */

import org.apache.xerces.parsers.SAXParser;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.Configuration;
import org.geotools.xml.impl.ParserHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import static cn.edu.xaut.SLGridDataNode.GUI.XS.NAMESPACE;

/**
 * Main interface to the geotools xml parser.
 *
 * <p>
 * <h3>Schema Resolution</h3>
 * See {@link org.geotools.xml.Configuration} javadocs for instructions on how
 * to customize schema resolution. This is often desirable in the case that
 * the instance document being parsed contains invalid uri's in schema imports
 * and includes.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class Parser {
/**
 * sax handler which maintains the element stack
 */
private ParserHandler handler;

/**
 * the sax parser driving the handler
 */
private SAXParser parser;

/**
 * the instance document being parsed
 */
private InputStream input;

/**
 * Creats a new instance of the parser.
 *
 * @param configuration The parser configuration, bindings and context,
 *                      must never be <code>null</code>.
 */
public Parser(Configuration configuration) {

    if (configuration == null) {
        throw new NullPointerException("configuration");
    }

    handler = new ParserHandler(configuration);
}

/**
 * Creates a new instance of the parser.
 *
 * @param configuration Object representing the configuration of the parser.
 * @param input         A uri representing the instance document to be parsed.
 * @throws ParserConfigurationException
 * @throws SAXException                 If a sax parser can not be created.
 * @throws URISyntaxException           If <code>input</code> is not a valid uri.
 * @deprecated use {@link #Parser(Configuration)} and {@link #parse(InputStream)}.
 */
public Parser(Configuration configuration, String input)
        throws IOException, URISyntaxException {
    this(configuration, new BufferedInputStream(
            new FileInputStream(new File(new URI(input)))));
}

/**
 * Creates a new instance of the parser.
 *
 * @param configuration Object representing the configuration of the parser.
 * @param input         The stream representing the instance document to be parsed.
 * @deprecated use {@link #Parser(Configuration)} and {@link #parse(InputStream)}.
 */
public Parser(Configuration configuration, InputStream input) {
    this(configuration);
    this.input = input;
}

/**
 * Signals the parser to parse the entire instance document. The object
 * returned from the parse is the object which has been bound to the root
 * element of the document. This method should only be called once for
 * a single instance document.
 *
 * @return The object representation of the root element of the document.
 * @throws IOException
 * @throws SAXException
 * @throws ParserConfigurationException
 * @deprecated use {@link #parse(InputStream)}
 */
public Object parse() throws IOException, SAXException,
                                     ParserConfigurationException {
    return parse(input);
}

/**
 * Parses an instance documented defined by an input stream.
 * <p>
 * The object returned from the parse is the object which has been bound to the root
 * element of the document. This method should only be called once for a single instance document.
 * </p>
 *
 * @return The object representation of the root element of the document.
 * @throws IOException
 * @throws SAXException
 * @throws ParserConfigurationException
 */
public Object parse(InputStream input) throws IOException,
                                                      SAXException, ParserConfigurationException {
    return parse(new InputSource(input));
}

/**
 * Parses an instance documented defined by a reader.
 * <p>
 * The object returned from the parse is the object which has been bound to the root
 * element of the document. This method should only be called once for a single instance document.
 * </p>
 *
 * @return The object representation of the root element of the document.
 * @throws IOException
 * @throws SAXException
 * @throws ParserConfigurationException
 */
public Object parse(Reader reader) throws IOException,
                                                  SAXException, ParserConfigurationException {
    return parse(new InputSource(reader));
}

/**
 * Parses an instance documented defined by a sax input source.
 * <p>
 * The object returned from the parse is the object which has been bound to the root
 * element of the document. This method should only be called once for a single instance document.
 * </p>
 *
 * @return The object representation of the root element of the document.
 * @throws IOException
 * @throws SAXException
 * @throws ParserConfigurationException
 */
public Object parse(InputSource source) throws IOException,
                                                       SAXException, ParserConfigurationException {
    parser = parser();
    parser.setContentHandler(handler);
    parser.setErrorHandler(handler);

    parser.parse(source);

    return handler.getValue();
}

/**
 * Sets the strict parsing flag.
 * <p>
 * When set to <code>true</code>, this will cause the parser to operate in
 * a strict mode, which means that xml being parsed must be exactly correct
 * with respect to the schema it references.
 * </p>
 * <p>
 * Some examples of cases in which the parser will throw an exception while
 * operating in strict mode:
 * <ul>
 *  <li>no 'schemaLocation' specified, or specified incorrectly
 *  <li>element found which is not declared in the schema
 * </ul>
 * </p>
 *
 * @param strict The strict flag.
 */
public void setStrict(boolean strict) {
    handler.setStrict(strict);
}

/**
 * Sets the flag controlling wether the parser should validate or not.
 *
 * @param validating Validation flag, <code>true</code> to validate, otherwise <code>false</code>
 */
public void setValidating(boolean validating) {
    handler.setValidating(validating);
}

/**
 * Returns a list of any validation errors that occured while parsing.
 *
 * @return A list of errors, or an empty list if none.
 */
public List getValidationErrors() {
    return handler.getValidationErrors();
}

/**
 * Returns the schema objects referenced by the instance document being
 * parsed. This method can only be called after a successful parse has
 * begun.
 *
 * @return The schema objects used to parse the document, or null if parsing
 * has not commenced.
 */
public XSDSchema[] getSchemas() {
    if (handler != null) {
        return handler.getSchemas();
    }

    return null;
}

/**
 * Returns the namespace mappings maintained by the parser.
 * <p>
 * Clients may register additional namespace mappings. This is useful when
 * an application whishes to provide some "default" namespace mappings.
 * </p>
 * <p>
 * Clients should register namespace mappings in the current "context", ie
 * do not call {@link NamespaceSupport#pushContext()}. Example:
 * <code>
 * Parser parser = new Parser( ... );
 * parser.getNamespaces().declarePrefix( "foo", "http://www.foo.com" );
 * ...
 * </code>
 * </p>
 *
 * @return The namespace support containing prefix to uri mappings.
 * @since 2.4
 */
public NamespaceSupport getNamespaces() {
    return handler.getNamespaceSupport();
}

protected SAXParser parser() throws ParserConfigurationException,
                                            SAXException {
    //JD: we use xerces directly here because jaxp does seem to allow use to
    // override all the namespaces to validate against
    SAXParser parser = new SAXParser();

    //set the appropriate features
    parser.setFeature("http://xml.org/sax/features/namespaces",
            true);
    if (handler.isValidating()) {
        parser.setFeature("http://xml.org/sax/features/validation",
                true);
        parser.setFeature(
                "http://apache.org/xml/features/validation/schema",
                true);
        parser
                .setFeature(
                        "http://apache.org/xml/features/validation/schema-full-checking",
                        true);
    }

    //set the schema sources of this configuration, and all dependent ones
    StringBuffer schemaLocation = new StringBuffer();
    for (Iterator d = handler.getConfiguration().allDependencies()
                              .iterator(); d.hasNext(); ) {
        Configuration dependency = (Configuration) d.next();

        //ignore xs namespace
        if (NAMESPACE.equals(dependency.getNamespaceURI()))
            continue;

        //seperate entries by space
        if (schemaLocation.length() > 0) {
            schemaLocation.append(" ");
        }

        //add the entry
        schemaLocation.append(dependency.getNamespaceURI());
        schemaLocation.append(" ");
        schemaLocation.append(dependency.getSchemaFileURL());
    }

    //set hte property to map namespaces to schema locations
    parser
            .setProperty(
                    "http://apache.org/xml/properties/schema/external-schemaLocation",
                    schemaLocation.toString());

    //set the default location
    parser
            .setProperty(
                    "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
                    handler.getConfiguration().getSchemaFileURL());

    return parser;
}

/**
 * Properties used to control the parser behaviour.
 * <p>
 * Parser properties are set in the configuration of a parser.
 * <pre>
 * Configuration configuration = new ....
 * configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ELEMENTS );
 * configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ATTRIBUTES );
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @deprecated
 */
public static interface Properties {

    /**
     * If set, the parser will continue to parse when it finds an element
     * and cannot determine its type.
     *
     * @deprecated use {@link Parser#setStrict(boolean)}
     */
    QName PARSE_UNKNOWN_ELEMENTS = new QName(
            "http://www.geotools.org", "parseUnknownElements");

    /**
     * If set, the parser will continue to parse when it finds an attribute
     * and cannot determine its type.
     *
     * @deprecated use {@link Parser#setStrict(boolean)}
     */
    QName PARSE_UNKNOWN_ATTRIBUTES = new QName(
            "http://www.geotools.org", "parseUnknownAttributes");

    /**
     * If set, the parser will ignore the schemaLocation attribute of an
     * instance document.
     *
     * @deprecated use {@link Parser#setStrict(boolean)}
     */
    QName IGNORE_SCHEMA_LOCATION = new QName(
            "http://www.geotools.org", "ignoreSchemaLocation");
}
}

