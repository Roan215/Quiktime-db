//SessionManager.java
package com.ust.util;

public class SessionManager {
 private static String currentUserId;
 private static String currentUserType;
 private static String currentUserName;

 public static void setCurrentUser(String userId, String userType, String userName) {
     currentUserId = userId;
     currentUserType = userType;
     currentUserName = userName;
 }

 public static String getCurrentUserId() {
     return currentUserId;
 }

 public static String getCurrentUserType() {
     return currentUserType;
 }

 public static String getCurrentUserName() {
     return currentUserName;
 }

 public static void clearSession() {
     currentUserId = null;
     currentUserType = null;
     currentUserName = null;
 }
}
