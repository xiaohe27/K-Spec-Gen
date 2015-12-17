###Method contracts
 `@(requires|ensures|returns)\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]*);`

###Loops
Loop property: In addition to loop invariant, which holds throughout the loop; `@entry` / `@exit` followed by 
some expression refer to the property hold at the beginning of the loop / after the last iteration of the loop respectively. 

 `//@(LI|entry|exit)\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]+);`
 
###Environment Cell entry:  `var` |-> `loc`

`([_\p{Alpha}][_\p{Alnum}]*)\p{Space}*\|->\p{Space}*([\p{Digit}]+)`

###Store Cell entry: `loc` |-> `val`

`([\p{Digit}]+)\p{Space}*\|->\p{Space}*([\p{Graph}&&[^,]])+`

##Implementation details
The above regular expressions are extracted from the project source code,
the full list of regular expression can be found at [Patterns.java](../KSpecGen/src/main/java/parser/annotation/Patterns.java).
