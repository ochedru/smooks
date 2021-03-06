/*
 * Milyn - Copyright (C) 2006 - 2011
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (version 2.1) as published by the Free Software
 * Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
 */

package org.dhatim.javabean.binding.model;

import org.dhatim.cdr.ParameterAccessor;
import org.dhatim.cdr.SmooksConfigurationException;
import org.dhatim.cdr.SmooksResourceConfiguration;
import org.dhatim.cdr.SmooksResourceConfigurationList;
import org.dhatim.cdr.xpath.SelectorStep;
import org.dhatim.cdr.xpath.SelectorStepBuilder;
import org.dhatim.container.ApplicationContext;
import org.dhatim.db.TransactionManagerType;
import org.dhatim.javabean.BeanInstanceCreator;
import org.dhatim.javabean.BeanInstancePopulator;
import org.dhatim.javabean.DataDecoder;
import org.dhatim.util.DollarBraceDecoder;
import org.dhatim.xml.NamespaceMappings;

import javax.xml.namespace.QName;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Bean binding model set.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ModelSet {

    /**
     * ModelSet base beans.
     * <p/>
     * A Smooks configuration can have multiple <jb:baseBeans> that can be wired together
     * in all sorts of ways to create models.  This is a Map of these baseBeans.  These
     * baseBeans are used (cloned) to create all possible models (with baseBeans all wired together).
     */
    private Map<String, Bean> baseBeans = new LinkedHashMap<String, Bean>();
    /**
     * Models.
     * <p/>
     * Should contain clones of the same baseBeans as in the baseBeans property (above), but
     * with their full graphs expanded i.e. all the bean wirings resolved and wired into
     * parent baseBeans etc.
     */
    private Map<String, Bean> models = new LinkedHashMap<String, Bean>();
    /**
     * Is the associated Smooks instance a binding only configuration.
     */
    private Boolean isBindingOnlyConfig;

    private ModelSet(SmooksResourceConfigurationList userConfigList) throws SmooksConfigurationException {
        createBaseBeanMap(userConfigList);
        createExpandedModels();
        resolveModelSelectors(userConfigList);
    }

    public Bean getModel(String beanId) {
        return models.get(beanId);
    }

    public Bean getModel(Class<?> beanType) {
        for(Bean model : models.values()) {
            if(model.getCreator().getBeanRuntimeInfo().getPopulateType() == beanType) {
                return model;
            }
        }
        return null;
    }

    public Map<String, Bean> getModels() {
        return models;
    }

    public boolean isBindingOnlyConfig() {
        return isBindingOnlyConfig;
    }

    private void createBaseBeanMap(SmooksResourceConfigurationList userConfigList) {
        for(int i = 0; i < userConfigList.size(); i++) {
            SmooksResourceConfiguration config = userConfigList.get(i);
            Object javaResource = config.getJavaResourceObject();

            if(javaResource instanceof BeanInstanceCreator) {
                BeanInstanceCreator beanCreator = (BeanInstanceCreator) javaResource;
                Bean bean = new Bean(beanCreator).setCloneable(true);

                baseBeans.put(bean.getBeanId(), bean);

                if(isBindingOnlyConfig == null) {
                    isBindingOnlyConfig = true;
                }
            } else if(javaResource instanceof BeanInstancePopulator) {
                BeanInstancePopulator beanPopulator = (BeanInstancePopulator) javaResource;
                Bean bean = baseBeans.get(beanPopulator.getBeanId());

                if(bean == null) {
                    throw new SmooksConfigurationException("Unexpected binding configuration exception.  Unknown parent beanId '' for binding configuration.");
                }

                if(beanPopulator.isBeanWiring()) {
                    bean.getBindings().add(new WiredBinding(beanPopulator));
                } else {
                    bean.getBindings().add(new DataBinding(beanPopulator));
                }
            } else if(isNonBindingResource(javaResource) && !isGlobalParamsConfig(config)) {
                // The user has configured something other than a bean binding config.
                isBindingOnlyConfig = false;
            }
        }
    }

    private boolean isNonBindingResource(Object javaResource) {
        if(javaResource instanceof DataDecoder) {
            return false;
        }

        // Ignore resource that do not manipulate the event stream...
        if(javaResource instanceof NamespaceMappings) {
            return false;
        }

        return true;
    }

    private boolean isGlobalParamsConfig(SmooksResourceConfiguration config) {
        return ParameterAccessor.GLOBAL_PARAMETERS.equals(config.getSelector());
    }

    private void createExpandedModels() {
        for(Bean bean : baseBeans.values()) {
            models.put(bean.getBeanId(), bean.clone(baseBeans, null));
        }
    }

    private void resolveModelSelectors(SmooksResourceConfigurationList userConfigList) {
        // Do the beans first...
        for(Bean model : models.values()) {
            resolveModelSelectors(model);
        }

        // Now run over all configs.. there may be router configs etc using hashed selectors...
        for(int i = 0; i < userConfigList.size(); i++) {
            expandSelector(userConfigList.get(i), false, null);
        }
    }

    private void resolveModelSelectors(Bean model) {
        SmooksResourceConfiguration beanConfig = model.getConfig();

        expandSelector(beanConfig, true, null);

        for(Binding binding : model.getBindings()) {
            SmooksResourceConfiguration bindingConfig = binding.getConfig();
            expandSelector(bindingConfig, true, beanConfig);

            if(binding instanceof WiredBinding) {
                resolveModelSelectors(((WiredBinding) binding).getWiredBean());
            }
        }
    }

    private void expandSelector(SmooksResourceConfiguration resourceConfiguration, boolean failOnMissingBean, SmooksResourceConfiguration context) {
        SelectorStep[] selectorSteps = resourceConfiguration.getSelectorSteps();
        QName targetElement = selectorSteps[0].getTargetElement();

        if(targetElement == null) {
            return;
        }

        String localPart = targetElement.getLocalPart();
        if(localPart.equals("#") && context != null) {
            resourceConfiguration.setSelectorSteps(concat(context.getSelectorSteps(), selectorSteps));
            return;
        }

        List<String> dollarBraceTokens = DollarBraceDecoder.getTokens(localPart);
        if(dollarBraceTokens.size() == 1) {
            String beanId = dollarBraceTokens.get(0);
            Bean bean = baseBeans.get(beanId);

            if(bean != null) {
                resourceConfiguration.setSelectorSteps(concat(bean.getConfig().getSelectorSteps(), selectorSteps));
            } else if(failOnMissingBean) {
                throw new SmooksConfigurationException("Invalid selector '" + SelectorStepBuilder.toString(selectorSteps) + "'.  Unknown beanId '" + beanId + "'.");
            }

        }
    }

    private SelectorStep[] concat(SelectorStep[] context, SelectorStep[] beanSelectorSteps) {
        SelectorStep[] newSteps = new SelectorStep[context.length + beanSelectorSteps.length - 1];

        System.arraycopy(context, 0, newSteps, 0, context.length);
        System.arraycopy(beanSelectorSteps, 1, newSteps, context.length, beanSelectorSteps.length - 1);

        return newSteps;
    }

    public static void build(ApplicationContext appContext) {
        ModelSet modelSet = get(appContext);
        if(modelSet == null) {
            modelSet = new ModelSet(appContext.getStore().getUserDefinedResourceList());
            appContext.setAttribute(ModelSet.class, modelSet);
        }
    }

    public static ModelSet get(ApplicationContext appContext) {
        return (ModelSet) appContext.getAttribute(ModelSet.class);
    }
}
