<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link href="google-code-prettify/prettify.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="google-code-prettify/prettify.js"></script>
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
                document.getElementById("javacode").style.fontSize = "17px";
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
    <div align="center">
        <em><h5 style="font-size: x-large; color: #000088">Program List</h5></em>
        <select style="text-align: center; font-size: large" onchange="loadXMLDoc(this.value)">
            <option value="" selected="selected">Choose one from the list</option>
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
    </div>
    <br/>

    <div><pre class="prettyprint" id="javablock">
    <code style="align-content: space-between;" class="language-java" id="javacode">
<textarea style="width: 100%; height: 60%; font-size: 20px; text-align: center" id="pgmText"
          ondblclick="this.value =''">
    Type your program here or choose an existing example from the top-center popup menu.
</textarea>
    </code>

</pre></div>

    <div style="text-align: center; vertical-align: bottom">
        <button type="submit" class="btn btn-primary">Get K-SPEC</button>
    </div>

    <textarea id="code" name="content" value="" hidden></textarea>

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
