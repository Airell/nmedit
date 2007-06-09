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
package net.sf.nmedit.jtheme.clavia.nordmodular.store;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTEnvelopeDisplay;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.ComponentElement;

import org.jdom.Element;

public class ADSREnvelopeStore extends AbstractEnvelopeStore
{

    public static ComponentElement createElement(StorageContext context, Element element)
    {
        ADSREnvelopeStore e = new ADSREnvelopeStore();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTEnvelopeDisplay createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        JTEnvelopeDisplay component = super.createComponent(context, descriptor, module);
        component.configureADSR();
        return component; 
    }

}
