package com.zch.tooldemos.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {
	public static String TEMP_DIR;
	public static String TEMP_IMG_DIR;

	public static List<String> extensions = new ArrayList<String>();//后缀集合

	static {
		// TODO:

		TEMP_DIR = Environment.getExternalStorageDirectory() + "/FireMonitor/temp/";
		TEMP_IMG_DIR = Environment.getExternalStorageDirectory()
				+ "/FireMonitor/image/";
		createSdDir(TEMP_DIR);
		createSdDir(TEMP_IMG_DIR);

		initExtentsion();
	}

	public static String RequestCamera(Context context, int code, String name) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(TEMP_IMG_DIR, name+System.currentTimeMillis() + ".png");
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra("return-data", true);
		((Activity) context).startActivityForResult(intent, code);
		return file.getPath();
	}

	public static File createSdDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static String compressFile(Bitmap bm, String picName) {
		File f = null;
		try {
			f = new File(TEMP_IMG_DIR, picName);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 30, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.getAbsolutePath();
	}

	public static Bitmap createImageThumbnail(String filePath) {
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(TEMP_IMG_DIR + fileName);
		file.isFile();
		return file.exists();
	}

	/**
	 * 删除缓存文件
	 */
	public static void clearFile(String dir) {
		File file = new File(dir);
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				clearFile(files[i].getPath());
			} else {
				files[i].delete();
			}
		}
	}

	public static String readFromLocal(String fileName, String encode)
			throws Exception {
		StringBuilder strFile = new StringBuilder();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					inputStream, encode));
			String readString = new String();
			while ((readString = buf.readLine()) != null) {
				strFile.append(readString);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			strFile.append(e.getMessage().toString());
		} catch (IOException e) {
			e.printStackTrace();
			strFile.append(e.getMessage().toString());
		} finally {
			if (null != inputStream) {
				inputStream.close();
			}
		}
		return strFile.toString();
	}

	/**
	 * 根据后缀过滤除了缓存目录以外的所有文件
	 * 
	 * @param path
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<File> listFiles(String path, ArrayList<String> exts) {
		if (exts == null || exts.size() == 0)
			return Collections.EMPTY_LIST;
		LinkedList<File> list = new LinkedList<File>();
		ArrayList<File> listPath = new ArrayList<File>();
		File dir = new File(path);
		if (!dir.exists()) {
			return Collections.EMPTY_LIST;
		}
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				list.add(file[i]);
			} else {
				for (String ext : exts) {
					if (file[i].getName().endsWith(ext)) {
						listPath.add(file[i]);
					}
				}
			}
		}

		File tmp;
		while (!list.isEmpty()) {
			tmp = list.removeFirst();
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null) {
					continue;
				}
				for (int i = 0; i < file.length; i++) {
					if (file[i].isDirectory()) {
						list.add(file[i]);
					} else {
						for (String ext : exts) {
							if (file[i].getName().endsWith(ext)) {
								listPath.add(file[i]);
							}
						}
					}
				}
			}
		}
		return listPath;
	}

	/**
	 * 将拍照上传的照片更新到相册
	 * @param file
	 * @param context
     */
	public static void sendBroad(File file, Context context){
		try{
			MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),
					file.getName(),null);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	/**
	 * 根据url地址获取对应的文件类型
	 * @param urlstr
	 * @return image/jpeg, text/html等格式
	 * @throws Exception
	 */
	public static String getFileType(String urlstr) throws Exception{
		BufferedInputStream bis = null;
		HttpURLConnection urlconnection = null;
		URL url = null;
		url = new URL(urlstr);
		urlconnection = (HttpURLConnection) url.openConnection();
		urlconnection.connect();
		bis = new BufferedInputStream(urlconnection.getInputStream());
		System.out.println("file type:"+HttpURLConnection.guessContentTypeFromStream(bis));
		return HttpURLConnection.guessContentTypeFromStream(bis);
	}

	/**
	 *
	 * @param linkUrl
	 * @return 通过链接的后缀名判断文件的类型
	 */
	public static String getTypeByExtenssion(String linkUrl) {
		if (linkUrl == null)
			return null;
		linkUrl=linkUrl.toLowerCase();
		for (String ext : extensions) {
			if (linkUrl.endsWith(ext)) {
				return ext;
			}
		}
		return null;
	}

	//自定义需要匹配链接的文件后缀，不满足的可以自行添加
	private static void initExtentsion() {
		extensions.add(".pdf");
		extensions.add(".doc");
		extensions.add(".txt");
		extensions.add(".xls");
		extensions.add(".html");
		extensions.add(".rtf");
		extensions.add(".mht");
		extensions.add(".rar");
		extensions.add(".ppt");
		extensions.add(".jpg");
		extensions.add(".docx");
		extensions.add(".xlsx");
		extensions.add(".pptx");
		extensions.add(".eml");
		extensions.add(".zip");
		extensions.add(".docm");
		extensions.add(".xlsm");
		extensions.add(".xlsb");
		extensions.add(".dotx");
		extensions.add(".csv");
		extensions.add(".mp4");
	}

}
