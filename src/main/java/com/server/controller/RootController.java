package com.server.controller;

import com.google.gson.Gson;
import com.server.model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/** Controller for handling requests to the root endpoint. */
public class RootController {
  private Users data;
  private Gson gson = new Gson();

  /**
   * Constructs the root controller.
   *
   * @param data The user data model.
   */
  public RootController(Users data) {
    this.data = data;
  }

  /**
   * Handles GET requests to the root endpoint.
   *
   * @param clientSocket The client socket.
   * @throws IOException If an I/O error occurs.
   */
  public void handleGetRequest(Socket clientSocket) throws IOException {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("status", "UP");
      responseBody.put("message", "Welcome to Basic Web Server API");
      responseBody.put("totalUsers", data.getArrayLength());

      // HTTP response header
      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: application/json");
      out.println();

      // HTTP response body
      out.println(gson.toJson(responseBody));
    }
  }
}
