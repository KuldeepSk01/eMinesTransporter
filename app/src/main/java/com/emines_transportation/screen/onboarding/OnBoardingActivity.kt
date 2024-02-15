package com.emines_transportation.screen.onboarding

import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.emines_transportation.R
import com.emines_transportation.adapter.OnBoardingAdapter
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.ActivityOnBoardingBinding
import com.emines_transportation.screen.login.LoginActivity
import org.koin.core.component.inject

class OnBoardingActivity : BaseActivity() {
    private lateinit var oBinding: ActivityOnBoardingBinding
    private val mViewModel: OnBoardViewModel by inject()
    private var viewPagerPosition = -1
    override val layoutId = R.layout.activity_on_boarding

    override fun onCreateInit(binding: ViewDataBinding?) {
        oBinding = binding as ActivityOnBoardingBinding
        mViewModel.onBoardingActivity = this@OnBoardingActivity
        oBinding.apply {
            vPageA2OnBoard.adapter =
                OnBoardingAdapter(mViewModel.getOnBoardList(), this@OnBoardingActivity)
            setOnIndicator(0)
            vPageA2OnBoard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    viewPagerPosition = position
                    setOnIndicator(position)
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setOnIndicator(position)

                }

            })


            tvSkip.setOnClickListener {
                launchActivity(LoginActivity::class.java)
                finish()
            }
            ivNextBtnOnBoarding.setOnClickListener {
                if (vPageA2OnBoard.currentItem == 2) {
                    launchActivity(LoginActivity::class.java)
                    finish()
                } else {
                    vPageA2OnBoard.currentItem++
                }
            }
        }


    }

    private fun setOnIndicator(position: Int) {
        /*   oBinding.ivIndicatorOne.setBackgroundResource(R.drawable.ic_selected_indicator)
           oBinding.ivIndicatorTwo.setBackgroundResource(R.drawable.ic_unselect_indicator)
           oBinding.ivIndicatorThree.setBackgroundResource(R.drawable.ic_unselect_indicator)*/

        oBinding.apply {
            when (position) {
                0 -> {
                    ivIndicatorOne.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_selected_indicator,
                            null
                        )
                    )
                    ivIndicatorTwo.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )
                    ivIndicatorThree.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )

                }

                1 -> {
                    ivIndicatorOne.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )
                    ivIndicatorTwo.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_selected_indicator,
                            null
                        )
                    )
                    ivIndicatorThree.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )

//                   ivIndicatorOne.setBackgroundResource(R.drawable.ic_unselect_indicator)
//                   ivIndicatorTwo.setBackgroundResource(R.drawable.ic_selected_indicator)
//                   ivIndicatorThree.setBackgroundResource(R.drawable.ic_unselect_indicator)
                }

                2 -> {
                    ivIndicatorOne.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )
                    ivIndicatorTwo.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_unselect_indicator,
                            null
                        )
                    )
                    ivIndicatorThree.setBackgroundDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_selected_indicator,
                            null
                        )
                    )

//
//                   ivIndicatorOne.setBackgroundResource(R.drawable.ic_unselect_indicator)
//                   ivIndicatorTwo.setBackgroundResource(R.drawable.ic_unselect_indicator)
//                   ivIndicatorThree.setBackgroundResource(R.drawable.ic_selected_indicator)
                }
            }

        }

    }

}