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
 * Created on Dec 17, 2006
 */
package net.nmedit.nmlibs.nmutils;

public class Timer
{
    
    private long time ;
    private int markcount;

    public void reset()
    {
        time = System.currentTimeMillis();
        markcount = 0;
    }
    
    public long time()
    {
        return System.currentTimeMillis()-time;
    }
    
    public String timeToSeconds(long t)
    {
        return (t/1000d)+"s";
    }
    
    public String timeToSeconds(double t)
    {
        return (t/1000d)+"s";
    }

    public void mark()
    {
        markcount ++;
    }
    
    public long avgTime()
    {
        long t = time();
        return markcount == 0 ? t : t/markcount;
    }

    public String toString()
    {
        return timeToSeconds(avgTime());
    }
    
}