/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package net.sf.nmedit.jpdl2.dom;

import java.util.Collection;

import net.sf.nmedit.jpdl2.PDLParseContext;

public interface PDLCondition
{

    /**
     * Evaluates the condition.
     */
    boolean isConditionTrue(PDLParseContext context);

    String getSource();

    /**
     * Returns the names of variables and labels on which this condition
     * depends. Label names have an @ (at)-sign as prefix.
     * 
     * @return the names of variables and labels on which this condition depends 
     */
    Collection<String> getDependencies();
    
}
