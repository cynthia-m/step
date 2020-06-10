
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
import java.io.PrintWriter;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {
 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //  response.setContentType("text/html");
    // PrintWriter out = response.getWriter();

    // UserService userService = UserServiceFactory.getUserService();
    // if (userService.isUserLoggedIn()) {
    //   out.println("True");
    // } else {
    //   out.println("False");
    // }
  }
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
   
  }
}
