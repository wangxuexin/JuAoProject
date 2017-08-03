package com.fizz.JuAoProject.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class FileUtils {

	/**
	 * 把图片压缩到200K
	 * 
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	/**
	 * 把图片压缩到200K
	 * 
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	public static File compressFile(String oldpath, String newPath) {
		Bitmap compressBitmap = FileUtils.decodeFile(oldpath);
		Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		newBitmap.compress(CompressFormat.PNG, 100, os);
		byte[] bytes = os.toByteArray();
		
		File file = null ;
		try {
			file = FileUtils.getFileFromBytes(bytes, newPath);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(newBitmap != null ){
				if(!newBitmap.isRecycled()){
					newBitmap.recycle();
				}
				newBitmap  = null;
			}
			if(compressBitmap != null ){
				if(!compressBitmap.isRecycled()){
					compressBitmap.recycle();
				}
				compressBitmap  = null;
			}
		}
		return file;
	}
	
	private static Bitmap ratingImage(String filePath,Bitmap bitmap){
		int degree = readPictureDegree(filePath);
		return rotaingImageView(degree, bitmap);
	}
	
	/**
	 *  旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
		Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
        		bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 图片压缩
	 * 
	 * @param fPath
	 * @return
	 */
	public static Bitmap decodeFile(String fPath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inDither = false; // Disable Dithering mode
		opts.inPurgeable = true; // Tell to gc that whether it needs free
		opts.inInputShareable = true; // Which kind of reference will be used to
		BitmapFactory.decodeFile(fPath, opts);
		final int REQUIRED_SIZE = 200;
		int scale = 1;
		if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
			final int heightRatio = Math.round((float) opts.outHeight
					/ (float) REQUIRED_SIZE);
			final int widthRatio = Math.round((float) opts.outWidth
					/ (float) REQUIRED_SIZE);
			scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
		}
		Log.i("scale", "scal ="+ scale);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(Config.ARGB_8888, false);
		return bm;
	}
	
	
	
	/**
	 * 创建目录
	 * @param path
	 */
	public static void setMkdir(String path)
	{
		File file = new File(path);
		if(!file.exists())
		{
			file.mkdirs();
			Log.e("file", "目录不存在  创建目录    ");
		}else{
			Log.e("file", "目录存在");
		}
	}
	
	/**
	 * 获取目录名称
	 * @param url
	 * @return FileName
	 */
	public static String getFileName(String url)
	{
		int lastIndexStart = url.lastIndexOf("/");
		if(lastIndexStart!=-1)
		{
			return url.substring(lastIndexStart+1, url.length());
		}else{
			return null;
		}
	}
	
	/**
	 * 删除该目录下的文件
	 * 
	 * @param path
	 */
	public static void delFile(String path) {
		if (!TextUtils.isEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public static File getRoot(Context context) {
		String dir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		} else {
			dir = context.getCacheDir().getAbsolutePath();
		}
		File root = new File(dir, "FinancialSystem");
//        root 是文件夹
		if (!root.exists()) {
			root.mkdirs();
		}
		return root;
	}


	public static void shareMultiplePictureToTimeLine(Context context, ArrayList<Uri> imageUris, String description) throws Exception {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//		intent.setAction(Intent.ACTION_SEND);

		intent.setType("image/*");
		intent.putExtra("Kdescription", description);//微信朋友圈专用

//		ArrayList<Uri> imageUris = new ArrayList<>();
//		for (File f : files) {
//			imageUris.add(Uri.fromFile(f));
//		}
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//		intent.putExtra(Intent.EXTRA_STREAM, imageUris.get(0));
		intent.putExtra(Intent.EXTRA_TEXT, description);
		context.startActivity(Intent.createChooser(intent, null));
	}

//	public static final File saveImageToSdCard(ImageView image) {
//		boolean success = false;
////      F.makeLog(image.toString());
//		// Encode the file as a PNG image.
//		File file = null;
//		try {
////          file = createImageFile();
//			file = createStableImageFile();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//		Bitmap bitmap = drawable.getBitmap();
//		FileOutputStream outStream;
//		try {
//			outStream = new FileOutputStream(file);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
////           100 to keep full quality of the image
//			outStream.flush();
//			outStream.close();
//			success = true;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (success) {
////          Toast.makeText(getApplicationContext(), "Image saved with success",
////                  Toast.LENGTH_LONG).show();
//			return file;
//		} else {
//			return null;
//		}
//	}

	/*public static File createStableImageFile() throws IOException {
		IMAGE_NAME++;
		String imageFileName = System.currentTimeMillis() + ".jpg";
		File storageDir = AppData.getContext().getExternalCacheDir();
//        File image = File.createTempFile(
//            imageFileName,  *//* prefix *//*
//            ".jpg",         *//* suffix *//*
//            storageDir      *//* directory *//*
//        );
		File image = new File(storageDir, imageFileName);

		// Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}*/
}
