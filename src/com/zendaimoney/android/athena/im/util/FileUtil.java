package com.zendaimoney.android.athena.im.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zendaimoney.android.athena.AppLog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;


public class FileUtil {

	/**
	 * <p>将文件转成base64 字符串</p>
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File  file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		inputFile.read(buffer);
        inputFile.close();
        return  Base64.encodeToString(buffer, Base64.DEFAULT);
	}
	/**
	 * <p>将base64字符解码保存文件</p>
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static String decoderBase64File(String base64Code,String targetPath) throws Exception {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
		AppLog.v("TAG", "path:" + targetPath);
		return targetPath;
	}
//	/**
//	 * <p>将base64字符保存文本文件</p>
//	 * @param base64Code
//	 * @param targetPath
//	 * @throws Exception
//	 */
//	public static void toFile(String base64Code,String targetPath) throws Exception {
//		byte[] buffer = base64Code.getBytes();
//		FileOutputStream out = new FileOutputStream(targetPath);
//		out.write(buffer);
//		out.close();
//	}
	public static void test() {
		try {
			String base64Code =encodeBase64File("D:\\1.jpg");
			System.out.println(base64Code);
			decoderBase64File(base64Code, "D:\\2.jpg");
//			toFile(base64Code, "D:\\three.txt");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  图片压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  

	/**
	 * 将图片缩小 并转化正Base64字串
	 * @param path 图片路径
	 * @return
	 * @throws Exception
	 */
	public static String img2Base64(String path) throws Exception {
		
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(path,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(path, newOpts);  
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos); //200
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bitmap.recycle();
//		return Base64.encodeToString(buffer, Base64.DEFAULT);
		return result;
	}
	
	/**
	 * 将Base64字串转化为Bitmap
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static Bitmap base642Img(String content) throws Exception {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(content, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}


//	/**
//	 * * 在SD卡上创建文件 * @param _filepath 文件名称 * @param _folder 文件夹名称
//	 */
//	public static File createFileOnSD(String _folder, String _file) {
//		File file = new File(_folder + _file);
//		File fileFolder = new File(_folder);
//		if (!fileFolder.exists())
//			fileFolder.mkdirs();
//		// 这里不做文件是否存在的判断
//		try {
//			file.createNewFile();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return file;
//	}
	
    /**
     * 创建文件及文件夹
     * @param _folder
     * @param _file
     * @return
     */
	public static String createFileOnSD(String _folder, String _file) {
		
		String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
		_folder = sdpath +_folder;
				
		File file = new File(_folder + _file);
		File fileFolder = new File(_folder);
		if (!fileFolder.exists())
			fileFolder.mkdirs();
		// 这里不做文件是否存在的判断
		try {
			file.createNewFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
}