package com.nebrija.owlapi.customswrl.pellet;

import java.util.List;

import com.clarkparsia.pellet.rules.builtins.BuiltIn;
import com.clarkparsia.pellet.rules.builtins.BuiltInRegistry;
import com.clarkparsia.pellet.rules.builtins.Function;
import com.clarkparsia.pellet.rules.builtins.FunctionBuiltIn;
import com.clarkparsia.pellet.rules.builtins.GeneralFunction;
import com.clarkparsia.pellet.rules.builtins.GeneralFunctionBuiltIn;
import com.clarkparsia.pellet.rules.builtins.NumericAdapter;
import com.clarkparsia.pellet.rules.builtins.NumericFunction;
import com.clarkparsia.pellet.rules.builtins.Tester;
import com.clarkparsia.pellet.rules.builtins.TesterBuiltIn;
import com.nebrija.owlapi.customswrl.RegistrationData;

public class PelletCustomBuiltinRegistrator {

	public static final String PARAM_CLASS="className";
	public static final String PARAM_TYPE="builtInType";	
	
	public void register(List<RegistrationData> customBuiltins){
		if (customBuiltins==null){
			return;
		}
		for (RegistrationData data: customBuiltins){
			try{
				String builtInClass = data.getParameter(PARAM_CLASS);
				String builtInClassType = data.getParameter(PARAM_TYPE);
				if(builtInClass==null || builtInClassType==null){
					return;
				}
				Object builtInObject = Class.forName(builtInClass).newInstance();
				BuiltIn b = null;
				if(builtInClassType.equals("GeneralFunction")){
					b = new GeneralFunctionBuiltIn((GeneralFunction)builtInObject);
				}else if(builtInClassType.equals("Function")){
					b = new FunctionBuiltIn((Function)builtInObject);
				}else if(builtInClassType.equals("NumericFunction")){
					b = new FunctionBuiltIn(new NumericAdapter( (NumericFunction)builtInObject ));
				}else if(builtInClassType.equals("Tester")){
					b = new TesterBuiltIn((Tester)builtInObject);
				}else{
					return;
				}
				BuiltInRegistry.instance.registerBuiltIn(data.getBuiltIn().getIRI().toString(),b);
			}catch(Exception e){
				e.printStackTrace();
				return;
			}			
		}
	}
}
