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
 * Created on Jul 24, 2006
 */
package net.sf.nmedit.nomad.theme.component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.property.IntegerProperty;
import net.sf.nmedit.nomad.theme.property.IntegerValue;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;


public class LFODisplay extends NomadDisplay implements EventListener<ParameterEvent>
{

    public final static int WF_SINE = 0;
    public final static int WF_TRI = 1;
    public final static int WF_SAW = 2;
    public final static int WF_INV_SAW = 3;
    public final static int WF_SQUARE = 4;
    
    private int vwf = WF_SAW;
    private double vphase = 0.5;
    private GeneralPath gp = new GeneralPath();
    private boolean rebuildpath = true;
    private int w = 0;
    private int h = 0;

    private Parameter parPhase = null;
    private Parameter parWave = null;
    
    public final static String IPHASE = "parameter#0";
    public final static String IWF = "parameter#1";
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if (rebuildpath || getWidth()!=w || getHeight()!=h)
        {
            gp.reset();
            w = getWidth();
            h = getHeight();
            rebuildpath = false;

            switch (vwf)
            {
                case WF_SINE: 
                    generateSine(gp);
                    break;
                case WF_TRI: 
                    generateTri(gp);
                    break;
                case WF_SAW: 
                    generateSaw(gp);
                    break;
                case WF_INV_SAW: 
                    generateInvSaw(gp); 
                    break;
                case WF_SQUARE: 
                    generateSquare(gp); 
                    break;
            }

            AffineTransform at = new AffineTransform();
            at.translate(0, h/2);
            at.scale(w-1, (h-1)/2d);
            at.scale(1, -1);
            gp.transform(at);
        }

        g.setColor(getForeground());
        Graphics2D g2 = (Graphics2D) g;

        g2.translate((((1-vphase)-0.5)*(w-1)), 0);
        g2.draw(gp);
        g2.translate(-w, 0);
        g2.draw(gp);
        g2.translate(2*w, 0);
        g2.draw(gp);
    }
    
    // path bounds:
    //     +1
    // 0 ------- 1
    //     -1
    
    private void generateSine(GeneralPath gp)
    {
        // peaks
        final float cx = 0.5f;
        final float var = 1/8f;
        final float h = 1.32f;
        
        gp.moveTo(0, 0);   
        gp.curveTo(
                 0+var, h,
                cx-var, h,
                cx, 0);
        gp.curveTo(
                cx+var, -h,
                 1-var, -h,
                 1, 0);
    }
    
    private void generateTri(GeneralPath gp)
    {
        gp.moveTo(0, 0);
        gp.lineTo(1/4f, 1);
        gp.lineTo(2/4f, 0);
        gp.lineTo(3/4f,-1);
        gp.lineTo(1, 0);
    }
    
    private void generateSaw(GeneralPath gp)
    {
        gp.moveTo(0, 0);
        gp.lineTo(1/2f, 1);
        gp.lineTo(1/2f,-1);
        gp.lineTo(1, 0);
    }
    
    private void generateInvSaw(GeneralPath gp)
    {
        gp.moveTo(0, -1);
        gp.lineTo(0, 1);
        gp.lineTo(1, -1);
    }
    
    private void generateSquare(GeneralPath gp)
    {
        gp.moveTo(0,-1);
        gp.lineTo(0, 1);
        gp.lineTo(1/2f, 1);
        gp.lineTo(1/2f, -1);
        gp.lineTo(1, -1);
    }

    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    public void setWaveform( int v )
    {
        if (this.vwf!=v)
        {
    
            switch (v)
            {
                case WF_SINE:
                case WF_TRI:
                case WF_SAW:
                case WF_INV_SAW:
                case WF_SQUARE: break;
                default: throw new IllegalArgumentException("Unknown waveform id:"+v);
            }
            
            this.vwf = v;
            rebuildpath = true;
            repaint();
        }
    }

    public void setPhase( double v )
    {
        this.vphase = bounded(v);
        repaint();
    }

    public void registerProperties(PropertySet set) {
        super.registerProperties(set);
        set.add(new ParameterProperty(0));
        set.add(new ParameterProperty(1));
        set.add(new WaveFormProperty());
    }
    
    private static class WaveFormProperty extends IntegerProperty
    {

        public WaveFormProperty( )
        {
            super( "waveform" );
        }

        @Override
        public Value decode( String value )
        {
            return new WaveFormValue(this, value);
        }

        @Override
        public Value encode( NomadComponent component )
        {
            if (component instanceof LFODisplay)
                return new WaveFormValue(this, ((LFODisplay)component).getWaveForm());
            return null;
        }
        
    }
    
    private static class WaveFormValue extends IntegerValue
    {
        public WaveFormValue( Property property, int value )
        {
            super( property, value );
        }

        public WaveFormValue( Property property, String representation )
        {
            super( property, representation );
        }

        @Override
        public void assignTo( NomadComponent component )
        {
            if (component instanceof LFODisplay)
                ((LFODisplay)component).setWaveform(getIntegerValue());
        }
        
    }
    
    public int getWaveForm()
    {
        return vwf;
    }

    public void link(Module module) {
        parPhase = module.getParameter(getParameterInfo(IPHASE).getContextId());
        if (parPhase!=null) parPhase.addListener(this);
        DParameter info = getParameterInfo(IWF);
        if (info!=null)
        {
            parWave = module.getParameter(info.getContextId());
            if (parWave!=null) parWave.addListener(this);
        }
        
        updateValues();
    }

    public void unlink() {
        if (parPhase!=null) parPhase.removeListener(this);
        if (parWave!=null) parWave.removeListener(this);

        parPhase = null;
        parWave = null;
    }
    
    protected void updateValues()
    {
        if (parPhase!=null) setPhase(getDoubleValue(parPhase));
        if (parWave!=null) setWaveform(parWave.getValue()-parWave.getMinValue());
    }
    
    public void event(ParameterEvent event)
    {
        Parameter p = event.getParameter();
        
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            if (parPhase==p) setPhase(getDoubleValue(p));
            else if (parWave==p) setWaveform(p.getValue()-p.getMinValue());
        }
    }
}