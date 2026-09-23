package com.server.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UsersTest {

  @Test
  public void testAddNewData() {
    Users users = new Users();
    int initialSize = users.getArrayLength();

    User newUser = new User("Test User", "Tester");
    users.addNewData(newUser);

    assertEquals(initialSize + 1, users.getArrayLength(), "Users array size should increase by 1");
    assertEquals(
        "Test User", users.getUserDataByIndex(initialSize).getName(), "New user name should match");
    assertEquals(
        "Tester", users.getUserDataByIndex(initialSize).getJob(), "New user job should match");
  }

  @Test
  public void testRemoveData() {
    Users users = new Users();
    int initialSize = users.getArrayLength();

    users.removeData(0); // Remove the first user

    assertEquals(initialSize - 1, users.getArrayLength(), "Users array size should decrease by 1");
  }
}
