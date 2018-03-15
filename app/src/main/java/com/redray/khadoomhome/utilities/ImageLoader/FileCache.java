package com.redray.khadoomhome.utilities.ImageLoader;

import android.content.Context;

import java.io.File;

class FileCache {

	private File cacheDir;


    FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Khadoom_Home");
        }else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

	File getFile(String url) {
		String filename = String.valueOf(url.hashCode());
		// String filename = URLEncoder.encode(url);

		return new File(cacheDir, filename);

	}

//	public void clear() {
//		File[] files = cacheDir.listFiles();
//		if (files == null)
//			return;
//		for (File f : files)
//			f.delete();
//	}

}