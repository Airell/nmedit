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
package net.sf.nmedit.jpdl2.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.nmedit.jpdl2.PDLCondition;
import net.sf.nmedit.jpdl2.PDLParseContext;
import net.sf.nmedit.jpdl2.format.Expression;

public class PDLCompiledCondition implements PDLCondition
{
    
    private Expression e;

    public PDLCompiledCondition(Expression e)
    {
        if (e.getResultType() != Expression.Type.Boolean)
            throw new IllegalArgumentException("invalid result type: "+e.getResultType()+", "+
                    e.getSource());
        
        this.e = e;
    }

    public String getSource()
    {
        return e.getSource();
    }

    public boolean isConditionTrue(PDLParseContext context)
    {
        return e.computeBoolean(context);
    }
    
    public String toString()
    {
        return "if("+getSource()+")";
    }

    public Collection<String> getDependencies()
    {
        Set<String> set = new HashSet<String>();
        e.collectDepencies(set);
        return Collections.unmodifiableSet(set);
    }
    
}
