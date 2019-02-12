package com.p6.cli.app;

import com.p6.utils.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;

/**
 * Entry point.
 */
public class App {
  /**
   * A test script.
   * @param args System args
   */
  public static void main(String[] args) {

    LoggingHelper.configureLoggingFramework(Level.ALL);
    Logger logger = LogManager.getLogger();

    String line = null;
    logger.info(System.getProperty("user.dir"));

    try {
      // FileReader reads text files in the default encoding.
      FileReader fileReader = new FileReader("test.txt");

      // Always wrap FileReader in BufferedReader.
      BufferedReader bufferedReader =  new BufferedReader(fileReader);

      while((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
      }

      // Always close files.
      bufferedReader.close();
    }
    catch(FileNotFoundException ex) {
      System.out.println(
        "Unable to open file '");
    }

    catch(IOException ex) {
      System.out.println(
        "Error reading file '");
      // Or we could just do this:
      // ex.printStackTrace();
    }



  }
}

