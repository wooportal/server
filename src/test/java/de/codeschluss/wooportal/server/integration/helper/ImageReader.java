package de.codeschluss.wooportal.server.integration.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class ImageReader {
  
  private String base64Pic;
  private final TestConfiguration testConfig;
  
  
  public ImageReader(TestConfiguration testConfig) {
    this.testConfig = testConfig;
  }

  /**
   * Gets the base 64 picture.
   *
   * @return the base 64 picture
   */
  public String getBase64Picture() {
    return base64Pic != null
        ? base64Pic
        : readFile();
  }

  private String readFile() {
    try {
      BufferedImage image = ImageIO.read(new File(testConfig.getPicturePath()));
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ImageIO.write(image, getType(), bos);         
      this.base64Pic = Base64Utils.encodeToString(bos.toByteArray());
      return this.base64Pic;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public String getMimeType() {
    return "image/" + getType();
  }
  
  public String getType() {
    return testConfig.getPicturePath().split("\\.")[1];
  }
}
