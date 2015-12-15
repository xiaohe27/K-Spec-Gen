<!DOCTYPE html>
<%@page import="view.KSpecBean" %>
<html lang="en">
<head>
    <link href="google-code-prettify/k.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="google-code-prettify/prettify.js"></script>
    <script type="text/javascript" src="google-code-prettify/lang-k.js"></script>
    <meta charset="UTF-8">
    <title>K Spec for the input Java file</title>
</head>
<body onload="prettyPrint()">
<%KSpecBean kspec = (KSpecBean) request.getAttribute("bean");%>

<h2>The k spec generated for the java file is</h2>

<div><span><pre class="prettyprint lang-k">
<font size="5">
    <code style="background-clip: border-box">
    <%
        out.println(kspec.getHTMLOutputOfKSpec());
    %>
    </code>
</font>

</pre></span></div>
</body>
</html>