/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jtheme.store;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.css.FakeRule;
import net.sf.nmedit.jtheme.image.ImageCache;
import net.sf.nmedit.jtheme.image.ImageResource;
import net.sf.nmedit.jtheme.store2.ComponentElement;
import net.sf.nmedit.jtheme.store2.ModuleElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

public abstract class StorageContext
{
    
    private transient Log log ;
    
    private Log getLog()
    {
        if (log == null)
            log = LogFactory.getLog(StorageContext.class);
        return log;
    }

    private Map<String, Class<? extends ComponentElement>> storeClassMap = new HashMap<String, Class<? extends ComponentElement>>();
    private Map<String, Method> storeMethodMap = new HashMap<String, Method>(); 

    public abstract CSSStyleSheet getStyleSheet();
    
    public abstract ImageCache getImageCache();
    
    protected Map<String, CSSStyleRule> styleRuleMap = new HashMap<String, CSSStyleRule>();
    
    private int idCounter = 1;
    public String generateId()
    {
        return "#"+Integer.toHexString(idCounter++);
    }


    public abstract ImageResource getCachedImage(Object source);
    
    public CSSStyleRule getStyleRule(String name)
    {
        CSSStyleRule rule = styleRuleMap.get(name);
        if (rule != null) return FakeRule.isFake(rule) ? null : rule;

        CSSStyleSheet css = getStyleSheet();
        CSSRuleList list = css.getCssRules();

        rule = null;
        
        for (int i=0;i<list.getLength();i++)
        {
            CSSRule arule = list.item(i);
            
            if (arule.getType() == CSSRule.STYLE_RULE)
            {
                CSSStyleRule srule = (CSSStyleRule) arule;
                
                if (name.equals(srule.getSelectorText()))
                {
                    // we found the rule
                    rule = srule;
                    break;
                }
            }
        }
        
        if (rule == null)
        {
            styleRuleMap.put(name, FakeRule.instance());
            return null;
        }
        
        styleRuleMap.put(name, rule);
        return rule;
        
    }
    
    public void installStore(String elementName, Class<? extends ComponentElement> storeClass)
    {
        storeClassMap.put(elementName, storeClass);
    }
    
    public Class<? extends ComponentElement> getStoreClass(String elementName)
    {
        return storeClassMap.get(elementName);
    }
    
    public abstract ModuleElement getModuleStoreById(Object id);
    
    public abstract ClassLoader getContextClassLoader();

    private Map<URL, Image> imageMap = new HashMap<URL, Image>();
    
    public Image getImage(String imageURL)
    {
        URL url = 
        getContextClassLoader()
        .getResource(imageURL);
        
        return (url != null) ? getImage(url) : null;
    }
    
    protected Image getImage(URL imageURL)
    {
        Image image = imageMap.get(imageURL);
        if (image == null)
        {
            try
            {
                image = ImageIO.read(imageURL);
                imageMap.put(imageURL, image);
            }
            catch (IOException e)
            {
                Log log = getLog();
                if (log.isWarnEnabled())
                {
                    log.warn("getImage("+imageURL+") failed",e);
                }
            }
        }
        return image;
    }
    
    public URL getContextResource(String name)
    {
        return getContextClassLoader().getResource(name);
    }
    
    public InputStream getContextResourceAsStream(String name)
    {
        return getContextClassLoader().getResourceAsStream(name);
    }
    
    protected Method getStoreCreateMethod(String elementName) throws JTException
    {
        Method create = storeMethodMap.get(elementName);
        if (create == null)
        {
            Class<? extends ComponentElement> storeClass = getStoreClass(elementName);
            if (storeClass == null)
                throw new JTException("No store for element: "+elementName);
            
            try
            {
                create = storeClass.getMethod("createElement", new Class<?>[]{StorageContext.class, Element.class});
            }
            catch (SecurityException e)
            {
                throw new JTException(e);
            }
            catch (NoSuchMethodException e)
            {
                throw new JTException("method createElement() not found in "+storeClass,e);
            }
            
            storeMethodMap.put(elementName, create);
        }
        return create;
    }
    
    public ComponentElement createStore(Element element)
      throws JTException
    {
        Method create = getStoreCreateMethod(element.getName());
        try
        {
            ComponentElement store = (ComponentElement) create.invoke(null, new Object[] {this, element});
            
            return store;
        }
        catch (IllegalArgumentException e)
        {
            throw new JTException("element "+element.getName(), e);
        }
        catch (IllegalAccessException e)
        {
            throw new JTException("element "+element.getName(), e);
        }
        catch (InvocationTargetException e)
        {
            throw new JTException("element "+element.getName(), e);
        }
    }
    
}

