package fi.eis.applications.chatapp.login.actions.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.types.ChatRoom;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Creation Date: 8.11.2014
 * Creation Time: 18:49
 *
 * Class retrieves the room list from an XML file.
 *
 * Alternative constructors are offered: log levels, one with default "rooms.xml" and another with
 * user-specified file name (that can contain a path with it).
 *
 * @author eis
 */
public class XMLBasedRoomsProvider implements RoomsProvider {
    private final String ROOM_CONFIG_FILE;
    private final SimpleLogger logger;

    public XMLBasedRoomsProvider() {
        this(LogLevel.ERROR);
    }
    public XMLBasedRoomsProvider(LogLevel logLevel) {
        this("rooms.xml", logLevel);
    }
    public XMLBasedRoomsProvider(final String fileName) {
        this(fileName, LogLevel.ERROR);
    }

    public XMLBasedRoomsProvider(final String fileName, LogLevel logLevel) {
        this.logger = new SimpleLogger(this.getClass());
        this.logger.setLogLevel(logLevel);
        this.ROOM_CONFIG_FILE = fileName;
    }

    @Override
    public List<ChatRoom> getRooms() {
        return readRoomsFromXML();
    }

    private List<ChatRoom> readRoomsFromXML() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        try {

            final File configurationFile = new File(this.ROOM_CONFIG_FILE);

            if (!configurationFile.exists()) {
                throw new IllegalStateException(String.format("Configuration file '%s' missing from path '%s'",
                        this.ROOM_CONFIG_FILE, new File(".").getAbsolutePath()));
            }

            if (!configurationFile.canRead()) {
                throw new IllegalStateException(String.format("Configuration file '%s' cannot be read from path",
                        this.ROOM_CONFIG_FILE, new File(".").getAbsolutePath()));
            }

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (configurationFile);

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            logger.debug("Root element of the doc is " +
                    doc.getDocumentElement().getNodeName());


            NodeList listOfRooms = doc.getElementsByTagName("room");
            int totalRooms = listOfRooms.getLength();
            logger.debug("Total no of items: " + totalRooms);

            for(int s=0; s<listOfRooms.getLength() ; s++){


                Node roomNode = listOfRooms.item(s);
                if(roomNode.getNodeType() == Node.ELEMENT_NODE){


                    Element roomElement = (Element)roomNode;

                    //-------
                    NodeList firstNameList = roomElement.getElementsByTagName("roomId");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    String roomId = textFNList.item(0).getNodeValue().trim();

                    //-------
                    NodeList lastNameList = roomElement.getElementsByTagName("roomName");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    String roomName = textLNList.item(0).getNodeValue().trim();

                    chatRooms.add(new ChatRoom(Integer.parseInt(roomId), roomName));

                }//end of if clause


            }//end of for loop with s var

            return chatRooms;


        } catch (SAXParseException err) {
            logger.error("** Parsing error" + ", line "
                    + err.getLineNumber () + ", uri " + err.getSystemId ());
            logger.error(" " + err.getMessage ());
            throw new IllegalStateException(err);
        } catch (SAXException | ParserConfigurationException | IOException | FactoryConfigurationError | ClassCastException e) {
            throw new IllegalStateException(e);
        }
    }

}
