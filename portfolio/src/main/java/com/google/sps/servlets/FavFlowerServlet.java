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
import java.util.HashMap; 

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@WebServlet("/fav-flower")
public class FavFlowerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    HashMap<String, Integer> flowerVotes = new HashMap<String, Integer>();
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Query query = new Query("Flower");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      flowerVotes.put(entity.getProperty("flowerChoice").toString(),1);
    }

    int numVotes = 0;
    
    for (Entity entity : results.asIterable()) {
      flowerVotes.replace(entity.getProperty("flowerChoice").toString(),
      flowerVotes.get(entity.getProperty("flowerChoice").toString())+1
      );
    }

    Gson gson = new Gson();
    String json = gson.toJson(flowerVotes);
    response.getWriter().println(json);
    System.out.println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String flower = request.getParameter("flower");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity flowerEntity = new Entity("Flower");
    flowerEntity.setProperty("flowerChoice",flower);
    flowerEntity.setProperty("count",1);
    datastore.put(flowerEntity);
    response.sendRedirect("/favFlower.html");
    
    
  }
}
