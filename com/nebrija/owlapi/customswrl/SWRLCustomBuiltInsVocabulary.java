package com.nebrija.owlapi.customswrl;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLPredicate;
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
public class SWRLCustomBuiltInsVocabulary implements SWRLPredicate {

	public static final String CONFIG_FILE = "customBuiltin.xml";
	
	// Code taken from org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary
	private String shortName;

    private IRI iri;

    // Arity of the predicate (-1 if infinite)
    private int minArity;

    private int maxArity;

    private static Map<String, SWRLCustomBuiltInsVocabulary> vocabulary = new TreeMap<String, SWRLCustomBuiltInsVocabulary>();


    //Static initialization to read and create
    //Unfortunately, Protege does not use Spring, so let's read from a config file
    static{
    	CustomSWRLConfigParser parser = new CustomSWRLConfigParser();
    	try{
    		parser.parse(CONFIG_FILE);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    SWRLCustomBuiltInsVocabulary(String name, String namespace, int arity) {
    	this(name, namespace, arity, arity);
    }
    
    SWRLCustomBuiltInsVocabulary(String name, String namespace, int minArity, int maxArity) {
        this.shortName = name;
        this.iri = IRI.create(namespace + name);
        this.minArity = minArity;
        this.maxArity = maxArity;
    }

    public String getShortName() {
        return shortName;
    }

    public IRI getIRI() {
        return iri;
    }

    public URI getURI() {
        return iri.toURI();
    }

    /**
     * Gets the minimum arity of this built in
     * @return The minimum arity of this built in
     */
    public int getMinArity() {
        return minArity;
    }

    /**
     * Gets the maximum arity of this built in.
     * @return The maximum arity of the built in or -1 if the arity is infinite
     */
    public int getMaxArity() {
        return maxArity;
    }

    /**
     * Returns the minimum arity of this built in.
     * @return The minimum arity of this built in.
     * @deprecated Use getMinArity and getMaxArity instead
     */
    @Deprecated
    public int getArity() {
        return minArity;
    }


    /**
     * Gets a builtin vocabulary value for a given IRI
     * @param iri The IRI
     * @return The builtin vocabulary having the specified IRI, or <code>null</code> if there is no builtin
     * vocabulary with the specified IRI
     */
    public static SWRLCustomBuiltInsVocabulary getBuiltIn(IRI iri) {
        for(SWRLCustomBuiltInsVocabulary v : getVocabulary().values()) {
            if(v.iri.equals(iri)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Gets a builtin vocabulary value for a given URI
     * @param uri The URI
     * @return The builtin vocabulary having the specified URI, or <code>null</code> if there is no builtin
     * vocabulary with the specified URI
     */
    public static SWRLCustomBuiltInsVocabulary getBuiltIn(URI uri) {
        for(SWRLCustomBuiltInsVocabulary v : getVocabulary().values()) {
            if(v.getURI().equals(uri)) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Adds a builtin to the vocabulary
     * @param String the builtin
     * @param Namespace the builtin namespace
     * @param minArity The minimum arity of this built in
     * @param maxArity The maximum arity of the built in or -1 if the arity is infinite
     */
    public static void addBuiltIn(String description, String namespace, int minArity, int maxArity){
    	SWRLCustomBuiltInsVocabulary custom = new SWRLCustomBuiltInsVocabulary(description, namespace, minArity,maxArity);
    	getVocabulary().put(description, custom);
    }

	public static Map<String,SWRLCustomBuiltInsVocabulary> getVocabulary() {
		return vocabulary;
	}
}
