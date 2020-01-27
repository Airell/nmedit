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
package net.nmedit.nmlibs.jpdl2.impl;

import net.nmedit.nmlibs.jpdl2.dom.PDLItemType;
import net.nmedit.nmlibs.jpdl2.dom.PDLOptional;

public class PDLOptionalImpl extends PDLBlockImpl implements PDLOptional
{
    
    public PDLItemType getType()
    {
        return PDLItemType.Optional;
    }

    public String toString()
    {
        return "Optional";
    }
    
}
