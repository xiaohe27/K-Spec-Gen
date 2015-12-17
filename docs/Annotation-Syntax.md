##Overall annotation description
The annotations used in the project borrowed a subset of syntax from [JML](https://en.wikipedia.org/wiki/Java_Modeling_Language),
and also incorporated some newly-defined syntax, so that the users are able to specify normal method contracts (pre/post conditions,
return values) as well as loop invariants. All the annotations are inside some java comment so that the annotated programs are
compilable by any valid java compiler. Specifically, the method contracts should be placed in method's javadoc whereas the loop 
invariant should be put in either line comment or block comment.

##High-Level Syntax

+ Basic property: all the java boolean expressions (e.g. `3 * 7 > 5 && x != 2`).  

+ Method precondition
 
 ```@requires <Method-Precondition-Here> ;```

+ Method postcondition

 ```@ensures <Method-Postcondition-Here> ;```

+ Method return value

 ```@returns <Method-Return-Expression-Here> ;```

+ Loop invariant

 ```@LI <Loop Invariant Expression> ;```

+  K's objectStore cell: store all the objects in the memory.
 
 ```@objectStore <The objects list>```

+ Environment: the stack is represented by the K's env cell, which maps variables to locations.

 ```@env{<The stack structure>}```
 
+ Heap structure: The heap is represented by the K's store cell which maps locations to values.

 ```@store{<The store structure>}```
 
 The internal elements of env, store and objectStore are separated by commas, and the rewrite is presented by 
 the usual `=>` symbol.
 
 You may find the [sample annotated java programs](../KSpecGen/examples) helpful for understanding the syntax.

