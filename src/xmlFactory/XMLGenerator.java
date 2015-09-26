package xmlFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class XMLGenerator{
    private Document myDoc;
    private Element myRoot;
    private Model myModel;
    
    public static void main(String[] args){
    	XMLGenerator generator = new XMLGenerator();
    	Model[] models = {new GOLModel(50,50,0.5),
    						new PredModel(50,50,0.2,0.7),
    						new SegModel(50,50,0.45,0.45),
    						new FireModel(50,50)};
    	for(Model model:models){
    		generator.init(model);
        	generator.createXml();
    	}
    }
 
    public void init(Model model) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            myDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
        myModel = model;
        myRoot = createRoot("MyModel");
    }
    
    private Element createRoot(String name){
    	Element root = myDoc.createElement(name);
    	myDoc.appendChild(root);
    	return root;
    }
 
    private Element addAttribute(Element root, Object name,Object attr){
    	Element e = myDoc.createElement(name.toString());
    	e.appendChild(myDoc.createTextNode(attr.toString()));
    	root.appendChild(e);
    	return e;
    }
    
    private Element addRootElement(Element root, Object name){
    	Element e = myDoc.createElement(name.toString());
    	root.appendChild(e);
    	return e;
    }
    
    private void addAttributes(Element root, Map<?,?> map){
    	map.forEach((k,v)->{addAttribute(root,k,v);});
    }
    
    private void createModel(){
    	Element model = addRootElement(myRoot,"model");
    	addAttributes(model, myModel.getModel());
    }
    
    private void createCells(){
    	Element cells = addRootElement(myRoot,"cells");
    	myModel.getCells().forEach(map->{
    		addAttributes(addRootElement(cells,"cell"),map);
    	});
    }
    
    public void createXml() {
    	
        createModel();
        createCells();
        String fileName = myModel+".xml";
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(myDoc);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            System.out.println("Successfully generated XML file!");
            System.out.println("Name: "+fileName);
        } catch (TransformerConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (TransformerException e) {
            System.out.println(e.getMessage());
        }
    }
 
    public void parserXml(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
             
            NodeList cells = document.getChildNodes();
            for (int i = 0; i < cells.getLength(); i++) {
                Node employee = cells.item(i);
                NodeList cellInfo = employee.getChildNodes();
                for (int j = 0; j < cellInfo.getLength(); j++) {
                    Node node = cellInfo.item(j);
                    NodeList cellMeta = node.getChildNodes();
                    for (int k = 0; k < cellMeta.getLength(); k++) {
                        System.out.println(cellMeta.item(k).getNodeName()
                                + ":" + cellMeta.item(k).getTextContent());
                    }
                }
            }
            System.out.println("Completed");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}