package com.blackstrawai.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.blackstrawai.ApplicationException;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class ReadCaptcha {
	
	private Logger logger = Logger.getLogger(ReadCaptcha.class);

	/**
	 * Returns text in image located in the specified file directory.
	 * 
	 * @param filePath
	 * @return text read from the image.
	 * @throws IOException
	 * @throws ApplicationException 
	 */
	public String detectTextFromImage(String filePath) throws IOException, ApplicationException {
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(createAndAddRequests(filePath));
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return textFromResponses(responses);
		} catch (Exception e) {
			throw new ApplicationException("Could not read text from image");
		}
	}
	
	/**
	 * Converts image into byte array.
	 * 
	 * @param filePath
	 * @return byte array of image in filePath
	 * @throws IOException
	 */
	private byte[] readFileAsByteArray(String filePath) throws IOException {
		BufferedImage bImage = ImageIO.read(new File(filePath));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bImage, "png", bos);
		return bos.toByteArray();
	}
	
	/**
	 * Creates a list of requests to be executed by ImageAnnotatorClient.
	 * 
	 * @param filePath
	 * @return list of requests
	 * @throws IOException
	 */
	private List<AnnotateImageRequest> createAndAddRequests(String filePath) throws IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		requests.add(createRequest(filePath));
		return requests;
	}
	
	/**
	 * Creates a request to extract features of an image for the purposes of text detection.
	 * 
	 * @param filePath
	 * @return AnnotateImageRequest instance
	 * @throws IOException
	 */
	private AnnotateImageRequest createRequest(String filePath) throws IOException {
		Image img = Image.newBuilder().setContent(ByteString.copyFrom(readFileAsByteArray(filePath))).build();
		Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
		return AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	}
	
	/**
	 * reads the text from generated responses.
	 * 
	 * @param responses
	 * @return text read from the image
	 * @throws ApplicationException
	 */
	private String textFromResponses(List<AnnotateImageResponse> responses) throws ApplicationException {
		String text = null;
		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				logger.error(res.getError().getMessage());
				throw new ApplicationException("Could not read text from image");
			}
			for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
				text = annotation.getDescription();
			}
		}
		return text;
	}
}
