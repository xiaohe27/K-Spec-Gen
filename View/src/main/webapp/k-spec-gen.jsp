<!DOCTYPE html>
<%@page import="view.KSpecBean" %>
<html lang="en">
<head>
    <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
    <meta charset="UTF-8">
    <title>K Spec for the input Java file</title>
</head>
<body>
<%KSpecBean kspec = (KSpecBean) request.getAttribute("bean");%>

<h6><%out.println(kspec.getAdditionalInfo());%></h6>

<h2>The content of the input java file is</h2>

<div><span><pre>

<font size="5">
    <code style="align-content: space-between" class="prettyprint"><%
        out.println(kspec.getInputJavaContent());
    %></code>
</font>

<br/>
  <h2>The k spec generated for the java file is</h2>
<font size="5">
    <code style="background-clip: border-box" class="prettyprint">
    <%
        out.println(kspec.getOutputKSpecContent());
    %>
    </code>
</font>

</pre></span></div>
</body>
</html>