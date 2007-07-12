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
package net.sf.nmedit.jnmprotocol;

/**
 * This class can be used to accept a specific midi message.
 * 
 * Example:
 * 
 * <code>
 * MessageMulticaster multicaster;
 * 
 * // create the acceptor 
 * NmMessageAcceptor<SynthSettingsMessage> acceptor =
 *  new NmMessageAcceptor<SynthSettingsMessage>(SynthSettingsMessage.class);
 *  
 * try
 * {
 *   // install acceptor
 *   multicaster.addProtocolListener(acceptor); 
 * 
 *   // add request message to the send queue
 *   protocol.send(new RequestSynthSettingsMessage());
 *   
 *   long timeout = 3000; // 3000 milliseconds timeout
 *   
 *   // this will trigger the protocol.heartbeat() method
 *   // and wait until the reply is received
 *   // when a timeout occurs an exception is thrown 
 *   multicaster.waitForReply(timeout);
 *   
 *   SynthSettingsMessage reply = multicaster.getRecentMessage();
 *   
 *   // do something with the reply message
 *   doSomething(reply);
 *   
 * }
 * finally
 * {
 *   // uninstall acceptor
 *   multicaster.removeProtocolListener(acceptor);
 * }
 * </code>
 *   
 */
public class NmMessageAcceptor<M extends MidiMessage> extends NmProtocolListener
{

    private Class<M> messageClass;
    private M firstMessage;
    private M recentMessage;
    private int acceptedCount;

    public NmMessageAcceptor(Class<M> messageClass)
    {
        this.messageClass = messageClass;
        this.firstMessage = null;
        this.recentMessage = null;
        this.acceptedCount = 0;
    }

    public void waitForReply(NmProtocol protocol, long timeout)
        throws Exception
    {
        final long timeoutThreshold = System.currentTimeMillis()+timeout;
        
        while (!isMessageReceived())
        {
            if (System.currentTimeMillis()>timeoutThreshold)
            {
                throw new Exception("timeout: "+timeout+"ms ("+messageClass.getName()+")");
            }

            protocol.heartbeat();

            try
            {
                protocol.awaitWorkSignal(100);
            }
            catch (InterruptedException e)
            {
                // no op
            }
        }
    }
    
    public void accept(MidiMessage message)
    {
        if (messageClass.isInstance(message))
        {
            M instance = messageClass.cast(message);
            if (firstMessage == null)
                firstMessage = instance;
            recentMessage = instance;
            acceptedCount ++;
        }
    }
    
    public int getAcceptedCount()
    {
        return acceptedCount;
    }
    
    public M getFirstMessage()
    {
        return firstMessage;
    }
    
    public M getRecentMessage()
    {
        return recentMessage;
    }
    
    public boolean isMessageReceived()
    {
        return firstMessage != null;
    }

    public void messageReceived(IAmMessage message) { accept(message); }
    public void messageReceived(LightMessage message) { accept(message); }
    public void messageReceived(MeterMessage message) { accept(message); }
    public void messageReceived(PatchMessage message) { accept(message); }
    public void messageReceived(AckMessage message) { accept(message); }
    public void messageReceived(PatchListMessage message) { accept(message); }
    public void messageReceived(NewPatchInSlotMessage message) { accept(message); }
    public void messageReceived(VoiceCountMessage message) { accept(message); }
    public void messageReceived(SlotsSelectedMessage message) { accept(message); }
    public void messageReceived(SlotActivatedMessage message) { accept(message); }
    public void messageReceived(ParameterMessage message) { accept(message); }
    public void messageReceived(ErrorMessage message) { accept(message); }
    public void messageReceived(SynthSettingsMessage message) { accept(message); }
    public void messageReceived(KnobAssignmentMessage message) { accept(message); }
    public void messageReceived(MorphAssignmentMessage message) { accept(message); }
    public void messageReceived(NoteMessage message) { accept(message); }
    public void messageReceived(MorphRangeChangeMessage message) { accept(message); }
    public void messageReceived(SetPatchTitleMessage message) { accept(message); }
    public void messageReceived(ParameterSelectMessage message) { accept(message); }
    
}
