package fi.eis.applications.chatapp.actions.impl;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fi.eis.applications.chatapp.actions.RoomsProvider;
import fi.eis.applications.chatapp.types.ChatRoom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: eis
 * Creation Date: 8.11.2014
 * Creation Time: 18:49
 */
public class DefaultRoomsProvider implements RoomsProvider {
    private static final String ROOM_CONFIG_FILE = "rooms.xml";
    @Override
    public List<ChatRoom> getRooms() {
        return readRoomsFromXML();
    }
    private List<ChatRoom> readRoomsFromXML() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        try {

            final File configurationFile = new File(ROOM_CONFIG_FILE);

            if (!configurationFile.exists()) {
                throw new IllegalStateException(String.format("Configuration file '%s' missing from path '%s'",
                        ROOM_CONFIG_FILE, new File(".").getAbsolutePath()));
            }

            if (!configurationFile.canRead()) {
                throw new IllegalStateException(String.format("Configuration file '%s' cannot be read from path",
                        ROOM_CONFIG_FILE, new File(".").getAbsolutePath()));
            }

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (configurationFile);

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " +
                    doc.getDocumentElement().getNodeName());


            NodeList listOfRooms = doc.getElementsByTagName("room");
            int totalRooms = listOfRooms.getLength();
            System.out.println("Total no of items: " + totalRooms);

            for(int s=0; s<listOfRooms.getLength() ; s++){


                Node roomNode = listOfRooms.item(s);
                if(roomNode.getNodeType() == Node.ELEMENT_NODE){


                    Element roomElement = (Element)roomNode;

                    //-------
                    NodeList firstNameList = roomElement.getElementsByTagName("roomId");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    String roomId = ((Node)textFNList.item(0)).getNodeValue().trim();

                    //-------
                    NodeList lastNameList = roomElement.getElementsByTagName("roomName");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    String roomName =
                            ((Node)textLNList.item(0)).getNodeValue().trim();

                    chatRooms.add(new ChatRoom(Integer.parseInt(roomId), roomName));

                }//end of if clause


            }//end of for loop with s var

            return chatRooms;


        }catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line "
                    + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());
            throw new IllegalStateException(err);
        }catch (SAXException e) {
            throw new IllegalStateException(e);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
