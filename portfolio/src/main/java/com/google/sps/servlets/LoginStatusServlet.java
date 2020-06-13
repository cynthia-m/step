
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
import java.util.HashMap; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {
 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    HashMap<String, String> ans = new HashMap<String, String> ();

    UserService userService = UserServiceFactory.getUserService();

    String check = "true";
    String login = "";
    String logout = "";
    if (userService.isUserLoggedIn()) {
      System.out.println("true");
      check= "true";

      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/submitComments.html";
      logout = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      logout = "Logout <a href=\"" + logout + "\">here</a>.";

    } else {
      check = "false";
      String urlToRedirectToAfterUserLogsIn = "/submitComments.html";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
      
      login += "Login <a href=\"" + loginUrl + "\">here</a>.";
      
      System.out.println("false");
    }
    ans.put("result", check);
    ans.put("login", login);
    ans.put("logout", logout);

    Gson gson = new Gson();
    String json = gson.toJson(ans);

    System.out.println(json);
    response.getWriter().println(json);
    
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
  }
}
