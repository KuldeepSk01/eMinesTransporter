package com.emines_transportation.screen.onboarding

import com.emines_transportation.R
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.model.CommonModel

class OnBoardViewModel : BaseViewModel() {
    var onBoardingActivity: OnBoardingActivity? = null
    fun getOnBoardList(): MutableList<CommonModel> {
        val list = mutableListOf<CommonModel>()
        list.add(
            CommonModel(
                "https://assets-global.website-files.com/60a4a4fb42c31d8c7ae0d7bd/61b0bb04360dff3c34314704_What%20Is%20Freight%20Shipping.jpg", onBoardingActivity!!.getString(R.string.loading_title_one)
            )
        )
        list.add(
            CommonModel(
                "https://vajiram-prod.s3.ap-south-1.amazonaws.com/What_is_E_Way_Bill_5bb2c38c4e.png",
                onBoardingActivity!!.getString(R.string.loading_title_two)
            )
        )
        list.add(
            CommonModel(
                "https://img.myloview.com/posters/grn-goods-receipt-note-acronym-business-concept-word-lettering-typography-design-illustration-with-line-icons-and-ornaments-vector-infographic-illustration-for-presentations-sites-reports-700-312547732.jpg",
                onBoardingActivity!!.getString(R.string.loading_title_three)
            )
        )
        return list
    }

}