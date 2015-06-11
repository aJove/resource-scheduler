package com.resource.schedule.interfaces.internal;

import java.util.List;

import com.resource.schedule.interfaces.external.Message;

public interface MessagePriorizer
{
   public Message getNextMessageToTreat( Message pLastTreatedMessage, List<? extends Message> aMessages );
}
