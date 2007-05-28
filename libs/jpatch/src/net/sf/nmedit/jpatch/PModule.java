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
package net.sf.nmedit.jpatch;

import java.awt.Dimension;
import java.awt.Point;

import net.sf.nmedit.jpatch.event.ModuleListener;


public interface PModule extends PComponent
{
    
    PModuleMetrics getModuleMetrics();
    
    PModuleDescriptor getDescriptor();
    
    PModuleContainer getParentComponent();
    
    int getComponentCount();
    
    PComponent getComponent(int index);
    
    int getParameterCount();
    
    PParameter getParameter(int index);
    
    PParameter getParameter(PParameterDescriptor descriptor);
    
    int getConnectorCount();
    
    PConnector getConnector(int index);

    PConnector getConnector(PConnectorDescriptor descriptor);
 
    String getTitle();
    
    void setTitle(String title);
    
    /**
     * Returns the index of this module in the parent module container.
     * If the module container is null, -1 will be returned, otherwise
     * <code>getParentComponent().indexOf(this)</code> will be returned.
     * 
     * @return the index of this module in the parent module container.
     */
    int getComponentIndex();

    void removeAllConnections();

    void addModuleListener(ModuleListener l);
    void removeModuleListener(ModuleListener l);

    Point getScreenLocation();
    Point getInternalLocation();

    int getInternalWidth();
    int getInternalHeight();
    int getScreenWidth();
    int getScreenHeight();
    void setInternalSize(int width, int height);
    void setScreenSize(int width, int height);
    void setInternalSize(Dimension size);
    void setScreenSize(Dimension size);
    
    void setScreenLocation(int x, int y);
    void setScreenLocation(Point location);

    int getScreenX();
    int getScreenY();
    
    void setInternalLocation(int x, int y);
    void setInternalLocation(Point location);

    int getInternalX();
    int getInternalY();

    PPatch getPatch();
    
}
