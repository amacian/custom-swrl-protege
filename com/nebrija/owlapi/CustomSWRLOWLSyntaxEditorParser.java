package com.nebrija.owlapi.customswrl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;

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
public class CustomSWRLOWLSyntaxEditorParser extends ManchesterOWLSyntaxEditorParser{


	public CustomSWRLOWLSyntaxEditorParser(OWLDataFactory owlDataFactory, String s) {
		super(owlDataFactory, s);	
	}

	public CustomSWRLOWLSyntaxEditorParser(
			OWLOntologyLoaderConfiguration configuration,
			OWLDataFactory dataFactory, String s) {
		super(configuration, dataFactory, s);
	}

	@Override
	public boolean isSWRLBuiltin(String name) {
		boolean core = super.isSWRLBuiltin(name);
		if(core){return core;}
		if(SWRLCustomBuiltInsVocabulary.getVocabulary().containsKey(name)){
			return true;
		}
		return false;
	}

	@Override
    protected ParserException createException(boolean classNameExpected, boolean objectPropertyNameExpected, boolean dataPropertyNameExpected, boolean individualNameExpected, boolean datatypeNameExpected, boolean annotationPropertyNameExpected, String... keywords) throws ParserException {
        Set<String> correctedKeywords = new HashSet<String>();
        correctedKeywords.addAll(Arrays.asList(keywords));
        correctedKeywords.addAll(SWRLCustomBuiltInsVocabulary.getVocabulary().keySet());
		return super.createException(classNameExpected,objectPropertyNameExpected,dataPropertyNameExpected,individualNameExpected,datatypeNameExpected,annotationPropertyNameExpected,correctedKeywords.toArray(new String[correctedKeywords.size()]));
	}

	@Override
	public IRI getIRI(String name) {
		if(SWRLCustomBuiltInsVocabulary.getVocabulary().containsKey(name)){
			SWRLCustomBuiltInsVocabulary v = SWRLCustomBuiltInsVocabulary.getVocabulary().get(name);
            return v.getIRI();
		}
		return super.getIRI(name);
	}

}
