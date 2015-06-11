package com.resource.schedule;

import com.resource.schedule.interfaces.external.Message;

public class DefaultMessage
   implements Message
{  
   public DefaultMessage( String sId, String sGroupId )
   {
      this.sId = sId;
      this.sGroupId = sGroupId;
   }
   
   @Override
   public void completed()
   {
      ;//nop
   }
   
   public String getId()
   {
      return sId;
   }
   
   public String getGroupId()
   {
      return sGroupId;
   }
   
   public boolean isTerminationMessage()
   {
      return false;
   }
   
   public String display()
   {
	  return sId + "|" + sGroupId;
   }
   
   protected String sId = null;
   protected String sGroupId = null;
}

