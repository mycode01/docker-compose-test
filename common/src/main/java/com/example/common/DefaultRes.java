package com.example.common;

public class DefaultRes {
  private final int code;
  private final String message;

  public static DefaultRes Response(int code, String message) {
    return new DefaultRes(code, message);
  }

  private DefaultRes(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() { return code; }
  public String getMessage() { return message; }

}
