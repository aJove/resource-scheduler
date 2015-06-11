package com.resource.schedule;

public class TerminationMessage
   extends DefaultMessage
{
   public TerminationMessage( String sGroupId )
   {
      super( null, sGroupId );
   }
   
   @Override
   public boolean isTerminationMessage()
   {
      return true;
   }
}
