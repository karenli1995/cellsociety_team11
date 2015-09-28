package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter {
	private Document myDoc;
	private Element myRoot;
	private Model myModel;

	/**
	 * Initialize with a model
	 * @param model
	 * @throws Exception
	 */
	public void init(Model model) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		myDoc = builder.newDocument();
		myModel = model;
		myRoot = myDoc.createElement("File");
		myDoc.appendChild(myRoot);
	}

	private Element addAttribute(Element root, Object name, Object attr) {
		Element e = myDoc.createElement(name.toString());
		e.appendChild(myDoc.createTextNode(attr.toString()));
		root.appendChild(e);
		return e;
	}

	private Element addRootElement(Element root, Object name) {
		Element e = myDoc.createElement(name.toString());
		root.appendChild(e);
		return e;
	}

	private void addAttributes(Element root, Map<?, ?> map) {
		map.forEach((k, v) -> {
			addAttribute(root, k, v);
		});
	}

	private void createModel() {
		Element model = addRootElement(myRoot, "model");
		addAttributes(model, myModel.getParameters());
	}

	private void createCells() {
		Element cells = addRootElement(myRoot, "cells");
		myModel.getCells().forEach(cell -> {
			addAttributes(addRootElement(cells, "cell"), cell.getAttributes());
		});
	}
	
	/**
	 * create an xml file in given directory
	 * @param dir
	 * @return absolute path name of the file created
	 * @throws Exception
	 */
	public String createXml(File dir) throws Exception {
		createModel();
		createCells();
		
		DateFormat df = new SimpleDateFormat("MMddyy_HHmmss");
    	Calendar calObj = Calendar.getInstance();
    	String timeStamp = df.format(calObj.getTime());
		String fileName = dir.getAbsolutePath()+File.separator+myModel+"_"+timeStamp+".xml";
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(myDoc);
		//transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		StreamResult result = new StreamResult(pw);
		transformer.transform(source, result);
		//System.out.println("Done! Name: " + fileName);
		return fileName;
	}

}
