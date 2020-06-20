// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<String> requestAttendees = request.getAttendees();
    Collection<String> requestOptionalAttendees = request.getOptionalAttendees();
    int requestDuration = (int) request.getDuration();

    Set<String> requestAttendeesSet = new HashSet<> ();
    Set<String> requestOptionalAttendeesSet = new HashSet<> ();
    requestOptionalAttendeesSet.addAll(requestOptionalAttendees);
    requestAttendeesSet.addAll(requestAttendees);
    requestAttendeesSet.addAll(requestOptionalAttendees);

    Collection<TimeRange> availableTimeRanges = new ArrayList<TimeRange> ();
    Collection<TimeRange> allEventsTimeRanges = new ArrayList<TimeRange> ();

    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
        allEventsTimeRanges.add((TimeRange) ((Event)currEvent.next()).getWhen());
    }

    int startTime = 0;
    int endTime = 0;
    TimeRange tempTimeRange = TimeRange.fromStartEnd(startTime, endTime, true);
    while (startTime<=1439 && endTime<=1439) {
      if(checkOverlapMultipleTimeRanges(tempTimeRange, allEventsTimeRanges)){
        if(startTime == endTime){
          startTime++;
          endTime++;
        }
        else{
          if (checkOverlapMultipleAttendees(tempTimeRange, requestAttendeesSet, events)) {
            if(endTime-startTime>=requestDuration){
              tempTimeRange = TimeRange.fromStartEnd(startTime, endTime-1, true);
              availableTimeRanges.add(tempTimeRange);
            }
            startTime = endTime;
          }
          else{
            endTime++;
          }
        }
      }
      else{
        endTime++;
      }
      tempTimeRange = TimeRange.fromStartEnd(startTime, endTime, true);
    }
    
    if(endTime-startTime>requestDuration){
      tempTimeRange = TimeRange.fromStartEnd(startTime, endTime-1, true);
        availableTimeRanges.add(tempTimeRange);
    }

    if(availableTimeRanges.size()==0 && requestOptionalAttendees.size()!=0 && requestAttendees.size()!=0){
      MeetingRequest requestWithoutOptionals = new MeetingRequest(requestAttendees, requestDuration);
      Collection<Event> eventsWithoutOptionalEvents = new ArrayList<>();
      for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
        Event tempEvent = (Event)currEvent.next();
        if(!checkOverlapAttendees(requestOptionalAttendeesSet, tempEvent.getAttendees())){
          eventsWithoutOptionalEvents.add(tempEvent);
        }
      }
      return query(eventsWithoutOptionalEvents, requestWithoutOptionals);
    }

    else{
      return availableTimeRanges;
    }
    

  }
  //checks if a timerange overlaps any of the time ranges
  public boolean checkOverlapMultipleTimeRanges(TimeRange singleTimeRange, Collection <TimeRange> timeRanges){

    for (Iterator currTimeRange = timeRanges.iterator(); currTimeRange.hasNext(); ){ 
      TimeRange tempTimeRange = (TimeRange) currTimeRange.next();
      if(singleTimeRange.overlaps(tempTimeRange)){
        return true;
      }
    }
    return false;

  }

  public boolean checkOverlapMultipleAttendees(TimeRange timeRange, Set<String> attendees, Collection<Event> events){
    Event tempEvent;
    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
      tempEvent = (Event) currEvent.next();
      if(timeRange.overlaps(tempEvent.getWhen()) && checkOverlapAttendees(attendees, tempEvent.getAttendees())){
        return true;
      }
    }
    return false;
  }

  public boolean checkOverlapAttendees(Set<String> attendees1, Set<String> attendees2){
     for(String attendeeFrom1 : attendees1){
        for(String attendeeFrom2 : attendees2){
          if(attendeeFrom1.equals(attendeeFrom2)){
            return true;
          }
        }
     }

    return false;

  }
}