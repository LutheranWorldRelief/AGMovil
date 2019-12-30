package kronos.cacaomovil.common;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Window;
import android.widget.ProgressBar;

import kronos.cacaomovil.R;
import kronos.cacaomovil.activities.ListadoGuias;
import kronos.cacaomovil.database.ArchivosDB;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DescargarArchivos {
    static int downloadedSize = 0;
    static int totalsize;
    static float per = 0;
    ProgressBar simpleProgressBar;
    Dialog dialogPop;

    public  DescargarArchivos(String url,String nombreArchivo,String file_type,String idGuia,int posicion,Activity actividad){
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(actividad)
                .setDownloadConcurrentLimit(3)
                .build();

        Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        String file = "/storage/emulated/0/filesGuias/"+nombreArchivo+fecha+"."+file_type;

        System.out.println(file);
        System.out.println(url);
        System.out.println(fecha);

        final Request request = new Request(url, file);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        //request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");


        FetchListener fetchListener = new FetchListener() {
            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {

            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
                if (request.getId() == download.getId()) {
                    //showDownloadInList(download);
                }
            }

            @Override
            public void onCompleted(@NotNull Download download) {
                fetch.close();
            }

            @Override
            public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
                if (request.getId() == download.getId()) {
                    //updateDownload(download, etaInMilliSeconds);
                }
                int progress = download.getProgress();
                System.out.println(progress);
                simpleProgressBar.setProgress(progress);

                if(progress==100){
                    ArchivosDB ArchivosData = new ArchivosDB(actividad);
                    ArchivosData.insertar(nombreArchivo+fecha+"."+file_type,idGuia);
                    dialogPop.dismiss();
                    ListadoGuias.recargar.INSTANCE.recargaFila(posicion,file_type);
                }
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        fetch.addListener(fetchListener);
        popPersonalizado(actividad);
        fetch.enqueue(request, request1 -> {
            //Request enqueued for download
        }, error -> {
            //Error while enqueuing download
        });
    }

    public void popPersonalizado(Activity context) {


        dialogPop = new Dialog(context);
        dialogPop.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPop.setContentView(R.layout.pop_descarga);
        dialogPop.setCancelable(false);

        simpleProgressBar = dialogPop.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setProgress(0);

        dialogPop.show();
    }


   /* public static File downloadFile(String dwnload_file_path) {
        File file = null;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String name = "";
        name = dwnload_file_path.substring(dwnload_file_path.lastIndexOf('/'), dwnload_file_path.length());//Create file name by picking download file name from URL


        //crear carpeta
        File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "filesGuias");
        nuevaCarpeta.mkdirs();

        file = new File(nuevaCarpeta, name);
        System.out.println(file.getAbsoluteFile());
        if (file.exists()) {
            return file;
        } else {


            try {
                System.out.println(dwnload_file_path);
                URL url = new URL("http://agd.codecastle.com.sv/uploads/upload_file/file_upload/2/CC-20181107-Planes_de_Trabajo-proyectos_LWR.pdf");
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                // connect
                urlConnection.connect();

                FileOutputStream fileOutput = new FileOutputStream(file);

                // Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                // this is the total size of the file which we are
                // downloading
                totalsize = urlConnection.getContentLength();
                System.out.println("Iniciando descarga del PDF...");


                // create a buffer...
                byte[] buffer = new byte[1024 * 1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalsize) * 100;
                }
                // close the output stream when complete //
                fileOutput.close();
                System.out.println("Descarga completada.");

            } catch (final MalformedURLException e) {
                System.out.println("Some error occured. Press back and try again."+e.getMessage());
            } catch (final IOException e) {
                System.out.println("Some error occured. Press back and try again."+e);
            } catch (final Exception e) {
                System.out.println("Failed to download image. Please check your internet connection."+e);
            }
            return file;

        }
    }*/


}
