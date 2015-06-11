package com.resource.schedule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.resource.schedule.ResourceScheduler;
import com.resource.schedule.TerminationMessage;
import com.resource.schedule.exception.InvalidMessageException;
import com.resource.schedule.interfaces.external.Gateway;
import com.resource.schedule.interfaces.internal.MessagePriorizer;
import com.resource.schedule.test.MockGateway;
import com.resource.schedule.test.TestMessage;
import com.resource.schedule.test.TestMessagePriorizer;

public class ResourceSchedulerTest {

	@Before
	public void setUp() 
	   throws Exception 
	{
	   int nResourcesNb = 2;
	   List<Gateway> aGateways = new ArrayList<Gateway>();
	   for( int i = 0; i < nResourcesNb; i++ )
	   {
		   aGateways.add( new MockGateway() );
	   }

	   MessagePriorizer pMessagePriorizer = new TestMessagePriorizer();

	   pResourceScheduler = new ResourceScheduler( aGateways, pMessagePriorizer );
	   
	   
	}

	@Test( expected=InvalidMessageException.class )
	public void testTerminationMessage() 
	   throws InvalidMessageException
	{
	   pResourceScheduler.addMessage( new TerminationMessage( "group_1" ) );
	   pResourceScheduler.addMessage( new TestMessage( "msg_8", "group_1" ) );
	}

	ResourceScheduler pResourceScheduler = null;
}
