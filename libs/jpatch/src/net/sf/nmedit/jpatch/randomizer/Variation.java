/* Copyright (C) 2006-2007 Julien Pauty
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
package net.sf.nmedit.jpatch.randomizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

public class Variation extends JComponent { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3543257101198489340L;
	int values[] = null;
	
	private static final Border VarBorder = BorderFactory.createLineBorder(Color.GRAY);

	// render to buffer - improves rendering performance of component
    private BufferedImage renderedImage = null;
    private boolean modifiedFlag = true;
	
	public Variation(){
		construct();
	}
	
	public Variation(int size) {
		if (size > 0) {
			values = new int[size];
			for (int i = 0; i < size; i ++)
			{
				values[i]=(int)(128*Math.random() );
				//System.out.println(values[i]);
			}
		}
		construct();
	}
	
	public Variation(Variation v,double range,double probability) {
		values = new int[v.getNbValues()];
		
		mutate(v,range,probability);		
		construct();
	}
	
	private void setModifiedFlag()
	{
	    modifiedFlag = true;
	    repaint();
	}
	
	private void construct(){
        setBackground(Color.BLACK);
        setBorder(VarBorder);
		setTransferHandler(new VariationTransferHandler());
		
		VariationListener listener = new VariationListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setPreferredSize(new Dimension(50,50));
		setMaximumSize(new Dimension(50,50));
		setMinimumSize(new Dimension(50,50));
		
	}
	
	public void mutate(Variation refVar,double range,double probability )
	{
		if (values == null)
		{
			values = new int[refVar.getNbValues()];
		}
		
		
		for (int i = 0 ; i  < values.length ; i++)
		{
			
			if (Math.random() < probability)
			{
				double amplitude = 127*2*range;
				int offset = (int)(Math.random()* amplitude
							-amplitude/2);
				int val = refVar.getValues()[i] + offset;
				values[i] = val < 0 ? 0: val > 128 ? 127:val;
			} else {
				values[i] = refVar.getValues()[i];
			}
		}	
		
		setModifiedFlag();
	}
	
	public void randomize(int size)
	{
		if (values == null || values.length != size)
		{
			values = new int[size];
		}
		
		for (int i = 0 ; i  < values.length ; i++)
		{
				values[i] = (int)(Math.random()*127);
		}
        setModifiedFlag();
	}
	
	public int getNbValues(){
		return values.length;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public Color colorValue(int hue){
		
		//return Color.getHSBColor(hue/128f, hue/128f, hue/128f);
		//return Color.getHSBColor(hue/128f, hue/128f,.8f);
		//return Color.getHSBColor(hue/128f, 0f, hue/128f);
		return Color.getHSBColor(.2f*hue/128f,1f-hue/128f,.8f); // ok variation rouge
		
		//return Color.getHSBColor(.3f*hue/128f,.5f+.5f*hue/128f,.8f);
		
	}

	private Polygon poly = null;
	
	private Polygon getPolygon()
	{
		//if (poly==null){ 
			if (values == null) return null;
			
			poly = new Polygon();

	        Dimension dd = getSizeWithoutInsets();
	        
	        int w = dd.width/8;
	        int h = dd.height/8;
	        
			
			poly.addPoint(w/2,h/2);
			
			int prevX = w/2;
			int prevY = h/2;
			double prevAngle = 0;
			for (int i = 0; i < values.length ; i += 2)
			{
				double angle = 2*Math.PI* values[i]/512f+prevAngle;
				double amplitude = values[i+1]/2;// shouldn't it be /2d ?
				int x = (int)(prevX+amplitude*Math.cos(angle));
				int y = (int)(prevY+amplitude*Math.sin(angle));
				poly.addPoint(x,y);
				prevX = x;
				prevY = y;
				prevAngle = angle;
			}
			
			Rectangle bound = poly.getBounds();
			
			// translate the poly to 0,0
			if (bound.x < 0) poly.translate(-bound.x, 0);
			if (bound.y < 0) poly.translate(0,-bound.y);
			
			// scale it
			double scaleX = bound.width/((double)getWidth()-10d);
			double scaleY = bound.height/((double)getHeight()-10d);
			
			//System.out.println(scaleX+" "+scaleY);
			for (int i=0; i < poly.xpoints.length;  i++)
			{
				poly.xpoints[i] /= scaleX;
				poly.xpoints[i] += 5;
			}
			for (int i=0; i < poly.ypoints.length;  i++)
			{
				poly.ypoints[i] /= scaleY;
				poly.ypoints[i] += 5;
			}
			//System.out.println(p.getBounds());
		//}
		return poly;
		
	}
	
	private Dimension getSizeWithoutInsets()
	{
        Insets insets = getInsets();
        int ww = getWidth()-insets.left-insets.right;
        int hh = getHeight()-insets.top-insets.bottom;
        return new Dimension(ww,hh);
	}

	double angles[] = new double[64];
	private Polygon getPolygon2()
	{
		//if (poly==null){ 
		if (values == null) return null;
		
		poly = new Polygon();
		
		Dimension dd = getSizeWithoutInsets();
		
		int w = dd.width/8;
		int h = dd.height/8;
		
		poly.addPoint(w/2,h/2);
		
		int prevX = w/2;
		int prevY = h/2;
		double prevAngle = 0;
		for (int i = 0; i < values.length ; i += 2)
		{ 
			double angle = 2*Math.PI* values[i]/512f+prevAngle;
			angles[i] = 2*Math.PI* values[i]/512f;  
			double amplitude = values[i+1]/2;// shouldn't it be /2d ?
			int x = (int)(prevX+amplitude*Math.cos(angle));
			int y = (int)(prevY+amplitude*Math.sin(angle));
			poly.addPoint(x,y);
			prevX = x;
			prevY = y;
			prevAngle = angle;
		}
		
		Rectangle bound = poly.getBounds();
		
		// translate the poly to 0,0
		if (bound.x < 0) poly.translate(-bound.x, 0);
		if (bound.y < 0) poly.translate(0,-bound.y);
		
		// scale it
		double scaleX = bound.width/((double)getWidth()-10d);
		double scaleY = bound.height/((double)getHeight()-10d);
		
		//System.out.println(scaleX+" "+scaleY);
		for (int i=0; i < poly.xpoints.length;  i++)
		{
			poly.xpoints[i] /= scaleX;
			poly.xpoints[i] += 5;
		}
		for (int i=0; i < poly.ypoints.length;  i++)
		{
			poly.ypoints[i] /= scaleY;
			poly.ypoints[i] += 5;
		}
		//System.out.println(p.getBounds());
		//}
		return poly;
		
	}
	
	public void setBorder(Border b)
	{
	    super.setBorder(b);
	    setModifiedFlag();
	}
	
	public void setBackground(Color bg)
	{
	    super.setBackground(bg);
	    setModifiedFlag();
	}
	
	public void paintComponent(Graphics g)
	{
		// not necessary super.paintComponent(g);
	    BufferedImage image = getRenderedImage();
	    g.drawImage(image, 0, 0, null);
    }
	
	private BufferedImage getRenderedImage()
	{
	    BufferedImage image = renderedImage;
	    
	    boolean recreate = image == null || (image.getWidth()!=getWidth()) && image.getHeight()!=getHeight();
	    
	    if ((!recreate) && (!modifiedFlag)) return image;
	    
	    if (recreate)
	    {
	        if (image != null) image.flush(); // free resources
	        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	        renderedImage = image;
	    }
	    
	    Graphics2D g2 = image.createGraphics();
	    try
	    {
	        renderVariation(g2);
	    }
	    finally
	    {
	        g2.dispose();
	    }

	    return image;
	}

	private void renderVariation(Graphics2D g2)
    {
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
//        if (values != null){
//          for (int x = 0 ; x < 8 ; x++)
//          {
//              for (int y = 0 ; y < 8 ; y++)
//              {
//                  int w = getWidth()/8;
//                  int h = getHeight()/8;
//                  //Color c =  mutator.getPalette()[mutator.getColors()[series][x+y*8]];
//                  Color c = colorValue(getValues()[x+y*8]);
//                  g2.setColor(c);
//                  g2.fillRect(x*w,y*h,w,h);
//           
//                  //g.fillRect(0,0,getWidth(),getHeight());
//              }                   
//          }
//        }

        Insets insets = getInsets();
        int w = getWidth()-insets.left-insets.right;
        int h = getHeight()-insets.top - insets.bottom;
        
        g2.setColor(getBackground());

        Border border = getBorder();
        if (border == null || border.isBorderOpaque())
            g2.fillRect(0, 0, getWidth(), getHeight());
        else
            g2.fillRect(insets.left, insets.top,w,h);

        if (values != null){
            Polygon p = getPolygon2();
            for (int i = 0 ; i < p.xpoints.length-1 ; i ++){
                if (i%2 == 0){
                    g2.setColor(colorValue((int )(angles[i]*256/Math.PI)));
                
                    //g2.setStroke(new StrokeWidthManager());
                    //System.out.println(angles[i]*256/Math.PI + " a ");
                }
                g2.drawLine(p.xpoints[i],p.ypoints[i],p.xpoints[i+1],p.ypoints[i+1]);
                
            }
        }
        
    }

    public void setValues(int[] values) {
		this.values = values;
        setModifiedFlag();
	}
	
	public class VariationListener implements MouseMotionListener, MouseListener{

		private MouseEvent firstMouseEvent = null;
		
		public void mouseDragged(MouseEvent e) {
			if (firstMouseEvent != null){				
	//			If they are holding down the control key, COPY rather than MOVE
	            int ctrlMask = InputEvent.CTRL_DOWN_MASK;
	            int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ?
	                  TransferHandler.COPY : TransferHandler.MOVE;
	
	            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
	            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
	            //Arbitrarily define a 5-pixel shift as the
	            //official beginning of a drag.
	            if (dx > 2 || dy > 2) {
	            	//System.out.println("drag");
	                //This is a drag, not a click.
	                JComponent c = (JComponent)e.getSource();
	                TransferHandler handler = c.getTransferHandler();
	                //Tell the transfer handler to initiate the drag.
	                handler.exportAsDrag(c, firstMouseEvent, action);
	                firstMouseEvent = null;
	            }
			}
		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			System.out.println("ee");
			firstMouseEvent = e;
	        e.consume();
		}

		public void mouseReleased(MouseEvent arg0) {
			firstMouseEvent = null;
			
		}
		
	}
} 
