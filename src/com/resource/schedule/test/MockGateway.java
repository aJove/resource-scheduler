package com.resource.schedule.test;

import com.resource.schedule.interfaces.external.Gateway;
import com.resource.schedule.interfaces.external.Message;

public class MockGateway
   extends Thread
   implements Gateway
{
   public void send( Message pMessage )
      throws Exception
   {
      int nProcessingTime = new Double( Math.random() * 1000 ).intValue();
      sleep( nProcessingTime );
      pMessage.completed();
   }
}
