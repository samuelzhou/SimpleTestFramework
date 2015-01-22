package samuel.test.framework.config;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import samuel.test.framework.exception.InvalidConfigurationException;

/**
 * The configuration class that help to get the values and attributes in configuration file.
 * @author sazhou
 */
public class Configuration {
	private Document doc;
	private NodeList testCaseNodes;
	private ArrayList<String> testCaseNames = new ArrayList<String>();
	private static Configuration configuration;
	/**
	 * Initialize Configuration object
	 * @param xmlFileName
	 * 		The XML configuration file, this file should have \<TestSuite\> tag to describe the test suite.
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 * 		Throws when the configuration file has invalid value or field.
	 * @return
	 * 		The configuration object.
	 */
	public static Configuration getConfiguration(String xmlFileName) throws ParserConfigurationException, SAXException,
			IOException, InvalidConfigurationException {
		if (configuration == null) {
			configuration = new Configuration(xmlFileName);
		}
		return configuration;
	}

	/**
	 * Get the initialized the configuration object, if configuration object hasn't been initialized, it will throw exception.
	 * @return
	 * 		The configuration object.
	 * @throws InvalidConfigurationException
	 */
	public static Configuration getConfiguration() throws InvalidConfigurationException {
		if (configuration == null) {
			throw new InvalidConfigurationException("Configuration has not been initialized yet!");
		}
		return configuration;
	}
	private Configuration(String xmlFileName) throws ParserConfigurationException, SAXException,
			IOException, InvalidConfigurationException {
		File xmlFile = new File(xmlFileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase("TestSuite")) {
			throw new InvalidConfigurationException(
					"The <TestSuite> tag doesn't exist in configuration file " + xmlFileName);
		}
		testCaseNodes = doc.getElementsByTagName("TestCase");
		for (int i = 0; i < testCaseNodes.getLength(); i++) {
			Node n = testCaseNodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)n;
				testCaseNames.add(e.getAttribute("name"));
			}
		}
	}

	/**
	 * Check if current test cases support Chrome browser.
	 * @return
	 * 		true if supported, false if not support.
	 */
	public boolean isSupportChrome() {
		try {
			return doc.getElementsByTagName("SupportChrome").item(0).getTextContent().equalsIgnoreCase("true");
		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<String> getTestCaseNames() {
		return testCaseNames;
	}

	/**
	 * Return the parameter name and parameter value pairs of a specified test case.
	 *
	 * @param testCaseName
	 * 		The name of a test case.
	 * @return
	 * 		The HashMap with parameter name/value pairs.
	 */
	public HashMap<String, String> getTestCaseConfigMap(String testCaseName) {
		Node node;
		Element element;
		HashMap<String, String> retMap = new HashMap<String, String>();
		for (int i = 0; i < testCaseNodes.getLength(); i++) {
			node = testCaseNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element)node;
				if (element.getAttribute("name").equals(testCaseName)) {
					NodeList nList = element.getChildNodes();
					for (int j = 0; j < nList.getLength(); j++) {
						node = nList.item(j);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							element = (Element)node;
							retMap.put(element.getTagName(), element.getTextContent());
						}
					}
					return retMap;
				}
			}
		}
		return null;
	}
}
