package com.ufistudio.ianlin.foodsafe.pages.main.testing

import android.os.Bundle
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener

class TestingFragment : InteractionView<OnPageInteractionListener.Pane>() {
    private lateinit var mViewModel: TestingViewModel
    private lateinit var mRecyclerViewAdapter: TestingListAdapter

    companion object {
        fun newInstance(): TestingFragment = newInstance()
        private val TAG = TestingFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}