package com.zelianko.kitchencalculator.yandex_ads

enum class BannerId {
    ONE_BANNER {
        override val bannerId = "R-M-13532950-2"
    },
    TWO_BANNER {
        override val bannerId = "R-M-13532950-3"
    },
    THREE_BANNER {
        override val bannerId = "R-M-13532950-4"
    },
    FOUR_BANNER {
        override val bannerId = "R-M-13532950-5"
    },
    FIVE_BANNER {
        override val bannerId = "R-M-13532950-6"
    },
    SIX_BANNER {
        override val bannerId = "R-M-13532950-7"
    },
    SEVEN_BANNER {
        override val bannerId = "R-M-13532950-8"
    };

    abstract val bannerId: String
}