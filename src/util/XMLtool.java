package util;/*
package util;

import app.graph.GNode;
import app.graph.Graph;
import app.graph.XMLmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

*/
/**
 * Created on 2/13/14.
 *//*

public class XMLtool {

	*/
/**
	 * XML WRITER
	 *//*

	public static void marshal() {
		try {
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(Graph.getMap(), System.out);
			m.marshal(Graph.getMap(), new File("./data/graphtest.xml"));
		} catch (JAXBException e) { e.printStackTrace(); }
	}

	*/
/**
	 * XML READER
	 *//*

	public static XMLmap unmarshal() {
		XMLmap map = new XMLmap();
		try {
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Unmarshaller m = jc.createUnmarshaller();
			map = (XMLmap) m.unmarshal(new File("./data/graphtest.xml"));
			for (GNode n : map.getNodes()) { System.out.println(n.getId() + "::" + n.getName() + "=>" + n.getSize()); }
			Graph.setMap(map);
		} catch (JAXBException e) { e.printStackTrace(); } return map;
	}
}
*/
