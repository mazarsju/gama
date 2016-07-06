package msi.ummisco.modelLibraryGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CheckConcepts {
	private static void executeForAWebsitePart(String path, String websitePart) {
		ArrayList<File> listFiles = new ArrayList<File>();
		Utils.getFilesFromFolder(path,listFiles);
		ArrayList<File> gamlFiles = Utils.filterFilesByExtension(listFiles,"md");
		
		ArrayList<String> listConcept = new ArrayList<String>();
		
		for (File file : gamlFiles) {
			try {
				listConcept = Utils.getConceptKeywords(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String concept : listConcept) {
				if (!ConceptManager.conceptIsPossibleToAdd(concept)) {
					System.out.println("WARNING : The concept "+concept+" is not a predefined concept !!");
				}
				else ConceptManager.addOccurrenceOfConcept(concept, websitePart);
			}
		}
	}
	
	private static void browseKeywords(String path) {
	    try {
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(path);
	    			
	    	doc.getDocumentElement().normalize();
	    			
	    	NodeList nList = doc.getElementsByTagName("keyword");

	    	for (int temp = 0; temp < nList.getLength(); temp++) {
	    		Node nNode = nList.item(temp);
	    		Element eElement = (Element) nNode;
	    		String category = eElement.getElementsByTagName("category").item(0).getTextContent();
	    		String conceptName = eElement.getElementsByTagName("name").item(0).getTextContent();
	    		if (category.equals("concept")) {
		    		if (ConceptManager.conceptIsPossibleToAdd(conceptName)) {
		    			for (int i=0; i < eElement.getElementsByTagName("associatedKeyword").getLength(); i++) {
		    				ConceptManager.addOccurrenceOfConcept(conceptName, ConceptManager.WebsitePart.GAML_REFERENCES.toString());
		    			}
	    			}
	    		}
	    	}
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	private static void writeReport(String file) throws IOException {
		String result = "";
		
		// read the file
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		
		while ((line = br.readLine()) != null) {
			if (line.contains("__________________________________")) {
				result += line+"\n";
				break;
			}
			else {
				result += line+"\n";
			}
		}
		br.close();
		result += "\n\n";
		
		// add the statistics
		result += ConceptManager.getExtendedStatistics();
		
		// write the file
		File outputFile = new File(file);
		FileOutputStream fileOut = new FileOutputStream(outputFile);
		fileOut.write(result.getBytes());
		fileOut.close();
	}
}
