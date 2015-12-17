This project is implemented in MVC pattern.

The module `KSpecGen` is the model part while `View` is the view part.

There are 21 tests of different types coming along with the deliverable.

Using ```mvn verify``` at current directory will run all these tests.

The testing presented here is focusing on the correctness and functionality
of this software instead of answering research questions. The detailed evaluation
can be found in the submitted report.

###Unit tests

####Expression transformation.

<ProjHome>/KSpecGen/src/test/java/transform/utils/TypeMappingTest.java is a 
parametric unit test (including 10 cases) which tests the the method which converts
java expression to k expression (e.g. `2 + 3 < 7` will be converted `2 +Int 3 <Int 7`).

####AST transformation.
   
`<ProjHome>/KSpecGen/src/test/java/transform/utils/KAST_TransformerTest.java` is a 
parametric unit test (including 4 cases) which tests the method which converts
java statement to k computation abstract syntax tree (which encodes the operational
semantics of the original java statement).

###Integration tests

There are 3 parametric integration tests, which tests the overall behavior of the system. 

`<ProjHome>/KSpecGen/src/test/java/integration` is the direcotry which contains these tests.

`NoAnnotationTest.java` tests the senario where the input program does not have the method contracts
written in the required syntax.

`NormalTest.java` tests the situation where the program has the desired annotations. 
The four cases in this test include programs with different complexity: with/without loop,
modifies heap versus only uses stack (pure functions without side effects).

`MixedAnnotationTest.java` tests a special case where the method contract annotation for
verification purpose is mixed with the original javadoc. 
