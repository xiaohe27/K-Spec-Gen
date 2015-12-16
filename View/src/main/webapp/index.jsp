<html>
<head>
    <link href="google-code-prettify/prettify.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="google-code-prettify/prettify.js"></script>
    <meta charset="UTF-8">
</head>
<body>
<script>
    function loadXMLDoc(myURL) {
        if (myURL == "")
            return;
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for older browsers
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                document.getElementById("code").value = xmlhttp.responseText;
                document.getElementById("javacode").innerHTML =
                        xmlhttp.responseText;
                document.getElementById("javablock").className = "prettyprint";
                prettyPrint();
            }
        };
        xmlhttp.open("GET", myURL, true);
        xmlhttp.send();
    }
</script>


<h2>Online K spec generator</h2>

<form action="HelloServlet" method="POST" onsubmit="setContent()" target="_blank">
    <select onchange="loadXMLDoc(this.value)">
        <option value="" selected="selected"></option>
        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/bst/bst.java">
            bst.java
        </option>
        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/list/listNode.java">
            listNode.java
        </option>

        <option value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/min/min.java">
            min.java
        </option>

        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/sum/sum.java">
            sum.java
        </option>
    </select>

    <br/>

    <div><span><pre class="prettyprint" id="javablock">
<font size="5">
    <code style="align-content: space-between" class="language-java" id="javacode">
<textarea style="height: 15cm; width: 35cm" wrap="hard" id="pgmText" ondblclick="this.value = ''">
    Type your program here or choose an existing example from the left hand side popup menu.
</textarea>
    </code>
</font>

</pre></span></div>

    <textarea id="code" name="content" value="" hidden></textarea>

    <br/>
    <input type="submit" value="!Submit!"/>
</form>

<script>
    function setContent() {
        if (document.getElementById("pgmText") != null) {
            document.getElementById("code").value =
                    document.getElementById("pgmText").value;
        }
    }
</script>
</body>
</html>
