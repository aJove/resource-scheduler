package com.resource.schedule.test;

import com.resource.schedule.DefaultMessage;

public class TestMessage
   extends DefaultMessage
{
   public TestMessage( String sId, String sGroupId )
   {
      super( sId, sGroupId );
   }
   
   @Override
   public void completed()
   {
      System.out.println( display() );
   }
}
