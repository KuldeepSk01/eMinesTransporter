package com.emines_transportation.screen.dashboard.delivered.detail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.databinding.ActivityOrderDetailBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.checkFileSize
import com.emines_transportation.util.checkIsImageExtensions
import com.emines_transportation.util.compressImageFilePath
import com.emines_transportation.util.downloadFileFromUrl
import com.emines_transportation.util.getRealPathFromURI
import com.emines_transportation.util.isConnectionAvailable
import com.emines_transportation.util.mLog
import com.emines_transportation.util.mToast
import com.emines_transportation.util.serializable
import com.emines_transportation.util.showSettingsDialog
import com.emines_transportation.util.verifyCameraPermission
import com.emines_transportation.util.verifyStoragePermission
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.core.component.inject
import java.io.File

class DeliveredDetailActivity : BaseActivity() {
    private lateinit var mBind: ActivityOrderDetailBinding
    private lateinit var transporterOrderResponse: TransporterOrderResponse
    private var billReceipt: String? = null

    private val mViewModel: PickupViewModel by inject()


    override val layoutId: Int
        get() = R.layout.activity_order_detail

    override fun onCreateInit(binding: ViewDataBinding?) {
        mBind = binding as ActivityOrderDetailBinding
        transporterOrderResponse = intent.getBundleExtra(Constants.DefaultConstant.BUNDLE_KEY)
            ?.serializable<TransporterOrderResponse>(Constants.DefaultConstant.MODEL_DETAIL)!!
        setDetail(transporterOrderResponse)
        mBind.apply {
            toolBarDeliveredDetail.apply {
                ivToolBarBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
                tvToolBarTitle.text = String.format(
                    "%s %s%s",
                    getString(R.string.order_id_text), getString(R.string.defultId),
                    transporterOrderResponse.id.toString()
                )
            }

            bottomButtons.apply {
                tvFirstBtn.apply {
                    text = getString(R.string.back_cap)
                    setOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
                tvSecondBtn.visibility = View.GONE
            }


            tvUploadBillReceipt.setOnClickListener {
                //upload bill receipt
                try {
                    CustomDialogs.showBottomSheetDialog(this@DeliveredDetailActivity,
                        object : CustomDialogs.OnBottomSheetClickListener {
                            override fun onImageClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val gallery = Intent(MediaStore.ACTION_PICK_IMAGES)
                                    mLog("PERMISSION_GRANTED 13")
                                    imageLauncher.launch(gallery)

                                } else {
                                    if (verifyStoragePermission(this@DeliveredDetailActivity)) {
                                        val gallery = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                                        )
                                        mLog("setInitialSetup: PERMISSION_GRANTED below 13")
                                        imageLauncher.launch(gallery)
                                    } else {
                                        showSettingsDialog(this@DeliveredDetailActivity)
                                    }
                                }

                                dialog.dismiss()

                            }

                            override fun onFileClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                    pdfIntent.type = "application/pdf"
                                    pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                    pdfLauncher.launch(
                                        Intent.createChooser(
                                            pdfIntent,
                                            "Select PDF file"
                                        )
                                    )

                                } else {
                                    if (verifyStoragePermission(this@DeliveredDetailActivity)) {
                                        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                        pdfIntent.type = "application/pdf"
                                        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                        pdfLauncher.launch(
                                            Intent.createChooser(
                                                pdfIntent,
                                                "Select PDF file"
                                            )
                                        )
                                    } else {
                                        showSettingsDialog(this@DeliveredDetailActivity)
                                        // mToast(getString(R.string.please_enable_required_permission))
                                    }
                                }

                                dialog.dismiss()
                            }

                            override fun onCameraClick(dialog: BottomSheetDialog) {
                                if (verifyCameraPermission(this@DeliveredDetailActivity)) {
                                    cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                } else {
                                    showSettingsDialog(this@DeliveredDetailActivity)
                                }
                                dialog.dismiss()

                            }

                        })

                } catch (e: Exception) {
                    mLog("Select or Capture Exception")
                }
            }


        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val b = data?.extras?.get("data") as Bitmap
                    billReceipt = compressImageFilePath(b, this@DeliveredDetailActivity)
                    if (checkFileSize(File(billReceipt))!! < 2.toDouble()) {
                        hitUploadDeliveredBill(billReceipt!!)

                        // mBind.ivUploadWeightReceiptInProcessDetail.setImageBitmap(b)
                        mLog("Image path : $billReceipt")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                billReceipt = null
                mLog("Capture Image Exception ${e.message}")
            }


        }


    private val pdfLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri = result.data?.data!!
                    val path =
                        getRealPathFromURI(imageUri, this@DeliveredDetailActivity)
                    /*val bytes = getBytes(imageUri, this)
                    val pdfSizeInKb = bytes!!.size / 1024.toDouble()
                    val pdfSizeInMB = pdfSizeInKb / 1024*/

                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        billReceipt = path
                        val file = File(billReceipt)
                        if (checkFileSize(file)!! < 2.toDouble()) {
                            hitUploadDeliveredBill(billReceipt!!)
                        } else {
                            mToast("File should be less then 2 mb.")
                        }
                        /*Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(mBind.ivUploadWeightReceiptInProcessDetail)*/
                        mLog("pdf path : $billReceipt")
                    } else {
                        mToast(getString(R.string.pdf_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                billReceipt = null
            }

        }


    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val uri = result.data?.data!!
                    mLog("Selected Image data $uri")
                    /*mBind.ivUploadGRNInProcessDetail.apply {
                        visibility = View.VISIBLE
                        setImageURI(uri)
                    }*/
                    billReceipt =
                        getRealPathFromURI(uri, this@DeliveredDetailActivity).toString()
                    val file = File(billReceipt)
                    if (checkFileSize(file)!! < 2.toDouble()) {
                        hitUploadDeliveredBill(billReceipt!!)
                    } else {
                        mToast("File should be less then 2 mb.")
                    }

                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                billReceipt = null
            }

        }


    private fun setDetail(it: TransporterOrderResponse) {
        mBind.apply {
            tvOrderDeliveryCharge.text = String.format(
                "%s %s",
                getString(R.string.indian_rupee_symbol),
                it.estimated_delivery_rate
            )

            mBind.apply {
                mLog("Order detail: ${it.toString()}")

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

                tvWeightOne.text = it.pickup_weight// String.format("%s %s","Picked Up Weight",)
                tvGRN.text = it.grn_number// String.format("%s %s","GRN Bill Number",)
                tvWeight2.text = it.delivery_weight // String.format("%s %s","Delivered Weight",)
                tvEWayBill.text = it.bill_number // String.format("%s %s","eWay Bill Number",)


                tvPONoDeliveredDetail.text = it.po_no
                tvPickupLocation.text = it.destination
                tvDropLocationDeliveredDetail.text = it.address
                tvItemDeliveryDate.text =
                    if (it.delivery_date.isNullOrEmpty()) it.estimated_delivery_date else it.delivery_date
                tvPickupDate.text = it.pickup_date
                tvAssignedDate.text = String.format("%s %s", "Assigned Date :", it.assigned_date)


                tvGoods1.text = it.one_requested_product
                tvPQ1.text = it.one_purchased_quantity
                tvUnit1.text = it.one_unit_of_quantity
                /*    tvRate1.text = it.one_unit_rate
                    tvGst1.text = it.one_product_gst
                    tvTAmount1.text = it.one_total_unit_price_excl_gst
                    tvGstAmount1.text = it.one_total_unit_price_incl_gst*/

                tvGoods2.text = it.two_requested_product
                tvPQ2.text = it.two_purchased_quantity
                tvUnit2.text = it.two_unit_of_quantity
                /* tvRate2.text = it.two_unit_rate
                 tvGst2.text = it.two_product_gst
                 tvTAmount2.text = it.two_total_unit_price_excl_gst
                 tvGstAmount2.text = it.two_total_unit_price_incl_gst*/

                tvGoods3.text = it.three_requested_product
                tvPQ3.text = it.three_purchased_quantity
                tvUnit3.text = it.three_unit_of_quantity
                /*  tvRate3.text = it.three_unit_rate
                  tvGst3.text = it.three_product_gst
                  tvTAmount3.text = it.three_total_unit_price_excl_gst
                  tvGstAmount3.text = it.three_total_unit_price_incl_gst*/

                tvGoods4.text = it.four_requested_product
                tvPQ4.text = it.four_purchased_quantity
                tvUnit4.text = it.four_unit_of_quantity
                /*   tvRate4.text = it.four_unit_rate
                   tvGst4.text = it.four_product_gst
                   tvTAmount4.text = it.four_total_unit_price_excl_gst
                   tvGstAmount4.text = it.four_total_unit_price_incl_gst*/


                /*  Glide.with(this@DeliveredDetailActivity).load("https://www.sw-india.com/wp-content/uploads/2022/04/E-WAY-BILL-LIMIT-EXTENDED.jpg").into(ivViewGRNDeliveredDetail)
                  Glide.with(this@DeliveredDetailActivity).load("https://www.sw-india.com/wp-content/uploads/2022/04/E-WAY-BILL-LIMIT-EXTENDED.jpg").into(ivViewWReceipt2DeliveredDetail)
                  Glide.with(this@DeliveredDetailActivity).load("https://www.sw-india.com/wp-content/uploads/2022/04/E-WAY-BILL-LIMIT-EXTENDED.jpg").into(ivViewEWayBillDeliveredDetail)
                  Glide.with(this@DeliveredDetailActivity).load("https://www.sw-india.com/wp-content/uploads/2022/04/E-WAY-BILL-LIMIT-EXTENDED.jpg").into(ivViewWReceiptDeliveredDetail)
  */

                ivViewUploadedDeliveredBillDetail.apply {
                    if (checkIsImageExtensions(it.delivery_bill_file)) {
                        Glide.with(this@DeliveredDetailActivity).load(it.delivery_bill_file)
                            .into(this)
                    } else {
                        Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(this)
                    }

                    setOnClickListener { click ->
                        if (it.delivery_bill_file.isBlank()) {
                            mToast(getString(R.string.file_not_uploaded))
                            return@setOnClickListener
                        }

                        if (checkIsImageExtensions(it.delivery_bill_file)) {
                            CustomDialogs.showImageDialog(
                                this@DeliveredDetailActivity,
                                it.delivery_bill_file,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        } else {
                            CustomDialogs.showWebViewDialog(
                                this@DeliveredDetailActivity,
                                it.delivery_bill_file,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }

                    }

                }

                ivViewWReceiptDeliveredDetail.apply {
                    if (checkIsImageExtensions(it.weight_receipt)) {
                        Glide.with(this@DeliveredDetailActivity).load(it.weight_receipt).into(this)
                    } else {
                        Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(this)
                    }

                    setOnClickListener { click ->
                        if (it.weight_receipt.isBlank()) {
                            mToast(getString(R.string.file_not_uploaded))
                            return@setOnClickListener
                        }

                        if (checkIsImageExtensions(it.weight_receipt)) {
                            CustomDialogs.showImageDialog(
                                this@DeliveredDetailActivity,
                                it.weight_receipt,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        } else {
                            CustomDialogs.showWebViewDialog(
                                this@DeliveredDetailActivity,
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
                ivViewEWayBillDeliveredDetail.apply {
                    if (checkIsImageExtensions(it.eway_bill)) {
                        Glide.with(this@DeliveredDetailActivity).load(it.eway_bill).into(this)
                    } else {
                        Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(this)
                    }

                    setOnClickListener { click ->

                        if (it.eway_bill.isBlank()) {
                            mToast(getString(R.string.file_not_uploaded))
                            return@setOnClickListener
                        }

                        if (checkIsImageExtensions(it.eway_bill)) {
                            CustomDialogs.showImageDialog(
                                this@DeliveredDetailActivity,
                                it.eway_bill,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        } else {
                            CustomDialogs.showWebViewDialog(
                                this@DeliveredDetailActivity,
                                it.eway_bill,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }


                        //  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.eway_bill)))


                    }
                }

                ivViewWReceipt2DeliveredDetail.apply {
                    if (checkIsImageExtensions(it.delivered_weight_receipt)) {
                        Glide.with(this@DeliveredDetailActivity).load(it.delivered_weight_receipt)
                            .into(this)
                    } else {
                        Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(this)
                    }

                    /*if (it.delivered_weight_receipt.contains(".png") || it.delivered_weight_receipt.contains(".jpg") || it.delivered_weight_receipt.contains(".jpeg") ){
                        Glide.with(this@DeliveredDetailActivity).load(it.delivered_weight_receipt)
                            .into(this)
                    }else{
                        Glide.with(this@DeliveredDetailActivity).load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png").into(this)
                    }*/
                    setOnClickListener { click ->
                        if (it.delivered_weight_receipt.isBlank()) {
                            mToast(getString(R.string.file_not_uploaded))
                            return@setOnClickListener
                        }


                        if (checkIsImageExtensions(it.delivered_weight_receipt)) {
                            CustomDialogs.showImageDialog(
                                this@DeliveredDetailActivity,
                                it.delivered_weight_receipt,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        } else {
                            CustomDialogs.showWebViewDialog(
                                this@DeliveredDetailActivity,
                                it.delivered_weight_receipt,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }

                        /*  startActivity(
                              Intent(
                                  Intent.ACTION_VIEW,
                                  Uri.parse(it.delivered_weight_receipt)
                              )
                          )*/

                    }
                }
                ivViewGRNDeliveredDetail.apply {
                    if (checkIsImageExtensions(it.grn_file)) {
                        Glide.with(this@DeliveredDetailActivity).load(it.grn_file).into(this)
                    } else {
                        Glide.with(this@DeliveredDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(this)
                    }

                    /* if (it.grn_file.contains(".png") || it.grn_file.contains(".jpg") || it.grn_file.contains(".jpeg") ){
                     }else{
                     }*/
                    setOnClickListener { click ->
                        if (it.grn_file.isBlank()) {
                            mToast(getString(R.string.file_not_uploaded))
                            return@setOnClickListener
                        }

                        if (checkIsImageExtensions(it.grn_file)) {
                            CustomDialogs.showImageDialog(
                                this@DeliveredDetailActivity,
                                it.grn_file,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        } else {
                            CustomDialogs.showWebViewDialog(
                                this@DeliveredDetailActivity,
                                it.grn_file,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }
                    }
                }


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

    }


    private fun hitUploadDeliveredBill(billReceipt: String) {
        if (!billReceipt.isNullOrEmpty()) {
            if (isConnectionAvailable()) {
                mViewModel.hitUploadDeliveredBillApi(
                    mPref.getUserDetail().id,
                    transporterOrderResponse.id,
                    billReceipt
                )
                mViewModel.getUploadDeliveredBillResponse()
                    .observe(this@DeliveredDetailActivity, uploadDeliveredBill)
            } else {
                mToast(getString(R.string.no_internet_available))
            }
        }

    }

    private val uploadDeliveredBill: Observer<ApiResponse<SuccessMsgResponse>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    mToast(it.data?.message.toString())
                    onBackPressedDispatcher.onBackPressed()
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }

}