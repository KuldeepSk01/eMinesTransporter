package com.emines_transportation.screen.dashboard

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.databinding.ActivityMainBinding
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.delivered.DeliveredFragment
import com.emines_transportation.screen.dashboard.home.HomeFragment
import com.emines_transportation.screen.dashboard.inprocess.InProcessFragment
import com.emines_transportation.screen.dashboard.pickup.PickupFragment
import com.emines_transportation.screen.dashboard.profile.ProfileFragment
import com.emines_transportation.util.mLog
import com.emines_transportation.util.mToast
import com.emines_transportation.util.requestStoragePermission
import com.emines_transportation.util.requestStoragePermissionAbove32
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : BaseActivity(), ActivityMainListener {
    private var isBackPressAgain = false
    private lateinit var mainListener: ActivityMainListener

    companion object {
        lateinit var mainActivityBinding: ActivityMainBinding
        const val HOME_FRAGMENT = 1
        const val PICKUP_FRAGMENT = 2
        const val INPROCESS_FRAGMENT = 3
        const val DELEVERED_FRAGMENT = 4
        const val PROFILE_FRAGMENT = 5

    }

    override val layoutId: Int
        get() = R.layout.activity_main

    private var onBackPressAgain: Boolean = false

    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }
    private val pickupFragment: PickupFragment by lazy {
        PickupFragment()
    }

    private val inProcessFragment: InProcessFragment by lazy {
        InProcessFragment()
    }

    private val deliveredFragment: DeliveredFragment by lazy {
        DeliveredFragment()
    }

    private val profileFragment: ProfileFragment by lazy {
        ProfileFragment()
    }

    override fun onCreateInit(binding: ViewDataBinding?) {
        mainActivityBinding = binding as ActivityMainBinding
        mainListener = this@MainActivity
        mainListener.onReplaceFragment(homeFragment)
        onSelectBtn(HOME_FRAGMENT)

        checkForUpdate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestStoragePermissionAbove32(this@MainActivity)
        } else {
            requestStoragePermission(this@MainActivity)
        }


        /*  mViewModel.hitProfileApi(mPref.getUserDetail()?.id!!)
          mViewModel.profileResponse.observe(this@MainActivity, profileDataObserver)
  */
        mainActivityBinding.apply {
            customBottomBarLayout.clHomeNav.setOnClickListener {
                mainListener.onReplaceFragment(homeFragment)
                onSelectBtn(HOME_FRAGMENT)

            }
            customBottomBarLayout.clCorporateNav.setOnClickListener {
                mainListener.onReplaceFragment(pickupFragment)
                onSelectBtn(PICKUP_FRAGMENT)


            }
            customBottomBarLayout.clOrderNav.setOnClickListener {
                mainListener.onReplaceFragment(deliveredFragment)
                onSelectBtn(DELEVERED_FRAGMENT)


            }
            customBottomBarLayout.clSellNav.setOnClickListener {
                mainListener.onReplaceFragment(inProcessFragment)
                onSelectBtn(INPROCESS_FRAGMENT)

            }
            customBottomBarLayout.clAccountNav.setOnClickListener {
                mainListener.onReplaceFragment(profileFragment)
                onSelectBtn(PROFILE_FRAGMENT)

            }
        }
    }


    private fun onSelectBtn(bottomTabItem: Int) {
        defaultIconsAndTextColor()
        mainActivityBinding.customBottomBarLayout.apply {
            when (bottomTabItem) {
                1 -> {
                    tvHomeNav.setTextColor(resources.getColor(R.color.primary_color, null))
                    ivHomeIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_selected_home, null)
                }

                2 -> {
                    tvCorporateNav.setTextColor(resources.getColor(R.color.primary_color, null))
                    ivCorporateIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_unselect_client, null)
                }

                3 -> {
                    tvSellNav.setTextColor(resources.getColor(R.color.primary_color, null))
                    ivSellIcon.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselected_pickup,
                            null
                        )
                }

                4 -> {
                    tvOrderNav.setTextColor(resources.getColor(R.color.primary_color, null))
                    ivOrderIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_order, null)
                }

                5 -> {
                    tvAccountNav.setTextColor(resources.getColor(R.color.primary_color, null))
                    ivAccountIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_account, null)
                }
            }

        }
    }

    private fun defaultIconsAndTextColor() {
        mainActivityBinding.customBottomBarLayout.apply {
            tvHomeNav.setTextColor(resources.getColor(R.color.default_text_color, null))
            tvCorporateNav.setTextColor(resources.getColor(R.color.default_text_color, null))
            tvSellNav.setTextColor(resources.getColor(R.color.default_text_color, null))
            tvOrderNav.setTextColor(resources.getColor(R.color.default_text_color, null))
            tvAccountNav.setTextColor(resources.getColor(R.color.default_text_color, null))
            ivHomeIcon.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_selected_home, null)
            ivCorporateIcon.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_unselect_client, null)
            ivSellIcon.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_unselected_pickup, null)
            ivOrderIcon.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_order, null)
            ivAccountIcon.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_account, null)

        }


    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            if (isBackPressAgain) {
                super.onBackPressed()
                onBackPressedDispatcher.onBackPressed()
            } else {
                isBackPressAgain = true
                Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT)
                    .show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isBackPressAgain = false
                }, 3000)
            }
        }
    }

    override fun onReplaceFragment(fragment: Fragment) {
        replaceFragment(
            R.id.flMainContainer,
            fragment
        )
    }


    private val profileDataObserver: Observer<ApiResponse<BaseResponse<TransporterResponse>>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()

                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    val ud = it.data?.data
                    mLog(ud.toString())
                    mPref.setUserDetail(ud)
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message!!)
                }
            }

        }
    }


    private fun checkForUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // an activity result launcher registered via registerForActivityResult
                    activityResultLauncher,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != AppCompatActivity.RESULT_OK) {
                Log.d(
                    "App Update",
                    "Update flow failed! Result code: " + result.resultCode
                );
                // If the update is canceled or fails,
                // you can request to start the update again.
            } else {
                Log.d(
                    "App Update",
                    "App Update is available "
                );
            }

        }


}