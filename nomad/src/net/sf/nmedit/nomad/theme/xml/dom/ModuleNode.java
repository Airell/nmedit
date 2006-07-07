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
 * Created on Feb 27, 2006
 */
package net.sf.nmedit.nomad.theme.xml.dom;

import java.util.Iterator;
import java.util.LinkedList;


public class ModuleNode implements Iterable<ComponentNode> {

	private LinkedList<ComponentNode> componentNodeList;
	private int id;
	
	public ModuleNode(int id) {
		this.id = id;
		componentNodeList = new LinkedList<ComponentNode>();
	}
	
	public void addComponentNode(ComponentNode cnode) {
		if (cnode==null)
			throw new NullPointerException();
		componentNodeList.add(cnode);
	}
	
	public Iterator<ComponentNode> iterator() {
		return componentNodeList.iterator();
	}

	public void clear() {
		componentNodeList.clear();
	}

	public int getId() {
		return id;
	}
	
}