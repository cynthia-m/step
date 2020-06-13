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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.Collections;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns comments to user.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    
    ArrayList<String> commentsFin = new ArrayList<String> ();
    ArrayList<String> comments = new ArrayList<String> ();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Feedback");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      comments.add((String) entity.getProperty("comment"));
    }

    int maxNumComments;
    try {
      maxNumComments = Integer.parseInt(request.getParameter("max"));
    } catch (NumberFormatException e) {
      maxNumComments = 3;
      System.err.println("Invalid Number, Use Default: 3");
    }
    String userEmail = "";

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      userEmail += userService.getCurrentUser().getEmail();
    }

    Collections.shuffle(comments);
    int currNumComments = 0;
    while (currNumComments < maxNumComments && currNumComments < comments.size()) {
      if (comments.get(currNumComments) != null) {
        commentsFin.add(userEmail+": " + comments.get(currNumComments));
      }
      currNumComments++;
    }
    response.getWriter().println(commentsFin);
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity taskEntity = new Entity("Feedback");
    taskEntity.setProperty("comment", comment);
    datastore.put(taskEntity);
    
    response.sendRedirect("/comments.html");
  }
}
