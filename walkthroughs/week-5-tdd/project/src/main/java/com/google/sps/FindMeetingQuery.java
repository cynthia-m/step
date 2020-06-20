
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

    Collection<String> attendees = request.getAttendees();
    Collection<String> optAttendees = request.getOptionalAttendees();
    int reqDuration = (int) request.getDuration();
    Set<String> test2 = new HashSet<> ();
    Set<String> setOptAttendees = new HashSet<> ();
    setOptAttendees.addAll(optAttendees);
    test2.addAll(attendees);
    test2.addAll(optAttendees);
    Collection<TimeRange> ans = new ArrayList<TimeRange> ();
    Collection<TimeRange> eventsTR = new ArrayList<TimeRange> ();
    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
        eventsTR.add((TimeRange) ((Event)currEvent.next()).getWhen());
    }

    int start = 0;
    int end = 0;
    int i = 0;
    TimeRange t = TimeRange.fromStartEnd(start, end, true);
    while (start<=1439 && end<=1439) {
      if(checkOverlapTime(t, eventsTR)){
        if(start == end){
          start++;
          end++;
        }
        else{
          if (checkOverlapAttendeesHelp(t, test2, events)) {
            if(end-start>=reqDuration){
              t = TimeRange.fromStartEnd(start, end-1, true);
              ans.add(t);
            }
            start = end;
          }
          else{
            end++;
          }
        }
      }
      else{
        end++;
      }
      t = TimeRange.fromStartEnd(start, end, true);
    }
    
    if(end-start>reqDuration){
      t = TimeRange.fromStartEnd(start, end-1, true);
        ans.add(t);
    }

    if(ans.size()==0 && optAttendees.size()!=0 && attendees.size()!=0){
      MeetingRequest req = new MeetingRequest(attendees, reqDuration);
      Collection<Event> ev = new ArrayList<>();
      for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
        Event e = (Event)currEvent.next();
        if(!checkOverlapAttendees(setOptAttendees, e.getAttendees())){
          ev.add(e);
        }
      }
      return query(ev, req);
    }

    else{
      return ans;
    }
    

  }
  //checks if a timerange overlaps any of the time ranges
  public boolean checkOverlapTime(TimeRange t1, Collection <TimeRange> t2){

    for (Iterator currEvent = t2.iterator(); currEvent.hasNext(); ){ 
      TimeRange t = (TimeRange) currEvent.next();
      if(t1.overlaps(t)){
        return true;
      }
    }
    return false;

  }

  public boolean checkOverlapAttendeesHelp(TimeRange t, Set<String> a1, Collection<Event> events){
    Event e;
    for (Iterator currEvent = events.iterator(); currEvent.hasNext(); ){ 
      e = (Event) currEvent.next();
      if(t.overlaps(e.getWhen()) && checkOverlapAttendees(a1, e.getAttendees())){
        return true;
      }
    }
    return false;
  }

  public boolean checkOverlapAttendees(Set<String> a1, Set<String> a2){
     for(String e1 : a1){
        for(String e2 : a2){
          if(e1.equals(e2)){
            return true;
          }
        }
     }

    return false;

  }
}