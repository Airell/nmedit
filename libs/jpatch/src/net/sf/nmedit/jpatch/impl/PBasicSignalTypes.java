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
package net.sf.nmedit.jpatch.impl;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PSignalTypes;
import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;

/**
 * The reference implementation of interface {@link PSignalTypes}.
 * @author Christian Schneider
 */
public class PBasicSignalTypes implements PSignalTypes, Serializable
{
    
    private static final long serialVersionUID = 8556928210665894178L;
    private PSignal noSignal = null;
    private transient List<PSignal> signalList ;
    private transient Map<Integer, PSignal> signalIdMap;
    private transient Map<String, PSignal> signalNameMap;

    public PBasicSignalTypes()
    {
        super();
        init(8);
    }

    protected void init(int initialCapacity)
    {
        signalList = new ArrayList<PSignal>(initialCapacity);
        signalIdMap = new HashMap<Integer, PSignal>(initialCapacity);
        signalNameMap = new HashMap<String, PSignal>(initialCapacity);
    }

    public PSignal create(int id, String name, boolean noSignal)
    {
        return create(id, name, noSignal ? Color.WHITE : Color.BLACK, noSignal);
    }

    public PSignal create(int id, String name, Color color, boolean noSignal)
    {
        if (signalIdMap.containsKey(id)) throw new IllegalArgumentException("id already defined: "+id);
        if (signalNameMap.containsKey(name)) throw new IllegalArgumentException("name already defined: "+name);
        PSignal signal = new PBasicSignal(this, id, name, color);
        addSignalType(signal);
        if (noSignal) this.noSignal = signal;
        return signal;
    }
    
    private void addSignalType(PSignal signal)
    {
        signalList.add(signal);
        signalIdMap.put(signal.getId(), signal);
        signalNameMap.put(signal.getName(), signal);
    }
    
    public PSignal getSignalType(int index)
    {
        return signalList.get(index);
    }

    public PSignal getSignalTypeById(int id)
    {
        return signalIdMap.get(id);
    }

    public PSignal getSignalTypeByName(String name)
    {
        return signalNameMap.get(name);
    }

    public int getSignalTypeCount()
    {
        return signalList.size();
    }

    public PSignal noSignal()
    {
        return noSignal;
    }

    public Iterator<PSignal> iterator()
    {
        return new UnmodifiableIterator<PSignal>(signalList.iterator());
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        int cnt = 0;
        for (PSignal t: signalList)
        {
            sb.append(t);
            if (++cnt<signalList.size())
                sb.append(",");
        }
        sb.append(']');
        return sb.toString();
    }

    public int hashCode()
    {
        return signalList.hashCode();
    }

    public boolean contains(PSignal signalType)
    {
        return signalList.contains(signalType);
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PSignalTypes))) return false;
        
        PSignalTypes st = (PSignalTypes) o;
        if (st.getSignalTypeCount() != getSignalTypeCount())
            return false;
        
        if (!(noSignal == st.noSignal() || (noSignal != null && noSignal.equals(st.noSignal()))))
            return false;
        
        for (int i=getSignalTypeCount()-1;i>=0;i--)
            if (!st.contains(signalList.get(i)))
                return false;
        return true;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(getSignalTypeCount());
        for (int i=0;i<signalList.size();i++)
            out.writeObject(signalList.get(i));
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        int signalTypeCount = in.readInt();
        init(signalTypeCount);
        while (signalTypeCount>0)
        {
            addSignalType((PSignal) in.readObject());
            signalTypeCount --;
        }
    }
    
}