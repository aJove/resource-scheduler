package com.resource.schedule;

import java.util.List;

import com.resource.schedule.interfaces.external.Message;
import com.resource.schedule.interfaces.internal.MessagePriorizer;

public class DefaultMessagePriorizer
   implements MessagePriorizer
{
   @Override
   public Message getNextMessageToTreat( Message pLastTreatedMessage, List<? extends Message> aMessages )
   {
      Message pResult = null;
      
      if( aMessages != null )
      {
         synchronized( aMessages )
         {
            if( pLastTreatedMessage != null )
            {
               boolean bFound = false;
               DefaultMessage pCurrentMessage = null;
               for( int i = 0; i < aMessages.size(); i ++ )
               {
                  pCurrentMessage = (DefaultMessage)aMessages.get(i);
                  if( pCurrentMessage.getGroupId().equals( ((DefaultMessage)pLastTreatedMessage).getGroupId() ) )
                  {
                     pResult = pCurrentMessage;
                     aMessages.remove( pCurrentMessage );
                     bFound = true;
                     break;
                  }
               }
               if( !bFound )
                  pResult = aMessages.remove(0);
            }
            else
            {
               pResult = aMessages.remove(0);
            }
         }
      }
      
      return pResult;
   }
}

