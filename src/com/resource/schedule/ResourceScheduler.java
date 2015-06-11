package com.resource.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.resource.schedule.exception.InvalidMessageException;
import com.resource.schedule.interfaces.external.Gateway;
import com.resource.schedule.interfaces.external.Message;
import com.resource.schedule.interfaces.internal.MessagePriorizer;

public class ResourceScheduler
{
   public ResourceScheduler( List<Gateway> aGateways )
   {
      if( aGateways == null )
      {
         aGateways = new ArrayList<Gateway>();
      }
      int nResourcesNb = aGateways.size(); //number of resources available
      
      hCancelledGroupIds = new HashMap<String, String>(); //ids of cancelled groups of messages
      hTerminatedGroupIds = new HashMap<String, String>(); //ids of groups of message which have been terminated
      
      pMessagePriorizer = new DefaultMessagePriorizer(); //alternative message prioritisation is possible
      aMessageQueue = new ArrayList<DefaultMessage>(); //the message queue, waiting to be processed
      
      aMessageSenders = new MessageSender[nResourcesNb];
      for( int i=0; i < nResourcesNb; i++ ) //according to the number of resources available, a fixed group of worker threads
      {
         aMessageSenders[i] = new MessageSender( aGateways.get(i) );
         aMessageSenders[i].start();
      }
   }
   
   public ResourceScheduler( List<Gateway> aGateways, MessagePriorizer pMessagePriorizer )
   {
      this( aGateways );
      
      if( pMessagePriorizer != null )
      {
         this.pMessagePriorizer = pMessagePriorizer;
      }
   }
   
   public void setMessageGroupCancellation( String sGroupId, boolean bCancelled )
   {
      if( sGroupId == null )
      {
         return;
      }
      
      if( bCancelled )
      {
         hCancelledGroupIds.put( sGroupId, null );
         synchronized( aMessageQueue ) //remove from the queue all messages belonging to the cancelled group
         {
            List<Message> aMessagesToRemove = new ArrayList<Message>();
            for( DefaultMessage pMessage : aMessageQueue )
            {
               if( pMessage.getGroupId().equals( sGroupId ) )
               {
                  aMessagesToRemove.add( pMessage );
               }
            }
            if( !aMessagesToRemove.isEmpty() )
            {
               aMessageQueue.removeAll( aMessagesToRemove );
            }
         }
      }
      else
      {
         hCancelledGroupIds.remove( sGroupId );
      }
   }
   
   public void addMessage( DefaultMessage pMessage ) 
      throws InvalidMessageException
   {
      if( pMessage == null || hCancelledGroupIds.containsKey( pMessage.getGroupId() ) ) //message belonging to a cancelled group are not treated
      {
         return;
      }
      
      if( pMessage.isTerminationMessage() )
      {
         hTerminatedGroupIds.put( pMessage.getGroupId(), null );
         return;
      }
      
      if( hTerminatedGroupIds.containsKey( pMessage.getGroupId() ) ) //trying to add a message after termination of its group throw an exception
      {
         throw new InvalidMessageException( 
           "Trying to add an invalid message : " + pMessage.getId() + "|" + pMessage.getGroupId() + 
           ", group " + pMessage.getGroupId() + " has been terminated" );
      }
      
      synchronized( aMessageQueue ) 
      {
         aMessageQueue.add( pMessage );
         aMessageQueue.notify(); //notify worker threads a new message has to be sent
      }
   }
   
   protected class MessageSender 
      extends Thread 
   {
      public MessageSender( Gateway pGateway )
      {
         this.pGateway = pGateway;
      }
      
      public void run() {
         Message pMessage = null;
         
         while( true ) 
         {
            synchronized( aMessageQueue ) 
            {
               while( aMessageQueue.isEmpty() ) 
               {
                  try
                  {
                     aMessageQueue.wait(); //waiting for a new message to send
                  }
                  catch( InterruptedException pIgnored )
                  {
                     ;//nop
                  }
               }
               
               pMessage = pMessagePriorizer.getNextMessageToTreat( pMessage, aMessageQueue ); //get message to send, according to prioritisation algorithm
            }
            
            try 
            {
               pGateway.send( pMessage );
            }
            catch( Exception pException )//if a RuntimeException is not thrown, the pool could leak threads
            {
               // log the error, using Log4J for example
            }
         }
      }
      
      private Gateway pGateway = null;
   }
   
   protected Map<String, String> hCancelledGroupIds = null;
   protected Map<String, String> hTerminatedGroupIds = null;
   protected MessagePriorizer pMessagePriorizer;
   
   protected final MessageSender[] aMessageSenders;
   protected final ArrayList<DefaultMessage> aMessageQueue;
}
