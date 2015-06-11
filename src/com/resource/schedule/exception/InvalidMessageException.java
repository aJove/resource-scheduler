package com.resource.schedule.exception;

public class InvalidMessageException
   extends Exception
{
   public InvalidMessageException( String sMessage )
   {
      super( sMessage );
   }
   
   private static final long serialVersionUID = -5101012005486390540L;
}
