package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R

class MessageFragment : Fragment(R.layout.fragment_message) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // This fragment is deprecated in this build.
        // Go to matches (or home) so the app still works.
        findNavController().navigate(R.id.matchesFragment)
    }
}
