package Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {
  public static void initLogger(Logger logger, String name) {
    try {
      FileHandler fileHandler = new FileHandler(name);
      Formatter customFormatter = new Formatter() {
        @Override
        public String format(LogRecord recordValue) {
          SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss.SSS");
          String msg = String.format("[%s] %s - %s%n",
              df.format(new Date(recordValue.getMillis())),
              recordValue.getLevel().getName(),
              recordValue.getMessage());
          return msg;
        }
      };
      fileHandler.setFormatter(customFormatter);
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

