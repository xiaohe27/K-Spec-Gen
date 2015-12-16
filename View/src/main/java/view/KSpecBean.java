package view;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml11;

/**
 * Created by hx312 on 12/7/2015.
 */
public class KSpecBean {
    private String additionalInfo;
    private String inputJavaContent;
    private String outputKSpecContent;

    public String getInputJavaContent() {
        return inputJavaContent;
    }

    public void setInputJavaContent(String inputJavaContent) {
        this.inputJavaContent = inputJavaContent;
    }

    public String getOutputKSpecContent() {
        return outputKSpecContent;
    }

    public String getHTMLOutputOfKSpec() {
        return escapeXml11(this.outputKSpecContent);
    }

    public void setOutputKSpecContent(String outputKSpecContent) {
        this.outputKSpecContent = outputKSpecContent;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
