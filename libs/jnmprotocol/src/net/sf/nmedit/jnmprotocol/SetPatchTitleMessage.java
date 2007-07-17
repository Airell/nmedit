/*
    Nord Modular Midi Protocol 3.03 Library
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

package net.sf.nmedit.jnmprotocol;

import java.util.*;

import net.sf.nmedit.jnmprotocol.utils.NmCharacter;
import net.sf.nmedit.jpdl.*;

public class SetPatchTitleMessage extends MidiMessage
{
    
    private String title = null;
    
    public SetPatchTitleMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("name", "data:data:name");
	set("cc", 0x14);
    set("sc", 0x27);
    }

    SetPatchTitleMessage(Packet packet)
    {
	this();
	setAll(packet);
        title = NmCharacter.extractName(packet.getPacket("data:data:name"));
    }
    
    public SetPatchTitleMessage(int slot, int pid, String title)
    {
        this();
        setTitle(slot, pid, title);
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(int slot, int pid, String title)
    {
        set("cc", 0x17);
        set("sc", 0x27);
        setSlot(slot);
        set("pid", pid);
        this.title = title == null ? "" : title;
    }

    public List<BitStream> getBitStream()
    throws MidiException
    {
        IntStream intStream = new IntStream();
        intStream.append(get("cc"));
        intStream.append(getSlot());
        intStream.append(get("pid"));
        intStream.append(get("sc"));
        NmCharacter.appendString(intStream, title);
        appendChecksum(intStream);
        return createBitstreamList(getBitStream(intStream));
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
        listener.messageReceived(this);
    }
}
