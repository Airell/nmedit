/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2007 Marcus Andersson

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

package net.sf.nmedit.jnmprotocol2;

import java.util.*;

import net.sf.nmedit.jnmprotocol2.MidiException;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.PDLData;
import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jpdl2.*;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

public class SynthSettingsMessage extends MidiMessage
{

    private BitStream settingsStream;
    private Map<String, Object> parameterMap;
    private boolean extendedSettings = false;

    private static final String[] setting_parameters = {
        "midiClockSource",
        "midiVelScaleMin",
        "ledsActive",
        "midiVelScaleMax",
        "midiClockBpm",
        "localOn",
        "keyboardMode",
        "pedalPolarity",
        "globalSync",
        "masterTune",
        "programChangeReceive",
        "programChangeSend",
        "knobMode",
        "midiChannelSlot0",
        "midiChannelSlot1",
        "midiChannelSlot2",
        "midiChannelSlot3"
    };

    private static final String[] extended_parameters = {
        "slot0Selected",
        "slot1Selected",
        "slot2Selected",
        "slot3Selected",
        "activeSlot",
        "slot0VoiceCount",
        "slot1VoiceCount",
        "slot2VoiceCount",
        "slot3VoiceCount"
    };

    private static void append(IntStream dst, int i, Map<String, Object> params)
    {
        Object value = params.get(setting_parameters[i]);
        if (value == null)
            throw new RuntimeException("parameter missing: "+setting_parameters[i]);
        if (!(value instanceof Integer))
            throw new RuntimeException("parameter not ant int: "+value);
        dst.append(((Integer)value).intValue());
    }
    
    public static IntStream createIntStream(Map<String, Object> params)
    {
        Object name = params.get("name");
        if (name == null || (!(name instanceof String)) || !NmCharacter.isValid((String)name))
            throw new RuntimeException("invalid parameter 'name': "+name);

        IntStream is = new IntStream();
        is.append(0x03); // type = 3 <= SynthSettings
        
        int i=0;
        append(is, i++, params); // midiClockSource
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params); // midiClockBpm
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params); // masterTune
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params); // knobMode
        NmCharacter.appendString(is, (String)name); // String$name
        append(is, i++, params); // midiChannelsSlotA
        append(is, i++, params); // midiChannelsSlotB
        append(is, i++, params); // midiChannelsSlotC
        append(is, i++, params); // midiChannelsSlotD
      
        return is;
    }
    
    public static BitStream createBitStream(IntStream data)
    {
        PDLPacketParser patchParser = new PDLPacketParser(PDLData.getPatchDoc());
        
        try
        {
            patchParser.parse(data);
            return patchParser.getBitStream();
        }
        catch (PDLException e)
        {
            throw new RuntimeException("could not generate bitstream", e);
        }
    }

    private SynthSettingsMessage()
    {
        super();
        settingsStream = new BitStream();

        addParameter("pid", "data:pid");
        set("cc", 0x1c);
        set("pid", 0);
    
        expectsreply = false; // true ???
        isreply = true;
    }

    public SynthSettingsMessage(PDLPacket packet) throws MidiException
    {
    this();
    setAll(packet);
    
    packet = packet.getPacket("data:next");
    while (packet != null) {
        settingsStream.append(packet.getVariable("data"), 7);
        packet = packet.getPacket("next");
    }
    // Remove padding
    settingsStream.setSize((settingsStream.getSize()/8)*8);
    
    // parse settings
    try
    {
    getParamMap();
    }
    catch (RuntimeException e)
    {
        // TODO do not use RuntimeException in getParamMap()
        MidiException me = new MidiException(e.getMessage(), 0);
        me.initCause(e);
    }
    }

    public SynthSettingsMessage(Map<String, Object> params)
    throws MidiException
    {
        this();
        set("slot", 0);
        this.parameterMap = params;
        
        IntStream is = createIntStream(params);
        BitStream bs = createBitStream(is);
        
        final int first = 1;
        final int last = 1;
        
        // Generate sysex bistream
        IntStream intStream = new IntStream();
        intStream.append(get("cc") + first + 2*last);
        intStream.append(get("slot"));
        intStream.append(0x01); // command
        intStream.append(0x01); // sectionsEnded = 1

        // Pad. Extra bits are ignored later.
        bs.append(0, 6);
        while (bs.isAvailable(7)) {
        intStream.append(bs.getInt(7));
        }
        
        // Generate sysex bitstream
        settingsStream = getBitStream(intStream);
    }

    public Map<String, Object> getParamMap() 
    {
        if (parameterMap != null)
            return parameterMap;
        
        parameterMap = new HashMap<String, Object>();
        
        PDLPacketParser parser = new PDLPacketParser(PDLData.getPatchDoc());
    
        PDLPacket packet;
        
        try
        {
            settingsStream.setPosition(0);
            packet = parser.parse(settingsStream);
        }
        catch (PDLException e)
        {
            throw new RuntimeException("could not parse settings", e);
        }
        finally
        {
            settingsStream.setPosition(0);
        }
        
        PDLPacket section = packet.getPacket("section");
        int type = section.getVariable("type");
        
        if (type != 0x03)
            throw new RuntimeException("wrong type: 0x"+Integer.toHexString(type));
        
        PDLPacket data = section.getPacket("data");
        
        for (int i=0;i<setting_parameters.length;i++)
        {
            String name = setting_parameters[i];
            parameterMap.put(name, data.getVariable(name));
        }
        
        {
            // name of the synth
            String name = NmCharacter.extractName(data.getPacket("name"));
            parameterMap.put("name", name);
        }
        
        data = data.getPacket("extended");
        if (data != null)
        {
            extendedSettings = true;
            for (int i=0;i<extended_parameters.length;i++)
            {
                String name = extended_parameters[i];
                parameterMap.put(name, data.getVariable(name));
            }
        }
        
        return parameterMap;
    }
    
    public boolean containsExtendedSettings()
    {
        getParamMap(); // parse data
        return extendedSettings;
    }
    
    public BitStream getBitStream() throws MidiException
    {
        return settingsStream;
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
    listener.messageReceived(this);
    }
    
}
