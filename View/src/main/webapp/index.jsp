<html>
<head>
    <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
    <meta charset="UTF-8">
</head>
<body>
<script>
    document.getElementById("pgmText").addEventListener('onchange', reset
            , false);

    function reset() {
        document.getElementById("pgmText").value = "good";
    }

    function loadXMLDoc(myURL) {
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for older browsers
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                document.getElementById("javacode").innerHTML =
                        xmlhttp.responseText;
            }
        };
        xmlhttp.open("GET", myURL, true);
        xmlhttp.send();
        prettyPrint();
    }
</script>


<h2>Online K spec generator</h2>

<form action="HelloServlet" method="GET">
    <select onchange="document.getElementById('url').value = this.value;
    loadXMLDoc(this.value)">
        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/bst/bst.java">
            bst.java
        </option>
        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/list/listNode.java">
            listNode.java
        </option>

        <option value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/min/min.java">min.java</option>

        <option
                value="https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/sum/sum.java">
            sum.java
        </option>
    </select>

    <br/>
    <input type="text" width="20cm" name="path" id="url" hidden/>

    <div><span><pre class="prettyprint">
<font size="5">
    <code style="align-content: space-between" class="language-java" id="javacode">
<textarea style="height: 15cm; width: 35cm" wrap="hard" name="content"
id="pgmText">
</textarea>
    </code>
</font>

</pre></span></div>

    <br/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>
