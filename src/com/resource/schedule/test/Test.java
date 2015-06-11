package com.resource.schedule.test;

import java.util.ArrayList;
import java.util.List;

import com.resource.schedule.ResourceScheduler;
import com.resource.schedule.TerminationMessage;
import com.resource.schedule.exception.InvalidMessageException;
import com.resource.schedule.interfaces.external.Gateway;
import com.resource.schedule.interfaces.internal.MessagePriorizer;

public class Test
{
   public static void main( String... args )
   {
      int nResourcesNb = 2;
      List<Gateway> aGateways = new ArrayList<Gateway>();
      for( int i = 0; i < nResourcesNb; i++ )
      {
         aGateways.add( new MockGateway() );
      }
      
      MessagePriorizer pMessagePriorizer = new TestMessagePriorizer();
      
      ResourceScheduler pResourceScheduler = new ResourceScheduler( aGateways, pMessagePriorizer );
      try
      {
         pResourceScheduler.addMessage( new TestMessage( "msg_1", "group_2" ) );
         pResourceScheduler.addMessage( new TestMessage( "msg_2", "group_1" ) );
         pResourceScheduler.addMessage( new TestMessage( "msg_3", "group_2" ) );
         pResourceScheduler.addMessage( new TestMessage( "msg_4", "group_3" ) );
         
         pResourceScheduler.addMessage( new TestMessage( "msg_5", "group_4" ) );
         pResourceScheduler.setMessageGroupCancellation( "group_4", true );
         pResourceScheduler.addMessage( new TestMessage( "msg_6", "group_4" ) );
         
         pResourceScheduler.addMessage( new TerminationMessage( "group_1" ) );
         pResourceScheduler.addMessage( new TestMessage( "msg_7", "group_1" ) );
      }
      catch( InvalidMessageException pException )
      {
         System.out.println( pException );
      }
   }
}
