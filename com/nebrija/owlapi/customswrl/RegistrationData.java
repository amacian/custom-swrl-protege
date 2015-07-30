package com.nebrija.owlapi.customswrl;

import java.util.HashMap;
import java.util.Map;

public class RegistrationData {
	
	private Map<String,String> parameters = new HashMap<String,String>();
	
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	private SWRLCustomBuiltInsVocabulary builtIn;
	
	public SWRLCustomBuiltInsVocabulary getBuiltIn() {
		return builtIn;
	}

	public void setBuiltIn(SWRLCustomBuiltInsVocabulary builtIn) {
		this.builtIn = builtIn;
	}

	public void addParameter(String key, String value){
		parameters.put(key, value);
	}
	
	public String getParameter(String key){
		return parameters.get(key);
	}
	
	
}
