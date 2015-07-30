package com.nebrija.owlapi.customswrl.pellet.example;

import org.mindswap.pellet.Literal;

import com.clarkparsia.pellet.rules.builtins.Tester;

public class SampleFunction implements Tester{

	@Override
	public boolean test(Literal[] arg0) {
		return true;
	}

}
