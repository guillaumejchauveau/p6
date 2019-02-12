package com.p6.parser;

public class Parser {


  public String[] parser(String rule) {
    String tab1[] = new String[3];
    tab1[0] = "";
    int j = 0;
    rule.trim();
    for (int i = 0; i < rule.length(); i++) {
      if (rule.charAt(i) == ',' || rule.charAt(i) == ':' || rule.charAt(i) == ' ')
      {
        j += 1;
        tab1[j] = "";
      }
      else
        tab1[j] = tab1[j] + rule.charAt(i);
    }
    return tab1;
  }
}
