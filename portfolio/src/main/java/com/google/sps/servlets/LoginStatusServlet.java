
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

/** Servlet that returns login status. */
@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {
 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    HashMap<String, String> accountProperties = new HashMap<String, String> ();

    UserService userService = UserServiceFactory.getUserService();

    String isLoggedIn = "true";
    String loginURL = "";
    String logoutURL = "";
    if (userService.isUserLoggedIn()) {
      isLoggedIn = "true";

      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/submitComments.html";
      logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      logoutURL = "Logout <a href=\"" + logoutURL + "\">here</a>.";
    } else {
      isLoggedIn = "false";

      String urlToRedirectToAfterUserLogsIn = "/submitComments.html";
      loginURL = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
      loginURL += "Login <a href=\"" + loginURL + "\">here</a>.";
    }
    accountProperties.put("checkLoggedIn", isLoggedIn);
    accountProperties.put("loginURL", loginURL);
    accountProperties.put("logoutURL", logoutURL);

    Gson gson = new Gson();
    String json = gson.toJson(accountProperties);
    response.getWriter().println(json);
  }
}
