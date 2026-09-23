package com.server;

import com.server.controller.RootController;
import com.server.controller.UsersController;
import com.server.model.Users;
import com.server.utils.HttpUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  private static Users data = new Users();
  private static RootController rootController = new RootController(data);
  private static UsersController usersController = new UsersController(data);
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

  public static void main(String[] args) throws Exception {
    try (ServerSocket serverSocket = new ServerSocket(8000)) {
      System.out.println("Server is running on http://localhost:" + serverSocket.getLocalPort());

      while (true) {
        try {
          Socket clientSocket = serverSocket.accept();
          threadPool.submit(() -> handleRequest(clientSocket));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void handleRequest(Socket clientSocket) {
    try (clientSocket;
        BufferedReader in =
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String requestLine = in.readLine();

      // client HTTP request line log
      if (requestLine == null) return;
      System.out.println(requestLine);

      // Routing
      if (requestLine.startsWith("GET / ")) {
        rootController.handleGetRequest(clientSocket);
      } else if (requestLine.startsWith("GET /users ")) {
        usersController.getAllUsers(clientSocket);
      } else if (requestLine.startsWith("POST /users ")) {
        usersController.postUserData(clientSocket, in);
      } else if (requestLine.startsWith("GET /users/")) {
        usersController.getUserDataById(clientSocket, requestLine);
      } else if (requestLine.startsWith("PATCH /users/")) {
        usersController.updateUserDataById(clientSocket, in, requestLine);
      } else if (requestLine.startsWith("DELETE /users/")) {
        usersController.deleteUserDataById(clientSocket, requestLine);
      } else if (requestLine.startsWith("GET /search?")) {
        usersController.getUserDataByQuery(clientSocket, requestLine);
      } else {
        HttpUtils.handle404ErrorResponse(clientSocket);
      }
    } catch (Exception e) {
      HttpUtils.handleServerErrorResponse(clientSocket, e);
    }
  }
}
