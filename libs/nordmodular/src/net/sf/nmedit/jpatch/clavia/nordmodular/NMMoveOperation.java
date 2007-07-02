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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.nmedit.jpatch.AbstractMoveOperation;
import net.sf.nmedit.jpatch.LayoutTool;
import net.sf.nmedit.jpatch.PModule;

public class NMMoveOperation extends AbstractMoveOperation
{
    
    private VoiceArea va;
    private int dx;
    private int dy;
    private boolean offsetSet = false;
    private Collection<PModule> moved = null;
    
    public NMMoveOperation(VoiceArea va)
    {
        this.va = va;
    }
    
    private void checkOffset()
    {
        if (!offsetSet)
            throw new IllegalStateException("offset not set");
    }
    
    protected final static int MAX_POS = 8000-1; // which value is correct ?

    @Override
    public void move()
    {
        checkOffset();
        
        if (isEmpty())
            return;
        
        LayoutTool layoutTool = new LayoutTool(va, modules);
        layoutTool.setDelta(dx, dy);
        Object[] data = layoutTool.move();
        List<PModule> tmpMoved = new ArrayList<PModule>(data.length/3);
        for (int i=0;i<data.length;i+=3)
        {
            PModule m = (PModule) data[i+0];
            int sx = (Integer) data[i+1];
            int sy = (Integer) data[i+2];
            m.setScreenLocation(sx, sy);
            tmpMoved.add(m);
        }
        
        moved = tmpMoved;
    }

    public Point getScreenOffset(Point dst)
    {   
        checkOffset();
        if (dst == null)
            dst = new Point();
        dst.setLocation(dx, dy);
        return dst;
    }

    public void setScreenOffset(int x, int y)
    {
        this.dx = x;
        this.dy = y;
        this.offsetSet = true;
    }

    public Collection<? extends PModule> getMovedModules()
    {
        if (moved == null)
            throw new IllegalStateException("move not called");
        return moved;
    }

}
