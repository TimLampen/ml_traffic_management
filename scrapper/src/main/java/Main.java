import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        AzureManager azure = new AzureManager();
        new TrafficScrapper(azure);
    }
}
