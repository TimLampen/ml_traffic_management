import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class TrafficScrapper {


    private static String[] URLS = new String[]{
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaDenmanEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaDenmanWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/DenmanGeorgiaSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/georgiaE.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/georgiaW.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/PenderEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleNorth_Georgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaEast_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSouth_Georgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaWest_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleNorth_Robson.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/RobsonEast_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSouth_Robson.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/RobsonWest_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSmitheNorth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSmitheEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSmitheSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSmitheWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleNorth_Nelson_South.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/NelsonEast_Granville_South.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSouth_Nelson_South.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/NelsonWest_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HowePacificNorth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HowePacificEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HowePacificSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HowePacificWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNorth_Georgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaEast_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbySouth_Georgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaWest_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNorth_Robson.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/RobsonEast_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbySouth_Robson.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/RobsonWest_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNelsonNorth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNelsonEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNelsonSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNelsonWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardDrakeNorth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardDrakeEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardDrakeSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardDrakeWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardPacificNorth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardPacificEast.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardBridgeSouth.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/BurrardPacificWest.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbyNorth_Pacific.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/PacificEast_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HornbySouth_Pacific.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/PacificWest_Hornby.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HoweNorthGeorgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaEastHowe.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/HoweSouthGeorgia.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GeorgiaWestHowe.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleNorth_Dunsmuir.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/DunsmuirEast_Granville.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/GranvilleSouth_Dunsmuir.jpg",
            "https://trafficcams.vancouver.ca/cameraimages/DunsmuirWest_Granville.jpg",
    };

    private HashMap<String, BufferedImage> previouslyDownloaded = new HashMap<>();
    AzureManager azure;

    public TrafficScrapper(AzureManager azure) throws IOException, InterruptedException {
            this.azure = azure;
            start();
    }


    public void start() throws IOException, InterruptedException {

        while(true){
            int newlyDownloaded = 0;
            for(String url : URLS){
                BufferedImage image = downloadImage(url);
                if(!previouslyDownloaded.containsKey(url) || !bufferedImagesEqual(previouslyDownloaded.get(url), image)){
                    previouslyDownloaded.put(url, image);

                    ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    ImageIO.write(image,"jpg", bas);
                    byte[] bytes = bas.toByteArray();
                    InputStream is = new ByteArrayInputStream(bytes);


                    String containerName = url.split("cameraimages/")[1].replace("_", "").replace(".jpg", "").toLowerCase();


                    this.azure.uploadImage(containerName, System.currentTimeMillis() + "", is, bytes.length);
                    newlyDownloaded++;
                }
            }

            if(newlyDownloaded > 0)
                System.out.println("Downloaded " + newlyDownloaded + " new images.");

            Thread.sleep(1000*60*10);//10min refresh
        }
    }

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private BufferedImage downloadImage(String strurl) throws IOException {
        URL url = new URL(strurl);
        BufferedImage img = ImageIO.read(url);
        return img;
    }

}
