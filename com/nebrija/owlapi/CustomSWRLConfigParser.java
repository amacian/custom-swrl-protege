package com.nebrija.owlapi.customswrl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*
* Copyright (C) 2012, Universidad Antonio de Nebrija
*
* Modifications to the initial code base are copyright of their
* respective authors, or their employers as appropriate.  Authorship
* of the modifications may be determined from the ChangeLog placed at
* the end of this file.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

/**
 * Author: asanchep<br>
 * http://www.nebrija.es/~asanchep/<br><br>
 * <p/>
 * Universidad Nebrija<br>
 * Computer Architecture and Technology Group<br>
 * Date: Feb 22, 2012<br><br>
 *
 */
public class CustomSWRLConfigParser {

	private DocumentBuilderFactory factory;
	
	private static final String BUILTIN = "builtin";
	private static final String NAME = "name";
	private static final String NAMESPACE = "ns";
	private static final String MIN_ARITY = "minArity";
	private static final String MAX_ARITY = "maxArity";
	
	
	private static final String REASONER_REGISTRATION_STRATEGY = "reasonerStrategy";
	private static final String REASONER_REGISTRATION_STRATEGY_CLASS = "class";
	
	private static final String REASONER_STRATEGY_PARAM = "param";
	private static final String REASONER_STRATEGY_PARAM_NAME = "name";
	private static final String REASONER_STRATEGY_PARAM_VALUE = "value";
	
	
    public CustomSWRLConfigParser() {
        factory = DocumentBuilderFactory.newInstance();
    }

	public void parse(String resource) throws ParserConfigurationException, SAXException, IOException {
		//<builtins>
		//<builtin name="myBuiltin" ns="http..." minArity="-1" maxArity="-1"/>
		//<builtin name="myBuiltin2" ns="http..." minArity="1" maxArity="2">
		// <reasonerStrategy ref="pellet">
		//   <param name="className" value="com.nebrija.custom.myBuiltin"/>
		//   <param name="builtInType" value="GeneralFunction"/>
		// </reasonerStrategy>
		//</builtin>
		//</builtins>
		
        InputStream file = ClassLoader.getSystemResourceAsStream(resource);
		//File
		DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        
        NodeList builtins = doc.getElementsByTagName(BUILTIN);
        if(builtins!=null){
	        for(int j=0;j<builtins.getLength();j++){
	        	addBuiltin(builtins.item(j));
	        }
        }
 
    }
	
	protected void addBuiltin(Node builtin){
        NamedNodeMap attributes = builtin.getAttributes();
        Node name = attributes.getNamedItem(NAME);
        Node ns = attributes.getNamedItem(NAMESPACE);
        Node min = attributes.getNamedItem(MIN_ARITY);
        Node max = attributes.getNamedItem(MAX_ARITY);
        if (name != null && ns!=null && min!=null && max !=null) {
            String valueName = name.getNodeValue();
            String valueNS = ns.getNodeValue();
            String valueMin = min.getNodeValue();
            String valueMax = max.getNodeValue();
            if (valueName != null && valueNS!=null && valueMin!=null && valueMax !=null) {
                int minArity=0;
                int maxArity=0;
            	try{
                	minArity=Integer.parseInt(valueMin);
                	maxArity=Integer.parseInt(valueMax);
                }catch(NumberFormatException nfe){
                	return;
                }
            	SWRLCustomBuiltInsVocabulary.addBuiltIn(valueName, valueNS, minArity, maxArity);
            }
        }
		
	}

	private Map<String, String> getParameters(NodeList params) {
		HashMap<String,String> parameters = new HashMap<String,String>();
		for (int i = 0; i < params.getLength(); i++) {
	        Node reg = params.item(i);
	        if (reg instanceof Element && reg.getNodeName().equals(REASONER_STRATEGY_PARAM)) {
	    		NamedNodeMap attributes = reg.getAttributes();
	    		Node name = attributes.getNamedItem(REASONER_STRATEGY_PARAM_NAME);
	    		Node value = attributes.getNamedItem(REASONER_STRATEGY_PARAM_VALUE);
	    		if (name==null || value==null){
	    			continue;
	    		}
	    		String parName = name.getNodeValue();
	    		String parValue = value.getNodeValue();
	    		
	    		parameters.put(parName, parValue);
	        }
        }
		return parameters;
	}
	
	/**
	 * Returns the list of data records to register the BuiltIn in the Reasoner.
	 * Register the builtIn if it was not previously done
	 * @param resource
	 * @param className
	 * @return
	 */
	public List<RegistrationData> parseRegistration(String resource, String className){
		List<RegistrationData> data = new ArrayList<RegistrationData>();
		try{
	        InputStream file = ClassLoader.getSystemResourceAsStream(resource);
			//File
			DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);
	        
	        NodeList builtins = doc.getElementsByTagName(BUILTIN);
	        if(builtins!=null){
		        for(int j=0;j<builtins.getLength();j++){
		        	RegistrationData info = processRegistrationData(builtins.item(j), className);
		        	if(info!=null){
		        		data.add(info);
		        	}
		        }
	        }		
		}catch(IOException ioe){
			ioe.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Returns the first registration data of a custom builtIn for the specific reasoner
	 * corresponding to className. 
	 * @param builtIn The XML node
	 * @param className The complete reasoner class name
	 * @return the information needed for registration
	 */
	private RegistrationData processRegistrationData(Node builtIn,String className) {
        NamedNodeMap attributes = builtIn.getAttributes();
		Node name = attributes.getNamedItem(NAME);
		if(name==null){
			return null;
		}
		String valueName = name.getNodeValue();
		SWRLCustomBuiltInsVocabulary custom = SWRLCustomBuiltInsVocabulary.getVocabulary().get(valueName);
		if(custom==null){
			addBuiltin(builtIn);
		}
		custom = SWRLCustomBuiltInsVocabulary.getVocabulary().get(valueName);
		if (custom==null){
			return null;
		}

		NodeList n = builtIn.getChildNodes();
		if (n==null){
			return null;
		}
		for(int i=0;i<n.getLength();i++){
        	try {
				Node strategy = n.item(i);
		        if (! (strategy instanceof Element) || !strategy.getNodeName().equals(REASONER_REGISTRATION_STRATEGY)) {
		        	continue;
		        }

				attributes = strategy.getAttributes();
			    Node classNameRetr = attributes.getNamedItem(REASONER_REGISTRATION_STRATEGY_CLASS);
			    if (classNameRetr!=null) {
			        String valueClass = classNameRetr.getNodeValue();
			        //Look for the appropriate Reasoner configuration (class)
			        if(!valueClass.equals(className)){
			        	continue;
			        }
			        RegistrationData rdata = new RegistrationData(); 
			        rdata.setBuiltIn(custom);
			        rdata.setParameters(getParameters(strategy.getChildNodes()));
			        return rdata;
	            }
			} catch (Exception e) {
				// TODO Enhance error control code.
				e.printStackTrace();
			}
		}	
		return null;
	}
	

	public static void main (String[] args) throws ParserConfigurationException, SAXException, IOException{
		new CustomSWRLConfigParser().parse("customBuiltin.xml");
		new com.clarkparsia.protege.plugin.pellet.PelletReasonerFactory();
	}
}
