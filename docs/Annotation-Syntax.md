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

+  Heap: heap is represented as <objectStore> cell in K.
 
 ```@objectStore {<The objects list>}```

+ Stack: stack is represented by <env> and <store> cell in K, which maps variables to locations and locations to values.

 ```@env {<The stack structure>}```

 ```@store {<The store structure>}```
 
 The internal elements of env, store and objectStore are separated by commas, and the rewrite is presented by 
 the usual `=>` symbol.
 
 You may find the [sample annotated java programs](../KSpecGen/examples) helpful for understanding the syntax.

##Detailed regular expression
The detailed regular expression used in the system can be found at [regex.md](regex.md).
