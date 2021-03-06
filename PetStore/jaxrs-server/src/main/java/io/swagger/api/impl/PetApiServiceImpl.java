package io.swagger.api.impl;


import io.swagger.api.*;
import io.swagger.model.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import io.swagger.model.ModelApiResponse;
import io.swagger.model.Pet;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;

import io.swagger.api.NotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-11-23T09:44:41.219Z")
public class PetApiServiceImpl extends PetApiService {
	HashMap<String,Pet> petMap=new HashMap<String,Pet>();
	
	
    @Override
    public Response addPet(Pet body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	String id = body.getId().toString();
    	String responseString = "";
    	if(!petMap.containsKey(id))
    	{	
    		petMap.put(id, body);
    	    if(null != body && null != body.getPhotoUrls())
    	    {
    	    	String base64EncodedString = body.getPhotoUrls().get(0);
    	    	
    	    	byte[] arraybase64Decoded = Base64.decodeBase64(base64EncodedString);
    	    	
    	    	try 
    	    	{
    	    	   String uploadPath =  "C:\\Uploads\\"+ body.getTags().get(0).getName();
    	    	   FileOutputStream fos = new FileOutputStream(uploadPath);
    	    	   fos.write(arraybase64Decoded);
    	    	   fos.close();
    	    	}
    	    	catch(IOException ex)
    	    	{
    	    		
    	    	}
    	    }
    		responseString = "Added pet with id "+ id;
    		System.out.println(responseString);
    		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, responseString)).build();
    	}
    	else
    	{
    		responseString = "Pet with id "+ id + " already exists";
    		System.out.println(responseString);
    		return Response.status(409).entity(new ApiResponseMessage(ApiResponseMessage.INFO, responseString)).build();
    	}
    }
    
    @Override
	public Response uploadFile(InputStream fileInputStream, FormDataContentDisposition fileDetail, SecurityContext securityContext) 
	{
		try 
		{
			  if(null == fileInputStream)
			  {
				  System.out.println("Input Stream is null");
				  return Response.status(500).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, 
						                                                    "Input Stream is not initialized")).build(); 
			  }
			  
			  //String fileName = getFileNameFromStream(fileInputStream);
			  //String uploadPath =  "C:\\Uploads\\"+ fileName;
			  String fileName = fileDetail.getFileName();
			  String uploadPath =  "C:\\Uploads\\"+fileName;
			  
			  
	 		  FileOutputStream fos = new FileOutputStream(uploadPath);
	 		  BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);			  
	 		  int BUF_SIZE = 4046;
	 		  byte[] byteArrBuffer = new byte[BUF_SIZE];
	 		  int bytesRead = 0;
	 		  
	 		  while((bytesRead = bufferedInputStream.read(byteArrBuffer, 0, BUF_SIZE)) != -1)
	 		  {
				 fos.write(byteArrBuffer, 0, bytesRead);  			  
	 		  }
			  
	 		  fileInputStream.close();
	 		  fos.close();
	 		  System.out.println("File "+ fileName +" uploaded successfully.");
	 	}
		catch(IOException ex)
		{
			return Response.status(500).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Error occured while saving file")).build();
		}
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "File uploaded successfully")).build();
	}
    
    String getFileNameFromStream(InputStream fileInputStream) throws IOException
    {                
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
    	String fileName = "";
    	String line = "";
    	//read file Name from Multipart header                
	    while ((line = bufferedReader.readLine()) != null) 
		{
	    	if(line.startsWith("Content-Disposition:"))
	    	{
	    		String [] tokens = line.split(";");
	    		String fileNameToken = tokens[tokens.length - 1];
	    		String [] subTokens = fileNameToken.split("=");
	    		fileName = subTokens[subTokens.length-1].replaceAll("\"", "");		
	    		//break;
	        }
	    	if(line.equals(""))
	    	{
	    		break;
	    	}
		}	    
        System.out.println("After reading stream header file Name is "+ fileName);      
    	return fileName;
    }
    
    @Override
    public Response deletePet(Long petId, String apiKey, SecurityContext securityContext) throws NotFoundException {

    	String strPetId = petId.toString();
    	if(!petMap.containsKey(strPetId))
        {	
         	System.out.println("Pet with id "+ petId.toString() +" not found.");
         	return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.INFO, "Pet with id "+ petId+ " not found." )).build();
        }
    	
    	petMap.remove(strPetId);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "Pet with id "+ petId +" deleted successfully")).build();
    }
    @Override
    public Response findPetsByStatus( @NotNull List<String> status, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response findPetsByTags( @NotNull List<String> tags, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getPetById(Long petId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	String strPetId = petId.toString();
        if(!petMap.containsKey(strPetId))
        {	
        	System.out.println("Pet with id "+ strPetId +" not found.");
        	return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.INFO, "Pet with id "+ strPetId + " not found." )).build();
        }
        Pet pet = petMap.get(strPetId);
        ObjectMapper mapper = new ObjectMapper();
        String responseString = "";
		try {
			responseString = mapper.writeValueAsString(pet);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Pet found");
        System.out.println(responseString);
        
        return Response.ok(responseString, MediaType.APPLICATION_JSON).build();
    }
    @Override
    public Response updatePet(Pet body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updatePetWithForm(Long petId, String name, String status, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response uploadFile(Long petId, String additionalMetadata, InputStream fileInputStream, FormDataContentDisposition fileDetail, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
