##Overall annotation description
The annotations used in the project borrowed a subset of syntax from [JML](https://en.wikipedia.org/wiki/Java_Modeling_Language),
and also incorporated some newly-defined syntax, so that the users are able to specify normal method contracts (pre/post conditions,
return values) as well as loop invariants. All the annotations are inside some java comment so that the annotated programs are
compilable by any valid java compiler. Specifically, the method contracts should be placed in method's javadoc whereas the loop 
invariant should be put in either line comment or block comment.

##High-Level Syntax

+ Method precondition
 
 ```@requires <Method-Precondition-Here> ;```

+ Method postcondition

 ```@ensures <Method-Postcondition-Here> ;```

+ Method return value

 ```@returns <Method-Return-Expression-Here> ;```
