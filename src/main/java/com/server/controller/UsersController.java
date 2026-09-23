package com.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.server.model.User;
import com.server.model.Users;
import com.server.utils.HttpUtils;
import com.server.utils.UserUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/** Controller for user endpoints, refactored to REST API (JSON). */
public class UsersController {
  private Users data;
  private Gson gson = new Gson();

  public UsersController(Users data) {
    this.data = data;
  }

  public void getAllUsers(Socket clientSocket) throws IOException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
      ArrayList<User> dataResult = data.getAllData();

      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: application/json");
      out.println();
      out.println(gson.toJson(dataResult));
    }
  }

  public void getUserDataById(Socket clientSocket, String requestLine) throws IOException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
      int userId = UserUtils.getUserId(requestLine);

      if (userId != -1 && userId <= data.getArrayLength()) {
        User user = data.getUserDataByIndex(userId - 1);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: application/json");
        out.println();
        out.println(gson.toJson(user));
      } else {
        HttpUtils.handle404ErrorResponse(clientSocket);
      }
    }
  }

  public void getUserDataByQuery(Socket clientSocket, String requestLine)
      throws IOException, URISyntaxException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

      String[] arrayRequestLine = requestLine.split(" ");
      URI uri = new URI(arrayRequestLine[1]);

      Map<String, String> queryParams = HttpUtils.extractQueryParams(uri.getQuery());
      ArrayList<User> dataResult = data.getUserDataByQuery(queryParams);

      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: application/json");
      out.println();
      out.println(gson.toJson(dataResult));
    }
  }

  public void postUserData(Socket clientSocket, BufferedReader in) throws IOException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
      String requestBody = HttpUtils.getRequestBody(in);

      User newUser = gson.fromJson(requestBody, User.class);
      data.addNewData(newUser);

      JsonObject response = new JsonObject();
      response.addProperty("status", "success");
      response.addProperty("message", "User added successfully");

      out.println("HTTP/1.1 201 Created");
      out.println("Content-Type: application/json");
      out.println();
      out.println(gson.toJson(response));
    }
  }

  public void updateUserDataById(Socket clientSocket, BufferedReader in, String requestLine)
      throws IOException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
      String requestBody = HttpUtils.getRequestBody(in);
      int userId = UserUtils.getUserId(requestLine);

      if (userId == -1 || userId > data.getArrayLength()) {
        HttpUtils.handle404ErrorResponse(clientSocket);
        return;
      }

      User updates = gson.fromJson(requestBody, User.class);
      User existingUser = data.getUserDataByIndex(userId - 1);

      if (updates.getName() != null && !updates.getName().trim().isEmpty()) {
        existingUser.setName(updates.getName());
      }
      if (updates.getJob() != null && !updates.getJob().trim().isEmpty()) {
        existingUser.setJob(updates.getJob());
      }

      JsonObject response = new JsonObject();
      response.addProperty("status", "success");
      response.addProperty("message", "Data updated successfully");

      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: application/json");
      out.println();
      out.println(gson.toJson(response));
    }
  }

  public void deleteUserDataById(Socket clientSocket, String requestLine) throws IOException {
    System.out.println("delete request received!");
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
      int userId = UserUtils.getUserId(requestLine);

      if (userId == -1 || userId > data.getArrayLength()) {
        HttpUtils.handle404ErrorResponse(clientSocket);
        return;
      }

      data.removeData(userId - 1);

      JsonObject response = new JsonObject();
      response.addProperty("status", "success");
      response.addProperty("message", "Data deleted successfully");

      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: application/json");
      out.println();
      out.println(gson.toJson(response));
    }
  }
}
