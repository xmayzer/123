package com.example.a123.service

import com.example.a123.model.Tip
import com.example.a123.repository.TipRepository

interface TipService {
    fun getTip(): Tip
}

class TipServiceImpl(private val tipRepository: TipRepository) : TipService {
    override fun getTip(): Tip {
        return tipRepository.getRandomTip()
    }
}