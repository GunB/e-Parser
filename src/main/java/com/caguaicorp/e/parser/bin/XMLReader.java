package com.caguaicorp.e.parser.bin;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.caguaicorp.e.parser.model.ElementHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLReader
        implements ElementHandler {

    String strPath;
    String strName;
    String strNewSufij = "New" + File.separator;
    String strFile2Change = "metadata.xml";
    File fileData = null;

    public XMLReader(String strPath, String strName) throws IOException {
        this.strPath = strPath;
        this.strName = strName;
        this.fileData = new File(this.strPath + File.separator + this.strName);
    }

    public XMLReader(File fileData) throws IOException {
        this.strPath = fileData.getParent();
        this.strName = fileData.getName();
        this.fileData = fileData;
        System.out.println("Path: " + this.strPath);
        System.out.println("Name: " + this.strName);
        System.out.println("File: " + this.fileData.getName());
    }

    @Override
    public Document Read(boolean createXML) throws IOException, SAXException, ParserConfigurationException {
        File fXmlFile = this.fileData;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(fXmlFile);
    }

    @Override
    public void WriteFinish(Document doc) throws IOException, TransformerException {
        System.out.println("Saving [File]: " + this.fileData.getPath());

        File f = this.fileData;

        f.getParentFile().mkdirs();
        f.createNewFile();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(f);
        transformer.transform(source, result);

        System.out.println("Saved [File]: " + this.fileData.getPath());
    }
}
