package com.example.stickers.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stickers.fragment.DownloadedStickersFragment
import com.example.stickers.fragment.OnlineStickersFragment

class StickersAdapter (fragmentActivity: FragmentActivity, private var totalCount: Int): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount

    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnlineStickersFragment()
            1 -> DownloadedStickersFragment()
            else -> OnlineStickersFragment()
        }

    }
}