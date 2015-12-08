<html>
<body>
<h2>Online K spec generator</h2>

<form action="HelloServlet" method="GET">
    <select onchange="document.getElementById('url').value = this.value;">
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
<textarea style="height: 15cm; width: 35cm" wrap="hard" name="content"
id="pgmText">
</textarea>
    <br/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>
