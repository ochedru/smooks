package org.dhatim.templating.xslt;

import org.dhatim.delivery.dom.DOMElementVisitor;
import org.dhatim.container.ExecutionContext;
import org.dhatim.xml.XmlUtil;
import org.dhatim.xml.DomUtils;
import org.dhatim.SmooksException;
import org.w3c.dom.Element;

/**
 * @author
 */
public class TransformOrderHeader implements DOMElementVisitor {

    public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
    }

    public void visitAfter(Element header, ExecutionContext executionContext) throws SmooksException {
        Element order = header.getOwnerDocument().getDocumentElement();

        // Map all the header fields onto the <Order> header...
        order.setAttribute("orderId", XmlUtil.getString(header, "order-id/text()"));
        order.setAttribute("statusCode", XmlUtil.getString(header, "status-code/text()"));
        order.setAttribute("netAmount", XmlUtil.getString(header, "net-amount/text()"));
        order.setAttribute("totalAmount", XmlUtil.getString(header, "total-amount/text()"));
        order.setAttribute("tax", XmlUtil.getString(header, "tax/text()"));
        order.setAttribute("date", XmlUtil.getString(header, "date/month/text()") + "-" +
                                   XmlUtil.getString(header, "date/day/text()") + "-" +
                                   XmlUtil.getString(header, "date/year/text()"));

        // Remove the header from the message...
        DomUtils.removeElement(header, false);
    }
}
