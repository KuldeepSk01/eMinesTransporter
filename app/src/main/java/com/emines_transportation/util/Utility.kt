package com.emines_transportation.util

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.emines_transportation.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


private var isConnect = false
private var isGpEnable = false
var IsValidBuyerField = false
private lateinit var context: Context

fun isLocationEnable() = isLocationEnable
private var isLocationEnable = false

fun setLocationPermission(isEnableLocationEnable: Boolean) {
    isLocationEnable = isEnableLocationEnable
}

fun setContext(c: Context) {
    context = c
}


fun isConnectionAvailable() = isConnect
fun setConnection(isAvailable: Boolean) {
    isConnect = isAvailable
}

fun mToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun mLog(msg: String) {
    Log.d("EminesEmployee", msg)
    println(msg)

}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key, T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}


fun View.showKeyboard() {
    requestFocusFromTouch()
    this.postDelayed({
        val im = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.showSoftInput(this, InputMethodManager.HIDE_NOT_ALWAYS)
    }, 2000)
}

fun View.hideKeyboard() {
    val im = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}


fun getTimeFormat(hh: Int, mm: Int): String {
    val sdf = SimpleDateFormat("hh:mm")
    val mTime: Calendar = Calendar.getInstance()
    mTime.set(Calendar.HOUR, hh)
    mTime.set(Calendar.MINUTE, mm)
    return sdf.format(mTime.time)
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val currentDate = Calendar.getInstance().time
    return sdf.format(currentDate)
}

fun getDateFormat(day: Int, month: Int, year: Int): String {
    //  val sdf = SimpleDateFormat("EEE dd MMM yyyy")
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val mTime: Calendar = Calendar.getInstance()
    mTime.set(Calendar.DAY_OF_MONTH, day)
    mTime.set(Calendar.MONTH, month)
    mTime.set(Calendar.YEAR, year)
    return sdf.format(mTime.time)
}

fun getDateFormat(date:String): String {
      val sdf = SimpleDateFormat("dd-mm-yyyy")
    val parseDate = sdf.parse(date)
    val mTime: Calendar = Calendar.getInstance()
    mTime.time = parseDate

    val sdf1 = SimpleDateFormat("dd MMM yyy")
    return sdf1.format(parseDate.time)
}


fun removeAllFragmentsFromFragment(fragment: Fragment) {
    val fm = fragment.requireActivity().supportFragmentManager
    for (i in 0 until fm.backStackEntryCount) {
        fm.popBackStack()
    }
    // onBackPress()
}


//this is used to get path from uri

fun getRealPathFromURI(uri: Uri, activity: Activity): String? {
    val returnCursor: Cursor = activity.applicationContext.contentResolver.query(
        uri, null, null, null, null
    )!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val file: File = File(activity.applicationContext.filesDir, name)
    try {
        val inputStream: InputStream =
            activity.applicationContext.contentResolver.openInputStream(uri)!!
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable = inputStream.available()
        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream.read(buffers).also { read = it } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream.close()
        outputStream.close()
        Log.e("File Path", "Path " + file.absolutePath)
    } catch (e: java.lang.Exception) {
        Log.e("Exception", e.message!!)
    }
    return file.absolutePath
}


fun getBytes(uri: Uri?, activity: Activity): ByteArray? {
    try {
        val inputStream = activity
            .applicationContext.contentResolver.openInputStream(uri!!)
        return readBytes(inputStream!!)
    } catch (ex: java.lang.Exception) {
        Log.d("MyTag", "could not get byte stream")
    }
    return null
}

@Throws(IOException::class)
private fun readBytes(inputStream: InputStream): ByteArray? {
    // this dynamically extends to take the bytes you read
    val byteBuffer = ByteArrayOutputStream()
    // this is storage overwritten on each iteration with bytes
    val bufferSize = 1 * 1024 * 1024
    val buffer = ByteArray(bufferSize)
    // we need to know how may bytes were read to write them to the byteBuffer
    var len = 0
    while (inputStream.read(buffer).also { len = it } != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    // and then we can return your byte array.
    return byteBuffer.toByteArray()
}

interface OnDropDownListener {
    fun onDropDownClick(item: String)
}

fun dropDownPopup(
    context: Context, isBelow: View, menuLayout: Int, listener: OnDropDownListener
): PopupMenu {
    val popup = PopupMenu(context, isBelow)
    popup.menuInflater.inflate(menuLayout, popup.menu)
    popup.setOnMenuItemClickListener { item ->
        listener.onDropDownClick(item.title.toString())
        true
    }
    return popup
}


fun getCalling(context: Context, mobile: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$mobile")
    context.startActivity(intent)
}

fun verifyStoragePermission(activity: Activity): Boolean {
   /* // Check if we have write permission
    val permission = ActivityCompat.checkSelfPermission(
        activity, Manifest.permission.READ_EXTERNAL_STORAGE,
    )*/
     return if (ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED) {
        // We don't have permission so prompt the user
        /* ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            Constants.RequestCodes.IMAGE_PICK_CODE
        )*/
         true
    }else {false}

}

fun verifyCameraPermission(activity: Activity): Boolean {
    // Check if we have write permission
    return if (ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED) {
        true
    }else { false}
}

fun requestStoragePermission(activity: Activity) {
    Dexter.withContext(activity).withPermissions(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    ).withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            // check if all permissions are granted
            if (report.areAllPermissionsGranted()) {
                //Toast.makeText(this, "Permissi", Toast.LENGTH_SHORT).show()
            }
            // check for permanent denial of any permission
            if (report.isAnyPermissionPermanentlyDenied) {
                // show alert dialog navigating to Settings
                // showSettingsDialog(activity)
            }
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: List<PermissionRequest>, token: PermissionToken
        ) {
            token.continuePermissionRequest()
        }
    }).withErrorListener {
        mToast("Error occurred!")
    }.onSameThread().check()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestStoragePermissionAbove32(activity: Activity) {
    Dexter.withContext(activity).withPermissions(
        Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.CAMERA
    ).withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            // check if all permissions are granted
            if (report.areAllPermissionsGranted()) {
                //Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show()
            }
            // check for permanent denial of any permission
            if (report.isAnyPermissionPermanentlyDenied) {
                // show alert dialog navigating to Settings
                //showSettingsDialog()
            }
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: List<PermissionRequest>, token: PermissionToken
        ) {
            token.continuePermissionRequest()
        }
    }).withErrorListener {
        mToast("Error occurred!")
    }.onSameThread().check()
}

 fun showSettingsDialog(activity: Activity) {
    val builder = android.app.AlertDialog.Builder(activity)
    builder.setTitle("Need Permissions")
    builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
    builder.setPositiveButton(
        "GOTO SETTINGS"
    ) { dialog, which ->
        dialog.cancel()
        openSettings(activity)
    }
    builder.setNegativeButton(
        "Cancel"
    ) { dialog, which -> dialog.cancel() }
    builder.show()
}

// navigating user to app settings
private fun openSettings(activity: Activity) {

  //  val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) //this is open location setting
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS) //this is open location setting
    intent.data = Uri.parse("package:" + activity.packageName)
    activity.startActivityForResult(intent, 101)
}



//this is used to get path from compressing bitmap
fun compressImageFilePath(bm: Bitmap, context: Context): String {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val file = File(context.filesDir, timeStamp + ".png")
    if (file.exists()) {
        file.delete()
    }
    try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
        bm.compress(Bitmap.CompressFormat.JPEG, 80, fos)

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            assert(fos != null)
            fos!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return file.path
}



//this is used to get Uri from bitmap
fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        inContext.contentResolver, inImage,
        "Title", null
    )
    return Uri.parse(path)
}


//this is used to calculate size of file
fun checkFileSize(file: File): Long? {
    val fileSizeInBytes = file.length()
    val fileSizeInKB = fileSizeInBytes / 1024
    return fileSizeInKB / 1024
}



 fun downloadFileFromUrl(url: String) {
    try {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val file = File(url)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val uri1 = Uri.parse(url)
        var request = DownloadManager.Request(uri1)
        val storagePath = Environment.DIRECTORY_DOWNLOADS
        request.setDestinationInExternalPublicDir(
            storagePath,
            "$timeStamp${file.name}"
        )
        request.setTitle(file.name)
        request.setDescription(context.getString(R.string.downloading))
        downloadManager.enqueue(request)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun checkIsImageExtensions(url: String): Boolean {
    return if (url.contains(".png") || url.contains(".jpg") || url.contains(
            ".jpeg"
        )
    ){
        true
    }else{
        false
    }
}


