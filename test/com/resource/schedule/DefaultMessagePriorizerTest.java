package com.resource.schedule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.resource.schedule.DefaultMessage;
import com.resource.schedule.DefaultMessagePriorizer;
import com.resource.schedule.interfaces.external.Message;
import com.resource.schedule.interfaces.internal.MessagePriorizer;

public class DefaultMessagePriorizerTest {

	@Before
	public void setUp() 
	   throws Exception 
	{
	   pMessagePriorizer = new DefaultMessagePriorizer();
	   aMessages = new ArrayList<Message>( 
	      Arrays.asList(
		     new DefaultMessage( "2", "1" ),
		     new DefaultMessage( "3", "2" ),
		     new DefaultMessage( "4", "3" ),
		     new DefaultMessage( "5", "2" )
	   ) );
	}

	@Test
	public void testShouldReturnNextMessageFromSameGroup() 
	{
	   DefaultMessage pExpectedMessage = (DefaultMessage)aMessages.get(1);
		
	   Message pLastTreatedMessage = new DefaultMessage( "1", "2" );
	   DefaultMessage pMessage = (DefaultMessage)pMessagePriorizer.getNextMessageToTreat( pLastTreatedMessage, aMessages );
	   
	   assertEquals( "Wrong message returned", pExpectedMessage.display(), pMessage.display() );
	   assertEquals( "Wrong update of Messages queue", 3, aMessages.size() );
	}
	
	@Test
	public void testShouldReturnFirstMessageFromQueueBecauseNoMoreMessageFromSameGroup() 
	{
	   DefaultMessage pExpectedMessage = (DefaultMessage)aMessages.get(0);
	   
	   Message pLastTreatedMessage = new DefaultMessage( "1", "4" );
	   DefaultMessage pMessage = (DefaultMessage)pMessagePriorizer.getNextMessageToTreat( pLastTreatedMessage, aMessages );
	   
	   assertEquals( "Wrong message returned", pExpectedMessage.display(), pMessage.display() );
	   assertEquals( "Wrong update of Messages queue", 3, aMessages.size() );
	}
	
	@Test
	public void testShouldReturnFirstMessageFromQueueBecauseFirstCall() 
	{
	   DefaultMessage pExpectedMessage = (DefaultMessage)aMessages.get(0);
	   
	   Message pLastTreatedMessage = null;
	   DefaultMessage pMessage = (DefaultMessage)pMessagePriorizer.getNextMessageToTreat( pLastTreatedMessage, aMessages );
	   
	   assertEquals( "Wrong message returned", pExpectedMessage.display(), pMessage.display() );
	   assertEquals( "Wrong update of Messages queue", 3, aMessages.size() );
	}

	List<? extends Message> aMessages = null;
	MessagePriorizer pMessagePriorizer = null;
}
