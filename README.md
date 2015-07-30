Custom SWRL builins for Protege 4.1 and Pellet

Some modifications to the Protege OWL Editor (one line to SWRLRuleChecker.java, one additional xml file and some new classes) and you will be working with Custom SWRL Builtins in the rule view. 

I also modified the Pellet Protege plugin (changing one class and adding some others) to read the same xml file and register those builtins automatically into this reasoner. I tested it with a dummy custom swrl builtin and it worked.

