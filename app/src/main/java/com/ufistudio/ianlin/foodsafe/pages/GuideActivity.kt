package com.ufistudio.ianlin.foodsafe.pages

import android.content.Intent
import android.os.Bundle
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneViewActivity

class GuideActivity : PaneViewActivity(), OnPageInteractionListener.Guide {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        val page = Page.GUIDE
        val args: Bundle = Bundle()

        switchPage(R.id.fragment_container, page, args, true, false)
    }

    override fun goToMain(page: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("page", page)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
