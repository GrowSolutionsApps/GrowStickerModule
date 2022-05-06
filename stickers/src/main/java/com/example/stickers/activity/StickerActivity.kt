package com.example.stickers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stickers.R
import com.example.stickers.adapter.StickersAdapter
import com.example.stickers.databinding.ActivityStickerBinding
import com.google.android.material.tabs.TabLayoutMediator

class StickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStickerBinding
    private var tabTitleList =  ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStickerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupViewPager()
        setupTabLayout()
        setupUI()
    }

    private fun setupUI() {
        binding.mrlBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = StickersAdapter(this@StickerActivity, binding.tabLayout.tabCount)
            binding.viewPager.adapter = adapter

        }
    }

    private fun setupTabLayout() {

        tabTitleList.add(getString(R.string.tab_stickers))
        tabTitleList.add(getString(R.string.tab_downloaded))

        TabLayoutMediator(binding.tabLayout,  binding.viewPager) { tab, position ->
            tab.text = tabTitleList[position]
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    override fun onBackPressed() {
        with(binding) {
            if (binding.viewPager.currentItem != 0) {
                viewPager.currentItem = viewPager.currentItem - 1;
            } else {
            super.onBackPressed()
            }
        }
    }
}