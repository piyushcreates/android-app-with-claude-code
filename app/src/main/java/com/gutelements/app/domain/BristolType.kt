package com.gutelements.app.domain

import androidx.annotation.StringRes
import com.gutelements.app.R

enum class BristolType(val number: Int, @param:StringRes val nameRes: Int, @param:StringRes val descriptionRes: Int) {
    TYPE_1(1, R.string.bristol_1_name, R.string.bristol_1_desc),
    TYPE_2(2, R.string.bristol_2_name, R.string.bristol_2_desc),
    TYPE_3(3, R.string.bristol_3_name, R.string.bristol_3_desc),
    TYPE_4(4, R.string.bristol_4_name, R.string.bristol_4_desc),
    TYPE_5(5, R.string.bristol_5_name, R.string.bristol_5_desc),
    TYPE_6(6, R.string.bristol_6_name, R.string.bristol_6_desc),
    TYPE_7(7, R.string.bristol_7_name, R.string.bristol_7_desc);

    companion object {
        fun of(number: Int): BristolType = entries.first { it.number == number }
    }
}
