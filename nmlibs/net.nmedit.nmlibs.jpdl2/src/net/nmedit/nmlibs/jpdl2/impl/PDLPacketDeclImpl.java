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

import net.nmedit.nmlibs.jpdl2.dom.PDLPacketDecl;

public class PDLPacketDeclImpl extends PDLBlockImpl implements PDLPacketDecl
{
    
    private String packetName;
    private int padding;
    private boolean inlined;

    public PDLPacketDeclImpl(String packetName, int padding)
    {
        this.packetName = packetName;
        this.padding = padding;
    }

    public String getName()
    {
        return packetName;
    }

    public int getPadding()
    {
        return padding;
    }

    public String toString()
    {
        return packetName+"%"+padding;
    }
    
    public void setInlined(boolean inlined)
    {
        this.inlined = inlined;
    }

    public boolean isInlined()
    {
        return inlined;
    }

}