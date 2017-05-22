package org.dhatim.templating.freemarker;

import org.dhatim.cdr.SmooksConfigurationException;
import org.dhatim.cdr.SmooksResourceConfiguration;
import org.dhatim.cdr.annotation.AppContext;
import org.dhatim.cdr.annotation.Configurator;
import org.dhatim.container.ApplicationContext;
import org.dhatim.delivery.ContentHandler;
import org.dhatim.delivery.ContentHandlerFactory;
import org.dhatim.delivery.annotation.Resource;
import org.dhatim.javabean.context.BeanContext;

/**
 * <a href="http://freemarker.org/">FreeMarker</a> templating {@link org.dhatim.delivery.Visitor} Creator class.
 * <p/>
 * This templating solution relies on the <a href="http://milyn.codehaus.org/downloads">Smooks JavaBean Cartridge</a>
 * to perform the JavaBean population that's required by <a href="http://freemarker.org/">FreeMarker</a> (for the data model).
 *
 * <h2>Targeting "ftl" Templates</h2>
 * The following is the basic configuration specification for FreeMarker resources:
 * <pre>
 * &lt;resource-config selector="<i>target-element</i>"&gt;
 *     &lt;resource&gt;<b>FreeMarker Resource - Inline or {@link org.dhatim.resource.URIResourceLocator URI}</b>&lt;/resource&gt;
 *
 *     &lt;!-- (Optional) The action to be applied on the template content. Should the content
 *          generated by the template:
 *          1. replace ("replace") the target element, or
 *          2. be added to ("addto") the target element, or
 *          3. be inserted before ("insertbefore") the target element, or
 *          4. be inserted after ("insertafter") the target element.
 *          5. be bound to ("bindto") a {@link BeanContext} variable named by the "bindId" param.
 *          Default "replace".--&gt;
 *     &lt;param name="<b>action</b>"&gt;<i>replace/addto/insertbefore/insertafter</i>&lt;/param&gt;
 *
 *     &lt;!-- (Optional) Should the template be applied before (true) or
 *             after (false) Smooks visits the child elements of the target element.
 *             Default "false".--&gt;
 *     &lt;param name="<b>applyTemplateBefore</b>"&gt;<i>true/false</i>&lt;/param&gt;
 *
 *     &lt;!-- (Optional) The name of the {@link org.dhatim.io.AbstractOutputStreamResource OutputStreamResource}
 *             to which the result should be written. If set, the "action" param is ignored. --&gt;
 *     &lt;param name="<b>outputStreamResource</b>"&gt;<i>xyzResource</i>&lt;/param&gt;
 *
 *     &lt;!-- (Optional) Template encoding.
 *          Default "UTF-8".--&gt;
 *     &lt;param name="<b>encoding</b>"&gt;<i>encoding</i>&lt;/param&gt;
 *
 *     &lt;!-- (Optional) bindId when "action" is "bindto".
 *     &lt;param name="<b>bindId</b>"&gt;<i>xxxx</i>&lt;/param&gt;
 *
 * &lt;/resource-config&gt;
 * </pre>
 * <p/>
 * <i><u>Example - URI based FreeMarker spec</u></i>:
 * <pre>
 * &lt;resource-config selector="<i>target-element</i>"&gt;
 *     &lt;!-- 1. See {@link org.dhatim.resource.URIResourceLocator} --&gt;
 *     &lt;resource&gt;/com/acme/order-transform.ftl&lt;/resource&gt;
 * &lt;/resource-config&gt;
 * </pre>
 * <p/>
 * <i><u>Example - Inlined FreeMarker spec</u></i>:
 * <pre>
 * &lt;resource-config selector="<i>target-element</i>"&gt;
 *     &lt;!-- 1. Note how we have to specify the resource type when it's inlined. --&gt;
 *     &lt;!-- 2. Note how the inlined FreeMarker template is wrapped as an XML Comment. CDATA Section wrapping also works. --&gt;
 *     &lt;resource <b color="red">type="ftl"</b>&gt;
 *         &lt;!--
 *            <i>Inline FreeMarker Template....</i>
 *         --&gt;
 *     &lt;/resource&gt;
 * &lt;/resource-config&gt;
 * </pre>
 *
 * @author tfennelly
 */
@Resource(type="ftl")
public class FreeMarkerContentHandlerFactory implements ContentHandlerFactory {

	@AppContext
	private ApplicationContext applicationContext;

    /**
	 * Create a FreeMarker based ContentHandler.
     * @param resourceConfig The SmooksResourceConfiguration for the FreeMarker.
     * @return The FreeMarker {@link org.dhatim.delivery.ContentHandler} instance.
	 */
	public synchronized ContentHandler create(SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException, InstantiationException {
        try {
            return Configurator.configure(new FreeMarkerTemplateProcessor(), resourceConfig, applicationContext);
        } catch (SmooksConfigurationException e) {
            throw e;
        } catch (Exception e) {
			InstantiationException instanceException = new InstantiationException("FreeMarker resource [" + resourceConfig.getResource() + "] not loadable.  FreeMarker resource invalid.");
			instanceException.initCause(e);
			throw instanceException;
		}
	}

}
