
import org.apache.commons.codec.binary.Base64;

import io.swagger.client.model.*;
import io.swagger.client.model.Pet.StatusEnum;
import io.swagger.client.api.PetApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class PetApiExample {

    public static void main(String[] args) throws IOException {
    	
        try 
        {	
        	
        	int choice = 3;
        	long petId =0;
        	String petName = "Tiger";
        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        	do
        	{
	            System.out.println("**** Upload File To Server ****");
	            System.out.println("1. Upload using Json.");
	            System.out.println("2. Upload using Multipart form data.");
	            System.out.println("3. Exit");
	            System.out.print("Enter your choice:");
	            String userInput = bufferedReader.readLine();
	            choice = Integer.parseInt(userInput);
	            String filePath = "";
	            switch(choice)
	            {
	            	case 1:
	            		System.out.print("Enter full path of file to upload:");
	            		filePath = bufferedReader.readLine();
	            		++petId;	            		
	            		SendBlobUsingJson(filePath, petId, petName + petId);
	            		break;
	            		
	            	case 2:
	            		System.out.print("Enter full path of file to upload:");
	            		filePath = bufferedReader.readLine();
	            		SendBlobUsingMultiPartFormData(filePath);
	            		break;
	            	case 3:
	            		System.out.println("You have chosen exit!");
	            		break;
	            		
	            	default:
	            		System.out.print("Invalid choice.");
	            		break;
	            }
        	}while(choice != 3);
        	System.out.print("Exiting.....");
        } 
        catch(NumberFormatException nfe)
        {
            System.err.println("Invalid Format!");
        }
        catch (Exception e) 
        {
            System.err.println("Exception when calling PetApi#addPet");
            e.printStackTrace();
        }
    }
    static PetApi GetPetApiObject()
    {
    	PetApi apiInstance = new PetApi();
    	//
    	//use setBasePath when server isn't hosted on localhost 
    	//
    	
    	//apiInstance.getApiClient().setBasePath("http://169.254.4.117:8080/v2");
        System.out.println("Basepath is "+ apiInstance.getApiClient().getBasePath());
        return apiInstance;
    }
    static void SendBlobUsingJson(String filePath, 
    		                      long petId ,
    		                      String petName) throws IOException 
    {
    	PetApi apiInstance = GetPetApiObject();
        Pet petObject = new Pet(); 
        petObject.setId(petId);
        petObject.setName(petName);
        Category category = new Category();
        category.setId(petId);
        category.name("Cat Species");
        petObject.setCategory(category);
        byte[] byteArrOriginalContent = Files.readAllBytes(Paths.get(filePath));
        byte[] base64encodedBytes = Base64.encodeBase64(byteArrOriginalContent);
        String content = new String(base64encodedBytes, "UTF-8");
        //add file content encoded with base64 as a PhotoUrl String 
        petObject.addPhotoUrlsItem(content);
        
        Tag tag = new Tag();
        tag.setId(petId); 
        int lastIndexOfSlash = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(lastIndexOfSlash + 1, filePath.length());
      
        //set tag as File Name
        tag.setName(fileName);
        List<Tag> tagList = new ArrayList<Tag>();
        tagList.add(tag);
        petObject.setTags(tagList);
        petObject.setStatus(StatusEnum.AVAILABLE);
        try 
        {
        	apiInstance.addPet(petObject);
        	Pet result = apiInstance.getPetById(petId);
            System.out.println(result.toString());
        }
        catch (Exception e) 
        {
            System.err.println("Exception when calling PetApi#addPet");
            e.printStackTrace();
        }
    }
    
    static void SendBlobUsingMultiPartFormData(String filePath)throws IOException 
    {
    	PetApi apiInstance = GetPetApiObject();    
       
        try 
        {
        	File fileObject = new File(filePath);
        	apiInstance.checkFileUpload(fileObject);
            System.out.println("File " + filePath +" uploaded successfully.");
        }
        catch (Exception e) 
        {
            System.err.println("Exception when calling PetApi#checkFileUpload");
            e.printStackTrace();
        }
    }
}
