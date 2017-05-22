package org.dhatim.cdr;

import org.dhatim.cdr.annotation.ConfigParam;
import org.dhatim.delivery.sax.SAXVisitBefore;
import org.dhatim.delivery.sax.SAXElement;
import org.dhatim.container.ExecutionContext;
import org.dhatim.SmooksException;

import java.io.IOException;

/**
 * @author
 */
public class ConfigurableVisitor implements SAXVisitBefore {

    private String stringParam;
    private Integer intParam;
    private String optionalStringParam;
    private int otherIntProp;

    @ConfigParam
    public ConfigurableVisitor setStringParam(String stringParam) {
        this.stringParam = stringParam;
        return this;
    }

    @ConfigParam(defaultVal = "5")
    public ConfigurableVisitor setIntParam(Integer intParam) {
        this.intParam = intParam;
        return this;
    }

    @ConfigParam(use = ConfigParam.Use.OPTIONAL)
    public ConfigurableVisitor setOptionalStringParam(String stringParam) {
        this.optionalStringParam = stringParam;
        return this;
    }

    public String getStringParam() {
        return stringParam;
    }

    public int getIntParam() {
        return intParam;
    }

    public String getOptionalStringParam() {
        return optionalStringParam;
    }

    public void visitBefore(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
    }
}
