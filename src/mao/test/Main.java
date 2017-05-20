package mao.test;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class Main {

	public static void main(String[] args) {
		try {
			FileSeekableStream ss = new FileSeekableStream("D:/file_test/exchange_ticket.tif");
			ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
			int count = dec.getNumPages();
			TIFFEncodeParam param = new TIFFEncodeParam();
			param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
			param.setLittleEndian(false); // Intel
			System.out.println("This TIF has " + count + " image(s)");
			
			for (int i = 0; i < count; i++) {
	            RenderedImage page = dec.decodeAsRenderedImage(i);
	            BufferedImage image = PlanarImage.wrapRenderedImage(page).getAsBufferedImage();
	            
	            if(i == (count-1)) {
	            	Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			           hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			           
			           BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
		           		
			           Result qrResult = new MultiFormatReader().decode(binaryBitmap);
			           System.out.println(qrResult.getText());
	            }
	            
	            File outputfile = new File("D:/file_test/saved("+i+").png");
				ImageIO.write(image, "png", outputfile);
	        }
			
			
			    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
