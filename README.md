This is a project for automatically generating k specification from annotated java program, 
where the k specification is an input for [k-program-verifier](https://github.com/paper-submission/pldi16-paper177) which can do program verification. Refer to [docs/Annotation-Syntax.md](docs/Annotation-Syntax.md) for the syntax of the supported annotations.

This software aims to be a service for programmers who want to do program verification
using [k framewrok](http://www.kframework.org/index.php/Main_Page), and therefore we've tried our best to present the service in a
user-friendly way. We have provided an online interface (with syntax highlighting)
for using the service in addition to the traditional command-line interface.

Web-interface:

1. Go to url [http://k-spec-generator.herokuapp.com/](http://k-spec-generator.herokuapp.com/)

2. Either select a pre-defined example from the top-center pop-up menu,
   or directly paste the annotated java program into the central textbox. 

3. Click the "Get K-SPEC" button and the generated k specification will be
   displayed in a new tab.

Command-line interface:

1. open the terminal in some directory.

2. clone the repository from github and go to the root directory of the project.

 ```bash
 git clone https://github.com/xiaohe27/K-Spec-Gen.git
 cd K-Spec-Gen
 ```

3. build the project via maven.

 ```mvn package```

4. The generated executable is in the path: 

 `<The-Path-To-Proj-Home>/KSpecGen/target/release/kspec-gen/kspec-gen/bin/`

5. If you add the path of the program "kspec-gen" to environment variable,
   then, it is easy to run it to generate k specification from java file:

 ```kspec-gen <path-to-the-annotated-java-file-or-directory>```

There are some simple descriptions about the current test cases in [docs/Evaluation.md](docs/Evaluation.md).

The project can be located in the [github](https://github.com/xiaohe27/K-Spec-Gen) page.


