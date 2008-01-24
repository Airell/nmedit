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
package net.sf.nmedit.jpdl2;

public interface PDLBlock extends Iterable<PDLItem>
{

    /**
     * Number of items in this block.
     * @return the number of items in this block
     */
    int getItemCount();
    
    /**
     * Returns the item at the specified index
     * @param index the item index
     * @return the item at the specified index
     */
    PDLItem getItem(int index);
 
    /**
     * Returns the minimum number of bits of this block.
     * Optional or conditional items are not included in the return value.
     * @return the minimum number of bits of this block
     */
    int getMinimumSize();
    
}
