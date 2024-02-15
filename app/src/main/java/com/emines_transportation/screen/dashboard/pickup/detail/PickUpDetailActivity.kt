package com.emines_transportation.screen.dashboard.pickup.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.emines_transportation.CustomDialogs
import com.emines_transportation.CustomDialogs.showBottomSheetDialog
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.databinding.ActivityPickUpDetailBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.checkFileSize
import com.emines_transportation.util.compressImageFilePath
import com.emines_transportation.util.getCurrentDate
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

class PickUpDetailActivity : BaseActivity() {
    private lateinit var mBind: ActivityPickUpDetailBinding
    private var weightReceiptImage: String? = null
    private var eWayBillReceiptImage: String? = null

    private val mViewModel: PickupViewModel by inject()
    private val userDetail = mPref.getUserDetail()

    private lateinit var transporterOrderResponse: TransporterOrderResponse


    override val layoutId: Int
        get() = R.layout.activity_pick_up_detail

    override fun onCreateInit(binding: ViewDataBinding?) {
        mBind = binding as ActivityPickUpDetailBinding
        transporterOrderResponse = intent.getBundleExtra(Constants.DefaultConstant.BUNDLE_KEY)
            ?.serializable<TransporterOrderResponse>(Constants.DefaultConstant.MODEL_DETAIL)!!
        setDetail(transporterOrderResponse)


        mBind.apply {
            toolBarPickupDetail.apply {
                tvToolBarTitle.text = String.format(
                    "%s %s",
                    getString(R.string.order_id_text),
                    transporterOrderResponse.id.toString()
                )
                ivToolBarBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

            when (transporterOrderResponse.status) {
                getString(R.string.denied) -> {
                    rlBottomBtn.visibility = View.GONE
                    rlAcceptedPickupDetail.visibility = View.GONE
                }

                getString(R.string.accepted) -> {
                    rlBottomBtn.visibility = View.GONE
                    rlAcceptedPickupDetail.visibility = View.VISIBLE
                }

                else -> {
                    rlBottomBtn.visibility = View.VISIBLE
                }
            }

            bottomBtn.apply {
                tvFirstBtn.apply {
                    text = getString(R.string.rejected)
                    setOnClickListener {
                        // mBind.rlAcceptedPickupDetail.visibility = View.GONE
                        hitChangeOrderStatus(getString(R.string.denied))
                    }
                }
                tvSecondBtn.apply {
                    text = getString(R.string.accepted)
                    setOnClickListener {
                        rlAcceptedPickupDetail.visibility = View.VISIBLE
                        rlBottomBtn.visibility = View.GONE
                        tvPickupDate.apply {
                            text = String.format("%s %s", "Pick up Date :", getCurrentDate())
                            visibility = View.VISIBLE
                        }
                        hitChangeOrderStatus(getString(R.string.accepted))
                    }
                }
            }

            ivUploadWeightReceiptPickupDetail.setOnClickListener {
                try {
                    showBottomSheetDialog(this@PickUpDetailActivity,
                        object : CustomDialogs.OnBottomSheetClickListener {
                            override fun onImageClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val gallery = Intent(MediaStore.ACTION_PICK_IMAGES)
                                    mLog("PERMISSION_GRANTED 13")
                                    startActivityForWeightReceipt.launch(gallery)
                                } else {
                                    if (verifyCameraPermission(this@PickUpDetailActivity)) {
                                        val gallery = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                                        )
                                        mLog("setInitialSetup: PERMISSION_GRANTED below 13")
                                        startActivityForWeightReceipt.launch(gallery)
                                    } else {
                                        showSettingsDialog(this@PickUpDetailActivity)
                                        //mToast("Please enable required permission")
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
                                    if (verifyCameraPermission(this@PickUpDetailActivity)) {
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
                                        showSettingsDialog(this@PickUpDetailActivity)

                                        // mToast("Please enable required permission")
                                    }

                                }

                                dialog.dismiss()
                            }

                            override fun onCameraClick(dialog: BottomSheetDialog) {
                                if (verifyCameraPermission(this@PickUpDetailActivity)) {
                                    startActivityCamera1.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                } else {
                                    showSettingsDialog(this@PickUpDetailActivity)
                                    // mToast("Please enable required permission")
                                }

                                dialog.dismiss()
                            }

                        })
                } catch (e: Exception) {
                    mLog("Select or Capture Exception")
                }

            }
            ivUploadEWayBillPickupDetail.setOnClickListener {
                try {
                    showBottomSheetDialog(this@PickUpDetailActivity,
                        object : CustomDialogs.OnBottomSheetClickListener {
                            override fun onImageClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val gallery = Intent(MediaStore.ACTION_PICK_IMAGES)
                                    startActivityForEWayBill.launch(gallery)
                                } else {
                                    if (verifyStoragePermission(this@PickUpDetailActivity)) {
                                        val gallery = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                                        )
                                        startActivityForEWayBill.launch(gallery)
                                    } else {
                                        showSettingsDialog(this@PickUpDetailActivity)
                                        // mToast(getString(R.string.please_enable_required_permission))
                                    }

                                }
                                dialog.dismiss()
                            }

                            override fun onFileClick(dialog: BottomSheetDialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                    pdfIntent.type = "application/pdf"
                                    pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                    startActivityForEWayBillPDF.launch(
                                        Intent.createChooser(
                                            pdfIntent,
                                            "Select PDF file"
                                        )
                                    )

                                } else {
                                    if (verifyStoragePermission(this@PickUpDetailActivity)) {
                                        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
                                        pdfIntent.type = "application/pdf"
                                        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                        startActivityForEWayBillPDF.launch(
                                            Intent.createChooser(
                                                pdfIntent,
                                                "Select PDF file"
                                            )
                                        )
                                    } else {
                                        showSettingsDialog(this@PickUpDetailActivity)
                                        // mToast(getString(R.string.please_enable_required_permission))
                                    }

                                }

                                dialog.dismiss()
                            }

                            override fun onCameraClick(dialog: BottomSheetDialog) {
                                if (verifyCameraPermission(this@PickUpDetailActivity)) {
                                    startActivityCamera2.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                } else {
                                    showSettingsDialog(this@PickUpDetailActivity)
                                    // mToast(getString(R.string.please_enable_required_permission))
                                }

                                dialog.dismiss()
                            }
                        })
                } catch (e: Exception) {
                    mLog("Select or Capture Exception")
                }

            }

            tvPickedUpBtnPickupDetail.setOnClickListener {
                if (weightReceiptImage == null) {
                    mToast(getString(R.string.please_upload_weight_receipt))
                    return@setOnClickListener
                }
                if (eWayBillReceiptImage == null) {
                    mToast(getString(R.string.please_upload_eway_bill_receipt))
                    return@setOnClickListener
                }

                mViewModel.hitPickedUpOrderByTransporterApi(
                    userDetail.id,
                    transporterOrderResponse.id,
                    etWeightReceiptNoPickupDetail.text.toString(),
                    etEWayBillNoPickupDetail.text.toString(),
                    weightReceiptImage,
                    eWayBillReceiptImage
                )
                mViewModel.getPickedUpOrderByTransporterResponse()
                    .observe(this@PickUpDetailActivity, pickedUpOrderObserver)
            }

        }
    }


    private val pickedUpOrderObserver: Observer<ApiResponse<SuccessMsgResponse>> by lazy {
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

    private fun hitChangeOrderStatus(status: String) {
        mViewModel.hitChangeOrderStatusByTransporterApi(
            mPref.getUserDetail().id,
            transporterOrderResponse.id,
            status
        )
        mViewModel.getChangeOrderStatusByTransporterResponse()
            .observe(this@PickUpDetailActivity, changerOrderStatusObserver)
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
                    mToast(it.data?.message.toString())
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }


    private fun setDetail(it: TransporterOrderResponse) {
        mBind.apply {
            tvPONoPickupDetail.text = it.po_no

            tvPickupLocation.text = it.destination //this is picked up location
            tvDropLocationPickupDetail.text = it.address // this is drop location
            tvItemPickupAssignedDate.text =
                String.format("%s %s", "Assigned Date :", it.assigned_date)

            tvStatus.apply {
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


            /*
                        tvStatus.apply {
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
            /*  tvRate1.text = it.one_unit_rate
              tvGst1.text = it.one_product_gst
              tvTAmount1.text = it.one_total_unit_price_excl_gst
              tvGstAmount1.text = it.one_total_unit_price_incl_gst
  */
            tvGoods2.text = it.two_requested_product
            tvPQ2.text = it.two_purchased_quantity
            tvUnit2.text = it.two_unit_of_quantity
            /* tvRate2.text = it.two_unit_rate
             tvGst2.text = it.two_product_gst
             tvTAmount2.text = it.two_total_unit_price_excl_gst
             tvGstAmount2.text = it.two_total_unit_price_incl_gst
 */
            tvGoods3.text = it.three_requested_product
            tvPQ3.text = it.three_purchased_quantity
            tvUnit3.text = it.three_unit_of_quantity
            /*  tvRate3.text = it.three_unit_rate
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
             tvGstAmount4.text = it.four_total_unit_price_incl_gst*/
            tvDropDatePickupDetail.text = it.po_date
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


    private val startActivityForWeightReceipt =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val uri = result.data?.data!!
                    val path = compressImageFilePath(
                        BitmapFactory.decodeFile(
                            getRealPathFromURI(
                                uri,
                                this@PickUpDetailActivity
                            )
                        ), this@PickUpDetailActivity
                    ).toString()
                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        weightReceiptImage = path
                        mBind.ivUploadWeightReceiptPickupDetail.apply {
                            visibility = View.VISIBLE
                            setImageURI(uri)
                        }
                        mLog("Image path : $eWayBillReceiptImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }

                    /*
                    mBind.ivUploadWeightReceiptPickupDetail.apply {
                        visibility = View.VISIBLE
                        setImageURI(uri)
                    }
                    weightReceiptImage =
                        getRealPathFromURI(uri, this@PickUpDetailActivity).toString()*/
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                weightReceiptImage = null
            }

        }


    private val startActivityForPDF =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    var file = getRealPathFromURI(result.data?.data!!, this@PickUpDetailActivity)
                    /*  val imageUri: Uri = result.data?.data!!
                      val bytes = getBytes(imageUri, this)
                      val pdfSizeInKb = bytes!!.size / 1024.toDouble()
                      val pdfSizeInMB = pdfSizeInKb / 1024*/

                    if (checkFileSize(File(file))!! < 2.toDouble()) {
                        weightReceiptImage = file
                        Glide.with(this@PickUpDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(mBind.ivUploadWeightReceiptPickupDetail)
                    } else {
                        mToast(getString(R.string.pdf_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                weightReceiptImage = null
            }

        }

    private val startActivityForEWayBillPDF =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri = result.data?.data!!
                    var file = getRealPathFromURI(imageUri, this@PickUpDetailActivity)
                    /*   val imageDataList: MutableList<ByteArray> = ArrayList()

                       val bytes = getBytes(imageUri, this)
                       imageDataList.add(bytes!!)*/
                    /*  val pdfSizeInKb = bytes!!.size / 1024.toDouble()
                      val pdfSizeInMB = pdfSizeInKb / 1024*/

                    if (checkFileSize(File(file))!! < 2.toDouble()) {
                        eWayBillReceiptImage = file
                        Glide.with(this@PickUpDetailActivity)
                            .load("https://blog.idrsolutions.com/app/uploads/2020/10/pdf-1.png")
                            .into(mBind.ivUploadEWayBillPickupDetail)
                    } else {
                        mToast(getString(R.string.pdf_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                eWayBillReceiptImage = null
            }

        }

    private val startActivityForEWayBill =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data!!
                    val path = compressImageFilePath(
                        BitmapFactory.decodeFile(
                            getRealPathFromURI(
                                uri,
                                this@PickUpDetailActivity
                            )
                        ), this@PickUpDetailActivity
                    ).toString()
                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        eWayBillReceiptImage = path
                        mBind.ivUploadEWayBillPickupDetail.apply {
                            visibility = View.VISIBLE
                            setImageURI(uri)
                        }
                        mLog("Image path : $eWayBillReceiptImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }

                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                eWayBillReceiptImage = null
            }

        }


    private val startActivityCamera1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val b = data?.extras?.get("data") as Bitmap
                    val path = compressImageFilePath(b, this@PickUpDetailActivity)

                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        weightReceiptImage = path
                        mBind.ivUploadWeightReceiptPickupDetail.setImageBitmap(b)
                        mLog("Image path : $weightReceiptImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                weightReceiptImage = null
            }

        }


    private val startActivityCamera2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val b = data?.extras?.get("data") as Bitmap
                    val path = compressImageFilePath(b, this@PickUpDetailActivity)

                    if (checkFileSize(File(path))!! < 2.toDouble()) {
                        eWayBillReceiptImage = path
                        mBind.ivUploadEWayBillPickupDetail.setImageBitmap(b)
                        mLog("Image path : $weightReceiptImage")
                    } else {
                        mToast(getString(R.string.image_should_be_less_then_or_equal_2mb))
                    }
                }
            } catch (e: Exception) {
                mLog("Nothing Selected Image")
                eWayBillReceiptImage = null
            }

        }


}