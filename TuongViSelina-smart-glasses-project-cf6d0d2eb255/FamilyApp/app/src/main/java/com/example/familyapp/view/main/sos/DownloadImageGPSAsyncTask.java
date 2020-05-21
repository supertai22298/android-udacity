package com.example.familyapp.view.main.sos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.familyapp.model.GPS;
import com.example.familyapp.model.Glasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;

//import androidx.exifinterface.media.ExifInterface;


public class DownloadImageGPSAsyncTask extends AsyncTask<String, Integer, GPSImage> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String url;
    private DownloadImageListener listener;

    public DownloadImageGPSAsyncTask(Context context, String url, DownloadImageListener listener) {
        this.context = context;
        this.url = url;
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected GPSImage doInBackground(String... strings) {
        try {
            GPSImage g = downloadBitmap(url);
            return g;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(GPSImage data) {
        super.onPostExecute(data);
        listener.onSuccess(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private GPSImage downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            GPSImage image = getImageDetail(inputStream);
            return image;
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    /**
     * Method that fetch info from your image file path.
     *
     * @param inputStream file path of image
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private GPSImage getImageDetail(InputStream inputStream) {
        GPSImage gpsImage = new GPSImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
                baos.flush();
            }
            gpsImage.setBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray())));
            ExifInterface exifInterface = new ExifInterface(new ByteArrayInputStream(baos.toByteArray()));
            String tagGpsLatitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String tagGpsLongitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

            try {
                if (tagGpsLatitude != null && tagGpsLongitude != null) {
                    double lat = convertToDegree(tagGpsLatitude);
                    double lng = convertToDegree(tagGpsLongitude);
                    gpsImage.setLat(lat);
                    gpsImage.setLng(lng);
                    gpsImage.setAddress(getAddress(lat, lng));
                }
            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpsImage;
    }

    private Bitmap getBitmap(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
                baos.flush();
            }
            inputStream.close();
            return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && addresses.size()>0){
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown location";
    }


    /**
     * Method to convert degrees, minutes, seconds to Decimal coordinates
     *
     * @param stringDMS string of degrees, minutes, seconds
     * @return decimal coordinate
     */
    private double convertToDegree(String stringDMS) {
        double result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        double FloatS = S0 / S1;
        result = FloatD + (FloatM / 60) + (FloatS / 3600);
        return result;
    }

    public interface DownloadImageListener {
        void onSuccess(GPSImage data);
    }
}
