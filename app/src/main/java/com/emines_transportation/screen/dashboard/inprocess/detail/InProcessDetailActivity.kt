package com.emines_transportation.screen.dashboard.inprocess.detail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.emines_transportation.CustomDialogs
import com.emines_transportation.CustomDialogs.showImageDialog
import com.emines_transportation.CustomDialogs.showWebViewDialog
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.databinding.ActivityInProcessDetailBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.checkFileSize
import com.emines_transportation.util.checkIsImageExtensions
import com.emines_transportation.util.compressImageFilePath
import com.emines_transportation.util.downloadFileFromUrl
import com.emines_transportation.util.getBytes
import com.emines_transportation.util.getRealPathFromURI
import com.emines_transportation.util.mLog
import com.emines_transportation.util.mToast
import com.emines_transportation.util.serializable
import com.emines_transportation.util.showSettingsDialog
import com.emines_transportation.util.verifyCameraPermission
import com.emines_transportation.util.verifyStoragePermission
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.core.component.inject
import java.io.File


class InProcessDetailActivity : BaseActivity() {
    private lateinit var mBind: ActivityInProcessDetailBinding
    private val mViewModel: PickupViewModel by inject()
    private val userDetail = mPref.getUserDetail()

    private var weightReceiptImage: String? = null
    private var grnImage: String? = null

    private lateinit var transporterOrderResponse: TransporterOrderResponse

    override val layoutId: Int
        get() = R.layout.activity_in_process_detail

    override fun onCreateInit(binding: ViewDataBinding?) {
        mBind = binding as ActivityInProcessDetailBinding
        transporterOrderResponse = intent.getBundleExtra(Constants.DefaultConstant.BUNDLE_KEY)
            ?.serializable<TransporterOrderResponse>(Constants.DefaultConstant.MODEL_DETAIL)!!
        setDetail(transporterOrderResponse)


        mBind.apply {
            toolBarInProcessDetail.apply {
                tvToolBarTitle.text = String.format(
                    "%s %s%s",
                    getString(R.string.order_id_text),getString(R.string.defultId),
                    transporterOrderResponse.id.toString()
                )
                ivToolBarBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

            when (transporterOrderResponse.status) {
                getString(R.string.reached) -> {
                    rlBottomBtn.visibility = View.GONE
                    rlAcceptedInProcessDetail.visibility = View.VISIBLE
                }

                else -> {
                    rlBottomBtn.visibility = View.VISIBLE
                }
            }

            bottomBtn.apply {
                tvFirstBtn.apply {
                    text = getString(R.string.back_cap)
                    setOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }

                tvSecondBtn.apply {
                    text = getString(R.string.reached)
                    setOnClickListener {
                        hitChangeOrderStatus(getString(R.string.reached))
                    }
                }

            }
            ivUploadWeightReceiptInProcessDetail.setOnClickListener {
                try {
                    CustomDialogs.showBottomSheetDialog(this@InProcessDetailActivity,
                        object : CustomDialogs.OnBottomSheetClickListener {
                            override fun onImageClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val gallery = Intent(MediaStore.ACTION_PICK_IMAGES)
                                    mLog("PERMISSION_GRANTED 13")
                                    startActivityForWeightReceipt.launch(gallery)
                                } else {
                                    if (verifyStoragePermission(this@InProcessDetailActivity)){
                                        val gallery = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                                        )
                                        mLog("setInitialSetup: PERMISSION_GRANTED below 13")
                                        startActivityForWeightReceipt.launch(gallery)
                                    }
                                    else {
                                        showSettingsDialog(this@InProcessDetailActivity)
                                        //mToast(getString(R.string.please_enable_required_permission))
                                    }

                                }
                                dialog.dismiss()

                            }

                            override fun onFileClick(dialog: BottomSheetDialog) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                    pdfIntent.type = "application/pdf"
                                    pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                    startActivityForPDF.launch(
                                        Intent.createChooser(
                                            pdfIntent,
                                            "Select PDF file"
                                        )
                                    )

                                } else {
                                    if (verifyStoragePermission(this@InProcessDetailActivity)){
                                        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                        pdfIntent.type = "application/pdf"
                                        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                        startActivityForPDF.launch(
                                            Intent.createChooser(
                                                pdfIntent,
                                                "Select PDF file"
                                            )
                                        )
                                    }
                                    else {
                                        showSettingsDialog(this@InProcessDetailActivity)
                                        //mToast(getString(R.string.please_enable_required_permission))
                                    }

                                }

                                dialog.dismiss()
                            }

                            override fun onCameraClick(dialog: BottomSheetDialog) {
                                if (verifyCameraPermission(this@InProcessDetailActivity)){
                                    startActivityCamera1.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                }
                                else {
                                    showSettingsDialog(this@InProcessDetailActivity)
                                    //mToast(getString(R.string.please_enable_required_permission))
                                }
                                dialog.dismiss()
                            }

                        })

                } catch (e: Exception) {
                    mLog("Select or Capture Exception")
                }
            }
            ivUploadGRNInProcessDetail.setOnClickListener {
                try {
                    CustomDialogs.showBottomSheetDialog(this@InProcessDetailActivity,
                        object : CustomDialogs.OnBottomSheetClickListener {
                            override fun onImageClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val gallery = Intent(MediaStore.ACTION_PICK_IMAGES)
                                    mLog("PERMISSION_GRANTED 13")
                                    startActivityForGRN.launch(gallery)

                                } else {
                                    if (verifyStoragePermission(this@InProcessDetailActivity)){
                                        val gallery = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                                        )
                                        mLog("setInitialSetup: PERMISSION_GRANTED below 13")
                                        startActivityForGRN.launch(gallery)
                                    }
                                    else {
                                        showSettingsDialog(this@InProcessDetailActivity)

                                        //mToast(getString(R.string.please_enable_required_permission))
                                    }
                                }

                                dialog.dismiss()

                            }

                            override fun onFileClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                    pdfIntent.type = "application/pdf"
                                    pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                    startActivityForGRNPDF.launch(
                                        Intent.createChooser(
                                            pdfIntent,
                                            "Select PDF file"
                                        )
                                    )

                                } else {
                                    if (verifyStoragePermission(this@InProcessDetailActivity)){
                                        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                        pdfIntent.type = "application/pdf"
                                        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                        startActivityForGRNPDF.launch(
                                            Intent.createChooser(
                                                pdfIntent,
                                                "Select PDF file"
                                            )
                                        )
                                    }
                                    else {
                                        showSettingsDialog(this@InProcessDetailActivity)
                                       // mToast(getString(R.string.please_enable_required_permission))
                                    }
                                }

                                dialog.dismiss()
                            }

                            override fun onCameraClick(dialog: BottomSheetDialog) {
                                if (verifyCameraPermission(this@InProcessDetailActivity)){
                                    startActivityCamera2.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                }
                                else {
                                    showSettingsDialog(this@InProcessDetailActivity)
                                    //mToast(getString(R.string.please_enable_required_permission))
                                }

                              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    startActivityCamera2.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                } else {
                                    if (verifyCameraPermission(this@InProcessDetailActivity)){
                                        startActivityCamera2.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                    }
                                    else {
                                        showSettingsDialog(this@InProcessDetailActivity)
                                        //mToast(getString(R.string.please_enable_required_permission))
                                    }
                                }*/
                                dialog.dismiss()

                            }

                        })

                } catch (e: Exception) {
                    mLog("Select or Capture Exception")
                }
            }


            tvDeliveredBtn.setOnClickListener {
                if (weightReceiptImage == null) {
                    mToast(getString(R.string.please_upload_weight_receipt))
                    return@setOnClickListener
                }
                if (grnImage == null) {
                    mToast(getString(R.string.please_upload_grn_receipt))
                    return@setOnClickListener
                }

                mViewModel.hitDeliveredOrderByTransporterApi(
                    userDetail.id,
                    transporterOrderResponse.id,
                    etWeightReceiptNoInProcessDetail.text.toString(),
                    etGRNNoInProcessDetail.text.toString(),
                    weightReceiptImage,
                    grnImage
                )
                mViewModel.getDeliveredOrderByTransporterResponse()
                    .observe(this@InProcessDetailActivity, deliveredOrderResponseObserver)
            }

        }
    }


    private val deliveredOrderResponseObserver: Observer<ApiResponse<SuccessMsgResponse>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    mToast(it.data?.message.toString())
                    finish()
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }


    private val changerOrderStatusObserver: Observer<ApiResponse<SuccessMsgResponse>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    mBind.rlBottomBtn.visibility = View.GONE
                    mBind.rlAcceptedInProcessDetail.visibility = View.VISIBLE
                    mToast(it.data?.message.toString())
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }


    private fun hitChangeOrderStatus(status: String) {
        mViewModel.hitChangeOrderStatusByTransporterApi(
            mPref.getUserDetail().id,
            transporterOrderResponse.id,
            status
        )
        mViewModel.getChangeOrderStatusByTransporterResponse()
            .observe(this@InProcessDetailActivity, changerOrderStatusObserver)
    }


    private val startActivityForWeightReceipt =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val uri = result.data?.data!!
                    mLog("Selected Image data $uri")
                    mBind.ivUploadWeightReceiptInProcessDetail.apply {
                        visibility = View.VISIBLE
                        setImageURI(uri)
                    }
                    weightReceiptImage =
                        getRealPathFromURI(uri, this@InProcessDetailActivity).toString()
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                weightReceiptImage = null
            }

        }

    private val startActivityForGRN =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val uri = result.data?.data!!
                    mLog("Selected Image data $uri")
                    mBind.ivUploadGRNInProcessDetail.apply {
                        visibility = View.VISIBLE
                        setImageURI(uri)
                    }
                    grnImage =
                        getRealPathFromURI(uri, this@InProcessDetailActivity).toString()
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                grnImage = null
            }

        }

    private fun setDetail(it: TransporterOrderResponse) {
        mBind.apply {

            tvBReqStatus.apply {
                when (it.status) {
                    context.getString(R.string.assigned) -> {
                        text = String.format("%s, %s", it.status, " ${it.assigned_date}")
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }

                    context.getString(R.string.new_order) -> {
                        text = it.status
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }

                    context.getString(R.string.reached) -> {
                        text = String.format("%s, %s", it.status, " ${it.reached_date}")
                        setTextColor(context.getColor(R.color.order_accepted_color))
                    }

                    context.getString(R.string.accepted) -> {
                        text = String.format("%s, %s", it.status, " ${it.accepte_date}")
                        setTextColor(context.getColor(R.color.order_accepted_color))
                    }

                    context.getString(R.string.delivered) -> {
                        text = String.format("%s, %s", it.status, " ${it.delivery_date}")
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }

                    context.getString(R.string.picked_up) -> {
                        text = String.format("%s, %s", it.status, " ${it.pickup_date}")
                        setTextColor(context.getColor(R.color.order_picked_color))
                    }

                    context.getString(R.string.denied) -> {
                        text = String.format("%s, %s", it.status, " ${it.denied_date}")
                        setTextColor(context.getColor(R.color.order_cancel_color))
                    }

                    context.getString(R.string.in_process) -> {
                        setTextColor(context.getColor(R.color.order_picked_color))
                    }
                }
            }
            tvWeightOne.text = it.pickup_weight // String.format("%s %s","Picked Up Weight",)
            tvEWayNumber.text = it.bill_number // String.format("%s %s","eWay Bill Number",)


            ivViewWReceiptInProcessDetail.apply {

                if (checkIsImageExtensions(it.weight_receipt)){
                    Glide.with(this@InProcessDetailActivity).load(it.weight_receipt).into(this)
                }
                else{
                    Glide.with(this@InProcessDetailActivity)
                        .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                        .into(this)
                }

                setOnClickListener { click ->
                    if (it.weight_receipt.isEmpty()) {
                        mToast(context.getString(R.string.file_not_uploaded))
                        return@setOnClickListener
                    }

                    if (checkIsImageExtensions(it.weight_receipt)){
                        showImageDialog(
                            this@InProcessDetailActivity,
                            it.weight_receipt,
                            object : CustomDialogs.OnShowImageDialogListener {
                                override fun onClickDownload(dialog: Dialog, url: String) {
                                    downloadFileFromUrl(url)
                                   // dialog.dismiss()
                                }
                            }).show()
                    }else
                    {
                        showWebViewDialog(
                            this@InProcessDetailActivity,
                            it.weight_receipt,
                            object : CustomDialogs.OnShowImageDialogListener {
                                override fun onClickDownload(dialog: Dialog, url: String) {
                                    downloadFileFromUrl(url)
                                   // dialog.dismiss()
                                }
                            }).show()
                    }
                }
            }
            ivViewEWayBillInProcessDetail.apply {
                if (checkIsImageExtensions(it.eway_bill)){
                    Glide.with(this@InProcessDetailActivity).load(it.eway_bill).into(this)
                }
                else{
                    Glide.with(this@InProcessDetailActivity)
                        .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                        .into(this)
                }
                setOnClickListener { click ->
                    if (it.eway_bill.isEmpty()) {
                        mToast(context.getString(R.string.file_not_uploaded))
                        return@setOnClickListener
                    }

                    if (checkIsImageExtensions(it.eway_bill)){
                        showImageDialog(
                            this@InProcessDetailActivity,
                            it.eway_bill,
                            object : CustomDialogs.OnShowImageDialogListener {
                                override fun onClickDownload(dialog: Dialog, url: String) {
                                    downloadFileFromUrl(url)
                                    dialog.dismiss()
                                }
                            }).show()
                    }else
                    {
                        showWebViewDialog(
                            this@InProcessDetailActivity,
                            it.eway_bill,
                            object : CustomDialogs.OnShowImageDialogListener {
                                override fun onClickDownload(dialog: Dialog, url: String) {
                                    downloadFileFromUrl(url)
                                    dialog.dismiss()
                                }
                            }).show()
                    }



                    //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.eway_bill)))
                }
            }

            /* Glide.with(this@InProcessDetailActivity).load(it.weight_receipt).into(ivViewWReceiptInProcessDetail)
             Glide.with(this@InProcessDetailActivity).load(it.eway_bill).into(ivViewEWayBillInProcessDetail)
 */
            tvPONoInProcessDetail.text = it.po_no
            tvPickupLocation.text = it.destination
            tvDropLocationInProcessDetail.text = it.address
            tvItemPickupAssignedDate.text =
                String.format("%s %s", "Assigned Date :", it.assigned_date)
            tvDeliveryDate.text = if (it.delivery_date.isNullOrEmpty()) it.estimated_delivery_date else it.delivery_date

           // tvDeliveryDate.text = it.delivery_date
            tvPickupDate.text = it.pickup_date
            tvStatusAcceptedInProcessDetail.text =
                String.format("%s, %s", getString(R.string.reached), it.reached_date)

            /*
                        tvBReqStatus.apply {
                            text = it.status
                            when (it.status) {
                                getString(R.string.new_order), getString(R.string.assigned) -> {
                                    this.setTextColor(getColor(R.color.order_complete_color))
                                }

                                getString(R.string.accepted), context.getString(R.string.reached) -> {
                                    this.setTextColor(getColor(R.color.order_accepted_color))
                                }

                                getString(R.string.delivered) -> {
                                    this.setTextColor(getColor(R.color.order_complete_color))
                                }

                                getString(R.string.picked_up) -> {
                                    this.setTextColor(getColor(R.color.order_picked_color))
                                }

                                getString(R.string.denied) -> {
                                    this.setTextColor(getColor(R.color.order_cancel_color))
                                }

                                getString(R.string.in_process) -> {
                                    this.setTextColor(getColor(R.color.order_picked_color))
                                }
                            }

                        }
            */




            tvGoods1.text = it.one_requested_product
            tvPQ1.text = it.one_purchased_quantity
            tvUnit1.text = it.one_unit_of_quantity
            /* tvRate1.text = it.one_unit_rate
             tvGst1.text = it.one_product_gst
             tvTAmount1.text = it.one_total_unit_price_excl_gst
             tvGstAmount1.text = it.one_total_unit_price_incl_gst*/

            tvGoods2.text = it.two_requested_product
            tvPQ2.text = it.two_purchased_quantity
            tvUnit2.text = it.two_unit_of_quantity
            /*  tvRate2.text = it.two_unit_rate
              tvGst2.text = it.two_product_gst
              tvTAmount2.text = it.two_total_unit_price_excl_gst
              tvGstAmount2.text = it.two_total_unit_price_incl_gst*/

            tvGoods3.text = it.three_requested_product
            tvPQ3.text = it.three_purchased_quantity
            tvUnit3.text = it.three_unit_of_quantity
            /* tvRate3.text = it.three_unit_rate
             tvGst3.text = it.three_product_gst
             tvTAmount3.text = it.three_total_unit_price_excl_gst
             tvGstAmount3.text = it.three_total_unit_price_incl_gst
 */
            tvGoods4.text = it.four_requested_product
            tvPQ4.text = it.four_purchased_quantity
            tvUnit4.text = it.four_unit_of_quantity
            /* tvRate4.text = it.four_unit_rate
             tvGst4.text = it.four_product_gst
             tvTAmount4.text = it.four_total_unit_price_excl_gst
             tvGstAmount4.text = it.four_total_unit_price_incl_gst
 */

            if (it.two_requested_product.isEmpty()) {
                llcGoods2.visibility = View.GONE
            } else {
                llcGoods2.visibility = View.VISIBLE
            }

            if (it.three_requested_product.isEmpty()) {
                llcGoods3.visibility = View.GONE
            } else {
                llcGoods3.visibility = View.VISIBLE
            }

            if (it.four_requested_product.isEmpty()) {
                llcGoods4.visibility = View.GONE
            } else {
                llcGoods4.visibility = View.VISIBLE
            }


        }
    }

    private val startActivityCamera1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val b = data?.extras?.get("data") as Bitmap
                    weightReceiptImage = compressImageFilePath(b, this@InProcessDetailActivity)
                    if (checkFileSize(File(weightReceiptImage))!! < 2.toDouble()) {
                        mBind.ivUploadWeightReceiptInProcessDetail.setImageBitmap(b)
                        mLog("Image path : $weightReceiptImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                weightReceiptImage = null
                mLog("Capture Image Exception ${e.message}")
            }


        }


    private val startActivityCamera2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val b = data?.extras?.get("data") as Bitmap
                    grnImage = compressImageFilePath(b, this@InProcessDetailActivity)
                    if (checkFileSize(File(grnImage))!! < 2.toDouble()) {
                        mBind.ivUploadGRNInProcessDetail.setImageBitmap(b)
                        mLog("Image path : $grnImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }

                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                grnImage = null
            }

        }


    private val startActivityForPDF =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri = result.data?.data!!
                    val path =
                        getRealPathFromURI(imageUri, this@InProcessDetailActivity)
                    /*val bytes = getBytes(imageUri, this)
                    val pdfSizeInKb = bytes!!.size / 1024.toDouble()
                    val pdfSizeInMB = pdfSizeInKb / 1024*/

                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        weightReceiptImage = path
                        Glide.with(this@InProcessDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(mBind.ivUploadWeightReceiptInProcessDetail)
                        mLog("pdf path : $weightReceiptImage")
                        //mBind.ivUploadWeightReceiptInProcessDetail.setImageURI(imageUri)
                    } else {
                        mToast(getString(R.string.pdf_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                weightReceiptImage = null
            }

        }

    private val startActivityForGRNPDF =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri = result.data?.data!!
                    val bytes = getBytes(imageUri, this)
                    val pdfSizeInKb = bytes!!.size / 1024.toDouble()
                    val pdfSizeInMB = pdfSizeInKb / 1024
                    if (pdfSizeInMB < 2.toDouble()) {
                        grnImage =
                            getRealPathFromURI(imageUri, this@InProcessDetailActivity)
                        mLog("pdf path : $weightReceiptImage")
                        Glide.with(this@InProcessDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(mBind.ivUploadGRNInProcessDetail)

                    } else {
                        mToast(getString(R.string.pdf_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                grnImage = null
            }

        }


   /* fun checkIsImageExtensions(url: String): Boolean {
        return if (url.contains(".png") || url.contains(".jpg") || url.contains(
                ".jpeg"
            )
        ){
            true
        }else{
            false
        }
    }*/

}