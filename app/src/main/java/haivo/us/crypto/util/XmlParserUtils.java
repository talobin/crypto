package haivo.us.crypto.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class XmlParserUtils {
    public static Node getFirstElementByTagName(Document doc, String name) {
        NodeList nodes = doc.getElementsByTagName(name);
        if (nodes == null || nodes.getLength() <= 0) {
            return null;
        }
        return nodes.item(0);
    }

    public static double getDoubleNodeValue(Node node) throws Exception {
        return Double.parseDouble(getTextNodeValue(node));
    }

    public static String getTextNodeValue(Node node) throws Exception {
        if (node != null && node.hasChildNodes()) {
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == (short) 3) {
                    return child.getNodeValue();
                }
            }
        }
        return BuildConfig.VERSION_NAME;
    }
}
