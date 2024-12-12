package com.example.a123.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.example.a123.repository.TipRepository
import com.example.a123.service.TipServiceImpl
import com.example.a123.databinding.FragmentLightBinding


class LightFragment : Fragment() {

    private lateinit var adviceTextView: TextView
    //adviceTextView.text = "Совет дня"
    private lateinit var tipService: TipServiceImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLightBinding.inflate(inflater, container, false)

        tipService = TipServiceImpl(TipRepository())

        // Получение случайного совета
        val tip = tipService.getTip()

        // Отображаем совет в TextView
        binding.lightTextView.text = tip.message

        return binding.root
    }
}