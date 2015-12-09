<!DOCTYPE html>
<%@page import="view.KSpecBean" %>
<html lang="en">
<head>
    <link href="google-code-prettify/prettify.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="google-code-prettify/prettify.js"></script>
    <meta charset="UTF-8">
    <title>K Spec for the input Java file</title>
</head>
<body>
<%KSpecBean kspec = (KSpecBean) request.getAttribute("bean");%>

<h6><%out.println(kspec.getAdditionalInfo());%></h6>

<h2>The content of the input java file is</h2>

<div><span><pre class="prettyprint">

<font size="5">
    <code style="align-content: space-between" class="language-java"><%
        out.println(kspec.getInputJavaContent());
    %></code>
</font>

</pre></span></div>

<br/>
  <h2>The k spec generated for the java file is</h2>

<div><span><pre class="prettyprint">
<font size="5">
    <code style="background-clip: border-box" class="language-xml">
    <%
        out.println(kspec.getHTMLOutputOfKSpec());
    %>
    </code>
</font>

</pre></span></div>
</body>
</html>