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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  ArrayList <String> comments = new ArrayList <String> ();
  ArrayList <String> comments_fin = new ArrayList <String> ();
    
    
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    response.setContentType("text/html;");
    int i = 0;
    int l = comments_fin.size();
    while(i<l){
      if(comments_fin.get(i)==null){
        comments_fin.remove(i);
        l--;
      }
      else{
        i++;
      }

    }
    
    response.getWriter().println(comments_fin);
  }
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
    comments_fin.clear();
    comments.clear();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    int curr_num_comments=0;
    
    int max_num_comments;
    try {
      max_num_comments = Integer.parseInt(request.getParameter("num_comment"));
    } catch (NumberFormatException e) {
      System.err.println("Invalid Number, Use Default: 3");
      max_num_comments = 3;
    }
   
    Entity taskEntity = new Entity("Feedback");
    taskEntity.setProperty("comment",comment);
    datastore.put(taskEntity);
    Query query = new Query("Feedback");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      comments.add((String)entity.getProperty("comment"));
      System.out.println("Take2"+ (String)entity.getProperty("comment"));
    }
    Collections.shuffle(comments);
    while(curr_num_comments<max_num_comments && curr_num_comments<comments.size()){
      comments_fin.add(comments.get(curr_num_comments));
      curr_num_comments++;
    }
   
    response.sendRedirect("/comments.html");
  }
}
