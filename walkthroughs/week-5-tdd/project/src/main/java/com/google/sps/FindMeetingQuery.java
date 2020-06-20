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

    Collection<TimeRange> availableTimeRanges = new ArrayList<TimeRange> ();

    Collection<String> requestAttendees = request.getAttendees();
    Collection<String> requestOptionalAttendees = request.getOptionalAttendees();
    Set<String> requestAttendeesSet = new HashSet<> ();
    Set<String> requestOptionalAttendeesSet = new HashSet<> ();
    requestOptionalAttendeesSet.addAll(requestOptionalAttendees);
    requestAttendeesSet.addAll(requestAttendees);
    requestAttendeesSet.addAll(requestOptionalAttendees);
    int requestDuration = (int) request.getDuration();
    
    Collection<TimeRange> allEventsTimeRanges = new ArrayList<TimeRange> ();
    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
      allEventsTimeRanges.add((TimeRange) ((Event) currEvent.next()).getWhen());
    }

    int startTime = 0;
    int endTime = 0;
    TimeRange tempTimeRange = TimeRange.fromStartEnd(startTime, endTime, true);
    while (startTime <= 1439 && endTime <= 1439) {
      if (checkOverlapMultipleTimeRanges(tempTimeRange, allEventsTimeRanges)) {
        //don't add if the start=end but the timerange overlaps
        //timerange has duration zero and should just be updated
        if (startTime == endTime) {
          startTime++;
          endTime++;
        } else {
          //want to add if the attendees overlap and the duration is long enough
          if (checkOverlapMultipleAttendees(tempTimeRange, requestAttendeesSet, events)) {
            if (endTime - startTime >= requestDuration ) {
              tempTimeRange = TimeRange.fromStartEnd(startTime, endTime-1, true);
              availableTimeRanges.add(tempTimeRange);
            }
            startTime = endTime;
          } else {
            endTime++;
          }
        }
      } else {
        endTime++;
      }
      tempTimeRange = TimeRange.fromStartEnd(startTime, endTime, true);
    }
    
    if (endTime - startTime > requestDuration) {
      tempTimeRange = TimeRange.fromStartEnd(startTime, endTime-1, true);
      availableTimeRanges.add(tempTimeRange);
    }
    //if the optional attendees cause there to be no available timeranges
    //remove the optional attendees and optional events
    if (availableTimeRanges.size() == 0 && requestOptionalAttendees.size() != 0 && requestAttendees.size() != 0) {
      MeetingRequest requestWithoutOptionals = new MeetingRequest(requestAttendees, requestDuration);
      Collection<Event> eventsWithoutOptionalEvents = new ArrayList<> ();
      for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ) { 
        Event tempEvent = (Event)currEvent.next();
        if (!checkOverlapAttendees(requestOptionalAttendeesSet, tempEvent.getAttendees())) {
          eventsWithoutOptionalEvents.add(tempEvent);
        }
      }
      return query(eventsWithoutOptionalEvents, requestWithoutOptionals);
    } else {
      return availableTimeRanges;
    }
  }

  //checks if a TimeRange overlaps any of the TimeRanges in a collection
  public boolean checkOverlapMultipleTimeRanges(TimeRange singleTimeRange, Collection <TimeRange> timeRanges) {
    for (Iterator currTimeRange = timeRanges.iterator(); currTimeRange.hasNext(); ) { 
      TimeRange tempTimeRange = (TimeRange) currTimeRange.next();
      if (singleTimeRange.overlaps(tempTimeRange)) {
        return true;
      }
    }
    return false;
  }

  //checks if a set of attendees overlaps any of the attendees in the Events of an overlapping TimeRange
  public boolean checkOverlapMultipleAttendees(TimeRange timeRange, Set<String> attendees, Collection<Event> events) {
    Event tempEvent;
    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ) { 
      tempEvent = (Event) currEvent.next();
      if (timeRange.overlaps(tempEvent.getWhen()) && checkOverlapAttendees(attendees, tempEvent.getAttendees())) {
        return true;
      }
    }
    return false;
  }

  //checks if two sets of attendees overlaps
  public boolean checkOverlapAttendees(Set<String> attendees1, Set<String> attendees2) {
     for (String attendeeFrom1 : attendees1) {
        for (String attendeeFrom2 : attendees2) {
          if (attendeeFrom1.equals(attendeeFrom2)) {
            return true;
          }
        }
     }
    return false;
  }
}