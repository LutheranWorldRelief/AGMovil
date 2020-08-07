package kronos.cacaomovil.common

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.view.Window
import android.widget.ProgressBar
import android.widget.Toast
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.Fetch.Impl.getInstance
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import kronos.cacaomovil.R
import java.io.File

class DescargarImagen(url: String?, nombreArchivo: String, actividad: Activity?) {
    var simpleProgressBar: ProgressBar? = null
    var dialogPop: Dialog? = null
    var context:Activity? = null

    fun popPersonalizado(context: Activity?) {
        this.context = context
        dialogPop = Dialog(context!!)
        dialogPop!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogPop!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogPop!!.setContentView(R.layout.pop_descarga)
        dialogPop!!.setCancelable(false)
        simpleProgressBar = dialogPop!!.findViewById(R.id.simpleProgressBar)
        simpleProgressBar!!.setProgress(0)
        dialogPop!!.show()
    }


    init {
        val fetchConfiguration = FetchConfiguration.Builder(actividad!!)
                .setDownloadConcurrentLimit(3)
                .build()
        val fetch = getInstance(fetchConfiguration)
        val file = "/storage/emulated/0/CacaoMovil/$nombreArchivo"
        val request = Request(url!!, file)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        //request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
        val fetchListener: FetchListener = object : FetchListener {
            override fun onWaitingNetwork(download: Download) {}
            override fun onError(download: Download, error: Error, throwable: Throwable?) {}
            override fun onAdded(download: Download) {}
            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                if (request.id == download.id) {
                    //showDownloadInList(download);
                }
            }

            override fun onCompleted(download: Download) {
                fetch.close()
                dialogPop!!.dismiss()

                var nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "CacaoMovil");
                nuevaCarpeta.mkdirs();
                var file = File(nuevaCarpeta, nombreArchivo)
                if (file.exists()) {
                    var path = Uri.fromFile(file)
                    try {
                        var intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(path, "image/*")
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context!!.startActivity(intent);
                        //finish();
                    } catch (e: ActivityNotFoundException) {
                        System.out.println(e.localizedMessage)
                    }
                } else {
                    var toast1 = Toast.makeText(context,"Lo sentimos, el archivo no existe. Inténtelo descargándolo de nuevo", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
                if (request.id == download.id) {
                    //updateDownload(download, etaInMilliSeconds);
                }
                val progress = download.progress
                println("progress $progress")
                simpleProgressBar!!.progress = progress
            }

            override fun onPaused(download: Download) {}
            override fun onResumed(download: Download) {}
           // override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, i: Int) {}
            override fun onStarted(download: Download, list: List<DownloadBlock>, i: Int) {}
            override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, i: Int) {}
            override fun onCancelled(download: Download) {}
            override fun onRemoved(download: Download) {}
            override fun onDeleted(download: Download) {}
        }
        fetch.addListener(fetchListener)
        popPersonalizado(actividad)
        fetch.enqueue(request, Func { request1: Request? -> }, Func { error: Error? -> })
    }
}