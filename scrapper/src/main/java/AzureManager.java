import com.azure.core.http.HttpResponse;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AzureManager {
    BlobServiceClient serviceClient;
    public AzureManager() {
        connect();
    }

    private void connect(){
        this.serviceClient = new BlobServiceClientBuilder()
                .endpoint(System.getenv("AZURE_CONTAINER"))
                .sasToken(System.getenv("AZURE_SASTOKEN"))
                .buildClient();
    }

    public void uploadImage(String containerName, String blobName, InputStream is, int length){
        containerName = containerName.toLowerCase();
        BlobContainerClient containerClient = this.serviceClient.getBlobContainerClient(containerName);

        if(!containerClient.exists())
            containerClient.create();



        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(is, length);
    }

    public BufferedImage downloadImage(String containerName, String blobName) throws IOException {
        containerName = containerName.toLowerCase();
        BlobContainerClient containerClient = this.serviceClient.getBlobContainerClient(containerName);
        if(!containerClient.exists()){
            System.out.println(containerName + " is not a valid container!");
            return null;
        }

        BlobClient blobClient = containerClient.getBlobClient(blobName);
        if(!blobClient.exists()){
            System.out.println(blobName + " is not a valid blob name!");
            return null;
        }

        BinaryData content = blobClient.downloadContent();

        return ImageIO.read(content.toStream());
    }
}
