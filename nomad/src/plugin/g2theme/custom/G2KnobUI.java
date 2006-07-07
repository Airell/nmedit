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

/*
 * Created on May 15, 2006
 */
package plugin.g2theme.custom;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.nomad.theme.laf.KnobUI;

public class G2KnobUI extends KnobUI
{

    public G2KnobUI()
    {
        clFill1 = Color.WHITE;
        clFill2 = Color.LIGHT_GRAY;
        edgeSize = 0;
    }

    private static G2KnobUI knobUI = new G2KnobUI();  
    
    public static ComponentUI createUI(JComponent c) 
    {
        return knobUI;
    }
    
}