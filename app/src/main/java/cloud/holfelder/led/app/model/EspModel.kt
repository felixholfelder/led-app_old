package cloud.holfelder.led.app.model

data class EspModel(
    var mode: Int?,
    var red: Int?,
    var green: Int?,
    var blue: Int?
) {
    fun toJson() =
        """{
            "mode": $mode,
            "red": $red,
            "green": $green,
            "blue": $blue
            }
        """.trimMargin()
}
