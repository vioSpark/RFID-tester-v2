package com.example.rfid_tester.ui.mifarecalssic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_tester.Card
import com.example.rfid_tester.R
import com.example.rfid_tester.databinding.FragmentMifareBinding


class MifareClassicFragment : Fragment() {
    var card = Card(byteArrayOf(0,1))
    private lateinit var mifareClassicViewModel: MifareClassicViewModel
    private var _binding: FragmentMifareBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mifareClassicViewModel =
            ViewModelProvider(this).get(MifareClassicViewModel::class.java)

        _binding = FragmentMifareBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMifare
        mifareClassicViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //TODO: this is null, does that means not found?
        //TODO: call updateInfo from mainActivity..
        val idTextView = container?.findViewById<TextView>(R.id.id_here)
        fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
        idTextView?.text = card.id.toHexString()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    public fun updateInfo(newCardInfo: Card) {
        card = newCardInfo
    }

}